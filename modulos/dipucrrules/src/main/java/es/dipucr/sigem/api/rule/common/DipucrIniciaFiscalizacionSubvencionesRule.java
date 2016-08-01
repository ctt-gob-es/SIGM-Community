package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.cartaDigital.DipucrIniciaCartaDigital;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ProcedimientosUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class DipucrIniciaFiscalizacionSubvencionesRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrIniciaCartaDigital.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			logger.info("INICIO - " + this.getClass().getName());
			/**Obtengo el organo resolutor del expediente**/
			int idCtProcedimientoNuevo = 0;
			IItemCollection itColExp = SecretariaUtil.buscaTodosExp(rulectx.getClientContext(), rulectx.getNumExp(),"WHERE COD_PCD LIKE 'FISCAL-SUBVEN'");
			if(itColExp.iterator()!=null){
				IItem it_ct_procedimiento = (IItem) itColExp.iterator().next();
				if(it_ct_procedimiento!=null){
					String nombreCertifObra = it_ct_procedimiento.getString("NOMBRE");	
				    IItem procArqObra = ProcedimientosUtil.getProcedimientoByCodNombre(rulectx, nombreCertifObra);  
				    if(procArqObra!=null){
				    	idCtProcedimientoNuevo = procArqObra.getInt("ID");
				    }
				}
			    
			}
			ExpedientesRelacionadosUtil.iniciaExpedienteRelacionadoHijo(rulectx.getClientContext(), idCtProcedimientoNuevo, rulectx.getNumExp(), "Fiscalización de subvenciones", true, null);		
		} catch (ISPACException e) { 
			logger.error("Error al generar el expediente de fiscalizacion. Numexp. "+rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el expediente de fiscalizacion. Numexp. "+rulectx.getNumExp() + " - " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al generar el expediente de fiscalizacion. Numexp. "+rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el expediente de fiscalizacion. Numexp. "+rulectx.getNumExp() + " - " + e.getMessage(), e);
		}
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	
}