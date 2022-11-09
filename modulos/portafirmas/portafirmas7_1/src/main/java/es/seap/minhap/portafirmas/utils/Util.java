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

package es.seap.minhap.portafirmas.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;

import es.gob.aapp.csvbroker.webservices.querydocument.v1.CSVQueryDocumentException;
import es.gob.afirma.core.AOException;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfFilesDTO;
import es.seap.minhap.portafirmas.domain.PfFiltersDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUserTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.envelope.GroupEnvelope;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;
import es.seap.minhap.portafirmas.utils.firma.CVSSignature;
import es.seap.minhap.portafirmas.utils.firma.SignatureProcessor;



public class Util {

	private static Util instance;

	private Logger log = Logger.getLogger(Util.class);
	
	public static final String DEFAULT_FORMAT = "dd/MM/yyyy";

	public static final String HOUR_FORMAT = "HH:mm:ss";

	public static final String EXTENDED_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

//	/**
//	 * a&ntilde;ade a un arbol de richfaces los nodos contenidos en un properties
//	 * @param path la ruta del nodo en el archivo properties, si es nulo no se tiene en cuenta
//	 * @param node el arbol de richfaces al que le vamos a a&ntilde;adir los nodos
//	 * @param properties archivo de propiedas que contiene los nombres de los nodos que se van a a&ntilde;adir
//	 */
//	public void addNodes(String path, TreeNode<String> node,
//			Properties properties) {
//		boolean end = false;
//		int counter = 1;
//		TreeNodeImpl<String> nodeImpl = null;
//		while (!end) {
//			String key = (path == null ? String.valueOf(counter) : path + '.'
//					+ counter);
//
//			String value = properties.getProperty(key);
//			if (value == null) {
//				end = true;
//			} else {
//				nodeImpl = new TreeNodeImpl<String>();
//				nodeImpl.setData(value);
//				node.addChild(Integer.valueOf(counter), nodeImpl);
//				addNodes(key, nodeImpl, properties);
//				counter++;
//
//			}
//		}
//	}
//	/**
//	 * Genera un arbol de richfaces con las aplicaciones asociadas a un usuario a partir
//	 * de una lista de las mismas
//	 * @param applications lista de aplicaciones asociadas a un usuario que se 
//	 * van a a&ntilde;adir al arbol
//	 * @param allString cadena que define el nodo padre
//	 * @return el arbol de richfaces con las aplicaciones asociadas a un usuario
//	 */
//	public TreeNode<String> loadApplicationsTree(
//			List<AbstractBaseDTO> applications, String allString) {
//		TreeNode<String> rootNode = null;
//		try {
//			Properties properties = new Properties();
//			Map<String, String> map = new HashMap<String, String>();
//			properties.put("1", allString);
//
//			int counter = 0;
//			//recorremos la lista y generamos el properties las claves asociadas a los niveles del arbol
//			for (AbstractBaseDTO pfApplicationDTO : applications) {
//				PfApplicationsDTO appDTO = (PfApplicationsDTO) pfApplicationDTO;
//				{
//					Integer level = 0;
//					List<Object[]> mapLevel = new ArrayList<Object[]>();
//					String key = null;
//					while (appDTO != null && key == null) {
//						//si no tiene aplicaci&oacute;n se a&ntilde;ade un c&oacute;digo generado al mapa
//						if (appDTO.getPfApplication() == null) {
//							key = "1." + ++counter;
//							properties.put(key, appDTO.getCapplication());
//							map.put(appDTO.getCapplication(), key);
//						//si tiene aplicaci&oacute;n se a&ntilde;ade el c&oacute;digo de la misma al mapa
//						} else {
//							key = map.get(appDTO.getPfApplication()
//									.getCapplication());
//							if (key != null) {
//								int counterAux = 0;
//								String keyAux = key + "." + ++counterAux;
//								while (properties.getProperty(keyAux) != null) {
//									keyAux = key + "." + ++counterAux;
//								}
//								properties
//										.put(keyAux, appDTO.getCapplication());
//								map.put(appDTO.getCapplication(), keyAux);
//								key = keyAux;
//
//							} else {
//								mapLevel.add(new Object[] { ++level,
//										appDTO.getCapplication() });
//							}
//						}
//						appDTO = appDTO.getPfApplication();
//
//					}
//					if (!mapLevel.isEmpty()) {
//						Iterator<Object[]> it = mapLevel.iterator();
//						while (it.hasNext()) {
//							Object[] object = it.next();
//							Integer levelAux = (Integer) object[0];
//							String appAux = (String) object[1];
//							String keyAux = key;
//							for (int i = level; i >= levelAux; i--) {
//								keyAux = keyAux + ".1";
//							}
//
//							properties.put(keyAux, appAux);
//							map.put(appAux, keyAux);
//						}
//					}
//				}
//			}
//			//creamos el nodo padre
//			rootNode = new TreeNodeImpl<String>();
//			//a&ntilde;adimos los nodos al arbol
//			addNodes(null, rootNode, properties);
//			//retornamos el arbol
//			return rootNode;
//
//		} catch (Exception e) {
//			throw new FacesException(e.getMessage(), e);
//		}
//	}
//
//	public void loadApplicationsParentTree(PfApplicationsDTO appDTO,
//			String parents, List<String> listKeys, List<String> listValues,
//			Map<String, String> map) {
//		int counter = 0;
//
//		if (appDTO.getPfApplication() != null) {
//			String key = parents + "." + ++counter;
//			loadApplicationsParentTree(appDTO.getPfApplication(), key,
//					listKeys, listValues, map);
//		}
//		listKeys.add(parents);
//		listValues.add(appDTO.getCapplication());
//
//	}
//	/**
//	 * Devuelve el arbol de las aplicaciones que pasamos como listado.
//	 * @param applications la lista de aplicaciones 
//	 * @param all el nodo padre
//	 * @return el arbol de las aplicaciones que pasamos como listado.
//	 */
//	public TreeNode<String> loadApplicationsAdmTree(
//			List<AbstractBaseDTO> applications, String all) {
//		TreeNode<String> rootNode = null;
//		try {
//			Properties properties = new Properties();
//			properties.put("1", all);
//
//			int counter = 0;
//			for (AbstractBaseDTO pfApplicationDTO : applications) {
//				PfApplicationsDTO appDTO = (PfApplicationsDTO) pfApplicationDTO;
//
//				if (appDTO.getPfApplication() == null) {
//					properties.put("1." + ++counter, appDTO.getCapplication());
//					if (appDTO.getPfApplications() != null
//							&& !appDTO.getPfApplications().isEmpty()) {
//						loadApplicationsChildTree(appDTO, properties, "1."
//								+ counter);
//					}
//				}
//			}
//
//			rootNode = new TreeNodeImpl<String>();
//			addNodes(null, rootNode, properties);
//
//			return rootNode;
//
//		} catch (Exception e) {
//			throw new FacesException(e.getMessage(), e);
//		}
//	}
	/**
	 * Obtiene una instancia del objeto actual.
	 * @return la instancia del objeto actual.
	 */
	public static synchronized Util getInstance() {
		if (instance == null) {
			instance = new Util();
		}
		return instance;

	}

	/**
	 * Cada palabra separada por un espacio en blanco en la cadena se convierte la primera
	 * letra de la misma a may&uacute;scula y el resto en min&uacute;scula 
	 *  capitalizeString(null)        = null
	 *  capitalizeString("")          = ""
	 *  capitalizeString("i am FINE") = "I Am Fine"
	 * @param str cadena a convertir
	 * @return la cadena cadena convertida
	 * @see WordUtils#capitalizeFully(String)
	 */
	public String capitalizeString(String str) {
		return WordUtils.capitalizeFully(str);
	}
	
	public String capitalizeWithSpaces(String str) {
		return WordUtils.capitalizeFully(str, new char[] {' '});
	}
	
