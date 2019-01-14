package es.dipucr.sigem.scheduler;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.scheduler.SchedulerTask;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.bdns.concesiones.client.BDNSConcesionesPagosClient;
import es.dipucr.bdns.dao.DevolucionDAO;
import es.dipucr.bdns.dao.PagoDAO;
import es.dipucr.bdns.devoluciones.client.BDNSDevolucionRentegrosClient;
import es.dipucr.bdns.objetos.Devolucion;
import es.dipucr.bdns.objetos.Pago;
import es.dipucr.factura.domain.bean.IngresosBean;
import es.dipucr.factura.domain.bean.OperacionGastosBean;
import es.dipucr.factura.services.factura.FacturaWSProxy;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.NumerosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * Tarea para recuperar anuncios de la BDNS y crear expedientes relacionados
 * para su inserción en el BOP
 */
public class EnviarPagosSicalBDNSTask extends SchedulerTask {

	/** Constantes **/
	public final static String SEND_PAGOS_BDNS_VARNAME = "SEND_PAGOS_BDNS";
	public final static String MAIL_ERROR_SUBJECT_VARNAME = "BDNS_SENDPAGOS_ERROR_MAIL_SUBJECT";
	public final static String MAIL_ERROR_CONTENT_VARNAME = "BDNS_SENDPAGOS_ERROR_MAIL_CONTENT";
	public final static String MAIL_ERROR_TO_VARNAME = "BDNS_SENDPAGOS_ERROR_MAIL_TO";
	public final static String MAXFECHA_EJERCICIO = "BDNS_SENDPAGOS_EJERCICIO_MAXFECHA";
	
	public final static String COD_TRAM_ALTA_PAGOS = "BDNS-PAGO-ALTA";
	
	public static final String SICALWIN_ESTADO_PAGO_REAL = "R";
	public static final String SICALWIN_ESTADO_OP_INGRESO = "REIN";
	public static final String SICALWIN_FASE_INGRESO = "I";
	
	/** Logger de la clase. */
    private static final Logger LOGGER = Logger.getLogger(EnviarPagosSicalBDNSTask.class);
    
    
    /**
     * Ejecuta la tarea del scheduler.
     */
    public void run() {
    	
    	ClientContext cct = new ClientContext();
		IInvesflowAPI invesFlowAPI = new InvesflowAPI(cct);
		cct.setAPI(invesFlowAPI);
    	
    	try{
    		
	        LOGGER.warn("INICIO Ejecución EnviarPagosSicalBDNSTask");
	        LOGGER.warn("*****************************************");
	        
			List<Entidad> entidades = EntidadesUtil.obtenerListaEntidades();
	        
	        if (!CollectionUtils.isEmpty(entidades)) {
	        	for (int i = 0; i < entidades.size(); i++) {
	        		
	        		Entidad entidad = entidades.get(i);
	
	        		if (entidad != null) {
	
	        			// Establecer la entidad en el thread local
	    				EntidadHelper.setEntidad(entidad);
	    				
	        			String sBDNSEntidad = null;
	        			try{
	        				sBDNSEntidad = ConfigurationMgr.getVarGlobal(cct, SEND_PAGOS_BDNS_VARNAME);
	        			}
	        			catch(ISPACException ex){
	        				LOGGER.info("Error al recuperar la variable SEND_PAGOS_BDNS de la entidad", ex);
	        			}
	        			
	        			boolean bProcesarEntidad = !StringUtils.isEmpty(sBDNSEntidad) && Boolean.valueOf(sBDNSEntidad).booleanValue();
	        			if (bProcesarEntidad){
	        			
	                        LOGGER.warn("*** Inicio de proceso de entidad: " 
	                        		+ entidad.getIdentificador() + " - " + entidad.getNombre());
		        			
		    				// Comprobar el estado de notificación
		    				execute(entidad);
		
	                        LOGGER.warn("*** Fin de proceso de entidad: " 
	                        		+ entidad.getIdentificador() + " - " + entidad.getNombre());
	        			}
	        		}
	        	}
	        }
	        
	        LOGGER.warn("FIN Ejecución EnviarPagosSicalBDNSTask");
	        LOGGER.warn("**************************************");
    	}
    	catch(Exception ex){
    		LOGGER.error("Error al enviar los pagos de Sical a la BDNS", ex);
    		enviarCorreoError(cct, ex);
    	}
    }

