package es.msssi.sigm.core.util;

import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoValidezDocumentoAnexoEnumVO;

public class TipoValidezDocumentoAnexoEnumMSSSIVO extends TipoValidezDocumentoAnexoEnumVO {

	private static final long serialVersionUID = 496282049062936123L;

	protected TipoValidezDocumentoAnexoEnumMSSSIVO(String name, int value) {
		super(name, value);
	}

	public static TipoValidezDocumentoAnexoEnumVO getValue(String value) {
		if (value == null)
			return null;
		
		if (value.equals(COPIA_NAME))
			return COPIA;
		else if (value.equals(COPIA_COMPULSADA_NAME))
			return COPIA_COMPULSADA;
		else if (value.equals(COPIA_ORIGINAL_NAME))
			return COPIA_ORIGINAL;
		else if (value.equals(ORIGINAL_NAME))
			return ORIGINAL;
		return COPIA;
	}

}
