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

package es.seap.minhap.portafirmas.utils.job;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import es.guadaltel.framework.signer.SignDocument;
import es.guadaltel.framework.signer.exception.SignerException;
import es.juntadeandalucia.cice.pfirma.factory.PfSignFactory;
import es.juntadeandalucia.cice.pfirma.sign.PfSign;
import es.seap.minhap.portafirmas.business.SignBO;
import es.seap.minhap.portafirmas.business.administration.PreSignBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.utils.AuthenticatorConstants;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;


public class PreSignService implements Job, ApplicationContextAware {

	private static final Logger log = Logger.getLogger(PreSignService.class);

	// Inyecto el contexto de la aplicación para acceder a los beans
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		log.info("execute PreSignJob init");

		BaseDAO baseDAO = applicationContext.getBean(BaseDAO.class);
		PreSignBO preSignBO = applicationContext.getBean(PreSignBO.class);

		SignBO signBO = applicationContext.getBean(SignBO.class);
		CustodyServiceFactory custodyServiceFactory = applicationContext.getBean(CustodyServiceFactory.class);

		log.info("Obtaining documents");
		List<AbstractBaseDTO> listDocuments = baseDAO.queryListMoreParameters("administration.documentNotPresigned", null);

		CustodyServiceOutput custodyService = null;
		CustodyServiceOutputDocument custodyDocument = null;

		long idConf = ((PfDocumentsDTO) listDocuments.get(0)).getPfRequest()
				.getPfApplication().getPfConfiguration().getPrimaryKey();
		Configuration conf = signBO.loadSignProperties(idConf);
		PfSign signer = PfSignFactory.createPfSign(conf, "afirma5", 
						conf.getString(AuthenticatorConstants.AUTHENTICATOR_AFIRMA5_TRUSTEDSTORE_PASS),
						conf.getString(AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_USER),
						conf.getString(AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_PASS));

		Iterator<AbstractBaseDTO> itDocs = listDocuments.iterator();
		while (itDocs.hasNext()) {

			log.debug("Inside the while");
			PfDocumentsDTO doc = (PfDocumentsDTO) itDocs.next();
			List<AbstractBaseDTO> docsPreSign = preSignBO
					.queryDocumentPresign(doc.getPfFile());
			String idDocument = null;

			if (!docsPreSign.isEmpty()) {
				// If there is another document with the same file, reuse the
				// idTransaction
				idDocument = ((PfDocumentsDTO) docsPreSign.get(0))
						.getCpresign();
			} else {
				
				try {
					custodyService = custodyServiceFactory
							.createCustodyServiceOutput(doc.getPfFile()
									.getCtype());
					custodyDocument = new CustodyServiceOutputDocument();
					custodyDocument.setIdentifier(doc.getChash());
					custodyDocument.setUri(doc.getPfFile().getCuri());
					custodyDocument.setIdEni(doc.getPfFile().getIdEni());
					custodyDocument.setRefNasDir3(doc.getPfFile().getRefNasDir3());
					byte[] bytes = custodyService.downloadFile(custodyDocument);
					long idCurrentConf = doc.getPfRequest().getPfApplication()
							.getPfConfiguration().getPrimaryKey();

					if (idConf != idCurrentConf) {
						idConf = idCurrentConf;
						conf = signBO.loadSignProperties(idConf);

						signer = PfSignFactory.createPfSign(conf, "afirma5",
									conf.getString(AuthenticatorConstants.AUTHENTICATOR_AFIRMA5_TRUSTEDSTORE_PASS),
									conf.getString(AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_USER),
									conf.getString(AuthenticatorConstants.AUTHENTICATOR_SERVER_HTTP_PASS));
					}

					SignDocument signDocument = new SignDocument();
					signDocument.setName(doc.getDname());
					signDocument.setMimeType(doc.getDmime());
					signDocument.setContent(bytes);
					idDocument = signer.registerDocument(signDocument);
					doc.setCpresign(idDocument);
					baseDAO.insertOrUpdateCommit(doc);
				} catch (CustodyServiceException e) {
					log.error("Error downloading the file " + doc.getDname()
							+ ", " + e.getMessage());
					// throw new JobExecutionException(e);
				} catch (SignerException e) {
					log.error("Error registering the document: "
							+ e.getMessage());
					// throw new JobExecutionException(e);
				}
			}

		}

		log.info("execute PreSignJob end");
	}

	public void handleAsynchronousException(Exception exception) {
		log.error(exception.getMessage());
	}

}
