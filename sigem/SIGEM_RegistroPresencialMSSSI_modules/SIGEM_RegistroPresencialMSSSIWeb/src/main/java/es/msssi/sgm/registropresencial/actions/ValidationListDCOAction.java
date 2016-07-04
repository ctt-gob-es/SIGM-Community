/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.actions;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.msssi.sgm.registropresencial.beans.QueryCompactSearchOrg;
import es.msssi.sgm.registropresencial.businessobject.UnidadTramitadoraBo;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Action que muestra lista de entidades y unidades DCO disponible.
 * 
 * @author cmorenog
 */
public class ValidationListDCOAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ValidationListDCOAction.class.getName());
    private UnidadTramitadoraBo unidadTramitadoraBo;
    private List<UnidadTramitacionIntercambioRegistralSIRVO> listaUnidadesTramitacionDCO = null;
    private static ApplicationContext appContext;
    private static QueryCompactSearchOrg queryCompactSearchUnitTram;
    static {
	appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
       }
    
    /**
     * Constructor.
     */
    public ValidationListDCOAction() {
	queryCompactSearchUnitTram = (QueryCompactSearchOrg)appContext.getBean("queryCompactSearchUnitTram");
    }

    /**
     * Implementa el autocompletado del combo de unidades tramitadoras.
     * 
     * @param query
     *            Parte del nombre del unidades tramitadoras.
     * 
     * @return suggestions Lista de unidades tramitadoras según el texto
     *         insertado.
     */
    
    public List<UnidadTramitacionIntercambioRegistralSIRVO> completeUnidTramitadora(
	String query) {
	try {
	unidadTramitadoraBo = new UnidadTramitadoraBo();
	String text = Utils.converterToCS(query);
	listaUnidadesTramitacionDCO = unidadTramitadoraBo.buscarUnidadesTramitadoras(
	    text, queryCompactSearchUnitTram);
	}
	catch (RPRegistralExchangeException e) {
	    LOG.error(
		ErrorConstants.GET_LIST_UNID_REGISTER_ERROR_MESSAGE, e);
	    Utils.redirectToErrorPage(
		e, null, null);
	}
	return listaUnidadesTramitacionDCO;
    }

    /**
     * Obtiene el valor del parámetro listaUnidadesTramitacionDCO.
     * 
     * @return listaUnidadesTramitacionDCO valor del campo a obtener.
     */
    public List<UnidadTramitacionIntercambioRegistralSIRVO> getListaUnidadesTramitacionDCO() {
	return listaUnidadesTramitacionDCO;
    }

    /**
     * Guarda el valor del parámetro listaUnidadesTramitacionDCO.
     * 
     * @param listaUnidadesTramitacionDCO
     *            valor del campo a guardar.
     */
    public void setListaUnidadesTramitacionDCO(
	List<UnidadTramitacionIntercambioRegistralSIRVO> listaUnidadesTramitacionDCO) {
	this.listaUnidadesTramitacionDCO = listaUnidadesTramitacionDCO;
    }

}