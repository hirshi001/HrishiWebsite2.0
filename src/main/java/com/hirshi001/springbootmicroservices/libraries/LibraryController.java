package com.hirshi001.springbootmicroservices.libraries;

import com.hirshi001.springbootmicroservices.chromedriver.DriverUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private LibraryRepository libraryRepository;

    @PostMapping("/githubrepoupdate")
    public void githubRepoUpdated(@RequestBody JsonNode payloadBody, @RequestHeader("X-Hub-Signature-256") String signatureHeader) {
        try {
            // TODO: potentially blacklist IPs that send multiple invalid signatures
            log.info("Received github repo update webhook");
            if (!GithubSignatureAuthenticator.verifySignature(payloadBody.toString(), githubWebhookSecret, signatureHeader))
                return;

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

    public void updateLibraryScreenshot(Library library) {
        final WebDriver driver = DriverUtil.getDriver();
        if(driver == null) {
            log.warn("Driver is null");
            return;
        }
        synchronized (driver) {
            try {
                driver.get(library.getUrl());
                Thread.sleep(1000);
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                libraryScreenshots.put(library.getName(), screenshot);
                log.info("Updated screenshot for library: " + library.getName());
            } catch (Exception e) {
                log.error("Error getting screenshot for library: " + library.getName(), e);
            }
        }
    }

    public void updateLibraryScreenshots() {
        log.info("Updating library screenshots");

        for (Library library : libraryRepository.findAll()) {
            updateLibraryScreenshot(library);
        }
    }


    @GetMapping("/libraries.json")
    public List<Library> libraries(@RequestParam(value = "search", required = false) String searchString) {
        if (searchString == null)
            return libraryRepository.findAll();
        else
            return libraryRepository.findProjectsByNameContainingIgnoreCase(searchString);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        updateLibraryScreenshots();
    }


    @GetMapping(
            value = "/libScreenshot",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] getLibraryScreenshot(@RequestParam(value = "name") String name) {
        return libraryScreenshots.get(name);
    }


    public byte[] get(String libraryName) {
        return libraryScreenshots.get(libraryName);
    }


}
