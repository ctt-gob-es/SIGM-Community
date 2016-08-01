/**
 * ServicioNotificacionesSOAPStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.boe.www.ServicioNotificaciones;

import org.apache.log4j.Logger;

import es.dipucr.sigem.ws.common.firma.SSLUtilities;
import es.dipucr.tablonEdictalUnico.commons.FuncionesComunes;

public class ServicioNotificacionesSOAPStub extends org.apache.axis.client.Stub
		implements es.boe.www.ServicioNotificaciones.ServicioNotificaciones {
	
	public static final Logger logger = Logger.getLogger(ServicioNotificacionesSOAPStub.class);
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector cachedSerQNames = new java.util.Vector();
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	static org.apache.axis.description.OperationDesc[] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[3];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("envioAnuncios");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://www.boe.es/ServicioNotificaciones/", "Envio"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.boe.es/ServicioNotificaciones/", "Envio"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Respuesta"));
		oper.setReturnClass(es.boe.www.ServicioNotificaciones.Respuesta.class);
		oper.setReturnQName(new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Respuesta"));
		oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("consultaEnvio");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://www.boe.es/ServicioNotificaciones/", "IdEnvio"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.boe.es/ServicioNotificaciones/", "IdEnvio"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Respuesta"));
		oper.setReturnClass(es.boe.www.ServicioNotificaciones.Respuesta.class);
		oper.setReturnQName(new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Respuesta"));
		oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[1] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("consultaAnuncio");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://www.boe.es/ServicioNotificaciones/",
						"IdAnuncio"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.boe.es/ServicioNotificaciones/",
						"IdAnuncio"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Respuesta"));
		oper.setReturnClass(es.boe.www.ServicioNotificaciones.Respuesta.class);
		oper.setReturnQName(new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Respuesta"));
		oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[2] = oper;

	}

	public ServicioNotificacionesSOAPStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public ServicioNotificacionesSOAPStub(java.net.URL endpointURL,
			javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public ServicioNotificacionesSOAPStub(javax.xml.rpc.Service service)
			throws org.apache.axis.AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service)
				.setTypeMappingVersion("1.2");
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
		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Anuncio");
		cachedSerQNames.add(qName);
		cls = es.boe.www.ServicioNotificaciones.Anuncio.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Aviso");
		cachedSerQNames.add(qName);
		cls = es.boe.www.ServicioNotificaciones.Aviso.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "CVE");
		cachedSerQNames.add(qName);
		cls = java.lang.String.class;
		cachedSerClasses.add(cls);
		cachedSerFactories
				.add(org.apache.axis.encoding.ser.BaseSerializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
								cls, qName));
		cachedDeserFactories
				.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleDeserializerFactory.class,
								cls, qName));

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Envio");
		cachedSerQNames.add(qName);
		cls = java.lang.String.class;
		cachedSerClasses.add(cls);
		cachedSerFactories
				.add(org.apache.axis.encoding.ser.BaseSerializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
								cls, qName));
		cachedDeserFactories
				.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleDeserializerFactory.class,
								cls, qName));

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Error");
		cachedSerQNames.add(qName);
		cls = es.boe.www.ServicioNotificaciones.Error.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Estado");
		cachedSerQNames.add(qName);
		cls = java.lang.String.class;
		cachedSerClasses.add(cls);
		cachedSerFactories
				.add(org.apache.axis.encoding.ser.BaseSerializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
								cls, qName));
		cachedDeserFactories
				.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleDeserializerFactory.class,
								cls, qName));

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Fecha");
		cachedSerQNames.add(qName);
		cls = java.lang.String.class;
		cachedSerClasses.add(cls);
		cachedSerFactories
				.add(org.apache.axis.encoding.ser.BaseSerializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
								cls, qName));
		cachedDeserFactories
				.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleDeserializerFactory.class,
								cls, qName));

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "IdAnuncio");
		cachedSerQNames.add(qName);
		cls = java.lang.String.class;
		cachedSerClasses.add(cls);
		cachedSerFactories
				.add(org.apache.axis.encoding.ser.BaseSerializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
								cls, qName));
		cachedDeserFactories
				.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleDeserializerFactory.class,
								cls, qName));

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "IdEnvio");
		cachedSerQNames.add(qName);
		cls = java.lang.String.class;
		cachedSerClasses.add(cls);
		cachedSerFactories
				.add(org.apache.axis.encoding.ser.BaseSerializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
								cls, qName));
		cachedDeserFactories
				.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleDeserializerFactory.class,
								cls, qName));

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "ListaAnuncios");
		cachedSerQNames.add(qName);
		cls = es.boe.www.ServicioNotificaciones.ListaAnuncios.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "ListaAvisos");
		cachedSerQNames.add(qName);
		cls = es.boe.www.ServicioNotificaciones.ListaAvisos.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "ListaErrores");
		cachedSerQNames.add(qName);
		cls = es.boe.www.ServicioNotificaciones.ListaErrores.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "NBO");
		cachedSerQNames.add(qName);
		cls = java.lang.String.class;
		cachedSerClasses.add(cls);
		cachedSerFactories
				.add(org.apache.axis.encoding.ser.BaseSerializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
								cls, qName));
		cachedDeserFactories
				.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
						.createFactory(
								org.apache.axis.encoding.ser.SimpleDeserializerFactory.class,
								cls, qName));

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Respuesta");
		cachedSerQNames.add(qName);
		cls = es.boe.www.ServicioNotificaciones.Respuesta.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(
				"http://www.boe.es/ServicioNotificaciones/", "Resultado");
		cachedSerQNames.add(qName);
		cls = es.boe.www.ServicioNotificaciones.Resultado.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

	}

	protected org.apache.axis.client.Call createCall()
			throws java.rmi.RemoteException {
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
						java.lang.Class cls = (java.lang.Class) cachedSerClasses
								.get(i);
						javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames
								.get(i);
						java.lang.Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							java.lang.Class sf = (java.lang.Class) cachedSerFactories
									.get(i);
							java.lang.Class df = (java.lang.Class) cachedDeserFactories
									.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories
									.get(i);
							org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories
									.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (java.lang.Throwable _t) {
			throw new org.apache.axis.AxisFault(
					"Failure trying to get the Call object", _t);
		}
	}

	public es.boe.www.ServicioNotificaciones.Respuesta envioAnuncios(
			java.lang.String envio) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://www.boe.es/ServicioNotificaciones/envioAnuncios");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("",
				"envioAnuncios"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			//SSLUtilities.firmarPeticion(_call);
			FuncionesComunes.firmarPeticion(_call);
			java.lang.Object _resp = _call
					.invoke(new java.lang.Object[] { envio });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (es.boe.www.ServicioNotificaciones.Respuesta) _resp;
				} catch (java.lang.Exception _exception) {
					return (es.boe.www.ServicioNotificaciones.Respuesta) org.apache.axis.utils.JavaUtils
							.convert(
									_resp,
									es.boe.www.ServicioNotificaciones.Respuesta.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			logger.error("Error en el envío. "+axisFaultException.getMessage(), axisFaultException);
			SSLUtilities.imprimirErrorEnvio(_call);
			throw axisFaultException;
		}
	}

	public es.boe.www.ServicioNotificaciones.Respuesta consultaEnvio(
			java.lang.String idEnvio) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://www.boe.es/ServicioNotificaciones/consultaEnvio");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("",
				"consultaEnvio"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			//SSLUtilities.firmarPeticion(_call);
			FuncionesComunes.firmarPeticion(_call);
			java.lang.Object _resp = _call
					.invoke(new java.lang.Object[] { idEnvio });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (es.boe.www.ServicioNotificaciones.Respuesta) _resp;
				} catch (java.lang.Exception _exception) {
					return (es.boe.www.ServicioNotificaciones.Respuesta) org.apache.axis.utils.JavaUtils
							.convert(
									_resp,
									es.boe.www.ServicioNotificaciones.Respuesta.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			logger.error("Error en el envío. "+axisFaultException.getMessage(), axisFaultException);
			SSLUtilities.imprimirErrorEnvio(_call);
			throw axisFaultException;
		}
	}

	public es.boe.www.ServicioNotificaciones.Respuesta consultaAnuncio(
			java.lang.String idAnuncio) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://www.boe.es/ServicioNotificaciones/consultaAnuncio");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("",
				"consultaAnuncio"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			//SSLUtilities.firmarPeticion(_call);
			FuncionesComunes.firmarPeticion(_call);
			java.lang.Object _resp = _call
					.invoke(new java.lang.Object[] { idAnuncio });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (es.boe.www.ServicioNotificaciones.Respuesta) _resp;
				} catch (java.lang.Exception _exception) {
					return (es.boe.www.ServicioNotificaciones.Respuesta) org.apache.axis.utils.JavaUtils
							.convert(
									_resp,
									es.boe.www.ServicioNotificaciones.Respuesta.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			logger.error("Error en el envío. "+axisFaultException.getMessage(), axisFaultException);
			SSLUtilities.imprimirErrorEnvio(_call);
			throw axisFaultException;
		}
	}

}
