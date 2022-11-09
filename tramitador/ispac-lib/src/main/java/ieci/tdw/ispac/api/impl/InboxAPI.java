package ieci.tdw.ispac.api.impl;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInboxAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRegisterAPI;
import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.IThirdPartyAPI;
import ieci.tdw.ispac.api.IWorklistAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.expedients.Expedients;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.item.Properties;
import ieci.tdw.ispac.api.item.Property;
import ieci.tdw.ispac.api.item.util.ListCollection;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.audit.IspacAuditConstants;
import ieci.tdw.ispac.audit.business.IspacAuditoriaManager;
import ieci.tdw.ispac.audit.business.manager.impl.IspacAuditoriaManagerImpl;
import ieci.tdw.ispac.audit.business.vo.AuditContext;
import ieci.tdw.ispac.audit.business.vo.events.IspacAuditEventAvisoConsultaVO;
import ieci.tdw.ispac.audit.business.vo.events.IspacAuditEventRegDistConsultaVO;
import ieci.tdw.ispac.audit.business.vo.events.IspacAuditEventRegDistModificacionVO;
import ieci.tdw.ispac.audit.config.ConfigurationAuditFileKeys;
import ieci.tdw.ispac.audit.config.ConfiguratorAudit;
import ieci.tdw.ispac.audit.context.AuditContextHolder;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.InboxContext;
import ieci.tdw.ispac.ispaclib.dao.wl.DeadLineDAO;
import ieci.tdw.ispac.ispaclib.item.GenericItem;
import ieci.tdw.ispac.ispaclib.notices.Notices;
import ieci.tdw.ispac.ispaclib.sicres.RegisterHelper;
import ieci.tdw.ispac.ispaclib.sicres.vo.Annexe;
import ieci.tdw.ispac.ispaclib.sicres.vo.Intray;
import ieci.tdw.ispac.ispaclib.sicres.vo.Register;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterInfo;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterType;
import ieci.tdw.ispac.ispaclib.sicres.vo.ThirdPerson;
import ieci.tdw.ispac.ispaclib.sign.SignConnectorFactory;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.OutputStream;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.TercerosUtil;

/**
 * API para gestionar la informaci�n de la bandeja de entrada.
 *
 */
public class InboxAPI implements IInboxAPI {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(InboxAPI.class);


	/**
	 * Contexto de cliente.
	 */
	private final ClientContext m_ctx;

	private IRegisterAPI registerAPI;

	//[Manu #93] * ALSIGM3 Modificaciones Auditor�a
	private IspacAuditoriaManager auditoriaManager;

	/**
	 * Constructor.
	 * @param context Contexto de cliente.
	 * @throws ISPACException
	 */
	public InboxAPI(ClientContext context) throws ISPACException {
		this.m_ctx = context;
		registerAPI = context.getAPI().getRegisterAPI();
		
		//[Manu #93] * ALSIGM3 Modificaciones Auditor�a
    	if(ConfiguratorAudit.getInstance().getPropertyBoolean(ConfigurationAuditFileKeys.KEY_AUDITORIA_ENABLE))
    		auditoriaManager = new IspacAuditoriaManagerImpl();
	}

	/* (non-Javadoc)
	 * @see ieci.tdw.ispac.api.IInboxAPI#getIntrays()
	 */
	public IItemCollection getInbox() throws ISPACException {

	    IInvesflowAPI invesflowAPI = m_ctx.getAPI();
	    IWorklistAPI worklistAPI = invesflowAPI.getWorkListAPI();

		String resp = worklistAPI.getRespString();
		return getInbox(resp);
	}

