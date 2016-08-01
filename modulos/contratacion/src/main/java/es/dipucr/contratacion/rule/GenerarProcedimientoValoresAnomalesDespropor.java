package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class GenerarProcedimientoValoresAnomalesDespropor  implements IRule{
	
	private static final Logger logger = Logger.getLogger(GenerarProcedimientoValoresAnomalesDespropor.class);
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			logger.warn("INICIO GenerarProcedimientoValoresAnomalesDespropor");
        	generaProcedimientoEspecifico(rulectx);		
			logger.warn("FIN GenerarProcedimientoValoresAnomalesDespropor");
		} catch (ISPACException e) { 
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void generaProcedimientoEspecifico(IRuleContext rulectx) throws Exception {
		
		ClientContext cct = (ClientContext) rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		ITXTransaction tx = invesflowAPI.getTransactionAPI();
		
        String procedimiento = "VALANORDESP";
		
 		// Obtener el código de procedimiento para el número de expediente
 		Map<String, String> params = new HashMap<String, String>();
 		params.put("COD_PCD", procedimiento);
 		
 		//Calculo el id de ese procedimiento a partir de la tabla spac_ct_procedimientos
 		IItemCollection collection = null;
 		Iterator<IItem> it = null;
        String consulta = "WHERE COD_PCD = '"+procedimiento+"'";
 		collection = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", consulta);
         it = collection.iterator();
         int id_procedimiento = 0;
         while (it.hasNext()){
         	IItem procExpediente = (IItem)it.next();
         	id_procedimiento = procExpediente.getInt("ID");

         }

 		// Crear el proceso del expediente
 		int nIdProcess2 = tx.createProcess(id_procedimiento, params);
 		IProcess process;
 		
 		process = invesflowAPI.getProcess(nIdProcess2);
 		
 		String numExpHijo = process.getString("NUMEXP");

 		IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);

 		registro.set("NUMEXP_PADRE", rulectx.getNumExp());
 		registro.set("NUMEXP_HIJO", numExpHijo);
 		registro.set("RELACION", "Val. anormales o despropor");

 		registro.store(cct);

 		
 		cct.endTX(true);
 		

 		/**
 		 * [Teresa] INICIO Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
 		 * **/
 		//Importar participantes.
 		ParticipantesUtil.importarParticipantes(cct, entitiesAPI, rulectx.getNumExp(), numExpHijo);
 		/**
 		 * [Teresa] FIN Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
 		 * **/
 		
 		/**
 		 * [Ticket #989 Teresa] INICIO CONTRATACION-SIGEM Añadir en el procedimiento de Valores Anormales o desproporcionados varíos campos
 		 * **/
 		//Obtengo el interesado principal
 		IItem expedienteLicitador = entitiesAPI.getExpedient(rulectx.getNumExp());
 		
 		IItem expedienteValoresAnormales = entitiesAPI.getExpedient(numExpHijo);
 		expedienteValoresAnormales.set("NIFCIFTITULAR", expedienteLicitador.getString("NIFCIFTITULAR"));
 		expedienteValoresAnormales.set("IDTITULAR", expedienteLicitador.getString("IDTITULAR"));
 		expedienteValoresAnormales.set("DOMICILIO", expedienteLicitador.getString("DOMICILIO"));
 		expedienteValoresAnormales.set("CIUDAD", expedienteLicitador.getString("CIUDAD"));
 		expedienteValoresAnormales.set("REGIONPAIS", expedienteLicitador.getString("REGIONPAIS"));
 		expedienteValoresAnormales.set("CPOSTAL", expedienteLicitador.getString("CPOSTAL"));
 		expedienteValoresAnormales.set("IDENTIDADTITULAR", expedienteLicitador.getString("IDENTIDADTITULAR"));
 		expedienteValoresAnormales.set("TIPOPERSONA", expedienteLicitador.getString("TIPOPERSONA"));
 		expedienteValoresAnormales.set("ROLTITULAR", expedienteLicitador.getString("ROLTITULAR"));
 		String nombreProcedimiento = "";
 		String query = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Plica'";
 		IItemCollection iCExpRel = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", query);
 		Iterator<IItem> itExpRel = iCExpRel.iterator();
 		if(itExpRel.hasNext()){
 			IItem procRelacionado = itExpRel.next();
 			String numExpProcContrat = procRelacionado.getString("NUMEXP_PADRE");
 			IItem procContratacion = entitiesAPI.getExpedient(numExpProcContrat);
 			nombreProcedimiento = procContratacion.getString("ASUNTO");
 		}
 		
 		expedienteValoresAnormales.set("ASUNTO", expedienteValoresAnormales.getString("ASUNTO")+" - "+nombreProcedimiento);
 		expedienteValoresAnormales.store(cct);
 		/**
 		 * [Ticket #989 Teresa] FIN CONTRATACION-SIGEM Añadir en el procedimiento de Valores Anormales o desproporcionados varíos campos
 		 * **/
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}
}
