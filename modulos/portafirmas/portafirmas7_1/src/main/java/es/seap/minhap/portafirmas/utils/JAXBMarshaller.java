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

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class JAXBMarshaller {

	
	//public static String marshallRootElement (Object o, Class<?> clase) throws JAXBException {
	public static <T> String marshallRootElement (T object, Class<T> clase) throws JAXBException {
		
		StringWriter sw = null;
		
		JAXBContext jc = JAXBContext.newInstance(clase);
		Marshaller marshaller = jc.createMarshaller();
		sw = new StringWriter();
		marshaller.marshal(object, sw);
		return sw.toString();
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T unmarshallRootElement (String s, Class<T> clase) throws JAXBException  {
	
		JAXBContext jc = JAXBContext.newInstance(clase);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		
		T objeto = (T)  unmarshaller.unmarshal(new StreamSource(new StringReader (s)));
		
		if (objeto instanceof JAXBElement<?>) {
			JAXBElement<T> jaxbElement = (JAXBElement<T>) objeto;
			objeto = (T) jaxbElement.getValue();
		}
		
		return objeto;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T unmarshallJAXBElement (String s, Class<T> clase) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(JAXBElement.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		
		JAXBElement<T> e = (JAXBElement<T>) unmarshaller.unmarshal(new StreamSource(new StringReader (s)));
		
		T objeto = (T) e.getValue();
		
		return objeto;
	}
}
