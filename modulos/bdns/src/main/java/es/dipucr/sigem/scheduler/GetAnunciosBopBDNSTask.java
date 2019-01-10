package es.dipucr.sigem.scheduler;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispactx.TXConstants;
import ieci.tdw.ispac.ispacweb.scheduler.SchedulerTask;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.axis.encoding.Base64;
import org.apache.log4j.Logger;

import BDNS.respuestaAnuncio.RespuestaAnuncio;
import BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncio;
import BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioCabecera;
import BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtracto;
import BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoES;
import BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;

import es.dipucr.bdns.api.impl.BDNSAPI;
import es.dipucr.bdns.boletinoficial.client.BDNSBopClient;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.sigem.api.rt.DipucrFileAdjuntoRT;
import es.dipucr.sigem.api.rt.DipucrSigemRegistroTelematicoAPI;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.HitosUtils;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;


/**
 * Tarea para recuperar anuncios de la BDNS y crear expedientes relacionados
 * para su inserción en el BOP
 */
public class GetAnunciosBopBDNSTask extends SchedulerTask {

	/** Constantes **/
	public final static String GET_ANUNCIOS_BDNS_VARNAME = "GET_ANUNCIOS_BDNS";
	public final static String TEXTO_ENLACE_DOCUMENTO = "BOP_BDNS_TEXTO_ENLACE_DOCUMENTO";
	public final static String TEXTO_ENLACE_DOCUMENTO_TAGCONV = "[ID_DOCUMENTO]";
	public final static String TEXTO_NO_DOCUMENTO_RESOL = "BOP_BDNS_TEXTO_NO_DOCUMENTO_RESOL";//[dipucr-Felipe #522]
	public final static String TEXTO_ENLACE_CONVOCATORIA = "BOP_BDNS_TEXTO_ENLACE_ANUNCIO";
	public final static String TEXTO_ENLACE_CONVOCATORIA_TAGCONV = "[COD_CONVOCATORIA]";
	public final static String MAIL_ERROR_SUBJECT_VARNAME = "BOP_BDNS_ERROR_MAIL_SUBJECT";
	public final static String MAIL_ERROR_CONTENT_VARNAME = "BOP_BDNS_ERROR_MAIL_CONTENT";
	public final static String MAIL_ERROR_TO_VARNAME = "BOP_BDNS_ERROR_MAIL_TO";
	
	public final static String NEW_PARAGRAPH_CODE = "||";
	
	public final static String COD_TRAMITE_BDNS_RT = "BOP_1_BDNS";
	public final static String COD_DOCUMENTO_ADJUNTO = "BOP1D1";
	public final static String EXTENSION_DOCUMENTO_ADJUNTO = "ODT";
	public final static String NOMBRE_DOCUMENTO_ADJUNTO = "Anuncio_BDNS.odt";
	
	public final static int MAX_LENGTH_SUMARIO = 250;
	public final static int MAX_LENGTH_FIRMANTE = 100;
	
