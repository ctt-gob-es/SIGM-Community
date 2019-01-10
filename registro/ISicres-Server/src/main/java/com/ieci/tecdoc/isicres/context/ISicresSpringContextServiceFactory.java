package com.ieci.tecdoc.isicres.context;

import org.springframework.context.ApplicationContext;


public class ISicresSpringContextServiceFactory {

	public static final int SPRING_INTEGRATION=1;
	public static final int NON_SPRING_INTEGRATION=2;


	public static ApplicationContext getInstance(int type) {
		ApplicationContext result = null;

		switch (type) {

		case SPRING_INTEGRATION:
			result = (ApplicationContext) ISicresSpringAppContext.getApplicationContext();
			break;

		case NON_SPRING_INTEGRATION:
			//result = new ISicresServiceNonSpringImpl();

			//[Dipucr-Manu Ticket#381] - INICIO - ALSIGM3 Registro Presencial va súper lento.
			new  ISicresServiceNonSpringImpl(false);

			result = (ApplicationContext) ISicresSpringAppContext.getApplicationContext();
			
			if(null == result){
				new  ISicresServiceNonSpringImpl();
				
				result = (ApplicationContext) ISicresSpringAppContext.getApplicationContext();
			}
			//[Dipucr-Manu Ticket#381] - FIN - ALSIGM3 Registro Presencial va súper lento.

			break;

		default:
			break;
		}

		return result;

	}

	/**
	 * @param type
	 * @param params
	 * @return
	 */
	public static ApplicationContext getInstance(int type,
			String... params) {
		ApplicationContext result = null;

		switch (type) {

		case SPRING_INTEGRATION:
			result = (ApplicationContext) ISicresSpringAppContext.getApplicationContext();
			break;

		case NON_SPRING_INTEGRATION:
			new ISicresServiceNonSpringImpl(params[0]);

			result = (ApplicationContext) ISicresSpringAppContext.getApplicationContext();

			break;

		default:
			break;
		}

		return result;

	}
}
