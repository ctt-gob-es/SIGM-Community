package es.msssi.sgm.registropresencial.beans;

import es.ieci.tecdoc.fwktd.core.enums.StringValuedEnum;

/**
 * Enumerados para los campos que se pueden copiar de los registros de entrada
 * con su descripcion.
 * 
 * @author cmorenog
 * 
 */
public class FieldsCopyOutputRegEnum extends StringValuedEnum {

    private static final long serialVersionUID = -585507961217664513L;

    public static final FieldsCopyOutputRegEnum FLD7 = new FieldsCopyOutputRegEnum("fld7label",
	    "fld7");// origen
    public static final FieldsCopyOutputRegEnum FLD8 = new FieldsCopyOutputRegEnum("fld8label",
	    "fld8");// destino
    public static final FieldsCopyOutputRegEnum FLD9 = new FieldsCopyOutputRegEnum("fld9label",
	    "fld9");// interesado
    public static final FieldsCopyOutputRegEnum FLD10 = new FieldsCopyOutputRegEnum("fld14label",
	    "fld10");// transporte
    public static final FieldsCopyOutputRegEnum FLD11 = new FieldsCopyOutputRegEnum("fld15label",
	    "fld11");// numero transporte
    public static final FieldsCopyOutputRegEnum FLD12 = new FieldsCopyOutputRegEnum("fld16label",
	    "fld12");//asunto
    public static final FieldsCopyOutputRegEnum FLD13 = new FieldsCopyOutputRegEnum("fld17label",
	    "fld13");// resumen
    public static final FieldsCopyOutputRegEnum FLD14 = new FieldsCopyOutputRegEnum("fld18label",
	    "fld14");// comentario
    public static final FieldsCopyOutputRegEnum FLD501 = new FieldsCopyOutputRegEnum("labelExpone",
	    "fld501");// expone
    public static final FieldsCopyOutputRegEnum FLD502 = new FieldsCopyOutputRegEnum(
	    "labelSolicita", "fld502");// solicita
    public static final FieldsCopyOutputRegEnum FLD507 = new FieldsCopyOutputRegEnum(
	    "labelobservForward", "fld507");// observaciones
    public static final FieldsCopyOutputRegEnum SOPDOC = new FieldsCopyOutputRegEnum(
	    "selectedDocSIRLabel", "sopDoc");// soporte

    // documentacion

    /**
     * Constructor.
     * 
     * @param name
     *            Nombre del enumerado.
     * @param value
     *            Valor del enumerado.
     */
    protected FieldsCopyOutputRegEnum(String name, String value) {
	super(name, value);
    }

    /**
     * Obtiene la constante asociada al valor.
     * 
     * @param value
     *            Valor de la constante
     * @return Constante.
     */
    public static FieldsCopyOutputRegEnum getFieldsCopy(String value) {
	return (FieldsCopyOutputRegEnum) StringValuedEnum.getEnum(FieldsCopyOutputRegEnum.class,
		value);
    }
}
