package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.tablon.services.TablonWSProxy;

public class EnviarPublicacionTablonGenericaRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(EnviarPublicacionTablonGenericaRule.class);
	
	public static String _MENSAJE_FIRMADO = "Publicación aceptada";
	public static String _MENSAJE_RECHAZADO = "Documento Rechazado: ";
	protected String nombreDocumento = "";
	protected String titulo = "";
	protected String descripcion = "";
	protected String codCategoria = "";
	protected String codServicio = "";
	protected Calendar calendarIniVigencia = null;;
	protected Calendar calendarFinVigencia = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			//Comprobamos que haya documento y este este firmado
			//*********************************************
			//Vemos si la solicitud ha sido firmada o rechazada
			IItem itemDocumento = DocumentosUtil.getPrimerDocumentByNombre(rulectx.getNumExp(), rulectx, nombreDocumento);
			
			if (null == itemDocumento){
				rulectx.setInfoMessage("No se puede terminar trámite. Es necesario " +
					"que exista el documento "+nombreDocumento+".");
				return false;
			}
			
			String estado = itemDocumento.getString("ESTADOFIRMA");
			if (estado.equals(SignStatesConstants.SIN_FIRMA)){
				rulectx.setInfoMessage("No se puede terminar trámite. " +
						"El documento de publicación debe estar firmado.");
				return false;
			}
			
			return true;
		}
		catch (Exception e) {
			logger.error("Error al validar si la publicación del tablón está firmada. Expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al validar si la publicación del tablón está firmada. Expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
	}
	
	/**
	 * Generación de la fase, el trámite y envío al Jefe de departamento para firma
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		String numexp = "";

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			//*********************************************
			//Envío de la publicación a eTablón
			//*********************************************
			//Vemos si la solicitud ha sido firmada o rechazada
			numexp = rulectx.getNumExp();
			IItem itemDocumento = DocumentosUtil.getPrimerDocumentByNombre(numexp, rulectx, nombreDocumento);
			int idDoc = itemDocumento.getKeyInt();
			boolean bWScorrecto = false;
			
			//Sacamos la fecha de firma
			Date dFechaFirma = itemDocumento.getDate("FFIRMA");
			Calendar calendarFechaFirma = Calendar.getInstance();
			calendarFechaFirma.setTime(dFechaFirma);
			
			//Obtenemos el CVE
			String cve = itemDocumento.getString("COD_COTEJO");
			
			//Id transacción
			String strQuery = "WHERE ID_DOCUMENTO = " + idDoc;
			IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_ID_TRANSACCION_FIRMA, strQuery);
			String idTransaccion = "";
			String hash = "";
			if (collection.next()){
				IItem itemTransaccion = (IItem)collection.iterator().next();
				idTransaccion = itemTransaccion.getString("ID_TRANSACCION");
				hash = itemTransaccion.getString("HASH");
			}
			
			//Obtenemos el fichero
			String strInfopagRde = itemDocumento.getString("INFOPAG_RDE");
			File filePublicacion = DocumentosUtil.getFile(cct, strInfopagRde, null, null);
			DataHandler dhPublicacion = new DataHandler(new FileDataSource(filePublicacion));
			
			//Hacemos la petición al servicio web
			TablonWSProxy wsTablon = new TablonWSProxy();
			String codEntidad = EntidadesAdmUtil.obtenerEntidad((ClientContext)cct);
			bWScorrecto = wsTablon.insertarPublicacion(codEntidad, titulo, descripcion, calendarFechaFirma, codServicio, 
					codCategoria, calendarIniVigencia, calendarFinVigencia, cve, hash, idTransaccion, numexp, 
					null, null, dhPublicacion);
			
			if (!bWScorrecto){
				logger.error("Error en el servicio web de la aplicación eTablón. Expediente: " + numexp + ". ");
				throw new ISPACRuleException("Error en el servicio web de la aplicación eTablón. Expediente: " + numexp + ". ");
			}
		}
		catch (Exception e) {
			logger.error("Error al insertar la publicación en el tablón. Expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al insertar la publicación en el tablón.Expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
