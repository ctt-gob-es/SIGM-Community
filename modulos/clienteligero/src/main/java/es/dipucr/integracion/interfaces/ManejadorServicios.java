package es.dipucr.integracion.interfaces;

import org.w3c.dom.Element;



/**
 * Define aquellos métodos comunes que todos las clases van a tener  que utilizar para el procesamiento del servicio Web.
 * Principalmente se encarga de asignar correctamente los namespaces correctos, crear el elemento que viajará en el nodo
 * <DatosEspecificos> y obtener los datos relevantes de este elemento.
 */
public interface ManejadorServicios {

	
	/**
	 * @return texto correspondiente al namespace usado en la petición. 
	 * Este método solo tiene sentido cuando se usa el cliente del Recubrimiento ya que el cliente
	 * de las librerías SCSP asigna los namespaces. Es decir, para el recubrimiento se usará este:
	 * 
	 * http://intermediacion.redsara.es/scsp/esquemas/ws/peticion
	 */
	public String getNameSpaceRequest();
	
	
	/**
	 * @return texto correspondiente al namespace usado en las respuestas dadas por los servicios.
	 * Lo normal es que retorne:
	 * Servicios V2: "http://www.map.es/scsp/esquemas/V2/respuesta";
	 * Servicios V3: "http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta";
	 * 
	 * Si algún servicio usa otros namespaces, sobreescribiendo este método en su clase podremos
	 * adaptarnos. Se usa para asignar los namespaces para luego buscar elementos por Xpath. 
	 * Hay varias maneras de obtener los datos: recorriendo los nodos, mediante Xpath, etc..
	 */
	public String getNameSpaceResponse();
	
	
	/**
	 * @return texto correspondiente al namespace usado por el servicio para los datos específicos.
	 * Lo normal es que retorne:
	 * Servicios V2: "http://www.map.es/scsp/esquemas/datosespecificos";
	 * Servicios V3: "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos";
	 */
	public String getNameSpaceDatosEspecificos();
	
	
	/**
	 * @return texto correspondiente al namespace usado en el nodo ConfirmacionPeticion 
	 * de los servicos asíncronos.
	 * 
	 */
	public String getNameSpaceConfirmationRequest();

	
	/**
	 * @return texto correspondiente al namespace usado para una petición de este tipo.
	 * 
	 * Este método solo tiene sentido cuando se usa el cliente del Recubrimiento ya que el cliente
	 * de las librerías SCSP asigna los namespaces
	 */
	public String getNameSpaceSolicitudRespuesta();
	
	
	/**
	 * Devuele el objeto que contiene el nodo correspondiente al esquema de datos-especificos.xsd de cada servicio.
	 * Para los servicios que no usen este nodo en su esquema de peticion.xsd se devolverá null. 
	 * Para el resto se formará la estructura personalizada acorde a su esquema específico.
	 * 
	 * @return Objeto de tipo org.w3c.dom.Element que contiene la estructura  de los datos específicos.
	 * @throws Exception
	 */
	public Element createDatosEspecificos() throws Exception;

	
	
	/**
	 * @param datosEspecificos Objeto de tipo org.w3c.dom.Element que contiene la estructura de los datos específicos.
	 * @return Texto formateado con la información de los datos especifíficos que se quiere mostrar por pantalla.
	 * @throws Exception
	 */
	public String getDatosEspecificosOutput(Element datosEspecificos) throws Exception;
	
	


	
}
