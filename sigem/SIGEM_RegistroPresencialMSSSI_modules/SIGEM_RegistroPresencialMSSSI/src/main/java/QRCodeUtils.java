// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 07/11/2016 16:57:51
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   QRCodeUtils.java

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;

public class QRCodeUtils
{

    public static int SIZE = 100;
    public static String FILETYPE = "png";
    public QRCodeUtils()
    {
    }

    public static byte[] genetareQRCode()
    {
        byte result[] = null;
        String textCode = generateJSon();
        try
        {
            Hashtable hintMap = new Hashtable();
           hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.MARGIN, 0);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(textCode, BarcodeFormat.QR_CODE, SIZE, SIZE, hintMap);
            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
           /* image.createGraphics();
            Graphics2D graphics = (Graphics2D)image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
            graphics.setColor(Color.BLACK);
            for(int i = 0; i < CrunchifyWidth; i++)
            {
                for(int j = 0; j < CrunchifyWidth; j++)
                    if(byteMatrixbyteMatrix.get(i, j))
                        graphics.fillRect(i, j, 1, 1);

            }*/
            int white = 255 << 16 | 255 << 8 | 255;
            int black = 0;
              
                for (int i = 0; i < CrunchifyWidth; i++) {
                    for (int j = 0; j < CrunchifyWidth; j++) {
                        image.setRGB(i, j, byteMatrix.get(i, j) ? black : white); // set pixel one by one
                    }
                }
         
                try {
                    ImageIO.write(image, "jpg", new File("c:/dynamsoftbarcode.jpg")); // save QR image to disk
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
         
          
           /* File myFile = new File("C:/imagen.png");
            ImageIO.write(image, FILETYPE, myFile);*/
        }
        catch(WriterException writerException)
        {
    
      
        }
        return result;
    }

    public static void createQRCode(String qrCodeData, String filePath, String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
        throws WriterException, IOException
    {
        BitMatrix matrix = (new MultiFormatWriter()).encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), new File(filePath));
    }

    private static String generateJSon()
    {
        String result = "";
        Map obj = new LinkedHashMap();
        obj.put("AP", "SIGM");
        obj.put("NR", "201502100000001");
        obj.put("OF.", "021");
        obj.put("TP", Integer.valueOf(1));
        obj.put("LR", Integer.valueOf(1));
        result = JSONValue.toJSONString(obj);
        return result;
    }

    public static void main(String args[])
    {
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hintMap.put(EncodeHintType.MARGIN, Integer.valueOf(0));
       
            genetareQRCode();
       
    }

   

}