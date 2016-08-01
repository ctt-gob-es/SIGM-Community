package com.tsol.modulos.buscador.actions;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tsol.modulos.buscador.beans.SearchBean;
import com.tsol.modulos.buscador.dao.SearchDAO;
import com.tsol.modulos.buscador.utils.BuscadorDocsUtils;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [eCenpri-Felipe #1216]
 * Desde la aplicación PERSONAS se consultará las resoluciones de
 * Decretos (DT), Plenos (PL) y Juntas de Gobierno (CG) para los empleados.
 * Este acción devuelve el documento correspondiente
 * @since 12.12.2014
 * @author Felipe
 *
 */
public class GetResolucionPersonalAction extends Action
{
	private static final String USUARIO_SESION = "sigem";
	
	public static String TIPORESOL_DECRETO = "DC";
	public static String TIPORESOL_JUNTAGOB = "CG";
	public static String TIPORESOL_PLENO = "PL";
	
	private static final Logger logger = Logger.getLogger(GetResolucionPersonalAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)  throws Exception  
	{
		String entidad = null;
		String usuario = USUARIO_SESION;
    	
		try
		{
			entidad = (String) request.getParameter("entidad");
			if (null == entidad || entidad.equals("")){
				logger.error("El parametro entidad es nulo o vacío");
				return null;
			}

			String numexp = (String) request.getParameter("numexp");
	        if (null == numexp || numexp.equals("")) {
				logger.error("El parametro numexp es nulo o vacío");
	            return null;
	        }
	        
	        String tipoResolucion = (String) request.getParameter("tipo");
	        if (null == tipoResolucion || tipoResolucion.equals("")) {
				logger.error("El parametro tipo es nulo o vacío");
	            return null;
	        }
	        
	        String particular = (String) request.getParameter("particular");
	        if (null == particular || particular.equals("")) {
				logger.error("El parametro particular es nulo o vacío");
	            return null;
	        } 
	        
	        SessionAPI sessionAPI = BuscadorDocsUtils.createSession(request, response, entidad, usuario);
			ClientContext ctx = sessionAPI.getClientContext();
			IEntitiesAPI entitiesAPI = sessionAPI.getAPI().getEntitiesAPI();
			
	        String idDoc = getIdDocumentoResolucion(ctx, numexp, tipoResolucion, particular);

			IItem itemDoc = DocumentosUtil.getDocumento(entitiesAPI, Integer.valueOf(idDoc));
			if (itemDoc == null) {
				return null;
			}
	        
			return BuscadorDocsUtils.imprimirDocumento(response, ctx, itemDoc);
		}
		catch (Exception e)
		{
			logger.error("Error al mostrar el documento: " + e.getMessage());
			throw e;
		}
    }


	private String getIdDocumentoResolucion(ClientContext ctx, String numexp,
			String tipoResolucion, String particular) throws ISPACException {

		SearchBean result = null;
		SearchDAO dao = new SearchDAO();
		
		if (TIPORESOL_DECRETO.equals(tipoResolucion)){
			result = dao.searchDecreto(ctx, numexp);
		}
		else if (TIPORESOL_JUNTAGOB.equals(tipoResolucion))
		{
			String organo = Constants.SECRETARIAPROC.TIPO_JUNTA;
			result = dao.searchJuntaPleno(ctx, numexp, organo, particular);
		}
		else if (TIPORESOL_PLENO.equals(tipoResolucion))
		{
			String organo = Constants.SECRETARIAPROC.TIPO_PLENO;
			result = dao.searchJuntaPleno(ctx, numexp, organo, particular);
		}
		
		String idDocumento = null;
		if (null != result){
			idDocumento = result.getId();
		}
		
		return idDocumento;
	}

}