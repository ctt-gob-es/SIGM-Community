package es.dipucr.sigem.api.rule.common.tablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
;

public class DipucrIniciaSolicitudPublicacionTablonEdictos implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrIniciaSolicitudPublicacionTablonEdictos.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try {			
			logger.info("INICIO - " + this.getClass().getName());
			numexp = rulectx.getNumExp();
			
			IClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
						
			IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE = '"+rulectx.getTaskId() + "'", "");
	        Iterator it = collection.iterator();
	        while (it.hasNext()){
	        	IItem doc = (IItem)it.next();
	        	generaAnuncio(rulectx, doc);
	        }				
			logger.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) { 
			logger.error("Error al iniciar el expediente de publicación en el tablón. Expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar el expediente de publicación en el tablón. Expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al iniciar el expediente de publicación en el tablón. Expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar el expediente de publicación en el tablón. Expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private void generaAnuncio(IRuleContext rulectx, IItem doc) throws Exception {
		
		IClientContext cct = (ClientContext) rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		ITXTransaction tx = invesflowAPI.getTransactionAPI();
		
		String numExpTablon = ExpedientesRelacionadosUtil.iniciaExpedienteHijoTablonEdictos(cct, rulectx.getNumExp(), false, true);
		
		if(StringUtils.isNotEmpty(numExpTablon)){
			//Rellenamos la pestaña
			IItem eTablon = null;
			IItemCollection eTablonCollection = entitiesAPI.getEntities("ETABLON_PUBLICACION", numExpTablon);
			Iterator eTablonIterator = eTablonCollection.iterator();
			if(eTablonIterator.hasNext()){
				eTablon = (IItem) eTablonIterator.next();
			}
			else{
				eTablon = entitiesAPI.createEntity("ETABLON_PUBLICACION", numExpTablon);
			}
			
			Iterator eTablonPadreIterator = entitiesAPI.getEntities("ETABLON_PUBLICACION", rulectx.getNumExp()).iterator();
			
			if(eTablonPadreIterator.hasNext()){
				IItem eTablonPadre = (IItem)eTablonPadreIterator.next();
				
				eTablon.set("TITULO", eTablonPadre.getString("TITULO"));			
				eTablon.set("DESCRIPCION", eTablonPadre.getString("DESCRIPCION"));
				eTablon.set("COD_CATEGORIA", eTablonPadre.getString("COD_CATEGORIA"));
				eTablon.set("CATEGORIA", eTablonPadre.getString("CATEGORIA"));			
				eTablon.set("COD_SERVICIO", eTablonPadre.getString("COD_SERVICIO"));
				eTablon.set("SERVICIO", eTablonPadre.getString("SERVICIO"));
				eTablon.set("FECHA_INI_VIGENCIA", eTablonPadre.getDate("FECHA_INI_VIGENCIA"));
				eTablon.set("FECHA_FIN_VIGENCIA", eTablonPadre.getDate("FECHA_FIN_VIGENCIA"));
				eTablon.set("SERVICIO_OTROS", eTablonPadre.getString("SERVICIO_OTROS"));			
				eTablon.set("CATEGORIA_OTROS", eTablonPadre.getString("CATEGORIA_OTROS"));
				
				eTablon.store(cct);
			}
			
			//Creamos el trámite
			IProcess itemProcess = invesflowAPI.getProcess(numExpTablon);
	    	int idProcess = itemProcess.getInt("ID");
			IItemCollection collExpsAux = invesflowAPI.getStagesProcess(idProcess);
			Iterator itExpsAux = collExpsAux.iterator();
	
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			int idFaseExp = iExpedienteAux.getInt("ID_FASE");
			
			String strQueryAux = "WHERE ID_FASE = "+idFaseExp+" ORDER BY ORDEN ASC";
			IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQueryAux);
			Iterator ITramiteProp = iTramiteProp.iterator();
			int idTramite=0;
			IItem tramite = (IItem)ITramiteProp.next();
			idTramite = tramite.getInt("ID");

			int idTramiteTablon = tx.createTask(idFase, idTramite);			
			
			//Pasamos el documento
			String infopag = doc.getString("INFOPAG");
			String infopag_rde = doc.getString("INFOPAG_RDE");
			
			if(infopag_rde != null && !infopag_rde.equals(""))
				infopag = infopag_rde;
	
			String extension = doc.getString("EXTENSION");
			String extension_rde = doc.getString("EXTENSION_RDE");
			
			if(extension_rde != null && !extension_rde.equals(""))
				extension = extension_rde;
			
			
			String nombre = doc.getString("DESCRIPCION");
			
			File documento = DocumentosUtil.getFile(cct, infopag, null, null);
			
			int tpdoc = DocumentosUtil.getTipoDoc(cct, "eTablon - Publicación", DocumentosUtil.BUSQUEDA_EXACTA, false);
			
			IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, idTramiteTablon, tpdoc, nombre, documento, extension);
			
			entityDoc.set("DESTINO", doc.getString("DESTINO"));
			entityDoc.set("DESTINO_ID", doc.getString("DESTINO_ID"));
			entityDoc.set("NREG", doc.getString("NREG"));
			entityDoc.set("FREG", doc.getDate("FREG"));
			entityDoc.set("ORIGEN", doc.getString("ORIGEN"));
			entityDoc.set("FAPROBACION", doc.getDate("FAPROBACION"));
			
			entityDoc.store(cct);
			
			if(documento != null && documento.exists()) documento.delete();
		}
	}
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			cct.setSsVariable("ANIO", ""+Calendar.getInstance().get(Calendar.YEAR));
		} catch (ISPACException e) {
			logger.error("Error al setear las variables de sesión. " + e.getMessage(), e);
		}
	}

	public void deleteSsVariables(IClientContext cct) {	
		try {
			cct.deleteSsVariable("ANIO");
		} catch (ISPACException e) {
			logger.error("Error al borrar las variables de sesión. " + e.getMessage(), e);			
		}
	}	
}