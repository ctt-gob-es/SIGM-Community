/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.util;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sigm.dao.dataaccess.domain.InputRegisterReportsCert;
import sigm.dao.dataaccess.domain.OutputRegisterReportsCert;
import sigm.dao.dataaccess.domain.XtField;
import sigm.dao.dataaccess.service.SIGMServiceManager;
import sigm.dao.exception.DaoException;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.jcabi.xml.XMLDocument;

import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.InterestedBo;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;
import es.msssi.sigm.core.exception.SigmWSException;

public class ReportUtil extends SqlMapClientBaseDao {
	private static Logger log = Logger.getLogger(ReportUtil.class.getName());
	

	/**
     * Mapea los valores de registros de entrada devueltos por la consulta para
     * generar los informes de certificados.
     * 
     * @param inputRegisterReportsCert
     *            resultado de base de datos.
     * @return inputRegisterHashMap datos mapeados.
     * @throws SQLException
     *             Si se produce un error al obtener los datos.
     */
    public LinkedHashMap<String, Object> fillInputRegisterReportsList(
	    InputRegisterReportsCert inputRegisterReportsCert)   {
//	LOG.trace("Entrando en ReportDAO.fillInputRegisterReportsList()");
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	LinkedHashMap<String, Object> inputRegisterHashMap = new LinkedHashMap<String, Object>();
	// CORRESPONDENCIA DE VALOR DE VISTA EN BBDD CON NOMBRE DE CAMPO
	// -------------------------------------------------------------
	// 1 IDBOOK NUMBER - Id. de libro.
	inputRegisterHashMap.put("IDBOOK", inputRegisterReportsCert.getIdBook());
	// 2 FDRID NUMBER - Id.
	inputRegisterHashMap.put("FDRID", inputRegisterReportsCert.getFdrid());
	// 3 TIMESTAMP DATE - Timestamp.
	inputRegisterHashMap.put("TIMESTAMP", inputRegisterReportsCert.getTimestamp());
	// 4 FLD1 VARCHAR2 - Número de registro.
	inputRegisterHashMap.put("FLD1", inputRegisterReportsCert.getFld1());
	// 5 FLD2 DATE - Fecha de registro.
	inputRegisterHashMap.put("FLD2", formatter.format(inputRegisterReportsCert.getFld2()));
	// 6 FLD3 VARCHAR2 - Usuario.
	inputRegisterHashMap.put("FLD3", inputRegisterReportsCert.getFld3());
	// 7 FLD4 DATE - Fecha de trabajo.
	inputRegisterHashMap.put("FLD4", inputRegisterReportsCert.getFld4());
	// 8 FLD5 NUMBER - Oficina de Registro.
	ScrOfic scrRegOfic = new ScrOfic();
	scrRegOfic.setId(inputRegisterReportsCert.getFld5());
	inputRegisterHashMap.put("FLD5", scrRegOfic);
	// 9 FLD5_TEXT VARCHAR2 - Nombre oficina de Registro.
	inputRegisterHashMap.put("FLD5_TEXT", inputRegisterReportsCert.getFld5_text());
	// 10 FLD5_ADDRESS VARCHAR2 - Dirección oficina.
	// 11 FLD5_CITY VARCHAR2 - Ciudad oficina.
	// 12 FLD5_ZIP VARCHAR2 - Cód. postal oficina.
	// 13 FLD5_COUNTRY VARCHAR2 - Provincia oficina.
	// 14 FLD5_TELEPHONE VARCHAR2 - Teléfono oficina.
	// 15 FLD5_FAX VARCHAR2 - Fax oficina.
	// 16 FLD5_EMAIL VARCHAR2 - Email oficina.
//	inputRegisterHashMap.put(
//		"FLD5_ADDRESS",
//		formatAddressOfic(inputRegisterReportsCert.getFld5_address(),
//			inputRegisterReportsCert.getFld5_city(),
//			inputRegisterReportsCert.getFld5_zip(),
//			inputRegisterReportsCert.getFld5_country(),
//			inputRegisterReportsCert.getFld5_telephone(),
//			inputRegisterReportsCert.getFld5_fax(),
//			inputRegisterReportsCert.getFld5_email()));
	// 17 FLD6 NUMBER - Estado.
	inputRegisterHashMap.put("FLD6", inputRegisterReportsCert.getFld6());
	// 18 FLD7 NUMBER - Origen.
	ScrOrg scrOrig = new ScrOrg();
	scrOrig.setId(inputRegisterReportsCert.getFld7());
	inputRegisterHashMap.put("FLD7", scrOrig);
	// 19 FLD7_TEXT VARCHAR2 - Nombre organismo origen.
	inputRegisterHashMap.put("FLD7_TEXT", inputRegisterReportsCert.getFld7_text());
	// 20 FLD8 NUMBER - Destino.
	ScrOrg scrDest = new ScrOrg();
	scrDest.setId(inputRegisterReportsCert.getFld8());
	inputRegisterHashMap.put("FLD8", scrDest);
	// 21 FLD8_TEXT VARCHAR2 - Nombre organismo destino.
	inputRegisterHashMap.put("FLD8_TEXT", inputRegisterReportsCert.getFld8_text());
	// 22 FLD9 VARCHAR2 - Remitentes.
	inputRegisterHashMap.put("FLD9", inputRegisterReportsCert.getFld9());
	// 23 FLD10 VARCHAR2 - Número de Registro original.
	inputRegisterHashMap.put("FLD10", inputRegisterReportsCert.getFld10());
	// 24 FLD11 NUMBER - Tipo de Registro original.
	inputRegisterHashMap.put("FLD11", inputRegisterReportsCert.getFld11());
	// 25 FLD12 DATE - Fecha de Registro original.
	inputRegisterHashMap.put("FLD12", inputRegisterReportsCert.getFld12());
	// 26 FLD13 NUMBER - Registro original.
	ScrOrg scrOriginalReg = new ScrOrg();
	scrOriginalReg.setId(inputRegisterReportsCert.getFld13());
	// 27 FLD13_TEXT VARCHAR2 - Nombre Registro original.
	inputRegisterHashMap.put("FLD13_TEXT", inputRegisterReportsCert.getFld13_text());
	inputRegisterHashMap.put("FLD13", scrOriginalReg);
	// 28 FLD14 VARCHAR2 - Tipos de transporte.
	inputRegisterHashMap.put("FLD14", inputRegisterReportsCert.getFld14());
	// 29 FLD15 VARCHAR2 - Número de transporte.
	inputRegisterHashMap.put("FLD15", inputRegisterReportsCert.getFld15());
	// 30 FLD16 NUMBER - Tipo de asunto.
	ScrCa scrCa = new ScrCa();
	scrCa.setId(inputRegisterReportsCert.getFld16());
	inputRegisterHashMap.put("FLD16", scrCa);
	// 31 FLD16_TEXT VARCHAR2 - Nombre de asunto.
	inputRegisterHashMap.put("FLD16_TEXT", inputRegisterReportsCert.getFld16_text());
	// 32 FLD17 VARCHAR2 - Resumen.
	inputRegisterHashMap.put("FLD17", inputRegisterReportsCert.getFld17());
	// 33 FLD19 VARCHAR2 - Referencia al Expediente.
	inputRegisterHashMap.put("FLD19", inputRegisterReportsCert.getFld19());
	// 34 FLD20 DATE - Fecha del documento.
	inputRegisterHashMap.put("FLD20", inputRegisterReportsCert.getFld20());
	return inputRegisterHashMap;
    }

