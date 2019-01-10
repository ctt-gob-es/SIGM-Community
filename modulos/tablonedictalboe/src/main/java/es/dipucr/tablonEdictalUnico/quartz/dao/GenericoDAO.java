package es.dipucr.tablonEdictalUnico.quartz.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Logger;

import es.dipucr.tablonEdictalUnico.quartz.util.Propiedades;

public class GenericoDAO {
	
	private static final Logger logger = Logger.getLogger(GenericoDAO.class);
		
	Propiedades p;
	
	public GenericoDAO(){
		init();
	}
	
	public void init(){
		p = new Propiedades();
	}
	
	public Connection getConexion(String _entidad, String bdd) throws Exception{
		
		Connection con = null;
		String ipDipucr = p.getProperty("servidor.ip.dipucr");
		String portDipucr = p.getProperty("servidor.port.dipucr");
		String userDipucr = p.getProperty("basedatos.user.dipucr");
		String passDipucr = p.getProperty("basedatos.pass.dipucr");
		
		String ip = p.getProperty("servidor.ip");
		String port_cluster1 = p.getProperty("servidor.port_cluster1");
		String port_cluster2 = p.getProperty("servidor.port_cluster2");
		String baseDatos = bdd;
		String entidad =_entidad;
		String user = p.getProperty("basedatos.user");
		String pass = p.getProperty("basedatos.pass");
		try {		
			//Primero probamos en la entidad de diputacion
			
			Class.forName("org.postgresql.Driver").newInstance();			
			con = DriverManager.getConnection("jdbc:postgresql://"+ ipDipucr + ":" + portDipucr  + "/" + baseDatos + "_" + entidad, userDipucr, passDipucr);			
			logger.debug("Conexion con la base de datos " +baseDatos + "_" + entidad+ " de la ip " + ipDipucr + ":" + portDipucr);	
			logger.debug("ENTRA POR DIPUTACION DE CIUDAD REAL" + con.toString());
		
		} catch (Exception e) {
		
			try {		
				con = DriverManager.getConnection("jdbc:postgresql://"+ ip + ":" + port_cluster1  + "/" + baseDatos + "_" + entidad, user, pass);			
				logger.debug("Conexion con la base de datos " +baseDatos + "_" + entidad+ " de la ip " + ip + ":" + port_cluster1);	
				logger.debug("ENTRA POR CLUSTER 1" + con.toString());
				logger.debug(e.getMessage(), e);
			
			} catch (Exception e2) {		
				try{
					con = DriverManager.getConnection("jdbc:postgresql://"+ ip + ":" + port_cluster2 + "/" + baseDatos + "_" + entidad, user, pass);	
					logger.debug("Conexion con la base de datos " +baseDatos + "_" + entidad + " de la ip " + ip + ":" + port_cluster2);
					logger.debug("ENTRA POR CLUSTER 2" + con.toString());
					logger.debug(e2.getMessage(), e2);

					return con;				
				} catch(Exception e1){
					logger.error("Error en la entidad. " + entidad + "." + e.getMessage(), e);					
					throw e1;
				}
			}
		}
		
		logger.debug("RETORNA CONEXION A SIGEM");
		return con;
	}
	
	/**
	 * [Felipe] Para que conecte sólo con las entidades de un cluster 
	 * @param _entidad
	 * @param bdd
	 * @return
	 * @throws Exception
	 */
	public Connection getConexion(String _entidad, String bdd, String port_cluster) throws Exception{
		
		Connection con = null;
		String ip = p.getProperty("servidor.ip");
		String baseDatos = bdd;
		String entidad =_entidad;
		String user = p.getProperty("basedatos.user");
		String pass = p.getProperty("basedatos.pass");
		
		try {		
			
			Class.forName("org.postgresql.Driver").newInstance();			
			con = DriverManager.getConnection("jdbc:postgresql://"+ ip + ":" + port_cluster  + "/" + baseDatos + "_" + entidad, user, pass);			
			logger.debug("Conexion con la base de datos " +baseDatos + "_" + entidad+ " de la ip " + ip + ":" + port_cluster);	
			logger.debug("ENTRA POR CLUSTER 1" + con.toString());			
		
		} catch (Exception e) {	
			logger.error("Error en la entidad. " + entidad + " bdd " + bdd + " port_cluster " + port_cluster + "." + e.getMessage(), e);
			throw e;
		}
		
		logger.debug("RETORNA CONEXION A SIGEM");
		return con;
	}
	
	public Connection getConexionPostgresDipucr(String _bbdd) throws Exception{
		
		Connection con = null;
		String ipDipucr = p.getProperty("servidor.ip.dipucr");
		String portDipucr = p.getProperty("servidor.port.dipucr");
		String userDipucr = p.getProperty("basedatos.user.dipucr");
		String passDipucr = p.getProperty("basedatos.pass.dipucr");
		
		try{
			Class.forName("org.postgresql.Driver").newInstance();			
			con = DriverManager.getConnection("jdbc:postgresql://"+ ipDipucr + ":" + portDipucr  + "/" + _bbdd, userDipucr, passDipucr);			
			logger.debug("Conexion con la base de datos " + _bbdd + " de la ip " + ipDipucr + ":" + portDipucr);	
			logger.debug("ENTRA DIPUCR" + con.toString());
			
		} catch (Exception e1) {
			logger.error("Error en la _bbdd. " + _bbdd + "." + e1.getMessage(), e1);
			throw e1;

		}
		
		logger.debug("RETORNA CONEXION A SIGEM");
		return con;
	}
	
	public Connection getConexionPostgresAyuntamiento(String _bbdd) throws Exception{
		
		Connection con = null;
	
		String ip = p.getProperty("servidor.ip");
		String port_cluster1 = p.getProperty("servidor.port_cluster1");
		String port_cluster2 = p.getProperty("servidor.port_cluster2");		
		String user = p.getProperty("basedatos.user");
		String pass = p.getProperty("basedatos.pass");
		
		try {				
			con = DriverManager.getConnection("jdbc:postgresql://"+ ip + ":" + port_cluster1  + "/" + _bbdd, user, pass);			
			logger.debug("Conexion con la base de datos " + _bbdd + " de la ip " + ip + ":" + port_cluster1);	
			logger.debug("ENTRA POR CLUSTER 1" + con.toString());
		
		} catch (Exception e) {		
			try{
				con = DriverManager.getConnection("jdbc:postgresql://"+ ip + ":" + port_cluster2  + "/" + _bbdd, user, pass);			
				logger.debug("Conexion con la base de datos " + _bbdd + " de la ip " + ip + ":" + port_cluster2);	
				logger.debug("ENTRA POR CLUSTER 2" + con.toString());
				logger.debug(e.getMessage(), e);
				
				return con;
				
			}catch(Exception e1){
				logger.error("Error en la _bbdd. " + _bbdd + "." + e1.getMessage(), e1);
				throw e1;
			}
		}
		
		logger.debug("RETORNA CONEXION A SIGEM");
		return con;
	}
}
