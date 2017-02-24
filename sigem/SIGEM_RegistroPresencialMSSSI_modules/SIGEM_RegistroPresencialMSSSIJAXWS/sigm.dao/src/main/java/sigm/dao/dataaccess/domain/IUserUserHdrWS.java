/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package sigm.dao.dataaccess.domain;


public class IUserUserHdrWS  {
    private long id;
    private String name;
    private boolean signRequired;

    /**
     * Constructor.
     *
     */
    public IUserUserHdrWS() {
    }
    
	public IUserUserHdrWS(long id, String name, boolean signRequired) {
		super();
		this.id = id;
		this.name = name;
		this.signRequired = signRequired;
	}

	public IUserUserHdrWS(String name, boolean signRequired) {
		super();
		this.name = name;
		this.signRequired = signRequired;
	}
 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public boolean isSignRequired() {
		return signRequired;
	}


	public void setSignRequired(boolean signRequired) {
		this.signRequired = signRequired;
	}
    
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
}