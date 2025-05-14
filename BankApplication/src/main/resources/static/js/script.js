//logout message
document.addEventListener("DOMContentLoaded", function () {
    const message = document.getElementById("logoutMessage");
    const errorMessage = document.getElementById("errorMessage");
    if (message) {
        setTimeout(() => {
            message.classList.add("fade-out");
            setTimeout(() => {
                message.remove();
            }, 2000);
        }, 6000);
    }
     if (errorMessage) {
            setTimeout(() => {
                message.classList.add("fade-out");
                setTimeout(() => {
                    errorMessage.remove();
                }, 2000);
            }, 6000);
        }
});


function toggleSidebar() {
  const sidebar = document.querySelector(".slide-bar-container");
  sidebar.style.transform =
    sidebar.style.transform === "translateX(0px)"
      ? "translateX(-100%)"
      : "translateX(0px)";
}
document.querySelectorAll(".slide-bar-items").forEach((items) => {
  items.addEventListener("click", function () {
    const target = this.dataset.target;
    if (!target) return;
    // remove the active class
    document
      .querySelectorAll(".slide-bar-items")
      .forEach((i) => i.classList.remove("active"));
    document
      .querySelectorAll(".content-selection")
      .forEach((selection) => selection.classList.remove("active"));

    // add the active class to clicked items
    this.classList.add("active");
    const targetElement = document.getElementById(target);
    if (targetElement) {
      targetElement.classList.add("active");
    }
     if (window.innerWidth < 600) {
       this.setAttribute('onclick', 'toggleSidebar()');
       toggleSidebar();
       this.removeAttribute('onclick', 'toggleSidebar()');
     }
  });
});
