package es.dipucr.tablonEdictalUnico.rule;

import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.boe.www.ServicioNotificaciones.Respuesta;
import es.boe.www.ServicioNotificaciones.ServicioNotificacionesProxy;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.tablonEdictalUnico.commons.FuncionesComunes;
import es.dipucr.tablonEdictalUnico.commons.ServiciosWebTEUFunciones;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

public class RecibirAnuncioTablonEdictalBOERule implements IRule{
	
	public static final Logger logger = Logger
			.getLogger(RecibirAnuncioTablonEdictalBOERule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}


	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}


	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			String tablonEdictal_address = ServiciosWebTEUFunciones.getDireccionSW();
			ServicioNotificacionesProxy notificacion = new ServicioNotificacionesProxy(tablonEdictal_address);
			Iterator <IItem> itTEUBOE = ConsultasGenericasUtil.queryEntities(rulectx, "TABLON_EDICTAL_BOE_DATOS", "NUMEXP='"+rulectx.getNumExp()+"'");
			while(itTEUBOE.hasNext()){
				IItem iTEUBOE = itTEUBOE.next();
				if(iTEUBOE.getString("IDENTIFICADORANUNCIOBOE")!=null){
					Respuesta respuesta = notificacion.consultaAnuncio(iTEUBOE.getString("IDENTIFICADORANUNCIOBOE"));
					FuncionesComunes.crearDocInformacionEnvioTEU(rulectx, respuesta);
					iTEUBOE.set("TRAIDOANUNCIOTEU", "SI");
					iTEUBOE.store(rulectx.getClientContext());
				}
			}
		} catch (RemoteException e) {
			logger.error("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
