package es.sigem.dipcoruna.desktop.asyncUploader.service.state;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import es.sigem.dipcoruna.desktop.asyncUploader.model.State;
import es.sigem.dipcoruna.framework.service.util.PreferenciasHolder;

/**
 * Implementacion del servicio de estados
 */
@Service("stateService")
public class StateServiceImpl implements StateService {

	private static final Gson GSON = new Gson();
	
	@Autowired
	private PreferenciasHolder preferencias;
	
	@Override
	public final String addState(final String filePath, final String entidad, final String tramite, final String tipoDocumento,	final String usuario,
			final String fase, final String tipoDestino, final String idDestino, final String nombre) {
		State estado = new State();
		estado.setFilePath(filePath);
		estado.setEntidad(entidad);
		estado.setTramite(tramite);
		estado.setTipoDocumento(tipoDocumento);
		estado.setFase(fase);
		estado.setTipoDestino(tipoDestino);
		estado.setIdDestino(idDestino);
		estado.setUsuario(usuario);
		estado.setNombre(nombre);
		estado.setFolder(String.valueOf((new Date().getTime())));
		String key = UUID.randomUUID().toString();
		preferencias.putProperty(key, GSON.toJson(estado));		
		return key;
	}

	@Override
	public final void updateState(final String key, final long transferidos) {
		String property = preferencias.getProperty(key);
		State estado = GSON.fromJson(property, State.class);
		estado.setTransferidos(transferidos);
		preferencias.putProperty(key, GSON.toJson(estado));
	}

	@Override
	public final void deleteState(final String key) {
		preferencias.removeProperty(key);
	}
	
	@Override
	public final Map<String, State> getAllState() {
		Map<String, String> propertiesAsMap = preferencias.getPropertiesAsMap();
		LinkedHashMap<String, State> resultado = new LinkedHashMap<String, State>();
		for (Map.Entry<String, String> entry : propertiesAsMap.entrySet()) {
			resultado.put(entry.getKey(), GSON.fromJson(entry.getValue(), State.class));
		}
		return resultado;
	}

	@Override
	public final State getState(final String key) {
		return GSON.fromJson(preferencias.getProperty(key), State.class);
	}

}
