package es.dipucr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GeneraFicheros {

	public static void main(String[] args) throws IOException {
		System.out.println("Empezamos.");
//		generaFicheros();
		copiaFicheros();
		System.out.println("Acabamos.");
	}
		
	public static void generaFicheros() throws IOException {		
		String entidad = "";
		for (int i=0; i<=125; i++){
			entidad = String.format("%03d",i);
			
			File carpeta = new File("salida\\" + entidad + "\\SUBSANACION_JUST_REPRESENT");
			carpeta.mkdirs();	
			File fichero = new File ("salida\\" + entidad + "\\SUBSANACION_JUST_REPRESENT\\formulario_vacio.xsl");
		
			FileOutputStream fos = new FileOutputStream(fichero);
			
			fos.write(Texto.dameContenido(entidad).getBytes());
			
			fos.flush();
			fos.close();
		}
	}
	
	public static void copiaFicheros() throws IOException {
		String entidad = "", sCarpetaEntrada = "", sFicheroEntrada = "", sCarpetaDestino = "", sFicheroDestino = "";
		
		sCarpetaEntrada = "entrada";
		sFicheroEntrada = "\\dameRepresentantes.jsp";
		
		File ficheroEntrada = new File (sCarpetaEntrada + sFicheroEntrada);				

		for (int i=0; i<=125; i++){
			FileInputStream fin = new FileInputStream(ficheroEntrada);
			
			entidad = String.format("%03d",i);
			
			sCarpetaDestino = "salida\\" + entidad + "\\SUBSANACION_JUST_REPRESENT";
			sFicheroDestino = "\\dameRepresentantes.jsp";
			
			File carpetaDestino = new File(sCarpetaDestino);
			carpetaDestino.mkdirs();
			File ficheroDestino = new File (sCarpetaDestino + sFicheroDestino);
		
			FileOutputStream fos = new FileOutputStream(ficheroDestino);
			
			byte[] buf = new byte[1024];
			int len;
			
			while ((len = fin.read(buf)) > 0) {
				fos.write(buf, 0, len);
			}
			
			fin.close();			
			fos.flush();
			fos.close();
		}		
	}
}
