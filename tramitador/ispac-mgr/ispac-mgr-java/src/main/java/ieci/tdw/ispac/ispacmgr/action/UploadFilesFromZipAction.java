package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispacmgr.action.form.UploadForm;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FileBean;
import es.dipucr.sigem.api.rule.common.utils.ZipUtils;

public class UploadFilesFromZipAction extends BaseAction {

	public final static String FILE_EXTENSION = "ZIP";
	
	public ActionForward executeAction(ActionMapping mapping,
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception	{

        ClientContext cct = session.getClientContext();
        ///////////////////////////////////////////////
		// Se obtiene el estado de tramitación
        IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
   	    IState currentState = managerAPI.currentState(getStateticket(request));

		int taskId = currentState.getTaskId();

		// Obtener el fichero a subir
		UploadForm uploadForm = (UploadForm) form;
		FormFile formFile = uploadForm.getTheFile();

		// Comprobar si ha llegado el fichero
		if (formFile == null) {
			throw new ISPACInfo(getResources(request).getMessage("exception.message.errorAttach"), false);
		}

		// Comprobar si el fichero tiene contenido
		if (formFile.getFileSize() == 0) {
			throw new ISPACInfo(getResources(request).getMessage("exception.message.attachFileEmpty"), false);
		}
		
		// Validar la extensión del fichero
		// String sFile = formFile.getFileName();
		// Obtener el nombre del fichero del campo oculto
		// evitando la mala codificación del nombre en el FormFile
		String sFileName = uploadForm.getTheFileName();
		String sExtension = FileUtils.getFileExtension(sFileName);
		String sMimeType = MimetypeMapping.getMimeType(sExtension);

		if (sMimeType == null) {

			//sMimeType = formFile.getContentType();
		    //throw new ISPACInfo("Tipo de fichero no permitido");
		    throw new ISPACInfo(getResources(request).getMessage("exception.message.typeNotAttach"),false);
		}
		if (!FILE_EXTENSION.equalsIgnoreCase(sExtension)){

		    throw new ISPACInfo(getResources(request).getMessage("exception.message.extensionNotZip"),false);
		}

		// Ejecución en un contexto transaccional
		boolean bCommit = false;

		try {
			
			int documentTypeId = Integer.parseInt(request.getParameter("documentTypeId"));

			FileTemporaryManager ftm = FileTemporaryManager.getInstance();
			File fileZip = ftm.newFile(sExtension);
			org.apache.commons.io.FileUtils.writeByteArrayToFile(fileZip, formFile.getFileData());
			
			List<FileBean> lisFilesZip = ZipUtils.extraerTodosFicheros(fileZip);
			
			for (FileBean filebean : lisFilesZip){
				
				String extensionSubfile = es.dipucr.sigem.api.rule.common.utils.FileUtils.getExtensionByNombreDoc(filebean.getName());
				DocumentosUtil.generaYAnexaDocumento(cct, taskId, documentTypeId, filebean.getName(), filebean.getFile(), extensionSubfile);
			}
			
		}
		catch (Exception e) {

			throw new ISPACInfo(getResources(request).getMessage("exception.message.canNotAttach")+ ": " + e.getLocalizedMessage(), false);
		}
		finally {
			cct.endTX(bCommit);
		}

		return mapping.findForward("success");
	}

}