	//[dipucr-Felipe #872]
	public final static String COD_TRAM_ALTA_CONV = "BDNS-CONV-ALTA";
	public final static String COD_TRAM_MODIF_CONV = "BDNS-CONV-MODIF";
	
	
	/** Logger de la clase. */
    private static final Logger LOGGER = Logger.getLogger(GetAnunciosBopBDNSTask.class);
    
    
    /**
     * Ejecuta la tarea del scheduler.
     */
    public void run() {
    	
    	ClientContext cct = new ClientContext();
		IInvesflowAPI invesFlowAPI = new InvesflowAPI(cct);
		cct.setAPI(invesFlowAPI);
    	
    	try{
    		
	        LOGGER.warn("INICIO Ejecución GetAnunciosBopBDNSTask");
	        LOGGER.warn("***************************************");
	        
			List<Entidad> entidades = EntidadesUtil.obtenerListaEntidades();
	        
	        if (!CollectionUtils.isEmpty(entidades)) {
	        	for (int i = 0; i < entidades.size(); i++) {
	        		
	        		Entidad entidad = entidades.get(i);
	
	        		if (entidad != null) {
	
	        			// Establecer la entidad en el thread local
	    				EntidadHelper.setEntidad(entidad);
	    				
	        			String sBDNSEntidad = null;
	        			try{
	        				sBDNSEntidad = ConfigurationMgr.getVarGlobal(cct, GET_ANUNCIOS_BDNS_VARNAME);
	        			}
	        			catch(ISPACException ex){
	        				LOGGER.info("Error al recuperar la variable GET_ANUNCIOS_BDNS de la entidad", ex);
	        			}
	        			
	        			boolean bProcesarEntidad = !StringUtils.isEmpty(sBDNSEntidad) && Boolean.valueOf(sBDNSEntidad).booleanValue();
	        			if (bProcesarEntidad){
	        			
	                        LOGGER.warn("*** Inicio de proceso de entidad: " 
	                        		+ entidad.getIdentificador() + " - " + entidad.getNombre());
		        			
		    				// Comprobar el estado de notificación
		    				execute(entidad);
		
	                        LOGGER.warn("*** Fin de proceso de entidad: " 
	                        		+ entidad.getIdentificador() + " - " + entidad.getNombre());
	        			}
	        		}
	        	}
	        }
	        
	        LOGGER.warn("FIN Ejecución GetAnunciosBopBDNSTask");
	        LOGGER.warn("************************************");
    	}
    	catch(Exception ex){
    		LOGGER.error("Error el recorrer las entidades y recuperar sus anuncios BDNS", ex);
    		enviarCorreoError(cct, ex);
    	}
    }

	public void execute(Entidad entidad) throws Exception {
    	
    	ClientContext cct = new ClientContext();
    	IInvesflowAPI invesFlowAPI = new InvesflowAPI(cct);
    	cct.setAPI(invesFlowAPI);
    	
    	try {
    		
    		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
    		BDNSBopClient client = new BDNSBopClient();
    		String idPeticion = null;
    		RespuestaAnuncio respuesta = null;
    		
    		/*
    		 * Posibles anuncios nuevos que hayan fallado en ejecuciones anterior
    		 */
    		IItemCollection colAnunciosError = BDNSAPI.getAnunciosBop(cct);
			@SuppressWarnings("rawtypes")
			List listAnuncios = colAnunciosError.toList();
    		for (int i = 0; i < listAnuncios.size(); i++){
    			IItem itemAnuncio = (IItem) listAnuncios.get(i);
    			int idAnuncio = itemAnuncio.getKeyInt();
    			
    			idPeticion = BDNSAPI.getIdPeticion(cct, entitiesAPI);
        		LOGGER.warn("Id petición BDNS: " + idPeticion);
        		LOGGER.warn("Id anuncio: " + idAnuncio);
        		
        		respuesta = client.solicitarAnuncios(idPeticion, idAnuncio);
        		procesarAnuncio(cct, entidad, respuesta, respuesta.getAnuncios()[0]);
        		BDNSAPI.borrarAnuncioBop(cct, idAnuncio);
    		}
    		
    		/*
    		 * Solicitud y procesamiento de nuevos anuncios
    		 */
    		idPeticion = BDNSAPI.getIdPeticion(cct, entitiesAPI);
    		LOGGER.warn("Id petición BDNS: " + idPeticion);
    		respuesta = client.solicitarAnuncios(idPeticion);
    		RespuestaAnuncioAnunciosAnuncio[] arrAnuncios = respuesta.getAnuncios();
    		//[PRUEBAS]
//    		respuesta = createFakeRespuesta(idPeticion);
//    		RespuestaAnuncioAnunciosAnuncio[] arrAnuncios = createFakeArrAnuncios();
    		
    		if (null != arrAnuncios){
    			LOGGER.warn("Se han recuperado " + arrAnuncios.length + " anuncios");
    			
    			for (RespuestaAnuncioAnunciosAnuncio anuncio : arrAnuncios){
        			
    				int idAnuncio = anuncio.getCabecera().getIdAnuncio();
    				BDNSAPI.nuevoAnuncioBop(cct, idAnuncio, idPeticion, respuesta.getTimestamp());
        		}
    			
    			for (RespuestaAnuncioAnunciosAnuncio anuncio : arrAnuncios){
        			
    				int idAnuncio = anuncio.getCabecera().getIdAnuncio();
    				procesarAnuncio(cct, entidad, respuesta, anuncio);
    				BDNSAPI.borrarAnuncioBop(cct, idAnuncio);
        		}
    		}
    		
    		
    		
    	}
    	catch (Exception e){
        	LOGGER.error("Error al procesar los anuncios BDNS de la entidad", e);
        	throw e;
    	}
    }

