package ieci.tdw.ispac.api.rule.procedures.secretaria;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;

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
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

public class GenerateActaBaseRule implements IRule {
	
	protected static final Logger logger = Logger.getLogger(GenerateActaBaseRule.class);

	protected String STR_prefijo                 = "Borrador de Acta de Pleno";
	protected String STR_nombreTramite           = STR_prefijo;
	protected String STR_nombreCabecera          = STR_prefijo + " - Cabecera";
	protected String STR_nombrePie               = STR_prefijo + " - Pie";
	protected String STR_nombreCabeceraPropuesta = STR_prefijo + " - Propuesta - Cabecera";
	protected String STR_nombrePiePropuesta      = STR_prefijo + " - Propuesta - Pie";
	protected String STR_nombreRuegos            = STR_prefijo + " - Ruegos y preguntas";
	
	private OpenOfficeHelper ooHelper = null;
	
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
	        DecimalFormat df = new DecimalFormat("0000");
	    	int documentId = 0;
	    	Object connectorSession = null;
	    	String nombrePlantilla;
	        List list = null;
	        Iterator itProps = null;
	        IItem iProp = null;
	        int orden = 1;
	        int nTotal = 0;
	        String extracto = "";
	        String debate = "";
	        String acuerdos = "";
	        int nSi = 0;
	        int nNo = 0;
	        int nAbs = 0;
	        Hashtable orden2exp = new Hashtable();
	    	
	        //Comprobación de trámites anteriores
	        //if ( !CommonFunctions.existeTramite(rulectx,"Convocatoria de la sesión"))
	        //{
	        //	throw new ISPACInfo("Es necesario realizar antes el trámite 'Convocatoria de la sesión'");
	        //}
	        //if ( CommonFunctions.existeTramite(rulectx,STR_nombreTramite))
	        //{
	        //	throw new ISPACInfo("Ya existe un trámite llamado '" + STR_nombreTramite + "'");
	        //}
	        
