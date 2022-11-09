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

import es.seap.minhap.portafirmas.domain.PfFiltersDTO;


public class FilterEnvelope {

	private PfFiltersDTO filterDTO;

	public FilterEnvelope() {

	}

	public FilterEnvelope(PfFiltersDTO filter) {
		super();
		if (filter != null) {
			this.setFilterDTO(filter);
		}
	}

	public PfFiltersDTO getFilterDTO() {
		return filterDTO;
	}

	public void setFilterDTO(PfFiltersDTO filter) {
		this.filterDTO = filter;
	}

	@Override
	public boolean equals(Object obj) {

		if (this.getFilterDTO() == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		if (this.getFilterDTO() == null) {
			return false;
		}

		final FilterEnvelope other = (FilterEnvelope) obj;
		if (this.getFilterDTO().getCfilter() != null && !this.getFilterDTO().getCfilter().equals(
				other.getFilterDTO().getCfilter())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.getFilterDTO() == null) ? 0 : this.getFilterDTO()
						.getCfilter().hashCode());
		return result;

	}

}
