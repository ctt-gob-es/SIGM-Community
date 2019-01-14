/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;
import com.ieci.tecdoc.common.invesdoc.Iusergrouphdr;
import com.ieci.tecdoc.common.invesdoc.Iuseruserhdr;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.AxSfIn;
import com.ieci.tecdoc.common.isicres.AxXf;

import es.msssi.sgm.registropresencial.beans.ErrorBean;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ItemBean;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.errors.RPBaseException;

/**
 * Clase que contiene métodos útiles para la aplicación.
 * 
 * @author cmorenog
 * */
public class Utils {
	
	private static final Logger LOG = Logger.getLogger(AuthenticationHelper.class);
	
	private static final String MESSAGE = "Excepción interna con código: ";
	private static final String MESSAGE_MSG = " y mensaje : ";
	private static final String FLD2 = "fld2";
	private static final String FLD4 = "fld4";
	private static final String FLD6 = "fld6";
	private static final String FLD7 = "fld7";
	private static final String FLD8 = "fld8";
	private static final String FLD10 = "fld10";
	private static final String FLD11 = "fld11";
	private static final String FLD12 = "fld12";
	private static final String FLD13 = "fld13";
	private static final String FLD14 = "fld14";
	private static final String FLD15 = "fld15";
	private static final String FLD16 = "fld16";
	private static final String FLD17 = "fld17";
	private static final String FLD19 = "fld19";
	private static final String FLD21 = "fld21";
	private static final String FLD22 = "fld22";
	private static final int FIELD18 = 18;
	private static final int BUFFER = 0xFFFF;

	/**
	 * Convierte un texto en un texto en mayúsculas y sin tildes.
	 * 
	 * @param valor
	 *            texto sin formatear
	 * @return texto en mayúsculas y sin tildes
	 * */
	public static String converterToCS(String valor) {
		LOG.trace("Entrando en Utils.converterToCS()");
		String result = null;
		if (valor != null) {
			result = valor.toUpperCase().replace("Á", "A").replace("É", "E")
					.replace("Í", "I").replace("Ó", "O").replace("Ú", "U")
					.replace("À", "A").replace("Â", "A").replace("È", "E")
					.replace("Ê", "E").replace("Ì", "I").replace("Î", "I")
					.replace("Ò", "O").replace("Ô", "O").replace("Ù", "U")
					.replace("Û", "U");

			result = result.trim();
		}
		return result;
	}

	/**
	 * Método estático para la redirección a una url con un parámetro.
	 * 
	 * @param beans
	 *            Map con los parámetros que se van a pasar en la memoria flash.
	 * @param readOnly
	 *            indica si es solo lectura
	 * @param url
	 *            página a la que redirecciona
	 * */
	public static void navigate(Map<String, Object> beans, boolean readOnly, String url) {
		
		LOG.trace("Entrando en Utils.navigate() para redirección con parámetros");
		
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("mostrar", false);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("readOnly", readOnly);
		
		for (Map.Entry<String, Object> entry : beans.entrySet()) {
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put(entry.getKey(), entry.getValue());
		}
		
		navigate(url);
	}

	/**
	 * Método estático para la redirección sin parámetros.
	 * 
	 * @param url
	 *            página a la que redirecciona
	 * */
	public static void navigate(String url) {
		
		LOG.trace("Entrando en Utils.navigate() para redirección sin parámetros");
		
		ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		nav.performNavigation(url + "?faces-redirect=true");// +
		// "?faces-redirect=true"
	}

	/**
	 * Método estático que devuelve un valor de un item de una lista.
	 * 
	 * @param itemList
	 *            lista con todos los items
	 * @param clave
	 *            la clave del item a buscar en la lista
	 * 
	 * @return value Item devuelto.
	 * */
	public static String getItemList(List<ItemBean> itemList, Object clave) {
	
		LOG.trace("Entrando en Utils.getItemList()");
		
		String value = null;
		if (clave != null) {
			for (ItemBean bean : itemList) {
				if (bean.getClave().equals(clave)) {
					value = bean.getValor();
				}
			}
		}
		
		return value;
	}

