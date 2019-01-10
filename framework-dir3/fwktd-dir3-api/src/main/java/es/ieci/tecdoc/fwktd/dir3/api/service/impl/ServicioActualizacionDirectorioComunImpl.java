package es.ieci.tecdoc.fwktd.dir3.api.service.impl;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ieci.tecdoc.fwktd.dir3.api.helper.XmlDcoToObject;
import es.ieci.tecdoc.fwktd.dir3.api.manager.DatosBasicosOficinaManager;
import es.ieci.tecdoc.fwktd.dir3.api.manager.DatosBasicosUnidadOrganicaManager;
import es.ieci.tecdoc.fwktd.dir3.api.manager.EstadoActualizacionDCOManager;
import es.ieci.tecdoc.fwktd.dir3.api.manager.GenerateScriptSQLManager;
import es.ieci.tecdoc.fwktd.dir3.api.vo.EstadoActualizacionDcoVO;
import es.ieci.tecdoc.fwktd.dir3.api.vo.oficina.OficinasVO;
import es.ieci.tecdoc.fwktd.dir3.api.vo.unidad.OrganismosVO;
import es.ieci.tecdoc.fwktd.dir3.core.service.ServicioActualizacionDirectorioComun;
import es.ieci.tecdoc.fwktd.dir3.services.ServicioObtenerActualizacionesDCO;
import eu.medsea.util.StringUtil;

/**
 * Implementacion por defecto del servicio de actualización del DCO
 *
 */
public class ServicioActualizacionDirectorioComunImpl implements ServicioActualizacionDirectorioComun{

	/**
	 * Servicio para obtener los ficheros de actualización del DCO
	 */
	protected ServicioObtenerActualizacionesDCO servicioObtenerActualizacionesDCO;
	/**
	 * Manager para la gestión de oficinas
	 */
	protected DatosBasicosOficinaManager datosBasicosOficinaManager;
	/**
	 * Manager para la gestión de unidades orgánicas
	 */
	protected DatosBasicosUnidadOrganicaManager datosBasicosUnidadOrganicaManager;
	/**
	 * Manager para la gestión de unidades orgánicas
	 */
	protected EstadoActualizacionDCOManager estadoActualizacionDCOManager;

	protected GenerateScriptSQLManager generateScriptSQLOficinaManager;
	protected GenerateScriptSQLManager generateScriptSQLUnidadOrganicaManagerImpl;

