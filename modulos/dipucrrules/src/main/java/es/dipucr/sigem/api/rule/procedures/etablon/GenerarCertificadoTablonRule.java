package es.dipucr.sigem.api.rule.procedures.etablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [eCenpri-Felipe ticket #504]
 * Ragla que
 * 1. Pone la fecha limite al trámite para que de una alarma al usuario 
 * 2. Genera el certificado de publicación
 * @author Felipe
 * @since 28.03.2012
 */
public class GenerarCertificadoTablonRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(GenerarCertificadoTablonRule.class);
	
	protected static final String _DOC_CERTIFICADO = "eTablon - Diligencia de publicación";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase, el trámite y envío al Jefe de departamento para firma
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		String numexp = "";
		
		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			numexp = rulectx.getNumExp();
			IItemCollection collection = null;
			String strQuery = null;
			
			collection = entitiesAPI.getEntities("ETABLON_PUBLICACION", numexp);
			IItem itemPublicacion = (IItem)collection.iterator().next();
			
			//*********************************************
			//Ponemos la fecha de alarma al trámite
			//*********************************************
			//Obtenemos la fecha de fin de vigencia
			Date dFechaFinVigencia = itemPublicacion.getDate("FECHA_FIN_VIGENCIA");
			Date dFechaAlarma = FechasUtil.addDias(dFechaFinVigencia, 1);
			
			//La ponemos en la tabla SPAC_DT_TRAMITES para que se visualice
			IItem itemDtTramite = entitiesAPI.getTask(rulectx.getTaskId());
			itemDtTramite.set("FECHA_LIMITE", dFechaAlarma);
			itemDtTramite.store(cct);
			
			//La ponemos en la tabla SPAC_TRAMITES para que la tenga en cuenta el publicador
			strQuery = "WHERE ID = " + rulectx.getTaskId();
	        collection = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery);
	        IItem itemTramite = (IItem)collection.iterator().next();
	        itemTramite.set("FECHA_LIMITE", dFechaAlarma);
			itemTramite.store(cct);
			
			//*********************************************
			//Generamos el certificado
			//*********************************************
			//Ponemos la fecha de inicio y de fin en la sesión
			IItem itemDocPublicacion = DocumentosUtil.getPrimerDocumentByNombre(rulectx.getNumExp(), rulectx, EnviarPublicacionTablonRule._DOC_PUBLICACION);
			Date dFechaIniVigencia = itemPublicacion.getDate("FECHA_INI_VIGENCIA");
			Date dFechaFirma = itemDocPublicacion.getDate("FFIRMA");
			if (dFechaFirma.after(dFechaIniVigencia)){
				dFechaIniVigencia = dFechaFirma;
			}
			cct.setSsVariable("FECHA_INICIO", FechasUtil.getFormattedDate(dFechaIniVigencia));
			cct.setSsVariable("FECHA_FIN", FechasUtil.getFormattedDate(dFechaFinVigencia));
			
			//Obtenemos el fichero de publicación
			String strInfoPagPub = itemDocPublicacion.getString("INFOPAG_RDE");
			File filePublicacion = DocumentosUtil.getFile(cct, strInfoPagPub, null, null);
						
			//Generamos la plantilla de certificado
			IItem itemDocCertificado = DocumentosUtil.generarDocumento(rulectx, _DOC_CERTIFICADO, _DOC_CERTIFICADO);
			int idDocumentoCert = itemDocCertificado.getKeyInt();
			
			//Convertimos a pdf la plantilla
			String strInfoPagCert = itemDocCertificado.getString("INFOPAG");
			String sRutaCertificado = DocumentConverter.convert2PDF(invesFlowAPI, strInfoPagCert, Constants._EXTENSION_ODT);
	        //Obtenemos el file
			File fileCertificado = new File(sRutaCertificado);
			
			//Concatenamos ambos archivos
			ArrayList<File> listFicheros = new ArrayList<File>();
			listFicheros.add(filePublicacion);
			listFicheros.add(fileCertificado);
        	File fileCompleto = PdfUtil.concatenarArchivos(listFicheros);

        	//INICIO [dipucr-Felipe #1068]
        	itemDocCertificado = DocumentosUtil.anexaDocumento(rulectx, rulectx.getTaskId(),
        			idDocumentoCert, fileCompleto, Constants._EXTENSION_PDF, _DOC_CERTIFICADO);
        	itemDocCertificado.set("AUTOR", itemDocPublicacion.getString("AUTOR"));
        	itemDocCertificado.set("AUTOR_INFO", itemDocPublicacion.getString("AUTOR_INFO"));
        	itemDocCertificado.store(cct);
        	//FIN [dipucr-Felipe #1068]
        	
        	if(fileCompleto != null && fileCompleto.exists()) fileCompleto.delete();
        	fileCompleto = null;
				
		}
		catch (Exception e) {
        	logger.error("Error generar la diligencia de publicación del Tablón del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error generar la diligencia de publicación del Tablón del expediente: " + numexp, e);
		}
		return null;
	}
	
	/**
	 * Aviso
	 */
	public boolean generarAvisoUsuario(IRuleContext rulectx, String cabecera, 
			String motivo, IItem itemDocumento, IItem itemPublicacion) 
		throws Exception
	{
		//*********************************************
		IClientContext ctx = rulectx.getClientContext();
  	    IInvesflowAPI invesFlowAPI = ctx.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		//*********************************************
		
		String numexp = rulectx.getNumExp();
		int idDoc = itemDocumento.getKeyInt();
		
		StringBuffer sbMessage = new StringBuffer();
		sbMessage.append("<b>");
		sbMessage.append(cabecera);
		sbMessage.append("</b><br/>");
		if (StringUtils.isNotEmpty(motivo)){
			sbMessage.append("Motivo: ");
			sbMessage.append(motivo);
			sbMessage.append("<br/>");
		}
		sbMessage.append("Documento: ");
		sbMessage.append(itemDocumento.getString("NOMBRE"));
		sbMessage.append("<br/>");
		sbMessage.append("Título: ");
		sbMessage.append(itemPublicacion.getString("TITULO"));
		
		String destino = DocumentosUtil.getAutorUID(entitiesAPI, idDoc);
		AvisosUtil.generarAviso(entitiesAPI, invesFlowAPI.getProcess(numexp).getInt("ID"),
				numexp, sbMessage.toString(), destino, ctx);

		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
