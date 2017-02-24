/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.json.simple.JSONValue;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase de gestion de qr.
 * 
 * @author cmorenog
 * 
 */
public class QRCodeUtils {
    private static final Logger LOG = Logger.getLogger(QRCodeUtils.class.getName());
    public static int SIZE = 29;
    public static String FILETYPE = "png";

    /**
     * Genera un codigo QR con los parámetros de entrada. El texto que genera es
     * un json.
     * 
     * @return
     */
    public byte[] genetareQRCode(Map<String, Object> param) {
	byte[] result = null;
	String textCode = generateJSon(param);
	try {
	    Hashtable<EncodeHintType, Object> hintMap = new Hashtable<EncodeHintType, Object>();
	    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	    hintMap.put(EncodeHintType.MARGIN, 0);
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix byteMatrix =
		    qrCodeWriter.encode(textCode, BarcodeFormat.QR_CODE, SIZE, SIZE, hintMap);
	    int CrunchifyWidth = byteMatrix.getWidth();
	    BufferedImage image =
		    new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
	   image.createGraphics();

	    Graphics2D graphics = (Graphics2D) image.getGraphics();
	    graphics.setColor(Color.WHITE);
	    graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
	    graphics.setColor(Color.BLACK);

	    for (int i = 0; i < CrunchifyWidth; i++) {
		for (int j = 0; j < CrunchifyWidth; j++) {
		    if (byteMatrix.get(i, j)) {
			graphics.fillRect(i, j, 1, 1);
		    }
		}
	    }
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(image, FILETYPE, baos);

	    baos.flush();
	    result = baos.toByteArray();
	    baos.close();
	}
	catch (WriterException writerException) {
	    LOG.error(ErrorConstants.CREATE_QRCODE_ERROR_MESSAGE, writerException);
	}
	catch (IOException iOException) {
	    LOG.error(ErrorConstants.CREATE_QRCODE_ERROR_MESSAGE, iOException);
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    private String generateJSon(Map<String, Object> param) {
	String result = "";
	Map<String, Object> obj = new LinkedHashMap<String, Object>();
	obj.put("AP", "SIGM");
	obj.put("NR", param.get("NUMERO_REGISTRO"));
	/*obj.put("Fecha Registro", param.get("FECHA_REGISTRO"));
	JSONArray list = new JSONArray();
	list.add(param.get("CODIGOOFICINA"));
	list.add(param.get("OFICINA"));*/
	obj.put("OF", param.get("CODIGOOFICINA"));
	obj.put("TP", 1);
	obj.put("LR", new Integer((Integer) param.get("LIBRO")));
	result = JSONValue.toJSONString(obj);
	return result;
    }

}
