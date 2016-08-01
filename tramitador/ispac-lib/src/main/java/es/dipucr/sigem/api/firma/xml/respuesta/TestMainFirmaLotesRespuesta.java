package es.dipucr.sigem.api.firma.xml.respuesta;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;



public class TestMainFirmaLotesRespuesta {

	public static void main(String[] args) throws JAXBException {

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactoryFirmaLotesRespuesta.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Signs signs = (Signs) unmarshaller.unmarshal(new File("lotes_firma_respuesta.xml"));
			
			List<Signs.Signresult> lista = signs.getSignresult();
			for (Signs.Signresult singlesign : lista ) {
				System.out.println(singlesign.getId());
			}
			
			Signs.Signresult singleresult = new Signs.Signresult();
			singleresult.setId("estoesunaprueba");
			lista.add(singleresult);
			
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(signs, new File("lotes_firma_respuesta2.xml"));
			
	}

}
