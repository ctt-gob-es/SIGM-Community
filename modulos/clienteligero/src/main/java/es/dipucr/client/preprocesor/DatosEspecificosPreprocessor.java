package es.dipucr.client.preprocesor;

import es.dipucr.core.datos.DatosEspecificosAdapterBase;
import ieci.tdw.ispac.api.errors.ISPACException;

public abstract interface DatosEspecificosPreprocessor
{
  public abstract void handleRequest(DatosEspecificosAdapterBase paramDatosAdapterBase) throws ISPACException;
}