			// Obtención de plantillas asociadas al trámite
	    	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	    	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty())
	    	{
	    		throw new ISPACInfo("No hay tipo de documento asociado al trámite");
	    	}

    		// Generación del documento por cada tipo de documento asociado
        	Iterator itTpDocs = taskTpDocCollection.iterator();
        	while(itTpDocs.hasNext())
        	{
		    	IItem taskTpDoc = (IItem)itTpDocs.next();
	    		documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	    		
	    		// Comprobar que el tipo de documento tiene asociado una plantilla
	        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
	        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty())
	        	{
	        		throw new ISPACInfo("No hay plantilla asociada al tipo de documento");
	        	}
	    		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
	        	templateId = tpDocsTemplate.getInt("ID");

	        	// Si la plantilla es la de propuestas entonces hay que usarla
	        	// varias veces, una por cada propuesta. Usaremos variables de
	        	// sistema para pasarle datos al tag <ispactag sessionvar='var'/>
	        	nombrePlantilla = tpDocsTemplate.getString("NOMBRE");
	        	boolean esPropuesta = 
	        		nombrePlantilla.compareTo(STR_nombreCabeceraPropuesta)==0 || 
	        		nombrePlantilla.compareTo(STR_nombrePiePropuesta)==0 ;

	        	if ( esPropuesta )
	        	{
	        		list = CommonFunctions.getPropuestas(rulectx, entitiesAPI);
	    	        itProps = list.iterator();
	    	        orden = 1;
	        	}
	        	boolean seguir = true;
	        	while ( seguir )
	        	{
	        		seguir = false;
					try 
					{
		        		if ( esPropuesta )
		        		{
		    	        	iProp = ((IItem)itProps.next());
		    	        	String strOrgano = CommonFunctions.getOrganoSesion(rulectx, null);
		    	        	boolean esAcuerdo = (strOrgano.compareTo("PLEN")==0 || strOrgano.compareTo("JGOB")==0 );
		    	        	String strCampo = esAcuerdo? "ACUERDOS":"DICTAMEN";
				        	if (iProp.get("EXTRACTO")!=null) extracto = (String)iProp.get("EXTRACTO"); else extracto = "";
				        	if (iProp.get("DEBATE")!=null) debate = (String)iProp.get("DEBATE"); else debate = "";
				        	if (iProp.get(strCampo)!=null) acuerdos = (String)iProp.get(strCampo); else acuerdos = "";
				        	debate = debate.replaceAll("\r\n", "\r"); //Evita saltos de línea duplicados
				        	acuerdos = acuerdos.replaceAll("\r\n", "\r");
				        	if (iProp.get("N_SI")!=null) nSi = iProp.getInt("N_SI"); else nSi=0;
				        	if (iProp.get("N_NO")!=null) nNo = iProp.getInt("N_NO"); else nNo=0;
				        	if (iProp.get("N_ABS")!=null) nAbs = iProp.getInt("N_ABS"); else nAbs=0;
				        	cct.setSsVariable("EXTRACTO", extracto);
				        	//cct.setSsVariable("DEBATE", debate);
				        	//cct.setSsVariable("ACUERDOS", acuerdos);
				        	setLongVariable(rulectx, "DEBATE", debate);
				        	setLongVariable(rulectx, "ACUERDOS", acuerdos);
				        	cct.setSsVariable("ORDEN", String.valueOf(orden));
				        	cct.setSsVariable("N_SI", String.valueOf(nSi));
				        	cct.setSsVariable("N_NO", String.valueOf(nNo));
				        	cct.setSsVariable("N_ABS", String.valueOf(nAbs));
				        	cct.setSsVariable("N_TOTAL", String.valueOf(nSi+nNo+nAbs));
				        	//Guardo el numero de expediente para luego recuperar el word de contenido.
				        	String numexp_origen = iProp.getString("NUMEXP_ORIGEN");
				        	if ( numexp_origen == null)
				        		numexp_origen = "no hay";
				        	orden2exp.put(new Integer(orden), numexp_origen);
		        		}

						connectorSession = gendocAPI.createConnectorSession();
						// Abrir transacción para que no se pueda generar un documento sin fichero
				        cct.beginTX();
					
			        	entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
						documentId = entityDocument.getKeyInt();
		
						// Generar el documento a partir de la plantilla
						IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
						entityTemplate.set("EXTENSION", "doc");
						entityTemplate.store(cct);
						
						if ( esPropuesta )
						{
							// Añadimos el número de orden al nombre del doc para identificarlo mejor.
							String templateDescripcion = entityTemplate.getString("DESCRIPCION");
							templateDescripcion = templateDescripcion + " - " + df.format(orden);
							entityTemplate.set("DESCRIPCION", templateDescripcion);
							entityTemplate.store(cct);
							//Borramos las variables de sistema
							cct.deleteSsVariable("EXTRACTO");
							//cct.deleteSsVariable("DEBATE");
							//cct.deleteSsVariable("ACUERDOS");
							deleteLongVariable(rulectx, "DEBATE");
							deleteLongVariable(rulectx, "ACUERDOS");
							cct.deleteSsVariable("ORDEN");
							cct.deleteSsVariable("N_SI");
							cct.deleteSsVariable("N_NO");
							cct.deleteSsVariable("N_ABS");
							cct.deleteSsVariable("N_TOTAL");
							//Preparamos la siguiente iteración
							orden++;
							nTotal++;
							seguir = esPropuesta && itProps.hasNext();
						}
					}
					catch (Throwable e)
					{
						// Si se produce algún error se hace rollback de la transacción
						cct.endTX(false);
						
						String message = "exception.documents.generate";
						String extraInfo = null;
						Throwable eCause = e.getCause();
						
						if (eCause instanceof ISPACException)
						{
							if (eCause.getCause() instanceof NoConnectException) 
							{
								extraInfo = "exception.extrainfo.documents.openoffice.off"; 
							}
							else
							{
								extraInfo = eCause.getCause().getMessage();
							}
						}
						else if (eCause instanceof DisposedException)
						{
							extraInfo = "exception.extrainfo.documents.openoffice.stop";
						}
						else
						{
							extraInfo = e.getMessage();
						}			
						throw new ISPACInfo(message, extraInfo);
						
					}
					finally
					{
						if (connectorSession != null)
						{
							gendocAPI.closeConnectorSession(connectorSession);
						}
					}
			    	
		        	// Si todo ha sido correcto se hace commit de la transacción
					cct.endTX(true);
	        	}
        	}
        	
        	//Ahora hay que concatenar todos los archivos que hemos generado
        	//
        	//    Obtener cabecera y guardarla en repositorio temporal
        	//    Bucle
        	//        Obtener siguiente fichero
        	//        Concatenar cabecera con fichero siguiente
        	//    Guardar cabecera ya procesada en repositorio documental
        	//    Borrar todos los documentos auxiliares

        	//Obtiene la cabecera
        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, entitiesAPI, STR_nombreCabecera);
        	logger.warn("strInfoPag "+strInfoPag +"STR_nombreCabecera "+STR_nombreCabecera);
        	File file1 = CommonFunctions.getFile(gendocAPI, strInfoPag);
        	logger.warn("strInfoPag "+strInfoPag +"STR_nombreCabecera "+STR_nombreCabecera);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());

    		//Obtiene e inserta cada uno de los fichero de propuesta (cabecera y pie)
    		nTotal = nTotal/2; //Estaba duplicado el número de propuestas porque hay dos documentos, cabecera y pie.
    		String descr = "";
    		File file = null;
        	for ( int i=1 ; i<=nTotal ; i++)
        	{
        		descr = STR_nombreCabeceraPropuesta + " - " + df.format(i);
            	strInfoPag = CommonFunctions.getInfoPag(rulectx, entitiesAPI, descr);
        		file = CommonFunctions.getFile(gendocAPI, strInfoPag);
        		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
        		file.delete();
        		
        		insertContenidoPropuesta(rulectx, xComponent, (String)orden2exp.get(new Integer(i)));

        		descr = STR_nombrePiePropuesta + " - " + df.format(i);
            	strInfoPag = CommonFunctions.getInfoPag(rulectx, entitiesAPI, descr);
        		file = CommonFunctions.getFile(gendocAPI, strInfoPag);
        		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
        		file.delete();
        	}
        	//Obtiene e inserta los ruegos y preguntas.
        	strInfoPag = CommonFunctions.getInfoPag(rulectx, entitiesAPI, STR_nombreRuegos);
    		file = CommonFunctions.getFile(gendocAPI, strInfoPag);
    		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();

    		//Obtiene e inserta el pie del acta.
        	strInfoPag = CommonFunctions.getInfoPag(rulectx, entitiesAPI, STR_nombrePie);
    		file = CommonFunctions.getFile(gendocAPI, strInfoPag);
    		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();
    		
    		//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
    		file1.delete();
    		
    		//Guarda el resultado en gestor documental
			String strQuery = "WHERE NOMBRE = '" + STR_nombreTramite + "'";
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
	        Iterator it = collection.iterator();
	        int tpdoc = 0;
	        if (it.hasNext())
	        {
	        	IItem tpd = (IItem)it.next();
	        	tpdoc = tpd.getInt("ID");
	        }
    		IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
    		FileInputStream in = new FileInputStream(file);
    		int docId = newdoc.getInt("ID");
    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, taskId, docId, in, (int)file.length(), "application/msword", STR_nombreTramite);
    		entityDoc.set("EXTENSION", "doc");
    		entityDoc.store(cct);
    		file.delete();
    		
    		//Borra los documentos intermedios del gestor documental
			strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION LIKE '" + STR_prefijo + " -%' OR DESCRIPCION LIKE 'Borrador de Acta de Pleno -%'";
	        collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
		}
		catch(Exception e)
		{
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException(e);
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	private void insertContenidoPropuesta(IRuleContext rulectx, XComponent xComponent, String numexp) throws ISPACRuleException
	{
		try
		{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			
			//Obtenemos los docuementos de la propuesta
			String strQuery = "WHERE NUMEXP = '" + numexp + "'";
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
	        Iterator it = collection.iterator();
	        boolean found = false;
	        while (it.hasNext() && !found)
	        {
	        	IItem iDoc = (IItem)it.next();
	        	
	        	//El contenido de la propuesta tiene que estar en formato Word (.doc)
	        	String extension = iDoc.getString("EXTENSION");
	        	if ( extension.toUpperCase().compareTo("DOC")==0)
	        	{
	        		//En concreto busco documentos de tipo Anexo a la Solicitud (propuestas desde Registro Telemático)
	        		//o de tipo Contenido de la propuesta (propuestas iniciadas desde escritorio de tramitación)
		        	String nombre = iDoc.getString("NOMBRE");
		        	if ( nombre.compareTo("Anexo a Solicitud" )== 0 ||
		        		 nombre.compareTo("Contenido de la propuesta") == 0)
		        	{
		        		found = true;
			        	String strInfoPag = iDoc.getString("INFOPAG");
			        	if (strInfoPag != null)
			        	{
			        		File file = CommonFunctions.getFile(gendocAPI, strInfoPag);
			        		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
			        		file.delete();
			        	}
		        	}
	        	}
	        }
		}
		catch(Exception e)
		{
			throw new ISPACRuleException(e); 	
		}
	}
	
	private void setLongVariable(IRuleContext rulectx, String nombre, String valor) throws ISPACRuleException
	{
		try
		{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			IItem entity = entitiesAPI.createEntity("TSOL_LONG_VARS", rulectx.getNumExp());
			entity.set("NOMBRE", nombre);
			entity.set("VALOR", valor);
			entity.store(cct);
		}
		catch(Exception e)
		{
			throw new ISPACRuleException(e); 	
		}
	}
	
	private void deleteLongVariable(IRuleContext rulectx, String nombre) throws ISPACRuleException
	{
		try
		{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' AND NOMBRE='" + nombre + "'";
			entitiesAPI.deleteEntities("TSOL_LONG_VARS", strQuery);
		}
		catch(Exception e)
		{
			throw new ISPACRuleException(e); 	
		}
	}
}
