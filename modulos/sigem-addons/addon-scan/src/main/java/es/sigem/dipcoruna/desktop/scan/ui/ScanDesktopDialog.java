package es.sigem.dipcoruna.desktop.scan.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.scan.events.model.config.listaperfiles.MostrarConfigListDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.CambioNombreFicheroEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.DispositivoSeleccionadoScanDesktopDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.SolicitadaSubidaFicherosEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.SolicitadoBorradoFicheroEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.SolicitadoEscaneadoEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.scandesktop.UnirPdfsScanDesktopDialogEvent;
import es.sigem.dipcoruna.desktop.scan.events.model.visor.MostrarVisorFicheroEvent;
import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;
import es.sigem.dipcoruna.desktop.scan.ui.model.FileJCheckBox;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;
import es.sigem.dipcoruna.framework.ui.ImageUtils;

@Component("scanDesktopDialog")
public class ScanDesktopDialog extends JDialog implements InitializingBean {
    private static final long serialVersionUID = 7159391470426550788L;

	@Autowired
	private SimpleMessageSource messageSource;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private JPanel panelLoading;
	private JPanel panelFicheros;
	private JComboBox<String> selectorDispositivos;
	private JComboBox<String> selectorPerfiles;
	private List<FileJCheckBox> checksFicherosEscaneados;


	@Override
	public void afterPropertiesSet() throws Exception {
		this.setTitle(messageSource.getMessage("escaneado.principal.titulo"));
		this.setLayout(new BorderLayout());
		this.add(initPanelCabecera(), BorderLayout.NORTH);
		this.add(initPanelTrabajo(), BorderLayout.CENTER);
		this.add(initPanelBotonera(), BorderLayout.SOUTH);
		this.setSize(730, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setModal(true);
	}


	private JPanel initPanelCabecera() {
		final JPanel jPanelDispoistivo = new JPanel();
		jPanelDispoistivo.add(new JLabel(messageSource.getMessage("etiqueta.escaner") + ":"));
		selectorDispositivos = new JComboBox<String>();
        selectorDispositivos.addActionListener (new ListenerSeleccionDispositivo());
		jPanelDispoistivo.add(selectorDispositivos);

		final JPanel jPanelPerfil = new JPanel();
		jPanelPerfil.add(new JLabel(messageSource.getMessage("etiqueta.perfil") + ":"));
		selectorPerfiles= new JComboBox<String>();
		jPanelPerfil.add(selectorPerfiles);

		final JButton botonConfiguracion = new JButton(ImageUtils.createImageIcon("images/gear.png"));
		botonConfiguracion.addActionListener(new ListenerBotonAbrirConfiguracion());
		botonConfiguracion.setBorder(BorderFactory.createEmptyBorder());
		botonConfiguracion.setContentAreaFilled(false);
		botonConfiguracion.setToolTipText(messageSource.getMessage("escaneado.navegarConfiguracion"));
		jPanelPerfil.add(botonConfiguracion);


		final JPanel jpanel = new JPanel(new BorderLayout());
		jpanel.add(jPanelDispoistivo, BorderLayout.WEST);
		jpanel.add(jPanelPerfil, BorderLayout.EAST);
		return jpanel;
	}


	private JPanel initPanelTrabajo() {
		panelFicheros = new JPanel(new GridBagLayout());

		final JScrollPane scrollPane = new JScrollPane(panelFicheros);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(5, 5, 710, 280);
        final JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(710, 280));
        contentPane.add(scrollPane);
        //contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        checksFicherosEscaneados = new ArrayList<FileJCheckBox>();
		repintarFicherosEnPanel(Collections.<File>emptyList());
		return contentPane;
	}

	private JPanel initPanelBotonera() {
	    panelLoading = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    final JLabel labelLoading = new JLabel("Subiendo archivos. Espere por favor.");
	    labelLoading.setForeground(Color.RED);
	    panelLoading.add(labelLoading);
        panelLoading.add(new JLabel(ImageUtils.createImageIcon("images/loading_32.gif")));
        panelLoading.setVisible(false);


		final JPanel jpanelBotonera = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		final JButton botonSalir = new JButton(messageSource.getMessage("boton.salir"));
	    botonSalir.addActionListener(new ListenerBotonSalir());
	        
	    final JButton botonSubirArchivos = new JButton(messageSource.getMessage("boton.subirArchivos"));
        botonSubirArchivos.addActionListener(new ListenerBotonSubirArchivos());
        
        final JButton botonGenerarLote = new JButton(messageSource.getMessage("boton.agruparLote"));
        botonGenerarLote.addActionListener(new ListenerBotonenerarLote());
	    
	    final JButton botonEscanear = new JButton(messageSource.getMessage("boton.escanear"));
		botonEscanear.addActionListener(new ListenerBotonEscanear());
		
		
		jpanelBotonera.add(botonSalir);
		jpanelBotonera.add(botonSubirArchivos);
		jpanelBotonera.add(botonGenerarLote);		
		jpanelBotonera.add(botonEscanear);


	    final JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(panelLoading, BorderLayout.WEST);
        jpanel.add(jpanelBotonera, BorderLayout.EAST);
        return jpanel;
	}


