package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class TerminaTramiteInfDevolucionFianzaRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(TerminaTramiteInfDevolucionFianzaRule.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {	
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			//Obtengo el numero de expediente del procedimiento generico
			String sQuery = "WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"' AND RELACION='Petición Contrato' ORDER BY ID ASC";
    		IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, sQuery);
	        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
	        String numexpPadreContratacion = null;
	        while (exp_relacionadosIterator.hasNext()){
	        	numexpPadreContratacion = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");
	        	
	        	sQuery = "WHERE NUMEXP_PADRE='"+numexpPadreContratacion+"' AND RELACION='Devolucion Fianza' ORDER BY ID ASC";
	    		exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, sQuery);
		        exp_relacionadosIterator = exp_relacionadosCollection.iterator();
		        while (exp_relacionadosIterator.hasNext()){
		        	numexpPadreContratacion = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");
		        }
	        }
			
			//Obtiene el número de Fase Adjudicación
	        sQuery = "WHERE NUMEXP='" + numexpPadreContratacion + "'";
			logger.warn("strQueryAux "+sQuery);
			IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, sQuery);
			Iterator<IItem> itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				return new Boolean(false);
			}
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			logger.warn("idFase "+idFase);
			
			//Se busca el id del tramite
			sQuery = "WHERE ID = "+idFase;
			 IItemCollection iFases = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, sQuery);
			 Iterator<IItem> IFases = iFases.iterator();
			 int idFaseProp=0;
			 
			 while (IFases.hasNext()){
				 IItem fase = (IItem)IFases.next();
				 idFaseProp = fase.getInt("ID_FASE");
			 }
			
			//Calculo el tramite que le corresponde
			 sQuery = "WHERE ID_FASE = "+idFaseProp+" AND NOMBRE='Informe Técnico'";
			 IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, sQuery);
			 Iterator<IItem> ITramiteProp = iTramiteProp.iterator();
			 int idTramite=0;
			 while (ITramiteProp.hasNext()){
				 IItem tramite = (IItem)ITramiteProp.next();
				 idTramite = tramite.getInt("ID");
			 }
			
			//Creo el tramite Informe Jurídico del Procedimiento genérico
			int idTramiteInfTecnico = transaction.createTask(idFase, idTramite);
			logger.warn("idTramitePropuesta "+idTramiteInfTecnico);
			
			logger.warn("Creado el tramite");
			
			IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE="+rulectx.getTaskId()+"", "FDOC DESC");
			IItem contenidoInforTecn = null;
			if (documentsCollection!=null && documentsCollection.next()){
				contenidoInforTecn = (IItem)documentsCollection.iterator().next();
			}else{
				throw new ISPACInfo("No se ha encontrado el documento de Documentación de la propuesta");
			}
			
			Object connectorSession = genDocAPI.createConnectorSession();
			
			//Documento nuevo de infopag
			IItem nuevoDocumento = (IItem)genDocAPI.createTaskDocument(idTramiteInfTecnico, contenidoInforTecn.getInt("ID_TPDOC"));
//			String infopag = contenidoInforTecn.getString("INFOPAG");
//			File fileInfTecn = DipucrCommonFunctions.getFile(rulectx, infopag);
//			FileInputStream inGD = new FileInputStream(fileInfTecn);
//			int docId = nuevoDocumento.getInt("ID");	
//			IItem entityDoc = genDocAPI.attachTaskInputStream(connectorSession, idTramiteInfTecnico, docId, inGD, (int)fileInfTecn.length(), "application/vnd.oasis.opendocument.text", "Informe TécnicoRDE");

			//Documento nuevo de infopagRde
			IItem nuevoDocumentoRde = (IItem)genDocAPI.createTaskDocument(idTramiteInfTecnico, contenidoInforTecn.getInt("ID_TPDOC"));			
			String infopag_rde = contenidoInforTecn.getString("INFOPAG_RDE");
			File fileInfTecnRde = DocumentosUtil.getFile(cct, infopag_rde, null, null);
			FileInputStream inGDRde = new FileInputStream(fileInfTecnRde);
			int docIdRde = nuevoDocumentoRde.getInt("ID");
			IItem entityDocRde = genDocAPI.attachTaskInputStream(connectorSession, idTramiteInfTecnico, docIdRde, inGDRde, (int)fileInfTecnRde.length(), "application/pdf", "Informe Técnico");
			
			
			
			String descripcion = contenidoInforTecn.getString("DESCRIPCION");
			nuevoDocumento.set("INFOPAG", contenidoInforTecn.getString("INFOPAG"));
			nuevoDocumento.set("DESCRIPCION", descripcion);
			nuevoDocumento.set("EXTENSION", contenidoInforTecn.getString("EXTENSION"));
			nuevoDocumento.set("EXTENSION_RDE", contenidoInforTecn.getString("EXTENSION_RDE"));
			nuevoDocumento.set("INFOPAG_RDE", entityDocRde.getString("INFOPAG"));
			nuevoDocumento.set("FAPROBACION", contenidoInforTecn.getDate("FAPROBACION"));
			nuevoDocumento.set("ESTADOFIRMA", contenidoInforTecn.getString("ESTADOFIRMA"));
			nuevoDocumento.store(cct);
			
			//elimino el apunte en la bbdd al nuevo documento que se ha creado
			//con ello dejo el documento en el repositorio
			DbCnt cnt = cct.getConnection();
			//cnt.openTX();
			String query = "DELETE FROM SPAC_DT_DOCUMENTOS WHERE ID_TRAMITE="+idTramiteInfTecnico+" AND NUMEXP='"+numexpPadreContratacion+"' AND INFOPAG_RDE IS null";
		 	cnt.execute(query);	
				
			//cnt.closeTX(true);
			
			
    	 }
    	catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta.",e);
        }
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

}
