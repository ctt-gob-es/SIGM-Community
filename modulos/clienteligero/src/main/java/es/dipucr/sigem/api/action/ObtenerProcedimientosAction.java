package es.dipucr.sigem.api.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.context.NextActivity;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.Defs;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.svd.common.DipucrFuncionesComunesSVD;
import es.dipucr.svd.services.ServiciosWebSVDFunciones;
import es.dipucr.verifdatos.services.ClienteLigeroProxy;

public class ObtenerProcedimientosAction extends BaseAction {
	
	private static final Logger logger = Logger.getLogger(ObtenerProcedimientosAction.class);
	
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
		

		Collection<String> certificados = new ArrayList<String>();
		BigInteger certId = new BigInteger("-1");
		//Obtenemos el certificado
		try{
			String attributeName = "javax.servlet.request.X509Certificate";
			Object obj = request.getAttribute(attributeName);
	      	if (obj instanceof java.security.cert.X509Certificate[]) {
	        	java.security.cert.X509Certificate[] certArray = (java.security.cert.X509Certificate[]) obj;
				certId = certArray[0].getSerialNumber();
			}
			if (obj instanceof java.security.cert.X509Certificate) {
	        	certificados.add(((X509Certificate) obj).getSubjectDN().toString());
	        	certId = ((X509Certificate) obj).getSerialNumber();
			}
	   	}catch(Exception e){
	   		logger.warn("Error. "+e.getMessage(), e);
	   		request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_LISTA_CERTIFICADOS);
	    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.toString());
	    	IState currentstate = managerAPI.currentState(getStateticket(request));
			return NextActivity.refresh(request, mapping, currentstate);
	   	}
		
		X509Certificate certificate;
		String certificadoId = ""+certId;
		
		try{
			if (certificadoId==null || certificadoId.equals("")){
				request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_SELECCIONAR_CERTIFICADO);
		    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: Al obtener el certificado de usuario");
		    	IState currentstate = managerAPI.currentState(getStateticket(request));
				return NextActivity.refresh(request, mapping, currentstate);
			}
			
			certificate = getX509Certificate(request, certificadoId);
			if(certificate == null){
				request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_SELECCIONAR_CERTIFICADO);
				request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: Al obtener el certificado de usuario");
				IState currentstate = managerAPI.currentState(getStateticket(request));
				return NextActivity.refresh(request, mapping, currentstate);
			}
		}catch(Exception e){
			logger.warn("Error. "+e.getMessage(), e);
			request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_SELECCIONAR_CERTIFICADO);
	    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.toString());
	    	IState currentstate = managerAPI.currentState(getStateticket(request));
			return NextActivity.refresh(request, mapping, currentstate);
	   	}
		request.getSession().setAttribute(Defs.CERTIFICADO_SELECCIONADO, certificate);
		//Validamos que sea correcto el certificado
		String sessionId = null;
		boolean aceptado = true;
   		String dn=certificate.getSubjectX500Principal().toString();
		
		String [] datosDni = dn.split(", ");
		String nombreDni = datosDni[0];
		String [] vNombreDni = nombreDni.split(" - ");
		String nombreDNI = vNombreDni[0];
		String [] vNombre = nombreDNI.split("CN=NOMBRE ");
		String nombre = "";
		try{
			nombre = vNombre[1];
		}catch(ArrayIndexOutOfBoundsException e){
			//CN=SERRANO GRANADOS EDUARDO JOSE - 05633523H, GIVENNAME=EDUARDO JOSE, SURNAME=SERRANO GRANADOS, SERIALNUMBER=05633523H, C=ES 
			vNombre = nombreDNI.split("CN=");
			nombre = vNombre[1];
		}
		String dni = vNombreDni[1];
		String [] vDni = dni.split(" ");
		
		String cif = "";
		try{
			cif = vDni[1];
		}
		catch(ArrayIndexOutOfBoundsException e){
			//CN=SERRANO GRANADOS EDUARDO JOSE - 05633523H, GIVENNAME=EDUARDO JOSE, SURNAME=SERRANO GRANADOS, SERIALNUMBER=05633523H, C=ES 
			cif = dni;
		}
		
		IItem ctProcedure = entitiesAPI.getEntity(SpacEntities.SPAC_CT_PROCEDIMIENTOS, state.getPcdId());
		String codigoProcedimiento = DipucrFuncionesComunesSVD.getCODProcGenericoByCODProcEspecifico(entitiesAPI, ctProcedure.getString("COD_PCD"));
		//String codigoProcedimiento = ctProcedure.getString("COD_PCD");
		logger.warn("codigoProcedimiento "+codigoProcedimiento);
		
		//Tabla que tiene la unión de los procedimientos de sigem con los procedimientos del cliente ligero.
		String strQuery = "WHERE VALOR = '"+codigoProcedimiento+"'";
		IItemCollection collection = entitiesAPI.queryEntities("DPCR_PROC_SVD", strQuery);
		Iterator<?> it = collection.iterator();
		String codProcSvd = "";
		while(it.hasNext()){
			IItem procedimiento_svd = (IItem) it.next();
			codProcSvd = procedimiento_svd.getString("SUSTITUTO");
		}
		logger.warn("codProcSvd "+codProcSvd);
    	
    	String url = ServiciosWebSVDFunciones.getDireccionClienteLigeroSW();
    	
		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy(url);
		//Devuelve un vector de string con:
		//codigo_#_nombre_#_emisor
		//VDISFWS01_#_Verificación de datos de identidad_#_MPTAP 
		String [] vListaServicios = null;
		try{
			vListaServicios = clienteLigero.consultaProcedimientoByNIF(EntidadesAdmUtil.obtenerNombreLargoEntidadById(cct), cif, codProcSvd);
		}catch (Exception e){
			logger.error("Se produjo una excepción - "+ e.getMessage(), e);
			throw new ISPACRuleException("Se produjo una excepción - "+ e.getMessage(), e);
		}
		
		logger.warn("vListaServicios "+vListaServicios);
		Vector<ItemBean> vect = new Vector<ItemBean>();
		if(vListaServicios != null){
			for(int i = 0; i < vListaServicios.length; i++){
				
				logger.warn("Procedimientos "+vListaServicios[i]);
				ItemBean itemB = new ItemBean();
				String [] proc = vListaServicios[i].split("_#_");
				itemB.setProperty("PROCEDIMIENTO", proc[0]);
				itemB.setProperty("NOMBRE", proc[1]);
				itemB.setProperty("EMISOR", proc[2]);
				itemB.setProperty("DNI", cif);
				itemB.setProperty("NOMBREDNI", nombre);
							
				vect.add(itemB);
			}
				
			List<ItemBean> list = vect.subList(0, vect.size());
			 request.setAttribute("ValueList", list);
			 
			 int taskId = currentState.getTaskId();
			 request.setAttribute("taskId", taskId+"");
			 request.setAttribute("numexp", state.getNumexp());
			//Busco el id de la entidad
	        IItemCollection coll = entitiesAPI.queryEntities("SPAC_CT_ENTIDADES", "WHERE NOMBRE='DPCR_SVD'");
	        Iterator<?> itEntidad = coll.iterator();
	        String iEntidadSVD = "";
	        if (itEntidad.hasNext())
	        {
	        	IItem iExpRel = (IItem)itEntidad.next();
	        	iEntidadSVD = iExpRel.getInt("ID")+"";
		        
	        }
			 request.setAttribute("entidad", iEntidadSVD);
			// Obtiene el decorador
			CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
			BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/procedimientoVerifDatosformatter.xml"));
			request.setAttribute("Formatter", formatter);
			return mapping.findForward("success");
			
		}
		else{
			ISPACInfo informacion=new ISPACInfo("El usuario no tiene permisos para ver ningún servicio. Contacte con el administrador.", "",false);
			request.getSession().setAttribute("infoAlert", informacion);
			IState currentstate = managerAPI.currentState(getStateticket(request));
			return NextActivity.refresh(request, mapping, currentstate);
		}
	}
		
	
	public static X509Certificate getX509Certificate(HttpServletRequest request, String serialNumber) {
		String attributeName = "javax.servlet.request.X509Certificate";

		Object obj = request.getAttribute(attributeName);
		X509Certificate certificate = null;
		if (obj instanceof java.security.cert.X509Certificate[]) {
			java.security.cert.X509Certificate[] certArray = (java.security.cert.X509Certificate[]) obj;
			for(int i=0; i<certArray.length; i++)
				if(certArray[i].getSerialNumber().toString().equals(serialNumber))
					return certArray[i];
			return null;
		}

		if (obj instanceof java.security.cert.X509Certificate) {
			certificate = (X509Certificate) obj;
			if(certificate.getSerialNumber().toString().equals(serialNumber))
				return certificate;
			else return null;
		}

		return null;
	}
}
