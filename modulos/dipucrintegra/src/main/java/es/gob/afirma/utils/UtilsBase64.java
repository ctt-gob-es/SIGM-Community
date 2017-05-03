// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/*
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-,2011 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.utils.UtilsBase64.java.</p>
 * <b>Description:</b><p>Class that provides methods for manipulating Base64 objects.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>22/03/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 22/03/2011.
 */
package es.gob.afirma.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;

/**
 * <p>Class that provides methods for manipulating Base64 objects.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 22/03/2011.
 */
public class UtilsBase64 {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(UtilsBase64.class);
	
	

	/**
	 * <p>Class that extends the {@link FilterOutputStream} class to add more functionality.</p>
	 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
	 * certificates and electronic signature.</p>
	 * @version 1.0, 01/03/2011.
	 */
	public class OutputStream extends FilterOutputStream {

		/**
		 * {@inheritDoc}
		 * @see java.io.FilterOutputStream#write(int)
		 */
		public void write(int i) throws IOException {
			buffer[position++] = (byte) i;
			if (position >= bufferLength) {
				if (encode) {
					super.out.write(encode3to4(buffer, bufferLength));
					lineLength += NumberConstants.INT_4;
					if (lineLength >= NumberConstants.INT_76) {
						super.out.write(NumberConstants.INT_10);
						lineLength = 0;
					}
				} else {
					super.out.write(decode4to3(buffer));
				}
				position = 0;
			}
		}

		/**
		 * {@inheritDoc}
		 * @see java.io.FilterOutputStream#write(byte[], int, int)
		 */
		public void write(byte abyte0[], int i, int j) throws IOException {
			for (int k = 0; k < j; k++) {
				write(abyte0[i + k]);
			}

		}

		/**
		 * {@inheritDoc}
		 * @see java.io.FilterOutputStream#flush()
		 */
		public void flush() throws IOException {
			if (position > 0) {
				if (encode) {
					super.out.write(encode3to4(buffer, position));
				} else {
					throw new IOException();
				}
			}
			super.flush();
			super.out.flush();
		}

		/**
		 * {@inheritDoc}
		 * @see java.io.FilterOutputStream#close()
		 */
		public void close() throws IOException {
			flush();
			super.close();
			super.out.close();
			buffer = null;
			super.out = null;
		}

		/**
		 * Attribute that indicates whether the object is encoded on Base64 (true) or not (false).
		 */
		private boolean encode;

		/**
		 * Attribute that represents the position of the offset in the data.
		 */
		private int position;

		/**
		 * Attribute that represents the data.
		 */
		private byte buffer[];

		/**
		 * Attribute that represents the length of the data.
		 */
		private int bufferLength;

		/**
		 * Attribute that represents the length of the line.
		 */
		private int lineLength;

		/**
		 * Constructor method for the internal class OutputStream.java.
		 * @param outputstream Parameter that represents the output stream of bytes.
		 */
		public OutputStream(java.io.OutputStream outputstream) {
			this(outputstream, true);
		}

		/**
		 * Constructor method for the internal class OutputStream.java.
		 * @param outputstream Parameter that represents the output stream of bytes.
		 * @param flag Parameter that indicates whether the object is encoded on Base64 (true) or not (false).
		 */
		public OutputStream(java.io.OutputStream outputstream, boolean flag) {
			super(outputstream);
			encode = flag;
			bufferLength = flag ? NumberConstants.INT_3 : NumberConstants.INT_4;
			buffer = new byte[bufferLength];
			position = 0;
			lineLength = 0;
		}
	}

	/**
	 * <p>Class that extends the {@link FilterInputStream} class to add more functionality.</p>
	 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
	 * certificates and electronic signature.</p>
	 * @version 1.0, 01/03/2011.
	 */
	public class InputStream extends FilterInputStream {

