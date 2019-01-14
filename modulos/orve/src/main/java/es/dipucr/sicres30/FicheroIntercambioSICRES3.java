//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2018.09.07 a las 10:37:23 AM CEST 
//


package es.dipucr.sicres30;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="De_Origen_o_Remitente">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Codigo_Entidad_Registral_Origen">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="21"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Decodificacion_Entidad_Registral_Origen" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Numero_Registro_Entrada">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Fecha_Hora_Entrada">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="14"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Timestamp_Entrada" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *                   &lt;element name="Codigo_Unidad_Tramitacion_Origen" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="21"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Decodificacion_Unidad_Tramitacion_Origen" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="De_Destino">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Codigo_Entidad_Registral_Destino">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="21"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Decodificacion_Entidad_Registral_Destino" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Codigo_Unidad_Tramitacion_Destino" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="21"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Decodificacion_Unidad_Tramitacion_Destino" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="De_Interesado" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Tipo_Documento_Identificacion_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Documento_Identificacion_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="17"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Razon_Social_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Nombre_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Primer_Apellido_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Segundo_Apellido_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Tipo_Documento_Identificacion_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Documento_Identificacion_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="17"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Razon_Social_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Nombre_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Primer_Apellido_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Segundo_Apellido_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Pais_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="4"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Provincia_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Municipio_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="5"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Direccion_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="160"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Codigo_Postal_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="5"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Correo_Electronico_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="160"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Telefono_Contacto_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Direccion_Electronica_Habilitada_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="160"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Canal_Preferente_Comunicacion_Interesado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Pais_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="4"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Provincia_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Municipio_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="5"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Direccion_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="160"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Codigo_Postal_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="5"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Correo_Electronico_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="160"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Telefono_Contacto_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Direccion_Electronica_Habilitada_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="160"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Canal_Preferente_Comunicacion_Representante" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Observaciones" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="160"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="De_Asunto">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Resumen">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="240"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Codigo_Asunto_Segun_Destino" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="16"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Referencia_Externa" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="16"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Numero_Expediente" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="De_Anexo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nombre_Fichero_Anexado">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Identificador_Fichero">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="50"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Validez_Documento" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Tipo_Documento">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Certificado" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *                   &lt;element name="Firma_Documento" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *                   &lt;element name="TimeStamp" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *                   &lt;element name="Validacion_OCSP_Certificado" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *                   &lt;element name="Hash" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                   &lt;element name="Tipo_MIME" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Anexo" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *                   &lt;element name="Identificador_Documento_Firmado" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="50"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Observaciones" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="50"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="De_Internos_Control">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Tipo_Transporte_Entrada" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Numero_Transporte_Entrada" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Nombre_Usuario" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Contacto_Usuario" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="160"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Identificador_Intercambio">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="33"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Aplicacion_Version_Emisora" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="4"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Tipo_Anotacion">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Descripcion_Tipo_Anotacion" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Tipo_Registro">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="1"/>
 *                         &lt;enumeration value="0"/>
 *                         &lt;enumeration value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Documentacion_Fisica">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="1"/>
 *                         &lt;enumeration value="1"/>
 *                         &lt;enumeration value="2"/>
 *                         &lt;enumeration value="3"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Observaciones_Apunte" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="50"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indicador_Prueba">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="1"/>
 *                         &lt;enumeration value="0"/>
 *                         &lt;enumeration value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Codigo_Entidad_Registral_Inicio">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="21"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Decodificacion_Entidad_Registral_Inicio" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="De_Formulario_Generico">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Expone">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="4000"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Solicita">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="4000"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "deOrigenORemitente",
    "deDestino",
    "deInteresado",
    "deAsunto",
    "deAnexo",
    "deInternosControl",
    "deFormularioGenerico"
})
@XmlRootElement(name = "Fichero_Intercambio_SICRES_3")
public class FicheroIntercambioSICRES3 {

    @XmlElement(name = "De_Origen_o_Remitente", required = true)
    protected FicheroIntercambioSICRES3 .DeOrigenORemitente deOrigenORemitente;
    @XmlElement(name = "De_Destino", required = true)
    protected FicheroIntercambioSICRES3 .DeDestino deDestino;
    @XmlElement(name = "De_Interesado", required = true)
    protected List<FicheroIntercambioSICRES3 .DeInteresado> deInteresado;
    @XmlElement(name = "De_Asunto", required = true)
    protected FicheroIntercambioSICRES3 .DeAsunto deAsunto;
    @XmlElement(name = "De_Anexo")
    protected List<FicheroIntercambioSICRES3 .DeAnexo> deAnexo;
    @XmlElement(name = "De_Internos_Control", required = true)
    protected FicheroIntercambioSICRES3 .DeInternosControl deInternosControl;
    @XmlElement(name = "De_Formulario_Generico", required = true)
    protected FicheroIntercambioSICRES3 .DeFormularioGenerico deFormularioGenerico;

    /**
     * Obtiene el valor de la propiedad deOrigenORemitente.
     * 
     * @return
     *     possible object is
     *     {@link FicheroIntercambioSICRES3 .DeOrigenORemitente }
     *     
     */
    public FicheroIntercambioSICRES3 .DeOrigenORemitente getDeOrigenORemitente() {
        return deOrigenORemitente;
    }

    /**
     * Define el valor de la propiedad deOrigenORemitente.
     * 
     * @param value
     *     allowed object is
     *     {@link FicheroIntercambioSICRES3 .DeOrigenORemitente }
     *     
     */
    public void setDeOrigenORemitente(FicheroIntercambioSICRES3 .DeOrigenORemitente value) {
        this.deOrigenORemitente = value;
    }

