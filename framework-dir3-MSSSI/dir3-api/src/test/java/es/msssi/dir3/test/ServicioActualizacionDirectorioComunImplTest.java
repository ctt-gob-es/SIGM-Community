/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import es.msssi.dir3.api.service.impl.UpdateServiceDCOImpl;
import es.msssi.dir3.core.errors.DIR3Exception;

@ContextConfiguration({ "/jndi.xml", "/beans/cxf.xml", "/beans/fwktd-dir3-test-beans.xml",
    "/beans/dir3-api-test-applicationContext.xml" })
public class ServicioActualizacionDirectorioComunImplTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private UpdateServiceDCOImpl apiServicioActualizacionDirectorioComunImpl;

    protected UpdateServiceDCOImpl getServicioConsultaDirectorioComun() {
	return apiServicioActualizacionDirectorioComunImpl;
    }

    @Test
    public void actualizarDCtest1() {
	try {
	    apiServicioActualizacionDirectorioComunImpl.updateDCO();
	}
	catch (DIR3Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
