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

package es.gob.afirma.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.utils.DefaultErrorHandler;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersException;

/**
 * Clase que agrupa un conjunto de utilidades para el manejo de XML.
 */
/**
 * <p>Xml utility class.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 09/02/2011.
 */
public final class UtilsXML {

    /**
     *  Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(UtilsXML.class);

    /**
     * Attribute that represents a factory API to obtain a parser that produces DOM object trees from XML documents.
     */
    private static DocumentBuilderFactory dbf = null;

    static {
	try {
	    dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
	    dbf.setIgnoringComments(true);
	} catch (Exception e) {}
    }

    /******************************************************************
    			Métodos para genrar árbol DOM a partir de texto
     ******************************************************************/

    /**
     * Constructor method for the class UtilsXML.java.
     */
    private UtilsXML() {
    }

    /**Constant that represents atribute used for delimited xpath.*/
    public static final String PATH_DELIMITER = "/";

    /**
     * Parsea el documento XML pasado como parámetroXML.
     * @param input Instancia de un Reader con el contenido del XML
     * @return Document documento DOM con el XML parseado.
     * @throws TransformersException no realiza ningún controld e excepciones.
     */
    public static Document parsearDocumento(Reader input) throws TransformersException {
	// obtiene el DOM parser y un resolver y document builder
	// asociado
	DOMParser parser = new DOMParser();
	// parsear
	try {
	    parser.parse(new InputSource(input));
	} catch (SAXException e) {
	    throw new TransformersException(e);
	} catch (IOException e) {
	    throw new TransformersException(e);
	}

	// devuelve el documento parseado
	return parser.getDocument();
    }

    /**
     * Importa un elemento al elemento indicado, con todos sus atributos. Duplicando nodos si es necesario
     * @return Element con el elemento modificado
     */
    /*
    public static Element importarElemento(Element el,Element element_to_add)
    {
    	try
    	{
    		String nombre=element_to_add.getTagName();
    		String valor=dameValorEl(element_to_add);
    		// Copiamos los atributos

    		for (h=e.getFirstChild(); h!=null; h=h.getNextSibling() )
    			if (h.getNodeType()!=Node.ATTRIBUTE_NODE)
    				getStringXml(h,sb);

    		NodeList nl=element_to_add.getChildNodes();
    		element_to_add=insertaElementoValorRep(el,nombre,valor);
    		Element el_aux=null;
    		for(int i=0;i<nl.getLength();i++)
    		{
    			if(nl.item(i) instanceof Element)
    			{
    				el_aux=(Element)nl.item(i);
    				importarElemento(element_to_add,el_aux);
    			}
    		}
    		return el;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
     */
    /*******************************************************************
    			Métodos de consulta del árbol DOM
     *******************************************************************/

    /**
     * Devuelve valor de un elemento, o null si no tiene.
     * @param e Elemento DOM del cual se quiere obtener el valor.
     * @return String con el valor del elemento pasado como parámetro o null en el caso de no tener valor.
     */
    public static String dameValorEl(Element e) {
	String result;
	if (e == null || e.getFirstChild() == null) {
	    result = null;
	} else if (e.getFirstChild().getNodeType() == Node.TEXT_NODE || e.getFirstChild().getNodeType() == Node.CDATA_SECTION_NODE) {
	    result = e.getFirstChild().getNodeValue(); // getFirstChild debería
	    // ser el #text#
	} else {
	    result = null;
	}
	return result;
    }

    /**
     * Devuelve el valor de un sub-elemento de un elemento, o null si no lo encuentra.
     * Este método devolverá el valor del elemento sólo si lo encuentra en el primer nivel de
     * la jerarquía especificada.
     * @param e Elemento DOM
     * @param  nombreEl nombre del sub-elemento para el que se pide el valor, puede incluir un path (ej:aplicacion/codigo)
     * @return String con el valor del elemento solicitado o null si no se encuentra.
     */
    public static String dameValorEl(Element e, String nombreEl) {
	String v;
	try {
	    // Separamos por '/' y vamos buscando descendientes
	    StringTokenizer stk = new StringTokenizer(nombreEl, PATH_DELIMITER);
	    Element eAux = e;
	    while (eAux != null && stk.hasMoreElements()) {
		String nombreNodo = (String) stk.nextElement();
		NodeList nl = eAux.getChildNodes();
		int i = 0;
		boolean encontrado = false;
		Node nodo = null;
		Node nodoAux = null;
		while (!encontrado && i < nl.getLength()) {
		    nodoAux = nl.item(i);
		    if (nodoAux.getNodeType() == Node.ELEMENT_NODE && ((Element) nodoAux).getTagName().equals(nombreNodo)) {
			encontrado = true;
			nodo = nodoAux;
		    }
		    i++;
		}
		if (nodo == null) {
		    eAux = null;
		} else {
		    // Next node
		    eAux = (Element) nodo;
		}
	    }
	    v = dameValorEl(eAux);
	} catch (NoSuchElementException ex) {
	    v = null;
	}
	return v;
    } // dameValorEl

