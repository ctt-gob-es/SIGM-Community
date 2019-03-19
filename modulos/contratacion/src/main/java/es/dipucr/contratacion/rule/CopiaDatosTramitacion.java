package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.objeto.sw.DatosTramitacion;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;

public class CopiaDatosTramitacion  implements IRule{
	
	protected static final Logger logger = Logger.getLogger(CopiaDatosTramitacion.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		boolean correcto = false;
		 try{
	 		//--------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------
	
			DatosTramitacion datosTramitacion = DipucrFuncionesComunesSW.getDatosTramitacion(cct, rulectx.getNumExp(), null);
			
			/**
			 * Si es = a null quiere decir que no ha insertado todavía en bbdd dos datos de la entidad del procedimiento de contratacion
			 * */
			
			if(datosTramitacion!=null){
				String consulta = "WHERE NUMEXP_HIJO = '"+rulectx.getNumExp()+"' AND RELACION='Petición Contrato'";
				IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", consulta);
				Iterator<IItem> it = collection.iterator(); 
				
				String numexpPeticion = "";
				if(it.hasNext()){
					IItem expRelacionados = (IItem)it.next();
					numexpPeticion = expRelacionados.getString("NUMEXP_PADRE");				
					}
		        
		        correcto = DipucrFuncionesComunesSW.setDatosTramitacion(numexpPeticion, datosTramitacion, cct, rulectx.getNumExp());
			}
	        
	        
				  
          
		 }catch(ISPACRuleException e){
			 logger.error("Error . " + e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		
        } catch (ISPACException e) {
        	logger.error("Error. " + e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
	
		}
		 
		return new Boolean(correcto);
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

}
