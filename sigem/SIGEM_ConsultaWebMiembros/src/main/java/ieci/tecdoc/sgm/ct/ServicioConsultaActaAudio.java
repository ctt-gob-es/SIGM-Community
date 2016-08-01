package ieci.tecdoc.sgm.ct;

import ieci.tecdoc.sgm.core.services.ConstantesServicios;
import ieci.tecdoc.sgm.core.services.consulta.Acta;
import ieci.tecdoc.sgm.core.services.consulta.ConsultaExpedientesException;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.ct.database.datatypes.Actas;
import ieci.tecdoc.sgm.ct.exception.ConsultaExcepcion;

import org.apache.log4j.Logger;

public class ServicioConsultaActaAudio{
	
	private static final Logger logger = Logger.getLogger(ServicioConsultaActaAudio.class);
	
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
	  public ieci.tecdoc.sgm.core.services.consulta.Actas obtenerActas(String NIF, Entidad entidad)throws ConsultaExcepcion, ConsultaExpedientesException {
		  Actas actas = new Actas();
		  //FicherosHito ficheros = new FicherosHito();
		  try {		
			  actas = GestorConsulta.getActas (NIF, entidad.getIdentificador());
			  return getActasServicio(actas);
			} catch (ConsultaExcepcion e) {
				logger.error("Error en consulta de expedientes por NIF.", e);
				throw getConsultaExpedientesException(e);
			} catch (Throwable e){
				logger.error("Error inesperado en consulta de expedientes por NIF.", e);
				throw new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION, e);
			} 
	  }
	  
	  private ieci.tecdoc.sgm.core.services.consulta.Actas getActasServicio(Actas actas) {

			if(actas == null){
				return null;
			}
			ieci.tecdoc.sgm.core.services.consulta.Actas oProp = new ieci.tecdoc.sgm.core.services.consulta.Actas();
			for(int i = 0; i < actas.count(); i++){
				oProp.add(getActaServicio((ieci.tecdoc.sgm.ct.database.datatypes.Acta)(actas.get(i))));
			}
			return oProp;
		}
	  
	  private Acta getActaServicio(ieci.tecdoc.sgm.ct.database.datatypes.Acta acta) {
			if(acta == null){
				return null;
			}
			ieci.tecdoc.sgm.core.services.consulta.Acta oPropuesta = new ieci.tecdoc.sgm.core.services.consulta.Acta();
			oPropuesta.setNumexp(acta.getNumexp());
			oPropuesta.setExtracto(acta.getExtracto());
			

			ieci.tecdoc.sgm.core.services.consulta.FicheroPropuesta fPropuestafin= new ieci.tecdoc.sgm.core.services.consulta.FicheroPropuesta();
			ieci.tecdoc.sgm.ct.database.datatypes.FicheroPropuesta fPropuestaIni = (ieci.tecdoc.sgm.ct.database.datatypes.FicheroPropuesta)acta.getDocumentoActa();
			fPropuestafin.setFechaFirma(fPropuestaIni.getFechaFirma());
			fPropuestafin.setGuid(fPropuestaIni.getGuid());
			fPropuestafin.setTitulo(fPropuestaIni.getTitulo());
			fPropuestafin.setId(fPropuestaIni.getId());
			
			oPropuesta.setDocumentoActa(fPropuestafin);
			return oPropuesta;
	}
	  
	  private ConsultaExpedientesException getConsultaExpedientesException(ConsultaExcepcion poException){
			if(poException == null){
				return new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION);
			}
			StringBuffer cCodigo = new StringBuffer(ConstantesServicios.SERVICE_QUERY_EXPS_ERROR_PREFIX);
			cCodigo.append(String.valueOf(poException.getErrorCode()));
			return new ConsultaExpedientesException(Long.valueOf(cCodigo.toString()).longValue(), poException);
			
		}

}