    /**
     * Obtiene el valor de la propiedad deDestino.
     * 
     * @return
     *     possible object is
     *     {@link FicheroIntercambioSICRES3 .DeDestino }
     *     
     */
    public FicheroIntercambioSICRES3 .DeDestino getDeDestino() {
        return deDestino;
    }

    /**
     * Define el valor de la propiedad deDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link FicheroIntercambioSICRES3 .DeDestino }
     *     
     */
    public void setDeDestino(FicheroIntercambioSICRES3 .DeDestino value) {
        this.deDestino = value;
    }

    /**
     * Gets the value of the deInteresado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deInteresado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeInteresado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FicheroIntercambioSICRES3 .DeInteresado }
     * 
     * 
     */
    public List<FicheroIntercambioSICRES3 .DeInteresado> getDeInteresado() {
        if (deInteresado == null) {
            deInteresado = new ArrayList<FicheroIntercambioSICRES3 .DeInteresado>();
        }
        return this.deInteresado;
    }

    /**
     * Obtiene el valor de la propiedad deAsunto.
     * 
     * @return
     *     possible object is
     *     {@link FicheroIntercambioSICRES3 .DeAsunto }
     *     
     */
    public FicheroIntercambioSICRES3 .DeAsunto getDeAsunto() {
        return deAsunto;
    }

    /**
     * Define el valor de la propiedad deAsunto.
     * 
     * @param value
     *     allowed object is
     *     {@link FicheroIntercambioSICRES3 .DeAsunto }
     *     
     */
    public void setDeAsunto(FicheroIntercambioSICRES3 .DeAsunto value) {
        this.deAsunto = value;
    }

    /**
     * Gets the value of the deAnexo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deAnexo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeAnexo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FicheroIntercambioSICRES3 .DeAnexo }
     * 
     * 
     */
    public List<FicheroIntercambioSICRES3 .DeAnexo> getDeAnexo() {
        if (deAnexo == null) {
            deAnexo = new ArrayList<FicheroIntercambioSICRES3 .DeAnexo>();
        }
        return this.deAnexo;
    }

    /**
     * Obtiene el valor de la propiedad deInternosControl.
     * 
     * @return
     *     possible object is
     *     {@link FicheroIntercambioSICRES3 .DeInternosControl }
     *     
     */
    public FicheroIntercambioSICRES3 .DeInternosControl getDeInternosControl() {
        return deInternosControl;
    }

    /**
     * Define el valor de la propiedad deInternosControl.
     * 
     * @param value
     *     allowed object is
     *     {@link FicheroIntercambioSICRES3 .DeInternosControl }
     *     
     */
    public void setDeInternosControl(FicheroIntercambioSICRES3 .DeInternosControl value) {
        this.deInternosControl = value;
    }

    /**
     * Obtiene el valor de la propiedad deFormularioGenerico.
     * 
     * @return
     *     possible object is
     *     {@link FicheroIntercambioSICRES3 .DeFormularioGenerico }
     *     
     */
    public FicheroIntercambioSICRES3 .DeFormularioGenerico getDeFormularioGenerico() {
        return deFormularioGenerico;
    }

