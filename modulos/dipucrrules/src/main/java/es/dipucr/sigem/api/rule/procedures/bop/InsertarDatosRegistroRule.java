package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [eCenpri-Felipe #489]
 * @author Felipe
 * @since 27.10.11
 */
public class InsertarDatosRegistroRule extends GenerateLiquidacionRecibos implements IRule 
{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        
	        String numexp = rulectx.getNumExp();
	        
	        //Busca el infopag del último documento del trámite actual
	        IItemCollection collection = entitiesAPI.getDocuments(numexp, "ID_TRAMITE = '" + rulectx.getTaskId() + "'", "FDOC DESC");

	        Iterator<?> it = collection.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
        	IItem itemDoc = (IItem)it.next();
        	
        	//Solo seguimos si se ha usado una de las plantillas de notificación de control
        	String strTipoDoc = itemDoc.getString("NOMBRE");
        	String strTemplateName = itemDoc.getString("DESCRIPCION");
        	if (!strTipoDoc.equals(Constants.BOP._TIPODOC_NOTIFCONTROL))
        	{
	        	return new Boolean(false);
        	}
        	
        	//Cambiamos el nombre del documento y le ponemos el destino
	        IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
        	String strNombreInteresado = itemExpediente.getString("IDENTIDADTITULAR");
        	itemDoc.set("DESCRIPCION", strTemplateName + " - " + strNombreInteresado);
        	itemDoc.set("DESTINO", strNombreInteresado);
        	itemDoc.store(cct);
        	
        	//Importamos el participante
        	String nifcifInteresado = itemExpediente.getString("NIFCIFTITULAR");
        	//[eCenpri-Felipe #510]
        	//Sólo insertamos el participante si su CIF/NIF no es nulo
        	if (StringUtils.isNotEmpty(nifcifInteresado)){
	        	ParticipantesUtil.insertarParticipanteByNIF(rulectx, rulectx.getNumExp(), nifcifInteresado,
						ParticipantesUtil._TIPO_INTERESADO, ParticipantesUtil._TIPO_PERSONA_FISICA, "");
        	}
        	
        	return new Boolean(true);
		
		} catch (Exception e) {
    	
			throw new ISPACRuleException("Error al insertar los datos para registrar.", e);
		}  
		
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
