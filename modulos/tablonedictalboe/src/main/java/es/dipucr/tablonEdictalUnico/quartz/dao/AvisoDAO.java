package es.dipucr.tablonEdictalUnico.quartz.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import es.dipucr.tablonEdictalUnico.commons.Constantes;
import es.dipucr.tablonEdictalUnico.quartz.bean.Aviso;

public class AvisoDAO extends GenericoDAO{
	private static Logger logger = Logger.getLogger(AvisoDAO.class);
	
	public AvisoDAO(){
		super();
	}
	
	public int nextIdAviso(String entidad){
		
		int result=0;
		
		try {
		    Connection con;
			con = this.getConexion(entidad, Constantes.BBDD_TRAMITADOR);
		
		    //String query = "SELECT id_aviso FROM spac_avisos_electronicos ORDER BY id_aviso DESC Limit 1";
			String query = "select nextval('spac_sq_id_avisos_electronicos')";
		    Statement sentencia = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);			
			
			ResultSet r = sentencia.executeQuery(query);
			
			while(r.next()){				
				result = r.getInt(Constantes.AVISO_ID_AVISO);				
			}
			
			r.close();
			r=null;
			sentencia.close();
			sentencia=null;
			con.close();
			con=null;
			
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
		
		return result;
		
	}
	
    public void insertarAviso(String entidad, Aviso aviso){
    	
    	try {
			Connection con;
			con = this.getConexion(entidad, Constantes.BBDD_TRAMITADOR);
			
			String query = "INSERT INTO spac_avisos_electronicos (ID_AVISO,"+
																"ID_PROC," +
																//" ID_RESP," +
																" TIPO_DESTINATARIO," +
																//" ID_DESTINATARIO, " +
																" FECHA," +
																" ID_EXPEDIENTE," +
																" ESTADO_AVISO," +
																" MENSAJE," +
																" TIPO_AVISO," +
																//" ID_MPROC" +
																//" UID_RESP" +
																" UID_DESTINATARIO)" +
	                        " VALUES (" +
	                        									aviso.getId_aviso() +
	                        									"," + aviso.getId_proc() +
																"," + aviso.getTipo_destinatario() +
																",'" + aviso.getFecha() + "'"+
																",'" + aviso.getId_expediente() + "'"+
																"," + aviso.getEstado_aviso() + 
																",'" + aviso.getMensaje() + "'"+
																",'" + aviso.getTipo_aviso() + "'"+
																",'" + aviso.getUid_destinatario() + "'"+ ")";
			
		    Statement sentencia = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);			
			sentencia.execute(query);
			
			sentencia.close();
			sentencia=null;
			con.close();
			con=null;
			
			logger.info("INSERTADO AVISO PARA EL EXPEDIENTE " + aviso.getId_expediente());
		} catch (Exception e) {			
			logger.error("Error en la entidad. " + entidad + "." + e.getMessage(),e);
		}
		
	}
}
