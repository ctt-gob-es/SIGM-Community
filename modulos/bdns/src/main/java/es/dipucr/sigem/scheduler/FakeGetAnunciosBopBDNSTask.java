package es.dipucr.sigem.scheduler;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
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
import es.dipucr.sigem.api.rt.DipucrFileAdjuntoRT;
import es.dipucr.sigem.api.rt.DipucrSigemRegistroTelematicoAPI;
import es.dipucr.sigem.api.rule.common.utils.EntidadesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;

/**
 * Tarea para recuperar anuncios de la BDNS y crear expedientes relacionados
 * para su inserción en el BOP
 */
public class FakeGetAnunciosBopBDNSTask extends SchedulerTask {

	/** Constantes **/
	public final static String GET_ANUNCIOS_BDNS_VARNAME = "GET_ANUNCIOS_BDNS";
	public final static String TEXTO_ENLACE_ANUNCIO = "BOP_BDNS_TEXTO_ENLACE_ANUNCIO";
	public final static String TEXTO_ENLACE_ANUNCIO_TAGCONV = "[COD_CONVOCATORIA]";
	public final static String MAIL_ERROR_SUBJECT_VARNAME = "BOP_BDNS_ERROR_MAIL_SUBJECT";
	public final static String MAIL_ERROR_CONTENT_VARNAME = "BOP_BDNS_ERROR_MAIL_CONTENT";
	public final static String MAIL_ERROR_TO_VARNAME = "BOP_BDNS_ERROR_MAIL_TO";
	
	public final static String NEW_PARAGRAPH_CODE = "||";
	
	public final static String COD_TRAMITE_BDNS_RT = "BOP_1_BDNS";
	public final static String COD_DOCUMENTO_ADJUNTO = "BOP1D1";
	public final static String EXTENSION_DOCUMENTO_ADJUNTO = "ODT";
	public final static String NOMBRE_DOCUMENTO_ADJUNTO = "Anuncio_BDNS.odt";
//	
	
