function logistic(x){
    return 1 / (1 + Math.exp(-x))
}

window.addEventListener('scroll', () => {
    let element = document.getElementById("welcome")
    let percent = window.scrollY / (element.offsetHeight - window.innerHeight);
    percent = Math.min(1, percent);
    percent = logistic(percent * 20 - 10);
    document.body.style.setProperty('--scroll', percent);
}, false);


