package es.dipucr.client.preprocesor;

import java.util.Map;

import es.dipucr.core.datos.DatosEspecificosAdapterBase;

public class DatosEspecificosPreprocessorCatastro implements DatosEspecificosPreprocessor {
	public void handleRequest(DatosEspecificosAdapterBase datosAdapter) {
		Map <String, String> params = null;
		for (String key : params.keySet()) {
			if ("datosEspecificos".equals(key))
				continue;
			if ("modoEntrada".equals(key)) {
				continue;
			}
			if ("cif".equals(key))
				continue;
			if ("certificado".equals(key)) {
				continue;
			}
			if ("consentimiento".equals(key))
				continue;
			if ("tipoDocumentacion".equals(key)) {
				continue;
			}
			String value = (String) params.get(key);
			params.put(key, (value == null) ? null : value.toUpperCase());
		}
	}
}