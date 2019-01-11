package es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.keys.IDocKeys;

import es.ieci.tecdoc.fwktd.sir.core.types.IndicadorPruebaEnum;
import es.ieci.tecdoc.fwktd.sir.core.vo.AnexoFirmaVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.AnexoVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.AsientoRegistralFormVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.AsientoRegistralVO;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.business.manager.RegistroManager;
import es.ieci.tecdoc.isicres.api.business.vo.CampoGenericoRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.DocumentoFisicoVO;
import es.ieci.tecdoc.isicres.api.business.vo.IdentificadorRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.business.vo.enums.TipoLibroEnum;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.DocumentoElectronicoAnexoManager;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.ConfiguracionCreateDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoContenidoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoDatosFirmaVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.IdentificadorDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoValidezDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.dao.BandejaSalidaIntercambioRegistralDAO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.exception.IntercambioRegistralException;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.exception.IntercambioRegistralExceptionCodes;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.ConfiguracionIntercambioRegistralManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralGeneradorObjetosManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralSIRManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralSalidaManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaSalidaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.EstadoIntercambioRegistralSalidaEnumVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.EstadoIntercambioRegistralSalidaVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralSalidaVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralVO;

public class IntercambioRegistralSalidaManagerImpl implements
		IntercambioRegistralSalidaManager {

	private static Logger logger = Logger.getLogger(IntercambioRegistralSalidaManagerImpl.class);

	/**
	 * dao para obtencion de la configuracion de intercambio registral
	 */
	protected ConfiguracionIntercambioRegistralManager configuracionIntercambioRegistralManager;

	/**
	 * Manager para el SIR
	 */
	protected IntercambioRegistralSIRManager intercambioRegistralSIRManager;

	protected RegistroManager registroManager;



	/**
	 * dao para obtencion de los elementos de la bandeja de salida de intercambio
	 */
	protected BandejaSalidaIntercambioRegistralDAO bandejaSalidaIntercambioRegistralDAO;

	/**
	 * Manager para construir objetos necesitados por el SIR
	 */
	protected IntercambioRegistralGeneradorObjetosManager intercambioRegistralGeneradorObjetosManager;

	/**
	 * Incrementer para obtener el id del intercambio registral de salida
	 */
	protected DataFieldMaxValueIncrementer intercambioRegistralSalidaIncrementer;


	/**
	 * Incrementer para obtener el id de las actualizaciones de estado del intercambio registral de salida
	 */
	protected DataFieldMaxValueIncrementer intercambioRegistralSalidaEstadoIncrementer;
	 protected DocumentoElectronicoAnexoManager documentoElectronicoAnexoManager;

	public boolean isIntercambioRegistral(String idUnidadAdministrativa) {

		boolean result=false;
		if(!StringUtils.isEmpty(idUnidadAdministrativa))
		{
			UnidadTramitacionIntercambioRegistralVO unidadDestino = getConfiguracionIntercambioRegistralManager().getUnidadTramitacionIntercambioRegistralVOByIdScrOrgs(idUnidadAdministrativa);

			if(unidadDestino!=null && StringUtils.isNotEmpty(unidadDestino.getCodeEntity()))
			{
				result=true;
			}
		}
		if(logger.isDebugEnabled())
		{
			String resultString = result?"SI":"NO";
			logger.debug("La unidad admnistrativa "+idUnidadAdministrativa+" "+resultString+" tiene configuracion de intercambio registral");
		}

		return result;
	}

	/**
	 * @deprecated
	 */
	public void toIntercambioRegistral(String idLibro, String idRegistro, String idOfic,
			String tipoOrigen, String idUnidadTramitacionDestino, String user) {

		if(isIntercambioRegistral(idUnidadTramitacionDestino))
		{

			IntercambioRegistralSalidaVO intercambioRegistral = new IntercambioRegistralSalidaVO();

			intercambioRegistral.setId(getIntercambioRegistralSalidaIncrementer().nextLongValue());
			intercambioRegistral.setIdLibro(Long.valueOf(idLibro));
			intercambioRegistral.setIdOfic(Integer.valueOf(idOfic));
			intercambioRegistral.setIdRegistro(Long.valueOf(idRegistro));
			intercambioRegistral.setEstado(EstadoIntercambioRegistralSalidaEnumVO.PENDIENTE);
			intercambioRegistral.setFechaEstado(Calendar.getInstance().getTime());
			intercambioRegistral.setTipoOrigen(Integer.valueOf(tipoOrigen));
			intercambioRegistral.setUsername(user);
			getBandejaSalidaIntercambioRegistralDAO().save(intercambioRegistral);

			//AUDITORIA DE CAMBIOS DE ESTADO
			saveHistorialCambiosEstado(intercambioRegistral);

			if(logger.isDebugEnabled())
			{
				logger.debug("Guardado el intercambio registral de salida con id = "+intercambioRegistral.getId());
			}
		}
		else
		{
			logger.info("No se puede guardar el intercambio registral de salida porque la unidad de tramitacion destino no tiene configuracion de intercambio registral mapeada.");
			throw new IntercambioRegistralException("No se puede guardar el intercambio registral de salida porque la unidad de tramitacion destino no tiene configuracion de intercambio registral mapeada.", IntercambioRegistralExceptionCodes.ERROR_CODE_UNIDAD_TRAMITACION_NO_MAPEADA);
		}
	}

	public void toIntercambioRegistralManual(List<String> idRegistros,
			String idLibro, String idOfic, String tipoOrigen, String user) {
		for (String idRegistro : idRegistros) {
			if(!isInBandejasalidaIntercambioRegistral(idRegistro, idLibro))
			{
				IntercambioRegistralSalidaVO intercambioRegistral = new IntercambioRegistralSalidaVO();

				intercambioRegistral.setId(getIntercambioRegistralSalidaIncrementer().nextLongValue());
				intercambioRegistral.setIdLibro(Long.valueOf(idLibro));
				intercambioRegistral.setIdOfic(Integer.valueOf(idOfic));
				intercambioRegistral.setIdRegistro(Long.valueOf(idRegistro));
				intercambioRegistral.setEstado(EstadoIntercambioRegistralSalidaEnumVO.PENDIENTE);
				intercambioRegistral.setFechaEstado(Calendar.getInstance().getTime());
				intercambioRegistral.setTipoOrigen(Integer.valueOf(tipoOrigen));
				intercambioRegistral.setUsername(user);
				getBandejaSalidaIntercambioRegistralDAO().save(intercambioRegistral);

				if(logger.isDebugEnabled())
				{
					logger.debug("Guardado el intercambio registral de salida con id = "+intercambioRegistral.getId());
				}
			}
			else
			{
				logger.info("El registro del libro = "+idLibro+" con id = "+idRegistro+" YA ESTÁ en la bandeja de salida de intercambio registral.");
			}
		}

	}

	private void saveBandejaSalida(IntercambioRegistralSalidaVO intercambioRegistralSalida,IdentificadorRegistroVO identificadorRegistro,TipoLibroEnum tipoLibro){

		getBandejaSalidaIntercambioRegistralDAO().save(intercambioRegistralSalida);

		saveHistorialCambiosEstado(intercambioRegistralSalida);

		UsuarioVO usuario = IsicresManagerProvider.getInstance().getContextoAplicacionManager().getUsuarioActual();

		//Actualizamos los datos de registro referentes a registo
		List <CampoGenericoRegistroVO> camposGenericos = new ArrayList<CampoGenericoRegistroVO>();
		CampoGenericoRegistroVO campoInvolucradoIntercambioRegistral = new CampoGenericoRegistroVO(Integer.toString(AxSf.FLD503_FIELD_ID),"1");
		camposGenericos.add(campoInvolucradoIntercambioRegistral);

		if (TipoLibroEnum.ENTRADA  == tipoLibro){
			registroManager.updateRegistroEntradaIR(usuario, identificadorRegistro, camposGenericos );
		}else{
			registroManager.updateRegistroSalidaIR(usuario, identificadorRegistro, camposGenericos );
		}
	}



	public void anularIntercambioRegistralSalidaById(String id) {

		BandejaSalidaIntercambioRegistralDAO dao = getBandejaSalidaIntercambioRegistralDAO();
		IntercambioRegistralSalidaVO intecambioRegistralSalida = dao.get(Long.parseLong(id));

		EstadoIntercambioRegistralSalidaVO estadoAnulado = new EstadoIntercambioRegistralSalidaVO();
		estadoAnulado.setEstado(EstadoIntercambioRegistralSalidaEnumVO.ANULADO);
		estadoAnulado.setFechaEstado(new Date());
		estadoAnulado.setIdExReg(Long.parseLong(id));
		//TODO obtener este usuario
		String userName = "usuario anulando";
		estadoAnulado.setUserName(userName );
		updateEstado(intecambioRegistralSalida, estadoAnulado  );
		if(logger.isDebugEnabled())
		{
			logger.debug("Anulado el intercambio registral de salida con id = "+id);
		}
	}




	public String enviarIntercambioRegistralSalida(String idLibro,
			String idRegistro, String idOfic, String username,
			String tipoOrigen,
			UnidadTramitacionIntercambioRegistralVO unidadDestino) {

		String result = null;

		IdentificadorRegistroVO identificadorRegistro = new IdentificadorRegistroVO(
				idRegistro, idLibro);
		TipoLibroEnum tipoLibro = TipoLibroEnum.getEnum(Integer
				.parseInt(tipoOrigen));

		/*List<IntercambioRegistralSalidaVO> listaIntercambiosSalida = getBandejaSalidaIntercambioRegistralDAO()
				.getIntercambiosRegistralesSalida(Integer.valueOf(idRegistro),
						Integer.valueOf(idLibro), Integer.valueOf(idOfic));

		ListIterator<IntercambioRegistralSalidaVO> itr = listaIntercambiosSalida
				.listIterator();
		IntercambioRegistralSalidaVO temp = null;*/

		IntercambioRegistralSalidaVO intercambioSalida = new IntercambioRegistralSalidaVO();
		intercambioSalida.setIdLibro(Long.parseLong(idLibro));
		intercambioSalida.setIdRegistro(Long.parseLong(idRegistro));
		intercambioSalida.setIdOfic(Integer.parseInt(idOfic));
		intercambioSalida.setTipoOrigen(Integer.parseInt(tipoOrigen));
		intercambioSalida.setUsername(username);




		// enviamos al sir y actualizamos valores el vo intercambioSalida
		result = enviarIntercambioRegistralSalida(intercambioSalida,
				unidadDestino);

		// guardamos en la bandeja de salida
		saveBandejaSalida(intercambioSalida, identificadorRegistro, tipoLibro);

		return result;
	}

	public String enviarIntercambioRegistralSalida(String idLibro,
		String idRegistro, String idOfic, String username,
		String usercontact, String tipoOrigen,
		UnidadTramitacionIntercambioRegistralVO unidadDestino) {

	String result = null;

	IdentificadorRegistroVO identificadorRegistro = new IdentificadorRegistroVO(
			idRegistro, idLibro);
	TipoLibroEnum tipoLibro = TipoLibroEnum.getEnum(Integer
			.parseInt(tipoOrigen));

	/*List<IntercambioRegistralSalidaVO> listaIntercambiosSalida = getBandejaSalidaIntercambioRegistralDAO()
			.getIntercambiosRegistralesSalida(Integer.valueOf(idRegistro),
					Integer.valueOf(idLibro), Integer.valueOf(idOfic));

	ListIterator<IntercambioRegistralSalidaVO> itr = listaIntercambiosSalida
			.listIterator();
	IntercambioRegistralSalidaVO temp = null;*/

	IntercambioRegistralSalidaVO intercambioSalida = new IntercambioRegistralSalidaVO();
	intercambioSalida.setIdLibro(Long.parseLong(idLibro));
	intercambioSalida.setIdRegistro(Long.parseLong(idRegistro));
	intercambioSalida.setIdOfic(Integer.parseInt(idOfic));
	intercambioSalida.setTipoOrigen(Integer.parseInt(tipoOrigen));
	intercambioSalida.setUsername(username);
	intercambioSalida.setUsercontact(usercontact);



	// enviamos al sir y actualizamos valores el vo intercambioSalida
	result = enviarIntercambioRegistralSalida(intercambioSalida,
			unidadDestino);

	// guardamos en la bandeja de salida
	saveBandejaSalida(intercambioSalida, identificadorRegistro, tipoLibro);

	return result;
}
	
	/**
	 * {@inheritDoc}
	 */
	public void undoAnularIntercambioRegistral(String id) {
		BandejaSalidaIntercambioRegistralDAO dao = getBandejaSalidaIntercambioRegistralDAO();
		IntercambioRegistralSalidaVO intecambioRegistralSalida = dao.get(Long.parseLong(id));

		EstadoIntercambioRegistralSalidaVO estadoPendiente = new EstadoIntercambioRegistralSalidaVO();
		estadoPendiente.setEstado(EstadoIntercambioRegistralSalidaEnumVO.PENDIENTE);
		estadoPendiente.setFechaEstado(new Date());
		estadoPendiente.setIdExReg(Long.parseLong(id));
		//TODO obtener este usuario
		String userName = "usuario desanulando";
		estadoPendiente.setUserName(userName );

		updateEstado(intecambioRegistralSalida, estadoPendiente);
		if(logger.isDebugEnabled())
		{
			logger.debug("Desanulado el intercambio registral de salida con id = "+id);
		}

	}

	public void updateEstado(
			IntercambioRegistralSalidaVO intecambioRegistralSalida,
			EstadoIntercambioRegistralSalidaVO estado) {
		getBandejaSalidaIntercambioRegistralDAO().updateEstado(intecambioRegistralSalida, estado);

		//AUDITORIA DE CAMBIOS DE ESTADO
		saveHistorialCambiosEstado(intecambioRegistralSalida);
	}

	public List<IntercambioRegistralSalidaVO> getIntercambiosRegistralesSalida(
			Integer estado) {
		return getBandejaSalidaIntercambioRegistralDAO().getIntercambiosRegistralesSalida(estado);
	}

	public BandejaSalidaItemVO completarBandejaSalidaItem(
			BandejaSalidaItemVO bandejaSalidaItemVO) {
		bandejaSalidaItemVO = getBandejaSalidaIntercambioRegistralDAO().completarBandejaSalidaItem(bandejaSalidaItemVO);
		return bandejaSalidaItemVO;
	}

	public List<BandejaSalidaItemVO> getBandejaSalidaIntercambioRegistral(
			Integer estado, Integer idOficina) {
		return getBandejaSalidaIntercambioRegistralDAO().getBandejaSalidaByEstadoYOficina(estado,idOficina);
	}

	public List<BandejaSalidaItemVO> getBandejaSalidaIntercambioRegistral(
			Integer estado, Integer idOficina, Integer idLibro) {
		return getBandejaSalidaIntercambioRegistralDAO().getBandejaSalidaByEstadoOficinaYLibro(estado,idOficina, idLibro);
	}
	
	public List<BandejaSalidaItemVO> getBandejaSalidaIntercambioRegistralEnviadosConError(Integer idOficina, Integer idLibro) {
		return getBandejaSalidaIntercambioRegistralDAO().getBandejaSalidaByEstadoOficinaYLibroEnviadosConError(idOficina, idLibro);
	}

	public IntercambioRegistralSalidaVO getIntercambioRegistralSalidaById(
			String id) {
		IntercambioRegistralSalidaVO intercambio = null;
		try{
			intercambio= getBandejaSalidaIntercambioRegistralDAO().get(Long.parseLong(id));
		}
		catch (NumberFormatException e) {
			logger.error("Error al parsear el ID de intercambio registral", e);
		}
		return intercambio;
	}


	public void deleteIntercambioRegistralSalida(Integer idLibro,
			Integer idRegistro, Integer idOficina) {
		//TODO ¿Comprobar que está pendiente?
		//¿Si está enviado...excepcion? retornamos true/false?
		getBandejaSalidaIntercambioRegistralDAO().deleteByIdArchIdFdr(idLibro, idRegistro,idOficina);

		if (logger.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append(
					"Eliminado el intercambio registral de salida del registro ")
					.append(idRegistro).append(" del libro ").append(idLibro);
			logger.debug(sb.toString());
		}

	}


	public void reenviarIntercambioRegistralSalidaById(
			String idIntercambioRegistralSalida, String user,String contacto,String descripcionReenvio,
			UnidadTramitacionIntercambioRegistralVO nuevoDestino, Integer idOficina, String codOficina) {
		// TODO: Almacena el asiento registral en la tabla scr_exregin
		// obtener datos del intercambio registral

		// vamos a a realizar actualizacion del estado del intercambio en local,
		// pasa a estado aceptado EstadoIntercambioRegistralEntradaEnumVO
		List<IntercambioRegistralSalidaVO> lista = getBandejaSalidaIntercambioRegistralDAO()
			.getIntercambiosRegistralesSalida(EstadoIntercambioRegistralSalidaEnumVO.DEVUELTO_VALUE);
		
		IntercambioRegistralSalidaVO intercambioRegistralSalidaVO = null;
		for (IntercambioRegistralSalidaVO isvo: lista){
		    if (String.valueOf(isvo.getIdIntercambioInterno()).equals(idIntercambioRegistralSalida)){
			intercambioRegistralSalidaVO = isvo;
		    }
		}
		intercambioRegistralSalidaVO.setEstado(EstadoIntercambioRegistralSalidaEnumVO.REENVIADO);
		intercambioRegistralSalidaVO.setComentarios(descripcionReenvio);
		intercambioRegistralSalidaVO.setCodeEntity(nuevoDestino.getCodeEntity());
		intercambioRegistralSalidaVO.setNameEntity(nuevoDestino.getNameEntity());
		intercambioRegistralSalidaVO.setCodeTramunit(nuevoDestino.getCodeTramunit());
		intercambioRegistralSalidaVO.setNameTramunit(nuevoDestino.getNameTramunit());
		intercambioRegistralSalidaVO.setFechaEstado(Calendar.getInstance()
        			.getTime());


		// Guardamos en la tabal scr_exreg y actualizamos estado en el SIR
		getBandejaSalidaIntercambioRegistralDAO().updateIntercambioRegistralSalidaVO(intercambioRegistralSalidaVO);

		getIntercambioRegistralSIRManager().reenviarAsientoRegistral(
				idIntercambioRegistralSalida, user,contacto,descripcionReenvio,nuevoDestino);

	}

	public List<IntercambioRegistralSalidaVO> getHistorialIntercambioRegistralSalida(String idLibro,
			String idRegistro, String idOficina) {

		List<IntercambioRegistralSalidaVO> intercambiosRegistralSalidaVO = getBandejaSalidaIntercambioRegistralDAO()
				.getIntercambiosRegistralesSalida(Integer.parseInt(idRegistro), Integer.parseInt(idLibro), Integer.parseInt(idOficina));

		for(int i=0; i < intercambiosRegistralSalidaVO.size(); i++){
			//obtenemos la informacion de los cambio de estado para el intercambio registral
			intercambiosRegistralSalidaVO.get(i).setEstadosIntercambioRegistralSalida(getBandejaSalidaIntercambioRegistralDAO()
					.getDetalleEstadosIntercambioRegistralSalida(
							intercambiosRegistralSalidaVO.get(i).getId()));
		}

//		for(Iterator<IntercambioRegistralSalidaVO> it=intercambiosRegistralSalidaVO.iterator();it.hasNext();){
//			IntercambioRegistralSalidaVO intercambioReg = it.next();
//
//
//			List<EstadoIntercambioRegistralSalidaVO> cambiosEstadoIntercambioRegistral = getBandejaSalidaIntercambioRegistralDAO()
//					.getDetalleEstadosIntercambioRegistralSalida(
//							intercambioReg.getId());
//
//			intercambioReg.setEstadosIntercambioRegistralSalida(cambiosEstadoIntercambioRegistral);
//		}

		return intercambiosRegistralSalidaVO;

	}

	/**
	 * Metodo que inserta el detalle de un cambio de estado
	 * @param intercambioRegistral
	 */
	private void saveHistorialCambiosEstado(
			IntercambioRegistralSalidaVO intercambioRegistral) {
		//creamos un detalle con la información del estado del intercambio registral
		EstadoIntercambioRegistralSalidaVO estado = new EstadoIntercambioRegistralSalidaVO();
		estado.setEstado(intercambioRegistral.getEstado());
		estado.setFechaEstado(intercambioRegistral.getFechaEstado());
		estado.setId(getIntercambioRegistralSalidaEstadoIncrementer().nextLongValue());
		estado.setIdExReg(intercambioRegistral.getId());
		estado.setUserName(intercambioRegistral.getUsername());

		//almacenamos los datos
		getBandejaSalidaIntercambioRegistralDAO().saveDetalleEstado(estado);

	}

	/**
	 * Método privado para enviar el registro al SIR y actualizar su estado, una
	 * vez que ya tenemos compuestos los objetos necesarios.
	 *
	 * @param intercambioRegistralSalida
	 * @param asientoParaIntercambio
	 * @return String. El identificador retornado por el SIR para el intercambio
	 */
	private String enviarIntercambioRegistralSalida(
			IntercambioRegistralSalidaVO intercambioRegistralSalida,
			UnidadTramitacionIntercambioRegistralVO unidadDestino) {

		// Convertimos y obtenemos datos para los objetos que necesita el SIR
		AsientoRegistralFormVO asientoRegistralIntercambio = getIntercambioRegistralGeneradorObjetosManager()
				.getAsientoRegistralIntercambioRegistralVO(
						intercambioRegistralSalida, unidadDestino);

		// Externalizar propiedad que indique si es una prueba
		if (true) {
			asientoRegistralIntercambio
					.setIndicadorPrueba(IndicadorPruebaEnum.NORMAL);
		}

		// enviamos el intercambio registral al modulo intermedio
		AsientoRegistralVO asiento = getIntercambioRegistralSIRManager()
				.enviarAsientoRegistral(asientoRegistralIntercambio);
		if (StringUtils.isNotEmpty(asiento.getCodigoError())) {
			logger
					.error("Ha ocurrido un error en el envio del asiento de intercambio registral con id = "
							+ intercambioRegistralSalida.getId());
			logger.error("Codigo de error = " + asiento.getCodigoError());
			logger.error("Descripcion de error = "
					+ asiento.getDescripcionError());
			throw new IntercambioRegistralException(
					"Ha ocurrido un error en el envio del asiento de intercambio registral",
					IntercambioRegistralExceptionCodes.ERROR_NOT_SEND_INTERCAMBIO_REGISTRAL);
		}

		intercambioRegistralSalida
				.setId(getIntercambioRegistralSalidaIncrementer()
						.nextLongValue());
		intercambioRegistralSalida.setIdIntercambioRegistral(asiento
				.getIdentificadorIntercambio());
		intercambioRegistralSalida.setIdIntercambioInterno(asiento.getId());
		intercambioRegistralSalida.setCodeEntity(asiento
				.getCodigoEntidadRegistralDestino());
		intercambioRegistralSalida.setNameEntity(asiento
				.getDescripcionEntidadRegistralDestino());
		intercambioRegistralSalida.setCodeTramunit(asiento
				.getCodigoUnidadTramitacionDestino());
		intercambioRegistralSalida.setNameTramunit(asiento
				.getDescripcionUnidadTramitacionDestino());
		intercambioRegistralSalida
				.setEstado(EstadoIntercambioRegistralSalidaEnumVO.ENVIADO);
		intercambioRegistralSalida.setFechaIntercambio(Calendar.getInstance()
				.getTime());
		intercambioRegistralSalida.setFechaEstado(Calendar.getInstance()
				.getTime());

		if (logger.isDebugEnabled()) {
			logger
					.debug("Se ha enviado correctamente el asiento de intercambio registral con id = "
							+ intercambioRegistralSalida.getId()
							+ " e identificador de intercambio = "
							+ asiento.getIdentificadorIntercambio());
		}

		return intercambioRegistralSalida.getIdIntercambioRegistral();
	}

	public BandejaSalidaIntercambioRegistralDAO getBandejaSalidaIntercambioRegistralDAO() {
		return bandejaSalidaIntercambioRegistralDAO;
	}

	public void setBandejaSalidaIntercambioRegistralDAO(
			BandejaSalidaIntercambioRegistralDAO bandejaSalidaIntercambioRegistralDAO) {
		this.bandejaSalidaIntercambioRegistralDAO = bandejaSalidaIntercambioRegistralDAO;
	}



	public ConfiguracionIntercambioRegistralManager getConfiguracionIntercambioRegistralManager() {
		return configuracionIntercambioRegistralManager;
	}


	public void setConfiguracionIntercambioRegistralManager(
			ConfiguracionIntercambioRegistralManager configuracionIntercambioRegistralManager) {
		this.configuracionIntercambioRegistralManager = configuracionIntercambioRegistralManager;
	}


	public IntercambioRegistralGeneradorObjetosManager getIntercambioRegistralGeneradorObjetosManager() {
		return intercambioRegistralGeneradorObjetosManager;
	}


	public void setIntercambioRegistralGeneradorObjetosManager(
			IntercambioRegistralGeneradorObjetosManager intercambioRegistralGeneradorObjetosManager) {
		this.intercambioRegistralGeneradorObjetosManager = intercambioRegistralGeneradorObjetosManager;
	}


	public DataFieldMaxValueIncrementer getIntercambioRegistralSalidaIncrementer() {
		return intercambioRegistralSalidaIncrementer;
	}

	public DataFieldMaxValueIncrementer getIntercambioRegistralSalidaEstadoIncrementer() {
		return intercambioRegistralSalidaEstadoIncrementer;
	}

	public void setIntercambioRegistralSalidaEstadoIncrementer(
			DataFieldMaxValueIncrementer intercambioRegistralSalidaEstadoIncrementer) {
		this.intercambioRegistralSalidaEstadoIncrementer = intercambioRegistralSalidaEstadoIncrementer;
	}

	public void setIntercambioRegistralSalidaIncrementer(
			DataFieldMaxValueIncrementer intercambioRegistralSalidaIncrementer) {
		this.intercambioRegistralSalidaIncrementer = intercambioRegistralSalidaIncrementer;
	}


	public IntercambioRegistralSIRManager getIntercambioRegistralSIRManager() {
		return intercambioRegistralSIRManager;
	}


	public void setIntercambioRegistralSIRManager(
			IntercambioRegistralSIRManager intercambioRegistralSIRManager) {
		this.intercambioRegistralSIRManager = intercambioRegistralSIRManager;
	}


	public boolean isInBandejasalidaIntercambioRegistral(String idRegistro,
			String idLibro) {
		// TODO Auto-generated method stub
		return false;
	}

	public RegistroManager getRegistroManager() {
		return registroManager;
	}

	public void setRegistroManager(RegistroManager registroManager) {
		this.registroManager = registroManager;
	}

	public void rectificarIntercambioRegistralSalidaById(String idIntercambioRegistralSalida,
		IdentificadorRegistroVO  identificadorRegistroVO) {
		
	 // obtener datos del intercambio registral
		AsientoRegistralVO asientoRegistral =
			getIntercambioRegistralSIRManager().getAsientoRegistral(idIntercambioRegistralSalida);
		//Generamos el documento principal
		List<DocumentoElectronicoAnexoVO> documentosElectronicoAnexo= new ArrayList<DocumentoElectronicoAnexoVO>();
		documentosElectronicoAnexo =populateDocumentoElectronicoAnexoVO(asientoRegistral, identificadorRegistroVO);
		
		
		ConfiguracionCreateDocumentoElectronicoAnexoVO cfg=new ConfiguracionCreateDocumentoElectronicoAnexoVO();
		
		//seteamos el nombre de la carpeta/clasificador de sicres sobre el que se guardaran
		
		String clasificador = null;
		cfg.setClasificador(clasificador);
		if (documentosElectronicoAnexo != null){
	        	for (DocumentoElectronicoAnexoVO documentoElectronicoAnexo:documentosElectronicoAnexo){
	        	    documentoElectronicoAnexoManager.create(documentoElectronicoAnexo,cfg);
	        	}
		}
	    // vamos a a realizar actualizacion del estado del intercambio en local,
		// pasa a estado aceptado EstadoIntercambioRegistralEntradaEnumVO
		List<IntercambioRegistralSalidaVO> lista = getBandejaSalidaIntercambioRegistralDAO()
			.getIntercambiosRegistralesSalida(EstadoIntercambioRegistralSalidaEnumVO.DEVUELTO_VALUE);
		
		IntercambioRegistralSalidaVO intercambioRegistralSalidaVO = null;
		for (IntercambioRegistralSalidaVO isvo: lista){
		    if (String.valueOf(isvo.getIdIntercambioInterno()).equals(idIntercambioRegistralSalida)){
			intercambioRegistralSalidaVO = isvo;
		    }
		}
		intercambioRegistralSalidaVO.setEstado(EstadoIntercambioRegistralSalidaEnumVO.RECTIFICADO);
		intercambioRegistralSalidaVO.setFechaEstado(Calendar.getInstance()
    			.getTime());

		// Guardamos en la tabal scr_exreg y actualizamos estado en el SIR
		getBandejaSalidaIntercambioRegistralDAO().updateIntercambioRegistralSalidaVO(intercambioRegistralSalidaVO);

	}
	    private List<DocumentoElectronicoAnexoVO> populateDocumentoElectronicoAnexoVO
	    	(AsientoRegistralVO asientoRegistral, IdentificadorRegistroVO  identificadorRegistroVO ) {

		List<AnexoVO> anexos = asientoRegistral.getAnexos();

		if (logger.isDebugEnabled()) {
		    logger.debug("Se van a recupear los anexos del intercambio registral con identificador = "
			    + asientoRegistral.getIdentificadorIntercambio());
		}

		if (anexos == null || anexos.size() == 0) {
		    if (logger.isDebugEnabled()) {
			logger.debug("El intercambio no tiene anexos");
		    }
		    return null;
		}

		// Verificamos los anexos adjuntados
		List<AnexoVO> anexosVerificados = verificarAnexos(anexos);

		// Generamos el listado de DocumentosRegistroVO a partir de los anexos
		// verificados
		List<DocumentoElectronicoAnexoVO> documentosElectronicoAnexo =
			mapearListAnexoVOaListDocumentoElectronicoVO(anexosVerificados, identificadorRegistroVO);

		// retornamos el array de documentos
		return documentosElectronicoAnexo;
	    }
	    public DocumentoElectronicoAnexoManager getDocumentoElectronicoAnexoManager() {
	        return documentoElectronicoAnexoManager;
	    }

	    public void setDocumentoElectronicoAnexoManager(
	    	DocumentoElectronicoAnexoManager documentoElectronicoAnexoManager) {
	        this.documentoElectronicoAnexoManager = documentoElectronicoAnexoManager;
	    }
	    /**
	     * Mï¿½todo que valida que los anexos del intercambio son correctos. En la
	     * actualidad, se valida que el nombre de los ficheros no se repita, si es
	     * asi aï¿½adimos un sufijo
	     * 
	     * @param anexosVO
	     *            - Listado de objetos {@link AnexoVO}
	     * 
	     * @return Listado de objetos {@link AnexoVO} verificados y adaptados
	     */
	    private List<AnexoVO> verificarAnexos(List<AnexoVO> anexosVO) {
		// Inicializamos el array de anexos verificados
		List<AnexoVO> result = new ArrayList<AnexoVO>();

		// Generamos una coleccion para verificar el nombre de las paginas
		// dentro de un documento
		Map<String, AnexoVO> nombresDeAnexosVO = new HashMap<String, AnexoVO>();
		// Creamos la variable contador para nombre de anexos con el mismo
		// nombre
		int count_nombreAnexo_duplicado = 0;

		for (Iterator it = (Iterator) anexosVO.iterator(); it.hasNext();) {
		    AnexoVO anexoVO = (AnexoVO) it.next();

		    // obtenemos el nombre de la pagina/fichero
		    String nombreFichero =
			    obtenerNombrePagina(count_nombreAnexo_duplicado, anexoVO, nombresDeAnexosVO);

		    // aï¿½adimos la informaciï¿½n del nombre al hashMap para que no se
		    // repita
		    nombresDeAnexosVO.put(nombreFichero, anexoVO);

		    // asignamos el nombre correcto al anexoVO
		    anexoVO.setNombreFichero(nombreFichero);

		    // aï¿½adimos el anexoVO a los anexos verificados
		    result.add(anexoVO);
		}

		// retornamos el array de anexos verificados
		return result;
	    }
	    
	    /**
	     * Mï¿½todo que obtiene el nombre de la pagina/fichero, validando que no se
	     * repita en un mismo documento dos paginas con el mismo nombre
	     * 
	     * @param count_nombreAnexo_duplicado
	     *            - Numero de la pagina
	     * @param anexoVO
	     *            - Informaciï¿½n de la pagina/documento/fichero
	     * @param nombresDeAnexosVO
	     *            - Colecciï¿½n con los anexos
	     * @param documentoFisico
	     *            - {@link DocumentoFisicoVO}
	     * 
	     * @return Nombre de la pagina/fichero
	     */
	    private String obtenerNombrePagina(int count_nombreAnexo_duplicado, AnexoVO anexoVO,
		    Map<String, AnexoVO> nombresDeAnexosVO) {

		// Obtenemos el nombre original de la pagina
		String result = anexoVO.getNombreFichero();

		// obtenemos el nombre del fichero
		String nombreFichero = result.substring(0, result.lastIndexOf("."));
		// obtenemos la extension del fichero
		String extension = result.substring(result.lastIndexOf("."), result.length());

		// comprobamos que el nombre de la pagina no exista ya en el documento
		if (nombresDeAnexosVO.containsKey(result)) {
		    boolean registrado = false;
		    while (!registrado) {
			// si existe aï¿½adimos un sufijo, para que no existan dos
			// ficheros
			// con el mismo nombre
			count_nombreAnexo_duplicado++;
			String sufijo = "_" + count_nombreAnexo_duplicado;

			// comprobamos que el nombre de la pagina maximo sea de 64
			// caracteres
			if ((result + sufijo).length() > IDocKeys.MAX_LENGTH_NAME_FILE) {
			    // si excede el tamaï¿½o recortamos el nombre del documento
			    result =
				    (nombreFichero.substring(
					    0,
					    IDocKeys.MAX_LENGTH_NAME_FILE
						    - (sufijo.length() + extension.length()))
					    + sufijo + extension);
			    if (!nombresDeAnexosVO.containsKey(result)) {
				// generamos el nombre del documento
				registrado = true;
			    }
			}
			else {
			    result = nombreFichero + sufijo + extension;
			    if (!nombresDeAnexosVO.containsKey(result)) {
				// generamos el nombre del documento
				registrado = true;
			    }
			}
		    }
		}
		else {
		    // No existe el nombre de la pagina en el documento, por tanto
		    // procedemos a comprobar que el nombre tiene una longitud vï¿½lida
		    // (64 caracteres)
		    if (result.length() > IDocKeys.MAX_LENGTH_NAME_FILE) {
			// truncamos el nombre del fichero a 64 caracteres
			result =
				(nombreFichero.substring(0,
					(IDocKeys.MAX_LENGTH_NAME_FILE - extension.length())))
					+ extension;
		    }
		}

		return result;
	    }


	    private List<AnexoFirmaVO> mapearAnexoFirma(List<AnexoVO> anexosVerificados) {
		List<AnexoFirmaVO> result = null;
		AnexoFirmaVO anexoFirmaVO = null;
		if (anexosVerificados != null){
		    result = new  ArrayList<AnexoFirmaVO>();
		    for (AnexoVO anexoOriginal:anexosVerificados){
			if (anexoOriginal.getIdentificadorDocumentoFirmado() == null ||
				(anexoOriginal.getIdentificadorDocumentoFirmado() != null &&
					anexoOriginal.getIdentificadorDocumentoFirmado().equals(anexoOriginal.getIdentificadorFichero()))){
			    anexoFirmaVO = new AnexoFirmaVO();
			    anexoFirmaVO.setOriginal(anexoOriginal);
			    if (!(anexoOriginal.getIdentificadorDocumentoFirmado() != null &&
					anexoOriginal.getIdentificadorDocumentoFirmado().equals(anexoOriginal.getIdentificadorFichero()))){
	        		    for (AnexoVO anexoFirma:anexosVerificados){
	        			if (anexoFirma.getIdentificadorDocumentoFirmado() != null && 
	        				anexoOriginal.getIdentificadorFichero().equals(anexoFirma.getIdentificadorDocumentoFirmado())){
	        			    anexoFirmaVO.setFirma(anexoFirma);
	        			}
	        		    }
			    }
			    result.add(anexoFirmaVO);
			}
		    }
		}
		return result;
	    }

	    
	    private List<DocumentoElectronicoAnexoVO> mapearListAnexoVOaListDocumentoElectronicoVO(
		    List<AnexoVO> anexosVerificados, IdentificadorRegistroVO  identificadorRegistroVO) {

		List<DocumentoElectronicoAnexoVO> result = new ArrayList<DocumentoElectronicoAnexoVO>();
		DocumentoElectronicoAnexoVO documentoElectronicoAnexo = null;
		IdentificadorDocumentoElectronicoAnexoVO id= new IdentificadorDocumentoElectronicoAnexoVO();
		id.setIdLibro(Long.parseLong(identificadorRegistroVO.getIdLibro()));
		id.setIdRegistro(Long.parseLong(identificadorRegistroVO.getIdRegistro()));
		
		List<AnexoFirmaVO> listAnexosVerificados = mapearAnexoFirma (anexosVerificados);
		
		for (AnexoFirmaVO anexoVerificado: listAnexosVerificados){
		    documentoElectronicoAnexo = new DocumentoElectronicoAnexoVO();
		    documentoElectronicoAnexo.setId(id);
		    //TODO codeName como tiene q ser este codigo
		    String codeName=("codeName"+anexoVerificado.getOriginal().getNombreFichero());
		    codeName=StringUtils.abbreviate(codeName, 21);
		    documentoElectronicoAnexo.setCodeName(codeName);
	        	
		    //extension
		    String extension=anexoVerificado.getOriginal().getNombreFichero().substring(anexoVerificado.getOriginal().getNombreFichero().lastIndexOf(".") + 1,
	        		anexoVerificado.getOriginal().getNombreFichero().length());
		    documentoElectronicoAnexo.setExtension(extension);
	        	
		    //contenido
		    DocumentoElectronicoAnexoContenidoVO contenido=new DocumentoElectronicoAnexoContenidoVO();
		    // Contenido del fichero
		    byte[] contenidoAnexo = getIntercambioRegistralSIRManager().getContenidoAnexo(anexoVerificado.getOriginal().getId());
		    contenido.setContent(contenidoAnexo);
	        	//contenido.setDocUID(documentoCompulsar.getDocumentoOriginal().getLocation());
		    documentoElectronicoAnexo.setContenido(contenido);
		    
		    documentoElectronicoAnexo.setMimeType(anexoVerificado.getOriginal().getTipoMIME());
		    //datos hash
		    DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumento= new DocumentoElectronicoAnexoDatosFirmaVO();
	        
		    if (anexoVerificado.getOriginal().getHash() != null){
			String hashDocumento= Base64.encodeBase64String(anexoVerificado.getOriginal().getHash());
			datosFirmaDocumento.setHash(hashDocumento);
			//String hashAlgDocumento=anexoVerificado.get;
			    //datosFirmaDocumento.setHashAlg(hashAlgDocumento);
			documentoElectronicoAnexo.setDatosFirma(datosFirmaDocumento);
		    }
		    
	        	
		    String name = anexoVerificado.getOriginal().getNombreFichero();
		    documentoElectronicoAnexo.setName(name);
		    if (anexoVerificado.getOriginal().getTipoDocumento() != null){
			String tipoDoc = anexoVerificado.getOriginal().getTipoDocumento().getValue();
			documentoElectronicoAnexo.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.getEnum(Integer.valueOf(tipoDoc.substring(1, 2))));
		    }
		    if (anexoVerificado.getOriginal().getNombreFichero().startsWith("AcuseReciboRegistro")){
        		documentoElectronicoAnexo.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.COPIA);
		    } else {
			 if (anexoVerificado.getOriginal().getValidezDocumento() != null){
     			String tipoVal = anexoVerificado.getOriginal().getValidezDocumento().getValue();
     			documentoElectronicoAnexo.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.getEnum(Integer.valueOf(tipoVal.substring(1, 2))));
     		    }
		    }
		    documentoElectronicoAnexo.setComentario(anexoVerificado.getOriginal().getObservaciones());
		    
		    //firmas del documento
		    List<DocumentoElectronicoAnexoVO> firmas=new ArrayList<DocumentoElectronicoAnexoVO>();
		    DocumentoElectronicoAnexoVO firmaDocumento= new DocumentoElectronicoAnexoVO();
		    //////////////////////////////////////////////////////////////////////////////
	        
		    if (anexoVerificado.getFirma() != null){
			IdentificadorDocumentoElectronicoAnexoVO idFima= new IdentificadorDocumentoElectronicoAnexoVO();
	        	
			idFima.setIdLibro(Long.parseLong(identificadorRegistroVO.getIdLibro()));
			idFima.setIdRegistro(Long.parseLong(identificadorRegistroVO.getIdRegistro()));
			firmaDocumento.setId(idFima);
	        	
			firmaDocumento.setComentario(anexoVerificado.getFirma().getObservaciones());
			//TODO codeName como tiene q ser este codigo
			String codeNameFirma="codeName"+anexoVerificado.getFirma().getIdentificadorDocumentoFirmado();
			codeNameFirma=StringUtils.abbreviate(codeNameFirma, 21);
			firmaDocumento.setCodeName(codeNameFirma);
	        	
			//contenido
			DocumentoElectronicoAnexoContenidoVO contenidoFirma=new DocumentoElectronicoAnexoContenidoVO();
			byte[] contenidoFirmaAnexo = getIntercambioRegistralSIRManager()
				.getContenidoAnexo(anexoVerificado.getFirma().getId());
		    	contenidoFirma.setContent(contenidoFirmaAnexo);
		    	//contenidoFirma.setDocUID(documentoCompulsar.getFirma().getLocation());
		    	firmaDocumento.setContenido(contenidoFirma);
	        	
		    	String extensionFirma=anexoVerificado.getFirma().getIdentificadorFichero().
			    substring(anexoVerificado.getFirma().getIdentificadorFichero().lastIndexOf(".") + 1,
			    anexoVerificado.getFirma().getIdentificadorFichero().length());
		    	firmaDocumento.setExtension(extensionFirma);
	        	
		    	//datos hash
		    	DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumentoFirma= new DocumentoElectronicoAnexoDatosFirmaVO();
	                if (anexoVerificado.getFirma().getHash() != null){
	                    
	                    String hashDocumentoFirma=Base64.encodeBase64String(anexoVerificado.getFirma().getHash());
	                    datosFirmaDocumentoFirma.setHash(hashDocumentoFirma);
	                    //String hashAlgDocumentoFirma=documentoCompulsar.getFirma().getHashAlg();
	                    //datosFirmaDocumentoFirma.setHashAlg(hashAlgDocumentoFirma);
	                    firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
	                }	
	                	
	                String nameFirma=anexoVerificado.getFirma().getNombreFichero();
	                firmaDocumento.setName(nameFirma);
	        	if (anexoVerificado.getFirma().getTipoDocumento() != null){
	        	    String tipoDocFirma = anexoVerificado.getFirma().getTipoDocumento().getValue();
	        	    firmaDocumento.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.getEnum(Integer.valueOf(tipoDocFirma.substring(1, 2))));
	        	}
	        	if (anexoVerificado.getFirma().getValidezDocumento() != null){
	        	    String tipoValFirma = anexoVerificado.getFirma().getValidezDocumento().getValue();
	     	    	    firmaDocumento.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.getEnum(Integer.valueOf(tipoValFirma.substring(1, 2))));
	     	    	}
		
	                //String algFirma=datosEspecificosDocumento.getAlgoritmoFirma();
	                //datosFirmaDocumentoFirma.setAlgFirma(algFirma);
	                if (anexoVerificado.getFirma().getCertificado() != null){
	                    String certificado=new String(anexoVerificado.getFirma().getCertificado());
	                    datosFirmaDocumentoFirma.setCertificado(certificado);
	                }
	                if (anexoVerificado.getFirma().getFirma() != null){
	                    String firma=new String(anexoVerificado.getFirma().getFirma());
	                    datosFirmaDocumentoFirma.setFirma(firma);
	                }
	               // String formatoFirma=datosEspecificosDocumento.getFormatoFirma();
	                //datosFirmaDocumentoFirma.setFormatoFirma(formatoFirma);
	                firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
	                	
	                firmas.add(firmaDocumento);
	                documentoElectronicoAnexo.setFirmas(firmas);
		    }
		    result.add(documentoElectronicoAnexo);
		}
		return result;
	    }

}
