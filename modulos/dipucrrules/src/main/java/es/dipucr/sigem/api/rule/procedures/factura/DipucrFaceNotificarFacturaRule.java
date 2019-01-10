package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * #1101 Procedimiento SIGEM para Factur@-FACe
 * Regla que notifica el documento de factura a los departamentos
 * seleccionados en la variable del sistema
 * @author dipucr-Felipe
 * @since 05.09.2014
 */
public class DipucrFaceNotificarFacturaRule implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrFaceNotificarFacturaRule.class);
	
	//--------------------------------------------------------------------------------------------------
    // Constantes
    //--------------------------------------------------------------------------------------------------
	private static final String EMAIL_TO_VAR_NAME = "FACTURAE_FIRMADA_EMAIL_TO";
	private static final String EMAIL_TO_COMPRAS_VAR_NAME = "FACTURAE_FIRMADA_EMAIL_TO_COMPRAS";
	private static final String EMAIL_SUBJECT_VAR_NAME = "FACTURAE_FIRMADA_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME = "FACTURAE_FIRMADA_EMAIL_CONTENT";
	
	
	
	private static final String DOCUMENTO_FACTURA = DipucrFaceFacturasUtil.DOCUMENTO_FACTURA;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	/**
	 * execute
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		File fileFactura = null;
		try{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			String numexp = rulectx.getNumExp();
			
			IItemCollection collection = 
				DocumentosUtil.getDocumentsByNombre(numexp, rulectx, DOCUMENTO_FACTURA);
			IItem itemDocumento = (IItem) collection.iterator().next();
			String estado = itemDocumento.getString("ESTADOFIRMA");
			
			//Sólo se avanzará la fase si el documento está firmado 
			if (estado.equals(SignStatesConstants.FIRMADO) ||
				estado.equals(SignStatesConstants.FIRMADO_CON_REPAROS))
			{
				IItem itemDatosFactura = DipucrFaceFacturasUtil.getFacturaEntity(cct, numexp);
				
				//Destinatario del correo
				String destinatario = ConfigurationMgr.getVarGlobal(cct, EMAIL_TO_VAR_NAME);
				
				//Número completo de la factura
				String numSerie = itemDatosFactura.getString("SERIE");
				String numFact = itemDatosFactura.getString("NUMERO");
				String numFacturaCompleto = numFact;
				if (!StringUtils.isEmpty(numSerie)){
					numFacturaCompleto = numSerie + "/" + numFact;
				}
				
				//Variables
				String nregRCF = itemDatosFactura.getString("RCF_NREG");
				Map<String,String> variables = new HashMap<String,String>();
				variables.put("RCF_NREG", itemDatosFactura.getString("RCF_NREG"));
				variables.put("CIF_PROVEEDOR", itemDatosFactura.getString("CIF_PROVEEDOR"));
				variables.put("NOMBRE_PROVEEDOR", itemDatosFactura.getString("NOMBRE_PROVEEDOR"));
				variables.put("NUM_FACTURA_COMPLETO", numFacturaCompleto);
				variables.put("IMPORTE", itemDatosFactura.getString("IMPORTE"));
				//INICIO [dipucr-Felipe #1335]
				IItemCollection collection3 = entitiesAPI.getEntities("REVISION_EFACTURA", numexp);
				IItem itemRevisionFactura = (IItem) collection3.iterator().next();
				String actividad = null;
				String numPartida = null;
				try{
					actividad = itemRevisionFactura.getString("ACTIVIDAD");
					numPartida = itemRevisionFactura.getString("NUM_PARTIDA");
				}
				catch (Exception ex) {}
				if (StringUtils.isNotEmpty(actividad) || StringUtils.isNotEmpty(numPartida)){
					variables.put("EXTRA_DATA", "Aplicación presupuestaria: " + actividad + " - " + numPartida);
				}
				else{
					variables.put("EXTRA_DATA", "");
				}
				//FIN [dipucr-Felipe #1335]
				
				//Archivo
				String infoPagRde = itemDocumento.getString("INFOPAG_RDE");
				String nombreFichero = "Factura_" + nregRCF.replace("/", "_");
				fileFactura = DocumentosUtil.getFile(cct, infoPagRde, nombreFichero, Constants._EXTENSION_PDF);
				
//				String nombreFichero = "Factura_" + nregRCF.replace("/", "_") + "." + Constants._EXTENSION_PDF;
//				input = new FileInputStream(fileFactura);
//				Attachment attachment = new Attachment(nombreFichero, IOUtils.toByteArray(input));
				
				//Envío del email
				MailUtil.enviarCorreoConVariablesUsoExterno //[dipucr-Felipe #1224]
				(
						rulectx,
						destinatario,
						EMAIL_SUBJECT_VAR_NAME,
						EMAIL_CONTENT_VAR_NAME,
						fileFactura,
						variables,
						false
				);
				
				//ENVÍO A COMPRAS, SÓLO LAS SUYAS
				IItem itemCtTramCompras = TramitesUtil.getTramiteByCode
						(rulectx, DipucrFaceFacturasUtil.COD_TRAM_REVISION2);
				String query = "WHERE NUMEXP = '" + numexp + "' AND ID_TRAM_CTL = " + itemCtTramCompras.getKeyInt();
//				IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
				IItemCollection collection2 = entitiesAPI.queryEntities("SPAC_DT_TRAMITES", query);
				
				//Sólo si existe el trámite de compras
				if (collection2.toList().size() > 0){
					
					String destinatario2 = ConfigurationMgr.getVarGlobal(cct, EMAIL_TO_COMPRAS_VAR_NAME);
					
					//Envío del email
					MailUtil.enviarCorreoConVariablesUsoExterno //[dipucr-Felipe #1224]
					(
							rulectx,
							destinatario2,
							EMAIL_SUBJECT_VAR_NAME,
							EMAIL_CONTENT_VAR_NAME,
							fileFactura,
							variables,
							false
					);
				}
				
			}
		}
		catch(Exception ex){
			String error = "Error al comprobar la firma de la factura "
					+ "para avanzar la fase: " + ex.getMessage();
			logger.error(error);
			throw new ISPACRuleException(error, ex);
		}
		finally{	
			//[Dipucr-Manu Ticket#399] - ALSIGM3 Temporales Factura en Sigem
			if(null != fileFactura && fileFactura.exists()){
				fileFactura.delete();
			}
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
