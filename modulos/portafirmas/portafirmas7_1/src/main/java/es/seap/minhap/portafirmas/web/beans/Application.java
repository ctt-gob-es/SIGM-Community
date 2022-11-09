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

import java.util.List;

public class Application {
	
	private Long primaryKey;
	private String appCode;
	private String appName;
	private String pfConfiguration;
	private String appParent;
	private String wsUser;
	private String wsLocation;
	private String wsPassword;
	private String wsActiva;
	private String wsNotifIntermedios;
	
	private List<ParameterConfig> paramInfomes;
	private DocumentEni documentEni;
	
	public Application(){
		super();
		documentEni = new DocumentEni();
	}
	
	/*public Application(String appCode, String appName, PfConfigurationsDTO appConf, String appParent2, PfApplicationsParameterDTO wsUser2, PfApplicationsParameterDTO wsLocation2, PfApplicationsParameterDTO wsPassword2, PfApplicationsParameterDTO wsActiva2, PfApplicationsParameterDTO wsNotifIntermedios2)
	{
		super();
		this.code = appCode;
		this.name = appName;
		this.pfConfiguration = appConf;
		this.appParent = appParent2;
		//this.wsActiva = wsActiva2;
		this.wsLocation = wsLocation2;
		//this.wsNotifIntermedios = wsNotifIntermedios2;
		this.wsPassword = wsPassword2;
		this.wsUser = wsUser2;
	}*/

	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppParent() {
		return appParent;
	}

	public void setAppParent(String appParent) {
		this.appParent = appParent;
	}

	public String getPfConfiguration() {
		return pfConfiguration;
	}

	public void setPfConfiguration(String pfConfiguration) {
		this.pfConfiguration = pfConfiguration;
	}

	public String getWsActiva() {
		return wsActiva;
	}

	public void setWsActiva(String wsActiva) {
		this.wsActiva = wsActiva;
	}

	public String getWsNotifIntermedios() {
		return wsNotifIntermedios;
	}

	public void setWsNotifIntermedios(String wsNotifIntermedios) {
		this.wsNotifIntermedios = wsNotifIntermedios;
	}

	public String getWsUser() {
		return wsUser;
	}

	public void setWsUser(String wsUser) {
		this.wsUser = wsUser;
	}

	public String getWsLocation() {
		return wsLocation;
	}

	public void setWsLocation(String wsLocation) {
		this.wsLocation = wsLocation;
	}

	public String getWsPassword() {
		return wsPassword;
	}

	public void setWsPassword(String wsPassword) {
		this.wsPassword = wsPassword;
	}

	public List<ParameterConfig> getParamInfomes() {
		return paramInfomes;
	}

	public void setParamInfomes(List<ParameterConfig> paramInfomes) {
		this.paramInfomes = paramInfomes;
	}

	public DocumentEni getDocumentEni() {
		return documentEni;
	}

	public void setDocumentEni(DocumentEni documentEni) {
		this.documentEni = documentEni;
	}
	
}
