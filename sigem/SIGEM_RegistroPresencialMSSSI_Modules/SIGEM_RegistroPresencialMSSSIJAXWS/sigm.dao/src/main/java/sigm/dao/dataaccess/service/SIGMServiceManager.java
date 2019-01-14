/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package sigm.dao.dataaccess.service;


public class SIGMServiceManager {

    private static XtfieldService xtfieldService = XtfieldService
	    .getInstance();
    private static IUserUserHdrWSService iUserUserHdrWSService = IUserUserHdrWSService
    	    .getInstance();
    private static OutputRegisterReportsCertService outputRegisterReportsCertService = OutputRegisterReportsCertService
    	    .getInstance();
    private static InputRegisterReportsCertService inputRegisterReportsCertService = InputRegisterReportsCertService
    	    .getInstance();
 
    private static AxpagehService axpagehService = AxpagehService
    	    .getInstance();
    
    public static XtfieldService getXtfieldService() {
    	return xtfieldService;
    }   
    public static IUserUserHdrWSService getiUserUserHdrWSService() {
    	return iUserUserHdrWSService;
    }   
    public static OutputRegisterReportsCertService getOutputRegisterReportsCertService() {
    	return outputRegisterReportsCertService;
    }
    public static InputRegisterReportsCertService getInputRegisterReportsCertService() {
    	return inputRegisterReportsCertService;
    }
    public static AxpagehService getAxpagehService() {
    	return axpagehService;
    }
    
}