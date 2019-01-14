/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.ws.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "historicoMovimiento", propOrder = {
    "movimientoRegistro"
})
public class HistoricoMovimiento {

    @XmlElement(required = true)
    protected List<MovimientoRegistro> movimientoRegistro;

    public HistoricoMovimiento() {
	}
    
	public HistoricoMovimiento(List<MovimientoRegistro> movimientoRegistro) {
		super();
		this.movimientoRegistro = movimientoRegistro;
	}
	public void setMovimientoRegistro(List<MovimientoRegistro> movimientoRegistro) {
		this.movimientoRegistro = movimientoRegistro;
	}
	public List<MovimientoRegistro> getMovimientoRegistro() {
        if (movimientoRegistro == null) {
        	movimientoRegistro = new ArrayList<MovimientoRegistro>();
        }		
		return movimientoRegistro;
	}
}
