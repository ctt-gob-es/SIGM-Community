package ieci.tecdoc.sgm.ct;

import ieci.tecdoc.sgm.core.services.ConstantesServicios;
import ieci.tecdoc.sgm.core.services.consulta.ConsultaExpedientesException;
import ieci.tecdoc.sgm.core.services.consulta.CriterioBusquedaExpedientes;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.Expedientes;
import ieci.tecdoc.sgm.core.services.consulta.Propuesta;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.ct.database.ExpedienteDatos;
import ieci.tecdoc.sgm.ct.database.datatypes.ExpedienteImpl;
import ieci.tecdoc.sgm.ct.database.datatypes.Propuestas;
import ieci.tecdoc.sgm.ct.exception.ConsultaExcepcion;

import java.util.Vector;

import org.apache.log4j.Logger;

public class ServicioConsultaExpedientes{

	private static final Logger logger = Logger.getLogger(ServicioConsultaExpedientesAdapter.class);
	
	/**
	 * Recupera de la base de datos los Ficheros adjuntos a un Hito.
	 * 
	 * @param guidHito
	 *            identificador del Hito.
	 * @return FicherosHito Objeto de tipo ArrayList con los Ficheros
	 * @throws ConsultaExcepcion
	 *             Si se produce algún error.
	 * @throws ConsultaExpedientesException 
	 */
	  public ieci.tecdoc.sgm.core.services.consulta.Propuestas obtenerPropuestas(String NIF, Entidad entidad, String numexp)throws ConsultaExcepcion, ConsultaExpedientesException {
		  Propuestas propuestas = new Propuestas();
		  //FicherosHito ficheros = new FicherosHito();
		  try {		
			  propuestas = GestorConsulta.consultarPropuesta (NIF, entidad.getIdentificador(), numexp);
			  return getPropuestasServicio(propuestas);
			} catch (ConsultaExcepcion e) {
				logger.error("Error en consulta de expedientes por NIF. "+e.getMessage(), e);
				throw getConsultaExpedientesException(e);
			} catch (Throwable e){
				logger.error("Error inesperado en consulta de expedientes por NIF. "+e.getMessage(), e);
				throw new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION, e);
			} 
	  }
	  
	  
	  public Expedientes busquedaExpedientes (CriterioBusquedaExpedientes oCriterio, Entidad entidad) throws ConsultaExpedientesException{
			try {		
				return getExpedientesServicio(GestorConsulta.busquedaExpedientes(null,
									oCriterio.getNIF(), 
									oCriterio.getFechaDesde(), 
									oCriterio.getFechaHasta(), 
									oCriterio.getOperadorConsulta(), 
									oCriterio.getEstado(),
									entidad.getIdentificador()));
			} catch (ConsultaExcepcion e) {
				logger.error("Error en búsqueda de expedientes. "+e.getMessage(), e);
				throw getConsultaExpedientesException(e);
			} catch (Throwable e){
				logger.error("Error inesperado en búsqueda de expedientes. "+e.getMessage(), e);
				throw new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION, e);
			}										
		}
		
	
	private ieci.tecdoc.sgm.core.services.consulta.Propuestas getPropuestasServicio(Propuestas propuestas) {

		if(propuestas == null){
			return null;
		}
		ieci.tecdoc.sgm.core.services.consulta.Propuestas oProp = new ieci.tecdoc.sgm.core.services.consulta.Propuestas();
		for(int i = 0; i < propuestas.count(); i++){
			oProp.add(getPropuestaServicio((ieci.tecdoc.sgm.ct.database.datatypes.Propuesta)(propuestas.get(i))));
		}
		return oProp;
	}

	private Propuesta getPropuestaServicio(ieci.tecdoc.sgm.ct.database.datatypes.Propuesta propuesta) {
			if(propuesta == null){
				return null;
			}
			ieci.tecdoc.sgm.core.services.consulta.Propuesta oPropuesta = new ieci.tecdoc.sgm.core.services.consulta.Propuesta();
			oPropuesta.setNumexp(propuesta.getNumexp());
			oPropuesta.setExtracto(propuesta.getExtracto());
			

			Vector docProFin = new Vector();
			Vector docPropuestaIni = propuesta.getDocumentosPropuestas();
			
			for(int i=0; i<docPropuestaIni.size();i++){
				ieci.tecdoc.sgm.core.services.consulta.FicheroPropuesta fPropuestafin= new ieci.tecdoc.sgm.core.services.consulta.FicheroPropuesta();
				ieci.tecdoc.sgm.ct.database.datatypes.FicheroPropuesta fPropuestaIni = (ieci.tecdoc.sgm.ct.database.datatypes.FicheroPropuesta)docPropuestaIni.get(i);
				fPropuestafin.setFechaFirma(fPropuestaIni.getFechaFirma());
				fPropuestafin.setGuid(fPropuestaIni.getGuid());
				fPropuestafin.setTitulo(fPropuestaIni.getTitulo());
				fPropuestafin.setId(fPropuestaIni.getId());
				docProFin.add(fPropuestafin);
			}
			
			oPropuesta.setDocumentosPropuestas(docProFin);
			return oPropuesta;
	}

	public Expedientes consultarExpedientesMiembros(
			String NIF, Entidad entidad, String nombreCertificado)
			throws ConsultaExpedientesException {
		try {		
			ieci.tecdoc.sgm.ct.database.datatypes.Expedientes oExps = GestorConsulta.consultarExpedientesMiembros(null, NIF, entidad.getIdentificador(), nombreCertificado);
			return getExpedientesServicio(oExps);
		} catch (ConsultaExcepcion e) {
			logger.error("Error en consulta de expedientes por NIF. "+e.getMessage(), e);
			throw getConsultaExpedientesException(e);
		} catch (Throwable e){
			logger.error("Error inesperado en consulta de expedientes por NIF. "+e.getMessage(), e);
			throw new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION, e);
		}
	}
	
	private ConsultaExpedientesException getConsultaExpedientesException(ConsultaExcepcion poException){
		if(poException == null){
			return new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION);
		}
		StringBuffer cCodigo = new StringBuffer(ConstantesServicios.SERVICE_QUERY_EXPS_ERROR_PREFIX);
		cCodigo.append(String.valueOf(poException.getErrorCode()));
		return new ConsultaExpedientesException(Long.valueOf(cCodigo.toString()).longValue(), poException);
		
	}
	
	private Expedientes getExpedientesServicio(ieci.tecdoc.sgm.ct.database.datatypes.Expedientes poExps){
		if(poExps == null){
			return null;
		}
		Expedientes oExps = new Expedientes();
		for(int i = 0; i < poExps.count(); i++){
			ExpedienteImpl exp = (ExpedienteImpl) poExps.get(i);
			oExps.add(getExpedienteServicio(exp));
		}
		return oExps;
	}
	
	private Expediente getExpedienteServicio(ExpedienteImpl poExp){
		if(poExp == null){
			return null;
		}
		Expediente oExpediente = new Expediente();
		oExpediente.setAportacion(poExp.getAportacion());
		oExpediente.setCodigoPresentacion(poExp.getCodigoPresentacion());
		oExpediente.setEstado(poExp.getEstado());
		oExpediente.setFechaInicio(poExp.getFechaInicio());
		oExpediente.setFechaRegistro(poExp.getFechaRegistro());
		oExpediente.setInformacionAuxiliar(poExp.getInformacionAuxiliar());
		oExpediente.setNotificacion(poExp.getNotificacion());
		oExpediente.setNumero(poExp.getNumero());
		oExpediente.setNumeroRegistro(poExp.getNumeroRegistro());
		oExpediente.setProcedimiento(poExp.getProcedimiento());
		oExpediente.setFechaConvocatoria(poExp.getFechaConvocatoria());
		oExpediente.setHoraConvocatoria(poExp.getHoraConvocatoria());
		return oExpediente;
	}
	
	public String obtenerURLAportacionExpedientes()
			throws ConsultaExpedientesException {
		try {		
			return GestorConsulta.obtenerURLAportacionExpedientes();
		} catch (ConsultaExcepcion e) {
			logger.error("Error obteniendo URL aportación de expedientes. "+e.getMessage(), e);
			throw getConsultaExpedientesException(e);
		} catch (Throwable e){
			logger.error("Error inesperado obteniendo URL aportación de expedientes. "+e.getMessage(), e);
			throw new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	public String obtenerURLNotificacionExpedientes()
			throws ConsultaExpedientesException {
		try {		
			return GestorConsulta.obtenerURLNotificacionExpedientes();
		} catch (ConsultaExcepcion e) {
			logger.error("Error obteniendo URL notificación expedientes. "+e.getMessage(), e);
			throw getConsultaExpedientesException(e);
		} catch (Throwable e){
			logger.error("Error inesperado obteniendo URL notificación de expedientes. "+e.getMessage(), e);
			throw new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION, e);
		}
	}

}
