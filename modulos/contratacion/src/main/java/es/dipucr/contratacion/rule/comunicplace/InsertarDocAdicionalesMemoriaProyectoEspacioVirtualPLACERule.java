package es.dipucr.contratacion.rule.comunicplace;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCopy;

import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import es.dipucr.contratacion.services.PlataformaContratacionStub.Documento;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class InsertarDocAdicionalesMemoriaProyectoEspacioVirtualPLACERule  implements IRule{
	
	public static final Logger LOGGER = Logger.getLogger(InsertarDocAdicionalesMemoriaProyectoEspacioVirtualPLACERule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		Documento[] docAdicional = DipucrFuncionesComunes.docInformacionAdicionalPliego(rulectx);
		
		File fichero = null;
		try{
			
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------			
			
			fichero = new File (FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance().newFileName(".pdf"));
			
			Document documentInforme = new Document();
			PdfCopy.getInstance(documentInforme,new FileOutputStream(fichero));
			documentInforme.open();
			
			DipucrFuncionesComunes.insertarImagenes(rulectx, documentInforme);
			
			documentInforme.add(new Phrase("\n"));

			documentInforme.add(new Phrase("INFORME SOBRE LOS DOCUMENTOS ANEXADOS DEL EXPEDIENTE DE CONTRATACIÓN"));
			documentInforme.add(new Phrase("\n"));
			documentInforme.add(new Phrase("\n"));		
			
//			for(int i=0; docAdicional!=null && i < docAdicional.length; i++){
//				
//				documentInforme.add(new Phrase(" - DOCUMENTO "+ i + "- Nombre: "+docAdicional[i].getNameDoc()));
//				documentInforme.add(new Phrase("\n"));
//				
//				DatosContrato datContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
//				docAdicional[i].setOrganoContratacion(datContrato.getOrganoContratacion());
//				
//				PlataformaContratacionStub platContratacion = new PlataformaContratacionStub(ServiciosWebContratacionFunciones.getDireccionSW());
//				int timeout = 10 * 60 * 1000; // 10 minutos
//				platContratacion._getServiceClient().getOptions().setProperty(HTTPConstants.SO_TIMEOUT, new Integer(timeout));
//				platContratacion._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(timeout));
//				
//				EnvioOtrosDocumentosEspacioVirtual envioOtrosDocumentosEspacioVirtual = new EnvioOtrosDocumentosEspacioVirtual();
//				envioOtrosDocumentosEspacioVirtual.setDocumentoAdicional(docAdicional[i]);
//				envioOtrosDocumentosEspacioVirtual.setEntidad(EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()));
//				envioOtrosDocumentosEspacioVirtual.setPublishedByUser(UsuariosUtil.getDni((ClientContext) rulectx.getClientContext()));
//				envioOtrosDocumentosEspacioVirtual.setOrganoContratacion(datContrato.getOrganoContratacion());
//				EnvioOtrosDocumentosEspacioVirtualResponse resultadoResponse = platContratacion.envioOtrosDocumentosEspacioVirtual(envioOtrosDocumentosEspacioVirtual);
//				
//				
//				if(resultadoResponse!=null){
//					Resultado resultado = resultadoResponse.get_return();
//					if(resultado.getPublicacion()!=null){
//						PublicationResult publicacion = resultado.getPublicacion();
//						documentInforme.add(new Phrase(publicacion.getResultCode()));
//						documentInforme.add(new Phrase(publicacion.getResultCodeDescription()));
//					}
//				}
//				
//				
//			}
			documentInforme.close();
			
			if (fichero != null) {
				IItemCollection tipDoc = entitiesAPI.queryEntities("SPAC_CT_TPDOC","WHERE NOMBRE='Información de Plataforma Contratación'");
				Iterator<IItem> tipDocIterator = tipDoc.iterator();
				int idTipDoc = 0;
				if (tipDocIterator.hasNext())
					idTipDoc  = ((IItem) tipDocIterator.next()).getInt("ID");
				
				DocumentosUtil.generaYAnexaDocumento(rulectx, idTipDoc, "Informe documentos expedientes", fichero, Constants._EXTENSION_PDF);
			}
//		} catch (RemoteException e) {
//			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
//			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
//		} catch (PlataformaContratacionDatatypeConfigurationExceptionException e) {
//			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
//			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
//		} catch (PlataformaContratacionUnsupportedEncodingExceptionException e) {
//			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
//			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
//		} catch (PlataformaContratacionMalformedURLExceptionException e) {
//			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
//			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
//		} catch (PlataformaContratacionJAXBExceptionException e) {
//			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
//			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (DocumentException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}finally{
			if(fichero!=null){
				fichero.delete();
			}
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
