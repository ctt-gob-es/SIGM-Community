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

package es.seap.minhap.portafirmas.business.administration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.ws.QueryServiceBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;


@Service
public class UpdateRequestAdmBO {
	
	private Logger log = Logger.getLogger(UpdateRequestAdmBO.class);
	
	@Autowired
	private QueryServiceBO queryServiceBO;
	
	@Autowired
	private NoticeBO noticeBO;
	
	/**
	 * Crea un fichero en el servidor con el contenido del File f.
	 * Devuelve el nombre del fichero.
	 * @param f
	 * @return
	 */
	public void createFileInServer (String name, File f) {		
		try {

			FileInputStream fis = new FileInputStream (f);
						
			byte[] bytes = getBytes(fis);
			
			FileOutputStream fout = new FileOutputStream (Constants.PATH_TEMP + name);
			fout.write(bytes);
			fout.close();
			
		} catch (Exception e) {
			log.error("No se puede crear el fichero en el servidor", e);
			throw new RuntimeException ("No se puede crear el fichero en el servidor", e);
		}
		
		
	}
	
	/**
	 * Procesa una lista de ficheros
	 * @param fileList
	 */
	public void processFileList (List<String> fileList) {
		
		List<String> exitosas = new ArrayList<String> ();
		List<String> fallidas = new ArrayList<String> ();
		for (String file : fileList) {
			processFile(file, exitosas, fallidas);
		}
		
		escribirResultados (exitosas, fallidas);
		
		
	}

	/**
	 * Lee el fichero de entrada, obtiene los hashes de las peticiones y envía los avisos a las aplicaciones.
	 * Las peticiones que hayan tenido éxito se guardarán en la lista "exitosas", en la lista "fallidas" las que no.
	 * @param fileName
	 */
	public void processFile (String fileName, List<String> exitosas, List<String> fallidas) {
		
		List<String> hashes = obtenerHashesPeticiones (Constants.PATH_TEMP + fileName);
		
		
		for (String hash : hashes) {
			boolean exito =updateRequest (hash);
			if (exito) {
				exitosas.add(hash);
			} else {
				fallidas.add(hash);
			}
		}
		
	}
	
	/**
	 * Escribe en dos ficheros el contenido de las dos listas, cada elemento de cada lista se escribe en una línea.
	 * @param exitosas
	 * @param fallidas
	 */
	private void escribirResultados (List<String> exitosas, List<String> fallidas) {
		try {
			createFileByContent ("exitosas.txt", exitosas);
		} catch (IOException e) {
			log.error("No ha podido crearse el fichero de peticiones con éxito");
		}
		
		try {
			createFileByContent ("fallidas.txt", fallidas);
		} catch (IOException e) {
			log.error("No ha podido crearse el fichero de peticiones fallidas");
		}
	}
	
	/**
	 * Crea un fichero de nombre "name". En cada línea escribe cada uno de los elementos de "content"
	 * @param name
	 * @param content
	 * @throws IOException
	 */
	private void createFileByContent (String name, List<String> content) throws IOException {
		String path = Constants.PATH_TEMP + name;
		String ln = System.getProperty("line.separator");
		
		PrintWriter pw = new PrintWriter (new FileOutputStream (path));
		
		for (String cadena : content) {
			pw.write(cadena + ln);
		}
		pw.flush();
		pw.close();
	}
	
	/**
	 * Envía la notificación a la aplicación correspondiente de una petición con un hash que se le pasa por parámetro.
	 * Devuleve true si ha habido éxito, false en caso contrario.
	 * @param hash
	 */
	private boolean updateRequest (String hash) {
		boolean result = true;
		try {			
			PfRequestsDTO req = queryServiceBO.checkRequest(hash);
			
			List<AbstractBaseDTO> peticiones = new ArrayList<AbstractBaseDTO> ();
			peticiones.add(req);
			if (req != null) {
				noticeBO.sendAdviceToAppServerViaFile(peticiones);
			}		
			
		} catch (PfirmaException e) {
			result = false;
			log.error ("No se ha podido recuperar la peticion " + hash);			
		} catch (NoticeException e) {
			result = false;
			log.error ("No se ha podido enviar el mensaje de la peticion " + hash);			
		} catch (Exception e) {
			result = false;
			log.error ("Se ha producido un error con la petición " + hash);
		}
		
		return result;
	}
	
	/**
	 * Obtiene los hashes de las peticiones de un fichero.
	 * En cada línea hay un hash
	 * @param filePath
	 * @return
	 */
	private List<String> obtenerHashesPeticiones (String filePath) {
		File archivo = new File (filePath);
		List<String> peticiones = new ArrayList<String> ();
		FileReader fr = null;
		BufferedReader br = null;
		try  {
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			
			String linea = br.readLine();
			while (linea != null && !linea.equalsIgnoreCase("")) {
				String hashPeticion = linea;
				peticiones.add(hashPeticion);
				linea = br.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException ("No se puede leer el fichero " + filePath, e);
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					log.error("ERROR: UpdateRequestAdmBO.obtenerHashesPeticiones, ", e);
				}
			}
			if(fr != null){
				try {
					fr.close();
				}  catch (IOException e) {
					log.error("ERROR: UpdateRequestAdmBO.obtenerHashesPeticiones, ", e);
				}
			}
		}
		
		return peticiones;
	}
	
	/**
	 * Devuelve los bytes leídos de un InputStream
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
          size = is.available();
          buf = new byte[size];
          len = is.read(buf, 0, size);
        } else {
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          buf = new byte[size];
          while ((len = is.read(buf, 0, size)) != -1)
            bos.write(buf, 0, len);
          buf = bos.toByteArray();
        }
        return buf;
    }

}
