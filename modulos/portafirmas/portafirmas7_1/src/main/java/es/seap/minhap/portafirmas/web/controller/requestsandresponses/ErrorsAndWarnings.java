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

package es.seap.minhap.portafirmas.web.controller.requestsandresponses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorsAndWarnings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> errors;
	private List<String> warnings;
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public List<String> getWarnings() {
		return warnings;
	}
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}
	public ErrorsAndWarnings(List<String> errors, List<String> warnings) {
		super();
		this.errors = errors;
		this.warnings = warnings;
	}
	public ErrorsAndWarnings() {
		super();
		errors = new ArrayList<String>();
		warnings = new ArrayList<String>();
	}

}
