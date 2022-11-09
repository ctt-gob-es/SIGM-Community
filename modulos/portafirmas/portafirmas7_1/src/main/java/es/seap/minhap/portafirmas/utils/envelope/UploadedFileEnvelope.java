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

package es.seap.minhap.portafirmas.utils.envelope;

import java.io.File;
import java.io.Serializable;

import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;

public class UploadedFileEnvelope implements Serializable {

	private static final long serialVersionUID = 1L;

	private File file;
	private PfDocumentsDTO document;
	int index;

	public UploadedFileEnvelope() {
		
	}

	public UploadedFileEnvelope(File file, PfDocumentsDTO document, int index) {
		this.file = file;
		this.document = document;
		this.index = index;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public PfDocumentsDTO getDocument() {
		return document;
	}

	public void setDocument(PfDocumentsDTO document) {
		this.document = document;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
