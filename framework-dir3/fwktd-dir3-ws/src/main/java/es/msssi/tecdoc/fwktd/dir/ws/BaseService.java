package es.msssi.tecdoc.fwktd.dir.ws;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 * Clase abstracta de la que extenderan los servicios web de dir3.
 * 
 * @author cmorenog
 * 
 */
public abstract class BaseService {
	
	@Resource
	WebServiceContext wsContext;
	
}
