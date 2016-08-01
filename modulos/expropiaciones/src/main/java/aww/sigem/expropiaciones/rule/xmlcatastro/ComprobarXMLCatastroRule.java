package aww.sigem.expropiaciones.rule.xmlcatastro;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.JasperReportUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ComprobarXMLCatastroRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(ComprobarXMLCatastroRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();

	        //------------------------------------------------------------------
	        
	        String refDoc = null;
			java.sql.Timestamp fDoc = null;
			
			IItem tramExcel = TramitesUtil.getTramiteByCode(rulectx, "xml-catastro");
			
			String consulta = "WHERE ID_TRAM_CTL = "+tramExcel.getInt("ID")+" AND NUMEXP='"+rulectx.getNumExp()+"'";
			IItemCollection tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
			Iterator<IItem> iterator = tramspacDtTramite.iterator();
			IItem itTramitedt = iterator.next();
			
			IItemCollection taskDocumentosColeccion = DocumentosUtil.getDocumentosByTramites(rulectx, rulectx.getNumExp(), itTramitedt.getInt("ID_TRAM_EXP"));
			
			Iterator<IItem> itDocumentos = taskDocumentosColeccion.iterator();
			JRXmlDataSource ds = null;
			while(itDocumentos.hasNext()){
				//logger.warn("Documento");
				IItem documento = (IItem) itDocumentos.next();
				java.sql.Timestamp fItem = (java.sql.Timestamp) documento.get("FDOC");
				//logger.warn("FDoc: " + fItem);
				if(fDoc!=null) {
					if(fItem.after(fDoc)) {
						fDoc = fItem;
						refDoc = documento.getString("INFOPAG");
					}
				} else {
					fDoc = fItem;
					refDoc = (String) documento.getString("INFOPAG");
				}
				ds = JasperReportUtil.crearDataSourceXML(DocumentosUtil.getFile(cct, refDoc, documento.getString("NOMBRE"), documento.getString("EXTENSION")),
						"/DS/LDS/DSA");
				
			}
			
			if(ds != null){
				/**
				 * DOCUMENTO CON TODAS LAS PARCELAS
				 * **/
				
				String fileDocParcelas = FileTemporaryManager.getInstance().getFileTemporaryPath()+"/"+FileTemporaryManager.getInstance().newFileName(".pdf");
				File ffilePathDocParcelas = new File(fileDocParcelas);
				try {
					ffilePathDocParcelas.createNewFile();
				} catch (IOException e) {
					logger.error("Error al crear el documento. "+ffilePathDocParcelas.getPath()+"-"+e.getMessage(), e);
					throw new ISPACRuleException("Error al crear el documento. "+ffilePathDocParcelas.getPath()+"-"+e.getMessage(), e);
				}
				

				JasperReport report = JasperReportUtil.obtenerObjetoJasper("Expropiaciones", "DatosExpropiacionFinca.jasper");
				
				if(report!=null){
					HashMap<String, String> map = new HashMap<String, String>();
					/**
					 * [Teresa Ticket #59 INICIO]Cambio de la ruta de las imágenes y del subreport 
					 * **/
					//map.put("IMAGES_REPOSITORY_PATH",FileTemplateManager.getInstance().getFileMgrPath());
					map.put("IMAGES_REPOSITORY_PATH", SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb"));
					map.put("SUBREPORT_DIR",SigemConfigFilePathResolver.getInstance().resolveFullPath("reports", "/SIGEM_TramitacionWeb"));
					/**
					 * [Teresa Ticket #59 FIN]Cambio de la ruta de las imágenes y del subreport 
					 * **/
					//Rellenamos el informe con la conexion creada y sus parametros establecidos   
					JasperPrint print = JasperReportUtil.rellenarInforme(report, map, ds);			
					
					if(ffilePathDocParcelas.exists()){
						JasperReportUtil.exportarReportAPdf(fileDocParcelas, print);
					}  
					
					
					
					int idTpdocNot = DocumentosUtil.getIdTipoDocByCodigo(cct, "Inf-Catastro");
					String sTpdoc = DocumentosUtil.getTipoDocNombreByCodigo(cct, "Inf-Catastro");
					
					IItem entityDocumentParcela = DocumentosUtil.generaYAnexaDocumento(cct, rulectx.getTaskId(), idTpdocNot, sTpdoc+" - Parcelas",ffilePathDocParcelas, Constants._EXTENSION_PDF);
					entityDocumentParcela.store(cct);
				}
			}
	        
		}catch(ISPACException e) 
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
        } 
		return new Boolean (true);
	}

	

	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

}
