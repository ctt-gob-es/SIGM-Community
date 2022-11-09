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

package es.seap.minhap.portafirmas.utils.metadata;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.business.metadata.ApplicationMetadataBO;
import es.seap.minhap.portafirmas.domain.PfApplicationsMetadataDTO;
import es.seap.minhap.portafirmas.exceptions.MetadataNotValidException;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.MetadatoAdicional;


@Component
public class MetadataValidator {
	
	@Autowired
	ApplicationMetadataBO applicationMetadataBO;
	
	@Autowired
	UtilComponent util;
	
	public void validarMetadatos(String idAplicacion, List<MetadatoAdicional> metadatosAdicionales) throws MetadataNotValidException {
		//obtenemos los metadatos definidos para la aplicación
		List<PfApplicationsMetadataDTO> metadatosAplicacion = applicationMetadataBO.getMetadatasByAplicacion(idAplicacion);
		
		preCheckMetadatas(idAplicacion, metadatosAplicacion, metadatosAdicionales);
		
		checksMetadatasDefined(idAplicacion, metadatosAplicacion, metadatosAdicionales);
		
		checksArriveObligatory(metadatosAplicacion, metadatosAdicionales);
		
		checkMetadataValue(metadatosAdicionales);
	}
	
	private void preCheckMetadatas(String idAplicacion, List<PfApplicationsMetadataDTO> metadatosAplicacion, List<MetadatoAdicional> metadatosAdicionales) throws MetadataNotValidException {
		if (util.isNotEmpty(metadatosAdicionales)) {
			if (util.isEmpty(metadatosAplicacion)) {
				throw new MetadataNotValidException("No se han definido metadatos adicionales para la aplicacion " + idAplicacion);
			}
		}
	}
	
	private void checksMetadatasDefined(String idAplicacion, List<PfApplicationsMetadataDTO> metadatosAplicacion, List<MetadatoAdicional> metadatosAdicionales) throws MetadataNotValidException {
		if (util.isNotEmpty(metadatosAdicionales)) {
			for (MetadatoAdicional metadatoAdicional : metadatosAdicionales) {
				if (!CollectionUtils.exists(metadatosAplicacion, predicatePfApplicationsMetadataDTOByName(metadatoAdicional.getNombre()))) {
					throw new MetadataNotValidException("Metadato adicional " + metadatoAdicional.getNombre() +  " no definido para la aplicacion " + idAplicacion);
				}
			}
		}
	}
	
	private void checksArriveObligatory(List<PfApplicationsMetadataDTO> metadatosAplicacion, List<MetadatoAdicional> metadatosAdicionales) throws MetadataNotValidException {
		List<PfApplicationsMetadataDTO> metadatosAplicacionObligatorios = new ArrayList<PfApplicationsMetadataDTO>();
		metadatosAplicacionObligatorios.addAll(metadatosAplicacion);
		
		//obtenemos los metadatos obligatorio que no sean ENI
		CollectionUtils.filter(metadatosAplicacionObligatorios, predicatePfApplicationsMetadataDTONoEniAndMandatory());
		
		for (PfApplicationsMetadataDTO metadataObligatorio : metadatosAplicacionObligatorios) {
			if (!CollectionUtils.exists(metadatosAdicionales, predicateMetadatoAdicionalByName(metadataObligatorio.getDname()))) {
				throw new MetadataNotValidException("Falta definir el metadato obligatorio " + metadataObligatorio.getDname());
			}
		}
	}
	
	private void checkMetadataValue(List<MetadatoAdicional> metadatosAdicionales) throws MetadataNotValidException {
		if (util.isNotEmpty(metadatosAdicionales)) {
			for (MetadatoAdicional metadatoAdicional : metadatosAdicionales) {
				if (StringUtils.isEmpty((String) metadatoAdicional.getValor())) {
					throw new MetadataNotValidException("Metadato adicional " + metadatoAdicional.getNombre() +  " no definido valor");
				}
			}
		}
	}
	
	private Predicate predicatePfApplicationsMetadataDTOByName(final String name) {
		return new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				PfApplicationsMetadataDTO data = (PfApplicationsMetadataDTO) arg0;
				return data.getDname().equals(name);
			}
		};
	}
	
	private Predicate predicatePfApplicationsMetadataDTONoEniAndMandatory() {
		return new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				PfApplicationsMetadataDTO data = (PfApplicationsMetadataDTO) arg0;
				return !data.getLeni() && data.getLmandatory();
			}
		};
	}
	
	private Predicate predicateMetadatoAdicionalByName(final String name) {
		return new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				MetadatoAdicional data = (MetadatoAdicional) arg0;
				return data.getNombre().equals(name);
			}
		};
	}
	
}
