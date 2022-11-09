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

package es.seap.minhap.portafirmas.utils.eni;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import es.seap.minhap.portafirmas.utils.Constants;

public class ComboUtils {

	public static Map<String, Object> getEstadosElaboracion(Properties messages) {
		Map<String, Object> estadosElaboracionList = new LinkedHashMap<String, Object>();
		estadosElaboracionList.put(messages.getProperty("estadoElaboracion.EE01"), "EE_01");
		estadosElaboracionList.put(messages.getProperty("estadoElaboracion.EE02"), "EE_02");
		estadosElaboracionList.put(messages.getProperty("estadoElaboracion.EE03"), "EE_03");
		estadosElaboracionList.put(messages.getProperty("estadoElaboracion.EE04"), "EE_04");
		estadosElaboracionList.put(messages.getProperty("estadoElaboracion.EE99"), "EE_99");
		return estadosElaboracionList;
	}
	
	public static Map<String, Object> getTiposDocumentales(Properties messages) {
		Map<String, Object> tiposDocumentalesList = new LinkedHashMap<String, Object>();
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD01"), "TD_01");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD02"), "TD_02");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD03"), "TD_03");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD04"), "TD_04");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD05"), "TD_05");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD06"), "TD_06");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD07"), "TD_07");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD08"), "TD_08");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD09"), "TD_09");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD10"), "TD_10");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD11"), "TD_11");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD12"), "TD_12");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD13"), "TD_13");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD14"), "TD_14");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD15"), "TD_15");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD16"), "TD_16");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD17"), "TD_17");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD18"), "TD_18");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD19"), "TD_19");
		tiposDocumentalesList.put(messages.getProperty("tipoDocumental.TD20"), "TD_20");
		return tiposDocumentalesList;
	}
	
	public static Map<String, Object> getOrigen(Properties messages) {
		Map<String, Object> origenList = new LinkedHashMap<String, Object>();
		origenList.put(messages.getProperty("origen.ciudadano"), "0");
		origenList.put(messages.getProperty("origen.administracion"), "1");
		return origenList;
	}
	
	public static Map<String, Object> getEstadosExpediente(Properties messages) {
		Map<String, Object> estadosExpedienteList = new LinkedHashMap<String, Object>();
		estadosExpedienteList.put(messages.getProperty("estadoExpediente.E01"), "E_01");
		estadosExpedienteList.put(messages.getProperty("estadoExpediente.E02"), "E_02");
		estadosExpedienteList.put(messages.getProperty("estadoExpediente.E03"), "E_03");
		return estadosExpedienteList;
	}
	
	public static Map<String, Object> getTiposCertificados(Properties messages) {
		Map<String, Object> tiposCertificadosList = new LinkedHashMap<String, Object>();
		tiposCertificadosList.put(messages.getProperty("tipoCertificado.TF06"), Constants.TYPE_CERTIFICATE_PADES);
		tiposCertificadosList.put(messages.getProperty("tipoCertificado.TF02"), Constants.TYPE_CERTIFICATE_XADES_DETACHED);
		tiposCertificadosList.put(messages.getProperty("tipoCertificado.TF03"), Constants.TYPE_CERTIFICATE_XADES_ENVELOPED);
		tiposCertificadosList.put(messages.getProperty("tipoCertificado.TF05"), Constants.TYPE_CERTIFICATE_CADES);
		return tiposCertificadosList;
	}
	
	public static List<String> getOrganosKeys(List<String> organos, HttpServletRequest request) {
		List<String> retorno = new ArrayList<String>();
		for (String organo : organos) {
			if (organo.indexOf("-") != -1) {
				retorno.add(organo.split("-")[0].trim());
			} else {
				retorno.add(organo);
			}
		}
		if(StringUtils.isNotBlank(request.getParameter("organos"))) {
			String organo = request.getParameter("organos");
			if (organo.indexOf("-") != -1) {
				retorno.add(organo.split("-")[0].trim());
			} else {
				retorno.add(organo);
			}
		}
		return retorno;
	}
	
	public static List<String> getInteresadosKeys(List<String> interesados, HttpServletRequest request) {
		List<String> retorno = new ArrayList<String>();
		for (String interesado : interesados) {
			if (interesado.indexOf("-") != -1) {
				retorno.add(interesado.split("-")[0].trim());
			} else {
				retorno.add(interesado);
			}
		}
		if(StringUtils.isNotBlank(request.getParameter("interesados"))) {
			String interesado = request.getParameter("interesados");		
			if (interesado.indexOf("-") != -1) {
				retorno.add(interesado.split("-")[0].trim());
			} else {
				retorno.add(interesado);
			}
		}
		return retorno;
	}
	
}
