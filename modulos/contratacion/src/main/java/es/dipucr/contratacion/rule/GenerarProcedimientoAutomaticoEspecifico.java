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
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.dao.procedure.ContratacionDatosContratoSDAO;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;


public class GenerarProcedimientoAutomaticoEspecifico implements IRule{
	
	private static final Logger logger = Logger.getLogger(GenerarProcedimientoAutomaticoEspecifico.class);
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			logger.warn("INICIO GenerarProcedimientoAutomaticoEspecifico");
        	generaProcedimientoEspecifico(rulectx);		
			logger.warn("FIN GenerarProcedimientoAutomaticoEspecifico");
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
		
		//Obtengo el procedimiento que le corresponde
		//Dependiendo de los datos que se han obtenido.
		String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
		IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_CONTRATO", consulta);
        Iterator<IItem> it = collection.iterator();
        String tipo_contrato = "";
        String proc_adjudicacion = "";
        while (it.hasNext()){
        	IItem contrato = (IItem)it.next();
        	tipo_contrato = contrato.getString("TIPO_CONTRATO");
        	proc_adjudicacion = contrato.getString("PROC_ADJ");
        }
        logger.warn("tipo_contrato. "+tipo_contrato);
        logger.warn("proc_adjudicacion. "+proc_adjudicacion);
        String cod_tipo_contrato = "";
        String cod_proc_adjudicacion = "";
        if(proc_adjudicacion == null || proc_adjudicacion.equals("")){
        	if(tipo_contrato!=""){
            	String [] v_cod_tipo_contrato = tipo_contrato.split(" - ");
            	if(v_cod_tipo_contrato.length > 0){
            		cod_tipo_contrato = v_cod_tipo_contrato[0];
            	}
            }
            collection = null;
            it = null;
            consulta = "WHERE TIPO_CONTRATO = '"+cod_tipo_contrato+"' AND PROCEDIMIENTO_ADJUDICACION IS NULL";
        }
        else{
        	if(tipo_contrato!="" && proc_adjudicacion!=""){
            	String [] v_cod_tipo_contrato = tipo_contrato.split(" - ");
            	if(v_cod_tipo_contrato.length > 0){
            		cod_tipo_contrato = v_cod_tipo_contrato[0];
            	}
            	String [] v_cod_proc_adjudicacion = proc_adjudicacion.split(" - ");
            	if(v_cod_proc_adjudicacion.length > 0){
            		cod_proc_adjudicacion = v_cod_proc_adjudicacion[0];
            	}
            }
            collection = null;
            it = null;
            consulta = "WHERE TIPO_CONTRATO = '"+cod_tipo_contrato+"' AND PROCEDIMIENTO_ADJUDICACION='"+cod_proc_adjudicacion+"'";
        }
        
        logger.warn(consulta);
		collection = entitiesAPI.queryEntities("CONTRATACION_RELACION_PROCED", consulta);
        it = collection.iterator();
        String procedimiento = "";
        if(it.hasNext()){
        	 while (it.hasNext()){
             	IItem procExpediente = (IItem)it.next();
             	procedimiento = procExpediente.getString("PROCEDIMIENTO_RELACION");
             }
             
     		// Obtener el código de procedimiento para el número de expediente
     		Map<String, String> params = new HashMap<String, String>();
     		params.put("COD_PCD", procedimiento);
     		
     		//Calculo el id de ese procedimiento a partir de la tabla spac_ct_procedimientos
     		collection = null;
             it = null;
             consulta = "WHERE COD_PCD = '"+procedimiento+"'";
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
     		
     		//Num exp nuevo.
     		String numExpHijo = process.getString("NUMEXP");
     		IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);

     		registro.set("NUMEXP_PADRE", rulectx.getNumExp());
     		registro.set("NUMEXP_HIJO", numExpHijo);
     		registro.set("RELACION", "Petición Contrato");

     		registro.store(cct);

     		
     		cct.endTX(true);
     		
     		clonar_contratacion_peticion(entitiesAPI, rulectx, numExpHijo, cct);
     		
