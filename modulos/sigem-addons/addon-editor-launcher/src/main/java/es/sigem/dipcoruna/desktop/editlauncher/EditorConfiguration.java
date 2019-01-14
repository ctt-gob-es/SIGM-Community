package es.sigem.dipcoruna.desktop.editlauncher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import es.sigem.dipcoruna.desktop.editlauncher.service.apps.GeneralAppService;
import es.sigem.dipcoruna.desktop.editlauncher.service.apps.impl.LinuxGeneralAppServiceImpl;
import es.sigem.dipcoruna.desktop.editlauncher.service.apps.impl.WindowsGeneralAppServiceImpl;
import es.sigem.dipcoruna.desktop.editlauncher.service.monitor.FileMonitorService;
import es.sigem.dipcoruna.desktop.editlauncher.service.monitor.impl.LinuxFileMonitorServiceImpl;
import es.sigem.dipcoruna.desktop.editlauncher.service.monitor.impl.WindowsFileMonitorServiceImpl;
import es.sigem.dipcoruna.framework.config.conditions.LinuxCondition;
import es.sigem.dipcoruna.framework.config.conditions.WindowsCondition;

@Configuration
public class EditorConfiguration {

    @Bean(name = "generalAppService")
    @Conditional(WindowsCondition.class)
    public GeneralAppService windowsGeneralAppService() {
        return new WindowsGeneralAppServiceImpl();
    } 

    @Bean(name = "fileMonitorService")
    @Conditional(WindowsCondition.class)
    public FileMonitorService windowsFileMonitorService() {
        return new WindowsFileMonitorServiceImpl();
    }
    
    // ************************************** //
    // ************************************** //
    
    @Bean(name = "generalAppService")
    @Conditional(LinuxCondition.class)
    public GeneralAppService linuxGeneralAppService() {
        return new LinuxGeneralAppServiceImpl();
    }
    
    @Bean(name = "fileMonitorService")
    @Conditional(LinuxCondition.class)
    public FileMonitorService linuxFileMonitorService() {
        return new LinuxFileMonitorServiceImpl();
    }

}
