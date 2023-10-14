let style = document.createElement('style');

let gallerySlides = document.getElementsByClassName("gallery-slides")[0]
let innerHTML = "";
for(let i = 0; i < gallerySlides.children.length; i++){
    innerHTML += ".gallery-slides.a" + i + " { left: -" +  i * 100 + "%; }"
}
style.innerHTML = innerHTML;

document.getElementsByTagName('head')[0].appendChild(style);

function galleryChange(dIndex) {
    let gallerySlides = document.getElementsByClassName("gallery-slides")[0]

    let index = parseInt(gallerySlides.getAttribute("data-index"))
    if(isNaN(index)){
        index = 0;
    }
    let newIndex = index + dIndex;
    if (newIndex < 0) {
        newIndex = 0
    }
    if (newIndex >= gallerySlides.children.length) {
        newIndex = gallerySlides.children.length - 1
    }
    console.log(newIndex)

    removeAll()
    gallerySlides.classList.add("a" + newIndex)
    gallerySlides.setAttribute("data-index", newIndex)
}

function removeAll(){
let gallerySlides = document.getElementsByClassName("gallery-slides")[0]
    for(let i = 0; i < gallerySlides.children.length; i++){
        gallerySlides.classList.remove("a" + i)
    }
}