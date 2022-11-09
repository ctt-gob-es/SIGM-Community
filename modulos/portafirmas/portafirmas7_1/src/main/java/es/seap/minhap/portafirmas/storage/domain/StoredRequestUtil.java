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

package es.seap.minhap.portafirmas.storage.domain;

public class StoredRequestUtil {

	private static StoredRequestUtil instance;

	/**
	 * Obtiene una instancia del objeto actual.
	 * @return la instancia del objeto actual.
	 */
	public static synchronized StoredRequestUtil getInstance() {
		if (instance == null) {
			instance = new StoredRequestUtil();
		}
		return instance;

	}

	public String getMonthName(String month) {

		String monthName = null;

		if (month != null && !"".equals(month)) {
			if (month.equals("1")) {
				monthName = "Enero";
			} else if (month.equals("2")) {
				monthName = "Febrero";
			} else if (month.equals("3")) {
				monthName = "Marzo";
			} else if (month.equals("4")) {
				monthName = "Abril";
			} else if (month.equals("5")) {
				monthName = "Mayo";
			} else if (month.equals("6")) {
				monthName = "Junio";
			} else if (month.equals("7")) {
				monthName = "Julio";
			} else if (month.equals("8")) {
				monthName = "Agosto";
			} else if (month.equals("9")) {
				monthName = "Septiembre";
			} else if (month.equals("10")) {
				monthName = "Octubre";
			} else if (month.equals("11")) {
				monthName = "Noviembre";
			} else if (month.equals("12")) {
				monthName = "Diciembre";
			}
		}

		return monthName;
	}

}
