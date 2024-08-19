//Gender
let man = document.getElementById("man");
let woman = document.getElementById("woman");
let selected = document.getElementById("chooseGender");

function confirmGender() {
    let gender = selected.value;
    
    if (gender == "Man") {
        man.selected = true;
        woman.selected = false;
        localStorage.setItem("selectedGender", "Man");
    } else {
        woman.selected = true;
        man.selected = false;
        localStorage.setItem("selectedGender", "Woman");
    }

    sendGenderAndKeyForm();
    location.reload();
}

window.onload = function() {
    
    //Gender
    let storedGender = localStorage.getItem("selectedGender");
    if (storedGender == "Man") {
        man.selected = true;
        woman.selected = false;
    } else {
        woman.selected = true;
        man.selected = false;
    }
   
    //Send gender
    sendGenderAndKeyForm();
};

//Send gender form
function sendGenderAndKeyForm() {
    let storedGender = localStorage.getItem("selectedGender");
    let storedUserKey = localStorage.getItem("userKey");

    $.ajax({
        url: "/loadingpage",
        type: "POST",
        data: {
            storedGender: storedGender,
            storedUserKey: storedUserKey
        },
        success: function(response) {
        },
        error: function(xhr, status, error) {
            console.error("Error sending data:", error);
        }
    });
}