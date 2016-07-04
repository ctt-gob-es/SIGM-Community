/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.Enum;

/**
 * Clase abstracta para la gestión de los enum.
 * 
 * @author cmorenog
 * 
 */
public class StringValuedEnum extends Enum {

    private static final long serialVersionUID = 1221422239628168937L;
    /**
     * Logger de la clase.
     */
    private static final Logger LOG = Logger.getLogger(StringValuedEnum.class);
    /**
     * The value contained in enum.
     */
    private final String value;

    /**
     * Constructor for enum item.
     * 
     * @param name
     *            the name of enum item
     * @param value
     *            the value of enum item
     */
    protected StringValuedEnum(String name, String value) {
	super(name);
	this.value = value;
    }

    /**
     * <p>
     * Gets an <code>Enum</code> object by class and value.
     * </p>
     * 
     * <p>
     * This method loops through the list of <code>Enum</code>, thus if there
     * are many <code>Enum</code>s this will be slow.
     * </p>
     * 
     * @param enumClass
     *            the class of the <code>Enum</code> to get
     * @param value
     *            the value of the <code>Enum</code> to get
     * @return the enum object, or null if the enum does not exist
     */
    @SuppressWarnings("rawtypes")
    public static Enum getEnum(
	Class enumClass, String value) {
	if (enumClass == null) {
	    throw new IllegalArgumentException(
		"The Enum Class must not be null");
	}
	List list = Enum.getEnumList(enumClass);
	StringValuedEnum enumerationResult = null;
	for (Iterator it = list.iterator(); it.hasNext();) {
	    StringValuedEnum enumeration = (StringValuedEnum) it.next();
	    if (enumeration.getValue().equals(
		value)) {
		enumerationResult = enumeration;
	    }
	}
	return enumerationResult;
    }

    /**
     * <p>
     * Get value of enum item.
     * </p>
     * 
     * @return the enum item's value.
     */
    public final String getValue() {
	return value;
    }

    /**
     * <p>
     * Tests for order.
     * </p>
     * 
     * <p>
     * The default ordering is numeric by value, but this can be overridden by
     * subclasses.
     * </p>
     * 
     * <p>
     * NOTE: From v2.2 the enums must be of the same type. If the parameter is
     * in a different class loader than this instance, reflection is used to
     * compare the values.
     * </p>
     * 
     * @see java.lang.Comparable#compareTo(Object)
     * @param other
     *            the other object to compare to
     * @return -ve if this is less than the other object, +ve if greater than,
     *         <code>0</code> of equal
     */
    public int compareTo(
	Object other) {
	int result = 0;
	if (other == this) {
	    result = 0;
	}
	else {
	    if (other.getClass() != this.getClass()) {
		if (other.getClass().getName().equals(
		    this.getClass().getName())) {
		    result = value.compareTo(getValueInOtherClassLoader(other));
		}
		else {
		    throw new ClassCastException(
			"Different enum class '" +
			    ClassUtils.getShortClassName(other.getClass()) + "'");
		}
	    }
	    else {
		result = value.compareTo(((StringValuedEnum) other).value);
	    }
	}
	return result;
    }

    /**
     * <p>
     * Use reflection to return an objects value.
     * </p>
     * 
     * @param other
     *            the object to determine the value for.
     * @return the value.
     */
    private String getValueInOtherClassLoader(
	Object other) {
	try {
	    Method mth = other.getClass().getMethod(
		"getValue", (Class[]) null);
	    return (String) mth.invoke(
		other, (Object[]) null);
	}
	catch (NoSuchMethodException e) {
	    LOG.error(
		"No se encuentra el método", e);
	}
	catch (IllegalAccessException e) {
	    LOG.error(
		"Acceso ilegal", e);
	}
	catch (InvocationTargetException e) {
	    LOG.error(
		"Error invocando el método", e);
	}
	throw new IllegalStateException(
	    "This should not happen");
    }

    /**
     * <p>
     * Human readable description of this <code>Enum</code> item.
     * </p>
     * 
     * @return String in the form <code>type[name=value]</code>, for example:
     *         <code>JavaVersion[Java 1.0=100]</code>. Note that the package
     *         name is stripped from the type name.
     */
    public String toString() {
	if (iToString == null) {
	    String shortName = ClassUtils.getShortClassName(getEnumClass());
	    iToString = shortName +
		"[" + getName() + "=" + getValue() + "]";
	}
	return iToString;
    }
}
