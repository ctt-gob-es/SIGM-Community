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

import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import java.util.ArrayList;
import java.util.List;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;


public class UsersParameters {

	private PfUsersDTO user;
	private Paginator paginator;
	private List<String> type;
	private String searchText;
	private String orderField;
	private String order;
	
	public UsersParameters() {
		super();
		paginator = new Paginator();
		searchText = "";
		orderField = "2";
		order = "asc";
		type = new ArrayList<String>();
	}

	public PfUsersDTO getUser() {
		return user;
	}

	public void setUser(PfUsersDTO user) {
		this.user = user;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
		this.type = type;
	}

}