package es.dipucr.sigem.api.rule.procedures.intervencion.creditosExtraordinarios;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;
import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

/**
 * 
 * @author teresa
 * @date 09/09/2009
 * @propósito 
 * adjuntada en el primer trámite "Creación del decreto".
 */
public class DipucrGeneraOficiosRemision implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrGeneraOficiosRemision.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			logger.info("INICIO - " + this.getClass().getName());
		
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			
			String numExp = rulectx.getNumExp();
			String nombre = "";
	    	String dirnot = "";
	    	String c_postal = "";
	    	String localidad = "";
	    	String caut = "";
	    	String recurso = "";
	    	String id_ext = "";
	    	//String recursoTexto = "";
	    	String observaciones = "";
	    	Object connectorSession = null;
			
			String plantillaDefecto = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
	    	
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, " (ROL != 'TRAS' OR ROL IS NULL)", "ID");
			for (Object oParticipante : participantes.toList()){
				try {
					connectorSession = gendocAPI.createConnectorSession();
					IItem participante = (IItem) oParticipante;

					cct.beginTX();
				
					if (participante!=null){
			        
			        	if ((String)participante.get("NOMBRE")!=null){
			        		nombre = (String)participante.get("NOMBRE");
			        	}else{
			        		nombre = "";
			        	}
			        	if ((String)participante.get("DIRNOT")!=null){
			        		dirnot = (String)participante.get("DIRNOT");
			        	}else{
			        		dirnot = "";
			        	}
			        	if ((String)participante.get("C_POSTAL")!=null){
			        		c_postal = (String)participante.get("C_POSTAL");
			        	}else{
			        		c_postal = "";
			        	}
			        	if ((String)participante.get("LOCALIDAD")!=null){
			        		localidad = (String)participante.get("LOCALIDAD");
			        	}else{
			        		localidad = "";
			        	}
			        	if ((String)participante.get("CAUT")!=null){
			        		caut = (String)participante.get("CAUT");
			        	}else{
			        		caut = "";
			        	}
			        	if ((String)participante.get("RECURSO")!=null){
			        		recurso = (String)participante.get("RECURSO");
			        	}else{
			        		recurso = "";
			        	}
			        	
			        	if ((String)participante.get("ID_EXT")!=null){
			        		id_ext = (String)participante.get("ID_EXT");
			        	}else{
			        		id_ext = "";
			        	}
			        	
			        	if ((String)participante.get("OBSERVACIONES")!=null){
			        		observaciones = (String)participante.get("OBSERVACIONES");
			        	}else{
			        		observaciones = "";
			        	}

			        	// Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
			        	String sqlQueryPart = "WHERE VALOR = '"+recurso+"'";
			        	IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
			        	if (colRecurso.iterator().hasNext()){
			        		IItem iRecurso = (IItem)colRecurso.iterator().next();
			        		recurso = iRecurso.getString("SUSTITUTO");
			        	}
			        	if (recurso.equals("")){
			        		recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.sinRECUSO;
			        	}
			        	else{
			        		recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.conRECUSO;
			        	}
			        	
			        	cct.setSsVariable("NOMBRE", nombre);
			        	cct.setSsVariable("DIRNOT", dirnot);
			        	cct.setSsVariable("C_POSTAL", c_postal);
			        	cct.setSsVariable("LOCALIDAD", localidad);
			        	cct.setSsVariable("CAUT", caut);
			        	cct.setSsVariable("RECURSO", recurso);
			        	cct.setSsVariable("OBSERVACIONES", observaciones);
			        	cct.setSsVariable("ANIO", ""+Calendar.getInstance().get(Calendar.YEAR));
			        	cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));							        	

						// Generar el documento a partir la plantilla
						IItem entityTemplate = DocumentosUtil.generarDocumento(rulectx, plantillaDefecto,null);
								
						String docref = entityTemplate.getString("INFOPAG");
						String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
						entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
						String templateDescripcion = entityTemplate.getString("DESCRIPCION");
						entityTemplate.set("DESCRIPCION", templateDescripcion+" - "+nombre);
						entityTemplate.set("DESTINO", cct.getSsVariable("NOMBRE"));
						entityTemplate.set("DESTINO_ID", id_ext);

						entityTemplate.store(cct);
				        
						// Si todo ha sido correcto borrar las variables de la session
						cct.deleteSsVariable("NOMBRE");
						cct.deleteSsVariable("DIRNOT");
						cct.deleteSsVariable("C_POSTAL");
						cct.deleteSsVariable("LOCALIDAD");
						cct.deleteSsVariable("CAUT");
						cct.deleteSsVariable("RECURSO");
						//cct.deleteSsVariable("RECURSO_TEXTO");
						cct.deleteSsVariable("OBSERVACIONES");
						cct.deleteSsVariable("ANIO");
			        	cct.deleteSsVariable("NOMBRE_TRAMITE");
				    }
				}catch (Throwable e) {
					
					// Si se produce algún error se hace rollback de la transacción
					cct.endTX(false);
					
					String message = "exception.documents.generate";
					String extraInfo = null;
					Throwable eCause = e.getCause();
					
					if (eCause instanceof ISPACException) {
						
						if (eCause.getCause() instanceof NoConnectException) {
							extraInfo = "exception.extrainfo.documents.openoffice.off"; 
						}
						else {
							extraInfo = eCause.getCause().getMessage();
						}
					}
					else if (eCause instanceof DisposedException) {
						extraInfo = "exception.extrainfo.documents.openoffice.stop";
					}
					else {
						extraInfo = e.getMessage();
					}
	            	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
					throw new ISPACInfo(message, extraInfo);
					
				}finally {
					
					if (connectorSession != null) {
						gendocAPI.closeConnectorSession(connectorSession);
					}
				}
			}// for
			cct.endTX(true);
			logger.info("FIN - " + this.getClass().getName());
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}	
}