		/**
		 * {@inheritDoc}
		 * @see java.io.FilterInputStream#read()
		 */
		public int read() throws IOException {
			if (position < 0) {
				if (encode) {
					byte abyte0[] = new byte[NumberConstants.INT_3];
					numSigBytes = 0;
					for (int i = 0; i < NumberConstants.INT_3; i++) {
						try {
							int k = super.in.read();
							if (k >= 0) {
								abyte0[i] = (byte) k;
								numSigBytes++;
							}
						} catch (IOException ioexception) {
							if (i == 0) {
								throw ioexception;
							}
						}
					}

					if (numSigBytes > 0) {
						encode3to4(abyte0, 0, numSigBytes, buffer, 0);
						position = 0;
					}
				} else {
					byte abyte1[] = new byte[NumberConstants.INT_4];
					int j = 0;
					for (j = 0; j < NumberConstants.INT_4; j++) {
						int l = 0;
						do {
							l = super.in.read();
						}
						while (l >= 0 && UtilsBase64.DECODABET[l & NumberConstants.HEX_0X7F] < NumberConstants.INT_MINUS_5);
						if (l < 0) {
							break;
						}
						abyte1[j] = (byte) l;
					}

					if (j == NumberConstants.INT_4) {
						numSigBytes = decode4to3(abyte1, 0, buffer, 0);
						position = 0;
					}
				}
			}
			if (position >= 0) {
				if (position >= numSigBytes) {
					return -1;
				}
				byte byte0 = buffer[position++];
				if (position >= bufferLength) {
					position = -1;
				}
				return byte0;
			} else {
				return -1;
			}
		}

		/**
		 * {@inheritDoc}
		 * @see java.io.FilterInputStream#read(byte[], int, int)
		 */
		public int read(byte abyte0[], int i, int j) throws IOException {
			int k;
			for (k = 0; k < j; k++) {
				int l = read();
				if (l < 0) {
					return -1;
				}
				abyte0[i + k] = (byte) l;
			}

			return k;
		}

		/**
		 * Attribute that indicates whether the object is encoded on Base64 (true) or not (false).
		 */
		private boolean encode;

		/**
		 * Attribute that represents the position of the offset in the data.
		 */
		private int position;

		/**
		 * Attribute that represents the data.
		 */
		private byte buffer[];

		/**
		 * Attribute that represents the length of the data.
		 */
		private int bufferLength;

		/**
		 * Attribute that represents the number of the next bytes.
		 */
		private int numSigBytes;

		/**
		 * Constructor method for the internal class InputStream.java.
		 * @param inputstream Parameter that represents the input stream of bytes.
		 */
		public InputStream(java.io.InputStream inputstream) {
			this(inputstream, false);
		}

		/**
		 * Constructor method for the internal class InputStream.java.
		 * @param inputstream Parameter that represents the input stream of bytes.
		 * @param flag Parameter that indicates whether the object is encoded on Base64 (true) or not (false).
		 */
		public InputStream(java.io.InputStream inputstream, boolean flag) {
			super(inputstream);
			encode = flag;
			bufferLength = flag ? NumberConstants.INT_4 : NumberConstants.INT_3;
			buffer = new byte[bufferLength];
			position = -1;
		}
	}

	/**
	 * Constructor method for the class UtilsBase64.java.
	 */
	public UtilsBase64() {
	}

	/**
	 * TODO: javadoc.
	 * @param abyte0 TODO: javadoc.
	 * @param i TODO: javadoc.
	 * @return TODO: javadoc.
	 */
	private static byte[ ] encode3to4(byte abyte0[], int i) {
		byte abyte1[] = new byte[NumberConstants.INT_4];
		encode3to4(abyte0, 0, i, abyte1, 0);
		return abyte1;
	}

