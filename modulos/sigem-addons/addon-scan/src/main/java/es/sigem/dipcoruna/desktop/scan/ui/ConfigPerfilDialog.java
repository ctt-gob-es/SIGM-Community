package es.sigem.dipcoruna.desktop.scan.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.scan.events.model.config.perfil.CrearOActualizarScanProfileEvent;
import es.sigem.dipcoruna.desktop.scan.model.config.FormatoColor;
import es.sigem.dipcoruna.desktop.scan.model.config.Resolucion;
import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;
import es.sigem.dipcoruna.desktop.scan.ui.model.ComboItem;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;

@Component("configPerfilDialog")
public class ConfigPerfilDialog extends JDialog implements InitializingBean {
	private static final long serialVersionUID = 1884573501310446104L;

	@Autowired
	private SimpleMessageSource messageSource;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private JTextField nombreDispositivo;
	private JTextField nombrePerfil;
	private JComboBox<ComboItem<FormatoColor>> formatosColor;
	private JComboBox<ComboItem<Resolucion>> resoluciones;
	private JCheckBox duplex;
	private JCheckBox cargaAutomatica;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.setTitle(messageSource.getMessage("configuracion.detallePerfil.titulo"));
		this.setLayout(new BorderLayout());
		this.add(initPanelCabecera(), BorderLayout.NORTH);
		this.add(initPanelTrabajo(), BorderLayout.EAST);
		this.add(initPanelBotonera(), BorderLayout.SOUTH);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.pack();
	}

	private JPanel initPanelCabecera() {
		final JPanel jpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		jpanel.add(new JLabel(messageSource.getMessage("configuracion.detallePerfil.perfil")));
		return jpanel;
	}

	private JPanel initPanelTrabajo() {
		final JPanel panelTrabajo = new JPanel(new GridLayout(6, 2));
		panelTrabajo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		nombreDispositivo = new JTextField(20);
		nombreDispositivo.setEditable(false);
		nombrePerfil = new JTextField(20);
		formatosColor = new JComboBox<ComboItem<FormatoColor>>(getOpcionesFormatoColor());
		resoluciones = new JComboBox<ComboItem<Resolucion>>(getOpcionesResolucion());
		duplex = new JCheckBox();
		cargaAutomatica = new JCheckBox();

		panelTrabajo.add(new JLabel(messageSource.getMessage("etiqueta.perfil.dispositivo") + ":"));
        panelTrabajo.add(nombreDispositivo);

		panelTrabajo.add(new JLabel(messageSource.getMessage("etiqueta.perfil.nombre") + ":"));
		panelTrabajo.add(nombrePerfil);

		panelTrabajo.add(new JLabel(messageSource.getMessage("etiqueta.perfil.formatoColor") + ":"));
		panelTrabajo.add(formatosColor);

		panelTrabajo.add(new JLabel(messageSource.getMessage("etiqueta.perfil.resolucion") + ":") );
		panelTrabajo.add(resoluciones);

		panelTrabajo.add(new JLabel(messageSource.getMessage("etiqueta.perfil.duplex") + ":") );
        panelTrabajo.add(duplex);

        panelTrabajo.add(new JLabel(messageSource.getMessage("etiqueta.perfil.cargaAutomatica") + ":") );
        panelTrabajo.add(cargaAutomatica);

		return panelTrabajo;
	}

	private JPanel initPanelBotonera() {
		final JPanel jpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		final JButton botonVolver = new JButton(messageSource.getMessage("boton.volver"));
		botonVolver.addActionListener(new ListenerBotonVolver());

		final JButton botonGuardar = new JButton(messageSource.getMessage("boton.guardar"));
	    botonGuardar.addActionListener(new ListenerBotonGuardar());

		jpanel.add(botonVolver);
		jpanel.add(botonGuardar);
		return jpanel;
	}


	public void mostrarPerfil(final ScanProfile perfil) {
	    try {
            nombreDispositivo.setText(new String(perfil.getNombreDispositivo().getBytes(), "UTF-8"));
        }
        catch (final UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		nombrePerfil.setText(perfil.getNombre());
		formatosColor.setSelectedItem(new ComboItem<FormatoColor>("", perfil.getFormatoColor()));
		resoluciones.setSelectedItem(new ComboItem<Resolucion>("", perfil.getResolucion()));
		duplex.setSelected(perfil.isDuplex());
		cargaAutomatica.setSelected(perfil.isCargaAutomatica());
	}



	private Vector<ComboItem<FormatoColor>> getOpcionesFormatoColor() {
		final Vector<ComboItem<FormatoColor>> opciones = new Vector<ComboItem<FormatoColor>>();

		for(final FormatoColor formatoColor: FormatoColor.values()) {
			opciones.add(new ComboItem<FormatoColor>(messageSource.getMessage("etiqueta.formatoColor." + formatoColor.name()), formatoColor));
		}
		return opciones;
	}

	private Vector<ComboItem<Resolucion>> getOpcionesResolucion() {
		final Vector<ComboItem<Resolucion>> opciones = new Vector<ComboItem<Resolucion>>();

		for(final Resolucion resolucion: Resolucion.values()) {
			opciones.add(new ComboItem<Resolucion>( String.valueOf(resolucion.getDpi()), resolucion));
		}
		return opciones;
	}



	// ************************************************** //


	private class ListenerBotonGuardar implements ActionListener {
		@Override
        public void actionPerformed(final ActionEvent e) {
            if (! seHanCubiertoTodosLosCampos()) {
            	JOptionPane.showMessageDialog(null, messageSource.getMessage("error.datosSinCubrir"), messageSource.getMessage("error.error"), JOptionPane.ERROR_MESSAGE);
		    	return;
		    }

            final ComboItem<FormatoColor> formatoColorSeleccionado = (ComboItem<FormatoColor>) formatosColor.getSelectedItem();
            final ComboItem<Resolucion> resolucionSeleccionada = (ComboItem<Resolucion>) resoluciones.getSelectedItem();

            final ScanProfile scanProfile = new ScanProfile();
            scanProfile.setNombreDispositivo(nombreDispositivo.getText());
            scanProfile.setNombre(nombrePerfil.getText());
            scanProfile.setFormatoColor(formatoColorSeleccionado.getValue());
            scanProfile.setResolucion(resolucionSeleccionada.getValue());
            scanProfile.setDuplex(duplex.isSelected());
            scanProfile.setCargaAutomatica(cargaAutomatica.isSelected());

            applicationEventPublisher.publishEvent(new CrearOActualizarScanProfileEvent(this, scanProfile));
        }

		private boolean seHanCubiertoTodosLosCampos() {
			return StringUtils.hasLength(nombrePerfil.getText()) &&
					formatosColor.getSelectedIndex() != -1 &&
					resoluciones.getSelectedIndex() != -1;
		}
	}

	private class ListenerBotonVolver implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			 dispose();
		}
	}

}
