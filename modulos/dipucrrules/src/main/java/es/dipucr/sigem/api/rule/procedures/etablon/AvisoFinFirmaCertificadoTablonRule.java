package es.dipucr.sigem.api.rule.procedures.etablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;


/**
 * [eCenpri-Felipe ticket #504]
 * Regla que avisa del resultado de la firma del certificado de publicación
 * @author Felipe
 * @since 08.03.2012
 */
public class AvisoFinFirmaCertificadoTablonRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(AvisoFinFirmaCertificadoTablonRule.class);

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

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			//Vemos si la solicitud ha sido firmada o rechazada
			String numexp = rulectx.getNumExp();
			IItem itemDocumento = (IItem) DocumentosUtil.getPrimerDocumentByNombre(numexp, rulectx, _DOC_CERTIFICADO);
			if(itemDocumento != null){
				String estado = itemDocumento.getString("ESTADOFIRMA");
				
				IItemCollection collection = entitiesAPI.getEntities("ETABLON_PUBLICACION", numexp);
				IItem itemPublicacion = (IItem)collection.iterator().next();
				
				String strMotivo = null;
				
				if (estado.equals(SignStatesConstants.FIRMADO)){
					TablonUtils.generarAvisoUsuario(rulectx, "eTablón: Diligencia de publicación firmada", 
							strMotivo, itemDocumento, itemPublicacion);
				}
	//			else if (estado.equals(SignStatesConstants.FIRMADO_CON_REPAROS)){
	//				bFirmado = true;
	//				strMotivo = itemDocumento.getString("MOTIVO_REPARO");
	//			}
				else if (estado.equals(SignStatesConstants.RECHAZADO)){
					strMotivo = itemDocumento.getString("MOTIVO_RECHAZO");
					TablonUtils.generarAvisoUsuario(rulectx, "eTablón: Diligencia de publicación rechazada", 
							strMotivo, itemDocumento, itemPublicacion);
				}
			}
		}
		catch (Exception e) {
        	logger.error("Error al avisar de la publicación al usuario en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al avisar de la publicación al usuario en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
