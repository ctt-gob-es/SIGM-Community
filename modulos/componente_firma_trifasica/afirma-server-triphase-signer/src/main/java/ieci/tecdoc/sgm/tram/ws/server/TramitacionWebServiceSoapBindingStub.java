/**
 * TramitacionWebServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class TramitacionWebServiceSoapBindingStub extends org.apache.axis.client.Stub implements ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[22];
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getProcedimientosPorTipo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "tipoProc"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "nombre"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ListaInfoBProcedimientos"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getProcedimientosPorTipoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getProcedimientos");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idProcs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ListaInfoBProcedimientos"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getProcedimientosReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getProcedimiento");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idProc"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Procedimiento"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.Procedimiento.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getProcedimientoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getFichero");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "guid"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Binario"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.Binario.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getFicheroReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getFicheroTemp");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "guid"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Binario"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.Binario.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getFicheroTempReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setFicheroTemp");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "guid"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "data"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "setFicheroTempReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getInfoFichero");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "guid"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoFichero"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.InfoFichero.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getInfoFicheroReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getInfoOcupacion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoOcupacion"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.InfoOcupacion.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getInfoOcupacionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("eliminaFicheros");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "guids"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "RetornoServicio"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.RetornoServicio.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "eliminaFicherosReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getIdsExpedientes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idProc"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "fechaIni"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "fechaFin"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "tipoOrd"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ListaIdentificadores"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.ListaIdentificadores.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getIdsExpedientesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getExpedientes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idExps"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ListaInfoBExpedientes"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.ListaInfoBExpedientes.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getExpedientesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getExpediente");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idExp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Expediente"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.Expediente.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getExpedienteReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("archivarExpedientes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idExps"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "RetornoServicio"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.RetornoServicio.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "archivarExpedientesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("iniciarExpediente");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "datosComunes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DatosComunesExpediente"), ieci.tecdoc.sgm.tram.ws.server.DatosComunesExpediente.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "datosEspecificos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "documentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfDocumentoExpedientes"), ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "documento"));
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Booleano"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.Booleano.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "iniciarExpedienteReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("anexarDocsExpediente");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "numExp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "numReg"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "fechaReg"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "documentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfDocumentoExpedientes"), ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "documento"));
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Booleano"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.Booleano.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "anexarDocsExpedienteReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("crearExpediente");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "datosComunes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DatosComunesExpediente"), ieci.tecdoc.sgm.tram.ws.server.DatosComunesExpediente.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "datosEspecificos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "documentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfDocumentoExpedientes"), ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "documento"));
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "initSystem"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Cadena"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.Cadena.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "crearExpedienteReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("cambiarEstadoAdministrativo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "numExp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "estadoAdm"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Booleano"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.Booleano.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "cambiarEstadoAdministrativo"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("moverExpedienteAFase");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "numExp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idFaseCatalogo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Booleano"));
        oper.setReturnClass(ieci.tecdoc.sgm.tram.ws.server.Booleano.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "moverExpedienteAFase"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("busquedaAvanzada");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "groupName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "searchFormName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "searchXML"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "domain"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "Cadena"));
        oper.setReturnClass(ieci.tdw.ispac.services.ws.server.Cadena.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "busquedaAvanzada"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[18] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("establecerDatosRegistroEntidad");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "nombreEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "numExp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "xmlDatosEspecificos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "Entero"));
        oper.setReturnClass(ieci.tdw.ispac.services.ws.server.Entero.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "establecerDatosRegistroEntidad"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[19] = oper;

    }

    private static void _initOperationDesc3(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("obtenerRegistroEntidad");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "nombreEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "numExp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "idRegistro"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "Cadena"));
        oper.setReturnClass(ieci.tdw.ispac.services.ws.server.Cadena.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "obtenerRegistroEntidad"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[20] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("obtenerRegistrosEntidad");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "idEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "nombreEntidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "numExp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "Cadena"));
        oper.setReturnClass(ieci.tdw.ispac.services.ws.server.Cadena.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "obtenerRegistrosEntidad"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[21] = oper;

    }

    public TramitacionWebServiceSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public TramitacionWebServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public TramitacionWebServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "RetornoServicio");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.core.services.dto.RetornoServicio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "Cadena");
            cachedSerQNames.add(qName);
            cls = ieci.tdw.ispac.services.ws.server.Cadena.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "Entero");
            cachedSerQNames.add(qName);
            cls = ieci.tdw.ispac.services.ws.server.Entero.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOf_xsd_string");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfDocElectronico");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.DocElectronico[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocElectronico");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfDocFisico");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.DocFisico[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocFisico");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfDocumentoExpedientes");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocumentoExpediente");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "documento");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfEmplazamiento");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Emplazamiento[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Emplazamiento");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfFirma");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Firma[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Firma");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfInfoBExpediente");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoBExpediente");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfInfoBProcedimiento");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.InfoBProcedimiento[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoBProcedimiento");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfInteresado");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Interesado[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Interesado");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfInteresadoExpediente");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.InteresadoExpediente[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InteresadoExpediente");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ArrayOfOrganoProductor");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.OrganoProductor[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "OrganoProductor");
            qName2 = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Binario");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Binario.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Booleano");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Booleano.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Cadena");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Cadena.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DatosComunesExpediente");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.DatosComunesExpediente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocElectronico");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.DocElectronico.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocFisico");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.DocFisico.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocumentoExpediente");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Emplazamiento");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Emplazamiento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Expediente");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Expediente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Firma");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Firma.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoBExpediente");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoBProcedimiento");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.InfoBProcedimiento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoFichero");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.InfoFichero.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoOcupacion");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.InfoOcupacion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Interesado");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Interesado.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InteresadoExpediente");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.InteresadoExpediente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ListaIdentificadores");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.ListaIdentificadores.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ListaInfoBExpedientes");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.ListaInfoBExpedientes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ListaInfoBProcedimientos");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "OrganoProductor");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.OrganoProductor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Procedimiento");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.Procedimiento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "RetornoServicio");
            cachedSerQNames.add(qName);
            cls = ieci.tecdoc.sgm.tram.ws.server.RetornoServicio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos getProcedimientosPorTipo(java.lang.String idEntidad, int tipoProc, java.lang.String nombre) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getProcedimientosPorTipo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, new java.lang.Integer(tipoProc), nombre});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos getProcedimientos(java.lang.String idEntidad, java.lang.String[] idProcs) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getProcedimientos"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, idProcs});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.Procedimiento getProcedimiento(java.lang.String idEntidad, java.lang.String idProc) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getProcedimiento"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, idProc});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.Procedimiento) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.Procedimiento) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.Procedimiento.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.Binario getFichero(java.lang.String idEntidad, java.lang.String guid) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getFichero"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, guid});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.Binario) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.Binario) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.Binario.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.Binario getFicheroTemp(java.lang.String idEntidad, java.lang.String guid) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getFicheroTemp"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, guid});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.Binario) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.Binario) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.Binario.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean setFicheroTemp(java.lang.String idEntidad, java.lang.String guid, byte[] data) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "setFicheroTemp"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, guid, data});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.InfoFichero getInfoFichero(java.lang.String idEntidad, java.lang.String guid) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getInfoFichero"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, guid});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.InfoFichero) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.InfoFichero) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.InfoFichero.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.InfoOcupacion getInfoOcupacion(java.lang.String idEntidad) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getInfoOcupacion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.InfoOcupacion) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.InfoOcupacion) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.InfoOcupacion.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.RetornoServicio eliminaFicheros(java.lang.String idEntidad, java.lang.String[] guids) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "eliminaFicheros"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, guids});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.RetornoServicio) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.RetornoServicio) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.RetornoServicio.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.ListaIdentificadores getIdsExpedientes(java.lang.String idEntidad, java.lang.String idProc, java.util.Calendar fechaIni, java.util.Calendar fechaFin, int tipoOrd) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getIdsExpedientes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, idProc, fechaIni, fechaFin, new java.lang.Integer(tipoOrd)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.ListaIdentificadores) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.ListaIdentificadores) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.ListaIdentificadores.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.ListaInfoBExpedientes getExpedientes(java.lang.String idEntidad, java.lang.String[] idExps) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getExpedientes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, idExps});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.ListaInfoBExpedientes) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.ListaInfoBExpedientes) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.ListaInfoBExpedientes.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.Expediente getExpediente(java.lang.String idEntidad, java.lang.String idExp) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "getExpediente"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, idExp});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.Expediente) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.Expediente) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.Expediente.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.RetornoServicio archivarExpedientes(java.lang.String idEntidad, java.lang.String[] idExps) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "archivarExpedientes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, idExps});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.RetornoServicio) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.RetornoServicio) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.RetornoServicio.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.Booleano iniciarExpediente(java.lang.String idEntidad, ieci.tecdoc.sgm.tram.ws.server.DatosComunesExpediente datosComunes, java.lang.String datosEspecificos, ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[] documentos) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "iniciarExpediente"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, datosComunes, datosEspecificos, documentos});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.Booleano) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.Booleano) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.Booleano.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.Booleano anexarDocsExpediente(java.lang.String idEntidad, java.lang.String numExp, java.lang.String numReg, java.util.Calendar fechaReg, ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[] documentos) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "anexarDocsExpediente"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, numExp, numReg, fechaReg, documentos});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.Booleano) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.Booleano) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.Booleano.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.Cadena crearExpediente(java.lang.String idEntidad, ieci.tecdoc.sgm.tram.ws.server.DatosComunesExpediente datosComunes, java.lang.String datosEspecificos, ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[] documentos, java.lang.String initSystem) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "crearExpediente"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, datosComunes, datosEspecificos, documentos, initSystem});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.Cadena) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.Cadena) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.Cadena.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.Booleano cambiarEstadoAdministrativo(java.lang.String idEntidad, java.lang.String numExp, java.lang.String estadoAdm) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "cambiarEstadoAdministrativo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, numExp, estadoAdm});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.Booleano) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.Booleano) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.Booleano.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tecdoc.sgm.tram.ws.server.Booleano moverExpedienteAFase(java.lang.String idEntidad, java.lang.String numExp, java.lang.String idFaseCatalogo) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "moverExpedienteAFase"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, numExp, idFaseCatalogo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tecdoc.sgm.tram.ws.server.Booleano) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tecdoc.sgm.tram.ws.server.Booleano) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tecdoc.sgm.tram.ws.server.Booleano.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tdw.ispac.services.ws.server.Cadena busquedaAvanzada(java.lang.String idEntidad, java.lang.String groupName, java.lang.String searchFormName, java.lang.String searchXML, int domain) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "busquedaAvanzada"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, groupName, searchFormName, searchXML, new java.lang.Integer(domain)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tdw.ispac.services.ws.server.Cadena) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tdw.ispac.services.ws.server.Cadena) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tdw.ispac.services.ws.server.Cadena.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tdw.ispac.services.ws.server.Entero establecerDatosRegistroEntidad(java.lang.String idEntidad, java.lang.String nombreEntidad, java.lang.String numExp, java.lang.String xmlDatosEspecificos) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "establecerDatosRegistroEntidad"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, nombreEntidad, numExp, xmlDatosEspecificos});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tdw.ispac.services.ws.server.Entero) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tdw.ispac.services.ws.server.Entero) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tdw.ispac.services.ws.server.Entero.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tdw.ispac.services.ws.server.Cadena obtenerRegistroEntidad(java.lang.String idEntidad, java.lang.String nombreEntidad, java.lang.String numExp, int idRegistro) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[20]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "obtenerRegistroEntidad"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, nombreEntidad, numExp, new java.lang.Integer(idRegistro)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tdw.ispac.services.ws.server.Cadena) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tdw.ispac.services.ws.server.Cadena) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tdw.ispac.services.ws.server.Cadena.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ieci.tdw.ispac.services.ws.server.Cadena obtenerRegistrosEntidad(java.lang.String idEntidad, java.lang.String nombreEntidad, java.lang.String numExp) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[21]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.services.ispac.tdw.ieci", "obtenerRegistrosEntidad"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, nombreEntidad, numExp});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ieci.tdw.ispac.services.ws.server.Cadena) _resp;
            } catch (java.lang.Exception _exception) {
                return (ieci.tdw.ispac.services.ws.server.Cadena) org.apache.axis.utils.JavaUtils.convert(_resp, ieci.tdw.ispac.services.ws.server.Cadena.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
