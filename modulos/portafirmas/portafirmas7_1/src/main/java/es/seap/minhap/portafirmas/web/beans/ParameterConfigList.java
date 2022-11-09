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

package es.seap.minhap.portafirmas.web.beans;

public class ParameterConfigList {

	private String configurationPk;
	private String [] arrayChecked;
	private String [] arrayId;
	private String [] arrayName;
	private String [] arrayValue;
	
	public String getConfigurationPk() {
		return configurationPk;
	}
	public void setConfigurationPk(String configurationPk) {
		this.configurationPk = configurationPk;
	}
	public String[] getArrayChecked() {
		return arrayChecked;
	}
	public void setArrayChecked(String[] arrayChecked) {
		this.arrayChecked = arrayChecked;
	}
	public String[] getArrayId() {
		return arrayId;
	}
	public void setArrayId(String[] arrayId) {
		this.arrayId = arrayId;
	}
	public String[] getArrayName() {
		return arrayName;
	}
	public void setArrayName(String[] arrayName) {
		this.arrayName = arrayName;
	}
	public String[] getArrayValue() {
		return arrayValue;
	}
	public void setArrayValue(String[] arrayValue) {
		this.arrayValue = arrayValue;
	}
	
}