    /**
     * Devuelve el valor de un sub-elemento de un elemento, o null si no lo encuentra.
     * @param element element
     * @param  nombreEl nombre del sub-elemento para el que se pide el valor, puede incluir un path (ej:aplicacion/codigo)
     * @return String con el valor del elemento solicitado o null si no se encuentra.
     */
    public static Element dameElemento(Element element, String nombreEl) {
	try {
	    Element e = element;
	    // Separamos por '/' y vamos buscando descendientes
	    StringTokenizer stk = new StringTokenizer(nombreEl, PATH_DELIMITER);
	    while (e != null && stk.hasMoreElements()) {
		NodeList nl = e.getElementsByTagName((String) stk.nextElement());
		if (nl == null || nl.getLength() < 1) {
		    e = null;
		} else {
		    // Next node
		    e = (Element) nl.item(0);
		}
	    }
	    return e;
	} catch (NoSuchElementException ex) {
	    return null;
	}
    }

    /**
     * Busca un elemento hijo con el nombre especificado.
     * @param  e Elemento DOM
     * @param nombreHijo nombre del elemento a buscar. Si es null no hace nada. Permite path (ej:aplicacion/codigo)
     * @return Element del hijo buscado o null si no se encuentra
     */
    public static Element buscaHijo(Element e, String nombreHijo) {
	Element encontrado;
	Element eAux = e;
	try {
	    // Separamos por '/' y vamos buscando descendientes
	    StringTokenizer stk = new StringTokenizer(nombreHijo, PATH_DELIMITER);
	    while (eAux != null && stk.hasMoreElements()) {
		NodeList nl = eAux.getElementsByTagName((String) stk.nextElement());
		if (nl == null || nl.getLength() < 1) {
		    eAux = null;
		} else {
		    // Next node
		    eAux = (Element) nl.item(0);
		}
	    }
	    encontrado = eAux;
	} catch (NoSuchElementException ex) {
	    encontrado = null;
	}
	return encontrado;
    }

    /**
     * Devuelve un vector con la lista de hijos con el nombre especificado. Este metodo busca los nodos hijos
     * que cuelgan del nodo padre, sólamente en el siguiente nivel de profundidad
     * @param e Elemento DOM
     * @param  nombreEl nombre del sub-elemento para el que se piden los valores, puede incluir un path (ej:aplicacion/codigo)
     * @return Vector en el que se incluirán los hijos encontrados
     */
    public static List<Object> buscaListaHijos(Element e, String nombreEl) {
	List<Object> v = null;
	try {
	    // if(v==null) {
	    // v = new ArrayList<Object>();
	    // }
	    v = new ArrayList<Object>();
	    // Vemos si es un elemento terminal buscando una /
	    int posicion = nombreEl.indexOf('/');
	    if (posicion == -1) {
		// Es un nodo terminal
		NodeList nl = e.getChildNodes();
		int i = 0;
		// boolean encontrado = false;
		Node nodo = null;
		while (i < nl.getLength()) {
		    nodo = nl.item(i);
		    if (nodo.getNodeType() == Node.ELEMENT_NODE && ((Element) nodo).getTagName().equals(nombreEl)) {
			v.add(nodo);
		    }
		    i++;
		}
	    } else {
		// No es un nodo terminal
		String nombreNodo = nombreEl.substring(0, posicion);
		NodeList nl = e.getChildNodes();
		int i = 0;
		Node nodo = null;
		// Node nodoAux = null;
		while (i < nl.getLength()) {
		    nodo = nl.item(i);
		    if (nodo.getNodeType() == Node.ELEMENT_NODE && ((Element) nodo).getTagName().equals(nombreNodo)) {
			v.addAll(buscaListaHijos((Element) nodo, nombreEl.substring(posicion + 1)));
		    }
		    i++;
		}
	    }
	} catch (NoSuchElementException ex) {
	    // nada para que no de error;
	}
	return v;
    }

