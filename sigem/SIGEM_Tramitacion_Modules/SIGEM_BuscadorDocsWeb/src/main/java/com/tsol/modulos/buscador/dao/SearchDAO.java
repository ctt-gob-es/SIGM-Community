package com.tsol.modulos.buscador.dao;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tsol.modulos.buscador.beans.SearchBean;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class SearchDAO {
	
	private static final Logger LOGGER = Logger.getLogger(SearchDAO.class);
	private static String PARTICULAR_SEPARATOR = "_"; //[eCenpri-Felipe #1216]
	
	/**
	 * [eCenpri-Felipe #828]
	 * Búsqueda por cod_cotejo
	 * @since 25.01.13
	 * @param entidad
	 * @param codCotejo
	 * @return
	 * @throws ISPACException 
	 */
	public static SearchBean searchDocument(ClientContext ctx, String codCotejo)
			throws ISPACException{
		
		String strQuery = new StringBuffer(" WHERE COD_COTEJO='")
			.append(DBUtil.replaceQuotes(codCotejo.trim()))
			.append("' ORDER BY ID DESC").toString();
		return searchDocumentQuery(ctx, strQuery);
	}
	
	/**
	 * [eCenpri-Felipe #828]
	 * Búsqueda por id_documento
	 * @since 25.01.13
	 * @param entidad
	 * @param idDoc
	 * @return
	 * @throws ISPACException 
	 */
	public static SearchBean searchDocumentById(ClientContext ctx, String idDoc)
			throws ISPACException{
		
		String strQuery = new StringBuffer(" WHERE ID=")
			.append(idDoc).append(" ORDER BY ID DESC").toString();
		return searchDocumentQuery(ctx, strQuery);
	}
	
	/**
	 * [dipucr-Felipe #1216]
	 * Recupera el documento de decreto firmado a partir del numexp
	 * @param entidad
	 * @param numexp
	 * @return
	 * @throws ISPACException 
	 */
	public SearchBean searchDecreto(ClientContext ctx, String numexp)
			throws ISPACException{
		
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append(" WHERE numexp = '");
		sbQuery.append(numexp);
		sbQuery.append("' AND nombre = '");
		sbQuery.append(Constants.DECRETOS._DOC_DECRETO);
		sbQuery.append("' AND estadofirma = '");
		sbQuery.append(SignStatesConstants.FIRMADO);
		sbQuery.append("' ORDER BY id DESC");
		
		return searchDocumentQuery(ctx, sbQuery.toString());
	}
	
	/**
	 * [dipucr-Felipe #1216]
	 * Recupera el documento de decreto firmado a partir del numexp
	 * @param entidad
	 * @param numexp
	 * @return
	 * @throws ISPACException 
	 */
	public SearchBean searchJuntaPleno(ClientContext ctx, String numConv, String organo, String particular) 
			throws ISPACException{
		
		if(null != particular && particular.contains(PARTICULAR_SEPARATOR)){
			particular = particular.split(PARTICULAR_SEPARATOR)[1];
			particular += ".- Urgencia Certificado";
		}
		
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append(" WHERE numexp IN "); //[dipucr-Felipe 3#254] Incluyo un IN
		sbQuery.append("(SELECT numexp FROM secr_sesion WHERE numconv = '");
		sbQuery.append(numConv);
		sbQuery.append("' AND organo = '");
		sbQuery.append(organo);
		sbQuery.append("') AND nombre = '");
		sbQuery.append(Constants.SECRETARIATRAMITES.CERTIFICADACUERD);
		sbQuery.append("' AND estadofirma = '");
		sbQuery.append(SignStatesConstants.FIRMADO);
		sbQuery.append("' AND descripcion LIKE '");
		sbQuery.append(particular);
		sbQuery.append("%' ORDER BY id DESC");

		return searchDocumentQuery(ctx, sbQuery.toString());
	}
	
	/**
	 * Antigua función searchDocument modificada [dipucr-Felipe #828]
	 * @param ctx
	 * @param strQuery [dipucr-Felipe #828]
	 * @return
	 * @throws ISPACException
	 */
	private static SearchBean searchDocumentQuery(ClientContext ctx, String strQuery)
			throws ISPACException {
		
		SearchBean searchBean = null;
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Buscando el documento con el código de cotejo: [" + strQuery + "]");
		}
		
		if (StringUtils.isNotBlank(strQuery)) {
			
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			
	    	IItemCollection documentos = entitiesAPI.queryEntities(ISPACEntities.DT_ID_DOCUMENTOS, strQuery);
			if ((documentos != null) && documentos.next()) {
				IItem documento = documentos.value();
				searchBean = getBeanFromDocument(documento);
			}
			//MQE #1023 INICIO Históricos. Concatenamos los históricos.
			if (null == searchBean){
				IItemCollection documentosH = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS_H", strQuery);
				if ((documentosH != null) && documentosH.next()) {
					IItem documentoH = documentosH.value();
					searchBean = getBeanFromDocument(documentoH);
				}
			}
			//MQE #1023 FIN Históricos. Concatenamos los históricos.
		}
		
		return searchBean;
	}
	
	/**
	 * [dipucr-Felipe #625 #828 #1023 #1216]
	 * @param documento
	 * @return
	 * @throws ISPACException 
	 */
	public static SearchBean getBeanFromDocument(IItem documento) throws ISPACException{
		
		SearchBean searchBean = null;
		if (documento != null) {

			searchBean = new SearchBean();
			searchBean.setId(documento.getString("ID"));
			searchBean.setNombre(documento.getString("NOMBRE"));
			searchBean.setNumExp(documento.getString("NUMEXP"));
			searchBean.setCodCotejo(documento.getString("COD_COTEJO"));
			searchBean.setFechaDoc(documento.getString("FDOC"));
			searchBean.setTpReg(documento.getString("TP_REG"));
			
			//Tomamos el infopag_rde. Si este es nulo tomamos el infopag
			String strInfopag = documento.getString("INFOPAG_RDE");
			if (StringUtils.isBlank(strInfopag)){
				strInfopag = documento.getString("INFOPAG");
			}
			searchBean.setInfopag(strInfopag);
			
			//INICIO [eCenpri-Felipe #625]
			searchBean.setNreg(documento.getString("NREG"));
			searchBean.setFreg(documento.getString("FREG"));
			searchBean.setOrigen(documento.getString("ORIGEN"));
			searchBean.setDestino(documento.getString("DESTINO"));
			//FIN [eCenpri-Felipe #625]
			searchBean.setEstado(documento.getString("ESTADO"));//[eCenpri-Felipe #828]
		}
		return searchBean;
	}
	
}
