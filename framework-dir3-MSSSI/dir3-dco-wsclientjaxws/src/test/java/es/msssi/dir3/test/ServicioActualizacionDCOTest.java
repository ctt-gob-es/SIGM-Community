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

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import es.msssi.dir3.exception.GetFileDownloadDCOException;
import es.msssi.dir3.services.DownloadServiceDCO;

@ContextConfiguration(locations = { "/beans/dir3-dco-wsclient-applicationContext.xml" })
public class ServicioActualizacionDCOTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    public DownloadServiceDCO dcoDownloadServiceDCO;

    public DownloadServiceDCO getDcoDownloadServiceDCO() {
	return dcoDownloadServiceDCO;
    }

    public void setServicioActualizacionDCO(
	DownloadServiceDCO dcoDownloadServiceDCO) {
	this.dcoDownloadServiceDCO = dcoDownloadServiceDCO;
    }

    @Test
    public void actualizacionOficinasTest() {
	Date lastDateUpdate = new Date(
	    "01/05/2004");
	try {
	    dcoDownloadServiceDCO.getFileUpdateOfficesDCO(lastDateUpdate);
	}
	catch (GetFileDownloadDCOException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void actualizacionUnidadesTest() {
	Date lastDateUpdate = Calendar.getInstance().getTime();

	try {
	    dcoDownloadServiceDCO.getFileUpdateUnitsDCO(lastDateUpdate);
	}
	catch (GetFileDownloadDCOException e) {

	    e.printStackTrace();
	}
    }
}
