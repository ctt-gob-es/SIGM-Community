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

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

public class StaticDesEncrypter {

	private static final Logger log = Logger.getLogger(StaticDesEncrypter.class);

	private static StaticDesEncrypter instance;

	private DesEncrypter crypter;

	public StaticDesEncrypter(String clave) throws Exception {
		byte[] claveEncode = StringParser.getInstance()
				.claveAutenticacionStringToByte(clave);
		DESKeySpec keyspec = new DESKeySpec(claveEncode);
		SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
		SecretKey k = kf.generateSecret(keyspec);
		crypter = new DesEncrypter(k);
	}

	public static StaticDesEncrypter getInstance(String clave) throws Exception {
		if (instance == null) {
			instance = new StaticDesEncrypter(clave);
		}
		return instance;
	}

	public String encode(String s) {
		try {
			return crypter.encrypt(s);
		} catch (Exception e) {
			log
					.error("Error encriptando '" + s + "'. Causa: "
							+ e.getMessage());
		}
		return null;
	}

	public String decode(String s) {
		try {
			return crypter.decrypt(s);
		} catch (Exception e) {
			log.error("Error desencriptando '" + s + "'. Causa: "
					+ e.getMessage());
		}
		return null;
	}

	/*
	 * public static void main(String[] args) { try{ String cadena="15001.0";
	 * String encode=StaticDesEncrypter.getInstance().encode(cadena); String
	 * decode=StaticDesEncrypter.getInstance().decode(encode);
	 * System.out.println(cadena); System.out.println(encode);
	 * System.out.println(decode); } catch (Exception e){
	 * System.out.println(e.getMessage()); } }
	 */

}
