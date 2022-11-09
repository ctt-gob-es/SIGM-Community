package ieci.tdw.ispac.ispaccatalog.action.signprocess;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.action.BaseAction;
import ieci.tdw.ispac.ispaccatalog.action.form.UsersForm;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnectorFactory;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.Signer;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.ServicioEstructuraOrganizativa;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

public class ViewSignersAction extends BaseAction {

	/** Logger de la clase. */
	private static final Logger logger = Logger
			.getLogger(ViewSignersAction.class);

	protected static final String PARAM_UID		 			= "uid";
	protected static final String PARAM_ACTION		 		= "action";
	protected static final String PARAM_SIGNER_NAME		 	= "signerName";
	protected static final String PARAM_SIGNER_TYPE		 	= "signerType";
	protected static final String PARAM_INVALID_SELECT 		= "invalidSelect";

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		IInvesflowAPI invesFlowAPI = session.getAPI();
        IRespManagerAPI respAPI = invesFlowAPI.getRespManagerAPI();

		UsersForm userForm = (UsersForm) form;

		String filtro = request.getParameter("filtro");

		String busqueda = request.getParameter("busqueda");

        // UID en el formulario para el radio seleccionado al aceptar
		if (!"true".equals(request.getParameter("accept")) ||
	        	(request.getAttribute(PARAM_INVALID_SELECT) != null)) {
    		List<Signer> signers = new ArrayList <Signer>() ;
    		List<Signer> signersFinal = new ArrayList <Signer>() ;
    		if (StringUtils.isNotEmpty(busqueda)){

        		if (logger.isDebugEnabled()) {
        			logger
        					.info("ViewSignersAction->Comienzo action viewSignerAction. Se va a obtener el conector del portafirmas....");
        		}

    			ProcessSignConnector processSignConnector = ProcessSignConnectorFactory.getInstance(session.getClientContext()).getProcessSignConnector();

    			if (logger.isDebugEnabled()) {
        			logger
        					.info("ViewSignersAction->Conector del portafirmas obtenido..Buscamos los firmantes");
        		}

    			try{
    				
	    			signers = processSignConnector.getSigners(session.getClientContext(), filtro);
	    			/**
	    			 * Ticket1273#Teresa Compruebo que est�n en la estrucutra organizativa
	    			 * **/
	    			for (Signer signer : signers) {
	        			ServicioEstructuraOrganizativa estructu = LocalizadorServicios.getServicioEstructuraOrganizativa();
	        			try{
	        				//INICIO [dipucr-Felipe #1514] Distintos cargos por usuario
	        				Usuarios usuarios = estructu.getUsuariosByNif(signer.getIdentifier(), EntidadesAdmUtil.obtenerEntidadObject(session.getClientContext()).getIdentificador());
	        				List<Usuario> listaUsuarios = usuarios.get_list();
	        				if (listaUsuarios.size() > 0){
	        					if (listaUsuarios.size() == 1){
	        						Usuario usuarioALSIGM = listaUsuarios.get(0);
			        				signer.setNameALSIGM(usuarioALSIGM.get_name());
			        				signer.setIdALSIGM("1-"+usuarioALSIGM.get_id());
			        				signersFinal.add(signer);
	        					}
	        					else{
	        						for (Usuario usuarioALSIGM : listaUsuarios){
	        							Signer signerAux = new Signer();
	        							signerAux.setIdentifier(signer.getIdentifier());
	        							signerAux.setName(signer.getName());
	        							signerAux.setNameALSIGM(usuarioALSIGM.get_name());
	        							signerAux.setIdALSIGM("1-"+usuarioALSIGM.get_id());
				        				signersFinal.add(signerAux);
	        						}
	        					}
		        			} 
	        				//FIN [dipucr-Felipe #1514] Distintos cargos por usuario
	        			}catch(Exception e){
	        				
	        			}
	        			       			
	    			}
    			}catch(Exception e){
    				logger.error("Se produjo una excepci�n al invocar al m�todo getSigners del conector del portafirmas con el filtro:"+filtro+" - "+e.getMessage(),e);
    				//Se ha detectado que si se busca por un filtro que no devuelve nada en lugar de devolver un listado vac�o o nulo devuelve una excepci�n
    				//por eso no se lanza excepci�n para que se muestre la p�gina sin datos

    			}
    		}
    		
    		

    		request.setAttribute("signers", signersFinal);
    		request.setAttribute("filtro", filtro);

    		if (logger.isDebugEnabled()) {
    			logger.info("ViewSignersAction->Hemos obtenido " + signers.size()
    					+ " firmantes");
    		}

    		request.removeAttribute("busqueda");
            request.setAttribute("URLParams", buildURLParams(request.getParameterMap()));
            return mapping.findForward("list");
        } else {

        	String signerName = request.getParameter("property(SIGNER_NAME)");
        	String signerType = request.getParameter("property(SIGNER_TYPE)");

        	String signerNameParameter = StringUtils.EMPTY;
        	String signerTypeParameter = StringUtils.EMPTY;

        	if (StringUtils.isNotEmpty(signerName)){
        		signerNameParameter = "&" +	PARAM_SIGNER_NAME +	"=" + signerName;
        	}

        	if (StringUtils.isNotEmpty(signerType)){
        		signerTypeParameter = "&" +	PARAM_SIGNER_TYPE +	"=" + signerType;
        	}

        	if(userForm.getUid()!=null){
            return new ActionForward("success", request.getParameter(PARAM_ACTION) +
            									"?" +
            									buildURLParams(request.getParameterMap()) +
            									"&" +
            									PARAM_UID +
            									"=" +
            									userForm.getUid()
            									+ signerNameParameter
            									+ signerTypeParameter
            									, true);
        	}
        	else{
        		 return new ActionForward("success", request.getParameter(PARAM_ACTION) +
							"?" +
							buildURLParams(request.getParameterMap()) +
							"&" +
							PARAM_UID +
							"=" +
							((String [])userForm.getMultibox())
							+ signerNameParameter
							+ signerTypeParameter
							, true);
        	}
        }
	}

	protected String buildURLParams(Map parameters) {

		StringBuffer url = new StringBuffer();
		String key = null;
		Map urlKeys = new HashMap();

		Iterator it = parameters.keySet().iterator();
		while (it.hasNext()) {

			key = (String) it.next();
			if ((!urlKeys.containsKey(key)) &&
					(!key.equals(PARAM_UID) && !key.equals(PARAM_SIGNER_NAME) && !key.equals(PARAM_SIGNER_TYPE) && (key.indexOf("property") == -1))) {
				String [] array=(String[]) parameters.get(key);
				//Recorremos todo el arraya para salvar todos los valores
				for(int i=0; i<array.length; i++){
					if (url.length() != 0) {
						url.append("&");
					}
					url.append(key).append("=").append(((String[])parameters.get(key))[i]);
				}

				urlKeys.put(key, null);
			}
		}

		return url.toString();
	}
}
