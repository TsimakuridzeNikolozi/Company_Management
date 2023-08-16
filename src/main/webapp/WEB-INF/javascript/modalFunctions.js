function showModal(message) {
    // Set person information in the modal
    document.getElementById('subject').innerText = message.subject + '  (' + message.type + ')';
    document.getElementById('senderEmail').innerText = message.senderEmail;
    document.getElementById('receiverEmail').innerText = message.receiverEmail;
    document.getElementById('timestamp').innerText = message.timestamp;
    document.getElementById('text').innerText = message.text;

    // Functionality depending on sender and receiver, and also the message type
    if (message.senderEmail === userEmail)
        document.getElementById('senderEmail').innerText = 'Me';
    else if (message.receiverEmail === userEmail) {
        document.getElementById('receiverEmail').innerText = 'Me';

        if (message.type === 'PERMIT') {
            document.getElementById('approveButton').style.display = 'inline';
            document.getElementById('denyButton').style.display = 'inline';
        } else if (message.type === 'WARNING') {
            document.getElementById('understoodButton').style.display = 'inline';
        }

        document.getElementById('formMessageId').value = message.id;
        document.getElementById('formSubject').value = message.subject;
        document.getElementById('formSenderEmail').value = message.senderEmail;
    }

    // Show the modal
    document.getElementById('emailModal').style.display = 'block';
}

function closeModal() {
    // Hide the buttons
    document.getElementById('approveButton').style.display = 'none';
    document.getElementById('denyButton').style.display = 'none';
    document.getElementById('understoodButton').style.display = 'none';
    // Hide the modal
    document.getElementById('emailModal').style.display = 'none';
}

function sendResponse() {

}