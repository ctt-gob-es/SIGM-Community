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
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.BinaryDocumentSign;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.ws.bean.SignatureSerializable;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SignatureServiceBO {

	private Logger log = Logger.getLogger(SignatureServiceBO.class);

	@Autowired
	private CustodyServiceFactory custodyServiceFactory;
	
	@Autowired
	private BinaryDocumentsBO binaryDocumentsBO;
	
	
	/**
	 * M&eacute;todo que obtiene un fichero de firma serializable a partir de un documento.
	 * @param doc Documento firmado.
	 * @return Firma serializable.
	 * @throws CustodyServiceException
	 */
	public SignatureSerializable getSignatureSerializable(PfDocumentsDTO doc) throws DocumentCantBeDownloadedException, IOException {
		
		SignatureSerializable signature = new SignatureSerializable();
		
		PfSignsDTO fileSign = custodyServiceFactory.signFileQuery(doc.getChash());		
		
		if (fileSign != null) {
			byte[] bytes = binaryDocumentsBO.getSignatureBySignDTO(fileSign);
			signature.setContentBytes(bytes);
			signature.setMimeType(Util.getInstance().loadSignMime().get(fileSign.getCformat()));
			signature.setSignFormat(fileSign.getCformat());
			signature.setSign(true);
			log.debug("Download sign succesfully");	
		} else {
			signature.setSign(false);
			log.debug("Download sign error, no se encuentra firma asociada a: " + doc.getChash());			
		}
		
		return signature;
	}
	
}