	public IItemCollection getInbox(String resp) throws ISPACException {

		List inboxItems = new ArrayList();

	    IInvesflowAPI invesflowAPI = m_ctx.getAPI();
	    IWorklistAPI worklistAPI = invesflowAPI.getWorkListAPI();

		Properties propSet = new Properties();
		propSet.add(new Property(0,"NOMBRE",Types.VARCHAR));
		propSet.add(new Property(1,"URL",Types.VARCHAR));
		propSet.add(new Property(2,"COUNT",Types.INTEGER));
		GenericItem inboxItem;

		// Avisos
		Notices notices = new Notices(m_ctx);
		int numNotices = notices.countNotices();
		if (numNotices > 0){
			inboxItem = new GenericItem(propSet, "NOMBRE");
			inboxItem.set("NOMBRE", "sucesos.avisosElectronicos");
			inboxItem.set("URL", "showNoticeList.do");
			inboxItem.set("COUNT", numNotices);
			inboxItems.add(inboxItem);
		}

		// Registros distribuidos
		if (registerAPI.existConnector()) {
			int nIntrays = registerAPI.countIntrais();
			//[dipucr-Felipe #1606] Para que salga el link aunque sea 0 y se puedan ver los registros archivados/devueltos
//			if (nIntrays > 0){
				inboxItem = new GenericItem(propSet, "NOMBRE");
				inboxItem.set("NOMBRE", "sucesos.registro");
				inboxItem.set("URL", "showIntrayList.do");
				inboxItem.set("COUNT", nIntrays);
				inboxItems.add(inboxItem);
//			}
		}

//		Terms terms = new Terms(m_ctx);
//		int nTerms = terms.countTerms();
		int nTerms = worklistAPI.countExpiredTerms(DeadLineDAO.TYPE_ALL, resp);

		// Plazos
		inboxItem = new GenericItem(propSet, "NOMBRE");
		inboxItem.set("NOMBRE", "sucesos.plazos");
		//Introducimos en la URL un parametro para que al hacer la consulta de
		//plazos expirados la realice con fecha final el d�a actual
		SimpleDateFormat fechaEs = new SimpleDateFormat("dd/MM/yyyy");
		inboxItem.set("URL", "showExpiredTerms.do?fechaFin="
				+ fechaEs.format(new Date()));
		inboxItem.set("COUNT", nTerms);
		inboxItems.add(inboxItem);

		// Informacion de tramitaciones agrupadas
	    int numeroTareasAgrupadas = worklistAPI.countBatchTasks(resp);
	    if (numeroTareasAgrupadas>0){
			inboxItem = new GenericItem(propSet, "NOMBRE");
			inboxItem.set("NOMBRE", "sucesos.tramitacionesAgrupadas");
			//Introducimos en la URL un parametro para que al hacer la consulta de
			//plazos expirados la realice con fecha final el d�a actual

			inboxItem.set("URL", "showBatchTaskList.do");
			inboxItem.set("COUNT", numeroTareasAgrupadas);
			inboxItems.add(inboxItem);
	    }

	    // Portafirmas
	    // INICIO [dipucr-Felipe #1246]
//	    if (ProcessSignConnectorFactory.getInstance(m_ctx).isDefaultConnector() && StringUtils.isNotBlank(FirmaConfiguration.getInstance(m_ctx).get(FirmaConfiguration.DIGITAL_SIGN_CONNECTOR_CLASS))) {
        ISignAPI signAPI = invesflowAPI.getSignAPI();
	    int countFirmas = signAPI.countCircuitsStepts(m_ctx.getRespId());
	    
	    if (SignConnectorFactory.isDefaultImplClass(m_ctx) || countFirmas > 0){//Portafirmas por defecto de SIGEM
			inboxItem = new GenericItem(propSet, "NOMBRE");
			inboxItem.set("NOMBRE", "sucesos.portafirmas");
			inboxItem.set("URL", "showBatchSignList.do");
			inboxItem.set("COUNT", countFirmas);
			inboxItems.add(inboxItem);
	    }
	    else{//Portafirmas MINHAP
	    	inboxItem = new GenericItem(propSet, "NOMBRE");
			inboxItem.set("NOMBRE", "sucesos.historicoFirmas");
			inboxItem.set("URL", "showSignHistoric.do");
//			inboxItem.set("COUNT", countFirmas);
			inboxItems.add(inboxItem);
	    }
	    // FIN [dipucr-Felipe #1246]

	    if(StringUtils.equalsIgnoreCase(resp, IResponsible.SUPERVISOR)||StringUtils.equalsIgnoreCase(resp, IResponsible.SUPERVISOR_MONITORING)){
	    	int nProcessSentTrash = invesflowAPI.countExpedientsSentToTrash();
	    	if(nProcessSentTrash>0){
	    		inboxItem = new GenericItem(propSet, "NOMBRE");
				inboxItem.set("NOMBRE", "sucesos.expedientesPapelera");
				//Introducimos en la URL un parametro para que al hacer la consulta de
				//plazos expirados la realice con fecha final el d�a actual
				inboxItem.set("URL", "showExpedientsSentToTrash.do?method=list&numreg="+nProcessSentTrash);
				inboxItem.set("COUNT", nProcessSentTrash);
				inboxItems.add(inboxItem);
	    	}
	    }

		return new ListCollection(inboxItems);
	}

