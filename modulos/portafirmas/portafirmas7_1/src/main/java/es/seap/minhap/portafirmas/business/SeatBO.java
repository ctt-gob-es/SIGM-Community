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

package es.seap.minhap.portafirmas.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.Seat;
import es.seap.minhap.portafirmas.web.converter.SeatConverter;
import es.seap.minhap.portafirmas.web.validation.SeatValidator;

@Service
public class SeatBO {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private ProvinceBO provinceBO;

	@Autowired
	private SeatConverter seatConverter;

	@Autowired
	private SeatValidator seatValidator;

	public List<AbstractBaseDTO> getAllSeats() {
		return baseDAO.queryListMoreParameters("request.queryAllProvinces", null);
	}

	public List<AbstractBaseDTO> getAllUsers() {
		return baseDAO.queryListMoreParameters("request.userAll", null);
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	@Transactional
	public ArrayList<String> saveSeat(String primaryKey,String seatCode,String seatName, String seatLongDuration,
			String seatURLCSV, String seatLdap, Boolean ... esOrganismo) {
		ArrayList<String> errors = new ArrayList<String>();
		try {

			if(Constants.MAIL_CONS_TRUE.equals(seatLongDuration)){
				seatURLCSV="";
			}
			
			Seat seat = new Seat(seatCode, seatName, Boolean.parseBoolean(seatLongDuration), seatURLCSV,  Boolean.parseBoolean(seatLdap));
			
			// Se obtienen la información de la vista
//			Seat seat = new Seat(seatCode, seatName);
			if(!Util.esVacioONulo(primaryKey)) {
				seat.setPrimaryKey(Long.parseLong(primaryKey));
			}
			
			// Se valida la vista
			seatValidator.validate(seat, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// se vuelcan los datos obtenidos de la vista
			PfProvinceDTO pfProvinceDTO = seatConverter.envelopeToDTO(seat);

			// Se validan las reglas de negocio
			provinceBO.validateSeat(pfProvinceDTO, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se realiza la persistencia
			
//			if(esOrganismo==null || esOrganismo.length==0 || !esOrganismo[0]){
//				
//				if(pfProvinceDTO.getOrganismo()==null
//						|| pfProvinceDTO.getOrganismo().getPrimaryKey()==null){
//					pfProvinceDTO.getOrganismo(new PfProvinceDTO());
//					
//					pfProvinceDTO.getPfProvincePadreDTO().setPrimaryKey(-1L);
//				}
//				
//			}

			provinceBO.saveSeat(pfProvinceDTO);
			
		} catch (Exception e) {
			log.error("Error al insertar la sede: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
}
