/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfFiltersDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.RequestListDTO;
import es.seap.minhap.portafirmas.domain.RequestTagListDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;

@Repository
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class RequestDAO {

	Logger log = Logger.getLogger(RequestDAO.class);

	private EntityManager entityManager = null;

    /*
     * Sets the entity manager.
     */
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

	public PfRequestsDTO queryRequest(AbstractBaseDTO baseDTO) {
		return entityManager.find(PfRequestsDTO.class, baseDTO.getPrimaryKey());
	}

	public PfRequestTagsDTO queryRequestTag(Long requestTagPk) {
		return entityManager.find(PfRequestTagsDTO.class, requestTagPk);
	}
	
	/**
	 * 
	 * @param baseDTO
	 * @return
	 */
	public PfRequestsTextDTO queryRequestText(AbstractBaseDTO baseDTO) {
		return entityManager.find(PfRequestsTextDTO.class, baseDTO.getPrimaryKey());
	}

	/**
	 * M&eacute;todo que devuelve el n&uacute;mero de peticiones a mostrar seg&uacute;n un listado de etiquetas que deben tener asociadas y el usuario que solicita la operaci&oacute;n.
	 * @param userDTO Objeto que contiene la informaci&oacute;n del usuario que est&aacute; dado de alta en el sistema.
	 * @param tagList Lista de etiquetas que deben tener las peticiones a buscar.
	 * @return N&uacute;mero de peticiones a mostrar.
	 */
	public String queryRequestListCount(PfUsersDTO userDTO,
			List<String> tagList) {

		String sQueryFrom = "select distinct reqTags.primaryKey "
				+ "from PfRequestTagsDTO reqTags "
				+ "join reqTags.pfRequest req "
				+ "join reqTags.pfTag reqTag "
				+ "join reqTags.pfUser usr ";

		String sQueryWhere = "";

		PfUsersDTO job = null;
		job = userDTO.getValidJob();
		if (job  != null) {
			sQueryWhere = " where (usr = :user or usr = :usrJob) and";
		} else {
			sQueryWhere = " where usr = :user and";
		}
		
		
		sQueryWhere += " (reqTag.ctag in (:reqFilter)) ";
		
		sQueryWhere += " and ((select count(reqTags2.primaryKey) from PfRequestTagsDTO reqTags2 join reqTags2.pfRequest req2 join reqTags2.pfTag tag2" +				
				   " where req2.primaryKey = req.primaryKey and tag2.ctag in ('" + Constants.C_TAG_ANULLED_TYPE + "') and tag2.ctype = 'SISTEMA')  = 0) ";

		sQueryWhere += "and (req.fstart is null or req.fstart < current_date()) ";
		
		//sQueryWhere += "and (req.lanulada = 'N' or req.lanulada is null)";
		
		//Se contemplan las invitadas aceptadas y las no invitadas a la hora de hacer el recuento de 
		//peticiones sin resolver o pendientes
		sQueryWhere += "and ((req.linvited = 'S' and req.laccepted = 'S') or (req.linvited = 'N'))";

		Query query = entityManager.createQuery(sQueryFrom + sQueryWhere);
		
		// params
		query.setParameter("user", userDTO);
		
		query.setParameter("reqFilter", tagList);

		if(job != null){
			query.setParameter("usrJob", job);
		}

		List<Object> list = query.getResultList();
		List<Long> resultList = eliminarRepetidas(list);
		String rowsNumber = String.valueOf(resultList.size());
		
		//String rowsNumber = "" + ((Long) query.getSingleResult()).intValue();

		return rowsNumber;
	}
	
	

	/**
	 * M&eacute;todo que devuelve el listado de las peticiones a mostrar en la bandeja de peticiones
	 * @param pageSize N&uacute;mero de peticiones a mostrar por p&aacute;gina. 
	 * @param pageActual Posici&oacute;n de la p&aacute;gina actual respecto al total.
	 * @param orderAttribute Indica el atributo o columna de la tabla de peticiones que se usar&aacute; para ordenarlas.
	 * @param order Indica el sentido de la ordenación.
	 * @param userDTO Objeto que contiene la informaci&oacute;n del usuario que est&aacute; dado de alta en el sistema.
	 * @param searchFilter Valor introducido por el usuario en el filtro de b&uacute;squeda de peticiones.
	 * @param labelFilter Valor para filtrar las peticiones seg&uacute;n su etiqueta.
	 * @param applicationFilter Valor para filtrar las peticiones seg&uacute;n la aplicaci&oacute;n a la que pertenecen.
	 * @param tagList Lista de etiquetas que deben tener las peticiones a buscar.
	 * @param job Objeto que define un rol de usuario en el caso de haber hecho login con el mismo.
	 * @param bbdd Valor que indica el tipo de la base de datos del Portafirmas.
	 * @return Listado con las peticiones a mostrar.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<AbstractBaseDTO> queryRequestListPaginated(int pageSize,
			int pageActual, String orderAttribute, String order,
			PfUsersDTO userDTO, String searchFilter, String labelFilter,
			String applicationFilter, Date initDateFilter, Date endDateFilter,
			List tagList, PfUsersDTO job, String requestFilter, String filtroTipo, List <PfApplicationsDTO> appValidators) {

		log.info("queryRequestListPaginated");
		RequestTagListDTO list = null;
		
		// 03/12/2014 Eliminada al eliminar "query". Se quita porque queryFetch unifica la paginación y las condiciones
		String sQuerySelect = "select reqTags1.primaryKey " +
				  "from es.seap.minhap.portafirmas.domain.PfRequestTagsDTO reqTags1 " +
				  "join reqTags1.pfRequest req1 " +
				  "join req1.pfApplication application " +
				  "where reqTags1 in (select distinct(reqTags) ";
		String sQueryFrom = "from PfRequestTagsDTO reqTags "
			+ "join reqTags.pfRequest req "
			+ "join reqTags.pfTag reqTag "
			+ "join reqTags.pfUser usr ";

		// Si hay filtro por tipo de petición se añaden las líneas de firma a la consulta
		if (!Util.esVacioONulo(filtroTipo)) {
			sQueryFrom += " left join reqTags.pfSignLine signLine ";
		}

		String sQueryWhere = "";

		String sQuerySelectCount = "select count (distinct reqTags.primaryKey) ";

		String sQuerySelectFetch = "select reqTags ";

		String sQueryFromFetch = "from PfRequestTagsDTO reqTags "
				+ "join fetch reqTags.pfRequest req "
				+ "join fetch req.pfImportance reqImp "
				+ "join fetch reqTags.pfTag reqTag "
				+ "join fetch reqTags.pfUser usr "
				+ "left join fetch reqTags.pfSignLine signLine "
				+ "join fetch req.pfUsersRemitters rem "
				+ "join fetch rem.pfUser usrRem "
				+ "join fetch req.pfApplication app "
				+ "join fetch app.pfConfiguration conf ";

		String sQueryWhereFetch = "";

		if (job != null) {
			sQueryWhere = "where (usr = :user or usr = :usrJob) ";
		} else {
			sQueryWhere = "where usr = :user ";
		}

		sQueryWhere += " and (req.fstart is null or req.fstart < current_date()) ";

		sQueryWhereFetch += sQueryWhere;
		
		/*
		 * Si se están consultando las peticiones anuladas, sólo debe buscar aquellas peticiones 
		 * con alguna etiqueta de tipo "SISTEMA" y etiqueta "TIPO.ANULADA".
		 * En caso contrario, se debe buscar peticiones con alguna etiqueta de tipo "ESTADO" y etiqueta
		 * de entre el listado que se le pasa como parámetro, y ninguna de tipo "SISTEMA" y etiqueta "TIPO.ANULADA".
		 */
		if(!requestFilter.equals(Constants.MESSAGES_CANCELLED)){
			sQueryWhere += "and (reqTag.ctag in (:reqFilter) and reqTag.ctype = 'ESTADO')";
			
			// 03/12/2014 En la consulta fetch se restringe por algo que ya esta limitado en la consulta que recupera las pk de tagReq "query"
			sQueryWhereFetch += " and ((reqTag.ctag in (:reqFilter) and reqTag.ctype = 'ESTADO') or (reqTag.ctype != 'ESTADO')) ";
			sQueryWhereFetch += " and (reqTag.ctag in (:reqFilter) and reqTag.ctype = 'ESTADO') ";
			
			sQueryWhere += " and ((select count(reqTags2.primaryKey) from PfRequestTagsDTO reqTags2 join reqTags2.pfRequest req2 join reqTags2.pfTag tag2" +				
					   " where req2.primaryKey = req.primaryKey and tag2.ctag in ('" + Constants.C_TAG_ANULLED_TYPE + "') and tag2.ctype = 'SISTEMA') = 0) ";
		}else{
			sQueryWhere += " and ((select count(reqTags2.primaryKey) from PfRequestTagsDTO reqTags2 join reqTags2.pfRequest req2 join reqTags2.pfTag tag2" +
					   " where req2.primaryKey = req.primaryKey and tag2.ctag in (:reqFilter))  > 0) ";
			sQueryWhere += " and (reqTag.ctag in (:reqFilter)) ";
			
			//[DipuCR-Agustin #1285] Quito de la consulta la propiesta reqTag.ctype, que devuelva las que son SISTEMA y tambiEn ESTADO
			//		   " where req2.primaryKey = req.primaryKey and tag2.ctag in (:reqFilter) and (tag2.ctype = 'SISTEMA')  > 0) ";
			//sQueryWhere += " and (reqTag.ctag in (:reqFilter) and reqTag.ctype = 'SISTEMA') ";
			
			//sQueryWhereFetch += " AND (reqTag.ctag in (:reqFilter) and tag2.ctype = 'SISTEMA') ";
		}

		List<PfApplicationsDTO> applicationList = null;
		List<PfApplicationsDTO> applicationListFilter = null;
		if (null != applicationFilter && !"".equals(applicationFilter)) {
			String sQueryAppChildren = "SELECT * "
						+ "FROM pf_aplicaciones apl "
						+ "CONNECT BY PRIOR apl.X_APLICACION = apl.APL_X_APLICACION "
						+ "START WITH apl.C_APLICACION = '" + applicationFilter + "'";
			Query queryAppChildren = entityManager.createNativeQuery(sQueryAppChildren, PfApplicationsDTO.class);
			applicationListFilter = queryAppChildren.getResultList();
		}
		if (appValidators!=null && !appValidators.isEmpty()){
			applicationList = appValidators;
		} else {
			applicationList = applicationListFilter;
		}
		
		if (applicationList!=null && !applicationList.isEmpty()) {
			sQueryFrom += "join req.pfApplication app ";
			sQueryWhere += " and app in (:application) ";
		}


		// search
		if (!Util.esVacioONulo(searchFilter)) {
			searchFilter = searchFilter.toUpperCase();
			searchFilter = Util.getInstance().translateSearch(searchFilter);
			sQueryFrom += "join req.pfDocuments doc "
					+ "join doc.pfDocumentType tdoc "
					+ "join req.pfUsersRemitters rem "
					+ "join rem.pfUser usrRem ";
			sQueryWhere += "and (" + Constants.TRANSLATE_SQL_BEGIN
					+ " req.dsubject" + Constants.TRANSLATE_SQL_END + "or "
					+ Constants.TRANSLATE_SQL_BEGIN + "req.dreference"
					+ Constants.TRANSLATE_SQL_END + "or "
					+ Constants.TRANSLATE_SQL_BEGIN + "doc.dname"
					+ Constants.TRANSLATE_SQL_END + "or "
					+ Constants.TRANSLATE_SQL_BEGIN + "tdoc.cdocumentType"
					+ Constants.TRANSLATE_SQL_END + "or "
					+ Constants.TRANSLATE_SQL_BEGIN + "usrRem.dname"
					+ Constants.TRANSLATE_SQL_END + "or "
					+ Constants.TRANSLATE_SQL_BEGIN + "usrRem.dsurname1"
					+ Constants.TRANSLATE_SQL_END + "or "
					+ Constants.TRANSLATE_SQL_BEGIN + "usrRem.dsurname2"
					+ Constants.TRANSLATE_SQL_END + " ) ";
		}

		if (!Util.esVacioONulo(labelFilter)) {
			sQueryFrom += " join req.pfRequestsTags rTagFilter "
					+ " join rTagFilter.pfTag tagFilter "
					+ " join rTagFilter.pfUser filterUser ";

			sQueryWhere += " and tagFilter.primaryKey = :label ";

		}

		// Filtro de fechas
		if (initDateFilter != null) {
			sQueryWhere += " and req.fcreated >= :initDate ";
		}
		if (endDateFilter != null) {
			sQueryWhere += " and req.fcreated <= :endDate ";
		}
		
//		if (Constants.MESSAGES_CANCELLED.equals(requestFilter)){
//			sQueryWhere += " and req.lanulada = 'S' ";
//		}else{
//			sQueryWhere += "and (req.lanulada = 'N' or req.lanulada is null) ";
//		}
		
		// Filtro por tipo de línea de firma (firma o vistobueno)
		if (Constants.C_TYPE_SIGNLINE_SIGN.equals(filtroTipo)) {
			sQueryWhere += " and signLine.ctype = 'FIRMA' ";
		} else if (Constants.C_TYPE_SIGNLINE_PASS.equals(filtroTipo)) {
			sQueryWhere += " and signLine.ctype = 'VISTOBUENO' ";
		}

		// 03/12/2014 Vamos a quitar la query y por tanto no se necesita las pk de retTags
		// fetch
		sQueryWhereFetch += " and reqTags.primaryKey in (:reqTags)";

		// order
		String sQueryOrder = "";
		String sQueryOrderFetch = "";

		if(Util.esVacioONulo(orderAttribute) || Util.esVacioONulo(order)) {
			sQueryOrder = " order by req1.fmodified ";
			sQueryOrderFetch = " order by req.fmodified ";
			sQueryOrder += "desc";
			sQueryOrderFetch += "desc";
		} else {
			if (orderAttribute.equals("application")) {
				sQueryOrder = " order by application.capplication ";
				sQueryOrderFetch = " order by app.capplication ";
			} else {
				sQueryOrder = " order by req1." + orderAttribute + " ";
				sQueryOrderFetch = " order by req." + orderAttribute + " ";
			} 
			sQueryOrder += order;
			sQueryOrderFetch += order;
		}
		
		Query queryCount = entityManager.createQuery(sQuerySelectCount + sQueryFrom + sQueryWhere);

		// 03/12/2014 Eliminada Query ya que la paginación incluye las condiciones
		Query query = entityManager.createQuery(sQuerySelect + sQueryFrom + sQueryWhere +  ") " + sQueryOrder);

		Query queryFetch = entityManager.createQuery(sQuerySelectFetch + sQueryFromFetch + sQueryWhereFetch + sQueryOrderFetch);
		
		// params
		query.setParameter("user", userDTO);
		queryCount.setParameter("user", userDTO);
		queryFetch.setParameter("user", userDTO);

		query.setParameter("reqFilter", tagList);
		if(!requestFilter.equals(Constants.MESSAGES_CANCELLED)){
			queryFetch.setParameter("reqFilter", tagList);
		}
		queryCount.setParameter("reqFilter", tagList);
		

		if (job != null) {
			query.setParameter("usrJob", job);
			queryCount.setParameter("usrJob", job);
			queryFetch.setParameter("usrJob", job);
		}

		if (applicationList != null && !applicationList.isEmpty()) {
			query.setParameter("application", applicationList);
			queryCount.setParameter("application", applicationList);
		}

		if (null != searchFilter && !"".equals(searchFilter)) {
			query.setParameter("search", searchFilter);
			queryCount.setParameter("search", searchFilter);
		}

		if (null != labelFilter && !"".equals(labelFilter)) {
			query.setParameter("label", Long.parseLong(labelFilter));
			queryCount.setParameter("label", Long.parseLong(labelFilter));
		}

		if (initDateFilter != null) {
			query.setParameter("initDate", initDateFilter);
			queryCount.setParameter("initDate", initDateFilter);
		}

		if (endDateFilter != null) {
			query.setParameter("endDate", endDateFilter);
			queryCount.setParameter("endDate", endDateFilter);
		}
		
		int initPage = (pageActual - 1) * pageSize;
		log.info("Pagination: initPage=" + initPage + " and sizePage=" + pageSize + ".");

		Long listQuerySize = (Long) queryCount.getSingleResult();
		List<AbstractBaseDTO> listQueryFetch = null;
		if (listQuerySize > 0) {
			log.info("Total size: " + listQuerySize + ".");
			
			List<Object> listQuery = query.getResultList();
			
			// Devuelve las claves primarias de las etiquetas petición y las peticiones
			List<Long> listQueryNonRepeated = eliminarRepetidas(listQuery);
			listQuerySize = new Long(listQueryNonRepeated.size());
			
			// requests primary keys
//			query.setMaxResults(pageSize);
//			query.setFirstResult(initPage);
			queryFetch.setMaxResults(pageSize);
			queryFetch.setFirstResult(initPage);
//			List<Object> listQuery = query.getResultList();

			// 03/12/2014 Vamos a quitar la query y por tanto no se necesita las pk de retTags
			// fetch all
			//queryFetch.setParameter("reqTags", listQuery);
			queryFetch.setParameter("reqTags", listQueryNonRepeated);
			listQueryFetch = queryFetch.getResultList();
		} else {
			listQueryFetch = new ArrayList<AbstractBaseDTO>();
		}

		// Se devuelven la lista de resultados paginados y el tamaño todal
		list = new RequestTagListDTO(listQueryFetch, listQuerySize);

		log.info("QueryList obtains: " + listQuerySize + "/" + listQueryFetch.size() + " values.");

		return list;
	}	

	/**
	 * M&eacute;todo que devuelve el listado de peticiones enviadas por el usuario.
	 * @param pageSize N&uacute;mero de peticiones a mostrar por p&aacute;gina.
	 * @param pageActual Posici&oacute;n de la p&aacute;gina actual respecto al total.
	 * @param orderAttribute Indica el atributo o columna de la tabla de peticiones que se usar&aacute; para ordenarlas.
	 * @param order Indica que la ordenaci&oacute;n de las peticiones debe ser ascendente.
	 * @param userDTO Objeto que contiene la informaci&oacute;n del usuario que est&aacute; dado de alta en el sistema.
	 * @param searchFilter Valor introducido por el usuario en el filtro de b&uacute;squeda de peticiones.
	 * @param labelFilter Valor para filtrar las peticiones seg&uacute;n su etiqueta.
	 * @param applicationFilter Valor para filtrar las peticiones seg&uacute;n la aplicaci&oacute;n a la que pertenecen.
	 * @param tagList Lista de etiquetas para filtrar las peticiones enviadas, según se quieran recuperar
	 * las enviadas-pendientes o las enviadas-terminadas
	 * @param bbdd Valor que indica el tipo de la base de datos del Portafirmas.
	 * @param sentType indica si se desean recuperar las enviadas-pendientes o las enviadas-terminadas
	 * @return Lista con las peticiones enviadas por el usuario.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<AbstractBaseDTO> queryRequestSentListPaginated(int pageSize,
			int pageActual, String orderAttribute, String order,
			PfUsersDTO userDTO, PfGroupsDTO groupDTO, String searchFilter, String labelFilter,
			String applicationFilter, Date initDateFilter, Date endDateFilter,
			List tagList, String sentType, String filtroTipo, List <PfApplicationsDTO> appValidators) {
		log.info("queryRequestSentListPaginated");

		String sQuerySelect = "select reqTags.primaryKey, request.primaryKey ";
		String sQueryFrom = "from PfRequestTagsDTO reqTags, PfRequestsDTO request "
				+ "join reqTags.pfRequest req "
				+ "join reqTags.pfTag reqTag ";
		if(!sentType.equals(Constants.MESSAGES_INVITED)){
			sQueryFrom += "join reqTags.pfUser usr ";
		}
				
		sQueryFrom += "join req.pfUsersRemitters rem " 
					+ "join rem.pfUser usrRem ";

		// Si se accede como grupo
		if (groupDTO != null) {
			sQueryFrom += "join rem.pfGroup grpRem ";
		}

		// Si hay filtro por tipo de petición se añaden las líneas de firma a la consulta
		if (filtroTipo != null && !"".equals(filtroTipo)) {
			sQueryFrom += " left join reqTags.pfSignLine signLine ";
		}

		String sQueryWhere = "";

		String sQuerySelectCount = "select count (distinct reqTags.primaryKey) ";

		String sQuerySelectFetch = "select reqTags ";

		String sQueryFromFetch = "from PfRequestTagsDTO reqTags "
				+ "join fetch reqTags.pfRequest req "
				+ "left join fetch reqTags.pfSignLine signLine "
				+ "join fetch req.pfImportance reqImp "
				+ "join fetch reqTags.pfTag reqTag ";
		if(!sentType.equals(Constants.MESSAGES_INVITED)){
				sQueryFromFetch += "join fetch reqTags.pfUser usr ";
		}
		sQueryFromFetch	+= "join fetch req.pfUsersRemitters rem "
				+ "join fetch rem.pfUser usrRem ";

		if (groupDTO != null) {
			sQueryFromFetch += "join fetch rem.pfGroup grpRem ";
		}

		String sQueryWhereFetch = "";

		if (Constants.MESSAGES_GROUP_SENT.equalsIgnoreCase(sentType) ||
			Constants.MESSAGES_GROUP_SENT_FINISHED.equalsIgnoreCase(sentType) ||
			Constants.MESSAGES_GROUP_EXPIRED.equalsIgnoreCase(sentType)) {	
			sQueryWhere = "where grpRem = :grupo ";
		} else  {
			sQueryWhere = "where usrRem = :user ";
		}
		// Se obliga a coger la reqTag que tiene el ctype de 'ESTADO' para recuperar una con código hash
		if(!Constants.MESSAGES_SENT_CANCELLED.equalsIgnoreCase(sentType)){
			sQueryWhere += " and reqTag.ctype = 'ESTADO' ";
		}
		String exp = "";
		// Si se quieren obtener las enviadas-pendientes, se añadirá a la cláusula: y que tenga alguna de las etiquetas contenida en tagList
		if (Constants.MESSAGES_SENT.equalsIgnoreCase(sentType) ||
			Constants.MESSAGES_GROUP_SENT.equalsIgnoreCase(sentType) ||
			Constants.MESSAGES_GROUP_EXPIRED.equalsIgnoreCase(sentType)){
			exp = " > 0 ";
		// Si se quieren obtener las enviadas-terminadas, se añadirá a la cláusula: y que NO tenga ninguna de las etiquetas contenidas en tagList
		} else if (Constants.MESSAGES_SENT_FINISHED.equalsIgnoreCase(sentType) ||
				   Constants.MESSAGES_GROUP_SENT_FINISHED.equalsIgnoreCase(sentType)) {
			exp = " = 0 ";
		}

		if(!sentType.equals(Constants.MESSAGES_INVITED) && !sentType.equals(Constants.MESSAGES_SENT_CANCELLED)){
			sQueryWhere += " and ((select count(reqTags2.primaryKey) from PfRequestTagsDTO reqTags2 join reqTags2.pfRequest req2 join reqTags2.pfTag tag2" +				
					   " where req2.primaryKey = req.primaryKey and tag2.ctag in (:reqFilter))  " + exp + ") ";

		}else if (sentType.equals(Constants.MESSAGES_INVITED)){
			sQueryWhere += " and req.linvited = 'S' ";
		}
		
		/*
		 * Si se están consultando las peticiones anuladas, sólo debe buscar aquellas peticiones 
		 * con alguna etiqueta de tipo "SISTEMA" y etiqueta "TIPO.ANULADA".
		 * En caso contrario, se debe buscar peticiones con alguna etiqueta de tipo "ESTADO" y etiqueta
		 * de entre el listado que se le pasa como parámetro, y ninguna de tipo "SISTEMA" y etiqueta "TIPO.ANULADA".
		 */
		if(!sentType.equals(Constants.MESSAGES_SENT_CANCELLED)){
			sQueryWhere += " and ((select count(reqTags2.primaryKey) from PfRequestTagsDTO reqTags2 join reqTags2.pfRequest req2 join reqTags2.pfTag tag2" +				
				   " where req2.primaryKey = req.primaryKey and tag2.ctag in ('" + Constants.C_TAG_ANULLED_TYPE + "') and tag2.ctype = 'SISTEMA')  = 0) ";
		}else{
			sQueryWhere += " and (select count(reqTags2.primaryKey) from PfRequestTagsDTO reqTags2 join reqTags2.pfRequest req2 join reqTags2.pfTag tag2" +				
					   " where req2.primaryKey = req.primaryKey and tag2.ctag in (:reqFilter) and tag2.ctype = 'SISTEMA') > 0 ";
			//sQueryWhere += " and (reqTag.ctag in (:reqFilter) and reqTag.ctype = 'SISTEMA') ";
		}

		sQueryWhereFetch += sQueryWhere;
		
		List<PfApplicationsDTO> applicationList = null;
		List<PfApplicationsDTO> applicationListFilter = null;
		if (null != applicationFilter && !"".equals(applicationFilter)) {
			String sQueryAppChildren = "SELECT * "
					+ "FROM pf_aplicaciones apl "
					+ "CONNECT BY PRIOR apl.X_APLICACION = apl.APL_X_APLICACION "
					+ "START WITH apl.C_APLICACION = '" + applicationFilter + "'";
			Query queryAppChildren = entityManager.createNativeQuery(sQueryAppChildren, PfApplicationsDTO.class);
			applicationListFilter = queryAppChildren.getResultList();
		}
		if (appValidators!=null && !appValidators.isEmpty()){
			applicationList = appValidators;
		} else {
			applicationList = applicationListFilter;
		}

		if (applicationList!=null && !applicationList.isEmpty()) {
			sQueryFrom += "join req.pfApplication app ";
			sQueryWhere += " and app in (:application) ";
		}

		// search
		if (null != searchFilter && !"".equals(searchFilter)) {
			searchFilter = searchFilter.toUpperCase();
			searchFilter = Util.getInstance().translateSearch(searchFilter);
			sQueryFrom += "join req.pfDocuments doc join doc.pfDocumentType tdoc  ";
			sQueryWhere += "and (" + Constants.TRANSLATE_SQL_BEGIN
					+ " req.dsubject" + Constants.TRANSLATE_SQL_END + "or "
					+ Constants.TRANSLATE_SQL_BEGIN + "req.dreference"
					+ Constants.TRANSLATE_SQL_END + "or "
					+ Constants.TRANSLATE_SQL_BEGIN + "doc.dname"
					+ Constants.TRANSLATE_SQL_END + "or "
					+ Constants.TRANSLATE_SQL_BEGIN + "tdoc.cdocumentType"
					+ Constants.TRANSLATE_SQL_END;
					
			if(!sentType.equals(Constants.MESSAGES_INVITED)){
				sQueryWhere += "or "
							+ Constants.TRANSLATE_SQL_BEGIN + "usr.dname"
							+ Constants.TRANSLATE_SQL_END + "or "
							+ Constants.TRANSLATE_SQL_BEGIN + "usr.dsurname1"
							+ Constants.TRANSLATE_SQL_END + "or "
							+ Constants.TRANSLATE_SQL_BEGIN + "usr.dsurname2"
							+ Constants.TRANSLATE_SQL_END + " ) ";
			}else{
				sQueryWhere += "or " + Constants.TRANSLATE_SQL_BEGIN + "req.invitedUser.cMail"
							+ Constants.TRANSLATE_SQL_END  + " ) ";
			}
		}

		if (null != labelFilter && !"".equals(labelFilter)) {
			sQueryFrom += " join req.pfRequestsTags rTagFilter "
					+ " join rTagFilter.pfTag tagFilter ";
					
			if(!sentType.equals(Constants.MESSAGES_INVITED)){
				sQueryFrom += " join rTagFilter.pfUser filterUser ";
			}
			sQueryWhere += " and tagFilter.primaryKey = :label ";
		}

		// Filtro de fechas
		if (initDateFilter != null) {
			sQueryWhere += " and req.fcreated >= :initDate ";
		}
		if (endDateFilter != null) {
			sQueryWhere += " and req.fcreated <= :endDate ";
		}

		// Filtro por tipo de línea de firma (firma o vistobueno)
		if (Constants.C_TYPE_SIGNLINE_SIGN.equals(filtroTipo)) {
			sQueryWhere += " and signLine.ctype = 'FIRMA' ";
		} else if (Constants.C_TYPE_SIGNLINE_PASS.equals(filtroTipo)) {
			sQueryWhere += " and signLine.ctype = 'VISTOBUENO' ";
		}
		
		//Filtro anuladas
