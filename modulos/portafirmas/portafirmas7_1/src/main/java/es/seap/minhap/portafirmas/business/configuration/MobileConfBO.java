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

package es.seap.minhap.portafirmas.business.configuration;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfMobileUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class MobileConfBO  {


	@Autowired
	private BaseDAO baseDAO;

	/**
	 * Recupera la lista de m&oacute;viles del usuario que pasamos como par&aacute;metro
	 * @param userDTO el usuario
	 * @return la lista de m&oacute;viles del usuario
	 */
	public List<AbstractBaseDTO> queryList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.mobileUser",
				"user", userDTO);
	}

	public void addMobile(List<AbstractBaseDTO> mobileList, PfUsersDTO userDTO) {
		AbstractBaseDTO mobile = new PfMobileUsersDTO();
		((PfMobileUsersDTO) mobile).setPfUser(userDTO);
		mobileList.add(0, mobile);
	}

	/**
	 * M&eacute;todo que guarda en BD un listado de tel&eacute;fonos m&oacute;viles y que, a su vez, elimina de la misma otro listado de tel&eacute;fonos.
	 * @param mobileList Listado de tel&eacute;fonos m&oacute;viles a a&ntilde;adir.
	 * @param mobileDeleteList Listado de tel&eacute;fonos m&oacute;viles a borrar.
	 */
	@Transactional(readOnly = false)
	public void saveMobileList(List<AbstractBaseDTO> mobileList,
			List<AbstractBaseDTO> mobileDeleteList) {
		baseDAO.updateList(mobileList);
		baseDAO.deleteList(mobileDeleteList);
	}

	/**
	 * M&eacute;todo que comprueba si un tel&eacute;fono m&oacute;vil ya existe dentro de la lista de tel&eacute;fonos m&oacute;viles.
	 * @param mobileList Listado de tel&eacute;fonos m&oacute;viles.
	 * @param mobile Tel&eacute;fono m&oacute;vil a buscar.
	 * @param indexEditing Posici&oacute;n del tel&eacute;fono a buscar.
	 * @return "True" si el tel&eacute;fono ya existe en la lista, "false" en caso contrario.
	 */
	public boolean existsDMobile(List<AbstractBaseDTO> mobileList,
			PfMobileUsersDTO mobile, int indexEditing) {
		boolean ret = false;
		if (mobileList != null && mobileList.size() > 0) {
			for (Iterator<AbstractBaseDTO> iterator = mobileList.iterator(); iterator.hasNext()	&& !ret;) {
				PfMobileUsersDTO current = (PfMobileUsersDTO) iterator.next();
				if (current.getDmobile().toUpperCase().equals(mobile.getDmobile().toUpperCase())) {
					if (indexEditing <= -1 && countMobileInList(mobileList, mobile) > 0) {
						ret = true;
					} else if (countMobileInList(mobileList, mobile) > 0) {
						ret = true;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * M&eacute;todo que calcula el n&uacute;mero de veces que aparece un tel&eacute;fono m&oacute;vil 
	 * en la lista de tel&eacute;fonos m&oacute;viles de entrada.
	 * @param mobileList Listado de tel&eacute;fonos m&oacute;viles.
	 * @param mobile Tel&eacute;fono m&oacute;vil a buscar.
	 * @return N&uacute;mero de veces que aparece el m&oacute;vil en la lista.
	 */
	private int countMobileInList(List<AbstractBaseDTO> mobileList,
			PfMobileUsersDTO mobile) {
		int count = 0;
		for (Iterator<AbstractBaseDTO> iterator = mobileList.iterator(); iterator.hasNext();) {
			PfMobileUsersDTO current = (PfMobileUsersDTO) iterator.next();
			if (current.getDmobile().toUpperCase().equals(
					mobile.getDmobile().toUpperCase())) {
				count++;
			}
		}
		return count;
	}

	/**
	 * M&eacute;todo que realiza una copia de un objeto que relaciona un usuario con un tel&eacute;fono m&oacute;vil.
	 * @param mobile Objeto a ser copiado.
	 * @return Copia del objeto que relaciona usuario y tel&eacute;fono m&oacute;vil.
	 */
	public PfMobileUsersDTO getMobileCopy(PfMobileUsersDTO mobile) {
		PfMobileUsersDTO copy = new PfMobileUsersDTO();
		copy.setCcreated(mobile.getCcreated());
		copy.setCmodified(mobile.getCmodified());
		copy.setDmobile(mobile.getDmobile());
		copy.setFcreated(mobile.getFcreated());
		copy.setFmodified(mobile.getFmodified());
		copy.setLnotify(mobile.getLnotify());
		copy.setPrimaryKey(mobile.getPrimaryKey());
		return copy;
	}

	/**
	 * M&eacute;todo que comprueba que un tel&eacute;fono m&oacute;vil es v&aacute;lido.
	 * @param editMobile Tel&eacute;fono a comprobar.
	 * @return "True" si el tel&eacute;fono es v&aacute;lido, "false" en caso contrario.
	 */
	public boolean chechDMobile(PfMobileUsersDTO editMobile) {
		// @Pattern(regex = "^\\d{1,9}$", message = "{errorMobileNumberFormat}")
		Pattern p = Pattern.compile("^((\\+)?(\\d{2}[ ]))?(\\d{9}){1}?$");
		Matcher m = p.matcher(editMobile.getDmobile());
		return m.find();
	}
}
