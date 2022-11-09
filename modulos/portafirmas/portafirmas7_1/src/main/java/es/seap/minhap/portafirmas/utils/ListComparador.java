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

/*

 Empresa desarrolladora: GuadalTEL S.A.

 Autor: Junta de AndalucÃ­a

 Derechos de explotaciÃ³n propiedad de la Junta de AndalucÃ­a.

 Ã‰ste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los tÃ©rminos de la Licencia EUPL European Public License publicada 
 por el organismo IDABC de la ComisiÃ³n Europea, en su versiÃ³n 1.0. o posteriores.

 Ã‰ste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÃ�A, incluso sin las presuntas garantÃ­as implÃ­citas de USABILIDAD o ADECUACIÃ“N A PROPÃ“SITO 
 CONCRETO. Para mas informaciÃ³n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por algÃºn motivo no le es posible visualizarla, puede 
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reÃ§u une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da 
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

package es.seap.minhap.portafirmas.utils;

import java.util.Comparator;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfAuthorizationFiltersDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfUserTagsDTO;

/**
 * 
 * @author jesusmlopez
 * 
 */
public class ListComparador implements Comparator<AbstractBaseDTO> {

	private String campo;
	private int order;

	/**
	 * Constructor
	 * @param campo - el tipo de campo que se va a comparar
	 * @param order - debe ser -1 &oacute; 1
	 */
	public ListComparador(String campo, int order) {
		this.campo = campo;
		this.order = order;
	}

	/**
	 * Compara dos objetos y devuelve el resultado como un n&uacute;mero entero
	 * @param o1 el primer objeto para ser comparado
	 * @param o2 el segundo objeto para ser comparado
	 * @return un n&uacute;mero negativo si el primer elemento es el mayor, 0 si ambos elementos son iguales o un n&uacute;mero positivo si el segundo elemento es el mayor
	 * @throws ClassCastException - si los tipos de los argumentos evitan que sean comparados por este comparador.
	 * @see java.lang.ClassCastException
	 */
	public int compare(AbstractBaseDTO o1, AbstractBaseDTO o2) {
		int res = 0;
		if (campo.equals("ctag")) {
			PfUserTagsDTO tag1 = (PfUserTagsDTO) o1;
			PfUserTagsDTO tag2 = (PfUserTagsDTO) o2;
			res = tag1.getPfTag().getCtag().toUpperCase().compareTo(
					tag2.getPfTag().getCtag().toUpperCase());
		} else if (campo.equals("fullName")) {
			PfAuthorizationFiltersDTO aut1 = (PfAuthorizationFiltersDTO) o1;
			PfAuthorizationFiltersDTO aut2 = (PfAuthorizationFiltersDTO) o2;
			res = aut1.getPfUsersAuthorization().getPfAuthorizedUser()
					.getFullName().toUpperCase().compareTo(
							aut2.getPfUsersAuthorization()
									.getPfAuthorizedUser().getFullName()
									.toUpperCase());
		} else if (campo.equals("orderDescCommentByDate")) {
			PfCommentsDTO com1 = (PfCommentsDTO) o1;
			PfCommentsDTO com2 = (PfCommentsDTO) o2;
			res = com2.getFcreated().compareTo(com1.getFcreated());
		} else if (campo.equals("orderAscCommentByDate")) {
			PfCommentsDTO com1 = (PfCommentsDTO) o1;
			PfCommentsDTO com2 = (PfCommentsDTO) o2;
			res = com1.getFcreated().compareTo(com2.getFcreated());
		} else if (campo.equals("signLines")) {
			PfSignLinesDTO sig1 = (PfSignLinesDTO) o1;
			PfSignLinesDTO sig2 = (PfSignLinesDTO) o2;
			res = sig1.getPrimaryKeyString().compareTo(
					sig2.getPrimaryKeyString());
		} else if (campo.equals("serverParams")) {
			PfConfigurationsParameterDTO confParam1 = (PfConfigurationsParameterDTO) o1;
			PfConfigurationsParameterDTO confParam2 = (PfConfigurationsParameterDTO) o2;
			res = confParam1.getPfParameter().getCparameter().toUpperCase()
					.compareTo(
							confParam2.getPfParameter().getCparameter()
									.toUpperCase());
		} else if (campo.equals("serverConfig")) {
			PfConfigurationsDTO conf1 = (PfConfigurationsDTO) o1;
			PfConfigurationsDTO conf2 = (PfConfigurationsDTO) o2;
			res = conf1.getCconfiguration().toUpperCase().compareTo(
					conf2.getCconfiguration().toUpperCase());
		} else if (campo.equals("requestTag")) {
			PfRequestTagsDTO rt1 = (PfRequestTagsDTO) o1;
			PfRequestTagsDTO rt2 = (PfRequestTagsDTO) o2;
			if (rt1.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_STATE)
					&& rt2.getPfTag().getCtype().equals(
							Constants.C_TYPE_TAG_USER)) {
				res = -1;
			} else if (rt1.getPfTag().getCtype().equals(
					rt2.getPfTag().getCtype())) {
				res = 0;
			} else {
				res = 1;
			}
			if (res == 0) {
				res = rt1.getPfTag().getCtag().toUpperCase().compareTo(
						rt2.getPfTag().getCtag().toUpperCase());
			}
		} else if (campo.equals("userTag")) {
			PfUserTagsDTO rt1 = (PfUserTagsDTO) o1;
			PfUserTagsDTO rt2 = (PfUserTagsDTO) o2;
			res = rt1.getPfTag().getCtag().toUpperCase().compareTo(
					rt2.getPfTag().getCtag().toUpperCase());
		} else if (campo.equals("configuration")) {
			PfConfigurationsDTO conf1 = (PfConfigurationsDTO) o1;
			PfConfigurationsDTO conf2 = (PfConfigurationsDTO) o2;
			res = conf1.getCconfiguration().toUpperCase().compareTo(
					conf2.getCconfiguration().toUpperCase());
		} else if (campo.equals("hierarchy")) {
			PfApplicationsDTO app1 = (PfApplicationsDTO) o1;
			PfApplicationsDTO app2 = (PfApplicationsDTO) o2;
			res = app1.getHierarchy().toUpperCase().compareTo(app2.getHierarchy().toUpperCase());			
		}

		return order * res;
	}
}
