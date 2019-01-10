package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.io.File;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisoFinFirmaRule;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.BloqueosFirmaUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ReglasUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * #1101 Procedimiento SIGEM para Factur@-FACe
 * Firma de la factura desde el circuito
 * @author dipucr-Felipe
 * @since 06.05.2014
 */
public class DipucrFaceFirmarFacturaRule implements IRule 
{
	private static final Logger logger =  Logger.getLogger(DipucrFaceFirmarFacturaRule.class);
	
	private static final String DOCUMENTO_FACTURA = DipucrFaceFacturasUtil.DOCUMENTO_FACTURA;
		
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Firma de la factura desde el circuito
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		IClientContext cct = rulectx.getClientContext();
		try{
			
			//*********************************************
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
			//*********************************************
			
			//Vemos si la factura ha sido firmada o rechazada
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			String numexp = DocumentosUtil.getNumExp(entitiesAPI, idDoc);
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			String estado = itemDocumento.getString("ESTADOFIRMA");
			String strMotivo = null; //De momento no hacemos nada
			boolean bFirmado = false;
			
			if (estado.equals(SignStatesConstants.FIRMADO)){
				bFirmado = true;
				strMotivo = itemDocumento.getString("MOTIVO_REPARO");
			}
			else if (estado.equals(SignStatesConstants.FIRMADO_CON_REPAROS)){
				bFirmado = true;
				strMotivo = itemDocumento.getString("MOTIVO_REPARO");
			}
			else if (estado.equals(SignStatesConstants.RECHAZADO)){
				bFirmado = false;
				strMotivo = itemDocumento.getString("MOTIVO_RECHAZO");
			}
			//Retocamos el motivo para no envar un null
			if (null == strMotivo) strMotivo = "";
			
			//Obtenemos los datos de la factura
			IItem itemFactura = DipucrFaceFacturasUtil.getFacturaEntity(cct, numexp);
			String nreg = itemFactura.getString("NREG_FACE");
			int idTramite = itemDocumento.getInt("ID_TRAMITE");
			
			//Dependiendo de si está firmado o no, se conforma la factura
			if (bFirmado){
				
				//Marcamos la factura como conformada en FACe y Factur@
				DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, nreg, EstadosFace.CONFORMADA, 0, strMotivo);
				
				//[dipucr-Felipe #1303] Envíamos el documento de factura firmado
				DipucrFaceFacturasUtil.enviarFacturaFirmada(cct, nreg, itemDocumento);
					
				//Si se acepta, cerramos el trámite
				//Desde el trámite se cerrará el expediente
				BloqueosFirmaUtil.cerrarSesionYEliminarBloqueos((ClientContext) cct, idTramite); //[dipucr-Felipe #1294]
				TramitesUtil.cerrarTramite(idTramite, rulectx);
				
			}
			else{
				//Enviamos el aviso al usuario que tramitó por último la factura
				//Obtenemos los datos de la revision de facturas
				IItemCollection collection = entitiesAPI.getEntities("REVISION_EFACTURA", numexp);
				IItem itemRevisarFactura = (IItem) collection.iterator().next();
				String idResp = itemRevisarFactura.getString("COD_USUARIO_REVISION");
				StringBuffer sbMessage = new StringBuffer();
				sbMessage.append("<b>");
				sbMessage.append(AvisoFinFirmaRule._MENSAJE_RECHAZADO);
				sbMessage.append(itemDocumento.getString("NOMBRE"));
				sbMessage.append("</b><br/>Motivo:");
				sbMessage.append(itemDocumento.getString("MOTIVO_RECHAZO"));
				AvisosUtil.generarAviso(entitiesAPI, invesFlowAPI.getProcess(numexp).getInt("ID"), 
						numexp, sbMessage.toString(), idResp, cct);
								
				//No marcamos la factura como rechazada, si no que la volvemos a pasar a Intervención o Compras
				//Recuperamos el último trámite que se creó
				StringBuffer sbQuery = new StringBuffer();
				sbQuery.append("WHERE NUMEXP = '");
				sbQuery.append(numexp);
				sbQuery.append("' AND FECHA_CIERRE IS NOT NULL");
				sbQuery.append(" ORDER BY FECHA_INICIO DESC");
				collection = entitiesAPI.queryEntities
				(
						ISPACEntities.DT_ID_TRAMITES,
						sbQuery.toString()
				);
				IItem itemUltimoTramite = (IItem) collection.iterator().next();
				int idTramCtl = itemUltimoTramite.getInt("ID_TRAM_CTL");
				String sCodUltimoTram = TramitesUtil.getCodTram(idTramCtl, cct);
				//Duplicamos el último trámite antes de la firma
				int idTramiteNuevo = TramitesUtil.crearTramite
						((ClientContext) cct, sCodUltimoTram, numexp);
				
				//INICIO [dipucr-Felipe #1151]
				//Lanzamos los avisos manualmente
				if (DipucrFaceFacturasUtil.COD_TRAM_REVISION3.equals(sCodUltimoTram)){
					
					ITask itemTramiteNuevo = invesFlowAPI.getTask(idTramiteNuevo);
					
					String idRespUltimoTram = itemRevisarFactura.getString("COD_GRUPO_REVISION");
					itemTramiteNuevo.set("ID_RESP", idRespUltimoTram);
					itemTramiteNuevo.store(cct);
					
					ReglasUtil.ejecutarReglaTramite(cct, idTramiteNuevo, "AvisoNuevoTramiteRule");
					ReglasUtil.ejecutarReglaTramite(cct, idTramiteNuevo, "AvisoEmailNuevoTramiteRule");
				}
				//FIN [dipucr-Felipe #1151]
				
				//Le copiamos el documento (sólo si es el trámite de Compras)
				//[dipucr-Felipe #1151] Sólo si no estamos en el primer trámite, que
				// ya genera automáticamente el documento, lo tenemos que copiar
				if (!DipucrFaceFacturasUtil.COD_TRAM_REVISION1.equals(sCodUltimoTram)){
					IItem itemDocFactura = DocumentosUtil.getPrimerDocumentByNombre
							(numexp, rulectx, DOCUMENTO_FACTURA); //[dipucr-Felipe #1348]
//							(rulectx.getNumExp(), rulectx, DOCUMENTO_FACTURA);
					String strInfoPag = itemDocFactura.getString("INFOPAG");
					File fileFactura = DocumentosUtil.getFile(cct, strInfoPag, null, null);
	
					//Obtenemos el tipo de documento
			    	collection = (IItemCollection) procedureAPI.getTaskTpDoc(idTramCtl);
			    	IItem itemTaskTpDoc = (IItem) collection.iterator().next();
			    	int documentTypeId = itemTaskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
					
					//Creamos el documento
		    		DocumentosUtil.generaYAnexaDocumento(rulectx, idTramiteNuevo, documentTypeId, 
		    				DOCUMENTO_FACTURA, fileFactura, Constants._EXTENSION_PDF);
					if(fileFactura != null && fileFactura.exists()) fileFactura.delete();
					fileFactura = null;
				}
				
				//Retrocedemos el estado de la factura en FACe
				//[dipucr-Felipe #1151] Sólo si el último trámite no es el de revisión del servicio
				if (!DipucrFaceFacturasUtil.COD_TRAM_REVISION3.equals(sCodUltimoTram)){
					DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, nreg, EstadosFace.VERIFICADA_RCF, 0, "");
					if (DipucrFaceFacturasUtil.COD_TRAM_REVISION1.equals(sCodUltimoTram)){
						DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, nreg, EstadosFace.RECIBIDA_RCF, 0, "");
					}
				}
				
				//Limpiamos la pestaña "Revisión eFactura" 
				itemRevisarFactura.set("CONFORMADA", "");
				itemRevisarFactura.set("COD_DEPARTAMENTO", "");
				itemRevisarFactura.set("DEPARTAMENTO", "");
				itemRevisarFactura.set("MOTIVO", "");
				itemRevisarFactura.store(cct);

				//Cerramos el trámite
				BloqueosFirmaUtil.cerrarSesionYEliminarBloqueos((ClientContext) cct, idTramite); //[dipucr-Felipe #1294]
				TramitesUtil.cerrarTramite(idTramite, rulectx);
			}
		}
		catch (Exception e) {
			String error = "Error al firmar la factura en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage();
        	logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
