package es.dipucr.sigem.api.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.ThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.svd.common.DipucrFuncionesComunesSVD;
import es.dipucr.svd.services.ServiciosWebSVDFunciones;
import es.dipucr.verifdatos.services.ClienteLigeroProxy;
import es.dipucr.verifdatos.services.ScspProxy;

public class PeticionSincronaAction extends BaseAction {
	

	private static final Logger logger = Logger.getLogger(PeticionSincronaAction.class);


	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		//----------------------------------------------------------------------------------------------
		ClientContext cct = session.getClientContext();
		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState state = managerAPI.currentState(getStateticket(request));
		IState currentState = managerAPI.currentState(getStateticket(request));
        //----------------------------------------------------------------------------------------------
		
		// Codigo de procedimiento
		String procSelec = request.getParameter("procedimiento");
		String emisor = request.getParameter("emisorServicio");
		//dni
		String dni = request.getParameter("dniValue");
		//nombre dni
		String nombreDni = request.getParameter("nombreDniValue");

		//Busco el id de la entidad
        IItemCollection coll = entitiesAPI.queryEntities("SPAC_CT_ENTIDADES", "WHERE NOMBRE='DPCR_SVD'");
        Iterator<?> it = coll.iterator();
        String iEntidadSVD = "";
        if (it.hasNext())
        {
        	IItem iExpRel = (IItem)it.next();
        	iEntidadSVD = iExpRel.getInt("ID")+"";
	        
        }
		
    	
    	String url = ServiciosWebSVDFunciones.getDireccionClienteLigeroSW();    	
		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy(url);
		//DatosGeograficosManager cgm = clienteLigero.getDatosGeograficosManager();
		//En el vector devuelve dos posiciones
		//0: getDescripcion
		//1. .getClassDatosEspecificos()
		String [] clienteLig = clienteLigero.getCertAutByCodCert(EntidadesAdmUtil.obtenerNombreLargoEntidadById(cct), procSelec);

    	
    	String urlSCSP = ServiciosWebSVDFunciones.getDireccionSCSPSW();		
		ScspProxy scsp = new ScspProxy(urlSCSP);
		//array de dos posiciones la primera el nombre del servicio y la segunda con la version del esquema
		String[] service = scsp.getCoreServByCodCertificadoDescriVersion(EntidadesAdmUtil.obtenerNombreLargoEntidadById(cct), procSelec);
		