    /**
     * Mapea los valores de registros de salida devueltos por la consulta para
     * generar los informes de certificados.
     *
     * @param outputRegisterReportsCert
     *            resultado de base de datos.
     * @return outputRegisterHashMap datos mapeados.
     * @throws java.sql.SQLException
     *             Si se produce un error al obtener los datos.
     */
    public LinkedHashMap<String, Object> fillOutputRegisterReportsList(
	    OutputRegisterReportsCert outputRegisterReportsCert) {
	log.trace("Entrando en ReportDAO.fillOutputRegisterReportsList()");
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	LinkedHashMap<String, Object> outputRegisterHashMap = new LinkedHashMap<String, Object>();
	// CORRESPONDENCIA DE VALOR DE VISTA EN BBDD CON NOMBRE DE CAMPO
	// -------------------------------------------------------------
	// 1 IDBOOK NUMBER - Id de libro.
	outputRegisterHashMap.put("IDBOOK", outputRegisterReportsCert.getIdBook());
	// 2 FDRID NUMBER - Id.
	outputRegisterHashMap.put("FDRID", outputRegisterReportsCert.getFdrid());
	// 3 TIMESTAMP DATE - Timestamp.
	outputRegisterHashMap.put("TIMESTAMP", outputRegisterReportsCert.getTimestamp());
	// 4 FLD1 VARCHAR2 - Número de registro.
	outputRegisterHashMap.put("FLD1", outputRegisterReportsCert.getFld1());
	// 5 FLD2 DATE - Fecha de registro.
	outputRegisterHashMap.put("FLD2",formatter.format(outputRegisterReportsCert.getFld2()));
	// 6 FLD3 VARCHAR2 - Usuario.
	outputRegisterHashMap.put("FLD3", outputRegisterReportsCert.getFld3());
	// 7 FLD4 DATE - Fecha de trabajo.
	outputRegisterHashMap.put("FLD4", outputRegisterReportsCert.getFld4());
	// 8 FLD5 NUMBER - Oficina de Registro.
	ScrOfic scrRegOfic = new ScrOfic();
	scrRegOfic.setId(outputRegisterReportsCert.getFld5());
	outputRegisterHashMap.put("FLD5", scrRegOfic);
	// 9 FLD5_TEXT VARCHAR2 - Nombre oficina de Registro.
	outputRegisterHashMap.put("FLD5_TEXT", outputRegisterReportsCert.getFld5_text());
	// 10 FLD5_ADDRESS VARCHAR2 - Dirección oficina.
	// 11 FLD5_CITY VARCHAR2 - Ciudad oficina.
	// 12 FLD5_ZIP VARCHAR2 - Cód. postal oficina.
	// 13 FLD5_COUNTRY VARCHAR2 - Provincia oficina.
	// 14 FLD5_TELEPHONE VARCHAR2 - Teléfono oficina.
	// 15 FLD5_FAX VARCHAR2 - Fax oficina.
	// 16 FLD5_EMAIL VARCHAR2 - Email oficina.
	outputRegisterHashMap.put(
		"FLD5_ADDRESS",
		formatAddressOfic(outputRegisterReportsCert.getFld5_address(),
			outputRegisterReportsCert.getFld5_city(),
			outputRegisterReportsCert.getFld5_zip(),
			outputRegisterReportsCert.getFld5_country(),
			outputRegisterReportsCert.getFld5_telephone(),
			outputRegisterReportsCert.getFld5_fax(),
			outputRegisterReportsCert.getFld5_email()));
	// 17 FLD6 NUMBER - Estado.
	outputRegisterHashMap.put("FLD6", outputRegisterReportsCert.getFld6());
	// 18 FLD7 NUMBER - Origen.
	ScrOrg scrOrig = new ScrOrg();
	scrOrig.setId(outputRegisterReportsCert.getFld7());
	outputRegisterHashMap.put("FLD7", scrOrig);
	// 19 FLD7_TEXT VARCHAR2 - Nombre origen.
	outputRegisterHashMap.put("FLD7_TEXT", outputRegisterReportsCert.getFld7_text());
	// 20 FLD8 NUMBER - Destino.
	ScrOrg scrDest = new ScrOrg();
	scrDest.setId(outputRegisterReportsCert.getFld8());
	outputRegisterHashMap.put("FLD8", scrDest);
	// 21 FLD8_TEXT VARCHAR2 - Nombre destino.
	outputRegisterHashMap.put("FLD8_TEXT", outputRegisterReportsCert.getFld8_text());
	// 22 FLD9 VARCHAR2 - Remitentes.
	outputRegisterHashMap.put("FLD9", outputRegisterReportsCert.getFld9());
	// 23 FLD10 VARCHAR2 - Tipos de transporte.
	outputRegisterHashMap.put("FLD10", outputRegisterReportsCert.getFld10());
	// 24 FLD11 VARCHAR2 - Número de transporte.
	outputRegisterHashMap.put("FLD11", outputRegisterReportsCert.getFld11());
	// 25 FLD12 NUMBER - Tipo de asunto.
	ScrCa scrCa = new ScrCa();
	scrCa.setId(outputRegisterReportsCert.getFld12());
	outputRegisterHashMap.put("FLD12", scrCa);
	// 26 FLD12 VARCHAR2 - Asunto.
	outputRegisterHashMap.put("FLD12_TEXT", outputRegisterReportsCert.getFld12_text());
	// 27 FLD13 VARCHAR2 - Resumen.
	outputRegisterHashMap.put("FLD13", outputRegisterReportsCert.getFld13());
	// 28 FLD15 DATE - Fecha del documento.
	outputRegisterHashMap.put("FLD15", outputRegisterReportsCert.getFld15());
	return outputRegisterHashMap;
    }


