/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.ws.eeutil.vis;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.1
 * 2017-10-06T08:13:16.545+02:00
 * Generated source version: 3.0.1
 * 
 */
@WebService(targetNamespace = "http://service.ws.inside.dsic.mpt.es/", name = "EeUtilService")
@XmlSeeAlso({ObjectFactory.class})
public interface EeUtilService {

    @WebResult(name = "listaPlantillas", targetNamespace = "")
    @RequestWrapper(localName = "obtenerPlantillas", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.ObtenerPlantillas")
    @WebMethod(action = "urn:obtenerPlantillas")
    @ResponseWrapper(localName = "obtenerPlantillasResponse", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.ObtenerPlantillasResponse")
    public java.util.List<es.seap.minhap.portafirmas.ws.eeutil.vis.Plantilla> obtenerPlantillas(
        @WebParam(name = "aplicacionInfo", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.ApplicationLogin aplicacionInfo
    ) throws InSideException;

    @WebResult(name = "listaPlantillas", targetNamespace = "")
    @RequestWrapper(localName = "eliminarPlantilla", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.EliminarPlantilla")
    @WebMethod(action = "urn:eliminarPlantilla")
    @ResponseWrapper(localName = "eliminarPlantillaResponse", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.EliminarPlantillaResponse")
    public java.util.List<es.seap.minhap.portafirmas.ws.eeutil.vis.Plantilla> eliminarPlantilla(
        @WebParam(name = "aplicacionInfo", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.ApplicationLogin aplicacionInfo,
        @WebParam(name = "idPlantilla", targetNamespace = "")
        java.lang.String idPlantilla
    ) throws InSideException;

    @WebResult(name = "salidaVisualizar", targetNamespace = "")
    @RequestWrapper(localName = "visualizarContenidoOriginal", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.VisualizarContenidoOriginal")
    @WebMethod(action = "urn:visualizarContenidoOriginal")
    @ResponseWrapper(localName = "visualizarContenidoOriginalResponse", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.VisualizarContenidoOriginalResponse")
    public es.seap.minhap.portafirmas.ws.eeutil.vis.SalidaVisualizacion visualizarContenidoOriginal(
        @WebParam(name = "aplicacionInfo", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.ApplicationLogin aplicacionInfo,
        @WebParam(name = "item", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.Item item
    ) throws InSideException;

    @WebResult(name = "salidaVisualizar", targetNamespace = "")
    @RequestWrapper(localName = "visualizarDocumentoConPlantilla", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.VisualizarDocumentoConPlantilla")
    @WebMethod(action = "urn:visualizarDocumentoConPlantilla")
    @ResponseWrapper(localName = "visualizarDocumentoConPlantillaResponse", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.VisualizarDocumentoConPlantillaResponse")
    public es.seap.minhap.portafirmas.ws.eeutil.vis.SalidaVisualizacion visualizarDocumentoConPlantilla(
        @WebParam(name = "aplicacionInfo", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.ApplicationLogin aplicacionInfo,
        @WebParam(name = "docEniAdicionales", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.DocumentoEniConMAdicionales docEniAdicionales,
        @WebParam(name = "plantilla", targetNamespace = "")
        java.lang.String plantilla
    ) throws InSideException;

    @WebResult(name = "salidaVisualizar", targetNamespace = "")
    @RequestWrapper(localName = "visualizar", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.Visualizar")
    @WebMethod(action = "urn:visualizar")
    @ResponseWrapper(localName = "visualizarResponse", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.VisualizarResponse")
    public es.seap.minhap.portafirmas.ws.eeutil.vis.SalidaVisualizacion visualizar(
        @WebParam(name = "aplicacionInfo", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.ApplicationLogin aplicacionInfo,
        @WebParam(name = "item", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.Item item,
        @WebParam(name = "opcionesVisualizacion", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.OpcionesVisualizacion opcionesVisualizacion
    ) throws InSideException;

    @WebResult(name = "listaPlantillas", targetNamespace = "")
    @RequestWrapper(localName = "asociarPlantilla", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.AsociarPlantilla")
    @WebMethod(action = "urn:asociarPlantilla")
    @ResponseWrapper(localName = "asociarPlantillaResponse", targetNamespace = "http://service.ws.inside.dsic.mpt.es/", className = "es.seap.minhap.portafirmas.ws.eeutil.vis.AsociarPlantillaResponse")
    public java.util.List<es.seap.minhap.portafirmas.ws.eeutil.vis.Plantilla> asociarPlantilla(
        @WebParam(name = "aplicacionInfo", targetNamespace = "")
        es.seap.minhap.portafirmas.ws.eeutil.vis.ApplicationLogin aplicacionInfo,
        @WebParam(name = "idPlantilla", targetNamespace = "")
        java.lang.String idPlantilla,
        @WebParam(name = "plantillaBytes", targetNamespace = "")
        byte[] plantillaBytes
    ) throws InSideException;
}
