package ieci.tdw.ispac.ispacmgr.tag;

import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.impl.SessionAPIFactory;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.bean.scheme.RegEntityBean;
import ieci.tdw.ispac.ispacmgr.bean.scheme.SchemeEntityBean;
import ieci.tdw.ispac.ispacweb.tag.context.StaticContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;

/**
 * Tag Menu Bar.
 */
public class MenuTaskTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	// Constantes --------------------------------------------------------------
    /** Nombre del atributo en el contexto de página que contiene los
     * identificadores de barras de menú existentes */
    public static final String ID_MAP = "ieci.tdw.ispac.ispacweb.menu.ID_MAP";

    private static final Logger logger = Logger.getLogger(MenuTaskTag.class);

    /** Nombre del atributo que contiene el esquema */
    private String name;
    
   

 // Fichero de recursos de la aplicación
	private static final String BUNDLE_NAME = "ieci.tdw.ispac.ispacmgr.resources.ApplicationResources";
	
   

    // Métodos -----------------------------------------------------------------
   
  
    /**
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
       
        return super.doStartTag();
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {

        try {
            drawTask();
            pageContext.getOut().flush();

        } catch (IOException e) {
            TagUtils.getInstance().saveException(pageContext, e);
            throw new JspException(e);
        }
        catch (ISPACException e) {
            TagUtils.getInstance().saveException(pageContext, e);
            throw new JspException(e);
        }
      
        return EVAL_PAGE;
    }
    
    
    // [Manu Ticket #56] Ordenamos los trámites por fechas
	// [Manu Ticket #707] Problemas de rendimiento Mostramos únicamente los 5 últimos trámites y aquellos que estén abiertos.
    private void drawTask()throws IOException,ISPACException{
  		 
 	  HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

 	  StringBuffer actuales = new StringBuffer();
 	  StringBuffer anteriores = new StringBuffer();
 	  

 	 ClientContext cct =((SessionAPI)getSession()).getClientContext();
 	 List schemeList = (List)pageContext.findAttribute(getName());
 	 actuales.append("<ul class=\"menu_grupo\">");
 	 anteriores.append("<ul class=\"menu_grupo\">");
 	 ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME,cct.getLocale() );
 	   
 	//[eCenpri-Felipe #467] Siempre desplegado
	 actuales.append("<li id=\"itemCurrentTask\" class=\"item_desplegado\"> <a href=\"#\" onclick=\"javascript:showItem('CurrentTask');return false;\"> ");
	 actuales.append(bundle.getString("menu.expTramites")+"</a>");

	 anteriores.append("<li id=\"itemHistoryTask\" class=\"item_plegado\"> <a href=\"#\" onclick=\"javascript:showItem('HistoryTask');return false;\"> ");
	 anteriores.append(bundle.getString("menu.expTramitesClose")+"</a>");

		//[Manu Ticket #56] Ordenamos los trámites por fechas
		Object taskIdParam=request.getParameter("taskId");
		//[Manu Ticket #56] Fin modificaciones

 	  	 actuales.append("<ul>");
 	  	 anteriores.append("<ul>");
	     if(schemeList!=null && schemeList.size()>0){
	    	for(int i=0; i<schemeList.size(); i++){
	    		SchemeEntityBean item=(SchemeEntityBean) schemeList.get(i);
	    		String entityId=item.getItem().getString("ID");
	    		if(StringUtils.equals(entityId, ""+ISPACEntities.DT_ID_TRAMITES)){
	    			//Recorremos cada uno de sus registros
	    			List registros=item.getRegs();
	    			ArrayList registrosAct = new ArrayList();
	    			if(registros!=null){
	    				
						// [Manu Ticket #56] Ordenamos los trámites por fechas
						try {
							RegEntityBean[] registrosOrdenados = new RegEntityBean[registros.size()];

							for (int cont = 0; cont < registros.size(); cont++) {
								registrosOrdenados[cont] = (RegEntityBean) registros.get(cont);

							}
							quicksort(registrosOrdenados, 0, registrosOrdenados.length - 1);

							registros = new ArrayList();

							for (int cont = 0; cont < registrosOrdenados.length; cont++) {

								int taskIdReg = ((Integer) registrosOrdenados[registrosOrdenados.length - 1 - cont].getParams().get("taskId")).intValue();
								if (taskIdParam != null && taskIdReg == Integer.parseInt(taskIdParam.toString())) {
									registros.add(registrosOrdenados[registrosOrdenados.length - 1 - cont]);
								} else if (cont < 5) {
									registros.add(registrosOrdenados[registrosOrdenados.length - 1 - cont]);
								} else {
									String estado = "" + registrosOrdenados[registrosOrdenados.length - cont - 1].getProperty("ESTADO");
									if (estado.equals("1")) {
										registros.add(registrosOrdenados[registrosOrdenados.length - 1 - cont]);
									}
								}
							}

							// Ordenamos nuevamente
							for (int mqe = 0; mqe < registros.size(); mqe++) {
								registrosAct.add((RegEntityBean) registros.get(registros.size() - 1 - mqe));
							}
						} catch (Exception e) {

						}
						// [Manu Ticket #56] Fin modificaciones
	    				
	    				
	    				for(int j=0; j<registrosAct.size(); j++){
	    					RegEntityBean reg=(RegEntityBean) registrosAct.get(j);
	    					//int key=((Integer) reg.getProperty("SCHEME_ID")).intValue();
	    					
	    					int id_fase_exp=((Integer) reg.getProperty("ID_FASE_EXP")).intValue();
	    					String stageId=request.getSession().getAttribute("stageId").toString();
	    					
	    					//Compruebo que estamo tratando un tramite de la fase actual
	    					if(Integer.parseInt(stageId)==id_fase_exp ){
	    						actuales = new StringBuffer(generateHtmlTask(actuales.toString(),reg,true));
	    					}
	    					//Fases anteriores
	    					//[Manu Ticket #56] no mostramos actuales, pueden ser muchos y sobrecargan
//	    					else if(Integer.parseInt(stageId)!=id_fase_exp ){
	    						
//	    						anteriores=generateHtmlTask(anteriores,reg,false);	
//	    					}
	    				}
	    			}
	    		}
	    	} 
	     }
	     
	     // [Manu Ticket #707] Problemas de rendimiento Añadimos la opción ver todos
	     JspWriter out = pageContext.getOut();

		 String context =request.getContextPath();
		   
	     String url=context+"/ShowAllTaskAction.do";
		 String urlImg = StaticContext.rewriteHref(pageContext, context, "img/search-mg.gif");
		 
		 //Ver todos
		 actuales.append("<li  class=\"menuBold\">"+bundle.getString("menu.expRel.verTodos"));
		 actuales.append("<a href=\"javascript: showFrame(\'workframe\', \'"+url+"\', '', true, false);\">");
		 actuales.append("<img title=\""+bundle.getString("menu.expRel.verTodos")+"\"  src=\""+urlImg+"\"/>");
		 actuales.append("</a></ul></li>");
		 
		
		 String urlAnteriores=context+"/ShowAllTaskAnterioresAction.do";		 
		 
		 //Ver todos
		 anteriores.append("<li  class=\"menuBold\">"+bundle.getString("menu.expRel.verTodos"));
		 anteriores.append("<a href=\"javascript: showFrame(\'workframe\', \'"+urlAnteriores+"\', '', true, false);\">");
		 anteriores.append("<img title=\""+bundle.getString("menu.expRel.verTodos")+"\"  src=\""+urlImg+"\"/>");
		 anteriores.append("</a></ul></li>");
		 
		 // [Manu Ticket #707] Fin modificaciones Problemas de rendimiento 
		 
	     anteriores.append("</ul>");
	     actuales.append("</ul>");
	     anteriores.append("</li>");
	     actuales.append("</li>");
	     anteriores.append("</ul>");
	     actuales.append("</ul>");
//	     JspWriter out = pageContext.getOut();
	     out.println(actuales);
	     out.println(anteriores);
	     
    }

   
   private String generateHtmlTask(String salida, RegEntityBean reg,boolean actual) throws ISPACException{
	   
	   HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	   String context =request.getContextPath();
	   salida+="<li class=\""+reg.getClase()+"\" >";
		
		if(StringUtils.isEmpty(reg.getUrl())){
			 salida+=reg.getProperty("SCHEME_EXPR").toString();
		}
		else{
			String url=context+"/"+reg.getUrl()+"?";
			
		
			int taskIdReg=((Integer) reg.getParams().get("taskId")).intValue();
			Object taskIdParam=request.getParameter("taskId");
			
				url=makeUrl(url, reg.getParams());
				
				if(taskIdParam!=null && taskIdReg==Integer.parseInt(taskIdParam.toString())){
					 salida+="<a   class=\"selectedTask\" href=\""+url+"\"><p class=\"withoutMarginTopBottom\"  >"+
							 reg.getString("SCHEME_EXPR")+"</p></a><p class=\"infoTramite\" >"+
							 reg.getString("FECHA_INICIO")+"</p>";
					 if(actual){
						 salida+="<script>javascript: hide_expand('7', \"img/arrow_down_sch.gif\", \"img/arrow_right_sch.gif\");</script>";	
					}
					else{
						salida+="<script>javascript: hide_expand('7Close', \"img/arrow_down_sch.gif\", \"img/arrow_right_sch.gif\");</script>";
						}
				}
				else{
					 salida+="<a  href=\""+url+"\"><p class=\"withoutMarginTopBottom\">"+
							 reg.getString("SCHEME_EXPR")+"</p></a><p class=\"infoTramite\" >"+
							 reg.getString("FECHA_INICIO")+"</p>";
				}
			}


	
	//Actividades de un subproceso
	if(reg.getLtRegEntityBean()!=null && reg.getLtRegEntityBean().size()>0){
		
		 salida+="<ul>";
		for(int k=0; k<reg.getLtRegEntityBean().size(); k++){
			RegEntityBean registro = (RegEntityBean) reg.getLtRegEntityBean().get(k);
			 salida+="<li class=\"actividad\" >";
			String url=context+"/"+registro.getUrl()+"&";
			url=makeUrl(url, reg.getParams());
			 salida+="<a  href=\""+url+"\"><p class=\"withoutMarginTopBottom\"  >"
			+registro.getString("NAME_STAGE")+"</p></a><p class=\"infoTramite\" >"+ reg.getString("FECHA_INICIO")+"</p>";
			 salida+="</li>";
		}
		 salida+="</ul>";
	
	}
	 salida+="</li>";
	 
	 return salida;
   }
/**
 * Construe la urr con los parámetros
 * @param url
 * @param params
 * @return
 */
   
   private String makeUrl(String url, Map params){
		Iterator itr=params.keySet().iterator();
		boolean primero=true;
		while(itr.hasNext()){
			String param=(String) itr.next();
			if(primero){
				url+=param+"="+params.get(param);
				primero=false;
			}
			else{
				url+="&"+param+"="+params.get(param);
			}
		}
		
		return url;
	   
   }
    
   
    /**
     * @return Devuelve el valor de <code>name</code>.
     */
    public String getName() {
        return this.name;
    }
    /**
     * Establece el valor de <code>name</code>.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la sessionApi
     */
    protected SessionAPI getSession() {

	  	SessionAPI sessionAPI = null;

	  	try	{
	  		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	  		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
	  		sessionAPI = SessionAPIFactory.getSessionAPI(request, response);
	    } catch( ISPACException e) {
	    	logger.warn("Error al obtener el SessionAPI", e);
	    }

	    return sessionAPI;
  }

	// [Manu Ticket #56] Ordenamos los trámites por fechas usando quicksort
	public void quicksort(RegEntityBean[] a, int izq, int der) {
		int i = izq;
		int j = der;
		// int pivote = a[ (izq + der) / 2];
		RegEntityBean pivote = a[(izq + der) / 2];

		do {
			try {
				int valorA = ((Integer) ((RegEntityBean) a[i]).getProperty("SCHEME_ID")).intValue();
				int valorPivote = ((Integer) pivote.getProperty("SCHEME_ID")).intValue();

				// while (a[i] < pivote) {
				while (valorA < valorPivote) {
					i++;
					valorA = ((Integer) ((RegEntityBean) a[i]).getProperty("SCHEME_ID")).intValue();
				}

				// while (a[j] > pivote) {
				int valorJ = ((Integer) ((RegEntityBean) a[j]).getProperty("SCHEME_ID")).intValue();
				while (valorJ > valorPivote) {
					j--;
					valorJ = ((Integer) ((RegEntityBean) a[j]).getProperty("SCHEME_ID")).intValue();
				}
			} catch (Exception e) {
				logger.error("ERROR - quickshort error: " + e.getMessage(), e);
			}
			if (i <= j) {
				RegEntityBean aux = a[i];
				a[i] = a[j];
				a[j] = aux;
				i++;
				j--;
			}
		} while (i <= j);
		if (izq < j) {
			quicksort(a, izq, j);
		}
		if (i < der) {
			quicksort(a, i, der);
		}
	}
	// [Manu Ticket #56] Fin modificaciones

}
