package es.clave.sp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

/**
 * thread local session holder
 */
public class SessionHolder {

	private static final ThreadLocal<HttpSession> sessionHolderMap = new ThreadLocal<HttpSession>();
	
	/** Lista de sesiones abiertas. */
	public static volatile Map<String, String> sessionsSAML = new ConcurrentHashMap<String, String>(10);

	private SessionHolder() {
	}

	public static void setId(HttpSession identifier) {
		if (null == identifier) {
			//throw some exception
		}
		sessionHolderMap.set(identifier);
	}

	public static HttpSession getId() {
		return sessionHolderMap.get();
	}

	public static void clear() {
		sessionHolderMap.remove();
	}
}
