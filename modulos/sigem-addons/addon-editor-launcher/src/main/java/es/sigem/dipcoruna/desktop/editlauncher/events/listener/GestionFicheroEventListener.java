package es.sigem.dipcoruna.desktop.editlauncher.events.listener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.editlauncher.events.model.EditorCerradoEvent;
import es.sigem.dipcoruna.desktop.editlauncher.events.model.FicheroModificadoEvent;
import es.sigem.dipcoruna.desktop.editlauncher.events.model.FinalizarAplicacionEvent;
import es.sigem.dipcoruna.desktop.editlauncher.events.model.LanzadoEditorEvent;
import es.sigem.dipcoruna.desktop.editlauncher.events.model.SubirFicheroEvent;
import es.sigem.dipcoruna.desktop.editlauncher.model.apps.ProcessWrapper;
import es.sigem.dipcoruna.desktop.editlauncher.service.LocalFileService;
import es.sigem.dipcoruna.desktop.editlauncher.service.holder.EditorInstanceContextHolder;
import es.sigem.dipcoruna.desktop.editlauncher.ui.InfoEdicionDialog;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;

@Component("ficheroEventListener")
public class GestionFicheroEventListener  {
	private static final Logger LOGGER = LoggerFactory.getLogger(GestionFicheroEventListener.class);

	@Value("${param.urlDoc}")
    private String urlDoc;

	@Autowired
	private InfoEdicionDialog infoEdicionDialog;

	@Autowired
	private LocalFileService localFileService;

	@Autowired
	private SimpleMessageSource messageSource;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;



	@EventListener
	@Async
	public void onApplicationEvent(final FicheroModificadoEvent event) {
	    LOGGER.info("El usuario guardó el fichero {}", event.getPathFichero());

	    EditorInstanceContextHolder.getContext().setFicheroDirty(true);

	    if (EditorInstanceContextHolder.getContext().isMostrarAvisoGuardado()) {
    	    final JCheckBox checkboxNoVolverAMostrar = new JCheckBox(messageSource.getMessage("appLauncherApplet.infoCambio.noVolverAMostrar"));
    	    final String message =  messageSource.getMessage("appLauncherApplet.infoCambio.text");
    	    final Object[] params = {message, checkboxNoVolverAMostrar};
    	    final int dialogResult = JOptionPane.showConfirmDialog(null, params, messageSource.getMessage("appLauncherApplet.infoCambio.title"), JOptionPane.YES_NO_OPTION);

    	    if (dialogResult == JOptionPane.YES_OPTION) {
    	        LOGGER.info("El usuario confirma la subida del fichero modificado {}", event.getPathFichero());
                actualizarDocumentoRemoto(event);
    	    }

    	    if (checkboxNoVolverAMostrar.isSelected()) {
    	        EditorInstanceContextHolder.getContext().setMostrarAvisoGuardado(false);
    	        EditorInstanceContextHolder.getContext().setGuardarFicheroSinAviso(dialogResult == JOptionPane.YES_OPTION);
    	    }
	    }
	    else {
	        if (EditorInstanceContextHolder.getContext().isGuardarFicheroSinAviso()) {
	            LOGGER.info("Se envía el fichero al servidor de forma automática {}", event.getPathFichero());
                actualizarDocumentoRemoto(event);
	        }
	    }	    	 
	}


    private void actualizarDocumentoRemoto(final FicheroModificadoEvent event) {
        Thread ventantaGuardando = new Thread(new Runnable() {
            @Override
            public void run() {
                JDialog dialog = new JDialog();
                dialog.setTitle(messageSource.getMessage("appLauncherApplet.enviando.title"));
                dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
                panel.add(new JLabel(messageSource.getMessage("appLauncherApplet.enviando.text")));
                dialog.add(panel);              
                dialog.setResizable(false);
                dialog.toFront();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                dialog.pack();
               
                try {                    
                    while (!Thread.currentThread().isInterrupted()) {                        
                        Thread.sleep(5000);                       
                    }
                } catch (InterruptedException e) {                    
                    dialog.dispose();
                    Thread.currentThread().interrupt();                    
                }
            }
            
        });
        ventantaGuardando.start();
        try {
            localFileService.actualizarDocumentoRemoto(urlDoc, event.getPathFichero());                
            EditorInstanceContextHolder.getContext().setFicheroDirty(false);
        }
        finally {
            ventantaGuardando.interrupt();
        }
    }   
        
    
	@EventListener
    public void onApplicationEvent(final LanzadoEditorEvent event) {
	    //Deshabilitado el interfaz gráfico. Se comenta el lanzamiento de la ventana y se hace el join para evitar que se termine la ejecución
//	    infoEdicionDialog.setProcess(event.getProcessWrapper());
//	    infoEdicionDialog.setFilePath(event.getFilePath());
//	    infoEdicionDialog.setVisible(true);
	    try {
            Thread.currentThread().join();
        }
        catch (InterruptedException e) {           
        }
    }


	@EventListener
    public void onApplicationEvent(final SubirFicheroEvent event) {
	    LOGGER.info("Guardando fichero remoto");
        localFileService.actualizarDocumentoRemoto(urlDoc, event.getFilePath());
    }


	@EventListener
    public void onApplicationEvent(final FinalizarAplicacionEvent event) {
	    final ProcessWrapper proccesWrapper = event.getProcessWrapper();
	    proccesWrapper.destroy();
	    try {
            TimeUnit.SECONDS.sleep(1); //Para darle tiempo al proceso a cerrarse completamente
        }
        catch (final InterruptedException e) {
        }
	    localFileService.limpiarDocumento(event.getFilePath());
	    System.exit(0);
    }
	
	
	
	@EventListener
	public void onApplicationEvent(final EditorCerradoEvent event) {
		int i = 0;
		while (new File(event.getFilePath() + ".uploading").exists()) {
			if (i++ > 100) {
				LOGGER.error("Se ha alcanzado el número máximo de esperas para determinar si el fichero se ha terminado de subir. Se sale directamente");
				break;
			}
			LOGGER.info("Esperando a que se termine de subir el fichero modificado {}", event.getFilePath());
			try {
	           TimeUnit.SECONDS.sleep(2);                
	        }
	        catch (InterruptedException e) {
	        }
		}		
        LOGGER.info("Cerrando aplicación");
        localFileService.limpiarDocumento(event.getFilePath());
        System.exit(0);
    }	
}
