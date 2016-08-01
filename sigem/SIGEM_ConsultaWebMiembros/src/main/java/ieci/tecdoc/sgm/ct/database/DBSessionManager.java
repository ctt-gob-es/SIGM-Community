package ieci.tecdoc.sgm.ct.database;

import ieci.tecdoc.sgm.core.config.impl.spring.Config;
import ieci.tecdoc.sgm.core.db.DataSourceManagerMultientidad;
import ieci.tecdoc.sgm.core.db.impl.MultipleDatasourceImpl;
import ieci.tecdoc.sgm.ct.Configuracion;

import java.sql.Connection;

public class DBSessionManager {

	public static Connection getSession(String entidad) throws Exception{
		return DataSourceManagerMultientidad.getInstance().getConnection(
				DataSourceManagerMultientidad.DEFAULT_DATASOURCE_NAME,
				Configuracion.getConfiguracion(), entidad);
	}
	
	public static Connection getSessionTramitador(String entidad) throws Exception{
		return getConnection(
				"SIGEM_TRAMITADOR_ENTIDAD_DATASOURCE",
				Configuracion.getConfiguracion(), entidad);
	}
	
	public static Connection getConnection(String datasourceName, Config poConfig, String entidad) throws Exception {
		   
	       Connection conn = null;
		   
		   if( (entidad == null) || ("".equals(entidad))){
			   throw new Exception("Error: entidad no definida a la hora de establecer conexión a la base de datos.");
		   }
		   
		   MultipleDatasourceImpl m_dataSource = (MultipleDatasourceImpl)poConfig.getBean(datasourceName);
		   if (m_dataSource != null) {
			   return m_dataSource.getDataSource("_" + entidad).getConnection();
		   }
	       
	       return conn;
	   }
}
