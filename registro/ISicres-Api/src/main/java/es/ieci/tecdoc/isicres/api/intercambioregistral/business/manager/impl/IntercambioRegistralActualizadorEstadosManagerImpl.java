package es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import es.ieci.tecdoc.fwktd.sir.core.types.EstadoAsientoRegistralEnum;
import es.ieci.tecdoc.fwktd.sir.core.vo.AsientoRegistralVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.EstadoAsientoRegistraVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralActualizadorEstadosManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralSIRManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralSalidaManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.DataMailDAO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.EstadoIntercambioRegistralSalidaEnumVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.EstadoIntercambioRegistralSalidaVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralSalidaVO;

public class IntercambioRegistralActualizadorEstadosManagerImpl implements
		IntercambioRegistralActualizadorEstadosManager {

	private static Logger logger = Logger.getLogger(IntercambioRegistralActualizadorEstadosManagerImpl.class);

	/**
	 * Manager para leer o actualizar los intercambios registrales de salida
	 */
	protected IntercambioRegistralSalidaManager intercambioRegistralSalidaManager;

	protected IntercambioRegistralSIRManager intercambioRegistralSIRManager;

	public List<DataMailDAO> actualizarEstadoEnviados() 
	{
			if (logger.isDebugEnabled()) {
				logger
						.debug("Se van a actualizar los estados de los intercambios registrales ENVIADOS");
			}
			// ¿Configurar un escheduler por entidad? ¿Leer desde aquí todas las
			// entidades y ejecutar por cada una?
	
			List<DataMailDAO> listDataMailDAO = new ArrayList<DataMailDAO>();
			
			List<IntercambioRegistralSalidaVO> intercambiosPendientes = getIntercambioRegistralSalidaManager()
					.getIntercambiosRegistralesSalida(
							EstadoIntercambioRegistralSalidaEnumVO.ENVIADO
									.getValue());
			if (intercambiosPendientes != null) 
			{
				boolean errorProcess;
				for (IntercambioRegistralSalidaVO intercambioRegistralSalidaVO : intercambiosPendientes) {
					
					errorProcess = false;
					EstadoAsientoRegistraVO estado = null;
					
					// Consultamos el estado en el SIR
					if (intercambioRegistralSalidaVO.getIdIntercambioInterno() != null) 
					{
						long startTime = 0;
						try 
						{
							startTime = System.currentTimeMillis();
							
							//obtenemos el estado del modulo intermedio
							estado = getIntercambioRegistralSIRManager()
									.getEstadoAsientoRegistral(
											intercambioRegistralSalidaVO
													.getIdIntercambioInterno()
													.toString());
						}
						catch (Exception e) {
							logger.error(e.getMessage());
							logger
									.error("Error al obtener el estado del intercambio registral en el modulo intermedio con id:"
											+ intercambioRegistralSalidaVO
													.getIdIntercambioInterno()
											+ " con id intercambio: " + intercambioRegistralSalidaVO.getIdIntercambioRegistral() + ""
													+ " e id " + intercambioRegistralSalidaVO.getId());
							
							long finishTime = System.currentTimeMillis()-startTime;
							logger.error("Tiempo de ejecución del método getEstadoAsientoRegistral: " + finishTime);
							
							
							logger
									.error("Continuamos intentando actualizar el siguiente intercambio");
							errorProcess = true;
							
						}	
							
						if(errorProcess) 
							continue;
						
						startTime = 0;
						try 
						{
							startTime = System.currentTimeMillis();
							
							if (estado == null) {
							    estado = getIntercambioRegistralSIRManager()
									.getEstadoAsientoRegistralByCode(
											intercambioRegistralSalidaVO.getIdIntercambioRegistral());
							}
							
						}
						 catch (Exception e) {
							logger.error(e.getMessage());
							logger
									.error("Error al obtener el estado del asiento registral con id:"
											+ intercambioRegistralSalidaVO
													.getIdIntercambioInterno()
											+ " con id intercambio: " + intercambioRegistralSalidaVO.getIdIntercambioRegistral() + ""
													+ " e id " + intercambioRegistralSalidaVO.getId());
							
							long finishTime = System.currentTimeMillis()-startTime;
							logger.error("Tiempo de ejecución del método getEstadoAsientoRegistralByCode: " + finishTime);
							
							logger
									.error("Continuamos intentando actualizar el siguiente intercambio");
							
							errorProcess = true;
						}
							
						if(errorProcess) 
							continue;
						
						try 
						{
							EstadoAsientoRegistralEnum estadoEnum = null;
							if (estado != null){
								estadoEnum = estado.getEstado();
							}
							
							//actuamos en caso de que el estado sea distinto de enviado a enviado y ack
							if (estadoEnum != null
									&&  !EstadoAsientoRegistralEnum.ENVIADO.equals(estadoEnum)
									&& !EstadoAsientoRegistralEnum.ENVIADO_Y_ACK.equals(estadoEnum)
									&&  !EstadoAsientoRegistralEnum.REENVIADO.equals(estadoEnum)
									&&  !EstadoAsientoRegistralEnum.REENVIADO_Y_ACK.equals(estadoEnum)
									&& !EstadoAsientoRegistralEnum.RECIBIDO.equals(estadoEnum)) {
	
								EstadoIntercambioRegistralSalidaVO nuevoEstadoSalida = null ; 
								if (estado != null) {
									nuevoEstadoSalida = new EstadoIntercambioRegistralSalidaVO();
									estadoEnum = estado.getEstado();
									String contactoUsuario = estado.getContactoUsuario();
									//de momento no se usa
									Map<String, String> datosAdicionales = estado.getDatosAdicionales();
									Date fechaEstado = estado.getFechaEstado();
									String nombreUsuario = estado.getNombreUsuario();
									String observaciones = estado.getObservaciones();
									
									nuevoEstadoSalida.setEstado(EstadoIntercambioRegistralSalidaEnumVO.getEnum(estadoEnum.getValue()));
									nuevoEstadoSalida.setComentarios(observaciones);
									nuevoEstadoSalida.setUserName(nombreUsuario +"-"+contactoUsuario);
									nuevoEstadoSalida.setFechaEstado(fechaEstado);
									nuevoEstadoSalida.setIdExReg(intercambioRegistralSalidaVO.getId());
									
								}
								
	
								getIntercambioRegistralSalidaManager()
										.updateEstado(intercambioRegistralSalidaVO,
												nuevoEstadoSalida);
								
								
								if (logger.isDebugEnabled()) {
									logger
											.debug("Actualizado el estado del intercambio registral "
													+ intercambioRegistralSalidaVO
															.getIdIntercambioInterno()
													+ " al estado "
													+ estado.getEstado().getName());
								}
								
								// Recuperamos los datos que se van a enviar por email
								listDataMailDao(estado, listDataMailDAO, 
										intercambioRegistralSalidaVO, estadoEnum);
	
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
							logger
									.error("Error al modificar el estado del intercambio registral con id:"
											+ intercambioRegistralSalidaVO
													.getIdIntercambioInterno()
											+ " con id intercambio: " + intercambioRegistralSalidaVO.getIdIntercambioRegistral() + ""
													+ " e id " + intercambioRegistralSalidaVO.getId());
							logger
									.error("Continuamos intentando actualizar el siguiente intercambio");
						}
					}
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger
							.debug("No hay intercambios registrales en estado ENVIADO para actualizar");
				}
			}
			
			
		List<IntercambioRegistralSalidaVO> intercambiosReenviados = getIntercambioRegistralSalidaManager()
				.getIntercambiosRegistralesSalida(
						EstadoIntercambioRegistralSalidaEnumVO.REENVIADO
								.getValue());
		if (intercambiosReenviados != null) {
			boolean errorProcess;
			for (IntercambioRegistralSalidaVO intercambioRegistralSalidaVO : intercambiosReenviados) {
				
				errorProcess = false;
				EstadoAsientoRegistraVO estado = null;
				
				// Consultamos el estado en el SIR
				if (intercambioRegistralSalidaVO.getIdIntercambioInterno() != null) {
					
					long startTime = 0;
					try 
					{
						startTime = System.currentTimeMillis();
						
						//obtenemos el estado del modulo intermedio
						estado = getIntercambioRegistralSIRManager()
								.getEstadoAsientoRegistral(
										intercambioRegistralSalidaVO
												.getIdIntercambioInterno()
												.toString());
					
					}
					catch (Exception e) {
						logger.error(e.getMessage());
						logger
								.error("Estado de Asiento (REENVIADO) - Error al obtener el estado del intercambio registral en el modulo intermedio con id:"
										+ intercambioRegistralSalidaVO
												.getIdIntercambioInterno()
										+ " con id intercambio: " + intercambioRegistralSalidaVO.getIdIntercambioRegistral() + ""
												+ " e id " + intercambioRegistralSalidaVO.getId());
						
						long finishTime = System.currentTimeMillis()-startTime;
						logger.error("Estado de Asiento (REENVIADO) - Tiempo de ejecución del método getEstadoAsientoRegistral: " + finishTime);
						
						
						logger
								.error("Estado de Asiento (REENVIADO) -Continuamos intentando actualizar el siguiente intercambio");
						errorProcess = true;
						
					}	
						
					if(errorProcess) 
						continue;
					
					startTime = 0;
					try 
					{
						startTime = System.currentTimeMillis();
						
						if (estado == null) {
						    estado = getIntercambioRegistralSIRManager()
								.getEstadoAsientoRegistralByCode(
										intercambioRegistralSalidaVO.getIdIntercambioRegistral());
						}
					}
					catch (Exception e) {
						logger.error(e.getMessage());
						logger
								.error("Estado de Asiento (REENVIADO) - Error al obtener el estado del asiento registral con id:"
										+ intercambioRegistralSalidaVO
												.getIdIntercambioInterno()
										+ " con id intercambio: " + intercambioRegistralSalidaVO.getIdIntercambioRegistral() + ""
												+ " e id " + intercambioRegistralSalidaVO.getId());
						
						long finishTime = System.currentTimeMillis()-startTime;
						logger.error("Estado de Asiento (REENVIADO) - Tiempo de ejecución del método getEstadoAsientoRegistralByCode: " + finishTime);
						
						logger
								.error("Estado de Asiento (REENVIADO) - Continuamos intentando actualizar el siguiente intercambio");
						
						errorProcess = true;
					}
						
					if(errorProcess) 
						continue;	
							
					try 
					{
						EstadoAsientoRegistralEnum estadoEnum = null;
						if (estado != null){
							estadoEnum = estado.getEstado();
						}
						
						//actuamos en caso de que el estado sea distinto de enviado a enviado y ack
						if (estadoEnum != null
								&&  !EstadoAsientoRegistralEnum.ENVIADO.equals(estadoEnum)
								&& !EstadoAsientoRegistralEnum.ENVIADO_Y_ACK.equals(estadoEnum)
								&&  !EstadoAsientoRegistralEnum.REENVIADO.equals(estadoEnum)
								&&  !EstadoAsientoRegistralEnum.REENVIADO_Y_ACK.equals(estadoEnum)
								&& !EstadoAsientoRegistralEnum.RECIBIDO.equals(estadoEnum)) {
	
							EstadoIntercambioRegistralSalidaVO nuevoEstadoSalida = null ; 
							if (estado != null) {
								nuevoEstadoSalida = new EstadoIntercambioRegistralSalidaVO();
								estadoEnum = estado.getEstado();
								String contactoUsuario = estado.getContactoUsuario();
								//de momento no se usa
								Map<String, String> datosAdicionales = estado.getDatosAdicionales();
								Date fechaEstado = estado.getFechaEstado();
								String nombreUsuario = estado.getNombreUsuario();
								String observaciones = estado.getObservaciones();
								
								nuevoEstadoSalida.setEstado(EstadoIntercambioRegistralSalidaEnumVO.getEnum(estadoEnum.getValue()));
								nuevoEstadoSalida.setComentarios(observaciones);
								nuevoEstadoSalida.setUserName(nombreUsuario +"-"+contactoUsuario);
								nuevoEstadoSalida.setFechaEstado(fechaEstado);
								nuevoEstadoSalida.setIdExReg(intercambioRegistralSalidaVO.getId());
								
							}
							
	
							getIntercambioRegistralSalidaManager()
									.updateEstado(intercambioRegistralSalidaVO,
											nuevoEstadoSalida);
							if (logger.isDebugEnabled()) {
								logger
										.debug("Actualizado el estado del intercambio registral "
												+ intercambioRegistralSalidaVO
														.getIdIntercambioInterno()
												+ " al estado "
												+ estado.getEstado().getName());
							}
							
							listDataMailDao(estado, listDataMailDAO, 
									intercambioRegistralSalidaVO, estadoEnum);
						}
					} catch (Exception e) {
						
						logger.error(e.getMessage());
						logger
								.error("Estado de Asiento (REENVIADO) - Error al actualizar el estado del intercambio registral con id:"
										+ intercambioRegistralSalidaVO
												.getIdIntercambioInterno()
										+ " con id intercambio: " + intercambioRegistralSalidaVO.getIdIntercambioRegistral() + ""
												+ " e id " + intercambioRegistralSalidaVO.getId());
						logger
								.error("Estado de Asiento (REENVIADO) - Continuamos intentando actualizar el siguiente intercambio");
						
					}
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger
						.debug("No hay intercambios registrales en estado REENVIO para actualizar");
			}
		}
		
		return listDataMailDAO;
	}

	public IntercambioRegistralSalidaManager getIntercambioRegistralSalidaManager() {
		return intercambioRegistralSalidaManager;
	}

	public void setIntercambioRegistralSalidaManager(
			IntercambioRegistralSalidaManager intercambioRegistralSalidaManager) {
		this.intercambioRegistralSalidaManager = intercambioRegistralSalidaManager;
	}

	public IntercambioRegistralSIRManager getIntercambioRegistralSIRManager() {
		return intercambioRegistralSIRManager;
	}

	public void setIntercambioRegistralSIRManager(
			IntercambioRegistralSIRManager intercambioRegistralSIRManager) {
		this.intercambioRegistralSIRManager = intercambioRegistralSIRManager;
	}

	
	/**
	 * Método que comprueba que el estado del intercambio sea RECHAZADO y prepara los datos para enviarlos por email a los interesados
	 * @param estado - Estado del Asiento Registral 
	 * @param listDataMailDAO - Listado de objetos DataMailDAO (Recupera los datos de los registros que se van a enviar por email) 
	 * @param intercambioRegistralSalidaVO - Obj. de Intercambio Registral de Salida
	 * @param estadoEnum - Estado ValuedEnum del Asiento Registral 
	 */
	private void listDataMailDao(EstadoAsientoRegistraVO estado, List<DataMailDAO> listDataMailDAO, 
			IntercambioRegistralSalidaVO intercambioRegistralSalidaVO, EstadoAsientoRegistralEnum estadoEnum) {
		
		// Recuperamos los datos que se van a enviar por email
		if(estado != null && 
			(EstadoAsientoRegistralEnum.RECHAZADO.equals(estadoEnum)
			|| EstadoAsientoRegistralEnum.DEVUELTO.equals(estadoEnum)
			|| EstadoAsientoRegistralEnum.RECHAZADO_Y_ACK.equals(estadoEnum)
			|| EstadoAsientoRegistralEnum.RECHAZADO_Y_ERROR.equals(estadoEnum))) {
					
			List<String> textSendMail = new ArrayList<String>();
			textSendMail = getTextSendMail(textSendMail, estado.getObservaciones(), intercambioRegistralSalidaVO);
			DataMailDAO dataMaildao = new DataMailDAO();
			
			dataMaildao.setCodeTramUnit(intercambioRegistralSalidaVO.getCodeTramunit());
			dataMaildao.setTextSender(textSendMail);
			if(intercambioRegistralSalidaVO.getTipoOrigen() == 1)
				dataMaildao.setTypeRegister("Entrada");
			else
				dataMaildao.setTypeRegister("Salida");
			
			if(!listDataMailDAO.isEmpty()) 
			{
				DataMailDAO dataMailDAOOut = getHasDataMailDAO(listDataMailDAO, dataMaildao);
				// Si ya existe en el arrayList el objeto DataMailDAO, se añaden los textSender al textSender existente
				if(dataMailDAOOut != null) {
					dataMailDAOOut.getTextSender().addAll(textSendMail);
				}
				else
				{
					listDataMailDAO.add(dataMaildao);
				}
			}
			else
			{
				listDataMailDAO.add(dataMaildao);
			}
		}
	}
	
	
	/**
     * Método que se encarga de recuperar los los datos del registro que van a enviar a través de intercambio registral
     * @param comentarios - Comentarios del rechazo
     * @param intercambioRegistralSalidaVO - Obj. IntercambioRegistralSalidaVO
     * @return List<String> - Texto del email
     */
    private List<String> getTextSendMail(List<String> textSendMail, String comentarios, IntercambioRegistralSalidaVO intercambioRegistralSalidaVO) {
    	String textSender = "";
    	AsientoRegistralVO asientoregistralVO = getIntercambioRegistralSIRManager().getAsientoRegistral(String.valueOf(intercambioRegistralSalidaVO.getIdIntercambioInterno()));
		textSender = "Reg.Orig: " + (StringUtils.isNotBlank(asientoregistralVO.getNumeroRegistroInicial()) ? asientoregistralVO.getNumeroRegistroInicial(): asientoregistralVO.getNumeroRegistro()) + " - Cod.Inter: " + intercambioRegistralSalidaVO.getIdIntercambioRegistral();
		if(StringUtils.isNotBlank(comentarios)) {
			textSender += ": " + comentarios;
		}
    	
		textSendMail.add(textSender);
        return textSendMail;
    }
    
    /**
     * Método que recupera del arrayList aquel objeto que se repite en cuanto a mismo Tipo de Registro y mismo Código de Unidad Tramitadora 
     * @param listDataMailDAO - Listado de objecto DataMailDAO a evaluar
     * @param dataMailDAOAdd - Objeto por el que se va a buscar dentro del arrayList
     * @return DataMailDAO - Devuelve el objeto si lo encuentra dentro del array, sino devuelve null
     */
    private DataMailDAO getHasDataMailDAO(List<DataMailDAO> listDataMailDAO, DataMailDAO dataMailDAOAdd) {
    	
    	for(DataMailDAO dataMailDAO: listDataMailDAO) {
    		if(StringUtils.isNotBlank(dataMailDAOAdd.getCodeTramUnit()) && 
    				StringUtils.isNotBlank(dataMailDAOAdd.getTypeRegister())) {
    			if(dataMailDAOAdd.getTypeRegister().equalsIgnoreCase(dataMailDAO.getTypeRegister()) &&
    					dataMailDAOAdd.getCodeTramUnit().equalsIgnoreCase(dataMailDAO.getCodeTramUnit()) ) {
    				return dataMailDAO;
    			}
    		}
    	}
    	return null;
    }
}
