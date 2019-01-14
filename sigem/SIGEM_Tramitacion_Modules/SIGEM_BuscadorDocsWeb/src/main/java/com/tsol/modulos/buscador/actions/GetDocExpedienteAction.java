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

/**
 * [dipucr-Felipe #745]
 * @since 13.06.2018
 * @author Felipe
 *
 * http://sei.dipucr.es:8080/SIGEM_BuscadorDocsWeb/getDocExpediente.do?entidad=005&numexp=DPCR&tipodoc=ANTICIPO
 */
public class GetDocExpedienteAction extends Action
{
	private static final String USUARIO_SESION = "sigem";
	
	private static final Logger logger = Logger.getLogger(GetDocExpedienteAction.class);

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
	        
	        String tipodoc = (String) request.getParameter("tipodoc");
	        if (null == tipodoc || tipodoc.equals("")) {
				logger.error("El parametro tipo es nulo o vacío");
	            return null;
	        }
	        
	        SessionAPI sessionAPI = BuscadorDocsUtils.createSession(request, response, entidad, usuario);
			ClientContext ctx = sessionAPI.getClientContext();
			IEntitiesAPI entitiesAPI = sessionAPI.getAPI().getEntitiesAPI();
			
	        String idDoc = getIdDocumentoExpediente(ctx, numexp, tipodoc);

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


	private String getIdDocumentoExpediente(ClientContext ctx, String numexp, String tipodoc) 
			throws ISPACException {

		SearchBean result = null;
		SearchDAO dao = new SearchDAO();
		
		result = dao.searchDocExpediente(ctx, numexp, tipodoc);
		
		String idDocumento = null;
		if (null != result){
			idDocumento = result.getId();
		}
		
		return idDocumento;
	}

}