    /**
     * Devuelve un vector con la lista de hijos del elemento especificado.
     * @param e Elemento DOM
     * @return Vector<Element>
     */
    public static List<Element> buscaListaHijos(Element e) {
	List<Element> v = new ArrayList<Element>();
	try {
	    // Es un nodo terminal
	    NodeList nl = e.getChildNodes();
	    int indice = 0;
	    while (indice < nl.getLength()) {
		if (nl.item(indice) instanceof Element) {
		    v.add((Element) nl.item(indice));
		}
		indice++;
	    }
	} catch (NoSuchElementException ex) {
	    // nada para que no de error;
	}
	return v;
    }

    /**
     * Crea un elemento hijo de un elemento dado.
     * @param e elemento al que se debe anyadir un hijo
     * @param nombreHijo nombre del nodo hijo del elemento. Si es null no hace nada
     * @return Element Devuelve el elemento original con el hijo anyadido.
     */
    public static Element ponHijo(Element e, String nombreHijo) {
	Element nuevoE = null;
	if (nombreHijo != null) {
	    nuevoE = e.getOwnerDocument().createElement(nombreHijo);
	    nuevoE = (Element) e.appendChild(nuevoE);
	}
	return nuevoE;
    }

    /**
     * Comprueba si existe un Elemnto con la ruta dada.
     * @param e Elemento DOM (el subarbol sobre el que se buscara)
     * @param nombreElemento nombre del elemento a buscar(Se admiten rutas con '/'). Si es null devuelve false
     * @return boolean indicando si existe o no el elemento
     */
    public static boolean existeElemento(Element e, String nombreElemento) {
	Element elem = e;
	if (nombreElemento == null) {
	    return false;
	}
	if (elem == null) {
	    return false;
	}
	try {
	    // Separamos por '/' y vamos buscando descendientes
	    StringTokenizer stk = new StringTokenizer(nombreElemento, PATH_DELIMITER);
	    while (elem != null && stk.hasMoreElements()) {
		NodeList nl = elem.getElementsByTagName((String) stk.nextElement());
		if (nl == null || nl.getLength() < 1) {
		    return false;
		} else {
		    elem = (Element) nl.item(0);
		}
	    }
	    return elem != null;
	} catch (NoSuchElementException ex) {}
	return false;
    }

    /**
     * Elimina un Element con la ruta dada.
     * @param e Elemento DOM (el subarbol sobre el que se buscara)
     * @param nombreElemento nombre del elemento a eliminar(Se admiten rutas con '/'). Si es null no hace nada
     * @return Elemento con el hijo eliminado. Si no lo encuentra devuelve null
     */
    public static Element eliminaElemento(Element e, String nombreElemento) {
	if (nombreElemento == null) {
	    return null;
	}
	if (e == null) {
	    return null;
	}
	Element removed = null;
	Element eAux = e;
	try {
	    // Separamos por '/' y vamos buscando descendientes
	    StringTokenizer stk = new StringTokenizer(nombreElemento, PATH_DELIMITER);
	    while (eAux != null && stk.hasMoreElements()) {
		NodeList nl = eAux.getElementsByTagName((String) stk.nextElement());
		if (nl == null || nl.getLength() < 1) {
		    return null;
		} else {
		    e = eAux;
		    eAux = (Element) nl.item(0);
		}
	    }
	    removed = (Element) e.removeChild(eAux);
	} catch (NoSuchElementException ex) {
	    removed = null;
	}
	return removed;
    }

    // PIC: Modfi. 30-09-04 Anyado el método para que antes de insertar se borre
    // el elemento, si existe.
    /**
     * Inserta el valor de un Element con la ruta dada y el valor dado, si existe previamente lo borra.
     * @param elemento Elemento DOM (el subarbol sobre el que se trabajara).
     * @param valorElemento valorElemento
     * @param nombreElemento nombre del elemento a insertar(Se admiten rutas con '/'). Si es null no hace nada
     * @return Elemento con el elemento insertado. Si se produce algun problema, devuelve null
     */
    public static Element sustituyeElementoValor(Element elemento, String nombreElemento, String valorElemento) {
	Element elementoInsertado = null;

	if (existeElemento(elemento, nombreElemento)) {
	    eliminaElemento(elemento, nombreElemento);
	}

	elementoInsertado = insertaElementoValor(elemento, nombreElemento, valorElemento);

	return elementoInsertado;
    }

