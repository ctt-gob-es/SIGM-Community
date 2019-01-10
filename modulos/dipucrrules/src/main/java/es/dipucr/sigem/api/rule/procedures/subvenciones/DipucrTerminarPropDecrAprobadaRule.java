package es.dipucr.sigem.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrTerminarPropDecrAprobadaRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrTerminarPropDecrAprobadaRule.class);

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
			
			if(nombProc.contains(Constants.SECRETARIAPROC.JUNTA) || nombProc.contains(Constants.SECRETARIAPROC.PLENO)){
				numexpProcedi = dtosProc[0];
			}

			
		}
		return numexpProcedi;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		boolean cerrarTramite = false;
		logger.warn("INICIO DipucrTerminarPropDecrAprobadaRule");
		try
		{
    		//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            //----------------------------------------------------------------------------------------------
            
            //Identificador del tramite actual.
            int idTask = rulectx.getTaskId();
            //logger.warn("idTask "+idTask);
            
            //Obtengo los expedientes relacionados
            String consultaSQL = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND " +
            		"RELACION LIKE '%"+idTask+"%'";
            //logger.warn("consultaSQL "+consultaSQL);
            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
			
			Iterator<?> it = itemCollection.iterator();
	        IItem item = null;
	        
	        String numexp_hijo = "";
	        
	        while (it.hasNext()) {
                item = ((IItem)it.next());
                numexp_hijo = item.getString("NUMEXP_HIJO");
                //logger.warn("numexp_hijo "+numexp_hijo);
	        }
	        
	        //Ya se ha obtenido el numero de expediente relacionado
	        //Ahora con ese expediente miro si tiene expedientes relacionado y seria una propuesta
	        //Si no tuviera sería un decreto
	        IItem iExped = ExpedientesUtil.getExpediente(cct, numexp_hijo);
	        String nombreProced = "";
	        if(iExped != null){
	        	nombreProced = iExped.getString("NOMBREPROCEDIMIENTO");
	        	//logger.warn("nombreProced "+nombreProced);
	        }
	        else{
	        	rulectx.setInfoMessage(Constants.MENSAJE.MENSAJERELACIONEXPEDIENTE);
	        }
	        
	        
	        nombreProced = nombreProced.toLowerCase();
	        //logger.warn("nombreProced "+nombreProced);
	        
	        //expediente creado como una propuesta
	        String numexpProced = "";
	        String numexpHijo_Propuesta = numexp_hijo;
	        if(nombreProced.contains(Constants.PROPUESTA.NOMBRE)){
	        	consultaSQL = "WHERE NUMEXP_PADRE = '" + numexp_hijo + "'";
	        	//logger.warn("consultaSQL "+consultaSQL);
		        itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
		        
		        it = itemCollection.iterator();
		        
		        //vector que almacena a que procedimientos esta asociado una propuesta
		        Vector<String> vSecret = new Vector<String>();
		        String nombreProcedSecretaria = "";
		        while(it.hasNext()){
		        	 item = ((IItem)it.next());
		        	 numexpHijo_Propuesta = item.getString("NUMEXP_HIJO");

		        	 IItem iExpedHijo = ExpedientesUtil.getExpediente(cct, numexpHijo_Propuesta);
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
	        
//	        itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_TRAMITES, consultaSQL);
	        itemCollection = TramitesUtil.queryTramites(cct, consultaSQL); //[dipucr-Felipe #842]
	        
	        it = itemCollection.iterator();
	        if(it.hasNext()){
		        while(it.hasNext()){
		        	item = ((IItem)it.next());
		        	int estado = item.getInt("ESTADO");
		        	if(estado == 3){
		        		cerrarTramite = true;
		        		insertarDocumentosNotifDecreto(cct, gendocAPI, entitiesAPI, rulectx, rulectx.getNumExp(), numexpProced, numexp_hijo);
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
	        	//logger.warn("else");
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
		logger.warn("FIN DipucrTerminarPropDecrAprobadaRule");
		return cerrarTramite;
	}

	private void insertarDocumentosNotifDecreto(ClientContext cct, IGenDocAPI gendocAPI, IEntitiesAPI entitiesAPI, IRuleContext rulectx, String numexp_origen, String exp_relacionado, String numexpPropuestaDecreto) throws ISPACRuleException {
		//Metodo que servira para insetar en el tramite lo documentos de notificacion del decreto y de la propuesta
		//y el certificado de dictamen, certificado de acuerdo o el decreto
		try{
			String tituloPropuesta = "";
			//Saco el titulo de la convocatoria para ponerlo en asunto y extracto
	        String strQuery = "WHERE NUMEXP='"+numexp_origen+"'";
	        IItemCollection colConvoc = entitiesAPI.queryEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, strQuery);
	        Iterator<?> itConvoc = colConvoc.iterator();

	        IItem convocatoria = (IItem)itConvoc.next();
	        if (convocatoria.getString("TITULO")!=null)tituloPropuesta = convocatoria.getString("TITULO"); else tituloPropuesta = "";
			
			//Obtengo el número de la propuesta en el procedimiento
	        int longPropu = tituloPropuesta.length();
	        tituloPropuesta = tituloPropuesta.substring(0, longPropu/2);
	        IItemCollection collection = DocumentosUtil.getDocumentsByDescripcion(exp_relacionado, rulectx, "numexp="+numexpPropuestaDecreto);
	        
	        Iterator<?> it = collection.iterator();
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

			//Insertar el documento de certificado de acuerdo, certificado de dictamen o el decreto
			 //Obtenemos los documentos a partir de su nombre
			List<IItem> filesConcatenate = new ArrayList<IItem>();

			strQuery = "DESCRIPCION='"+documento+Constants.SECRETARIATRAMITES.CERTIFICADACUERD+"' " +
							"OR DESCRIPCION='"+documento+Constants.TIPODOC.CERTIFICADO_DICTAMEN+"' " +
	        				"OR NOMBRE='"+Constants.DECRETOS._DOC_DECRETO+"'";
			
			IItemCollection documentsCollection = DocumentosUtil.getDocumentos(cct, exp_relacionado, strQuery, "FDOC DESC");
			
			it  = documentsCollection.iterator();
	        IItem itemDoc = null;
	        while (it.hasNext()){
	        	itemDoc = (IItem)it.next();
	        	filesConcatenate.add(itemDoc);
			}  		
			
			//Insertar el documento de notificaciones de cada participante
	    	IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numexp_origen, "(ROL != 'TRAS' OR ROL IS NULL)", "ID"); 
	    	Iterator<?> itParticipante = participantes.iterator();
	    	StringBuffer sNombreSQL = new StringBuffer();
	    	while (itParticipante.hasNext())
        	{
        		IItem participante = (IItem)itParticipante.next();
        		String nombrePar = participante.getString("NOMBRE");
        		sNombreSQL.append("DESCRIPCION LIKE '%"+nombrePar+"%' ");
        		if(itParticipante.hasNext()){
        			sNombreSQL.append("OR ");
        		}
        		
        	}
	    	
	    	
			documentsCollection = DocumentosUtil.getDocumentos(cct, exp_relacionado, sNombreSQL.toString(), "FDOC DESC");
			
			it  = documentsCollection.iterator();
			IItem itemDocNot = null;
			while (it.hasNext()){
				itemDocNot = (IItem)it.next();
				filesConcatenate.add(itemDocNot);
			}
				
			//obtenemos el tipo de documento que le vamos a poner a zip que se va a crear
			//Obtenemos el id del tipo de documento de "Contenido de la propuesta" para
	        //ponerle el nombre del documento al zip
	        strQuery = "WHERE NOMBRE = '"+Constants.TIPODOC.DOCUMENTACION_APROBACION+"'";
	        int idTypeDocument = DocumentosUtil.getTipoDoc(cct, Constants.TIPODOC.DOCUMENTACION_APROBACION, DocumentosUtil.BUSQUEDA_EXACTA, false);
			if (idTypeDocument > Integer.MIN_VALUE){
				String sExtension = "zip";
				//Crear el zip con los documentos
				//http://www.manual-java.com/codigos-java/compresion-decompresion-archivos-zip-java.html
				File zipFile = DocumentosUtil.createDocumentsZipFile(gendocAPI, filesConcatenate);
				
		        //Generamos el documento zip
		        String sFase = FasesUtil.getFase(rulectx, entitiesAPI, rulectx.getStageProcedureId());
				String sName = sFase+"& Numexp = "+numexpPropuestaDecreto;//Campo descripción del documento

		        IItem docZip = DocumentosUtil.generaYAnexaDocumento(rulectx, idTypeDocument, sName, zipFile, sExtension);
				docZip.set("FAPROBACION", itemDoc.getDate("FAPROBACION"));
				docZip.store(cct);
			}
			else{
				throw new ISPACInfo("Error al obtener el tipo de documento");
			}	
		}catch(Exception e){
			logger.error("Error. " + e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}
	}
}
