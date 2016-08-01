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
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.DatosComunesExpediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InteresadoExpediente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class ExpedientesRelacionadosUtil {
	
	private static final Logger logger = Logger.getLogger(ExpedientesRelacionadosUtil.class);

	public static final String NOMBREPROCPADRECARTADIGITAL = "Comunicación Administrativa Electrónica";
	public static final String RELACIONCARTADIGITAL = "Comunicación Administrativa Electrónica";
	
	public static final String NOMBREPROCPADREFISCALIZACIONSUBVENCIONES = "Fiscalización de subvenciones";
	public static final String RELACIONFISCALIZACIONSUBVENCIONES = "Fiscalización de subvenciones";
	
	public static final String NOMBREPROCPADREACCTELEXP = "Acceso Telemático Expedientes";
	public static final String RELACIONACCTELEXP = "Acceso Telemático Expedientes";
	
	public static final String NOMBREPROCPADRETABLONEDICTOS = "eTablón";
	public static final String RELACIONTABLONEDICOTOS = "Publicación en Tablón de Edictos";
	
	public static final String NOMBREPROCPADREPORTAFIRMA = "Firma Documentos Externos";
	public static final String RELACIONPORTAFIRMA = "Firma Doc. Externos";
	
	@SuppressWarnings("unchecked")
	public static Vector<String> obtenerExpRelacionadoByExpediente_Columna( IEntitiesAPI entitiesAPI, String numexp, String nombreColumna, String campoBusqueda) throws ISPACRuleException {
		Vector<String> resultado = null;
		try{
			//Buscamos los expedientes relacionados hijos
	        String consultaSQL = "WHERE "+nombreColumna+" = '" + numexp + "'";
	        IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
	        
	        //Si no tenemos hijos padre no hacemos nada
	        if(itemCollection != null && itemCollection.toList().size()>0){
				Iterator<IItem> it = itemCollection.iterator();
		        IItem item = null;
		        resultado = new Vector<String>();
		        String numexp_padre = "";
		        //Para cada hijo
		        while (it.hasNext()) {
		        	 item = it.next();
		             numexp_padre = item.getString(campoBusqueda);
		             resultado.add(numexp_padre);
		        }
	        }
		}catch(ISPACException e){
            	logger.error("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
            	throw new ISPACRuleException("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
         }
		return resultado;
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
					if(importaParticipantes)ParticipantesUtil.importarParticipantes((ClientContext)cct, entitiesAPI, numexpPadre, numexpHijo);
					if(relacionaExpedientes)relacionaExpedientes(cct, numexpPadre, numexpHijo, relacion);
				}
			}
		}
		catch(Exception e){
			logger.error("Error al iniciar el expediente relacionado de tipo " + nombreProcedimientoPadre + ". " + e.getMessage(), e);
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
			
			int id_pcd_actual = ExpedientesUtil.getExpediente(cct, numexpPadre).getInt("ID_PCD");
			
			//Buscamos el procedimiento asociado a dicho código para recuperar el departamento
			IItem procedimiento = procedureAPI.getProcedureById(id_pcd_actual);
			if(procedimiento != null){
				String org_rsltr = (String) procedimiento.get("CTPROCEDIMIENTOS:ORG_RSLTR");
				if(StringUtils.isNotEmpty(org_rsltr)){
					procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE ORG_RSLTR = '" + org_rsltr + "'");
					
				}
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar el expediente hijo. " + e.getMessage(), e);
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
				}
			}
		}
		catch(Exception e){
			logger.error( "Error al recuperar el expediente hijo. " + e.getMessage(), e);
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
		boolean bCommit = false;
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
					asunto = expedienteOrig.getString("ASUNTO");
					nreg = expedienteOrig.getString("NREG");					
					
					InteresadoExpediente interesadoPrincipal = new InteresadoExpediente();
					interesadoPrincipal.setNifcif(expedienteOrig.getString("NIFCIFTITULAR"));
					interesadoPrincipal.setName(expedienteOrig.getString("IDENTIDADTITULAR"));
					interesadoPrincipal.setPostalCode(expedienteOrig.getString("CPOSTAL"));
					interesadoPrincipal.setRegionCountry(expedienteOrig.getString("REGIONPAIS"));
					interesadoPrincipal.setPostalAddress(expedienteOrig.getString("DOMICILIO"));
					interesadoPrincipal.setPlaceCity(expedienteOrig.getString("CIUDAD"));
					interesadoPrincipal.setNotificationAddressType(expedienteOrig.getString("TIPODIRECCIONINTERESADO"));
					interesadoPrincipal.setIndPrincipal(InterestedPerson.IND_PRINCIPAL);
					
					InteresadoExpediente[] interesados = new InteresadoExpediente[1];
					interesados[0] = interesadoPrincipal;
					
					DatosComunesExpediente datosComunes = new DatosComunesExpediente();
					datosComunes.setFechaRegistro(expedienteOrig.getDate("FREG"));
					datosComunes.setInteresados(interesados);
					datosComunes.setNumeroRegistro(nreg);
					datosComunes.setTipoAsunto(expedienteOrig.getString("ASUNTO"));
					datosComunes.setIdOrganismo(expedienteOrig.getString("ASUNTO"));
			        
			        CommonData commonData = getCommonData(datosComunes);
			        commonData.setSubjectType(codigoProcedimientoNuevo);
			        
					// Crear el API de expedientes
			    	Expedients expedientsAPI = new Expedients((ClientContext) cct);
			        numexpNuevo =  expedientsAPI.initExpedient(commonData, null, null, "RT");
				}
				catch(Exception e){
					logger.error("Error al iniciar un nuevo expediente para el registro: " + nreg + ". " + e.getMessage(), e);
					throw new ISPACException("Error al iniciar un nuevo expediente para el registro: " + nreg + ". " + e.getMessage(), e);
				}
				finally {
					if (!ongoingTX) {
						cct.endTX(bCommit);
					}
				}				
			}
			else{				
				Map<String, String> params = new HashMap<String, String>();
				params.put("COD_PCD", codigoProcedimientoNuevo);
		
				// Crear el proceso del expediente
				int nIdProcess2 = tx.createProcess(idCtProcedimientoNuevo, params);
				IProcess process;
							
				process = cct.getAPI().getProcess(nIdProcess2);
				
				numexpNuevo = process.getString("NUMEXP"); 
			}
			
			expNuevo = cct.getAPI().getEntitiesAPI().getExpedient(numexpNuevo);
			if(StringUtils.isNotEmpty(asunto) && expNuevo != null)
				expNuevo.set("ASUNTO", asunto); 
			expNuevo.store(cct);
			
			bCommit = true;
		}
		catch(Exception e){
			logger.error("Error al iniciar un nuevo expediente. " + e.getMessage(), e);
			throw new ISPACException("Error al iniciar un nuevo expediente. " + e.getMessage(), e);
		}
		
		return expNuevo;
	}
	
	public static void relacionaExpedientes(IClientContext cct, String numexpPadre, String numexpHijo, String relacion) throws ISPACException{
		try{
			IItem registro = cct.getAPI().getEntitiesAPI().createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
			registro.set("NUMEXP_PADRE", numexpPadre);
			registro.set("NUMEXP_HIJO", numexpHijo);
			registro.set("RELACION", relacion);
			registro.store(cct);
		}
		catch(Exception e){
			logger.error("Error al relacionar expedientes (" +numexpPadre + ", " + numexpHijo + ", " + relacion +")." + e.getMessage(), e);
			throw new ISPACException("Error al relacionar expedientes (" +numexpPadre + ", " + numexpHijo + ", " + relacion +")." + e.getMessage(), e);
		}
	}
	
	public static IItem iniciaExpedienteRelacionadoHijo (IClientContext cct, int idCtProcedimientoNuevo, String numexpPadre, String relacion, boolean importaParticipantes, IItem expedienteOrig) throws ISPACRuleException{
		IItem expHijo = null;	
		try{
			expHijo = iniciaExpediente(cct, idCtProcedimientoNuevo, expedienteOrig);
			String numexpHijo = expHijo.getString("NUMEXP");
			relacionaExpedientes(cct, numexpPadre, numexpHijo, relacion);	
			if(importaParticipantes)
				ParticipantesUtil.importarParticipantes((ClientContext)cct, cct.getAPI().getEntitiesAPI(), numexpPadre, numexpHijo);
		}
		catch(ISPACRuleException e){
			logger.error("Error al iniciar y relacionar un expediente hijo del expediente: " + numexpPadre + ". " +e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar y relacionar un expediente hijo del expediente: " + numexpPadre + ". " +e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al iniciar y relacionar un expediente hijo del expediente: " + numexpPadre + ". " +e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar y relacionar un expediente hijo del expediente: " + numexpPadre + ". " +e.getMessage(), e);
		}
		return expHijo;
		
	}
	
	public static boolean esProcTipo(IClientContext cct, int idProcedimiento, String tipo) throws ISPACException {

		boolean resultado = false;
		try {
			IItem catalogo = cct.getAPI().getProcedureAPI().getProcedureById( idProcedimiento);
			int id_padre = catalogo.getInt("CTPROCEDIMIENTOS:ID_PADRE");			
			if (id_padre != -1) {				
				resultado = esProcTipo(cct, id_padre, tipo);
			} else {
				String nom_pcd = "";
				if (catalogo.getString("CTPROCEDIMIENTOS:NOMBRE") != null){
					nom_pcd = catalogo.getString("CTPROCEDIMIENTOS:NOMBRE");
					if (nom_pcd.toUpperCase().indexOf(tipo.toUpperCase()) >= 0) {
						resultado = true;					
					}
				}
			}
		}catch(Exception e){
			logger.error("Erro al consultar el tipo de procedimiento." + e.getMessage(), e);
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
	        String consultaSQL = "WHERE NUMEXP_PADRE = '" + numExpPadre + "'";
	        IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
	        
	        //Si no tenemos hijos padre no hacemos nada
	        if(itemCollection != null && itemCollection.next()){
				Iterator it = itemCollection.iterator();
		        IItem item = null;
		        
		        String numexp_hijo = "";
		        //Para cada hijo
		        while (it.hasNext()) {
	                item = ((IItem)it.next());
	                numexp_hijo = item.getString("NUMEXP_HIJO");

	                //Si ya está incluido en resultado de esta llamada, no hacemos nada
	                boolean existe = false;
	                for (int i = 0; i < resultado.size() && !existe; i++){                	
	                	if(numexp_hijo.equals(resultado.get(i))){
	                			existe = true;
	                	}
	                }
	                //Si ya está incluido en los resultado temporales que llevamos tampoco hacemos nada
	                boolean existe2 = false;
	                for (int i = 0; i < resultadoTemp.size() && !existe2; i++){                	
	                	if(numexp_hijo.equals(resultadoTemp.get(i)))
	                			existe2 = true;	                	
	                }
	                //Si no existe lo añadimos y buscamos sus hijos y los añadimos al resultado
	                if(!existe && !existe2) {	                		       
	                	resultadoAux = getProcedimientosRelacionadosHijos(numexp_hijo, entitiesAPI, resultado);
	                	//Añadimos al resultado el resultado de resultadoTemp
		                for (int j=  0; j < resultadoAux.size(); j++){
		                	resultado.add(resultadoAux.get(j));
		                }
	                }			
		        }
	        }
		}
        catch(ISPACException e){
        	logger.error("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
        	throw new ISPACRuleException("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
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
	        String consultaSQL = "WHERE NUMEXP_HIJO = '" + numExpHijo + "'";
	        IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
	        
	        //Si no tenemos hijos padre no hacemos nada
	        if(itemCollection != null && itemCollection.next()){
				Iterator it = itemCollection.iterator();
		        IItem item = null;
		        
		        String numexp_padre = "";
		        //Para cada hijo
		        while (it.hasNext()) {
	                item = ((IItem)it.next());
	                numexp_padre = item.getString("NUMEXP_PADRE");
	                //Si ya está incluido en resultado de esta llamada, no hacemos nada
	                boolean existe = false;
	                for (int i = 0; i < resultado.size() && !existe; i++){                	
	                	if(numexp_padre.equals(resultado.get(i))){
	                			existe = true;
	                	}
	                }
	                //Si ya está incluido en los resultado temporales que llevamos tampoco hacemos nada
	                boolean existe2 = false;
	                for (int i = 0; i < resultadoTemp.size() && !existe2; i++){                	
	                	if(numexp_padre.equals(resultadoTemp.get(i)))
	                			existe2 = true;	                	
	                }
	                //Si no existe lo añadimos y buscamos sus hijos y los añadimos al resultado
	                if(!existe && !existe2) {	                		       
	                	resultadoAux = getProcedimientosRelacionadosPadres(numexp_padre, entitiesAPI, resultado);
	                	//Añadimos al resultado el resultado de resultadoTemp
		                for (int j=  0; j < resultadoAux.size(); j++){
		                	resultado.add(resultadoAux.get(j));
		                }
	                }			
		        }
	        }
		}
        catch(ISPACException e){
        	logger.error("ERROR al recuperar los expedientes relacionados padres: "+e.getMessage(), e);
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
			 commonData.setInterested(
					 getInterestedList(datosComunes.getInteresados()));
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
	    	
	    if (inter != null) {
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

	/**
	 * [1239 SIGEM - CONTRATACIÓN] 
	 * Inicio
	 * Crear un trámite que genere un foliado de todo el expediente de contratación ordenado por fecha de creación del documento.
	 * **/
	public static Vector<String> recuperarTodosExpRelacionados(IRuleContext rulectx, String numexp, Vector<String> resultadoAux) throws ISPACRuleException {
		Vector<String> resultado = new Vector<String>();
		try{
			/****************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlow = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlow.getEntitiesAPI();
			/****************************************************************************/
			
			Vector<String> numexpHijo = obtenerExpRelacionadoByExpediente_Columna(entitiesAPI, numexp, "NUMEXP_HIJO", "NUMEXP_PADRE");
			Vector<String> numexpPadre = obtenerExpRelacionadoByExpediente_Columna(entitiesAPI, numexp, "NUMEXP_PADRE", "NUMEXP_HIJO");
			Vector<String> numexpAmbos = new Vector<String>();
			if(numexpHijo!=null){
				numexpAmbos.addAll(numexpHijo);
			}
			if(numexpPadre!=null){
				numexpAmbos.addAll(numexpPadre);
			}			
			for(int i=0; i<numexpAmbos.size(); i++){
				String numexpAux = numexpAmbos.get(i);
				if(!resultadoAux.contains(numexpAux)){
					//Compruebo que no sea un expediente del BOP o de órganos Colegiados
					//Ya que esos expedientes tienen muchos expedientes relacionados que no nos interesan
					//Hablando con Felipe el BOP no tiene expedientes relacionados.
					resultadoAux.add(numexpAux);
					if(!SecretariaUtil.esExpedienteProcedimientoOrganosColegiados(rulectx, numexp)){
						resultadoAux.addAll(recuperarTodosExpRelacionados(rulectx, numexpAux, resultadoAux));
					}
				}				
			}
			resultado.addAll(resultadoAux);	
		}catch(ISPACException e){
        	logger.error("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
        	throw new ISPACRuleException("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
		}
		return resultado;
	}
	
}