    /**
     * Define el valor de la propiedad deFormularioGenerico.
     * 
     * @param value
     *     allowed object is
     *     {@link FicheroIntercambioSICRES3 .DeFormularioGenerico }
     *     
     */
    public void setDeFormularioGenerico(FicheroIntercambioSICRES3 .DeFormularioGenerico value) {
        this.deFormularioGenerico = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Nombre_Fichero_Anexado">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Identificador_Fichero">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="50"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Validez_Documento" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Tipo_Documento">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Certificado" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
     *         &lt;element name="Firma_Documento" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
     *         &lt;element name="TimeStamp" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
     *         &lt;element name="Validacion_OCSP_Certificado" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
     *         &lt;element name="Hash" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
     *         &lt;element name="Tipo_MIME" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Anexo" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
     *         &lt;element name="Identificador_Documento_Firmado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="50"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Observaciones" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="50"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "nombreFicheroAnexado",
        "identificadorFichero",
        "validezDocumento",
        "tipoDocumento",
        "certificado",
        "firmaDocumento",
        "timeStamp",
        "validacionOCSPCertificado",
        "hash",
        "tipoMIME",
        "anexo",
        "identificadorDocumentoFirmado",
        "observaciones"
    })
    public static class DeAnexo {

        @XmlElement(name = "Nombre_Fichero_Anexado", required = true)
        protected String nombreFicheroAnexado;
        @XmlElement(name = "Identificador_Fichero", required = true)
        protected String identificadorFichero;
        @XmlElement(name = "Validez_Documento")
        protected String validezDocumento;
        @XmlElement(name = "Tipo_Documento", required = true)
        protected String tipoDocumento;
        @XmlElement(name = "Certificado")
        protected byte[] certificado;
        @XmlElement(name = "Firma_Documento")
        protected byte[] firmaDocumento;
        @XmlElement(name = "TimeStamp")
        protected byte[] timeStamp;
        @XmlElement(name = "Validacion_OCSP_Certificado")
        protected byte[] validacionOCSPCertificado;
        @XmlElement(name = "Hash", required = true)
        protected byte[] hash;
        @XmlElement(name = "Tipo_MIME")
        protected String tipoMIME;
        @XmlElement(name = "Anexo")
        protected byte[] anexo;
        @XmlElement(name = "Identificador_Documento_Firmado")
        protected String identificadorDocumentoFirmado;
        @XmlElement(name = "Observaciones")
        protected String observaciones;

        /**
         * Obtiene el valor de la propiedad nombreFicheroAnexado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreFicheroAnexado() {
            return nombreFicheroAnexado;
        }

        /**
         * Define el valor de la propiedad nombreFicheroAnexado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreFicheroAnexado(String value) {
            this.nombreFicheroAnexado = value;
        }

        /**
         * Obtiene el valor de la propiedad identificadorFichero.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdentificadorFichero() {
            return identificadorFichero;
        }

        /**
         * Define el valor de la propiedad identificadorFichero.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdentificadorFichero(String value) {
            this.identificadorFichero = value;
        }

        /**
         * Obtiene el valor de la propiedad validezDocumento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValidezDocumento() {
            return validezDocumento;
        }

        /**
         * Define el valor de la propiedad validezDocumento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValidezDocumento(String value) {
            this.validezDocumento = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoDocumento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoDocumento() {
            return tipoDocumento;
        }

        /**
         * Define el valor de la propiedad tipoDocumento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoDocumento(String value) {
            this.tipoDocumento = value;
        }

        /**
         * Obtiene el valor de la propiedad certificado.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getCertificado() {
            return certificado;
        }

        /**
         * Define el valor de la propiedad certificado.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setCertificado(byte[] value) {
            this.certificado = value;
        }

        /**
         * Obtiene el valor de la propiedad firmaDocumento.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getFirmaDocumento() {
            return firmaDocumento;
        }

        /**
         * Define el valor de la propiedad firmaDocumento.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setFirmaDocumento(byte[] value) {
            this.firmaDocumento = value;
        }

        /**
         * Obtiene el valor de la propiedad timeStamp.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getTimeStamp() {
            return timeStamp;
        }

        /**
         * Define el valor de la propiedad timeStamp.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setTimeStamp(byte[] value) {
            this.timeStamp = value;
        }

        /**
         * Obtiene el valor de la propiedad validacionOCSPCertificado.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getValidacionOCSPCertificado() {
            return validacionOCSPCertificado;
        }

        /**
         * Define el valor de la propiedad validacionOCSPCertificado.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setValidacionOCSPCertificado(byte[] value) {
            this.validacionOCSPCertificado = value;
        }

        /**
         * Obtiene el valor de la propiedad hash.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getHash() {
            return hash;
        }

        /**
         * Define el valor de la propiedad hash.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setHash(byte[] value) {
            this.hash = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoMIME.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoMIME() {
            return tipoMIME;
        }

        /**
         * Define el valor de la propiedad tipoMIME.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoMIME(String value) {
            this.tipoMIME = value;
        }

        /**
         * Obtiene el valor de la propiedad anexo.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getAnexo() {
            return anexo;
        }

        /**
         * Define el valor de la propiedad anexo.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setAnexo(byte[] value) {
            this.anexo = value;
        }

        /**
         * Obtiene el valor de la propiedad identificadorDocumentoFirmado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdentificadorDocumentoFirmado() {
            return identificadorDocumentoFirmado;
        }

        /**
         * Define el valor de la propiedad identificadorDocumentoFirmado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdentificadorDocumentoFirmado(String value) {
            this.identificadorDocumentoFirmado = value;
        }

        /**
         * Obtiene el valor de la propiedad observaciones.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getObservaciones() {
            return observaciones;
        }

        /**
         * Define el valor de la propiedad observaciones.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setObservaciones(String value) {
            this.observaciones = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Resumen">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="240"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Codigo_Asunto_Segun_Destino" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="16"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Referencia_Externa" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="16"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Numero_Expediente" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "resumen",
        "codigoAsuntoSegunDestino",
        "referenciaExterna",
        "numeroExpediente"
    })
    public static class DeAsunto {

        @XmlElement(name = "Resumen", required = true)
        protected String resumen;
        @XmlElement(name = "Codigo_Asunto_Segun_Destino")
        protected String codigoAsuntoSegunDestino;
        @XmlElement(name = "Referencia_Externa")
        protected String referenciaExterna;
        @XmlElement(name = "Numero_Expediente")
        protected String numeroExpediente;

        /**
         * Obtiene el valor de la propiedad resumen.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResumen() {
            return resumen;
        }

        /**
         * Define el valor de la propiedad resumen.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResumen(String value) {
            this.resumen = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoAsuntoSegunDestino.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoAsuntoSegunDestino() {
            return codigoAsuntoSegunDestino;
        }

        /**
         * Define el valor de la propiedad codigoAsuntoSegunDestino.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoAsuntoSegunDestino(String value) {
            this.codigoAsuntoSegunDestino = value;
        }

        /**
         * Obtiene el valor de la propiedad referenciaExterna.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReferenciaExterna() {
            return referenciaExterna;
        }

        /**
         * Define el valor de la propiedad referenciaExterna.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReferenciaExterna(String value) {
            this.referenciaExterna = value;
        }

        /**
         * Obtiene el valor de la propiedad numeroExpediente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroExpediente() {
            return numeroExpediente;
        }

        /**
         * Define el valor de la propiedad numeroExpediente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroExpediente(String value) {
            this.numeroExpediente = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Codigo_Entidad_Registral_Destino">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="21"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Decodificacion_Entidad_Registral_Destino" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Codigo_Unidad_Tramitacion_Destino" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="21"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Decodificacion_Unidad_Tramitacion_Destino" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codigoEntidadRegistralDestino",
        "decodificacionEntidadRegistralDestino",
        "codigoUnidadTramitacionDestino",
        "decodificacionUnidadTramitacionDestino"
    })
    public static class DeDestino {

        @XmlElement(name = "Codigo_Entidad_Registral_Destino", required = true)
        protected String codigoEntidadRegistralDestino;
        @XmlElement(name = "Decodificacion_Entidad_Registral_Destino")
        protected String decodificacionEntidadRegistralDestino;
        @XmlElement(name = "Codigo_Unidad_Tramitacion_Destino")
        protected String codigoUnidadTramitacionDestino;
        @XmlElement(name = "Decodificacion_Unidad_Tramitacion_Destino")
        protected String decodificacionUnidadTramitacionDestino;

        /**
         * Obtiene el valor de la propiedad codigoEntidadRegistralDestino.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoEntidadRegistralDestino() {
            return codigoEntidadRegistralDestino;
        }

        /**
         * Define el valor de la propiedad codigoEntidadRegistralDestino.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoEntidadRegistralDestino(String value) {
            this.codigoEntidadRegistralDestino = value;
        }

        /**
         * Obtiene el valor de la propiedad decodificacionEntidadRegistralDestino.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDecodificacionEntidadRegistralDestino() {
            return decodificacionEntidadRegistralDestino;
        }

        /**
         * Define el valor de la propiedad decodificacionEntidadRegistralDestino.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDecodificacionEntidadRegistralDestino(String value) {
            this.decodificacionEntidadRegistralDestino = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoUnidadTramitacionDestino.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoUnidadTramitacionDestino() {
            return codigoUnidadTramitacionDestino;
        }

        /**
         * Define el valor de la propiedad codigoUnidadTramitacionDestino.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoUnidadTramitacionDestino(String value) {
            this.codigoUnidadTramitacionDestino = value;
        }

        /**
         * Obtiene el valor de la propiedad decodificacionUnidadTramitacionDestino.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDecodificacionUnidadTramitacionDestino() {
            return decodificacionUnidadTramitacionDestino;
        }

        /**
         * Define el valor de la propiedad decodificacionUnidadTramitacionDestino.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDecodificacionUnidadTramitacionDestino(String value) {
            this.decodificacionUnidadTramitacionDestino = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Expone">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="4000"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Solicita">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="4000"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "expone",
        "solicita"
    })
    public static class DeFormularioGenerico {

        @XmlElement(name = "Expone", required = true)
        protected String expone;
        @XmlElement(name = "Solicita", required = true)
        protected String solicita;

        /**
         * Obtiene el valor de la propiedad expone.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getExpone() {
            return expone;
        }

        /**
         * Define el valor de la propiedad expone.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setExpone(String value) {
            this.expone = value;
        }

        /**
         * Obtiene el valor de la propiedad solicita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSolicita() {
            return solicita;
        }

        /**
         * Define el valor de la propiedad solicita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSolicita(String value) {
            this.solicita = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Tipo_Documento_Identificacion_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Documento_Identificacion_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="17"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Razon_Social_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Nombre_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Primer_Apellido_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Segundo_Apellido_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Tipo_Documento_Identificacion_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Documento_Identificacion_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="17"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Razon_Social_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Nombre_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Primer_Apellido_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Segundo_Apellido_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Pais_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="4"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Provincia_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Municipio_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="5"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Direccion_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="160"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Codigo_Postal_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="5"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Correo_Electronico_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="160"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Telefono_Contacto_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Direccion_Electronica_Habilitada_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="160"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Canal_Preferente_Comunicacion_Interesado" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Pais_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="4"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Provincia_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Municipio_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="5"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Direccion_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="160"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Codigo_Postal_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="5"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Correo_Electronico_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="160"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Telefono_Contacto_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Direccion_Electronica_Habilitada_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="160"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Canal_Preferente_Comunicacion_Representante" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Observaciones" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="160"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tipoDocumentoIdentificacionInteresado",
        "documentoIdentificacionInteresado",
        "razonSocialInteresado",
        "nombreInteresado",
        "primerApellidoInteresado",
        "segundoApellidoInteresado",
        "tipoDocumentoIdentificacionRepresentante",
        "documentoIdentificacionRepresentante",
        "razonSocialRepresentante",
        "nombreRepresentante",
        "primerApellidoRepresentante",
        "segundoApellidoRepresentante",
        "paisInteresado",
        "provinciaInteresado",
        "municipioInteresado",
        "direccionInteresado",
        "codigoPostalInteresado",
        "correoElectronicoInteresado",
        "telefonoContactoInteresado",
        "direccionElectronicaHabilitadaInteresado",
        "canalPreferenteComunicacionInteresado",
        "paisRepresentante",
        "provinciaRepresentante",
        "municipioRepresentante",
        "direccionRepresentante",
        "codigoPostalRepresentante",
        "correoElectronicoRepresentante",
        "telefonoContactoRepresentante",
        "direccionElectronicaHabilitadaRepresentante",
        "canalPreferenteComunicacionRepresentante",
        "observaciones"
    })
    public static class DeInteresado {

        @XmlElement(name = "Tipo_Documento_Identificacion_Interesado")
        protected String tipoDocumentoIdentificacionInteresado;
        @XmlElement(name = "Documento_Identificacion_Interesado")
        protected String documentoIdentificacionInteresado;
        @XmlElement(name = "Razon_Social_Interesado")
        protected String razonSocialInteresado;
        @XmlElement(name = "Nombre_Interesado")
        protected String nombreInteresado;
        @XmlElement(name = "Primer_Apellido_Interesado")
        protected String primerApellidoInteresado;
        @XmlElement(name = "Segundo_Apellido_Interesado")
        protected String segundoApellidoInteresado;
        @XmlElement(name = "Tipo_Documento_Identificacion_Representante")
        protected String tipoDocumentoIdentificacionRepresentante;
        @XmlElement(name = "Documento_Identificacion_Representante")
        protected String documentoIdentificacionRepresentante;
        @XmlElement(name = "Razon_Social_Representante")
        protected String razonSocialRepresentante;
        @XmlElement(name = "Nombre_Representante")
        protected String nombreRepresentante;
        @XmlElement(name = "Primer_Apellido_Representante")
        protected String primerApellidoRepresentante;
        @XmlElement(name = "Segundo_Apellido_Representante")
        protected String segundoApellidoRepresentante;
        @XmlElement(name = "Pais_Interesado")
        protected String paisInteresado;
        @XmlElement(name = "Provincia_Interesado")
        protected String provinciaInteresado;
        @XmlElement(name = "Municipio_Interesado")
        protected String municipioInteresado;
        @XmlElement(name = "Direccion_Interesado")
        protected String direccionInteresado;
        @XmlElement(name = "Codigo_Postal_Interesado")
        protected String codigoPostalInteresado;
        @XmlElement(name = "Correo_Electronico_Interesado")
        protected String correoElectronicoInteresado;
        @XmlElement(name = "Telefono_Contacto_Interesado")
        protected String telefonoContactoInteresado;
        @XmlElement(name = "Direccion_Electronica_Habilitada_Interesado")
        protected String direccionElectronicaHabilitadaInteresado;
        @XmlElement(name = "Canal_Preferente_Comunicacion_Interesado")
        protected String canalPreferenteComunicacionInteresado;
        @XmlElement(name = "Pais_Representante")
        protected String paisRepresentante;
        @XmlElement(name = "Provincia_Representante")
        protected String provinciaRepresentante;
        @XmlElement(name = "Municipio_Representante")
        protected String municipioRepresentante;
        @XmlElement(name = "Direccion_Representante")
        protected String direccionRepresentante;
        @XmlElement(name = "Codigo_Postal_Representante")
        protected String codigoPostalRepresentante;
        @XmlElement(name = "Correo_Electronico_Representante")
        protected String correoElectronicoRepresentante;
        @XmlElement(name = "Telefono_Contacto_Representante")
        protected String telefonoContactoRepresentante;
        @XmlElement(name = "Direccion_Electronica_Habilitada_Representante")
        protected String direccionElectronicaHabilitadaRepresentante;
        @XmlElement(name = "Canal_Preferente_Comunicacion_Representante")
        protected String canalPreferenteComunicacionRepresentante;
        @XmlElement(name = "Observaciones")
        protected String observaciones;

        /**
         * Obtiene el valor de la propiedad tipoDocumentoIdentificacionInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoDocumentoIdentificacionInteresado() {
            return tipoDocumentoIdentificacionInteresado;
        }

        /**
         * Define el valor de la propiedad tipoDocumentoIdentificacionInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoDocumentoIdentificacionInteresado(String value) {
            this.tipoDocumentoIdentificacionInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad documentoIdentificacionInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDocumentoIdentificacionInteresado() {
            return documentoIdentificacionInteresado;
        }

        /**
         * Define el valor de la propiedad documentoIdentificacionInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocumentoIdentificacionInteresado(String value) {
            this.documentoIdentificacionInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad razonSocialInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRazonSocialInteresado() {
            return razonSocialInteresado;
        }

        /**
         * Define el valor de la propiedad razonSocialInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRazonSocialInteresado(String value) {
            this.razonSocialInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad nombreInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreInteresado() {
            return nombreInteresado;
        }

        /**
         * Define el valor de la propiedad nombreInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreInteresado(String value) {
            this.nombreInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad primerApellidoInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPrimerApellidoInteresado() {
            return primerApellidoInteresado;
        }

        /**
         * Define el valor de la propiedad primerApellidoInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPrimerApellidoInteresado(String value) {
            this.primerApellidoInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad segundoApellidoInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSegundoApellidoInteresado() {
            return segundoApellidoInteresado;
        }

        /**
         * Define el valor de la propiedad segundoApellidoInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSegundoApellidoInteresado(String value) {
            this.segundoApellidoInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoDocumentoIdentificacionRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoDocumentoIdentificacionRepresentante() {
            return tipoDocumentoIdentificacionRepresentante;
        }

        /**
         * Define el valor de la propiedad tipoDocumentoIdentificacionRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoDocumentoIdentificacionRepresentante(String value) {
            this.tipoDocumentoIdentificacionRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad documentoIdentificacionRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDocumentoIdentificacionRepresentante() {
            return documentoIdentificacionRepresentante;
        }

        /**
         * Define el valor de la propiedad documentoIdentificacionRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocumentoIdentificacionRepresentante(String value) {
            this.documentoIdentificacionRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad razonSocialRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRazonSocialRepresentante() {
            return razonSocialRepresentante;
        }

        /**
         * Define el valor de la propiedad razonSocialRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRazonSocialRepresentante(String value) {
            this.razonSocialRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad nombreRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreRepresentante() {
            return nombreRepresentante;
        }

        /**
         * Define el valor de la propiedad nombreRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreRepresentante(String value) {
            this.nombreRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad primerApellidoRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPrimerApellidoRepresentante() {
            return primerApellidoRepresentante;
        }

        /**
         * Define el valor de la propiedad primerApellidoRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPrimerApellidoRepresentante(String value) {
            this.primerApellidoRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad segundoApellidoRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSegundoApellidoRepresentante() {
            return segundoApellidoRepresentante;
        }

        /**
         * Define el valor de la propiedad segundoApellidoRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSegundoApellidoRepresentante(String value) {
            this.segundoApellidoRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad paisInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaisInteresado() {
            return paisInteresado;
        }

        /**
         * Define el valor de la propiedad paisInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaisInteresado(String value) {
            this.paisInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad provinciaInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProvinciaInteresado() {
            return provinciaInteresado;
        }

        /**
         * Define el valor de la propiedad provinciaInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProvinciaInteresado(String value) {
            this.provinciaInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad municipioInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMunicipioInteresado() {
            return municipioInteresado;
        }

        /**
         * Define el valor de la propiedad municipioInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMunicipioInteresado(String value) {
            this.municipioInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad direccionInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDireccionInteresado() {
            return direccionInteresado;
        }

        /**
         * Define el valor de la propiedad direccionInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDireccionInteresado(String value) {
            this.direccionInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoPostalInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoPostalInteresado() {
            return codigoPostalInteresado;
        }

        /**
         * Define el valor de la propiedad codigoPostalInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoPostalInteresado(String value) {
            this.codigoPostalInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad correoElectronicoInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCorreoElectronicoInteresado() {
            return correoElectronicoInteresado;
        }

        /**
         * Define el valor de la propiedad correoElectronicoInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCorreoElectronicoInteresado(String value) {
            this.correoElectronicoInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad telefonoContactoInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTelefonoContactoInteresado() {
            return telefonoContactoInteresado;
        }

        /**
         * Define el valor de la propiedad telefonoContactoInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTelefonoContactoInteresado(String value) {
            this.telefonoContactoInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad direccionElectronicaHabilitadaInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDireccionElectronicaHabilitadaInteresado() {
            return direccionElectronicaHabilitadaInteresado;
        }

        /**
         * Define el valor de la propiedad direccionElectronicaHabilitadaInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDireccionElectronicaHabilitadaInteresado(String value) {
            this.direccionElectronicaHabilitadaInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad canalPreferenteComunicacionInteresado.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCanalPreferenteComunicacionInteresado() {
            return canalPreferenteComunicacionInteresado;
        }

        /**
         * Define el valor de la propiedad canalPreferenteComunicacionInteresado.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCanalPreferenteComunicacionInteresado(String value) {
            this.canalPreferenteComunicacionInteresado = value;
        }

        /**
         * Obtiene el valor de la propiedad paisRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaisRepresentante() {
            return paisRepresentante;
        }

        /**
         * Define el valor de la propiedad paisRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaisRepresentante(String value) {
            this.paisRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad provinciaRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProvinciaRepresentante() {
            return provinciaRepresentante;
        }

        /**
         * Define el valor de la propiedad provinciaRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProvinciaRepresentante(String value) {
            this.provinciaRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad municipioRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMunicipioRepresentante() {
            return municipioRepresentante;
        }

        /**
         * Define el valor de la propiedad municipioRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMunicipioRepresentante(String value) {
            this.municipioRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad direccionRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDireccionRepresentante() {
            return direccionRepresentante;
        }

        /**
         * Define el valor de la propiedad direccionRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDireccionRepresentante(String value) {
            this.direccionRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoPostalRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoPostalRepresentante() {
            return codigoPostalRepresentante;
        }

        /**
         * Define el valor de la propiedad codigoPostalRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoPostalRepresentante(String value) {
            this.codigoPostalRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad correoElectronicoRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCorreoElectronicoRepresentante() {
            return correoElectronicoRepresentante;
        }

        /**
         * Define el valor de la propiedad correoElectronicoRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCorreoElectronicoRepresentante(String value) {
            this.correoElectronicoRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad telefonoContactoRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTelefonoContactoRepresentante() {
            return telefonoContactoRepresentante;
        }

        /**
         * Define el valor de la propiedad telefonoContactoRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTelefonoContactoRepresentante(String value) {
            this.telefonoContactoRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad direccionElectronicaHabilitadaRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDireccionElectronicaHabilitadaRepresentante() {
            return direccionElectronicaHabilitadaRepresentante;
        }

        /**
         * Define el valor de la propiedad direccionElectronicaHabilitadaRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDireccionElectronicaHabilitadaRepresentante(String value) {
            this.direccionElectronicaHabilitadaRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad canalPreferenteComunicacionRepresentante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCanalPreferenteComunicacionRepresentante() {
            return canalPreferenteComunicacionRepresentante;
        }

        /**
         * Define el valor de la propiedad canalPreferenteComunicacionRepresentante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCanalPreferenteComunicacionRepresentante(String value) {
            this.canalPreferenteComunicacionRepresentante = value;
        }

        /**
         * Obtiene el valor de la propiedad observaciones.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getObservaciones() {
            return observaciones;
        }

        /**
         * Define el valor de la propiedad observaciones.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setObservaciones(String value) {
            this.observaciones = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Tipo_Transporte_Entrada" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Numero_Transporte_Entrada" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Nombre_Usuario" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Contacto_Usuario" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="160"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Identificador_Intercambio">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="33"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Aplicacion_Version_Emisora" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="4"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Tipo_Anotacion">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Descripcion_Tipo_Anotacion" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Tipo_Registro">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="1"/>
     *               &lt;enumeration value="0"/>
     *               &lt;enumeration value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Documentacion_Fisica">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="1"/>
     *               &lt;enumeration value="1"/>
     *               &lt;enumeration value="2"/>
     *               &lt;enumeration value="3"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Observaciones_Apunte" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="50"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Indicador_Prueba">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="1"/>
     *               &lt;enumeration value="0"/>
     *               &lt;enumeration value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Codigo_Entidad_Registral_Inicio">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="21"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Decodificacion_Entidad_Registral_Inicio" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tipoTransporteEntrada",
        "numeroTransporteEntrada",
        "nombreUsuario",
        "contactoUsuario",
        "identificadorIntercambio",
        "aplicacionVersionEmisora",
        "tipoAnotacion",
        "descripcionTipoAnotacion",
        "tipoRegistro",
        "documentacionFisica",
        "observacionesApunte",
        "indicadorPrueba",
        "codigoEntidadRegistralInicio",
        "decodificacionEntidadRegistralInicio"
    })
    public static class DeInternosControl {

        @XmlElement(name = "Tipo_Transporte_Entrada")
        protected String tipoTransporteEntrada;
        @XmlElement(name = "Numero_Transporte_Entrada")
        protected String numeroTransporteEntrada;
        @XmlElement(name = "Nombre_Usuario")
        protected String nombreUsuario;
        @XmlElement(name = "Contacto_Usuario")
        protected String contactoUsuario;
        @XmlElement(name = "Identificador_Intercambio", required = true)
        protected String identificadorIntercambio;
        @XmlElement(name = "Aplicacion_Version_Emisora")
        protected String aplicacionVersionEmisora;
        @XmlElement(name = "Tipo_Anotacion", required = true)
        protected String tipoAnotacion;
        @XmlElement(name = "Descripcion_Tipo_Anotacion")
        protected String descripcionTipoAnotacion;
        @XmlElement(name = "Tipo_Registro", required = true)
        protected String tipoRegistro;
        @XmlElement(name = "Documentacion_Fisica", required = true)
        protected String documentacionFisica;
        @XmlElement(name = "Observaciones_Apunte")
        protected String observacionesApunte;
        @XmlElement(name = "Indicador_Prueba", required = true)
        protected String indicadorPrueba;
        @XmlElement(name = "Codigo_Entidad_Registral_Inicio", required = true)
        protected String codigoEntidadRegistralInicio;
        @XmlElement(name = "Decodificacion_Entidad_Registral_Inicio")
        protected String decodificacionEntidadRegistralInicio;

        /**
         * Obtiene el valor de la propiedad tipoTransporteEntrada.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoTransporteEntrada() {
            return tipoTransporteEntrada;
        }

        /**
         * Define el valor de la propiedad tipoTransporteEntrada.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoTransporteEntrada(String value) {
            this.tipoTransporteEntrada = value;
        }

        /**
         * Obtiene el valor de la propiedad numeroTransporteEntrada.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroTransporteEntrada() {
            return numeroTransporteEntrada;
        }

        /**
         * Define el valor de la propiedad numeroTransporteEntrada.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroTransporteEntrada(String value) {
            this.numeroTransporteEntrada = value;
        }

        /**
         * Obtiene el valor de la propiedad nombreUsuario.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreUsuario() {
            return nombreUsuario;
        }

        /**
         * Define el valor de la propiedad nombreUsuario.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreUsuario(String value) {
            this.nombreUsuario = value;
        }

        /**
         * Obtiene el valor de la propiedad contactoUsuario.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContactoUsuario() {
            return contactoUsuario;
        }

        /**
         * Define el valor de la propiedad contactoUsuario.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContactoUsuario(String value) {
            this.contactoUsuario = value;
        }

        /**
         * Obtiene el valor de la propiedad identificadorIntercambio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdentificadorIntercambio() {
            return identificadorIntercambio;
        }

        /**
         * Define el valor de la propiedad identificadorIntercambio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdentificadorIntercambio(String value) {
            this.identificadorIntercambio = value;
        }

        /**
         * Obtiene el valor de la propiedad aplicacionVersionEmisora.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAplicacionVersionEmisora() {
            return aplicacionVersionEmisora;
        }

        /**
         * Define el valor de la propiedad aplicacionVersionEmisora.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAplicacionVersionEmisora(String value) {
            this.aplicacionVersionEmisora = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoAnotacion.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoAnotacion() {
            return tipoAnotacion;
        }

        /**
         * Define el valor de la propiedad tipoAnotacion.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoAnotacion(String value) {
            this.tipoAnotacion = value;
        }

        /**
         * Obtiene el valor de la propiedad descripcionTipoAnotacion.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripcionTipoAnotacion() {
            return descripcionTipoAnotacion;
        }

        /**
         * Define el valor de la propiedad descripcionTipoAnotacion.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripcionTipoAnotacion(String value) {
            this.descripcionTipoAnotacion = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoRegistro.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoRegistro() {
            return tipoRegistro;
        }

        /**
         * Define el valor de la propiedad tipoRegistro.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoRegistro(String value) {
            this.tipoRegistro = value;
        }

        /**
         * Obtiene el valor de la propiedad documentacionFisica.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDocumentacionFisica() {
            return documentacionFisica;
        }

        /**
         * Define el valor de la propiedad documentacionFisica.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocumentacionFisica(String value) {
            this.documentacionFisica = value;
        }

        /**
         * Obtiene el valor de la propiedad observacionesApunte.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getObservacionesApunte() {
            return observacionesApunte;
        }

        /**
         * Define el valor de la propiedad observacionesApunte.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setObservacionesApunte(String value) {
            this.observacionesApunte = value;
        }

        /**
         * Obtiene el valor de la propiedad indicadorPrueba.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndicadorPrueba() {
            return indicadorPrueba;
        }

        /**
         * Define el valor de la propiedad indicadorPrueba.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndicadorPrueba(String value) {
            this.indicadorPrueba = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoEntidadRegistralInicio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoEntidadRegistralInicio() {
            return codigoEntidadRegistralInicio;
        }

        /**
         * Define el valor de la propiedad codigoEntidadRegistralInicio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoEntidadRegistralInicio(String value) {
            this.codigoEntidadRegistralInicio = value;
        }

        /**
         * Obtiene el valor de la propiedad decodificacionEntidadRegistralInicio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDecodificacionEntidadRegistralInicio() {
            return decodificacionEntidadRegistralInicio;
        }

        /**
         * Define el valor de la propiedad decodificacionEntidadRegistralInicio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDecodificacionEntidadRegistralInicio(String value) {
            this.decodificacionEntidadRegistralInicio = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Codigo_Entidad_Registral_Origen">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="21"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Decodificacion_Entidad_Registral_Origen" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Numero_Registro_Entrada">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Fecha_Hora_Entrada">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="14"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Timestamp_Entrada" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
     *         &lt;element name="Codigo_Unidad_Tramitacion_Origen" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="21"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Decodificacion_Unidad_Tramitacion_Origen" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codigoEntidadRegistralOrigen",
        "decodificacionEntidadRegistralOrigen",
        "numeroRegistroEntrada",
        "fechaHoraEntrada",
        "timestampEntrada",
        "codigoUnidadTramitacionOrigen",
        "decodificacionUnidadTramitacionOrigen"
    })
    public static class DeOrigenORemitente {

        @XmlElement(name = "Codigo_Entidad_Registral_Origen", required = true)
        protected String codigoEntidadRegistralOrigen;
        @XmlElement(name = "Decodificacion_Entidad_Registral_Origen")
        protected String decodificacionEntidadRegistralOrigen;
        @XmlElement(name = "Numero_Registro_Entrada", required = true)
        protected String numeroRegistroEntrada;
        @XmlElement(name = "Fecha_Hora_Entrada", required = true)
        protected String fechaHoraEntrada;
        @XmlElement(name = "Timestamp_Entrada")
        protected byte[] timestampEntrada;
        @XmlElement(name = "Codigo_Unidad_Tramitacion_Origen")
        protected String codigoUnidadTramitacionOrigen;
        @XmlElement(name = "Decodificacion_Unidad_Tramitacion_Origen")
        protected String decodificacionUnidadTramitacionOrigen;

        /**
         * Obtiene el valor de la propiedad codigoEntidadRegistralOrigen.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoEntidadRegistralOrigen() {
            return codigoEntidadRegistralOrigen;
        }

        /**
         * Define el valor de la propiedad codigoEntidadRegistralOrigen.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoEntidadRegistralOrigen(String value) {
            this.codigoEntidadRegistralOrigen = value;
        }

        /**
         * Obtiene el valor de la propiedad decodificacionEntidadRegistralOrigen.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDecodificacionEntidadRegistralOrigen() {
            return decodificacionEntidadRegistralOrigen;
        }

        /**
         * Define el valor de la propiedad decodificacionEntidadRegistralOrigen.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDecodificacionEntidadRegistralOrigen(String value) {
            this.decodificacionEntidadRegistralOrigen = value;
        }

        /**
         * Obtiene el valor de la propiedad numeroRegistroEntrada.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroRegistroEntrada() {
            return numeroRegistroEntrada;
        }

        /**
         * Define el valor de la propiedad numeroRegistroEntrada.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroRegistroEntrada(String value) {
            this.numeroRegistroEntrada = value;
        }

        /**
         * Obtiene el valor de la propiedad fechaHoraEntrada.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFechaHoraEntrada() {
            return fechaHoraEntrada;
        }

        /**
         * Define el valor de la propiedad fechaHoraEntrada.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFechaHoraEntrada(String value) {
            this.fechaHoraEntrada = value;
        }

        /**
         * Obtiene el valor de la propiedad timestampEntrada.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getTimestampEntrada() {
            return timestampEntrada;
        }

        /**
         * Define el valor de la propiedad timestampEntrada.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setTimestampEntrada(byte[] value) {
            this.timestampEntrada = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoUnidadTramitacionOrigen.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoUnidadTramitacionOrigen() {
            return codigoUnidadTramitacionOrigen;
        }

        /**
         * Define el valor de la propiedad codigoUnidadTramitacionOrigen.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoUnidadTramitacionOrigen(String value) {
            this.codigoUnidadTramitacionOrigen = value;
        }

        /**
         * Obtiene el valor de la propiedad decodificacionUnidadTramitacionOrigen.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDecodificacionUnidadTramitacionOrigen() {
            return decodificacionUnidadTramitacionOrigen;
        }

        /**
         * Define el valor de la propiedad decodificacionUnidadTramitacionOrigen.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDecodificacionUnidadTramitacionOrigen(String value) {
            this.decodificacionUnidadTramitacionOrigen = value;
        }

    }

}
