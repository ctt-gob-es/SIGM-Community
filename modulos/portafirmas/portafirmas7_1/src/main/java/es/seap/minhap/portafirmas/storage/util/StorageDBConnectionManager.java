/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.storage.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class StorageDBConnectionManager {

	@Resource(name = "storageProperties")
	private Properties properties;

	// Obtiene la conexión con la base de datos de histórico
	public Connection getConnection(String entorno) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Class.forName("oracle.jdbc.driver.OracleDriver");

        String db = null;
        String user = null;
        String pwd = null;

        if (StorageConstants.PORTAFIRMA.equals(entorno)) {
        	db   = properties.getProperty(StorageConstants.SOURCE_DATABASE);
        	user = properties.getProperty(StorageConstants.SOURCE_USER);
        	pwd  = properties.getProperty(StorageConstants.SOURCE_PASSWORD);
        } else {
        	db   = properties.getProperty(StorageConstants.STORAGE_DATABASE);
        	user = properties.getProperty(StorageConstants.STORAGE_USER);
        	pwd  = properties.getProperty(StorageConstants.STORAGE_PASSWORD);
        }

        conn = DriverManager.getConnection(db, user, pwd);
        conn.setAutoCommit(false);
        return conn;
    }

}
