package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class TramiteCertifNotifIndividualizadoRule implements IRule{
	
	private OpenOfficeHelper ooHelper = null;
	private static final Logger logger = Logger.getLogger(TramiteCertifNotifIndividualizadoRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
		
	}
	/**
	 * [Ticket#380# Teresa]
	 * SIGEM Secretaria cambiar trámite de notificaciones para poder terminar el trámite habiendo certificados sin firmar
	 * Comprobar si existen propuestas sin firmar si no existen no se crea el trámite.
	 * **/
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		boolean validate = false;
		try{
			//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			//----------------------------------------------------------------------------------------------
			logger.info("INICIO - " + this.getClass().getName());
			
			ooHelper = OpenOfficeHelper.getInstance();
			boolean urgencia = false;
			/**
			 * [Ticket#380# Teresa]
			 * SIGEM Secretaria cambiar trámite de notificaciones para poder terminar el trámite habiendo certificados sin firmar
			 * Comprobar si existen propuestas sin firmar si no existen no se crea el trámite.
			 * **/
	        IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Propuesta' OR NOMBRE='Propuesta Urgencia'", "");
	        Iterator it = collection.iterator();
	        while (it.hasNext())
	        {
	        	String descripcionPropuesta = "";
	        	IItem docPropuesta = (IItem)it.next();
	        	if (docPropuesta.getString("DESCRIPCION")!=null) descripcionPropuesta = (String)docPropuesta.getString("DESCRIPCION"); else descripcionPropuesta = "";
	        	
	        	String [] vDesc = descripcionPropuesta.split(" - ");
	        	//Propuesta - 5 . Adhesión de la Excma. Diputación Provincial de C, numexp=DPCR2010/9933
	        	//Propuesta Urgencia - 1 . Aprobación inicial del expediente de modificación de créditos, numexp=DPCR2010/10152
	        	if(vDesc.length >= 2){
	        		String tipoPropuesta = vDesc[0];
		        	String nombrePropuesta = vDesc[1];
		        	String [] vNombrProp = nombrePropuesta.split(" . ");
		        	if(vNombrProp.length > 1){
		        		String ordenPropuesta = vNombrProp[0];
		        		String nombreCertif = "";
		        		if(tipoPropuesta.equals("Propuesta")){
		        			urgencia = false;
		        			nombreCertif = ordenPropuesta+".-Certificado de acuerdos";
		        		}
		        		else{
		        			urgencia = true;
		        			nombreCertif = ordenPropuesta+".- Urgencia Certificado de acuerdos";
		        		}
		        		IItemCollection collectionCertificado = entitiesAPI.getDocuments(rulectx.getNumExp(), "DESCRIPCION='"+nombreCertif+"'", "");
		    	        Iterator itCertificado = collectionCertificado.iterator();
		    	        //No existe ese certificado en el trámite de Certificado de acuerdo por lo tanto hay
		    	        //que crear dentro del nuevo trámite 'Certificados Individualizados'
		    	        if (!itCertificado.hasNext())
		    	        {
		    	        	generarCertificadoIndividualizado(rulectx, cct, entitiesAPI, gendocAPI, urgencia, descripcionPropuesta, ordenPropuesta);
		    	        }
		        	}
	        	}
	        }
	        if(ooHelper!= null) ooHelper.dispose();
		}catch(Exception e){
			try {
				throw new Exception("Error a la hora de recorrer las propuestas " + e.getMessage(), e);
			} catch (Exception e1) {
				logger.error("Error a la hora de recorrer las propuestas " + e1.getMessage(), e1);
			}
		}
		finally{
			ooHelper.dispose();
		}
		logger.info("FIN - " + this.getClass().getName());
		return new Boolean(validate);
	}

	@SuppressWarnings("rawtypes")
	private void generarCertificadoIndividualizado(IRuleContext rulectx, ClientContext cct, IEntitiesAPI entitiesAPI, IGenDocAPI gendocAPI, boolean urgencia, String descripcionPropuesta, String ordenPropuesta) {
		
		
		String organo = "";
		try {
			organo = SecretariaUtil.getOrgano(rulectx);
		} catch (ISPACRuleException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String strNombreDocCab = "";
		String strNombreDoc = "";
		String strNombreDocPie = "";
		if(organo.equals("JGOB")){
			strNombreDocCab = "Certificado de acuerdos - Cabecera";
		}
		if(organo.equals("PLEN")){
			strNombreDocCab = "Certificado de acuerdos - Cabecera - Pleno";
		}
		strNombreDoc = "Certificado de acuerdos";
		strNombreDocPie = "Certificado de acuerdos - Pie";
		
		SimpleDateFormat dateformat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
		String fecha = dateformat.format(new Date());
		
		try {
			cct.setSsVariable("FECHA", fecha);
			
			//logger.warn("NOTIFICACION CABECERA INICIO "+strNombreDocCab);
        	DocumentosUtil.generarDocumento(rulectx, strNombreDocCab, null);
        	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, strNombreDocCab);
        	//logger.warn(strInfoPag);
        	File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
        	XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
    		file.delete();
    		//logger.warn("FIN CABECERA NOTIFICACION");
    		
    		//Cuerpo
        	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, descripcionPropuesta);
        	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
        	DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();
    		
    		//Pie
	    	//logger.warn("NOTIFICACION PIE INICIO "+strNombreDocPie);
	    	DocumentosUtil.generarDocumento(rulectx, strNombreDocPie, null);
	    	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, strNombreDocPie);
	    	//logger.warn(strInfoPag);
	    	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
	    	DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
			file.delete();
			//logger.warn("FIN PIE NOTIFICACION");
			
			//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(".odt");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			
			
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			
			//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, strNombreDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);

			IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, strNombreDoc, file, "odt");
			if(!urgencia){
				entityDoc.set("DESCRIPCION", ordenPropuesta +".-"+strNombreDoc);
			}
			else{
				entityDoc.set("DESCRIPCION", ordenPropuesta +".- Urgencia "+strNombreDoc);
			}
			
			entityDoc.store(cct);
			if(file != null && file.exists()) file.delete();
			
			//Borra los documentos intermedios del gestor documental
	        IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "DESCRIPCION LIKE '" + strNombreDoc + " -%' OR DESCRIPCION LIKE 'Notificación de acuerdos -%' OR DESCRIPCION LIKE 'Dictamen de acuerdos -%'", "");
	        Iterator it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
    		strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, descripcionPropuesta);
        	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
        	DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();
    		
    		cct.deleteSsVariable("FECHA");

		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}
	/**
	 * Compruebo si todas las propuestas esten ya certificadas y si estan
	 * el trámite no se crea. 
	 * **/
	@SuppressWarnings("rawtypes")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean validate = false;
		try{
			//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//----------------------------------------------------------------------------------------------
		
			/**
			 * [Ticket#380# Teresa]
			 * SIGEM Secretaria cambiar trámite de notificaciones para poder terminar el trámite habiendo certificados sin firmar
			 * Comprobar si existen propuestas sin firmar si no existen no se crea el trámite.
			 * **/
	        IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Propuesta' OR NOMBRE='Propuesta Urgencia'", "");
	        Iterator it = collection.iterator();
	        while (it.hasNext() && !validate)
	        {
	        	String descripcion = "";
	        	IItem doc = (IItem)it.next();
	        	if (doc.getString("DESCRIPCION")!=null) descripcion = (String)doc.getString("DESCRIPCION"); else descripcion = "";
	        	
	        	String [] vDesc = descripcion.split(" - ");
	        	//Propuesta - 5 . Adhesión de la Excma. Diputación Provincial de C, numexp=DPCR2010/9933
	        	//Propuesta Urgencia - 1 . Aprobación inicial del expediente de modificación de créditos, numexp=DPCR2010/10152
	        	if(vDesc.length >= 2){
	        		String tipoPropuesta = vDesc[0];
		        	String nombrePropuesta = vDesc[1];
		        	String [] vNombrProp = nombrePropuesta.split(" . ");
		        	if(vNombrProp.length > 1){
		        		String ordenPropuesta = vNombrProp[0];
		        		String nombreCertif = "";
		        		if(tipoPropuesta.equals("Propuesta")){
		        			nombreCertif = ordenPropuesta+".-Certificado de acuerdos";
		        		}
		        		else{
		        			nombreCertif = ordenPropuesta+".- Urgencia Certificado de acuerdos";
		        		}
		        		IItemCollection collectionCertificado = entitiesAPI.getDocuments(rulectx.getNumExp(), "DESCRIPCION='"+nombreCertif+"'", "");
		    	        Iterator itCertificado = collectionCertificado.iterator();
		    	        //No existe ese certificado en el trámite de Certificado de acuerdo por lo tanto hay
		    	        //que crear dentro del nuevo trámite 'Certificados Individualizados'
		    	        if (!itCertificado.hasNext())
		    	        {
		    	        	validate = true;
		    	        }
		        	}
	        	}
	        }
		}catch(Exception e){
			try {
				throw new Exception("Error a la hora de recorrer las propuestas. " + e.getMessage(), e);
			} catch (Exception e1) {
				logger.error("Error a la hora de recorrer las propuestas. " + e1.getMessage() , e1);
			}
		}
		if(!validate){
			rulectx.setInfoMessage("No existen Certificados para individualizar, ya que están todos en el trámite de 'Certificado de acuerdos'");
		}
		
		return validate;
	}

}
