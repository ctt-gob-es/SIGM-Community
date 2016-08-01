package ieci.tdw.ispac.api.rule.procedures.secretaria;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
//import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

public class GenerateJustificantesAsistenciaRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
		
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			
			// Variables
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			String numExp = rulectx.getNumExp();
			String ndoc = "";
			String nombre = "";
	    	String dirnot = "";
	    	String c_postal = "";
	    	String localidad = "";
	    	String caut = "";
	    	int documentId = 0;
	    	Object connectorSession = null;
	    	boolean esJustificante = false;
	    	boolean esCertificacion = false;
	    	
			//Obtener participantes del expediente actual
			String sqlQueryPart = "WHERE NUMEXP = '"+numExp+"'";	
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
			
			//Comprobar que hay algún participante para el cual generar su acuse
			if (participantes!=null && participantes.toList().size()>=1) 
			{
				// 3. Obtener plantillas "Justificante de asistencia" y "Certificación de Asistentes"
				// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
	        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	        	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty())
	        	{
	        		throw new ISPACInfo("No hay tipo de documento asociado al trámite");
	        	}
	        	else 
	        	{
	        		//Hay dos tipos de documento asociados al trámite: Certificado de Asistentes
	        		//y Justificante de Asistencia. El Certificado es para todos los integrantes
	        		//juntos en una lista. Los justificantes son uno por integrante.
	        		Iterator it = taskTpDocCollection.iterator();
	        		IItem taskTpDoc = null;
	        		while (it.hasNext())
	        		{
	        			taskTpDoc = (IItem)it.next();
	        			esJustificante = taskTpDoc.get("CT_TPDOC:NOMBRE").equals("Justificante de asistencia");
	        			esCertificacion = taskTpDoc.get("CT_TPDOC:NOMBRE").equals("Certificación de Asistentes");
	        			if ( esJustificante || esCertificacion )
	        			{
	        				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	        			
			        		// Comprobar que el tipo de documento tiene asociado una plantilla
				        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
				        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
				        		throw new ISPACInfo("No hay plantilla asociada al tipo de documento");
				        	}else{
				        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
					        	templateId = tpDocsTemplate.getInt("ID");
					        	
					        	cct.setSsVariable("DESCR_ORGANO",getDescripcionOrgano(rulectx));
					        	
					        	if ( esJustificante )
					        	{
						    		// 4. Para cada participante generar un justificante de asistencia
									for (int i=0;i<participantes.toList().size();i++)
									{
										try {
											connectorSession = gendocAPI.createConnectorSession();
											IItem participante = (IItem) participantes.toList().get(i);
											// Abrir transacción para que no se pueda generar un documento sin fichero
									        cct.beginTX();
										
											if (participante!=null)
											{
												//Comprobamos que el participante ha asistido a la sesión de gobierno
												String strAsiste = (String)participante.get("ASISTE");
												if ( strAsiste!=null && strAsiste.compareTo("SI")==0)
													{
													// Añadir a la session los datos para poder utilizar <ispatag sessionvar='var'> en la plantilla
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
					
										        	entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
													documentId = entityDocument.getKeyInt();
					
													// Generar el documento a partir la plantilla Justificante de asistencia
													IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
					
													// Referencia al fichero del documento en el gestor documental
													//En la versión 1.9 ha cambiado esto del MimetypeMapping. 
													//Cuando pasemos de la 1.5 a la 1.9 lo cambiamos
													//String docref = entityTemplate.getString("INFOPAG");
													//String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
													//String sExt = MimetypeMapping.getInstance().getExtension(sMimetype);
													//entityTemplate.set("EXTENSION", sExt);
													entityTemplate.set("EXTENSION", "doc");
													String templateDescripcion = entityTemplate.getString("DESCRIPCION");
													templateDescripcion = templateDescripcion + " - " + cct.getSsVariable("NOMBRE");
													entityTemplate.set("DESCRIPCION", templateDescripcion);
													entityTemplate.store(cct);
													
										        	/*
													// 5. Actualizar el campo 'Acuse_Generado' con valor 'Y'
													IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
													participanteAActualizar.set("ACUSE_GENERADO", "Y");
													participanteAActualizar.store(cct);
											        */
													
													// Si todo ha sido correcto borrar las variables de la session
													cct.deleteSsVariable("NDOC");
													cct.deleteSsVariable("NOMBRE");
													cct.deleteSsVariable("DIRNOT");
													cct.deleteSsVariable("C_POSTAL");
													cct.deleteSsVariable("LOCALIDAD");
													cct.deleteSsVariable("CAUT");
												}							        
										    }
										}catch (Throwable e) {
											
											// Si se produce algún error se hace rollback de la transacción
											cct.endTX(false);
											
											String message = "exception.documents.generate";
											String extraInfo = null;
											Throwable eCause = e.getCause();
											
											if (eCause instanceof ISPACException) {
												
												if (eCause.getCause() instanceof NoConnectException) {
													extraInfo = "exception.extrainfo.documents.openoffice.off"; 
												}
												else {
													extraInfo = eCause.getCause().getMessage();
												}
											}
											else if (eCause instanceof DisposedException) {
												extraInfo = "exception.extrainfo.documents.openoffice.stop";
											}
											else {
												extraInfo = e.getMessage();
											}			
											throw new ISPACInfo(message, extraInfo);
											
										}finally {
											
											if (connectorSession != null) {
												gendocAPI.closeConnectorSession(connectorSession);
											}
										}
									}// for
									
					        	}
					        	else //Es certificación
					        	{
									try 
									{
										connectorSession = gendocAPI.createConnectorSession();
										// Abrir transacción para que no se pueda generar un documento sin fichero
								        cct.beginTX();
									
							        	entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
										documentId = entityDocument.getKeyInt();
		
										// Generar el documento a partir la plantilla Justificante de asistencia
										IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
		
										// Referencia al fichero del documento en el gestor documental
										entityTemplate.set("EXTENSION", "doc");
										entityTemplate.store(cct);
								    }
									catch (Throwable e) 
									{
										// Si se produce algún error se hace rollback de la transacción
										cct.endTX(false);
										
										String message = "exception.documents.generate";
										String extraInfo = null;
										Throwable eCause = e.getCause();
										
										if (eCause instanceof ISPACException) {
											
											if (eCause.getCause() instanceof NoConnectException) {
												extraInfo = "exception.extrainfo.documents.openoffice.off"; 
											}
											else {
												extraInfo = eCause.getCause().getMessage();
											}
										}
										else if (eCause instanceof DisposedException) {
											extraInfo = "exception.extrainfo.documents.openoffice.stop";
										}
										else {
											extraInfo = e.getMessage();
										}			
										throw new ISPACInfo(message, extraInfo);
										
									}finally {
										
										if (connectorSession != null) {
											gendocAPI.closeConnectorSession(connectorSession);
										}
									}
					        	}
					        	cct.deleteSsVariable("DESCR_ORGANO");
					      	}
	        			}
		        	}
	        		if ( documentTypeId == 0)
	        		{
	        			throw new ISPACInfo("No existe el tipo de documento Justificante de asistencia ni Certificación de Asistentes.");
	        		}
	        	}
			}
			// Si todo ha sido correcto se hace commit de la transacción
			cct.endTX(true);
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	private String getDescripcionOrgano(IRuleContext rulectx) throws ISPACException
	{
		String strDescr = "";

		try
		{
			String strOrgano = CommonFunctions.getSesion(rulectx,null).getString("ORGANO");
			if (strOrgano.compareTo("PLEN")==0)
			{
				strDescr = "del ";
			}
			else
			{
				strDescr = "de la ";
			}
			strDescr += CommonFunctions.getNombreOrganoSesion(rulectx,null);
			if (strOrgano.compareTo("COMI")==0)
			{
				strDescr += " del área de " + CommonFunctions.getNombreAreaSesion(rulectx,null);
			}
		}
		catch(ISPACException e)
		{
        	throw new ISPACException(e);
        }
		return strDescr;
	}
}
