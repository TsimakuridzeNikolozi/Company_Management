function changeEmailType() {
    var selectedEmailType = document.getElementById("select").value.toString();
    var receiverEmailField = document.getElementById("receiverEmail");

    if (selectedEmailType === "permission") {
        receiverEmailField.value = parentEmail;
        receiverEmailField.readOnly = true;
    } else {
        receiverEmailField.readOnly = false;
    }
}