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

package es.seap.minhap.portafirmas.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.web.beans.Seat;

@Component
public class SeatConverter {
	
	@Autowired
	private ProvinceBO provinceBO;

	/**
	 * Obtiene el objeto a persistir en BB.DD. a partir de los datos de la vista
	 * @param seat
	 * @return
	 */
	public PfProvinceDTO envelopeToDTO(Seat seat) {
		PfProvinceDTO pfProvinceDTO = null;
		if(seat.getPrimaryKey() == null) {
			pfProvinceDTO = new PfProvinceDTO();
		} else {
			pfProvinceDTO = provinceBO.querySeatByPk(seat.getPrimaryKey());
		}	
		pfProvinceDTO.setPrimaryKey(seat.getPrimaryKey());
		pfProvinceDTO.setCcodigoprovincia(seat.getCode());
		pfProvinceDTO.setCnombre(seat.getName());
		pfProvinceDTO.setLLargaDuracion(seat.isLongDuration());
		pfProvinceDTO.setCUrlCSV(seat.getUrlCSV());
		pfProvinceDTO.setLLdap(seat.isLdap());
		
		return pfProvinceDTO;
	}

}
