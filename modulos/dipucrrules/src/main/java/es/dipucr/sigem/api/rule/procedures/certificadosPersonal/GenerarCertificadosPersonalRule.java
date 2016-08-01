package es.dipucr.sigem.api.rule.procedures.certificadosPersonal;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWSProxy;
import es.dipucr.webempleado.services.certificadosPersonal.ObjetoCertificadoDataHandler;


/**
 * [eCenpri-Felipe #632]
 * Genera el documento de certificado conectándose con el WS del portal
 * Adjunto el documento al trámite
 * @author Felipe
 * @since 15.06.2012
 */
public class GenerarCertificadosPersonalRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(GenerarCertificadosPersonalRule.class);

	protected static final String _DOC_CERTIFICADO = Constants.CERTPERSONAL._DOC_CERTIFICADO;
	protected boolean bManual = false; //[eCenpri-Felipe #787]
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		//INICIO [eCenpri-Felipe #787]
		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			String tipoExpediente = itemExpediente.getString("ESTADOINFO");
			bManual = (ExpedientesUtil._TIPO_MANUAL.equals(tipoExpediente));
			//Si el expediente se ha iniciado manualmente, comprobamos que se hayan rellenado
			//al menos los datos del Interesado.
			if (bManual){
				IItemCollection collection = entitiesAPI.getEntities("RRHH_CERTPERSONAL", numexp);
				boolean bErrorDatos = false;
				if (collection.toList().size() == 0){
					bErrorDatos = true;
				}
				else{
					IItem itemSolicitud = (IItem)collection.iterator().next();
					String nif = itemSolicitud.getString("NIF_INTERESADO");
					String nombre = itemSolicitud.getString("NOMBRE_INTERESADO");
					if (StringUtils.isEmpty(nif) || StringUtils.isEmpty(nombre)){
						bErrorDatos = true;
					}
					//Cuando es manual, siempre es de tipo servicios prestados
					itemSolicitud.set("ID_CERTIFICADO", 1);
					itemSolicitud.set("DESC_CERTIFICADO", "SERVICIOS PRESTADOS A LA DIPUTACIÓN PROVINCIAL");
					itemSolicitud.store(cct);
				}
				
				if (bErrorDatos){
					rulectx.setInfoMessage("Debe rellenar al menos el NIF y el Nombre del Interesado" +
							" en la pestaña de 'Certificados de Personal'");
					return false;
				}
			}
			
		}
		catch (Exception e) {
			logger.error("Error al comprobar los datos en el certificado de personal del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al comprobar los datos en el certificado de personal del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		//FIN [eCenpri-Felipe #787]
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			IItemCollection collection = null;
			String numexp = rulectx.getNumExp();
			
			//Recuperamos los datos del solicitante
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			String nif = itemExpediente.getString("NIFCIFTITULAR");
			String nombre = itemExpediente.getString("IDENTIDADTITULAR");
			
			//Recuperar los datos 
			collection = entitiesAPI.getEntities("RRHH_CERTPERSONAL", numexp);
			IItem itemSolicitudCertificado = (IItem)collection.iterator().next();
			int tipoCert = itemSolicitudCertificado.getInt("ID_CERTIFICADO");
			String nombreCert = itemSolicitudCertificado.getString("DESC_CERTIFICADO");
			String lugarPresentacion = itemSolicitudCertificado.getString("LUGAR_PRESENTACION");//[dipucr-Felipe #1399]
			
			//Para el caso en el que solicite Personal en nombre de un usuario
			String nifInteresado = itemSolicitudCertificado.getString("NIF_INTERESADO");
			String nombreInteresado = itemSolicitudCertificado.getString("NOMBRE_INTERESADO");
			if (StringUtils.isNotEmpty(nifInteresado)){
				nif = nifInteresado;
				itemExpediente.set("NIFCIFTITULAR", nifInteresado);
				nombre = nombreInteresado;
				itemExpediente.set("IDENTIDADTITULAR", nombreInteresado);
			}
			
			//Ponemos nombre al asunto del expediente
			StringBuffer sbAsunto = new StringBuffer();
			sbAsunto.append("Solicitud por parte de ");
			sbAsunto.append(nombre);
			sbAsunto.append(" del certificado de '");
			sbAsunto.append(nombreCert);
			sbAsunto.append("'");
			itemExpediente.set("ASUNTO", sbAsunto.toString());
			itemExpediente.store(cct);
			
			//Generación del Informe de Anticipos
			IItem itemDocSolicitud = DocumentosUtil.generarDocumento(rulectx, _DOC_CERTIFICADO, _DOC_CERTIFICADO);
			int idDocumento = itemDocSolicitud.getKeyInt();
			
			//INICIO [eCenpri-Felipe #787]
			//Si el expediente se ha iniciado manualmente, dejamos la plantilla como está
			//para que el tramitador la edite. No hacemos nada más
			if (bManual){
				return new Boolean(true);
			}
			//FIN [eCenpri-Felipe #787]

			//Recuperamos el certificado del servicio web del portal
			CertificadosPersonalWSProxy wsCertif = new CertificadosPersonalWSProxy();
			ObjetoCertificadoDataHandler oCert = wsCertif.generarCertificado(nif, nombre, lugarPresentacion, tipoCert);//[dipucr-Felipe #1399]
			DataHandler dhCertificado = oCert.getCertificado();
			
			//Obtenemos el file
			String nombreArchivo = dhCertificado.getName();
        	File file = new File(nombreArchivo);

        	//Le anexamos el documento
    		DocumentosUtil.anexaDocumento(rulectx, rulectx.getTaskId(), idDocumento, file, Constants._EXTENSION_PDF, _DOC_CERTIFICADO);
        	file.delete();
		}
		catch (Exception e) {
			logger.error("Error en la generación del certificado de personal del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación del certificado de personal del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
