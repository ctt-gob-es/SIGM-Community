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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comment {

	private String primaryKey;
	private String userId;
	private String userName;
	private String tcomment;
	private Date fcreated;
	private List<String> users;
	private List<User> signers;
	private String currentUser;
	private String currentGroup;
	
	public Comment() {
		super();
		userId = "";
		userName = "";
		tcomment = "";
		fcreated = null;
		users = new ArrayList<String>();
		signers = new ArrayList<User>();
		currentUser = "";
		currentGroup = "";		
		
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
	 * @return the tcomment
	 */
	public String getTcomment() {
		return tcomment;
	}


	/**
	 * @param tcomment the tcomment to set
	 */
	public void setTcomment(String tcomment) {
		this.tcomment = tcomment;
	}


	/**
	 * @return the fcreated
	 */
	public Date getFcreated() {
		return fcreated;
	}


	/**
	 * @param fcreated the fcreated to set
	 */
	public void setFcreated(Date fcreated) {
		this.fcreated = fcreated;
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
	 * @return the users
	 */
	public List<String> getUsers() {
		return users;
	}


	/**
	 * @param users the users to set
	 */
	public void setUsers(List<String> users) {
		this.users = users;
	}


	/**
	 * @return the signers
	 */
	public List<User> getSigners() {
		return signers;
	}


	/**
	 * @param signers the signers to set
	 */
	public void setSigners(List<User> signers) {
		this.signers = signers;
	}


	/**
	 * @return the currentUser
	 */
	public String getCurrentUser() {
		return currentUser;
	}


	/**
	 * @param currentUser the currentUser to set
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}


	/**
	 * @return the currentGroup
	 */
	public String getCurrentGroup() {
		return currentGroup;
	}


	/**
	 * @param currentGroup the currentGroup to set
	 */
	public void setCurrentGroup(String currentGroup) {
		this.currentGroup = currentGroup;
	}
	
	
}
