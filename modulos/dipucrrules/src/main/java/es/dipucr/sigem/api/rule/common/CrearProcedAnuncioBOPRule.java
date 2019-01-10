package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcedure;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.procedure.PProcedimientoDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.directory.DirectoryConnectorFactory;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryConnector;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryEntry;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.resp.RespFactory;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.security.SecurityMgr;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class CrearProcedAnuncioBOPRule implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(CrearProcedAnuncioBOPRule.class);
	
	private String resp;

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
		
	}
	
	/**
	 * [Teresa] Ticket #233 SIGEM creación de un tramite que se conecte con 
	 * el procedimiento 'Insercción de anuncio Interno'
	 * Para ello se creara el procedimiento 'Inserción de Anuncio Interno en el BOP'
	 * se creará el trámite 'Propuesta de publicación' este trámite generará una plantilla
	 * que se insertarán los datos de la propuesta en dicha plantilla.
	 * **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		OpenOfficeHelper ooHelper = null;
		try {
			/*****************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();
			ooHelper = OpenOfficeHelper.getInstance();
			/*****************************************************************/
			//Identificador del trámite actual
			int nIdTask = rulectx.getStageId();
			
			//relacion con el expediente del boletin
			String relacion="proc.anuncioBOP."+nIdTask;
			
			IItemCollection col = getProcedure(cct, Constants.CONSTANTES.PROCEDIMIENTO_ANUNCIO_INTERNO_BOP);
			
			Iterator it = col.iterator();
			if(!it.hasNext()){
				throw new ISPACInfo("No tiene permisos para iniciar el expediente asociado.");
			}
			
			//Obtengo el ID del procedimiento
			IItem item = null;
			int idProcedure = 0;
			while (it.hasNext()){
				item = ((IItem)it.next());
				idProcedure = item.getInt("ID");
			}
			
			// Obtener el código de procedimiento para el número de expediente
			IItem ctProcedure = entitiesAPI.getEntity(SpacEntities.SPAC_CT_PROCEDIMIENTOS, idProcedure);
			Map params = new HashMap();
			params.put("COD_PCD", ctProcedure.getString("COD_PCD"));

			// Crear el proceso del expediente
			int nIdProcess2 = tx.createProcess(idProcedure, params);
			
			IProcess process = invesflowAPI.getProcess(nIdProcess2);
			String numExpHijo = process.getString("NUMEXP");

			IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
			
			registro.set("NUMEXP_PADRE", rulectx.getNumExp());
			registro.set("NUMEXP_HIJO", numExpHijo);
			//
			registro.set("RELACION", relacion);

			registro.store(cct);
			
			//Hasta aquí ya tenemos creado el procedimiento 'Inserción de Anuncio Interno en el BOP'
			//Ahora toca crear el trámite y añadirle en el anuncio el contenido de la propuesta.
			//Obtiene el número de fase de la propuesta
			
			
			String strQueryAux = "WHERE NUMEXP='" + numExpHijo + "'";
			IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
			Iterator itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				return new Boolean(false);
			}
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			
			//Se busca el id del tramite
			 String consulta = "WHERE ID = "+idFase;
			 IItemCollection iFases = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, consulta);
			 Iterator IFases = iFases.iterator();
			 int idFaseProp=0;
			 
			 while (IFases.hasNext()){
				 IItem fase = (IItem)IFases.next();
				 idFaseProp = fase.getInt("ID_FASE");
			 }
			 
			 consulta = "WHERE ID_FASE = "+idFaseProp;
			 IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, consulta);
			 Iterator ITramiteProp = iTramiteProp.iterator();
			 int idTramite=0;
			 while (ITramiteProp.hasNext()){
				 IItem tramite = (IItem)ITramiteProp.next();
				 idTramite = tramite.getInt("ID");
			 }
			 
			//Creo el tramite Propuesta de publicación 
			int idTramitePropuesta = tx.createTask(idFase, idTramite);
			
			
			
			//CABECERA INICIO 
        	DocumentosUtil.generarDocumento(rulectx, Constants.PLANTILLADOC.BOPANUNCIO, Constants.PLANTILLADOC.BOPANUNCIO, null, numExpHijo, idTramitePropuesta);
        	String strInfoPag = DocumentosUtil.getInfoPagByNombre(numExpHijo, rulectx, Constants.PLANTILLADOC.BOPANUNCIO);
        	
        	File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
        	XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
    		file.delete();
    		//FIN CABECERA
    		
    		//Cuerpo
    		//Cojo el contenido de la propuesta
    		int taskIdExpedienteInicial = nIdTask;
    		IItemCollection colProp = entitiesAPI.getDocuments(rulectx.getNumExp(), "UPPER(NOMBRE) LIKE '%"+Constants.CONSTANTES.PROPUESTA+"%' AND ID_TRAMITE != "+taskIdExpedienteInicial, "");
    		Iterator itProp = colProp.iterator();
    		IItem iPropuesta = (IItem)itProp.next();
	        String infopag = iPropuesta.getString("INFOPAG");
	        
        	file = DocumentosUtil.getFile(cct, infopag, null, null);
        	DipucrCommonFunctions.concatena(xComponent, "file://" + file.getPath());
    		file.delete();
			
    		//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_ODT);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			
			//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, Constants.PLANTILLADOC.BOPANUNCIO, DocumentosUtil.BUSQUEDA_EXACTA, false);

			DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropuesta, tpdoc, Constants.PLANTILLADOC.BOPANUNCIO, file, Constants._EXTENSION_ODT);
			
			//Borra los documentos intermedios del gestor documental
	        IItemCollection collection = DocumentosUtil.getDocumentsByDescripcion(numExpHijo, rulectx, "BOP - Anuncio doc old");
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
	        		
			return new Boolean(true);
    	} catch(Exception e) {
    		logger.error("No se ha podido realizar la publicacion del anuncio en el BOP. " + e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido realizar la publicacion del anuncio en el BOP. " + e.getMessage(), e);        	
        } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
	}
	
	public IItemCollection getProcedure(ClientContext context, String strProcHijo) throws ISPACException
	{
		// Los que puede crear el usuario y los que pueden crear los que sustituye
		String resp = getSubstitutesRespString(context);
		//Con este metodo que esta en comentarios accede al fichero ApplicationResources.properties
		//de ispac-mgr-webapp
		//String nombreProcedure=getString("procedimiento."+strProcHijo+".asociado.nombre");
		String nombreProcedure = strProcHijo;
		
		DbCnt cnt = null;
		try
		{
			cnt = context.getConnection();
			CollectionDAO pcdset = new CollectionDAO(PProcedimientoDAO.class);

			/* Procedimientos en vigor */
			String sqlquery = "WHERE ESTADO=" + IProcedure.PCD_STATE_CURRENT
			+ " AND TIPO=" + IProcedure.PROCEDURE_TYPE
			+ " AND ID IN" 
			+ " (SELECT ID_PCD FROM SPAC_SS_PERMISOS WHERE  PERMISO="
			+ ISecurityAPI.ISPAC_RIGHTS_CREATEEXP + DBUtil.addAndInResponsibleCondition("UID_USR", resp) + ")"
			+ " AND NOMBRE = '"+nombreProcedure+"' ORDER BY NOMBRE";
			pcdset.query(cnt,sqlquery);

			IItemCollection col = pcdset.disconnect();


			return col;
		}
		catch (ISPACException ie)
		{
			throw new ISPACException("Error en WLWorklist:getProcs()", ie);
		}
		finally
		{
			context.releaseConnection(cnt);
		}
	}
	
	private String getSubstitutesRespString(ClientContext context) throws ISPACException
	{
		if (StringUtils.isEmpty(resp)) {

			StringBuffer respList=new StringBuffer();
			DbCnt cnt = context.getConnection();

			try
			{
				Responsible user = context.getUser();
				respList.append(user.getRespString());


				SecurityMgr security = new SecurityMgr(cnt);

				// Sustituir (sustituidos por el usuario y por los grupos a los que pertenece el usuario)
				IItemCollection collection = security.getAllSubstitutes(user);
				while (collection.next()) {

					IItem substitute = (IItem) collection.value();
					respList.append("," + getRespStringFromEntryUID(substitute.getString("UID_SUSTITUIDO")));
				}

			}
			finally
			{
				context.releaseConnection( cnt);
			}

			resp = respList.toString();
		}

		return resp;
	}
	
	private String getRespStringFromEntryUID(String entryUID) throws ISPACException {

		IDirectoryConnector directory = DirectoryConnectorFactory.getConnector();

		IDirectoryEntry entry = directory.getEntryFromUID(entryUID);
		Responsible resp= RespFactory.createResponsible(entry);

		return resp.getRespString();
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}
}
