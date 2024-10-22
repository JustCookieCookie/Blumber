//Status bar
let statusBar = document.getElementById("statusBar");


//Confirm window
let confirmWindow = document.getElementById("confirmationWindow");
let buttonConfirm = document.getElementById("buttonConfirm");
let buttonDelete = document.getElementById("buttonDelete");

let body = document.getElementsByTagName("body")[0];

function confirmSave() {
    confirmWindow.style.display = "block";
    buttonConfirm.style.display = "block";
    buttonDelete.style.display = "none";

    let paragraph = document.getElementById("paragraph");
    paragraph.textContent = "Are you sure you want to keep your profile? Please note that you have entered your gender correctly!";
    
    body.style.pointerEvents = "none";
    confirmWindow.style.pointerEvents = "auto";
}
function confirmDelete() {
    confirmWindow.style.display = "block";
    buttonConfirm.style.display = "none";
    buttonDelete.style.display = "block";

    let paragraph = document.getElementById("paragraph");
    paragraph.textContent = "Delete profile?";

    body.style.pointerEvents = "none";
    confirmWindow.style.pointerEvents = "auto";
}
function cancelWindow() {
    body.style.pointerEvents = "auto";
    confirmWindow.style.display = "none";
}



//User photo
let userPhoto = document.getElementById("imgUserPhoto");

let reader = new FileReader();
document.getElementById("inputUserPhoto").onchange = function(event) {
    let file = event.target.files[0];
    if (file) {
        reader.onload = function() {
            userPhoto.src = reader.result;
            userPhoto.style.display = "block";
        };
        reader.readAsDataURL(file);
    }
};


//Settings
let userName;
let aboutUser;
let userContact; //link
let userContactText; //link name

function saveSettings() {
    let hcaptchaResponse = hcaptcha.getResponse();
    
    if (hcaptchaResponse) {
        confirmWindow.style.display = "none";
        body.style.pointerEvents = "all";
    
        userName = document.getElementById("inputName").value.trim();
        aboutUser = document.getElementById("inputAboutMe").value.trim();
        userContact = document.getElementById("inputMyContact").value.trim();
        userContactText = document.getElementById("inputMyContactText").value.trim();
    
        let saveUserPhoto = document.getElementById("inputUserPhoto").value;
    
        if (userName !== "" && aboutUser !== "" && userContact !== "" && userContactText !== "" && userPhoto.src !== window.location.href) {
        
            localStorage.setItem("userName", userName);
            localStorage.setItem("aboutUser", aboutUser);
            localStorage.setItem("userContact", userContact);
            localStorage.setItem("userContactText", userContactText);
            if (saveUserPhoto !== "") {
                localStorage.setItem("userPhoto", reader.result);
            }
        
            statusBar.style.display = "block";
            statusBar.textContent = "Saving succeeded!";
            statusBar.style.color = "green";
    
            sendData();
        } else {
            statusBar.style.display = "block";
            statusBar.textContent = "Error! All fields must be filled in!";
            statusBar.style.color = "red";
        }
    } else {
        confirmWindow.style.display = "none";
        body.style.pointerEvents = "all";
        statusBar.style.display = "block";
        statusBar.textContent = "Error! The captcha didn't pass!";
        statusBar.style.color = "red";
    }
}
function sendData() {
    let storedUserKey = localStorage.getItem("userKey");
    let storedGender = localStorage.getItem("selectedGender");
    let storedUserName = localStorage.getItem("userName");
    let storedAboutUser = localStorage.getItem("aboutUser");
    let storedUserContact = localStorage.getItem("userContact");
    let storedUserContactText = localStorage.getItem("userContactText");

    let imgElement = document.getElementById("imgUserPhoto");
    let storedPhoto = imgElement.src;

    $.ajax({
        url: "/settingsdata",
        type: "POST",
        data: {
            storedUserKey: storedUserKey,
            storedGender: storedGender,
            storedUserName: storedUserName,
            storedAboutUser: storedAboutUser,
            storedUserContact: storedUserContact,
            storedUserContactText: storedUserContactText,
            storedPhoto: storedPhoto
        },
        success: function(response) {
        },
        error: function(xhr, status, error) {
            console.error("Error sending data:", error);
        }
    });
}

