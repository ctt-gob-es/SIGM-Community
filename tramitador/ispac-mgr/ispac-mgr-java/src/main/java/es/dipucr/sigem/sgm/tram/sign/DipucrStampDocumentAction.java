package es.dipucr.sigem.sgm.tram.sign;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.gendoc.stamp.StampConnector;
import ieci.tdw.ispac.ispaclib.gendoc.stamp.StampConnectorFactory;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;

import java.io.File;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class DipucrStampDocumentAction extends BaseAction {
	
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		// Obtener el id del documento a sellar
		String documentId = request.getParameter("documentId");
		
		
		
		if (StringUtils.isNotBlank(documentId)) {

			File stampedDoc = null;
			
			try {
		  	    IInvesflowAPI invesFlowAPI = session.getAPI();
				IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	
		  		// Obtener el documento a sellar con el registro
		  		IItem document = DocumentosUtil.getDocumento(entitiesAPI, TypeConverter.parseInt(documentId));
		  		
		  	    // Compruebo la extensión del documento para ver si es pdf.
		  		String extDocum ="";
		  		String tipoRegistro = "";
		  		String numRegistro="";
		  		String fechaRegistro="";
		  		String departamento = "";
		  		
		  		if (document.getString("EXTENSION_RDE")!=null) extDocum = document.getString("EXTENSION_RDE"); else extDocum="";
		  		
		  		if(extDocum.equals("pdf")){
		  			String infoPag = document.getString("INFOPAG");
		  			String infoPagRDE = document.getString("INFOPAG_RDE");
		  			
		  			File file=null;
		  			DipucrSignConnector dSigConec = new DipucrSignConnector();
		  			if(StringUtils.isNotBlank(infoPagRDE)){
		  				 file = DocumentosUtil.getFile(session.getClientContext(), infoPagRDE, null, null);
		  			} else {
		  				 file = DocumentosUtil.getFile(session.getClientContext(), infoPag, null, null);
		  			}
		  			
		  			String pathFileTemp = FileTemporaryManager.getInstance().put(file.getAbsolutePath(), ".pdf");
		  			if (document.getString("TP_REG")!=null) tipoRegistro = document.getString("TP_REG"); else tipoRegistro="";
		  			if (document.getString("NREG")!=null) numRegistro = document.getString("NREG"); else numRegistro="";
		  			if (document.getString("FREG")!=null) fechaRegistro = document.getString("FREG"); else fechaRegistro="";
		  			if (document.getString("ORIGEN")!=null) departamento = document.getString("ORIGEN"); else departamento="";
		  			
		  			dSigConec.addGrayBand(file, pathFileTemp, infoPagRDE, tipoRegistro, numRegistro, fechaRegistro, departamento);

		  			
		  			String fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
		  			
		  			
		  			File fileSello = new File(fileName);	  			
	  			  			
		        	
		        	response.setContentType(MimetypeMapping.getMimeType("pdf"));
			    	response.setHeader("Pragma", "public");
			    	response.setHeader("Cache-Control", "max-age=0");
		            response.setHeader("Content-Transfer-Encoding", "binary");
		            response.setHeader("Content-Disposition", "attachment; filename=\"" + pathFileTemp + "\"");
					response.setContentLength((int) fileSello.length());
					
					ServletOutputStream ouputStream = response.getOutputStream();
					FileUtils.copy(fileSello, ouputStream);
					ouputStream.flush();
					ouputStream.close(); 			
		  		
		  		}
		  		else{
		  		// Sellar el documento
					StampConnector stampConnector = StampConnectorFactory.getInstance().getStampConnector();
					stampedDoc = stampConnector.stampDocument(session.getClientContext(),
							document, getResources(request));
		
		            // Descargar el documento sellado
		            response.setContentType(MimetypeMapping.getMimeType(document.getString("EXTENSION")));
			    	response.setHeader("Pragma", "public");
			    	response.setHeader("Cache-Control", "max-age=0");
		            response.setHeader("Content-Transfer-Encoding", "binary");
		            response.setHeader("Content-Disposition", "attachment; filename=\"" 
		            		+ document.getString("NOMBRE") + "." 
		            		+ document.getString("EXTENSION") + "\"");
					response.setContentLength((int) stampedDoc.length());
					
					ServletOutputStream ouputStream = response.getOutputStream();
					FileUtils.copy(stampedDoc, ouputStream);
					ouputStream.flush();
					ouputStream.close();
		  		}
				
			} catch (ISPACInfo e) {
				LOGGER.error("Error al sellar el documento", e);
				e.setRefresh(false);
				throw e;
			} catch (Exception e) {
				LOGGER.error("Error al sellar el documento", e);
				throw new ISPACInfo(e,false);
			} finally {
				if (stampedDoc != null) {
					stampedDoc.delete();
				}
			}
		}

		return null;
	}
}