	/**
	 * TODO: javadoc.
	 * @param abyte0 TODO: javadoc.
	 * @param i TODO: javadoc.
	 * @param j TODO: javadoc.
	 * @param abyte1 TODO: javadoc.
	 * @param k TODO: javadoc.
	 * @return TODO: javadoc.
	 */
	private static byte[ ] encode3to4(byte abyte0[], int i, int j, byte abyte1[], int k) {
		int l = (j <= 0 ? 0 : abyte0[i] << NumberConstants.INT_24 >>> NumberConstants.INT_8) | (j <= 1 ? 0 : abyte0[i + 1] << NumberConstants.INT_24 >>> NumberConstants.INT_16) | (j <= 2 ? 0 : abyte0[i + 2] << NumberConstants.INT_24 >>> NumberConstants.INT_24);
		switch (j) {
			case NumberConstants.INT_3: // '\003'
				abyte1[k] = ALPHABET[l >>> NumberConstants.INT_18];
				abyte1[k + 1] = ALPHABET[l >>> NumberConstants.INT_12 & NumberConstants.HEX_0X3F];
				abyte1[k + 2] = ALPHABET[l >>> NumberConstants.INT_6 & NumberConstants.HEX_0X3F];
				abyte1[k + NumberConstants.INT_3] = ALPHABET[l & NumberConstants.HEX_0X3F];
				return abyte1;

			case 2: // '\002'
				abyte1[k] = ALPHABET[l >>> NumberConstants.INT_18];
				abyte1[k + 1] = ALPHABET[l >>> NumberConstants.INT_12 & NumberConstants.HEX_0X3F];
				abyte1[k + 2] = ALPHABET[l >>> NumberConstants.INT_6 & NumberConstants.HEX_0X3F];
				abyte1[k + NumberConstants.INT_3] = NumberConstants.INT_61;
				return abyte1;

			case 1: // '\001'
				abyte1[k] = ALPHABET[l >>> NumberConstants.INT_18];
				abyte1[k + 1] = ALPHABET[l >>> NumberConstants.INT_12 & NumberConstants.HEX_0X3F];
				abyte1[k + 2] = NumberConstants.INT_61;
				abyte1[k + NumberConstants.INT_3] = NumberConstants.INT_61;
				return abyte1;
		}
		return abyte1;
	}

	/**
	 * Method that encodes a {@link Serializable} object.
	 * @param serializable Parameter that represents the object to encode.
	 * @return a string that represents the encoded object.
	 */
	public static String encodeObject(Serializable serializable) {
		ByteArrayOutputStream bytearrayoutputstream = null;
		OutputStream outputstream = null;
		ObjectOutputStream objectoutputstream = null;
		try {
			bytearrayoutputstream = new ByteArrayOutputStream();
			outputstream = new UtilsBase64().new OutputStream(bytearrayoutputstream, true);
			objectoutputstream = new ObjectOutputStream(outputstream);
			objectoutputstream.writeObject(serializable);
		} catch (IOException ioexception) {
			LOGGER.error(ioexception.getMessage(), ioexception);
			return null;
		} finally {
			// Cerramos recursos
			UtilsResources.safeCloseOutputStream(objectoutputstream);
			UtilsResources.safeCloseOutputStream(outputstream);
			UtilsResources.safeCloseOutputStream(bytearrayoutputstream);
		}
		return new String(bytearrayoutputstream.toByteArray());
	}

	/**
	 * Method that encodes a bytes array.
	 * @param abyte0 Parameter that represents the object to encode.
	 * @return a string that represents the encoded object.
	 */
	public static String encodeBytes(byte abyte0[]) {
		return encodeBytes(abyte0, 0, abyte0.length);
	}

	/**
	 * Method that encodes a bytes array.
	 * @param abyte0 Parameter that represents the object to encode.
	 * @param i Parameter that represents the initial position in the bytes array.
	 * @param j Parameter that represents the final position in the bytes array.
	 * @return a string that represents the encoded object.
	 */
	public static String encodeBytes(byte abyte0[], int i, int j) {
		int k = j * NumberConstants.INT_4 / NumberConstants.INT_3;
		byte abyte1[] = new byte[k + (j % NumberConstants.INT_3 <= 0 ? 0 : NumberConstants.INT_4) + k / NumberConstants.INT_76];
		int l = 0;
		int i1 = 0;
		int j1 = j - 2;
		int k1 = 0;
		while (l < j1) {
			encode3to4(abyte0, l, NumberConstants.INT_3, abyte1, i1);
			k1 += NumberConstants.INT_4;
			if (k1 == NumberConstants.INT_76) {
				abyte1[i1 + NumberConstants.INT_4] = NumberConstants.INT_10;
				i1++;
				k1 = 0;
			}
			l += NumberConstants.INT_3;
			i1 += NumberConstants.INT_4;
		}
		if (l < j) {
			encode3to4(abyte0, l, j - l, abyte1, i1);
			i1 += NumberConstants.INT_4;
		}
		return new String(abyte1, 0, i1);
	}

