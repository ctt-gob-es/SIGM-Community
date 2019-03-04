package es.dipucr.contratacion.menor;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.objeto.sw.Campo;
import es.dipucr.contratacion.objeto.sw.DatosContrato;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.ProcedimientosUtil;

public class DipucrIniciaCartaDigitalMenor implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(DipucrIniciaCartaDigitalMenor.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			generaCarta(rulectx);
			
		} catch (ISPACException e) { 
			LOGGER.error("Error al generar la comunicación administrativa. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al generar la comunicación administrativa. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. " + e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void generaCarta(IRuleContext rulectx) throws Exception {
		
		IClientContext cct = (ClientContext) rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		try{
			DatosContrato datosContrato = DipucrFuncionesComunesSW.getDatosContrato(cct, rulectx.getNumExp());
			Campo [] vCampo = datosContrato.getCpv();
			for(int i=0; i < vCampo.length; i++){
				Campo cpv = vCampo[i];
				String numExpHijo = "";
				String expPadre = "";
				IItem procedimientoMenor = ProcedimientosUtil.getProcedimientoByConsulta(rulectx, "WHERE TITULO LIKE '%"+cpv.getId()+"%'");
				
				if(procedimientoMenor != null){
					if(procedimientoMenor.getInt("ID")>0){
						LOGGER.warn("CPV. "+cpv.getId()+" - "+cpv.getValor()+"el código de procedimiento que le corresponde es"+procedimientoMenor.getString("COD_PCD"));
						
						IItem expediente = ExpedientesRelacionadosUtil.iniciaExpediente(cct, procedimientoMenor.getInt("ID"), ExpedientesUtil.getExpediente(cct, rulectx.getNumExp()));
						if(expediente!=null){
							if(expediente.getString("NUMEXP")!=null)numExpHijo = expediente.getString("NUMEXP");							
							
							String consulta = "WHERE NUMEXP LIKE '%2016%' AND CODPROCEDIMIENTO='"+procedimientoMenor.getString("COD_PCD")+"'";
							IItemCollection colExp = ExpedientesUtil.queryExpedientes(cct, consulta);
							Iterator<IItem> itExpe = colExp.iterator();
							LOGGER.warn("Los expedientes del 2016 con ese código de procedimiento son: "+colExp.toList().size());
							if(itExpe.hasNext()){
								IItem exp = itExpe.next();
								expPadre = exp.getString("NUMEXP");
							}
							else{
								LOGGER.error("No existe ningún expediente del 2016 con este CPV. "+cpv.getId()+" - "+cpv.getValor()+" por lo tanto iniciamos una carta digital normal");
								numExpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteHijoCartaDigital(cct, rulectx.getNumExp(), true, true);
							}
						}
						else{
							LOGGER.error("No existe ningún expediente con este CPV. "+cpv.getId()+" - "+cpv.getValor()+" por lo tanto iniciamos una carta digital normal");
							numExpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteHijoCartaDigital(cct, rulectx.getNumExp(), true, true);
						}		
						
					}
					else{
						LOGGER.error("No existe ningún procedimiento con este CPV. "+cpv.getId()+" - "+cpv.getValor()+" por lo tanto iniciamos una carta digital normal");
						numExpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteHijoCartaDigital(cct, rulectx.getNumExp(), true, true);
					}
				}
				else{
					LOGGER.error("No existe ningún procedimiento con este CPV. "+cpv.getId()+" - "+cpv.getValor()+" por lo tanto iniciamos una carta digital normal");
					numExpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteHijoCartaDigital(cct, rulectx.getNumExp(), true, true);
				}
				if(StringUtils.isNotEmpty(numExpHijo)){
					if(expPadre!=null && !expPadre.equals("")){
						ParticipantesUtil.importarParticipantes((ClientContext)cct, entitiesAPI, expPadre, numExpHijo);
						ExpedientesRelacionadosUtil.relacionaExpedientes(cct, rulectx.getNumExp(), numExpHijo, "Carta Digital - "+cpv.getId());
					}
					else{
						LOGGER.error("No se ha podido importar participantes, ya que no existe ningún expedientes con este CPV. "+cpv.getId()+" - "+cpv.getValor()+" por lo tanto iniciamos una carta digital normal");
					}
					//ExpedientesRelacionadosUtil.relacionaExpedientes(cct, rulectx.getNumExp(), numExpHijo, "Carta Digital - "+cpv.getId());
				}
				
			}		
		}
		catch(Exception e){
			LOGGER.error("Error al iniciar la carta digital del contrato menor. " + e.getMessage(), e);
			throw new ISPACException("Error al iniciar la carta digital del contrato menor. " + e.getMessage(), e);
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	

}
