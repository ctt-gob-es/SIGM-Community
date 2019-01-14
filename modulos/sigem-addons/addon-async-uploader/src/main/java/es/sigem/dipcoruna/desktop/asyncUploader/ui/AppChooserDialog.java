package es.sigem.dipcoruna.desktop.asyncUploader.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.asyncUploader.model.FTPUploadStatus;
import es.sigem.dipcoruna.desktop.asyncUploader.service.exception.FTPServiceException;
import es.sigem.dipcoruna.desktop.asyncUploader.service.ftp.FtpService;
import es.sigem.dipcoruna.desktop.asyncUploader.service.state.StateService;
import es.sigem.dipcoruna.desktop.asyncUploader.util.ftp.DataTransferListener;
import es.sigem.dipcoruna.desktop.asyncUploader.work.AsyncUploaderWorker;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;
import es.sigem.dipcoruna.framework.ui.ImageUtils;

/**
 * Dialog para escoger el documento y subirlo al FTP
 */
@Component("appChooserDialog")
public class AppChooserDialog extends JDialog implements InitializingBean, PropertyChangeListener, ActionListener {

	private static final int FORM_FIELDS_SIZE = 25;
	
	private static final int MAX_PERCENTAGE = 100;
	
	private static final long MAX_SIZE = 2147483648L; 

	private static final long serialVersionUID = 7159391470426550788L;

	@Autowired
	private SimpleMessageSource messageSource;
	
	@Autowired
	private DataTransferListener dtl;
	
	@Autowired
	private StateService stateService;
	
	@Autowired
	private FtpService ftpService;
	
	@Value("${param.entidad}")
	private String entidad;
	
	@Value("${param.tpdoc}")
	private String tpDoc;
	
	@Value("${param.user}")
	private String user;

	@Value("${param.tramite}")
	private String tramite;
	
	@Value("${param.fase}")
	private String fase;
	
	@Value("${param.tipodestino}")
	private String tipoDestino;
	
	@Value("${param.iddestino}")
	private String idDestino;
	
	private final JTextField fileNameField = new JTextField();
	private final JTextField filePathField = new JTextField();
	private JButton openButton;
	private final JLabel superiorLabel = new JLabel(); 
	private JProgressBar progressBar = new JProgressBar(0, MAX_PERCENTAGE);
	private final JLabel estado = new JLabel();
	private JButton sendButton;
	private JButton exitButton;
	private JButton abortButton;
	
	private String key;
	
	/**
	 * Constructor
	 */
	public AppChooserDialog() {
		super(null, java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon(this.getClass().getResource("/images/icono.png")).getImage());
    }
	
	
	/**
	 * UploadStatus
	 * @param value Value
	 */
	public final void uploadResult(final FTPUploadStatus value) {
		if (value != null) {
			switch (value) {
			case ABORTED:
				estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.canceled"));
				stateService.deleteState(this.key);
				salirActivo();
				break;
			case COMPLETED:
				estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.ok"));
				stateService.deleteState(this.key);
				salirActivo();
				break;
			case FAILED:
				estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.error"));
				this.sendButton.setText(messageSource.getMessage("asyncUploaderAddon.button.retry"));
				enviarSalirActivo();
				break;
			default:
				break;
			}
		} else {
			estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.error"));
			this.sendButton.setText(messageSource.getMessage("asyncUploaderAddon.button.retry"));
			enviarSalirActivo();
		}
	}

	
    public String getFilePath() {
		return this.filePathField.getText();
	}

	public void setFilePath(final String filePath) {
		this.filePathField.setText(filePath);
	}

	public String getFileName() {
		return this.fileNameField.getText();
	}

	public void setDefaultFilePath(String defaultFilePath) {
		this.filePathField.setText(defaultFilePath);
		pack();
	}
	
