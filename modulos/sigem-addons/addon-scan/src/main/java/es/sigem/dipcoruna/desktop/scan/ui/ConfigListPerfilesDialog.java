package es.sigem.dipcoruna.desktop.scan.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.scan.events.model.config.listaperfiles.DispositivoSeleccionadConfigListDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.config.perfil.EliminarScanProfileEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.config.perfil.MostrarConfigPerfilDialogEvent;
import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;

@Component("configListDialog")
public class ConfigListPerfilesDialog extends JDialog implements InitializingBean {
	private static final long serialVersionUID = 7159391470426550788L;

	@Autowired
	private SimpleMessageSource messageSource;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private JComboBox<String> selectorDispositivos;
	private JList<String> listaPerfiles;
	private DefaultListModel<String> listModelPerfiles;


	@Override
	public void afterPropertiesSet() throws Exception {
		this.setTitle(messageSource.getMessage("configuracion.listaPerfiles.titulo"));
		this.setLayout(new BorderLayout());
		this.add(initPanelCabecera(), BorderLayout.NORTH);
		this.add(initPanelTrabajo(), BorderLayout.EAST);
		this.add(initPanelBotonera(), BorderLayout.SOUTH);
		this.setSize(400, 280);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setModal(true);
	}


	private JPanel initPanelCabecera() {
		final JPanel jpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		jpanel.add(new JLabel(messageSource.getMessage("configuracion.listaPerfiles.perfiles")));
		return jpanel;
	}


	private JPanel initPanelTrabajo() {
		final JPanel panelTrabajo = new JPanel();
		panelTrabajo.setLayout(new BoxLayout(panelTrabajo, BoxLayout.PAGE_AXIS));
		panelTrabajo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


		final JPanel panelDispositivos = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JLabel labelScanner = new JLabel(messageSource.getMessage("etiqueta.escaner") + ":");
		labelScanner.setSize(150, 10);
		panelDispositivos.add(labelScanner);
		selectorDispositivos = new JComboBox<String>();
		selectorDispositivos.addActionListener (new ListenerSeleccionDispositivo());
		panelDispositivos.add(selectorDispositivos);



		final JPanel panelPerfiles = new JPanel(new FlowLayout(FlowLayout.LEFT));
		listModelPerfiles = new DefaultListModel<String>();

		listaPerfiles = new JList<String>(listModelPerfiles);
		listaPerfiles.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listaPerfiles.setLayoutOrientation(JList.VERTICAL);
		listaPerfiles.setVisibleRowCount(-1);

		final JScrollPane listScroller = new JScrollPane(listaPerfiles);
		listScroller.setPreferredSize(new Dimension(320, 130));

		panelPerfiles.add(new JLabel(messageSource.getMessage("etiqueta.perfiles") + ":"));
		panelPerfiles.add(listScroller);


		panelTrabajo.add(panelDispositivos);
		panelTrabajo.add(panelPerfiles);

		return panelTrabajo;
	}


	private JPanel initPanelBotonera() {
		final JPanel jpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		final JButton botonSalir = new JButton(messageSource.getMessage("boton.salir"));
		botonSalir.addActionListener(new ListenerBotonSalir());

		final JButton botonEliminar = new JButton(messageSource.getMessage("boton.eliminar"));
		botonEliminar.addActionListener(new ListenerBotonEliminar());

		final JButton botonActualizar = new JButton(messageSource.getMessage("boton.detalle"));
		botonActualizar.addActionListener(new ListenerBotonActualizar());

		final JButton botonNuevo = new JButton(messageSource.getMessage("boton.nuevo"));
		botonNuevo.addActionListener(new ListenerBotonNuevo());


		jpanel.add(botonSalir);
		jpanel.add(botonEliminar);
		jpanel.add(botonActualizar);
		jpanel.add(botonNuevo);

		return jpanel;
	}


	public void repitarListaDispositivos(final List<String> dispositivos) {
	    selectorDispositivos.removeAllItems();
        for (final String dispositivo : dispositivos) {
            selectorDispositivos.addItem(dispositivo);
        }
	}

	public String getSelectedDispositivo() {
	    return (String) selectorDispositivos.getSelectedItem();
	}

	public void repintarPerfilesEnLista(final List<ScanProfile> scanProfiles) {
		listModelPerfiles.removeAllElements();
		for (final ScanProfile scanProfile : scanProfiles) {
			listModelPerfiles.addElement(scanProfile.getNombre());
		}
	}



	// **************************** //


	private class ListenerBotonNuevo implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
		    if (!StringUtils.hasText((String)selectorDispositivos.getSelectedItem())) {
		        JOptionPane.showMessageDialog(null, messageSource.getMessage("error.seleccionVacia.escaner"), messageSource.getMessage("error.error"), JOptionPane.ERROR_MESSAGE);
                return;
		    }

			applicationEventPublisher.publishEvent(new MostrarConfigPerfilDialogEvent(this, null, (String)selectorDispositivos.getSelectedItem()));
		}
	}

	private class ListenerBotonActualizar implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			final int perfilSeleccionado = listaPerfiles.getSelectedIndex();
			if (perfilSeleccionado == -1) {
		    	JOptionPane.showMessageDialog(null, messageSource.getMessage("error.seleccionVacia"), messageSource.getMessage("error.error"), JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
			applicationEventPublisher.publishEvent(new MostrarConfigPerfilDialogEvent(this, listModelPerfiles.get(perfilSeleccionado), (String)selectorDispositivos.getSelectedItem()));
		}
	}


	private class ListenerBotonEliminar implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
		    final int perfilSeleccionado = listaPerfiles.getSelectedIndex();
            if (perfilSeleccionado == -1) {
                JOptionPane.showMessageDialog(null, messageSource.getMessage("error.seleccionVacia"), messageSource.getMessage("error.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }


		    final int result = JOptionPane.showConfirmDialog(null, messageSource.getMessage("configuracion.listaPerfiles.confirmarEliminacion", new String[] {listModelPerfiles.get(perfilSeleccionado)}), null, JOptionPane.YES_NO_OPTION);
    		if(result == JOptionPane.YES_OPTION) {
    			applicationEventPublisher.publishEvent(new EliminarScanProfileEvent(this, listModelPerfiles.get(perfilSeleccionado), (String)selectorDispositivos.getSelectedItem()));
    		}
		}
	}


	private class ListenerBotonSalir implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			dispose();
		}
	}


	private class ListenerSeleccionDispositivo implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
           applicationEventPublisher.publishEvent(new DispositivoSeleccionadConfigListDialogEvent(this));
        }
    }

}
