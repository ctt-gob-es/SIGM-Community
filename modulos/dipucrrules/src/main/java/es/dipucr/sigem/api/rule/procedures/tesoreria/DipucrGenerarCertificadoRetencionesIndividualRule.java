package es.dipucr.sigem.api.rule.procedures.tesoreria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.webempleado.services.certRetencion.CertificadoDeRetencionesServiceProxy;
import es.dipucr.webempleado.services.certRetencion.ObjetoCertificadoDataHandler;

public class DipucrGenerarCertificadoRetencionesIndividualRule implements IRule {
	private static final Logger logger = Logger.getLogger(DipucrGenerarCertificadoRetencionesIndividualRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
				
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		String numexp = "";
		try {	
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			numexp = rulectx.getNumExp();
			CertificadoDeRetencionesServiceProxy crs = new CertificadoDeRetencionesServiceProxy();
			
			String consultaDNI = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
			IItemCollection dnisCol = entitiesAPI.queryEntities("DPCR_NIF_CERT_RETEN", consultaDNI);
			Iterator<?> dnisIterator = dnisCol.iterator();
			
			if(dnisIterator.hasNext()){
				
				String dnis = ((IItem)dnisIterator.next()).getString("NIFS");
					
	        	//Creacion del documento certificado de retenciones
				int idTipDoc = DocumentosUtil.getTipoDoc(cct, Constants.TESORERIA.CERTIFICADO_RETENCIONES, DocumentosUtil.BUSQUEDA_EXACTA, false);

		        String strQueryNombre="SELECT ID FROM SPAC_P_PLANTDOC WHERE NOMBRE='"+Constants.TESORERIA.CERTIFICADO_RETENCIONES+"' AND ID_TPDOC="+idTipDoc+";";
		        ResultSet planDocIterator = cct.getConnection().executeQuery(strQueryNombre).getResultSet();
		        int idPlantillaCertiComp = 0;
		        if(planDocIterator.next()) idPlantillaCertiComp = planDocIterator.getInt("ID");
	
		        
			    //El contexto viene vacío, tenemos que rellenarlo
				IProcess exp = invesFlowAPI.getProcess(rulectx.getNumExp());
				// Inicia el contexto de ejecución para que se ejecuten
				// las reglas asociadas a la entidad //////////////////
				StateContext stateContext = cct.getStateContext();
				if (stateContext == null) {
					stateContext = new StateContext();
					((ClientContext)cct).setStateContext(stateContext);
					}
				stateContext.setPcdId(exp.getInt("ID_PCD"));
				stateContext.setProcessId(exp.getKeyInt());
				stateContext.setNumexp(rulectx.getNumExp());
		        
		        if(dnis != null && !dnis.equals("")){
			        String[] dnisArray = dnis.split(",");
			        
			        for(int i=0; i<dnisArray.length; i++){
		
				        if(dnisArray[i] != null && !dnisArray[i].trim().equals("")){
							crs = new CertificadoDeRetencionesServiceProxy();
							
							ObjetoCertificadoDataHandler certRetenc = crs.generarCertificadosRetenciones(dnisArray[i].trim().toUpperCase());
													
							DataHandler dh = (DataHandler)certRetenc.getCertificado();
							String descripcion = certRetenc.getNombreCertificado();
							
							String nombreCertificadoReten = dh.getName();
							
				        	File file = new File(nombreCertificadoReten);

				        	IItem entityDocument = DocumentosUtil.generaYAnexaDocumento(rulectx, idTipDoc, descripcion, file, Constants._EXTENSION_PDF);
					        entityDocument.set("ID_PLANTILLA", idPlantillaCertiComp);
					        entityDocument.store(cct);
					        
				        	if(file != null && file.exists()) file.delete();
					        cct.endTX(true);
				        }
			        }
		        }
			}
		} catch (RemoteException e) {
			logger.error("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ", error al conectar. " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ", error al conectar. " + e.getMessage(), e);
		} catch (ISPACRuleException e) {
			logger.error("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (SQLException e) {
			logger.error("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido generar el certificado de retenciones del expediente: " + numexp + ". " + e.getMessage(), e);
		}

		logger.info("FIN - " + this.getClass().getName());
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}