	public IItemCollection getNotices() throws ISPACException {
		Notices notices = new Notices(m_ctx);

		IItemCollection result = notices.getNotices();

		// TODO: Auditar consulta de avisos
		auditConsultaAvisos(result);

		return result;
	}

	/**
	 * @param result
	 * @throws ISPACException
	 */
	private void auditConsultaAvisos(IItemCollection result) throws ISPACException {
		//[Manu #93] * ALSIGM3 Modificaciones Auditor�a
    	if(ConfiguratorAudit.getInstance().getPropertyBoolean(ConfigurationAuditFileKeys.KEY_AUDITORIA_ENABLE)){
    		auditoriaManager = new IspacAuditoriaManagerImpl();
	    		
			AuditContext auditContext = AuditContextHolder.getAuditContext();
	
			IspacAuditEventAvisoConsultaVO evento = new IspacAuditEventAvisoConsultaVO();
			evento.setAppDescription(IspacAuditConstants.APP_DESCRIPTION);
			evento.setAppId(IspacAuditConstants.getAppId());
			Iterator iterResults = result.iterator();
			Map avisos = new HashMap();
			while (iterResults.hasNext()) {
				IItem item = (IItem) iterResults.next();
				avisos.put(item.getString("SPAC_AVISOS_ELECTRONICOS:ID_AVISO"), item.getXmlValues());
			}
	
			evento.setAvisos(avisos);
			evento.setUser("");
			evento.setIdUser("");
			evento.setUserHostName("");
			evento.setUserIp("");
	
			evento.setFecha(new Date());
	
			if (auditContext != null) {
				evento.setUserHostName(auditContext.getUserHost());
				evento.setUserIp(auditContext.getUserIP());
				evento.setUser(auditContext.getUser());
				evento.setIdUser(auditContext.getUserId());
			} else {
				//logger.error("ERROR EN LA AUDITOR�A. No est� disponible el contexto de auditor�a en el thread local. Faltan los siguientes valores por auditar: userId, user, userHost y userIp");
			}
			logger.info("Auditando la consulta del aviso");
			auditoriaManager.audit(evento);
    	}
	}


	public void modifyNotice(int noticeId, int newstate) throws ISPACException {
		Notices notices = new Notices(m_ctx);
		notices.modifyNotice(noticeId, newstate);
	}

