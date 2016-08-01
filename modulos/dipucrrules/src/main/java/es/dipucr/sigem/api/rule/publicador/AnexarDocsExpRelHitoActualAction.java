package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacpublicador.business.attribute.AttributeContext;
import ieci.tdw.ispac.ispacpublicador.business.context.RuleContext;
import ieci.tdw.ispac.ispacpublicador.business.exceptions.ActionException;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.FicheroHito;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.HitosExpediente;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.core.services.repositorio.ServicioRepositorioDocumentosTramitacion;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.publicador.service.ConsultaTelematicaService;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;

/**
 * Acción para anexar los documentos firmados del trámite que se está cerrando al hito actual (Trámite iniciado) de un expediente 
 * en la Consulta Telemática.
 * 
 */
public class AnexarDocsExpRelHitoActualAction extends SigemBaseAction {

	ClientContext context = null;
	/** Logger de la clase. */
    private static final Logger logger = Logger.getLogger(AnexarDocsExpRelHitoActualAction.class);

	/** Logger de la clase. */
    private static final Logger CONSULTA_TELEMATICA = Logger.getLogger("CONSULTA_TELEMATICA");

    
    /**
     * Constructor.
     * 
     */
    public AnexarDocsExpRelHitoActualAction() {
    	super();
    }
    
    /**
     * Ejecuta la acción.
     * @param rctx Contexto de ejecución de la regla
     * @param attContext Atributos con información extra, utilizados dentro de 
     * la ejecución de la regla.
     * @return true si la ejecución termina correctamente, false en caso 
     * contrario.
     * @throws ActionException si ocurre algún error.
     */
    @SuppressWarnings("unchecked")
	public boolean execute(RuleContext rctx, AttributeContext attContext) throws ActionException {
    	
        if (logger.isInfoEnabled()) {
            logger.info("Acción [" + this.getClass().getName() + "] en ejecución");
        }

        String numexp = (String) rctx.getProperties().get("idobjeto");

        ConsultaTelematicaService service = new ConsultaTelematicaService();
        FicherosHito ficheros = null;
        ExpedienteVO expediente = null;

        try {
        	Expediente existeExp = null;
        	
	        String numExp = rctx.getIdObjeto();
	        ConsultaTelematicaService serviceExp = new ConsultaTelematicaService();
	        expediente = serviceExp.getExpediente(numExp);
	        
    		ServicioConsultaExpedientes consultaExp = LocalizadorServicios.getServicioConsultaExpedientes();

    		try{
    			existeExp = consultaExp.obtenerDetalle(expediente.getCnum(), getEntidad());
    		}
    		catch(Exception e){}
    		if( existeExp != null){
		        	
				ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
	
		        HitoExpediente hito = consulta.obtenerHitoEstado(rctx.getIdObjeto(), getEntidad());
		        if (hito == null) {
		        	throw new ActionException("No se ha encontrado el hito actual del expediente " + rctx.getIdObjeto());
		        }
		        
		        context = new ClientContext();
				context.setAPI(new InvesflowAPI(context));
				
				HitosExpediente hitos = consulta.obtenerHistoricoExpediente(numexp, getEntidad());
				FicherosHito ficherosExistentes  = new FicherosHito();
				
				for(Object oHito: hitos.getHitosExpediente())
					ficherosExistentes.getFicheros().addAll(consulta.obtenerFicherosHito(((HitoExpediente)oHito).getGuid(), getEntidad()).getFicheros());
		    	
				ArrayList<String> expedientes = ExpedientesRelacionadosUtil.getProcedimientosRelacionadosHijos(numexp, context.getAPI().getEntitiesAPI());
				String consultaDocumentos = getConsultaDocumentos(context, numexp, expedientes);
				
				IItemCollection docs = DocumentosUtil.queryDocumentos(context, consultaDocumentos);
							
				ficheros =  getFicheros(hito.getGuid(), docs, ficherosExistentes);
				 
		        if ((ficheros != null) && ficheros.count() > 0) {
		        	consulta.anexarFicherosHitoActual(ficheros, getEntidad());
		        }
	        }

	        logOk(ficheros);

        } catch (ActionException e) {
        	setInfo(e.getLocalizedMessage());
        	logError(e);
        	
        	try {
	    		service.deleteFicherosHito(ficheros);
        	} catch (Throwable t) {
        		logger.warn("No se han podido eliminar los ficheros en RDE", e);
        	}
        	
            throw e;
        } catch (Throwable e) {
        	setInfo("Error al anexar ficheros al hito actual: " + e.toString());
        	logError(e);
        	
        	try {
	    		service.deleteFicherosHito(ficheros);
        	} catch (Throwable t) {
        		logger.warn("No se han podido eliminar los ficheros en RDE", e);
        	}
        	
            throw new ActionException(e);
        }
        
        return true;
    }

