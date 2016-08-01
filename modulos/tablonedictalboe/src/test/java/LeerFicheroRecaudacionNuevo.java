import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LeerFicheroRecaudacionNuevo {

	public static void main(String[] args) {
		try {		
			 DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory.newInstance();
			    DocumentBuilder creadorDocumento = fábricaCreadorDocumento.newDocumentBuilder();
			    Document documento = creadorDocumento.parse("C:\\Teresa\\borrar\\EnvioBOE.xml");
			    //Obtener el elemento raíz del documento
			    Element raiz = documento.getDocumentElement();

			    //Obtener la lista de nodos que tienen etiqueta "EMPLEADO"
			    NodeList listaEmpleados = raiz.getElementsByTagName("BOP");
			    //Recorrer la lista de empleados
			    for(int i=0; i<listaEmpleados.getLength(); i++) {   
			        //Obtener de la lista un empleado tras otro
			        Node empleado = listaEmpleados.item(i);
			        System.out.println("Empleado "+i);
			        System.out.println("==========");    

			        //Obtener la lista de los datos que contiene ese empleado
			        NodeList datosEmpleado = empleado.getChildNodes();
			        //Recorrer la lista de los datos que contiene el empleado
			        for(int j=0; j<datosEmpleado.getLength(); j++) {
			            //Obtener de la lista de datos un dato tras otro
			            Node dato = datosEmpleado.item(j);

			            //Comprobar que el dato se trata de un nodo de tipo Element
			            if(dato.getNodeType()==Node.ELEMENT_NODE) {
			                //Mostrar el nombre del tipo de dato
			                System.out.print(dato.getNodeName()+": ");
			                //El valor está contenido en un hijo del nodo Element
			                Node datoContenido = dato.getFirstChild();                        
			                //Mostrar el valor contenido en el nodo que debe ser de tipo Text
			                if(datoContenido!=null && datoContenido.getNodeType()==Node.TEXT_NODE)
			                    System.out.println(datoContenido.getNodeValue());
			            }
			        }
			        //Se deja un salto de línea de separación entre cada empleado
			        System.out.println();
			    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