	/**
	 * Método que procesa el anuncio y lo envía a SIGEM
	 * @param cct 
	 * @param entidad
	 * @param respuesta
	 * @param anuncio
	 * @throws Exception
	 */
	private void procesarAnuncio(ClientContext cct, Entidad entidad, RespuestaAnuncio respuesta,
			RespuestaAnuncioAnunciosAnuncio anuncio) throws Exception {
		
		int idAnuncio = anuncio.getCabecera().getIdAnuncio();
		LOGGER.warn("Se va a procesar el anuncio " + idAnuncio);
		
		//Ponemos el texto en B64
		String[] arrTextoParrafos = anuncio.getExtracto().getES().getTextoES();
		StringBuffer sbTextoAnuncio = new StringBuffer();
		for (String parrafo : arrTextoParrafos){
			sbTextoAnuncio.append(parrafo);
			sbTextoAnuncio.append(NEW_PARAGRAPH_CODE);
		}
		String textoBase64 = new String(Base64.encode(sbTextoAnuncio.toString().getBytes()));
		
		DipucrFileAdjuntoRT oFileAdjunto = crearArchivoAdjunto(cct, anuncio);
		ArrayList<DipucrFileAdjuntoRT> listAdjuntos = new ArrayList<DipucrFileAdjuntoRT>();
		listAdjuntos.add(oFileAdjunto);
		
		//[dipucr-Felipe #872] Usuario tramitador
		String numexp = anuncio.getCabecera().getRefConvocatoria();
		IResponsible usuario = getUsuarioTramitador(cct, numexp);
		
		//Solicitud de registro e inicio del expediente
		DipucrSigemRegistroTelematicoAPI.registrarFacturaAndIniciarExpediente
		(
				entidad, 
				COD_TRAMITE_BDNS_RT, 
				anuncio.getCabecera().getCodAdminPublica(),
				anuncio.getCabecera().getAdminPublica(),
				createXMLRegistro(respuesta, anuncio, textoBase64), 
				createDatosEspecificos(respuesta, anuncio, textoBase64, usuario),//[dipucr-Felipe #872]
				listAdjuntos
		);
		
		LOGGER.warn("Anuncio " + idAnuncio + " procesado correctamente");
	}

	
	/**
	 * Método que crea el XML de solicitud del registro telemático
	 * @param respuesta 
	 * @param anuncio
	 * @param textoBase64 
	 * @return
	 * @throws Exception
	 */
	private String createXMLRegistro(RespuestaAnuncio respuesta, RespuestaAnuncioAnunciosAnuncio anuncio, String textoBase64) throws Exception {

		StringBuffer sbXml = new StringBuffer();
		
		sbXml.append("<BOPAnuncioConvocatoriaBDNS>");

		sbXml.append("<Cabecera>");
		sbXml.append("<Id_Anuncio>" + anuncio.getCabecera().getIdAnuncio() + "</Id_Anuncio>");
		sbXml.append("<Id_Peticion>" + respuesta.getIdPeticion() + "</Id_Peticion>");
		sbXml.append("<Hora_Peticion>" + respuesta.getTimestamp() + "</Hora_Peticion>");
		sbXml.append("<Cod_AdminPublica>" + anuncio.getCabecera().getCodAdminPublica() + "</Cod_AdminPublica>");
		sbXml.append("<AdminPublica>" + getCDATASection(anuncio.getCabecera().getAdminPublica()) + "</AdminPublica>");
		sbXml.append("<Cod_Organo>" + anuncio.getCabecera().getCodOrgano() + "</Cod_Organo>");
		sbXml.append("<Organo>" + getCDATASection(anuncio.getCabecera().getOrgano()) + "</Organo>");
		sbXml.append("<Cod_Convocatoria>" + anuncio.getCabecera().getCodigoConvocatoria() + "</Cod_Convocatoria>");
		sbXml.append("<Ref_Convocatoria>" + anuncio.getCabecera().getRefConvocatoria() + "</Ref_Convocatoria>");
		sbXml.append("<Desc_Convocatoria>" + getCDATASection(anuncio.getCabecera().getDesConvocatoria()) + "</Desc_Convocatoria>");
		sbXml.append("</Cabecera>");

		sbXml.append("<Extracto>");
		sbXml.append("<Titulo>" + getCDATASection(anuncio.getExtracto().getES().getTituloES()) + "</Titulo>");
		sbXml.append("<Texto>" + getCDATASection(textoBase64) + "</Texto>");
		sbXml.append("<Firma>");
		sbXml.append("<LugarFirma>" + getCDATASection(anuncio.getExtracto().getES().getPieFirmaES().getLugarFirmaES()) + "</LugarFirma>");
		sbXml.append("<FechaFirma>" + anuncio.getExtracto().getES().getPieFirmaES().getFechaFirmaES() + "</FechaFirma>");
		sbXml.append("<Firmante>" + getCDATASection(anuncio.getExtracto().getES().getPieFirmaES().getFirmanteES()) + "</Firmante>");
		sbXml.append("</Firma>");
		sbXml.append("</Extracto>");
		
		sbXml.append("</BOPAnuncioConvocatoriaBDNS>");
		
		return sbXml.toString();
	}
	
