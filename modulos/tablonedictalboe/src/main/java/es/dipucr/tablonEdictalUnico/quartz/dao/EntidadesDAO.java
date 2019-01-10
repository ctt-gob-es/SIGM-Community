package es.dipucr.tablonEdictalUnico.quartz.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import es.dipucr.tablonEdictalUnico.quartz.bean.Entidad;


public class EntidadesDAO extends GenericoDAO {
	private static final Logger logger = Logger.getLogger(" es.dipucr.sigem.consolidacion.dao.EntidadesDAO");
	
	public EntidadesDAO(){
		super();
	}
	
	public ArrayList<Entidad> dameEntidadesAyuntamientos() throws Exception{
		
		logger.info("Entra en dameEntidades");
		
		ArrayList<Entidad> entidades= new ArrayList<Entidad>();
		GenericoDAO dao = new GenericoDAO();		
		
		try {
		    Connection con;
			con = dao.getConexionPostgresDipucr("sigemAdmin");
		
//		    String query = "SELECT id,nombrecorto FROM sgm_adm_entidades ORDER BY nombrecorto";
			String query = "SELECT id,nombrecorto FROM sgm_adm_entidades ORDER BY id";
		    Statement sentencia = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);			
			
			ResultSet r = sentencia.executeQuery(query);
			
			while(r.next()){
				
				Entidad aux = new Entidad();
				
				aux.setId(r.getString("ID"));
				aux.setNombre(r.getString("NOMBRECORTO"));
				
				entidades.add(aux);
			}
			
			/*con = dao.getConexionPostgresAyuntamiento("sigemAdmin");

			query = "SELECT id,nombrecorto FROM sgm_adm_entidades ORDER BY id";
		    sentencia = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);			
			
			r = sentencia.executeQuery(query);
			
			while(r.next()){
				
				Entidad aux = new Entidad();
				
				aux.setId(r.getString("ID"));
				aux.setNombre(r.getString("NOMBRECORTO"));
				
				entidades.add(aux);
								
			}*/
			
			r.close();
			r=null;
			sentencia.close();
			sentencia=null;
			con.close();
			con=null;
			
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
			throw e1;
		}		
		
		logger.info("sale de dameEntidades");
		return entidades;
		
	}
	
	
	/**
	 * [eCenpri-Felipe #805]
	 * @param idEntidad
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	/*public boolean updatePasswordEntidad(String codEntidad, String newPassword) throws Exception{
		
		GenericoDAO dao = new GenericoDAO();		
		
		boolean bResult = false;
		try {
			
		    Connection con;
			con = dao.getConexionPostgres("sigemAdmin");
		
		    String sql = "UPDATE sgm_adm_entidades SET password_entidad = '" 
		    		+ newPassword + "' WHERE id = '" + codEntidad + "'";
		    Statement statement = con.createStatement();			
			
			bResult = statement.execute(sql);
			
			statement.close();
			statement=null;
			con.close();
			con=null;
			
		} catch (Exception e1) {
			logger.error("ERROR. " + e1.getMessage(), e1);
			throw e1;
		}
		return bResult;
		
	}*/

}
