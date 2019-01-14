/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package sigm.dao.language;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import core.tools.BaseResourceBoundle;

public class Sigmdao extends BaseResourceBoundle {

	public static final String DEFAULT_NAME = "sigmdaoMsg";

	private static HashMap<Locale, Sigmdao> instances;

	private static Locale defaultLocale = new Locale("ES", "es");

	public static Locale getDefaultLocale() {
		return defaultLocale;
	}

	public static void setDefaultLocale(Locale newDefaultLocale) {
		defaultLocale = newDefaultLocale;
	}

	public static Sigmdao getInstance() {
		return getInstance(getDefaultLocale());
	}

	public static Sigmdao getInstance(Locale locale) {
		if (locale == null) {
			locale = getDefaultLocale();
		}
		Sigmdao dao = null;
		if (instances == null) {
			instances = new HashMap<Locale, Sigmdao>();
		}
		if (instances.containsKey(locale)) {
			dao = instances.get(locale);
		} else {
			dao = new Sigmdao(locale);
			instances.put(locale, dao);
		}
		return dao;
	}

	private Sigmdao(Locale locale) {
		setParent(ResourceBundle.getBundle(DEFAULT_NAME, locale));
	}

}