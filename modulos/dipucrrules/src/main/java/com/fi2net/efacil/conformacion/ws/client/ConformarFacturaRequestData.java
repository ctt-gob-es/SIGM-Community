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
 * ConformarFacturaRequestData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.fi2net.efacil.conformacion.ws.client;


/**
 * Contenedor de la solicitud de conformacion de facturas.
 */
public class ConformarFacturaRequestData  implements java.io.Serializable {
    /* Identificador de la factura a conformar */
    private com.fi2net.efacil.conformacion.ws.client.InvoiceIdentificator invoiceId;

    /* Indica si la factura ha sido conformada. Si es true la factura
     * estara conformada, si es false no lo estara. */
    private boolean conformacionOK;

    //[eCenpri-Felipe #737]
    private String motivo;
    
    /**
     * Constructor.
     */
    public ConformarFacturaRequestData() {
    }

    /**
     * Constructor.
     *
     * @param invoiceId ID de la factura.
     * @param conformacionOK {@code true} si la conformación ha sido OK.
     */
    public ConformarFacturaRequestData(
           com.fi2net.efacil.conformacion.ws.client.InvoiceIdentificator invoiceId,
           boolean conformacionOK) {
           this.invoiceId = invoiceId;
           this.conformacionOK = conformacionOK;
    }


    /**
     * Gets the invoiceId value for this ConformarFacturaRequestData.
     * 
     * @return invoiceId   * Identificador de la factura a conformar
     */
    public com.fi2net.efacil.conformacion.ws.client.InvoiceIdentificator getInvoiceId() {
        return invoiceId;
    }


    /**
     * Sets the invoiceId value for this ConformarFacturaRequestData.
     * 
     * @param invoiceId   * Identificador de la factura a conformar
     */
    public void setInvoiceId(com.fi2net.efacil.conformacion.ws.client.InvoiceIdentificator invoiceId) {
        this.invoiceId = invoiceId;
    }


    /**
     * Gets the conformacionOK value for this ConformarFacturaRequestData.
     * 
     * @return conformacionOK   * Indica si la factura ha sido conformada. Si es true la factura
     * estara conformada,
     *                         si es false no lo estara.
     */
    public boolean isConformacionOK() {
        return conformacionOK;
    }


    /**
     * Sets the conformacionOK value for this ConformarFacturaRequestData.
     * 
     * @param conformacionOK   * Indica si la factura ha sido conformada. Si es true la factura
     * estara conformada,
     *                         si es false no lo estara.
     */
    public void setConformacionOK(boolean conformacionOK) {
        this.conformacionOK = conformacionOK;
    }
    
    
    /**
     * Devuelve el motivo
     * @return
     */
    public String getMotivo() {
		return motivo;
	}

    /**
     * Define el valor del motivo
     * @param motivo
     */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}


	private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConformarFacturaRequestData)) return false;
        ConformarFacturaRequestData other = (ConformarFacturaRequestData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.invoiceId==null && other.getInvoiceId()==null) || 
             (this.invoiceId!=null &&
              this.invoiceId.equals(other.getInvoiceId()))) &&
            this.conformacionOK == other.isConformacionOK();
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
        if (getInvoiceId() != null) {
            _hashCode += getInvoiceId().hashCode();
        }
        _hashCode += (isConformacionOK() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConformarFacturaRequestData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "ConformarFacturaRequestData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invoiceId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "invoiceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "InvoiceIdentificator"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conformacionOK");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "conformacionOK"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
