package es.sigem.dipcoruna.desktop.asyncUploader.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
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
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.asyncUploader.model.FTPUploadStatus;
import es.sigem.dipcoruna.desktop.asyncUploader.model.State;
import es.sigem.dipcoruna.desktop.asyncUploader.service.exception.FTPServiceException;
import es.sigem.dipcoruna.desktop.asyncUploader.service.ftp.FtpService;
import es.sigem.dipcoruna.desktop.asyncUploader.service.state.StateService;
import es.sigem.dipcoruna.desktop.asyncUploader.util.ftp.DataTransferListener;
import es.sigem.dipcoruna.desktop.asyncUploader.work.AsyncUploaderWorker;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;

/**
 * Dialog para ver las subidas pendientes y gestionarlas
 */
@Component("appResumeDialog")
public class AppResumeDialog extends JDialog implements InitializingBean, PropertyChangeListener, ActionListener {

	private static final int MAX_PERCENTAGE = 100;

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
	
	private JProgressBar progressBar = new JProgressBar(0, MAX_PERCENTAGE);
	private final JLabel estado = new JLabel();
	private JButton sendButton;
	private JButton exitButton;
	private JButton abortButton;
	private JButton deleteButton;
	private DefaultTableModel tableModel = null;
	private JTable table = null;
	
	private String key;
	
	/**
	 * Constructor
	 */
	public AppResumeDialog() {
		super(null, java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon(this.getClass().getResource("/images/icono.png")).getImage());
    }
	

