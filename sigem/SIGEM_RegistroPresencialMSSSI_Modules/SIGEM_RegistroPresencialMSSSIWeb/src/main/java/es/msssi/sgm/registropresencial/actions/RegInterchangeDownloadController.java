/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.actions;

import java.io.ByteArrayInputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import es.ieci.tecdoc.fwktd.sir.core.vo.AnexoVO;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralManager;

/**
 * Clase que implementa la descarga de documentos desde el intercambio regitral.
 * 
 * @author cmorenog
 * 
 */
public class RegInterchangeDownloadController {
    /** fichero. */
    private StreamedContent file;
    /** Anexo. */
    private AnexoVO anexo;
    private IntercambioRegistralManager intercambioManager;

    /**
     * constructor.
     */
    public RegInterchangeDownloadController() {
	intercambioManager = IsicresManagerProvider.getInstance().getIntercambioRegistralManager();
    }

    /**
     * devuelve el contenido del documento a descargar.
     * 
     * @return Contenido.
     */
    public StreamedContent getFileDownload() {
	if (anexo != null) {
	    byte[] contenido = intercambioManager.getContenidoAnexo(anexo.getId());
	    file = new DefaultStreamedContent(
		new ByteArrayInputStream(
		    contenido), anexo.getTipoMIME(), anexo.getNombreFichero());
	}
	return file;
    }

    /**
     * Obtiene el valor del parámetro anexo.
     * 
     * @return anexo valor del campo a obtener.
     */
    public AnexoVO getAnexo() {
	return anexo;
    }

    /**
     * Guarda el valor del parámetro anexo.
     * 
     * @param anexo
     *            del campo a guardar.
     */
    public void setAnexo(
	AnexoVO anexo) {
	this.anexo = anexo;
    }

}
