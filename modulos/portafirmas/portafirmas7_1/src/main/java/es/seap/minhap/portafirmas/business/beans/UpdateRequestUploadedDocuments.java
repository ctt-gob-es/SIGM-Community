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

package es.seap.minhap.portafirmas.business.beans;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class UpdateRequestUploadedDocuments {

	private Integer documentsIndex;
	private HashMap<String, UploadedDocument> documents;
	

	public UpdateRequestUploadedDocuments() {
		this.documentsIndex = 0;
		this.documents = new HashMap<>();
		
	}

	public Integer addDocument(UploadedDocument document) {
		Integer index = this.documentsIndex++;
		this.documents.put(index.toString(), document);
		return index;
	}

	public UploadedDocument getDocumentByIndex(int index) {
		return this.documents.get(String.valueOf(index));
	}

	public Map<String, UploadedDocument> getDocuments() {
		return this.documents;
	}

	public void removeDocument(String key) {
		this.documents.remove(key);
		this.documentsIndex--;
	}
	
	public void removeAlls() {
		this.documents.clear();
		this.documentsIndex = 0;
	}

	public void incDocumentsIndex() {
		this.documentsIndex++;
	}

	public void decDocumentsIndex() {
		this.documentsIndex--;
	}

	public Integer getDocumentsCount() {
		return this.documents.size();
	}

	public String getDocumentString() {
		String result = "";
		StringBuilder sb = new StringBuilder();

		for (Integer i=0; i < this.documentsIndex; i++) {
			UploadedDocument document = this.documents.get(i.toString());

			if (document != null && document.toString().isEmpty()) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(document.getName());
			}
		}
		
		result = sb.toString();

		return result;
	}

}
