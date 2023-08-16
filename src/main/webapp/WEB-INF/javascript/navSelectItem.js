var curr = document.getElementById(id);
curr.classList.add("selected");

window.onbeforeunload = function(){
    curr.classList.remove("selected");
}