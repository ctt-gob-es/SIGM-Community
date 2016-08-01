package es.dipucr.sigem.actions.manualesUsuario.catalogo;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IManualUsuarioAPI;
import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaccatalog.action.BaseAction;
import ieci.tdw.ispac.ispaccatalog.action.form.UploadForm;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.app.EntityApp;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.bean.ValidationError;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuario;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

public class StoreManualUsuarioAction extends BaseAction
{
	public ActionForward executeAction(
	ActionMapping mapping,
	ActionForm form,
	HttpServletRequest request,
	HttpServletResponse response,
	SessionAPI session)
	throws Exception
	{
		
 		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, session.getClientContext(), new int[] {
				ISecurityAPI.FUNC_INV_DOCTYPES_EDIT,
				ISecurityAPI.FUNC_INV_TEMPLATES_EDIT });
		
		IInvesflowAPI invesFlowAPI = session.getAPI();
        ICatalogAPI catalogAPI = invesFlowAPI.getCatalogAPI();
    	IManualUsuarioAPI manualUsuarioAPI = invesFlowAPI.getManualUsuarioAPI();

    	// Formulario asociado a la acción
		UploadForm defaultForm = (UploadForm) form;

		int keyId = Integer.parseInt(defaultForm.getKey());
		int entityId = Integer.parseInt(defaultForm.getEntity());
		String nombre = defaultForm.getProperty("NOMBRE");
		String descripcion = defaultForm.getProperty("DESCRIPCION");
		String version = defaultForm.getProperty("VERSION");
		String visibilidad = defaultForm.getProperty("VISIBILIDAD");
		String tipo = defaultForm.getProperty("TIPO");
		String url = defaultForm.getProperty("URL");
		CTManualUsuario manualUsuario = null;

		try {
			
			if (keyId == ISPACEntities.ENTITY_NULLREGKEYID) {
				
				FormFile fichero = defaultForm.getUploadFile();
				EntityApp entityapp = catalogAPI.getCTDefaultEntityApp(entityId, getRealPath(""));
				
				// Comprobar si existe otra plantilla con el mismo nombre
				IItemCollection itemcol = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_CT_MANUALES_USUARIO, 
						" WHERE NOMBRE = '" + DBUtil.replaceQuotes(nombre) + "'");				
				if (itemcol.next() && !isGeneric(manualUsuarioAPI, itemcol)) {					
					ActionMessages errors = new ActionMessages();
					errors.add("property(NOMBRE)", new ActionMessage("error.template.nameDuplicated", new String[] {nombre}));
					saveAppErrors(request, errors);
					
					return new ActionForward(mapping.getInput());
				}
				
//				// Comprobar si existe otra plantilla con el mismo código
//				if (StringUtils.isNotBlank(code)) {
//					itemcol = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_P_MANUALES_USUARIO, 
//							" WHERE COD_PLANT = '" + DBUtil.replaceQuotes(code) + "'");				
//					if (itemcol.next()) {					
//						ActionMessages errors = new ActionMessages();
//						errors.add("property(COD_PLANT)", new ActionMessage("error.template.codeDuplicated", new String[] {code}));
//						saveAppErrors(request, errors);
//						
//						return new ActionForward(mapping.getInput());
//					}
//				}

				if (!entityapp.validate()) {
					
					ActionMessages errors = new ActionMessages();
					List<?> errorList = entityapp.getErrors();
					Iterator<?> iteError = errorList.iterator();
					while (iteError.hasNext()) {
						
						ValidationError validError = (ValidationError) iteError.next();
						ActionMessage error = new ActionMessage(validError.getErrorKey(), validError.getArgs());
						errors.add(ActionMessages.GLOBAL_MESSAGE, error);
					}
					saveAppErrors(request, errors);
					
					return new ActionForward(mapping.getInput());
				}				
				
				if (fichero.getFileName().equals("")) {
					manualUsuario = manualUsuarioAPI.newManualUsuario(nombre, descripcion, version, visibilidad, tipo, url, null);
				} else {
					if (fichero.getFileSize() > 0) {
						
						// Comprobar si el tipo MIME de la plantilla está soportado
						String mimeType = MimetypeMapping.getFileMimeType(fichero.getFileName());						
						manualUsuario = manualUsuarioAPI.newManualUsuario(nombre, descripcion, version, visibilidad, tipo, url, fichero.getInputStream(), mimeType);

					} else { 
						throw new ISPACInfo("exception.uploadfile.empty");
					}
				}
			}
			else {				
				EntityApp entityapp = catalogAPI.getCTDefaultEntityApp(entityId, getRealPath(""));
				
				// Comprobar si existe otra plantilla con el mismo nombre
				IItemCollection itemcol = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_CT_MANUALES_USUARIO, 
						" WHERE NOMBRE = '" + DBUtil.replaceQuotes(nombre) + "' AND ID != " + keyId);				
 				if (itemcol.next() && !isGeneric(manualUsuarioAPI, itemcol)) {					
					ActionMessages errors = new ActionMessages();
					errors.add("property(NOMBRE)", new ActionMessage("error.template.nameDuplicated", new String[] {nombre}));
					saveAppErrors(request, errors);
					
					return new ActionForward(mapping.getInput());
				}
 				
//				// Comprobar si existe otra plantilla con el mismo código
//				if (StringUtils.isNotBlank(code)) {
//					itemcol = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_CT_TEMPLATE,
//							" WHERE COD_PLANT = '" + DBUtil.replaceQuotes(code) + "' AND ID != " + keyId);
//					if (itemcol.next()) {					
//						ActionMessages errors = new ActionMessages();
//						errors.add("property(COD_PLANT)", new ActionMessage("error.template.codeDuplicated", new String[] {code}));
//						saveAppErrors(request, errors);
//						
//						return new ActionForward(mapping.getInput());
//					}
//				}
 				
