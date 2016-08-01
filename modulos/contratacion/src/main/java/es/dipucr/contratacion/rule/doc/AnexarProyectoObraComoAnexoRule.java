package es.dipucr.contratacion.rule.doc;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

public class AnexarProyectoObraComoAnexoRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(AnexarProyectoObraComoAnexoRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		/*
		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
			// --------------------------------------------------------------------
		
			//Obtengo el numexp del procedimiento de Petición de contratación
		
			String sqlQueryPart = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Petición Contrato'";
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        Iterator<IItem> itExpRel = exp_relacionados.iterator();
	        String numexpPetCont = "";
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpPetCont = itemExpRel.getString("NUMEXP_PADRE");
	        	
	        	IItem exp = entitiesAPI.getExpedient(numexpPetCont);
	        	
	        	//obtengo el id_tramite y id_fase
				String strQuery = "WHERE NOMBRE = 'Informe Necesidad Contrato' AND ID_PCD="+exp.getInt("ID_PCD");
		        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
		        
				Iterator <IItem> it = collection.iterator();
		        if (it.hasNext())
		        {
		        	IItem doc = it.next();
		        	int idFase = doc.getInt("ID_FASE");
		        	String idTtramiteBpm = doc.getString("ID_TRAMITE_BPM");
		        	
		        	String query = "NUMEXP='"+numexpPetCont+"' AND ID_FASE_PCD="+idFase+" AND ID_TRAMITE_PCD="+idTtramiteBpm+" AND NOMBRE='Proyecto de las Obras'";
		        	logger.warn("query "+query);
		 			IItemCollection docsCollection = entitiesAPI.getDocuments(numexpPetCont, query, "FDOC DESC");
		 			
		 			Iterator <IItem> docIterator = docsCollection.iterator();
					while(docIterator.hasNext()){
						
						IItem docPres = docIterator.next();
						
						//Comprobar si esta firmado o no 
						String infoPag = "";
						if(docPres.getString("INFOPAG_RDE")!=null){
							infoPag= docPres.getString("INFOPAG_RDE");
						}
						else{
							if(docPres.getString("INFOPAG")!=null) infoPag= docPres.getString("INFOPAG");
						}
						File fichero = DipucrCommonFunctions.getFile(rulectx, infoPag);
						
						IItemCollection tpDocCollection = procedureAPI.getTaskTpDoc(rulectx.getTaskId());
				        Iterator tpDocIterator = tpDocCollection.iterator();
				        int idTpDoc = 0;
				        while (tpDocIterator.hasNext()){
				        	IItem tpDoc = (IItem)tpDocIterator.next();
				        	String nombretpDoc = (String) tpDoc.get("CT_TPDOC:NOMBRE");
			        		if(StringUtils.isNotEmpty(nombretpDoc) && nombretpDoc.trim().toUpperCase().indexOf("ANEXO")>=0)
				        		idTpDoc = tpDoc.getInt("CT_TPDOC:ID");
				        }
				        
				        if(idTpDoc != 0){
				        	IItem newDoc = genDocAPI.createTaskDocument(rulectx.getTaskId(), idTpDoc);
				    		
				        	FileInputStream inGD = new FileInputStream(fichero);
				    		int docId = newDoc.getInt("ID");
				    		
				    		IItem entityDoc = genDocAPI.attachTaskInputStream(genDocAPI.createConnectorSession(), idTramiteExpCartaDigital, docId, inGD, (int)fichero.length(), MimetypeMapping.getMimeType(extension), descripcion);
				    		entityDoc.set("EXTENSION", extension);	
				    		entityDoc.set("DESCRIPCION", descripcion);
				    		
				    		entityDoc.store(cct);
				    		if(inGD != null) inGD.close();
						}
				        if (fichero != null & fichero.exists()) fichero.delete();

					}
		        }
	        }
		}catch(ISPACRuleException e){
			logger.error("error: "+e.toString());
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error("error: "+e.toString());
			throw new ISPACRuleException("Error. ",e);
		}
*/
		return new Boolean (true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