	/**
	 * Obtiene la informaci�n del registro distribuido.
	 * @param register N�mero de registro.
	 * @return Informaci�n del registro distribuido.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public Intray getIntray(String register) throws ISPACException {
		if (!registerAPI.existConnector()) {
			throw new ISPACException("exception.sicres.notConfigured");
		}
		// TODO: Auditar la consulta de registros distribuidos

		Intray intray = registerAPI.getIntray(register);
		List intrays = new ArrayList();
		intrays.add(intray);

		auditarConsultaRegistrosDistribuidos(intrays);
		return intray;
	}

	/**
	 * Obtiene la lista de registros distribuidos asociados al usuario conectado.
	 * @return Lista de registros distribuidos ({@link Intray}).
	 * @throws ISPACException si ocurre alg�n error.
	 */
	@SuppressWarnings("rawtypes")
	public List getIntrays() throws ISPACException {
		if (!registerAPI.existConnector()) {
			throw new ISPACException("exception.sicres.notConfigured");
		}
		List intrays = registerAPI.getIntrays();
		
		//[dipucr-Felipe #1501] Rellenamos el interesado principal de la lista de interesados
		setIntraysMainSenderName(intrays);
		
		if(ConfiguratorAudit.getInstance().getPropertyBoolean(ConfigurationAuditFileKeys.KEY_AUDITORIA_ENABLE))

		auditarConsultaRegistrosDistribuidos(intrays);

		return intrays;
	}
	
	/**
	 * [dipucr-Felipe #1606]
	 * Obtiene la lista de registros distribuidos ARCHIVADOS o DEVUELTOS asociados al usuario conectado.
	 * @return Lista de registros distribuidos ({@link Intray}).
	 * @throws ISPACException si ocurre alg�n error.
	 */
	@SuppressWarnings("rawtypes")
	public List getIntraysArchived() throws ISPACException {
		if (!registerAPI.existConnector()) {
			throw new ISPACException("exception.sicres.notConfigured");
		}

		List intrays = registerAPI.getIntraysArchived();
		
		//[dipucr-Felipe #1501] Rellenamos el interesado principal de la lista de interesados
		setIntraysMainSenderName(intrays);
		
		if(ConfiguratorAudit.getInstance().getPropertyBoolean(ConfigurationAuditFileKeys.KEY_AUDITORIA_ENABLE))
			auditarConsultaRegistrosDistribuidos(intrays);

		return intrays;
	}

	/**
	 * [dipucr-Felipe #1501]
	 * Rellena el campo de interesado principal en cada uno de los registros de la lista
	 * @param intrays - Pasamos el listado de registros por referencia
	 */
	@SuppressWarnings("rawtypes")
	private void setIntraysMainSenderName(List intrays) {
		
		for (Object obj : intrays){
			Intray intray = (Intray) obj;
			ThirdPerson[] arrRemitentes = intray.getRegisterSender();
			if (null != arrRemitentes && arrRemitentes.length > 0){
				intray.setMainSenderName(arrRemitentes[0].getName());
			}
		}
	}

	/**
	 * @param registrosDistribuidosMap
	 * @param intrays
	 */
	private void auditarConsultaRegistrosDistribuidos(List intrays) {
		//[Manu #93] * ALSIGM3 Modificaciones Auditor�a
    	if(ConfiguratorAudit.getInstance().getPropertyBoolean(ConfigurationAuditFileKeys.KEY_AUDITORIA_ENABLE)){
    		auditoriaManager = new IspacAuditoriaManagerImpl();

			Map registrosDistribuidosMap = new HashMap();
			Iterator iterIntrays = intrays.iterator();
			while (iterIntrays.hasNext()) {
				Intray intray = (Intray) iterIntrays.next();
				intray.getId();
				registrosDistribuidosMap.put(intray.getId(), intray.toString());
			}
	
			AuditContext auditContext = AuditContextHolder.getAuditContext();
	
			IspacAuditEventRegDistConsultaVO evento = new IspacAuditEventRegDistConsultaVO();
			evento.setAppDescription(IspacAuditConstants.APP_DESCRIPTION);
			evento.setAppId(IspacAuditConstants.getAppId());
			evento.setUserHostName("");
			evento.setUserIp("");
			evento.setUser("");
			evento.setIdUser("");
			evento.setRegistros(registrosDistribuidosMap);
	
			evento.setFecha(new Date());
	
			if (auditContext != null) {
				evento.setUserHostName(auditContext.getUserHost());
				evento.setUserIp(auditContext.getUserIP());
				evento.setUser(auditContext.getUser());
				evento.setIdUser(auditContext.getUserId());
			} else {
				//logger.error("ERROR EN LA AUDITOR�A. No est� disponible el contexto de auditor�a en el thread local. Faltan los siguientes valores por auditar: userId, user, userHost y userIp");
			}
			logger.info("Auditando la consulta de los registros distribuidos");
			auditoriaManager.audit(evento);
    	}
	}

