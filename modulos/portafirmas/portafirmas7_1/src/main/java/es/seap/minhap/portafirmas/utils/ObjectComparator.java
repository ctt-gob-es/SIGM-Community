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

import es.seap.minhap.portafirmas.utils.quartz.bean.JobScheduler;

/**
 * Clase comparadora de Jobs
 * @author jesusmlopez
 *
 */
public class ObjectComparator implements Comparator<Object> {

	private int order;
	/**
	 * El par&aacute;metro que pasamos al constructor determina la direcci&oacute;n de la ordenaci&oacute;n
	 * de fechas, si es negativo '-1' ordena de mayor a menor (del m&aacute;s antiguo al m&aacute;s reciente)
	 * y si es positivo '1' ordenar&aacute; de menor a mayor (del m&aacute;s reciente al m&aacute;s antiguo)
	 * @param order la direcci&oacute;n de la ordenaci&oacute;n, admite valores negativos o positivos si el 
	 * valor es cero el metodo de comparaci&oacute;n no funcionar&aacute; bien ya que siempre devolver&aacute; que las
	 * fechas son iguales
	 */
	public ObjectComparator(int order){
		this.order = order;
	}
	/**
	 * Compara si dos Jobs tienen la misma fecha
	 * @return un entero negativo, cero o un entero positivo si el primer argumento es menor, igual a, o mayor que el segundo.
	 * @see java.util.Comparator
	 */
	public int compare(Object o1, Object o2) {
		Date date1 = ((JobScheduler) o1).getDate();
		Date date2 = ((JobScheduler) o2).getDate();
		//La comparaci&oacute;n de fechas el valor 0 si el argumento de fecha es igual a esta fecha, 
		//un valor menor que cero si la fecha es anterior a la Fecha de argumento, 
		//y un valor mayor que 0 si esta fecha es posterior a la Fecha de argumento.
		return date1.compareTo(date2) * order;
	}

}
