package es.dipucr.sigem.firmaReparo.api;

import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;

import java.util.List;

public interface ISignReparoAPI extends ISignAPI{

	public List firmarReparo(String[] stepIds, String[] signs, String certificado, String entityId, String motivoReparo) throws ISPACException;
}
