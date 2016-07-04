/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package sigm.dao.exception;


/**
 * Class to manage DAO exceptions.
 *
 * @author jviota
 * @version version, 07/01/2015 14:20
 */
public class DaoException extends GenericException {

    private static final long serialVersionUID = -8562280490048153537L;

    public DaoException() {
	super();
    }

    public DaoException(String msgID) {
	super(msgID);
    }

    public DaoException(String msgID, Object[] exArgs) {
	super(msgID, exArgs);
    }

    public DaoException(Exception originalException) {
	super(originalException.getMessage(), originalException);
    }

    public DaoException(String msgID, Exception originalException) {
	super(msgID, originalException);
    }

    public DaoException(String msgID, Object[] exArgs, Exception originalException) {
	super(msgID, exArgs, originalException);
    }
}
