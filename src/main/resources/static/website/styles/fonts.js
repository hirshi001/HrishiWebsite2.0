
function addClassForCharacters(className, delay){
    let elements = Array.from(document.getElementsByClassName(className))

    for(let i = 0; i < elements.length; i++){
        let text = elements[i].textContent;
        let newHTML = "";
        for(let j = 0; j < text.length; j++){
            if(text[j] === " "){
                newHTML += "<span class='space'></span>";
                continue;
            }
            newHTML += "<span class=\"" + elements[i].classList.toString() +"\">" + text[j] + "</span>";
        }
        console.log(elements[i].classList.toString())
        elements[i].innerHTML = newHTML;
        elements[i].classList.remove(className)
        elements[i].classList.add("no-wrap")
        console.log(elements[i].classList)

    }

    elements = document.getElementsByClassName(className)

    for(let i = 0; i < elements.length; i++){
        elements[i].style.animationDelay = (i * delay) % 1 + "s";
    }
}


addClassForCharacters("jump", 0.1)
addClassForCharacters("hover-font", 0)
addClassForCharacters("sink", 0)

let hoverFonts = document.getElementsByClassName("hover-font")
for(let i = 0; i < hoverFonts.length; i++){
    window.addEventListener("mousemove", function(e){
        let targetNode = hoverFonts[i];
        let centerX = targetNode.offsetLeft + targetNode.offsetWidth / 2;
        let centerY = targetNode.offsetTop + targetNode.offsetHeight / 2;

        let mx = e.clientX + window.scrollX;
        let my = e.clientY + window.scrollY;
        let x = mx - centerX;
        let y = my - centerY;
        // increase the scale based on distance from mouse, so closer, bigger
        let distance = Math.sqrt(x*x + y*y);
        let scale = 1;
        if(distance > 50){
            scale = 1;
        }else{
            scale = 1 + (50 - distance) / 100;
        }
        targetNode.style.setProperty('--scale', scale);
    })
}

