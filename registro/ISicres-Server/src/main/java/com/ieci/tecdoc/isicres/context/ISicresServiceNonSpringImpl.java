package com.ieci.tecdoc.isicres.context;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ISicresServiceNonSpringImpl {

	protected static ISicresServiceNonSpringImpl _instance = null;


	public synchronized static ISicresServiceNonSpringImpl getInstance() {
		if (_instance == null) {
			_instance = new ISicresServiceNonSpringImpl();
		}

		return _instance;
	}

	/**
	 * constructor implementacion por defecto del a traves del modulo api
	 */
	public ISicresServiceNonSpringImpl() {
		super();
		new ClassPathXmlApplicationContext(
				"/beans/isicres-server-applicationContext.xml");
	}
	
	/**
	* [Dipucr-Manu Ticket#381] - INICIO - ALSIGM3 Registro Presencial va súper lento.
	* constructor implementacion por defecto del a traves del modulo api
	*/
	public ISicresServiceNonSpringImpl(boolean refresh) {
		super();
		new ClassPathXmlApplicationContext(new String[] {"/beans/isicres-server-applicationContext.xml"}, refresh);
	}
	//[Dipucr-Manu Ticket#381] - fin - ALSIGM3 Registro Presencial va súper lento.

	/**
	 * constructor para implementaciones personalizadas
	 *
	 * @param contextPath
	 */
	public ISicresServiceNonSpringImpl(String contextPath) {
		super();
		new ClassPathXmlApplicationContext(
				"/beans/isicres-server-applicationContext.xml",
				contextPath);
	}

}