	private void repintarFicherosEnPanel(final List<File> ficheros) {
		panelFicheros.removeAll();
		checksFicherosEscaneados.clear();

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTH;
		//c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.ipady=10;

		c.gridy=0;

//		c.gridx=0;
//	    panelFicheros.add(getLabelSubrayado(messageSource.getMessage("etiqueta.documento.tipo")), c);
	        
		c.gridx=1;
		panelFicheros.add(getLabelSubrayado(messageSource.getMessage("etiqueta.documento.tipo")), c);

		c.gridx=2;
		panelFicheros.add(getLabelSubrayado(messageSource.getMessage("etiqueta.documento.nombre")), c);

		c.gridx=3;
		panelFicheros.add(getLabelSubrayado(messageSource.getMessage("etiqueta.documento.tamanho")), c);

		c.gridx=4;
		panelFicheros.add(getLabelSubrayado(messageSource.getMessage("etiqueta.documento.ver")), c);

		c.gridx=5;
		panelFicheros.add(getLabelSubrayado(messageSource.getMessage("etiqueta.documento.renombrar")), c);
		
		c.gridx=6;
		panelFicheros.add(getLabelSubrayado(messageSource.getMessage("etiqueta.documento.eliminar")), c);


		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		for(int i=0; i<ficheros.size(); i++) {
			final File file = ficheros.get(i);
			c.gridy=i+1;
			
			c.gridx=0;
            c.anchor = GridBagConstraints.CENTER;
            FileJCheckBox check = new FileJCheckBox(file);            
            checksFicherosEscaneados.add(check);
            panelFicheros.add(check, c);
            								
			
			c.gridx=1;
			c.anchor = GridBagConstraints.CENTER;
			panelFicheros.add(new JLabel(FilenameUtils.getExtension(file.getName()).toUpperCase()), c);

			c.gridx=2;
			c.anchor = GridBagConstraints.WEST;
			panelFicheros.add(new JLabel(file.getName()), c);

			c.gridx=3;
			c.anchor = GridBagConstraints.WEST;
			panelFicheros.add(new JLabel(FileUtils.byteCountToDisplaySize(file.length())), c);

			c.gridx=4;
			c.anchor = GridBagConstraints.CENTER;
			final JButton botonVerUnFichero = new JButton(ImageUtils.createImageIcon("images/eye.png"));
			botonVerUnFichero.addActionListener(new ListenerBotonVerUnFichero(file));
			botonVerUnFichero.setBorder(BorderFactory.createEmptyBorder());
			botonVerUnFichero.setContentAreaFilled(false);
			botonVerUnFichero.setToolTipText(messageSource.getMessage("escaneado.verArchivo"));
			panelFicheros.add(botonVerUnFichero, c);

			c.gridx=5;
			c.anchor = GridBagConstraints.CENTER;
			final JButton botonRenombrarUnFichero = new JButton(ImageUtils.createImageIcon("images/editar.png"));
			botonRenombrarUnFichero.addActionListener(new ListenerBotonRenombrarFichero(file));
			botonRenombrarUnFichero.setBorder(BorderFactory.createEmptyBorder());
			botonRenombrarUnFichero.setContentAreaFilled(false);
			botonRenombrarUnFichero.setToolTipText(messageSource.getMessage("escaneado.renombrarArchivo"));
			panelFicheros.add(botonRenombrarUnFichero, c);
			
			c.gridx=6;
			c.anchor = GridBagConstraints.CENTER;
			final JButton botonBorrarUnFichero = new JButton(ImageUtils.createImageIcon("images/delete.png"));
			botonBorrarUnFichero.addActionListener(new ListenerBotonBorrarUnFichero(file));
			botonBorrarUnFichero.setBorder(BorderFactory.createEmptyBorder());
			botonBorrarUnFichero.setContentAreaFilled(false);
			botonBorrarUnFichero.setToolTipText(messageSource.getMessage("escaneado.eliminarArchivo"));
			panelFicheros.add(botonBorrarUnFichero, c);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JLabel getLabelSubrayado(final String text) {
		final JLabel label = new JLabel(text);
		final Font font = label.getFont();
		final Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		label.setFont(font.deriveFont(attributes));
		return label;
	}


	public void actualizarFicheros(final List<File> ficheros) {
		repintarFicherosEnPanel(ficheros);
		pack();
	}


	public void actualizarListaDispositivos(final List<String> dispositivos) {
		selectorDispositivos.removeAllItems();
		for (final String dispositivo : dispositivos) {
			selectorDispositivos.addItem(dispositivo);
		}
	}

   public void setSelectedDispositivo(final String nombreDispositivo) {
       for (int i=0; i<selectorDispositivos.getItemCount(); i++) {
           if (selectorDispositivos.getItemAt(i).equals(nombreDispositivo)) {
               selectorDispositivos.setSelectedIndex(i);
           }
       }
    }

   public String getSelectedDispositivo() {
        return (String) selectorDispositivos.getSelectedItem();
    }


	public void actualizarListaPerfiles (final List<ScanProfile> scanProfiles) {
		selectorPerfiles.removeAllItems();
		  for (final ScanProfile scanProfile : scanProfiles) {
		      selectorPerfiles.addItem(scanProfile.getNombre());
	       }
	}

	 public void setSelectedPerfil(final String nombrePerfil) {
	       for (int i=0; i<selectorPerfiles.getItemCount(); i++) {
	           if (selectorPerfiles.getItemAt(i).equals(nombrePerfil)) {
	               selectorPerfiles.setSelectedIndex(i);
	           }
	       }
	  }


	// ********************************* //

	private class ListenerBotonEscanear implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
		    if (selectorDispositivos.getSelectedIndex() == -1) {
		        JOptionPane.showMessageDialog(null, messageSource.getMessage("error.seleccionVacia.escaner"), messageSource.getMessage("error.error"), JOptionPane.ERROR_MESSAGE);
		        return;
		    }

		    if (selectorPerfiles.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null, messageSource.getMessage("error.seleccionVacia.perfil"), messageSource.getMessage("error.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

			applicationEventPublisher.publishEvent(new SolicitadoEscaneadoEvent(this, (String)selectorDispositivos.getSelectedItem(), (String)selectorPerfiles.getSelectedItem()));
		}
	}

	private class ListenerBotonSubirArchivos implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			applicationEventPublisher.publishEvent(new SolicitadaSubidaFicherosEvent(this));
		}
	}
	
