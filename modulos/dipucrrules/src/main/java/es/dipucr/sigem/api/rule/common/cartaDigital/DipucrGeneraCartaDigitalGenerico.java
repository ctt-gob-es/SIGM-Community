package es.dipucr.sigem.api.rule.common.cartaDigital;

import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class DipucrGeneraCartaDigitalGenerico implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrGeneraCartaDigitalGenerico.class);
	private String plantillaCartaDigital = "";
	private String tipoDocumento = "";
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {		
		try{
			logger.info("INICIO - " + this.getClass().getName());
			plantillaCartaDigital = DocumentosUtil.getPlantillaDefecto(rulectx.getClientContext(), rulectx.getTaskProcedureId());

			if (StringUtils.isNotEmpty(plantillaCartaDigital)) {
				tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(rulectx.getClientContext(), plantillaCartaDigital);
			}
		}
		catch(ISPACException e){
			logger.error("Error al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		try{
		
			generaCarta(rulectx);
			logger.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) {
			logger.error("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR, " + e.getMessage(), e);
			throw new ISPACRuleException("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR, " + e.getMessage(), e);	    	
	    }
		return null;
	}

	private void generaCarta(IRuleContext rulectx) throws ISPACRuleException {
		String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
		OpenOfficeHelper ooHelper = null;
		try{
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			cct.endTX(true);					
			
			
			int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);	
					
			IItem itDoc = DocumentosUtil.generarDocumento(rulectx, plantillaCartaDigital, plantillaCartaDigital);
			
			//Abre el documento
			//String extension = "odt";
			String fileName = FileTemporaryManager.getInstance().newFileName("."+extensionEntidad);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			OutputStream out = new FileOutputStream(fileName);
			Object connectorSession = genDocAPI.createConnectorSession();
    		genDocAPI.getDocument(connectorSession, itDoc.getString("INFOPAG"), out);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + fileName);
    		
    		IItem doc = DocumentosUtil.getUltimaPropuestaFirmada(cct, rulectx.getNumExp());
    		if(doc != null){
    			String infoPag = doc.getString("INFOPAG");
	        	File fileBases = DocumentosUtil.getFile(cct, infoPag, null, null);
	        	DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + fileBases.getPath(), extensionEntidad);
	        	fileBases.delete();
			}
    		
    		 //Guarda el documento
			String fileNameOut = FileTemporaryManager.getInstance().newFileName("."+extensionEntidad);
			fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
			
			String filter = DocumentosUtil.getFiltroOpenOffice(extensionEntidad);
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut, filter);
    		File fileOut = new File(fileNameOut);    		

			DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), documentTypeId, plantillaCartaDigital, fileOut, extensionEntidad);
			
			itDoc.delete(cct);

    		cct.deleteSsVariable("NOMBRE_TRAMITE");
    		
    		//Borra archivos temporales

    		fileOut.delete();
			//[eCenpri_Manu Ticket #249]
    		if(null != out){
    			out.close();
    		}
    		
    		out.flush();		
					
			cct.endTX(true);
		} catch(Exception e) {
			logger.error("Error al generar la carta digital en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la carta digital en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	    	
	    } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
	}

	public void deleteSsVariables(IClientContext cct) {	
	}	
}
