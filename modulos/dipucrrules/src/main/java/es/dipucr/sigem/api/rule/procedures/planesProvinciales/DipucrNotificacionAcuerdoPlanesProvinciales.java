package es.dipucr.sigem.api.rule.procedures.planesProvinciales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio;
import es.dipucr.services.server.planesProvinciales.PlanProvincialServicioProxy;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrNotificacionAcuerdoPlanesProvinciales implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrNotificacionAcuerdoPlanesProvinciales.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			String anio = "2011";
			logger.info("INICIO - DipucrNotificacionAcuerdoPlanesProvinciales");
			
			PlanProvincialServicioProxy planService;
		    planService = new PlanProvincialServicioProxy();
		    DatosCuadroMinisterio[] datosCuadroAMinisterio = planService.getCuadroA(Integer.parseInt(anio));		    
		    DatosCuadroMinisterio[] datosCuadroCMinisterio = planService.getCuadroC(Integer.parseInt(anio));
			
			// APIS
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			cct.endTX(true);
			// Variables
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");	
			String nombreTramite =  processTask.getString("NOMBRE");
			
			String numExp = rulectx.getNumExp();
			String nombre = "";
	    	String dirnot = "";
	    	String c_postal = "";
	    	String localidad = "";
	    	String caut = "";
	    	String id_ext = "";
	    	int documentId = 0;
	    	Object connectorSession = null;
			String sFileTemplate = null;
	    	
			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, " (ROL != 'TRAS' OR ROL IS NULL) ", "ID");

			// 2. Comprobar que hay algún participante para el cual generar su notificación
			if (participantes!=null && participantes.toList().size()>=1) {
				// 3. Obtener plantilla "Comunicación Administrativa Ayuntamientos Planes"
				// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
	        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);     		
        		Iterator it = taskTpDocCollection.iterator();
        		while (it.hasNext()){
        			IItem taskTpDoc = (IItem)it.next();
        			if ((((String)taskTpDoc.get("CT_TPDOC:NOMBRE")).trim().toUpperCase()).equals(("Notificación Ayuntamientos Aprobación Obra").trim().toUpperCase())){        				
        				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
        			}
        		}
        		
        		//Comprobamos que haya encontrado el Tipo de documento
        		if (documentTypeId != 0){
	        		// Comprobar que el tipo de documento tiene asociado una plantilla
		        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
		        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
		        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
		        	}else{
		        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
			        	templateId = tpDocsTemplate.getInt("ID");
			        	IItem entityDocumentT  = gendocAPI.createTaskDocument(taskId, documentTypeId);
						int documentIdT = entityDocumentT.getKeyInt();						

						IItem entityTemplateT = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentIdT, templateId);
						String infoPagT = entityTemplateT.getString("INFOPAG");
						entityTemplateT.store(cct);
			        	
			    		// 4. Para cada participante generar una notificación
						for (int i=0;i<participantes.toList().size();i++){
							try {
								connectorSession = gendocAPI.createConnectorSession();
								IItem participante = (IItem) participantes.toList().get(i);
								// Abrir transacción para que no se pueda generar un documento sin fichero
						        cct.beginTX();

								if (participante!=null){						        
									// Añadir a la session los datos para poder utilizar <ispactag sessionvar='var'> en la plantilla
						        	if ((String)participante.get("NOMBRE")!=null){
						        		nombre = (String)participante.get("NOMBRE");
						        	}else{
						        		nombre = "";
						        	}
						        	if ((String)participante.get("DIRNOT")!=null){
						        		dirnot = (String)participante.get("DIRNOT");
						        	}else{
						        		dirnot = "";
						        	}
						        	if ((String)participante.get("C_POSTAL")!=null){
						        		c_postal = (String)participante.get("C_POSTAL");
						        	}else{
						        		c_postal = "";
						        	}
						        	if ((String)participante.get("LOCALIDAD")!=null){
						        		localidad = (String)participante.get("LOCALIDAD");
						        	}else{
						        		localidad = "";
						        	}
						        	if ((String)participante.get("CAUT")!=null){
						        		caut = (String)participante.get("CAUT");
						        	}else{
						        		caut = "";
						        	}
						        	/**
						        	 * INICIO[Teresa] Ticket#106#: añadir el campo id_ext
						        	 * **/
						        	if ((String)participante.get("ID_EXT")!=null){
						        		id_ext = (String)participante.get("ID_EXT");
						        	}else{
						        		id_ext = "";
						        	}
						        	/**
						        	 * FIN[Teresa] Ticket#106#: añadir el campo id_ext
						        	 * **/
						        	
						        	if(nombre.toUpperCase().contains("Ñ")){
						        		nombre = nombre.replace("ñ", "¥");
						        		nombre = nombre.replace("Ñ", "¥");						        		
						        	}
						        	
//						        	planService = new PlanProvincialServicioProxy();
//						        	plan = planService.getPlanProvincial(nombre, "2011");
						        	//vector con dos posiciones, la primera el número de obra, la seguna los datos de la obra
						        	
						        	List listaDatos = new ArrayList();
						        	Object[] dato;
						        	DatosCuadroMinisterio cuadroA;
						        	for(int j = 0; j<datosCuadroAMinisterio.length; j++){
						        		cuadroA = datosCuadroAMinisterio[j];
						        		dato = new Object[2];
						        		if(cuadroA.getMunicipio().trim().toUpperCase().contains(nombre.trim().toUpperCase())){
						        			dato[0] = new Integer(j+1);
						        			dato[1] = cuadroA;
						        			listaDatos.add(dato);
						        		}
						        	}
						        	for(int j = 0; j<datosCuadroCMinisterio.length; j++){
						        		cuadroA = datosCuadroCMinisterio[j];
						        		dato = new Object[2];
						        		if(cuadroA.getMunicipio().trim().toUpperCase().contains(nombre.trim().toUpperCase())){
						        			dato[0] = new Integer(j+1);
						        			dato[1] = cuadroA;
						        			listaDatos.add(dato);
						        		}
						        	}
						        	
						        	if(nombre.toUpperCase().contains("¥")){						        		
						        		nombre = nombre.replace("¥", "Ñ");						        		
						        	}
						        	
						        	if(listaDatos != null && listaDatos.size() > 0){
						        	
						        		for(int k = 0; k < listaDatos.size(); k++){
						        			dato = (Object[]) listaDatos.get(k);
						        			int numeroObra = ((Integer) dato[0]).intValue();
						        			DatosCuadroMinisterio datosObra = (DatosCuadroMinisterio) dato[1];
						        			
						        			setSsVariables(cct, rulectx);
								        	cct.setSsVariable("NOMBRE", nombre);
								        	cct.setSsVariable("DIRNOT", dirnot);
								        	cct.setSsVariable("C_POSTAL", c_postal);
								        	cct.setSsVariable("LOCALIDAD", localidad);
								        	cct.setSsVariable("CAUT", caut);
								        	//Asignamos el nombre del trátime ya que si no no lo muestra
								    		cct.setSsVariable("NOMBRE_TRAMITE", nombreTramite);
								        	
								        	if(datosObra != null){								        		
									        	cct.setSsVariable("NOMBRE", nombre);
									        	cct.setSsVariable("NUMERO", "" + numeroObra);
									        	cct.setSsVariable("DENOMINACIONOBRA", datosObra.getDenoobra());
									        	cct.setSsVariable("ESTADO", "" + datosObra.getEstado());
									        	cct.setSsVariable("DIPUTACION", "" + datosObra.getDiputacion());
									        	cct.setSsVariable("APORTACIONAYUNTAMIENTO", "" + datosObra.getAyuntamiento());
									        	cct.setSsVariable("TOTAL", "" + (datosObra.getEstado()+datosObra.getDiputacion()+datosObra.getAyuntamiento()));
								        	}
								        	else{
								        		cct.setSsVariable("NOMBRE", nombre);
								        		cct.setSsVariable("NUMERO", "");
									        	cct.setSsVariable("DENOMINACIONOBRA", "");
									        	cct.setSsVariable("DIPUTACION", "");
									        	cct.setSsVariable("ESTADO", "");
									        	cct.setSsVariable("APORTACIONAYUNTAMIENTO", "");
									        	cct.setSsVariable("TOTAL", "");
								        	}
								        	
								        	
								        	entityDocument  = gendocAPI.createTaskDocument(taskId, documentTypeId);
											documentId = entityDocument.getKeyInt();
		
											sFileTemplate = DocumentosUtil.getFile(cct, infoPagT, null, null).getName();
																				
											// Generar el documento a partir la plantilla 
											IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);
											
											// Referencia al fichero del documento en el gestor documental
											String docref = entityTemplate.getString("INFOPAG");
											String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
											entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
											String templateDescripcion = entityTemplate.getString("DESCRIPCION");
											templateDescripcion = templateDescripcion + " - " + cct.getSsVariable("NOMBRE");
											entityTemplate.set("DESCRIPCION", templateDescripcion);
											entityTemplate.set("DESTINO", cct.getSsVariable("NOMBRE"));
											/**
								        	 * INICIO[Teresa] Ticket#106#: añadir el campo id_ext
								        	 * **/
											entityTemplate.set("DESTINO_ID", id_ext);
											/**
								        	 * FIN[Teresa] Ticket#106#: añadir el campo id_ext
								        	 * **/
											entityTemplate.store(cct);
																		        
											// Si todo ha sido correcto borrar las variables de la session
											deleteSsVariables(cct);
											cct.deleteSsVariable("NOMBRE");
											cct.deleteSsVariable("DIRNOT");
											cct.deleteSsVariable("C_POSTAL");
											cct.deleteSsVariable("LOCALIDAD");
											cct.deleteSsVariable("CAUT");
											cct.deleteSsVariable("RECURSO");
											cct.deleteSsVariable("OBSERVACIONES");
											cct.deleteSsVariable("NOMBRE");
								        	cct.deleteSsVariable("NUMERO");
								        	cct.deleteSsVariable("DENOMINACIONOBRA");
								        	cct.deleteSsVariable("ESTADO");
								        	cct.deleteSsVariable("DIPUTACION");
								        	cct.deleteSsVariable("APORTACIONAYUNTAMIENTO");
								        	cct.deleteSsVariable("TOTAL");
							        	}//fin for cada obra
						        	}//Si hay obras
								}
							}catch (Exception e) {
								
								// Si se produce algún error se hace rollback de la transacción
								cct.endTX(false);
								logger.info("ERROR: "+e.getMessage());
								logger.info("ERROR: "+e.getStackTrace());
								logger.info("ERROR: "+e.toString());
								logger.info("ERROR: "+e.getStackTrace().toString());
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
				            	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
								throw new ISPACInfo(message, extraInfo);
								
							}finally {
								
								if (connectorSession != null) {
									gendocAPI.closeConnectorSession(connectorSession);
								}
							}
						}// for
						entityTemplateT.delete(cct);
						entityDocumentT.delete(cct);
			      	}	        		
	        	}
			}		
			cct.endTX(true);
			logger.info("FIN - DipucrNotificacionAcuerdoPlanesProvinciales");
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			String anio = "", anioAnterior = "", fechaSegundoPleno = "", fechaPrimerBop = "";
			String fechaPrimeraNotificacion = ""; 
			StringBuffer nregistros = new StringBuffer();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			String sqlQueryPart = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			IItemCollection participantes = entitiesAPI.queryEntities("DIPUCRPLANESPROVINCIALES", sqlQueryPart);
			Iterator it = (Iterator) participantes.iterator();
			if(it.hasNext()){
				IItem item = (IItem)it.next();
				anio = item.getString("ANIO");
				fechaSegundoPleno = item.getString("SEGUNDOPLENO");
				fechaPrimerBop = item.getString("PRIMERBOP");
			}
			if(anio == null){
				anio = "";
			}
			else{
				try{
					anioAnterior = ""+(Integer.parseInt(anio)-1);
				}
				catch(Exception e){anioAnterior = "";}
			}
			if(fechaSegundoPleno == null) fechaSegundoPleno = "";
			if(fechaPrimerBop == null) fechaPrimerBop = "";
			
			IItemCollection docs = entitiesAPI.getDocuments(rulectx.getNumExp(), "UPPER(NOMBRE) LIKE '%COMUNICACIÓN ADMINISTRATIVA%' AND COD_COTEJO IS NOT NULL AND NREG != NULL", "");
			Iterator it2 = (Iterator) docs.iterator();
			while(it2.hasNext()){
				IItem item = (IItem)it2.next();
				fechaPrimeraNotificacion = item.getString("FREG");
				nregistros.append(item.getString("NREG"));
				nregistros.append(",");
			}
			if(nregistros!= null && !nregistros.equals(""))
				nregistros.substring(0, nregistros.length());
			
			if(fechaPrimeraNotificacion == null) fechaPrimeraNotificacion = "";
			
			cct.setSsVariable("ANIO", anio);			
			cct.setSsVariable("FECHASEGUNDOPLENO", fechaSegundoPleno);
			cct.setSsVariable("FECHAPRIMERBOP", fechaPrimerBop);
			cct.setSsVariable("ANIOANTERIOR", anioAnterior);
			cct.setSsVariable("FECHAPRIMERANOTIFICACION", fechaPrimeraNotificacion);
			cct.setSsVariable("NREGISTROSALIDAPRIMERANOTIF", nregistros.toString());			
		} catch (ISPACException e) {				
			logger.error(e.getMessage(), e);
		}
	}

	public void deleteSsVariables(IClientContext cct) {	
		try {
			cct.deleteSsVariable("ANIO");
			cct.deleteSsVariable("FECHAPRIMERPLENO");
			cct.deleteSsVariable("FECHAPRIMERBOP");
			cct.deleteSsVariable("ANIOANTERIOR");
			cct.deleteSsVariable("FECHAPRIMERANOTIFICACION");
			cct.deleteSsVariable("NREGISTROSALIDAPRIMERANOTIF");			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}	
}
