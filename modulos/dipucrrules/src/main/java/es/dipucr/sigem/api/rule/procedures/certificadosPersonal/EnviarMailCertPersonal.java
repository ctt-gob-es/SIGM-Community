package es.dipucr.sigem.api.rule.procedures.certificadosPersonal;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


public class EnviarMailCertPersonal  implements IRule {
	
	
	private static final Logger logger = Logger.getLogger(EnviarMailCertPersonal.class);
	
	//True si el documento ha sido enviado a Comparece
	private boolean bEnviadoComparece = false;
	//eMail del solicitante
	private String emailSolicitante = null;
	//Documento de "documentos sellados"
	IItem itemDocSellados = null;
	
	//Nombre del documento Carta digital
	private static final String DOC_CARTA_DIGITAL = "Carta digital";
	
	//Descripcion de los documentos "Participantes Comparece" y "Documentos Sellados"
	private static final String DOC_DESC_DOCSELLADOS = "Documentos Sellados";

	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //---------------------------------------------------------------------------------------------- 
	
	        StringBuffer sbQuery = null;
	        IItemCollection collection = null;
	        String numexp = rulectx.getNumExp();
	        
	        //Comprobar que tenemos el documento a enviar
			sbQuery = new StringBuffer();
			sbQuery.append("NOMBRE = '");
			sbQuery.append(DOC_CARTA_DIGITAL);
			sbQuery.append("' AND ID_TRAMITE = ");
			sbQuery.append(rulectx.getTaskId());
			collection = entitiesAPI.getDocuments(numexp, sbQuery.toString(), "");
			
			//Comprobamos que existe el documento
			if (collection.toList().size() == 0){
				rulectx.setInfoMessage("No existe ningún documento de " + DOC_CARTA_DIGITAL + ".");
				return false;
			}
			
			IItem itemDocCarta = (IItem) collection.iterator().next();
			//Comprobamos que esté firmado
			String infopagRde = itemDocCarta.getString("INFOPAG_RDE");
			if (StringUtils.isEmpty(infopagRde)){
				rulectx.setInfoMessage("El documento de " + DOC_CARTA_DIGITAL + " debe estar firmado.");
				return false;
			}
			
			//Comprobamos que se haya registrado de salida
			String nreg = itemDocCarta.getString("NREG");
			if (StringUtils.isEmpty(nreg)){
				rulectx.setInfoMessage("Debe registrar la salida del documento de " + DOC_CARTA_DIGITAL + ".");
				return false;
			}
			
			//Si no se ha enviado a Comparece, debemos enviarlo por email
			sbQuery = new StringBuffer();
			sbQuery.append("DESCRIPCION = '");
			sbQuery.append(DOC_DESC_DOCSELLADOS);
			sbQuery.append("' AND ID_TRAMITE = ");
			sbQuery.append(rulectx.getTaskId());
			collection = entitiesAPI.getDocuments(numexp, sbQuery.toString(), "");
			itemDocSellados = null;
			
			if (collection.next()){
				//[dipucr-Felipe #180] Antes controlábamos el infopag, ahora basta que tengamos documento
				//Si existe el documento "Documentos sellados", es que no ha habido envío por comparece
				itemDocSellados = (IItem) collection.iterator().next();
				bEnviadoComparece = false;
			}
			else{
				bEnviadoComparece = true;
			}
			
			//Si no ha sido enviado por el Comparece, lo intentamos por mail
			//Comprobar que el participante tenga email
			if (!bEnviadoComparece){
				collection = ParticipantesUtil.getParticipantes( cct, numexp, "", "");
				if (collection.next()){
					IItem itemParticipante = (IItem) collection.iterator().next();
					emailSolicitante = itemParticipante.getString("DIRECCIONTELEMATICA");
					if (StringUtils.isEmpty(emailSolicitante)){
						rulectx.setInfoMessage("No se puede enviar el email al solicitante." +
								" Es necesario rellenar su dirección telemática en la pestaña 'Participantes'.");
						return false;
					}
				}
				else{
					rulectx.setInfoMessage("No se puede enviar el email porque no hay ningún" +
							" participante asociado al expediente.");
					return false;
				}
			}	
					