	/**
	 * Construye el mensaje de error en caso de excepción y redirecciona a la
	 * página de error.
	 * 
	 * @param rpBaseException
	 *            Excepción interna de Registro.
	 * @param tecDocException
	 *            Excepción general de SIGM.
	 * @param exception
	 *            Excepción general de Java.
	 */
	public static void redirectToErrorPage(RPBaseException rpBaseException, TecDocException tecDocException, Exception exception) {

		ErrorBean errorBean = new ErrorBean();
		
		if (rpBaseException != null) {
			// Comprueba si llega una excepción interna del Registro.
			errorBean.setErrorCode(rpBaseException.getCode().getCode());
			errorBean.setErrorMessage(rpBaseException.getShortMessage());
			
			LOG.trace(MESSAGE + rpBaseException.getCode().getCode() + MESSAGE_MSG + rpBaseException.getShortMessage());
			
		} else if (tecDocException != null) {
			// Comprueba si llega una excepción de SIGM.
			errorBean.setErrorCode(tecDocException.getCode());
			errorBean.setErrorMessage(tecDocException.getMessage());
			
			LOG.trace(MESSAGE + tecDocException.getCode() + MESSAGE_MSG + tecDocException.getMessage());
			
		} else {
			// Comprueba si llega una excepción de Java.
			errorBean.setErrorCode(exception.getLocalizedMessage());
			errorBean.setErrorMessage(exception.getMessage());
			
			LOG.trace(MESSAGE + exception.getLocalizedMessage() + MESSAGE_MSG + exception.getMessage());
			
		}

		// Devuelve un null y no funciona correctamente con filtros
		FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("errorBean", errorBean);

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("error.xhtml");
			
		} catch (IOException ioException) {
			LOG.error("La página error.xhtml no existe", ioException);
		}
	}

	/**
	 * Transforma un objeto de fecha Timestamp de sql en un String con formato
	 * \"dd-MM-yyyy hh:mm:ss\".
	 * 
	 * @param date
	 *            objeto a convertir.
	 * @return dateFormatted fecha convertida.
	 */
	public static String formatTimeStampInString(Timestamp date) {
		
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dateFormatted = format1.format(date);
		
		return dateFormatted;
	}

	/**
	 * Transforma un objeto de fecha Date de sql en un String con formato
	 * \"dd-MM-yyyy hh:mm:ss\".
	 * 
	 * @param date
	 *            objeto a convertir.
	 * @return dateFormatted fecha convertida.
	 */
	public static String formatDateInString(Date date) {
		
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dateFormatted = format1.format(date);
		
		return dateFormatted;
	}

	/**
	 * Convierte los datos de un objeto InputStream a un array de bytes.
	 * 
	 * @param inputStream
	 *            Datos a convertir.
	 * @return inputStreamInByteArray Array de bytes con los datos convertidos.
	 * @throws IOException
	 *             Si se ha producido un error en la conversión.
	 */
	public static byte[] convertInputStreamToByeArray(InputStream inputStream) throws IOException {
		
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		byte[] inputStreamInByteArray = null;
		byte[] buffer = new byte[BUFFER];
		
		for (int len; (len = inputStream.read(buffer)) != -1;) {
			byteArrayOS.write(buffer, 0, len);
		}
		
		byteArrayOS.flush();
		inputStreamInByteArray = byteArrayOS.toByteArray();
		
		return inputStreamInByteArray;
	}

	/**
	 * Mapea la información proveniente de inputregister a un axsf.
	 * 
	 * @param inputRegisterBean
	 *            Bean a mapear.
	 * @return newAxSF Bean mapeado.
	 */
	public static AxSf mappingInputRegisterToAxSF( InputRegisterBean inputRegisterBean) {
		
		LOG.trace("Entrando en mappingInputRegisterToAxSF()");
		
		AxSf newAxSF = new AxSfIn();
		
		if (inputRegisterBean.getFld2() != null) {
			newAxSF.addAttributeName(FLD2);
			newAxSF.addAttributeValue(FLD2, inputRegisterBean.getFld2());
		}
		
		if (inputRegisterBean.getFld4() != null) {
			newAxSF.addAttributeName(FLD4);
			newAxSF.addAttributeValue(FLD4, inputRegisterBean.getFld4());
		}
		
		if (inputRegisterBean.getFld6() != null) {
			newAxSF.addAttributeName(FLD6);
			newAxSF.addAttributeValue(FLD6, inputRegisterBean.getFld6());
		}
		
		if (inputRegisterBean.getFld7() != null) {
			newAxSF.addAttributeName(FLD7);
			newAxSF.addAttributeValue(FLD7, inputRegisterBean.getFld7().getId());
		}
		
		if (inputRegisterBean.getFld8() != null) {
			newAxSF.addAttributeName(FLD8);
			newAxSF.addAttributeValue(FLD8, inputRegisterBean.getFld8().getId());
		}
		
		if (inputRegisterBean.getFld10() != null && !"".equals(inputRegisterBean.getFld10())) {
			newAxSF.addAttributeName(FLD10);
			newAxSF.addAttributeValue(FLD10, inputRegisterBean.getFld10());
		}
		
		if (inputRegisterBean.getFld11() != null && !"".equals(String.valueOf(inputRegisterBean.getFld11()))) {
			newAxSF.addAttributeName(FLD11);
			newAxSF.addAttributeValue(FLD11, inputRegisterBean.getFld11());
		}
		
		if (inputRegisterBean.getFld12() != null) {
			newAxSF.addAttributeName(FLD12);
			newAxSF.addAttributeValue(FLD12, inputRegisterBean.getFld12());
		}
		
		if (inputRegisterBean.getFld13() != null && !"".equals(inputRegisterBean.getFld13())) {
			newAxSF.addAttributeName(FLD13);
			newAxSF.addAttributeValue(FLD13, inputRegisterBean.getFld13());
		}
		
		if (inputRegisterBean.getFld14() != null && !"".equals(inputRegisterBean.getFld14())) {
			newAxSF.addAttributeName(FLD14);
			newAxSF.addAttributeValue(FLD14, inputRegisterBean.getFld14());
		}
		
		if (inputRegisterBean.getFld15() != null && !"".equals(inputRegisterBean.getFld15())) {
			newAxSF.addAttributeName(FLD15);
			newAxSF.addAttributeValue(FLD15, inputRegisterBean.getFld15());
		}
		
		if (inputRegisterBean.getFld16() != null && !"".equals(String.valueOf(inputRegisterBean.getFld16()))) {
			newAxSF.addAttributeName(FLD16);
			newAxSF.addAttributeValue(FLD16, inputRegisterBean.getFld16().getId());
		}
		if (inputRegisterBean.getFld17() != null && !"".equals(inputRegisterBean.getFld17())) {
			newAxSF.addAttributeName(FLD17);
			newAxSF.addAttributeValue(FLD17, inputRegisterBean.getFld17());
		}
		if (inputRegisterBean.getFld19() != null && !"".equals(inputRegisterBean.getFld19())) {
			newAxSF.addAttributeName(FLD19);
			newAxSF.addAttributeValue(FLD19, inputRegisterBean.getFld19());
		}
		
		if (inputRegisterBean.getFld18() != null && !"".equals(inputRegisterBean.getFld18())) {
			AxXf axXf = new AxXf();
			axXf.setFldId(FIELD18);
			axXf.setText(inputRegisterBean.getFld18());
			newAxSF.addExtendedField(FIELD18, axXf);
		}
		
		if (inputRegisterBean.getFld21() != null && !"".equals(inputRegisterBean.getFld21())) {
			newAxSF.addAttributeName(FLD21);
			newAxSF.addAttributeValue(FLD21, inputRegisterBean.getFld21());
		}
		
		if (inputRegisterBean.getFld22() != null && !"".equals(inputRegisterBean.getFld22())) {
			newAxSF.addAttributeName(FLD22);
			newAxSF.addAttributeValue(FLD22, inputRegisterBean.getFld22());
		}
		
		return newAxSF;
	}

	/**
	 * Mapea la información proveniente de outputregister a un axsf.
	 * 
	 * @param outputRegisterBean
	 *            Bean a mapear.
	 * @return newAxSF Bean mapeado.
	 */
	public static AxSf mappingOutputRegisterToAxSF( OutputRegisterBean outputRegisterBean) {
		
		LOG.trace("Entrando en mappingInputRegisterToAxSF()");
		
		AxSf newAxSF = new AxSfIn();
		
		if (outputRegisterBean.getFld2() != null) {
			newAxSF.addAttributeName(FLD2);
			newAxSF.addAttributeValue(FLD2, outputRegisterBean.getFld2());
		}
		
		if (outputRegisterBean.getFld4() != null) {
			newAxSF.addAttributeName(FLD4);
			newAxSF.addAttributeValue(FLD4, outputRegisterBean.getFld4());
		}
		
		if (outputRegisterBean.getFld6() != null) {
			newAxSF.addAttributeName(FLD6);
			newAxSF.addAttributeValue(FLD6, outputRegisterBean.getFld6());
		}
		
		if (outputRegisterBean.getFld7() != null) {
			newAxSF.addAttributeName(FLD7);
			newAxSF.addAttributeValue(FLD7, outputRegisterBean.getFld7().getId());
		}
		
		if (outputRegisterBean.getFld8() != null) {
			newAxSF.addAttributeName(FLD8);
			newAxSF.addAttributeValue(FLD8, outputRegisterBean.getFld8().getId());
		}
		
		if (outputRegisterBean.getFld10() != null && !"".equals(outputRegisterBean.getFld10())) {
			newAxSF.addAttributeName(FLD10);
			newAxSF.addAttributeValue(FLD10, outputRegisterBean.getFld10());
		}
		
		if (outputRegisterBean.getFld11() != null && !"".equals(String.valueOf(outputRegisterBean.getFld11()))) {
			newAxSF.addAttributeName(FLD11);
			newAxSF.addAttributeValue(FLD11, outputRegisterBean.getFld11());
		}
		
		if (outputRegisterBean.getFld12() != null && !"".equals(String.valueOf(outputRegisterBean.getFld12()))) {
			newAxSF.addAttributeName(FLD12);
			newAxSF.addAttributeValue(FLD12, outputRegisterBean.getFld12().getId());
		}
		
		if (outputRegisterBean.getFld13() != null && !"".equals(outputRegisterBean.getFld13())) {
			newAxSF.addAttributeName(FLD13);
			newAxSF.addAttributeValue(FLD13, outputRegisterBean.getFld13());
		}
		
		if (outputRegisterBean.getFld14() != null && !"".equals(outputRegisterBean.getFld14())) {
			newAxSF.addAttributeName(FLD14);
			newAxSF.addAttributeValue(FLD14, outputRegisterBean.getFld14());
		}
		
		if (outputRegisterBean.getFld21() != null && !"".equals(outputRegisterBean.getFld21())) {
			newAxSF.addAttributeName(FLD21);
			newAxSF.addAttributeValue(FLD21, outputRegisterBean.getFld21());
		}
		
		if (outputRegisterBean.getFld22() != null && !"".equals(outputRegisterBean.getFld22())) {
			newAxSF.addAttributeName(FLD22);
			newAxSF.addAttributeValue(FLD22, outputRegisterBean.getFld22());
		}
		
		return newAxSF;
	}

	public static Iuseruserhdr defaultUser( List<Iuseruserhdr> listDepartament2, Integer idorg) {
		
		Iuseruserhdr result = null;
		
		if (listDepartament2 != null) {
			for (Iuseruserhdr user : listDepartament2) {
				if (idorg.equals(user.getId())) {
					result = user;
				}
			}
		}
		
		return result;
	}
	
	public static Iuserdepthdr defaultDepart( List<Iuserdepthdr> listDepartament2, Integer idorg) {
		
		Iuserdepthdr result = null;
		
		if (listDepartament2 != null) {
			for (Iuserdepthdr depart : listDepartament2) {
				if (idorg.equals(depart.getIdorg())) {
					result = depart;
				}
			}
		}
		
		return result;
	}

	public static Iusergrouphdr defaultGroup( List<Iusergrouphdr> listDepartament2, Integer idorg) {
	
		Iusergrouphdr result = null;
	
		if (listDepartament2 != null) {
			for (Iusergrouphdr group : listDepartament2) {
				if (idorg.equals(group.getId())) {
					result = group;
				}
			}
		}
	
		return result;
	}
}