	/**
	 * @param registrosDistribuidosMap
	 * @param intrays
	 */
	private void auditarModificacionRegistroDistribuidos(String intrayId) {
		//[Manu #93] * ALSIGM3 Modificaciones Auditor�a
    	if(ConfiguratorAudit.getInstance().getPropertyBoolean(ConfigurationAuditFileKeys.KEY_AUDITORIA_ENABLE)){
    		auditoriaManager = new IspacAuditoriaManagerImpl();

			AuditContext auditContext = AuditContextHolder.getAuditContext();
	
			IspacAuditEventRegDistModificacionVO evento = new IspacAuditEventRegDistModificacionVO();
			evento.setAppDescription(IspacAuditConstants.APP_DESCRIPTION);
			evento.setAppId(IspacAuditConstants.getAppId());
			evento.setUserHostName("");
			evento.setUserIp("");
			evento.setUser("");
			evento.setIdUser("");
			evento.setIdRegistroDistribuido(intrayId);
			evento.setNewValue("ARCHIVADO");
			evento.setFecha(new Date());
	
			if (auditContext != null) {
				evento.setUserHostName(auditContext.getUserHost());
				evento.setUserIp(auditContext.getUserIP());
			} else {
				//logger.error("ERROR EN LA AUDITOR�A. No est� disponible el contexto de auditor�a en el thread local. Faltan los siguientes valores por auditar: userId, user, userHost y userIp");
			}
			logger.info("Auditando la consulta de los registros distribuidos");
			auditoriaManager.audit(evento);
    	}
	}


//	public void addToProccess(String register, int process, int type)
//			throws ISPACException {
//
//		ITXTransaction transaction = m_ctx.getAPI().getTransactionAPI();
//		InboxContext ctx = new InboxContext(m_ctx, register);
//		ctx.setProcess(process);
//
//		if (type == IInboxAPI.CREADO)
//			transaction.executeEvents(EventsDefines.EVENT_OBJ_PROCEDURE, ctx
//					.getProcedure(), EventsDefines.EVENT_INBOX_CREATE, ctx);
//		else if (type == IInboxAPI.ANEXADO)
//			transaction.executeEvents(EventsDefines.EVENT_OBJ_PROCEDURE, ctx
//					.getProcedure(), EventsDefines.EVENT_INBOX_ANNEX, ctx);
//
//		ISicresConnector sicres = SicresConnectorFactory.getInstance()
//		.getSicresConnector(m_ctx);
//		sicres.addToProccess(Integer.parseInt(register), process, type);
//	}

