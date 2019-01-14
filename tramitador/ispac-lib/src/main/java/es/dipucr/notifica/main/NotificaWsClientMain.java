package es.dipucr.notifica.main;


//import ieci.tecdoc.sgm.comun.Base64;
//
//import java.io.BufferedWriter;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Paths;
//import java.nio.file.Files;
//import java.rmi.RemoteException;
//import java.security.DigestInputStream;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//import org.apache.log4j.Logger;
//
//import es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipoOrganismoEmisor;
//import es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipo_destinatario;
//import es.dipucr.notifica.ws.notifica._1_0.Consulta_cies;
//import es.dipucr.notifica.ws.notifica._1_0.Direccion_electronica_habilitada;
//import es.dipucr.notifica.ws.notifica._1_0.Documento;
//import es.dipucr.notifica.ws.notifica._1_0.Info_envio;
//import es.dipucr.notifica.ws.notifica._1_0.NotificaWsPortTypeProxy;
//import es.dipucr.notifica.ws.notifica._1_0.Opciones_emision;
//import es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio;
//import es.dipucr.notifica.ws.notifica._1_0.Resultado_alta;
//import es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion;
//import es.dipucr.notifica.ws.notifica._1_0.Resultado_datado;
//import es.dipucr.notifica.ws.notifica._1_0.Resultado_estado;
//import es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos;
//import es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoEmisor;
//import es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCIE;
//import es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCorreos;
//import es.dipucr.notifica.ws.notifica._1_0.Tipo_destinatario;
//import es.dipucr.notifica.ws.notifica._1_0.Tipo_domicilio;
//import es.dipucr.notifica.ws.notifica._1_0.Tipo_envio;
//import es.dipucr.notifica.ws.notifica._1_0.Tipo_municipio;
//import es.dipucr.notifica.ws.notifica._1_0.Tipo_pais;
//import es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario;
//import es.dipucr.notifica.ws.notifica._1_0.Tipo_procedimiento;
//import es.dipucr.notifica.ws.notifica._1_0.Tipo_provincia;


//// ESTA CLASE SÓLO FUNCIONA CON JAVA 1.7
public class NotificaWsClientMain {
	
//public static final Logger logger = Logger.getLogger(NotificaWsClientMain.class);
	

