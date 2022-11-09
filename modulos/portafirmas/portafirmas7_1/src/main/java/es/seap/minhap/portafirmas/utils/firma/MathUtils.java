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

package es.seap.minhap.portafirmas.utils.firma;
import java.util.Vector;

/**
 * Clase copiada del proyecto eeutil
 * @author rus
 *
 */
/**
 * Ofrece diversas funciones matem&aacute;ticas de ayuda.
 */
public class MathUtils {

	/**
	 * Realiza la operaci&oacute;n XOR sobre arrays de bytes para obtener un array de bytes
	 * &uacute;nico.<br/>
	 * La suma de los arrays dar&aacute; como resultado un array del tama&ntilde;o del array
	 * m&aacute;s largo que se opera. Para operar con los arrays m&aacute;s cortos, se
	 * agregar&aacute;n en sus &uacute;ltimas posiciones rellenas con ceros ('0').
	 * @param values Valores que se desean operar.
	 * @return Array con el resultado del XOR.
	 */
	public static byte[] xorArrays(Vector<byte[]> values) {

		// Calculamos la longitud maxima de los arrays
		int maxLenght = 0;
		for(int i=0; i<values.size(); i++) {
			maxLenght = maxLenght > values.get(i).length ? maxLenght : values.get(i).length; 
		}
		
		// Rellenamos los arrays mas cortos con 0 hasta que sean tan largos como el que mas
		// y los almacenamos todos en un array de arrays de bytes.
		byte[][] blocks = new byte[values.size()][];
		for(int j=0; j<values.size(); j++) {
			byte[] v = values.elementAt(j);
			if(v.length < maxLenght) {
				byte[] newV = new byte[maxLenght];
				for(int i=0; i<v.length; i++)
					newV[i] = v[i];
				for(int i=v.length; i<newV.length; i++) {
					newV[i] = 0;
				}
				blocks[j] = newV;
			} else {
				blocks[j] = v;
			}
		}
		
		// El resultado es igual a la suma de todos los bytes de las distintas cadenas
		byte[] result = new byte[maxLenght];
		for(int i=0; i<result.length; i++) {
			result[i] = 0;
			for(int j=0; j<blocks.length; j++) {
				result[i] ^= blocks[j][i];
			}
		}
		
		return result;
	}
}


