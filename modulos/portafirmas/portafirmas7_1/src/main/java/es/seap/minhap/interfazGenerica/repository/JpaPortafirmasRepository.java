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

package es.seap.minhap.interfazGenerica.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.interfazGenerica.domain.Portafirmas;
import es.seap.minhap.portafirmas.utils.UtilComponent;

@Repository
public class JpaPortafirmasRepository implements PortafirmasRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UtilComponent util;

	@Override
	@Transactional
	public void guardar(Portafirmas portafirmas) {
		if (portafirmas.getIdPortafirmas() == null) {
			entityManager.persist(portafirmas);
		} else {
			entityManager.merge(portafirmas);
		}
		entityManager.flush();
	}
	
	@Override
	public Portafirmas obtener(Long idPortafirmas) {
		return this.entityManager.find(Portafirmas.class, idPortafirmas);
	}

	@Override
	public List<Portafirmas> buscar(Portafirmas portafirma) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Portafirmas> consulta = cb.createQuery(Portafirmas.class);
		Root<Portafirmas> portafirmas = consulta.from(Portafirmas.class);

		List<Predicate> predicados = new ArrayList<Predicate>();
        if(portafirma.getcPortafirmas() != null) {
        	predicados.add(cb.equal(portafirmas.get("cPortafirmas"), portafirma.getcPortafirmas()));
        }
        if(!util.esVacioONulo(portafirma.getNombre())) {
        	predicados.add(cb.equal(portafirmas.get("nombre"), portafirma.getNombre()));
        }
        consulta.select(portafirmas).where(predicados.toArray(new Predicate[]{}));
        
        List<Portafirmas> listaPortafirmas = entityManager.createQuery(consulta).getResultList();
        
		return listaPortafirmas;
	}

	@Override
	@Transactional
	public void borrar(Portafirmas portafirmas) {
		this.entityManager.remove(this.entityManager.find(Portafirmas.class, portafirmas.getIdPortafirmas()));
	}

	public List<Portafirmas> obtenerTodos() {
		return this.entityManager.createQuery("SELECT p FROM Portafirmas p order by p.nombre", Portafirmas.class).getResultList();
	}

}
