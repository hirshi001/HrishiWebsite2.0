// NOTICE: This script will insert the sidebar html into the page, so do not use this script if you don't want sidebar

// Selecting the sidebar and buttons
let sidebar = document.querySelector(".sidebar");
let sidebarOpenBtn = document.querySelector("#sidebar-open");
let sidebarCloseBtn = document.querySelector("#sidebar-close");
let sidebarLockBtn = document.querySelector("#lock-icon");
let searchResults = document.querySelector("search_results")
const isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)


// Function to toggle the lock state of the sidebar
const toggleLock = () => {
    // If the sidebar is not locked
    if (sidebar.classList.contains("locked")) {
        unlockSidebar()
    } else {
        lockSidebar()
    }
};

const lockSidebar = () => {
    let content = document.getElementById("content")
    sidebar.classList.add("locked");
    sidebar.classList.remove("hoverable");
    sidebarLockBtn.classList.replace("bx-lock-open-alt", "bx-lock-alt");
    sidebarLockBtn.title = "Unlock Sidebar";
    document.cookie = "sidebarLock=True"
    content.style.paddingLeft = 275 + "px";
}

const unlockSidebar = () => {
    let content = document.getElementById("content")
    sidebar.classList.remove("locked");
    sidebar.classList.add("hoverable");
    sidebarLockBtn.classList.replace("bx-lock-alt", "bx-lock-open-alt");
    sidebarLockBtn.title = "Lock Sidebar";
    document.cookie = "sidebarLock=False"
    content.style.paddingLeft = 85 + "px";
}

// Function to hide the sidebar when the mouse leaves
const hideSidebar = () => {
    if (sidebar.classList.contains("hoverable")) {
        sidebar.classList.add("close");
        if (sidebar.contains(document.activeElement)) {
            document.activeElement.blur()
        }
         hideSearchResults()
    }
};

// Function to show the sidebar when the mouse enter
const showSidebar = () => {
    if (sidebar.classList.contains("hoverable")) {
        sidebar.classList.remove("close");
    }
};

// Function to show and hide the sidebar
const toggleSidebar = () => {
    sidebar.classList.toggle("close");
    if (sidebar.contains(document.activeElement)) {
        document.activeElement.blur()
    }
};

const showSearchResults = () => {
    if (searchResults) {
        searchResults.classList.remove("hidden")
    }
}

const hideSearchResults = () => {
    let searchResults = document.getElementById("search_results")
    if (searchResults) {
        searchResults.classList.add("hidden")
    }
}


