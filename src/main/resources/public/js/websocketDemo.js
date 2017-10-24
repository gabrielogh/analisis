//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/App");
webSocket.onmessage = function (msg) { updateGame(msg); };
webSocket.onclose = function () { alert("Fuiste desconectado. Chau") };

//Send message if "Send" is clicked
id("send").addEventListener("click", function () {
    var val = document.getElementById('resp1').getAttribute('data-value');
    sendMessage(val);
});

id("send2").addEventListener("click", function () {
    var val = document.getElementById('resp2').getAttribute('data-value');
    sendMessage(val);
});

id("send3").addEventListener("click", function () {
    var val = document.getElementById('resp3').getAttribute('data-value');
    sendMessage(val);
});

id("send4").addEventListener("click", function () {
    var val = document.getElementById('resp4').getAttribute('data-value');
    sendMessage(val);
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);
        id("message").value = "";
    }
}

//Update the chat-panel, and the list of connected users
function updateGame(msg,pregunta) {
    var data = JSON.parse(msg.data);
    insert("chat", data.userMessage);
    insert("turno", data.userTurno);
    id("userlist").innerHTML = "";
    data.userlist.forEach(function (user) {
        insert("userlist", "<li>" + user + "</li>");
    });
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}