    /**
     * Agrega a un mapa de datos el contenido de campos extendidos
     * @param mapReport mapa de datos
     * @param listXtfield campos extendidos
     */
	public void addExtendedFields(Map<String, Object> mapReport, List<XtField> listXtfield) {
		Map<String, Object> secciones = new HashMap<String, Object>();
		mapReport.put(Constants.XML_TAG_EXTENDIDOS, secciones);
		
		for(int i=0;i<listXtfield.size();i++){
			XtField xtField = listXtfield.get(i);
			if(xtField.getName().equals(Constants.TIPO_PROCEDIMIENTO_FIELD_BBDD)){
				mapReport.put(Constants.XML_TAG_TIPO_PROC, xtField.getValor()+Constants.HYPHEN_CHAR+xtField.getDescripcion());
				continue;
			}

			Map<String, Object> seccion = null;
			if(xtField.getSeccion()!=null){
				if(secciones.get(xtField.getSeccion()) == null)
					seccion = new HashMap<String, Object>();
				else
					seccion = (Map<String, Object>) secciones.get(xtField.getSeccion());

				seccion.put(String.valueOf(xtField.getFldid()), xtField.getValor()+Constants.XML_VALOR_SEPARATOR+xtField.getDescripcion());
				secciones.put(xtField.getSeccion(), seccion);	
			} else {
				seccion = new HashMap<String, Object>();
				seccion.put(String.valueOf(xtField.getFldid()), xtField.getValor()+Constants.XML_VALOR_SEPARATOR+xtField.getDescripcion());
				secciones.put(Constants.XML_TAG_OTROS, seccion);
				
			}
 
		}
		
	}    
    
