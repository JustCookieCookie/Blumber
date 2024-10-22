function newProfile(stroredProfileNumber) {
    let storedGender = localStorage.getItem("selectedGender");
    let storedUserKey = localStorage.getItem("userKey");

    $.ajax({
        url: "/data/post",
        type: "POST",
        data: {
            storedGender: storedGender,
            storedUserKey: storedUserKey,
            stroredProfileNumber: stroredProfileNumber
        },
        success: function(response) {
            getUserProfile();
        },
        error: function(xhr, status, error) {
            console.error("Error sending data:", error);
        }
    });
}


function getUserProfile() {
    let userName1 = document.getElementById("userName1");
    let userGender1 = document.getElementById("userGender1");
    let aboutUser1 = document.getElementById("aboutUser1");
    let userContactLink1 = document.getElementById("userContactLink1");
    let userPhoto1 = document.getElementById("photoOnePicture");
    /********************************************************************************/
    let userName2 = document.getElementById("userName2");
    let userGender2 = document.getElementById("userGender2");
    let aboutUser2 = document.getElementById("aboutUser2");
    let userContactLink2 = document.getElementById("userContactLink2");
    let userPhoto2 = document.getElementById("photoTwoPicture");

    fetch('/data/get')
    .then(response => response.json())
    .then(data => {

        if (data.profileNumber == 1) {
            userName1.textContent = data.userName;
            userGender1.textContent = data.userGender;
            aboutUser1.textContent = data.aboutUser;
            userContactLink1.href = data.userContactLink;
            userContactLink1.textContent = data.userContactLinkName;
            userPhoto1.src = data.userPhoto;
        } else {
            userName2.textContent = data.userName;
            userGender2.textContent = data.userGender;
            aboutUser2.textContent = data.aboutUser;
            userContactLink2.href = data.userContactLink;
            userContactLink2.textContent = data.userContactLinkName;
            userPhoto2.src = data.userPhoto;
        }
    })
    .catch(error => console.error('Error:', error));
}