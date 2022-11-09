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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUserTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class LabelBO {


	@Autowired
	private BaseDAO baseDAO;

	/**
	 * obtiene de la bbdd la lista de etiquetas para un usuario
	 * @param userDTO usuario del que vamos a obtener las etiquetas
	 * @return lista de etiquetas del usuario
	 */
	public List<AbstractBaseDTO> queryList(PfUsersDTO userDTO) {
		return baseDAO.queryListOneParameter("configuration.userlabelsAll",	"user", userDTO);
	}

	public List<AbstractBaseDTO> queryList(PfUsersDTO userDTO, PfGroupsDTO grupo) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("grupo", grupo);
		parameters.put("user", userDTO);

		return baseDAO.queryListMoreParameters("configuration.userlabelsAllForAGroup", parameters);
	}

	/**
	 * 
	 * @param labelUserList Listado de etiquetas a guardar.
	 * @param labelUserDeleteList Listado de etiquetas a eliminar.
	 * @param userDTO Usuario que a&ntilde;ade y/o elimina las etiquetas.
	 */
	@Transactional(readOnly = false)
	public void saveLabelsList(List<AbstractBaseDTO> labelUserList,
			List<AbstractBaseDTO> labelUserDeleteList, PfUsersDTO userDTO) {
		// insert/update all that is possible
		for (Iterator<AbstractBaseDTO> iterator = labelUserList.iterator(); iterator.hasNext();) {
			PfUserTagsDTO userTag = (PfUserTagsDTO) iterator.next();
			List<AbstractBaseDTO> requestTagList = associatedRequestTags(userDTO, userTag);
			PfTagsDTO checkTag = existsTag(userTag);
			// si la etiqueta a a&ntilde;adir no existe previamente se a&ntilde;ade a la BBDD
			if (checkTag == null || checkTag.getPrimaryKey() == null) {
				PfTagsDTO newTag = new PfTagsDTO();
				newTag.setCtag(userTag.getPfTag().getCtag().toUpperCase());
				newTag.setCtype(Constants.C_TYPE_TAG_USER);
				baseDAO.insertOrUpdate(newTag);
				userTag.setPfTag(newTag);
			} else {
				// else we set the value we just recieved, cause it's is
				// possible that user changed ctag and the previous primarykey
				// it's still here
				userTag.setPfTag(checkTag);
			}
			for (Iterator<AbstractBaseDTO> iterator2 = requestTagList.iterator(); iterator2.hasNext();) {
				PfRequestTagsDTO reqTag = (PfRequestTagsDTO) iterator2.next();
				// we need to update those request tags with the new value
				reqTag.setPfTag(userTag.getPfTag());

			}
			baseDAO.insertOrUpdateList(requestTagList);

		}

		baseDAO.insertOrUpdateList(labelUserList);

		// delete as much as you can
		for (Iterator<AbstractBaseDTO> iterator = labelUserDeleteList
				.iterator(); iterator.hasNext();) {
			PfUserTagsDTO deleteUserTag = (PfUserTagsDTO) iterator.next();
			// delete associated request tag to request
			List<AbstractBaseDTO> deleteRequestTagList = associatedRequestTags(
					userDTO, deleteUserTag);
			baseDAO.deleteList(deleteRequestTagList);
		}
		baseDAO.deleteList(labelUserDeleteList);
	}

	@Transactional(readOnly = false)
	public void insertLabel(PfTagsDTO pfTag, PfUserTagsDTO pfUserTag) {
		// Búsqueda de la etiqueta en BB.DD
		PfTagsDTO pfTagBBDD = searchLabel(pfTag);
		// Si la etiqueta no existe se guarda
		if(pfTagBBDD == null) {
			baseDAO.insertOrUpdate(pfTag);
			pfUserTag.setPfTag(pfTag);
		} else {
			// .. si no, se usa la de BB.DD.
			pfUserTag.setPfTag(pfTagBBDD);
		}		
		// Se guarda la relación etiqueta-usuario
		baseDAO.insertOrUpdate(pfUserTag);
	}

	/**
	 * M&eacute;todo que devuelve todas las relaciones entre la etiqueta y el usuario que se pasan como par&aacute;metro.
	 * @param userDTO Usuario a relacionar con la etiqueta.
	 * @param userTagsDTO Etiqueta a relacionar con el usuario.
	 * @return Listado de relaciones entre el usuario y la etiqueta.
	 */
	public List<AbstractBaseDTO> associatedRequestTags(PfUsersDTO userDTO, PfUserTagsDTO userTagsDTO) {
		if (userTagsDTO.getPfTag() != null &&
				userTagsDTO.getPrimaryKey() != null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("user", userDTO);
			parameters.put("tag", userTagsDTO.getPfTag());
			return baseDAO.queryListMoreParameters(
					"request.requestUserTagAssociated", parameters);
		} else {
			return new ArrayList<AbstractBaseDTO>();
		}
	}

	//	private boolean associatedTagOtherUsers(PfUserTagsDTO userTagsDTO) {
	//
	//		boolean result = false;
	//		Map<String, Object> parameters = new HashMap<String, Object>();
	//
	//		parameters.put("user", userTagsDTO.getPfUser());
	//		parameters.put("tag", userTagsDTO.getPfTag());
	//		Long count = baseDAO.queryCount("request.requestTagAssociated",
	//				parameters);
	//
	//		if (count.intValue() > 0) {
	//			result = true;
	//		}
	//		return result;
	//	}

	/**
	 * M&eacute;todo que si existen usuarios distintos al que se pasa como 
	 * par&aacute;metro que tienen asociada la etiqueta de entrada.
	 * @param userTagsDTO Objeto que relaciona un usuario con una etiqueta
	 * @return Devuelve "true" si existen usuarios con la etiqueta asociada, "false" en caso contrario.
	 */
	//	private boolean associatedTagOtherUser(PfUserTagsDTO userTagsDTO) {
	//
	//		boolean result = false;
	//		Map<String, Object> parameters = new HashMap<String, Object>();
	//
	//		parameters.put("tag", userTagsDTO.getPfTag());
	//		parameters.put("user", userTagsDTO.getPfUser());
	//		Long count = baseDAO.queryCount(
	//				"request.requestTagOtherUserAssociated", parameters);
	//		if (count.intValue() > 0) {
	//			result = true;
	//		}
	//		return result;
	//	}

	/**
	 * M&eacute;todo que comprueba si una etiqueta ya existe.
	 * @param userTagsDTO Etiqueta que se comprueba si existe.
	 * @return Devuelve un objeto etiqueta en caso de que ya exista.
	 */
	public PfTagsDTO existsTag(PfUserTagsDTO userTagsDTO) {
		return (PfTagsDTO) baseDAO.queryElementOneParameter(
				"configuration.label", "ctag", userTagsDTO.getPfTag().getCtag().toUpperCase());
	}

	/**
	 * M&eacute;todo que comprueba si ya existe una etiqueta de un usuario dentro de la lista de etiquetas.
	 * @param labelList Lista de etiquetas.
	 * @param userTagsDTO Etiqueta del usuario en cuesti&oacute;n.
	 * @param indexEditing Posici&oacute;n de la etiqueta en edici&oacute;n en la lista.
	 * @return Devuelve "true" si ya existe la etiqueta en la lista. "False" en caso contrario.
	 */
	public boolean existsLabelForUser(List<AbstractBaseDTO> labelList,
			PfUserTagsDTO userTagsDTO, int indexEditing) {
		boolean result = false;
		List<AbstractBaseDTO> auxList = new ArrayList<AbstractBaseDTO>();
		auxList.addAll(labelList);
		if (indexEditing > -1) {
			// we need to check the value in the list, so we put the new value
			// in a copy of the list
			auxList.set(indexEditing, userTagsDTO);
		}
		for (Iterator<AbstractBaseDTO> iterator = auxList.iterator(); iterator.hasNext();) {
			PfUserTagsDTO current = (PfUserTagsDTO) iterator.next();
			if (current.getPfTag().getCtag().toUpperCase().equals(
					userTagsDTO.getPfTag().getCtag().toUpperCase())) {
				if (indexEditing <= -1
						&& countCTagInList(auxList, userTagsDTO) > 0) {
					result = true;
				} else if (countCTagInList(auxList, userTagsDTO) > 1) {
					result = true;
				}
			}
		}

		return result;
	}

	// public boolean existsLabelForUserAtList(List<AbstractBaseDTO> labelList,
	// PfUserTagsDTO userTag) {
	// // labelList has all labels, labels from DB and session labels
	//
	// for (Iterator<AbstractBaseDTO> iterator = labelList.iterator(); iterator
	// .hasNext();) {
	//
	// PfUserTagsDTO current = (PfUserTagsDTO) iterator.next();
	// // here we check if that element is already in list as a new
	// // element
	// if (current.getPfTag().getCtagCapitalized().equals(
	// userTag.getPfTag().getCtagCapitalized())) {
	// // it is possible that we are still checking the same
	// // element in list
	// if (!current.equals(userTag)) {
	// userTag.getPfTag().setCtag("");
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	// public boolean existsLabelForUserBD(PfUserTagsDTO userTagsDTO, String
	// ctag) {
	// Map<String, Object> parameters = new HashMap<String, Object>();
	// parameters.put("ctag", ctag);
	// parameters.put("pfUser", userTagsDTO.getPfUser());
	// List<AbstractBaseDTO> aux = baseDAO.queryListMoreParameters(
	// "configuration.userlabel", parameters);
	// if (aux != null && aux.size() > 0) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	/**
	 * M&eacute;todo que calcula el n&uacute;mero de veces que el c&oacute;digo de la etiqueta de un usuario
	 * se repite en la lista de etiquetas.
	 * @param labelList Listado de etiquetas.
	 * @param userTagsDTO Objeto que relaciona una etiqueta y un usuario.
	 * @return N&uacute;mero de repeticiones del c&oacute;digo de la etiqueta en la lista.
	 */
	private int countCTagInList(List<AbstractBaseDTO> labelList, PfUserTagsDTO userTagsDTO) {
		int count = 0;
		for (Iterator<AbstractBaseDTO> iterator = labelList.iterator(); iterator.hasNext();) {
			PfUserTagsDTO current = (PfUserTagsDTO) iterator.next();
			if (current.getPfTag().getCtag().toUpperCase().equals(
					userTagsDTO.getPfTag().getCtag().toUpperCase())) {
				count++;
			}
		}
		return count;
	}

	/**
	 * M&eacute;todo que comprueba si ya existe una etiqueta en la lista de etiquetas.
	 * @param labelList Listado de etiquetas.
	 * @param userTagsDTO Objeto que relaciona una etiqueta con un usuario.
	 * @param ctag C&oacute;digo de la etiqueta.
	 * @return Devuelve "true" si la etiqueta existe en el listado. "False" en caso contrario.
	 */
	public boolean existsLabel(List<AbstractBaseDTO> labelList,
			PfUserTagsDTO userTagsDTO, String ctag) {
		boolean ret = false;
		PfUserTagsDTO auxLabel;
		PfTagsDTO tag = (PfTagsDTO) baseDAO.queryElementOneParameter(
				"configuration.label", "ctag", ctag);

		if (tag != null && !tag.getPrimaryKeyString().equals("")) {
			userTagsDTO.setPfTag(tag);
		} else {
			userTagsDTO.getPfTag().setCtag(ctag);
		}

		// we need to compare the tag with our user tags
		if (labelList != null && !labelList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = labelList.iterator();
			while (it.hasNext() && !ret) {
				auxLabel = (PfUserTagsDTO) it.next();
				// Not the same row and different code
				if ((auxLabel.getPfTag().getCtag().equals(
						userTagsDTO.getPfTag().getCtag())
						&& auxLabel.getPfTag().getCtype().equals(
								userTagsDTO.getPfTag().getCtype())
						&& !userTagsDTO.getPfTag().equals(auxLabel.getPfTag())) || 
						(!auxLabel.getPrimaryKeyString().equals(
								userTagsDTO.getPrimaryKeyString()))) {
					ret = true;
				}
			}
		}
		return ret;
	}

	/** Devuelve verdadero si ya existe una etiqueta con el nombre de etiqueta <code>ctag</code>
	 * @param pfTag
	 * @return
	 */
	public PfTagsDTO searchLabel(PfTagsDTO pfTag) {
		return (PfTagsDTO) baseDAO.queryElementOneParameter("configuration.label", "ctag", pfTag.getCtag());
	}

	/**
	 * M&eacute;todo que elimina la etiqueta de la lista de etiquetas y la a&ntilde;ade a la lista de etiquetas a borrar.
	 * @param userTag Etiqueta a borrar.
	 * @param labelList Listado de etiquetas.
	 * @param labelUserDeleteList Listado de etiquetas a borrar.
	 */
	public void addLabelToDelete(PfUserTagsDTO userTag,
			List<AbstractBaseDTO> labelList,
			List<AbstractBaseDTO> labelUserDeleteList) {

		labelUserDeleteList.add(userTag);
		labelList.remove(userTag);
		//
		// // boolean associatedTagOtherUsers =
		// // this.associatedTagOtherUsers(userTag);
		// // if (!associatedTagOtherUsers) {
		// // tagDeleteList.add(userTag.getPfTag());
		// // }
		// if (!isOtherUserLabel(userTag)) {
		// labelDeleteList.add(userTag.getPfTag());
		// }

	}

	/**
	 * M&eacute;todo que convierte una lista de etiquetas en un conjunto (Set) de etiquetas
	 * @param list Lista de etiquetas a convertir en conjunto.
	 * @return Conjunto de etiquetas.
	 */
	public Set<PfUserTagsDTO> userTagList2Set(List<AbstractBaseDTO> list) {
		Set<PfUserTagsDTO> result = new HashSet<PfUserTagsDTO>();
		for (Iterator<AbstractBaseDTO> it = list.iterator(); it.hasNext();) {
			PfUserTagsDTO aux = (PfUserTagsDTO) it.next();
			result.add(aux);
		}
		return result;
	}

	/**
	 * M&eacute;todo que crea una copia de un objeto que relaciona una etiqueta y un usuario.
	 * @param userTag Objeto que relaciona una etiqueta y un usuario a copiar.
	 * @return Relaci&oacute;n de etiqueta y usuario copiada.
	 */
	public PfUserTagsDTO getUserTagCopy(PfUserTagsDTO userTag) {
		PfUserTagsDTO copy = new PfUserTagsDTO();
		copy.setCcolor(userTag.getCcolor());
		copy.setCcreated(userTag.getCcreated());
		copy.setCmodified(userTag.getCmodified());
		copy.setFcreated(userTag.getFcreated());
		copy.setFmodified(userTag.getFmodified());
		copy.setPfTag(getTagCopy(userTag.getPfTag()));
		copy.setPfUser(userTag.getPfUser());
		copy.setPrimaryKey(userTag.getPrimaryKey());
		return copy;
	}

	/**
	 * Método que obtiene una etiqueta de usuario a partir de su PK
	 * @param id Primary key de la etiqueta de usuario
	 * @return Etiqueta de usuario
	 */
	@Transactional(readOnly = true)
	public PfUserTagsDTO queryUserTagById(String id) {
		return (PfUserTagsDTO) baseDAO.queryElementOneParameter("request.requestUserTagByPK", "pk", Long.parseLong(id));
	}

	/**
	 * Método que actualiza una etiqueta de usuario en base de datos
	 * @param userTag Etiqueta de usuario
	 */
	@Transactional(readOnly = false)
	public void insertOrUpdateUserTag(PfUserTagsDTO userTag) {
		baseDAO.insertOrUpdate(userTag);
	}

	/**
	 * @param requestsTags
	 */
	@Transactional(readOnly = false)
	public void deleteRequestsTag(PfUsersDTO pfUser, PfUserTagsDTO userTag) {
		if (userTag != null) {
			baseDAO.delete(userTag);
			baseDAO.deleteList(associatedRequestTags(pfUser, userTag));
		}
	}

	/**
	 * M&eacute;todo que crea una copia de un objeto etiqueta.
	 * @param pfTag Objeto etiqueta a copiar.
	 * @return Copia de la etiqueta.
	 */
	private PfTagsDTO getTagCopy(PfTagsDTO pfTag) {
		PfTagsDTO copy = new PfTagsDTO();
		copy.setCcolor(pfTag.getCcolor());
		copy.setCcreated(pfTag.getCcreated());
		copy.setCmodified(pfTag.getCmodified());
		copy.setCtag(pfTag.getCtag());
		copy.setCtype(pfTag.getCtype());
		copy.setFcreated(pfTag.getFcreated());
		copy.setFmodified(pfTag.getFmodified());
		copy.setPrimaryKey(pfTag.getPrimaryKey());
		return copy;
	}

	//	public boolean isOtherUserLabel(PfUserTagsDTO userTagsDTO) {
	//		Map<String, Object> parameters = new HashMap<String, Object>();
	//		parameters.put("ctag", userTagsDTO.getPfTag().getCtag().toUpperCase());
	//		parameters.put("pfUser", userTagsDTO.getPfUser());
	//		List<AbstractBaseDTO> aux = baseDAO.queryListMoreParameters(
	//				"configuration.otherUserlabel", parameters);
	//		if (aux != null && aux.size() > 0) {
	//			return true;
	//		} else {
	//			return false;
	//		}
	//	}
}
