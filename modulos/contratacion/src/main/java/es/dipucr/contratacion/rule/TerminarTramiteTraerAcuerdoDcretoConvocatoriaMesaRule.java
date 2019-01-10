package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class TerminarTramiteTraerAcuerdoDcretoConvocatoriaMesaRule implements IRule {
	
	String descripcion = "";
	
	private static final Logger logger = Logger.getLogger(TerminarTramiteTraerAcuerdoDcretoConvocatoriaMesaRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return new Boolean(true);
	}

	private String devolverProcedimientoMayor(Vector<String> secret) {
		String numexpProcedi = "";
		for(int i=0; i<secret.size(); i++){
			String sRes = (String) secret.elementAt(i);
			String [] dtosProc = sRes.split("&");
			String nombProc = dtosProc[1];
			
			if(nombProc.contains(Constants.SECRETARIAPROC.JUNTA) || nombProc.contains(Constants.SECRETARIAPROC.PLENO)
					|| nombProc.contains(Constants.SECRETARIAPROC.MESA)){
				numexpProcedi = dtosProc[0];
			}
		}
		return numexpProcedi;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		boolean cerrarTramite = false;
		logger.info("INICIO - " + this.getClass().getName());
		try
		{
    		//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            //Identificador del tramite actual.
            int idTask = rulectx.getTaskId();
            
            //Obtengo los expedientes relacionados
            String consultaSQL = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION LIKE '%"+idTask+"%'";
            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
			
			Iterator<IItem> it = itemCollection.iterator();
	        IItem item = null;
	        
	        String numexp_hijo = "";
	        
	        while (it.hasNext()) {
                item = ((IItem)it.next());
                numexp_hijo = item.getString("NUMEXP_HIJO");
	        }
	        
	        //Ya se ha obtenido el numero de expediente relacionado
	        //Ahora con ese expediente miro si tiene expedientes relacionado y seria una propuesta
	        //Si no tuviera sería un decreto
	        IItem iExped = entitiesAPI.getExpedient(numexp_hijo);
	        String nombreProced = "";
	        if(iExped != null){
	        	nombreProced = iExped.getString("NOMBREPROCEDIMIENTO");
	        }
	        else{
	        	rulectx.setInfoMessage(Constants.MENSAJE.MENSAJERELACIONEXPEDIENTE);
	        }
	        
	        nombreProced = nombreProced.toLowerCase();
	        
	        //expediente creado como una propuesta
	        String numexpProced = "";
	        String numexpHijo_Propuesta = numexp_hijo;
	        if(nombreProced.contains(Constants.PROPUESTA.NOMBRE)){
	        	consultaSQL = "WHERE NUMEXP_PADRE = '" + numexp_hijo + "'";
		        itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
		        
		        it = itemCollection.iterator();
		        
		        //vector que almacena a que procedimientos esta asociado una propuesta
		        Vector<String> vSecret = new Vector<String>();
		        String nombreProcedSecretaria = "";
		        while(it.hasNext()){
		        	 item = ((IItem)it.next());
		        	 numexpHijo_Propuesta = item.getString("NUMEXP_HIJO");
		        	 IItem iExpedHijo = entitiesAPI.getExpedient(numexpHijo_Propuesta);
		        	 nombreProcedSecretaria = iExpedHijo.getString("NOMBREPROCEDIMIENTO");
		        	 //Para que no salga en expedientes relacionados el procedimientos de subvenciones
		        	 if(!nombreProcedSecretaria.contains(Constants.SUBVENCIONES.RELACION)){
		        		 String datosProce = numexpHijo_Propuesta +"&"+nombreProcedSecretaria;
		        		 vSecret.add(datosProce);
		        	 }
		        }
		        if(vSecret.size() >= 2){
		        	numexpProced = devolverProcedimientoMayor(vSecret);
		        }
		        else{
		        	 if(!nombreProcedSecretaria.contains(Constants.SUBVENCIONES.RELACION)){
		        		 numexpProced = numexpHijo_Propuesta;
		        	 }
		        }
	        }
	        else{
	        	//decreto
	        	numexpProced = numexpHijo_Propuesta;
	        }
	        //Ahora se comprueba si en este procedimiento tiene el tramite dictamenes cerrados
	        //y así compruebo q ha sido dictaminada o certificada para ello se miran los tramites
	        //para comprobar esto se mira la tabla spac_dt_tramites y el estado=3.
	        //Notificaciones y traslado de dictamenes
	        //Certificado de acuerdos
	        consultaSQL = "WHERE NUMEXP='"+numexpProced+"' AND " +
	        		"(NOMBRE='"+Constants.SECRETARIATRAMITES.CERTIFICADACUERD+"' OR NOMBRE='"+Constants.SECRETARIATRAMITES.NOTIFTRASLDICTAMEN+"' " +
	        				"OR NOMBRE='"+Constants.DECRETOSTRAMITES.FIRMASTRASLADO+"' " +
	        				"OR NOMBRE='"+Constants.SECRETARIATRAMITES.CERTIFICADACUERDNOTIFICACI+"') AND ESTADO=3";
	        logger.warn("consultaSQL "+consultaSQL);
	        
//	        itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_TRAMITES, consultaSQL);
	        itemCollection = TramitesUtil.queryTramites(cct, consultaSQL); //[dipucr-Felipe #842]
	        
	        it = itemCollection.iterator();
	        if(it.hasNext()){
		        while(it.hasNext()){
		        	item = ((IItem)it.next());
		        	int estado = item.getInt("ESTADO");
		        	if(estado == 3){
		        		cerrarTramite = true;
		        		insertarDocumentosNotifDecreto(rulectx, numexpProced, numexp_hijo);
		        	}
		        	else{
		        		if(nombreProced.contains(Constants.PROPUESTA.NOMBRE)){
		        			rulectx.setInfoMessage(Constants.MENSAJESECRETARIA.MENSAJEDICTAMINADO);
		        		}
		        		else{
		        			rulectx.setInfoMessage(Constants.MENSAJESDECRETOS.MENSAJEDECRETADO);
		        		}
		        	}
		        }
	        }
	        else{
	        	if(nombreProced.contains(Constants.PROPUESTA.NOMBRE)){
        			rulectx.setInfoMessage(Constants.MENSAJESECRETARIA.MENSAJEDICTAMINADO);
        		}
        		else{
        			rulectx.setInfoMessage(Constants.MENSAJESDECRETOS.MENSAJEDECRETADO);
        		}
	        }
		}catch(Exception e) {
			throw new ISPACRuleException(e);
		}
		logger.info("FIN - " + this.getClass().getName());
		return cerrarTramite;
	}

	@SuppressWarnings("unchecked")
	private void insertarDocumentosNotifDecreto(IRuleContext rulectx, String exp_relacionado, String numexpPropuestaDecreto) throws ISPACRuleException {
		
		//Metodo que servira para insetar en el tramite lo documentos de notificacion del decreto y de la propuesta
		//y el certificado de dictamen, certificado de acuerdo o el decreto
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
	        //----------------------------------------------------------------------------------------------
			
			
			String tituloPropuesta = "";
			//Saco el titulo de la convocatoria para ponerlo en asunto y extracto
	        String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
	        IItemCollection colConvoc = entitiesAPI.queryEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, strQuery);
	        Iterator<IItem> itConvoc = colConvoc.iterator();

	        IItem convocatoria = (IItem)itConvoc.next();
	        if (convocatoria.getString("TITULO")!=null)tituloPropuesta = convocatoria.getString("TITULO"); else tituloPropuesta = "";
			
			//Obtengo el número de la propuesta en el procedimiento
	        int longPropu = tituloPropuesta.length();
	        tituloPropuesta = tituloPropuesta.substring(0, longPropu/2);
	        IItemCollection collection = DocumentosUtil.getDocumentsByDescripcion(exp_relacionado, rulectx, "'%numexp="+numexpPropuestaDecreto+"%'");
	        
	        Iterator<IItem> it = collection.iterator();
	        IItem prop = null;
	        String sDescripcion = "";
	        int orden = 0;
	        String tipoPropuesta = "";
	        
	        if(it.hasNext()){
	        	prop = (IItem)it.next();
	        	sDescripcion = prop.getString("DESCRIPCION");
	        	String [] vDesc = sDescripcion.split(" . ");
	        	orden = Integer.parseInt(vDesc[1]);
	        	tipoPropuesta = vDesc[0];
	        }
	        String documento ="";
	        if(tipoPropuesta.equals(Constants.PROPUESTA.PROPUESTA_URGENCIA)){
	        	documento = orden + Constants.PROPUESTA.URGENCIA+" ";
	        }
	        else{
	        	documento = orden+".-";
	        }

			strQuery = "NUMEXP = '"+exp_relacionado+"' AND (DESCRIPCION='"+documento+Constants.SECRETARIATRAMITES.CERTIFICADACUERD+"' " +
							"OR DESCRIPCION='"+documento+Constants.TIPODOC.CERTIFICADO_DICTAMEN+"' " +
	        				"OR NOMBRE='"+Constants.DECRETOS._DOC_DECRETO+"')";
			
			IItemCollection documentsCollection = entitiesAPI.getDocuments(exp_relacionado, strQuery, "FDOC DESC");
			
			it  = documentsCollection.iterator();
	        IItem itemDoc = null;
	        while (it.hasNext()){
	        	//Documento de certificado
	        	itemDoc = (IItem)it.next();
	        	
	        	// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
	        	IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
				int idTramCtl = processTask.getInt("ID_TRAM_CTL");
	        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	        	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()){
	        		throw new ISPACInfo("No existe este tipo de documento");
	        	}else {
	        		
	        		//Hay dos tipos de documento asociados al trámite: Decreto y Notificación Decreto
	        		//Necesitamos el de Notificación del Decreto
	        		Iterator<IItem> itTpDOC = taskTpDocCollection.iterator();
	        		while (itTpDOC.hasNext()){
	        			IItem taskTpDoc = (IItem)itTpDOC.next();
	        			if (taskTpDoc.get("CT_TPDOC:NOMBRE").equals(Constants.TIPODOC.DOCUMENTACION_APROBACION)){
	        				int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	        				
	        				String infopag_rde = itemDoc.getString("INFOPAG_RDE");
	        				File filePropuesta = DocumentosUtil.getFile(cct, infopag_rde, null, null);
	        				
	        				if(descripcion.equals("")){
	        					descripcion = itemDoc.getString("DESCRIPCION");
	        				}
	        				
	        				
	        				IItem nuevoDocumento = DocumentosUtil.generaYAnexaDocumento(rulectx, documentTypeId, descripcion, filePropuesta, Constants._EXTENSION_PDF);
	        				
	    					Date ffirma = itemDoc.getDate("FAPROBACION");
	    					
	    					nuevoDocumento.set("NOMBRE", Constants.TIPODOC.DOCUMENTACION_APROBACION);
	    					nuevoDocumento.set("FAPROBACION", ffirma);
	    					nuevoDocumento.store(cct);
	    					if(filePropuesta != null && filePropuesta.exists()) filePropuesta.delete();
	        			}
	        		}
	        	}
			}  		
	    	
		}catch(Exception e){
			throw new ISPACRuleException(e);
		}
	}
}
