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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.dao.CustodyDAO;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputReport;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class DBCustodyServiceOutputImpl implements CustodyServiceOutput {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> parameterMap = new HashMap<String, Object>();

	public byte[] downloadFile(CustodyServiceOutputDocument document) throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		return custodyDAO.downloadFile(document.getIdentifier());
	}
	
	public byte[] downloadDocelFile(CustodyServiceOutputDocument document) throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		return custodyDAO.downloadDocelFile(document.getIdentifier());
	}

	public BigDecimal fileSize(CustodyServiceOutputDocument document)
			throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		BigDecimal result = custodyDAO.fileSize(document.getIdentifier());
		return result;
	}

	public void initialize(Map<String, Object> parameterMap)
			throws CustodyServiceException {
		this.parameterMap = parameterMap;
	}

	public  byte[] downloadSign(CustodyServiceOutputSign sign) throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		return custodyDAO.downloadSign(sign);

	}

	public BigDecimal signSize(CustodyServiceOutputSign sign)
			throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		BigDecimal result = custodyDAO.signSize(sign);
		return result;
	}
	
	public byte[] downloadReport (CustodyServiceOutputReport report) throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		return custodyDAO.downloadReport(report);
	}

	@Override
	public boolean deleteFile(CustodyServiceOutputDocument document) throws CustodyServiceException {
		//FALTA IMPLEMENTAR POR BBDD
		//[TICKET1303#PORTAFIRMAS] Elimnar documento de la bbdd cuando se utiliza el servicio de deleteRequestSend
		return false;
	}

	@Override
	public boolean deleteSign(CustodyServiceOutputSign sign) throws CustodyServiceException {
		//FALTA IMPLEMENTAR POR BBDD
		//[TICKET1303#PORTAFIRMAS] Elimnar documento de la bbdd cuando se utiliza el servicio de deleteRequestSend
		return false;
	}
}
