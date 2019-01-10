package es.dipucr.sigem.api.rule.procedures.portaFirmasExterno;


import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacmgr.action.SelectSubstituteAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.EnhancedUserJobAssociated;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryEnhancedUserJobAssociatedToUser;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryEnhancedUserJobAssociatedToUserResponse;
import es.dipucr.portafirmas.common.Configuracion;
import es.dipucr.portafirmas.common.ServiciosWebPortaFirmasFunciones;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;


public class SelectParticipantesNDOCValueAction  extends SelectSubstituteAction
{
	  //private Logger logger = Logger.getLogger(SelectListadoCodicePliegoAction.class);

	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session)
	    throws Exception
	  {
	    
		  //Estado del contexto de tramitación
		    ClientContext cct = session.getClientContext();
	 		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
	 		IState state = managerAPI.currentState(getStateticket(request));
		    
		    //logger.warn("strQuery "+strQuery);
		    IItemCollection icServContrat = ParticipantesUtil.getParticipantes(cct, state.getNumexp(), "ROL='INT'", "");
		    Iterator<IItem> itServContrat = icServContrat.iterator();

		    ArrayList<ItemBean> vect = new ArrayList<ItemBean>();
			while (itServContrat.hasNext()) {
				IItem itemServContrat = itServContrat.next();
	
				ItemBean itemB = new ItemBean();
				itemB.setProperty("VALOR", itemServContrat.getString("NDOC"));
				itemB.setProperty("SUSTITUTO", itemServContrat.getString("NDOC")+" - "+itemServContrat.getString("NOMBRE"));
				
				//String direccionPortaFirmaExternoConsulta = DipucrCommonFunctions.getVarGlobal(Configuracion.DIRECCION_PORTAFIRMASEXTERNO_CONSULTA);
				String direccionPortaFirmaExternoConsulta = ServiciosWebPortaFirmasFunciones.getDireccionSWConsulta();
				QueryServiceStub consulta = new QueryServiceStub(direccionPortaFirmaExternoConsulta);

				QueryEnhancedUserJobAssociatedToUser queryEnhancedUserJobAssociatedToUser = new QueryEnhancedUserJobAssociatedToUser();
				queryEnhancedUserJobAssociatedToUser.setAuthentication(Configuracion.getAuthenticationConsultaPADES(cct));
				queryEnhancedUserJobAssociatedToUser.setUserIdentifier(itemServContrat.getString("NDOC").trim());
				QueryEnhancedUserJobAssociatedToUserResponse respuesta = consulta.queryEnhancedUserJobAssociatedToUser(queryEnhancedUserJobAssociatedToUser);
				EnhancedUserJobAssociated [] enhancedUserJobAssociated = respuesta.getEnhancedUserJobAssociatedList().getEnhancedUserJobAssociated();
				if(enhancedUserJobAssociated!=null && enhancedUserJobAssociated.length>0){
					String identificador = enhancedUserJobAssociated[0].getEnhancedJob().getJob().getIdentifier();
					String [] vIdent = identificador.split(" - ");
					String cargo = "";
					if(vIdent.length>1){
						cargo = vIdent[1];
					}
					if(vIdent.length==1){
						cargo = vIdent[0];
					}
					itemB.setProperty("CARGO", cargo);
					
					String descripCago = enhancedUserJobAssociated[0].getEnhancedJob().getJob().getDescription();
					String [] vDesc = descripCago.split(" - ");
					String desCargo = "";
					if(vDesc.length>1){
						desCargo = vDesc[1];
					}
					if(vDesc.length==1){
						desCargo = vDesc[0];
					}
					itemB.setProperty("DESCRIPCION_CARGO", desCargo);
				}
				else{
					itemB.setProperty("CARGO", "");
					itemB.setProperty("DESCRIPCION_CARGO", "");
				}
				vect.add(itemB);
	
			}
		    //int maxResultados = vect.size();
		    
		    List<ItemBean> list = vect.subList(0, vect.size());
			//request.setAttribute("SubstituteList", list);
		    request.setAttribute("SubstituteList", list);

			processFormatter(request, "/digester/subtitutoformatter.xml");
			
			return mapping.findForward("success");
		  }
		  	  
	}
