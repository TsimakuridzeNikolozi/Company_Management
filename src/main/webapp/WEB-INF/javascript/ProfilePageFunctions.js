function saveFields() {
    var firstName = document.getElementById('firstName').value;
    var lastName = document.getElementById('lastName').value;
    var idNumber = document.getElementById('idNumber').value;
    var birthDate = document.getElementById('birthDate').value;
    var phoneNumber = document.getElementById('phoneNumber').value;
    var gender = document.querySelector('input[name="gender"]:checked').value; // Get selected gender value

    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'profilePage', true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // Handle the response from the server
            console.log(xhr.responseText);
        }
    };

    var params = 'firstName=' + encodeURIComponent(firstName) +
        '&lastName=' + encodeURIComponent(lastName) +
        '&idNumber=' + encodeURIComponent(idNumber) +
        '&birthDate=' + encodeURIComponent(birthDate) +
        '&gender=' + encodeURIComponent(gender) +
        '&phoneNumber=' + encodeURIComponent(phoneNumber);

    xhr.send(params);
}
function validateNumber(inputElement, errorMessageElement) {
    var inputValue = inputElement.value;
    var numericPattern = /^[0-9]+$/;
    if (!numericPattern.test(inputValue)) {
        inputElement.classList.add("invalid-input");
        errorMessageElement.textContent = "Please enter a valid number.";
        document.getElementById("saveButton").disabled = true;
    } else {
        inputElement.classList.remove("invalid-input");
        errorMessageElement.textContent = "";
        document.getElementById("saveButton").disabled = false;
    }
}
function validateAlphabeticInput(inputElement, errorMessageElement) {
    var inputValue = inputElement.value;
    var alphabeticPattern = /^[a-zA-Z]+$/;

    if (!alphabeticPattern.test(inputValue)) {
        inputElement.classList.add("invalid-input");
        errorMessageElement.textContent = "Please enter alphabetic characters only.";
        document.getElementById("saveButton").disabled = true;
    } else {
        inputElement.classList.remove("invalid-input");
        errorMessageElement.textContent = "";
        document.getElementById("saveButton").disabled = false;
    }
}
document.addEventListener('DOMContentLoaded', function() {
    var errorMessage = document.querySelector('.error');
    if (errorMessage) {
        errorMessage.addEventListener('click', function() {
            errorMessage.style.display = 'none';
        });
    }
});