const onLoad = (source = 'website') => {
    fetch(source+"/sidebar").then(response => response.text())
        .then(data => {
            document.getElementById("sidebar-div").innerHTML = data


            sidebar = document.querySelector(".sidebar");
            sidebarOpenBtn = document.querySelector("#sidebar-open");
            sidebarCloseBtn = document.querySelector("#sidebar-close");
            sidebarLockBtn = document.querySelector("#lock-icon");

            // If the window width is less than 800px, close the sidebar and remove hoverability and lock
            // if (window.innerWidth < 800) {
            //     sidebar.classList.add("close");
            //     sidebar.classList.remove("locked");
            //     sidebar.classList.remove("hoverable");
            // }

            // Adding event listeners to buttons and sidebar for the corresponding actions
            sidebarLockBtn.addEventListener("click", toggleLock);
            sidebar.addEventListener("mouseleave", hideSidebar);
            sidebar.addEventListener("mouseenter", showSidebar);
            // sidebarOpenBtn.addEventListener("click", toggleSidebar);
            // sidebarCloseBtn.addEventListener("click", toggleSidebar);
            sidebar.addEventListener("focusin", ()=>{
                console.log("focusin")
                showSidebar();
            });
            sidebar.addEventListener("focusout", ()=>{
                console.log("focusout")

                // hide sidebar after 100ms to allow focus to be transferred to other elements
                setTimeout(() => {
                    if(searchResults.contains(document.activeElement) || sidebar.contains(document.activeElement) || sidebar===document.activeElement){
                        showSidebar();
                    }else {
                        hideSidebar();
                    }
                }, 100);
            });

            searchResults = document.getElementById("search_results")
            searchResults.addEventListener("focusin", ()=>{
                console.log("focusin search")
                showSidebar();
                showSearchResults();
            });


            // fade out animation
            let anchors = document.getElementsByTagName("a");
            for (let i = 0; i < anchors.length; i++) {
                if (anchors[i].hostname !== window.location.hostname || anchors[i].pathname === window.location.pathname) {
                     continue;
                 }
                anchors[i].addEventListener('click', function (event) {
                    var fader = document.getElementById('fader'),
                        anchor = event.currentTarget;

                    var listener = function () {
                        window.location = anchor.href;
                        fader.removeEventListener('animationend', listener);
                    };
                    fader.addEventListener('animationend', listener);

                    event.preventDefault();
                    fader.classList.add('fade-in');
                });
            }

            window.addEventListener('pageshow', function (event) {
                if (!event.persisted) {
                    return;
                }
                let fader = document.getElementById('fader');
                fader.classList.remove('fade-in');
            });

            // lock toggle check
            const cookieToggle = document.cookie.split("; ")
                .find((row) => row.startsWith("sidebarLock"))?.split("=")[1]

            let content = document.getElementById("content")
            if (cookieToggle === "True") {
                lockSidebar()
                showSidebar()
            } else {
                unlockSidebar()
                hideSidebar();
            }


            // add search input listener
            let input = document.getElementById("search_project")
            let results = document.getElementById("search_results")
            input.addEventListener("input", function (event) {
                search_projects(event.target.value)
            });

            input.addEventListener("focusin", function (event) {
                if (event.target.value === "") {
                    search_projects("")
                }
                showSearchResults();
                showSearchResults();
            });

            input.addEventListener("focusout", function (event) {
                if (!results.contains(event.relatedTarget) && !input.contains(event.relatedTarget)) {
                    hideSearchResults();
                }
            });

            results.addEventListener("focusout", function (event) {
                if (!results.contains(event.relatedTarget) && !input.contains(event.relatedTarget)) {
                    hideSearchResults();
                }
            });

            document.querySelector(".menu_container").addEventListener("scroll", function (event) {
                reposition_search_results()
            });
            reposition_search_results()
            hideSearchResults()
        });
}

const reposition_search_results = () => {
    let input = document.getElementById("search_results")
    input.style.top = input.parentElement.parentElement.getBoundingClientRect().top + "px"
}

const search_projects = (target) => {
    fetch("/projects/projects_display.json?search=" + target.toLowerCase()).then(response => response.json()).then(data => {

        let div = document.getElementById("search_results")
        div.innerHTML = ""
        let ul = div.appendChild(document.createElement("ul"))
        for (let i = 0; i < data.length; i++) {

            let li = ul.appendChild(document.createElement("li"))
            let link = li.appendChild(document.createElement("a"))
            let span = link.appendChild(document.createElement("span"))
            let image = link.appendChild(document.createElement("img"))

            link.href = "/projects#" + data[i]["name"].toLowerCase().replace(" ", "-");
            // console.log(link.href)
            span.innerText = data[i]["name"];
            image.src = data[i]["image"]

            link.addEventListener("click", function (event) {
                console.log(link.href)
                location.assign(link.href)
                if (window.location.pathname !== "/projects") {
                    location.reload()
                }
            });
        }
    });
}


// lock toggle check
const cookieToggle = document.cookie.split("; ")
    .find((row) => row.startsWith("sidebarLock"))?.split("=")[1]


let content = document.getElementById("content")
let transition = window.getComputedStyle(content).transition
content.style.transition = "none"

if (cookieToggle === "True") {
    content.style.paddingLeft = 275 + "px";
} else if (cookieToggle === "False") {
    content.style.paddingLeft = 85 + "px";
}


// sleep for 100ms to allow transition to finish
setTimeout(() => {
    content.style.transition = transition
}, 100);
