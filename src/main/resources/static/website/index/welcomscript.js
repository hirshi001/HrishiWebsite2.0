function logistic(x){
    return 1 / (1 + Math.exp(-x))
}

const welcomeSvg = document.getElementById("welcome").getElementsByTagName("svg")[0]
const svgTextArea = welcomeSvg.getElementsByTagName("text")[0]
console.log(svgTextArea.transform)



window.addEventListener('scroll', () => {
    let element = document.getElementById("welcome")
    let percent = window.scrollY / (element.offsetHeight - window.innerHeight);
    percent = Math.min(1, percent);
    percent = logistic(percent * 20 - 10);
    document.body.style.setProperty('--scroll', percent);

    // set scale, rotation
    let newScale = (1 - percent) + (percent) * 1000
    svgTextArea.setAttribute("x", (window.innerHeight/2-svgTextArea.getBBox().width/2).toString())
    svgTextArea.setAttribute("transform", 'scale(' + newScale + ')')

    // set opacity
    svgTextArea.style.opacity = (1 - percent).toString();
    svgTextArea.rotate = 90 * percent


}, false);


