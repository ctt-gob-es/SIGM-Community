package es.dipucr.sigem.api.rule.common.utils;

import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;

import java.util.ArrayList;
import java.util.List;

public class EntidadesUtil {

	/**
	 * Obtiene la lista de entidades del sistema SIGEM
	 * @return List ArrayList de objetos
	 *         eci.tecdoc.sgm.core.services.dto.Entidad
	 */
	@SuppressWarnings("rawtypes")
	public static List<Entidad> obtenerListaEntidades() {
		try {
			ServicioEntidades oServicio = LocalizadorServicios.getServicioEntidades();
			List oLista = oServicio.obtenerEntidades();
			return getEntidades(oLista);
		} catch (Exception e) {
			return new ArrayList<Entidad>();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<Entidad> getEntidades(List oLista) {

		ArrayList<Entidad> listEntidades = new ArrayList();

		for (int i = 0; i < oLista.size(); i++) {
			ieci.tecdoc.sgm.core.services.entidades.Entidad oEntidad =
					(ieci.tecdoc.sgm.core.services.entidades.Entidad) oLista.get(i);
			listEntidades.add(getEntidadDto(oEntidad));
		}
		return listEntidades;
	}

	public static Entidad getEntidadDto(ieci.tecdoc.sgm.core.services.entidades.Entidad oEntidad) {
		
		if (oEntidad == null) return null;

		Entidad poEntidad = new Entidad();

		poEntidad.setIdentificador(oEntidad.getIdentificador());
		poEntidad.setNombre(oEntidad.getNombreLargo());

		return poEntidad;
	}
}
