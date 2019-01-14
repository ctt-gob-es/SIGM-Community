package es.sigem.dipcoruna.framework.service.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component("simpleMessageSource")
public class SimpleMessageSourceImpl implements SimpleMessageSource {
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public String getMessage(String code) {		
		return getMessage(code, null);
	}

	@Override
	public String getMessage(String code, Object[] args) {		
		return messageSource.getMessage(code, args, "[No encontrado mensaje '"+code+"']", LocaleContextHolder.getLocale());
	}
}
