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

import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.domain.PfServersDTO;
import es.seap.minhap.portafirmas.web.beans.Server;

@Component
public class ServerConverter {
	
	/**
	 * Rellena el DTO con las propiedades del objeto de envoltura.
	 * @param server objeto de envoltura.
	 * @param serverDTO dto a rellenar
	 */
	public void envelopeToDTO (final Server server, final PfServersDTO serverDTO) {
		serverDTO.setCserver(server.getCode().trim());
		serverDTO.setDserver(server.getDescription().trim());
		serverDTO.setLmain(server.isDefault());
	}

}
