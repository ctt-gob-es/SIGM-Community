package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.common.ServiciosWebContratacionFunciones;
import es.dipucr.contratacion.services.PlataformaContratacionProxy;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

public class PruebaConsultarDatosAltaRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(AnuncioLicitacionRule.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			// --------------------------------------------------------------------			
		
			PlataformaContratacionProxy platContratacion = new PlataformaContratacionProxy(ServiciosWebContratacionFunciones.getDireccionSW());
			
			//Petición
			String publishedByUser = UsuariosUtil.getDni(cct);
			
			//Resultado resultado = platContratacion.consultarDatosAlta("005", rulectx.getNumExp(), publishedByUser);
			
			logger.warn("TERMINADO");
			
			
//		} catch (RemoteException e) {
//			logger.error("ERROR"+ e.getMessage());
//			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} 

		return new Boolean (true);
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

}
