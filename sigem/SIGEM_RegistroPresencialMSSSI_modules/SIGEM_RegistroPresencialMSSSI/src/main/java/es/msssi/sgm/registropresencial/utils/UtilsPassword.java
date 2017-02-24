package es.msssi.sgm.registropresencial.utils;

import java.security.MessageDigest;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public final class UtilsPassword {

    // ~ Static fields/initializers
    // ---------------------------------------------

    public static final int USER_IS_NOT_LOCKED = 0;
    public static final int USER_IS_LOCKED = 1;
    public static final int RESETED_NUM_CNTS = 0;
    public static final String PWD_MUST_BE_CHANGED = "Y";
    public static final String PWD_MUST_NOT_BE_CHANGED = "N";
    public static final double PWD_VP_INDF = -1;
    public static final int PWD_MAX_LENGTH = 32;

    private static final String PWD_TO_CRYPT = "IUSER";

    // ~ Constructors
    // -----------------------------------------------------------

    private UtilsPassword() {
    }

    // ~ Methods
    // ----------------------------------------------------------------

    public static String encryptPassword(
	String decPwd, int userId)
	throws Exception {

	String encPwd = null;
	String passwordToGenKey = PWD_TO_CRYPT +
	    userId;

	encPwd = encryptPassword(
	    decPwd, passwordToGenKey);

	return encPwd;

    }

    public static String decryptPassword(
	String pwd, int userId)
	throws Exception {

	String decPwd = null;
	String passwordToGenKey = PWD_TO_CRYPT +
	    userId;

	decPwd = decryptPassword(
	    pwd, passwordToGenKey);

	return decPwd;

    }

    public static boolean validatePassword(
	int userId, String decPwd, String encPwd)
	throws Exception {

	boolean valid = false;
	String encPwd1;

	encPwd1 = encryptPassword(
	    decPwd, userId);
	if (encPwd1.equals(encPwd))
	    valid = true;

	return valid;

    }

    public static int getVpDays(
	double vp) {

	return (int) (vp / 24);

    }

    public static double getVp(
	int d) {

	return (double) d * 24;

    }

    public static String encryptPassword(
	String decPwd, String passwordToGenKey)
	throws Exception {

	String encPwd = null;

	// Obtener el texto que va a dar el hash inicial
	byte buf[] = passwordToGenKey.getBytes("UTF-8");

	Security.addProvider(new BouncyCastleProvider());

	// Obtener el hash
	MessageDigest md = MessageDigest.getInstance(
	    "MD5", "BC");

	md.update(buf);
	byte hash[] = md.digest();

	// Aunque no lo diga en ninguna parte, el cryptoapi usa una clave de 128
	// bits con todo ceros
	// menos los 5 primeros bytes para cifrar....
	byte newHash[] =
	    { (byte) hash[0], (byte) hash[1], (byte) hash[2], (byte) hash[3], (byte) hash[4],
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

	// Obtener la key basada en ese hash
	SecretKeySpec key = new SecretKeySpec(
	    newHash, "RC4");

	// Obtener el objeto Cipher e inicializarlo con la key
	Cipher cipher = Cipher.getInstance(
	    "RC4", "BC");
	cipher.init(
	    Cipher.ENCRYPT_MODE, key);

	// Obtener los datos a cifrar
	byte bufPlain[] = decPwd.getBytes();

	// cifrar
	byte bufCipher[] = cipher.doFinal(bufPlain);

	encPwd = encode(bufCipher);

	return encPwd;

    }

    public static String decryptPassword(
	String encPwd, String passwordToGenKey)
	throws Exception {

	String decPwd = null;

	// Obtener el texto que va a dar el hash inicial
	byte buf[] = passwordToGenKey.getBytes("UTF-8");

	Security.addProvider(new BouncyCastleProvider());

	// Obtener el hash
	MessageDigest md = MessageDigest.getInstance(
	    "MD5", "BC");

	md.update(buf);
	byte hash[] = md.digest();

	// Aunque no lo diga en ninguna parte, el cryptoapi usa una clave de 128
	// bits con todo ceros
	// menos los 5 primeros bytes para cifrar....
	byte newHash[] =
	    { (byte) hash[0], (byte) hash[1], (byte) hash[2], (byte) hash[3], (byte) hash[4],
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

	// Obtener la key basada en ese hash
	SecretKeySpec key = new SecretKeySpec(
	    newHash, "RC4");

	// Obtener el objeto Cipher e inicializarlo con la key
	Cipher cipher = Cipher.getInstance(
	    "RC4", "BC");
	cipher.init(
	    Cipher.DECRYPT_MODE, key);

	// Obtener los datos a descifrar
	// La contraseña viene codificada en base64
	byte bufPlain[] = decode(encPwd);

	// descifrar
	byte bufCipher[] = cipher.doFinal(bufPlain);

	decPwd = new String(
	    bufCipher, "UTF-8");

	return decPwd;

    }
    public static byte[] decode(String encData) throws Exception
    {
       
       byte[]        decData;
       BASE64Decoder decoder;
       
       decoder = new BASE64Decoder();      
       decData = decoder.decodeBuffer(encData);
       
       return decData;
       
    }
    
    public static String encode(byte[] decData)
    {
       
       String        encData;
       BASE64Encoder encoder;
       
       encoder = new BASE64Encoder();      
       encData = encoder.encode(decData);
       
       return encData;
       
    }
}