	/**
	 * Carga los state correspondientes en la tabla
	 */
	private void loadTableData() {
		Map<String, State> allState = stateService.getAllState();
		if (this.tableModel != null) {
			this.tableModel.setRowCount(0);
			for (Entry<String, State> entry : allState.entrySet()) {
				this.tableModel.addRow(new Object[] { entry.getKey(), transformarState(entry.getValue())});
			}
		}
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
				break;
			case COMPLETED:
				estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.ok"));
				stateService.deleteState(this.key);
				break;
			case FAILED:
				estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.error"));
				this.sendButton.setText(messageSource.getMessage("asyncUploaderAddon.button.retry"));
				break;
			default:
				break;
			}
		} else {
			estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.error"));
			this.sendButton.setText(messageSource.getMessage("asyncUploaderAddon.button.retry"));
		}
		loadTableData();
		botonesEnEspera();
	}

	
	@Override
	public final void afterPropertiesSet() throws Exception {
		
		setTitle(messageSource.getMessage("asyncUploaderAddon.select.title"));
		setModal(true);

		final Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JPanel panelVacio = new JPanel();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1.0;
		contentPane.add(panelVacio, c);
		
		c.ipady = 20;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1.0;
		contentPane.add(getPanelBotonera(), c);
		
		JPanel panelVacioDerecha = new JPanel();
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1.0;
		contentPane.add(panelVacioDerecha, c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1.0;
		contentPane.add(getPanelProgress(), c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.weightx = 3.0;
		c.insets = new Insets(0, 10, 0, 10);
		contentPane.add(getPanelForm(), c);
		
		setResizable(false);
		if (JDialog.isDefaultLookAndFeelDecorated()) {
			final boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				setUndecorated(true);
				getRootPane().setWindowDecorationStyle(JRootPane.QUESTION_DIALOG);
			}
		}
		pack();
		loadTableData();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent we) {
			}
		});		
	}
		

	/**
	 * Compone el panel del progreso y del estado
	 * @return Panel
	 */
	private JPanel getPanelProgress() {
		final JPanel panel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxLayout);
		
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);
		Dimension d = new Dimension(250, 25);
		this.progressBar.setMinimumSize(d);
		this.progressBar.setMaximumSize(d);
		this.progressBar.setPreferredSize(d);
        panel.add(progressBar);
        
        this.estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.started"));
        panel.add(this.estado);
        
        return panel;
	}
	
	
	/**
	 * Compone el panel central del formulario
	 * @return Panel
	 */
    private JPanel getPanelForm() {
    	final JPanel panel = new JPanel();
    	panel.setLayout(new BorderLayout());
        
		this.tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 5429700966409027953L;

			@Override
			public boolean isCellEditable(final int row, final int col) {
				return false;
			}
		};
		
		this.tableModel.addColumn(messageSource.getMessage("asyncUploaderAddon.table.column.key"));
		this.tableModel.addColumn(messageSource.getMessage("asyncUploaderAddon.table.column.valor"));

		this.table = new JTable(this.tableModel);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(240);
		table.getColumnModel().getColumn(1).setPreferredWidth(440);
		
		JScrollPane panelDroga = new JScrollPane(this.table);
		panelDroga.setMinimumSize(new Dimension(680, 50));
		panel.add(panelDroga, BorderLayout.NORTH);
        
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

        this.deleteButton = new JButton(messageSource.getMessage("asyncUploaderAddon.button.delete"));
        this.deleteButton.setActionCommand("delete");
        this.deleteButton.addActionListener(this);
        this.deleteButton.setEnabled(true);
        panel.add(deleteButton);

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
			
			if (table.getSelectedRow() == -1) {
				messageDialog("asyncUploaderAddon.table.error.selected", "asyncUploaderAddon.select.error.title");
			} else {
				this.key = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
				
				botonesEnProceso();
				
				AsyncUploaderWorker worker = new AsyncUploaderWorker();
				worker.setAppResumeDialog(this);
				worker.setFtpService(ftpService);
				dtl.setWorker(worker);
				dtl.setKey(key);
				State state = stateService.getState(this.key);
				dtl.setTotalTransferred(state.getTransferidos());
				dtl.setSize(FileUtils.getFile(state.getFilePath()).length());
				
				this.estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.working"));
				worker.setKey(key);
				worker.addPropertyChangeListener(this);
				worker.execute();
			}
		} else if (e.getActionCommand().equals("exit")) {
			dispose();
		} else if (e.getActionCommand().equals("delete")) {
			if (table.getSelectedRow() == -1) {
				messageDialog("asyncUploaderAddon.table.error.selected", "asyncUploaderAddon.select.error.title");
			} else {
				this.key = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
				stateService.deleteState(this.key);
				estado.setText(messageSource.getMessage("asyncUploaderAddon.message.state.deleted"));
				botonesEnEspera();
				loadTableData();
			}
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
			botonesEnEspera();
			loadTableData();
		}
	}

	
	/**
	 * Muestra un dialog de mensaje con las claves recibidas
	 * @param messageKey titleKey
	 * @param titleKey messageKey
	 */
	private void messageDialog(final String messageKey, final String titleKey) {
		JOptionPane.showMessageDialog(AppResumeDialog.this, messageSource.getMessage(messageKey),
				messageSource.getMessage(titleKey), JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * Botones para cuando se esta procesando una subida
	 */
	private void botonesEnProceso() {
		this.sendButton.setEnabled(false);
		this.exitButton.setEnabled(false);
		this.abortButton.setEnabled(true);
		this.deleteButton.setEnabled(false);
	}

	
	/**
	 * Botones para cuando se esta en espera de una accion
	 */
	private void botonesEnEspera() {
		this.sendButton.setEnabled(true);
		this.exitButton.setEnabled(true);
		this.abortButton.setEnabled(false);
		this.deleteButton.setEnabled(true);
	}
	
	/**
	 * Obtiene un String a partir del State
	 * @param state state
	 * @return String
	 */
	private String transformarState(final State state) {
		String percent = String.valueOf((int) ((state.getTransferidos() * 100.0f) / FileUtils.getFile(state.getFilePath()).length()));
		return "[" + percent + "%] - Ruta: " + state.getFilePath();
	}

}
