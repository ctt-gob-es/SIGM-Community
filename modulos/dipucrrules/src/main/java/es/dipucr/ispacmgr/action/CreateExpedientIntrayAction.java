package es.dipucr.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInboxAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRegisterAPI;
import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.IThirdPartyAPI;
import ieci.tdw.ispac.api.IWorklistAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcedure;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.util.ListCollection;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.InboxContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.directory.DirectoryConnectorFactory;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryConnector;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryEntry;
import ieci.tdw.ispac.ispaclib.resp.RespFactory;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.security.SecurityMgr;
import ieci.tdw.ispac.ispaclib.sicres.vo.Intray;
import ieci.tdw.ispac.ispaclib.sicres.vo.Register;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterInfo;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterType;
import ieci.tdw.ispac.ispaclib.sicres.vo.ThirdPerson;
import ieci.tdw.ispac.ispaclib.thirdparty.IElectronicAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IPostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.context.NextActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro;
import es.dipucr.sigem.api.rule.common.AccesoBBDDeTramitacion;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.RegistrosDistribuidosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class CreateExpedientIntrayAction extends BaseAction {


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		/**
		 * [Teresa]
		 * Ticket #373 SIGEM Registro Distribudo. Hacer que al aceptar el registro distribuido se cree automaticamente el expediente
		 * **/
		IInvesflowAPI invesflowAPI = session.getAPI();
		IWorklistAPI workListAPI = invesflowAPI.getWorkListAPI();
    	IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
    	IRegisterAPI registerAPI = invesflowAPI.getRegisterAPI();
    	ClientContext cct = (ClientContext) session.getClientContext();
    	IInboxAPI inbox = invesflowAPI.getInboxAPI();
    			
    	//Numero del registro de entrada.
		String register = request.getParameter("register");

		//Calcula el procedimiento apartir del tipo de asunto.
		String tipoAsunto = request.getParameter("matterTypeName");
		//Por ejemplo: tipoAsunto: SAJ - SOLICITUD DE ASESORAMIENTO JURÍDICO, con este dato nos vamos a la tabla
		//sgmrtcatalogo_tramites para ver con el asunto que tipo de procedimiento le corresponde
		String [] vAsunto = tipoAsunto.split(" - ");
		int pcdId = 0;
        ISPACException info = null;
        String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
        int processId = 0;
		if(vAsunto.length > 1){
			String asunto = vAsunto[0];
			//Si el asunto no es un Intervención Interesados Procedimiento quiere decir
			//Que no hay que subsanar ningun documento por lo tanto habría que crear el procedimiento
			if(!asunto.equals("SUBS_REG_PRES")){
				AccesoBBDDeTramitacion accsETram = new AccesoBBDDeTramitacion(entidad);
				Vector idProcedimiento = accsETram.getIdProcedimiento(asunto);
				//no esta asociado el tipo de asunto a ningun procedimiento
				/**
				 * [Ticket #481# TCG] SIGEM error al aceptar un registro distribuido
				 * **/
				if(idProcedimiento.size() > 0){
					StringBuffer sqlProc = new StringBuffer();
					sqlProc.append("COD_PCD='"+idProcedimiento.get(0)+"' ");
					for(int i= 1; i<idProcedimiento.size(); i++){
						sqlProc.append("OR COD_PCD='"+idProcedimiento.get(i)+"' ");
					}
					String sQuery = "WHERE "+sqlProc.toString();
					IItemCollection procedimiento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, sQuery);
//					Iterator<IItem> itProcedimiento = procedimiento.iterator();
					
					/**
					 * INICIO Ticket #394# SIGEM Error al iniciar un expediente con registro distribuido
					 * **/
					List procedVigente = procedimAsuntoVigente(workListAPI, procedimiento);
					/**
					 * FIN Ticket #394# SIGEM Error al iniciar un expediente con registro distribuido
					 * **/
					
	
					//Vemos que con ese asunto pueden existir dos procedimientos asociados a el
					if(procedVigente.size()>1){
						// Listado de procedimientos instanciables por el usuario
						request.setAttribute("InstProcedureList", procedVigente);
						
						// Presentación de los datos
						CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
						BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/instancedformatter.xml"));
						request.setAttribute("FormatterInstProcedure", formatter);
	
						return mapping.findForward("success");
					}
					// [Manu Ticket #76] INICIO ALSIGM3 Error al aceptar distribución de registros que no corresponden
					else if(procedVigente.size()==0){
						ISPACInfo informacion=new ISPACInfo("No tiene permisos para iniciar el expediente asociado al registro", "",false);
						request.getSession().setAttribute("infoAlert", informacion);
						
						// Retornar a la página del registro distribuido
						ActionForward forward = mapping.findForward("noPerm");
						String path = new StringBuffer(forward.getPath())
							.append(forward.getPath().indexOf("?") > 0 ? "&" : "?")
							.append("id=").append(register)
							.toString();
						
						return new ActionForward(forward.getName(), path, forward.getRedirect());
						
						//return mapping.findForward("noPerm");
					}
					// [Manu Ticket #76] FIN ALSIGM3 Error al aceptar distribución de registros que no corresponden
					else{
						//[Teresa Ticket#156] INICIO ALSIGM3 error al iniciar un expediente desde los registros distribuidos del tramitador
						if(procedVigente.get(0) instanceof ItemBean){
							ItemBean proced = (ItemBean)procedVigente.get(0);
							pcdId = Integer.parseInt(proced.getString("ID"));
						}
						if(procedVigente.get(0) instanceof IItem){
							IItem proced = (IItem)procedVigente.get(0);
							pcdId = proced.getInt("ID");
						}
						//[Teresa Ticket#156] FIN ALSIGM3 error al iniciar un expediente desde los registros distribuidos del tramitador

						// Crear el proceso a partir del registro distribuido
						processId = createProcess(cct, registerAPI, register, pcdId);
						
						return NextActivity.afterStartProcess(request, processId, invesflowAPI, mapping);	
					}
				}
				else{
					//Si no existe un procedimiento asociado, quiere decir que no existe tipo de asunto
					// en la tabla scr_ca columna id_org=0 por lo tanto 
					//la solución es mostrar todos los procedimientos
					//que tenga permisos ese usuario.
					IItemCollection col = getProcedure(cct, entitiesAPI);
					
					List procedVidente = procedimAsuntoVigente(workListAPI, col);
					
					request.setAttribute("InstProcedureList", procedVidente);
					
					// Presentación de los datos
					CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
					BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/instancedformatter.xml"));
					request.setAttribute("FormatterInstProcedure", formatter);

					return mapping.findForward("success");
				}
				
			}
			//Habría que subsanar un documento, insertar el documento directamente al expediente
			//[INICIO Teresa Ticket #384] SIGEM Registro Presencial Subsanación de documentos.
			else{
				//Calcula el procedimiento apartir del tipo de asunto.
				Intray intray = inbox.getIntray(register);
				String registerNumber = intray.getRegisterNumber();
				
				AccesoBBDDRegistro accsRegistro= new AccesoBBDDRegistro(entidad);
				String numexpRegistro = accsRegistro.getNumExpRegistro(registerNumber);

				if(numexpRegistro != null){
					//Spac_fases
					//Obtiene el número de fase 
					String strQueryAux = "WHERE NUMEXP='" + numexpRegistro + "'";
					IItemCollection collExpsAux = entitiesAPI.queryEntities("SPAC_FASES", strQueryAux);
					Iterator<IItem> itExpsAux = collExpsAux.iterator();
					IItem iExpedienteAux = itExpsAux.next();
					String idFase = iExpedienteAux.getString("ID_FASE_BPM");
					
					//Almacenar los documentos en el trámite de subsanación.
					// Adjuntar los documentos del registro distribuido al expediente
					RegistrosDistribuidosUtil.addDocuments(cct, numexpRegistro, intray);
					
					ISPACInfo informacion=new ISPACInfo("Agregado el/los documento/s al expediente a subsanar", "",false);
					request.getSession().setAttribute("infoAlert", informacion);
					
					//[Manu Ticket #75] INICIO ALSIGM3 Error al aceptar la distribución de subsanaciones desde registro presencial.
					ActionForward showexp = mapping.findForward("showexp");
					return new ActionForward(showexp.getName(),				
				        showexp.getPath() + "?numexp="+numexpRegistro+"&stageId="+idFase, true);
					//[Manu Ticket #75] FIN ALSIGM3 Error al aceptar la distribución de subsanaciones desde registro presencial.
				}
				//Los de registro no han insertado el número de expediente
				else{
					//[INICIO Teresa Ticket # 393# SIGEM Registro Presencial; subsanación de documentos; añadir número de expediente]
					request.setAttribute("register", register);
					return mapping.findForward("numexp");
					//[FIN Teresa Ticket # 393# SIGEM Registro Presencial; subsanación de documentos; añadir número de expediente]
				}
			}
			//[FIN Teresa Ticket #384] SIGEM Registro Presencial Subsanación de documentos.
		}
		else{
			info = new ISPACException("Este registro no tiene tipo de asunto asociado", true);
			request.getSession().setAttribute("infoAlert", info);
			IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
			IState currentstate = managerAPI.currentState(getStateticket(request));
			return NextActivity.refresh(request, mapping, currentstate);
		}
	}
	
	public IItemCollection getProcedure(ClientContext context, IEntitiesAPI entitiesAPI) throws ISPACException
	{
		// Los que puede crear el usuario y los que pueden crear los que sustituye
		String resp = getSubstitutesRespString(context);
		try
		{
			/* Procedimientos en vigor */
			String sqlquery = "WHERE ID IN (SELECT ID FROM SPAC_P_PROCEDIMIENTOS " +
			"WHERE ESTADO=" + IProcedure.PCD_STATE_CURRENT
			+ " AND TIPO=" + IProcedure.PROCEDURE_TYPE
			//Busqueda si tiene permisos para iniciar un expediente
			+ " AND ID IN" 
			+ " (SELECT ID_PCD FROM SPAC_SS_PERMISOS WHERE  PERMISO="
			+ ISecurityAPI.ISPAC_RIGHTS_CREATEEXP + DBUtil.addAndInResponsibleCondition("UID_USR", resp) + "));";
			
			return entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, sqlquery);
		}
		catch (ISPACException ie)
		{
			throw new ISPACException("Error en WLWorklist:getProcs()", ie);
		}
	}
	
	private String getSubstitutesRespString(ClientContext context) throws ISPACException
	{
		String resp = "";
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
	
	/**
	 * 
	 * @param entryUID
	 * @return
	 * @throws ISPACException
	 */
	
	private String getRespStringFromEntryUID(String entryUID) throws ISPACException {

		IDirectoryConnector directory = DirectoryConnectorFactory.getConnector();

		IDirectoryEntry entry = directory.getEntryFromUID(entryUID);
		Responsible resp= RespFactory.createResponsible(entry);

		return resp.getRespString();
	}
	
	/**
	 * INICIO Ticket #394# SIGEM Error al iniciar un expediente con registro distribuido
	 * @throws ISPACException 
	 * **/
	
	@SuppressWarnings("unchecked")
	private List<IItem> procedimAsuntoVigente(IWorklistAPI workListAPI, IItemCollection procedimientoAsunto) throws ISPACException {
		
		List<ItemBean> lProce = new Vector<ItemBean>();
		List<IItem> lProceFinal = new Vector<IItem>();
		ListCollection iProceFinal = null;
		
		try {
			//Recorrer el vector de proc en relacion con el asunto para almacenar en un vector 
			//los id que le corresponden con el tipo de asunto seleccionado en registro.
			Iterator<IItem> iProcNoVig = procedimientoAsunto.iterator();
			Vector<Integer> vIdProcNoVig = new Vector<Integer>();
			while(iProcNoVig.hasNext()){
				IItem itProcNoVig = iProcNoVig.next();
				vIdProcNoVig.add(itProcNoVig.getInt("ID"));
			}
			
			//Viendo si el procedimiento esta vigente y puede ser responsable		
			IItemCollection procVigente = workListAPI.getCreateProcedures();
			
			lProce = CollectionBean.getBeanList(procVigente);
			for(ItemBean procedimiento : lProce){
				if(vIdProcNoVig.contains(procedimiento.getItem().getInt("ID"))){
					IItem itemProced = procedimiento.getItem();
					lProceFinal.add(itemProced);
				}
			}
			//Cambio para que si no existe procedimiento asociado a ese registro telemático 
			//muestre todo todos los procedimientos asociados al usuario
			if(lProceFinal.size()==0){
				for(ItemBean itemProced : lProce){
					IItem  itemProcedPrueba = itemProced.getItem();
					lProceFinal.add(itemProcedPrueba);
				}
			}
			
			iProceFinal = new ListCollection(lProceFinal);
			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		
		return CollectionBean.getBeanList(iProceFinal);
	}
	
	/**
	 * FIN Ticket #394# SIGEM Error al iniciar un expediente con registro distribuido
	 * **/
	
	
	/**
	 * Se sacan de DipucrCommonFunctions ya que realmente no son funciones comunes iniciar expediente desde distribución
	 * 
	 * */
	
	/**
	 * Inicia un expediente a partir de un registro distribuido.
	 * @param cct 
	 * @param registerAPI 
	 * @param register Número de registro distribuido.
	 * @param pcdId Identificador del procedimiento.
	 * @return Identificador del proceso creado.
	 * @throws ISPACException si ocurre algún error.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int createProcess(ClientContext cct, IRegisterAPI registerAPI, String register, int pcdId) throws ISPACException {

		// Ejecución en un contexto transaccional
		boolean ongoingTX = cct.ongoingTX();
		boolean bCommit = false;
		
		int processId = ISPACEntities.ENTITY_NULLREGKEYID;
		
		try {

			// Conector con SICRES
			if (!registerAPI.existConnector()) {
				throw new ISPACException("exception.sicres.notConfigured");
			}
			
			if (!ongoingTX) {
				cct.beginTX();
			}
			
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();
			IInboxAPI inboxAPI = invesflowAPI.getInboxAPI();
			IThirdPartyAPI thirdPartyAPI = invesflowAPI.getThirdPartyAPI();
			
			// Información del procedimiento
			IItem ctProcedure = entitiesAPI.getEntity(
					SpacEntities.SPAC_CT_PROCEDIMIENTOS, pcdId);
			
			// Parámetros para el expediente
			Map params = new HashMap();
			params.put("COD_PCD", ctProcedure.getString("COD_PCD"));

			// Crear el proceso del expediente
	    	processId = tx.createProcess(pcdId, params);

			InboxContext ctx = new InboxContext(cct, register);
			ctx.setProcess(processId);

	    	// Información del proceso
	    	IProcess process = invesflowAPI.getProcess(processId);

	        // Información del registro de entrada
			Intray intray = inboxAPI.getIntray(register);
	    	
	    	// Información del expediente creado
	    	IItem expedient = ExpedientesUtil.getExpediente(cct, process.getString("NUMEXP"));
	    	expedient.set("NREG", intray.getRegisterNumber());

	    	// Información del registro de entrada
			RegisterInfo registerInfo = new RegisterInfo(null, 
					intray.getRegisterNumber(), null, RegisterType.ENTRADA);
			Register inReg = registerAPI.readRegister(registerInfo);
	    	if (inReg != null) {
	    		
	    		// Fecha de registro
	    		expedient.set("FREG", inReg.getRegisterOrigin().getRegisterDate().getTime());
	    		
	            if (inReg.getRegisterData() != null) {
	            	
	            	// INTERESADO PRINCIPAL
	            	if (!ArrayUtils.isEmpty(inReg.getRegisterData().getParticipants())) {
	                		
	                	ThirdPerson thirdPerson = inReg.getRegisterData().getParticipants() [0];
	                	if (thirdPerson != null) {

	                		// Obtener el interesado principal
		                	IThirdPartyAdapter thirdParty = null;
		                	
		                	if ((thirdPartyAPI != null) && StringUtils.isNotBlank(thirdPerson.getId())) {
		                		thirdParty = thirdPartyAPI.lookupById(thirdPerson.getId());
	                		}
		                	
		                	if (thirdParty != null) {
		                		
		                		//Informacion del participante como interesado
		            	    	//Comprobamos si existe ya el participante, si existe no hacemos nada
		            	    	IItemCollection participantes = ParticipantesUtil.getParticipantes(cct, process.getString("NUMEXP"), "ID_EXT = '" + thirdParty.getIdExt()+"'", "");
		            	    	if(!participantes.iterator().hasNext()){
		        		    		cct.beginTX();
		        			        IItem nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, process.getString("NUMEXP"));
		        			        
		        			        nuevoParticipante.set("ID_EXT", thirdParty.getIdExt());
		        			        nuevoParticipante.set("ROL", "INT");
		        			        nuevoParticipante.set("TIPO_PERSONA", "F");
		        			        nuevoParticipante.set("NDOC", thirdParty.getIdentificacion());
		        			        nuevoParticipante.set("NOMBRE", thirdParty.getNombreCompleto());
		        			        
		        			        if(thirdParty.getDefaultDireccionPostal()!=null){
		        			        	if(thirdParty.getDefaultDireccionPostal().getDireccionPostal()!=null){
			        			        	nuevoParticipante.set("DIRNOT", thirdParty.getDefaultDireccionPostal().getDireccionPostal());
			        			        }
			        			        if(thirdParty.getDefaultDireccionPostal().getCodigoPostal()!=null){
			        			        	nuevoParticipante.set("C_POSTAL", thirdParty.getDefaultDireccionPostal().getCodigoPostal());
			        			        }
			        		            if(thirdParty.getDefaultDireccionPostal().getPoblacion()!=null){
			        		            	nuevoParticipante.set("LOCALIDAD", thirdParty.getDefaultDireccionPostal().getPoblacion());
			        		            }
			        		            if(thirdParty.getDefaultDireccionPostal().getComunidadAutonoma()!=null){
			        		            	nuevoParticipante.set("CAUT", thirdParty.getDefaultDireccionPostal().getComunidadAutonoma());
			        		            }
			        		            if(thirdParty.getDefaultDireccionPostal().getTelefono()!=null){
			        		            	nuevoParticipante.set("TFNO_FIJO", thirdParty.getDefaultDireccionPostal().getTelefono());
			        		            }
		        			        }
		        		            
		        		            nuevoParticipante.set("TIPO_DIRECCION", "T");	               		       
		        		            try{
		        		            	nuevoParticipante.store(cct);
		        		            	cct.endTX(true);
		        		            }
		        		            catch(Exception e){
		        		            	logger.error("Error al guardar el participante con id: " + thirdParty.getIdExt() + ". Expediente: " + process.getString("NUMEXP") + ". " + e.getMessage(), e);
		        		            	cct.endTX(false);
		        		            }
		        				}//fin si !existe
		                		
			                	expedient.set("TIPOPERSONA", thirdParty.getTipoPersona());
			                	expedient.set("IDTITULAR", thirdParty.getIdExt());
			                	expedient.set("NIFCIFTITULAR", thirdParty.getIdentificacion());
			                	expedient.set("IDENTIDADTITULAR", thirdParty.getNombreCompleto());
			                	
			                	IPostalAddressAdapter dirPostal = thirdParty.getDefaultDireccionPostal();
			                	if (dirPostal != null) {
				                	expedient.set("DOMICILIO", dirPostal.getDireccionPostal());
				                	expedient.set("CPOSTAL", dirPostal.getCodigoPostal());
				                	expedient.set("CIUDAD", dirPostal.getMunicipio());
				                	expedient.set("TFNOFIJO", dirPostal.getTelefono());

				                	String regionPais = dirPostal.getProvincia();
				                	if (StringUtils.isNotEmpty(dirPostal.getPais()))
				                		regionPais += "/"+dirPostal.getPais();
				                	
				                	expedient.set("REGIONPAIS", regionPais);
			                	}
			                	
			                	IElectronicAddressAdapter dirElectronica = thirdParty.getDefaultDireccionElectronica();
			                	if (dirElectronica != null) {
			                		if (dirElectronica.getTipo() == IElectronicAddressAdapter.PHONE_TYPE) {
			                			expedient.set("TFNOMOVIL", dirElectronica.getDireccion());
			                		} else {
			                			expedient.set("DIRECCIONTELEMATICA", dirElectronica.getDireccion());
			                		}
			                	}
			                	
			                	String addressType = "P";
			                	if (thirdParty.isNotificacionTelematica()) {
			                		addressType = "T";
			                	}
			                	expedient.set("TIPODIRECCIONINTERESADO", addressType);
		                	} else {
		                		expedient.set("IDENTIDADTITULAR", thirdPerson.getName());
		                	}
	                	}
	            	}
	            	
	            	// ASUNTO (se corresponde con el resumen de registro)
	    	    	if (inReg.getRegisterData().getSummary() != null) {
	    	    		expedient.set("ASUNTO", StringUtils.nullToEmpty(inReg.getRegisterData().getSummary()));
	    	    	} else if (inReg.getRegisterData().getSubject() != null) {
	    	    		expedient.set("ASUNTO", StringUtils.nullToEmpty(inReg.getRegisterData().getSubject().getName()));
	    	    	}
	            }
	    	}
	    	
	    	expedient.store(cct);
	    	
	    	logger.info("Inicio Evento");
			// Ejecutar el evento
			ITXTransaction transaction = invesflowAPI.getTransactionAPI();
			transaction.executeEvents(EventsDefines.EVENT_OBJ_PROCEDURE, ctx
					.getProcedure(), EventsDefines.EVENT_INBOX_CREATE, ctx);
			
			logger.info("Inicio AddDocument");
			// Adjuntar los documentos del registro distribuido al expediente
			RegistrosDistribuidosUtil.addDocuments(cct, process.getString("NUMEXP"), intray);
			
			logger.info("FIN AddDocument");

			// Si todo ha sido correcto se hace commit de la transacción
			bCommit = true;	
		}
		finally {
			if (!ongoingTX) {
				cct.endTX(bCommit);
			}
		}
		
		return processId;
	}
	 
	
}