	/**
     * Muestra un log del resultado.
     * @param ficheros Ficheros anexados.
     */
    private static void logOk(FicherosHito ficheros) {
    	
    	if (CONSULTA_TELEMATICA.isInfoEnabled()) {
	        CONSULTA_TELEMATICA.info("Anexión de ficheros al hito actual:\n" + (ficheros == null? 0 : ficheros.count()));
    	}
    }

    /**
     * Muestra un log de error.
     * @param e Excepción provocada por el error.
     */
    private static void logError(Throwable e) {
        CONSULTA_TELEMATICA.error("Error en la acción " + AnexarDocsExpRelHitoActualAction.class.getName(), e);
    }
    
    protected FicherosHito getFicheros(String guidHito, IItemCollection docs, FicherosHito ficherosExistentes) throws ISPACException, SigemException {
    	
    	// Ficheros asociados al hito
        FicherosHito ficheros = new FicherosHito();
        
        // Documentos
        if ((docs != null) && docs.next()) {
        	
			// Llamada al API de RDE
			ServicioRepositorioDocumentosTramitacion rde = LocalizadorServicios.getServicioRepositorioDocumentosTramitacion();

			IItem doc;
			FicheroHito fichero;
			String guid;
			String guidRDE;
			
			do {
				doc = docs.value();
				String titulo = doc.getInt("ID") + " - " + doc.getString("NOMBRE") + " - " +doc.getString("DESCRIPCION");
				if(titulo.length()>128) titulo = titulo.substring(0,127);
				
				boolean existe = false;
				
				for(int i = 0; i< ficherosExistentes.getFicheros().size() && !existe; i++ ){
		    		FicheroHito ficheroExistente = (FicheroHito)ficherosExistentes.getFicheros().get(i);
		    		
		    		if(ficheroExistente.getTitulo().equals(titulo)){
		    			existe = true;
		    		}
				}
				if(!existe){
	    			// GUID del fichero en el tramitador
					guid = doc.getString("INFOPAG_RDE");
					if (StringUtils.isNotBlank(guid)) {
						
						// Almacenar el fichero en RDE
						guidRDE = rde.storeDocumentInfoPag(null, EntidadHelper.getEntidad(), guid, doc.getString("EXTENSION_RDE"));
		
						// Información del fichero
						fichero = new FicheroHito();
						fichero.setGuid(guidRDE);
						fichero.setGuidHito(guidHito);
						fichero.setTitulo(titulo);
						
						// Añadir el fichero a la lista
						ficheros.add(fichero);
					}
				}
			} while (docs.next());
        }
        
        return ficheros;
    }
 	
