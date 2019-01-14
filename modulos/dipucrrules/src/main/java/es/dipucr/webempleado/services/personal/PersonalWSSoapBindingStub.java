/**
 * PersonalWSSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.personal;

public class PersonalWSSoapBindingStub extends org.apache.axis.client.Stub implements es.dipucr.webempleado.services.personal.PersonalWS {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[19];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getProvision");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codigo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Provision"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Provision.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getProvisionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getEscala");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codigo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Escalas"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Escalas.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getEscalaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getSubescala");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codigo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Subescalas"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Subescalas.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getSubescalaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getClase");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codigo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Clases"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Clases.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getClaseReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTipoHorario");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codigo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "HorariosId"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.HorariosId.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getTipoHorarioReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getServiciosByCodigo");
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Servicios"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Servicios[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getServiciosByCodigoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getServiciosByNombre");
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Servicios"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Servicios[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getServiciosByNombreReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPuestoComplemento");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codServicio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "numPuesto"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "PuestoComplementos"));
        oper.setReturnClass(es.dipucr.webempleado.domain.beans.PuestoComplementos.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuestoComplementoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPuestosComplementosVacantes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codServicio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "PuestoComplementos"));
        oper.setReturnClass(es.dipucr.webempleado.domain.beans.PuestoComplementos[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuestosComplementosVacantesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPuestosComplementos");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codServicio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "PuestoComplementos"));
        oper.setReturnClass(es.dipucr.webempleado.domain.beans.PuestoComplementos[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuestosComplementosReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getHorarios");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "HorariosId"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.HorariosId[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getHorariosReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getGrupos");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getGruposReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getProvisiones");
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Provision"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Provision[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getProvisionesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getExpediente");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "nif"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Expediente"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Expediente.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getExpedienteReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getVidaLaboral");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "nif"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "PeriodoLaboral"));
        oper.setReturnClass(es.dipucr.webempleado.domain.beans.PeriodoLaboral[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getVidaLaboralReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPuestos");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codServicio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Puestos"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Puestos[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuestosReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPuesto");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codigo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Puestos"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Puestos.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuestoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getCategoria");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codigo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Categorias"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.Categorias.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getCategoriaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTipoPuesto");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "codigo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "TipoP"));
        oper.setReturnClass(es.dipucr.webempleado.model.mapping.TipoP.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getTipoPuestoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[18] = oper;

    }

    public PersonalWSSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PersonalWSSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PersonalWSSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "PeriodoLaboral");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.domain.beans.PeriodoLaboral.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "PuestoComplementos");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.domain.beans.PuestoComplementos.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Categorias");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Categorias.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Clases");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Clases.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Escalas");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Escalas.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Expediente");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Expediente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Fper020");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Fper020.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Fper210");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Fper210.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Fper210Id");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Fper210Id.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "HorariosId");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.HorariosId.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "HorPersonal");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.HorPersonal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "HorPersonalId");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.HorPersonalId.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Provision");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Provision.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Puestos");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Puestos.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Servicios");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Servicios.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Subescalas");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.Subescalas.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "TipoP");
            cachedSerQNames.add(qName);
            cls = es.dipucr.webempleado.model.mapping.TipoP.class;
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

    public es.dipucr.webempleado.model.mapping.Provision getProvision(java.lang.String codigo) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getProvision"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codigo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Provision) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Provision) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Provision.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Escalas getEscala(java.lang.String codigo) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getEscala"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codigo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Escalas) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Escalas) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Escalas.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Subescalas getSubescala(java.lang.String codigo) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getSubescala"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codigo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Subescalas) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Subescalas) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Subescalas.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Clases getClase(java.lang.String codigo) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getClase"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codigo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Clases) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Clases) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Clases.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.HorariosId getTipoHorario(java.lang.String codigo, java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getTipoHorario"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codigo, anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.HorariosId) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.HorariosId) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.HorariosId.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Servicios[] getServiciosByCodigo() throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getServiciosByCodigo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Servicios[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Servicios[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Servicios[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Servicios[] getServiciosByNombre() throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getServiciosByNombre"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Servicios[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Servicios[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Servicios[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.domain.beans.PuestoComplementos getPuestoComplemento(java.lang.String codServicio, java.lang.String numPuesto, java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuestoComplemento"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codServicio, numPuesto, anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.domain.beans.PuestoComplementos) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.domain.beans.PuestoComplementos) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.domain.beans.PuestoComplementos.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.domain.beans.PuestoComplementos[] getPuestosComplementosVacantes(java.lang.String codServicio, java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuestosComplementosVacantes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codServicio, anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.domain.beans.PuestoComplementos[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.domain.beans.PuestoComplementos[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.domain.beans.PuestoComplementos[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.domain.beans.PuestoComplementos[] getPuestosComplementos(java.lang.String codServicio, java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuestosComplementos"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codServicio, anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.domain.beans.PuestoComplementos[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.domain.beans.PuestoComplementos[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.domain.beans.PuestoComplementos[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.HorariosId[] getHorarios(java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getHorarios"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.HorariosId[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.HorariosId[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.HorariosId[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String[] getGrupos() throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getGrupos"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String[]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Provision[] getProvisiones() throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getProvisiones"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Provision[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Provision[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Provision[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Expediente getExpediente(java.lang.String nif) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getExpediente"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nif});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Expediente) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Expediente) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Expediente.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.domain.beans.PeriodoLaboral[] getVidaLaboral(java.lang.String nif) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getVidaLaboral"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nif});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.domain.beans.PeriodoLaboral[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.domain.beans.PeriodoLaboral[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.domain.beans.PeriodoLaboral[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Puestos[] getPuestos(java.lang.String codServicio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuestos"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codServicio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Puestos[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Puestos[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Puestos[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Puestos getPuesto(java.lang.String codigo) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getPuesto"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codigo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Puestos) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Puestos) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Puestos.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.Categorias getCategoria(java.lang.String codigo) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getCategoria"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codigo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.Categorias) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.Categorias) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.Categorias.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.webempleado.model.mapping.TipoP getTipoPuesto(java.lang.String codigo) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "getTipoPuesto"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codigo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.webempleado.model.mapping.TipoP) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.webempleado.model.mapping.TipoP) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.webempleado.model.mapping.TipoP.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
