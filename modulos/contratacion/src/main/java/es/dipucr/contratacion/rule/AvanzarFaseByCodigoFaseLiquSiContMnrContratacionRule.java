package es.dipucr.contratacion.rule;

import es.dipucr.contratacion.objeto.sw.DatosContrato;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

/**
 * Ticket #504 - Avanzar la fase del expediente
 * @author Felipe-ecenpri
 * @since 04.04.2012
 */
public class AvanzarFaseByCodigoFaseLiquSiContMnrContratacionRule implements IRule {
	
	private static final String CODIGO_AVANZAR_FASE = "fas-liq-cont";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
    	boolean rellenaProcContr = false;
    	DatosContrato datosContrato = DipucrFuncionesComunesSW.getDatosContrato(rulectx.getClientContext(), rulectx.getNumExp());
    	
		if(datosContrato!=null && datosContrato.getProcedimientoContratacion()!=null){
			rellenaProcContr = true;
		}
		
    	return rellenaProcContr;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {        
    	String numexp = rulectx.getNumExp();    	
    	boolean encontradoFase = false;
    	
    	try{ 
    		ClientContext cct = (ClientContext) rulectx.getClientContext();
    		DatosContrato datosContrato = DipucrFuncionesComunesSW.getDatosContrato(rulectx.getClientContext(), numexp);
    		
    		if(datosContrato!=null && datosContrato.getProcedimientoContratacion()!=null){
    			//6 - Contrato Menor
    			if(datosContrato.getProcedimientoContratacion().getId().equals("6")){    				

    				IStage fase = ExpedientesUtil.avanzarFase(cct, numexp);
    				
    	        	while(fase!=null && !encontradoFase){
    	        		int id_fase = fase.getInt("ID_FASE");
    	        		
    		        	if(id_fase > 0){
    		        		IItem spac_p_fases = FasesUtil.getSpacPFasesById(rulectx, id_fase);
    		        		
    		        		if(spac_p_fases!=null){
    		        			int id_ctfase = 0;
    		        			
    		        			if(spac_p_fases.getInt("ID_CTFASE")>0){
    		        				id_ctfase = spac_p_fases.getInt("ID_CTFASE");
    		        				
    		        				if(id_ctfase > 0){
    		        					String codigoSiguiente = FasesUtil.getCodFaseSpacCTFasesById(cct, id_ctfase);
    		        					
    		        					if(CODIGO_AVANZAR_FASE.equals(codigoSiguiente)){
    		        						encontradoFase = true;
    		        						ExpedientesUtil.retrocederFase(cct, numexp);
    		        						
    		        					} else {
    		        						fase = ExpedientesUtil.avanzarFase(cct, numexp);
    		        					}
    		        				}
    		        			}
    		        		}	        		
    		        	}	        	
    		        }
    			} else{
    				ExpedientesUtil.avanzarFase(cct, numexp);
    			}
    		}
	        	        
	        return true;
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al avanzar fase del expediente. Numexp:" + numexp+ " - "+e.getMessage(), e);
	    } 
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException
    {
    }

}