	/**
	 * Método que crea los datos específicos a cargar en el expediente
	 * @param anuncio
	 * @param textoBase64 
	 * @return
	 * @throws Exception
	 */
	private String createDatosEspecificos(RespuestaAnuncio respuesta, RespuestaAnuncioAnunciosAnuncio anuncio, 
			String textoBase64, IResponsible usuario) throws Exception
	{
		StringBuffer sbXml = new StringBuffer();

		sbXml.append("<Id_Anuncio>" + anuncio.getCabecera().getIdAnuncio() + "</Id_Anuncio>");
		sbXml.append("<Id_Peticion>" + respuesta.getIdPeticion() + "</Id_Peticion>");
		sbXml.append("<Hora_Peticion>" + respuesta.getTimestamp() + "</Hora_Peticion>");
		sbXml.append("<Cod_AdminPublica>" + anuncio.getCabecera().getCodAdminPublica() + "</Cod_AdminPublica>");
		sbXml.append("<AdminPublica>" + getCDATASection(anuncio.getCabecera().getAdminPublica()) + "</AdminPublica>");
		sbXml.append("<Cod_Organo>" + anuncio.getCabecera().getCodOrgano() + "</Cod_Organo>");
		sbXml.append("<Organo>" + getCDATASection(anuncio.getCabecera().getOrgano()) + "</Organo>");
		sbXml.append("<Cod_Convocatoria>" + anuncio.getCabecera().getCodigoConvocatoria() + "</Cod_Convocatoria>");
		sbXml.append("<Ref_Convocatoria>" + anuncio.getCabecera().getRefConvocatoria() + "</Ref_Convocatoria>");
		sbXml.append("<Desc_Convocatoria>" + getCDATASection(anuncio.getCabecera().getDesConvocatoria()) + "</Desc_Convocatoria>");
		String titulo = anuncio.getExtracto().getES().getTituloES();
		if (titulo.length() >= MAX_LENGTH_SUMARIO){
			titulo = titulo.substring(0, MAX_LENGTH_SUMARIO - 5).concat("...");
		}
		sbXml.append("<Titulo>" + getCDATASection(titulo) + "</Titulo>");
		sbXml.append("<Texto>" + getCDATASection(textoBase64) + "</Texto>");
		sbXml.append("<LugarFirma>" + getCDATASection(anuncio.getExtracto().getES().getPieFirmaES().getLugarFirmaES()) + "</LugarFirma>");
		sbXml.append("<FechaFirma>" + FechasUtil.getFormattedDate(anuncio.getExtracto().getES().getPieFirmaES().getFechaFirmaES()) + "</FechaFirma>");
		String firmante = anuncio.getExtracto().getES().getPieFirmaES().getFirmanteES();
		if (firmante.length() >= MAX_LENGTH_FIRMANTE){
			firmante = firmante.substring(0, MAX_LENGTH_FIRMANTE - 5).concat("...");
		}
		sbXml.append("<Firmante>" + getCDATASection(firmante) + "</Firmante>");
		
		//INICIO [dipucr-Felipe #872]
		if (null != usuario){
			sbXml.append("<Id_Usuario>" + usuario.getUID() + "</Id_Usuario>");
			sbXml.append("<Nombre_Usuario>" + usuario.getName() + "</Nombre_Usuario>");
		}
		//FIN [dipucr-Felipe #872]
				
		return sbXml.toString();
	}

