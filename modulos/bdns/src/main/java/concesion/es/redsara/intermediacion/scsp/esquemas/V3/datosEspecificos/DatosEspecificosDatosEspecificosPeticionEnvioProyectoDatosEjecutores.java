/**
 * DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutoresEjecutores[] ejecutores;

    public DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores() {
    }

    public DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutoresEjecutores[] ejecutores) {
           this.ejecutores = ejecutores;
    }


    /**
     * Gets the ejecutores value for this DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores.
     * 
     * @return ejecutores
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutoresEjecutores[] getEjecutores() {
        return ejecutores;
    }


    /**
     * Sets the ejecutores value for this DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores.
     * 
     * @param ejecutores
     */
    public void setEjecutores(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutoresEjecutores[] ejecutores) {
        this.ejecutores = ejecutores;
    }

    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutoresEjecutores getEjecutores(int i) {
        return this.ejecutores[i];
    }

    public void setEjecutores(int i, concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutoresEjecutores _value) {
        this.ejecutores[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores)) return false;
        DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores other = (DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ejecutores==null && other.getEjecutores()==null) || 
             (this.ejecutores!=null &&
              java.util.Arrays.equals(this.ejecutores, other.getEjecutores())));
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
        if (getEjecutores() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEjecutores());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEjecutores(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionEnvioProyectoDatosEjecutores.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Envio>Proyecto>DatosEjecutores"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ejecutores");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Ejecutores"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>DatosEspecificos>DatosEspecificosPeticion>Envio>Proyecto>DatosEjecutores>Ejecutores"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
