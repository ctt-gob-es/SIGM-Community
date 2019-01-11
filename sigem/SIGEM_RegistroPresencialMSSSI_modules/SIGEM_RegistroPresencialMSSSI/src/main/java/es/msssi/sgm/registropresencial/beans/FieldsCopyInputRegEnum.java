package es.msssi.sgm.registropresencial.beans;

import es.ieci.tecdoc.fwktd.core.enums.StringValuedEnum;

/**
 * Enumerados para los campos que se pueden copiar de los registros de entrada
 * con su descripcion.
 * 
 * @author cmorenog
 * 
 */
public class FieldsCopyInputRegEnum extends StringValuedEnum {

    private static final long serialVersionUID = -585507961217664513L;

    public static final FieldsCopyInputRegEnum FLD7 = new FieldsCopyInputRegEnum("fld7label",
	    "fld7");// origen
    public static final FieldsCopyInputRegEnum FLD8 = new FieldsCopyInputRegEnum("fld8label",
	    "fld8");// destino
    public static final FieldsCopyInputRegEnum FLD9 = new FieldsCopyInputRegEnum("fld9label",
	    "fld9");// interesado
    public static final FieldsCopyInputRegEnum FLD10 = new FieldsCopyInputRegEnum("fld10label",
	    "fld10");// numero registro original
    public static final FieldsCopyInputRegEnum FLD11 = new FieldsCopyInputRegEnum("fld11label",
	    "fld11");// tipo registro original
    public static final FieldsCopyInputRegEnum FLD12 = new FieldsCopyInputRegEnum("fld12label",
	    "fld12");// fecha registro original
    public static final FieldsCopyInputRegEnum FLD14 = new FieldsCopyInputRegEnum("fld14label",
	    "fld14");// transporte
    public static final FieldsCopyInputRegEnum FLD15 = new FieldsCopyInputRegEnum("fld15label",
	    "fld15");// numero transporte
    public static final FieldsCopyInputRegEnum FLD16 = new FieldsCopyInputRegEnum("fld16label",
	    "fld16");// asunto
    public static final FieldsCopyInputRegEnum FLD17 = new FieldsCopyInputRegEnum("fld17label",
	    "fld17");// resumen
    public static final FieldsCopyInputRegEnum FLD18 = new FieldsCopyInputRegEnum("fld18label",
	    "fld18");// comentario
    public static final FieldsCopyInputRegEnum FLD19 = new FieldsCopyInputRegEnum("fld19label",
	    "fld19");// expediente
    public static final FieldsCopyInputRegEnum FLD501 = new FieldsCopyInputRegEnum("labelExpone",
	    "fld501");// expone
    public static final FieldsCopyInputRegEnum FLD502 = new FieldsCopyInputRegEnum("labelSolicita",
	    "fld502");// solicita
    public static final FieldsCopyInputRegEnum FLD507 = new FieldsCopyInputRegEnum(
	    "labelobservForward", "fld507");// observaciones
    public static final FieldsCopyInputRegEnum SOPDOC = new FieldsCopyInputRegEnum(
	    "selectedDocSIRLabel", "sopDoc");// soporte documentacion

    /**
     * Constructor.
     * 
     * @param name
     *            Nombre del enumerado.
     * @param value
     *            Valor del enumerado.
     */
    protected FieldsCopyInputRegEnum(String name, String value) {
	super(name, value);
    }

    /**
     * Obtiene la constante asociada al valor.
     * 
     * @param value
     *            Valor de la constante
     * @return Constante.
     */
    public static FieldsCopyInputRegEnum getFieldsCopy(String value) {
	return (FieldsCopyInputRegEnum) StringValuedEnum.getEnum(FieldsCopyInputRegEnum.class,
		value);
    }
}