	/**
	 * 
	 * @param cct 
	 * @param anuncio
	 * @return
	 * @throws Exception 
	 */
	private DipucrFileAdjuntoRT crearArchivoAdjunto(ClientContext cct, RespuestaAnuncioAnunciosAnuncio anuncio) throws Exception {

		DipucrFileAdjuntoRT ofileAdjunto = new DipucrFileAdjuntoRT();
		ofileAdjunto.setCodAdjunto(COD_DOCUMENTO_ADJUNTO);
		ofileAdjunto.setExtensionAdjunto(EXTENSION_DOCUMENTO_ADJUNTO);
		ofileAdjunto.setNombre(NOMBRE_DOCUMENTO_ADJUNTO);
		
		File fileAdjunto = crearFileAdjunto(cct, anuncio);
		ofileAdjunto.setFile(fileAdjunto);
		
		return ofileAdjunto;
	}

	/**
	 * 
	 * @param cct 
	 * @param anuncio
	 * @throws Exception 
	 */
	private File crearFileAdjunto(ClientContext cct, RespuestaAnuncioAnunciosAnuncio anuncio) throws Exception {

		FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
		File fileAdjunto = ftMgr.newFile("odt");
		
		OpenOfficeHelper ooHelper = OpenOfficeHelper.getInstance();
		XComponent xComponent = ooHelper.loadDocument("file://" + fileAdjunto.getPath());
		
		XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
	    XText xText = xTextDocument.getText();
	    
	    // create a text cursor
	    XTextCursor xTextCursor = xText.createTextCursor();
	    xTextCursor.gotoRange(xText.getEnd(),false);
	    
	    //Insertar un salto de página para que conserve el estilo
//	    xText.insertControlCharacter(xTextCursor, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);

	    String [] arrParrafos = anuncio.getExtracto().getES().getTextoES();
	    for (String parrafo : arrParrafos){
	    	xText.insertString(xTextCursor, parrafo, false);
	    	xText.insertString(xTextCursor, "\r\r", false);
//	    	xText.insertControlCharacter(xTextCursor, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
	    }
	    
	    //INICIO [dipucr-Felipe #343] Enlace a nuestro documento de anuncio (sólo si dipucr)
	    if (anuncio.getCabecera().getCodAdminPublica().equals(Constantes.DIR3_DIPUCR)){
	    	LOGGER.warn("Convocatoria de Diputación. Vamos a buscar el documento de resolución");
	    	String textoEnlace1 = ConfigurationMgr.getVarGlobal(cct, TEXTO_ENLACE_DOCUMENTO);
	    	IItem itemDocResolucion = BDNSDipucrFuncionesComunes.getDocumentoResolucionConvocatoria
	    			(cct, anuncio.getCabecera().getCodigoConvocatoria());
	    	if(null != itemDocResolucion){
		    	int idDoc = itemDocResolucion.getKeyInt();
		    	textoEnlace1 = textoEnlace1.replace(TEXTO_ENLACE_DOCUMENTO_TAGCONV, String.valueOf(idDoc));
		    	xText.insertString(xTextCursor, textoEnlace1, false);
		    	xText.insertString(xTextCursor, "\r\r", false);
	    	}
	    	else{
	    		//INICIO [dipucr-Felipe #522]
	    		String textoNoDocumento = ConfigurationMgr.getVarGlobal(cct, TEXTO_NO_DOCUMENTO_RESOL);
	    		xText.insertString(xTextCursor, textoNoDocumento, false);
		    	xText.insertString(xTextCursor, "\r\r", false);
//	    		throw new Exception("No se encontrado ningún documento de resolución para la convocatoria de "
//	    				+ "Diputación Ciudad Real. Columna NUM_ACTO de SPAC_DT_DOCUMENTOS. Id.Convocatoria: " 
//	    				+ anuncio.getCabecera().getCodigoConvocatoria() + ". Revisar los logs de SIGEM_TramitacionWeb");
		    	//FIN [dipucr-Felipe #522]
	    	}
	    }
	    //FIN [dipucr-Felipe #343]
	    
	    //Enlace al anuncio en la BDNS
	    String textoEnlace2 = ConfigurationMgr.getVarGlobal(cct, TEXTO_ENLACE_CONVOCATORIA);
	    String codConvocatoria = anuncio.getCabecera().getCodigoConvocatoria();
	    textoEnlace2 = textoEnlace2.replace(TEXTO_ENLACE_CONVOCATORIA_TAGCONV, codConvocatoria);
	    xText.insertString(xTextCursor, textoEnlace2, false);
    	xText.insertString(xTextCursor, "\r", false);
	    
	    String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
		String filePathOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
	    OpenOfficeHelper.saveDocument(xComponent,"file://" + filePathOut, "");
	    
		if(null != ooHelper){
        	ooHelper.dispose();
        }
	    
		return new File(filePathOut);
	}
	
