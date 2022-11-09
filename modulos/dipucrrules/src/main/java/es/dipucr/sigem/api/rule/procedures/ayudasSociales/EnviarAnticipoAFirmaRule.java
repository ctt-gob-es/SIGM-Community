package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.webempleado.services.ayudasSociales.AnticiposWSProxy;


/**
 * [eCenpri-Felipe #346]
 * Regla que crea el trámite de "Firmar anticipos"
 * @author Felipe
 * @since 28.06.2011
 */
public class EnviarAnticipoAFirmaRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(EnviarAnticipoAFirmaRule.class);

	protected static final String _DOC_INFORME_ANTICIPO = "Anticipo reintegrable";
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ICatalogAPI catalogAPI = invesFlowAPI.getCatalogAPI();
			ISignAPI signAPI = invesFlowAPI.getSignAPI();
			//*********************************************
			
			IItemCollection collection = null;
			String numexp = rulectx.getNumExp();
			
			//Obtenemos el objeto con los datos de la solicitud
			collection = entitiesAPI.getEntities("DPCR_ANTICIPOS", numexp);
			IItem itemSolicitudAnticipos = (IItem)collection.iterator().next();
			
			//El contexto viene vacío, tenemos que rellenarlo
			IProcess exp = invesFlowAPI.getProcess(numexp);
			int idProcedimiento = exp.getInt("ID_PCD");
            StateContext stateContext = cct.getStateContext();
            if (stateContext == null) {
                stateContext = new StateContext();
                ((ClientContext)cct).setStateContext(stateContext);
            }
            stateContext.setPcdId(idProcedimiento);
            stateContext.setProcessId(exp.getKeyInt());
            stateContext.setNumexp(numexp);
			
			//Generación del Informe de Anticipos
			IItem itemDocSolicitud = DocumentosUtil.generarDocumento(rulectx, _DOC_INFORME_ANTICIPO, _DOC_INFORME_ANTICIPO);
			int idDocumento = itemDocSolicitud.getKeyInt();
			
			//Obtención del circuito de firma de licencias
			collection = catalogAPI.getCtosFirmasProcedure(idProcedimiento);
			IItem itemCircuito = (IItem)collection.iterator().next();
			int idCircuito = itemCircuito.getInt("CFC:ID_CIRCUITO");
			
			//Mandamos a la firma el documento
			signAPI.initCircuitPortafirmas(idCircuito, idDocumento);//[dipucr-Felipe #1246]
			
			//Si todo ha ido bien creamos el anticipo pendiente en el portal
			AnticiposWSProxy wsAnticipos = new AnticiposWSProxy();
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			Date dFreg = itemExpediente.getDate("FREG");
			Calendar calendarFreg = Calendar.getInstance();
			calendarFreg.setTime(dFreg);
			wsAnticipos.crearAnticipoPendiente
				(itemExpediente.getString("NIFCIFTITULAR"), 
				 itemSolicitudAnticipos.getDouble("IMPORTE_TOTAL"),
				 itemSolicitudAnticipos.getDouble("IMPORTE_MES"), 
				 itemSolicitudAnticipos.getDouble("IMPORTE_ULTIMO_MES"),
				 itemSolicitudAnticipos.getInt("NUM_MESES"),
				 itemSolicitudAnticipos.getString("OBSERVACIONES"),
				 itemExpediente.getString("NREG"),
				 calendarFreg,
				 numexp
			);
		
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de los anticipos. " + e.getMessage(), e);
		}
		return null;
	}
	
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
