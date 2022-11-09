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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

public class DesEncrypter {

	private Cipher ecipher;
	private Cipher dcipher;

	public DesEncrypter(SecretKey key) throws Exception {
		ecipher = Cipher.getInstance("DES");
		dcipher = Cipher.getInstance("DES");
		ecipher.init(Cipher.ENCRYPT_MODE, key);
		dcipher.init(Cipher.DECRYPT_MODE, key);
	}

	public String encrypt(String str) throws Exception {
		byte[] utf8 = str.getBytes("UTF8");
		byte[] enc = ecipher.doFinal(utf8);
		return new String(Base64.encodeBase64(enc), "UTF8");
	}

	public String decrypt(String str) throws Exception {
		byte[] dec = Base64.decodeBase64(str.getBytes());
		byte[] utf8 = dcipher.doFinal(dec);
		return new String(utf8, "UTF8");
	}
}