	public void execute(Entidad entidad) throws Exception {
    	
    	ClientContext cct = new ClientContext();
    	IInvesflowAPI invesFlowAPI = new InvesflowAPI(cct);
    	cct.setAPI(invesFlowAPI);
    	
    	try {
    		
    		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
    		
			//Recuperamos las concesiones no pagadas en cuyo expediente haya un trámite de alta de pagos abierto
    		StringBuffer sbQueryConcesiones = new StringBuffer();
    		sbQueryConcesiones.append("WHERE NUMEXP IN (SELECT NUMEXP FROM SPAC_TRAMITES WHERE ID_CTTRAMITE=");
    		sbQueryConcesiones.append("(SELECT ID FROM SPAC_CT_TRAMITES WHERE COD_TRAM='");
    		sbQueryConcesiones.append(COD_TRAM_ALTA_PAGOS);
    		sbQueryConcesiones.append("')) ORDER BY NUMEXP, ID_CONVOCATORIA, CIF_BENEFICIARIO");
    		IItemCollection colConcesiones = entitiesAPI.queryEntities("BDNS_IGAE_CONCESION", sbQueryConcesiones.toString());
    		@SuppressWarnings("rawtypes")
			Iterator itConcesiones = colConcesiones.iterator();
    		
    		PagoDAO pagoDAO = new PagoDAO();
			DevolucionDAO devolucionDAO = new DevolucionDAO();
    		
			FacturaWSProxy wsFactura = new FacturaWSProxy();
			ArrayList<Pago> listPagos = new ArrayList<Pago>();
			ArrayList<Devolucion> listDevoluciones = new ArrayList<Devolucion>();
			
			String numexpConvocatoria = null;
			String numexpAnterior = null;
			IItem itemConvocatoria = null;
    		
			/**
			 * 1. RECUPERACIÓN DE TODOS LOS PAGOS Y DEVOLUCIONES QUE TODAVÍA NO SE HAN ENVIADO A LA BDNS
			 */
    		while(itConcesiones.hasNext()){
    			
    			IItem itemConcesion = (IItem) itConcesiones.next();
    			numexpConvocatoria = itemConcesion.getString("NUMEXP");
    			if (!numexpConvocatoria.equals(numexpAnterior)){
    				IItemCollection colConvocatorias = entitiesAPI.queryEntities("BDNS_IGAE_CONVOCATORIA", "WHERE NUMEXP='" + numexpConvocatoria + "'");
    				itemConvocatoria = (IItem) colConvocatorias.iterator().next();
    				numexpAnterior = numexpConvocatoria;
    			}
				
    			boolean bPagada = (Constants.VALIDACION.SI.equals(itemConcesion.get("PAGADA")));
    			String cifBeneficiario = itemConcesion.getString("CIF_BENEFICIARIO");
    			String idConvocatoria = itemConcesion.getString("ID_CONVOCATORIA");
    			String refConcesion = itemConcesion.getString("NUMEXP_SOLICITUD");
    			String refContable =  itemConvocatoria.getString("REF_CONTABLE");

    			//[dipucr-Felipe #466]
    			//No está recuperando pagos del año anterior, pues no enviaron los pagos a su debido tiempo
//    			String ejercicio = getEjercicio(cct);
    			String ejercicio = itemConvocatoria.getString("EJERCICIO");
    			
    			//[dipucr-Felipe #467]
    			listPagos = new ArrayList<Pago>();
    			listDevoluciones = new ArrayList<Devolucion>();
    			
    			/** 1. Buscamos nuevos pagos sólo para las concesiones no pagadas **/
    			if (!bPagada){
    				
					OperacionGastosBean[] arrPagos = wsFactura.recuperarOperacionesGastoDefinitivas(entidad.getIdentificador(), 
							ejercicio, cifBeneficiario, refContable, SICALWIN_ESTADO_PAGO_REAL);
					if (null != arrPagos && arrPagos.length > 0){
						
						for (OperacionGastosBean datosOperacionSical : arrPagos){

							Pago pago = pagoDAO.newObjectNotPersist(numexpConvocatoria, cifBeneficiario, 
									idConvocatoria, refConcesion, datosOperacionSical.getNumOperacion(),
									datosOperacionSical.getFechaOperacion(), datosOperacionSical.getImporte());
							
							boolean bExistePago = pagoDAO.existsBBDD(entitiesAPI, pago);
							if (!bExistePago){
								listPagos.add(pago);
							}
						}
					}
    			}
    			
    			/** 2. Buscamos devoluciones/reintegros para todas, pagadas y no pagadas, que tengan el trámite de pagos abierto **/
    			/** 2.1. Los reintegros de un mismo año se buscan como operaciones de gasto con fase "REIN" **/
    			OperacionGastosBean[] arrReintegrosMismoAnio = wsFactura.recuperarOperacionesGastoDefinitivas
    					(entidad.getIdentificador(), ejercicio, cifBeneficiario, refContable, SICALWIN_ESTADO_OP_INGRESO);
    			
				if (null != arrReintegrosMismoAnio && arrReintegrosMismoAnio.length > 0){
					
					for (OperacionGastosBean datosOperacionSical : arrReintegrosMismoAnio){

						Devolucion devolucion = devolucionDAO.newObjectNotPersist(numexpConvocatoria, cifBeneficiario, 
								idConvocatoria, refConcesion, datosOperacionSical.getNumOperacion(),
								datosOperacionSical.getFechaOperacion(), datosOperacionSical.getImporte());

						boolean bExisteDevolucion = devolucionDAO.existsBBDD(entitiesAPI, devolucion);
						if (!bExisteDevolucion){
							listDevoluciones.add(devolucion);
						}
					}
				}

    			/** 2.2. Los reintegros/devoluciones en años distintos, están en la BBDD de Sicalwin como ingresos, con el grupo apuntes = ref.contable **/
				IngresosBean[] arrIngresos = wsFactura.recuperarIngresos
						(entidad.getIdentificador(), ejercicio, cifBeneficiario, refContable, SICALWIN_FASE_INGRESO);
				
				if (null != arrIngresos && arrIngresos.length > 0){
					
					for (IngresosBean ingreso : arrIngresos){

						Devolucion devolucion = devolucionDAO.newObjectNotPersist(numexpConvocatoria, cifBeneficiario, idConvocatoria, 
								refConcesion, ingreso.getNumOperacion(), ingreso.getFechaOperacion(), ingreso.getImporte());
						
						boolean bExisteDevolucion = devolucionDAO.existsBBDD(entitiesAPI, devolucion);
						if (!bExisteDevolucion){
							listDevoluciones.add(devolucion);
						}
					}
				}
				
				//INICIO [dipucr-Felipe #467]
				//Movemos esta sección dentro del bucle de concesiones (estaba fuera)
				//con el fin de tener información individualizada del pago que da el error
				/**
				 * 3. ENVÍO DE LOS PAGOS Y DEVOLUCIONES A LA BDNS
				 * 4. ACTUALIZACIÓN DE LOS PAGOS Y DEVOLUCIONES EN LA BBDD DE SIGEM
				 */
	    		if (listPagos.size() > 0){
	    			try{
	    				BDNSConcesionesPagosClient.altaModifPagos(cct, listPagos);
	    	    		LOGGER.warn("PAGOS: Se han dado de alta los siguientes pagos en la BDNS");
	    	    		for (Pago pago : listPagos){
	    	    			pagoDAO.store(cct, pago);
	    	    			LOGGER.warn(pago.toStringLine());
	    				}
	    			}
	    			catch(Exception ex){
	    				String error = "PAGOS: Error al remitir los pagos de la convocatoria " + numexpConvocatoria +
	    						". Beneficiario: " + cifBeneficiario + " (expediente " + refConcesion + ")";
	    				LOGGER.warn(error, ex);
	    				for (Pago pago : listPagos){
	    	    			LOGGER.warn(pago.toStringLine());
	    				}
	    				throw new ISPACException(error, ex);
	    			}
	    		}
	    		
	    		if (listDevoluciones.size() > 0){
	    			try{
			    		BDNSDevolucionRentegrosClient.altaModifDevoluciones(cct, listDevoluciones);
			    		LOGGER.warn("REINTEGROS: Se han dado de alta los siguientes reintegros/devoluciones en la BDNS");
			    		for (Devolucion devolucion : listDevoluciones){
							devolucionDAO.store(cct, devolucion);
			    			LOGGER.warn(devolucion.toStringLine());
						}
	    			}
	    			catch(Exception ex){
	    				String error = "REINTEGROS: Error al remitir los reintegros/devoluciones de la convocatoria " + numexpConvocatoria +
	    						". Beneficiario: " + cifBeneficiario + " (expediente " + refConcesion + ")";
	    				LOGGER.warn(error, ex);
	    				for (Devolucion devolucion : listDevoluciones){
			    			LOGGER.warn(devolucion.toStringLine());
						}
	    				throw new ISPACException(error, ex);
	    			}
	    		}
	    		//FIN [dipucr-Felipe #467]
    		}
    		
			/**
			 * 5. COMPROBACIÓN DE CONCESIONES - PARA MARCAR O DESMARCAR SI ESTÁN PAGADAS
			 */
    		colConcesiones = entitiesAPI.queryEntities("BDNS_IGAE_CONCESION", sbQueryConcesiones.toString());
    		itConcesiones = colConcesiones.iterator();
    		
    		while(itConcesiones.hasNext()){
    			IItem itemConcesion = (IItem) itConcesiones.next();
    			
    			//Recuperamos todos los pagos de la concesion para sumar el importe
    			String idConvocatoria = itemConcesion.getString("ID_CONVOCATORIA");
    			String cifBeneficiario = itemConcesion.getString("CIF_BENEFICIARIO");
    			List<Pago> listPagosConcesion = pagoDAO.getPagosByConcesion(cct, idConvocatoria, cifBeneficiario);
    			List<Devolucion> listDevolucionesConcesion = devolucionDAO.getDevolucionesByConcesion(cct, idConvocatoria, cifBeneficiario);
				
				if (listPagosConcesion.size() > 0 || listDevolucionesConcesion.size() > 0){

					double importeAcumulado = 0.0;
					
					for (Pago pago : listPagosConcesion){
						importeAcumulado += pago.getImportePagado();
					}
					for (Devolucion devolucion : listDevolucionesConcesion){
						importeAcumulado -= devolucion.getImporteDevolucion();
					}
					
					double importeConcesion = itemConcesion.getDouble("SUBVENCION");
					if (!StringUtils.isEmpty(itemConcesion.getString("DEVOLUCION"))){
						double devolucion = itemConcesion.getDouble("DEVOLUCION");
						importeConcesion -= devolucion;
					}
					
					//[dipucr-Felipe #632]
					importeAcumulado = NumerosUtil.redondear(importeAcumulado);
					importeConcesion = NumerosUtil.redondear(importeConcesion);
					
					if(importeAcumulado == importeConcesion){
						itemConcesion.set("PAGADA", Constants.VALIDACION.SI);
						itemConcesion.store(cct);
					}
					else if(importeAcumulado < importeConcesion){ //Concesiones devueltas
						itemConcesion.set("PAGADA", Constants.VALIDACION.NO);
						itemConcesion.store(cct);
					}
					else if(importeAcumulado > importeConcesion){
						String error = "El importe acumulado de los pagos de la concesion '" + 
								idConvocatoria + "-" + cifBeneficiario + "' es superior al importe total concedido";
						LOGGER.warn("PAGOS:");
						for (Pago pago : listPagosConcesion){
							LOGGER.warn(pago.toStringLine());
						}
						LOGGER.warn("DEVOLUCIONES:");
						for (Devolucion dev : listDevolucionesConcesion){
							LOGGER.warn(dev.toStringLine());
						}
						LOGGER.warn("Importe acumulado: " + importeAcumulado);
						LOGGER.warn("Importe concesión: " + importeConcesion);
						throw new ISPACException(error);
					}
				}
    		}
    	}
    	catch (Exception e){
        	LOGGER.error("[" + entidad.getIdentificador() + "]" + "Error al enviar los pagos de Sical a la BDNS", e);
        	throw e;
    	}
    }

