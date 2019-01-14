package es.dipucr.orve.dao;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import java.util.Date;

public class OrveHistoRegistroDAO extends ObjectDAO {

    private static final long serialVersionUID = 1L;
    
    public static final String TABLENAME     = "ORVE_HISTO_REGISTRO";
    static final String IDSEQUENCE     = "SPAC_SQ_ID_ORVEHISTOREGISTRO";
    static final String IDKEY         = "ID";
    
    //Mapeo de las columnas de la tabla
    public static final String IDENTIFICADOR_ORVE = "IDENTIFICADOR_ORVE";
    public static final String NREG_SIGEM = "NREG_SIGEM";
    public static final String FECHA_REGISTRO = "FECHA_REGISTRO";
    
    /**
     * Constructor vacio
     * @throws ISPACException
     */
    public OrveHistoRegistroDAO() throws ISPACException {
        super(null);
    }
    
    /**
     * @param cnt
     * @throws ISPACException
     */
    public OrveHistoRegistroDAO(DbCnt cnt) throws ISPACException {
        super(cnt, null);
    }
  
    /**
     * Contructor que carga una determinada plantilla
     * @param cnt conexion
     * @param id identificador de la plantilla
     * @throws ISPACException
     */
     public OrveHistoRegistroDAO(DbCnt cnt, int id) throws ISPACException {        
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
       
     public void setIdentificadorOrve(int identificadorOrve) throws ISPACException{
         this.set(IDENTIFICADOR_ORVE, identificadorOrve);
     }
     
     public void setNregSigem(String nregSigem) throws ISPACException{
         this.set(NREG_SIGEM, nregSigem);
     }
     
     public void setFechaRegistro(Date fechaRegistro) throws ISPACException{
         this.set(FECHA_REGISTRO, fechaRegistro);
     }
     
     public static OrveHistoRegistroDAO getByIdentificadorOrve(DbCnt cnt, int identificadorOrve) throws ISPACException {
            
        String sql = "WHERE " + IDENTIFICADOR_ORVE + " = " + identificadorOrve;
         
        CollectionDAO collection = new CollectionDAO(OrveHistoRegistroDAO.class);
        collection.query(cnt,sql);
        
        if (collection.next()) {
            return (OrveHistoRegistroDAO) collection.value();
        }
        
        return null;
    }
}
