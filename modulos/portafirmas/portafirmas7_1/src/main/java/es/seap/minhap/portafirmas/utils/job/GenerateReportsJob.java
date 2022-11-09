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

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.SignBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;

/**
 * Clase que define el servicio quartz que genera los informes de firma de forma estática
 * para todas las firmas que se han generado en el día de ejecución del servicio
 * @author hugo
 *
 */
public class GenerateReportsJob implements Job {

	private static final Logger log = Logger.getLogger(GenerateReportsJob.class);

	// Inyecto el contexto de la aplicación para acceder a los beans
	/*private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}*/
	
	@Autowired
	private ApplicationVO applicationVO;
	
	@Autowired 
	private SignBO signBO;
	
	@Autowired
	private ApplicationBO applicationBO;

	//@Autowired
	//ReportBO reportBO;
	@Autowired 
	private BinaryDocumentsBO binaryDocumentsBO;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debug("EJECUTANDO la tarea de GENERACIÓN DE INFORMES DE FIRMA");

		//ApplicationVO applicationVO = applicationContext.getBean(ApplicationVO.class);
		
		// Necesario para que se injecten los Autowired
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);


		// Se generan los informes si está activa la generación de CSVs y de informes de firma
		if (applicationVO.getCsvActivated() && applicationVO.getReportActivated()) {
			//SignBO signBO = applicationContext.getBean(SignBO.class);
			//ReportBO reportBO = applicationContext.getBean(ReportBO.class);

			// Recupero las firmas hechas en el día
			List<AbstractBaseDTO> signs = signBO.getSignsFromDate(new Date(System.currentTimeMillis()));
//			List<AbstractBaseDTO> signs = signBO.getSignsFromDate(new GregorianCalendar(2013, Calendar.FEBRUARY, 15).getTime());
			
			for (AbstractBaseDTO signDTO : signs) {
				PfSignsDTO sign = (PfSignsDTO) signDTO;
				try {
					// Se genera el informe de firma y se guarda en base de datos si no existiera
					//reportBO.procesarFirma(sign, true);**
					binaryDocumentsBO.getReportBySignDTO(sign, applicationBO.queryApplicationPfirma());
				} catch (Throwable t) {
					log.error("Error procesando el fichero para obtener el informe de la firma " + sign.getPrimaryKeyString());
					throw new JobExecutionException(t.getMessage());
				}
			}
		}
	}

}