	/**
	 * Inicia un expediente a partir de un registro distribuido.
	 * @param register N�mero de registro distribuido.
	 * @param pcdId Identificador del procedimiento.
	 * @return Identificador del proceso creado.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public int createProcess(String register, int pcdId)
			throws ISPACException {

		if (logger.isInfoEnabled()) {
			logger.info("Crear un expediente del procedimiento [" + pcdId
					+ "] a partir del registro distribuido [" + register + "]");
		}

		// Ejecuci�n en un contexto transaccional
		boolean ongoingTX = m_ctx.ongoingTX();
		boolean bCommit = false;

		int processId = ISPACEntities.ENTITY_NULLREGKEYID;

		try {

			// Conector con SICRES
			if (!registerAPI.existConnector()) {
				throw new ISPACException("exception.sicres.notConfigured");
			}

			if (!ongoingTX) {
				m_ctx.beginTX();
			}

			IInvesflowAPI invesflowAPI = m_ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();
			IInboxAPI inboxAPI = invesflowAPI.getInboxAPI();
			IThirdPartyAPI thirdPartyAPI = invesflowAPI.getThirdPartyAPI();

			// Informaci�n del procedimiento
			IItem ctProcedure = entitiesAPI.getEntity(
					SpacEntities.SPAC_CT_PROCEDIMIENTOS, pcdId);

			// Par�metros para el expediente
			Map params = new HashMap();
			params.put("COD_PCD", ctProcedure.getString("COD_PCD"));

			// Crear el proceso del expediente
	    	processId = tx.createProcess(pcdId, params);

			InboxContext ctx = new InboxContext(m_ctx, register);
			ctx.setProcess(processId);

	    	// Informaci�n del proceso
	    	IProcess process = invesflowAPI.getProcess(processId);

	        // Informaci�n del registro de entrada
			Intray intray = inboxAPI.getIntray(register);

	    	// Informaci�n del expediente creado
	    	String numExp = process.getString("NUMEXP");
	    	String registerNumber = intray.getRegisterNumber();

			IItem expedient = entitiesAPI.getExpedient(numExp);
	    	expedient.set("NREG", registerNumber);

	    	// Informaci�n del registro de entrada
			RegisterInfo registerInfo = new RegisterInfo(null,
					registerNumber, null, RegisterType.ENTRADA);
			Register inReg = registerAPI.readRegister(registerInfo);
	    	if (inReg != null) {

		    	Date registerDate = inReg.getRegisterOrigin().getRegisterDate().getTime();


	    		//Comprobamos si la entidad SPAC_REGISTROS_ES esta vinculada al procedimiento para vincular el Apunte de registro al expediente a crear
	    		boolean associatedRegESEntity = RegisterHelper.isAssocitedRegistrosESEntity(m_ctx, numExp);
	    		IItem itemRegisterES = null;

	    		if (associatedRegESEntity){
	    			//Si no esta asociado ya el apunte de registro con el expediente, se realiza la asociacion
	    			IItemCollection itemcol = entitiesAPI.queryEntities(SpacEntities.SPAC_REGISTROS_ES_NAME, "WHERE NREG = '" + registerNumber + "' AND NUMEXP = '"+DBUtil.replaceQuotes(numExp)+"' AND TP_REG = '" + RegisterType.ENTRADA + "'");
	    			if (!itemcol.next()){
	    				itemRegisterES = entitiesAPI.createEntity(SpacEntities.SPAC_REGISTROS_ES_NAME, numExp);
	    				itemRegisterES.set("NREG", registerNumber);
	    				itemRegisterES.set("FREG", registerDate);
	    				itemRegisterES.set("TP_REG", RegisterType.ENTRADA);
	    				itemRegisterES.set("ORIGINO_EXPEDIENTE", "SI");

	    				//Se vincula el expediente al apunte de registro
	    				registerAPI.linkExpedient(new RegisterInfo(null, registerNumber, null, RegisterType.ENTRADA), numExp);
	    			}

	    			//Se da de alta los interesados como participantes en el expediente menos el primero que se asocia como interesado principal
					RegisterHelper.insertParticipants(m_ctx, registerNumber,RegisterType.ENTRADA, numExp, false);
				}

	    		// Fecha de registro
	    		expedient.set("FREG", registerDate);

	            if (inReg.getRegisterData() != null) {

	            	// INTERESADO PRINCIPAL
	            	if (!ArrayUtils.isEmpty(inReg.getRegisterData().getParticipants())) {
	                	ThirdPerson thirdPerson = inReg.getRegisterData().getParticipants() [0];
	                	TercerosUtil.setInteresadoExpedienteByTercero(m_ctx, numExp, expedient, thirdPerson, false);
	            	}

	            	// ASUNTO (se corresponde con el resumen de registro)
	    	    	if (StringUtils.isNotBlank(inReg.getRegisterData().getSummary())) {
	    	    		expedient.set("ASUNTO", inReg.getRegisterData().getSummary());
	    	    	} else if (inReg.getRegisterData().getSubject() != null) {
	    	    		expedient.set("ASUNTO", StringUtils.nullToEmpty(inReg.getRegisterData().getSubject().getName()));
	    	    	}

	    	    	if (itemRegisterES != null){
	    	    		itemRegisterES.set("ASUNTO", expedient.get("ASUNTO"));
	    	    	}


	            }

	            if (itemRegisterES != null){
	            	itemRegisterES.store(m_ctx);
	            }

	    	}

	    	expedient.store(m_ctx);

			// Ejecutar el evento
			ITXTransaction transaction = invesflowAPI.getTransactionAPI();
			transaction.executeEvents(EventsDefines.EVENT_OBJ_PROCEDURE, ctx
					.getProcedure(), EventsDefines.EVENT_INBOX_CREATE, ctx);

			// Adjuntar los documentos del registro distribuido al expediente
			Expedients expedientsAPI = new Expedients(m_ctx);
			expedientsAPI.addDocuments(process.getString("NUMEXP"), intray);

			// Si todo ha sido correcto se hace commit de la transacci�n
			bCommit = true;
		}
		finally {
			if (!ongoingTX) {
				m_ctx.endTX(bCommit);
			}
		}

		// Archivar el registro distribuido
		checkAutoArchiving(register);

		return processId;
	}

	/**
	 * Anexa el registro distribuido al expediente.
	 * @param register N�mero de registro distribuido.
	 * @param numExp N�mero de expediente.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void annexToProcess(String register, String numExp) throws ISPACException {
		annexToProcess(register, numExp, 0, 0);
	}

	/**
    * Anexa el registro distribuido al expediente.
    * @param register N�mero de registro distribuido.
    * @param numExp N�mero de expediente.
    * @param taskid Identificador del tr�mite instanciando en el expediente al que se anexar�n los documentos del apunte de registro
    * @throws ISPACException si ocurre alg�n error.
    */
	public void annexToProcess(String register, String numExp, int taskId) throws ISPACException {
		annexToProcess(register, numExp, taskId, 0);
	}

