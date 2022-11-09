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


public class Message {

	private String primaryKey;
	private String ttext;
	private String dsubject;
	private String fstart;
	private String fexpiration;
	private String scopeType;
	private String userId;
	private String userName;
	private String provinceCode;
	private String userMessagePk;
	
	
	public Message() {
		super();
		ttext = "";
		fstart = null;
		fexpiration = null;
		
	}


	/**
	 * @return the primaryKey
	 */
	public String getPrimaryKey() {
		return primaryKey;
	}


	/**
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}


	/**
	 * @return the ttext
	 */
	public String getTtext() {
		return ttext;
	}


	/**
	 * @param ttext the ttext to set
	 */
	public void setTtext(String ttext) {
		this.ttext = ttext;
	}


	/**
	 * @return the fstart
	 */
	public String getFstart() {
		return fstart;
	}


	/**
	 * @param fstart the fstart to set
	 */
	public void setFstart(String fstart) {
		this.fstart = fstart;
	}


	/**
	 * @return the fexpiration
	 */
	public String getFexpiration() {
		return fexpiration;
	}


	/**
	 * @param fexpiration the fexpiration to set
	 */
	public void setFexpiration(String fexpiration) {
		this.fexpiration = fexpiration;
	}


	/**
	 * @return the dsubject
	 */
	public String getDsubject() {
		return dsubject;
	}


	/**
	 * @param dsubject the dsubject to set
	 */
	public void setDsubject(String dsubject) {
		this.dsubject = dsubject;
	}


	/**
	 * @return the scopeType
	 */
	public String getScopeType() {
		return scopeType;
	}


	/**
	 * @param scopeType the scopeType to set
	 */
	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * @return the provinceCode
	 */
	public String getProvinceCode() {
		return provinceCode;
	}


	/**
	 * @param provinceCode the provinceCode to set
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}


	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}


	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the userMessagePk
	 */
	public String getUserMessagePk() {
		return userMessagePk;
	}


	/**
	 * @param userMessagePk the userMessagePk to set
	 */
	public void setUserMessagePk(String userMessagePk) {
		this.userMessagePk = userMessagePk;
	}



	
	
}
