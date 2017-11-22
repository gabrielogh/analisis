//Establish the WebSocket connection and set up event handlers

var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/playOnline");
webSocket.onmessage = function (msg) { updateByToken(msg); };
webSocket.onclose = function () { alert("Fuiste Desconectado. Seguramente por feo. Chau") };
webSocket.onopen = function() {setActualUser();};

var user_id;
var username;
var jugando;
var game_id;
var timeout;
var barra_carga = 0;

function updateByToken(msg){
  var data = JSON.parse(msg.data);
  if(data.token == "updateOnlineUsers"){
    updateQueue(data);
  }
  else if(data.token == "sendQuestion"){
    game_id=data.game_id;
    sendQuestion(msg);
  }
  else if(data.token == "showResult"){
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
  $('#title-queue').removeClass('queueTitle').html('');
  var data = JSON.parse(msg.data);
  barra_carga=0;
  stop_interval();
  if(data.user_id!=user_id){
    $('#tablero').fadeOut();
    //$('#options').removeClass('respuesta').html('');
    id("turno").innerHTML='<b>Es el turno de tu oponente, debes esperar, no seas ansioso, sabemos que eres un deborador de preguntas.</b>'
  }
  else{
    $('#tablero').fadeIn();
    $('#respuesta-correcta').removeClass('alert alert-success').html('');
    $('#respuesta-incorrecta').removeClass('alert alert-danger').html('');  
    actualizarFicha(user_id, data.question_id, data.turn, game_id);
    id("turno").innerHTML='<b>Es su turno. Si se siente frustrado ante la imposibilidad de contestar una pregunta, piense en las personas que escriben los temrinos y condiciones</b>';
    id("question").innerHTML='<h1>'+data.question+'</h1>';
    id("options").innerHTML='<li class="respuesta"><button class="btn btn-success" id="btn-responder" value="'+data.option1+'" onclick="stop_interval();sendAnswer('+user_id+','+1+','+data.question_id+','+data.turn+','+game_id+')">'+data.option1+'</buton></li>';
    id("options").innerHTML+='<li class="respuesta"><button class="btn btn-success" id="btn-responder" value="'+data.option2+'" onclick="stop_interval();sendAnswer('+user_id+','+2+','+data.question_id+','+data.turn+','+game_id+')">'+data.option2+'</buton></li>';
    id("options").innerHTML+='<li class="respuesta"><button class="btn btn-success" id="btn-responder" value="'+data.option3+'" onclick="stop_interval();sendAnswer('+user_id+','+3+','+data.question_id+','+data.turn+','+game_id+')">'+data.option3+'</buton></li>';
    id("options").innerHTML+='<li class="respuesta"><button class="btn btn-success" id="btn-responder" value="'+data.option4+'" onclick="stop_interval();sendAnswer('+user_id+','+4+','+data.question_id+','+data.turn+','+game_id+')">'+data.option4+'</buton></li>';
  }
}

function sendAnswer(user_id, answer, question_id, turn, game_id){
  var data = {"id":user_id, "answer":answer, "token":"answer", "game_id":game_id, "question_id":question_id, "turn":turn};    
  stop_interval();
  webSocket.send(JSON.stringify(data));
}

function showResult(data){
  $('#resultado_respuesta').removeClass('').html('');
  if(data.user_id == user_id){
    if(data.correct){
      var resultado = '<div id="respuesta-incorrecta" class="alert alert-success"><strong>Respondiste Bien, por fin!!</strong></div>';
      id("resultado_respuesta").innerHTML=resultado;
    }
    else{
      var resultado = '<div id="respuesta-correcta" class="alert alert-danger"><strong>Respondiste MAL, nunca logras nada en tu vida, FRACASADO</strong></div>';
      id("resultado_respuesta").innerHTML=resultado;
    }
  }
}

function showFinalStats(data){
  $('#tablero').removeClass('').html('');

  if(data.winner == user_id){
    if(data.player==1){
      var result = 'Ganaste.<li>Respuestas correctas:'+data.corrects_p1+'</li><li>Respuestas incorrectas:'+data.incorrects_p1+'</li>';
    }
    else{
      var result = 'Ganaste.<li>Respuestas correctas:'+data.corrects_p2+'</li><li>Respuestas incorrectas:'+data.incorrects_p2+'</li>';
    }
  }
  else if(data.loser == user_id){
    if(data.player==2){
      var result ='Perdiste.<li>Respuestas correctas:'+data.corrects_p1+'</li><li>Respuestas incorrectas:'+data.incorrects_p1+'</li>';
    }
    else{
      var result ='Perdiste.<li>Respuestas correctas:'+data.corrects_p2+'</li><li>Respuestas incorrectas:'+data.incorrects_p2+'</li>';
    }
  }
  else{ var result ='Empate';}
  var boton = '<button id="salir_btn" class="btn btn-danger">Salir</buton>'+'<button id="re_play_btn" class="btn btn-success" onclick="play('+data.winner+','+ data.loser+')">Volver a jugar</buton>';
  var div = '<div id="stats" class="center-stats">';
  var table = '<table id="final_results">';
  var tr = '<tr>';
  var th1 = ' <th>Respuestas Correctas<font color="green"> Jugador 1</font></th>';
  var th2 = ' <th>Respuestas Incorrectas<font color="red"> Jugador 1</font></th>';
  var th3 = ' <th>Respuestas Correctas<font color="red"> Jugador 2</font></th>';
  var th4 = ' <th>Respuestas Incorrectas<font color="red"> Jugador 2</font></th>'; 
  var trend = '</tr>';
  var tr2 = '<tr>';
  var td1 ='      <td id="'+data.corrects_p1 +'"><b>'+data.corrects_p1 +'</b></td>';
  var td2 ='      <td id="'+data.corrects_p2 +'"><b>'+data.incorrects_p1+'</b></td>';
  var td3 ='      <td id="inc_p1">'+data.corrects_p2+'</td>';
  var td4 ='      <td id="inc_p2">'+data.incorrects_p2+'</td>';
  var tr2end ='</tr>';
  var endtable ='</table>';
  var enddiv ='</div>';

  var concat = result+''+div+''+table+''+tr+''+th1+''+th2+''+th3+''+th4+''+trend+''+tr2+''+td1+''+td2+''+td3+''+td4+''+tr2end+''+endtable+''+enddiv+''+boton;
  id("content").innerHTML=concat;
}

function actualizarBarra(progreso) {
  if (progreso<0 || progreso>100) {return;}
  var $barra = $('#barra-progreso');
  $barra.attr('aria-valuenow', progreso);
  $barra.css('width', progreso + '%');
}

function actualizarFicha(user_id, question_id, turn, game_id) {
  var $barra = $('#barra-progreso');
  $(document).ready(function(){
    start_interval(user_id,question_id,turn,game_id);
  });

}

function start_interval(user_id, question_id, turn, game_id) {
  timeout = setInterval(function(){ time(user_id, question_id, turn, game_id)}, 100);
}

function stop_interval(){ clearInterval(timeout);}

function time(user_id, question_id, turn, game_id){
  if(barra_carga < 100) {
    barra_carga++;
    actualizarBarra(barra_carga);
  }
  if(barra_carga == 100){
    sendAnswer(user_id, -1, question_id, turn, game_id);
  }
}

function showMessage(data){id("respuesta-correcta").innerHTML=message;}

function updateQueue(data) {
  var id_user_in_queue = 0;
  $('#stats').removeClass('center-stats').html('');
  $('#resultado_respuesta').removeClass('').html('');
  id("queue").innerHTML = "";
  data.userlist.forEach(function (user){
    if(user.id!=user_id && user.username!=null) {
      id_user_in_queue++;
      var input = '<li>'+user.username+'<button class="btn btn-info" value='+user.id+' id='+id_user_in_queue+' onclick="play('+user_id+','+user.id+')">Jugar</buton></li>';
      insert("queue", input);
    }
  });
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) { id(targetId).insertAdjacentHTML("afterbegin", message);}

//Helper function for selecting element by id
function id(id) { return document.getElementById(id);}