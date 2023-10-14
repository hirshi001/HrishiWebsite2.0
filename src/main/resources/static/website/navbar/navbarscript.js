// NOTICE: Do not use this script if the page does not have a navbar

var prevScrollpos = window.scrollY;
var scrollClick = false;

addEventListener("scroll", function() {
    if (scrollClick) {
        scrollClick = false;
        return;
    }
    let currentScrollPos = window.scrollY;
    let navbar = document.getElementById("navbar");
    if (prevScrollpos > currentScrollPos ) {
        navbar.style.top = "0";
    } else {
        navbar.style.top = "-"+navbar.offsetHeight + "px"
    }
    prevScrollpos = currentScrollPos;
})

addEventListener("mousemove", function(e) {
    let currentScrollPos = window.pageYOffset;
    let y = e.clientY;

    let navbar = document.getElementById("navbar");
    if (y < navbar.offsetHeight) {
        document.getElementById("navbar").style.top = "0";
    }
    prevScrollpos = currentScrollPos;

})

function onNavBarButtonClick(){
    scrollClick = true;
}

let navbar = document.getElementById("navbar");
document.getElementById("content").style.paddingTop = navbar.offsetHeight + "px";
