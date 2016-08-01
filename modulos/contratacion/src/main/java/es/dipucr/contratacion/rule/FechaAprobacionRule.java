package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class FechaAprobacionRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(FechaAprobacionRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean cerrarTramite = false;
		logger.warn("INICIO DipucrTerminarPropDecrAprobadaRule");
		try
		{
    		//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            //Identificador del tramite actual.
            int idTask = rulectx.getTaskId();
            //logger.warn("idTask "+idTask);
            
            //Obtengo los expedientes relacionados
            String consultaSQL = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION LIKE '%"+idTask+"%'";
            //logger.warn("consultaSQL "+consultaSQL);
            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
			
			Iterator<IItem> it = itemCollection.iterator();
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
	        IItem iExped = entitiesAPI.getExpedient(numexp_hijo);
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
		        	 //logger.warn("numexpHijo_Propuesta "+numexpHijo_Propuesta);
		        	 IItem iExpedHijo = entitiesAPI.getExpedient(numexpHijo_Propuesta);
		        	 nombreProcedSecretaria = iExpedHijo.getString("NOMBREPROCEDIMIENTO");
		        	 //logger.warn("nombreProcedSecretaria "+nombreProcedSecretaria);
		        	 //logger.warn("Constants.SUBVENCIONES.RELACION "+Constants.SUBVENCIONES.RELACION);
		        	 //Para que no salga en expedientes relacionados el procedimientos de subvenciones
		        	 if(!nombreProcedSecretaria.contains(Constants.SUBVENCIONES.RELACION)){
		        		 String datosProce = numexpHijo_Propuesta +"&"+nombreProcedSecretaria;
		        		 //logger.warn("datosProce "+datosProce);
		        		 vSecret.add(datosProce);
		        	 }
		        }
		        //logger.warn("vSecret.size() "+vSecret.size());
		        if(vSecret.size() >= 2){
		        	numexpProced = devolverProcedimientoMayor(vSecret);
		        	//logger.warn("numexpProced "+numexpProced);
		        }
		        else{
		        	 if(!nombreProcedSecretaria.contains(Constants.SUBVENCIONES.RELACION)){
		        		 numexpProced = numexpHijo_Propuesta;
		        		 //logger.warn("numexpProced "+numexpProced);
		        	 }
		        }
		        
		       
	        }
	        else{
	        	//decreto
	        	numexpProced = numexpHijo_Propuesta;
	        	//logger.warn("numexpProced "+numexpProced);
	        }
	        
	        //Obtenemos la fecha del decreto o del acuerdo
	        logger.warn("numexpProced "+numexpProced);
	        IItemCollection itemCollectionDecreto = entitiesAPI.queryEntities("SGD_DECRETO", "WHERE NUMEXP = '" + numexpProced + "'");
	        Iterator <IItem> itDecreto = itemCollectionDecreto.iterator();
	        Date fecha = null;
	        if(itDecreto.hasNext()){
	        	IItem iDecreto = itDecreto.next();
	        	fecha = iDecreto.getDate("FECHA_DECRETO");
	        }
	        else{
	        	//Miro a ver si se ha aprobado por acuerdo
	        	IItemCollection itemCollectionAcuerdo = entitiesAPI.queryEntities("SECR_SESION", "WHERE NUMEXP = '" + numexpProced + "'");
		        Iterator <IItem> itAcuerdo = itemCollectionAcuerdo.iterator();
		        if(itDecreto.hasNext()){
		        	IItem iAcuerdo = itAcuerdo.next();
		        	fecha = iAcuerdo.getDate("FECHA");
		        }
	        }
	        if(fecha!=null){
	        	IItemCollection resLiciCollection = entitiesAPI.getEntities("CONTRATACION_DATOS_TRAMIT", rulectx.getNumExp());
				Iterator <IItem> resLiciIterator = resLiciCollection.iterator();
				if(resLiciIterator.hasNext()){
					IItem adj = resLiciIterator.next();
					adj.set("F_APROBACION_PROYECTO", fecha);
					adj.set("F_APRO_EXP_CONT", fecha);
					adj.store(cct);
				}
				else{
					IItem adjudicacion = entitiesAPI.createEntity("CONTRATACION_ADJUDICACION","");
					adjudicacion.set("NUMEXP", rulectx.getNumExp());
					adjudicacion.set("F_APROBACION_PROYECTO", fecha);
					adjudicacion.set("F_APRO_EXP_CONT", fecha);
					adjudicacion.store(cct);
				}
	        }
	        
	        //Para poder cerrar tramite compruebo que se haya aprobado el borrador del acta y la firma del decreto
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
	        
	        itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_TRAMITES, consultaSQL);
	        it = itemCollection.iterator();
	        if(it.hasNext()){
	        	 while(it.hasNext()){
			        	item = ((IItem)it.next());
			        	int estado = item.getInt("ESTADO");
			        	//logger.warn("estado "+estado);
			        	if(estado == 3){
			        		cerrarTramite = true;
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

}
