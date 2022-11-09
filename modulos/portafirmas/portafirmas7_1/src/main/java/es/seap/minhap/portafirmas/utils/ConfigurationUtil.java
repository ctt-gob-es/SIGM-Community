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

package es.seap.minhap.portafirmas.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;

public class ConfigurationUtil {
	
	/**
	 * Convierte una colecci�n de objetos en un mapa de tipo String,String que corresponde con
	 * el parametro y su valor
	 * @param configurationParameters la colecci�n de objetos que vamos a transformar en un mapa
	 * @return el mapa de tipo String,String generado a partir de la coleccion
	 * @see #recuperaValorParametroYSustituyeEntorno(PfConfigurationsParameterDTO)
	 */
	public static Map<String, String> convierteListaParametrosConfiguracionEnMapa(Collection<AbstractBaseDTO> configurationParameters) {
		// Build Properties object for authenticator
		Map<String, String> auxMap = new HashMap<String, String>();
		// Load map to get parameters and values
		for (AbstractBaseDTO abstractBaseDTO : configurationParameters) {
			PfConfigurationsParameterDTO configParam = (PfConfigurationsParameterDTO) abstractBaseDTO;
			String valorParametro = recuperaValorParametroYSustituyeEntorno(configParam);
			auxMap.put(configParam.getPfParameter().getCparameter(), valorParametro);
		}
		
		return auxMap;
	}
	
	/**
	 * Recupera el valor del parametro de configuracion que esta contenido en el objeto de tipo PfConfigurationsParameterDTO 
	 * y Reemplaza del parametro de configuracion la cadena contenida en la constante "Constants.SGTIC_CONFIGPATH" 
	 * por el valor de la variable del sistema
	 * @param configParam el objeto que contiene el valor del parametro de configuracion
	 * @return el parametro de configuraci�n con la cadena contenida en la constante "Constants.SGTIC_CONFIGPATH"  
	 * reemplazado por el valor de la variable del sistema
	 * @see Constants#SGTIC_CONFIGPATH SGTIC_CONFIGPATH
	 * @see #reemplazaVariableEntornoRutaConfiguracion(String)
	 */
	public static String recuperaValorParametroYSustituyeEntorno(PfConfigurationsParameterDTO configParam) {
		return (configParam == null) ? null : reemplazaVariableEntornoRutaConfiguracion(configParam.getTvalue());
	}
	
	/**
	 * Reemplaza del par�metro de configuraci�n la cadena contenida en la constante "Constants.SGTIC_CONFIGPATH" 
	 * por el valor de la variable del sistema
	 * @param cadena contiene el valor del par�metro de configuraci�n
	 * @return el par�metro de configuraci�n con la cadena contenida en la constante "Constants.SGTIC_CONFIGPATH"  
	 * reemplazado por el valor de la variable del sistema
	 * @see Constants#SGTIC_CONFIGPATH SGTIC_CONFIGPATH
	 */
	private static String reemplazaVariableEntornoRutaConfiguracion(String cadena) {
		if (cadena != null) {
			//recoge el valor de la variable del sistema
			String valorVariableEntorno = System.getProperty(Constants.SGTIC_CONFIGPATH);
			if (valorVariableEntorno != null) {
				cadena = cadena.replaceAll("[$][{]" + Constants.SGTIC_CONFIGPATH + "[}]", valorVariableEntorno);
			}
		}
		return cadena;
	}		

}
