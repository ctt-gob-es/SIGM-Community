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

/**
 * Esta interfaz contiene las constantes utilizadas en la
 * transformación de peticiones.
 * 
 * @author SEPAOT
 *
 */
public interface ParseTransformerConstants {

	//Nombre de nodos del mensaje de respuesta
	/**
	 * Attribute that represents error node in spanish language.
	 */
	String EXCEPTION_ELEMENT_SP = "Excepcion";

	/**
	 * Attribute that represents error node in international language.
	 */
	String EXCEPTION_ELEMENT = "Exception";

	/**
	 * Attribute that represents .
	 */
	String RESPONSE_ELEMENT = "response";

	/**
	 * Attribute that represents response tag in spanish version.
	 */
	String RESPONSE_ELEMENT_SP = "respuesta";
	//Claves en estructuras Map
	/**
	 * Attribute that represents .
	 */
	String ERROR_KEY = "errorResponse";
	/**
	 * Attribute that represents .
	 */
	String OK_KEY = "okResponse";

}
