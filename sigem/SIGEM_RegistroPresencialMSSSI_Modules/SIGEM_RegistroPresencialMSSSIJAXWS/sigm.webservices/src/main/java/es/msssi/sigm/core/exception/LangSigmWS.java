/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import core.tools.BaseResourceBoundle;
import es.msssi.sigm.core.util.Constants;

public class LangSigmWS extends BaseResourceBoundle {

	public static final String DEFAULT_NAME = Constants.PREFIX_MESSAGE_TEXT;

	private static HashMap<Locale, LangSigmWS> instances;

	private static Locale defaultLocale = new Locale("ES", "es");

	public static Locale getDefaultLocale() {
		return defaultLocale;
	}

	public static void setDefaultLocale(Locale newDefaultLocale) {
		defaultLocale = newDefaultLocale;
	}

	public static LangSigmWS getInstance() {
		return getInstance(getDefaultLocale());
	}

	public static LangSigmWS getInstance(Locale locale) {
		if (locale == null) {
			locale = getDefaultLocale();
		}
		LangSigmWS langClass = null;
		if (instances == null) {
			instances = new HashMap<Locale, LangSigmWS>();
		}
		if (instances.containsKey(locale)) {
			langClass = instances.get(locale);
		} else {
			langClass = new LangSigmWS(locale);
			instances.put(locale, langClass);
		}
		return langClass;
	}

	private LangSigmWS(Locale locale) {
		setParent(ResourceBundle.getBundle(DEFAULT_NAME, locale));
	}

}