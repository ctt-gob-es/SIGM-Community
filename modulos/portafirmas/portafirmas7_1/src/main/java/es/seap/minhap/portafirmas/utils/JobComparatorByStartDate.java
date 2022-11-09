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

package es.seap.minhap.portafirmas.utils;

import java.util.Comparator;
import java.util.Date;

import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;

public class JobComparatorByStartDate implements Comparator<PfUsersJobDTO> {
	/**
	 * Compara dos jobs por la fecha de inicio de cada job
	 * @return si la fecha del job o1 es posterior a la del 
	 * o2 devuelve 1, si la fecha del job o1 es anterior a la
	 * del o2 devuelve -1, si son iguales devuelve 0 
	 */
	public int compare(PfUsersJobDTO o1, PfUsersJobDTO o2) {
		int ret = 0;
		Date date1 = ((PfUsersJobDTO) o1).getFstart();
		Date date2 = ((PfUsersJobDTO) o2).getFstart();
		if(date1 != null && date1.after(date2)){
			ret = 1;
		}
		else if(date1 != null && date1.before(date2)){
			ret = -1;
		}
		return ret;
	}

}