	private static final Logger logger = LoggerFactory.getLogger(ServicioActualizacionDirectorioComunImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.ieci.tecdoc.fwktd.dir3.core.service.ServicioActualizacionDirectorioComun
	 * #actualizarDirectorioComun()
	 */
	public void actualizarDirectorioComun() {
		if(logger.isDebugEnabled()){
			logger.debug("Comienza la actualización del sistema");
		}

		//Obtenemos la fecha de la ultima actualizacion
		EstadoActualizacionDcoVO estadoActualizacion = estadoActualizacionDCOManager.getLastSuccessUpdate();
		if(logger.isDebugEnabled()){
			StringBuffer sb = new StringBuffer();
			sb.append("Los datos de la última actualización son: ")
			  .append(estadoActualizacion);
			logger.debug(sb.toString());
		}

		//Obtenemos los ficheros con los datos
		String fileInicializarOficinas = getServicioObtenerActualizacionesDCO().getFicheroActualizarOficinasDCO(estadoActualizacion.getFechaActualizacion());
		String fileInicializarUnidades = getServicioObtenerActualizacionesDCO().getFicheroActualizarUnidadesDCO(estadoActualizacion.getFechaActualizacion());

		//Transformamos los datos de los ficheros a VOs y actualizamos los datos
		if(StringUtils.isNotEmpty(fileInicializarOficinas)){
			OficinasVO oficinasDCO = XmlDcoToObject.getInstance().getOficinasFromXmlFile(fileInicializarOficinas);
			getDatosBasicosOficinaManager().updateDatosBasicosOficinas(oficinasDCO);

			File ficheroBorrar = new File(fileInicializarOficinas);
			if(null != ficheroBorrar && ficheroBorrar.exists()){
				ficheroBorrar.delete();
			}
		}
		if(StringUtils.isNotEmpty(fileInicializarUnidades)){
			OrganismosVO organismosDCO = XmlDcoToObject.getInstance().getOrganismosFromXmlFile(fileInicializarUnidades);
			getDatosBasicosUnidadOrganicaManager().updateDatosBasicosUnidadesOrganicas(organismosDCO);
		
			File ficheroBorrar = new File(fileInicializarUnidades);
			if(null != ficheroBorrar && ficheroBorrar.exists()){
				ficheroBorrar.delete();
			}
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("Actualizados los datos de oficinas y organismos");
		}

		//Actualizamos la fecha de ultima actualizacion
		estadoActualizacion.setFechaActualizacion(Calendar.getInstance().getTime());
		getEstadoActualizacionDCOManager().update(estadoActualizacion);

		if(logger.isDebugEnabled()){
			logger.debug("Finaliza la actualización del sistema");
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.ieci.tecdoc.fwktd.dir3.core.service.ServicioActualizacionDirectorioComun
	 * #generateScriptsActualizacionDirectorioComun()
	 */
	public void generateScriptsActualizacionDirectorioComun() {
		if(logger.isDebugEnabled()){
			logger.debug("Comienza la generación de los script de actualización del sistema");
		}

		//Obtenemos la fecha de la ultima actualizacion
		Date lastDateUpdate = estadoActualizacionDCOManager.getLastSuccessUpdate().getFechaActualizacion();

		if(logger.isDebugEnabled()){
			StringBuffer sb = new StringBuffer();
			sb.append("La fecha de la ultima actualización es: ")
			  .append(lastDateUpdate.toString());
			logger.debug(sb.toString());
		}

		//Obtenemos los ficheros con los datos
		String fileInicializarOficinas = getServicioObtenerActualizacionesDCO().getFicheroActualizarOficinasDCO(lastDateUpdate);
		String fileInicializarUnidades = getServicioObtenerActualizacionesDCO().getFicheroActualizarUnidadesDCO(lastDateUpdate);

		//Generamos los script
		getGenerateScriptSQLOficinaManager().generateScriptActualizacion(fileInicializarOficinas);
		getGenerateScriptSQLUnidadOrganicaManagerImpl().generateScriptActualizacion(fileInicializarUnidades);
		
		if(logger.isDebugEnabled()){
			logger.debug("Finaliza la generación de los script de actualización del sistema");
		}
	}

	public ServicioObtenerActualizacionesDCO getServicioObtenerActualizacionesDCO() {
		return servicioObtenerActualizacionesDCO;
	}

	public void setServicioObtenerActualizacionesDCO(
			ServicioObtenerActualizacionesDCO servicioObtenerActualizacionesDCO) {
		this.servicioObtenerActualizacionesDCO = servicioObtenerActualizacionesDCO;
	}

	public DatosBasicosOficinaManager getDatosBasicosOficinaManager() {
		return datosBasicosOficinaManager;
	}

	public void setDatosBasicosOficinaManager(
			DatosBasicosOficinaManager datosBasicosOficinaManager) {
		this.datosBasicosOficinaManager = datosBasicosOficinaManager;
	}

	public DatosBasicosUnidadOrganicaManager getDatosBasicosUnidadOrganicaManager() {
		return datosBasicosUnidadOrganicaManager;
	}

	public void setDatosBasicosUnidadOrganicaManager(
			DatosBasicosUnidadOrganicaManager datosBasicosUnidadOrganicaManager) {
		this.datosBasicosUnidadOrganicaManager = datosBasicosUnidadOrganicaManager;
	}

	public EstadoActualizacionDCOManager getEstadoActualizacionDCOManager() {
		return estadoActualizacionDCOManager;
	}

	public void setEstadoActualizacionDCOManager(
			EstadoActualizacionDCOManager estadoActualizacionDCOManager) {
		this.estadoActualizacionDCOManager = estadoActualizacionDCOManager;
	}

	public GenerateScriptSQLManager getGenerateScriptSQLOficinaManager() {
		return generateScriptSQLOficinaManager;
	}

	public void setGenerateScriptSQLOficinaManager(
			GenerateScriptSQLManager generateScriptSQLOficinaManager) {
		this.generateScriptSQLOficinaManager = generateScriptSQLOficinaManager;
	}

	public GenerateScriptSQLManager getGenerateScriptSQLUnidadOrganicaManagerImpl() {
		return generateScriptSQLUnidadOrganicaManagerImpl;
	}

	public void setGenerateScriptSQLUnidadOrganicaManagerImpl(
			GenerateScriptSQLManager generateScriptSQLUnidadOrganicaManagerImpl) {
		this.generateScriptSQLUnidadOrganicaManagerImpl = generateScriptSQLUnidadOrganicaManagerImpl;
	}

}
