package es.dipucr.sigem.api.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.app.EntityApp;
import ieci.tdw.ispac.ispaclib.bean.ValidationError;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacmgr.action.form.EntityForm;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IScheme;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import es.dipucr.notificador.model.TerceroWS;
import es.dipucr.sigem.api.rule.common.utils.CompareceUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.jccm.notificador.ws.AplicacionesServiceProxy;

public class AccesoCompareceAction extends BaseAction {

	public ActionForward executeAction(ActionMapping mapping,
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {
		
		ClientContext cct = session.getClientContext();

		IEntitiesAPI entapi = session.getAPI().getEntitiesAPI();
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		
		// Estado del contexto de tramitación
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState currentstate = managerAPI.currentState(getStateticket(request));
		IState state = managerAPI.currentState(getStateticket(request));
		
		IScheme scheme = managerAPI.getSchemeAPI();

		// Formulario asociado a la acción
		EntityForm defaultForm = (EntityForm) form;
		
		String comparece = "";		
		String dni = defaultForm.getProperty("SPAC_DT_INTERVINIENTES:NDOC");
		
		//<!-- [Teresa] Ticket #654  SIGEM-COMPARECE Controlar que al crear un expediente no haya sido ya creado-->
        IItemCollection collection = ParticipantesUtil.getParticipantes(cct, state.getNumexp(),"NDOC='"+dni+"'", "");
        
        if (collection.toList().size()<=1)
        {
			String movil = defaultForm.getProperty("SPAC_DT_INTERVINIENTES:TFNO_MOVIL");
			String email = defaultForm.getProperty("SPAC_DT_INTERVINIENTES:DIRECCIONTELEMATICA");
			if (defaultForm.getProperty("DPCR_PARTICIPANTES_COMPARECE:COMPARECE")!=null) comparece = defaultForm.getProperty("DPCR_PARTICIPANTES_COMPARECE:COMPARECE"); else comparece="";
		
			//Compruebo que ese Participante no este dado de alta en el expediente, porque si esta dado de alta
			//y le da al boton Guardar lo que se quiere hacer es modificar los datos del usuario
			IItemCollection collPart = ParticipantesUtil.getParticipantes(cct, state.getNumexp(),"NDOC='"+dni+"'", "");
	        Iterator<?> itcollAllPart = collPart.iterator();
	        ISPACException info = null;
	        if(itcollAllPart.hasNext()){
	        	boolean correcto = modificacionTercero(dni, email, movil, entidad);
	        	if(!correcto){
	        		info = new ISPACException("Error al realizar la modificación", true);
					request.getSession().setAttribute("infoAlert", info);
	        	}
	        }
	        
	 		//Este cambio es para que cuando se de de alta un participante automaticamente se de dealta en el comparece y 
	        // en el envio de las notificaciones
	        if(comparece.equals("")){
	        	defaultForm.setProperty("DPCR_PARTICIPANTES_COMPARECE:COMPARECE", "SI");
	        	defaultForm.setProperty("DPCR_PARTICIPANTES_COMPARECE:NOTIFICACION", "SI");
	        	defaultForm.setProperty("DPCR_PARTICIPANTES_COMPARECE:DNI_PARTICIPANTE", dni);
	        	boolean alta = CompareceUtil.comprobarUsuario(dni, entidad);
				
				if(!alta){
					info = new ISPACException("No esta dado de alta en el comparece", true);
					request.getSession().setAttribute("infoAlert", info);
					boolean correcto = CompareceUtil.altaTercero(dni, email, movil, entidad);
					if(correcto){
						info = new ISPACException("Se ha producido correctamente la insercción", true);
						request.getSession().setAttribute("infoAlert", info);
					}
					else{
						info = new ISPACException("Error al realizar la insercción", true);
						request.getSession().setAttribute("infoAlert", info);
					}
				}
				else{
					info = new ISPACException("Esta dado de alta en el comparece", true);
					request.getSession().setAttribute("infoAlert", info);
				}
	        }
			//Si quiere que se le de alta en el comparece.
			if(comparece.equals("SI")){
				boolean alta = CompareceUtil.comprobarUsuario(dni, entidad);
				
				if(!alta){
					info = new ISPACException("No esta dado de alta en el comparece", true);
					request.getSession().setAttribute("infoAlert", info);
					boolean correcto = CompareceUtil.altaTercero(dni, email, movil, entidad);
					if(correcto){
						info = new ISPACException("Se ha producido correctamente la insercción", true);
						request.getSession().setAttribute("infoAlert", info);
					}
					else{
						info = new ISPACException("Error al realizar la insercción", true);
						request.getSession().setAttribute("infoAlert", info);
					}
				}
				else{
					info = new ISPACException("Esta dado de alta en el comparece", true);
					request.getSession().setAttribute("infoAlert", info);
				}
			}
			
			// Actualizar el estado con los datos del formulario (almacenando el id de entidad y el regId del registro de la entidad)
			String entity = defaultForm.getEntity();
			if (!StringUtils.isEmpty(entity)) {
				currentstate.setEntityId(TypeConverter.parseInt(entity, ISPACEntities.ENTITY_NULLREGKEYID));
			}
			String key = defaultForm.getKey();
			if (!StringUtils.isEmpty(key)) {
				currentstate.setEntityRegId(TypeConverter.parseInt(key, ISPACEntities.ENTITY_NULLREGKEYID));
			}
	
			// Si el estado esta en solo lectura, no se procedera a guardar los datos.
			// No se deberia dar el caso de intentar guardar estando en solo lectura, 
			// pero si se diera el caso nos aseguramos que no se permita guardar datos.
			// Y evitar guardar cuando se ha perdido la sesión.
			if ((!currentstate.getReadonly()) &&
				(currentstate.getEntityId() != ISPACEntities.ENTITY_NULLREGKEYID)) {
	
				int keyId = currentstate.getEntityRegId();
	
				EntityApp entityapp = null;
				
				// Ejecución en un contexto transaccional
				boolean bCommit = false;
	
				try {
					// Abrir transacción
					cct.beginTX();
					int entityId = currentstate.getEntityId();
	
					// Si el identificador del registro es nulo (-1) (alta de registro en la entidad) se debe
					// crear una nueva entrada
					if (keyId == ISPACEntities.ENTITY_NULLREGKEYID) {
						
						//creacion de un registro de entidad (al llamar a create se obtiene un id de la secuencia de la entidad)
						IItem newitem = entapi.createEntity(entityId);
						
						//se almacena en el estado el id del objeto q se va a crear
						currentstate.setEntityRegId(newitem.getKeyInt());
						
						// Se inserta el Numero de expediente en el registro de la entidad (si tiene campo NUMEXP)
						IItem itemCat = entapi.getCatalogEntity(entityId);
						String fieldNumExp = itemCat.getString("CAMPO_NUMEXP");
						if (StringUtils.isNotEmpty(fieldNumExp)) {
							newitem.set(fieldNumExp, currentstate.getNumexp());
						}
						
						newitem.store(cct);
					}
					
					// No utilizar el formulario por defecto
					// (para ver los documentos del expediente en el trámite)
					boolean noDefault = false;
					if (request.getParameter("nodefault")!= null && request.getParameter("nodefault").equals("true")) {
						noDefault = true;
					}
	
					// Obtener la aplicación que gestiona la entidad
					entityapp = scheme.getEntityApp(currentstate, getRealPath(""), currentstate.getEntityRegId(), noDefault);
	
					// Permite modificar los datos del formulario
					defaultForm.setReadonly("false");
					// Salva el identificador de la entidad
					defaultForm.setEntity(Integer.toString(entityId));
					// Salva el identificador del registro
					defaultForm.setKey(Integer.toString(keyId));
					//recoger los datos del formulario y pasarlos a la entidad a traves
					//del entityApp invocando a su metodo setValue
					defaultForm.processEntityApp(entityapp);
					
					//el entityApp ya tiene los valores de la entidad. Ahora se validan
					if (!entityapp.validate()) {
	
						ActionMessages errors = new ActionMessages();
						List<?> errorList = entityapp.getErrors();
						Iterator<?> iteError = errorList.iterator();
						while (iteError.hasNext()) {
							
							// Mostrar los errores de la validación del entityapp
							ValidationError validError = (ValidationError) iteError.next();
							ActionMessage error = new ActionMessage(validError.getErrorKey(), validError.getArgs());
							errors.add(validError.getProperty(), error);
						}
						
						//saveErrors(request, errors);
						request.getSession().setAttribute(Globals.ERROR_KEY, errors);
						//sAction = "error";
											
						// como hubo error si el identificador del registro era nulo (-1) (se estaba creando un registro nuevo)
						//se anula la clave generada del estado
						
						if (keyId == ISPACEntities.ENTITY_NULLREGKEYID) {
							currentstate.setEntityRegId(ISPACEntities.ENTITY_NULLREGKEYID);
						}
					}
					else {
						// Guardar la entidad
						entityapp.store();
						
						// Si la validación es correcta se hace commit de la transacción
						bCommit = true;
					}
				}
				catch (ISPACException e) {
					
					// Y si el identificador del registro es nulo (-1) se anula la clave generada
					if (keyId == ISPACEntities.ENTITY_NULLREGKEYID) {
						currentstate.setEntityRegId(ISPACEntities.ENTITY_NULLREGKEYID);
					}
					
					// Guardar el estado actual para evitar que se muestre otra entidad, esto ocurre
					// cuando anteriormente se ha cargado esa otra entidad utilizando el workframe
					setStateticket(request, currentstate);
	
					if (entityapp != null) {
						
						throw new ISPACInfo(e.getMessage());
					}
					else {
						// Suele producirse error en las secuencias al estar mal inicializadas
						// provocando una duplicación de keys
						throw new ISPACRuleException(e);
					}
				}
				finally {
					cct.endTX(bCommit);
				}
			}
        }
        else{
        	//Ya esta creado este representante.
        	ISPACException info = new ISPACException("Este representante ya existe", true);
        	request.getSession().setAttribute("infoAlert", info);
        }
		
		ActionForward action = mapping.findForward(currentstate.getLabel());
		String param = currentstate.getQueryString();

		// Mantener la ordenación de la lista si existe
		String displayTagOrderParams = getDisplayTagOrderParams(request);
		if (!StringUtils.isEmpty(displayTagOrderParams)) {
			param = param + "&" + displayTagOrderParams;
		}
		
		// Mantener la pestaña de datos activa
    	if (request.getParameter("block") != null){
    		String block = "&block="+request.getParameter("block");
    		param = param + block;
    	}
    	
    	// Mantener la forma de presentar el formulario
    	if (request.getParameter("form") != null){
    		String paramForm = "&form="+request.getParameter("form");
    		param = param + paramForm;
    	}
    	
    	// Mantener no utilizar el formulario por defecto
    	if (request.getParameter("nodefault") != null){
    		String paramForm = "&nodefault="+request.getParameter("nodefault");
    		param = param + paramForm;
    	}
    	
		return new ActionForward(action.getName(), action.getPath() + param, action.getRedirect());
    	
	}
	
	private boolean modificacionTercero(String dni, String email, String movil, String entidad) throws ISPACRuleException {
		boolean correcto = true;
		AplicacionesServiceProxy asp = new AplicacionesServiceProxy();
		try {
			//Compruebo si el email y el movil del usuario es el diferente de la modificacion
			// Si es diferente hago la modificacion
			TerceroWS tercero = asp.consultarDNI(entidad, dni);
			if(tercero != null){
				String emailComparece = tercero.getEmail();
				String movilComparece = tercero.getTelefono()+"";
				if(!emailComparece.equals(email) || !movilComparece.equals(movil)){
					correcto = asp.modificarTercero(entidad, dni, email, movil);
				}
			}
		} catch (RemoteException e) {
			throw new ISPACRuleException(e);
		}
		return correcto;
	}
}