	/**
    * Anexa el registro distribuido al expediente.
    * @param register N�mero de registro distribuido.
    * @param numExp N�mero de expediente.
    * @param taskid Identificador del tr�mite instanciando en el expediente al que se anexar�n los documentos del apunte de registro
    * @param typeDocId Identificador del tipo de documento que se asignar� a los documentos del apunte de registro
    * @throws ISPACException si ocurre alg�n error.
    */
	public void annexToProcess(String register, String numExp, int taskId, int typeDocId)
			throws ISPACException {

		if (logger.isInfoEnabled()) {
			logger.info("Anexar los documentos del registro distribuido ["
					+ register + "] al expediente [" + numExp
					+ "] en el tramite [ID=" + taskId
					+ "] con tipo de documento [ID=" + typeDocId + "]");
		}

		IInvesflowAPI invesflowAPI = m_ctx.getAPI();

		// Ejecuci�n en un contexto transaccional
		boolean ongoingTX = m_ctx.ongoingTX();
		boolean bCommit = false;

		try {
			// Inicio de transacci�n
			if (!ongoingTX) {
				m_ctx.beginTX();
			}

			// Informaci�n del proceso
			IProcess process = invesflowAPI.getProcess(numExp);

			// Contexto de la bandeja de entrada
			ITXTransaction transaction = invesflowAPI.getTransactionAPI();
			InboxContext ctx = new InboxContext(m_ctx, register);
			ctx.setProcess(process.getInt("ID"));

			// Ejecutar el evento
			transaction.executeEvents(EventsDefines.EVENT_OBJ_PROCEDURE,
					ctx.getProcedure(), EventsDefines.EVENT_INBOX_ANNEX, ctx);

			// Informaci�n del registro distribuido
			IInboxAPI inboxAPI = invesflowAPI.getInboxAPI();
			Intray intray = inboxAPI.getIntray(register);

			// Adjuntar los documentos del registro distribuido al expediente
			Expedients expedientsAPI = new Expedients(m_ctx);
			expedientsAPI.addDocuments(numExp, intray, taskId, typeDocId);

			// Si todo ha sido correcto se hace commit de la transacci�n
			bCommit = true;
		}
		finally {
			if (!ongoingTX) {
				m_ctx.endTX(bCommit);
			}
		}

		// Archivar el registro distribuido
		checkAutoArchiving(register);
	}

//	public void distribute(String register, Responsible responsible,
//			String message, boolean archive) throws ISPACException {
//		ISicresConnector sicres = SicresConnectorFactory.getInstance()
//			.getSicresConnector(m_ctx);
//		sicres.distribute(Integer.parseInt(register), responsible, message,
//				archive);
//	}


