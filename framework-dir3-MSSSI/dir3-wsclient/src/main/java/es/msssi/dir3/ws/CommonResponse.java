/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import consultservice.ws.dir3.msssi.es.consultbasicdataofresponse.ConsultBasicDataOFResponse;
import consultservice.ws.dir3.msssi.es.consultbasicdatauoresponse.ConsultBasicDataUOResponse;
import consultservice.ws.dir3.msssi.es.consultofresponse.ConsultOFResponse;
import consultservice.ws.dir3.msssi.es.consultuoresponse.ConsultUOResponse;
import consultservice.ws.dir3.msssi.es.countresponse.CountResponse;
import consultservice.ws.dir3.msssi.es.getofresponse.GetOFResponse;
import consultservice.ws.dir3.msssi.es.getuoresponse.GetUOResponse;

/**
 * <p>
 * Java class for commonResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="commonResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "commonResponse")
@XmlSeeAlso({ CountResponse.class, GetUOResponse.class, ConsultBasicDataOFResponse.class,
    ConsultOFResponse.class, GetOFResponse.class, ConsultUOResponse.class,
    ConsultBasicDataUOResponse.class })
public class CommonResponse {

}
