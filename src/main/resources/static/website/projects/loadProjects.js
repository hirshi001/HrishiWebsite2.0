let embed_link = "<iframe frameborder=\"0\" src=\"https://itch.io/embed-upload/7586480?color=333333\" allowfullscreen=\"\" width=\"640\" height=\"500\"><a href=\"https://hirshi001.itch.io/pixelwars\">Play PixelWars on itch.io</a></iframe>"
let clickToStartDiv = null

addEventListener("hashchange", () => {
    loadProjects()
})

function clickToStart() {
    clickToStartDiv = document.getElementsByClassName("click-to-start")[0]
    let currentProject = document.getElementById("project-game-display")
    currentProject.removeChild(clickToStartDiv)
    currentProject.innerHTML = embed_link + currentProject.innerHTML
}

function displayGame(link, name, img, repoLink, description) {
    if (clickToStartDiv != null) {
        let currentProject = document.getElementById("project-game-display")
        currentProject.innerHTML = ""
        currentProject.appendChild(clickToStartDiv)
        clickToStartDiv = null
    }

    document.getElementById("thumbnail").src = img ? img : ""
    document.getElementById("repo_link").href = repoLink ? repoLink : ""
    document.getElementById("project-title").textContent = name ? name : "Name not provided."
    document.getElementById("project-description").innerText = description ? description : "Description not provided."
    embed_link = link
    parent.location.hash = name.toLowerCase()
}

function loadProjects() {
    let search = "";
    if(parent.location.hash==="") {
        search = "id=1"
    }else{
        search = "searchTerm=" + parent.location.hash.substring(1).replace("-", " ")
    }
    fetch("/projects.json?"+search).then(r => r.json()).then(data => {
        data = data[0]
        displayGame(data["gameEmbed"], data["name"], data["image"], data["repositoryLink"], data["description"])
    }).catch((e) => {
        fetch("/projects.json?id=1").then(r => r.json()).then(data => {
            data = data[0]
            displayGame(data["gameEmbed"], data["name"], data["image"], data["repositoryLink"], data["description"])
        })
    })


    fetch("/projects.json").then(r => r.json()).then(data => {
        let projectList = document.getElementById("project-links")
        projectList.innerHTML = ""

        for (let i = 0; i < data.length; i++) {
            let project = data[i]
            let projectDiv = document.createElement("div")
            projectDiv.classList.add("project")

            let d1 = document.createElement("div")

            let button = document.createElement("button")
            button.onclick = () => displayGame(project["gameEmbed"], project["name"], project["image"], project["repositoryLink"], project["description"])

            let img = document.createElement("img")
            img.src = project["image"]
            img.alt = project["alt-image"]


            button.appendChild(img)
            d1.appendChild(button)
            projectDiv.appendChild(d1)

            let d2 = document.createElement("div")
            let link = document.createElement("a")
            link.classList.add("project-title")
            link.href = project["url"]
            link.target = "_blank"
            link.rel = "noopener noreferrer"
            link.innerText = project["name"]

            d2.appendChild(link)
            projectDiv.appendChild(d2)
            projectList.appendChild(projectDiv)
        }

    });
}
