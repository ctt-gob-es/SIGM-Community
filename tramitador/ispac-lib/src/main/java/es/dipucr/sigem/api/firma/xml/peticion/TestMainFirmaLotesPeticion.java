package es.dipucr.sigem.api.firma.xml.peticion;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class TestMainFirmaLotesPeticion {

	public static void main(String[] args) throws JAXBException {

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactoryFirmaLotesPeticion.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Signbatch signbatch = (Signbatch) unmarshaller.unmarshal(new File("lotes_firma.xml"));
						
			List<Signbatch.Singlesign> lista = signbatch.getSinglesign();
			for (Signbatch.Singlesign singlesign : lista) {
				System.out.println(singlesign.getId());
			}
			
			Signbatch.Singlesign singlesign = new Signbatch.Singlesign();
			singlesign.setId("estoesunaprueba");
			lista.add(singlesign);
			
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(signbatch, new File("lotes_firma2.xml"));
	}

}
