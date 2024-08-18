function userKeyGeneration() {
    let storedUserKey = localStorage.getItem("userKey");

    if (storedUserKey === null) {
        const characters = "qwertyuiopasdfghjklzxcvbnm1234567890";
        let userKey = "";
    
        for (let i = 0; i < 30; i++) {
            let randomIndex = Math.floor(Math.random() * characters.length);
            userKey += characters[randomIndex];
        }
        localStorage.setItem("userKey", userKey);
    }
}