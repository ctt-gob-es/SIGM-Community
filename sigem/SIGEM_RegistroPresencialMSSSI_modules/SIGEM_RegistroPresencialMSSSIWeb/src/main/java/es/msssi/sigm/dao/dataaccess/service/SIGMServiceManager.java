package es.msssi.sigm.dao.dataaccess.service;

/**
 * Clase de gestion de Servicios de acceso a base de datos.
 * 
 * */
public class SIGMServiceManager {

    private static IUserUserHdrService iUserUserHdrService = IUserUserHdrService.getInstance();
    
    private static IUserUserHdrOBService iUserUserHdrOBService = IUserUserHdrOBService.getInstance();
    
    /**
     * iUserUserHdrService.
     */
    public static IUserUserHdrService getiUserUserHdrService() {
    	return iUserUserHdrService;
    }
    /**
     * iUserUserHdrOBService.
     */
    public static IUserUserHdrOBService getiUserUserHdrOBService() {
    	return iUserUserHdrOBService;
    }
}