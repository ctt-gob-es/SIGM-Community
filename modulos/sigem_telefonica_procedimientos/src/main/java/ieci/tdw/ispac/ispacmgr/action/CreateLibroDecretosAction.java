package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISPACVariable;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.ManagerState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class CreateLibroDecretosAction extends BaseAction {

    private static final String NOMBRE_DOCUMENTO = "Libro registro decretos";
    
    SignDocument aSignDocument;

    private IClientContext clientContext;
    //[eCenpri-Felipe Ticket #157] Se define una variable para indicar el nº máximo de decretos
    //La antigua longitud de los arrays, 1000, no era suficiente
    private static final int TAMANIO_ARRAYS = 3000; //[eCenpri-Felipe Ticket #157]
    //TODO: por que no se definen arraylists?
    
    //Clase utilizada para la creacion del Libro de Decretos, documento pdf con la concatenacion de los pdf de decretos
    public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws ISPACException, IOException  {
        
        ClientContext cct = session.getClientContext();
        this.clientContext = cct;
        // Se obtiene el estado de tramitación
        IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
           IState currentState = managerAPI.currentState(getStateticket(request));

          IInvesflowAPI invesFlowAPI = session.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
        
        String mensajeRespuesta = "No se han encontrado en el repositorio los siguientes documentos: ";
        boolean variosDocNoEncontrado = false;
        
        //Obtenemos los parámetros recibidos del formulario
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
        String fechaIniDec = request.getParameter("fechaIniDec");
        Date dFechaIniDec = null;
        
        if (StringUtils.isNotEmpty(fechaIniDec)){
            
            try {
                dFechaIniDec = sdf.parse(fechaIniDec);
            } catch(Exception e){
                LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
                throw new ISPACInfo("Fecha de inicio incorrecta");
            }
        }
        
        String fechaFinDec = request.getParameter("fechaFinDec");
        Date dFechaFinDec = null;
        
        if (StringUtils.isNotEmpty(fechaFinDec)){
            try {
                dFechaFinDec = sdf.parse(fechaFinDec);
            
            } catch(Exception e){
                LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
                throw new ISPACInfo("Fecha de fin incorrecta");
            }
        }
        String inicioDec = request.getParameter("inicioDec");
        int iInicioDec = -1;
        
        if (StringUtils.isNotEmpty(inicioDec)){
            try {
                iInicioDec = Integer.parseInt(inicioDec);
                
                if (iInicioDec < 0){
                    throw new ISPACInfo("Decreto inicio incorrecto");
                }
                
            } catch(Exception e){
                LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
                throw new ISPACInfo("Decreto inicio incorrecto");
            }
        }
        String finDec = request.getParameter("finDec");
        int iFinDec = -1;
        
        if (StringUtils.isNotEmpty(finDec)){
            try {
                iFinDec = Integer.parseInt(finDec);
            
                if (iFinDec < 0){
                    throw new ISPACInfo("Decreto fin incorrecto");
                }
                
            } catch(Exception e){
                LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
                throw new ISPACInfo("Decreto fin incorrecto");
            }
        }
        
        //Leemos el valor de tipoDec (sel: selección, cre: creación)
        String tipoDec = request.getParameter("tipoDec");

        //INICIO [eCenpri-Felipe Ticket #157]
        String[] expedientesOrden = new String[TAMANIO_ARRAYS];//expedientes ordenados por numero_decreto
        
        //aSignDocument = new SignDocument[1000];
        //docItemSignDocument = new IItem[1000];
        //File[] files = new File[1000];
        String[] filesInfoPag = new String[TAMANIO_ARRAYS];
        String[] expedientes = new String[TAMANIO_ARRAYS];
        int[] unicos = new int[TAMANIO_ARRAYS];
        //FIN [eCenpri-Felipe Ticket #157]
        String infoPag = null;
        //String nombre = null;
        String infoPagCon = null;
        //String nombreCon = null;

        //Primero sacamos en un Iterator los numexp de decretos no incluidos
        String strQuery = "WHERE (INCLUIDO_LIBRO IS NULL OR INCLUIDO_LIBRO = '0') AND FECHA_DECRETO IS NOT NULL AND NUMERO_DECRETO IS NOT NULL ORDER BY ANIO, NUMERO_DECRETO";
        IItemCollection collectionNoIncluidos = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
        Iterator<?> itNoIncluidos = collectionNoIncluidos.iterator();

        
        //Posteriormente recorremos el Iterator y generamos una única consulta del tipo
        // FROM SPAC_DT_DOCUMENTOS WHERE NOMBRE = 'Decreto' AND EXTENSION_RDE = 'pdf' AND NUMEXP=ARRAY[1] OR NUMEXP=ARRAY[2] OR NUMEXP=ARRAY[3]...
        String query = "WHERE NOMBRE = 'Decreto' AND EXTENSION_RDE = 'pdf'";
        String queryNumExp = null;
        
        IItem decItem = null;
        int nNoIncluidos = 0;
        
        while (itNoIncluidos.hasNext()){
            decItem = (IItem)itNoIncluidos.next();
            
            //Para acceder a las propiedades de los IItems resultado de la consulta
            //hay que calificarlas adecuadamente utilizando su prefijo
            if (isDecretoValido(decItem,dFechaIniDec,dFechaFinDec,iInicioDec,iFinDec)){
                nNoIncluidos++;
                if (nNoIncluidos==1){
                    queryNumExp = "NUMEXP='"+decItem.getString("NUMEXP")+"'";
                } else {
                    queryNumExp = queryNumExp+" OR NUMEXP='"+decItem.getString("NUMEXP")+"'";
                }
                expedientesOrden[nNoIncluidos-1]=decItem.getString("NUMEXP");
            }
        }
        
        if (nNoIncluidos>0){
        //Hay expedientes de Decretos que no han sido incluidos en Libro registro decretos anterior.
        //Buscamos si tienen fichero pdf asociado:
            //query = query+" AND ("+queryNumExp+") ORDER BY NUMEXP";
            //[eCenpri-Felipe Ticket #157] Se añade la ordenación a la query pues cuando había dos
            //decretos en pdf para un mismo expediente se estaba liando
            query = query+" AND ("+queryNumExp+") ORDER BY ID ASC";
            IItemCollection itemCol = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, query);
            Iterator<?> it = itemCol.iterator();
            
            //File file = null;
            IItem docItem = null;
            int doc=0;
            int nDocIncluidos=0;
            
            File aux = null;
            
            boolean docNoEncontrado = false;
            variosDocNoEncontrado = false;
            
            if (it.hasNext()){
                docItem = (IItem)it.next();
                infoPag = docItem.getString(DocumentosUtil.INFOPAG_RDE);
                //nombre = docItem.getString("NOMBRE");
                //docItem.set("NOMBRE", NOMBRE_DOCUMENTO);
                //Id del primer fichero: docItem.getString("ID")
                //signDocument = new SignDocument(docItem);
                
                LOGGER.warn("- Id: "+docItem.getInt("ID"));
                LOGGER.warn("  Numexp: "+docItem.getString("NUMEXP"));
                
                aSignDocument = new SignDocument(docItem);
                //docItemSignDocument[0] = docItem;
                
                try{
                    aux = DocumentosUtil.getFile(cct, infoPag);
                    
                } catch (Exception e){
                    //throw new ISPACInfo("No se ha encontrado el documento Decreto (id="+docItem.getInt("ID")+") del expediente "+docItem.getString("NUMEXP")+" en el repositorio");
                    if (variosDocNoEncontrado){
                        mensajeRespuesta = mensajeRespuesta + ", " + docItem.getInt("ID")+" ("+docItem.getString("NUMEXP")+")";
                    
                    } else{
                        mensajeRespuesta = mensajeRespuesta + docItem.getInt("ID")+" ("+docItem.getString("NUMEXP")+")";
                    }
                    
                    LOGGER.info("No se mostraba mensaje alguno. " + mensajeRespuesta + ". " + e.getMessage(), e);

                    docNoEncontrado = true;
                    variosDocNoEncontrado = true;
                }
                
                if (!docNoEncontrado){
                    //files[0] = getFile(infoPag, "Decreto", 0);
                    //files[0] = aux;
                    filesInfoPag[0] = infoPag;
                    
                    expedientes[0] = docItem.getString("NUMEXP");
                    nDocIncluidos++;
                    unicos[0] = nDocIncluidos;
                    //aSignDocument[0].getItemDoc().set("NOMBRE", "Libro registro decretos");
                    doc=1;
                }
            }

            while (it.hasNext()){
                docNoEncontrado = false;
                docItem = (IItem)it.next();
                infoPagCon = docItem.getString(DocumentosUtil.INFOPAG_RDE);
                //nombreCon = docItem.getString("NOMBRE");
                
                LOGGER.warn("- Id: "+docItem.getInt("ID"));
                LOGGER.warn("  Numexp: "+docItem.getString("NUMEXP"));
                
                //aSignDocument[doc] = new SignDocument(docItem);
                //docItemSignDocument[doc] = docItem;
                
                try{
                    if(null != aux && aux.exists()){
                        aux.delete();
                    }
                    aux = null; //Fuerzo a liberar este objeto
                    aux = DocumentosUtil.getFile(cct, infoPagCon);
                    
                } catch (Exception e){
                    if (variosDocNoEncontrado){
                        mensajeRespuesta = mensajeRespuesta + ", " + docItem.getInt("ID")+" ("+docItem.getString("NUMEXP")+")";
                    } else {
                        mensajeRespuesta = mensajeRespuesta + docItem.getInt("ID")+" ("+docItem.getString("NUMEXP")+")";
                    }
                    
                    LOGGER.info("No se mostraba mensaje alguno. " + mensajeRespuesta + ". " + e.getMessage(), e);
                    
                    docNoEncontrado = true;
                    variosDocNoEncontrado = true;
                }
                
                if (!docNoEncontrado){
                    //files[doc] = aux;
                    filesInfoPag[doc] = infoPagCon;
                    
                    expedientes[doc] = docItem.getString("NUMEXP");
                    //unicos[doc] = true;
                    boolean reemplaza=false;
                    for (int iExpedientes=0; iExpedientes<doc; iExpedientes++){
                        if(docItem.getString("NUMEXP").equals(expedientes[iExpedientes]) && unicos[iExpedientes]>0){
                            //Si ya había un Decreto en ese expediente lo reemplazamos por el Decreto más reciente
                            reemplaza=true;
                            unicos[doc]=unicos[iExpedientes];
                            unicos[iExpedientes] = 0;
                        }
                    }
                    
                    if (!reemplaza){
                        nDocIncluidos++;
                        unicos[doc]=nDocIncluidos;
                    }
                    doc++;
                }
            }
            
            if (doc>0){
            //Hay documentos pdf de Decretos que aún no han sido incluidos en un Libro registro decretos anterior     
                //concatenaPdf(files, unicos, expedientes, expedientesOrden, nNoIncluidos, doc, cuentaUnicos);
                
                File aux2 = concatenaPdf(filesInfoPag, unicos, expedientes, expedientesOrden, nNoIncluidos, doc);
                
                    //InputStream inConcatenado = new FileInputStream(files[0]);
                    InputStream inConcatenado = new FileInputStream(aux2);

                    // Validar la extensión del fichero
                    //String sFile = files[0].getName();
                    
                    //int index = sFile.lastIndexOf(".");
                    
                    //String sExtension = sFile.substring(index + 1, sFile.length());
                    String sExtension = "pdf";
                    
                    //String sName = sFile.substring(0, index);
                    String sName = NOMBRE_DOCUMENTO;//Campo DESCRIPCION de la tabla SPAC_DT_DOCUMENTOS
                    
                    Calendar c = new GregorianCalendar();
                    String dia = Integer.toString(c.get(Calendar.DATE));
                    String mes = Integer.toString(c.get(Calendar.MONTH)+1);//Los meses van del 0 al 11.
                    String annio = Integer.toString(c.get(Calendar.YEAR));
                    
                    if (dia.length()==1){
                        dia="0"+dia;
                    }
                    if (mes.length()==1){
                        mes="0"+mes;
                    }
                    
                    String fecha = dia+"/"+mes+"/"+annio;
                    sName = sName+"-"+fecha;
                    //MimetypeMapping mime = MimetypeMapping.getInstance();
                    //String sMimeType = mime.getMimetype(sExtension);
                    String sMimeType = MimetypeMapping.getMimeType(sExtension);
            
                    if (sMimeType == null) {
                        
                        if(null != inConcatenado) {
                            inConcatenado.close();
                        }
                        throw new ISPACInfo(getResources(request).getMessage("exception.message.typeNotAttach"));
                    }
            
                    int state = currentState.getState();
                    int stageId = currentState.getStageId();
                    int taskId = currentState.getTaskId();
                    int activityId = currentState.getActivityId();
                    int taskPcdId = currentState.getTaskPcdId();
                    int keyId = 0;
                    
                    IItem entityDocument = null;
                
                    String[] ids = null;
                    List<String> oks = new ArrayList<String>();
                    List<String> errors = new ArrayList<String>();
                    
                    switch (state) {
                        case ManagerState.PROCESSESLIST :
                        case ManagerState.SUBPROCESSESLIST:
                            ids = StringUtils.split(cct.getSsVariable(ISPACVariable.STAGES_ACTIVITES_DOCUMENTS_GENERATION), '-');
                            cct.deleteSsVariable(ISPACVariable.STAGES_ACTIVITES_DOCUMENTS_GENERATION);
                        break;
                        
                        case ManagerState.EXPEDIENT:
                            ids = new String[]{String.valueOf(stageId)};
                        break;
                        case ManagerState.SUBPROCESS:
                            ids = new String[]{String.valueOf(activityId)};
                        break;
                        case ManagerState.TASK:
                            ids = new String[]{String.valueOf(taskId)};
                        break;
                        default:
                            //No se hace nada por defecto
                    }
                    
                    for (int i = 0; i < ids.length; i++) {
                        LOGGER.warn("For ids. id="+i);
                        int currentId = Integer.parseInt(ids[i]);
                        try {
                            
                            // Abrir transacción para que no se pueda generar un documento sin fichero
                            cct.beginTX();
                            
                            // Si se recibe el documentTypeId significa que se va a crear un nuevo documento con un fichero anexado
                            // en caso contrario, se sustituirá el fichero del documento ya existente
                            String sParameter = "" + docItem.getInt("ID_TPDOC");//
                            
                            // Crear el documento del tipo recibido
                            int documentTypeId = Integer.parseInt(sParameter);
                            
                            switch (state) {
                            
                                case ManagerState.EXPEDIENT :
                                case ManagerState.PROCESSESLIST:
                                    entityDocument = genDocAPI.createStageDocument(currentId, documentTypeId);
                                    break;
                                    
                                case ManagerState.SUBPROCESS :
                                case ManagerState.SUBPROCESSESLIST:
                                    entityDocument = genDocAPI.createActivityDocument(currentId, taskId, taskPcdId, documentTypeId);
                                    break;
                                    
                                case ManagerState.TASK :
                                    entityDocument = genDocAPI.createTaskDocument(currentId, documentTypeId);
                                    break;
                                
                                default :
                                    // Cerrar la conexión
                                    cct.endTX(false);
                                    
                                    return null;
                            }
                                
                            keyId = entityDocument.getKeyInt();
                        
                            // Establecer la extensión del documento para mostrar
                            // un icono descriptivo del tipo de documento en la lista de documentos
                            entityDocument.set(DocumentosUtil.EXTENSION, sExtension);
                            entityDocument.store(cct);
                
                            // Fichero a anexar
                            InputStream in = aSignDocument.getDocument();
                            int length = aSignDocument.getLength();
                            
                            Object connectorSession = null;
                            try {
                                connectorSession = genDocAPI.createConnectorSession();
                
                                switch (state) {
                                    
                                    case ManagerState.EXPEDIENT :
                                    case ManagerState.PROCESSESLIST:
                                        genDocAPI.attachStageInputStream(connectorSession, currentId, keyId, in, length, sMimeType, sName);
                                        break;
                                        
                                    case ManagerState.SUBPROCESS:
                                    case ManagerState.SUBPROCESSESLIST:
                                        genDocAPI.attachStageInputStream(connectorSession, currentId, keyId, in, length, sMimeType, sName);
                                        break;
                                        
                                    case ManagerState.TASK :
                                        //genDocAPI.attachTaskInputStream(connectorSession, currentId, keyId, inConcatenado, length+length2, sMimeType, sName);//
                                        genDocAPI.attachTaskInputStream(connectorSession, currentId, keyId, inConcatenado, length, sMimeType, sName);//
                                        break;
                                        
                                    default :
                                        // Cerrar la conexión
                                        cct.endTX(false);
                                    
                                        return null;
                                }
                                
                            } finally {
                                if (connectorSession != null) {
                                    genDocAPI.closeConnectorSession(connectorSession);
                                }
                            }
                            
                            // Si ha sido correcto se hace commit de la transacción
                            cct.endTX(true);
                            
                        } catch (Exception e) {
                            
                            LOGGER.info("No se mostraba mensaje alguno. " + e.getMessage(), e);
                            // Si se produce algún error se hace rollback de la transacción
                            cct.endTX(false);
                            
                            // Si se produce algún error
                            // el nuevo documento se borra para que no haya documentos sin ficheros
                            /*
                            if (entityDocument != null) {        
                                entityDocument.delete(cct);
                            }
                            */
                            switch (state) {
                                case ManagerState.PROCESSESLIST :
                                case ManagerState.SUBPROCESSESLIST:
                                    IStage stage = cct.getAPI().getStage(currentId);
                                    errors.add(stage.getString("NUMEXP"));                        
                                    break;
                                default:
                                    throw new ISPACInfo(getResources(request).getMessage("exception.message.canNotAttach")+ ": " + e.getMessage());
                            }
                            continue;
                        }
                        if (state== ManagerState.PROCESSESLIST || state == ManagerState.SUBPROCESSESLIST) {
                                IStage stage = cct.getAPI().getStage(currentId);
                                oks.add(stage.getString("NUMEXP"));                        
                        }
                    }
                    if (state== ManagerState.PROCESSESLIST || state == ManagerState.SUBPROCESSESLIST) {
                        request.setAttribute(ActionsConstants.OK_UPLOAD_FILES_LIST, oks);
                        request.setAttribute(ActionsConstants.ERROR_UPLOAD_FILES_LIST, errors);
                    }
                    
                //Cerramos los ficheros
                for(int k=0;k<doc;k++){
                    //files[k].delete();
                    //files[k]=null;
                    //aSignDocument[k].getDocument().close();
                    //aSignDocument[k]=null;
                    
                    //aSignDocument.getDocument().close();
                    //aSignDocument=null;
                }
                //files=null;
                //aSignDocument=null;
                expedientes=null;
                unicos=null;
                
                query = "WHERE ID = "+keyId+"";
                itemCol = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, query);
                it = itemCol.iterator();
                while (it.hasNext()){
                    docItem = (IItem)it.next();
                    docItem.set("NOMBRE", NOMBRE_DOCUMENTO);
                    docItem.store(cct);
                }
                
                if ("CRE".equalsIgnoreCase(tipoDec)){//Tipo de Decreto de creación (no de selección)
                    //Marcamos los decretos añadidos al libro como ya incluidos
                    strQuery = "WHERE INCLUIDO_LIBRO IS NULL OR INCLUIDO_LIBRO = '0' AND ("+queryNumExp+")";
                    collectionNoIncluidos = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
                    itNoIncluidos = collectionNoIncluidos.iterator();
                    
                    while (itNoIncluidos.hasNext()){
                        decItem = (IItem)itNoIncluidos.next();
                        decItem.set("INCLUIDO_LIBRO", 1);
                        decItem.store(cct);
                    }
                }
                
                if (variosDocNoEncontrado){
                    throw new ISPACInfo(mensajeRespuesta);
                
                } else {
                    throw new ISPACInfo("Se ha creado el Libro registro decretos correctamente");
                }
                
            }//if (doc>0){
            
            if(null != aux && aux.exists()){
                aux.delete();
            }
            
        }//if (nNoIncluidos>0){
        
        if (variosDocNoEncontrado){
            throw new ISPACInfo(mensajeRespuesta);
            
        } else {
            throw new ISPACInfo("No se han encontrado Decretos con los criterios indicados sin incluir en libros de registro anteriores");
        }
        
        
    }
    
    private boolean isDecretoValido(IItem decItem,Date dFechaIniDec,Date dFechaFinDec,int iInicioDec,int iFinDec) throws ISPACException{
        
        Date fDecreto = null;
        
        try {
            fDecreto = decItem.getDate("FECHA_DECRETO");
        } catch (ISPACException e) {
            LOGGER.error("ERROR. " + e.getMessage(), e);
        }
        int nDecreto = decItem.getInt("NUMERO_DECRETO");
        
        if(dFechaIniDec!=null && fDecreto.before(dFechaIniDec)){
            return false;
        }
        if(dFechaFinDec!=null && fDecreto.after(dFechaFinDec)){
            return false;
        }
        if(iInicioDec != -1 && nDecreto < iInicioDec){
            return false;
        }
        if(iFinDec != -1 && nDecreto > iFinDec){
            return false;
        }
        return true;
    }
    
    
    private File getFile(String infoPag) throws ISPACException{

        IGenDocAPI gendocAPI = clientContext.getAPI().getGenDocAPI();
        Object connectorSession = null;
        try {
            connectorSession = gendocAPI.createConnectorSession();
            File file = null;
            
            try{
                //String extension = MimetypeMapping.getInstance().getExtension(gendocAPI.getMimeType(connectorSession, infoPag));
                String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, infoPag));
                
                //Se almacena documento y firma en el gestor documental
                String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
                fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
                
                OutputStream out = new FileOutputStream(fileName);
                gendocAPI.getDocument(connectorSession, infoPag, out);
                
                file = new File(fileName);
                
                //aSignDocument[doc].setDocument(new FileInputStream(file));
                aSignDocument.setDocument(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new ISPACException(e);
            }
            return file;
        } finally {
            if (connectorSession != null) {
                gendocAPI.closeConnectorSession(connectorSession);
            }
        }        
    }
    
    //private void concatenaPdf(File[] files, int[] unicos, String[] expedientes, String[] expedientesOrden, int nNoIncluidos, int doc, int cuentaUnicos) {
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private File concatenaPdf(String[] filesInfoPag, int[] unicos, String[] expedientes, String[] expedientesOrden, int nNoIncluidos, int doc) {
    
        File resultado = null;
        
        try {
            
            /*
            InputStream args[] = new InputStream[doc];
            for(int i=0;i<doc;i++){
                args[i]= new FileInputStream(files[i]);
            }
            */
            int pageOffset = 0;
            List<?> master = new ArrayList();
            //int f = 0;
            
            Document document = null;
            PdfCopy  writer = null;
            boolean primero = true;//Indica si es el primer fichero (sobre el que se concatenarán el resto de ficheros)
            
            //Recorremos los expedientes ordenados. Para cada uno lo buscamos en el array de expedientes y si su valor
            //en unicos es >0 es que es válido y hay que anexarlo al PDF global
            String expediente = null;
            
            for (int j=0; j<nNoIncluidos; j++) {
                LOGGER.warn("Concatena id="+j);
                
                expediente = expedientesOrden[j];
                int iExpedientes=0;//indice para recorrer el array expedientes en busca del expediente obtenido de expedientesOrdenados
                
                while (iExpedientes<doc && (!expediente.equals(expedientes[iExpedientes]) || (expediente.equals(expedientes[iExpedientes]) && unicos[iExpedientes]==0))){
                    //for (recorrer el array que hay q pasar por parametro de expedientes[], q esta ordenado por numdecreto)
                    //para cada uno recorrer unicos hasta encontrar donde esta el true y adjuntarlo
                    iExpedientes++;
                }
            
                if (iExpedientes < doc){//Se ha encontrado el expediente (el decreto tiene documento firmado por el presidente (nºdecreto asignado)
                    // Creamos un reader para el documento
                    PdfReader reader = null;
                    //reader = new PdfReader(aSignDocument[f].getDocument());
                    
                    //reader = new PdfReader(aSignDocument[iExpedientes].getDocument());
                    getFile(filesInfoPag[iExpedientes]);
                    reader = new PdfReader(aSignDocument.getDocument());
                    
                    
                    reader.consolidateNamedDestinations();
                    int n = reader.getNumberOfPages();
                    List bookmarks = SimpleBookmark.getBookmark(reader);
                    
                    if (bookmarks != null) {
                        if (pageOffset != 0){
                            SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                        }
                        master.addAll(bookmarks);
                    }
                    
                    pageOffset += n;
                    
                    if (primero) {
                        // Creamos un objeto Document
                        document = new Document(reader.getPageSizeWithRotation(1));
                        // Creamos un writer que escuche al documento
                        
                        //writer = new PdfCopy(document, new FileOutputStream(files[0]));
                        resultado = getFile(filesInfoPag[0]);
                        writer = new PdfCopy(document, new FileOutputStream(resultado));
                        
                        writer.setViewerPreferences(PdfCopy.PageModeUseOutlines);//
                        // Abrimos el documento
                        document.open();
                        primero = false;
                    }
                    
                    // Añadimos el contenido
                    PdfImportedPage page;
                    
                    document.newPage();
                    
                    for (int i = 1; i < n; i++) {
                        page = writer.getImportedPage(reader, i);
                        writer.addPage(page);
                    }
                }//if (iExpedientes < doc){
            }//for (int j=0; j<nDocIncluidos; j++){
                
            if (!master.isEmpty()){
                writer.setOutlines(master);
            }
            document.close();
            
        } catch(Exception e) {
            LOGGER.error("ERROR. " + e.getMessage(), e);
        }
        
        return resultado;
        
    }
}
