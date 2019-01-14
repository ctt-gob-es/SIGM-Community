package es.sigem.dipcoruna.desktop.asyncUploader.service.state;

import java.util.Map;

import es.sigem.dipcoruna.desktop.asyncUploader.model.State;

/**
 * Interfaz del servicio de gestion de estados en preferencias holder
 */
public interface StateService {

	/**
	 * Add state to state collection
	 * @param filePath filePath
	 * @param entidad entidad
	 * @param tramite tramite
	 * @param tipoDocumento tipoDocumento
	 * @param usuario usuario
	 * @param fase fase
	 * @param tipoDestino tipoDestino
	 * @param idDestino idDestino
	 * @param nombre nombre
	 * @return new state key
	 */
	String addState(String filePath, String entidad, String tramite, String tipoDocumento, String usuario, String fase,
			String tipoDestino, String idDestino, String nombre);
	
	/**
	 * Obtiene un estado
	 * @param key Key
	 * @return State
	 */
	State getState(String key);
	
	/**
	 * Actualiza el valor de transferidos
	 * @param key Key
	 * @param transferidos Bytes transferidos
	 */
	void updateState(String key, long transferidos);

	/**
	 * Borra el estado
	 * @param key Key
	 */
	void deleteState(String key);

	/**
	 * Obtiene todos los estados
	 * @return Mapa de estados
	 */
	Map<String, State> getAllState();

	
}
