package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class EliminarSpacDtDocumentosInfTecniRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(EliminarSpacDtDocumentosInfTecniRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //----------------------------------------------------------------------------------------------
			
			IItemCollection docDecretoCollection = DocumentosUtil.queryDocumentos(cct, " WHERE NUMEXP='"+rulectx.getNumExp()+"' AND DESCRIPCION='Informe Técnico' AND INFOPAG_RDE IS null");
    		for(Object docDecretoO : docDecretoCollection.toList()){
    			IItem docDecreto = (IItem) docDecretoO;
    			docDecreto.delete(cct);
    		}
			
    	}
		catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	logger.error("No se ha podido inicializar la propuesta. Numexp. "+rulectx.getNumExp()+" Error. "+e.getMessage(),e);
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta. Numexp. "+rulectx.getNumExp()+" Error. "+e.getMessage(),e);
        	
        }
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