				defaultForm.processEntityApp(entityapp);
				entityapp.getItem().set("FECHA", new Date());
				entityapp.store();
			}
			
		} catch(Exception e) {
			ActionForward action = mapping.findForward("success");
			String urlRedirec = action.getPath()
							  + "?entity="+ entityId
							  + "&type="+ defaultForm.getProperty("ID_TPDOC")
							  + "&key="+ keyId;

			request.getSession().setAttribute(BaseAction.LAST_URL_SESSION_KEY, urlRedirec);
			
			if (e instanceof ISPACInfo) {
				throw e;
			} else {
				throw new ISPACInfo(e.getMessage());
			}
		}
		
		if (manualUsuario != null) {
			keyId = manualUsuario.getManualUsuario();
		}
		
		ActionForward action = mapping.findForward("success");
		String redirected = action.getPath()
						  + "?entity="+ entityId
						  + "&type="+ defaultForm.getProperty("ID_TPDOC")
						  + "&key="+ keyId;
		return new ActionForward( action.getName(), redirected, true);
	}
	
	/**
	 * Para saber si el nombre de la plantilla que se esta creando coincide con una plantilla genérica o especifica.
	 * En este caso si el nombre de la nueva plantilla coincide con una plantilla específica no pasaría nada.
	 * @param manualUsuarioAPI
	 * @param itemcol
	 * @return
	 * @throws ISPACException 
	 */
	private boolean isGeneric(IManualUsuarioAPI manualUsuarioAPI, IItemCollection itemcol) throws ISPACException {
		
		List<?> manualesUsuario = CollectionBean.getBeanList(itemcol);
		for(Iterator<?> iter = manualesUsuario.iterator(); iter.hasNext();) {
			ItemBean item = (ItemBean) iter.next();
			if(manualUsuarioAPI.isProcedureManualUsuario(item.getItem().getInt("ID")))
				return true;
		}
		return false;
	}
	
}