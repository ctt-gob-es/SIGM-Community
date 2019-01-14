package es.dipucr.contratacion.rule.petSuministro;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import es.dipucr.contratacion.objeto.DatosContrato;

public class NoAvanzarFaseSiNoRellenadoDatosContratoRule implements IRule{
	
	protected static final Logger logger = Logger.getLogger(NoAvanzarFaseSiNoRellenadoDatosContratoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean avanzarFase = true;
		try{
			logger.warn("INICIO - "+this.getClass().getName());
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------
			DatosContrato datosContato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
			if(datosContato==null){
				avanzarFase = false;
				rulectx.setInfoMessage("Falta por rellenar la entidad 'Datos del Contrato'");
			}
			else{
				if(datosContato.getNumContrato()==null){
					avanzarFase = false;
					rulectx.setInfoMessage("Falta por rellenar la entidad 'Datos del Contrato'");
				}
			}
		}
		catch (Exception e) 
		{
	        throw new ISPACRuleException("Error al comprobar el estado de la fase de Peticion de suministro", e);
	    } 
		return avanzarFase;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
