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

 Autor: Junta de Andaluc&iacute;a

 Derechos de explotaci&oacute;n propiedad de la Junta de Andaluc&iacute;a.

 Éste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los t&eacute;rminos de la Licencia EUPL European Public License publicada
 por el organismo IDABC de la Comisi&oacute;n Europea, en su versi&oacute;n 1.0. o posteriores.

 Éste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÍA, incluso sin las presuntas garant&iacute;as impl&iacute;citas de USABILIDAD o ADECUACIÓN A PROPÓSITO
 CONCRETO. Para mas informaci&oacute;n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por alg&uacute;n motivo no le es posible visualizarla, puede
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reçu une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

package es.seap.minhap.portafirmas.business.administration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class DocumentTypeAdmBO {

	@Autowired
	private BaseDAO baseDAO;

	/**
	 * Recupera la lista de tipos de documento donde la Clave ajena a PF_APLICACIONES es nula
	 * @return lista de tipos de documento
	 */
	public List<AbstractBaseDTO> queryList() {
		return baseDAO.queryListMoreParameters(
				"administration.documentTypeAdmAll", null);
	}
	/**
	 * Actualiza la lista de tipos de documento que pasamos como par&aacute;metro
	 * y borra la lista de tipos de documento a borrar que pasamos como par&aacute;metro
	 * en bbdd.
	 * @param documentTypeList lista de tipos de documento a actualizar
	 * @param documentTypeDeleteList lista de tipos de documento a borrar
	 */
	@Transactional(readOnly = false)
	public void saveDocumentTypeList(List<AbstractBaseDTO> documentTypeList,
			List<AbstractBaseDTO> documentTypeDeleteList) {
		for (Iterator<AbstractBaseDTO> iterator = documentTypeList.iterator(); iterator
				.hasNext();) {
			PfDocumentTypesDTO documentType = (PfDocumentTypesDTO) iterator
					.next();
			documentType.setCdocumentType(documentType.getCdocumentType()
					.toUpperCase());
		}
		baseDAO.updateList(documentTypeList);
		baseDAO.deleteList(documentTypeDeleteList);
	}
	/**
	 * Verifica si el tipo de documento pasado como par&aacute;matro est&aacute; asociado
	 * a alg&uacute;n documento o no
	 * @param baseDTO el tipo de documento
	 * @return true si el tipo de documento est&aacute; asociado a un documento,
	 * false en caso contrario
	 */
	public boolean checkDocumentAssociated(AbstractBaseDTO baseDTO) {
		boolean ret = false;
		if (baseDTO != null && baseDTO.getPrimaryKey() != null) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("docType", baseDTO);
			Long queryResult = baseDAO.queryCount(
					"administration.documentTypeAdmAssociated", queryParams);
			if (queryResult != null && queryResult.longValue() > 0) {
				ret = true;
			}
		}
		return ret;
	}
	/**
	 * Verifica si el tipo de documento existe en la lista que pasamos como par&aacute;metro
	 * @param documentTypeList la lista de tipos de documento
	 * @param documentType el tipo de documento
	 * @param editingIndex el indice de la lista que estamos editando
	 * @return true si el documento existe en la lista, false en caso contrario
	 */
	public boolean existsCDocumentType(List<AbstractBaseDTO> documentTypeList,
			PfDocumentTypesDTO documentType, int editingIndex) {
		boolean ret = false;
		PfDocumentTypesDTO auxDocType;
		if (documentTypeList != null && !documentTypeList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = documentTypeList.iterator();
			while (it.hasNext() && ret == false) {
				auxDocType = (PfDocumentTypesDTO) it.next();
				// Not the same row and different code
				if (auxDocType != null
						&& auxDocType.getCdocumentType() != null
						&& auxDocType.getCdocumentType().toUpperCase().equals(
								documentType.getCdocumentType().toUpperCase())
						&& !documentType.getPrimaryKeyString().equals(
								auxDocType.getPrimaryKeyString())) {
					ret = true;
				}
				if (auxDocType != null
						&& auxDocType.getCdocumentType() != null
						&& auxDocType.getCdocumentType().toUpperCase().equals(
								documentType.getCdocumentType().toUpperCase())
						&& auxDocType.getPrimaryKey() == null
						&& documentType.getPrimaryKey() == null
						&& !auxDocType.equals(documentType)) {
					if (editingIndex <= -1
							&& countCDocumentTypeInList(documentTypeList,
									documentType) > 0) {
						ret = true;
					} else if (countCDocumentTypeInList(documentTypeList,
							documentType) > 1) {
						ret = true;
					}
				}
			}
		}
		return ret;
	}
	/**
	 * Devuelve el n&uacute;mero de veces que aparece el tipo de documento en la lista
	 * @param documentTypeList lista de tipos de documento
	 * @param documentType el tipo de documento
	 * @return el n&uacute;mero de veces que aparece el tipo de documento en la lista, devuelve
	 * cero en caso de que no aparezca
	 */
	private int countCDocumentTypeInList(
			List<AbstractBaseDTO> documentTypeList,
			PfDocumentTypesDTO documentType) {
		int count = 0;
		for (Iterator<AbstractBaseDTO> iterator = documentTypeList.iterator(); iterator
				.hasNext();) {
			PfDocumentTypesDTO doc = (PfDocumentTypesDTO) iterator.next();
			if (doc.getCdocumentType() != null
					&& doc.getCdocumentType().toUpperCase().equals(
							documentType.getCdocumentType().toUpperCase())) {
				count++;
			}
		}
		return count;
	}
	/**
	 * Recupera una copia del objeto que pasamos como par&aacute;metro
	 * @param documentType el objeto de tipo de documento
	 * @return la copia del objeto
	 */
	public PfDocumentTypesDTO getCopyDocumentType(
			PfDocumentTypesDTO documentType) {
		PfDocumentTypesDTO copy = new PfDocumentTypesDTO();
		copy.setPrimaryKey(documentType.getPrimaryKey());
		copy.setCcreated(documentType.getCcreated());
		copy.setFcreated(documentType.getFcreated());
		copy.setCmodified(documentType.getCmodified());
		copy.setFmodified(documentType.getFmodified());
		copy.setCdocumentType(documentType.getCdocumentType());
		copy.setDdocumentType(documentType.getDdocumentType());
		copy.setLvalid(documentType.getLvalid());
		return copy;
	}

}