	public void setDocumentExtension(String documentExtension) {
		 superiorLabel.setText(messageSource.getMessage("appLauncherApplet.select.message", new Object [] {documentExtension}));
		 pack();
	}
	
	
	@Override
	public final void afterPropertiesSet() throws Exception {
		
		setTitle(messageSource.getMessage("asyncUploaderAddon.select.title"));
		setModal(true);

		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(getPanelDescripcion(), BorderLayout.NORTH);
		contentPane.add(getPanelForm(), BorderLayout.CENTER);
		contentPane.add(getPanelBotonera(), BorderLayout.SOUTH);
		
		setResizable(false);
		if (JDialog.isDefaultLookAndFeelDecorated()) {
			final boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				setUndecorated(true);
				getRootPane().setWindowDecorationStyle(JRootPane.QUESTION_DIALOG);
			}
		}
		pack();
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent we) {
			}
		});		
	}
		

	/**
	 * Panel superior con el texto
	 * @return Panel
	 */
	private JPanel getPanelDescripcion() {
    	final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        superiorLabel.setText(messageSource.getMessage("asyncUploaderAddon.select.message", new Object [] {""}));
        panel.add(superiorLabel);
        return panel;
    }

	
	/**
	 * Compone el panel central del formulario
	 * @return Panel
	 */
    private JPanel getPanelForm() {
    	final JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	
    	final GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.insets = new Insets(0, 5, 0, 5);

    	this.fileNameField.setColumns(FORM_FIELDS_SIZE);
    	this.fileNameField.setEditable(true);

    	this.filePathField.setColumns(FORM_FIELDS_SIZE);
        this.filePathField.setEditable(false);

        final JLabel fileNameLabel = new JLabel(messageSource.getMessage("asyncUploaderAddon.select.label.name"), SwingConstants.TRAILING);        
        fileNameLabel.setLabelFor(this.filePathField);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(fileNameLabel, c);
        
        c.gridx = 1;
        c.gridy = 0;
        panel.add(this.fileNameField, c);
        
        final JLabel filePathLabel = new JLabel(messageSource.getMessage("asyncUploaderAddon.select.label.file"), SwingConstants.TRAILING);        
        filePathLabel.setLabelFor(this.filePathField);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(filePathLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(this.filePathField, c);
        
        this.openButton = new JButton(ImageUtils.createImageIcon("images/Open16.gif"));
        this.openButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser filechooser = new JFileChooser();
				filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            final int returnVal = filechooser.showOpenDialog(panel);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                final File file = filechooser.getSelectedFile();
	                if (file != null) {
	                	setFilePath(file.getAbsolutePath());
	                }
	            }
			}
    	});

        c.gridx = 2;
        c.gridy = 1;
        panel.add(openButton, c);

        c.gridx = 1;
        c.gridy = 2;
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);
        panel.add(progressBar, c);
        
        c.gridx = 1;
        c.gridy = 3;
        this.estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.started"));
        panel.add(this.estado, c);
        
        return panel;
    }

    
    /**
     * Construye el panel que contiene la botonera
     * @return Panel
     */
	private JPanel getPanelBotonera() {
    	final JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout());

    	this.sendButton = new JButton(messageSource.getMessage("asyncUploaderAddon.button.send"));
        this.sendButton.setActionCommand("start");
        this.sendButton.addActionListener(this);
        this.sendButton.setEnabled(true);
        panel.add(this.sendButton);

        this.abortButton = new JButton(messageSource.getMessage("asyncUploaderAddon.button.abort"));
        this.abortButton.setActionCommand("abort");
        this.abortButton.addActionListener(this);
        this.abortButton.setEnabled(false);
        panel.add(abortButton);

        this.exitButton = new JButton(messageSource.getMessage("asyncUploaderAddon.button.cancel"));
        this.exitButton.setActionCommand("exit");
        this.exitButton.addActionListener(this);
        panel.add(exitButton);

        return panel;
	}


	/**
	 * Implementacion de propertyChange
	 */
	@Override
	public final void propertyChange(final PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			Integer value = (Integer) evt.getNewValue();
			progressBar.setValue(value);
		}
	}

	
	@Override
	public final void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("start")) {
			if (getFileName().trim().length() == 0) {
				messageDialog("asyncUploaderAddon.select.error.name.empty", "asyncUploaderAddon.select.error.title");
			} else if (getFilePath().trim().length() == 0) {
				messageDialog("asyncUploaderAddon.select.error.file.empty", "asyncUploaderAddon.select.error.title");
			} else if (FileUtils.getFile(getFilePath()).length() > MAX_SIZE) {
				messageDialog("asyncUploaderAddon.select.error.file.toobig", "asyncUploaderAddon.select.error.title");
			} else {
				this.openButton.setEnabled(false);
				this.fileNameField.setEnabled(false);
				this.filePathField.setEnabled(false);
				abortarActivo();
				AsyncUploaderWorker worker = new AsyncUploaderWorker();
				worker.setAppChooserDialog(this);
				worker.setFtpService(ftpService);
				dtl.setWorker(worker);
				if (key == null) {
					this.key = stateService.addState(getFilePath(), entidad, tramite, tpDoc, user, fase, tipoDestino, idDestino, getFileName());
					dtl.setSize(FileUtils.getFile(getFilePath()).length());
					dtl.setKey(key);
				}
				this.estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.working"));
				worker.setKey(key);
				worker.addPropertyChangeListener(this);
				worker.execute();
			}
		} else if (e.getActionCommand().equals("exit")) {
			dispose();
		} else if (e.getActionCommand().equals("abort")) {
			if (key != null && stateService.getState(key) != null) {
				try {
					ftpService.abortarSubida(this.key);
				} catch (FTPServiceException e1) {
					messageDialog("asyncUploaderAddon.abort.error", "asyncUploaderAddon.select.error.title");
				}
				stateService.deleteState(key);
			}
			estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.canceled"));
			salirActivo();
		}
	}

	
	/**
	 * Muestra un dialog de mensaje con las claves recibidas
	 * @param messageKey titleKey
	 * @param titleKey messageKey
	 */
	private void messageDialog(final String messageKey, final String titleKey) {
		JOptionPane.showMessageDialog(AppChooserDialog.this, messageSource.getMessage(messageKey),
				messageSource.getMessage(titleKey), JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * Deja activo unicamente el boton salir
	 */
	private void salirActivo() {
		this.sendButton.setEnabled(false);
		this.exitButton.setEnabled(true);
		this.abortButton.setEnabled(false);
	}

	
	/**
	 * Deja activo unicamente el boton abortar
	 */
	private void abortarActivo() {
		this.sendButton.setEnabled(false);
		this.exitButton.setEnabled(false);
		this.abortButton.setEnabled(true);
	}

	
	/**
	 * Deja activos los botones enviar y salir
	 */
	private void enviarSalirActivo() {
		this.sendButton.setEnabled(true);
		this.exitButton.setEnabled(true);
		this.abortButton.setEnabled(false);
	}

}
