/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package sigm.dao.dataaccess.persistence;

import sigm.dao.dataaccess.domain.XtField;
import sigm.dao.exception.DaoException;
 
public interface XtfieldMapper {
 
    public int insert(XtField obj) throws DaoException;
	public XtField getByName(String name) throws DaoException;
    public XtField getById(int id) throws DaoException;
//    public List<XtField> getAll() throws DaoException;
	public XtField getByXtfield(XtField obj) throws DaoException;

 
}