	/**
     * Rellena la dirección completa de una oficina.
     * 
     * @param address
     *            Dirección de la oficina.
     * @param city
     *            Ciudad de la oficina.
     * @param zip
     *            Cód. postal de la oficina.
     * @param country
     *            País de la oficina (realmente se utiliza como provincia).
     * @param telephone
     *            Teléfono de la oficina.
     * @param fax
     *            Fax de la oficina.
     * @param mail
     *            Email de la oficina.
     * @return la dirección de la oficina.
     */
    public String formatAddressOfic(String address, String city, String zip, String country,
	    String telephone, String fax, String mail) {
	log.trace("Entrando en ReportDAO.formatAddressOrg()");
	String addressOrg = "";
	if (address != null) {
	    addressOrg += address;
	}
	if (city != null) {
	    addressOrg += ", " + city;
	}
	if (zip != null) {
	    addressOrg += " (" + zip + ")";
	}
	if (country != null) {
	    addressOrg += " " + country;
	}
	if (telephone != null) {
	    addressOrg += " Tel:" + telephone;
	}
	if (fax != null) {
	    addressOrg += " Fax:" + fax;
	}
	if (mail != null) {
	    addressOrg += " Email:" + mail;
	}
	log.trace("Saliendo en ReportDAO.formatAddressOrg()");
	return addressOrg;
    }


    /**
     * Crea un documento XML con los datos del registro
     * @param mapReport datos del registro
     * @param fdrid id del registro
     * @param idBook libro
     * @param useCaseConf  useCaseConf
     * @return Document
     * @throws SigmWSException 
     */
	public Document createXML(Map<String, Object> mapReport, Integer fdrid, int idBook, UseCaseConf useCaseConf) throws SigmWSException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc=null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			
			// INFORME_RP
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(Constants.XML_TAG_INFORME_RP);
			doc.appendChild(rootElement);		
			
