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

package es.seap.minhap.portafirmas.utils.document.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputReport;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class FilePathCustodyServiceOutputImpl implements CustodyServiceOutput {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(FilePathCustodyServiceOutputImpl.class);

	public void initialize(Map<String, Object> parameterMap)
			throws CustodyServiceException {
		// nothing
	}
	
	@Override
	public boolean deleteFile(CustodyServiceOutputDocument document) throws CustodyServiceException {
		log.info("deleteFile init");
		boolean borrado = false;
		File fichero = new File(document.getUri());
		if (fichero.delete()) borrado=true;
		log.info("deleteFile end");
		return borrado;
	}

	public byte[] downloadFile(CustodyServiceOutputDocument document) throws CustodyServiceException {
		log.info("downloadFile init");
		byte[] bytes = download(document.getUri());
		log.info("downloadFile end");
		return bytes;
	}
	
	public byte[] downloadDocelFile(CustodyServiceOutputDocument document) throws CustodyServiceException {
		log.info("downloadDocelFile init");
		byte[] bytes =  download(document.getUri());
		log.info("downloadDocelFile end");
		return bytes;
	}

	public BigDecimal fileSize(CustodyServiceOutputDocument document)
			throws CustodyServiceException {
		log.info("fileSize init");
		return size(document.getUri());
	}
	

	@Override
	public boolean deleteSign(CustodyServiceOutputSign sign) throws CustodyServiceException {
		log.info("deleteFile init");
		boolean borrado = false;
		File fichero = new File(sign.getUri());
		if (fichero.delete()) borrado=true;
		log.info("deleteFile end");
		return borrado;
	}

	public byte[] downloadSign(CustodyServiceOutputSign sign) throws CustodyServiceException {
		log.info("downloadSign init");
		byte[] bytes =  download(sign.getUri());
		log.info("downloadSign end");
		return bytes;

	}

	public BigDecimal signSize(CustodyServiceOutputSign sign)
			throws CustodyServiceException {
		log.info("fileSize end");
		return size(sign.getUri());
	}
	
	public byte[] downloadReport (CustodyServiceOutputReport report) throws CustodyServiceException {
		log.info("downloadReport init");
		byte[] bytes =  download(report.getUri());
		log.info("downloadReport end");
		return bytes;
		
	}

	private BigDecimal size(String uri) throws CustodyServiceException {
		File file = new File(uri);
		int size = (int) file.length();
		return new BigDecimal(size);
	}

	private byte[] download(String uri) throws CustodyServiceException {
		FileInputStream fileIn = null;
		byte[] bytes = null;
		try {
			fileIn = new FileInputStream(uri);
			bytes = IOUtils.toByteArray(fileIn);
			fileIn.close();
		} catch (Exception e) {
			log.error("ERROR: FilePathCustodyServiceOutputImpl.download, ", e);
			throw new CustodyServiceException(e.getMessage());
		}finally{
			if(fileIn != null){
				try {
					fileIn.close();
				} catch (IOException e) {
					log.error("ERROR: FilePathCustoryServiceOutputImpl.download: Not possible to close FileIn, ", e);
				}
			}
		}
		
		return bytes;
	}	
	
}
