

var $barra = $('#barra-progreso');
actualizarBarra(0);


function actualizarBarra(progreso) {
	if (progreso<0 || progreso>100) {
		return;
	}
	var $barra = $('#barra-progreso');
	$barra.attr('aria-valuenow', progreso);
	$barra.css('width', progreso + '%');
}

