/*
 * Plan Avanza Local Soluciones – e-fácil – Copyright © 2011 – Ministerio de Industria, Turismo y 
 * Comercio.
 *
 * Este programa es software libre, por ello está permitido redistribuirlo y/o modificarlo bajo los 
 * términos de la GNU General Public License, en su versión 3, publicada por la Free Software 
 * Foundation.
 *
 * Junto con este programa debe haber recibido una copia de la GNU General Public License, en 
 * caso contrario puede consultarla en <http://www.gnu.org/licenses/>.
 *
 * El presente programa posee las siguientes cláusulas particulares de licencia GPL v3:
 *
 *  - Queda restringido su uso a Administraciones Públicas, en el ámbito de sus 
 * competencias legalmente establecidas y con la finalidad de utilidad pública e interés 
 * social;
 *  - La distribución y el uso del software tienen carácter gratuito;
 *  - Ni el software ni ninguno de los Módulos que lo componen serán usados ni 
 * sublicenciados, bajo ninguna circunstancia, con fines o intereses comerciales, bien sea 
 * directa o indirectamente, por parte de la Administración Pública, sus funcionarios, 
 * empleados o cualquier otra persona u organización dentro o fuera de la 
 * administración.
 *
 */

/**
 * InvoiceIdentificator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.fi2net.efacil.conformacion.ws.client;


/**
 * Identificador de la factura o anexo
 */
public class InvoiceIdentificator  implements java.io.Serializable {
    /* Codigo de usuario de la factura. */
    private java.lang.String codUsuario;

    /* Codigo del fichero. */
    private int codFile;

    /**
     * Constructor.
     */
    public InvoiceIdentificator() {
    }

    /**
     * Constructor.
     *
     * @param codUsuario Código de usuario.
     * @param codFile Código de fichero.
     */
    public InvoiceIdentificator(
           java.lang.String codUsuario,
           int codFile) {
           this.codUsuario = codUsuario;
           this.codFile = codFile;
    }


    /**
     * Gets the codUsuario value for this InvoiceIdentificator.
     * 
     * @return codUsuario   * Codigo de usuario de la factura.
     */
    public java.lang.String getCodUsuario() {
        return codUsuario;
    }


    /**
     * Sets the codUsuario value for this InvoiceIdentificator.
     * 
     * @param codUsuario   * Codigo de usuario de la factura.
     */
    public void setCodUsuario(java.lang.String codUsuario) {
        this.codUsuario = codUsuario;
    }


    /**
     * Gets the codFile value for this InvoiceIdentificator.
     * 
     * @return codFile   * Codigo del fichero.
     */
    public int getCodFile() {
        return codFile;
    }


    /**
     * Sets the codFile value for this InvoiceIdentificator.
     * 
     * @param codFile   * Codigo del fichero.
     */
    public void setCodFile(int codFile) {
        this.codFile = codFile;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InvoiceIdentificator)) return false;
        InvoiceIdentificator other = (InvoiceIdentificator) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codUsuario==null && other.getCodUsuario()==null) || 
             (this.codUsuario!=null &&
              this.codUsuario.equals(other.getCodUsuario()))) &&
            this.codFile == other.getCodFile();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCodUsuario() != null) {
            _hashCode += getCodUsuario().hashCode();
        }
        _hashCode += getCodFile();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InvoiceIdentificator.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "InvoiceIdentificator"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codUsuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "codUsuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codFile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "codFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
