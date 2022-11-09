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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputReport;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceInput;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class FilePathCustodyServiceInputImpl implements CustodyServiceInput {
	private static final long serialVersionUID = 1L;

	public static final String C_PARAMETER_FILEPATH_PATH = "RUTA";

	private Logger log = Logger.getLogger(FilePathCustodyServiceInputImpl.class);

	private String path;
	private String dayOfMonthStr = "";
	private String monthOfYearStr = "";
	private String yearStr = "";
	private Map<String, String> signExtensions;	

	public void initialize(Map<String, Object> parameterMap)
			throws CustodyServiceException {
		this.path = (String) parameterMap.get(C_PARAMETER_FILEPATH_PATH);
		if (path == null || path.equals("")) {
			throw new CustodyServiceException(
					"No path to store files defined. Check configuration parameters");
		}		
		signExtensions = Util.getInstance().loadSignExtensions();
		
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		dayOfMonthStr = String.valueOf(dayOfMonth);
		int month = cal.get(Calendar.MONTH)+1;
		monthOfYearStr = String.valueOf(month);
		int year = cal.get(Calendar.YEAR);
		yearStr = String.valueOf(year);
	}

	public String uploadFile(CustodyServiceInputDocument document,
			InputStream input) throws CustodyServiceException {
		log.info("uploadFile init");
		String filePath ="";
		String filePathDirectory ="";
		// Get file name
		String fileName = Util.getInstance().getNameFile(document.getName());
		// Replace fileName with hash code
		fileName = Util.getInstance().replaceFileNameWithHash(fileName,
				document.getCheckHash());
		String separator = path.substring(path.length() - 1).equals(
				File.separatorChar + "") ? "" : File.separatorChar + "";
		//Agustin--> concat es mucho mas optimo que el operador + en los String
		//filePath = path + separator + document.getRefNasDir3() + separator + dayOfYearStr + separator + fileName;
		filePath = filePath.concat(path);
		filePath = filePath.concat(separator);
		filePath = filePath.concat(document.getRefNasDir3());
		filePath = filePath.concat(separator);
		filePath = filePath.concat(yearStr);
		filePath = filePath.concat("_");
		filePath = filePath.concat(monthOfYearStr);
		filePath = filePath.concat("_");
		filePath = filePath.concat(dayOfMonthStr);
		filePathDirectory = filePath;
		filePath = filePath.concat(separator);
		filePath = filePath.concat(fileName);
		uploadFile(filePath, filePathDirectory, input);
		log.info("uploadFile end");
		return filePath;
	}

	public String uploadSign(CustodyServiceInputSign sign, InputStream input)
			throws CustodyServiceException {
		log.info("uploadSign init");
		String filePath = "";
		String filePathDirectory ="";
		String separator = path.substring(path.length() - 1).equals(
				File.separatorChar + "") ? "" : File.separatorChar + "";
		// Set file path
		//Agustin--> concat es mucho mas optimo que el operador + en los String
		//String filePath = path + separator + sign.getRefNasDir3() + separator + dayOfYearStr + separator + sign.getTransaction();
		filePath = filePath.concat(path);
		filePath = filePath.concat(separator);
		filePath = filePath.concat(sign.getRefNasDir3());
		filePath = filePath.concat(separator);
		filePath = filePath.concat(yearStr);
		filePath = filePath.concat("_");
		filePath = filePath.concat(monthOfYearStr);
		filePath = filePath.concat("_");
		filePath = filePath.concat(dayOfMonthStr);
		filePathDirectory = filePath;
		filePath = filePath.concat(separator);
		filePath = filePath.concat(sign.getTransaction());
		// Set sign extension
		//Agustin--> concat es mucho mas optimo que el operador + en los String
		//filePath = filePath + "." + signExtensions.get(sign.getFormat());
		filePath = filePath.concat(".");
		filePath = filePath.concat(signExtensions.get(sign.getFormat()));
		uploadFile(filePath, filePathDirectory, input);
		log.info("uploadFile end");
		return filePath;
	}
	
	public String uploadReport(CustodyServiceInputReport report, InputStream input)
	throws CustodyServiceException {
		log.info("uploadReport init");
		//TODO en caso de tener custodia en sistema de ficheros.
		log.info("uploadFile end");
		return null;
	}

	private void uploadFile(String filePath, String fileDirectory, InputStream input)
			throws CustodyServiceException {
		try {			
			
			//Agustin--> incluir multientidad, si no existen los directorios los creo
			File directory = new File(fileDirectory);
			if (!directory.exists()) {
				if(!directory.mkdirs())
					throw new CustodyServiceException("Error al crear el directorio necesario para subir el archivo" + filePath);					
			}		
			
			OutputStream outputStream = new FileOutputStream(filePath);
			int read;
			byte[] buffer = new byte[Constants.BUFFER_SIZE];
			while ((read = input.read(buffer)) > 0) {
				outputStream.write(buffer, 0, read);
			}
			input.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			throw new CustodyServiceException(e.getMessage());
		} catch (IOException e) {
			throw new CustodyServiceException(e.getMessage());
		}
	}
	
	public String uploadNormalizedReport(CustodyServiceInputReport report, InputStream input)
	throws CustodyServiceException {
		log.info("uploadNormalizedReport init");
		//TODO en caso de tener custodia en sistema de ficheros.
		log.info("uploadFile end");
		return null;
	}
}