    // FIN PIC

    /**
     * Inserta el valor de un Element con la ruta dada y el valor dado.
     * @param e Elemento DOM (el subarbol sobre el que se trabajara)
     * @param nombreElemento nombre del elemento a insertar(Se admiten rutas con '/'). Si es null no hace nada
     * @param valor valor.
     * @return Elemento con el elemento insertado. Si se produce algun problema, devuelve null
     */
    public static Element insertaElementoValor(Element e, String nombreElemento, String valor) {
	if (GenericUtils.checkNullValues(nombreElemento, e)) {
	    return null;
	}
	Element eAux = e;
	try {
	    // Separamos por '/' y vamos buscando descendientes
	    StringTokenizer stk = new StringTokenizer(nombreElemento, PATH_DELIMITER);
	    while (eAux != null && stk.hasMoreElements()) {
		String name = (String) stk.nextElement();
		NodeList nl = eAux.getElementsByTagName(name);
		if (nl == null || nl.getLength() < 1) {
		    ponHijo(eAux, name);// Creo el hijo si es necesario
		    nl = eAux.getElementsByTagName(name);// Vuelvo a buscar
		    if (nl == null || nl.getLength() < 1) {
			return null;// No se introdujo el elemento por alguna
			// razon
		    }
		    // e=eAux;
		    eAux = (Element) nl.item(0);
		} else {
		    // e=eAux;
		    eAux = (Element) nl.item(0);
		}
	    }
	    if (valor != null) {
		Node tx = eAux.getOwnerDocument().createTextNode(valor);
		eAux.appendChild(tx);
	    }
	    return eAux;
	} catch (NoSuchElementException ex) {}
	return null;
    }

    /**
     * Codifica caracteres en XML.
     * @param c carácter a codificar
     * @return valor del caracter codificado
     */
    static String dameCharXml(char c) {
	switch (c) {
	    case '&':
		return "&amp;";
	    case '<':
		return "&lt;";
	    case '>':
		return "&gt;";
	    case '"':
		return "&quot;";

		// case '\'': return "#39;";
	    default:
		if ((int) c < NumberConstants.INT_32) {
		    return " "; // Con Tamino, eso da error
		} else {
		    return "" + c;
		}
	}
    }

    /*************************************************************/
    /* Métodos para crear un XML desde un objeto y viceversa    */
    /*************************************************************/
    /**
     * Este método genera una cadena con los datos de un objeto en XML.
     * Será heredada por todas las clases de datos, sólo es capaz de mostrar los campos públicos,
     * por lo que las clases de datos deben definir sus campos como públicos.
     * @param objeto instancia que se quiere pasar a XML
     * @param nombreRaiz nombre del elemento raiz del XML resultante.No puede ser nulo
     * @param comoAtributos indica si los valores en el XML se ponen como atributos (true) o como elementos (false)
     * @exception TransformersException Exception
     * @return String.
     */
    public static String toXMLString(Object objeto, String nombreRaiz, boolean comoAtributos) throws TransformersException {
	StringBuffer resultado = new StringBuffer();
	if (objeto != null) {
	    // Comienzo de documento
	    if (comoAtributos) {
		resultado.append("<");
		resultado.append(nombreRaiz);
	    } else {
		resultado.append("<");
		resultado.append(nombreRaiz);
		resultado.append(">");
	    }
	    // Sacamos todos los campos
	    StringBuffer camposNoSimples = extractFields(objeto, resultado, comoAtributos);
	    if (camposNoSimples != null) {
		if (comoAtributos) {
		    resultado.append(">");// Se cierra el padre
		}
		resultado.append(camposNoSimples);// Se incluyen los campos no
		// simples
	    }
	    // Fin de documento
	    if (comoAtributos && camposNoSimples == null) {
		resultado.append(" />");
	    } else {
		resultado.append("</" + nombreRaiz + ">");
	    }
	}
	return resultado.toString();
    }