	/**
	 * Para las zonas XML de texto, genera una sección CDATA
	 * @param content
	 * @return
	 * @throws Exception 
	 */
	protected String getCDATASection(String content) throws Exception{
		StringBuffer sbContentCData = new StringBuffer();
		sbContentCData.append("<![CDATA[");
//		sbContentCData.append(content);
		sbContentCData.append(new String(content.getBytes("ISO-8859-1")));//[dipucr-Felipe #374]
		sbContentCData.append("]]>");
		return sbContentCData.toString();
	}
	
	/**
	 * Enviar un correo de error al administrador
	 * @param ctx
	 * @param ex
	 * @throws ISPACException 
	 */
	private void enviarCorreoError(IClientContext cct, Exception ex) {

		try{
			String strDirNotif = ConfigurationMgr.getVarGlobal(cct, MAIL_ERROR_TO_VARNAME);
			HashMap<String, String> variables = new HashMap<String, String>();
			variables.put("EXCEPCION", ex.getMessage());
			MailUtil.enviarCorreoConVariables
				(cct, strDirNotif, MAIL_ERROR_SUBJECT_VARNAME, MAIL_ERROR_CONTENT_VARNAME, null, variables);
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			LOGGER.error("[" + idEntidad + "] Correo de error enviado: " + ex.getMessage() + " : " + ex.getCause(), ex);
		}
		catch(Exception ex1){
			LOGGER.error("Se produjo una excepción al enviar el correo de error", ex1);
		}
	}
	