			// REGISTRO
			Element elemRegistro = doc.createElement(Constants.XML_TAG_REGISTRO);
			rootElement.appendChild(elemRegistro);
		
			for (Entry<String, Object> entry : mapReport.entrySet()) {
			    if (entry.getValue() != null) {			
			    	
			    	
					if (entry.getValue() instanceof ScrOfic) {
						Element element = doc.createElement(entry.getKey());
					    ScrOfic scrOfic = (ScrOfic) entry.getValue();
				    	elemRegistro.appendChild(element);
			    		
			    		element.appendChild(doc.createTextNode(String.valueOf(scrOfic.getId())));			    			
					}
					else if (entry.getValue() instanceof ScrOrg) {
					    ScrOrg scrOrg = (ScrOrg) entry.getValue();
					    
						Element element = doc.createElement(entry.getKey());
			    		element.appendChild(doc.createTextNode(String.valueOf(scrOrg.getId())));
				    	elemRegistro.appendChild(element);
					}
					else if (entry.getValue() instanceof ScrCa) {
					    ScrCa scrCa = (ScrCa) entry.getValue();
					    
						Element element = doc.createElement(entry.getKey());
			    		element.appendChild(doc.createTextNode(String.valueOf(scrCa.getId())));
				    	elemRegistro.appendChild(element);
					}
					else if (entry.getValue() instanceof Timestamp) {
						Timestamp value = (Timestamp) entry.getValue();
						String fecha = new SimpleDateFormat(Constants.DATE_ACUSE_FORMAT_LONG).format(value);
						
						Element element = doc.createElement(entry.getKey());
			    		element.appendChild(doc.createTextNode(fecha));
				    	elemRegistro.appendChild(element);
					}
					else if (Constants.XML_TAG_EXTENDIDOS.equals(entry.getKey())) {
						Element elementExtendidos = doc.createElement(entry.getKey());
						elemRegistro.appendChild(elementExtendidos);
						 
						Map<String, Object> mapSecciones = (Map<String, Object>) entry.getValue();
						for (Entry<String, Object> entrySecciones : mapSecciones.entrySet()) {
							
					    	Element elementSeccion = doc.createElement(Constants.XML_TAG_SECCION);
					    	elementSeccion.setAttribute(Constants.XML_ATTRIBUTE_NOMBRE, entrySecciones.getKey());					    	
					    	elementExtendidos.appendChild(elementSeccion);
							 
					    	
							Map<String, Object> mapSeccion = (Map<String, Object>) entrySecciones.getValue();
							for (Entry<String, Object> entryElem : mapSeccion.entrySet()) {
						    	
								String elementos = (String) entryElem.getValue();
								String[] split = elementos.split(Constants.XML_VALOR_SEPARATOR);
								
						    	Element elementValue = doc.createElement(Constants.XML_TAG_DATO);
						    	elementSeccion.appendChild(elementValue);			    			
						    	
								Element elemValor = doc.createElement(Constants.XML_TAG_VALOR);
								elemValor.appendChild(doc.createTextNode(String.valueOf(split[0])));
						    	elementValue.appendChild(elemValor);
						    	
								Element elemDescripcion = doc.createElement(Constants.XML_TAG_DESCRIPCION);
								elemDescripcion.appendChild(doc.createTextNode(String.valueOf(split[1])));
						    	elementValue.appendChild(elemDescripcion);
						    	
					    		
							}
						}

			    	} else if (Constants.XML_TAG_DOCUMENTOS.equals(entry.getKey())) {
						Element elementDocumentos = doc.createElement(entry.getKey());
						elemRegistro.appendChild(elementDocumentos);
						 
						List<Map<String, String>> listDocumentos = (List<Map<String, String>>) entry.getValue();
						for (Map<String, String> item: listDocumentos) {
							
							Element elementDocumento = doc.createElement(Constants.XML_TAG_DOCUMENTO);
							
							Element elementNombre = doc.createElement(Constants.XML_TAG_NOMBRE);							
				    		Element elementTamano = doc.createElement(Constants.XML_TAG_TAMANIO);							
				    		Element elementValidez = doc.createElement(Constants.XML_TAG_VALIDEZ);							
				    		Element elementTipo = doc.createElement(Constants.XML_TAG_TIPO);							
				    		Element elementHash = doc.createElement(Constants.XML_TAG_HASH);							
				    		Element elementHashAlg = doc.createElement(Constants.XML_TAG_HASH_ALG);							
				    		Element elementComentario = doc.createElement(Constants.XML_TAG_COMENTARIOS);							
							
							elementNombre.appendChild(doc.createTextNode(item.get(Constants.XML_TAG_NOMBRE)));
							elementDocumento.appendChild(elementNombre);
				    		elementDocumentos.appendChild(elementDocumento);
				    									
				    		elementTamano.appendChild(doc.createTextNode(item.get(Constants.XML_TAG_TAMANIO)));
							elementDocumento.appendChild(elementTamano);
				    		elementDocumentos.appendChild(elementDocumento);
				    		
				    		elementValidez.appendChild(doc.createTextNode(item.get(Constants.XML_TAG_VALIDEZ)));
							elementDocumento.appendChild(elementValidez);
				    		elementDocumentos.appendChild(elementDocumento);
				    		
				    		elementTipo.appendChild(doc.createTextNode(item.get(Constants.XML_TAG_TIPO)));
							elementDocumento.appendChild(elementTipo);
				    		elementDocumentos.appendChild(elementDocumento);
							
							elementHash.appendChild(doc.createTextNode(item.get(Constants.XML_TAG_HASH)));
							elementDocumento.appendChild(elementHash);
				    		elementDocumentos.appendChild(elementDocumento);
				    		
				    		elementHashAlg.appendChild(doc.createTextNode(item.get(Constants.XML_TAG_HASH_ALG)));
							elementDocumento.appendChild(elementHashAlg);
				    		elementDocumentos.appendChild(elementDocumento);
				    		
				    		elementComentario.appendChild(doc.createTextNode(item.get(Constants.XML_TAG_COMENTARIOS)));
							elementDocumento.appendChild(elementComentario);
				    		elementDocumentos.appendChild(elementDocumento);
						}

					
			    	} else if (!"FLD9".equals(entry.getKey())) {
						Element element = doc.createElement(entry.getKey());
			    		element.appendChild(doc.createTextNode(String.valueOf(entry.getValue())));
				    	elemRegistro.appendChild(element);
			    	}
					
					

			    } else if ("FLD9".equals(entry.getKey())) {
			    	InterestedBo interestedBo = new InterestedBo();
			    	String senderList = interestedBo.fillSenderFieldFromSenderListToRelationsReport(fdrid,idBook,useCaseConf);
				    
			    	Element element = doc.createElement(entry.getKey());
		    		element.appendChild(doc.createTextNode(senderList));
		    		elemRegistro.appendChild(element);			    		
			    }
			    
			}
			
