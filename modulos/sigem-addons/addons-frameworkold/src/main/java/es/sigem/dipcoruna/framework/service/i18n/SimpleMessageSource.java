package es.sigem.dipcoruna.framework.service.i18n;

/**
 * Interfaz simplificado para la internacionalización de mensajes
 * @author Cixtec
 *
 */
public interface SimpleMessageSource {
	String getMessage(String code);

	String getMessage(String code, Object[] args);
}
