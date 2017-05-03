// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

package es.gob.afirma.transformers.xmlTransformers;

import es.gob.afirma.transformers.TransformersException;

/**
 * Esta interfaz alberga los métodos comunes que debe proporcionar
 * un generador de par&aacute;metros de entrada o salida para los servicios publicados por la plataforma @Firma.
 * 
 * @author SEPAOT
 *
 */
public interface IXmlTransformer {

	/**
	 * Obtiene el nombre del servicio para el cual ha sido configurado el generador.
	 * @return nombre del servicio para el cual ha sido configurado el generador.
	 */
	String getService();
	
	
	/**
	 * Obtiene el nombre del método del servicio web a invocar.
	 * @return nombre del método.
	 */
	String getMethod();

	/**
	 * Obtiene el tipo de par&aacute;metro a generar, entrada o salida. Los posibles valores son request y response.
	 * @return tipo de par&aacute;metro a generar, entrada o salida.
	 */
	String getType();

	/**
	 * Obtiene la versi&oacute;n del par&aacute;metro para la cual ha sido configurado el generador.
	 * @return versi&oacute;n del par&aacute;metro para la cual ha sido configurado el generador.
	 */
	String getMessageVersion();
	
	

	/**
	 * Transforma una estructura de un determinado tipo en un par&aacute;metro de entrada o salida esperado o generado
	 * por un servicio publicado por la plataforma.
	 * con los datos del par&aacute;metro de salida.
	 * @param params valores de los distintos par&aacute;metros que formaran el par&aacute;metro de entrada o salida en formato XML.
	 * @return un objeto de tipo String que representa un par&aacute;metro de entrada o salida en formato XML esperado o generado
	 * por un servicio publicado por la plataforma @Firma.
	 * @throws TransformersException si ocurre un error al generar el par&aacute;metro.
	 */
	Object transform(Object params) throws TransformersException;
}