			return true;
				
		}
		catch(Exception e){
			throw new ISPACRuleException("Error al comprobar el envio por " +
        			" mail de la carta al solicitante", e);
	    }
	}

	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		File fileDocSellados = null;
		File fileAnexo = null;
		
		try
		{
			//Si se ha enviado a Comparece, no hacemos nada
	        if (bEnviadoComparece){
	        	return new Boolean(true);
	        }
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //---------------------------------------------------------------------------------------------- 
        	
	        StringBuffer sbQuery = null;
	        IItemCollection collection = null;
	        String numexp = rulectx.getNumExp();
			
	        //Obtenemos su file
			String infoPagDocSellados = itemDocSellados.getString("INFOPAG");
			fileDocSellados = DocumentosUtil.getFile(cct, infoPagDocSellados, "Certificado", Constants._EXTENSION_PDF);
			
			//Obtenemos el documento anexo (el certificado) del primer trámite
			sbQuery = new StringBuffer();
			sbQuery.append("NOMBRE = 'Anexo'");
			collection = entitiesAPI.getDocuments(numexp, sbQuery.toString(), "");
			
			if (collection.toList().size() == 0){
				throw new ISPACRuleException("No hay ningún documento anexo.");
			}
			else if (collection.toList().size() > 1){
				throw new ISPACRuleException("Hay más de un documento Anexo en el expediente.");
			}
			IItem itemDocAnexo = (IItem) collection.iterator().next();
			//Obtenemos su file
			String infoPagAnexo = itemDocAnexo.getString("INFOPAG");
			fileAnexo = DocumentosUtil.getFile(cct, infoPagAnexo, null, null);
			
			//Anexamos el certificado a los documentos sellados
			String strDescAnexo = itemDocAnexo.getString("DESCRIPCION");
			strDescAnexo += "." + Constants._EXTENSION_PDF;
			PdfUtil.anexarDocumento(fileDocSellados, fileAnexo, strDescAnexo);

        	//Enviamos el mail
			
			String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");

			Object[] imagen = {rutaImg, new Boolean(true), "logoCabeceraPersonal.png", "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);
			
			StringBuffer sbContenido = new StringBuffer();
			sbContenido.append("<img src='cid:escudo' width='200px'/>");
			sbContenido.append("<p align=justify>");
			sbContenido.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Estimado señor/a:");
			sbContenido.append("<br/> <br/>");
			sbContenido.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Adjunto se remite el certificado solicitado.");

			sbContenido.append("<br/> <br/>");
			
			String cAsunto= "[AL-SIGM] Certicado solicitado";
//			String nombreAdjunto = "Certificado." + Constants._EXTENSION_PDF;
//			MailUtil.enviarCorreo(rulectx, emailSolicitante, cAsunto, cContenido, fileDocSellados);
//			MailUtil.enviarCorreoConAcuses(rulectx, emailSolicitante, fileDocSellados, sbContenido.toString(), cAsunto, nombreAdjunto, emailSolicitante, false, imagenes);
			
			String descripError = "";
			boolean enviadoEmail = false;
			Date dFechaEnvio = new Date();
			
			MailUtil.enviarCorreo(rulectx, emailSolicitante, cAsunto, sbContenido.toString(), fileDocSellados, imagenes);
			enviadoEmail = true;
			dFechaEnvio = new Date();
			DipucrCommonFunctions.insertarAcuseEmail(emailSolicitante ,dFechaEnvio, "", "", enviadoEmail, emailSolicitante, descripError, rulectx);
			
			fileAnexo.delete();
			fileDocSellados.delete();
			
			return new Boolean(true);
		}
		catch(Exception e) 
		{
			logger.warn(e.getMessage());
        	throw new ISPACRuleException("Error al enviar el mail al solicitante " + emailSolicitante, e);
        }
	}

		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
