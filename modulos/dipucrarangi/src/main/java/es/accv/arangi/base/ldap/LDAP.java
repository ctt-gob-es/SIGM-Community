/**
 * LICENCIA LGPL:
 * 
 * Esta librería es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los términos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versión 2.1 de 
 * la Licencia, o (a su elección) cualquier versión posterior.
 * 
 * Esta librería se distribuye con la esperanza de que sea útil, pero SIN 
 * NINGUNA GARANTÍA; tampoco las implícitas garantías de MERCANTILIDAD o 
 * ADECUACIÓN A UN PROPÓSITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para más detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librería; si no es así, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5º Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPSearchResults;

import es.accv.arangi.base.exception.ldap.LDAPException;

/**
 * Esta clase ofrece métodos de alto nivel para buscar DNs dentro de un
 * directorio LDAP, así como para copiar o mover elementos entre sus ramas.<br><br>
 * 
 * Requiere de servidores que cumplan la versión 3 de LDAP.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class LDAP {

	/**
	 * Puerto por defecto para LDAP (389)
	 */
	public static final int LDAP_DEFAULT_PORT = 389;
	
	
	/**
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(LDAP.class);
	
	/**
	 * Conexión LDAP
	 */
	LDAPConnection lc;
	
	/**
	 * Constructor
	 * 
	 * @param host Host LDAP
	 * @param port Puerto del LDAP (normalmente 389)
	 * @param user Usuario LDAP
	 * @param password Contraseña para el usuario LDAP
	 * @throws LDAPException Excepción accediendo al LDAP
	 */
	public LDAP (String host, int port, String user, String password) throws LDAPException {

		logger.debug("[LDAP]::Entrada:: " + Arrays.asList(new Object[] { host, port, user }));
		
		lc = new LDAPConnection();
		try {
			lc.connect(host, port);
		} catch (com.novell.ldap.LDAPException e) {
			logger.info ("[LDAP]::No ha sido posible conectarse al host " + host + ":" + port, e);
			throw new LDAPException ("No ha sido posible conectarse al host " + host + ":" + port, e);
		}
		try {
			lc.bind( LDAPConnection.LDAP_V3, user, password.getBytes() );
		} catch (com.novell.ldap.LDAPException e) {
			logger.info ("[LDAP]::No ha sido posible autenticarse en el host " + host + ":" + port, e);
			throw new LDAPException ("No ha sido posible autenticarse en el al host " + host + ":" + port, e);
		}

	}
	
	/**
	 * Constructor
	 * 
	 * @param host Host LDAP
	 * @param user Usuario LDAP
	 * @param password Contraseña para el usuario LDAP
	 * @throws LDAPException Excepción accediendo al LDAP
	 */
	public LDAP (String host, String user, String password) throws LDAPException {
		this(host, LDAP_DEFAULT_PORT, user, password);
	}
	
	/**
	 * Realiza una búsqueda de DNs en una rama en base a un filtro
	 * 
	 * @param branch Rama sobre la que realizar la búsqueda
	 * @param filter Filtro
	 * @return Lista de DNs de la rama que coinciden con el filtro
	 * @throws LDAPException 
	 */
	public List<String> getDNs (String branch, String filter) throws LDAPException {
		
		logger.debug("[LDAP.getDNs]::Entrada::" + Arrays.asList(new Object[] { branch, filter }));
				
		LDAPSearchResults results;
		try {
			results = lc.search(branch, LDAPConnection.SCOPE_ONE, filter, null, false);
		} catch (com.novell.ldap.LDAPException e) {
			logger.info ("[LDAP.getDNs]::Ha ocurrido un error realizando la búsqueda", e);
			throw new LDAPException ("Ha ocurrido un error realizando la búsqueda", e);
		}
		List<String> result = new ArrayList<String>();
		while (results.hasMore()) {
			LDAPEntry entry;
			try {
				entry = results.next();
			} catch (com.novell.ldap.LDAPException e) {
				logger.info ("[LDAP.getDNs]::Ha ocurrido un error recuperando una de las entradas resultado de la búsqueda", e);
				throw new LDAPException ("Ha ocurrido un error recuperando una de las entradas resultado de la búsqueda", e);
			}
			result.add(entry.getDN());
		}
		
		logger.debug("[LDAP.getDNs]::Se devolverán " + result.size() + " elementos.");
		
		return result;
	}
	
	/**
	 * Copia una entrada a un dn destino. Si el DN destino no
	 * existe lo crea. Si ya existía añade el elemento.
	 * 
	 * @param dnOrigin DN origen
	 * @param dnDestination DN destino
	 * @throws Exception 
	 */
	public void copy (String dnOrigin, String dnDestination) throws LDAPException {
		
		logger.debug("[LDAP.copy]::Entrada::" + Arrays.asList(new Object[] { dnOrigin, dnDestination }));

		//-- Buscar origen
		LDAPEntry entryOrigen;
		try {
			entryOrigen = getEntry(dnOrigin);
		} catch (com.novell.ldap.LDAPException e1) {
			logger.info("[LDAP.copy]::Se ha producido un error buscando la entrada origen: " + dnOrigin);
			throw new LDAPException ("Se ha producido un error buscando la entrada origen: " + dnOrigin);
		}
		if (entryOrigen == null) {
			logger.info("[LDAP.copy]::No se ha encontrado la entrada origen: " + dnOrigin);
			throw new LDAPException ("No se ha encontrado la entrada para el DN '" + dnOrigin + "'");
		}
		logger.debug("[LDAP.copy]::Encontrada entrada origen: " + dnOrigin);

		//-- Buscar destino
		LDAPEntry entryDestino;
		try {
			entryDestino = getEntry (dnDestination);
		} catch (com.novell.ldap.LDAPException e1) {
			logger.info("[LDAP.copy]::Se ha producido un error buscando la entrada destino: " + dnDestination);
			throw new LDAPException ("Se ha producido un error buscando la entrada destino: " + dnDestination);
		}
		if (entryDestino != null) {
			logger.debug("[LDAP.copy]::Encontrada entrada destino: " + dnDestination);

			//-- Modificar el destino para añadirle todos los atributos binarios del origen
			List<LDAPModification> lModificaciones = new ArrayList<LDAPModification>();
			for (Iterator iterator = entryOrigen.getAttributeSet().iterator(); iterator.hasNext();) {
				
				LDAPAttribute attrOrigen = (LDAPAttribute)iterator.next();
				
				if (attrOrigen.hasSubtype("binary")) {
					
					//-- Crear la modificación
					logger.debug("[LDAP.copy]::Añadir atributo '" + attrOrigen.getBaseName() + "' al destino: " + dnDestination);
					LDAPModification modification = new LDAPModification(LDAPModification.ADD, attrOrigen);
					
					lModificaciones.add (modification);
				}
			}
			
			//-- Realizar las modificaciones
			try {
				lc.modify(dnDestination, lModificaciones.toArray(new LDAPModification[0]));
			} catch (com.novell.ldap.LDAPException e) {
				logger.info ("[LDAP.getDNs]::No se ha podido modificar la entrada con DN=" + dnDestination, e);
				throw new LDAPException ("No se ha podido modificar la entrada con DN=" + dnDestination, e);
			}
			
		} else {
			//-- Crear en destino una copia del origen
			logger.debug("[LDAP.copy]::No se ha encontrado la entrada destino: " + dnDestination);
			entryDestino = new LDAPEntry(dnDestination, entryOrigen.getAttributeSet());
			try {
				lc.add (entryDestino);
			} catch (com.novell.ldap.LDAPException e) {
				logger.info ("[LDAP.getDNs]::No se ha podido añadir la entrada con DN=" + dnDestination, e);
				throw new LDAPException ("No se ha podido añadir la entrada con DN=" + dnDestination, e);
			}
		}
	}
	
	/**
	 * Mueve una entrada a un dn destino. Si el DN destino no
	 * existe lo crea. Si ya existía añade el elemento.
	 * 
	 * @param dnOrigin DN origen
	 * @param dnDestination DN destino
	 * @throws LDAPException 
	 */
	public void move (String dnOrigin, String dnDestination) throws LDAPException {
		
		logger.debug("[LDAP.move]::Entrada::" + Arrays.asList(new Object[] { dnOrigin, dnDestination }));

		//-- Copiar 
		copy (dnOrigin, dnDestination);
		logger.debug("[LDAP.move]::Se ha realizado la copia::" + Arrays.asList(new Object[] { dnOrigin, dnDestination }));
		
		//-- Todo ha ido bien, eliminar el origen
		try {
			lc.delete(dnOrigin);
		} catch (com.novell.ldap.LDAPException e) {
			logger.info ("[LDAP.getDNs]::No se ha podido eliminar la entrada con DN=" + dnOrigin, e);
			throw new LDAPException ("No se ha podido eliminar la entrada con DN=" + dnOrigin, e);
		}
		logger.debug("[LDAP.move]::Se ha eliminado la entrada: " + dnOrigin);
	}
	
	/**
	 * Cierra la conexión
	 * 
	 * @throws LDAPException 
	 */
	public void close () throws LDAPException {
		
		logger.debug("[LDAP.close]::Entrada");

		try {
			lc.disconnect();
		} catch (com.novell.ldap.LDAPException e) {
			logger.info ("[LDAP.getDNs]::No se ha podido cerrar la conexión con el servidor LDAP", e);
			throw new LDAPException ("No se ha podido cerrar la conexión con el servidor LDAP", e);
		}
		logger.debug("[LDAP.close]::Se ha cerrado la conexión con el servidor LDAP");
	}
	
	
	/*
	 * Obtiene una entrada en base a su DN
	 */
	private LDAPEntry getEntry (String dn) throws com.novell.ldap.LDAPException {
		LDAPSearchResults results = lc.search(dn, LDAPConnection.SCOPE_BASE, null, null, false);
		Object object;
		try {
			object = results.next();
		} catch (com.novell.ldap.LDAPException e) {
			return null;
		}
		if (!(object instanceof LDAPEntry)) {
			return null;
		}
		
		return (LDAPEntry) object;
	}
	
	
//	public static void main (String[] args) {
//		
//		String host = "192.168.161.2";
//		int port = 389;
//		String user = "cn=Admin,o=Generalitat Valenciana,c=ES";
//		String password = "baltimore";
//		
//		LDAP ldap = null;
//		try {
//			ldap = new LDAP(host, port, user, password);
//			System.out.println ("Conectado a LDAP");
//			List<String> lDNs = ldap.getDNs("ou=ciudadanos,o=Generalitat Valenciana,c=es", "cn=*NIF:73387270C*");
//			for (Iterator iterator = lDNs.iterator(); iterator.hasNext();) {
//				String dn = (String) iterator.next();
//				String dnDest = dn.replaceFirst("ciudadanos","infonova");
//				System.out.println ("DN: " + dn);
//				ldap.copy(dn, dnDest);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				ldap.close();
//			} catch (LDAPException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
}