	/**
	 * Devuelve el ejercicio de contabilidad para el que tenemos que consultar
	 * Hasta el día 20 de Enero aprox. de un año seguiremos consultando el año anterior. Según variable BDNS_SENDPAGOS_EJERCICIO_MAXFECHA
	 * @return
	 * @throws ISPACException 
	 */
	private String getEjercicio(IClientContext cct) throws Exception{
		
		int anio = FechasUtil.getAnyoActual();
		String sMaxDiaEjercicio = ConfigurationMgr.getVarGlobal(cct, MAXFECHA_EJERCICIO);
		sMaxDiaEjercicio = sMaxDiaEjercicio.replace("yyyy", String.valueOf(anio));
		
		Date dMaxDiaEjercicio = FechasUtil.convertToDate(sMaxDiaEjercicio);
		Date dFechaActual = new Date();
		String ejercicio = null;
		if (dFechaActual.before(dMaxDiaEjercicio)){
			ejercicio = String.valueOf(anio - 1);
		}
		else{
			ejercicio = String.valueOf(anio);
		}
		return ejercicio;
	}
	
	
	/**
	 * Enviar un correo de error al administrador
	 * @param ctx
	 * @param ex
	 * @throws ISPACException 
	 */
	private void enviarCorreoError(IClientContext cct, Exception ex) {

		try{
			String strDirNotif = ConfigurationMgr.getVarGlobal(cct, MAIL_ERROR_TO_VARNAME);
			HashMap<String, String> variables = new HashMap<String, String>();
			variables.put("EXCEPCION", ex.getMessage());
			String causa = "";
			if (null != ex.getCause()){
				causa = ex.getCause().getMessage();
			}
			variables.put("CAUSA", causa);
			
			MailUtil.enviarCorreoConVariables
				(cct, strDirNotif, MAIL_ERROR_SUBJECT_VARNAME, MAIL_ERROR_CONTENT_VARNAME, null, variables);
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			LOGGER.error("[" + idEntidad + "] Correo de error enviado: " + ex.getMessage() + " : " + ex.getCause(), ex);
		}
		catch(Exception ex1){
			LOGGER.error("Se produjo una excepción al enviar el correo de error", ex1);
		}
	}
	
}