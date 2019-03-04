package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.expedients.CommonData;
import ieci.tdw.ispac.api.expedients.Expedients;
import ieci.tdw.ispac.api.expedients.InterestedPerson;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.join.TableJoinFactoryDAO;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.DatosComunesExpediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InteresadoExpediente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class ExpedientesRelacionadosUtil {
	
	private static final Logger LOGGER = Logger.getLogger(ExpedientesRelacionadosUtil.class);
	
	public static final String VAR_PROCEDIMIENTOS_EXCLUIDOS_GET_TODOS_RELACIONADOS = "VAR_PROCEDIMIENTOS_EXCLUIDOS_GET_TODOS_RELACIONADOS"; 
	
	public static final String NUMEXP_PADRE = "NUMEXP_PADRE";
	public static final String NUMEXP_HIJO = "NUMEXP_HIJO";
	public static final String RELACION = "RELACION";
	
	public static final String ORDER_BY_HIJO_DESC = getOrderByHijoDesc();
	public static final String ORDER_BY_HIJO_ASC = getOrderByHijoAsc();
	
	public static final String STR_ORDER_BY = " ORDER BY SUBSTR(SPLIT_PART(%s, '/',1), 5)::INT %s, SPLIT_PART(%s, '/',2)::INT %s";
	public static final String ORDER_DESC = "DESC";
	public static final String ORDER_ASC = "ASC";

	public static final String NOMBREPROCPADRECARTADIGITAL = "Comunicación Administrativa Electrónica";
	public static final String RELACIONCARTADIGITAL = "Comunicación Administrativa Electrónica";
	
	public static final String NOMBREPROCPADREFISCALIZACIONSUBVENCIONES = "Fiscalización de subvenciones";
	public static final String RELACIONFISCALIZACIONSUBVENCIONES = "Fiscalización de subvenciones";
	
	public static final String NOMBREPROCPADREACCTELEXP = "Acceso Telemático Expedientes";
	public static final String RELACIONACCTELEXP = "Acceso Telemático Expedientes";
	
	public static final String NOMBREPROCPADRETABLONEDICTOS = "eTablón";
	public static final String RELACIONTABLONEDICOTOS = "Publicación en Tablón de Edictos";
	
	public static final String NOMBREPROCPADREPORTAFIRMA = "Firma de documentos en Port@firmas";
	public static final String RELACIONPORTAFIRMA = "Firma Doc. Externos";
	
	public static final String RELACIONPROPUESTA = "proc.Propuesta%";
	
	public static final String RELACIONDECRETOS = "proc.Decreto%";
	
	private ExpedientesRelacionadosUtil(){
	}
	
	public static String getOrderByHijoDesc(){
		return getOrderBy(NUMEXP_HIJO, ORDER_DESC);
	}
	
	public static String getOrderByHijoAsc(){
		return getOrderBy(NUMEXP_HIJO, ORDER_ASC);
	}
	
	public static String getOrderByPadreDesc(){
		return getOrderBy(NUMEXP_PADRE, ORDER_DESC);
	}
	
	public static String getOrderByPadreAsc(){
		return getOrderBy(NUMEXP_PADRE, ORDER_ASC);
	}
	
	public static String getOrderBy(String tipoExp, String orden){
		return String.format(STR_ORDER_BY, tipoExp, orden, tipoExp, orden);
	}
	
	public static IItemCollection getExpedientesByRelacion(IEntitiesAPI entitiesAPI, String numexpPadre, String relacion) throws ISPACException {
		IItemCollection itRelaciones = null;
		try{
			String consultaSQL = "WHERE " + ExpedientesRelacionadosUtil.NUMEXP_PADRE + " = '" + numexpPadre + "' and "+ExpedientesRelacionadosUtil.RELACION+" LIKE '"+relacion+"'";
			itRelaciones = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);

		} catch (ISPACException e) {
			LOGGER.error("Error al obtener los expedientes relacionado " + numexpPadre + ". " + e.getMessage(), e);
			throw new ISPACException("Error al obtener los expedientes relacionado " + numexpPadre + ". " + e.getMessage(), e);
		}
		
		return itRelaciones;
	}
	
	public static String iniciaExpedienteHijoFirmaDocExternos(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes) throws ISPACException{	
		return iniciaExpedienteHijoFirmaDocExternos(cct, numexpPadre, importaParticipantes, relacionaExpedientes, null);
	}
	
	public static String iniciaExpedienteHijoFirmaDocExternos(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes, IItem expedienteOrig) throws ISPACException{	
		return iniciaExpedienteHijoTipo(cct, numexpPadre, NOMBREPROCPADREPORTAFIRMA, RELACIONPORTAFIRMA, importaParticipantes, relacionaExpedientes, expedienteOrig);
	}
	
	public static String iniciaExpedienteHijoFiscalizacionSubvenciones(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes) throws ISPACException{	
		return iniciaExpedienteHijoFiscalizacionSubvenciones(cct, numexpPadre, importaParticipantes, relacionaExpedientes, null);
	}
	public static String iniciaExpedienteHijoFiscalizacionSubvenciones(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes, IItem expedienteOrig) throws ISPACException{	
		return iniciaExpedienteHijoTipo(cct, numexpPadre, NOMBREPROCPADREFISCALIZACIONSUBVENCIONES, RELACIONFISCALIZACIONSUBVENCIONES, importaParticipantes, relacionaExpedientes, expedienteOrig);
	}
	
	public static String iniciaExpedienteHijoCartaDigital(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes) throws ISPACException{	
		return iniciaExpedienteHijoCartaDigital(cct, numexpPadre, importaParticipantes, relacionaExpedientes, null);
	}
	
	public static String iniciaExpedienteHijoCartaDigital(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes, IItem expedienteOrig) throws ISPACException{	
		return iniciaExpedienteHijoTipo(cct, numexpPadre, NOMBREPROCPADRECARTADIGITAL, RELACIONCARTADIGITAL, importaParticipantes, relacionaExpedientes, expedienteOrig);
	}
	
	public static int buscaExpDelDepartamentoCartaDigital(IClientContext cct, String numexpPadre) throws ISPACException{
		return buscaExpDelDepartamento(cct, numexpPadre, NOMBREPROCPADRECARTADIGITAL);
	}
	
	public static String iniciaExpedienteHijoAccTelExp(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes) throws ISPACException{	
		return iniciaExpedienteHijoAccTelExp(cct, numexpPadre, importaParticipantes, relacionaExpedientes, null);
	}
	
	public static String iniciaExpedienteHijoAccTelExp(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes, IItem expedienteOrig) throws ISPACException{	
		return iniciaExpedienteHijoTipo(cct, numexpPadre, NOMBREPROCPADREACCTELEXP, RELACIONACCTELEXP, importaParticipantes, relacionaExpedientes, expedienteOrig);
	}
	
	public static int buscaExpDelDepartamentoAccTelExp(IClientContext cct, String numexpPadre) throws ISPACException{
		return buscaExpDelDepartamento(cct, numexpPadre, NOMBREPROCPADREACCTELEXP);
	}
	
	public static String iniciaExpedienteHijoTablonEdictos(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes) throws ISPACException{	
		return iniciaExpedienteHijoTablonEdictos(cct, numexpPadre, importaParticipantes, relacionaExpedientes, null);
	}
	
	public static String iniciaExpedienteHijoTablonEdictos(IClientContext cct, String numexpPadre, boolean importaParticipantes, boolean relacionaExpedientes, IItem expedienteOrig) throws ISPACException{	
		return iniciaExpedienteHijoTipo(cct, numexpPadre, NOMBREPROCPADRETABLONEDICTOS, RELACIONTABLONEDICOTOS, importaParticipantes, relacionaExpedientes, expedienteOrig);
	}
	
	public static int buscaExpDelDepartamentoTablonEdictos(IClientContext cct, String numexpPadre) throws ISPACException{
		return buscaExpDelDepartamento(cct, numexpPadre, NOMBREPROCPADRETABLONEDICTOS);
	}
	
	public static String iniciaExpedienteHijoTipo(IClientContext cct, String numexpPadre, String nombreProcedimientoPadre, String relacion, boolean importaParticipantes, boolean relacionaExpedientes, IItem expedienteOrig) throws ISPACException{
		String numexpHijo = "";
		int idProcedimientoHijo= 0;
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			idProcedimientoHijo = buscaExpDelDepartamento(cct, numexpPadre, nombreProcedimientoPadre);
			
			if(idProcedimientoHijo > 0){
				numexpHijo = (iniciaExpediente(cct, idProcedimientoHijo, expedienteOrig)).getString("NUMEXP");
				if(StringUtils.isNotEmpty(numexpHijo)){
					if(importaParticipantes){
						ParticipantesUtil.importarParticipantes((ClientContext)cct, entitiesAPI, numexpPadre, numexpHijo);
					}
					if(relacionaExpedientes){
						relacionaExpedientes(cct, numexpPadre, numexpHijo, relacion);
					}
				}
			}
		} catch(Exception e){
			LOGGER.error("Error al iniciar el expediente relacionado de tipo " + nombreProcedimientoPadre + ". " + e.getMessage(), e);
			throw new ISPACException("Error al iniciar el expediente relacionado de tipo " + nombreProcedimientoPadre + ". " + e.getMessage(), e);
		}
		return numexpHijo;	
	}	
	
	public static IItemCollection buscaTodosExpDelDepartamento (IClientContext cct, String numexpPadre) throws ISPACException{
		IItemCollection procedimientosDelDepartamento = null;
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
			
			int idPcdActual = ExpedientesUtil.getExpediente(cct, numexpPadre).getInt("ID_PCD");
			
			//Buscamos el procedimiento asociado a dicho código para recuperar el departamento
			IItem procedimiento = procedureAPI.getProcedureById(idPcdActual);
			if(procedimiento != null){
				String orgRsltr = (String) procedimiento.get("CTPROCEDIMIENTOS:ORG_RSLTR");
				if(StringUtils.isNotEmpty(orgRsltr)){
					procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE ORG_RSLTR = '" + orgRsltr + "'");
					
				}
			}
		} catch(Exception e){
			LOGGER.error("Error al recuperar el expediente hijo. " + e.getMessage(), e);
			throw new ISPACException( "Error al recuperar el expediente hijo. " + e.getMessage(), e);
		}
		return procedimientosDelDepartamento;
	}
	
	@SuppressWarnings("rawtypes")
	public static int buscaExpDelDepartamento (IClientContext cct, String numexpPadre, String nombreProcedimientoPadre) throws ISPACException{
		int idProcedimientoHijo = 0;
		try{
			IItemCollection procsCollection = buscaTodosExpDelDepartamento(cct, numexpPadre);
			Iterator procsIterator = procsCollection.iterator();
			boolean encontrado = false;
			while(procsIterator.hasNext() && !encontrado){
				IItem procs = (IItem) procsIterator.next();	
				idProcedimientoHijo = procs.getInt("ID");
				if(esProcTipo(cct, idProcedimientoHijo, nombreProcedimientoPadre)){
					encontrado = true;
					idProcedimientoHijo = procs.getInt("ID");
				}
				else{
					idProcedimientoHijo = 0;
				}
			}
		} catch(Exception e){
			LOGGER.error( "Error al recuperar el expediente hijo. " + e.getMessage(), e);
			throw new ISPACException( "Error al recuperar el expediente hijo. " + e.getMessage(), e);
		}
		return idProcedimientoHijo;
	}
	
	public static IItem iniciaExpediente(IClientContext cct, int idCtProcedimientoNuevo, IItem expedienteOrig) throws ISPACException{
		String numexpNuevo = null;
		IItem expNuevo = null;
		String asunto = "";

		// Ejecución en un contexto transaccional
		boolean ongoingTX = cct.ongoingTX();
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();
			IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
			
			IItem catalogo = procedureAPI.getProcedureById(idCtProcedimientoNuevo);
			String codigoProcedimientoNuevo = catalogo.getString("CTPROCEDIMIENTOS:COD_PCD");
	
			if(expedienteOrig != null){
				String nreg = "";
				try{
					if (!ongoingTX) {
						cct.beginTX();
					}
					asunto = expedienteOrig.getString(ExpedientesUtil.ASUNTO);
					nreg = expedienteOrig.getString(ExpedientesUtil.NREG);					
					
					InteresadoExpediente interesadoPrincipal = new InteresadoExpediente();
					interesadoPrincipal.setNifcif(expedienteOrig.getString(ExpedientesUtil.NIFCIFTITULAR));
					interesadoPrincipal.setName(expedienteOrig.getString(ExpedientesUtil.IDENTIDADTITULAR));
					interesadoPrincipal.setPostalCode(expedienteOrig.getString(ExpedientesUtil.CPOSTAL));
					interesadoPrincipal.setRegionCountry(expedienteOrig.getString(ExpedientesUtil.REGIONPAIS));
					interesadoPrincipal.setPostalAddress(expedienteOrig.getString(ExpedientesUtil.DOMICILIO));
					interesadoPrincipal.setPlaceCity(expedienteOrig.getString(ExpedientesUtil.CIUDAD));
					interesadoPrincipal.setNotificationAddressType(expedienteOrig.getString(ExpedientesUtil.TIPODIRECCIONINTERESADO));
					interesadoPrincipal.setIndPrincipal(InterestedPerson.IND_PRINCIPAL);
					
					InteresadoExpediente[] interesados = new InteresadoExpediente[1];
					interesados[0] = interesadoPrincipal;
					
					DatosComunesExpediente datosComunes = new DatosComunesExpediente();
					datosComunes.setFechaRegistro(expedienteOrig.getDate(ExpedientesUtil.FREG));
					datosComunes.setInteresados(interesados);
					datosComunes.setNumeroRegistro(nreg);
					datosComunes.setTipoAsunto(expedienteOrig.getString(ExpedientesUtil.ASUNTO));
					datosComunes.setIdOrganismo(expedienteOrig.getString(ExpedientesUtil.ASUNTO));
			        
			        CommonData commonData = getCommonData(datosComunes);
			        commonData.setSubjectType(codigoProcedimientoNuevo);
			        
					// Crear el API de expedientes
			    	Expedients expedientsAPI = new Expedients((ClientContext) cct);
			        numexpNuevo =  expedientsAPI.initExpedient(commonData, null, null, "RT");
				} catch(Exception e){
					LOGGER.error("Error al iniciar un nuevo expediente para el registro: " + nreg + ". " + e.getMessage(), e);
					if (!ongoingTX) {
						cct.endTX(false);
					}
					throw new ISPACException("Error al iniciar un nuevo expediente para el registro: " + nreg + ". " + e.getMessage(), e);
				} finally {
					if (!ongoingTX) {
						cct.endTX(true);
					}
				}			
			} else{				
				Map<String, String> params = new HashMap<String, String>();
				params.put("COD_PCD", codigoProcedimientoNuevo);
		
				// Crear el proceso del expediente
				int nIdProcess2 = tx.createProcess(idCtProcedimientoNuevo, params);
				IProcess process;
							
				process = cct.getAPI().getProcess(nIdProcess2);
				
				numexpNuevo = process.getString("NUMEXP"); 
			}
			
			expNuevo = cct.getAPI().getEntitiesAPI().getExpedient(numexpNuevo);
			if(StringUtils.isNotEmpty(asunto) && expNuevo != null){
				expNuevo.set("ASUNTO", asunto); 
			}
			expNuevo.store(cct);
		} catch(Exception e){
			LOGGER.error("Error al iniciar un nuevo expediente. " + e.getMessage(), e);
			throw new ISPACException("Error al iniciar un nuevo expediente. " + e.getMessage(), e);
		}
		
		return expNuevo;
	}
	
	public static void relacionaExpedientes(IClientContext cct, String numexpPadre, String numexpHijo, String relacion) throws ISPACException{
		try{
			IItem registro = cct.getAPI().getEntitiesAPI().createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
			registro.set(ExpedientesRelacionadosUtil.NUMEXP_PADRE, numexpPadre);
			registro.set(ExpedientesRelacionadosUtil.NUMEXP_HIJO, numexpHijo);
			registro.set(ExpedientesRelacionadosUtil.RELACION, relacion);
			registro.store(cct);
		} catch(Exception e){
			LOGGER.error("Error al relacionar expedientes (" +numexpPadre + ", " + numexpHijo + ", " + relacion +")." + e.getMessage(), e);
			throw new ISPACException("Error al relacionar expedientes (" +numexpPadre + ", " + numexpHijo + ", " + relacion +")." + e.getMessage(), e);
		}
	}
	
	public static IItem iniciaExpedienteRelacionadoHijo (IClientContext cct, int idCtProcedimientoNuevo, String numexpPadre, String relacion, boolean importaParticipantes, IItem expedienteOrig) throws ISPACRuleException{
		IItem expHijo = null;	
		try{
			expHijo = iniciaExpediente(cct, idCtProcedimientoNuevo, expedienteOrig);
			String numexpHijo = expHijo.getString("NUMEXP");
			relacionaExpedientes(cct, numexpPadre, numexpHijo, relacion);	
			if(importaParticipantes){
				ParticipantesUtil.importarParticipantes((ClientContext)cct, cct.getAPI().getEntitiesAPI(), numexpPadre, numexpHijo);
			}
		} catch(ISPACRuleException e){
			LOGGER.error("Error al iniciar y relacionar un expediente hijo del expediente: " + numexpPadre + ". " +e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar y relacionar un expediente hijo del expediente: " + numexpPadre + ". " +e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Error al iniciar y relacionar un expediente hijo del expediente: " + numexpPadre + ". " +e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar y relacionar un expediente hijo del expediente: " + numexpPadre + ". " +e.getMessage(), e);
		}
		return expHijo;
		
	}
	
	public static boolean esProcTipo(IClientContext cct, int idProcedimiento, String tipo) throws ISPACException {

		boolean resultado = false;
		try {
			IItem catalogo = cct.getAPI().getProcedureAPI().getProcedureById( idProcedimiento);
			int idPadre = catalogo.getInt("CTPROCEDIMIENTOS:ID_PADRE");			
			if (idPadre != -1) {				
				resultado = esProcTipo(cct, idPadre, tipo);
			} else {
				String nomPcd = "";
				if (catalogo.getString("CTPROCEDIMIENTOS:NOMBRE") != null){
					nomPcd = catalogo.getString("CTPROCEDIMIENTOS:NOMBRE");
					if (nomPcd.toUpperCase().indexOf(tipo.toUpperCase()) >= 0) {
						resultado = true;					
					}
				}
			}
		} catch(Exception e){
			LOGGER.error("Erro al consultar el tipo de procedimiento." + e.getMessage(), e);
			throw new ISPACException("Erro al consultar el tipo de procedimiento." + e.getMessage(), e);
		}
		return resultado;
	}
	
	/**
	 * MQE método que devuelve una lista con todos los expedientes relacionados con el que se pasa como parámetro, por debajo de él
	 * es decir todos sus expedientes hijos y los hijos de sus hijos.
	 * Controlamos que no se produzcan bucles, para ello antes de incluir un expediente, comprobamos que no se encuentre ya incluido.
	 */
	public static ArrayList<String> getProcedimientosRelacionadosHijos(String numExpPadre, IEntitiesAPI entitiesAPI) throws ISPACRuleException{
		return getProcedimientosRelacionadosHijos(numExpPadre, entitiesAPI, new ArrayList<String>());		
	}
	
	@SuppressWarnings("rawtypes")
	public static ArrayList<String> getProcedimientosRelacionadosHijos(String numExpPadre, IEntitiesAPI entitiesAPI, ArrayList<String> resultadoTemp)throws ISPACRuleException{
		
		ArrayList<String> resultado = new ArrayList<String>();
		ArrayList<String> resultadoAux = new ArrayList<String>();

		//En primer lugar añadimos el padre, como primer expediente
		resultado.add(numExpPadre);

		try{
			//Buscamos los expedientes relacionados hijos
	        String consultaSQL = "WHERE " + ExpedientesRelacionadosUtil.NUMEXP_PADRE + " = '" + numExpPadre + "'";
	        IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
	        
	        //Si no tenemos hijos padre no hacemos nada
	        if(itemCollection != null && itemCollection.next()){
				Iterator it = itemCollection.iterator();
		        IItem item = null;
		        
		        String numexpHijo = "";
		        //Para cada hijo
		        while (it.hasNext()) {
	                item = ((IItem)it.next());
	                numexpHijo = item.getString(ExpedientesRelacionadosUtil.NUMEXP_HIJO);

	                //Si ya está incluido en resultado de esta llamada, no hacemos nada
	                boolean existe = false;
	                for (int i = 0; i < resultado.size() && !existe; i++){                	
	                	if(numexpHijo.equals(resultado.get(i))){
	                			existe = true;
	                	}
	                }
	                //Si ya está incluido en los resultado temporales que llevamos tampoco hacemos nada
	                boolean existe2 = false;
	                for (int i = 0; i < resultadoTemp.size() && !existe2; i++){                	
	                	if(numexpHijo.equals(resultadoTemp.get(i)))
	                			existe2 = true;	                	
	                }
	                //Si no existe lo añadimos y buscamos sus hijos y los añadimos al resultado
	                if(!existe && !existe2) {	                		       
	                	resultadoAux = getProcedimientosRelacionadosHijos(numexpHijo, entitiesAPI, resultado);
	                	//Añadimos al resultado el resultado de resultadoTemp
		                for (int j=  0; j < resultadoAux.size(); j++){
		                	resultado.add(resultadoAux.get(j));
		                }
	                }			
		        }
	        }
		} catch(ISPACException e){
        	LOGGER.error("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
        	throw new ISPACRuleException("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
        }
        return resultado;
	}
	
	public static List<String> getProcedimientosRelacionadosHijosByVariosEstadosAdm(IRuleContext rulectx, String numExpPadre, List<String> resultadoTemp, List<String> estadosAdm)throws ISPACRuleException{
        
        List<String> resultado = new ArrayList<String>();
        List<String> resultadoAux = new ArrayList<String>();
        
        try{
        	IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
        	
            //En primer lugar añadimos el padre, como primer expediente
            if(null != estadosAdm && !estadosAdm.isEmpty()){
            	IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numExpPadre);
            	if(expediente != null){
            		String estadoAdm = expediente.getString(ExpedientesUtil.ESTADOADM);
            		if(estadosAdm.contains(estadoAdm)){
            			resultado.add(numExpPadre);
            		}
                }
            } else {
            	resultado.add(numExpPadre);
            }
            //Buscamos los expedientes relacionados hijos
            String consultaSQL = "WHERE " + ExpedientesRelacionadosUtil.NUMEXP_PADRE + " = '" + numExpPadre + "'";
            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
            
            //Si no tenemos hijos padre no hacemos nada
            if(itemCollection != null && itemCollection.next()){
                Iterator<?> it = itemCollection.iterator();
                IItem item = null;
                
                String numexpHijo = "";
                //Para cada hijo
                while (it.hasNext()) {
                    item = ((IItem)it.next());
                    numexpHijo = item.getString(ExpedientesRelacionadosUtil.NUMEXP_HIJO);

                    //Si ya está incluido en resultado de esta llamada, no hacemos nada
                    boolean existe = false;
                    for (int i = 0; i < resultado.size() && !existe; i++){                    
                        if(numexpHijo.equals(resultado.get(i))){
                                existe = true;
                        }
                    }
                    //Si ya está incluido en los resultado temporales que llevamos tampoco hacemos nada
                    boolean existe2 = false;
                    for (int i = 0; i < resultadoTemp.size() && !existe2; i++){                    
                        if(numexpHijo.equals(resultadoTemp.get(i))){
                            existe2 = true;                        
                        }
                    }
                    //Si no existe lo añadimos y buscamos sus hijos y los añadimos al resultado
                    if(!existe && !existe2) {                                   
                        resultadoAux = getProcedimientosRelacionadosHijosByVariosEstadosAdm(rulectx, numexpHijo, (ArrayList<String>) resultado, estadosAdm);
                        //Añadimos al resultado el resultado de resultadoTemp
                        for (int j=  0; j < resultadoAux.size(); j++){
                            resultado.add(resultadoAux.get(j));
                        }
                    }            
                }
            }
        } catch(ISPACException e){
            LOGGER.error("ERROR al recuperar los expedientes relacionados hijos del expediente: " + numExpPadre + ". " + e.getMessage(), e);
            throw new ISPACRuleException("ERROR  al recuperar los expedientes relacionados hijos del expediente: " + numExpPadre + ". " +  e.getMessage(), e);
        }
        return resultado;
    }
	
	
	/**
	 * MQE fin moficaciones
	 */
	/**
	 * MQE método que devuelve una lista con todos los expedientes relacionados con el que se pasa como parámetro, por debajo de él
	 * es decir todos sus expedientes hijos y los hijos de sus hijos.
	 * Controlamos que no se produzcan bucles, para ello antes de incluir un expediente, comprobamos que no se encuentre ya incluido.
	 */
	public static ArrayList<String> getProcedimientosRelacionadosPadres(String numExpPadre, IEntitiesAPI entitiesAPI)throws ISPACRuleException{
		return getProcedimientosRelacionadosPadres(numExpPadre, entitiesAPI, new ArrayList<String>());		
	}
	@SuppressWarnings("rawtypes")
	public static ArrayList<String> getProcedimientosRelacionadosPadres(String numExpHijo, IEntitiesAPI entitiesAPI, ArrayList<String> resultadoTemp) throws ISPACRuleException{
		
		ArrayList<String> resultado = new ArrayList<String>();
		ArrayList<String> resultadoAux = new ArrayList<String>();

		//En primer lugar añadimos el padre, como primer expediente
		resultado.add(numExpHijo);

		try{
			//Buscamos los expedientes relacionados hijos
	        String consultaSQL = "WHERE " + ExpedientesRelacionadosUtil.NUMEXP_HIJO + " = '" + numExpHijo + "'";
	        IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
	        
	        //Si no tenemos hijos padre no hacemos nada
	        if(itemCollection != null && itemCollection.next()){
				Iterator it = itemCollection.iterator();
		        IItem item = null;
		        
		        String numexpPadre = "";
		        //Para cada hijo
		        while (it.hasNext()) {
	                item = ((IItem)it.next());
	                numexpPadre = item.getString(ExpedientesRelacionadosUtil.NUMEXP_PADRE);
	                //Si ya está incluido en resultado de esta llamada, no hacemos nada
	                boolean existe = false;
	                for (int i = 0; i < resultado.size() && !existe; i++){                	
	                	if(numexpPadre.equals(resultado.get(i))){
	                			existe = true;
	                	}
	                }
	                //Si ya está incluido en los resultado temporales que llevamos tampoco hacemos nada
	                boolean existe2 = false;
	                for (int i = 0; i < resultadoTemp.size() && !existe2; i++){                	
	                	if(numexpPadre.equals(resultadoTemp.get(i)))
	                			existe2 = true;	                	
	                }
	                //Si no existe lo añadimos y buscamos sus hijos y los añadimos al resultado
	                if(!existe && !existe2) {	                		       
	                	resultadoAux = getProcedimientosRelacionadosPadres(numexpPadre, entitiesAPI, resultado);
	                	//Añadimos al resultado el resultado de resultadoTemp
		                for (int j=  0; j < resultadoAux.size(); j++){
		                	resultado.add(resultadoAux.get(j));
		                }
	                }			
		        }
	        }
		} catch(ISPACException e){
        	LOGGER.error("ERROR al recuperar los expedientes relacionados padres: "+e.getMessage(), e);
        	throw new ISPACRuleException("ERROR al recuperar los expedientes relacionados padres: "+e.getMessage(), e);
        }
        return resultado;
	}
	
	/**
	 * MQE fin moficaciones
	 */
	
	private static CommonData getCommonData(DatosComunesExpediente datosComunes) {
		CommonData commonData = null;

		if (datosComunes != null) {
			commonData = new CommonData();
			commonData.setOrganismId(datosComunes.getIdOrganismo());
			commonData.setRegisterNumber(datosComunes.getNumeroRegistro());
			commonData.setRegisterDate(datosComunes.getFechaRegistro());
			commonData.setInterested(getInterestedList(datosComunes.getInteresados()));
		}
		return commonData;
	}
	 
	private static List<InterestedPerson> getInterestedList(InteresadoExpediente[] interesados) {
		List<InterestedPerson> interestedList = new ArrayList<InterestedPerson>();
		
		if (interesados != null) {
			for (int i = 0; i < interesados.length; i++) {
				interestedList.add(getInterestedPerson(interesados[i]));
			}
		}
		
		return interestedList;
	}
	
	private static InterestedPerson getInterestedPerson(InteresadoExpediente inter) {
		InterestedPerson interested = null;

		if (null != inter) {
			interested = new InterestedPerson();
			interested.setThirdPartyId(inter.getThirdPartyId());
			interested.setIndPrincipal(inter.getIndPrincipal());
			interested.setNifcif(inter.getNifcif());
			interested.setName(inter.getName());
			interested.setPostalAddress(inter.getPostalAddress());
			interested.setPostalCode(inter.getPostalCode());
			interested.setPlaceCity(inter.getPlaceCity());
			interested.setRegionCountry(inter.getRegionCountry());
			interested.setTelematicAddress(inter.getTelematicAddress());
			interested.setNotificationAddressType(inter.getNotificationAddressType());
			interested.setPhone(inter.getPhone());
			interested.setMobilePhone(inter.getMobilePhone());
		}
		
		return interested;
	}
	
	public static List<String> getExpedientesRelacionadosHijosByEstadoAdm(IRuleContext rulectx, String estadoAdm) throws ISPACException{
		return getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, "", "");
	}
	
	public static List<String> getExpedientesRelacionadosHijosByEstadoAdm(IRuleContext rulectx, String estadoAdm, String orderBy) throws ISPACException{
		return getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, "", orderBy);
	}
	
	public static List<String> getExpedientesRelacionadosHijosByEstadoAdmCondicion(IRuleContext rulectx, String estadoAdm, String condicion) throws ISPACException{
		return getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, condicion, "");
	}
		
	public static List<String> getExpedientesRelacionadosHijosByEstadoAdm(IRuleContext rulectx, String estadoAdm, String condicion, String orderBy) throws ISPACException{
		List<String> estadosAdmList = new ArrayList<String>();
		estadosAdmList.add(estadoAdm);
		
		return getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosAdmList, condicion, orderBy);
	}
	
	public static List<String> getExpedientesRelacionadosHijosByVariosEstadosAdm(IRuleContext rulectx, List<String> estadosAdmList) throws ISPACException{
		return getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosAdmList, "", "");
	}
	
	public static List<String> getExpedientesRelacionadosHijosByVariosEstadosAdm(IRuleContext rulectx, List<String> estadosAdmList, String orderBy) throws ISPACException{
		return getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosAdmList, "", orderBy);
	}
	
	public static List<String> getExpedientesRelacionadosHijosByVariosEstadosAdmCondicion(IRuleContext rulectx, List<String> estadosAdmList, String condicion) throws ISPACException{
		return getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosAdmList, condicion, "");
	}
	
	public static List<String> getExpedientesRelacionadosHijosByVariosEstadosAdm(IRuleContext rulectx, List<String> estadosAdmList, String condicion, String orderBy) throws ISPACException{
		String numexp = null;
		
		try{
			numexp = rulectx.getNumExp();
			IClientContext cct = rulectx.getClientContext();
			String strQuery = " WHERE R.NUMEXP_PADRE = '" + numexp + "' AND R.NUMEXP_HIJO = E.NUMEXP " + " AND ESTADOADM IN ";
			
			strQuery += " ('";
			if(null != estadosAdmList && !estadosAdmList.isEmpty()){
				strQuery += StringUtils.join(estadosAdmList, "','");
			} 	
			strQuery += "') ";

			if(StringUtils.isNotEmpty(condicion)){
				if(!condicion.trim().toUpperCase().startsWith("AND") || ! !condicion.trim().toUpperCase().startsWith("OR")){
					strQuery += " AND ";
				}
				strQuery += condicion;
			}
			
			if(StringUtils.isNotEmpty(orderBy)){
				if(orderBy.toUpperCase().indexOf("ORDER BY") > 0){
					strQuery += orderBy;
				} else {
					strQuery += " ORDER BY " + orderBy;
				}
			}

			TableJoinFactoryDAO factory = new TableJoinFactoryDAO();
			factory.addTable(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "R");
			factory.addTable(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "E");

			CollectionDAO collectionJoin = factory.queryTableJoin(cct.getConnection(), strQuery);
			collectionJoin.disconnect();
			
			//Recorremos los registros recuperados
			ArrayList<String> listNumexpRel = new ArrayList<String>();
			while (collectionJoin.next()){
				IItem item = (IItem) collectionJoin.value();
				listNumexpRel.add(item.getString("E:NUMEXP"));
			}
			return listNumexpRel;
			
		} catch(Exception ex) {
			String error = "Error al obtener los expedientes relacionados hijos del expediente " + numexp + " para los estados administrativos " + estadosAdmList;
			LOGGER.error(error, ex);
			throw new ISPACException(error, ex);
		}
	}
	
	public static List<String> getExpedientesRelacionadosHijosByQuery(IClientContext cct, String consulta) throws ISPACException{
		
		List<String> listNumexpRel = new ArrayList<String>();
		
		try{
			if(StringUtils.isNotEmpty(consulta)){
				if(!consulta.toUpperCase().trim().startsWith("WHERE")){
					consulta = " WHERE " + consulta;
				}

				TableJoinFactoryDAO factory = new TableJoinFactoryDAO();
				factory.addTable(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "R");
				factory.addTable(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "E");
	
				CollectionDAO collectionJoin = factory.queryTableJoin(cct.getConnection(), consulta);
				collectionJoin.disconnect();
				
				while (collectionJoin.next()) {
					IItem item = (IItem) collectionJoin.value();
					listNumexpRel.add(item.getString("E:NUMEXP"));
				}
			}
			return listNumexpRel;
		} catch(Exception ex){
			String error = "Error al obtener los expedientes relacionados hijos del expediente para la consulta: " + consulta;
			LOGGER.error(error, ex);
			throw new ISPACException(error, ex);
		}
	}

	/**
	 * [1239 SIGEM - CONTRATACIÓN] 
	 * Inicio
	 * Crear un trámite que genere un foliado de todo el expediente de contratación ordenado por fecha de creación del documento.
	 * **/
	public static Vector<String> getTodosExpRelacionados(IRuleContext rulectx, String numexp) throws ISPACRuleException {
		Vector<String> resultado = new Vector<String>();

		getTodosExpRelacionados(rulectx, numexp, resultado);
		
		return resultado;
	}
	
	public static void getTodosExpRelacionados(IRuleContext rulectx, String numexp, Vector<String> resultado) throws ISPACRuleException {
		try{
			/****************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlow = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlow.getEntitiesAPI();
			/****************************************************************************/
			
			Vector<String> numexpRelacionados = getExpedientesRelacionados(entitiesAPI, numexp);
			
			if (null != numexpRelacionados && !numexpRelacionados.isEmpty()){
				for(String numexpAux : numexpRelacionados){					
					if(!resultado.contains(numexpAux)){
						resultado.add(numexpAux);
						if(!isEnProcedimientosExcluidosGetTodosExpRelacionados(rulectx, numexpAux)){
							getTodosExpRelacionados(rulectx, numexpAux, resultado);
						}
					}
				}				
			}
		} catch(ISPACException e) {
        	LOGGER.error("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
        	throw new ISPACRuleException("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
		}
	}
	
	public static boolean isEnProcedimientosExcluidosGetTodosExpRelacionados(IRuleContext rulectx, String numexp) throws ISPACRuleException {
		boolean estaExcluido = false;
		try{
			String sListaExcluidos = DipucrCommonFunctions.getVarGlobal(VAR_PROCEDIMIENTOS_EXCLUIDOS_GET_TODOS_RELACIONADOS);
			if(StringUtils.isNotEmpty(sListaExcluidos)){
				IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numexp);
				if(null != expediente){
					String codPcdExpediente = expediente.getString(ExpedientesUtil.CODPROCEDIMIENTO);
					String[] listaCodProcedimientosExcluidos = sListaExcluidos.split(";");
					
					if(null != listaCodProcedimientosExcluidos && listaCodProcedimientosExcluidos.length > 0){
						for(String codProcedimientoExcluido : listaCodProcedimientosExcluidos){
							if(codPcdExpediente.equals(codProcedimientoExcluido)){
								estaExcluido = true;
							}
						}
					}
				}
			} else {
				estaExcluido = true;
			}
		} catch (ISPACRuleException e) {
			LOGGER.error("Error al comprobar si está en la lista de excluidos al recuperar todos los relaionados. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al comprobar si está en la lista de excluidos al recuperar todos los relaionados. " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Error al comprobar si está en la lista de excluidos al recuperar todos los relaionados. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al comprobar si está en la lista de excluidos al recuperar todos los relaionados. " + e.getMessage(), e);
		}
		return estaExcluido;
	}

	public static Vector<String> getExpedientesRelacionados(IEntitiesAPI entitiesAPI, String numexp) throws ISPACRuleException{
		Vector<String> numexpRelacionados = new Vector<String>();
		
		numexpRelacionados.addAll(getExpRelacionadosHijos(entitiesAPI, numexp));
		numexpRelacionados.addAll(getExpRelacionadosPadres(entitiesAPI, numexp));

		return numexpRelacionados;
	}
	
	public static Vector<String> getExpRelacionadosPadres (IEntitiesAPI entitiesAPI, String numexp) throws ISPACRuleException{
		return getExpRelacionadoByExpediente_Columna(entitiesAPI, numexp, NUMEXP_HIJO, NUMEXP_PADRE);	
	}
	public static Vector<String> getExpRelacionadosHijos (IEntitiesAPI entitiesAPI, String numexp) throws ISPACRuleException{
		return getExpRelacionadoByExpediente_Columna(entitiesAPI, numexp, NUMEXP_PADRE, NUMEXP_HIJO);	
	}
	
	private static Vector<String> getExpRelacionadoByExpediente_Columna(IEntitiesAPI entitiesAPI, String numexp, String nombreColumna, String campoBusqueda) throws ISPACRuleException {
		Vector<String> resultado = new Vector<String>();
		try{
	        String consultaSQL = "WHERE " + nombreColumna + " = '" + numexp + "'";
	        IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);       
			Iterator<?> it = itemCollection.iterator();
			
	        while (it.hasNext()) {
	        	 IItem item = (IItem)it.next();
	        	 String numexpRelacionado = item.getString(campoBusqueda);
	        	 if(StringUtils.isNotEmpty(numexpRelacionado) && !"null".equals(numexpRelacionado)){
	        		 resultado.add(numexpRelacionado);
	        	 }
	        }
		} catch(ISPACException e){
            LOGGER.error("ERROR al recuperar los expedientes relacionados del expediente: " + numexp + ". " + e.getMessage(), e);
            throw new ISPACRuleException("ERROR al recuperar los expedientes relacionados del expediente: " + numexp + ". " + e.getMessage(), e);
        }
		return resultado;
	}
}
