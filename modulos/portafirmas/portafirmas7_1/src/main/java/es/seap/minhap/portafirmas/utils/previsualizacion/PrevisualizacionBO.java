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

package es.seap.minhap.portafirmas.utils.previsualizacion;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ws.EEUtilMiscBO;
import es.seap.minhap.portafirmas.business.ws.EEUtilUtilFirmaBO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.SignData;
import es.seap.minhap.portafirmas.utils.SignDataUtil;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PrevisualizacionBO {

	@Autowired
	private EEUtilUtilFirmaBO eeUtilUtilFirmaBO;

	@Autowired
	private EEUtilMiscBO eeUtilMiscBO;

	@Autowired
	private SignDataUtil signDataUtil;

	public void previsualizacion(InputStream firma, OutputStream output, PfApplicationsDTO peticionario)
			throws Exception {

		byte[] bytesFirma = IOUtils.toByteArray(firma);

		SignData datosFirma = signDataUtil.getDataFromSign(bytesFirma);
		//Se obtiene el byte[] de los datos de firma.
		byte[] bytesDocumento = getDocumentBytes(datosFirma);
		String tipoFirma = datosFirma.getTipoFirma();
		String mimeDocument = null;

		// Si el mime devuelto por Afirma es "application/octet-stream", lo
		// ponemos a nulo, para que el servicio eeutil lo averigüe por su
		// cuenta.
		if (datosFirma.getTipoMime().contentEquals(Constants.AFIRMA_MIME_GENERICO)) {
			mimeDocument = null;
		} else {
			mimeDocument = datosFirma.getTipoMime();
		}

		byte[] previsBytes = eeUtilUtilFirmaBO.getDocumentWithSignInfo(bytesFirma, bytesDocumento, mimeDocument,
				tipoFirma, Constants.PREVISUALIZACION, null, null, peticionario);

		IOUtils.write(previsBytes, output);

	}

	/**
	 * Se obtiene el byte[] del documento.
	 * Si el mimeType es text/tcn, se llama al método que transforma dicho documento TCN a formato PDF, y el 
	 * mimeType se vuelve a poner como application/pdf para que no de problemas al abrirlo.
	 * Si el mimeType no es de tipo text/tcn, se
	 * @param datosFirma
	 * @return
	 * @throws EeutilException
	 */
	private byte[] getDocumentBytes(SignData datosFirma) throws EeutilException {
		if("text/tcn".equals(datosFirma.getTipoMime())){
			datosFirma.setTipoMime("application/pdf");
			return eeUtilMiscBO.getPdfFromTCN(datosFirma.getDocument());
		}else{
			return datosFirma.getDocument();
		}

	}

	/**
	 * Se obtiene el byte[] del documento.
	 * Si el mimeType es text/tcn, se llama al método que transforma dicho documento TCN a formato PDF, y el 
	 * mimeType se vuelve a poner como application/pdf para que no de problemas al abrirlo.
	 * Si el mimeType no es de tipo text/tcn, se
	 * @param datosFirma
	 * @return
	 * @throws EeutilException
	 */
	public byte[] getpdfBytes(byte[] datosTCN) throws EeutilException {

		return eeUtilMiscBO.getPdfFromTCN(datosTCN);

	}
}