	public String getConsultaDocumentos(IClientContext cct, String numexp, ArrayList<String> expedientes) throws ISPACException {
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		String nombre = "";
		StringBuffer nombreSinDni = new StringBuffer("");
		String[] identidad = null;
		String nif = "";
		
		IItem expedienteOriginal = ExpedientesUtil.getExpediente(cct, numexp);
		
		if (expedienteOriginal.getString("NREG") != null) {
		}
		if (expedienteOriginal.getString("IDENTIDADTITULAR") != null)
			nombre = expedienteOriginal.getString("IDENTIDADTITULAR");
		if (expedienteOriginal.getString("NIFCIFTITULAR") != null)
			nif = expedienteOriginal.getString("NIFCIFTITULAR");

		if (nif == null || nif.equals("")) {
			if (nombre != null && !nombre.equals("")) {
				identidad = nombre.split(" ");
				nombreSinDni.append(identidad[1]);

				for (int i = 2; i < identidad.length; i++) {
					nombreSinDni.append(" " + identidad[i]);
				}
			}
		} else {
			nombreSinDni = new StringBuffer(nombre);
		}

		String consultaDocumentos = "";
		String consultaDecre = "";
		String consultaSecre = "";
		String consultaBOP = "";
		String consultaTablon = "";
		String consultaElse = "";	

		//Comprobamos si es de secretaría, si lo es, buscamos la propuesta
		String expedientePropuesta = getExpedientePropuesta(cct, entitiesAPI, expedientes);
		
		// Recorremos todos los expedientes en función del expediente u
		// obtenemos todos los firmados y las entradas
		// o únicamente los certificados y las notificaciones
		for (int i = 0; i < expedientes.size(); i++) {
			IItem itemProc = ExpedientesUtil.getExpediente(cct, expedientes.get(i));
			if(itemProc != null){
				int idProcedimiento = itemProc.getInt("ID_PCD");
				// Comprobamos si el expediente es de los que no hay que sacar
				// documentos
	
				// No se obtienen documentos de Propuestas y Comisiones
				if (esProcTipo(entitiesAPI, idProcedimiento, "DECRETO")){
					String id = "";
					String consultaDecretos = "";
					String orden = "";
					if(!nombreSinDni.toString().equals("")) {
						// Es de tipo decreto recuperamos solo las notificaciones y los acuses
						consultaDecretos = " (UPPER(DESCRIPCION) LIKE UPPER('NOTIFICA%"
								+ nombreSinDni.toString() + "%') OR (UPPER(DESCRIPCION) LIKE UPPER('NOTIFICA%"
								+ nombreSinDni.toString() + "%') AND (UPPER(EXTENSION) ='PDF' OR UPPER(EXTENSION_RDE) ='PDF'))) ";
						
						consultaDecretos += " OR (UPPER(DESCRIPCION) LIKE UPPER('ACUSE%" + nombreSinDni.toString()
								+ "%') AND (UPPER(EXTENSION) ='PDF' OR UPPER(EXTENSION_RDE) ='PDF')) ";
						//consultaDecretos += " OR (ESTADOFIRMA IN ('02','03') AND UPPER(DESCRIPCION) LIKE UPPER('%DECRETO%'))";
						//consultaDecretos += " OR (TP_REG = 'ENTRADA' AND (UPPER(EXTENSION) = 'PDF' OR UPPER(EXTENSION_RDE) ='PDF'))";
						
						orden = "CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION DESC";
					}
					else{//si no tiene interesado principal toma el propio decreto
						consultaDecretos = "UPPER(NOMBRE) = 'DECRETO'";
						orden = "ID DESC LIMIT 1";
					}
						
					IItemCollection icDoc = DocumentosUtil.getDocumentos(cct, expedientes.get(i), consultaDecretos, orden);
					Iterator<?> docNotificaciones;
					if (icDoc.next()) {
						docNotificaciones = icDoc.iterator();
						while (docNotificaciones.hasNext()) {
							id += ((IItem) docNotificaciones.next())
									.getString("ID");
							if (docNotificaciones.hasNext())
								id += ",";
						}
					}					
					
					if (!id.equals("")) {
						if (!consultaDecre.equals(""))
							consultaDecre += " OR (ID IN (" + id + "))";
						else
							consultaDecre = " (ID IN (" + id + "))";
					}
				} else if (esProcTipo(entitiesAPI, idProcedimiento, "SECRETAR") && !expedientes.get(i).equals(expedientePropuesta)){
					String id = "";
					IItemCollection icDoc = DocumentosUtil.getDocumentos(cct, expedientes.get(i), "DESCRIPCION LIKE '%"	+ expedientePropuesta + "%'", "");
					Iterator<?> docNotificaciones;
					if (icDoc.next()) {
						String tipoPropuesta="";
						String numeroPropuesta = "";
						String esUrgencia = "";
						String esUrgencia2 = "";
					
						docNotificaciones = icDoc.iterator();
						if (docNotificaciones.hasNext()) {
							String descripcion = ((IItem) docNotificaciones.next())
									.getString("DESCRIPCION");
							String[] vectorDescripcion = descripcion.split(" . ");
							if (vectorDescripcion.length > 1) {
								tipoPropuesta = vectorDescripcion[0];
								numeroPropuesta = vectorDescripcion[1];
							}
							if (tipoPropuesta.toUpperCase().equals("PROPUESTA URGENCIA")){
								esUrgencia = ".- Urgencia Certificado";
								esUrgencia2= "Urgencia";
							}
							else esUrgencia = ".-Certificado";
							
							String consulta = "((UPPER(DESCRIPCION) LIKE UPPER('"+numeroPropuesta+esUrgencia+"%')";
							
							if(!nombre.equals("")) consulta += "OR UPPER(DESCRIPCION) LIKE UPPER('"+numeroPropuesta+"%"+esUrgencia2+"%"+nombre+"%')";
							
							consulta += ") AND ESTADOFIRMA IN ('02','03')) OR (TP_REG = 'ENTRADA' AND (UPPER(EXTENSION) = 'PDF' OR UPPER(EXTENSION_RDE) ='PDF'))";
							IItemCollection icDoc2 = DocumentosUtil.getDocumentos(cct, expedientes.get(i), consulta, "CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION DESC");
							Iterator<?> it2 = icDoc2.iterator();
							while (it2.hasNext()){
								id += ((IItem) it2.next()).getString("ID");
								if (it2.hasNext())
									id += ",";	
							}
						}
						
					}				
					if (!id.equals("")) {
						if (!consultaSecre.equals(""))
							consultaSecre += " OR (ID IN (" + id + "))";
						else
							consultaSecre += " (ID IN (" + id + "))";
					}
				}
				else if (esProcTipo(entitiesAPI, idProcedimiento, "BOP")){
					String idPropuestaAnuncio = "";
					String idAnuncioPublicado = "";
					
					IItemCollection solBopCollection = entitiesAPI.getEntities("BOP_SOLICITUD", expedientes.get(i));
					Iterator<?> solBopIterator = solBopCollection.iterator();
					if(solBopIterator.hasNext()){
						IItem solBop = (IItem) solBopIterator.next();
						Date fecha_publicacion = solBop.getDate("FECHA_PUBLICACION");
						String num_anuncio = solBop.getString("NUM_ANUNCIO_BOP");
						
						if(fecha_publicacion != null){
							IItemCollection publicacionBOPCollection = entitiesAPI.queryEntities("BOP_PUBLICACION", "WHERE FECHA = '"+ fecha_publicacion+"'");
							Iterator<?> publicacionBOPIterator = publicacionBOPCollection.iterator();
							if(publicacionBOPIterator.hasNext()){
								IItem publicacionBOP = (IItem)publicacionBOPIterator.next();
								String numexpPublicacion = publicacionBOP.getString("NUMEXP");
								
								IItemCollection documentosPublicacionBOPCollection = DocumentosUtil.getDocumentos(cct, numexpPublicacion,  "DESCRIPCION LIKE '"+num_anuncio+" -%'", "");
								Iterator<?> documentosPublicacionBOPIterator = documentosPublicacionBOPCollection.iterator();
								if(documentosPublicacionBOPIterator.hasNext()){
									IItem documentosPublicacion = (IItem)documentosPublicacionBOPIterator.next();
									idAnuncioPublicado = documentosPublicacion.getString("ID");
								}
							}
						}
					}	
					if(idAnuncioPublicado.equals("")){
						IItemCollection docPropAnuncioCollection = DocumentosUtil.getDocumentos(cct, expedientes.get(i), "", "CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION ASC");
						Iterator<?> docPropAnuncioIterator = docPropAnuncioCollection.iterator();
						if(docPropAnuncioIterator.hasNext()){
							IItem docPropAnuncio = (IItem) docPropAnuncioIterator.next();
							idPropuestaAnuncio = docPropAnuncio.getString("ID");
						}
					}
					
					if (!idAnuncioPublicado.equals("")) {
						if (!consultaBOP.equals(""))
							consultaBOP += " OR (ID IN (" + idAnuncioPublicado + "))";
						else
							consultaBOP += " (ID IN (" + idAnuncioPublicado + "))";
					}
					else if (!idPropuestaAnuncio.equals("")) {
						if (!consultaBOP.equals(""))
							consultaBOP += " OR (ID IN (" + idPropuestaAnuncio + "))";
						else
							consultaBOP += " (ID IN (" + idPropuestaAnuncio + "))";
					}
					
				}
				else if (esProcTipo(entitiesAPI, idProcedimiento, "ETABLÓN")){
					String idDocTablon = "";
					
					IItemCollection docTablonCollection = DocumentosUtil.getDocumentos(cct, expedientes.get(i), "UPPER(NOMBRE) LIKE 'ETABLON - DILIGENCIA%'", "");
					Iterator<?> docTabloniIterator = docTablonCollection.iterator();
					if(docTabloniIterator.hasNext()){
						IItem docTablon = (IItem) docTabloniIterator.next();
						idDocTablon = docTablon.getString("ID");
					}
					if (!idDocTablon.equals("")) {
						if (!consultaTablon.equals(""))
							consultaTablon += " OR (ID IN (" + idDocTablon + "))";
						else
							consultaTablon += " (ID IN (" + idDocTablon + "))";
					}
				}
				else {					
					String idFoliado = "";
					IItemCollection docFoliadoCollection = DocumentosUtil.getDocumentos(cct, expedientes.get(i), "UPPER(NOMBRE) LIKE 'EXPEDIENTE FOLIADO%' AND UPPER(DESCRIPCION) NOT LIKE 'DOCUMENTOS NO INCLUIDOS'", "");
					Iterator<?> docFoliadoiIterator = docFoliadoCollection.iterator();
					if(docFoliadoiIterator.hasNext()){
						IItem docFoliado = (IItem) docFoliadoiIterator.next();
						idFoliado = docFoliado.getString("ID");
					}
				
					if (!idFoliado.equals("")) {
						if (!consultaElse.equals(""))
							consultaElse += " OR (ID IN (" + idFoliado + "))";
						else
							consultaElse += " (ID IN (" + idFoliado + "))";
					}
				}
			}
		}

		if (!consultaDecre.equals(""))
			if(StringUtils.isEmpty(consultaDocumentos)) consultaDocumentos =  consultaDecre;
			else  consultaDocumentos += " OR " + consultaDecre;

		if (!consultaSecre.equals(""))
			if(StringUtils.isEmpty(consultaDocumentos)) consultaDocumentos =  consultaSecre;
			else  consultaDocumentos += " OR " + consultaSecre;

		if (!consultaBOP.equals(""))
			if(StringUtils.isEmpty(consultaDocumentos)) consultaDocumentos =  consultaBOP;
			else  consultaDocumentos += " OR " + consultaBOP;

		if (!consultaTablon.equals(""))
			if(StringUtils.isEmpty(consultaDocumentos)) consultaDocumentos =  consultaTablon;
			else  consultaDocumentos += " OR " + consultaTablon;

		if (!consultaElse.equals(""))
			if(StringUtils.isEmpty(consultaDocumentos)) consultaDocumentos =  consultaElse;
			else  consultaDocumentos += " OR " + consultaElse;
		
		if(!consultaDocumentos.equals("")){
			consultaDocumentos += " ORDER BY CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION ASC";		

			consultaDocumentos = " WHERE " + consultaDocumentos;
		} else
			consultaDocumentos = " WHERE 1=2 ";

		return consultaDocumentos;
	}
 	