	public static void main(String[] args) {
		
//			//String notifica_address = ServiciosWebNotificaFunciones.getDireccionSW();
//			String notifica_address = "https://notificaws.redsara.es/ws/soap/NotificaWs";
//			//String notifica_address = "https://se-notificaws.redsara.es/ws/soap/NotificaWs"; //Tramite de pruebas SIA 234108
//		    NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy(notifica_address);			 	
//			try
//			{
//				/*
//				//1 CONSULTAR CIES NO TENEMOS CONVENIO CON HACIENDA
//				Consulta_cies cc = new Consulta_cies();			
//				cc.setOrganismo_emisor("L02000013");
//				//swptp.consultaCies(cc);				 
//				 */
//				
//				/*
//				//2 CONSULTAR ORGANISMOS DADOS DE ALTA EN NOTIFICA
//				//swptp.consultaCies(cc);
//				Resultado_organismos_activos roa = swptp.consultaOrganismosActivos();
//				if(null!=roa)
//				{
//					System.out.println(roa.getDescripcion_respuesta());
//					ArrayOfTipoOrganismoEmisor arrayOrganismos = roa.getOrganismos();
//					//for (int i = 0; i < arrayOrganismos.; i++) {
//					//	System.out.println(arrayOrganismos[i].getNombre());						
//					//}
//				}
//				*/
//				
//				/*
//				//3 CONSULTAR ENVIO
//				System.out.println("consultaDatadoEnvio");
//				//Resultado_datado rd = swptp.consultaDatadoEnvio("58b564591a432");
//				Resultado_datado rd = swptp.consultaDatadoEnvio("58b68ae9d6e43");
//				//Resultado_datado rd = swptp.consultaDatadoEnvio("58b68bbc7924d");
//				System.out.println("Resultado_datado-->Descripcion_respuesta: "+rd.getDescripcion_respuesta());
//				System.out.println("Resultado_datado-->Descripcion_estado_actual: "+rd.getDatado().getDescripcion_estado_actual());
//				System.out.println("Resultado_datado-->Estado_actual: "+rd.getDatado().getEstado_actual());
//				System.out.println("Resultado_datado-->Ncc_id_externo: "+rd.getDatado().getNcc_id_externo());
//				System.out.println("Resultado_datado-->Fecha_actualizacion: "+rd.getDatado().getFecha_actualizacion().getTime().toString());
//				System.out.println("##########################Identificador_envio##############################");	
//				System.out.println("Resultado_datado-->Identificador_envio-->Identificador: "+rd.getDatado().getIdentificador_envio().getIdentificador());
//				System.out.println("Resultado_datado-->Identificador_envio-->NifTitular: "+rd.getDatado().getIdentificador_envio().getNif_titular());
//				System.out.println("Resultado_datado-->Identificador_envio-->ReferenciaEmisor: "+rd.getDatado().getIdentificador_envio().getReferencia_emisor());	
//				System.out.println("##########################Datado##############################");	
//				System.out.println("Resultado_datado-->Datado-->Item-->Descripcion: "+rd.getDatado().getDatado().getItem(0).getDescripcion());
//				System.out.println("Resultado_datado-->Datado-->Item-->Estado: "+rd.getDatado().getDatado().getItem(0).getEstado());
//				System.out.println("Resultado_datado-->Datado-->Item-->Fecha: "+rd.getDatado().getDatado().getItem(0).getFecha().getTime().toString());		
//				System.out.println("FIN consultaDatadoEnvio");
//				*/				
//				
//				/*
//				//4 CONSULTAR ENVIO, puedo obtener toda información del envío.
//				
//				System.out.println("infoEnvio");				
//				Info_envio ie = new Info_envio();
//				ie.setEnvio_destinatario("58b80c90c74a4");
//				ResultadoInfoEnvio rie = swptp.infoEnvio(ie);
//				System.out.println("Resultado de la petición: "+rie.getDescripcion_respuesta());				
//				System.out.println("FIN infoEnvio");
//				*/
//				
//				
//				
//				//7-->58b7f0c4eb35d (Carpeta Ciudadana Obligado False 10 dias Notificada)
//				//Estados: Pendiente de comparecencia --> Notificada --> FIN (Certificado)
//				//8-->58b7f215e91e2 (Carpeta Ciudadana Obligado False 10 dias Rehusada)
//				//Estados: Pendiente de comparecencia --> Rehusada --> FIN (Certificado)
//				//5 CONSULTAR CERTIFICADO ENVIO			
//				System.out.println("consultaCertificacionEnvio");				
//				Resultado_certificacion rc = swptp.consultaCertificacionEnvio("58bfc5817b9d9");
//				System.out.println("getDescripcion_respuesta: "+rc.getDescripcion_respuesta());	
//				System.out.println("getCodigo_respuesta: "+rc.getCodigo_respuesta());
//				System.out.println("getFecha_actualizacion: "+rc.getCertificacion().getFecha_actualizacion().getTime().toString());
//				
//				//Add this to write a string to a file
//				//
//				try {
//				    //BufferedWriter out = new BufferedWriter(new FileWriter("test_acuse.pdf"));
//				    //out.write(rc.getCertificacion().getPdf_certificado());  //Replace with the string 
//				                                             //you are trying to write  
//				    //out.close();
//					FileOutputStream fos = new FileOutputStream("test_acuse_deh_obligado_true_notificado_antonio.pdf");
//					fos.write(Base64.decode(rc.getCertificacion().getPdf_certificado()));
//					fos.close();
//				}
//				catch (IOException e)
//				{
//				    System.out.println("Exception ");
//
//				}
//				System.out.println("FIN consultaCertificacionEnvio");
//								
//				
//				/*
//				//6 CONSULTAR ESTADO
//				System.out.println("consultarEstado");	
//				Resultado_estado re = swptp.consultaEstado("58bfc5817b9d9"); //58b80f3a25566(10), 58b68ae9d6e43(5)
//				if(null!=re)
//				{
//					System.out.println("getDescripcion_respuesta: "+re.getDescripcion_respuesta());
//					System.out.println("getCodigo_respuesta: "+re.getCodigo_respuesta());	
//					System.out.println("getEstado: "+re.getEstado().getEstado());
//				}
//				System.out.println("FIN consultarEstado");
//				*/		
//				
//				/*
//				//7 ALTA ENVÍO, envío de notificacióna Notific@
//				
//				Tipo_envio envio_type = new Tipo_envio();		
//				//Concepto
//				envio_type.setConcepto("PRUEBA10 ConObligadotrue Retardo_postal_deh0");								
//				//Destinararios Lo cargaremos del participante del expediente
//				ArrayOfTipo_destinatario arrayDestinatarios = new ArrayOfTipo_destinatario();
//				Tipo_destinatario [] arrayTd = new Tipo_destinatario[1];
//				Tipo_destinatario aux = new Tipo_destinatario();
//				Tipo_persona_destinatario aux_pers = new Tipo_persona_destinatario();	
//				
////				aux_pers.setApellidos("De Juan Casero");+
////				aux_pers.setEmail("luis_dejuan@dipucr.es");
////				aux_pers.setNif("05644882S");
////				aux_pers.setNombre("Luis");
////				aux_pers.setTelefono("679412760");			
//				
//				aux_pers.setApellidos("Jiménez Moreno");
//				aux_pers.setEmail("agustin_jimenez@dipucr.es");
//				aux_pers.setNif("47062508T");
//				aux_pers.setNombre("Agustín");
//				aux_pers.setTelefono("679412760");	
//				
//				aux.setDestinatario(aux_pers);
//				Direccion_electronica_habilitada aux_dir_elec_hab= new Direccion_electronica_habilitada();
//				aux_dir_elec_hab.setCodigo_procedimiento("214091");//214091¿Para que indicar aquí se lo indico también maás adelante? 212201 -->	Notificación Electrónica de Resolución Obligatorio para envíos a la DEH NO OBLIGADOS. Código de procedimiento en la DEH al que el NIF debe estar suscrito.
//				aux_dir_elec_hab.setNif("47062508T");
//				////////////////////////////OBLIGADO? /////////////////////////////////////////////	
//				aux_dir_elec_hab.setObligado(true);//¿Que pasa si es faso? Si el NIF del buzon no esta suscrito al procedimiento de la DEH, el envio solo estara en Carpeta Ciudadana / Sedes, por lo que debe indicar la Caducidad
//				////////////////////////////FIN OBLIGADO? /////////////////////////////////////////////	
//				aux.setDireccion_electronica(aux_dir_elec_hab);
////				Tipo_domicilio tipo_domicilio = new Tipo_domicilio();
////				tipo_domicilio.setBloque("3");
////				tipo_domicilio.setCalificador_numero("");
////				tipo_domicilio.setCie(1);
////				tipo_domicilio.setCodigo_postal("13005");
////				tipo_domicilio.setComplemento("1");
////				tipo_domicilio.setEscalera("3");
////				tipo_domicilio.setLinea_1("12345");
////				tipo_domicilio.setLinea_2("12345");
////				Tipo_municipio tm = new Tipo_municipio();
////				tm.setNombre("Ciudad Real");
////				tm.setCodigo_ine("130343");
////				tipo_domicilio.setMunicipio(tm);
////				tipo_domicilio.setNombre_via("Calle Toledo");
////				tipo_domicilio.setNumero_casa("12");
////				Tipo_pais tp = new Tipo_pais();
////				tp.setCodigo_iso3166("ES");
////				tp.setNombre("España");
////				tipo_domicilio.setPais(tp);
////				tipo_domicilio.setPlanta("3");
////				tipo_domicilio.setPoblacion("Ciudad Real");
////				tipo_domicilio.setPortal("3");
////				Tipo_provincia tp1 = new Tipo_provincia();
////				tp1.setCodigo_provincia("13");
////				tp1.setNombre("Ciudad Real");
////				tipo_domicilio.setProvincia(tp1);
////				tipo_domicilio.setPuerta("3");
////				tipo_domicilio.setPunto_kilometrico("");
////				tipo_domicilio.setTipo_domicilio_concreto("nacional");
////				tipo_domicilio.setTipo_via("Calle");
////				tipo_domicilio.setApartado_correos("13071");				
//				//aux.setDomicilio(tipo_domicilio); //SI NO ASIGNO DOMICILIO PASA DEL CIE !!!!!
//				////////////////////////////SI OBLIGADO = false dias en carpeta ciudadana /////////////////////////////////////////////	
//				Opciones_emision opciones_emision = new Opciones_emision();
//				Calendar cal = Calendar.getInstance();
//				//cal.add(Calendar.DAY_OF_YEAR, 10);
//				cal.add(Calendar.MINUTE, 1);
//				Date date_caducidad = cal.getTime();
//				String sdf = new SimpleDateFormat("YYYY-MM-DD").format(date_caducidad);				
//				opciones_emision.setCaducidad(sdf);
//				////////////////////////////FIN SI OBLIGADO = false dias en carpeta ciudadana /////////////////////////////////////////////	
//				//aux_dir_elec_hab.setObligado(false);//¿Que pasa si es faso? Si el NIF del buzon no esta suscrito al procedimiento de la DEH, el envio solo estara en Carpeta Ciudadana / Sedes, por lo que debe indicar la Caducidad
//				////////////////////////////RETARDO POSTAL?? /////////////////////////////////////////////	
//				opciones_emision.setRetardo_postal_deh(0); //10 ?
//				aux.setOpciones_emision(opciones_emision);
//				////////////////////////////FIN RETARDO POSTAL?? /////////////////////////////////////////////
//				
//				//////////////////////////// ID DE LA NOTIFICACION /////////////////////////////////////////////				
//				String referencia_emisor = "10";//Obligatorio y único dentro de cada organismo. Referencia interna del Emisor del envío. No pueden existir dos envíos con la misma referencia para un mismo organismo. Máximo 20 caracteres.
//				//4-->58b564591a432		
//				//5-->58b68ae9d6e43
//				//6-->58b68bbc7924d
//				//7-->58b7f0c4eb35d (Carpeta Ciudadana Obligado False 10 dias Notificada)
//				//Estados: Pendiente de comparecencia --> Notificada --> FIN (Certificado)
//				//8-->58b7f215e91e2 (Carpeta Ciudadana Obligado False 10 dias Rehusada)
//				//Estados: Pendiente de comparecencia --> Rehusada --> FIN (Certificado)
//				//9-->58b80c90c74a4 (ojo que aparece como 8 en el concepto) Al poner le 10 en el retardo de la notificación no pasa a la deh directamente espera 10 días supongo osea que si pones obligado = false hace caso a la caducidad y el retardo de correos es para el obligado = true
//				//10-->58b80f3a25566
//				//Estados: Pendiente de comparecencia --> 
//				// CUBRIR EL ERROR DE DUPLICADA
//				////////////////////////////FIN ID DE LA NOTIFICACION  /////////////////////////////////////////////
//				
//				aux.setReferencia_emisor(referencia_emisor);
//				String servicio = "normal";//Obligatorio. Tipo de servicio de envío a realizar, puede ser urgente o normal. Válido para la DEH y para el CIE, otorgando prioridad en la cola de envíos. En el caso del envío postal no afecta la entrega, es decir, la entrega se realiza en los plazos normales.
//				aux.setServicio(servicio);
//				String tipo_domicilio1 = "concreto";
//				aux.setTipo_domicilio(tipo_domicilio1);//Obligatorio para envíos postales. Debe rellenarse el campo tipo_domicilio_concreto, que puede ser nacional/extranjero/sin_normalizar/apartado_correos. Si no se dispone de Código Postal, como sucede en algunos países, se colocará el valor 00000.
//				aux.setTitular(aux_pers);
//				arrayTd[0]= aux;
//				arrayDestinatarios.setItem(arrayTd);
//				envio_type.setDestinatarios(arrayDestinatarios);				
//				
//				//Documento
//				Documento doc= new Documento();
//				try {
//					doc.setContenido(Base64Utils.encodeFileToBase64Binary("G:\\doc\\pruebas_curso_sigem_autofirma.pdf"));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				String generar_csv="no"; //si/no Obligatorio. En caso de que desee que Notific@ genere un CSV y que el documento esté firmado digitalmente se debe introducir SI, caso contrario debe introducir NO. El CSV se introducirá en el pie del documento con la información de los firmantes para la consulta por parte del receptor
//				doc.setGenerar_csv(generar_csv);
//				
//				String myHash = "SHA1"; // or "SHA-1" or "SHA-256" or "MD5"
//				MessageDigest md = null;
//				try {
//					md = MessageDigest.getInstance(myHash);
//				} catch (NoSuchAlgorithmException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}				
//				InputStream is = null;
//				
//				//Solo valido para java 1.7
//				try {
//					
//					is = Files.newInputStream(Paths.get("G:\\doc\\pruebas_curso_sigem_autofirma.pdf"));
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				DigestInputStream dis = new DigestInputStream(is, md);
//				//http://hash.online-convert.com/es/generador-sha1
//				//String hash_sha1 = String.valueOf(dis.hashCode());
//				//hex: 13074388c763c3c65c323dc4d5f65eb546944c22
//				//HEX: 13074388C763C3C65C323DC4D5F65EB546944C22
//				//h:e:x: 13:07:43:88:c7:63:c3:c6:5c:32:3d:c4:d5:f6:5e:b5:46:94:4c:22 
//				//base64: EwdDiMdjw8ZcMj3E1fZetUaUTCI= 
//				String hash_sha1 = "EwdDiMdjw8ZcMj3E1fZetUaUTCI=";
//				doc.setHash_sha1(hash_sha1);				
//				String normalizado = "no"; //"si/no" Obligatorio. En caso de que el documento reserve en la primera página el espacio en blanco necesario para	introducir la ventanilla del sobre (NCC, titular, destinatario, domicilio) se debe rellenar con SI. En caso contrario debe rellenarse con NO y el CIE adjuntará una página adicional con la ventana para el ensobrado, incrementando el coste del servicio por la página impresa y por el peso adicional del servicio postal.
//				doc.setNormalizado(normalizado); 
//				envio_type.setDocumento(doc);				
//				
//				//Fecha envio programado
//				Calendar fecha_envio_programado = Calendar.getInstance();
//				fecha_envio_programado.add(Calendar.HOUR, 1);
//				//envio_type.setFecha_envio_programado(fecha_envio_programado);				
//				
//				//Organismo emisor
//				//Obligatorio. Código correspondiente al DIR3.
//				TipoOrganismoEmisor toe = new TipoOrganismoEmisor();
//				toe.setCodigo_dir3("L02000013");//L02000013	
//				toe.setNombre("Diputacion Provincial de CiudadReal");
//				envio_type.setOrganismo_emisor(toe);				
//				
//				//Organismo pagador cie
//				//TipoOrganismoPagadorCIE topcie = null;
//				//TipoOrganismoPagadorCIE topcie = new TipoOrganismoPagadorCIE();
//				//topcie.setCodigo_dir3("L02000013");//L02000013	
//				//Calendar fecha_vigencia = Calendar.getInstance();
//				//fecha_vigencia.add(Calendar.MONTH, 1);
//				//topcie.setFecha_vigencia(fecha_vigencia.getTime());
//				//envio_type.setOrganismo_pagador_cie(topcie);				
//				
//				//Organismo pagador correos
//				//TipoOrganismoPagadorCorreos topc = null;
//				//TipoOrganismoPagadorCorreos topc = new TipoOrganismoPagadorCorreos();
//				//topc.setCodigo_dir3("L02000013");//L02000013	
//				//topc.setCodigo_cliente_facturacion_correos("123456789");
//				//topc.setFecha_vigencia(fecha_vigencia.getTime());
//				//topc.setNumero_contrato_correos("123456789");
//				//envio_type.setOrganismo_pagador_correos(topc);				
//				
//				//Procedimiento
//				Tipo_procedimiento procedimiento = new Tipo_procedimiento("214091", "CONTRATOS MENORES");
//				envio_type.setProcedimiento(procedimiento);				
//				
//				//Tipo de envio
//				String tipo_envio = "notificacion";//notificación o comunicación.
//				envio_type.setTipo_envio(tipo_envio);	
//				/////////////////////////////////////// METODO QUE ENVÍA /////////////////////////////////////////
//				Resultado_alta ra = swptp.altaEnvio(envio_type);				
//				if(null!=ra)
//				{
//					System.out.println("getDescripcion_respuesta:"+ra.getDescripcion_respuesta());
//					System.out.println("getCodigo_respuesta:"+ra.getCodigo_respuesta());
//					System.out.println("getDescripcion_respuesta:"+ra.getDescripcion_respuesta());
//					System.out.println("getCodigo_csv:"+ra.getCodigo_csv());
//					System.out.println("getIdentificadores:"+ra.getIdentificadores().getItem(0).getIdentificador());
//				}	
//				/////////////////////////////////////////////////////////////////////////////////////////////////
//				
//				*/
//			}
//			catch (RemoteException e)
//			{
//				e.printStackTrace();
//			}
//			
			
			
	}

}

