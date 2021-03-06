package es.ieci.tecdoc.isicres.admin.sbo.uas.ldap;

import java.sql.Connection;

import es.ieci.tecdoc.isicres.admin.base.dbex.DbConnection;
import es.ieci.tecdoc.isicres.admin.core.db.DBSessionManager;
import es.ieci.tecdoc.isicres.admin.core.db.DbConnectionConfig;
import es.ieci.tecdoc.isicres.admin.core.ldap.LdapConnCfg;
import es.ieci.tecdoc.isicres.admin.sbo.uas.base.UasAuthToken;



public final class UasBnoAuthEx
{

	//~ Constructors -----------------------------------------------------------

	private UasBnoAuthEx(){}
	
	//~ Methods ----------------------------------------------------------------

	/**
	 * @autor IECISA
	 * 
	 * @param name				 Login del usuario
	 * @param pwd				 Password introducida por el usuario al logarse
	 * @param cntsTriesNum	 N�mero de intentos de login que ha realizado el usuario
	 * 
	 * @return UasAuthToken	Informaci�n completa del usuario
	 * @throws Exception		Exception if the application business logic throws an exception
	 * 
	 * @since V1.0
	 */
	public static UasAuthToken authenticateUser(Connection jdbcCnt, String name,
                                               String pwd, int cntsTriesNum, String entidad)
									   throws Exception
	{

		UasAuthToken   token    = null;		
		LdapConnCfg    ldapCfg  = null;
		UasAuthConfig  authCfg  = null;

		DbConnection dbConn=new DbConnection();
		try{
			dbConn.open(DBSessionManager.getSession());
			
			ldapCfg = UasConfigUtil.createLdapConnConfig(entidad);
			authCfg = UasConfigUtil.createUasAuthConfig(entidad);			
			
			token = UasMdoAuth.authenticateUser(dbConn, ldapCfg, authCfg, name, 
			                                    pwd, cntsTriesNum, entidad);
		
			return token;

		}
		 catch(Exception e)
		{
			return token;
		}finally{
			dbConn.close();
		}

	}
	
	/**
	 * @autor IECISA
	 * 
	 * @param dbConConfig		Objeto que contiene la informaci�n de la conexi�n directa a BBDD sin
	 * 								utilizar el pool de conexiones del Servidor de Aplicaciones
	 * @param ldapCfg			Objeto que contiene la informaci�n de la conexi�n directa al servidor
	 * 								de LDAP
	 * @param authConfig 
	 * @param name 				Login del usuario
	 * @param pwd 				Password introducida por el usuario al logarse
	 * @param cntsTriesNum 		N�mero de intentos de login que ha realizado el usuario 
	 * @return UasAuthToken       	Informaci�n completa del usuario
	 *
	 * @throws 		 			Exception if the application business logic throws an exception
	 * 
	 * @since V1.0
	 */
	public static UasAuthToken authenticateUser(DbConnectionConfig dbConConfig,
												           LdapConnCfg ldapCfg,
												           UasAuthConfig authConfig,
												           String name, String pwd,
												           int cntsTriesNum, String entidad)
									   throws Exception
	{

		UasAuthToken   token    = null;		

		DbConnection dbConn=new DbConnection();
		try{
			dbConn.open(DBSessionManager.getSession());	
			
			token = UasMdoAuth.authenticateUser(dbConn, ldapCfg, authConfig, name, pwd,
									                  cntsTriesNum, entidad);

			return token;

		}
		 catch(Exception e)
		{
			return token;
		}finally{
			dbConn.close();
		}

	}
	
	public static UasAuthToken authenticateUser(String name, String entidad)
										 throws Exception
	{

		UasAuthToken   token    = null;		
		LdapConnCfg    ldapCfg  = null;
		UasAuthConfig  authCfg  = null;

		DbConnection dbConn=new DbConnection();
		try{
			dbConn.open(DBSessionManager.getSession());
			
			ldapCfg = UasConfigUtil.createLdapConnConfig(entidad);
			authCfg = UasConfigUtil.createUasAuthConfig(entidad);	
			
			token = UasMdoAuth.authenticateUser(dbConn, ldapCfg, authCfg, name, entidad);

			return token;

		}
		 catch(Exception e)
		{
			return token;

		}finally{
			dbConn.close();
		}

	}

	public static UasAuthToken authenticateUser(DbConnectionConfig dbConConfig,
												           LdapConnCfg ldapCfg,
												           UasAuthConfig authConfig,
												           String name, String entidad)
									   throws Exception
	{

		UasAuthToken   token = null;	

		DbConnection dbConn=new DbConnection();
		try{
			dbConn.open(DBSessionManager.getSession());
			
			token = UasMdoAuth.authenticateUser(dbConn, ldapCfg, authConfig, name, entidad);

			return token;

		}
		 catch(Exception e)
		{
			return token;

		}finally{
			dbConn.close();
		}

	}	

}
 // class