	public String getExpedientePropuesta(IClientContext cct, IEntitiesAPI entitiesAPI, ArrayList<String> expedientes) throws ISPACException {
		String resultado = "";
		String expedientePropuesta = "";
		for (int i =0; i<expedientes.size();i++){
			expedientePropuesta = expedientes.get(i);
			IItem itemProc = ExpedientesUtil.getExpediente(cct, expedientes.get(i));
			if(itemProc != null){
				String nombreProcedimiento = itemProc.getString("NOMBREPROCEDIMIENTO");
				if(nombreProcedimiento.toUpperCase().indexOf("PROPUESTA")>-1){
					resultado = expedientePropuesta;
				}
			}			
		}
		return resultado;
	}

	public boolean esProcTipo(IEntitiesAPI entitiesAPI, int idProcedimiento, String tipo) {

		boolean resultado = false;
		try {
			IItem catalogo = entitiesAPI.getEntity(
					SpacEntities.SPAC_CT_PROCEDIMIENTOS, idProcedimiento);

			int id_padre = catalogo.getInt("ID_PADRE");
			if (id_padre != -1) {				
				resultado = esProcTipo(entitiesAPI, id_padre, tipo);
			} else {
				String nom_pcd = "";
				if (catalogo.getString("NOMBRE") != null){
					nom_pcd = catalogo.getString("NOMBRE");
					if (nom_pcd.toUpperCase().indexOf(tipo.toUpperCase()) >= 0) {
						resultado = true;					
					}
				}
			}
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		return resultado;
	}
}