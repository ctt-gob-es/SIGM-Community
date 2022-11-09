package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ReglasUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * #1101 Procedimiento SIGEM para Factur@-FACe
 * Valida si se conforma o no la factura y dependiendo del departamento
 * seleccionado c�al es el tr�mite a generar autom�ticamente
 * @author dipucr-Felipe
 * @since 06.05.2014
 */
public class DipucrFaceRevisarFacturaRule implements IRule 
{
	private static final Logger logger = Logger.getLogger(DipucrFaceRevisarFacturaRule.class);
	private static final String DOCUMENTO_FACTURA = DipucrFaceFacturasUtil.DOCUMENTO_FACTURA;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validamos que se hayan rellenado los datos de conformaci�n
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			IItemCollection collection = null;	
			String numexp = rulectx.getNumExp();
			
			//Obtenemos los datos de la revision de facturas
			collection = entitiesAPI.getEntities("REVISION_EFACTURA", numexp);
			if (!collection.next()){
				rulectx.setInfoMessage("No se puede terminar el tr�mite. Es necesario " +
						"rellenar los datos de conformaci�n en la pesta�a 'Revisi�n eFactura'.");
				return false;
			}
			
			IItem itemRevisarFactura = (IItem) collection.iterator().next();
			//Dependiendo de si la factura ha sido o no conformada
			String conformada = itemRevisarFactura.getString("CONFORMADA");
			if (StringUtils.isEmpty(conformada)){
				rulectx.setInfoMessage("No se puede terminar el tr�mite. Es necesario " +
						"los campos obligatorios en la pesta�a 'Revisi�n eFactura'.");
				return false;
			}
			else{
				String codDepartamento = itemRevisarFactura.getString("COD_DEPARTAMENTO");
				if (conformada.equals("SI") && StringUtils.isEmpty(codDepartamento)){
					rulectx.setInfoMessage("Si la factura se conforma " +
							"es necesario rellenar el departamento en la pesta�a 'Revisi�n eFactura'.");
					return false;
				}
				
				String motivo = itemRevisarFactura.getString("MOTIVO");
				if (conformada.equals("NO") && StringUtils.isEmpty(motivo)){
					rulectx.setInfoMessage("Si la factura no se conforma " +
							"es necesario rellenar el motivo en la pesta�a 'Revisi�n eFactura'.");
					return false;
				}
			}	
		}
		catch (Exception e) {
			String error = "Error al revisar la factura en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage();
        	logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return true;
		
	}
	
	/**
	 * Revisa el campo de conformaci�n
	 * Crea el tr�mite correspondiente dependiendo del departamento seleccionado
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ISignAPI signAPI = invesFlowAPI.getSignAPI();
			IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
			//*********************************************
			
			String strQuery = null;
			IItemCollection collection = null;	
			String numexp = rulectx.getNumExp();
			boolean bFirmas = false;
			boolean bTramitacionPrevia = false; //[dipucr-Felipe #1151]
			boolean bTramRevisionServicio = false; //[dipucr-Felipe #1151]

			//Obtenemos los datos de la factura
			IItem itemFactura = DipucrFaceFacturasUtil.getFacturaEntity(cct, numexp);
//			String nreg = itemFactura.getString("NREG_FACE");
			
			//Obtenemos los datos de la revision de facturas
			collection = entitiesAPI.getEntities("REVISION_EFACTURA", numexp);
			IItem itemRevisarFactura = (IItem) collection.iterator().next();
			String codDepartamento = itemRevisarFactura.getString("COD_DEPARTAMENTO");
			
			//Vemos el tr�mite en el que nos encontramos
			ITask itemTramiteActual = invesFlowAPI.getTask(rulectx.getTaskId());
			int idCTTramite = itemTramiteActual.getInt("ID_CTTRAMITE");
			String sCodTramiteActual = TramitesUtil.getCodTram(idCTTramite, cct);
			bTramRevisionServicio = (sCodTramiteActual.equals(DipucrFaceFacturasUtil.COD_TRAM_REVISION3));
			
			//Dependiendo de si la factura ha sido o no conformada
			String conformada = itemRevisarFactura.getString("CONFORMADA");
			if (conformada.equals("SI")){
				
				//Si el departamento elegido es compras y estamos en el primer tr�mite,
				//pasamos al segundo tr�mite de revisi�n
				//Si se trata de otro departamento o ya estamos en el segundo tr�mite,
				//pasamos al tr�mite de firma de la factura
				String sCodTramiteNuevo = null;
				int idCircuito = Integer.MIN_VALUE; //[dipucr-Felipe #1151]
				String grupoTramServicio = null; //[dipucr-Felipe #1151]
				
				if (codDepartamento.equalsIgnoreCase(DipucrFaceFacturasUtil.DEP_COMPRAS)
						&& sCodTramiteActual.equals(DipucrFaceFacturasUtil.COD_TRAM_REVISION1)){
					sCodTramiteNuevo = DipucrFaceFacturasUtil.COD_TRAM_REVISION2;
				}
				else{
					//INICIO [dipucr-Felipe #1151]
					//Dependiendo de si el servicio tiene tramitaci�n previa o va directamente a la firma
					//Sacamos el circuito correspondiente al departamento
					
					String sTablaCircuitos = null;
					if (!sCodTramiteActual.equals(DipucrFaceFacturasUtil.COD_TRAM_REVISION3)){
						sTablaCircuitos = "EFACIL_VLDTBL_CIRCUITOS";
					}
					else{
						sTablaCircuitos = "EFACIL_VLDTBL_CIRCUITOS_2";
					}
					
					strQuery = "WHERE VALOR = '" + codDepartamento + "'";
					collection = entitiesAPI.queryEntities(sTablaCircuitos, strQuery);
					IItem itemCircuitoDept = (IItem) collection.iterator().next();
					String sIdCircuitoOGrupo = itemCircuitoDept.getString("SUSTITUTO");
					
					//Es un grupo
					if (sIdCircuitoOGrupo.contains("-")){
						bTramitacionPrevia = true;
						grupoTramServicio = sIdCircuitoOGrupo;
						sCodTramiteNuevo = DipucrFaceFacturasUtil.COD_TRAM_REVISION3;
					}
					else{ //Es un circuito
						bFirmas = true;
						idCircuito = Integer.valueOf(sIdCircuitoOGrupo);
						sCodTramiteNuevo = DipucrFaceFacturasUtil.COD_TRAM_FIRMA;
					}
					//FIN [dipucr-Felipe #1151]
				}
				//Creamos el tr�mite que corresponda
				int idTramiteNuevo = TramitesUtil.crearTramite(sCodTramiteNuevo, rulectx);
				ITask itemTramiteNuevo = invesFlowAPI.getTask(idTramiteNuevo);
				
				//INICIO [dipucr-Felipe #1151]
				//Ponemos como responsable el grupo y lanzamos los avisos
				if (bTramitacionPrevia){
					
					itemTramiteNuevo.set("ID_RESP", grupoTramServicio);
					itemTramiteNuevo.store(cct);
					
					int idTask = itemTramiteNuevo.getKeyInt();
					ReglasUtil.ejecutarReglaTramite(cct, idTask, "AvisoNuevoTramiteRule");
					ReglasUtil.ejecutarReglaTramite(cct, idTask, "AvisoEmailNuevoTramiteRule");
				}
				//FIN [dipucr-Felipe #1151]
				
				//Le copiamos el documento
				IItem itemDocFactura = DocumentosUtil.getPrimerDocumentByNombre
						(rulectx.getNumExp(), rulectx, DOCUMENTO_FACTURA);
				String strInfoPag = itemDocFactura.getString("INFOPAG");
				File fileFactura = DocumentosUtil.getFile(cct, strInfoPag, null, null);

				//Obtenemos el tipo de documento
				int idTramCtl = itemTramiteNuevo.getInt("ID_CTTRAMITE");
		    	collection = (IItemCollection) procedureAPI.getTaskTpDoc(idTramCtl);
		    	IItem itemTaskTpDoc = (IItem) collection.iterator().next();
		    	int documentTypeId = itemTaskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
				
				//Creamos el documento
	    		IItem itemDocFacturaNuevo = DocumentosUtil.generaYAnexaDocumento
	    				(rulectx, idTramiteNuevo, documentTypeId, DOCUMENTO_FACTURA, fileFactura, Constants._EXTENSION_PDF);
	    		int idDocumento = itemDocFacturaNuevo.getKeyInt();
				if(fileFactura != null && fileFactura.exists()) fileFactura.delete();
				fileFactura = null;
				
				//Si no tiene que revisarlo Compras (3er tr�mite), se pasa directamente
				//al circuito de firma que corresponda
				if (bFirmas){
					//Iniciamos el circuito de firma
					signAPI.initCircuitPortafirmas(idCircuito, idDocumento);//[dipucr-Felipe #1246]
				}
				//Si lo revisa Compras, limpiamos la pesta�a "Revisi�n eFactura" 
				else{
					itemRevisarFactura.set("CONFORMADA", "");
					itemRevisarFactura.set("COD_DEPARTAMENTO", "");
					itemRevisarFactura.set("DEPARTAMENTO", "");
					itemRevisarFactura.set("MOTIVO", "");
				}
				
				//A�adimos el usuario responsable
				String idResp = cct.getRespId();
				itemRevisarFactura.set("COD_USUARIO_REVISION", idResp);
				if (bTramRevisionServicio){
					String idGrupoResp = itemTramiteActual.getString("ID_RESP");
					itemRevisarFactura.set("COD_GRUPO_REVISION", idGrupoResp);
				}
				itemRevisarFactura.store(cct);
				
				//Cambiamos el estado de la factura en FACe y la aplicaci�n Factur@
				//[dipucr-Felipe #1151] S�lo si no estamos en el tr�mite de tramitaci�n previa del servicio
				try{
					if (!bTramRevisionServicio){
						if (sCodTramiteActual.equals(DipucrFaceFacturasUtil.COD_TRAM_REVISION1)){
							if(bFirmas || bTramitacionPrevia){
								DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, itemFactura, EstadosFace.VERIFICADA_RCF, -1, "");
								DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, itemFactura, EstadosFace.RECIBIDA_DESTINO, 0, "");
							}
							else{
								DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, itemFactura, EstadosFace.VERIFICADA_RCF, 0, "");
							}
						}
						else{
							DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, itemFactura, EstadosFace.RECIBIDA_DESTINO, 1, "");
						}
					}
				}
				//[dipucr-Felipe #1472] Si ha insertado la factura en sical pero falla al cambiarle el estado
				//Quitamos la firma del portafirmas
				catch(Exception ex){
					if (bFirmas){
						signAPI.deleteCircuitPortafirmas(idDocumento);
					}
					throw ex;
				}
			}
			//NO CONFORMADA
			else{
				
				//Si estamos en compras o intervenci�n, rechazamos
				if (!bTramRevisionServicio){
					//Ponemos la factura como "Rechazada"
					String motivo = itemRevisarFactura.getString("MOTIVO");
					DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, itemFactura, EstadosFace.RECHAZADA, 0, motivo);
							
//					ExpedientesUtil.avanzarFase(cct, numexp);
				}
				else{ //Si estamos en el tr�mite de revisi�n del servicio
					
					//Enviamos el aviso al usuario que tramit� por �ltimo la factura
					//Obtenemos los datos de la revision de facturas
					String idResp = itemRevisarFactura.getString("COD_USUARIO_REVISION");
					String motivoRechazo = itemRevisarFactura.getString("MOTIVO");
					
					StringBuffer sbMessage = new StringBuffer();
					sbMessage.append("<b>Factura ");
					sbMessage.append(itemFactura.getString("RCF_NREG"));
					sbMessage.append(" rechazada desde el Servicio");
					sbMessage.append("</b><br/>Motivo: ");
					sbMessage.append(motivoRechazo);
					AvisosUtil.generarAviso(entitiesAPI, invesFlowAPI.getProcess(numexp).getInt("ID"), 
							numexp, sbMessage.toString(), idResp, cct);
									
					//No marcamos la factura como rechazada, si no que la volvemos a pasar a Intervenci�n o Compras
					//Recuperamos el �ltimo tr�mite que se cre�
					StringBuffer sbQuery = new StringBuffer();
					sbQuery.append("WHERE NUMEXP = '");
					sbQuery.append(numexp);
					sbQuery.append("' AND FECHA_CIERRE IS NOT NULL");
					//INICIO [dipucr-Felipe #491]
					sbQuery.append(" AND ID_TRAM_CTL NOT IN (SELECT ID FROM SPAC_CT_TRAMITES "
							+ "WHERE COD_TRAM = '" + DipucrFaceFacturasUtil.COD_TRAM_REVISION3 
							+ "' OR COD_TRAM = '" + DipucrFaceFacturasUtil.COD_TRAM_FIRMA + "')");
					//FIN [dipucr-Felipe #491]
					sbQuery.append(" ORDER BY FECHA_INICIO DESC");
					collection = entitiesAPI.queryEntities
					(
							ISPACEntities.DT_ID_TRAMITES,
							sbQuery.toString()
					);
					IItem itemUltimoTramite = (IItem) collection.iterator().next();
					int idTramCtl = itemUltimoTramite.getInt("ID_TRAM_CTL");
					String sCodUltimoTram = TramitesUtil.getCodTram(idTramCtl, cct);
					//Duplicamos el �ltimo tr�mite antes de la firma
					int idTramiteNuevo = TramitesUtil.crearTramite
							((ClientContext) cct, sCodUltimoTram, numexp);
					
					//Le copiamos el documento (s�lo si es el tr�mite de Compras)
					if (DipucrFaceFacturasUtil.COD_TRAM_REVISION2.equals(sCodUltimoTram)){
						IItem itemDocFactura = DocumentosUtil.getPrimerDocumentByNombre
								(rulectx.getNumExp(), rulectx, DOCUMENTO_FACTURA);
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
					
					//Ponemos el motivo de rechazo en las observaciones del tr�mite anterior
					String consulta = "WHERE ID_TRAM_EXP = " + itemTramiteActual.getKeyInt();
					IItemCollection collectionDTTrams = TramitesUtil.queryTramites(cct, consulta);
					IItem itemDTTramiteActual = (IItem) collectionDTTrams.iterator().next();
					String observaciones = itemDTTramiteActual.getString("OBSERVACIONES");
					
					if(!StringUtils.isEmpty(observaciones)) observaciones += "\n";
					else observaciones="";
					observaciones += "Rechazado: "+ motivoRechazo;

					if(observaciones.length()>254) observaciones = observaciones.substring(0,253);
					itemDTTramiteActual.set("OBSERVACIONES", observaciones);
					itemDTTramiteActual.store(cct);							
					
					//Limpiamos la pesta�a "Revisi�n eFactura" 
					itemRevisarFactura.set("CONFORMADA", "");
					itemRevisarFactura.set("COD_DEPARTAMENTO", "");
					itemRevisarFactura.set("DEPARTAMENTO", "");
					itemRevisarFactura.set("MOTIVO", "");
					itemRevisarFactura.store(cct);
					
					//Retrocedemos el estado de la factura en FACe
					DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, itemFactura, EstadosFace.VERIFICADA_RCF, 0, "");
					if (DipucrFaceFacturasUtil.COD_TRAM_REVISION1.equals(sCodUltimoTram)){
						DipucrFaceFacturasUtil.cambiarEstadoFactura(cct, itemFactura, EstadosFace.RECIBIDA_RCF, 0, "");
					}
				}
			}
		}
		catch (Exception e) {
        	logger.error("Error al revisar la factura en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al revisar la factura en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
