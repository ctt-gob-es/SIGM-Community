/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.type;

/**
 * Enumerados para las constantes de los organismos raiz.
 * 
 * @author cmorenog
 * 
 */
public class UORootEnum extends StringValuedEnum {
    /**
     */
    public static final UORootEnum U = new UORootEnum(
	"U99999999", "U");
    /**
     */
    public static final UORootEnum HE = new UORootEnum(
	"HE9999999", "HE");
    /**
     */
    public static final UORootEnum HA = new UORootEnum(
	"HA9999999", "HA");
    /**
     */
    public static final UORootEnum HL = new UORootEnum(
	"HL9999999", "HL");
    /**
     */
    public static final UORootEnum L = new UORootEnum(
	"LA9999999", "L");
    /**
     */
    public static final UORootEnum E = new UORootEnum(
	"EA9999999", "E");
    /**
     */
    public static final UORootEnum A = new UORootEnum(
	"A99999999", "A");
    /**
     */
    public static final UORootEnum I = new UORootEnum(
	"I99999999", "I");
    /**
     */
    public static final UORootEnum J = new UORootEnum(
	"J99999999", "J");
    
    private static final long serialVersionUID = -5427563304712979383L;

    /**
     * Constructor.
     * 
     * @param name
     *            Nombre del enumerado.
     * @param value
     *            Valor del enumerado.
     */
    protected UORootEnum(String name, String value) {
	super(name, value);
    }

    /**
     * Obtiene la constante asociada al valor.
     * 
     * @param codigo
     *            el codigo de un organismo.
     * @return Constante.
     */
    public static UORootEnum getRaiz(
	String codigo) {
	UORootEnum result = null;
	if (!codigo.startsWith("H")) {
	    result = (UORootEnum) StringValuedEnum.getEnum(
		UORootEnum.class, codigo.substring(
		    0, 1));
	}
	else {
	    result = (UORootEnum) StringValuedEnum.getEnum(
		UORootEnum.class, codigo.substring(
		    0, 2));
	}
	return result;
    }
}
