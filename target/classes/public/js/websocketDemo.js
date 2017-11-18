//Establish the WebSocket connection and set up event handlers

var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/playOnline");
webSocket.onmessage = function (msg) { updateByToken(msg); };
webSocket.onclose = function () { alert("Fuiste Desconectado. Seguramente por feo. Chau") };
webSocket.onopen = function() {setActualUser();};

var user_id;
var username;
var jugando;
var game_id;

console.log(user_id);

function updateByToken(msg){
  var data = JSON.parse(msg.data);
  console.log(data.token);
  if(data.token == "updateOnlineUsers"){
    updateQueue(data);
  }
  else if(data.token == "UpdateTurn"){
    updateTurn();
  }
  else if(data.token == "sendQuestion"){
    game_id=data.game_id;
    sendQuestion(msg);
  }
  else if(data.token == "showResult"){
    console.log("ENTRAMOS AL SHOWRESULTS!!: ");
    showResult(data);
  }
  else if(data.token == "gameFinished"){
    showFinalStats(data);
  }
  else{
    console.log("error");
  }
}

function setActualUser(){
  user_id = id("user_id").innerHTML;
  username = id("username").innerHTML;
  var data = {"user_id": user_id, "username": username, "token":"putUser"};   
  webSocket.send(JSON.stringify(data)); 
}

function play(p1_id, p2_id){
  jugando = true;
  var data = {"p1_id":p1_id, "p2_id": p2_id, "token":"startGame"};
  webSocket.send(JSON.stringify(data));
}

function sendQuestion(msg){
  $('#queue').removeClass('center').html('');
  var data = JSON.parse(msg.data);
  console.log(data.user_id);
  console.log(user_id);
  console.log(data.user_id!=user_id);
  if(data.user_id!=user_id){
    id("turno").innerHTML='<b>Es el turno de tu oponente, debes esperar, no seas ansioso, sabemos que eres un deborador de preguntas.</b>'
  }
  else{
    id("turno").innerHTML='<b>Es su turno. Si se siente frustrado ante la imposibilidad de contestar una pregunta, piense en las personas que escriben los temrinos y condiciones</b>'
    id("question").innerHTML=data.question;
    id("options").innerHTML='<li><button class="btn btn-success" id="btn-responder" value="'+data.option1+'" onclick="sendAnswer('+user_id+','+1+','+data.question_id+','+data.turn+','+game_id+')">'+data.option1+'</buton></li>';
    id("options").innerHTML+='<li><button class="btn btn-success" id="btn-responder" value="'+data.option2+'" onclick="sendAnswer('+user_id+','+2+','+data.question_id+','+data.turn+','+game_id+')">'+data.option2+'</buton></li>';
    id("options").innerHTML+='<li><button class="btn btn-success" id="btn-responder" value="'+data.option3+'" onclick="sendAnswer('+user_id+','+3+','+data.question_id+','+data.turn+','+game_id+')">'+data.option3+'</buton></li>';
    id("options").innerHTML+='<li><button class="btn btn-success" id="btn-responder" value="'+data.option4+'" onclick="sendAnswer('+user_id+','+4+','+data.question_id+','+data.turn+','+game_id+')">'+data.option4+'</buton></li>';
  }
}

function sendAnswer(user_id, answer, question_id, turn, game_id){
  console.log("user_id:"+user_id);
  console.log("answer_id:"+answer);
  console.log("question_id:"+question_id);
  console.log("turn:"+turn);
  console.log("game_id:"+game_id);
  var dataJson = {"id":user_id, "answer":answer, "token":"answer", "game_id":game_id, "question_id":question_id, "turn":turn};    
  var data = JSON.stringify(dataJson);
  webSocket.send(data);
}

function showResult(data){
  if(data.user_id == user_id){
    if(data.correct)
      showMessage("Respondiste Bien");

    else
      showMessage("Respondiste MAL, nunca pegas una en tu vida, FRACASADO");
  }
}

function showFinalStats(data){
  var button = '<li><button>Aceptar</buton></li>';
  switch(data.winner){
    case "1":
      var input = 'Ganaste.<li>Respuestas correctas:'+data.correct+'</li><li>Respuestas incorrectas:'+data.wrong+'</li>';
      var form = '<form action="/" method="get">'+input+ ' '+button+'</form>';
      showMessage(form);
      break;
    case "2":
      var input ='Perdiste.<li>Respuestas correctas:'+data.correct+'</li><li>Respuestas incorrectas:'+data.wrong+'</li>';
      var form = '<form action="/" method="get">'+input+''+ button+'</form>';
      showMessage(form);
      break;
    default:
      var input ='Empate';
      var form = '<form action="/" method="get">'+input+ ''+ button+'</form>';
      showMessage(form);
  } 
}

function WinnerMessage(){
  if(id("play").innerHTML=="Play"){
    clear();
    var mesg = '<h1> Tu rival ha fracasado, como con todo lo que se propone en su vida. Ganaste.</h1>';
    var input = '<li><button>Aceptar</buton></li>';
    var form = '<form action="/playonline" method="get">'+mesg+'' +input+'</form>';
    showMessage(form);
  }
}

function updateTurn(){ if(document.getElementById("turn")!=null){ location.reload();}}

function showMessage(message){id("respuesta-estado").innerHTML=message; console.log("entramos a la ultima funcion");}

function updateQueue(data) {
  var id_user_in_queue = 0;
  $('#stats').removeClass('center-stats').html('');
  id("queue").innerHTML = "";
  data.userlist.forEach(function (user){
    if(user.id!=user_id && user.username!=null) {
      id_user_in_queue++;
      var input = '<li>'+user.username+'<button value='+user.id+' id='+id_user_in_queue+' onclick="play('+user_id+','+user.id+')">Jugar</buton></li>';
      insert("queue", input);
    }
  });
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) { id(targetId).insertAdjacentHTML("afterbegin", message);}

//Helper function for selecting element by id
function id(id) { return document.getElementById(id);}