function deleteProfile() {
    let hcaptchaResponse = hcaptcha.getResponse();
    
    if (hcaptchaResponse) {
        confirmWindow.style.display = "none";
        body.style.pointerEvents = "all";

        userName = "";
        aboutUser = "";
        userContact = "";
        userContactText = "";
        userPhoto.src = "";

        localStorage.setItem("userName", "");
        localStorage.setItem("aboutUser", "");
        localStorage.setItem("userContact", "");
        localStorage.setItem("userContactText", "");
        localStorage.setItem("userPhoto", "");

        document.getElementById("inputName").value = userName;
        document.getElementById("inputAboutMe").value = aboutUser;
        document.getElementById("inputMyContact").value = userContact;
        document.getElementById("inputMyContactText").value = userContactText;
        userPhoto.style.display = "none";
        userPhoto.src = "";

        document.getElementById("inputUserPhoto").value = "";

        statusBar.style.display = "block";
        statusBar.textContent = "Profile deleted successfully!";
        statusBar.style.color = "green";


        //Textarea height
        let textareaHeight = document.getElementById("inputAboutMe");
        if (textareaHeight.value === "") {
            if (window.innerWidth >= 730) {
                textareaHeight.style.height = "28px";
            } else if (window.innerWidth < 730) {
                textareaHeight.style.height = "21px";
            }
        }

        deleteData();
    } else {
        confirmWindow.style.display = "none";
        body.style.pointerEvents = "all";
        statusBar.style.display = "block";
        statusBar.textContent = "Error! The captcha didn't pass!";
        statusBar.style.color = "red";
    }
}
function deleteData() {
    let storedUserKey = localStorage.getItem("userKey");

    $.ajax({
        url: "/settingsdelete",
        type: "POST",
        data: {
            storedUserKey: storedUserKey,
        },
        success: function(response) {
        },
        error: function(xhr, status, error) {
            console.error("Error sending data:", error);
        }
    });
}

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

    statusBar.style.display = "block";
    statusBar.textContent = "Gender saved!";
    statusBar.style.color = "green";

    sendGenderForm();
}

//Start
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

    //User profile
    userName = localStorage.getItem("userName");
    aboutUser = localStorage.getItem("aboutUser");
    userContact = localStorage.getItem("userContact");
    userContactText = localStorage.getItem("userContactText");
    let storedPhoto = localStorage.getItem("userPhoto");

    if (userName !== "") {
        document.getElementById("inputName").value = userName;
    }
    if (aboutUser !== "") {
        document.getElementById("inputAboutMe").value = aboutUser;
    }
    if (userContact !== "") {
        document.getElementById("inputMyContact").value = userContact;
    }
    if (userContactText !== "") {
        document.getElementById("inputMyContactText").value = userContactText;
    }
    if (storedPhoto !== "") {
        userPhoto.src = storedPhoto;
        if (storedPhoto !== null) {
            userPhoto.style.display = "block";
        }
    }

    //Textarea height
    if (document.getElementById("inputAboutMe").value !== "") {
        document.getElementById("inputAboutMe").style.height = localStorage.getItem("textareaHeight") + "px";
    }

};

//Auto textarea height
function autoGrow(element) {
    element.style.height = "5px";
    element.style.height = (element.scrollHeight) + "px";

    localStorage.setItem("textareaHeight", element.scrollHeight);
}

//Send gender form
function sendGenderForm() {
    let storedGender = localStorage.getItem("selectedGender");
    let storedUserKey = localStorage.getItem("userKey");
    
    $.ajax({
        url: "/loadingpage",
        type: "POST",
        data: {
            storedGender: storedGender,
            storedUserKey: storedUserKey,
        },
        success: function(response) {
        },
        error: function(xhr, status, error) {
            console.error("Error sending data:", error);
        }
    });
}