	/**
	 * @param objeto
	 * @return verdadero si el objeto es null o vacio
	 */
	public static boolean esVacioONulo(Object objeto){
		boolean retorno = false;
		if(objeto == null){
			retorno = true;
		} else {
			if(objeto instanceof String){
				String campo = (String) objeto;
				if(campo.trim().equals("")){
					retorno = true;
				}
			}
			else if (objeto instanceof ArrayList){
				ArrayList<?> campo = (ArrayList<?>) objeto;
				if(campo.isEmpty()){
					retorno = true;
				}
			}
		}
		return retorno;
	}

	/**
	 * @param objeto
	 * @return vacio si es nulo o lo mismo si no
	 */
	public static String vacioSiNulo(String objeto) {
		String retorno = "";
		if(objeto != null){
			retorno = (String) objeto ;
		}
		return retorno;
	}
	/**
	 * Recupera el nombre completo del usuario, nombre y apellidos
	 * @param userDTO el usuario
	 * @return el nombre completo del usuario con los apellidos
	 */
	public String completeUserName(PfUsersDTO userDTO) {

		String ret = nombreCompleto (userDTO.getDname(), userDTO.getDsurname1(), userDTO.getDsurname2());
		return ret;
		
	}

	
	public String completeUserName (UserEnvelope user) {
		String ret = nombreCompleto (user.getDname(), user.getDapell1(), user.getDapell2());
		return ret;
		
			
	}
	
	/**
	 * Recupera el nombre completo del usuario, nombre, apellidos y sede
	 * @param userDTO el usuario
	 * @return el nombre completo del usuario con los apellidos y su sede
	 */
	public String completeUserNameWithProvince(PfUsersDTO userDTO) {

		String ret = nombreCompleto (userDTO.getDname(), userDTO.getDsurname1(), userDTO.getDsurname2());
	
		if (userDTO.getPfProvince() != null) {
			String seatFull = capitalizeWithSpaces(userDTO.getPfProvince().getCnombre());
			ret = "[" + seatFull.trim() + "] " + ret;
		}
		
		return ret;
		
	}
	
	public String completeUserNameWithProvince (UserEnvelope user) {
		String ret = nombreCompleto (user.getDname(), user.getDapell1(), user.getDapell2());
		
		if (user.getNombreProvincia() != null) {
			String seatFull = capitalizeWithSpaces(user.getNombreProvincia());
			ret = "[" + seatFull.trim() + "] " + ret;
		}
		
		return ret;
		
	}
	
	public String nombreCompleto (String name, String surname1, String surname2) {
		String ret = "";
		
		String nameFull = capitalizeWithSpaces(name);
		if (nameFull != null) {
			ret += nameFull;
		}
		String surname1Full = capitalizeWithSpaces(surname1);
		if (surname1Full != null) {
			ret += " " + surname1Full;
		}
		String surname2Full = capitalizeWithSpaces(surname2);
		if (surname2Full != null) {
			ret += " " + surname2Full;
		}
		
		ret = ret.trim();
		
		return ret;
	}

	/**
	 * Crea un c&oacute;digo hash
	 * @return el c&oacute;digo hash creado
	 */
	public String createHash() {
		Random rn = new Random();
		String chash = "";
		int j = 0;
		for (int i = 0; i < Constants.C_HASH_DEFAULTLENGTH; i++) {
			j = rn.nextInt();
			if (j < 0) {
				j = j * -1;
			}
			chash += Constants.C_HASH_ALFANUMERICS[j
					% Constants.C_HASH_ALFANUMERICS.length];
		}
		return chash;
	}
	
	public String createHash(byte[] bytes) {

		String hashStr = "";

		try {
			MessageDigest md5;
			md5 = MessageDigest.getInstance("SHA1");

			md5.update(bytes);
			byte[] hash = md5.digest();
			for (int x = 0; x < hash.length; x++) {
				hashStr += Integer.toHexString(hash[x] & 0xff);
			}
		} catch (NoSuchAlgorithmException ex) {
			log.debug("Error al codificar el String :" + ex);
		}
		return hashStr;
	}

