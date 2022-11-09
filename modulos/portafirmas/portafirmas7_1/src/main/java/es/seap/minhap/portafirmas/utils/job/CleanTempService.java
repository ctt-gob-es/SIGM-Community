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

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;

public class CleanTempService implements Job {

	private static final Logger log = Logger.getLogger(CleanTempService.class);
	/**
	 * Ejecuta un job que borra de la carpeta temporal del sistema, todos los archivos del tipo TEMP_FILE_PREFIX
	 * que fueron creados antes de ayer y los que son del tipo de subida temporal, se sabe por el sufijo .upload
	 */
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("execute CleanTempServiceJob init");
		// get temp dir
		String tempDirPath = Constants.PATH_TEMP;
		File tempDir = new File(tempDirPath);
		// get temp files
		String[] tempFiles = tempDir.list();
		if (tempFiles != null) {
			for (int i = 0; i < tempFiles.length; i++) {
				// check that temp file belongs to pfirma
				if (tempFiles[i] != null
						&& tempFiles[i].indexOf(Constants.TEMP_FILE_PREFIX) != -1) {
					// obtain creation date from file name
					Date creationDate = Util.getInstance()
							.extractDateFromFileName(tempFiles[i]);
					// if file was created before yesterday just delete it
					if (creationDate.before(Util.getInstance().getYesterday())) {
						deleteFile(tempFiles[i]);
					}
				}
				// Or are from upload component
				else if (tempFiles[i] != null && isUploadTempFile(tempFiles[i])) {
					deleteFile(tempDirPath + tempFiles[i]);
				}
			}

		} else {
			log.info("Temporal directory is empty");
		}
		log.info("execute CleanTempServiceJob end");
	}
	/**
	 * Informa si un archivo es del tipo de subida temporal, se sabe por el sufijo .upload
	 * @param tempFileName el nombre del archivo
	 * @return si es o no un archivo de subida temporal
	 */
	private static boolean isUploadTempFile(String tempFileName) {
		boolean isTemp = false;
		String fileExtension = "";
		if (tempFileName.lastIndexOf(".") != -1) {
			fileExtension = tempFileName.substring(tempFileName
					.lastIndexOf("."), tempFileName.length());
		}
		if (fileExtension != null && fileExtension.equals(".data")) {
			isTemp = true;
		}
		return isTemp;
	}
	/**
	 * Borra un archivo si existe
	 * @param tempFile el archivo que se debe borrar
	 */
	private void deleteFile(String tempFile) {
		File fileToDelete = new File(tempFile);
		if (fileToDelete.exists()) {
			fileToDelete.delete();
		}
	}
	/**
	 * Muestra en el log, con traza de error, el mensaje de la excepci&oacute;n pasada como par&aacute;metro
	 * @param exception 
	 */
	public void handleAsynchronousException(Exception exception) {
		log.error(exception.getMessage());
	}

}
