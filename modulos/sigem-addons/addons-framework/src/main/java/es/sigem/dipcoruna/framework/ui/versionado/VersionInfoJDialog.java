package es.sigem.dipcoruna.framework.ui.versionado;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.framework.events.model.AbrirNavegadorEvent;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;

@Component("versionInfoJDialog")
public class VersionInfoJDialog extends JDialog implements InitializingBean {
	private static final long serialVersionUID = -2592119823010419621L;
	

	@Autowired
	private SimpleMessageSource messageSource;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher; 
	
	
	private JLabel labelVersion;	
	private String urlDescarga;
	
	private boolean descargarMasTarde = true;
	
	public boolean isDescargarMasTarde() {
		return descargarMasTarde;
	}

	public void setDescargarMasTarde(boolean descargarMasTarde) {
		this.descargarMasTarde = descargarMasTarde;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setTitle(messageSource.getMessage("desktop.framework.version.nuevaVersion"));		
		setLayout(new BorderLayout());
		
		add(crearPanelDescripcionSuperior(), BorderLayout.NORTH);
		add(crearPanelVersionCentral(), BorderLayout.CENTER);
		add(crearPanelBotoneraInferior(), BorderLayout.SOUTH);

		
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
	}
	
	
	
	public void setTexts(String versionActual, String nuevaVersion, String urlDescarga, String whatsNew){
		this.urlDescarga = urlDescarga;
		labelVersion.setText(messageSource.getMessage("desktop.framework.version.nuevaVersion.info", new Object[] {versionActual, nuevaVersion, whatsNew}));	
		pack();
	}
	

	
	private JPanel crearPanelDescripcionSuperior() {
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JLabel textoCabecera = new JLabel(messageSource.getMessage("desktop.framework.version.nuevaVersion"));		
		panel.add(textoCabecera);
		
		return panel;
	}
	
	
	private JPanel crearPanelVersionCentral() {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		labelVersion = new JLabel("Cuerpo", SwingConstants.CENTER);				
		panel.add(labelVersion);
					
		return panel;
	}
	
	
	private JPanel crearPanelBotoneraInferior() {
		final JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout());
    	panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    	
    	final JButton okButton = new JButton(messageSource.getMessage("desktop.framework.version.boton.masTarde"));
    	okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				descargarMasTarde = true;
				setVisible(false);
				dispose();
			}
    	});
        panel.add(okButton);
        
    	
    	final JButton cancelButton = new JButton(messageSource.getMessage("desktop.framework.version.boton.descargar"));    		    	
        cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				applicationEventPublisher.publishEvent(new AbrirNavegadorEvent(this, urlDescarga));
				descargarMasTarde = false;
				setVisible(false);
				dispose();
			}
    	});
        panel.add(cancelButton);
    	        
		return panel;
	}
	
	
	
}