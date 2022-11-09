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

package es.seap.minhap.portafirmas.business.ws;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.CSVBO;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.BinaryDocument;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.dsic.csv.ws.Documento;
import es.seap.minhap.portafirmas.exceptions.CSVNotFoundException;
import es.seap.minhap.portafirmas.utils.Util;
import net.java.dev.jaxb.array.IntArray;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class WSDocCSVBO {


	Logger log = Logger.getLogger(WSDocCSVBO.class);
	
	@Autowired
	private CSVBO csvBO;
	
	@Autowired
	ApplicationBO applicationBO;

	@Autowired
	BinaryDocumentsBO binaryDocumentsBO;

	
	@Transactional (readOnly=false)
	public Documento obtenerDocumento(String idAplicacion, String csv,
			  IntArray posicCSV, String nif,
			  String numExpediente) {
	
		ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		Documento resultado = new Documento();
		try {
			
			csv = Util.formatearCsv(csv);
			boolean normalizado = false;
			PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByCsv (csv);
			if (signDTO == null) {
				signDTO = binaryDocumentsBO.getSignDTOByCsvNormalizado (csv);
				normalizado = true;
			}
			byte[] bytes = csvBO.getInformeByCSV(signDTO, applicationBO.loadApplication(idAplicacion), normalizado);
			
			resultado.setContenidoDocumento(bytes);
			resultado.setNombreDocumento(binaryDocumentsBO.getNombreReport(signDTO.getPfDocument().getDname()));
			
		} catch (CSVNotFoundException e) {			
			resultado.setContenidoDocumento(null);
			resultado.setNombreDocumento(null);			
			log.error("ERROR: WSDocCSVBO.obtenerDocumento, ", e);
		} catch (Exception e) {
			log.error("Error obteniendo informe", e);
			throw new RuntimeException(e);
		}
		
		return resultado;
	}
	
}
