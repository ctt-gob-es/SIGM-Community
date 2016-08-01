package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class BusquedaDocValoresAnormalesDesprop implements IRule{
	
	private static final Logger logger = Logger.getLogger(BusquedaDocValoresAnormalesDesprop.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {
	
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try {
			/*************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesflowAPI.getGenDocAPI();
			 /***********************************************************/
			
			logger.warn("INICIO BusquedaDocValoresAnormalesDesprop");
			
	        String numexp_padre = "";
	        String consulta = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION LIKE '%Licitador%'";
	        logger.warn("consulta "+consulta);
	        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consulta);
	        
	        Iterator<IItem> it = collection.iterator();
	        logger.warn("it "+collection.toList().size());
	        while (it.hasNext())
	        {
	        	numexp_padre = ((IItem)it.next()).getString("NUMEXP_PADRE");
	        	logger.warn("numexp_padre "+numexp_padre);
	        	//Busqueda del expediente Valores Anormales o Desproporcionados
	        	consulta = "WHERE NUMEXP_PADRE='"+numexp_padre+"' AND RELACION LIKE '%Val. anormales o despropor%'";
		        logger.warn("consulta "+consulta);
		        IItemCollection collectionValDesp = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consulta);
		        Iterator<IItem> itValDes = collectionValDesp.iterator();
		        logger.warn("it "+collectionValDesp.toList().size());
		        while (itValDes.hasNext())
		        {
		        	IItem itemvalDesp = (IItem)itValDes.next();
		        	String numexp_hijo_valDesp = itemvalDesp.getString("NUMEXP_HIJO");
		        	logger.warn("numexp_hijo_valDesp "+numexp_hijo_valDesp);
		        	
		        	//Compruebo que el procedimiento se llame '"Valores Anormales o Desproporcionados"'
		        	IItem expedienteVal = entitiesAPI.getExpedient(numexp_hijo_valDesp);
		        	String nombreExpe = expedienteVal.getString("NOMBREPROCEDIMIENTO");
		        	if(nombreExpe.equals("Valores Anormales o Desproporcionados")){
		        		IItemCollection documentsCollection = entitiesAPI.getDocuments(numexp_hijo_valDesp, "NOMBRE LIKE '%Informe%' AND ESTADOFIRMA='02'", "FDOC DESC");
		        		Iterator<IItem> itValDesColl = documentsCollection.iterator();
						while (itValDesColl.hasNext()){
							IItem contenidoDiligencia = (IItem)itValDesColl.next();
							IItem nuevoDocumento = (IItem)genDocAPI.createTaskDocument(rulectx.getTaskId(), contenidoDiligencia.getInt("ID_TPDOC"));
							
							String infopag = contenidoDiligencia.getString("INFOPAG_RDE");
							logger.warn("infopag "+infopag);
							String descripcion = contenidoDiligencia.getString("DESCRIPCION");	
							
//							File fileDiligencia = DipucrCommonFunctions.getFile(rulectx, infopag);
//							FileInputStream inGD = new FileInputStream(fileDiligencia);
//							int docId = nuevoDocumento.getInt("ID");
//										
//							Object connectorSession = genDocAPI.createConnectorSession();
							//IItem entityDoc = genDocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, inGD, (int)fileDiligencia.length(), "application/pdf", "Diligencia de Subsanación");

							//Busco el nombre de la empresa
							IItem expediente = entitiesAPI.getExpedient(numexp_padre);
							nuevoDocumento.set("DESCRIPCION", descripcion+" - "+expediente.getString("IDENTIDADTITULAR")+" - "+numexp_hijo_valDesp);
							nuevoDocumento.set("EXTENSION_RDE", contenidoDiligencia.getString("EXTENSION_RDE"));
							nuevoDocumento.set("EXTENSION", contenidoDiligencia.getString("EXTENSION_RDE"));
							nuevoDocumento.set("ESTADOFIRMA", contenidoDiligencia.getString("ESTADOFIRMA"));
							nuevoDocumento.set("FAPROBACION", contenidoDiligencia.getDate("FAPROBACION"));
							nuevoDocumento.set("COD_COTEJO", contenidoDiligencia.getString("COD_COTEJO"));
							nuevoDocumento.store(cct);
						}
		        	}
		        	
		        }

	        }
			
			
			
			logger.warn("FIN BusquedaDocValoresAnormalesDesprop");
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return null;
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

}
