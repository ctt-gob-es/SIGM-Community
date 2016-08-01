package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.notificador.model.TerceroWS;
import es.jccm.notificador.ws.AplicacionesServiceProxy;

public class VerificarUsuarioComparece implements IRule{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(VerificarUsuarioComparece.class);
	
	@SuppressWarnings("rawtypes")
	List lParticipantes = new ArrayList();

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

        try {
        	//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //----------------------------------------------------------------------------------------------

	        //Obtengo los participantes del expediente.
	        
	        Iterator<?> itPariticipantes = lParticipantes.iterator();
			
			IItem partic = null;
			if(itPariticipantes != null){
			
				while(itPariticipantes.hasNext()){
					//Acceso al servicio web de comparece
		        	AplicacionesServiceProxy asp = new AplicacionesServiceProxy();
					partic = (IItem)itPariticipantes.next();
					String dni = partic.getString("NDOC");
					TerceroWS b = asp.consultarDNI("1", dni);
					if(b != null){
						partic.set("ROL", "COPR");
						partic.store(cct);
					}
					else{
						logger.info("No esta validado en el comparece. ");
					}
				}
			}

				
		} catch (RemoteException e) {
			logger.error("Se produjo una excepción. " + e.getMessage(), e);
			throw new ISPACRuleException("Se produjo una excepción. " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Se produjo una excepción. " + e.getMessage(), e);
			throw new ISPACRuleException("Se produjo una excepción. " + e.getMessage(), e);
		}
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}
}