    /**
     * Extracts fields to convert to xml string.
     * @param objeto instance to transform a xml string
     * @param resultado xml result
     * @param comoAtributos if values are included (true) as attributes or elements (false)
     * @return a list of no simple fields
     * @throws TransformersException in error case.
     */
    private static StringBuffer extractFields(Object objeto, StringBuffer resultado, boolean comoAtributos) throws TransformersException {
	Class<? extends Object> clase = objeto.getClass();
	// Se prepara para formatear números
	DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	dfs.setDecimalSeparator(',');
	dfs.setGroupingSeparator('.');
	DecimalFormat dfImporte = new DecimalFormat("#,##0.00", dfs);
	StringBuffer camposNoSimples = null;
	Object valor = null;
	String nombre = null;
	try {
	    Field[ ] campos = clase.getFields();
	    for (int i = 0; i < campos.length; i++) {
		if (!Modifier.isStatic(campos[i].getModifiers())) {
		    // No se meten los static por ser constantes.
		    valor = campos[i].get(objeto);
		    nombre = campos[i].getName();
		    if (valor != null) {
			if (esCampoSimple(campos[i].getType().getName())) {
			    if (comoAtributos) {
				resultado.append(" " + nombre + "=\"");
			    } else {
				resultado.append("<" + nombre + ">");
			    }
			    resultado.append(valorCampo(objeto, campos[i], valor, dfImporte));
			    if (comoAtributos) {
				resultado.append("\"");
			    } else {
				resultado.append("</" + nombre + ">");
			    }
			} else {
			    if (camposNoSimples == null) {
				camposNoSimples = new StringBuffer();
			    }
			    camposNoSimples.append(toXMLString(valor, nombre, comoAtributos));
			}
		    }
		}
	    }
	} catch (IllegalAccessException e) {
	    throw new TransformersException(e);
	}
	return camposNoSimples;
    }

    /**
     *  Método auxiliar que genera el XML asociado a un atributo.
     * @param objeto objeto que se está pasando a XML
     * @param campo campo que se está tratando
     * @param valor valor del campo
     * @param dfImporte formato para los doubles
     * @return XML asociado al campo
     * @throws TransformersException Exception
     */
    private static String valorCampo(Object objeto, Field campo, Object valor, DecimalFormat dfImporte) throws TransformersException {

	String resultado = "";
	try {
	    if (campo != null && valor != null) {
		String nombreTipo = campo.getType().getName();
		if (nombreTipo.equals("double")) {
		    resultado = dfImporte.format(campo.getDouble(objeto));
		} else {
		    resultado = String.valueOf(valor);
		}
	    }
	} catch (IllegalAccessException e) {
	    throw new TransformersException(e);
	}
	return resultado;
    }

    /**
     * Método auxliar que indica si un campo es de tipo simple o hay que descomponerlo.
     * @param nombreTipo nombre del tipo del campo
     * @return boolean
     */
    private static boolean esCampoSimple(String nombreTipo) {
	if (nombreTipo.equals("double") || nombreTipo.equals("short") || nombreTipo.equals("int") || nombreTipo.equals("long")) {
	    return true;
	} else if (nombreTipo.equals("boolean") || nombreTipo.equals("java.lang.Short") || nombreTipo.equals("java.lang.Integer")) {
	    return true;
	} else if (nombreTipo.equals("java.lang.Double") || nombreTipo.equals("java.lang.String")) {
	    return true;
	}
	return false;

    }

    /*******************************************************************
     Tratamiento de secciones CDATA
     *******************************************************************/

    /*******************************************************************
     Métodos de consulta del árbol DOM
     *******************************************************************/

