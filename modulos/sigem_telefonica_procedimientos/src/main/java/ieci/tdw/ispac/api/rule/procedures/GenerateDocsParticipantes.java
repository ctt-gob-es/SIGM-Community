package ieci.tdw.ispac.api.rule.procedures;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

/**
 * Regla  genérica utilizada para generar un documento a partir de una plantilla 
 * personalizado para cada Participante del expediente.
 * Esta regla funcionará en trámites que sólo tengan configurada una plantilla. Sí el
 * trámite tiene más de una habrá que construir una regla específica para seleccionar
 * la plantilla adecuada.
 */
public class GenerateDocsParticipantes implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		
		try
		{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
	        //----------------------------------------------------------------------------------------------

			//Variables
			String ndoc = "";
			String nombre = "";
	    	String dirnot = "";
	    	String c_postal = "";
	    	String localidad = "";
	    	String caut = "";
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");

			//Obtener participantes del expediente actual
			String sqlQueryPart = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";	
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
			
			//Comprobar que hay algún participante para el cual generar su comunicado
			if (participantes!=null && participantes.toList().size()>=1) 
			{
				//Comprobar que hay alguna plantilla asociada al trámite
	        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	        	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty())
	        	{
	        		throw new ISPACInfo("No hay tipo de documento asociado al trámite");
	        	}
	        	else 
	        	{
	        		//Tomamos el primer (y debería ser el único) tipo de documento
	        		IItem taskTpDoc = (IItem)taskTpDocCollection.iterator().next();
	        		String strTpDocName = (String)taskTpDoc.get("CT_TPDOC:NOMBRE");	        		
    				int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
		        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
		        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty())
		        	{
		        		throw new ISPACInfo("No hay plantilla asociada al tipo de documento");
		        	}
		        	else
		        	{
		        		//Tomamos la primera plantilla (también debería ser la única)
		        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
		        		String strTemplateName = (String)tpDocsTemplate.get("NOMBRE");
			        	
			        	//Hay una plantilla así que podemos recorrer la lista de
			        	//participantes para generar los documentos
						for (int i=0;i<participantes.toList().size();i++)
						{
							IItem participante = (IItem) participantes.toList().get(i);
							if (participante!=null)
							{
					        	if ((String)participante.get("NDOC")!=null) ndoc = (String)participante.get("NDOC");
					        	if ((String)participante.get("NOMBRE")!=null) nombre = (String)participante.get("NOMBRE");
					        	if ((String)participante.get("DIRNOT")!=null) dirnot = (String)participante.get("DIRNOT");
					        	if ((String)participante.get("C_POSTAL")!=null) c_postal = (String)participante.get("C_POSTAL");
					        	if ((String)participante.get("LOCALIDAD")!=null) localidad = (String)participante.get("LOCALIDAD");
					        	if ((String)participante.get("CAUT")!=null) caut = (String)participante.get("CAUT");
					        	cct.setSsVariable("NDOC", ndoc);
					        	cct.setSsVariable("NOMBRE", nombre);
					        	cct.setSsVariable("DIRNOT", dirnot);
					        	cct.setSsVariable("C_POSTAL", c_postal);
					        	cct.setSsVariable("LOCALIDAD", localidad);
					        	cct.setSsVariable("CAUT", caut);
								
						        CommonFunctions.generarDocumento(rulectx, strTpDocName, strTemplateName, participante.getString("NDOC"));
								
								cct.deleteSsVariable("NDOC");
								cct.deleteSsVariable("NOMBRE");
								cct.deleteSsVariable("DIRNOT");
								cct.deleteSsVariable("C_POSTAL");
								cct.deleteSsVariable("LOCALIDAD");
								cct.deleteSsVariable("CAUT");
							}
						}
		        	}
	        	}
				
			}
		}
		catch(ISPACException e)
		{
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
