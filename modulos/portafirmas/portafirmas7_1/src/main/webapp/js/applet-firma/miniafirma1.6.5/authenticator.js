// Se obtiene el certificado de la firma de un texto aleatorio. 
function obtenerCertificado()
{
	var dataB64 = MiniApplet.getBase64FromText ("" + Math.random()*11, null);
	MiniApplet.sign(dataB64, "SHA1withRSA", "XAdES", null, certificadoCorrecto, certificadoError);
}