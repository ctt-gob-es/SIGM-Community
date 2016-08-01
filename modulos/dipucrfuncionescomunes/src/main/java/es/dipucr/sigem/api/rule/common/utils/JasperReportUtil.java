package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;

public class JasperReportUtil {
	
	public static final Logger logger = Logger.getLogger(JasperReportUtil.class);
	
	public static JRXmlDataSource crearDataSourceXML(File ficheroXML, String expresion) throws ISPACRuleException {
		JRXmlDataSource jRXmlDataSource = null;
		try {
			jRXmlDataSource = new JRXmlDataSource(ficheroXML, expresion);
		} catch (JRException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return jRXmlDataSource;
	}

	public static JasperReport obtenerObjetoJasper(String nombreCarpeta, String nombrePlantillaJasper) throws ISPACRuleException {
		JasperReport jasperReport = null;
		
		try {
			//Compruebo que el nombre introducido no tenga la extension
			String [] vNombrePlantilla = nombrePlantillaJasper.split("\\.");
			String extensionPlantillaJasper = "";
			String sNombrePlantillaJasper = vNombrePlantilla[0];
			if(vNombrePlantilla.length>0){
				extensionPlantillaJasper = vNombrePlantilla[1];
			}
			else{
				extensionPlantillaJasper = "jasper";
			}
			
			/**
			 * [Ticket #59# INICIO]ALSIGM3 incluir la carpeta report del SIGEM 2.0 dentro del proyecto SIGEM_TramitacionWeb del ALSIGM 3.0 
			 * **/
			String ruta = Thread.currentThread().getContextClassLoader().getResource("../../report").getPath();
			if(nombreCarpeta!=null && !StringUtils.isEmpty(nombreCarpeta)){
				ruta=ruta+nombreCarpeta+File.separator;
			}			
			
			ruta = ruta+sNombrePlantillaJasper+"."+extensionPlantillaJasper;
			
			/**
			 * [Ticket #59# FIN]ALSIGM3 incluir la carpeta report del SIGEM 2.0 dentro del proyecto SIGEM_TramitacionWeb del ALSIGM 3.0 
			 * **/
			File fPantiJasp = new File(ruta);
			if(fPantiJasp.exists()){
				//La pantilla existe
				if(extensionPlantillaJasper.equals("jasper")){
					
					jasperReport = (JasperReport) JRLoader.loadObject(ruta);
				}
				if(vNombrePlantilla[1].equals("jrxml")){
					JasperDesign jd=JRXmlLoader.load(ruta);								
					jasperReport = JasperCompileManager.compileReport(jd);   
				}
			}

		} catch (JRException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return jasperReport;
	}
	
	public static void exportarReportAPdf(String fileDocParcelas, JasperPrint print) throws ISPACRuleException {
		try{
			JasperExportManager.exportReportToPdfFile(print, fileDocParcelas);
		}catch (JRException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al exportar el Jasper a PDF. ",e);
		}
	}
	
	public static  JasperPrint rellenarInforme(JasperReport report, HashMap<String, String> map, JRBeanCollectionDataSource ds) throws ISPACRuleException {
		JasperPrint print = null;
		try{
			print = JasperFillManager.fillReport(report,map, ds);
		}catch (JRException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al rellenar el Informe Jasper desde un objeto. ",e);
		}
		return print;
	}
	
	public static JasperPrint rellenarInforme(JasperReport report, HashMap<String, String> map, JRXmlDataSource ds) throws ISPACRuleException {
		JasperPrint print = null;
		try{
			print = JasperFillManager.fillReport(report,map, ds);
		}catch (JRException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al rellenar el Informe Jasper desde un XML. ",e);
		}
		return print;
	}
	
	public static File obtenerPdftoXml(IRuleContext rulectx, IItem docXML, String rutaXml, String nombreCarpetaJasper, String nombreJasper) throws ISPACRuleException {
		File fPdf = null;
		try {
			JRXmlDataSource ds = JasperReportUtil.crearDataSourceXML(
					DocumentosUtil.getFile(rulectx.getClientContext(), docXML.getString("INFOPAG"), docXML.getString("NOMBRE"),
							docXML.getString("EXTENSION")),rutaXml);
			if (ds != null) {
				/**
				 * DOCUMENTO CON TODOS LOS CONTRATOS
				 * **/

				String fileDoc= FileTemporaryManager.getInstance().getFileTemporaryPath() + File.separator
						+ FileTemporaryManager.getInstance().newFileName(".pdf");
				fPdf = new File(fileDoc);

				JasperReport report = JasperReportUtil.obtenerObjetoJasper(nombreCarpetaJasper, nombreJasper);

				HashMap<String, String> map = new HashMap<String, String>();
				//map.put("IMAGES_REPOSITORY_PATH", FileTemplateManager.getInstance().getFileMgrPath());
				/**
				 * [Ticket #59# INICIO]Cambio para que utilice la carpeta skin 
				 * **/
				map.put("IMAGES_REPOSITORY_PATH", SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb"));
				/**
				 * [Ticket #59# FIN]
				 * **/

				// Rellenamos el informe con la conexion creada y sus parametros
				// establecidos
				JasperPrint print = JasperReportUtil.rellenarInforme(report, map, ds);

				if (!fPdf.exists()) {
					JasperReportUtil.exportarReportAPdf(fileDoc, print);
				}
			}
		} catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}

		return fPdf;
	}


}
