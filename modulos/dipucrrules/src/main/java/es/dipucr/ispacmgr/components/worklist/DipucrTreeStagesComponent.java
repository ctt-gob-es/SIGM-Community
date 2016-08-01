package es.dipucr.ispacmgr.components.worklist;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISessionAPI;
import ieci.tdw.ispac.api.IWorklistAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcedure;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.bean.TreeItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispacmgr.components.worklist.AbstractProcedureTreeComponent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.lang.StringUtils;
import org.directwebremoting.dwrp.Batch;

import es.dipucr.sigem.dao.DpcrFasesProcDAO;

/**
 * Muestra la información de las fases bajo la responsabilidad del usuario .
 *
 */
public class DipucrTreeStagesComponent extends AbstractProcedureTreeComponent { 
	
	//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el área de trabajo por año de inicio de expediente.
	private String anio = "";

	/**
	 * Constructor.
	 */
	public DipucrTreeStagesComponent() {
		super();
		setId("treeStages");
	}
	
	/**
	 * Renderiza el componente.
	 * @param context Contexto de servlets.
	 * @param request Petición del cliente.
	 * @param sessionAPI API de sesión.
	 * @param params Parámetros de configuración.
	 * @return Código HTML para mostrar en pantalla.
	 * @throws ISPACException si ocurre algún error.
	 */
    public void render(ServletContext context, HttpServletRequest request, ISessionAPI sessionAPI) 
    		throws ISPACException { 
		
    	// Contexto del cliente
		//[eCenpri-Manu Ticket #131] - INICIO - ALSIGM3 Filtrar el área de trabajo por año de inicio de expediente.
    	ClientContext ctx = sessionAPI.getClientContext();    	
    	
    	Batch atributos = (Batch) request.getAttribute("org.directwebremoting.dwrp.batch");
    	String direccion = atributos.getPage();
    	String[] anioArray = direccion.split("anio=");
    	if(anioArray.length > 1){
    		anio = anioArray[1].split("&")[0];
    	}    	
    	
    	// Título
    	setTitle(getMessage(ctx.getLocale(), "stages.titulo"));

		// Responsabilidades del usuario conectado
		IInvesflowAPI invesflowAPI = ctx.getAPI();
		IWorklistAPI workListAPI = invesflowAPI.getWorkListAPI();
		String resp = workListAPI.getRespString();
    	
		//
		IItemCollection stagesItemCollection = null;
		
    	if(!StringUtils.isEmpty(anio) && !anio.equals("----")){
			stagesItemCollection = DpcrFasesProcDAO.getAllStages(ctx, resp, anio).disconnect();
    	}
    	else{
    		stagesItemCollection = DpcrFasesProcDAO.getAllStages(ctx, resp).disconnect();
    	}
		MultiHashMap stagesMap = toMap(stagesItemCollection, "ID_PCD");
		
		// Árbol de procedimientos
		String html =  "<h1>" + getMessage(ctx.getLocale(), "filtro.anioInicioExp") + "  <select name=\"anio\" id=\"anio\" class=\"input\" value=\"----\" onchange=\"setAnio(this.value);\">"
    			+ "			<options>"
    			+ "				<option value='----'> - TODOS - </option>"
    			+ "			</options>"
    			+ "		</select>"
    			+ "		</h1>"
    			+ "		<script>"
    			+ "			var anioSelect = '" + anio + "';"
    			+ "			var anio = 2009;"
    			+ "			var f = new Date();"
    			+ "			var anioAc = f.getFullYear();"
    			+ "			for (var i=1; (anio+i)<=anioAc; i++){"
    			+ "				document.getElementById('anio').options[i] = new Option (anio+i,anio+i);"
    			+ "				if((anio+i)==anioSelect){"
    			+ "					document.getElementById('anio').options[i].selected = true;"
    			+ "				}"
    			+ "			}"
    			+ "		</script>"    			
    			+ drawPcdTree(ctx, context, request, getProcedureTree(ctx), stagesMap);
			//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el área de trabajo por año de inicio de expediente.
		setContent(html);
    } 
    
