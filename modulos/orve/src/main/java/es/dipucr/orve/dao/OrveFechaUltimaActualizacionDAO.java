package es.dipucr.orve.dao;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import java.util.Date;

public class OrveFechaUltimaActualizacionDAO extends ObjectDAO {

    private static final long serialVersionUID = 1L;
    
    public static final String TABLENAME     = "ORVE_FECHA_ULTIMA_ACTUALIZACION";
    static final String IDSEQUENCE     = "SPAC_SQ_ID_ORVEFECHAULTIMAACTUALIZACION";
    static final String IDKEY         = "ID";
    
    //Mapeo de las columnas de la tabla
    public static final String FECHA_ULTIMA_ACTUALIZACION = "FECHA_ULTIMA_ACTUALIZACION";
    public static final String CORREO_ENVIADO = "CORREO_ENVIADO";
    
    public static final String CORREO_ENVIADO_SI = "SI";
    public static final String CORREO_ENVIADO_NO = "NO";
    
    /**
     * Constructor vacio
     * @throws ISPACException
     */
    public OrveFechaUltimaActualizacionDAO() throws ISPACException {
        super(null);
    }
    
    /**
     * @param cnt
     * @throws ISPACException
     */
    public OrveFechaUltimaActualizacionDAO(DbCnt cnt) throws ISPACException {
        super(cnt, null);
    }
  
    /**
     * Contructor que carga una determinada plantilla
     * @param cnt conexion
     * @param id identificador de la plantilla
     * @throws ISPACException
     */
     public OrveFechaUltimaActualizacionDAO(DbCnt cnt, int id) throws ISPACException {        
         super(cnt, id, null);
     }    
    
    /**
     * Devuelve la query por defecto
     * @return query
     * @throws ISPACException 
     */
     protected String getDefaultSQL(int nActionDAO) throws ISPACException {
         return " WHERE " + IDKEY + " = " + getInt(IDKEY);
     }
  
     /**
      * Devuelve el nombre del campo clave primaria
      * @return nombre de la clave primaria
      * @throws ISPACException 
      */
     public String getKeyName() throws ISPACException {
         return IDKEY;
     }
  
     /**
      * Devuelve el nombre de la tabla
      * @return Nombre de la tabla
      * @throws ISPACException 
      */
     public String getTableName()     {
         return TABLENAME;
     }
  
     /**
      * Para crear un nuevo registro con identificador proporcionado
      * por la secuencia correspondiente
      * @param cnt conexion
      * @throws ISPACException
      */
     public void newObject(DbCnt cnt) throws ISPACException {
         set(IDKEY, IdSequenceMgr.getIdSequence(cnt, IDSEQUENCE));
     }    
     
     public static OrveFechaUltimaActualizacionDAO getFechaUltimaActualizacion(DbCnt cnt) throws ISPACException {
         
        CollectionDAO collection = new CollectionDAO(OrveFechaUltimaActualizacionDAO.class);
        collection.query(cnt, "");
         
        if (collection.next()) {
            return (OrveFechaUltimaActualizacionDAO) collection.value();
        }
         
        return null;
     }
     
     public void setFechaUltimaActualizacion(Date fechaUltimaActualizacion) throws ISPACException{
         this.set(FECHA_ULTIMA_ACTUALIZACION, fechaUltimaActualizacion);
     }
     
     public void setCorreoEnviado(boolean correoEnviado) throws ISPACException{
         if(correoEnviado){
             this.set(CORREO_ENVIADO, CORREO_ENVIADO_SI);             
         } else {
             this.set(CORREO_ENVIADO, CORREO_ENVIADO_NO);
         }
     }
     
     public boolean isCorreoEnviado() throws ISPACException{
         return CORREO_ENVIADO_SI.equals(this.get(CORREO_ENVIADO));
     }
}
