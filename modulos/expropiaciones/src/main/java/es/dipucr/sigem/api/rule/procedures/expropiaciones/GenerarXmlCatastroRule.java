package es.dipucr.sigem.api.rule.procedures.expropiaciones;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExcelUtils;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class GenerarXmlCatastroRule implements IRule {
	private final Logger logger = Logger
			.getLogger(GenerarXmlCatastroRule.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {

	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			/****************************************************************/
			IClientContext cct = rulectx.getClientContext();
			/****************************************************************/
			logger.warn("INICIO");
			IItem tramExcel = TramitesUtil.getTramiteByCode(rulectx, "excel_expropiaci");
			
			String consulta = "WHERE ID_TRAM_CTL = "+tramExcel.getInt("ID")+" AND NUMEXP='"+rulectx.getNumExp()+"'";
			IItemCollection tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
			Iterator<IItem> iterator = tramspacDtTramite.iterator();
			IItem itTramitedt = iterator.next();
			
			IItemCollection itColExcel = DocumentosUtil
					.getDocumentosByTramites(rulectx, rulectx.getNumExp(), itTramitedt.getInt("ID_TRAM_EXP"));
			
			Iterator<IItem> itExcel = itColExcel.iterator();
			if (itExcel.hasNext()) {
				IItem itdocExel = itExcel.next();
				String infopag = itdocExel.getString("INFOPAG");
				String extension = itdocExel.getString("EXTENSION");
				File fileExcel = DocumentosUtil.getFile(cct, infopag, "", extension);
				List<List<String>> tablaExpro = ExcelUtils.getAllBySheet(fileExcel, 0);

				File file = montarXML(ExpedientesUtil.getExpediente(cct, rulectx.getNumExp()).getString("ASUNTO"), tablaExpro);
				
				//creo el trámite de Generar XML para Catastro
				IItem codTipoDoc = DocumentosUtil.getTipoDocByCodigo(cct, "xml_catastro");
				int id = codTipoDoc.getInt("ID");
				String nombre = codTipoDoc.getString("NOMBRE");
				DocumentosUtil.generaYAnexaDocumento(rulectx, id, nombre, file, "xml");

				// creamos un nodo para agregarle a la raíz, el cual va a
				// contener algún texto
			}

		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}
		return new Boolean(true);
	}

	private File montarXML(String finalidad, List<List<String>> tablaExpro) throws ISPACRuleException {
		File resultado = null;
		try {
			// Montar el xml
			// Creamos primero los constructores, factory’s y demás
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation implementation = builder.getDOMImplementation();
			
			// creamos el objeto Document, lo que vendría a ser nuestra raíz del
			// arbol XML y también le seteamos la versión de XML
			Document document = implementation.createDocument(null,"LISTADATOS", null);
			document.setXmlVersion("1.0");

			// Obtenemos la raíz
			Element raiz = document.getDocumentElement();
			
			//creamos un nodo para agregarle a la raíz, el cual va a contener algún texto
			//<FEC>26/06/2014</FEC>
			Element nodoNombreCampoFecha = document.createElement("FEC"); //creamos un nuevo elemento
			SimpleDateFormat sm = new SimpleDateFormat("dd/mm/yyyy");
			String strDate = sm.format(new Date());
			Text nodoValorCampo = document.createTextNode(strDate); //Ingresamos la info				
			nodoNombreCampoFecha.appendChild(nodoValorCampo); 						
			raiz.appendChild(nodoNombreCampoFecha); //pegamos el elemento a la raiz "Documento"
			//<FIN>Expr Argamasilla</FIN>
			Element  nodoNombreCampoFinal = document.createElement("FIN"); //creamos un nuevo elemento
			nodoValorCampo = document.createTextNode(finalidad); //Ingresamos la info				
			nodoNombreCampoFinal.appendChild(nodoValorCampo); 						
			raiz.appendChild(nodoNombreCampoFinal);
			//LIstado de valores.
			/**
			 * <DAT>
			 * 		<PRO>13</PRO>
			 * 		<MUN>20</MUN>
			 * 		<POL>1</POL>
			 * 		<PAR>123</PAR>
			 * </DAT>
			 * **/
			//Empiezo en uno por el título  de la tabla
			for(int i=1; i< tablaExpro.size(); i++){
				List<String> filas = tablaExpro.get(i);
				Element nodoNombreCampoDat = document.createElement("DAT"); //creamos un nuevo elemento
				
				//Referencia Catastral
				if(!StringUtils.isEmpty(filas.get(0))){
					Element nodoNombreCampoRefCat = document.createElement("RC");
					Text nodoValorCampoRefCat = document.createTextNode(filas.get(0)); //Ingresamos la info				
					nodoNombreCampoRefCat.appendChild(nodoValorCampoRefCat); 
					nodoNombreCampoDat.appendChild(nodoNombreCampoRefCat);
				}				
				
				//Provincia
				if(!StringUtils.isEmpty(filas.get(1))){
					Element nodoNombreCampoProvincia = document.createElement("PRO");
					Text nodoValorCampoProvincia = document.createTextNode(filas.get(1)); //Ingresamos la info				
					nodoNombreCampoProvincia.appendChild(nodoValorCampoProvincia); 
					nodoNombreCampoDat.appendChild(nodoNombreCampoProvincia);
				}				
				
				//Municipio
				if(!StringUtils.isEmpty(filas.get(2))){
					Element nodoNombreCampoMunicipio = document.createElement("MUN");
					Text nodoValorCampoMunicipio = document.createTextNode(filas.get(2)); //Ingresamos la info				
					nodoNombreCampoMunicipio.appendChild(nodoValorCampoMunicipio); 
					nodoNombreCampoDat.appendChild(nodoNombreCampoMunicipio);
				}				
				
				//Poligono
				if(!StringUtils.isEmpty(filas.get(3))){
					Element nodoNombreCampoPoligono = document.createElement("POL");
					Text nodoValorCampoPoligono = document.createTextNode(filas.get(3)); //Ingresamos la info				
					nodoNombreCampoPoligono.appendChild(nodoValorCampoPoligono); 
					nodoNombreCampoDat.appendChild(nodoNombreCampoPoligono);
				}				
				
				//Parcela
				if(!StringUtils.isEmpty(filas.get(4))){
					Element nodoNombreCampoParcela = document.createElement("PAR");
					Text nodoValorCampoParcela = document.createTextNode(filas.get(4)); //Ingresamos la info				
					nodoNombreCampoParcela.appendChild(nodoValorCampoParcela); 
					nodoNombreCampoDat.appendChild(nodoNombreCampoParcela);
				}				
				
				raiz.appendChild(nodoNombreCampoDat); 
				
			}
			
			
			
			// guardar ese XML creado en un archivo, array de byte o lo que queramos 
			Source source = new DOMSource(document);
			String pathFileTemp = FileTemporaryManager.getInstance().getFileTemporaryPath()+"/"+FileTemporaryManager.getInstance().newFileName(".xml");
			resultado = new java.io.File(pathFileTemp);
			Result result = new StreamResult(resultado); //nombre del archivo
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
			
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		} catch (TransformerConfigurationException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		} catch (TransformerFactoryConfigurationError e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		} catch (TransformerException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}
		return resultado;
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}


	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean res = false;
		try {
			
			IItemCollection itColExcel = DocumentosUtil.getDocumentosByCodTramites(rulectx, rulectx.getNumExp(), "excel_expropiaci");
			if (itColExcel.toList().size() != 1) {
				rulectx.setInfoMessage("No se admite más de un documento o falta introducir el documento excel de la expropiación.");
			}
			else{
				res = true;
			}
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}
		return res;
	}

}