	private class ListenerBotonenerarLote implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            List<File> ficherosSeleccionados = new ArrayList<File>();
            for (FileJCheckBox fileJCheckBox : checksFicherosEscaneados) {
                if (fileJCheckBox.isSelected()) {
                    ficherosSeleccionados.add(fileJCheckBox.getFile());
                }
            }
            
            if (ficherosSeleccionados.size() < 2) {
                JOptionPane.showMessageDialog(null, messageSource.getMessage("error.seleccionVacia.ficherosLote"), messageSource.getMessage("error.error"), JOptionPane.ERROR_MESSAGE);
                return; 
            }
            
            applicationEventPublisher.publishEvent(new UnirPdfsScanDesktopDialogEvent(this, ficherosSeleccionados));
        }
    }
	
	

	private class ListenerBotonSalir implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			dispose();
		}
	}

	private class ListenerBotonAbrirConfiguracion implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			applicationEventPublisher.publishEvent(new MostrarConfigListDialogEvent(this));
		}
	}


	private class ListenerBotonVerUnFichero implements ActionListener {
		private final File file;

		public ListenerBotonVerUnFichero(final File file) {
			this.file = file;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			applicationEventPublisher.publishEvent(new MostrarVisorFicheroEvent(this, file));
		}
	}


	private class ListenerBotonBorrarUnFichero implements ActionListener {
		private final File file;

		public ListenerBotonBorrarUnFichero(final File file) {
			this.file = file;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			applicationEventPublisher.publishEvent(new SolicitadoBorradoFicheroEvent(this, file));
		}
	}
	
	private class ListenerBotonRenombrarFichero implements ActionListener {
		private final File file;

		public ListenerBotonRenombrarFichero(final File file) {
			this.file = file;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
        	String nombreFichero = JOptionPane.showInputDialog(messageSource.getMessage("escaneado.mensaje.renombrarFichero"));
        	
        	if(null == nombreFichero){
        		nombreFichero = "";
        	} else {
        		String extension = FilenameUtils.getExtension(nombreFichero);			

    			if(!isEmpty(nombreFichero) && nombreFichero.length() > 31){
    				nombreFichero = nombreFichero.substring(0, 26 - extension.length()) + "(...)";
    			}
        	}
			applicationEventPublisher.publishEvent(new CambioNombreFicheroEvent(this, file, nombreFichero));
		}
	}

	private class ListenerSeleccionDispositivo implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            applicationEventPublisher.publishEvent(new DispositivoSeleccionadoScanDesktopDialogEvent(this));
        }
    }
	
	public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
