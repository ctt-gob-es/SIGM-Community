package test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;

import aww.sigem.expropiaciones.catastro.entidades.CargaInicial;
import aww.sigem.expropiaciones.catastro.entidades.Finca;
import aww.sigem.expropiaciones.catastro.parser.ExprParser;

public class Prueba {

	public static void main(String[] args) {
		try {
			ExprParser SAXHandler = new ExprParser();

			SAXParser parser = new SAXParser();
			parser.setContentHandler(SAXHandler);
			parser.setErrorHandler(SAXHandler);
			InputStream is = new BufferedInputStream(new FileInputStream("CatastroMala.XML"));
			InputSource source = new InputSource(is);
			parser.parse(source);
			List<Finca> lista = SAXHandler.getListaFincas();
			List<CargaInicial> error = SAXHandler.getErrores();
			System.out.println("lista.size " + lista.size());
			if (lista != null) {
				Finca finca = new Finca();
				for (int i = 0; i < lista.size(); i++) {
					finca = (Finca) lista.get(i);
					System.out.println("Municipio " + finca.getMunicipio());
					System.out.println("Cod_mun " + finca.getCod_mun());
				}

			}

		} catch (Exception ex) {
			System.out.println(ex);
		}

	}

}
