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

package es.seap.minhap.portafirmas.business.beans.binarydocuments;

public class BinaryDocument {
	
	protected String mime;
	protected String name;
	
	protected AbstractContent content;
	
	public BinaryDocument (String name, String mime, AbstractContent content) {
		this.name = name;
		this.mime = mime;
		this.content = content;
	}
	 
	public BinaryDocument() {
		// TODO Auto-generated constructor stub
	}

	public String getMime() {
		return mime;
	}
	public void setMime(String mime) {
		this.mime = mime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AbstractContent getContent() {
		return content;
	}
	public void setContent(AbstractContent content) {
		this.content = content;
	}
	
}