	public String createHash(InputStream input) {
		String hashStr = "";

		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA1");

			DigestInputStream digestInputStream = new DigestInputStream(input, md5);
			File file = new File(Constants.PATH_TEMP + "tmp" + System.currentTimeMillis());
			FileOutputStream fileOut = new FileOutputStream(file);
			OutputStream streamAux = new DigestOutputStream(fileOut, md5);
			IOUtils.copy(input, streamAux);

			md5 = digestInputStream.getMessageDigest();

			byte[] hash = md5.digest();
			hashStr = new String(Hex.encodeHex(hash));

			input.close();
			try {
				input.reset(); //Lo dejamos en la posición inicial para que se pueda volver a leer
			} catch (Exception e) {
				// Algunos objetos no soportan reset por eso capturamos el error y continuamos
				//e.printStackTrace();
			}
			file.delete();
		} catch (NoSuchAlgorithmException ex) {
			log.error("createHash error", ex);
		} catch (FileNotFoundException e) {
			log.error("createHash error", e);
		} catch (IOException e) {
			log.error("createHash error", e);
		}
		// log.info("end createHash");
		return hashStr;
	}

	public String truncatePathFile(String nameFile) {
		String result = "";

		String systemSeparator = System.getProperty("file.separator");

		if (nameFile.indexOf(nameFile) > -1) {
			result = nameFile
					.substring(nameFile.lastIndexOf(systemSeparator) + 1);
		}
		return result;
	}

	/**
	 * Elimina del nombre del documento la ruta del mismo
	 * @param nameFile nombre del documento
	 * @return el nombre del documento con la ruta truncada
	 */
	public String getNameFile(String nameFile) {

		String result = "";

		result = truncatePathFile(nameFile);

		return result;
	}

	public String replaceFileNameWithHash(String fileName, String hashId) {
		// Get file name
		String name = getNameFile(fileName);
		// Discard file extension
		if (hasExtension(name)) {
			name = name.substring(0, name.lastIndexOf("."));
		}

		fileName = getNameFile(fileName).replace(name, hashId);
		return fileName;
	}

	public String appendHashToFileName(String fileName, String hash) {
		String returnString = "";
		if (hasExtension(fileName)) {
			// Get file extension
			String ext = fileName.substring(fileName.lastIndexOf('.'), fileName
					.length());
			// Discard file extension
			returnString = fileName.replaceAll(ext, "");
			// Append hash and file extension
			returnString = returnString + "_" + hash + ext;
		} else {
			returnString = returnString + hash;
		}
		return returnString;
	}

	/**
	 * Obtiene la fecha de creaci&oacute;n de un archivo del tipo TEMP_FILE_PREFIX
	 * @param fileName el nombre del archivo, este nombre contiene la fecha de creaci&oacute;n
	 * @return la fecha de creaci&oacute;n contenida en el nombre del archivo
	 * @see es.seap.minhap.portafirmas.utils.Constants#TEMP_FILE_PREFIX
	 */
	public Date extractDateFromFileName(String fileName) {
		String milis = fileName.substring(fileName.indexOf("_") + 1, fileName
				.lastIndexOf("_"));
		return new Date(Long.parseLong(milis));

	}

	/**
	 * Obtiene el tipo de documento 'MIME' del nombre del archivo
	 * @param nameFile nombre del archivo
	 * @return el tipo 'MIME' del fichero
	 * @see es.seap.minhap.portafirmas.utils.MimeType#mimeTypeOf(String)
	 */
	public String getMimeTypeOf(String nameFile) {
		return MimeType.getInstance().mimeTypeOf(nameFile);
	}
	
	/**
	 * Obtiene la extension del nombre del archivo
	 * @param nameFile nombre del archivo
	 * @return la extension del fichero
	 */
	public String getExtensionOf(String nameFile) {
		return MimeType.getInstance().extensionOf(nameFile);
	}

	/**
	 * Obtiene el tipo de documento 'MIME' a partir del archivo
	 * @param file archivo
	 * @return el tipo 'MIME' del fichero
	 */

	public String getMime (byte[] datos) throws Exception{
		//Magic parser = new Magic() ;
		MagicMatch match;
		String mime = null;
		try {
			match = Magic.getMagicMatch(datos);
			mime = match.getMimeType();
		} catch (MagicParseException|MagicMatchNotFoundException|MagicException e) {
			throw new MagicParseException("No se ha podido obtener el Mime del documento ", e);
		}/* catch (MagicMatchNotFoundException e) {
			throw new MagicMatchNotFoundException("No se ha podido obtener el Mime del documento ", e);
		} catch (MagicException e) {
			throw new MagicException("No se ha podido obtener el Mime del documento ", e);
		}*/
		
		return mime;
		
	}

	/**
	 * Obtiene el tipo de documento 'MIME' a partir del archivo
	 * @param file archivo
	 * @return el tipo 'MIME' del fichero
	 */
	public String getMimeFile (File file) throws Exception{
		//Magic parser = new Magic() ;
		MagicMatch match;
		String mime = null;
			try {
				FileInputStream fis = new FileInputStream(file);
				match = Magic.getMagicMatch(getBytes(fis));
				mime = match.getMimeType();
			} catch (MagicParseException|MagicMatchNotFoundException|MagicException e) {
				throw new Exception ("No se ha podido obtener el Mime del documento ", e);
			}/* catch (MagicMatchNotFoundException e) {
				throw new Exception ("No se ha podido obtener el Mime del documento ", e);
			} catch (MagicException e) {
				throw new Exception ("No se ha podido obtener el Mime del documento ", e);
			}*/
		
		return mime;
		
	}

	/**
	 * Retorna si el nombre de un archivo tiene extensi&oacute;n o no
	 * @param nameFile nombre del archivo
	 * @return si tiene o no extensi&oacute;n
	 */
	public boolean hasExtension(String nameFile) {

		if (nameFile.lastIndexOf('.') > -1) {
			return true;
		} else {
			return false;
		}
	}

	public String tagsToString(Set<PfRequestTagsDTO> tagsDTO, PfUsersDTO userDTO) {
		// TODO: ORdenar las etiquetas pa que siempre salgan en el mismo orden

		String tags = "";
		for (Iterator<PfRequestTagsDTO> iterator = tagsDTO.iterator(); iterator
				.hasNext();) {
			PfRequestTagsDTO pfTagsDTO = iterator.next();
			if (pfTagsDTO.getPfUser().getPrimaryKey().compareTo(
					userDTO.getPrimaryKey()) == 0) {
				if (iterator.hasNext()) {
					tags += pfTagsDTO.getPfTag().getCtag() + ", ";
				} else {
					tags += pfTagsDTO.getPfTag().getCtag();
				}
			}
		}
		return tags;
	}

	public List<PfRequestTagsDTO> userRequestTagsToList(
			Set<PfRequestTagsDTO> tagsDTO, PfUsersDTO userDTO) {

		List<PfRequestTagsDTO> tagsState = new ArrayList<PfRequestTagsDTO>();
		List<PfRequestTagsDTO> tags = new ArrayList<PfRequestTagsDTO>();

		// TODO: ORdenar las etiquetas pa que siempre salgan en el mismo orden

		for (Iterator<PfRequestTagsDTO> iterator = tagsDTO.iterator(); iterator
				.hasNext();) {
			PfRequestTagsDTO pfRequestTagsDTO = iterator.next();

			if (pfRequestTagsDTO.getPfUser().getPrimaryKey().compareTo(
					userDTO.getPrimaryKey()) == 0
					&& pfRequestTagsDTO.getPfTag().getCtype().equals(
							Constants.C_TYPE_TAG_STATE)) {
				tagsState.add(pfRequestTagsDTO);
			}
		}
		tags.addAll(tagsState);
		return tags;
	}

	public List<PfUserTagsDTO> userTagsToList(Set<PfRequestTagsDTO> tagsDTO,
			PfUsersDTO userDTO) {

		List<PfUserTagsDTO> tagsUser = new ArrayList<PfUserTagsDTO>();
		List<PfUserTagsDTO> tags = new ArrayList<PfUserTagsDTO>();
		for (Iterator<PfRequestTagsDTO> iterator = tagsDTO.iterator(); iterator
				.hasNext();) {
			PfRequestTagsDTO pfRequestTagsDTO = iterator.next();
			if (pfRequestTagsDTO.getPfUser().getPrimaryKey().compareTo(
					userDTO.getPrimaryKey()) == 0
					&& pfRequestTagsDTO.getPfTag().getCtype().equals(
							Constants.C_TYPE_TAG_USER)) {
				// we need to find the userTag asociated
				for (Iterator<PfUserTagsDTO> iterator2 = userDTO
						.getPfTagsUsers().iterator(); iterator2.hasNext();) {
					PfUserTagsDTO userTagsDTO = iterator2.next();
					if (userTagsDTO.getPfTag().getCtag().equals(
							pfRequestTagsDTO.getPfTag().getCtag())) {
						tagsUser.add(userTagsDTO);
					}
				}
			}
		}
		tags.addAll(tagsUser);
		ListComparador myComparator = new ListComparador("ctag", 1);
		Collections.sort(tags, myComparator);
		return tags;
	}

	public List<PfCommentsDTO> getPfCommentsList(Set<PfCommentsDTO> set) {
		List<PfCommentsDTO> list = new ArrayList<PfCommentsDTO>();

		for (Iterator<PfCommentsDTO> iterator = set.iterator(); iterator
				.hasNext();) {
			PfCommentsDTO aux = iterator.next();
			list.add(aux);
		}

		ListComparador myComparator = new ListComparador(
				"orderAscCommentByDate", 1);
		Collections.sort(list, myComparator);
		return list;
	}

	public Calendar dateToCalendar(Date date) {
		Calendar calendar = null;
		if (date != null) {
			calendar = Calendar.getInstance();
			calendar.setTime(date);
		}
		return calendar;
	}
	
	/**
	 * @param fecha
	 * @return
	 */
	public Date stringToDate(String fecha) {
		return stringToDate(fecha, null);
	}

	/**
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public Date stringToDate(String fecha, String formato) {
		if (esVacioONulo(fecha)) {
			return null;
		}
		if (esVacioONulo(formato)) {
			formato = DEFAULT_FORMAT;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formato);
		try {
			return formatter.parse(fecha);
		} catch (ParseException e) {
			log.error("Error al formatear fecha " + fecha, e);
			return null;
		}
	}
	
	/**
	 * @param fecha
	 * @return
	 */
	public String dateToString(Date fecha) {
		return dateToString(fecha, null);
	}
	
	/**
	 * Crea un objeto de tipo Date a partir de fecha y hora de tipo String
	 * @param fecha
	 * @param hora
	 * @return
	 */
	public Date dateComplete(String fecha, String hora) {
		return stringToDate(fecha + ' ' + hora, Util.EXTENDED_DATE_FORMAT);
	}
	/**
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public String dateToString(Date fecha, String formato) {
		if (fecha == null) {
			return null;
		}
		if (esVacioONulo(formato)) {
			formato = DEFAULT_FORMAT;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formato);
		return formatter.format(fecha);
	}
	
	

	// AUX
//	private void loadApplicationsChildTree(PfApplicationsDTO appDTO,
//			Properties properties, String parents) {
//		int counter = 0;
//		Iterator<PfApplicationsDTO> itApp = appDTO.getPfApplications()
//				.iterator();
//		while (itApp.hasNext()) {
//			String parentsAux = parents + "." + ++counter;
//			PfApplicationsDTO appChild = itApp.next();
//			properties.put(parentsAux, appChild.getCapplication());
//			loadApplicationsChildTree(appChild, properties, parentsAux);
//		}
//	}

	public List<AbstractBaseDTO> filterList(List<AbstractBaseDTO> list,
			AbstractBaseDTO baseDTO) {
		List<AbstractBaseDTO> auxList = new ArrayList<AbstractBaseDTO>();
		for (Iterator<AbstractBaseDTO> iterator = auxList.iterator(); iterator
				.hasNext();) {
			AbstractBaseDTO aux = iterator.next();
			if (aux.getPrimaryKeyString().equals(baseDTO.getPrimaryKeyString())) {
				auxList.add(aux);
			}

		}
		return auxList;
	}

	/**
	 * Obtiene la posici&oacute;n del objeto en la lista
	 * @param list la lista 
	 * @param object el objeto
	 * @return la posici&oacute;n en la lista, el primer indice es 0, -1 si no est&aacute; en la lista
	 */
	public int getPositionInList(List<AbstractBaseDTO> list,
			AbstractBaseDTO object) {

		int index = -1;

		for (int i = 0; i < list.size(); i++) {
			if (object.getPrimaryKeyString().equals(
					list.get(i).getPrimaryKeyString())) {
				index = i;
			}
		}
		return index;
	}

	/**
	 * Obtiene el indice de una petici&oacute;n en un listado en pantalla, si no lo encuentra devuelve -1
	 * @param list lista de peticiones
	 * @param object la peticion seleccionada
	 * @param initPage el &iacute;ndice de inicio de la p&aacute;gina en la que nos encontramos
	 * @param endPage el indice de fin de la p&aacute;gina en la que no encontramos
	 * @return el indice de la petici&oacute;n en la p&aacute;gina que nos encontramos, si no lo encuentra devuelve -1
	 */
	public int getPositionInListRequest(List<AbstractBaseDTO> list,
			AbstractBaseDTO object, int initPage, int endPage) {

		int index = -1;

		for (int i = initPage; i < endPage && i < list.size(); i++) {
			if (object.getPrimaryKeyString().equals(
					list.get(i).getPrimaryKeyString())) {
				index = i;
			}
		}
		return index;
	}

	public List<AbstractBaseDTO> listMark(int pageSize, int pageActual,
			List<AbstractBaseDTO> requestList) {
		List<AbstractBaseDTO> resultList = new ArrayList<AbstractBaseDTO>();
		int initPage = (pageActual - 1) * pageSize;
		int endPage = initPage + pageSize;
		PfRequestTagsDTO requestTag = null;
		for (int i = initPage; i < endPage && i < requestList.size(); i++) {
			requestTag = (PfRequestTagsDTO) requestList.get(i);
			if (requestTag.getPfRequest().isSelected()) {
				resultList.add(requestTag);
			}
		}
		return resultList;
	}

	/**
	 * Comprueba si la fecha de inicio es anterior a la de fin
	 * @param fStart fecha de inicio
	 * @param fEnd fecha de fin
	 * @return true si la fecha de inicio es anterior, false en caso contrario
	 */
	public boolean checkDate(Date fStart, Date fEnd) {
		log.info("checkDate init");
		boolean error = true;
		if (fEnd != null && fStart != null && fEnd.before(fStart)) {
			error = false;
		}
		log.info("checkDate end");
		return error;
	}
	
	/**
	 * Comprueba si dos periodos de tiempo se solapan
	 * @param inicio1 Fecha de inicio del primer periodo
	 * @param fin1 Fecha de fin de primer periodo. Si es nula se considera que el periodo no tiene fin.
	 * @param inicio2 Fecha de inicio de segundo periodo
	 * @param fin2 Fecha de fin de segundo periodo
	 * @return true si se solapan, false en caso contrario.
	 */
	public boolean checkPeriodOverlap (Date inicio1, Date fin1, Date inicio2, Date fin2) {
		boolean overlap = true;
		
		if (inicio1 == null) {
			if (fin1 != null) {
				if (inicio2 != null && inicio2.after(fin1)) {
					overlap = false;
				}
			}
			
		} else {
			if (fin1 == null) {
				if (fin2 != null && fin2.before(inicio1)) {
					overlap = false;
				}
			} else {
				if ((fin2 != null && fin2.before(inicio1) ) ||
						(inicio2 != null && inicio2.after(fin1))) {
					overlap = false;
					
				}
					
			}
		}
		
		return overlap;
		
	}

	/**
	 * Devuelve un Date con la fecha del Date recibido y la hora indicada en los parámetros de entrada
	 */
	public Date getDateWithOtherTime (Date date, int hour, int minute, int second) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND,second);
		return c.getTime();
	}

	public static StringBuilder readFileAsString(String filePath)
			throws java.io.IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[Constants.BUFFER_SIZE];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[Constants.BUFFER_SIZE];
		}
		reader.close();
		return fileData;
	}

	public String removeSpacesPoints(String str) {
		String[] auxArray = str.split(" ");
		StringBuilder sb = new StringBuilder();
		String result = "";

		// remove spaces
		for (int i = 0; i < auxArray.length; i++) {
			sb.append(auxArray[i]);
		}
		result = sb.toString();
		
		//auxArray = result.split(".");

		// remove points
		while (result.indexOf('.') != -1) {
			result = result.substring(0, result.indexOf('.'))
					+ result.substring(result.indexOf('.') + 1);
		}
		return result;
	}

	public PfUsersDTO getUserOfJob(PfUsersDTO cargo) {
		PfUsersDTO user = null;
		if (cargo != null && cargo.getPfUsersJobs() != null
				&& !cargo.getPfUsersJobs().isEmpty()) {
			for (PfUsersJobDTO userJob : cargo.getPfUsersJobs()) {
				if (userJob != null && isValidJob(userJob)) {
					user = userJob.getPfUser();
				}
			}
		}

		return user;
	}

	
	public PfUsersDTO getUserValidJob(PfUsersDTO user) {
		PfUsersDTO validJob = null;
		if (user != null && user.getPfUsersJobs() != null
				&& !user.getPfUsersJobs().isEmpty()) {
			for (PfUsersJobDTO userJob : user.getPfUsersJobs()) {
				if (userJob != null && isValidJob(userJob)) {
					validJob = userJob.getPfUserJob();
				}
			}
		}

		return validJob;
	}

	/**
	 * Determina si el cargo es valido en el momento actual
	 * @param userJob
	 * @return
	 */
	public boolean isValidJob(PfUsersJobDTO userJob) {
		boolean valid = false;
		Date now = new Date();
		// El instante actual debe estar comprendido entre la fecha de inicio y 
		// y de fin, si la hubiese y el cargo debe ser válido.
		if (now.after(userJob.getFstart()) &&
			(userJob.getFend() == null || now.before(userJob.getFend())) && 
			userJob.getPfUserJob().getLvalid()) {
			valid = true;
		}
		return valid;
	}

	/**
	 * Devuelve el primer elemento seleccionado de la lista que pasamos como par&aacute;metro
	 * @param list
	 * @return el primer elemento de la lista que est&aacute; seleccionado, nulo si no hay ning&uacute;n
	 * elemento seleccionado en la lista
	 */
	public AbstractBaseDTO anySelected(List<AbstractBaseDTO> list) {
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator
				.hasNext();) {
			AbstractBaseDTO aux = (AbstractBaseDTO) iterator.next();
			if (aux.isSelected()) {
				return aux;
			}
		}
		return null;
	}

	/**
	 * Obtiene el maximo orden de la lista de filtros sum&aacute;ndole 1, si no encuentra orden
	 * devuelve cero
	 * @param list lista de filtros
	 * @return devuelve el maximo orden de los filtros de la lista + 1,si no encuentra orden
	 * devuelve cero
	 */
	public Long getMaxCorder(List<AbstractBaseDTO> list) {
		int max = 0;
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator
				.hasNext();) {
			PfFiltersDTO filter = (PfFiltersDTO) iterator.next();
			if (filter.getCorder().intValue() >= max) {
				max = filter.getCorder().intValue() + 1;
			}
		}
		return new Long(max);
	}

	public Long getDaysOfDifference(Date date1, Date date2) {
		Long time1 = date1.getTime();
		Long time2 = date2.getTime();

		Long days = (time1 - time2) / Constants.MILISEC_BY_DAY;
		return days;
	}

	/**
	 * 
	 * @return la fecha del d&iacute;a de ayer
	 */
	public Date getYesterday() {
		Date today = new Date();
		Date yesterday = new Date(today.getTime() - Constants.MILISEC_BY_DAY);
		return yesterday;
	}

	/**
	 * Hace una copia del filtro que pasamos como par&aacute;metro
	 * @param filterDTO el dt de filtro
	 * @return la copia del filtro que pasado como par&aacute;metro
	 */
	public PfFiltersDTO duplicateFilter(PfFiltersDTO filterDTO) {

		PfFiltersDTO copy = new PfFiltersDTO();

		copy.setCcreated(filterDTO.getCcreated());
		copy.setCfilter(filterDTO.getCfilter());
		copy.setCmodified(filterDTO.getCmodified());
		copy.setCorder(filterDTO.getCorder());
		copy.setFcreated(filterDTO.getFcreated());
		copy.setFend(filterDTO.getFend());
		copy.setFmodified(filterDTO.getFmodified());
		copy.setFstart(filterDTO.getFstart());
		copy.setLvalid(filterDTO.getLvalid());
		copy.setPfApplication(filterDTO.getPfApplication());
		copy.setPfAuthorizationFilters(filterDTO.getPfAuthorizationFilters());
		copy.setPfAuthorizationType(filterDTO.getPfAuthorizationType());
		copy.setPfUser(filterDTO.getPfUser());
		copy.setPrimaryKey(filterDTO.getPrimaryKey());
		copy.setSelected(filterDTO.isSelected());
		copy.setTreason(filterDTO.getTreason());
		copy.setTsubjectFilter(filterDTO.getTsubjectFilter());
		copy.setUpdated(filterDTO.isUpdated());

		return copy;
	}

	/**
	 * Hace una copia del usuario que pasamos como par&aacute;metro
	 * @param pfUser dto de usuario
	 * @return la copia del usuario pasado como par&aacute;metro
	 */
	public PfUsersDTO duplicateUser(PfUsersDTO pfUser) {

		PfUsersDTO copy = new PfUsersDTO();

		copy.setCanagram(pfUser.getCanagram());
		copy.setCcreated(pfUser.getCcreated());
		copy.setCidentifier(pfUser.getCidentifier());
		copy.setCmodified(pfUser.getCmodified());
		copy.setCtype(pfUser.getCtype());
		copy.setDname(pfUser.getDname());
		copy.setDsurname1(pfUser.getDsurname1());
		copy.setDsurname2(pfUser.getDsurname2());
		copy.setFcreated(pfUser.getFcreated());
		copy.setFmodified(pfUser.getFmodified());
		copy.setLvalid(pfUser.getLvalid());
		copy.setPfComments(pfUser.getPfComments());
		copy.setPfFilters(pfUser.getPfFilters());
		copy.setPfHistoricRequests(pfUser.getPfHistoricRequests());
		copy.setPfOthersUsers(pfUser.getPfOthersUsers());
		copy.setPfSigners(pfUser.getPfSigners());
		copy.setPfTagsUsers(pfUser.getPfTagsUsers());
		copy.setPfUsers(pfUser.getPfUsers());
		copy.setPfUsersApplications(pfUser.getPfUsersApplications());
		copy.setPfUsersAuthorizations(pfUser.getPfUsersAuthorizations());
		copy.setPfUsersEmails(pfUser.getPfUsersEmails());
		copy.setPfUsersJobs(pfUser.getPfUsersJobs());
		copy.setPfUsersMobiles(pfUser.getPfUsersMobiles());
		copy.setPfUsersParameters(pfUser.getPfUsersParameters());
		copy.setPfUsersProfiles(pfUser.getPfUsersProfiles());
		copy.setPfUsersProvinces(pfUser.getPfUsersProvinces());
		copy.setPfProvince(pfUser.getPfProvince());
		copy.setPfUsersRemitters(pfUser.getPfUsersRemitters());
		copy.setPrimaryKey(pfUser.getPrimaryKey());
		copy.setSelected(pfUser.isSelected());
		copy.setUpdated(pfUser.isUpdated());
		copy.setValidadores(pfUser.getValidadores());
		copy.setValidadorDe(pfUser.getValidadorDe());
		copy.setLvisible(pfUser.getLvisible());
		copy.setLshownotifwarning(pfUser.getLshownotifwarning());

		return copy;
	}

	public boolean isInList(List<AbstractBaseDTO> list, AbstractBaseDTO object) {
		boolean result = false;
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator
				.hasNext()
				&& !result;) {
			AbstractBaseDTO aux = (AbstractBaseDTO) iterator.next();
			if (aux.getPrimaryKey().equals(object.getPrimaryKey())) {
				result = true;
			}
		}
		return result;
	}

	public CustodyServiceInputDocument documentDTOToCustodyServiceInputDocument(
			PfDocumentsDTO documentDTO, PfFilesDTO fileDTO, long size) {
		CustodyServiceInputDocument document = new CustodyServiceInputDocument();
		document.setCheckAlg(fileDTO.getChashAlg());
		document.setCheckHash(fileDTO.getChash());
		document.setIdentifier(documentDTO.getChash());
		document.setMime(documentDTO.getDmime());
		document.setName(documentDTO.getDname());
		document.setRequestHash(documentDTO.getPfRequest().getChash());
		document.setType(documentDTO.getPfDocumentType().getCdocumentType());
		document.setSize(new BigDecimal(size));
		document.setRefNasDir3(fileDTO.getRefNasDir3());
		document.setIdEni(fileDTO.getIdEni());
		return document;
	}

	public CustodyServiceInputDocument docelDocumentToCustodyServiceInputDocument(String docName, PfFilesDTO fileDTO, long size) {
		CustodyServiceInputDocument document = new CustodyServiceInputDocument();
		document.setCheckAlg(fileDTO.getChashAlg());
		document.setCheckHash(fileDTO.getChash());
		document.setIdentifier(null);
		document.setMime(null);
		document.setName(docName);
		document.setRequestHash(null);
		document.setType(null);
		document.setSize(new BigDecimal(size));
		document.setRefNasDir3(fileDTO.getRefNasDir3());
		document.setIdEni(fileDTO.getIdEni());
		return document;

	}

	public CustodyServiceOutputDocument docelDocumentToCustodyServiceOutputDocument(PfFilesDTO fileDTO) {
		CustodyServiceOutputDocument document = new CustodyServiceOutputDocument();
		document.setIdentifier(fileDTO.getChash());
		document.setUri(fileDTO.getCuri());
		document.setRefNasDir3(fileDTO.getRefNasDir3());
		document.setIdEni(fileDTO.getIdEni());
		return document;

	}

	public CustodyServiceOutputSign docelDocumentSignedToCustodyServiceOutputSign(PfSignsDTO signDTO) {
		CustodyServiceOutputSign sign = new CustodyServiceOutputSign();
		sign.setIdentifier(signDTO.getPrimaryKeyString());
		sign.setType(signDTO.getCtype());
		sign.setUri(signDTO.getCuri());
		sign.setRefNasDir3(signDTO.getRefNASDir3Firma());
		sign.setIdEni(signDTO.getRefNASIdEniFirma());
		return sign;

	}
	
	/**
	 * Devuelve un mapa con clave el formato de firma del portafirmas y valor su extensi&oacute;n asociada
	 * @return el mapa con clave el formato de firma y valor su extensi&oacute;n asociada
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_PKCS7
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_EXTENSION_CMS
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_CMS
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_EXTENSION_CMS
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_XADES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_EXTENSION_XADES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_XADES_BES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_EXTENSION_XADES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_XADES_T
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_EXTENSION_XADES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_CADES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_EXTENSION_CADES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_PDF
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_EXTENSION_PDF
	 */
	public Map<String, String> loadSignExtensions() {
		Map<String, String> signFormats = new HashMap<String, String>();
		signFormats.put(Constants.SIGN_FORMAT_PKCS7,
				Constants.SIGN_EXTENSION_CMS);
		signFormats.put(Constants.SIGN_FORMAT_CMS,
				Constants.SIGN_EXTENSION_CMS);
		signFormats.put(Constants.SIGN_FORMAT_XADES,
				Constants.SIGN_EXTENSION_XADES);
		signFormats.put(Constants.SIGN_FORMAT_XADES_BES,
				Constants.SIGN_EXTENSION_XADES);
		signFormats.put(Constants.SIGN_FORMAT_XADES_T,
				Constants.SIGN_EXTENSION_XADES);
		signFormats.put(Constants.SIGN_FORMAT_CADES,
				Constants.SIGN_EXTENSION_CADES);
		signFormats.put(Constants.SIGN_FORMAT_PDF,
				Constants.SIGN_EXTENSION_PDF);
		signFormats.put(Constants.SIGN_FORMAT_PDF,
				Constants.SIGN_EXTENSION_PDF);
		return signFormats;
	}

	/**
	 * Devuelve un mapa con clave el formato de firma del portafirmas y valor el MIMETYPE asociado
	 * @return el mapa con clave el formato de firma y valor el MIMETYPE asociado
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_PKCS7
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_EXTENSION_CMS
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_CMS
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_MIMETYPE_PKCS7
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_XADES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_MIMETYPE_XML
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_XADES_BES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_XADES_T
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_CADES
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_MIMETYPE_PKCS7
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_FORMAT_PDF
	 * @see es.seap.minhap.portafirmas.utils.Constants#SIGN_MIMETYPE_PDF
	 */
	public Map<String, String> loadSignMime() {
		Map<String, String> signFormats = new HashMap<String, String>();
		signFormats.put(Constants.SIGN_FORMAT_PKCS7,
				Constants.SIGN_EXTENSION_CMS);
		signFormats.put(Constants.SIGN_FORMAT_CMS,
				Constants.SIGN_MIMETYPE_PKCS7);
		signFormats.put(Constants.SIGN_FORMAT_XADES,
				Constants.SIGN_MIMETYPE_XML);
		signFormats.put(Constants.SIGN_FORMAT_XADES_BES,
				Constants.SIGN_MIMETYPE_XML);
		signFormats.put(Constants.SIGN_FORMAT_XADES_T,
				Constants.SIGN_MIMETYPE_XML);
		signFormats.put(Constants.SIGN_FORMAT_CADES,
				Constants.SIGN_MIMETYPE_PKCS7);
		signFormats.put(Constants.SIGN_FORMAT_PDF,
				Constants.SIGN_MIMETYPE_PDF);
		signFormats.put(Constants.SIGN_FORMAT_XADES_ENVELOPING,
				Constants.SIGN_MIMETYPE_XML);
		signFormats.put(Constants.SIGN_FORMAT_XADES_IMPLICIT,
				Constants.SIGN_MIMETYPE_XML);
		signFormats.put(Constants.SIGN_FORMAT_XADES_EXPLICIT,
				Constants.SIGN_MIMETYPE_XML);
		signFormats.put(Constants.SIGN_FORMAT_XADES_ENVELOPED,
				Constants.SIGN_MIMETYPE_XML);
		signFormats.put(Constants.SIGN_FORMAT_FACTURAE,
				Constants.SIGN_MIMETYPE_XML);
		return signFormats;
	}

	public String translateSearch(String source) {
		String target = "";
		if (null != source) {
			target = source;
			target = target.replace(Constants.TRANSLATE_SOURCE_AT,
					Constants.TRANSLATE_TARGET_A);
			target = target.replace(Constants.TRANSLATE_SOURCE_AD,
					Constants.TRANSLATE_TARGET_A);
			target = target.replace(Constants.TRANSLATE_SOURCE_ET,
					Constants.TRANSLATE_TARGET_E);
			target = target.replace(Constants.TRANSLATE_SOURCE_ED,
					Constants.TRANSLATE_TARGET_E);
			target = target.replace(Constants.TRANSLATE_SOURCE_IT,
					Constants.TRANSLATE_TARGET_I);
			target = target.replace(Constants.TRANSLATE_SOURCE_ID,
					Constants.TRANSLATE_TARGET_I);
			target = target.replace(Constants.TRANSLATE_SOURCE_OT,
					Constants.TRANSLATE_TARGET_O);
			target = target.replace(Constants.TRANSLATE_SOURCE_OD,
					Constants.TRANSLATE_TARGET_O);
			target = target.replace(Constants.TRANSLATE_SOURCE_UT,
					Constants.TRANSLATE_TARGET_U);
			target = target.replace(Constants.TRANSLATE_SOURCE_UD,
					Constants.TRANSLATE_TARGET_U);
			target = target.replace(Constants.TRANSLATE_SOURCE_C,
					Constants.TRANSLATE_TARGET_C);
			target = target.replace(Constants.TRANSLATE_SOURCE_N,
					Constants.TRANSLATE_TARGET_N);
		}
		return target;
	}

	/**
	 * Marca los objetos de la lista como seleccionados a false
	 * @param list lista de DTO abstracto
	 * @see es.seap.minhap.portafirmas.domain.AbstractBaseDTO#setSelected(boolean)
	 */
	public void unSelectAll(List<AbstractBaseDTO> list) {
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator
				.hasNext();) {
			AbstractBaseDTO abstractBaseDTO = (AbstractBaseDTO) iterator.next();
			abstractBaseDTO.setSelected(false);
		}
	}

	public void updateInList(List<AbstractBaseDTO> list, AbstractBaseDTO element) {
		boolean found = false;
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator
				.hasNext()
				&& !found;) {
			AbstractBaseDTO abstractBaseDTO = (AbstractBaseDTO) iterator.next();
			if (abstractBaseDTO.getPrimaryKey().compareTo(
					element.getPrimaryKey()) == 0) {
				found = true;
				abstractBaseDTO.setUpdated(true);
			}
		}

	}

	public List<AbstractBaseDTO> correctModeRequestList(
			List<AbstractBaseDTO> list) {
		List<AbstractBaseDTO> resultList = new ArrayList<AbstractBaseDTO>();

		for (AbstractBaseDTO abstractBaseDTO : list) {
			if (((PfRequestTagsDTO) abstractBaseDTO).isSignModeCorrect()) {
				resultList.add(abstractBaseDTO);
			}
		}

		return resultList;
	}
	
	
	public String getCSV (InputStream sign) throws Exception{
		int n = 0;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try  {
			while((n = sign.read(buffer)) > 0) {
				baos.write(buffer, 0, n);
			}
		} catch (IOException ioe) {
			String mensaje = "No se puede leer el InputStream de la firma: " + ioe.getMessage();
			log.error(mensaje);
			throw new Exception (mensaje, ioe);
		}
		byte[] signBytes = baos.toByteArray();
		
		return getCSV(signBytes);
		
	}

	public String getCSV (byte[] sign) throws Exception{
		String result = null;
		try {
			// Se crea el procesador de firma y se le inserta la firma a tratar.
			SignatureProcessor signatureProcessor = new SignatureProcessor();
			signatureProcessor.processSign(sign);
				 
			// Se crea el objeto que genera el CVS de la firma
			CVSSignature cvs = new CVSSignature(signatureProcessor);
	
			// Se genera el CVS de la firma
			cvs.getCVS();
	//		result = AOUtil.hexify(cvs.getCVS(), false);
	
			// Se obtiene el CVS en formato texto
			result = cvs.getCVSText();
		} catch (AOException aoe) {
			String mensaje = "No se ha podido obtener el CSV de la firma: " + aoe.getMessage();
			log.error(mensaje, aoe);
			throw new Exception (mensaje, aoe);
		
		}
		
		return result;
	}

	/**
	 * Método que obtiene la extensión de un fichero a partir de su nombre completo
	 * @param fileName Nombre del fichero
	 * @return Extensión del fichero
	 */
	public String getFileExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		String extension = fileName.substring(index + 1, fileName.length());
		return extension;
	}

	/**
	 * Método que elimina los acentos del contenido de  una celda de excel
	 * @param cell Celda de excel
	 * @return Contenido de la celda sin acentos
	 */
	public String filterCell(String cell) {
		cell = cell.replace("á", "a");
		cell = cell.replace("Á", "A");
		cell = cell.replace("é", "e");
		cell = cell.replace("É", "E");
		cell = cell.replace("í", "i");
		cell = cell.replace("Í", "I");
		cell = cell.replace("ó", "o");
		cell = cell.replace("Ó", "O");
		cell = cell.replace("ú", "u");
		cell = cell.replace("Ú", "U");
		cell = cell.replace("ñ", "n");
		cell = cell.replace("Ñ", "N");
		return cell;
	}

	/*private Document getDOMDocument(Object source) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		if (source instanceof File) {
			File f = (File) source;
			doc = db.parse(f);
		} else if (source instanceof InputStream) {
			InputStream is = (InputStream) source;
			doc = db.parse(is);
		}
		return doc;
	}*/
	
	/**
	 * Consideraremos que el árbol DOM representa a una firma XML con el documento implícito cuando contenga
	 * el nodo CONTENT y el nodo SignatureProperties.
	 * @param dom árbol DOM de un documento XML.
	 * @return true si representa una firma XML, false en caso contrario.
	 */
	/*private boolean isDocumentXMLSign (Document dom) {
		//comprobamos que tenga el nodo CONTENT y el nodo Signature, en cuyo caso
		boolean is = false;
		Element elementRoot = dom.getDocumentElement();		
		NodeList nl = elementRoot.getElementsByTagName("CONTENT");
		if (nl.getLength() != 0) {
			nl = elementRoot.getElementsByTagName("ds:Signature");
			if (nl.getLength() != 0) {
				is = true;
			}
		}
		return is;
	}*/
	
	/**
	 * Consideraremos que el árbol DOM representa a una firma XML con el documento implícito cuando contenga
	 * el nodo CONTENT y el nodo SignatureProperties.
	 * @param dom árbol DOM de un documento XML.
	 * @return true si representa una firma XML, false en caso contrario.
	 */
	/*private boolean isDocumentXMLSign (Document dom) {
		//comprobamos que tenga el nodo CONTENT y el nodo Signature, en cuyo caso
		boolean is = false;
		Element elementRoot = dom.getDocumentElement();	
		
		// Nodo que contiene al documento firmado
		Node documentoFirmado = elementRoot.getFirstChild();
		NamedNodeMap atributos = documentoFirmado.getAttributes();
		
		// Comprobamos que tenga el atributo Id y el atributo Encoding:				
		int i=0;
		boolean encontrados = false;
		boolean idFound = false;
		boolean encodingFound = false;
		
		while (i < atributos.getLength() && !encontrados) {
			if (atributos.item(i).getNodeName().equalsIgnoreCase("id")){				
				idFound = true;
			} else if (atributos.item(i).getNodeName().equalsIgnoreCase("encoding")) {
				encodingFound = true;
			}
			
			encontrados = idFound && encodingFound;
			i++;
		}
		
		if (encontrados) {
			// Comprobamos que el documento contenga el nodo "ds:Signature"
			NodeList nl = elementRoot.getElementsByTagName("ds:Signature");
			if (nl.getLength() != 0) {
				is = true;
			}
		}
		
		return is;
	}*/

	/**
	 * Consideraremos que el árbol DOM representa a una firma XML con el documento implícito cuando contenga
	 * el nodo CONTENT y el nodo SignatureProperties.
	 * @param dom árbol DOM de un documento XML.
	 * @return true si representa una firma XML, false en caso contrario.
	 */
	/*private boolean isDocumentXMLSign (Document dom) {
		boolean is = true;
		javax.xml.xpath.XPath xpath = null;
		try {
			//xpath = javax.xml.xpath.XPathFactory.newInstance("http://java.sun.com/jaxp/xpath/dom").newXPath();
			xpath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Seleccionar el nodo que tenga el atributo Id y el atributo Encoding
		String expression = "//*[(@Id|@ID|@id) and (@Encoding|@ENCODING|@encoding)]";			
		Node nodo = null;
		try {
			nodo = (Node) xpath.evaluate(expression, dom, javax.xml.xpath.XPathConstants.NODE);
		} catch (XPathExpressionException e) {
		}
		
		if (nodo == null) {
			is = false;
		}
		
		if (is) {
			NamedNodeMap atributos = nodo.getAttributes();
			int i=0;
			boolean encontrado = false;
			while (i < atributos.getLength() && !encontrado) {
				Node atributo = atributos.item(i);
				if (atributo.getNodeName().equalsIgnoreCase("encoding")) {
					encontrado = true;
					if (!atributo.getNodeValue().contains("base64")) {
						is = false;
					}
				}
				i++;
			}
		}
		
		if (is) {
			expression = "//Signature[@Id|@id|@ID]";
			NodeList lista = null;
			try {
				lista = (NodeList) xpath.evaluate(expression, dom, javax.xml.xpath.XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
			}
			if (lista.getLength() == 0) {
				is = false;
			}
		}

		return is;
	}*/

	/**
	 * Método para detectar si un documento es, en realidad, una firma XML.
	 * @param document Documento a comprobar
	 * @return true si se trata de una firma XML, false en caso contrario.
	 * @throws IOException en caso de no poder parsear el documento por haber algún problema con los bytes de entrada.
	 */
	/*public boolean checkIsXMLSign (Object document) {
		boolean parseError = false;
		boolean isSigned = false;
		
		Document dom = null;
		try {
			dom = getDOMDocument(document);
		// Si se produce alguna excepción al parsear, significará que no es un XML y por tanto no será una firma XML.
		} catch (ParserConfigurationException pce) {
			parseError = true;
		} catch (SAXException se){
			parseError = true;
		} catch (IOException ioe) {
			parseError = true;
		}
		
		// Si se ha podido parsear el documento, comprobamos que se trata de una firma XML.
		// entenderemos que sí se trata de una firma XML.
		if (!parseError) {
			isSigned = this.isDocumentXMLSign(dom);
		} 

		return isSigned;		
	}*/

	/**
	 * Obtiene un objeto SignData a partir de una firma XML.
	 * @param sign firma XML.
	 * @return un objeto que contiene el documento que se ha firmado y el mime de ese documento.
	 * @throws Exception cuando la firma no sea XML, o bien no se pueda obtener alguno de los datos.
	 */
	/*public SignData getDataFromSign (byte[] sign) throws Exception{
		SignData datosFirma = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {			
			throw new Exception ("No se puede parsear la firma " + e.getMessage(), e);
		}
		Document dom = null;
		try {
			dom = db.parse(new ByteArrayInputStream(sign));
		} catch (SAXException e) {
			throw new Exception ("No se puede parsear la firma " + e.getMessage(), e);			
		} catch (IOException e) {
			throw new Exception ("No se puede parsear la firma " + e.getMessage(), e);			
		}
		Element elementRoot = dom.getDocumentElement();
		NodeList nl = elementRoot.getElementsByTagName("CONTENT");
		
		if (nl.getLength() == 0) {
			throw new Exception ("La firma no contiene el nodo CONTENT. No se puede extraer el documento.");
		}
		
		Node content = nl.item(0);
		String b64Content = content.getFirstChild().getNodeValue();
		byte[] document = Base64.decodeBase64(b64Content);
		
		Node mimeNode = content.getAttributes().getNamedItem("MimeType");
		
		if (mimeNode == null) {
			throw new Exception ("El nodo CONTENT no contiene el atributo MymeTipe.");
		}
		String mime = mimeNode.getNodeValue();
		
		datosFirma = new SignData(mime, document);
	
		return datosFirma;
	}*/

	public byte[] getBytes(InputStream is) throws IOException {

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

	/*public static void main (String[] args) {
		try {
			FileInputStream fi = new FileInputStream("/home/rus/desarrollo/firmas/word_horizontal.doc.xades");
			FileInputStream fi2 = new FileInputStream("/home/rus/desarrollo/firmas/firmaIgae.xsig");
			
			System.out.println(Util.getInstance().checkIsXMLSign(fi));
			System.out.println(Util.getInstance().checkIsXMLSign(fi2));
			
					/*	byte[] bytes = Util.getInstance().getBytes(fi);
			boolean is = Util.getInstance().checkIsXMLSign(bytes);
			System.out.println("ES: " + is);
			
			SignData datos = Util.getInstance().getDataFromSign(bytes);
			FileOutputStream salida = new FileOutputStream("/home/rus/desarrollo/ficheros/probando_doc.doc");
			salida.write(datos.getDocument());
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public List<UserEnvelope> toUserEnvelopeList(List<AbstractBaseDTO> users) {
		List<UserEnvelope> envelopes = new ArrayList<UserEnvelope>();
		for (AbstractBaseDTO user : users) {
			envelopes.add(new UserEnvelope((PfUsersDTO) user));
		}
		return envelopes;
	}

	public List<GroupEnvelope> toGroupEnvelopeList(List<AbstractBaseDTO> groups) {
		List<GroupEnvelope> envelopes = new ArrayList<GroupEnvelope>();
		for (AbstractBaseDTO group : groups) {
			envelopes.add(new GroupEnvelope((PfGroupsDTO) group));
		}
		return envelopes;
	}
	
	/**
	 * Construye un nombre a partir del nombre original, un prefijo, un sufijo y una nueva extensión.
	 * Los parámetros nulos se ignoran.
	 * Si el parámetro "conservarExtension" es true, no se le quitará la extensión al nombre, si es false,
	 * se elimina todo a partir del último punto.
	 * @param originalName
	 * @param conservarExtension
	 * @param prefix
	 * @param suffix
	 * @param extension
	 * @return
	 */
	public String getNombreFichero (String originalName, boolean conservarExtension, String prefix, String suffix, String extension) {
		String name = originalName;
		if (!conservarExtension) {
			int indexOfDot = originalName.lastIndexOf(".");
			if (indexOfDot > -1) {
				name = originalName.substring(0, indexOfDot);
			}
		}
		if (prefix != null) {
			name = prefix + name; 
		}
		if (suffix != null) {
			name = name + suffix;
		}
		if (extension != null) {
			name = name + "." + extension;
		}
		return name;
	}

	/**
	 * Devuelve lo que está entre la cadena "O=" y la coma (",").
	 * @param cadena
	 * @return
	 */
	public static String getOrganizationIssuerX500Principal (String issuer) {
		int index = issuer.indexOf("O=");
		int index2 = issuer.indexOf(",", index);
		if ((index < index2)) {
			return issuer.substring(index + 2, index2);
		} else {
			return "";
		}
	}

	/**
	 * Introduce un asterisco entre los caracteres // y [, o si la entrada es "//", introduce un asterico al final;
	 * Ejemplo: 
	 * 		Entrada: //[@Id=CONTENT-1c5d50a7-f5e1-4171-9d10-d9c2909fcb72]
	 * 		Salida: //*[@Id=CONTENT-1c5d50a7-f5e1-4171-9d10-d9c2909fcb72]
	 * @param xpathExpression
	 * @return
	 */
	public static String meteAsteriscoXPath (String xpathExpression) {
		
		if (!"//".contentEquals(xpathExpression)) {
			String regexp = "//\\[";
			xpathExpression = xpathExpression.replaceAll(regexp, "//*[");
		} else {
			xpathExpression = xpathExpression + "*";
		}
		
		return xpathExpression;		
	}

	/**
	 * Introduce una comilla simple entre el carácter "=" y el primer dígito,número o guión encontrado.
	 * Introduce otra comilla simple entre el último dígito, número o guión encontrado y el carácter "]"
	 * Ejemplo:
	 * 		Entrada:  //*[@Id=CONTENT-1c5d50a7-f5e1-4171-9d10-d9c2909fcb72]
	 *  	Salida:	  //*[@Id='CONTENT-1c5d50a7-f5e1-4171-9d10-d9c2909fcb72']
	 * @param xpathExpression
	 * @return
	 */
	public static String meteComillasXPath (String xpathExpression) {
		String regexp = "=[a-zA-Z|0-9|\\-]+\\]";
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(xpathExpression);
		if (m.find()) {		
			int i = m.start();
			int j = m.end();
			if (i > -1 && j > -1) {
				xpathExpression = xpathExpression.substring(0, i+1) + "'" + xpathExpression.substring(i+1, j-1) + "'" + xpathExpression.substring(j-1);
			}
		}
		return xpathExpression;
	}
	
	public static List<String> profilesAdmin(){
		List<String> identificadoresPerfilesAdmin = new ArrayList<String>();
		identificadoresPerfilesAdmin.add(Constants.C_PROFILES_ADMIN_PROVINCE);
		identificadoresPerfilesAdmin.add(Constants.C_PROFILES_ADMIN);
		identificadoresPerfilesAdmin.add(Constants.C_PROFILES_ADMIN_ORGANISM);
		identificadoresPerfilesAdmin.add(Constants.C_PROFILES_ADMIN_CAID);
		return identificadoresPerfilesAdmin;
	}

	public static String formatearCsv(String csv) throws CSVQueryDocumentException {
		String csvResult = null;
		if (Util.esVacioONulo(csv)) {
			throw new CSVQueryDocumentException("El código seguro de verificación introducido no es correcto", null);
		} else {
			// Si vienen los guiones, se quitan
			csvResult = csv.trim().replaceAll("-", "");
			//Si viene el código GEN, se quita
			csvResult = csvResult.replaceAll("GEN", "");
			//Si viene el código INTERNO, se quita
			csvResult = csvResult.replaceAll("INTERNO", "");
		}
		
		return csvResult;
	}
	
    public static boolean validateEmail(String email) {
   	 
        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
 
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }
    
    public static boolean validateIDEni(String value){
    	Pattern pattern = Pattern.compile("ES_[A-Z]{1}[A-Z0-9]{1}[0-9]{7}_[0-9]{4}_.*");
    	Matcher matcher = pattern.matcher(value);
    	return matcher.find();
    }
    
    public String generarIDEni(String hash, String dir3) {
    	SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    	String idEni = "ES_";
    	idEni += dir3 + "_";
    	Date today = new Date();
    	String anio = sdfYear.format(today);
    	idEni += anio + "_";
    	idEni += hash;
    	if (idEni.length()>52){
    		idEni = idEni.substring(0, 51);
    	}
    	return idEni;
    	
    	
    }
}