     		int [] idDatos = clonar_contratacion_datos_contrato(entitiesAPI, rulectx, numExpHijo, cct);
     		
     		clonar_tabla_s(entitiesAPI, rulectx, numExpHijo, cct, idDatos, "CONTRATACION_DATOS_CONTRATO_S");
     		
//     		clonar_contratacion_datos_tramit(entitiesAPI, rulectx, numExpHijo, cct);
//     		
//     		idDatos = clonar_dpcr_codice_pliegos(entitiesAPI, rulectx, numExpHijo, cct);
//     		
//     		clonar_tabla_s(entitiesAPI, rulectx, numExpHijo, cct, idDatos, "DPCR_CODICE_PLIEGOS_S");
//     		
//     		idDatos = clonar_CONTRATACION_INF_EMPRESA(entitiesAPI, rulectx, numExpHijo, cct);
//     		
//     		clonar_tabla_s(entitiesAPI, rulectx, numExpHijo, cct, idDatos, "CONTRATACION_INF_EMPRESA_S");
//     		
//     		clonar_CONTRATACION_DATOS_LIC(entitiesAPI, rulectx, numExpHijo, cct);
     		
     		/**
     		 * [Teresa] INICIO Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
     		 * **/
     		//Importar participantes.
     		ParticipantesUtil.importarParticipantes(cct, entitiesAPI, rulectx.getNumExp(), numExpHijo);
     		/**
     		 * [Teresa] FIN Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
     		 * **/
        }
        else{
        	logger.warn("No existe procedimiento aosciado");
        	rulectx.setInfoMessage("No existe procedimiento asociado");
        }
       
	}

	/*private void clonar_CONTRATACION_DATOS_LIC(IEntitiesAPI entitiesAPI,
			IRuleContext rulectx, String numExpHijo, ClientContext cct) {
		try{
			//Inicializa los datos de la CONTRATACION_PETICION
			logger.warn("INICIO CONTRATACION_DATOS_LIC ");
			IItemCollection  collection = null;
			Iterator it = null;
	        String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_LIC", consulta);
	        it = collection.iterator();
	        String expresiones_calculo_val = null;
	        String just_proceso = null;
	        String desc_ini_eventos = null;
	        String pres_ofert_org_contrat = null;
	        String present_oferta = null;
	        String tram_gasto = null;
	        String frec_pagos = null;
	        String tipo_oferta = null;
	        String val_tip_oferta = null;
	        String alg_calc_pond = null;
	        
	        
	        while (it.hasNext()){
	        	IItem procExpediente = (IItem)it.next();
	        	expresiones_calculo_val = procExpediente.getString("EXPRESIONES_CALCULO_VAL");
	        	just_proceso = procExpediente.getString("JUST_PROCESO");
	        	desc_ini_eventos = procExpediente.getString("DESC_INI_EVENTOS");
	        	pres_ofert_org_contrat = procExpediente.getString("PRES_OFERT_ORG_CONTRAT");
	        	present_oferta = procExpediente.getString("PRESENT_OFERTA");
	        	tram_gasto = procExpediente.getString("TRAM_GASTO");
	        	frec_pagos = procExpediente.getString("FREC_PAGOS");
	        	tipo_oferta = procExpediente.getString("TIPO_OFERTA");
	        	val_tip_oferta = procExpediente.getString("VAL_TIP_OFERTA");
	        	alg_calc_pond = procExpediente.getString("ALG_CALC_POND");
	        	
	        }
			IItem contrato = entitiesAPI.createEntity("CONTRATACION_DATOS_LIC", numExpHijo);
			if (contrato != null)
			{
				contrato.set("EXPRESIONES_CALCULO_VAL", expresiones_calculo_val);
	        	contrato.set("JUST_PROCESO", just_proceso);
	        	contrato.set("DESC_INI_EVENTOS", desc_ini_eventos);
	        	contrato.set("PRES_OFERT_ORG_CONTRAT", pres_ofert_org_contrat);
	        	contrato.set("PRESENT_OFERTA", present_oferta);
	        	contrato.set("TRAM_GASTO", tram_gasto);
	        	contrato.set("FREC_PAGOS", frec_pagos);
	        	contrato.set("TIPO_OFERTA", tipo_oferta);
	        	contrato.set("VAL_TIP_OFERTA", val_tip_oferta);
	        	contrato.set("ALG_CALC_POND", alg_calc_pond);
				contrato.store(cct);
			}
			logger.warn("CREADA CONTRATACION_DATOS_LIC ");
		} catch (ISPACException e) {
			logger.error(e.getMessage());
			throw new ISPACRuleException("Error. ",e);
		}
		
	}

	private int[] clonar_CONTRATACION_INF_EMPRESA(IEntitiesAPI entitiesAPI,
			IRuleContext rulectx, String numExpHijo, ClientContext cct) {
		int [] resultado = new int [2];
		try{
			//Inicializa los datos de la CONTRATACION_PETICION
			logger.warn("INICIO CONTRATACION_INF_EMPRESA");
			IItemCollection  collection = null;
			Iterator it = null;
	        String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			collection = entitiesAPI.queryEntities("CONTRATACION_INF_EMPRESA", consulta);
	        it = collection.iterator();
	        String cpv2 = "";
	        String cpv3 = "";
	        String cpv1 = "";
	        String cod_cpv1 = "";
	        String cod_cpv2 = "";
	        String cod_cpv3 = "";
	        String cod_cpv4 = "";
	        String cpv4 = "";
	        String cod_clas_emp1 = "";
	        String clas_emp1 = "";
	        String cod_clas_emp2 = "";
	        String clas_emp2 = "";
	        String cod_clas_emp3 = "";
	        String clas_emp3 = "";
	        String tip_eventos = "";
	        String tip_cap_tecn = "";
	        int idCodicePliegos = 0;
	        
	        while (it.hasNext()){
	        	IItem procExpediente = (IItem)it.next();
	        	cpv2 = procExpediente.getString("CPV2");
	        	cpv3 = procExpediente.getString("CPV3");
	        	cpv1 = procExpediente.getString("CPV1");
	        	cod_cpv1 = procExpediente.getString("COD_CPV1");
	        	cod_cpv2 = procExpediente.getString("COD_CPV2");
	        	cod_cpv3 = procExpediente.getString("COD_CPV3");
	        	cod_cpv4 = procExpediente.getString("COD_CPV4");
	        	cpv4 = procExpediente.getString("CPV4");
	        	cod_clas_emp1 = procExpediente.getString("COD_CLAS_EMP1");
	        	clas_emp1 = procExpediente.getString("CLAS_EMP1");
	        	cod_clas_emp2 = procExpediente.getString("COD_CLAS_EMP2");
	        	clas_emp2 = procExpediente.getString("CLAS_EMP2");
	        	cod_clas_emp3 = procExpediente.getString("COD_CLAS_EMP3");
	        	clas_emp3 = procExpediente.getString("CLAS_EMP3");
	        	tip_eventos = procExpediente.getString("TIP_EVENTOS");
	        	tip_cap_tecn = procExpediente.getString("TIP_CAP_TECN");
	        	idCodicePliegos = procExpediente.getInt("ID");
	            resultado[0] = idCodicePliegos;
	        }
	        int resNuevoId = 0;
			IItem contrato = entitiesAPI.createEntity("CONTRATACION_INF_EMPRESA", numExpHijo);
			if (contrato != null)
			{
				contrato.set("CPV2", cpv2);
	        	contrato.set("CPV3", cpv3);
	        	contrato.set("CPV1", cpv1);
	        	contrato.set("COD_CPV1", cod_cpv1);
	        	contrato.set("COD_CPV2", cod_cpv2);
	        	contrato.set("COD_CPV3", cod_cpv3);
	        	contrato.set("COD_CPV4", cod_cpv4);
	        	contrato.set("CPV4", cpv4);
	        	contrato.set("COD_CLAS_EMP1", cod_clas_emp1);
	        	contrato.set("CLAS_EMP1", clas_emp1);
	        	contrato.set("COD_CLAS_EMP2", cod_clas_emp2);
	        	contrato.set("CLAS_EMP2", clas_emp2);
	        	contrato.set("COD_CLAS_EMP3", cod_clas_emp3);
	        	contrato.set("CLAS_EMP3", clas_emp3);
	        	contrato.set("TIP_EVENTOS", tip_eventos);
	        	contrato.set("TIP_CAP_TECN", tip_cap_tecn);
				contrato.store(cct);
				resNuevoId = contrato.getInt("ID");
				resultado[1] = resNuevoId;
			}
			logger.warn("CREADA CONTRATACION_INF_EMPRESA ");
		} catch (ISPACException e) {
			logger.error(e.getMessage());
			throw new ISPACRuleException("Error. ",e);
		}
		return resultado;
	}*/

	private void clonar_tabla_s(IEntitiesAPI entitiesAPI,
			IRuleContext rulectx, String numExpHijo, ClientContext cct,
			int[] idDatos, String tablaBD) throws ISPACRuleException {
		try{
			//Inicializa los datos de la CONTRATACION_PETICION
			logger.warn("INICIO  "+tablaBD);
	        
	        String consulta="SELECT FIELD,REG_ID,VALUE FROM "+tablaBD+" WHERE REG_ID = "+idDatos[0]+"";
	        ResultSet datos = cct.getConnection().executeQuery(consulta).getResultSet();
	        String field = "";
	        String value = "";
	        Vector<String[]> valores = new Vector<String[]> ();
	        if(datos!=null)
	      	{
	        	while(datos.next()){
	        		String [] vDatos = new String [2];
	          		if (datos.getString("FIELD")!=null) field = datos.getString("FIELD"); else field="";
	          		if (datos.getString("VALUE")!=null) value = datos.getString("VALUE"); else value="";
	          		vDatos[0] = field;
	          		vDatos[1] = value;
	          		valores.add(vDatos);
	          	}
	      	}
	        DbCnt cnt = cct.getConnection();
	        
	        for (int i = 0; i < valores.size(); i++){
	        	String [] vDatos = (String[])valores.get(i);
	        	ContratacionDatosContratoSDAO pcftdao = new ContratacionDatosContratoSDAO(cnt);
	 			pcftdao.createNew(cnt);
	 			pcftdao.set("FIELD", vDatos[0]);
	 			pcftdao.set("REG_ID", idDatos[1]);
	 			pcftdao.set("VALUE", vDatos[1]);
	 			pcftdao.store(cnt);
	      	}
			
			logger.warn("CREADA "+tablaBD);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
	}
	
	

	/*private int[] clonar_dpcr_codice_pliegos(IEntitiesAPI entitiesAPI,
			IRuleContext rulectx, String numExpHijo, ClientContext cct) {
		int [] resultado = new int [2];
		try{
			//Inicializa los datos de la CONTRATACION_PETICION
			logger.warn("INICIO DPCR_CODICE_PLIEGOS");
			IItemCollection  collection = null;
			Iterator it = null;
	        String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			collection = entitiesAPI.queryEntities("DPCR_CODICE_PLIEGOS", consulta);
	        it = collection.iterator();
	        String tipo_contrato = "";
	        String contrato_sumin = "";
	        String proclicit = "";
	        String proc_cont_secpub = "";
	        String criterio_codigo = "";
	        String tipo_adjudicacion = "";
	        String org_contrat = "";
	        String contrato_sectpub = "";
	        int idCodicePliegos = 0;
	        
	        while (it.hasNext()){
	        	IItem procExpediente = (IItem)it.next();
	        	tipo_contrato = procExpediente.getString("TIPO_CONTRATO");
	        	contrato_sumin = procExpediente.getString("CONTRATO_SUMIN");
	        	proclicit = procExpediente.getString("PROCLICIT");
	        	proc_cont_secpub = procExpediente.getString("PROC_CONT_SECPUB");
	        	criterio_codigo = procExpediente.getString("CRITERIO_CODIGO");
	        	tipo_adjudicacion = procExpediente.getString("TIPO_ADJUDICACION");
	        	org_contrat = procExpediente.getString("ORG_CONTRAT");
	        	contrato_sectpub = procExpediente.getString("CONTRATO_SECTPUB");
	        	idCodicePliegos = procExpediente.getInt("ID");
	            resultado[0] = idCodicePliegos;
	        }
	        int resNuevoId = 0;
			IItem contrato = entitiesAPI.createEntity("DPCR_CODICE_PLIEGOS", numExpHijo);
			if (contrato != null)
			{
				contrato.set("TIPO_CONTRATO", tipo_contrato);
	        	contrato.set("CONTRATO_SUMIN", contrato_sumin);
	        	contrato.set("PROCLICIT", proclicit);
	        	contrato.set("PROC_CONT_SECPUB", proc_cont_secpub);
	        	contrato.set("CRITERIO_CODIGO", criterio_codigo);
	        	contrato.set("TIPO_ADJUDICACION", tipo_adjudicacion);
	        	contrato.set("ORG_CONTRAT", org_contrat);
	        	contrato.set("CONTRATO_SECTPUB", contrato_sectpub);
				contrato.store(cct);
				resNuevoId = contrato.getInt("ID");
				resultado[1] = resNuevoId;
			}
			logger.warn("CREADA DPCR_CODICE_PLIEGOS ");
		} catch (ISPACException e) {
			logger.error(e.getMessage());
			throw new ISPACRuleException("Error. ",e);
		}
		return resultado;
	}*/

	/*private void clonar_contratacion_datos_tramit(IEntitiesAPI entitiesAPI,
			IRuleContext rulectx, String numExpHijo, ClientContext cct) {
		try{
			//Inicializa los datos de la CONTRATACION_PETICION
			logger.warn("INICIO CONTRATACION_DATOS_TRAMIT ");
			IItemCollection  collection = null;
			Iterator it = null;
	        String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_TRAMIT", consulta);
	        it = collection.iterator();
	        Date f_apro_exp_cont = null;
	        Date f_pub_bop_exp_cont = null;
	        Date f_pub_doce_exp_cont = null;
	        Date f_term_pazo_presen_prop = null;
	        Date f_apert_propos = null;
	        int imp_adj_cont = 0;
	        String emp_adj_cont = "";
	        String nif_adjudicataria = "";
	        String repre_adjudicataria = "";
	        String domicilio_notif_adj = "";
	        Date f_contrato = null;
	        int garantia_definitiva = 0;
	        int garantia_complementaria = 0;
	        String plazo_ejec_contrat = "";
	        Date f_fin_plazo_ejec = null;
	        Date f_acta_compr_replanteo = null;
	        Date f_recepcion_prov = null;
	        Date f_recepcion_definitiva = null;
	        Date f_dev_garant_defin = null;
	        Date f_dev_garant_compl = null;
	        
	        while (it.hasNext()){
	        	IItem procExpediente = (IItem)it.next();
	        	f_apro_exp_cont = procExpediente.getDate("F_APRO_EXP_CONT");
	        	f_pub_bop_exp_cont = procExpediente.getDate("F_PUB_BOP_EXP_CONT");
	        	f_pub_doce_exp_cont = procExpediente.getDate("F_PUB_DOCE_EXP_CONT");
	        	f_term_pazo_presen_prop = procExpediente.getDate("F_TERM_PAZO_PRESEN_PROP");
	        	f_apert_propos = procExpediente.getDate("F_APERT_PROPOS");
	        	imp_adj_cont = procExpediente.getInt("IMP_ADJ_CONT");
	        	emp_adj_cont = procExpediente.getString("EMP_ADJ_CONT");
	        	nif_adjudicataria = procExpediente.getString("NIF_ADJUDICATARIA");
	        	repre_adjudicataria = procExpediente.getString("REPRE_ADJUDICATARIA");
	        	domicilio_notif_adj = procExpediente.getString("DOMICILIO_NOTIF_ADJ");
	        	f_contrato = procExpediente.getDate("F_CONTRATO");
	        	garantia_definitiva = procExpediente.getInt("GARANTIA_DEFINITIVA");
	        	garantia_complementaria = procExpediente.getInt("GARANTIA_COMPLEMENTARIA");
	        	plazo_ejec_contrat = procExpediente.getString("PLAZO_EJEC_CONTRAT");
	        	f_fin_plazo_ejec = procExpediente.getDate("F_FIN_PLAZO_EJEC");
	        	f_acta_compr_replanteo = procExpediente.getDate("F_ACTA_COMPR_REPLANTEO");
	        	f_recepcion_prov = procExpediente.getDate("F_RECEPCION_PROV");
	        	f_recepcion_definitiva = procExpediente.getDate("F_RECEPCION_DEFINITIVA");
	        	f_dev_garant_defin = procExpediente.getDate("F_DEV_GARANT_DEFIN");
	        	f_dev_garant_compl = procExpediente.getDate("F_DEV_GARANT_COMPL");
	        }
			IItem contrato = entitiesAPI.createEntity("CONTRATACION_DATOS_TRAMIT", numExpHijo);
			if (contrato != null)
			{
				contrato.set("F_APRO_EXP_CONT", f_apro_exp_cont);
	        	contrato.set("F_PUB_BOP_EXP_CONT", f_pub_bop_exp_cont);
	        	contrato.set("F_PUB_DOCE_EXP_CONT", f_pub_doce_exp_cont);
	        	contrato.set("F_TERM_PAZO_PRESEN_PROP", f_term_pazo_presen_prop);
	        	contrato.set("F_APERT_PROPOS", f_apert_propos);
	        	contrato.set("IMP_ADJ_CONT", imp_adj_cont);
	        	contrato.set("EMP_ADJ_CONT", emp_adj_cont);
	        	contrato.set("NIF_ADJUDICATARIA", nif_adjudicataria);
	        	contrato.set("REPRE_ADJUDICATARIA", repre_adjudicataria);
	        	contrato.set("DOMICILIO_NOTIF_ADJ", domicilio_notif_adj);
	        	contrato.set("F_CONTRATO", f_contrato);
	        	contrato.set("GARANTIA_DEFINITIVA", garantia_definitiva);
	        	contrato.set("GARANTIA_COMPLEMENTARIA", garantia_complementaria);
	        	contrato.set("PLAZO_EJEC_CONTRAT", plazo_ejec_contrat);
	        	contrato.set("F_FIN_PLAZO_EJEC", f_fin_plazo_ejec);
	        	contrato.set("F_ACTA_COMPR_REPLANTEO", f_acta_compr_replanteo);
	        	contrato.set("F_RECEPCION_PROV", f_recepcion_prov);
	        	contrato.set("F_RECEPCION_DEFINITIVA", f_recepcion_definitiva);
	        	contrato.set("F_DEV_GARANT_DEFIN", f_dev_garant_defin);
	        	contrato.set("F_DEV_GARANT_COMPL", f_dev_garant_compl);
				contrato.store(cct);
			}
			logger.warn("CREADA CONTRATACION_DATOS_TRAMIT ");
		} catch (ISPACException e) {
			logger.error(e.getMessage());
			throw new ISPACRuleException("Error. ",e);
		}
		
	}*/
	@SuppressWarnings("unchecked")
	private int[] clonar_contratacion_datos_contrato(IEntitiesAPI entitiesAPI,
			IRuleContext rulectx, String numExpHijo, ClientContext cct) throws ISPACRuleException {
		
		int [] resultado = new int [2];
		try{
			//Inicializa los datos de la CONTRATACION_PETICION
			logger.warn("INICIO CONTRATACION_DATOS_CONTRATO ");
			IItemCollection  collection = null;
	        String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_CONTRATO", consulta);
			Iterator<IItem> it = collection.iterator();
	        String ncontrato = "";
	        String objeto_contrato = "";
	        String tipo_contrato = "";
	        String cod_arc_clase_nomenc = "";
	        String proc_adj = "";
	        String forma_tramitacionDC = "";
	        String organo_contratacion = "";
	        String cont_suj_reg_armo = "";
	        String precio_estimado_contrato = "";
	        String contrato_sumin = "";
	        String tram_gasto = "";
	        int idDatosContrato = 0;
	        String caracteristicas_bienes = null;
	        String proc_neg_art = null;
	        String abierto_crit_multip = null;
	        String provincia_contrato = null;
	        
	        while (it.hasNext()){
	        	IItem procExpediente = (IItem)it.next();
	        	ncontrato = procExpediente.getString("NCONTRATO");
	            objeto_contrato = procExpediente.getString("OBJETO_CONTRATO");
	            tipo_contrato = procExpediente.getString("TIPO_CONTRATO");
	            cod_arc_clase_nomenc = procExpediente.getString("COD_ARC_CLASE_NOMENC");
	            proc_adj = procExpediente.getString("PROC_ADJ");
	            forma_tramitacionDC = procExpediente.getString("FORMA_TRAMITACION");
	            organo_contratacion = procExpediente.getString("ORGANO_CONTRATACION");
	            cont_suj_reg_armo = procExpediente.getString("CONT_SUJ_REG_ARMO");
	            precio_estimado_contrato = procExpediente.getString("PRECIO_ESTIMADO_CONTRATO");
	            idDatosContrato = procExpediente.getInt("ID");
	            resultado[0] = idDatosContrato;
	            tram_gasto = procExpediente.getString("TRAM_GASTO");
	            contrato_sumin = procExpediente.getString("CONTRATO_SUMIN");
	            if(procExpediente.getString("CARACTERISTICA_BIENES_RENDCUEN")!=null) caracteristicas_bienes= procExpediente.getString("CARACTERISTICA_BIENES_RENDCUEN");
	            if(procExpediente.getString("PROCNEGARTICULO")!=null) proc_neg_art = procExpediente.getString("PROCNEGARTICULO");
	            if(procExpediente.getString("ABIERTO_CRITERIOS_MULTIPLES")!=null) abierto_crit_multip=procExpediente.getString("ABIERTO_CRITERIOS_MULTIPLES");
	            if(procExpediente.getString("PROVINCIA_CONTRATO")!=null) provincia_contrato = procExpediente.getString("PROVINCIA_CONTRATO");
	        }
	        int resNuevoId = 0;
			IItem contrato = entitiesAPI.createEntity("CONTRATACION_DATOS_CONTRATO", numExpHijo);
			if (contrato != null)
			{
				//Insetar en el asunto del expediente el objeto del contrato
				IItem expediente = entitiesAPI.getExpedient(numExpHijo);  		
	     		expediente.set("ASUNTO", objeto_contrato);
	     		expediente.store(cct);
				
				contrato.set("NCONTRATO", ncontrato);
	            contrato.set("OBJETO_CONTRATO", objeto_contrato);
	            contrato.set("TIPO_CONTRATO", tipo_contrato);
	            contrato.set("COD_ARC_CLASE_NOMENC", cod_arc_clase_nomenc);
	            contrato.set("PROC_ADJ", proc_adj);
	            contrato.set("FORMA_TRAMITACION", forma_tramitacionDC);
	            contrato.set("ORGANO_CONTRATACION", organo_contratacion);
	            contrato.set("CONT_SUJ_REG_ARMO", cont_suj_reg_armo);
	            contrato.set("TRAM_GASTO", tram_gasto);
	            contrato.set("CONTRATO_SUMIN", contrato_sumin);
	            contrato.set("PRECIO_ESTIMADO_CONTRATO", precio_estimado_contrato);
	            if(caracteristicas_bienes!=null)	contrato.set("CARACTERISTICA_BIENES_RENDCUEN", caracteristicas_bienes);
	            if(proc_neg_art!=null) contrato.set("PROCNEGARTICULO", proc_neg_art);
	            if(abierto_crit_multip!=null) contrato.set("ABIERTO_CRITERIOS_MULTIPLES", abierto_crit_multip);
	            if(provincia_contrato!=null) contrato.set("PROVINCIA_CONTRATO", provincia_contrato);
				contrato.store(cct);
				resNuevoId = contrato.getInt("ID");
				resultado[1] = resNuevoId;
			}
			logger.warn("CREADA CONTRATACION_DATOS_CONTRATO ");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	private void clonar_contratacion_peticion(IEntitiesAPI entitiesAPI, IRuleContext rulectx, String numExpHijo, ClientContext cct) throws ISPACRuleException {
		try{
			//Actualizar las entidades con los datos
			//Inicializa los datos de la CONTRATACION_PETICION
			logger.warn("INICIO CONTRATACION_PETICION ");
			IItemCollection  collection = null;
	        String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			collection = entitiesAPI.queryEntities("CONTRATACION_PETICION", consulta);
			Iterator<IItem> it = collection.iterator();
	        String motivo_peticion = "";
	        String iva = "";
	        String presupuesto = "";
	        String total = "";
	        String serv_resp = "";
	        String resp_contrato = "";
	        String director_obra = "";
	        while (it.hasNext()){
	        	IItem procExpediente = (IItem)it.next();
	        	motivo_peticion = procExpediente.getString("MOTIVO_PETICION");
	        	iva = procExpediente.getString("IVA");
	        	presupuesto = procExpediente.getString("PRESUPUESTO");
	        	total = procExpediente.getString("TOTAL");
	        	serv_resp = procExpediente.getString("SERVICIO_RESPONSABLE");
	        	resp_contrato = procExpediente.getString("RESP_CONTRATO");
	        	director_obra = procExpediente.getString("DIRECTOR_OBRA");
	        }
	        
			IItem peticion = entitiesAPI.createEntity("CONTRATACION_PETICION", numExpHijo);
			if (peticion != null)
			{
				peticion.set("MOTIVO_PETICION", motivo_peticion);
				peticion.set("IVA", iva);
				peticion.set("PRESUPUESTO", presupuesto);
				peticion.set("TOTAL", total);
				peticion.set("SERVICIO_RESPONSABLE", serv_resp);
				peticion.set("RESP_CONTRATO", resp_contrato);
				peticion.set("DIRECTOR_OBRA", director_obra);
				peticion.store(cct);
			}
			logger.warn("CREADA CONTRATACION_PETICION ");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean cerrar = true;
		try{
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			//Obtengo el procedimiento que le corresponde
			//Dependiendo de los datos que se han obtenido.
			String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_CONTRATO", consulta);
	        Iterator<IItem> it = collection.iterator();
	        String tipo_contrato = "";
	        String proc_adjudicacion = "";
	        while (it.hasNext()){
	        	IItem contrato = (IItem)it.next();
	        	tipo_contrato = contrato.getString("TIPO_CONTRATO");
	        	proc_adjudicacion = contrato.getString("PROC_ADJ");

	        }
	        logger.warn("tipo_contrato. "+tipo_contrato);
	        logger.warn("proc_adjudicacion. "+proc_adjudicacion);
	        String cod_tipo_contrato = "";
	        String cod_proc_adjudicacion = "";
	        if(tipo_contrato!="" && proc_adjudicacion!="" ){
	        	String [] v_cod_tipo_contrato = tipo_contrato.split(" - ");
	        	if(v_cod_tipo_contrato.length > 0){
	        		cod_tipo_contrato = v_cod_tipo_contrato[0];
	        	}
	        	String [] v_cod_proc_adjudicacion = proc_adjudicacion.split(" - ");
	        	if(v_cod_proc_adjudicacion.length > 0){
	        		cod_proc_adjudicacion = v_cod_proc_adjudicacion[0];
	        	}
	        	
	        }
	        collection = null;
	        it = null;
	        consulta = "WHERE TIPO_CONTRATO = '"+cod_tipo_contrato+"' AND PROCEDIMIENTO_ADJUDICACION='"+cod_proc_adjudicacion+"'";
	        logger.warn(consulta);
			collection = entitiesAPI.queryEntities("CONTRATACION_RELACION_PROCED", consulta);
	        it = collection.iterator();
	        if(!it.hasNext()){
	        	logger.warn("No existe procedimiento aosciado");
	        	rulectx.setInfoMessage("No puede cerrarse el trámite porque no existe procedimiento asociado");
	        	cerrar = false;
	        }
		}catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return cerrar;
		
	}
	
}