package es.dipucr.sigem.api.rule.procedures.tesoreria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.webempleado.services.certRetencion.CertificadoDeRetencionesServiceProxy;
import es.dipucr.webempleado.services.certRetencion.ObjetoCertificadoDataHandler;

public class DipucrEnviarCertificadoRetenciones  implements IRule {
	private static final Logger logger = Logger.getLogger(DipucrEnviarCertificadoRetenciones.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	@SuppressWarnings( "rawtypes" )
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		boolean correcto = true;
		try {
		
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			CertificadoDeRetencionesServiceProxy crs = new CertificadoDeRetencionesServiceProxy();
	        IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE="+rulectx.getTaskId()+"", "");
	        Iterator it = collection.iterator();
			
			 while (it.hasNext() && correcto){
				 crs = new CertificadoDeRetencionesServiceProxy();
				 IItem iCertRet = (IItem)it.next();
				 String sInfoPag = iCertRet.getString("INFOPAG_RDE");
				 if(sInfoPag!=null){
					 File file = DocumentosUtil.getFile(cct, sInfoPag, null, Constants._EXTENSION_PDF);
					 ObjetoCertificadoDataHandler certDatHad = new ObjetoCertificadoDataHandler(new DataHandler(new FileDataSource(file)) , iCertRet.getString("DESCRIPCION"));
					 crs.enviarCertificadosRetenciones(certDatHad);
				 }
				 else{
					 correcto = false;
					 rulectx.setInfoMessage("El documento no ha sido firmado");
				 }
				 
			 }
			 return new Boolean(correcto);

		} catch (ISPACException e) {	
			logger.error(e.getMessage(), e);
			return new Boolean(false);
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			return new Boolean(false);
		}

		
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}
	

}