    /**
     * Transforms DOM tree structure to xml string.
     * @param xmlElement xml element.
     * @param omitXmlDeclaration specifies whether the XSLT processor should output an XML declaration;
     * @return The result of transforming in string format.
     * @throws TransformersException if an error happens.
     */
    public static String transformDOMtoString(Element xmlElement, boolean omitXmlDeclaration) throws TransformersException {
	String res;
	try {
	    StringWriter strWtr = new StringWriter();
	    StreamResult strResult = new StreamResult(strWtr);
	    TransformerFactory tfac = TransformerFactory.newInstance();

	    Transformer trans = tfac.newTransformer();
	    trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXmlDeclaration ? "yes" : "no");
	    trans.transform(new DOMSource(xmlElement), strResult);
	    res = strResult.getWriter().toString();
	} catch (Exception e) {
	    throw new TransformersException(Language.getResIntegra(ILogConstantKeys.UXML_LOG001), e);
	}
	return res;
    }

    /**
     * Transforms DOM tree structure to xml string.
     * @param doc xml document.
     * @return The result of transforming in string format.
     * @throws TransformersException if an error happens.
     */
    public static String transformDOMtoString(Document doc) throws TransformersException {
	return transformDOMtoString(doc.getDocumentElement(), false);
    }

    /**
     * Deletes nodes or tags not used in the xml response. This method finds all 'afirmaNodeType' nodes
     * and deletes them if they are equals than types given.
     * @param xmlNode xml node
     * @param optionalNodeTypes node types to delete
     */
    public static void deleteNodesNotUsed(Element xmlNode, String[ ] optionalNodeTypes) {
	if (xmlNode == null) {
	    return;
	}
	NodeList nodelist = xmlNode.getChildNodes();
	// buscamos todos los elementos hijos de tipo NODO o etiqueta
	for (int index = 0; index < nodelist.getLength(); index++) {
	    Node node = nodelist.item(index);
	    if (Node.ELEMENT_NODE != node.getNodeType()) {
		continue;
	    }
	    boolean eraseNode = false;
	    // buscamos los atributos de cada etiqueta
	    if (node.hasAttributes()) {
		NamedNodeMap nNMap = node.getAttributes();
		for (int i = 0; i < nNMap.getLength(); i++) {
		    Node attribute = nNMap.item(i);
		    // localizamos tipo de atributo para determinar si se borra
		    // el nodo
		    if (isDeletedNode(attribute, optionalNodeTypes)) {
			eraseNode = true;
		    }
		}
	    }
	    if (eraseNode) {
		node.getParentNode().removeChild(node);
	    } else {
		if (node.hasChildNodes()) {
		    deleteNodesNotUsed((Element) node, optionalNodeTypes);
		}
	    }

	}
    }

    /**
     * Checks whether a node is deleted by attribute information.
     * @param attribute attribute.
     * @param optionalNodeTypes types of attributes
     * @return true if node will be deleted or false otherwise.
     */
    private static boolean isDeletedNode(Node attribute, String[ ] optionalNodeTypes) {
	if (Node.ATTRIBUTE_NODE == attribute.getNodeType() && TransformersConstants.ATTR_XML_NODE_TYPE.equals(attribute.getNodeName())) {
	    for (String type: optionalNodeTypes) {
		if (attribute.getNodeValue() != null && attribute.getNodeValue().equals(type)) {
		    return true;
		}
	    }
	    // eliminamos el tipo de atributo @firma innecesario para el xml
	    // resultante
	    // ((Element)
	    // attribute.getParentNode()).removeAttribute(attribute.getNodeName());
	    ((Attr) attribute).getOwnerElement().removeAttribute(attribute.getNodeName());
	}
	return false;
    }

    /**
     * Removes 'afirmaNodeType' attribute.
     * @param element xml node
     */
    public static void removeAfirmaAttribute(Element element) {
	if (element != null && element.hasAttributes()) {
	    NamedNodeMap nNMap = element.getAttributes();
	    for (int i = 0; i < nNMap.getLength(); i++) {
		Node attribute = nNMap.item(i);
		if (Node.ATTRIBUTE_NODE == attribute.getNodeType() && TransformersConstants.ATTR_XML_NODE_TYPE.equals(attribute.getNodeName())) {
		    element.removeAttribute(attribute.getNodeName());
		}
	    }
	}
    }

    /**
     * Inserts the given attribute into a specificated node of xml node.
     * @param xmlNode xml node.
     * @param attributePath path of node.
     * @param value attribute value.
     * @return the xml with introduced attributes
     */
    public static Element insertAttributeValue(Element xmlNode, String attributePath, String value) {
	if (GenericUtils.checkNullValues(xmlNode, attributePath, value) || attributePath.trim().length() == 0 || value.trim().length() == 0) {
	    return null;
	}
	// obtenemos el atributo a buscar.
	String[ ] data = attributePath.split(TransformersConstants.ATTRIBUTE_SEPARATOR);
	if (data.length < 2) {
	    LOGGER.warn(Language.getFormatResIntegra(ILogConstantKeys.UXML_LOG002, new Object[ ] { attributePath, xmlNode.getNodeName() }));
	    return null;
	}
	String nodePath = data[0];
	String attributeName = data[1];

	Element eAux = findNode(nodePath, xmlNode);
	if (eAux != null) {
	    eAux.setAttribute(attributeName, value);
	    removeAfirmaAttribute(eAux);
	}
	return eAux;

    }

    /**
     * Finds a node in a element by xpath address.
     * @param nodePath xpath address
     * @param xmlNode element.
     * @return element found.
     */
    private static Element findNode(String nodePath, Element xmlNode) {
	StringTokenizer stk = new StringTokenizer(nodePath, PATH_DELIMITER);
	Element eAux = xmlNode;
	while (eAux != null && stk.hasMoreElements()) {
	    removeAfirmaAttribute(eAux);
	    String tagName = (String) stk.nextElement();
	    NodeList nl = eAux.getElementsByTagName(tagName);

	    if (nl == null || nl.getLength() < 1) {
		ponHijo(eAux, tagName);// Creo el hijo si es necesario
		nl = eAux.getElementsByTagName(tagName);// Vuelvo a buscar
		if (nl == null || nl.getLength() < 1) {
		    return null;// No se introdujo el elemento por alguna razon
		}
		// xmlNode = eAux;
		eAux = (Element) nl.item(0);

	    } else {
		eAux = (Element) nl.item(0);
	    }
	}
	return eAux;
    }

    /**
     * Insert a value into a element given (a text or a XML document).
     * @param element DOM element.
     * @param elementName element name to insert (it uses paths with '/').
     * 	If this value is null it doesn't anything
     * @param value value (text value or other XML document).
     * @return Element with new node appended (node of type text or node type Element). In error case returns null.
     */
    public static Element insertValueElement(Element element, String elementName, String value) {
	if (GenericUtils.checkNullValues(element, elementName, value)) {
	    return null;
	}
	Element eAux = element;
	// Separamos por '/' y vamos buscando descendientes
	StringTokenizer stk = new StringTokenizer(elementName, PATH_DELIMITER);
	while (eAux != null && stk.hasMoreElements()) {
	    removeAfirmaAttribute(eAux);
	    String name = (String) stk.nextElement();
	    NodeList nl = eAux.getElementsByTagName(name);
	    if (nl == null || nl.getLength() < 1) {
		ponHijo(eAux, name);// Creo el hijo si es necesario
		nl = eAux.getElementsByTagName(name);// Vuelvo a buscar
		if (nl == null || nl.getLength() < 1) {
		    return null;// No se introdujo el elemento por alguna razon
		}
		// elementSrc = eAux;
		eAux = (Element) nl.item(0);
	    } else {
		// elementSrc = eAux;
		eAux = (Element) nl.item(0);
	    }
	}
	if (eAux != null) {
	    Node newNode;
	    // Se comprueba si es un documento XML
	    Document xmlDoc = UtilsXML.parseXMLDocument(value);
	    if (xmlDoc == null) { // Si no es un documento XML, se incluye como
		// nodo-texto.
		newNode = eAux.getOwnerDocument().createTextNode(value);
	    } else { // En caso de ser un documento XML, se incluye como un nodo
		// de tipo Element (un xml dentro de otro).
		newNode = eAux.getOwnerDocument().adoptNode(xmlDoc.getDocumentElement());
	    }
	    eAux.appendChild(newNode);
	    removeAfirmaAttribute(eAux);
	}
	return eAux;
    }

    /**
     * Retrieves value attribute by xpath node.
     * @param element xml element to find node.
     * @param elementPath xpath to find attribute node.
     * @param attributeName attribute name to extract value.
     * @return value attribute.
     */
    public static String getAttributeValue(Element element, String elementPath, String attributeName) {
	String value = null;
	if (element != null && elementPath != null && attributeName != null) {
	    Element tmpElement = dameElemento(element, elementPath);
	    value = tmpElement.getAttribute(attributeName);
	}
	return value;
    }

    /**
     * Retrieves value attribute of a given node.
     * @param element element to find attribute.
     * @param attributeName attribute name to extract value.
     * @return value attribute of a given node.
     */
    public static String getAttributeValue(Element element, String attributeName) {
	return getAttributeValue(element, "", attributeName);
    }

    /**
     * Retrieves relative path of a given node.
     * @param nodeChild node for relative path.
     * @param parent root node.
     * @return relative path of node.
     */
    public static String getRelativeXPath(Node nodeChild, Node parent) {
	String result = null;
	if (nodeChild != null) {
	    result = nodeChild.getNodeName();
	    if (nodeChild.getParentNode() != null) {
		Node auxEl = nodeChild.getParentNode();
		while (auxEl.getParentNode() != null && !auxEl.equals(parent)) {
		    result = auxEl.getNodeName() + PATH_DELIMITER + result;
		    auxEl = auxEl.getParentNode();
		}
	    }
	}
	return result;
    }

    /**
     * Retrieves absolute path of a node.
     * @param node node for relative path.
     * @return absolute path of a node.
     */
    public static String getNodeXPath(Node node) {
	return getRelativeXPath(node, node.getOwnerDocument().getDocumentElement());
    }

    /**
     * Gets first children of type element from a xml node element.
     * @param element xml element.
     * @return first children of type element from a xml node element.
     */
    public static Element getFirstElementNode(Element element) {
	NodeList nl = element.getChildNodes();
	for (int i = 0; 0 < nl.getLength(); i++) {
	    if (Node.ELEMENT_NODE == nl.item(i).getNodeType()) {
		return (Element) nl.item(i);
	    }
	}
	return null;
    }

    /**
     * Transformer that applies a pretty print to a xml string.
     * @param xml xml to tranform
     * @return a pretty xml string.
     * @throws TransformersException if happens a error in the transform process.
     */
    public static String prettyPrintXml(String xml) throws TransformersException {
	String result = null;
	try {
	    // Instanciamos transformer input
	    Source xmlInput = new StreamSource(new StringReader(xml));
	    StreamResult xmlOutput = new StreamResult(new StringWriter());

	    // configuarción transformer
	    Transformer transformer;

	    transformer = TransformerFactory.newInstance().newTransformer();
	    // transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"dtd_definition.dtd");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

	    transformer.transform(xmlInput, xmlOutput);
	    result = xmlOutput.getWriter().toString();
	} catch (TransformerConfigurationException e) {
	    LOGGER.error(e);
	    throw new TransformersException(e);
	} catch (TransformerFactoryConfigurationError e) {
	    LOGGER.error(e);
	    throw new TransformersException(e);
	} catch (TransformerException e) {
	    LOGGER.error(e);
	    throw new TransformersException(e);
	}

	return result;
    }

    /**
     * Method that obtains a {@link Document} object from an input stream.
     * @param is Parameter that represents the input stream.
     * @return the {@link Document} object associated.
     * @throws TransformerException If the method fails.
     */
    public static Document getDocument(InputStream is) throws TransformerException {
	javax.xml.parsers.DocumentBuilder db = null;

	try {
	    db = dbf.newDocumentBuilder();

	    return db.parse(is);
	} catch (Exception e) {
	    throw new TransformerException(e.getMessage(), e);
	}
    }

    /**
     * Method that instancies a new {@link Document} object.
     * @return a new {@link Document} object.
     * @throws ParserConfigurationException If the method fails.
     */
    public static Document newDocument() throws ParserConfigurationException {
	javax.xml.parsers.DocumentBuilder db = null;
	db = dbf.newDocumentBuilder();
	return db.newDocument();
    }

    /**
     * Method that obtains a {@link Document} object from an input stream and validates XML document against an XSD schema.
     * @param xsdSchema xsd schema definition.
     * @param xml xml to convert.
     * @return the {@link Document} object.
     * @throws TransformersException If the process fails.
     */
    public static Document getDocumentWithXsdValidation(File xsdSchema, InputStream xml) throws TransformersException {
	// Constantes para validacion de Schemas
	final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	// Creamos la factoria e indicamos que hay validacion
	DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	documentBuilderFactory.setNamespaceAware(true);
	documentBuilderFactory.setValidating(true);

	try {

	    // Configurando el Schema de validacion
	    documentBuilderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
	    documentBuilderFactory.setAttribute(JAXP_SCHEMA_SOURCE, xsdSchema);

	    // Parseando el documento
	    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    documentBuilder.setErrorHandler(new DefaultErrorHandler());

	    return documentBuilder.parse(xml);

	} catch (SAXException e) {
	    throw new TransformersException(e);
	} catch (IOException e) {
	    throw new TransformersException(e);
	} catch (ParserConfigurationException e) {
	    throw new TransformersException(e);
	}
    }

    /**
     * Parse a XML Document to a {@link Document} instance.
     * @param xml string with XML content.
     * @return a {@link Document document} instance if the string given is a XML document or null otherwise.
     */
    public static Document parseXMLDocument(String xml) {
	Document result = null;
	if (GenericUtils.assertStringValue(xml)) {
	    String xmlWithoutSpaces = xml.trim();
	    if (xmlWithoutSpaces.startsWith("<") && xmlWithoutSpaces.endsWith(">")) {
		try {
		    result = UtilsXML.getDocument(new ByteArrayInputStream(xmlWithoutSpaces.getBytes()));
		} catch (TransformerException e) {}
	    }
	}
	return result;
    }

} // UtilsXML
