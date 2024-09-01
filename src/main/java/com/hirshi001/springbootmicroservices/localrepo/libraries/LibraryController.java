package com.hirshi001.springbootmicroservices.localrepo.libraries;

import com.hirshi001.springbootmicroservices.chromedriver.DriverNotFoundException;
import com.hirshi001.springbootmicroservices.chromedriver.DriverUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.hirshi001.springbootmicroservices.chromedriver.ScreenShotter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
public class LibraryController {

    private final Map<String, byte[]> libraryScreenshots = new ConcurrentHashMap<>();


    @Value("${github.webhooks.secret}")
    private String githubWebhookSecret;

    @Value("${app.base-folder}")
    private String baseFolder;

    @Value("${app.library-folder}")
    private String libraryFolder;


    private final LibraryRepository libraryRepository = new LibraryRepositoryImpl();

    @PostMapping("/githubrepoupdate")
    public void githubRepoUpdated(
            @RequestBody JsonNode payloadBody,
            @RequestHeader("X-Hub-Signature-256") String signatureHeader,
            HttpServletRequest request) throws Throwable {

        if (!DriverUtil.driverExists())
            return;


        try {
            // TODO: potentially blacklist IPs that send multiple invalid signatures
            log.info("Received github repo update webhook");
            if (!GithubSignatureAuthenticator.verifySignature(payloadBody.toString(), githubWebhookSecret, signatureHeader)) {
                log.warn("Failed to authenticate SignatureHeader for Github Repo Update from {}:{}", request.getRemoteHost(), request.getRemotePort());
                return;
            }

            JsonNode value = payloadBody.get("repository");
            if (value == null) return;

            value = value.get("id");
            if (value == null) return;

            Library library = libraryRepository.getById(value.asInt());
            if (library == null) return;

            updateLibraryScreenshot(library);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/libraries.json")
    public List<Library> libraries(@RequestParam(value = "search", required = false) String searchString) {
        if (searchString == null)
            return libraryRepository.getAll(null);
        else
            return libraryRepository.searchFor(searchString, null);
    }


    @GetMapping(
            value = "/libScreenshot",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] getLibraryScreenshot(@RequestParam(value = "name") String name) {
        byte[] screenShot = libraryScreenshots.get(name);
        if (screenShot != null)
            return screenShot;

        Library library = libraryRepository.getByName(name);
        if(library == null)
            return null;

        return getDefaultImage(library);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() throws Exception {
        loadLibraries();
        if (DriverUtil.init()) {
            try {
                updateLibraryScreenshots();
            } catch (Exception e) {
                log.error("Failed to update library screenshots", e);
            }
        }
    }

    public void loadLibraries() throws FileNotFoundException {
        libraryRepository.setRepoPath(Path.of(baseFolder, libraryFolder, "libraries.json"));
        libraryRepository.defaultReload(true);
        libraryRepository.load();
    }

    private byte[] getDefaultImage(Library library) {
        Path imgPath = Path.of(baseFolder, libraryFolder, library.getDefaultImage());
        try {
            return Files.readAllBytes(imgPath);
        } catch (IOException ignored) {
        }

        return null;
    }

    synchronized void updateLibraryScreenshot(Library library) {
        byte[] screenshot = null;

        try {
            screenshot = ScreenShotter.getScreenshot(library.getUrl(), 5);
        } catch (Exception e) {
            screenshot = getDefaultImage(library);
        }

        if (screenshot != null)
            libraryScreenshots.put(library.getName(), screenshot);
    }

    public void updateLibraryScreenshots() {
        log.info("Updating library screenshots");

        for (Library library : libraryRepository) {
            updateLibraryScreenshot(library);
        }
    }

}
