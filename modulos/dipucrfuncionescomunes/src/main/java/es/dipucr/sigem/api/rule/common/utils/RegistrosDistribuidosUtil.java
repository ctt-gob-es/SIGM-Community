package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInboxAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.connector.IDocConnector;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.expedients.Document;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.ExpedientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;
import ieci.tdw.ispac.ispaclib.dao.cat.CTTpDocDAO;
import ieci.tdw.ispac.ispaclib.dao.entity.EntityDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.entity.DocumentData;
import ieci.tdw.ispac.ispaclib.gendoc.DMConnectorFactory;
import ieci.tdw.ispac.ispaclib.gendoc.DMDocumentManager;
import ieci.tdw.ispac.ispaclib.sicres.vo.Annexe;
import ieci.tdw.ispac.ispaclib.sicres.vo.Intray;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class RegistrosDistribuidosUtil {
	
	private static final Logger logger = Logger.getLogger(RegistrosDistribuidosUtil.class);

	private static final String DOC_ORIGIN_TELEMATIC 	= "REGISTRO TELEMÁTICO";
	private static final String DOC_ORIGIN_PRESENTIAL	= "REGISTRO PRESENCIAL";
	private static final String DOC_REG_TYPE			= "ENTRADA";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean addDocuments(ClientContext cct, String numExp, Intray intray) throws ISPACException {

    	if (StringUtils.isNotBlank(numExp) && (intray != null)) {

			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IInboxAPI inboxAPI = invesFlowAPI.getInboxAPI();

			// Identificadores de los documentos
			Annexe[] annexes = inboxAPI.getAnnexes(intray.getId());
			if (annexes != null) {

				IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();

				// Identificador de la fase activa del expediente
				int activaStageId = getActiveStageId(cct, numExp);
				
				// Lista de referencias de los documentos creados en el gestor
				// documental para eliminarlos en caso de error
				List documentRefs = new ArrayList();
		
				try {
					
					// Gestor de ficheros temporales
					FileTemporaryManager fileTempMgr = FileTemporaryManager.getInstance();
					
					for (int i = 0; i < annexes.length; i++) {

						// Información del anexo
						Annexe annex = annexes[i];

						File file = null;
						
						try {
							
							// Crear fichero temporal
							file = fileTempMgr.newFile();
							logger.info("file "+file.getAbsolutePath());
							
							logger.info("intray.getId() "+intray.getId());
							logger.info("annex.getId() "+annex.getId());
							

							// Obtener fichero del anexo
							FileOutputStream out = new FileOutputStream(file);
							logger.info("out "+out);
							inboxAPI.getAnnexe(intray.getId(), annex.getId(), out);
							out.close();
							logger.info("file "+file);
							logger.info("file "+file.length());
							
							// Componer información del documento
							Document doc = new Document();
							doc.setCode(ISPACConfiguration.getInstance().get(ISPACConfiguration.SICRES_INTRAY_DEFAULT_DOCUMENT_TYPE));
							doc.setName(annex.getName());
							doc.setExtension(annex.getExt());
							doc.setLength(new Long(file.length()).intValue());
							FileInputStream in = new FileInputStream(file);
							doc.setContent(in);
							
							// Crear documento en la fase activa
							IItem docEntity = createStageDocument(cct, activaStageId, 
									intray.getRegisterNumber(), intray.getRegisterDate(), 
									doc, false); 
							logger.info("docEntity "+docEntity);
							
							in.close();
							
							// Añadir el GUID del documento a la lista
							documentRefs.add(docEntity.getString("INFOPAG"));
							
						} finally {
							
							// Eliminar el fichero temporal
							if (file != null) {
								fileTempMgr.delete(file);
							}
						}
					}
					
				} catch (Exception e) {
		
					logger.error("Error al añadir documentos al expediente", e);
					
					// Eliminar los ficheros subidos al gestor documental
					deleteAttachedFiles(genDocAPI, documentRefs);
		
					throw new ISPACException("Error al anexar documentos al expediente", e);
				}
    		}
    	}
    	
		return true;
	}
	 
    private static IItem createStageDocument(ClientContext cct, int stageId, String regNum, Date regDate, 
    		Document doc, boolean telematic) throws ISPACException {

		IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();

		// Obtener el tipo MIME
    	String mimeType = MimetypeMapping.getMimeType(doc.getExtension());
    	
		// Validar el tipo de documento
		int tpdoc = DocumentosUtil.getTipoDoc(cct, doc.getCode(), DocumentosUtil.BUSQUEDA_EXACTA, false);

    	// Generar un documento asociado la fase
		IItem docEntity = createStageDocument(cct, stageId,	tpdoc);
		
		// Establecer los datos del documento
		docEntity.set("ORIGEN", telematic ? DOC_ORIGIN_TELEMATIC : DOC_ORIGIN_PRESENTIAL);
		docEntity.set("TP_REG", DOC_REG_TYPE);
		docEntity.set("EXTENSION", doc.getExtension());
		docEntity.set("NREG", regNum);
		docEntity.set("FREG", regDate);
		docEntity.store(cct);
				
		Object connectorSession = null;
		try {
			connectorSession = genDocAPI.createConnectorSession();

			// Subir el fichero al gestor documental
			docEntity = attachStageInputStream(cct, connectorSession, stageId, 
					docEntity.getKeyInt(), doc.getContent(), doc.getLength(), 
					mimeType, doc.getName());
		}finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}				
		return docEntity;
    }
    
    private static int getActiveStageId(ClientContext cct, String numExp) throws ISPACException {
    	
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		String sql = new StringBuffer("WHERE NUMEXP = '").append(DBUtil.replaceQuotes(numExp)).append("'").toString();
		
		IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_FASES, sql);
		IItem item = collection.value();
		return item.getKeyInt();
    }
    

    /**
     * Eliminar los documentos físicos del gestor documental.
     * 
     * @param genDocAPI API de generacón de documentos
     * @param documentRefs Lista de referencias a los documentos creados en el gestor documental.
     */
    @SuppressWarnings("rawtypes")
	private static void deleteAttachedFiles(IGenDocAPI genDocAPI,
    						  		 List documentRefs) {
    	
    	if (!documentRefs.isEmpty()) {
    		
    		Iterator it = documentRefs.iterator();
    		while (it.hasNext()) {
    			
    			String docref = (String) it.next();
    			
		    	if ((docref != null) &&
			    	(!docref.equals(""))) {
		    		
		    		Object connectorSession = null;
		    		try {
		    			connectorSession = genDocAPI.createConnectorSession();
		    			// Eliminar el documento fisico del gestor documental
			            genDocAPI.deleteDocument(connectorSession, docref);
			    	}
		    		catch (Exception e) {}
		    		finally {
						if (connectorSession != null) {
							try{
								genDocAPI.closeConnectorSession(connectorSession);
							}catch(Exception e){}
						}
			    	}
		    	}
    		}
    	}
    }
    
    public static IItem createStageDocument(ClientContext cct, int stageId,int docType) throws ISPACException {
    	ExpedientContext expctx=new ExpedientContext(cct);
    	expctx.setStage(stageId);
    	
    	return createDocument(cct, expctx,docType);
    }
    
    
    private static IItem createDocument(ClientContext cct, ExpedientContext ctx,int docType)
	throws ISPACException
	{
		DbCnt cnt = null;

		try
		{
			cnt = cct.getConnection();

			CTTpDocDAO tpdoc = new CTTpDocDAO(cnt,docType);
			DocumentData docdata =
			new DocumentData(ctx.getNumExp(),
			        		 tpdoc.getInt("ID"),
							 ctx.getStagePCD(),
							 ctx.getStage(),
							 ctx.getTaskPCD(),
							 ctx.getTask(),
							 tpdoc.getString("NOMBRE"),
							 cct.getUser().getUID(),
							 cct.getUser().getName(),
							 tpdoc.getString("TIPOREG"));
			docdata.setEntity( ctx.getEntity());
			docdata.setKey( ctx.getKey());

			
			DMDocumentManager manager = new DMDocumentManager(cct,ctx);

			EntityDAO document = manager.createDocumentEntity(docdata);
			document.store(cnt);

			//Ejecutar eventos asociados a la creación del documento.

			//Se añaden propiedades extra para el contexto de ejecución de la regla.
			ctx.addContextParams(docdata.getRuleParameters());

/*			ctx.addContextParam(RuleProperties.RCTX_DOCUMENTID,String.valueOf(docdata.getDocId()));
			ctx.addContextParam(RuleProperties.RCTX_DOCUMENTTYPE,String.valueOf(docdata.getDocType()));
			ctx.addContextParam(RuleProperties.RCTX_DOCUMENTNAME,docdata.getName());
			ctx.addContextParam(RuleProperties.RCTX_DOCUMENTAUTHOR,docdata.getAuthor());
*/
			//String stateTicket = cct.getTicket();
			ITXTransaction txapi=cct.getAPI().getTransactionAPI();
			//String stateTicket = "113|226|9499|230|921|15611|6393|"+ctx.getNumExp()+"|6|7|15617|true|false|0|0|0|0|0|0";
			StateContext stateContext = new StateContext();;
			
			if (stateContext.getActivityId() != 0) {
				txapi.executeEvents(EventsDefines.EVENT_OBJ_ACTIVITY,ctx.getStagePCD(),EventsDefines.EVENT_DOCUMENT_NEW,ctx);
			} else if (ctx.getTaskPCD() != 0) {
			    txapi.executeEvents(EventsDefines.EVENT_OBJ_TASK,ctx.getTaskPCD(),EventsDefines.EVENT_DOCUMENT_NEW,ctx);
			} else if (ctx.getStagePCD()!= 0) {
			    txapi.executeEvents(EventsDefines.EVENT_OBJ_STAGE,ctx.getStagePCD(),EventsDefines.EVENT_DOCUMENT_NEW,ctx);
			}

			return document;
		}
		finally
		{
			cct.releaseConnection(cnt);
		}
	}
    
    /**
	 * Anexa un stream al documento en el contexto de una fase.
	 * @param stageId identificador de la fase.
	 * @param docId identificador del documento.
	 * @param in stream
	 * @param length longitud del stream
	 * @param sMimeType tipo mime del fichero
	 * @param sName Nombre del fichero
	 */
	public static IItem attachStageInputStream(ClientContext cct, Object connectorSession, int stageId,int docId,InputStream in,int length,String sMimeType,String sName)
	throws ISPACException
	{
		ExpedientContext expctx=new ExpedientContext(cct);
		expctx.setStage(stageId);

		return attachInputStream(cct,connectorSession,null,expctx,docId,in,length,sMimeType,sName,null);
	}
	
	public IItem attachStageInputStream(ClientContext cct, Object connectorSession, Object obj, int stageId, int docId, InputStream in, int length, String sMimeType, String sName, String sProperties) throws ISPACException {
		ExpedientContext expctx=new ExpedientContext(cct);
		expctx.setStage(stageId);

		return attachInputStream(cct, connectorSession,obj,expctx,docId,in,length,sMimeType,sName,sProperties);
	}
    
	private static IItem attachInputStream(ClientContext cct, Object connectorSession, Object obj, ExpedientContext ctx,int docId,InputStream in,int length,String sMimeType,String sName, String sProperties)
	throws ISPACException
	{
		DbCnt cnt = null;
		String sDocRefAnt = null;
		String sDocRefNew = null;
		IDocConnector connector = null;

		try {
			
			cnt = cct.getConnection();

			// Obtiene el conector de almacenamiento
			if (obj != null)
				connector = DMConnectorFactory.getInstance(cct).getConnector(obj);
			else
				connector = DMConnectorFactory.getInstance(cct).getConnector();

			DMDocumentManager manager = new DMDocumentManager(cct,ctx);

			EntityDAO document = manager.getDocumentEntity(docId);

			CTTpDocDAO tpdoc = new CTTpDocDAO(cnt,document.getInt("ID_TPDOC"));
			
			String documentName = sName;
			if (StringUtils.indexOf(documentName, ".") == -1)
				documentName += "."+document.getString("EXTENSION");
			
			DocumentData docdata =
			new DocumentData(ctx.getNumExp(),
			        		 tpdoc.getInt("ID"),
							 ctx.getStagePCD(),
							 ctx.getStage(),
							 ctx.getTaskPCD(),
							 ctx.getTask(),
							 //tpdoc.getString("NOMBRE"),
							 documentName,
							 cct.getUser().getUID(),
							 cct.getUser().getName(),
							 tpdoc.getString("TIPOREG"));
			docdata.setEntity( ctx.getEntity());
			docdata.setKey( ctx.getKey());

			docdata.setDoc(document.getKeyInt());
			docdata.setMimeType( sMimeType);

			
			// Obtiene el documento XML con las propiedades del documento
			if (sProperties == null)
				sProperties = manager.getProperties(docdata);

			// Referencia al fichero del documento
			sDocRefAnt = document.getString("INFOPAG");
			
			// Referencia al nuevo fichero del documento
			sDocRefNew = connector.newDocument(connectorSession, in, length, sProperties);
			
			// Actualizar el documento
			document.set("INFOPAG", sDocRefNew);
			document.set("ID_PLANTILLA", ((Integer)null));
			if (sName != null) {
				document.set("DESCRIPCION", sName);
			}
			document.store(cnt);
			
			docdata.setDocRef(sDocRefNew);
			
			// Controlar error en los eventos para eliminar el nuevo fichero:
			// - que elimina el fichero cuando el documento es nuevo
			// - que restaura el fichero anterior cuando el documento es sustituido
			try {

				// Ejecutar eventos asociados al anexar un fichero
				ctx.addContextParams(docdata.getRuleParameters());
	
				/*
				ctx.addContextParam(RuleProperties.RCTX_DOCUMENTID,String.valueOf(docdata.getDocId()));
				ctx.addContextParam(RuleProperties.RCTX_DOCUMENTTYPE,String.valueOf(docdata.getDocType()));
				ctx.addContextParam(RuleProperties.RCTX_DOCUMENTNAME,docdata.getName());
				ctx.addContextParam(RuleProperties.RCTX_DOCUMENTAUTHOR,docdata.getAuthor());
				ctx.addContextParam(RuleProperties.RCTX_DOCUMENTMIMETYPE,docdata.getMimeType());
				ctx.addContextParam(RuleProperties.RCTX_DOCUMENTREF,sDocRef);
				ctx.addContextParam(RuleProperties.RCTX_TEMPLATEID,String.valueOf(docdata.getTemplate()));
				ctx.addContextParam(RuleProperties.RCTX_TEMPLATENAME,filename);
				 */
	
				ITXTransaction txapi = cct.getAPI().getTransactionAPI();
				StateContext stateContext = new StateContext();
				
				if (stateContext.getActivityId() != 0) {
					txapi.executeEvents(EventsDefines.EVENT_OBJ_ACTIVITY,ctx.getStagePCD(),EventsDefines.EVENT_TEMPLATE_EXTERNAL,ctx);
				} else if (ctx.getTaskPCD() != 0) {
				    txapi.executeEvents(EventsDefines.EVENT_OBJ_TASK,ctx.getTaskPCD(),EventsDefines.EVENT_TEMPLATE_EXTERNAL,ctx);
				} else if (ctx.getStagePCD() != 0) {
				    txapi.executeEvents(EventsDefines.EVENT_OBJ_STAGE,ctx.getStagePCD(),EventsDefines.EVENT_TEMPLATE_EXTERNAL,ctx);
				}
			}
			catch (Exception e) {
				
				// Eliminar el nuevo fichero
				connector.deleteDocument(connectorSession, sDocRefNew);
				
				throw new ISPACException("Error eventos anexar fichero", e);
			}
			
			// Eliminar el fichero anterior
			if (sDocRefAnt != null && !StringUtils.equals(sDocRefAnt, sDocRefNew)) {
				connector.deleteDocument(connectorSession, sDocRefAnt);
			}
			
			return document;
		}
		finally
		{
			cct.releaseConnection(cnt);
		}
	}
}