    protected TreeItemBean getProcedureTree(ClientContext ctx)
			throws ISPACException {

		Map<String, String> tableentitymap = new HashMap<String, String>();
		tableentitymap.put("SPAC_CT_PROCEDIMIENTO", "SPAC_CT_PROCEDIMIENTOS");
		tableentitymap.put("SPAC_P_PROCEDIMIENTO", "SPAC_P_PROCEDIMIENTOS");

		// Seleccionamos la última versión de cada procedimiento.
		String whereClause = " WHERE SPAC_P_PROCEDIMIENTO.ID = SPAC_CT_PROCEDIMIENTO.ID"
				+ " AND SPAC_P_PROCEDIMIENTO.TIPO=" + IProcedure.PROCEDURE_TYPE
				+ " ORDER BY SPAC_CT_PROCEDIMIENTO.ID_PADRE, SPAC_CT_PROCEDIMIENTO.NOMBRE";

		IItemCollection itemcol = ctx.getAPI().getCatalogAPI()
				.queryCTEntities(tableentitymap, whereClause);

		// Obtenemos el árbol de ItemBeans
		return CollectionBean.getBeanTree(itemcol, "SPAC_P_PROCEDIMIENTO:ID",
				"SPAC_CT_PROCEDIMIENTO:ID_PADRE");
	}
    
	public MultiHashMap toMap(IItemCollection stagesItemCollection, String hashPropertyKey) throws ISPACException
	{
		MultiHashMap map = new MultiHashMap();
		Iterator<?> stagesIterator = stagesItemCollection.iterator(); 
		
		while(stagesIterator.hasNext()){
			IItem obj=(IItem)stagesIterator.next();
			map.put(obj.get(hashPropertyKey),obj);
		}
		return map;
	}
    
    protected String drawPcdTree(ClientContext ctx, ServletContext context,
			HttpServletRequest request, TreeItemBean pcdTree, MultiHashMap stagesMap)
			throws ISPACException {

    	String html = "";
    	
    	if (pcdTree != null) {
    		
    		String childrenHTML = "";
    		
			//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el área de trabajo por año de inicio de expediente.
			List<?> children = pcdTree.getChildren();
			if (!CollectionUtils.isEmpty(children)) {
				for (int i = 0; i < children.size(); i++) {
					childrenHTML += drawPcdTree(ctx, context, request, (TreeItemBean) children.get(i), stagesMap);	
				}
			}

    		IItem item = pcdTree.getItem();
    		if (item != null) {
				String stagesHTML = "";
    			Iterator<?> stages = stagesMap.iterator(new Long(item.getString("SPAC_P_PROCEDIMIENTO:ID")));
    			
    			if(stages.hasNext()){
    				stagesHTML += "<ul>";
    				
					while(stages.hasNext()){
		    			
		    			ItemBean stage = new ItemBean((IItem) stages.next());
						if (stage.getString("NOMBRE") != null) {
							
							stagesHTML += "<li style=\"list-style-image: url(" + rewriteHref(context, request, "img/fase.gif") + ");\">";
								
							stagesHTML += "<a href=\"" + request.getContextPath() + "/showProcessList.do?stagePcdId=" + stage.getString("ID_FASE") + "&anio=" + anio + "\" class=\"menu\">"
									+ stage.getString("NOMBRE") + "&nbsp;(" + stage.getString("COUNT") + ")"
									+ "</a>";
								stagesHTML += "</li>";
						}
					}
					
					stagesHTML += "</ul>";
	    		}
    			
    			if (StringUtils.isNotBlank(stagesHTML) || StringUtils.isNotBlank(childrenHTML)) {
			    	html += "<li class=\"menu11Bold\" style=\"list-style-image: url("
							+ rewriteHref(context, request, "img/procedimiento.gif")
							+ ");\">";
			    	
			    	html += getMessage(ctx.getLocale(), "stages.label.procedimiento", new Object[] {
			    		item.getString("SPAC_P_PROCEDIMIENTO:NOMBRE"),
			    		item.getString("SPAC_P_PROCEDIMIENTO:NVERSION")
			    	});
					html += "</li>";
					html += stagesHTML;
				}
    		}
    		
    		if (StringUtils.isNotBlank(childrenHTML)) {
    			html += "<ul>" + childrenHTML + "</ul>";
    		}
    	}

		return html;
    }
}
