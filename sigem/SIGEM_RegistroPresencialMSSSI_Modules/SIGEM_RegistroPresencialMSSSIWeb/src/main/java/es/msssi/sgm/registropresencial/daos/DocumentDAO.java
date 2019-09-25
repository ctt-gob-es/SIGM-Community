/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.daos;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.isicres.AxPKById;

import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;

/**
 * Clase que contiene los métodos para recuperar los documentos de los
 * registros.
 * 
 * @author jortizs
 * 
 */
public class DocumentDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(DocumentDAO.class.getName());
    private String typePK = null;

    /**
     * Obtiene el nombre de tabla de documentos a partir del libro donde se
     * busca.
     * 
     * @return tableName El nombre de la tabla donde buscar.
     */
    protected String getDocTableName() {
	String tableName = "A" +
	    typePK + "DOCH";
	return tableName;
    }

    /**
     * Obtiene el nombre de tabla de páginas a partir del libro donde se busca.
     * 
     * @return tableName El nombre de la tabla donde buscar.
     */
    protected String getPageTableName() {
	String tableName = "A" +
	    typePK + "PAGEH";
	return tableName;
    }

    /**
     * Obtiene la información básica de un documento anexo a un registro, para
     * mostrarla en el listado.
     * 
     * @param axPKById
     *            La información principal sobre la que buscar.
     * @return resultDoc La información del documento.
     */
    public Axdoch getDocBasicInfo(AxPKById axPKById) {
	LOG.trace("Entrando en DocumentDAO.getDocBasicInfo");
	typePK = axPKById.getType();
	Axdoch axdoch = new Axdoch();
	HashMap<String, Object> docSqlParameters = new HashMap<String, Object>();
	docSqlParameters.put("tableName", getDocTableName());
	docSqlParameters.put("id", axPKById.getId());
	docSqlParameters.put("fdrid", axPKById.getFdrId());
	axdoch =
		(Axdoch) getSqlMapClientTemplate().queryForObject("Axdoch.selectAxdoch",
			docSqlParameters);
	/*for (Axdoch axdoch : axdochList) {
	    resultDoc.setId(axdoch.getId());
	    resultDoc.setFdrId(axdoch.getFdrid());
	    resultDoc.setName(axdoch.getName());
	    resultDoc.setCrtnDate(axdoch.getCrtndate());
	}*/
	LOG.trace("Saliendo de DocumentDAO.getDocBasicInfo");
	return axdoch;
    }

    /**
     * Obtiene la información básica de una página de un documento anexo a un
     * registro, para mostrarla en el listado.
     * 
     * @param axPKById
     *            La información principal sobre la que buscar.
     * @return pageDoc La información de la página.
     */
    public Axpageh getPageBasicInfo(AxPKById axPKById) {
	LOG.trace("Entrando en DocumentDAO.getPageBasicInfo");
	typePK = axPKById.getType();

	Axpageh axpageh = new Axpageh();
	HashMap<String, Object> pageSqlParameters = new HashMap<String, Object>();
	pageSqlParameters.put("tableName", getPageTableName());
	pageSqlParameters.put("id", axPKById.getId());
	pageSqlParameters.put("fdrid", axPKById.getFdrId());
	pageSqlParameters.put("idBook", axPKById.getType());
	axpageh =
		(Axpageh) getSqlMapClientTemplate().queryForObject("Axpageh.selectAxpageh",
			pageSqlParameters);
	/*for (Axpageh axpageh : axpagehList) {
	    resultPage.setId(axpageh.getId());
	    resultPage.setFdrId(axpageh.getFdrid());
	    resultPage.setName(axpageh.getName());
	    resultPage.setDocId(axpageh.getDocId());
	    resultPage.setCrtnDate(axpageh.getCrtndate());
	    resultPage.setLoc(axpageh.getLoc());
	}*/
	LOG.trace("Saliendo de DocumentDAO.getPageBasicInfo");
	return axpageh;
    }

    /**
     * Obtiene la información básica de las páginas de un documento anexo a un
     * registro, para mostrarla en el listado.
     * 
     * @param axPKByIdDoc
     *            La información principal sobre la que buscar.
     * @param flag
     * 		indicador de si es un fichero interno no imprimible en los acuses.
     * @return pageDoc La información de la página.
     */
    @SuppressWarnings("unchecked")
    public List<Axpageh> getPagesBasicInfo(AxPKById axPKByIdDoc, Integer flag) {
    	
	LOG.trace("Entrando en DocumentDAO.getPagesBasicInfo");
	
	typePK = axPKByIdDoc.getType();
	List<Axpageh> axpagehList = null;
	HashMap<String, Object> pageSqlParameters = new HashMap<String, Object>();
	pageSqlParameters.put("tableName", getPageTableName());
	pageSqlParameters.put("docid", axPKByIdDoc.getId());
	pageSqlParameters.put("fdrid", axPKByIdDoc.getFdrId());
	
	// Adaptación a Alfresco. Si el conector no es de alfresco, se mantiene la anterior funcionalidad
	String listadoAxpageh = "Axpageh.listAxpageh";
	String connectorManager = null;
	try {
		connectorManager = ISPACConfiguration.getInstance().get(ISPACConfiguration.CONNECTOR_MANAGER);
	} catch (ISPACException e) {
		LOG.error(e);
	}
	if (connectorManager != null && (connectorManager.equalsIgnoreCase("alfresco") || 
			connectorManager.equalsIgnoreCase("alfrescoCMIS"))) {
		listadoAxpageh = "Axpageh.listAxpagehAlfresco";
	} else {
		pageSqlParameters.put("idBook", axPKByIdDoc.getType());
	}
	
	 if (flag != null){
	    pageSqlParameters.put("flag", flag);
	}
	
	axpagehList =
		(List<Axpageh>) getSqlMapClientTemplate().queryForList(listadoAxpageh,
			pageSqlParameters);
	
	LOG.trace("Saliendo de DocumentDAO.getPagesBasicInfo");
	return axpagehList;
    }
    
    /**
     * Obtiene la información básica de las páginas de un documento anexo a un
     * registro, para mostrarla en el listado.
     * 
     * @param axPKByIdDoc
     *            La información principal sobre la que buscar.
     * @return pageDoc La información de la página.
     */
    public List<Axpageh> getPagesBasicInfo(AxPKById axPKByIdDoc) {
	LOG.trace("Entrando en DocumentDAO.getPagesBasicInfo");
	List<Axpageh> axpagehList = null;
	axpagehList = getPagesBasicInfo(axPKByIdDoc, null);
	LOG.trace("Saliendo de DocumentDAO.getPagesBasicInfo");
	return axpagehList;
    }
    
    /**
     * Borra un documento incluyendo sus páginas.
     * 
     * @param axPKByIdDoc
     *            La información principal sobre la que buscar.
     */
    public void deleteDocument(AxPKById axPKByIdDoc) {
	LOG.trace("Entrando en DocumentDAO.deleteDocument");
	typePK = axPKByIdDoc.getType();
	int delete = 0;
	HashMap<String, Object> pageSqlParameters = new HashMap<String, Object>();
	pageSqlParameters.put("tableNamePag", getPageTableName());
	pageSqlParameters.put("tableNameDoc", getDocTableName());
	pageSqlParameters.put("docid", axPKByIdDoc.getId());
	pageSqlParameters.put("fdrid", axPKByIdDoc.getFdrId());
	pageSqlParameters.put("idBook", axPKByIdDoc.getType());
	delete = getSqlMapClientTemplate().update("SrcPageRepository.deleteSrcPageRepository",
		pageSqlParameters);
	delete = getSqlMapClientTemplate().delete("Axpageh.deleteAxpageh",
			pageSqlParameters);
	delete = getSqlMapClientTemplate().delete("Axdoch.deleteAxdoch",
				pageSqlParameters);
	LOG.trace("Saliendo de DocumentDAO.deleteDocument");
    }
    
    /**
     * Borra las páginas del documento sin borrar el documento.
     * 
     * @param axPKByIdDoc
     *            La información principal sobre la que buscar.
     */
    public void deletePage(AxPKById axPKByIdDoc, int fileId) {
	LOG.trace("Entrando en DocumentDAO.deletePage");
	typePK = axPKByIdDoc.getType();
	int delete = 0;
	HashMap<String, Object> pageSqlParameters = new HashMap<String, Object>();
	pageSqlParameters.put("tableNamePag", getPageTableName());
	pageSqlParameters.put("tableNameDoc", getDocTableName());
	pageSqlParameters.put("docid", axPKByIdDoc.getId());
	pageSqlParameters.put("fdrid", axPKByIdDoc.getFdrId());
	pageSqlParameters.put("idBook", axPKByIdDoc.getType());
	pageSqlParameters.put("fileid", fileId);
	delete = getSqlMapClientTemplate().update("SrcPageRepository.deleteSrcPageRepository",
		pageSqlParameters);
	delete = getSqlMapClientTemplate().delete("Axpageh.deleteUnAxpageh",
			pageSqlParameters);
	LOG.trace("Saliendo de DocumentDAO.deleteDocument");
    }
    
    /**
     * Obtiene el valor del parámetro typePK.
     * 
     * @return typePK valor del campo a obtener.
     */
    public String getTypePK() {
	return typePK;
    }

    /**
     * Guarda el valor del parámetro typePK.
     * 
     * @param typePK
     *            valor del campo a guardar.
     */
    public void setTypePK(
	String typePK) {
	this.typePK = typePK;
    }

    /**
     * Obtiene la información básica de una página de un documento anexo a un
     * registro, para mostrarla en el listado.
     * 
     * @param axPKById
     *            La información principal sobre la que buscar.
     * @return pageDoc La información de la página.
     */
    public Integer countPageReport(AxPKById axPKById) {
	LOG.trace("Entrando en DocumentDAO.countPageReport");
	typePK = axPKById.getType();
	Integer count = 0;
	HashMap<String, Object> pageSqlParameters = new HashMap<String, Object>();
	pageSqlParameters.put("tableName", getPageTableName());
	pageSqlParameters.put("id", axPKById.getId());
	pageSqlParameters.put("fdrid", axPKById.getFdrId());
	pageSqlParameters.put("idBook", axPKById.getType());
	count =
		(Integer) getSqlMapClientTemplate().queryForObject("Axpageh.countAxpagehReport",
			pageSqlParameters);

	LOG.trace("Saliendo de DocumentDAO.countPageReport");
	return count;
    }

    /**
     * Marca el documento como acuse.
     * 
     * @param axPKByIdDoc
     *            La información principal sobre la que buscar.
     * @param	idpage
     * 			id pagina.
     * @param numAcuse
     * 		número de acuse.
     */
    public void updateFlag(AxPKById axPKByIdDoc, int idpage, Integer numAcuse) {
	LOG.trace("Entrando en DocumentDAO.updateFlag");
	typePK = axPKByIdDoc.getType();
	HashMap<String, Object> pageSqlParameters = new HashMap<String, Object>();
	pageSqlParameters.put("tableNamePag", getPageTableName());
	pageSqlParameters.put("tableName", getDocTableName());
	pageSqlParameters.put("fdrid", axPKByIdDoc.getFdrId());
	pageSqlParameters.put("idBook", axPKByIdDoc.getType());
	pageSqlParameters.put("idPage", idpage);
	pageSqlParameters.put("flag", numAcuse);
	getSqlMapClientTemplate().update("SrcPageRepository.updateFlagSrcPageRepository",
		pageSqlParameters);
	LOG.trace("Saliendo de DocumentDAO.updateFlag");
    }


    
}