	/**
	 * Acepta un registro distribuido.
	 * @param register N�mero de registro.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void acceptIntray(String register) throws ISPACException {
		if (!registerAPI.existConnector()) {
			throw new ISPACException("exception.sicres.notConfigured");
		}
		registerAPI.acceptIntray(register);
	}

	/**
	 * Rechaza un registro distribuido.
	 * @param register N�mero de registro.
	 * @param reason Motivo del rechazo.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void rejectIntray(String register, String reason) throws ISPACException {
		if (!registerAPI.existConnector()) {
			throw new ISPACException("exception.sicres.notConfigured");
		}
		registerAPI.rejectIntray(register, reason);
	}

	/**
	 * Archiva un registro distribuido.
	 * @param register N�mero de registro.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void archiveIntray(String register) throws ISPACException {

		if (logger.isInfoEnabled()) {
			logger.info("Archivar el registro distribuido [" + register + "]");
		}

		if (!registerAPI.existConnector()) {
			throw new ISPACException("exception.sicres.notConfigured");
		}
		// TODO: Auditar el archivado del registro distribuido
		registerAPI.archiveIntray(register);
		this.auditarModificacionRegistroDistribuidos(register);
	}

//	public void changeState(String register, int state) throws ISPACException {
//		ISicresConnector sicres = SicresConnectorFactory.getInstance()
//			.getSicresConnector(m_ctx);
//		sicres.changeState(Integer.parseInt(register), state);
//	}

	public Annexe[] getAnnexes(String register) throws ISPACException {
		if (!registerAPI.existConnector()) {
			throw new ISPACException("exception.sicres.notConfigured");
		}
		return registerAPI.getAnnexes(register);
	}

	public void getAnnexe(String register, String annexe, OutputStream out)
			throws ISPACException {
		if (!registerAPI.existConnector()) {
			throw new ISPACException("exception.sicres.notConfigured");
		}
		registerAPI.getAnnexe(register, annexe, out);
	}
	
	/**
	 * [dipucr-Felipe #1689]
	 * Sobrecargamos el m�todo para dar la posibilidad de no cerrar el stream
	 */
	public void getAnnexe(String register, String annexe, OutputStream out, boolean bCloseStream)
			throws ISPACException {
		if (!registerAPI.existConnector()) {
			throw new ISPACException("exception.sicres.notConfigured");
		}
		registerAPI.getAnnexe(register, annexe, out, bCloseStream);
	}

	/**
	 * Comprueba si se debe archivar autom�ticamente el registro distribuido.
	 * @param register Identificador del registro distribuido.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	protected void checkAutoArchiving(String register) throws ISPACException {

		// Archivar el registro distribuido
		String autoArchiving = ConfigurationMgr.getVarGlobal(m_ctx, ConfigurationMgr.INTRAY_AUTO_ARCHIVING);
		if (logger.isInfoEnabled()) {
			logger.info(ConfigurationMgr.INTRAY_AUTO_ARCHIVING + ": [" + autoArchiving + "]");
		}

		if ("true".equals(autoArchiving)) {

			if (logger.isInfoEnabled()) {
				logger.info("Est� configurado el archivo autom�tico de los registros distribuidos");
			}

			archiveIntray(register);
		}
	}


}