	/**
	 * Method that encodes a string.
	 * @param s Parameter that represents the object to encode.
	 * @return a string that represents the encoded object.
	 */
	public static String encodeString(String s) {
		return encodeBytes(s.getBytes());
	}

	/**
	 * TODO: javadoc.
	 * @param abyte0 TODO: javadoc.
	 * @return TODO: javadoc.
	 */
	private static byte[ ] decode4to3(byte abyte0[]) {
		byte abyte1[] = new byte[NumberConstants.INT_3];
		int i = decode4to3(abyte0, 0, abyte1, 0);
		byte abyte2[] = new byte[i];
		System.arraycopy(abyte1, 0, abyte2, 0, i);
		return abyte2;
	}

	/**
	 * TODO: javadoc.
	 * @param abyte0 TODO: javadoc.
	 * @param i TODO: javadoc.
	 * @param abyte1 TODO: javadoc.
	 * @param j TODO: javadoc.
	 * @return TODO: javadoc.
	 */
	private static int decode4to3(byte abyte0[], int i, byte abyte1[], int j) {
		if (abyte0[i + 2] == NumberConstants.INT_61) {
			int k = DECODABET[abyte0[i]] << NumberConstants.INT_24 >>> NumberConstants.INT_6 | DECODABET[abyte0[i + 1]] << NumberConstants.INT_24 >>> NumberConstants.INT_12;
			abyte1[j] = (byte) (k >>> NumberConstants.INT_16);
			return 1;
		}
		if (abyte0[i + NumberConstants.INT_3] == NumberConstants.INT_61) {
			int l = DECODABET[abyte0[i]] << NumberConstants.INT_24 >>> NumberConstants.INT_6 | DECODABET[abyte0[i + 1]] << NumberConstants.INT_24 >>> NumberConstants.INT_12 | DECODABET[abyte0[i + 2]] << NumberConstants.INT_24 >>> NumberConstants.INT_18;
			abyte1[j] = (byte) (l >>> NumberConstants.INT_16);
			abyte1[j + 1] = (byte) (l >>> NumberConstants.INT_8);
			return 2;
		} else {
			int i1 = DECODABET[abyte0[i]] << NumberConstants.INT_24 >>> NumberConstants.INT_6 | DECODABET[abyte0[i + 1]] << NumberConstants.INT_24 >>> NumberConstants.INT_12 | DECODABET[abyte0[i + 2]] << NumberConstants.INT_24 >>> NumberConstants.INT_18 | DECODABET[abyte0[i + NumberConstants.INT_3]] << NumberConstants.INT_24 >>> NumberConstants.INT_24;
			abyte1[j] = (byte) (i1 >> NumberConstants.INT_16);
			abyte1[j + 1] = (byte) (i1 >> NumberConstants.INT_8);
			abyte1[j + 2] = (byte) i1;
			return NumberConstants.INT_3;
		}
	}

	/**
	 * Method that decodes a string.
	 * @param s Parameter that represents the object to decode.
	 * @return a bytes array that represents the decoded object.
	 */
	public static byte[ ] decode(String s) {

		String sParam = s;

		byte[ ] result = null;
		sParam = sParam.replaceAll("\\r\\n", "");
		sParam = sParam.replaceAll("\\n", "");
		byte abyte0[] = sParam.getBytes();

		if (abyte0.length % NumberConstants.INT_4 == 0) {

			result = decode(abyte0, 0, abyte0.length);

		} else {
			LOGGER.error(Language.getResIntegra(ILogConstantKeys.UB_LOG002));

		}

		return result;

	}

	/**
	 * Method that decodes a string.
	 * @param s Parameter that represents the object to decode.
	 * @return a string that represents the decoded object.
	 */
	public static String decodeToString(String s) {
		return new String(decode(s));
	}

