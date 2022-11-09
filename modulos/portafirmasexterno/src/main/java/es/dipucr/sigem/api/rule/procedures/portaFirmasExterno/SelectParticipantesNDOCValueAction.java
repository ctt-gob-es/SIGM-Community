package es.dipucr.sigem.api.rule.procedures.portaFirmasExterno;


import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnectorFactory;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.SelectSubstituteAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;


public class SelectParticipantesNDOCValueAction  extends SelectSubstituteAction
{
	private Logger LOGGER = Logger.getLogger(SelectParticipantesNDOCValueAction.class);

	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception
	  {
	    
		  //Estado del contexto de tramitación
		    ClientContext cct = session.getClientContext();
	 		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
			try {
				IState state = managerAPI.currentState(getStateticket(request));
				 //logger.warn("strQuery "+strQuery);
			    IItemCollection icServContrat = ParticipantesUtil.getParticipantes(cct, state.getNumexp(), "ROL='INT'", "");
			    Iterator<?> itServContrat = icServContrat.iterator();

			    ArrayList<ItemBean> vect = new ArrayList<ItemBean>();
				while (itServContrat.hasNext()) {
					IItem itemServContrat = (IItem) itServContrat.next();
					
					String dni = "";
					if(StringUtils.isNotEmpty(itemServContrat.getString("NDOC"))) dni = itemServContrat.getString("NDOC");
					String nombre = "";
					if(StringUtils.isNotEmpty(itemServContrat.getString("NOMBRE"))) nombre = itemServContrat.getString("NOMBRE");
					String email = "";
					if(StringUtils.isNotEmpty(itemServContrat.getString("EMAIL"))) email = itemServContrat.getString("EMAIL");
					if(StringUtils.isNotEmpty(dni) && StringUtils.isNotEmpty(nombre)){
						
						String [] nombreApellidos = nombre.split(" ");
						String apellidos = "";
						if(nombreApellidos.length>0){
							nombre = nombreApellidos[0];
							for(int i = 1; i< nombreApellidos.length; i++){
								apellidos = apellidos + " " +nombreApellidos[i];
							}
						}
						
						crearUsuarioPortafirmas(EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador(), dni, nombre, apellidos, email);
						
						ItemBean itemB = new ItemBean();
						itemB.setProperty("VALOR",dni);
						itemB.setProperty("SUSTITUTO", dni+" - "+nombre+ " "+ apellidos);	
						itemB.setProperty("CARGO", "");
						itemB.setProperty("DESCRIPCION_CARGO", "");
						
						vect.add(itemB);
					}
					List<ItemBean> list = vect.subList(0, vect.size());
				    request.setAttribute("SubstituteList", list);		
				}		    

				processFormatter(request, "/digester/subtitutoformatter.xml");
			} catch (ISPACException e) {
				LOGGER.error("Error en el método state. "+e.getMessage(), e);
				throw new ISPACRuleException("Error en el método state. "+e.getMessage(),e);
			}	   
			
			return mapping.findForward("success");
		  }
	
	private void crearUsuarioPortafirmas(String entidadId, String dni, String nombre, String apellidos, String email) throws ISPACRuleException {
		
    	//Solo tiene un campo para el apellido, no podemos saber el segundo apellido
		try {
			ProcessSignConnector portafirmasSignConnector = ProcessSignConnectorFactory.getInstance(entidadId).getProcessSignConnector();
			if(StringUtils.isNotEmpty(dni)){
				if(!portafirmasSignConnector.existeUsuarioPortafirmas(dni)){
					portafirmasSignConnector.crearUsuarioPortafirmas(entidadId, dni, nombre, StringUtils.defaultString(apellidos), "", email);
				
				}
			}
		} catch (ISPACException e) {
			LOGGER.error("ERROR al crear el usuario: " + StringUtils.defaultString(dni) + " - " + StringUtils.defaultString(nombre) + " " + StringUtils.defaultString(apellidos), e);
			throw new ISPACRuleException("ERROR al crear el usuario: " + StringUtils.defaultString(dni) + " - " + StringUtils.defaultString(nombre) + " " + StringUtils.defaultString(apellidos), e);
		}
	}
		  	  
	}
