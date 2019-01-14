package es.sigem.dipcoruna.desktop.editlauncher.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.editlauncher.events.model.FinalizarAplicacionEvent;
import es.sigem.dipcoruna.desktop.editlauncher.events.model.SubirFicheroEvent;
import es.sigem.dipcoruna.desktop.editlauncher.model.apps.ProcessWrapper;
import es.sigem.dipcoruna.desktop.editlauncher.service.holder.EditorInstanceContextHolder;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;

@Component("infoEdicionDialog")
public class InfoEdicionDialog extends JDialog implements InitializingBean {

	private static final long serialVersionUID = 7864450586642936806L;

	@Autowired
	private SimpleMessageSource messageSource;

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

	private JPanel panelPrincipal;
	private JButton botonToggle;

	private ProcessWrapper processWrapper;
    private String filePath; //Fichero local que está siendo editado

    public void setProcess(final ProcessWrapper processWrapper) {
        this.processWrapper = processWrapper;

    }

    public void setFilePath(final String filePath) {
    	this.filePath = filePath;
    }




	@Override
	public void afterPropertiesSet() throws Exception {
		this.setTitle(messageSource.getMessage("appLauncherApplet.formInfo.title"));
		this.setModal(true);
		this.setAlwaysOnTop(true);


		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		panelPrincipal = getPanelPrincipal();
		panelPrincipal.setVisible(true);

		contentPane.add(panelPrincipal, BorderLayout.CENTER);
		contentPane.add(getButtonsPanel(), BorderLayout.SOUTH);

		this.pack();
		this.setLocationRelativeTo(null);

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent we) {
			    confirmarSalir();
			}
		});


		final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth());
        final int y = (0);
        this.setLocation(x, y);
	}


	private JPanel getPanelPrincipal() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(getNorthPanel(), BorderLayout.NORTH);
		panel.setVisible(true);
		return panel;
	}


	private JPanel getNorthPanel() {
    	final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        final JLabel appPathLabel = new JLabel(messageSource.getMessage("appLauncherApplet.formInfo.info"));

        panel.add(appPathLabel);

        return panel;
    }


	private JPanel getButtonsPanel() {
    	final JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout());

    	final JButton salirButton = new JButton(messageSource.getMessage("appLauncherApplet.button.salir"));
        salirButton.addActionListener(new ListenerBotonSalir());
        panel.add(salirButton);

		botonToggle= new JButton(messageSource.getMessage("appLauncherApplet.button.minimizar"));
		botonToggle.addActionListener(new ListenerBotonToggle());
		panel.add(botonToggle);

        final JButton subirButton = new JButton(messageSource.getMessage("appLauncherApplet.button.subirYSalir"));
        subirButton.addActionListener(new ListenerBotonSubir());
        panel.add(subirButton);

        return panel;
	}






	private void toggle() {
		panelPrincipal.setVisible(!panelPrincipal.isVisible());
		if (panelPrincipal.isVisible()) {
			botonToggle.setText(messageSource.getMessage("appLauncherApplet.button.minimizar"));
		}
		else {
			botonToggle.setText(messageSource.getMessage("appLauncherApplet.button.maximizar"));
		}
		this.pack();
	}


	private void confirmarSalir() {
	    String mensaje = "";
	    if (EditorInstanceContextHolder.getContext().isFicheroDirty()) {
	        mensaje = messageSource.getMessage("appLauncherApplet.formInfo.confirmarSalir.hayCambios.text");
	    }
	    else  {
	        mensaje = messageSource.getMessage("appLauncherApplet.formInfo.confirmarSalir.noCambios.text");
	    }

        final int result = JOptionPane.showConfirmDialog(null,
                mensaje,
                messageSource.getMessage("appLauncherApplet.formInfo.confirmarSalir.tittle"), JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.YES_OPTION) {
            applicationEventPublisher.publishEvent(new FinalizarAplicacionEvent(this, processWrapper, filePath));
        }
	}

	// ----------------------------------------------------------------------------- //

	private class ListenerBotonSubir implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (EditorInstanceContextHolder.getContext().isFicheroDirty()) {
                JOptionPane.showMessageDialog(null,
                        messageSource.getMessage("appLauncherApplet.formInfo.confirmarEnviar.text"),
                        messageSource.getMessage("appLauncherApplet.formInfo.confirmarEnviar.tittle"),
                        JOptionPane.INFORMATION_MESSAGE
                );
                applicationEventPublisher.publishEvent(new SubirFicheroEvent(this, filePath));
                applicationEventPublisher.publishEvent(new FinalizarAplicacionEvent(this, processWrapper, filePath));
            }
            else {
                JOptionPane.showMessageDialog(null,
                        messageSource.getMessage("appLauncherApplet.formInfo.confirmarEnviar.noModificado.tittle"),
                        messageSource.getMessage("appLauncherApplet.formInfo.confirmarEnviar.tittle"),
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

	private class ListenerBotonToggle implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			toggle();
		}
	}


    private class ListenerBotonSalir implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            confirmarSalir();
        }
    }
}
