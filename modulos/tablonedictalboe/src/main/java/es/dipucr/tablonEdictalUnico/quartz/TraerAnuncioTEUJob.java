package es.dipucr.tablonEdictalUnico.quartz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import es.dipucr.tablonEdictalUnico.commons.FuncionesComunes;
import es.dipucr.tablonEdictalUnico.quartz.bean.Entidad;
import es.dipucr.tablonEdictalUnico.quartz.dao.EntidadesDAO;

public class TraerAnuncioTEUJob implements Job {
	
	public static final Logger logger = Logger.getLogger(TraerAnuncioTEUJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Entidad entidad = null;
		try{
			
			EntidadesDAO ent_dao = new EntidadesDAO();
			ArrayList<Entidad> lEntidades = ent_dao.dameEntidadesAyuntamientos();		
			Iterator<Entidad> itr = lEntidades.iterator();
			
			
			while(itr.hasNext()){
				
				entidad = (Entidad)itr.next();				
				logger.warn("---- COMIENZA CONTROL ----");
				logger.warn(Calendar.getInstance().getTime().toString() + " -- Inicio ENTIDAD -- " + entidad);
		    	
		    	FuncionesComunes.obtenerAnuncioTEUBOE(entidad);
		    	 	
		    	
		    	logger.warn(Calendar.getInstance().getTime().toString() + " -- Termina ENTIDAD -- " + entidad);
		    	logger.warn("---- FIN CONTROL ----");
				
			}
        
		}catch(Exception e){
			if(entidad!=null){
				logger.error("ERROR!, EN HILO PRINCIPAL DE CONSOLIDACION, ENTIDAD " + entidad.getId() + " - "+entidad.getNombre()+". "+e.getMessage(), e);
			}
			logger.error("ERROR!, EN HILO PRINCIPAL DE CONSOLIDACION. "+e.getMessage(), e);	
			
		}
	}

}
