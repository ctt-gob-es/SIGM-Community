/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.provider;

import com.ieci.tecdoc.isicres.context.ISicresBeansProvider;

import es.ieci.tecdoc.isicres.terceros.business.manager.TerceroManager;


/**
 * Clase que implementa el proveedor de terceros.
 * 
 * @author cmorenog
 */
public class ISicresBeanTercerosProvider extends ISicresBeansProvider{
	
	private static ISicresBeanTercerosProvider instance=null;

	private static final String TERCEROS_MANAGER_BEAN = "terceroManager";
	
	/**
	 * instancia el proveedor.
	 * 
	 * @return proveedor
	 *             El proveedor de terceros.
	 */
	public static ISicresBeanTercerosProvider getInstance(){
		if (instance==null){
			instance = new ISicresBeanTercerosProvider();
		}
		return instance;
	}
	
	/**
	 * devuelve el manejador de terceros.
	 * 
	 * @return terceroManager
	 *             El manager de terceros.
	 */
	public TerceroManager getTerceroManager() {
		TerceroManager result = (TerceroManager) getGenericBean(TERCEROS_MANAGER_BEAN);
		return result;
	}

}