	/** Logger de la clase. */
    private static final Logger logger = 
    	Logger.getLogger(GetAnunciosBopBDNSTask.class);
    
    
    /**
     * Ejecuta la tarea del scheduler.
     */
    public void run() {

		ClientContext cct = new ClientContext();
		IInvesflowAPI invesFlowAPI = new InvesflowAPI(cct);
		cct.setAPI(invesFlowAPI);
		
    	try{
	        if (logger.isDebugEnabled()) {
	            logger.debug("INICIO Ejecución GetAnunciosBopBDNSTask");
	        }

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
	        				logger.error("Error al recuperar la variable GET_ANUNCIOS_BDNS de la entidad", ex);
	        			}
	        			
	        			boolean bProcesarEntidad = !StringUtils.isEmpty(sBDNSEntidad) && Boolean.valueOf(sBDNSEntidad).booleanValue();
	        			if (bProcesarEntidad){
	        			
		                    if (logger.isInfoEnabled()) {
		                        logger.info("Inicio de proceso de entidad: " 
		                        		+ entidad.getIdentificador() + " - " + entidad.getNombre());
		                    }
		        			
		    				// Comprobar el estado de notificación
		    				execute(entidad);
		
		                    if (logger.isInfoEnabled()) {
		                        logger.info("Fin de proceso de entidad: " 
		                        		+ entidad.getIdentificador() + " - " + entidad.getNombre());
		                    }
	        			}
	        		}
	        	}
	        }
	        
	        if (logger.isDebugEnabled()) {
	            logger.debug("FIN Ejecución GetAnunciosBopBDNSTask");
	        }
    	}
    	catch(Exception ex){
    		logger.error("Error el recorrer las entidades y recuperar sus anuncios BDNS", ex);
    		enviarCorreoError(cct, ex);
    	}
    }
    
    public void execute(Entidad entidad) throws Exception {
    	
    	ClientContext cct = new ClientContext();
    	IInvesflowAPI invesFlowAPI = new InvesflowAPI(cct);
    	cct.setAPI(invesFlowAPI);
    	
    	try {
    		
    		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
    		
    		//Formamos la petición de anuncio
//    		BDNSBopClient client = new BDNSBopClient();
//    		RespuestaAnuncio respuesta = client.solicitarAnuncios(getIdPeticion(entitiesAPI));
    		logger.warn("Id petición BDNS: " + BDNSAPI.getIdPeticion(cct, entitiesAPI));
    		RespuestaAnuncio respuesta = getFakeRespuestaAnuncios();
    		
    		for (RespuestaAnuncioAnunciosAnuncio anuncio : respuesta.getAnuncios()){
    			
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
    			
    			//Solicitud de registro e inicio del expediente
    			DipucrSigemRegistroTelematicoAPI.registrarFacturaAndIniciarExpediente
    			(
    					entidad, 
    					COD_TRAMITE_BDNS_RT, 
    					anuncio.getCabecera().getCodAdminPublica(),
    					anuncio.getCabecera().getAdminPublica(),
    					createXMLRegistro(respuesta, anuncio, textoBase64), 
    					createDatosEspecificos(respuesta, anuncio, textoBase64),
    					listAdjuntos
    			);
    			
    		}
    	}
    	catch (Exception e){
        	logger.error("Error al procesar los anuncios BDNS de la entidad", e);
        	throw e;
    	}
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
	private String createDatosEspecificos(RespuestaAnuncio respuesta, RespuestaAnuncioAnunciosAnuncio anuncio, String textoBase64) throws Exception
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
		sbXml.append("<Titulo>" + getCDATASection(anuncio.getExtracto().getES().getTituloES()) + "</Titulo>");
		sbXml.append("<Texto>" + getCDATASection(textoBase64) + "</Texto>");
		sbXml.append("<LugarFirma>" + getCDATASection(anuncio.getExtracto().getES().getPieFirmaES().getLugarFirmaES()) + "</LugarFirma>");
		sbXml.append("<FechaFirma>" + FechasUtil.getFormattedDate(anuncio.getExtracto().getES().getPieFirmaES().getFechaFirmaES()) + "</FechaFirma>");
		sbXml.append("<Firmante>" + getCDATASection(anuncio.getExtracto().getES().getPieFirmaES().getFirmanteES()) + "</Firmante>");
				
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
	    
	    //Enlace al anuncio en la BDNS
	    String textoEnlace = ConfigurationMgr.getVarGlobal(cct, TEXTO_ENLACE_ANUNCIO);
	    String codConvocatoria = anuncio.getCabecera().getCodigoConvocatoria();
	    textoEnlace = textoEnlace.replace(TEXTO_ENLACE_ANUNCIO_TAGCONV, codConvocatoria);
	    xText.insertString(xTextCursor, textoEnlace, false);
    	xText.insertString(xTextCursor, "\r", false);
  
	    
	    String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
		String filePathOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
	    OpenOfficeHelper.saveDocument(xComponent,"file://" + filePathOut, "");
	    
		return new File(filePathOut);
	}

	/**
	 * 
	 * @return
	 */
	private RespuestaAnuncio getFakeRespuestaAnuncios() {

		RespuestaAnuncio respuesta = new RespuestaAnuncio();
		respuesta.setIdPeticion("L02000013-1");
		respuesta.setLiteralError("La respuesta es correcta");
		respuesta.setTimestamp("16/06/2016 11:28");
		respuesta.setCodigoEstado("3000");
		
		RespuestaAnuncioAnunciosAnuncio anuncio = new RespuestaAnuncioAnunciosAnuncio();
		RespuestaAnuncioAnunciosAnuncioCabecera cabecera = new RespuestaAnuncioAnunciosAnuncioCabecera();
		cabecera.setAdminPublica("GOBIERNO DE ARAGÓN");
		cabecera.setCodAdminPublica("A02002834");
		cabecera.setCodigoConvocatoria("300765");
		cabecera.setCodOrgano("A02003457");
		cabecera.setDesConvocatoria("Prueba Ricardo");
		cabecera.setIdAnuncio(204);
		cabecera.setOrgano("INTERVENCIÓN GENERAL DE ARAGON");
		cabecera.setRefConvocatoria(null);
		anuncio.setCabecera(cabecera);
		
		RespuestaAnuncioAnunciosAnuncioExtracto extracto = new RespuestaAnuncioAnunciosAnuncioExtracto();
		RespuestaAnuncioAnunciosAnuncioExtractoES extractoES = new RespuestaAnuncioAnunciosAnuncioExtractoES();
		extractoES.setTituloES("Título del anuncio de la BDNS");
		String [] arrParrafos = new String[4];
		arrParrafos[0] = "Extracto de la aprobación de la apertura de la convocatoria de la 10ª edición de los Premios al Civismo.";
		arrParrafos[1] = "En conformidad con el previsto en los artículos 17.3.b y 20.8.a de la Ley 38/2003, de 17 de noviembre, General de Subvenciones, se publica el extracto de la convocatoria el texto completo de la cual puede consultarse en la Base de datos Nacional de Subvenciones.";
		arrParrafos[2] = "Primero. Beneficiarios.";
		arrParrafos[3] = "Pueden optar al premio del civismo todas las entidades, colectivos y organizaciones inscritos en el Registro de Entidades Ciudadanas del Ayuntamiento de Rubí, así como las personas mayores de 18 años empadronadas a la ciudad.";
		extractoES.setTextoES(arrParrafos);
		RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES pieFirmaES = new RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES();
		pieFirmaES.setFirmanteES("Felipe Moyano");
		pieFirmaES.setFechaFirmaES(new Date());
		pieFirmaES.setLugarFirmaES("Ciudad Real");
		extractoES.setPieFirmaES(pieFirmaES);
		extracto.setES(extractoES);
		anuncio.setExtracto(extracto);
		
		RespuestaAnuncioAnunciosAnuncio[] arrAnuncios = { anuncio };
		respuesta.setAnuncios(arrAnuncios);
		
		return respuesta;
	}
	
	/**
	 * Para las zonas XML de texto, genera una sección CDATA
	 * @param content
	 * @return
	 */
	protected String getCDATASection(String content){
		StringBuffer sbContentCData = new StringBuffer();
		sbContentCData.append("<![CDATA[");
		sbContentCData.append(content);
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
		}
		catch(Exception ex1){
			logger.error("Se produjo una excepción al enviar el correo de error", ex1);
		}
	}
	
}