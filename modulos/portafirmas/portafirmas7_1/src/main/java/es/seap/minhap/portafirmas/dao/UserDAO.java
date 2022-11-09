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

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUnidadOrganizacionalDTO;
import es.seap.minhap.portafirmas.domain.PfUsersProfileDTO;
import es.seap.minhap.portafirmas.web.beans.Paginator;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;

@Repository
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class UserDAO {

	Logger log = Logger.getLogger(UserDAO.class);

    @PersistenceContext
	private EntityManager entityManager;
    
    @Autowired
    private ProvinceBO provinceBO;
    
    @Autowired
    private UserAdmBO userAdmBO;

	/**
	 * Consulta que recupera los usuarios teniendo en cuenta paginación
	 * @param usersParameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AbstractBaseDTO> queryUserListPaginated(UsersParameters usersParameters) {
		Paginator paginator = usersParameters.getPaginator();
		List<AbstractBaseDTO> provinceList = null;
		List <PfUnidadOrganizacionalDTO> organismos = usersParameters.getUser().getPfUnidadOrganizacionalList();
		Set<PfUsersProfileDTO> userProfile = usersParameters.getUser().getPfUsersProfiles();
		
		boolean isAdministrator = (userAdmBO.isAdministrator(userProfile) || userAdmBO.isAdminCAID(userProfile));
		boolean isOrganismAdministrator = userAdmBO.isOrganismAdministrator(userProfile);
		
		// Consultas
		Query query = entityManager.createQuery(getUserQuery(usersParameters, isAdministrator));
		Query queryCount = entityManager.createQuery(getUserQueryCount(usersParameters, isAdministrator));
		// .. parametros..
		String find = "%" + usersParameters.getSearchText() + "%";
		query.setParameter("find", find);
		queryCount.setParameter("find", find);
		query.setParameter("type", usersParameters.getType());
		queryCount.setParameter("type", usersParameters.getType());
		if(!isAdministrator) {
			List<AbstractBaseDTO> adminProvinces = provinceBO.getAdminProvinces(usersParameters.getUser());			
			if (!adminProvinces.isEmpty()){
				provinceList = adminProvinces;
				
				if (organismos != null){
					if (isOrganismAdministrator){
						List<AbstractBaseDTO> provinceOrganismList =  provinceBO.getAdminProvincesOrganism(organismos);
						provinceList.addAll(provinceOrganismList);
					}
				}
				
			}			
			query.setParameter("province", provinceList);
			queryCount.setParameter("province", provinceList);
		}
		// .. se configura la paginación..
		query.setMaxResults(paginator.getPageSize());
		query.setFirstResult((paginator.getCurrentPage() - 1) * paginator.getPageSize());
		
		// .. ejecución de las consultas
		List<AbstractBaseDTO> usersList = query.getResultList();
		List<Long> lista = queryCount.getResultList();
		int length = lista.size();
		 
		// Se actualiza la paginación
		paginator.setInboxSize(length);
		// Se recalcula el numero de paginas con su método interno
		paginator.setNumPages(paginator.getNumPages());
		
		return usersList;
	}

	/**
	 * Obtiene la consulta para recuperar usuarios
	 * 
	 * @param usersParameters
	 * @param isAdministrator
	 * @return
	 */
	private String getUserQuery(UsersParameters usersParameters, boolean isAdministrator) {
		return getQuery(usersParameters, isAdministrator, true);
	}
	
	/**
	 * Obtiene la consulta para contar usuarios
	 * 
	 * @param usersParameters
	 * @param isAdministrator
	 * @return
	 */
	private String getUserQueryCount(UsersParameters usersParameters, boolean isAdministrator) {
		return getQuery(usersParameters, isAdministrator, false);
	}

	/**
	 * Método que engloba el código común y distinto, de las consultas de recuperación
	 * y conteo de usuarios
	 * 
	 * @param usersParameters
	 * @param isAdministrator
	 * @param isSelect
	 * @return
	 */
	private String getQuery(UsersParameters usersParameters, boolean isAdministrator, boolean isSelect) {
		//  En el conteo, no se cargan los datos.
		StringBuilder sb = new StringBuilder();
		if(isSelect) {
			sb.append("select user ");
		} else {
			sb.append("select distinct user.primaryKey ");
		}
		sb.append("  from PfUsersDTO user ");
		if(isSelect) {
			sb.append("left join fetch user.portafirmas portafirmas ");
			sb.append("left join fetch user.pfProvince province ");
			sb.append("left join fetch user.pfUsersJobs userJob ");
			sb.append("left join fetch userJob.pfUserJob job ");
		} else {
			sb.append("left join user.portafirmas portafirmas ");
			sb.append("left join user.pfProvince province ");
			sb.append("left join user.pfUsersJobs userJob ");
			sb.append("left join userJob.pfUserJob job ");
		}
		sb.append("where (  convert(trim(upper(user.dname||' '||coalesce(user.dsurname1,'')||' '||coalesce(user.dsurname2,''))),'US7ASCII') like convert(upper(:find),'US7ASCII') ");
		sb.append("	   or convert(upper(user.cidentifier), 'US7ASCII') like convert(upper(:find), 'US7ASCII') ");
		sb.append("	   or convert(upper(province.cnombre), 'US7ASCII') like convert(upper(:find), 'US7ASCII') ");
		sb.append("	   or convert(upper(portafirmas.nombre), 'US7ASCII') like convert(upper(:find), 'US7ASCII') ");
		sb.append("	   or convert(upper(job.dname), 'US7ASCII') like convert(upper(:find), 'US7ASCII') ) ");
		sb.append("  and user.ctype in :type ");
		// Si no es administrador, se condiciona la sede (tabla provincia)
		if(!isAdministrator) {
			sb.append(" and province in :province ");
		}
		// Ordenación para la parte de recuperación de datos
		if(isSelect) {
			String order = usersParameters.getOrder();
			String orderField = getOrderField(Integer.valueOf(usersParameters.getOrderField()));
			sb.append("order by user.").append(orderField).append(" ").append(order);
		}
		return sb.toString();
	}

	/**
	 * @param orderFieldId
	 * @return
	 */
	private String getOrderField(int orderFieldId) {
		switch (orderFieldId) {
			case 1: return "cidentifier";
			case 2: return "dname";
			case 3: return "dsurname1";
			case 4: return "dsurname2";
			case 5: return "pfProvince.cnombre";
			case 6: return "lvalid";
			case 7: return "lvisible";
			default: return "dsurname1,user.dsurname2,user.dname";
		}
	}
}
