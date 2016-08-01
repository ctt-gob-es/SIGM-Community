package es.dipucr.contratacion.action;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.SelectSubstituteAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.contratacion.objeto.ValorXML;
import es.dipucr.contratacion.utils.LectorXML;


public class SelectListadoCodicePliegoAction  extends SelectSubstituteAction
{
	  //private Logger logger = Logger.getLogger(SelectListadoCodicePliegoAction.class);

	  @SuppressWarnings("unchecked")
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session)
	    throws Exception
	  {
	    IInvesflowAPI invesFlowAPI = session.getAPI();
	    IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

	    String columna = "";
	    String tipoContrato = request.getParameter("tipoContrato");
	   // logger.warn("tipoContrato " + tipoContrato);
	    if(StringUtils.isEmpty(tipoContrato)){
	    	columna = request.getParameter("atributo");
		   // logger.warn("columna " + columna);
	    }
	    //Esta opción esta activa cuando se utiliza subtipo de contrato que es un subconjunto de tipo de contrato
	    else{
			String [] vTipoContrato = tipoContrato.split(" - ");
			String idTipoContrato = "0";
			if(vTipoContrato.length >0){
				idTipoContrato = vTipoContrato[0];
		    	if (idTipoContrato.equalsIgnoreCase("1")){  
		    		columna= "COD_CONTRATO_SUMIN";
				}else if (idTipoContrato.equalsIgnoreCase("2")){
					columna = "COD_CONTRATO_SECTPUB";
				}else	if (idTipoContrato.equalsIgnoreCase("3")){  
					columna = "COD_CONTRATO_OBRAS";
				}else	if (idTipoContrato.equalsIgnoreCase("50")){  
					columna = "COD_SUB_PATRIM";
				}
			}
	    }
	    
	    String strQuery = "WHERE CODICE_PLIEGOS = '" + columna + "'";
	    //logger.warn("strQuery "+strQuery);
	    IItemCollection icServContrat = entitiesAPI.queryEntities("CONTRATACION_SERVICIOS", strQuery);
	    Iterator<IItem> itServContrat = icServContrat.iterator();

	    String url = "";

	    while (itServContrat.hasNext()) {
	      IItem itemServContrat = itServContrat.next();
	      url = itemServContrat.getString("URL");
	      //logger.warn("url. " + url);
	    }
	    
	    ArrayList<ItemBean> vect = new ArrayList<ItemBean>();

	    if ((url != null) && (!url.equals(""))) {
	    	//logger.warn("-------------------------------------inicio PARSEO--------------------------------------------");
	    	ArrayList<ValorXML> nodos = new ArrayList<ValorXML>();	
	    	LectorXML lector = new LectorXML(nodos);
			lector.leer(url);
			//logger.warn("----------------------------------realizado PARSEO--------------------------------------------");
			
			//Ordacion descendente
			HashMap<String, String> hmCodice = new HashMap<String, String>();
			String [] stringArray = obtenerCodeKey(nodos, hmCodice);
			Arrays.sort(stringArray);
			//logger.warn("Vector ordenado");
			
			for (int i = 0; i < stringArray.length; i++) {

				String key = stringArray[i];
				String value = hmCodice.get(key);
				//logger.warn("key "+key+" value "+value);
				ItemBean itemB = new ItemBean();
				itemB.setProperty("VALOR", key);
				itemB.setProperty("SUSTITUTO", key+" - "+value);
				
				//Se añaden estas líneas para que si es un tipo de contrato
				//el subtipo lo ponga a vacío y si no es no haga nada
				itemB.setProperty("CONTRATO_SUMIN", "");
				
				if(columna.equals("COD_TIPO_CONTRATO")){
					itemB.setProperty("CONTRATO_SUMIN", "VACIO");
				}
				vect.add(itemB);
			}
	    }
	    //int maxResultados = vect.size();
	    
	    List<ItemBean> list = vect.subList(0, vect.size());
		//request.setAttribute("SubstituteList", list);
	    request.setAttribute("SubstituteList", list);

		processFormatter(request, "/digester/subtitutoformatter.xml");
		
		return mapping.findForward("success");
	  }
	  
	  private static String[] obtenerCodeKey(ArrayList<ValorXML> nodos, HashMap<String, String> hmCodice) {
			String [] sCodeKey = new String [nodos.size()];
			for(int i = 0; i < nodos.size(); i++){
				ValorXML valorXml = nodos.get(i);
				hmCodice.put(valorXml.getCodeKey(), valorXml.getNombre());
				sCodeKey[i] = valorXml.getCodeKey();
			}
			return sCodeKey;
			
		}
	  	  
}
