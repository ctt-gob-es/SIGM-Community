/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.utils;


/**
 * Constantes de SIGMADMIN.
 * 
 * @author cmorenog
 * 
 */
public class SIGMADMINConstants {

    /**
     * indica sin permisos
     */
    public static final int OBJ_PERM_NONE = 0;

    /**
     * indica permiso de consulta
     */
    public static final int OBJ_PERM_QUERY = 1;

    /**
     * indica permiso de actualizaciÃ³n
     */
    public static final int OBJ_PERM_UPDATE = 2;

    /**
     * indica permiso de creaciÃ³n
     */
    public static final int OBJ_PERM_CREATION = 4;

    /**
     * indica permiso de borrado
     */
    public static final int OBJ_PERM_DELETION = 8;

    /**
     * Indica que el usuario es al que se aplican los permisos.
     */
    public static final int DESTINATION_USER = 1;

    /**
     * Indica que el departamento es al que se aplican los permisos.
     */
    public static final int DESTINATION_DEPT = 2;

    public static final int OBJECT_OWNER_TYPE_DIRECTORY = 4;
    public static final int OBJECT_OWNER_TYPE_ARCHIVE = 5;
    public static final int OBJECT_OWNER_TYPE_FOLDER = 6;
    public static final int OBJECT_OWNER_TYPE_FORMAT = 7;
    public static final int OBJECT_OWNER_TYPE_REPORT = 8;

    public static final int OBJECT_OWNER_TYPE_USER = 1;
    public static final int OBJECT_OWNER_TYPE_DEPT = 2;
    public static final int OBJECT_OWNER_TYPE_GROUP = 3;

    /**
     * NingÃºn permiso.
     */
    public static final int PERMISSION_NONE = 0;

    /**
     * Permiso de bÃºsqueda.
     */
    public static final int PERMISSION_QUERY = 1;

    /**
     * Permiso de actualizaciÃ³n.
     */
    public static final int PERMISSION_UPDATE = 2;

    /**
     * Permiso de creaciÃ³n.
     */
    public static final int PERMISSION_CREATION = 4;

    /**
     * Permiso de borrado.
     */
    public static final int PERMISSION_DELETION = 8;

    /**
     * Permiso de impresiÃ³n.
     */
    public static final int PERMISSION_PRINTING = 16;

    /**
     * Todos los permisos.
     */
    public static final int PERMISSION_ALL = 31;

    /**
     * Identificador de producto de las <b>Herramientas de Sistema</b>.
     */
    public static final int PRODUCT_SYSTEM = 1;

    /**
     * Identificador de producto del <b>Administrador de Usuarios</b>.
     */
    public static final int PRODUCT_USER = 2;

    /**
     * Identificador de producto de la <b>AplicaciÃ³n de Consulta</b>.
     */
    public static final int PRODUCT_IDOC = 3;

    /**
     * Identificador de producto de <b>invesFlow</b>.
     */
    public static final int PRODUCT_IFLOW = 4;

    /**
     * Identificador de producto de <b>inveSicres</b>.
     */
    public static final int PRODUCT_ISICRES = 5;

    /**
     * Identificador de producto del <b>Administrador de VolÃºmenes</b>.
     */
    public static final int PRODUCT_VOLUME = 6;

    /**
     * Identificador del producto del <b>Catalogo de Tramites</b>
     */
    public static final int PRODUCT_CATALOG = 7;
    /**
     * Identificador del <b>portal</b>
     */
    public static final int PRODUCT_PORTAL = 8;

    public static final int APLICACION_ADMINISTRACION = 1;
    public static final int APLICACION_ESTRUCTURA_ORGANIZATIVA = 2;
    public static final int APLICACION_CATALOGO_PROCEDIMIENTOS = 3;
    public static final int APLICACION_ARCHIVO = 4;
    public static final int APLICACION_REGISTRO = 5;
    public static final int APLICACION_REPOSITORIOS_DOCUMENTALES = 6;
    public static final int APLICACION_CATALOGO_TRAMITES = 7;
    public static final int APLICACION_USUARIOS_PORTAL = 8;
    public static final int APLICACION_CONSULTA_EXPEDIENTES = 9;
    public static final int APLICACION_CONSULTA_REGISTROS_TELEMATICOS = 10;
    
    /**
     * Identificador de perfil <b>Sin Derechos</b>.
     */
    public static final int PROFILE_NONE = 0;

    /**
     * Identificador de perfil <b>EstÃ¡ndar</b>.
     */
    public static final int PROFILE_STANDARD = 1;

    /**
     * Identificador de perfil <b>Administrador</b>.
     */
    public static final int PROFILE_MANAGER = 2;

    /**
     * Identificador de perfil <b>Superusuario</b>.
     */
    public static final int PROFILE_SUPERUSER = 3;

    /**
     * Permisos genéricos con Intercambio Registral
     */
    public static final int GENERIC_PERMS_USER_WITH_IR = 8191;
    
    /**
     * Permisos genéricos sin Intercambio Registral
     */
	public static final int GENERIC_PERMS_USER_WITHOUT_IR = 8175;
}