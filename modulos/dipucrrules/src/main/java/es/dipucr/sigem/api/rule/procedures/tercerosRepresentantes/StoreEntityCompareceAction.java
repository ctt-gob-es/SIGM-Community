package es.dipucr.sigem.api.rule.procedures.tercerosRepresentantes;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
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

public class StoreEntityCompareceAction extends BaseAction {

	public ActionForward executeAction(ActionMapping mapping,
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {

		ClientContext cct = session.getClientContext();

		IEntitiesAPI entapi = session.getAPI().getEntitiesAPI();
		
		// Estado del contexto de tramitación
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState currentstate = managerAPI.currentState(getStateticket(request));
		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		
		IScheme scheme = managerAPI.getSchemeAPI();

		// Formulario asociado a la acción
		EntityForm defaultForm = (EntityForm) form;
		
		
		String nifciftitular = defaultForm.getProperty("SPAC_EXPEDIENTES:NIFCIFTITULAR");
		
		String consulta = "WHERE NIFCIFTITULAR='"+nifciftitular+"' AND (CODPROCEDIMIENTO='CR-TERC-01' OR CODPROCEDIMIENTO='PCD-222' OR CODPROCEDIMIENTO='PCD-223')";

        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXPEDIENTES, consulta);
        
        if (collection.toList().size()<2)
        {
        	//Quiere decir que ese expediente no ha sido creado

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

    			int entityId = 399;
    			int keyId = -1;

    			EntityApp entityapp = null;
    			
    			// Ejecución en un contexto transaccional
    			boolean bCommit = false;

    			try {
    				// Abrir transacción
    				cct.beginTX();

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
    				entityapp = scheme.getEntityApp(currentstate, entityId, keyId, 0, getRealPath(""), keyId, noDefault);

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
    				if (!validateError()) {

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
    					throw e;
    				}
    			}
    			finally {
    				cct.endTX(bCommit);
    			}
    		}
        	
        }
        else{
        	//Ya esta creado este representado.
        	ISPACException info = new ISPACException("Este representado ya existe", true);
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

	private boolean validateError() {
		// TODO Auto-generated method stub
		return false;
	}

}