	/**
	 * [dipucr-Felipe #872]
	 * Devolver usuario tramitador de los trámites de alta convocatoria
	 * @return
	 * @throws ISPACException 
	 */
	private IResponsible getUsuarioTramitador(IClientContext cct, String numexp) throws ISPACException{
		
		IInvesflowAPI invesflowAPI = cct.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		
		//Obtenemos el id proceso
		IResponsible usuario = null;
		IItem itemExpedient = entitiesAPI.getExpedient(numexp);
		
		if (null != itemExpedient){//Puede que sea un anuncio externo y no de diputación
		
			int idProceso = itemExpedient.getInt("IDPROCESO");
			
			//Obtenemos los nombres de los trámites de alta y modif de convocatorias
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append("WHERE NUMEXP = '" + numexp + "' AND NOMBRE IN ");
			sbQuery.append("(SELECT NOMBRE FROM SPAC_CT_TRAMITES WHERE COD_TRAM IN ");
			sbQuery.append("('" + COD_TRAM_ALTA_CONV + "', '" + COD_TRAM_MODIF_CONV + "'))");
			sbQuery.append(" ORDER BY ID DESC");
	
			IItemCollection colTramites = TramitesUtil.queryTramites(cct, sbQuery.toString());
	
			if (colTramites.next()){
				
				IItem itemTramite = colTramites.value();
				int idTramExp = itemTramite.getInt("ID_TRAM_EXP");
				
				sbQuery = new StringBuffer();
				sbQuery.append("INFO LIKE '%" + idTramExp + "%'");
				sbQuery.append(" AND HITO = " + TXConstants.MILESTONE_TASK_START);
				IItemCollection colHitos = HitosUtils.getHitos(cct, idProceso, sbQuery.toString());
				
				if (colHitos.next()){
					IItem itemHito = colHitos.value();
					String idAutor = itemHito.getString("AUTOR");
					
					IRespManagerAPI respAPI = invesflowAPI.getRespManagerAPI();
					usuario = respAPI.getResp(idAutor);
				}
			}
		}
		
		return usuario;
	}
	
	/**
	 * Método que crea una respuesta fake sin necesidad de llamar al servicio web
	 * @param idPeticion 
	 * @return
	 */
	private RespuestaAnuncio createFakeRespuesta(String idPeticion) {

		RespuestaAnuncio respuesta = new RespuestaAnuncio();
		respuesta.setIdPeticion(idPeticion);
		respuesta.setTimestamp(FechasUtil.getFormattedDate(new Date()));
		return respuesta;
	}

	/**
	 * Método que crea un listado de anuncios fake sin necesidad de llamar al servicio web
	 * @return
	 */
	private RespuestaAnuncioAnunciosAnuncio[] createFakeArrAnuncios() {

		RespuestaAnuncioAnunciosAnuncio anuncio = new RespuestaAnuncioAnunciosAnuncio();
		
		RespuestaAnuncioAnunciosAnuncioCabecera cabecera = new RespuestaAnuncioAnunciosAnuncioCabecera(); 
		cabecera.setIdAnuncio(1111);
		cabecera.setCodAdminPublica("L02000013");
		cabecera.setAdminPublica("DIPUTACIÓN PROVINCIAL DE CIUDAD REAL");
		cabecera.setCodOrgano("L02000013");
		cabecera.setOrgano("DIPUTACIÓN PROVINCIAL DE CIUDAD REAL");
		cabecera.setCodigoConvocatoria("307224");
		cabecera.setRefConvocatoria("DPCR2017/244");
		cabecera.setDesConvocatoria("Convocatoria de Subvención Campaña de Fomento Teatral");
		anuncio.setCabecera(cabecera);
		
		RespuestaAnuncioAnunciosAnuncioExtracto extracto = new RespuestaAnuncioAnunciosAnuncioExtracto();
		RespuestaAnuncioAnunciosAnuncioExtractoES ES = new RespuestaAnuncioAnunciosAnuncioExtractoES();
		ES.setTituloES("titulo");
		String[] arrTexto = new String[1];
		arrTexto[0] = "QkROUyhJZGVudGlmLik6MzA3MjI0fHx0ZXh0b3x8";
		ES.setTextoES(arrTexto);
		RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES pieFirma = new RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES(); 
		pieFirma.setLugarFirmaES("Ciudad Real");
		pieFirma.setFechaFirmaES(new Date());
		pieFirma.setFirmanteES("José Manuel Caballero Serrano");
		ES.setPieFirmaES(pieFirma);
		extracto.setES(ES);
		anuncio.setExtracto(extracto);
		
		RespuestaAnuncioAnunciosAnuncio[] arrAnuncios = new RespuestaAnuncioAnunciosAnuncio[1];
		arrAnuncios[0] = anuncio;
		
		return arrAnuncios;
	}
	
}