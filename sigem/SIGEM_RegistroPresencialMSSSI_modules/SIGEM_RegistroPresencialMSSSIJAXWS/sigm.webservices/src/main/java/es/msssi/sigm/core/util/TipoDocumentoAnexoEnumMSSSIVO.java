package es.msssi.sigm.core.util;

import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;

public class TipoDocumentoAnexoEnumMSSSIVO extends TipoDocumentoAnexoEnumVO {

	private static final long serialVersionUID = 496282049062936123L;

	protected TipoDocumentoAnexoEnumMSSSIVO(String name, int value) {
		super(name, value);
	}

	public static TipoDocumentoAnexoEnumVO getValue(String value) {
		if (value.equals(FORMULARIO_NAME))
			return FORMULARIO;
		else if (value.equals(DOCUMENTO_ADJUNTO_FORMULARIO_NAME))
			return DOCUMENTO_ADJUNTO_FORMULARIO;
		else if (value.equals(FICHERO_TECNICO_NAME))
			return FICHERO_TECNICO;
		return DOCUMENTO_ADJUNTO_FORMULARIO;
	}

}