//		if (Constants.MESSAGES_CANCELLED.equals(sentType)){
//			sQueryWhere += " and req.lanulada = 'S' ";
//		}else{
//			sQueryWhere += "and (req.lanulada = 'N' or req.lanulada is null) ";
//		}


		// Se cruzan las etiquetas petición con las peticiones
		sQueryWhere += " and req = request ";

		// order
		String sQueryOrder = "";
		String sQueryOrderFetch = "";
		
		if(Util.esVacioONulo(orderAttribute) || Util.esVacioONulo(order)) {
			sQueryOrder = " order by request.fmodified ";
			sQueryOrderFetch = " order by req.fmodified ";
			sQueryOrder += "desc";
			sQueryOrderFetch += "desc";
		} else {
			if (orderAttribute.equals("application")) {
				sQueryOrder = " order by application.capplication ";
				sQueryOrderFetch = " order by app.capplication ";
			} else {
				sQueryOrder = " order by request." + orderAttribute + " ";
				sQueryOrderFetch = " order by req." + orderAttribute + " ";
			} 
			sQueryOrder += order;
			sQueryOrderFetch += order;
		}

		Query queryCount = entityManager.createQuery(sQuerySelectCount
				+ sQueryFrom + sQueryWhere);

		Query query = entityManager.createQuery(sQuerySelect + sQueryFrom
				+ sQueryWhere + sQueryOrder);

		// params
		if (groupDTO != null) {
			query.setParameter("grupo", groupDTO);
			queryCount.setParameter("grupo", groupDTO);
		} else {
			query.setParameter("user", userDTO);
			queryCount.setParameter("user", userDTO);
		}
		
		if(!sentType.equals(Constants.MESSAGES_INVITED)){
			query.setParameter("reqFilter", tagList);
			queryCount.setParameter("reqFilter", tagList);
		}

		if (applicationList != null && !applicationList.isEmpty()) {
			query.setParameter("application", applicationList);
			queryCount.setParameter("application", applicationList);
		}

		if (null != searchFilter && !"".equals(searchFilter)) {
			query.setParameter("search", searchFilter.toUpperCase());
			queryCount.setParameter("search", searchFilter.toUpperCase());
		}

		if (null != labelFilter && !"".equals(labelFilter)) {
			query.setParameter("label", Long.parseLong(labelFilter));
			queryCount.setParameter("label", Long.parseLong(labelFilter));
		}

		if (initDateFilter != null) {
			query.setParameter("initDate", initDateFilter);
			queryCount.setParameter("initDate", initDateFilter);
		}

		if (endDateFilter != null) {
			query.setParameter("endDate", endDateFilter);
			queryCount.setParameter("endDate", endDateFilter);
		}

		int initPage = (pageActual - 1) * pageSize;
		log.info("Pagination: initPage=" + initPage + " and sizePage="
				+ pageSize + ".");

		Long listQuerySize = (Long) queryCount.getSingleResult();
		List<AbstractBaseDTO> listQueryFetch = null;
		if (listQuerySize > 0) {
			log.info("Total size: " + listQuerySize + ".");

			// requests primary keys
			List<Object> listQuery = query.getResultList();

			// Devuelve las claves primarias de las etiquetas petición y las peticiones
			List<Long> listQueryNonRepeated = eliminarRepetidas(listQuery);
			listQuerySize = new Long(listQueryNonRepeated.size());

			// Lista auxiliar
			List<List<Long>> auxList = null;

			// fetch
			if (listQuerySize < 1000) {
				sQueryWhereFetch += " and reqTags.primaryKey in (:reqTags)";
			} else {
				auxList = new ArrayList<List<Long>>();

				sQueryWhereFetch += " and (";
				Long inListSize = listQuerySize;
				int i = 0;
				while (inListSize > 999) {
					if (i > 0) {
						sQueryWhereFetch += "or ";
					}

					sQueryWhereFetch +=
						"reqTags.primaryKey in (:reqTags" + i + ") ";
					auxList.add(i, listQueryNonRepeated.subList(i*999, (i+1)*999));

					inListSize = inListSize - 999;
					i++;
				}
				sQueryWhereFetch +=
					"or reqTags.primaryKey in (:reqTags" + i + "))";
				auxList.add(i, listQueryNonRepeated.subList(i*999, listQuerySize.intValue()));
			}

			// fetch all
			Query queryFetch = entityManager.createQuery(sQuerySelectFetch
					+ sQueryFromFetch + sQueryWhereFetch + sQueryOrderFetch);

			// params
			if (groupDTO != null) {
				queryFetch.setParameter("grupo", groupDTO);
			} else{
				queryFetch.setParameter("user", userDTO);
			}
			
			if(!sentType.equals(Constants.MESSAGES_INVITED)){
				queryFetch.setParameter("reqFilter", tagList);
			}

			queryFetch.setMaxResults(pageSize);
			queryFetch.setFirstResult(initPage);

			if (auxList != null && !auxList.isEmpty()) {
				for (int i=0; i < auxList.size(); i++) {
					queryFetch.setParameter("reqTags" + i, auxList.get(i));
				}
			} else {
				queryFetch.setParameter("reqTags", listQueryNonRepeated);
			}

			listQueryFetch = queryFetch.getResultList();
		} else {
			listQueryFetch = new ArrayList<AbstractBaseDTO>();
		}

		log.info("QueryList obtains: " + listQuerySize + "/"
				+ listQueryFetch.size() + " values.");

		return new RequestTagListDTO(listQueryFetch, listQuerySize);
	}

	private List<Long> eliminarRepetidas(List<Object> keys) {
		List<Long> resultList = new ArrayList<Long>();
		List<Long> addedRequests = new ArrayList<Long>();
		int cuenta = 0;
		
		if(keys != null && keys.size()>0){
			if(keys.get(0) instanceof Object[]){
				for (Object current : keys) {
					Object[] columns = (Object[]) current;
					Long reqTagPk = (Long) columns[0];
					Long reqPk 	= (Long) columns[1];

					if (!addedRequests.contains(reqPk)) {
						resultList.add(reqTagPk);
						addedRequests.add(reqPk);
						cuenta++;
					}
					if (cuenta == 1000) {
						break;
					}
				}
			}else if(keys.get(0) instanceof Object){
				for(Object current : keys){
					Long reqTagPk = (Long) current;
					PfRequestTagsDTO req = queryRequestTag(reqTagPk);
					if (!addedRequests.contains(req.getPfRequest().getPrimaryKey())) {
						resultList.add(reqTagPk);
						addedRequests.add(req.getPfRequest().getPrimaryKey());
						cuenta++;
					}
					if (cuenta == 1000) {
						break;
					}
				}
			}
		}
		

		return resultList;
	}


	/**
	 * Recupera la lista de peticiones donde las etiquetas tienen un codigo de etiqueta 'NUEVO','LEIDO','EN ESPERA' 
	 * y tipo de etiqueta 'ESTADO' y le a&ntilde;ade a la query los par&aacute;metros que se pasan en caso de que no sean nulos
	 * @param filter el filtro
	 * @param req la petici&oacute;n
	 * @param bbdd la base de datos 
	 * @return la lista de peticiones
	 */
	@SuppressWarnings("unchecked")
	public List<AbstractBaseDTO> queryFilterRequests(PfFiltersDTO filter, PfRequestsDTO req) {
		//las peticiones donde las etiquetas tienen un codigo de etiqueta 'NUEVO','LEIDO','EN ESPERA'
		//y tipo de etiqueta 'ESTADO'
		String queryString = "select distinct(req) "
				+ "from PfRequestsDTO req " + "join req.pfApplication app "
				+ "join fetch req.pfSignsLines sli "
				+ "join fetch sli.pfSigners sig "
				+ "join fetch sig.pfUser usr "
				+ "join fetch req.pfRequestsTags reqTag "
				+ "join fetch reqTag.pfTag tag "
				+ "join fetch reqTag.pfUser usrTag "
				+ "where tag.ctag in ('NUEVO','LEIDO','EN ESPERA','VALIDADO') "
				+ "and tag.ctype = 'ESTADO' "
				+ "and usr = :usr and usr = usrTag ";

		// application
		List<PfApplicationsDTO> applicationList = null;
		if (null != filter.getPfApplication()
				&& !"".equals(filter.getPfApplication().getCapplication())) {
			// children
			String sQueryAppChildren = "SELECT * "
						+ "FROM pf_aplicaciones apl "
						+ "CONNECT BY PRIOR apl.X_APLICACION = apl.APL_X_APLICACION "
						+ "START WITH apl.C_APLICACION = :app ";
			Query queryAppChildren = entityManager.createQuery(sQueryAppChildren).setParameter("apl", PfApplicationsDTO.class);
			queryAppChildren.setParameter("app", filter.getPfApplication().getPrimaryKey());
			applicationList = (List<PfApplicationsDTO>) queryAppChildren.getResultList();
			if (!applicationList.isEmpty()) {
				queryString += " and app in (:application) ";
			}

		}

		// dates
		if (null != filter.getFstart()) {
			queryString += " and req.fentry > :start ";
		}
		if (null != filter.getFend()) {
			queryString += " and req.fentry < :end ";
		}

		// subject
		if (null != filter.getTsubjectFilter()
				&& !"".equals(filter.getTsubjectFilter())) {
			queryString += "and (upper(req.dsubject) like '%'||:subject||'%' "
					+ "or upper(req.dreference) like '%'||:subject||'%' ";
		}

		// request affected
		if (null != req) {
			queryString += "and req = :req ";
		}

		Query query = entityManager.createQuery(queryString);

		// application
		if (null != filter.getPfApplication()
				&& !"".equals(filter.getPfApplication().getCapplication())
				&& applicationList != null && !applicationList.isEmpty()) {
			query.setParameter("application", applicationList);
		}

		// dates
		if (null != filter.getFstart()) {
			query.setParameter("start", filter.getFstart());
		}
		if (null != filter.getFend()) {
			query.setParameter("end", filter.getFend());
		}
		// subject
		if (null != filter.getTsubjectFilter()
				&& !"".equals(filter.getTsubjectFilter())) {
			query.setParameter("subject", filter.getTsubjectFilter());
		}

		// request affected
		if (null != req) {
			query.setParameter("req", req);
		}

		query.setParameter("usr", filter.getPfUser());
		List<AbstractBaseDTO> resultList = query.getResultList();
		return resultList;

	}
	
	/**
	 * Solución para la posibilidad de crear peticiones con destinatarios repetidos
	 *	TODO: Faltaría la consulta de peticiones EN ESPERA/CADUCADAS (¿sirve con que haya una etiqueta
	 *	en dicho estado o tendrían que estar todas en el rango de estados?)
	 * @param pageSize
	 * @param pageActual
	 * @param orderAttribute
	 * @param order
	 * @param userDTO
	 * @param searchFilter
	 * @param labelFilter
	 * @param applicationFilter
	 * @param initDateFilter
	 * @param endDateFilter
	 * @param tagList
	 * @param job
	 * @param requestFilter
	 * @param filtroTipo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<AbstractBaseDTO> queryRequestListPaginatedReceived(int pageSize,
			int pageActual, String orderAttribute, String order,
			PfUsersDTO userDTO, String searchFilter, String labelFilter,
			String applicationFilter, Date initDateFilter, Date endDateFilter,
			List tagList, PfUsersDTO job, String requestFilter, String filtroTipo) {

		log.info("queryRequestListPaginated");
		RequestListDTO list = null;
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		//SELECT
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PfRequestsDTO> cq = cb.createQuery(PfRequestsDTO.class);
		Root<PfRequestsDTO> req = cq.from(PfRequestsDTO.class);
		Root<PfRequestTagsDTO> reqTag = cq.from(PfRequestTagsDTO.class);
		Root<PfTagsDTO> tag = cq.from(PfTagsDTO.class);
		Root<PfApplicationsDTO> aplic = cq.from(PfApplicationsDTO.class);
		Root<PfUsersDTO> usr = cq.from(PfUsersDTO.class);
		Root<PfImportanceLevelsDTO> reqImp = cq.from(PfImportanceLevelsDTO.class);
		Root<PfConfigurationsDTO> conf = cq.from(PfConfigurationsDTO.class);
		
		//CLAUSULAS WHERE
		//Peticion vs. Etiqueta Peticion
		Predicate p1 = cb.equal(req.get("primaryKey"), reqTag.get("pfRequest").get("primaryKey"));
		predicates.add(p1);
		//Etiqueta Peticion vs. Etiqueta
		Predicate p2 = cb.equal(reqTag.get("pfTag").get("primaryKey"), tag.get("primaryKey"));
		predicates.add(p2);
		//Petición vs. Aplicación
		Predicate p3 = cb.equal(req.get("pfApplication").get("primaryKey"), aplic.get("primaryKey"));
		predicates.add(p3);
		//Etiqueta Petición vs. Usuario
		Predicate p4 = cb.equal(reqTag.get("pfUser").get("primaryKey"), usr.get("primaryKey"));
		predicates.add(p4);
		Predicate p5 = cb.or(req.get("fstart").isNull(), 
				cb.lessThan(req.get("fstart").as(Date.class), new Date()));
		predicates.add(p5);
		
		Predicate p9 = cb.greaterThanOrEqualTo(req.get("fcreated").as(Date.class), initDateFilter);
		predicates.add(p9);
		Predicate p10 = cb.lessThanOrEqualTo(req.get("fcreated").as(Date.class), endDateFilter);
		predicates.add(p10);
		//Petición vs. Nivel importancia
		Predicate p11 = cb.equal(req.get("pfImportance").get("primaryKey"), reqImp.get("primaryKey"));
		predicates.add(p11);
		//Aplicación vs. Configuración
		Predicate p12 = cb.equal(aplic.get("pfConfiguration").get("primaryKey"), conf.get("primaryKey"));
		predicates.add(p12);
		//Etiqueta Petición vs. Línea Firma
		Join<PfRequestTagsDTO, PfSignLinesDTO> signLine = reqTag.join("pfSignLine", JoinType.LEFT);
		//Petición vs. Usuario 
		Fetch<PfRequestsDTO, PfUsersDTO> rem = req.fetch("pfUsersRemitters").fetch("pfUser");
		
		// Si hay filtro por tipo de petición se añaden las líneas de firma a la consulta
		/*if (!Util.esVacioONulo(filtroTipo)) {
			Root<PfSignLinesDTO> signLine = cq.from(PfSignLinesDTO.class);
			Predicate p6 = cb.equal(reqTag.get("pfSignLine").get("primaryKey"), signLine.get("primaryKey"));
			predicates.add(p6);
			
			Root<PfSignLinesDTO> signLineCount = cqCount.from(PfSignLinesDTO.class);
			Predicate p6Count = cbCount.equal(reqTagCount.get("pfSignLine").get("primaryKey"), signLineCount.get("primaryKey"));
			predicatesCount.add(p6Count);
		}*/
		
		//Filtro por tipo de línea de firma (firma o vistobueno)
		if (Constants.C_TYPE_SIGNLINE_SIGN.equals(filtroTipo)) {
			Predicate p13 = cb.equal(signLine.get("ctype"), "FIRMA");
			predicates.add(p13);
		} else if (Constants.C_TYPE_SIGNLINE_PASS.equals(filtroTipo)) {
			Predicate p14 = cb.equal(signLine.get("ctype"), "VISTOBUENO");
			predicates.add(p14);
		}
		
		if (job != null) {
			Predicate or = cb.or(cb.equal(usr.get("primaryKey"), userDTO.getPrimaryKey()), 
					cb.equal(usr.get("primaryKey"), job.getPrimaryKey()));
			predicates.add(or);			
		} else {
			Predicate p14 = cb.equal(usr.get("primaryKey"), userDTO.getPrimaryKey());
			predicates.add(p14);
		}
		
		//Filtro para aplicación
		List<PfApplicationsDTO> applicationList = null;
		if (null != applicationFilter && !"".equals(applicationFilter)) {
			String sQueryAppChildren = "SELECT apl.x_aplicacion "
						+ "FROM pf_aplicaciones apl "
						+ "CONNECT BY PRIOR apl.X_APLICACION = apl.APL_X_APLICACION "
						+ "START WITH apl.C_APLICACION = '" + applicationFilter + "'";
			Query queryAppChildren = entityManager.createNativeQuery(sQueryAppChildren);
			applicationList = queryAppChildren.getResultList();
			if (!applicationList.isEmpty()) {
				Predicate p14 = cb.and(aplic.get("primaryKey").in(applicationList));
				predicates.add(p14);
			}
		}


		// search
		if (!Util.esVacioONulo(searchFilter)) {
			searchFilter = searchFilter.toUpperCase();
			searchFilter = Util.getInstance().translateSearch(searchFilter);
			Join<PfRequestsDTO, PfDocumentsDTO> doc = req.join("pfDocuments");
			Join<Join<PfRequestsDTO, PfDocumentsDTO>, PfDocumentTypesDTO> tdoc = doc.join("pfDocumentType");
			Join<PfRequestsDTO, PfUsersDTO> usrRem = req.join("pfUsersRemitters").join("pfUser");
			
			Predicate p14 = cb.like(cb.upper(req.get("dsubject").as(String.class)), '%' + searchFilter + '%');
			Predicate p15 = cb.like(cb.upper(req.get("dreference").as(String.class)), '%' + searchFilter + '%');
			Predicate p16 = cb.like(cb.upper(doc.get("dname").as(String.class)), '%' + searchFilter + '%');
			Predicate p17 = cb.like(cb.upper(tdoc.get("cdocumentType").as(String.class)), '%' + searchFilter + '%');
			Predicate p18 = cb.like(cb.upper(usrRem.get("dname").as(String.class)), '%' + searchFilter + '%');
			Predicate p19 = cb.like(cb.upper(usrRem.get("dsurname1").as(String.class)), '%' + searchFilter + '%');
			Predicate p20 = cb.like(cb.upper(usrRem.get("dsurname2").as(String.class)), '%' + searchFilter + '%');
			predicates.add(cb.or(p14, p15, p16, p17, p18, p19, p20));
		}

		
		//Filtro etiquetas
		if (!Util.esVacioONulo(labelFilter)) {
			Join<PfRequestsDTO, PfRequestTagsDTO> rTagFilter = req.join("pfRequestsTags");
			Join<Join<PfRequestsDTO, PfRequestTagsDTO>, PfTagsDTO> tagFilter = rTagFilter.join("pfTag");
			Join<Join<PfRequestsDTO, PfRequestTagsDTO>, PfUsersDTO> filterUser = rTagFilter.join("pfUser");
			Predicate p15 = cb.equal(tagFilter.get("primaryKey"), Long.parseLong(labelFilter));
			predicates.add(p15);

		}
		
		List<Long> requestList = null;	
		String subQueryEstados = "";
		if(tagList.contains("FIRMADO") && tagList.contains("RETIRADO") 
				&& tagList.contains("DEVUELTO") && tagList.contains("VISTOBUENO")){
			subQueryEstados = "SELECT pet_x_peticion FROM PETICIONES_FIRM_DEV_VB_RET";
			
			Query queryTagsEstados = entityManager.createNativeQuery(subQueryEstados);
			requestList = queryTagsEstados.getResultList();

			Predicate or = cb.disjunction();
			if(requestList.size() >  1000){
				List<?> subList = null;
				while(requestList.size()>1000){
			        subList = requestList.subList(0, 1000);		        
			        or.getExpressions().add(req.get("primaryKey").in(subList));
			        requestList.subList(0, 1000).clear();
			      }
				if(!requestList.isEmpty()){
					or.getExpressions().add(req.get("primaryKey").in(requestList));
				}
				predicates.add(or);
			}else{
				Predicate p7 = cb.and(req.get("primaryKey").in(requestList));
				predicates.add(p7);
			}
		}else{
		//if(tagList.contains("NUEVO") && tagList.contains("LEIDO") && tagList.contains("VALIDADO")){
			subQueryEstados = "SELECT pet_x_peticion FROM PETICIONES_LEIDO_NUEVO_VALID";
			
			Predicate p7 = cb.equal(tag.get("ctype"), "ESTADO");
			Predicate p8 = cb.and(tag.get("ctag").in(tagList));
			Predicate p7b = cb.notEqual(tag.get("ctype"), "ESTADO");
			predicates.add(cb.or(cb.and(p7,p8), p7b));
			
		}
				

	
		//ORDEN
		if(Util.esVacioONulo(orderAttribute) || Util.esVacioONulo(order)) {
			cq.select(req).distinct(true).orderBy(cb.desc(req.get("fmodified")));
		}else{
			if (orderAttribute.equals("application")) {
				cq.select(req).distinct(true).orderBy(cb.desc(aplic.get("capplication")));
			}else{
				cq.select(req).distinct(true).orderBy(cb.desc(req.get(orderAttribute)));
			}
		}
		

		//WHERE SELECT/COUNT
		//El count se hace sobre el mismo criteriaQuery, y se hace un size() posterior para evitar
		//problemas a la hora de montar la query
		cq.where(predicates.toArray(new Predicate[] {}));
		Query query = entityManager.createQuery(cq);
		Query queryCount = entityManager.createQuery(cq);
		
		int initPage = (pageActual - 1) * pageSize;
		log.info("Pagination: initPage=" + initPage + " and sizePage=" + pageSize + ".");
		List<AbstractBaseDTO> listQuery = null;
		Long listQuerySize = (long) queryCount.getResultList().size();
		if (listQuerySize > 0) {
			log.info("Total size: " + listQuerySize + ".");	
			query.setMaxResults(pageSize);
			query.setFirstResult(initPage);
			listQuery = query.getResultList();
		}else{
			listQuery = new ArrayList<AbstractBaseDTO>();
		}
		
		list =  new RequestListDTO(listQuery, listQuerySize, pageSize);
		
		log.info("QueryList obtains: " + listQuerySize + "/" + listQuery.size() + " values.");
		
		return list;
	}	
	
	

}
