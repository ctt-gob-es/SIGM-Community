package es.dipucr.sigem.api.rule.common.publicador;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.FicheroHito;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.HitosExpediente;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.core.services.repositorio.ServicioRepositorioDocumentosTramitacion;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class AnexaDocFinFirma implements IRule {
	
	private static final Logger logger = Logger.getLogger(AnexaDocFinFirma.class);
	
	public static String _MENSAJE_FIRMADO = "Documento Firmado: ";
	public static String _MENSAJE_FIRMADO_REPARO = "Documento Firmado con Reparo: ";
	public static String _MENSAJE_RECHAZADO = "Documento Rechazado: ";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			String numexp = itemDocumento.getString("NUMEXP");
			
			String strEstadoFirma = itemDocumento.getString("ESTADOFIRMA");
			
			if (strEstadoFirma.equals(SignStatesConstants.FIRMADO) || strEstadoFirma.equals(SignStatesConstants.FIRMADO_CON_REPAROS) || strEstadoFirma.equals(SignStatesConstants.RECHAZADO)){
				
				ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();

		        HitoExpediente hito = consulta.obtenerHitoEstado(numexp, EntidadHelper.getEntidad());
		        if (hito == null) {
		        	logger.error("No se ha encontrado el hito actual del expediente " + numexp);
		        }
		        else{	
			        FicherosHito ficheros = null;
	
					HitosExpediente hitos = consulta.obtenerHistoricoExpediente(numexp, EntidadHelper.getEntidad());
					FicherosHito ficherosExistentes  = new FicherosHito();
					
					for(Object oHito: hitos.getHitosExpediente())
						ficherosExistentes.getFicheros().addAll(consulta.obtenerFicherosHito(((HitoExpediente)oHito).getGuid(), EntidadHelper.getEntidad()).getFicheros());
			    	
					ficheros =  getFicheros(cct, hito.getGuid(), itemDocumento, ficherosExistentes);
					
			        if ((ficheros != null) && ficheros.count() > 0) {
			        	consulta.anexarFicherosHitoActual(ficheros, EntidadHelper.getEntidad());
			        }
		        }
			}
		} catch (Exception e){
			logger.error("Error al insertar el documento en el hito del publicador.", e);
			return false;
		}
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	protected FicherosHito getFicheros(IClientContext cct, String guidHito, IItem doc, FicherosHito ficherosExistentes) throws ISPACException, SigemException {
    	
    	// Ficheros asociados al hito
        FicherosHito ficheros = new FicherosHito();
        
        // Documentos
        if (doc != null) {
        	
			ServicioRepositorioDocumentosTramitacion rde = LocalizadorServicios.getServicioRepositorioDocumentosTramitacion();

			FicheroHito fichero;
			String guid;
			String guidRDE;
		
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
				guid = doc.getString("INFOPAG_RDE");
				if (StringUtils.isNotBlank(guid)) {
					
					// Almacenar el fichero en RDE
					guidRDE = rde.storeDocumentInfoPag(null, EntidadHelper.getEntidad(), guid, getDocExt(cct, guid));
	
					// Información del fichero
					fichero = new FicheroHito();
					fichero.setGuid(guidRDE);
					fichero.setGuidHito(guidHito);
					fichero.setTitulo(titulo);
					
					// Añadir el fichero a la lista
					ficheros.add(fichero);
				}
			}
        }
        
        return ficheros;
    }
	
	private String getDocExt(IClientContext cct, String guid) throws ISPACException {

		String ext = null;

		IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
		Object connectorSession = null;

		try {
			connectorSession = genDocAPI.createConnectorSession();
			String mimeType = genDocAPI.getMimeType(connectorSession, guid);
			if (StringUtils.isNotBlank(mimeType)) {
				ext = MimetypeMapping.getExtension(mimeType);
			}
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}		        	

		return ext;
	}
}
