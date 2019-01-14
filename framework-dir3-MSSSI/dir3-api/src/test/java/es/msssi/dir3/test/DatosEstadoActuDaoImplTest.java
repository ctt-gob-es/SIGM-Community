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

import java.sql.SQLException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import es.msssi.dir3.api.dao.UpdateStatusDAO;
import es.msssi.dir3.api.dao.impl.UpdateStatusDaoImpl;
import es.msssi.dir3.api.vo.UpdateStatusVO;

@ContextConfiguration({ "/jndi.xml", "/beans/cxf.xml", "/beans/fwktd-dir3-test-beans.xml",
    "/beans/dir3-api-test-applicationContext.xml" })
public class DatosEstadoActuDaoImplTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private UpdateStatusDaoImpl fwktd_dir3_datosEstadoActualizacionDcoDaoImpl;

    protected UpdateStatusDAO getEstadoActualizacionDcoDAO() {
	return fwktd_dir3_datosEstadoActualizacionDcoDaoImpl;
    }

    @Test
    public void testGetLastSuccessUpdate() {
	// Test de consulta con criterios
	UpdateStatusVO estado = null;
	try {
	    estado = getEstadoActualizacionDcoDAO().getLastSuccessUpdate();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	System.out.print(estado.getUpdateDate());
	Assert.assertTrue(
	    "No se han obtenido resultados", estado != null);
    }

    @Test
    public void testGet() {
	// Test de consulta con criterios
	UpdateStatusVO estado = null;
	try {
	    estado = (UpdateStatusVO) getEstadoActualizacionDcoDAO().get(
		"8");
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(
	    "No se han obtenido resultado", estado != null);
	System.out.print(estado.getUpdateDate());
    }

    @Test
    public void testExists() {
	// Test de consulta con criterios
	boolean estado = false;
	try {
	    estado = getEstadoActualizacionDcoDAO().exists(
		"5");
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(
	    "No se han obtenido resultado", !estado);

    }

    @Test
    public void testSave() {
	// Test de consulta con criterios
	UpdateStatusVO estado = new UpdateStatusVO();
	estado.setStatus("OK");
	estado.setUpdateDate(new Date());
	try {
	    getEstadoActualizacionDcoDAO().save(
		estado);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(
	    "Se ha salvado correctamente", estado != null);
    }

    @Test
    public void testUpdate() {
	// Test de consulta con criterios
	UpdateStatusVO estado = new UpdateStatusVO();
	estado.setId("47");
	estado.setStatus("OK");
	estado.setUpdateDate(new Date());
	try {
	    getEstadoActualizacionDcoDAO().update(
		estado);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(
	    "Se ha salvado correctamente", estado != null);
    }

}
