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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.dao.CustodyDAO;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputReport;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceInput;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class DBCustodyServiceInputImpl implements CustodyServiceInput {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> parameterMap = new HashMap<String, Object>();

	public void initialize(Map<String, Object> parameterMap)
			throws CustodyServiceException {
		this.parameterMap = parameterMap;
	}

	public String uploadFile(CustodyServiceInputDocument document,
			InputStream input) throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		return custodyDAO.uploadFile(input, document);
	}

	public String uploadSign(CustodyServiceInputSign sign, InputStream input)
			throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		return custodyDAO.uploadSign(sign, input);
	}
	
	public String uploadReport (CustodyServiceInputReport report, InputStream input)
			throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		return custodyDAO.uploadReport(report, input);
	}
	
	public String uploadNormalizedReport (CustodyServiceInputReport report, InputStream input)
			throws CustodyServiceException {
		CustodyDAO custodyDAO = new CustodyDAO((BaseDAO) parameterMap
				.get("baseDAO"));
		return custodyDAO.uploadNormalizedReport(report, input);
	}
}