	/**
	 * Method that decodes a string.
	 * @param s Parameter that represents the string to decode.
	 * @return an object that represents the decoded string.
	 */
	public static Object decodeToObject(String s) {
		byte abyte0[] = decode(s);
		ByteArrayInputStream bytearrayinputstream = null;
		ObjectInputStream objectinputstream = null;
		try {
			try {
				bytearrayinputstream = new ByteArrayInputStream(abyte0);
				objectinputstream = new ObjectInputStream(bytearrayinputstream);
				return objectinputstream.readObject();
			} catch (IOException ioexception) {
				LOGGER.error(ioexception.getMessage(), ioexception);
				return null;
			} catch (ClassNotFoundException classnotfoundexception) {
				LOGGER.error(classnotfoundexception.getMessage(), classnotfoundexception);
			}
			return null;
		} finally {
			// Cerramos recursos
			UtilsResources.safeCloseInputStream(bytearrayinputstream);
			// Cerramos recursos
			UtilsResources.safeCloseInputStream(objectinputstream);
		}
	}

	/**
	 * Method that decodes a bytes array.
	 * @param abyte0 Parameter that represents the object to decode.
	 * @param i Parameter that represents the initial position in the bytes array.
	 * @param j Parameter that represents the final position in the bytes array.
	 * @return the decoded bytes array.
	 */
	public static byte[ ] decode(byte abyte0[], int i, int j) {
		int k = j * NumberConstants.INT_3 / NumberConstants.INT_4;
		byte abyte1[] = new byte[k];
		int l = 0;
		byte abyte2[] = new byte[NumberConstants.INT_4];
		int i1 = 0;

		for (int j1 = 0; j1 < j; j1++) {
			byte byte0 = (byte) (abyte0[j1] & NumberConstants.HEX_0X7F);
			byte byte1 = DECODABET[byte0];
			if (byte1 >= NumberConstants.INT_MINUS_5) {
				if (byte1 < -1) {
					continue;
				}
				abyte2[i1++] = byte0;
				if (i1 <= NumberConstants.INT_3) {
					continue;
				}
				l += decode4to3(abyte2, 0, abyte1, l);
				i1 = 0;
				if (byte0 == NumberConstants.INT_61) {
					break;
				}
			} else {
				LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.UB_LOG001, new Object[ ] { "" + j1, "" + abyte0[j1] }) + abyte0[j1] + "(decimal)");
				return null;
			}
		}

		byte abyte3[] = new byte[l];
		System.arraycopy(abyte1, 0, abyte3, 0, l);
		return abyte3;
	}

	/**
	 * Constant attribute that represents the boolean to identify the <i>ENCODE</i> mode.
	 */
	public static final boolean ENCODE = true;

	/**
	 * Constant attribute that represents the boolean to identify the <i>DECODE</i> mode.
	 */
	public static final boolean DECODE = false;

	/**
	 * Constant attribute that represents the number to identify the max length for a line.
	 */
	public static final int MAX_LINE_LENGTH = 76;

	/**
	 * Constant attribute that represents the byte to identify the equal sign.
	 */
	public static final byte EQUALS_SIGN = NumberConstants.INT_61;

	/**
	 * Constant attribute that represents the byte to identify the line break.
	 */
	public static final byte NEW_LINE = 10;

	/**
	 * Constant attribute that represents the array bytes to identify the encoded alphabet.
	 */
	private static final byte ALPHABET[] = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };

	/**
	 * Constant attribute that represents the array bytes to identify the decoded alphabet.
	 */
	private static final byte DECODABET[] = { -9, -9, -9, -9, -9, -9, -9, -9, -9, NumberConstants.INT_MINUS_5, NumberConstants.INT_MINUS_5, -9, -9, NumberConstants.INT_MINUS_5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, NumberConstants.INT_MINUS_5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, NumberConstants.INT_61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, NumberConstants.INT_3, NumberConstants.INT_4, 5, NumberConstants.INT_6, 7, NumberConstants.INT_8, 9, 10, 11, NumberConstants.INT_12, 13, 14, 15, NumberConstants.INT_16, 17, NumberConstants.INT_18, 19, 20, 21, 22, 23, NumberConstants.INT_24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };

	/**
	 * Constant attribute that represents the byte to identify the bad encoding.
	 */
	public static final byte BAD_ENCODING = -9;

	/**
	 * Constant attribute that represents the byte to identify the white space encoded.
	 */
	public static final byte WHITE_SPACE_ENC = NumberConstants.INT_MINUS_5;

	/**
	 * Constant attribute that represents the byte to identify the equal sign encoded.
	 */
	public static final byte EQUALS_SIGN_ENC = -1;
}
