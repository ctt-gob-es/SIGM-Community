package es.clave.sp;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        ApplicationContextProvider.setGlobalAppCtxt(ctx);
    }
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
    private static void setGlobalAppCtxt(ApplicationContext ctx){
        applicationContext=ctx;
    }
}
