package es.dipucr.contratacion.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispacmgr.action.SelectSubstituteAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.contratacion.objeto.ValorXML;
import es.dipucr.contratacion.utils.LectorXML;

public class SelectListadoCodiceClasificacionEmpresaAction extends SelectSubstituteAction
{
	  private Logger logger = Logger.getLogger(SelectListadoCodiceClasificacionEmpresaAction.class);

	  @SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session)
	    throws Exception
	  {
	    IInvesflowAPI invesFlowAPI = session.getAPI();
	    IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

	    String columna = request.getParameter("atributo");
	    logger.warn("columna " + columna);

	    String strQuery = "WHERE CODICE_PLIEGOS = '" + columna + "'";
	    IItemCollection icServContrat = entitiesAPI.queryEntities("CONTRATACION_SERVICIOS", strQuery);
	    Iterator<IItem> itServContrat = icServContrat.iterator();

	    String url = "";
	    
	    String parameters = request.getParameter("parameters");
	    
	    Map sParameters = (Map)request.getSession().getAttribute(parameters);
	    Iterator<String> it = sParameters.keySet().iterator();
        while (it.hasNext()) {
        	String key = (String) it.next();
        	logger.warn("---"+sParameters.get(key));
        }
	    
	    String cod_cpv = request.getParameter("cod_cpv");
	    logger.warn("cod_cpv " + cod_cpv);

	    while (itServContrat.hasNext()) {
	      IItem itemServContrat = (IItem)itServContrat.next();
	      url = itemServContrat.getString("URL");
	      logger.warn("url. " + url);
	    }
	    
	    ArrayList<ItemBean> vect = new ArrayList<ItemBean>();

	    if ((url != null) && (!url.equals(""))) {
	    	logger.warn("-------------------------------------inicio PARSEO--------------------------------------------");
	    	ArrayList<ValorXML> nodos = new ArrayList<ValorXML>();	
	    	LectorXML lector = new LectorXML(nodos);
			lector.leer(url);
			logger.warn("----------------------------------realizado PARSEO--------------------------------------------");
			
			//Caracteres a coger
			String caracteres = request.getParameter("caracteres");
		    logger.warn("caracteres " + caracteres);
		    int numGrupoCarac = 0;
		    try{
		    	numGrupoCarac = Integer.parseInt(caracteres);
		    }catch(NumberFormatException e){
		    	logger.warn("No es entero");
		    	logger.warn("numGrupoCarac " + numGrupoCarac);
		    }
		    
		    //Inicio del codigo, caracteres que se tiene que coger del atributo cod_cpv
		    //Si pone 0 todavia no se ha cogido nada, si pone 1 se ha cogido 1 caracteres que ponga en caracteres
		    
		    String cadenaInicio = "";
		    if(request.getParameter("cadenaInicio")!=null) cadenaInicio=request.getParameter("cadenaInicio");
		    logger.warn("cadenaInicio " + cadenaInicio);
			
			//Ordenacion descendente
			HashMap<String, String> hmCodice = new HashMap<String, String>();
			obtenerCodeKey(nodos, hmCodice);
			//Arrays.sort(stringArray);
			//logger.warn("Vector ordenado");
			Set<String> keyCodice = hmCodice.keySet();
			Iterator<String> iKeyCodice = keyCodice.iterator();
			ArrayList<String> valoresSinRepetirKey = new ArrayList<String>();
			//Listado a mostrar
			Vector<String> valoresMostrarKey = new Vector<String>();
			
			//Cojo los primero caracteres de cod_cpv
			//calculo el número de caracteres de la cadena que han pasado
			int caracterACoger = 0;
		    try{
		    	caracterACoger = Integer.parseInt(cadenaInicio);
		    }catch(NumberFormatException e){
		    	logger.warn("No es entero");
		    	logger.warn("numGrupoCarac " + caracterACoger);
		    }
		    int caracter = 0;
		    String cadenacode_cpv = "";
		    if(cod_cpv != null){
		    	 if(cod_cpv.length() >= caracterACoger*numGrupoCarac){
				    	cadenacode_cpv = cod_cpv.substring(0, caracterACoger*numGrupoCarac);
					    caracter = cadenacode_cpv.length();
				    }
		    }
			
			while (iKeyCodice.hasNext()) {
				String key = iKeyCodice.next();
				if(caracterACoger==0){
					//F*-1 -> se sustituye el número por el *
					String KeyAste = key.substring(0, 1) + "*" + key.substring(2, key.length());
					if(key.equals(KeyAste)){
						if(!valoresSinRepetirKey.contains(KeyAste)){
							valoresSinRepetirKey.add(key);
						}
					}
				}
				if(caracterACoger==1){
					//F*-1 -> se sustituye el número por el *
					String KeyAste = key.substring(0, 1) + "*" + key.substring(2, key.length());
					if(!key.equals(KeyAste)){
						//compruebo el primer carácter
						if(key.substring(0, 1).equals(cod_cpv.substring(0, 1))){
							if(!valoresSinRepetirKey.contains(KeyAste)){
								valoresSinRepetirKey.add(key);
							}
						}						
					}
				}
				if(caracterACoger==2){
					//F*-1 -> se sustituye el número por el *
					String KeyAste = key.substring(0, 1) + "*" + key.substring(2, key.length());
					if(!key.equals(KeyAste)){
						//compruebo el primer carácter
						if(key.substring(0, 2).equals(cod_cpv.substring(0, 2))){
							if(!valoresSinRepetirKey.contains(KeyAste)){
								valoresSinRepetirKey.add(key);
							}
						}						
					}
				}
				
			}
			Object [] aValoresMostrar = null;
			aValoresMostrar = valoresSinRepetirKey.toArray();

			Arrays.sort(aValoresMostrar);
			ArrayList<String> caracteresSinRepetir = new ArrayList<String>();
			
			for(int i = 0; i < aValoresMostrar.length; i++){
				String key = (String)aValoresMostrar[i];
				String value = hmCodice.get(key);
				String primerCaracterKey = "";
				if(caracterACoger==0){
					primerCaracterKey = key.substring(0, 1);
				}
				if(caracterACoger==1){
					primerCaracterKey = key.substring(0, 2);
				}
				ItemBean itemB = new ItemBean();
				if(caracterACoger==2){
					itemB.setProperty("VALOR", key);
					itemB.setProperty("SUSTITUTO", value);
					itemB.setProperty("CONTRATO_SUMIN", "");
					if(columna.equals("COD_TIPO_CONTRATO")){
						itemB.setProperty("CONTRATO_SUMIN", "VACIO");
					}
					vect.add(itemB);
				}
				else{
					if(!caracteresSinRepetir.contains(primerCaracterKey)){
						caracteresSinRepetir.add(primerCaracterKey);
						
						itemB.setProperty("VALOR", key);
						if(value == null){
							value="";
						}
						//suprimir las cantidades.
						String valorSinCantidades = "";
						String [] vValorSinCantidades = value.split("\\(");
						if(vValorSinCantidades.length>0){
							valorSinCantidades = vValorSinCantidades[0];
						}
						
						itemB.setProperty("SUSTITUTO", valorSinCantidades);
						
						
						//Se añaden estas líneas para que si es un tipo de contrato
						//el subtipo lo ponga a vacío y si no es no haga nada
						itemB.setProperty("CONTRATO_SUMIN", "");
						
						if(columna.equals("COD_TIPO_CONTRATO")){
							itemB.setProperty("CONTRATO_SUMIN", "VACIO");
						}
						vect.add(itemB);
					}
				}				
			}

	    }
	    
	    
	    
	    List<ItemBean> list = vect.subList(0, vect.size());
		//request.setAttribute("SubstituteList", list);
	    request.setAttribute("SubstituteList", list);

		processFormatter(request, "/digester/substituteformatter.xml");
		
		return mapping.findForward("success");
	  }
	  
	  private static void obtenerCodeKey(ArrayList<ValorXML> nodos, HashMap<String, String> hmCodice) {
			for(int i = 0; i < nodos.size(); i++){
				ValorXML valorXml = (ValorXML) nodos.get(i);
				hmCodice.put(valorXml.getCodeKey(), valorXml.getNombre());
			}
			
		}
}
