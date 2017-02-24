/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.beans;

import java.io.Serializable;

import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxSf;

import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralVO;

/**
 * Bean que guarda los datos de la búsqueda de destinatarios del intercambio
 * registral.
 * 
 * @author cmorenog
 */
public class SearchDestinationRegInterchangeBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private UnidadTramitacionIntercambioRegistralVO unidTramPorDefecto;
    private ScrRegstate book;
    private AxSf axsf;
    private UnidadTramitacionIntercambioRegistralSIRVO unidadTramitadoraDestino;

    /**
     * Constructor.
     */
    public SearchDestinationRegInterchangeBean() {
    }

    /**
     * Obtiene el valor del parámetro unidTramPorDefecto.
     * 
     * @return unidTramPorDefecto valor del campo a obtener.
     */
    public UnidadTramitacionIntercambioRegistralVO getUnidTramPorDefecto() {
	return unidTramPorDefecto;
    }

    /**
     * Guarda el valor del parámetro unidTramPorDefecto.
     * 
     * @param unidTramPorDefecto
     *            valor del campo a guardar.
     */
    public void setUnidTramPorDefecto(
	UnidadTramitacionIntercambioRegistralVO unidTramPorDefecto) {
	this.unidTramPorDefecto = unidTramPorDefecto;
    }

    /**
     * Obtiene el valor del parámetro book.
     * 
     * @return book valor del campo a obtener.
     */
    public ScrRegstate getBook() {
	return book;
    }

    /**
     * Guarda el valor del parámetro book.
     * 
     * @param book
     *            valor del campo a guardar.
     */
    public void setBook(
	ScrRegstate book) {
	this.book = book;
    }

    /**
     * Obtiene el valor del parámetro axsf.
     * 
     * @return axsf valor del campo a obtener.
     */
    public AxSf getAxsf() {
	return axsf;
    }

    /**
     * Guarda el valor del parámetro axsf.
     * 
     * @param axsf
     *            valor del campo a guardar.
     */
    public void setAxsf(
	AxSf axsf) {
	this.axsf = axsf;
    }

    /**
     * Obtiene el valor del parámetro unidadTramitadoraDestino.
     * 
     * @return unidadTramitadoraDestino valor del campo a obtener.
     */
    public UnidadTramitacionIntercambioRegistralSIRVO getUnidadTramitadoraDestino() {
	return unidadTramitadoraDestino;
    }

    /**
     * Guarda el valor del parámetro unidadTramitadoraDestino.
     * 
     * @param unidadTramitadoraDestino
     *            valor del campo a guardar.
     */
    public void setUnidadTramitadoraDestino(
	UnidadTramitacionIntercambioRegistralSIRVO unidadTramitadoraDestino) {
	this.unidadTramitadoraDestino = unidadTramitadoraDestino;
    }
}