		//Devuelve el contenido que tiene el fichero de la tabla 'certificados_autorizaciones' columna 'xml_template'
		String strDatosEspecificos = clienteLigero.getXmlDatosEspecificos(EntidadesAdmUtil.obtenerNombreLargoEntidadById(cct), procSelec);
		int taskId = currentState.getTaskId();
		//No existen datos específicos
		//if(clienteLig[1] == null || (procSelec.equals("AEAT101I") || procSelec.equals("AEAT102I") || procSelec.equals("AEAT103I") || procSelec.equals("AEAT104I"))){
		//Q2827003ATGSS001-> ya que me devuelve datos especificos y si lo vemos en el formulario del cliente ligero no pide datos
		//if(clienteLig[1] == null || strDatosEspecificos==null || strDatosEspecificos.equals("") || procSelec.equals("Q2827003ATGSS001")){
		if(clienteLig[1] == null || procSelec.equals("Q2827003ATGSS001") || (procSelec.equals("ECOT101I") || procSelec.equals("ECOT102I") || procSelec.equals("ECOT103I") || procSelec.equals("ECOT104I"))){
			envioPeticionDirecta(request,session);
			
			ActionForward actionForward = new ActionForward();
			actionForward.setPath("/showTask.do?taskId=" + taskId);
			actionForward.setRedirect(true);	
			
			return actionForward;
		}
		//Contiene datos específicos
		else{
			//TODO queda probar  la peticion con datos específicos.
			//Unidad Tramitadora
			String numexp = state.getNumexp();
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
	        String sNombreDepartamento = itemExpediente.getString("SECCIONINICIADORA");
	        
	        //procedimiento
	        int pdcId = state.getPcdId();
			IItem ctProcedure = entitiesAPI.getEntity(SpacEntities.SPAC_CT_PROCEDIMIENTOS, pdcId);
			String codProc = DipucrFuncionesComunesSVD.getCODProcGenericoByCODProcEspecifico(entitiesAPI, ctProcedure.getString("COD_PCD"));
			//String codProc = ctProcedure.getString("COD_PCD");
			String nomProc = ctProcedure.getString("NOMBRE");
			
			//Finalidad
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			String finalidad = "Entidad: --> "+entidad+" ## "+nomProc;
			
			//Datos del titular
			//Buscamos en spac_dt_intervinientes
        	IItemCollection participantes = ParticipantesUtil.getParticipantes(cct, numexp,  "ROL != 'TRAS' OR ROL IS NULL", "ID");
        	Iterator<?> itParticipante = participantes.iterator();

        	String nombrePartic = "";
    		String ndocPartic = "";
        	while(itParticipante.hasNext()){
        		IItem participante = (IItem)itParticipante.next();
        		
        		if ((String)participante.getString("NDOC")!=null) ndocPartic = (String)participante.getString("NDOC");     		
	        	if ((String)participante.getString("NOMBRE")!=null) nombrePartic = (String)participante.getString("NOMBRE");
        	}
        	
        	request.setAttribute("numexp", numexp);
        	request.setAttribute("nombrePartic", nombrePartic);
        	request.setAttribute("ndocPartic", ndocPartic);
			request.setAttribute("finalidad", finalidad);
			request.setAttribute("nombreProcedimiento", nomProc);
			request.setAttribute("codigoProcedimiento", codProc);
			request.setAttribute("unidadTramitadora", sNombreDepartamento);
			request.setAttribute("nombreSolicitante", "");
			request.setAttribute("cifSolicitante", "");
			request.setAttribute("codigoCertificado", procSelec);
			//request.setAttribute("certificadoAutorizacino", certAut);
			request.setAttribute("descripcion", clienteLig[0]);
			//Clase de ese servicio
			request.setAttribute("xmlDatosEspecificos", clienteLig[1]);
			request.setAttribute("coreDescripcion", service[0]);
			//request.setAttribute("servicio", service);
			request.setAttribute("version", service[1].indexOf("V3") > -1 ? "V3" : "V2");
			request.setAttribute("isV2", service[1].indexOf("V2") > -1 ? true : false);
			request.setAttribute("defaultFinalidad", "");
			request.setAttribute("nombreFuncionario", nombreDni);
			request.setAttribute("cifFuncionario", dni);
			request.setAttribute("procedimientos", "");
			request.setAttribute("procedimientosCount", 0);
			request.setAttribute("usuario", "");
			request.setAttribute("taskId", taskId+"");
			//request.setAttribute("strDatosEspecificos", strDatosEspecificos);
			request.setAttribute("emisor", emisor);
			request.setAttribute("session", session);
			request.setAttribute("entidad", iEntidadSVD);
			
	
			return mapping.findForward("success");
		}
	}


	private void envioPeticionDirecta(HttpServletRequest request, SessionAPI session) throws ISPACRuleException {
		String nombreProc = "";
		try{
			//----------------------------------------------------------------------------------------------
			ClientContext cct = session.getClientContext();
			IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
			IState currentState = managerAPI.currentState(getStateticket(request));
	        //----------------------------------------------------------------------------------------------
			
			// Codigo de procedimiento
			nombreProc = request.getParameter("nombre");

			StringBuffer sbRespuestaPdf = null;
			PeticionSincronaSinDatosEspec clienteRecubrimiento = new PeticionSincronaSinDatosEspec(nombreProc);
			try {
				//pongo = null porque no tiene datos especificos
				sbRespuestaPdf = clienteRecubrimiento.invoke(session, request, null);
				
			} catch (Exception e) {
				logger.error("Error en la búsqueda del nombre de procedimiento. "+nombreProc+" - "+e.getMessage(), e);
				throw new ISPACRuleException("Error en la búsqueda del nombre de procedimiento. "+nombreProc+" - "+e.getMessage(), e);
			}
			if(sbRespuestaPdf!=null){
				byte decoded[] = Base64.decodeBase64(sbRespuestaPdf.toString());
				//Guarda el resultado en gestor documental
				String STR_nombreTramite = "Servicio de Verificación de Datos";
		        int tpdoc = DocumentosUtil.getTipoDoc(cct, STR_nombreTramite, DocumentosUtil.BUSQUEDA_EXACTA, false);

		        int taskId = currentState.getTaskId();
				
				String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() +  "/" + FileTemporaryManager.getInstance().newFileName(".pdf");
				FileOutputStream  fos = null;
				try {
					fos = new FileOutputStream(rutaFileName);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				fos.write(decoded);
				fos.close();
				fos.flush();
				
				File file = new File(rutaFileName);
				
				IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(cct, taskId, tpdoc, nombreProc, file, Constants._EXTENSION_PDF);
				entityDoc.set("FFIRMA", new Date());
				entityDoc.store(cct);
				
				if(file != null && file.exists()) file.delete();
			}
			
		} catch (ISPACException e) {
			logger.error("Se produjo una excepción, con el nombre del procedimient "+nombreProc+" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Se produjo una excepción, con el nombre del procedimient "+nombreProc+" - "+ e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Se produjo una excepción, con el nombre del procedimient "+nombreProc+" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Se produjo una excepción, con el nombre del procedimient "+nombreProc+" - "+ e.getMessage(), e);
		}
	}
}
