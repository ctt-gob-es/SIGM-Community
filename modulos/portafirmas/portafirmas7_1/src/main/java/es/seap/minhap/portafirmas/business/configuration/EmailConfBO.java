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
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersEmailDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class EmailConfBO {


	@Autowired
	private BaseDAO baseDAO;

	/**
	 * Recupera la lista de emails del usuario que pasamos como par&aacute;metro
	 * @param userDTO el usuario
	 * @return la lista de emails del usuario
	 */
	public List<AbstractBaseDTO> queryList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.emailUser", "user",	userDTO);
	}

	/**
	 * M&eacute;todo que crea un nuevo objeto email, el cual se asocia con el usuario de entrada y se a&ntilde;ade a su lista de emails.
	 * @param emailList Listado emails del usuario.
	 * @param userDTO Usuario para el que se crea el nuevo email.
	 */
	public void addEmail(List<AbstractBaseDTO> emailList, PfUsersDTO userDTO) {
		AbstractBaseDTO email = new PfUsersEmailDTO();
		((PfUsersEmailDTO) email).setPfUser(userDTO);
		emailList.add(0, email);
	}

	/**
	 * M&eacute;todo que guarda/elimina en BBDD los correos electr&oacute;nicos definidios en dos listas para a&ntilde;adir/borrar.
	 * @param emailList Listado de correos electr&oacute;nicos a a&ntilde;adir a la BD.
	 * @param emailDeleteList Listado de correos a eliminar de la BD.
	 */
	@Transactional(readOnly = false)
	public void saveEmailList(List<AbstractBaseDTO> emailList,
			List<AbstractBaseDTO> emailDeleteList) {
		baseDAO.updateList(emailList);
		baseDAO.deleteList(emailDeleteList);
	}

	/**
	 * M&eacute;todo que comprueba que el correo electr&oacute;nico a a&ntilde;adir no est&aacute; ya definido en la lista de ese usuario.
	 * @param emailList Lista de direcciones del usuario.
	 * @param email Correo electr&oacute;nico a a&ntilde;adir.
	 * @return "True" si el correo ya existe, "false" en caso contrario.
	 */
	public boolean existsEmail(List<AbstractBaseDTO> emailList, PfUsersEmailDTO email) {
		boolean existe = false;
		PfUsersEmailDTO auxEmail;
		if (emailList != null && !emailList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = emailList.iterator();
			while (!existe && it.hasNext()) {
				auxEmail = (PfUsersEmailDTO) it.next();
				// Not the same row and different code
				if (auxEmail.getDemail().equals(email.getDemail())) {
					existe = true;
				}
			}
		}
		return existe;
	}

	/**
	 * M&eacute;todo que calcula el n&uacute;mero de veces que aparece una direcci&oacute;n de correo dentro de 
	 * la lista de correos de entrada.
	 * @param emailList Listado de correos.
	 * @param email Direcci&oacute;n de correo a buscar.
	 * @return Devuelve el n&uacute;mero de veces que aparece la direcci&oacute;n en la lista.
	 */
//	private int countEmailInList(List<AbstractBaseDTO> emailList, PfUsersEmailDTO email) {
//		int count = 0;
//		for (Iterator<AbstractBaseDTO> iterator = emailList.iterator(); iterator.hasNext();) {
//			PfUsersEmailDTO current = (PfUsersEmailDTO) iterator.next();
//			if (current.getDemail() != null &&
//				current.getDemail().equals(email.getDemail())) {
//				count++;
//			}
//		}
//		return count;
//	}

	/**
	 * M&eacute;todo que crea una copia de un objeto que relaciona un usuario 
	 * con una direcci&oacute;n de correo electr&oacute;nico.
	 * @param email Objeto que relaciona un usuario con una direcci&oacute;n 
	 * de correo electr&oacute;nico a copiar.
	 * @return Copia del objeto que relaciona usuario con email.
	 */
	public PfUsersEmailDTO getCopyOfEmail(PfUsersEmailDTO email) {
		PfUsersEmailDTO copy = new PfUsersEmailDTO();
		copy.setCcreated(email.getCcreated());
		copy.setCmodified(email.getCmodified());
		copy.setDemail(email.getDemail());
		copy.setFcreated(email.getFcreated());
		copy.setFmodified(email.getFmodified());
		copy.setLnotify(email.getLnotify());
		copy.setPrimaryKey(email.getPrimaryKey());
		return copy;
	}

	/**
	 * M&eacute;todo que comprueba que una direcci&oacute;n de correo es v&aacute;lida.
	 * @param emailString Texto de la direcci&oacute;n de correo.
	 * @return "True" si la direcci&oacute;n es v&aacute;lida, "false" en caso contrario.
	 */
	public boolean validEmailAddress(String emailString) {
		Pattern p = Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");
		Matcher m = p.matcher(emailString);
		return m.find();

	}

	/**
	 * Método que guarda una dirección de email
	 * @param pfUsersEmail
	 */
	@Transactional(readOnly = false)
	public void saveEmail(PfUsersEmailDTO pfUsersEmail) {
		baseDAO.insertOrUpdate(pfUsersEmail);
	}

	@Transactional(readOnly = false)
	public void deleteEmail(PfUsersEmailDTO pfUsersEmail) {
		baseDAO.delete(pfUsersEmail);
	}

	/**
	 * Método para validar un email
	 * @param pfUsersEmail
	 * @param msgError
	 * @return
	 */
	public String validEmail(PfUsersEmailDTO pfUsersEmail) {
		String msgError = null;
		if(!validEmailAddress(pfUsersEmail.getDemail())) {
			msgError = "La dirección de correo electrónico no es válida";
		}
		List<AbstractBaseDTO> emailsUserList = queryList(pfUsersEmail.getPfUser());
		if(existsEmail(emailsUserList, pfUsersEmail)) {
			msgError = "La dirección de correo electrónico ya existe.";
		}
		return msgError;
	}

	/**
	 * @param primaryKey
	 * @return
	 */
	@Transactional(readOnly = false)
	public PfUsersEmailDTO queryUsersEmailById(String primaryKey) {
		return (PfUsersEmailDTO) baseDAO.queryElementOneParameter(
				"configuration.usersEmailByPK", "pk", Long.parseLong(primaryKey));
	}

}
