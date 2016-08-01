package es.dipucr.sigem.api.rule.procedures.tesoreria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
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

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.webempleado.services.certRetencion.CertificadoDeRetencionesServiceProxy;
import es.dipucr.webempleado.services.certRetencion.ObjetoCertificadoDataHandler;

public class DipucrGenerarCertificadoRetencionesRule implements IRule {
	private static final Logger logger = Logger.getLogger(DipucrGenerarCertificadoRetencionesRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
				
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		
		Long t1 = System.currentTimeMillis(); 
		String numexp = "";
		
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			numexp = rulectx.getNumExp();
			
			CertificadoDeRetencionesServiceProxy crs = new CertificadoDeRetencionesServiceProxy();

			String [] dni = crs.dameNIF();

        	//Creacion del documento certificado de retenciones
			int idTipDoc = DocumentosUtil.getTipoDoc(cct, Constants.TESORERIA.CERTIFICADO_RETENCIONES, DocumentosUtil.BUSQUEDA_EXACTA, false);

	        String strQueryNombre="SELECT ID FROM SPAC_P_PLANTDOC WHERE NOMBRE='"+Constants.TESORERIA.CERTIFICADO_RETENCIONES+"' AND ID_TPDOC="+idTipDoc+";";
	        ResultSet planDocIterator = cct.getConnection().executeQuery(strQueryNombre).getResultSet();
	        int idPlantillaCertiComp = 0;
	        if(planDocIterator.next()) idPlantillaCertiComp = planDocIterator.getInt("ID");
	        
	        logger.debug("certRetenc.length "+dni.length);
	        
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
	        
	        int idFase = rulectx.getStageId();
	        
	        
	        String consulta = "WHERE ID = "+idFase;
			 IItemCollection iFases = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, consulta);
			 Iterator<?> IFases = iFases.iterator();
			 int idFaseProp=0;
			 
			 while (IFases.hasNext()){
				 IItem fase = (IItem)IFases.next();
				 idFaseProp = fase.getInt("ID_FASE");
			 }
			 
			 logger.debug("idFaseProp "+idFaseProp);
			 
			 consulta = "WHERE ID_FASE = "+idFaseProp;
			 IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, consulta);
			 Iterator<?> ITramiteProp = iTramiteProp.iterator();
			 int idTramite=0;
			 while (ITramiteProp.hasNext()){
				 IItem tramite = (IItem)ITramiteProp.next();
				 idTramite = tramite.getInt("ID");
			 }
			 logger.debug("idTramite "+idTramite);
			 
			 
	        int numCertReten = 300;
	        try{
	        	numCertReten = Integer.parseInt(DipucrCommonFunctions.getVarGlobal("NUM_CERT_RETEN_TRAM"));	
	        }
	        catch(NumberFormatException e){
	        	logger.warn("No se ha podido recuperar el valor de la variable: NUM_CERT_RETEN_TRAM, se toma el valor por defecto de 300 documentos por trámite. " + e.getMessage(), e);
	        }
	        //Se dividirá el numero de personas entre 500 para que
	        // cada trámite tenga 500 documentos y así no va a ver problemas a la hora de firmar ahora.
	        int cantidadTramites = dni.length / numCertReten;
	        
	        int modDivTramites = dni.length % numCertReten;
	        //si el modulo es distinto de 0 hay que añadir un trámite mas
	         if(modDivTramites != 0){
	        	 cantidadTramites += 1;
	         }
	         int inicio = 0;
	         int fin = 0;
	         for(int j=0; j <cantidadTramites; j++){
	        	//Creo el tramite Generar Certificado de Retenciones
				int idTramitePropuesta = transaction.createTask(idFase, idTramite);
				logger.debug("idTramitePropuesta "+idTramitePropuesta);
				
				logger.debug("Creado el tramite Generar Certificado de Retenciones");
				
				inicio = j * numCertReten;
				fin = inicio + numCertReten;
				
				logger.debug("inicio "+inicio);
				logger.debug("fin "+fin);

				for(int i=inicio; i < fin && i < dni.length; i++){
					crs = new CertificadoDeRetencionesServiceProxy();
					
					ObjetoCertificadoDataHandler certRetenc = crs.generarCertificadosRetenciones(dni[i]);
					
					
					DataHandler dh = (DataHandler)certRetenc.getCertificado();
					String descripcion = certRetenc.getNombreCertificado();
					
					String nombreCertificadoReten = dh.getName();
					
		        	File file = new File(nombreCertificadoReten);
		        	
		        	IItem entityDocument = DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropuesta, idTipDoc, descripcion, file, Constants._EXTENSION_PDF);
			        entityDocument.set("ID_PLANTILLA", idPlantillaCertiComp);
					entityDocument.store(cct);

					cct.endTX(true);

		        	if(file != null && file.exists()) file.delete();
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
		}
		
		Long t2 = System.currentTimeMillis();
		long tiempoTardadoEnMilisegundos = t2-t1; 
		logger.debug("iempoTardadoEnMilisegundos "+tiempoTardadoEnMilisegundos);

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
