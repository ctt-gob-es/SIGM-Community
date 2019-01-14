package core.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware{
    
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        AppContext.setApplicationContext(ctx);
    }
    
    public static ApplicationContext getApplicationContext(){
        return AppContext.getApplicationContext();
    }
    
}
