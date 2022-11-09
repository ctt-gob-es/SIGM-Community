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


public class Paginator {

	// Número de elementos que se muestran por página
	private Integer pageSize;
	
	// Página mostrada de entre todas las que tiene la búsqueda
	private Integer currentPage;
	
	// Número de paginas que tiene la búsqueda
	private Integer numPages;
	
	// Número total de elementos que tiene la búsqueda
	private Integer inboxSize;
	
	// Verdadero si se está posicionado en la primera página
	private boolean isFirst;
	
	// Verdadero si se está posicionado en la última página
	private boolean isLast;
	
	private ArrayList<Integer> pages;

	public Paginator() {
		this.currentPage = 1;
		this.pageSize = 10;
	}

	public Paginator(Integer currentPage) {
		this.pageSize = 10;
		this.currentPage = currentPage;
	}

	public Paginator(Integer inboxSize, Integer pageSize) {
		this.inboxSize = inboxSize;
		this.pageSize = pageSize;
		this.currentPage = 1;
		this.numPages = this.getNumPages();
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getInboxSize() {
		return inboxSize;
	}

	public void setInboxSize(Integer inboxSize) {
		this.inboxSize = inboxSize;
	}

	public boolean isIsFirst() {
		this.isFirst = this.currentPage == 1;
		return this.isFirst;
	}

	public void setIsFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isIsLast() {
		this.isLast = this.currentPage.equals(this.numPages);
		return isLast;
	}

	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}

	public Integer getNumPages() {
		if (inboxSize != null) {
			this.numPages = (int) Math.ceil((double)inboxSize / (double)pageSize);
		} else {
			this.numPages = 0;
		}
		setPages();
		return numPages;
	}

	public void setNumPages(Integer numPages) {
		this.numPages = numPages;
	}

	public ArrayList<Integer> getPages() {
		return pages;
	}

	public void setPages() {
		pages = new ArrayList<Integer>();
		for (int i = 1; i <= numPages; i++) {
			pages.add(i);
		}
	}

}
