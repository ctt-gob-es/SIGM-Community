package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class ComprobarUsuarioCompareceExpRelacionadosRule extends VerificarUsuarioComparece{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(ComprobarUsuarioCompareceExpRelacionadosRule.class);


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {	

        try {
        	//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        //	Obtengo los participantes del expediente.			
    		IItemCollection collection = DocumentosUtil.getDocumentsByNombre(rulectx.getNumExp(), rulectx, "Propuesta", "DESCRIPCION");
    		
    		Vector <IItem> vPropuesta = SecretariaUtil.orderPropuestas(collection);
    		
    		if(vPropuesta != null){
	 	        for( int i = 0; i < vPropuesta.size(); i++) {
	 	        	if(vPropuesta.get(i)!=null){
	 	        		
	 	        		IItem item = ((IItem)vPropuesta.get(i));
		 	        	String descripcion = item.getString("DESCRIPCION");
		 	        	
		 	    		//Sacar el expediente de esa propuesta
		 	    		String numexp_origen=sacarNumExp(descripcion);
		 	    		
		 	    		//Se genera una notificación por cada participante de la propuesta
			        	IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numexp_origen, "ROL != 'TRAS' OR ROL IS NULL", "ID");
			        	Iterator iPart = participantes.iterator();
			        	if(iPart != null){
			        		while(iPart.hasNext()){
			        			lParticipantes.add((IItem)iPart.next());
			        		}
			        	}		 	    		
	 	        	}
	 	        }
    		}			
        } catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		return new Boolean(true);
	}	

	private String sacarNumExp(String descripcion) throws ISPACRuleException {
		String exp = "";
		try{
        	int pos = descripcion.indexOf(", numexp=");
        	String res = descripcion.substring(pos, descripcion.length());
        	descripcion = res.replaceFirst(", numexp=", "");
        	
        	exp = descripcion.substring(0, descripcion.length());
		}catch(Exception e)
		{
			logger.error(e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException(e);
        }
		return exp;
	}
}