			// PROCEEDING
			Element elemProceeding = doc.createElement(Constants.XML_TAG_PROCEEDING);
			elemProceeding.appendChild(doc.createTextNode(Constants.REPORT_PROCEEEDING_TEXT));
			elemRegistro.appendChild(elemProceeding);			
			
			
			
			log.debug("Documento creado: "+new XMLDocument(doc).toString());
 
			
			
			
			
		} catch (ParserConfigurationException e) {
			throw new SigmWSException("err.xml.creation", e);
		}
		
		return doc;
		
	}


	/**
	 * Crea un documento pdf a partir de una plantilla y un documento XML con los datos
	 * @param document documento XML
	 * @param params parámetros
	 * @param reportDir directorio de plantillas 
	 * @param jasperReportFileName nombre de plantilla
	 * @param reportExpression expresion donde empieza la plantilla
	 * @return byte[]
	 * @throws SigmWSException
	 */
	public byte[] buildReportToPdf(Document document, Map<Object, Object> params, String reportDir,
			String jasperReportFileName, String reportExpression) throws SigmWSException {

		byte[] pdfReportByteArray = null;

		try {
			JRXmlDataSource dataSource = new JRXmlDataSource(document, reportExpression);

			File fileReportsDir = new File(reportDir);
			if (!fileReportsDir.exists()) {
				throw new SigmWSException("file.not_found" + new String[] { String.valueOf(fileReportsDir) });
			}

			params.put("REPORT_FILE_RESOLVER", new SimpleFileResolver(new File(reportDir)));

			JasperPrint jasperPrint = JasperFillManager.fillReport(reportDir + jasperReportFileName, params, dataSource);

			pdfReportByteArray = JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (JRException e) {
			throw new SigmWSException("jasper.excepcion", e);
		}

		return pdfReportByteArray;
	}

	public void addDocuments(Map<String, Object> mapReport, String sessionID, int idBook,
			Integer fdrid, String sigemEntidadMsssi) throws BookException, SessionException, ValidationException, SigmWSException {
		
		List<Map<String, String>> listDocumentos = null;
		
		
		try {
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("idBook", idBook);
			params.put("fdrid", fdrid);
			
			List<Axpageh> listAxpageh = SIGMServiceManager.getAxpagehService().getByFdrid(params);
			
			if(listAxpageh!=null){
				
				for (Axpageh page : listAxpageh) {
					
					if(listDocumentos == null)
						listDocumentos = new ArrayList<Map<String, String>>();			
				 
					Map<String, String> datosDocumento = new HashMap<String, String>();

					datosDocumento.put(Constants.XML_TAG_NOMBRE, page.getName());
					datosDocumento.put(Constants.XML_TAG_TAMANIO, (page.getFileSize() != null) ? String.valueOf(page.getFileSize()/1024):null);
					datosDocumento.put(Constants.XML_TAG_VALIDEZ, (page.getNameValidityType() != null) ? page.getNameValidityType(): "");
					datosDocumento.put(Constants.XML_TAG_TIPO, page.getNameDocumentType());
					datosDocumento.put(Constants.XML_TAG_HASH, (page.getHash() != null) ? new String(Base64.decode(page.getHash())):null);
					datosDocumento.put(Constants.XML_TAG_HASH_ALG, (page.getHashAlg() != null) ? page.getHashAlg(): "");
					datosDocumento.put(Constants.XML_TAG_COMENTARIOS, (page.getComments() != null) ? page.getComments(): "");

					listDocumentos.add(datosDocumento);
				}
				
				
			}
			
		} catch (DaoException e) {
			throw new SigmWSException("err.sigm.query.docelect", new String[]{String.valueOf(fdrid)}, e);
		}

		
		
//		List<AxDoch> docs = null;
//		docs = FolderFileSession.getBookFolderDocsWithPages(sessionID, idBook, fdrid,
//				Constants.SIGEM_ENTIDAD_MSSSI);
//		if (docs != null && docs.size() != 0) {
//			log.debug("Documentos: " + docs.size());
//			for (AxDoch doc : docs) {
//				List pages = doc.getPages();
//				if (pages.size() == 0)
//					continue;
//				AxPageh page = (AxPageh) pages.get(0);
//				log.debug("Nombre completo: " + page.getName());			
//				 
//				if(listDocumentos == null)
//					listDocumentos = new ArrayList<String>();
//
//				String documento = page.getName();
//				try {
//					String findHash = DBEntityDAOFactory
//							.getCurrentDBEntityDAO().getHashDocument(idBook,
//									fdrid, page.getId() , null,
//									true, Constants.SIGEM_ENTIDAD_MSSSI);
//
//					String decryptHashDocument = Hex.encode(Base64.decode(CryptoUtils.decryptHashDocument(findHash)),false);
//					log.debug("Hash: "+decryptHashDocument);
//					
//
//				documento += Constants.XML_VALOR_SEPARATOR+decryptHashDocument.toUpperCase();
//
//				listDocumentos.add(documento);
//
//				} catch (Exception e) {
//					throw new SigmWSException("err.sigm.query.hash");
//				}			
//			}
//
//		}
		
		if(listDocumentos !=null)
			mapReport.put(Constants.XML_TAG_DOCUMENTOS, listDocumentos);
		
	}
  
}
