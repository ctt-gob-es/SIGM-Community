package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import es.dipucr.sigem.api.rule.common.avisos.AvisoAnuncio;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

/**
 * [ecenpri-Felipe Ticket #39] Nuevo procedimiento Propuesta de Solicitud de Anuncio
 * @since 04.08.2010
 * @author Felipe
 */
public class CrearTramitePreparacionAnuncioRule implements IRule 
{
	public static String _COD_PREPARACION = "PREP_ANUNCIO";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		//Declaraciones
		int idTask = Integer.MIN_VALUE;
		String sResponsable = null;
		
		try{
			
			//INICIO [dipucr-Felipe #1199] Comprobamos que el trámite 
			//no exista ya, por ejemplo, al retroceder la fase
			IItem itemCtTramite = TramitesUtil.getTramiteByCode(rulectx, _COD_PREPARACION);
			String nombreTramite = itemCtTramite.getString("NOMBRE");
			
			if (!TramitesUtil.existeTramite(rulectx, nombreTramite)){
				//FIN [dipucr-Felipe #1199]
			
				//Generamos el trámite
				idTask = TramitesUtil.crearTramite(_COD_PREPARACION, rulectx);
				
				//Tenemos que generar el aviso por código pues cuando se llama a createTask
				//no tiene todavía responsable y la regla AvisoNuevoTramite falla
				ClientContext cct = (ClientContext) rulectx.getClientContext();
		        IInvesflowAPI invesFlowAPI = cct.getAPI();
				IItem itemProcedure = invesFlowAPI.getProcedure(rulectx.getProcedureId());
				sResponsable = itemProcedure.getString("ID_RESP");
				
				//Generamos el aviso
				AvisoAnuncio.generarAvisoAnuncio(rulectx, idTask, sResponsable);
			}
		}
		catch (Exception e) {
			
			throw new ISPACRuleException(e);
		}
		
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
