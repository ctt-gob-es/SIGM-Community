/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import java.text.ParseException;
import es.msssi.dir3.api.dao.impl.AddressDaoImpl;
/**
 * Handler para transformar la fecha del xml en formato correcto.
 * 
 * @author cmorenog
 * 
 */
public class DateHandler extends GeneralizedFieldHandler {

    private static final String FORMAT = "dd/MM/yyyy";
    /**
     * Logger de la clase.
     */
    private static final Logger logger = Logger.getLogger(AddressDaoImpl.class);

    private SimpleDateFormat formatter = new SimpleDateFormat(
	FORMAT);

    /**
     * Formatea la fecha que entra como valor.
     * 
     * @param value
     *           fecha a transformar.
     * @return String con el objecto formateado.
     */
    public Object convertUponGet(
	Object value) {
	Date date = null;
	Object object = null;
	if (value != null) {
	   date = (Date) value;
	   object = formatter.format(date);
	}
	return object;
    }
    
    /**
     * Formatea la fecha que entra como valor.
     * 
     * @param value
     *           fecha a transformar.
     * @return Object objeto date formateado.
     */
    public Object convertUponSet(
	Object value) {
	Date date = null;
	try {
	    date = formatter.parse((String) value);
	}
	catch (ParseException px) {
	    logger.error("Parse Exception (bad date format) : " +
		(String) value);
	    date = null; 
	}
	return date;
    }
    
    /**
     * Devuelve el objeto class de Date.
     * @return class clase de fecha.
     */
    public Class<?> getFieldType() {
	return Date.class;
    }
    
    /**
     * Devuelve la instancia del objeto
     * @param parent el objeto.
     * @return Object null.
     */
    public Object newInstance(
	Object parent)
	throws IllegalStateException {
	return null;
    }
}