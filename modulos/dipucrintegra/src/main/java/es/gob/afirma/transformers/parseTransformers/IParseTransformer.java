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

package es.gob.afirma.transformers.parseTransformers;

import es.gob.afirma.transformers.TransformersException;

/**
 * Esta interfaz alberga los métodos comunes que debe proporcionar
 * un parseador de par&aacute;metros de salida de servicios publicados por la plataforma @Firma.
 * 
 * @author SEPAOT
 *
 */
public interface IParseTransformer {

	/**
	 * Obtiene el nombre del servicio para el cual ha sido configurado el parseador.
	 * @return nombre del servicio para el cual ha sido configurado el parseador.
	 */
	String getRequest();
	
	/**
	 * Obtiene el nombre del método del servicio web para el cual ha sido configurado el parseador.
	 * @return nombre del método del servicio web para el cual ha sido configurado el parseador.
	 */
	String getMethod();

	/**
	 * Obtiene la versi&oacute;n del par&aacute;metro para la cual ha sido configurado el parseador.
	 * @return versi&oacute;n del par&aacute;metro para la cual ha sido configurado el parseador.
	 */
	String getMessageVersion();

	/**
	 * Transforma una respuesta generada por la plataforma en el tipo objeto deseado por el desarrollador (estructura clave / valor, colecci&oacute;n, ...)
	 * con los datos del par&aacute;metro de salida.
	 * @param xmlResponse cadena con formato XML que ser&aacute; parseada.
	 * @return un objeto del tipo deseado por el desarrollador (estructura clave / valor, colecci&oacute;n, ...) con el resultado de parsear un
	 * par&aacute;metro de salida.
	 * @throws TransformersException si ocurre un error al transformar la respuesta.
	 */
	Object transform(String xmlResponse) throws TransformersException;
}
