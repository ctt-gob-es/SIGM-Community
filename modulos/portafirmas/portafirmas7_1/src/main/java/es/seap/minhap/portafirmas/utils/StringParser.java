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

import java.text.DecimalFormat;

import java.util.StringTokenizer;

public class StringParser {

	private static StringParser instance = null;
	public static final int DEFAULT_SHORT_LENGTH = 55;
	public static final String DOUBLE_PATTERN = "##############################";

	private StringParser() {
	}

	private synchronized static void createInstance() {
		if (instance == null) {
			instance = new StringParser();
		}
	}

	public static StringParser getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private byte StringToByte(String number) {
		return (byte) Integer.parseInt(number.substring(2), 16);
	}

	public byte[] claveAutenticacionStringToByte(String pass) {
		StringTokenizer token = new StringTokenizer(pass, ",");
		int f = token.countTokens();
		byte[] b = new byte[f];
		for (int i = 0; i < f; i++) {
			b[i] = StringToByte(token.nextToken());
		}
		return b;
	}

	public String shortString(String charString, int size) {
		String res = "";
		String sufix = "...";
		if (charString != null && !charString.equals("")
				&& charString.length() >= size) {
			res = charString.substring(0, size - sufix.length()) + sufix;
		} else {
			res = charString;
		}
		return res;
	}

	public String shortString(String charString) {
		return shortString(charString, DEFAULT_SHORT_LENGTH);
	}

	public String firstUpperAndRestLowerCase(String charString) {
		if (charString == null || charString.equals("")) {
			return "";
		}

		String[] charStrings = null;
		String res = "";
		charStrings = charString.split(" ");
		for (int i = 0; i < charStrings.length; i++) {
			if (charStrings[i].length() > 0) {
				res += charStrings[i].substring(0, 1).toUpperCase()
						+ charStrings[i].substring(1, charStrings[i].length())
								.toLowerCase();
				if (i < charStrings.length - 1) {
					res += " ";
				}
			}
		}
		return res;
	}

	public String extractClassName(Class<?> s) {
		if (s != null) {
			String className = s.getName();
			String packageName = s.getPackage().getName();
			if (className != null && !className.equals("")) {
				if (packageName != null && !packageName.equals("")) {
					return className.replaceFirst(packageName + ".", "");
				} else {
					return className;
				}
			} else {
				return null;
			}
		}
		return null;
	}

	public String parseDouble(double d) {
		DecimalFormat f = new DecimalFormat(DOUBLE_PATTERN);
		return f.format(d);
	}

	public String convertStringAscii(String stringToConvert) {

		int currentLocation = 0;
		int locationInString;
		StringBuffer tempSB;

		// find next escape code
		while ((locationInString = stringToConvert.indexOf("&#",
				currentLocation)) != -1) {
			tempSB = new StringBuffer("");
			for (int i = locationInString + 2; Character
					.isDigit(stringToConvert.charAt(i)); ++i) {
				// add all digits to ds
				tempSB.append(stringToConvert.charAt(i));
			}

			// convert to character
			char v = (char) Integer.parseInt(tempSB.toString());

			// append to result string
			// returnThisSB.append(v);
			stringToConvert = stringToConvert.replace("&#" + tempSB + ";", v
					+ "");
			currentLocation = locationInString + 1;
		}
		return stringToConvert;

	}
}
