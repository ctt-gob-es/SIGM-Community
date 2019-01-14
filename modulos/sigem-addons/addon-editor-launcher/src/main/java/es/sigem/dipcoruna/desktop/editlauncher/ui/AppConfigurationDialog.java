package es.sigem.dipcoruna.desktop.editlauncher.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.desktop.editlauncher.events.listener.ErrorGeneralEventListener;
import es.sigem.dipcoruna.desktop.editlauncher.events.model.ActualizadasPreferenciaslEvent;
import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;

@Component("appConfigurationDialog")
public class AppConfigurationDialog extends JDialog implements InitializingBean {
	private static final long serialVersionUID = 2345008904923070974L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorGeneralEventListener.class);
	

	@Autowired
	private SimpleMessageSource messageSource;

	@Autowired
	private AppFormDialog appFormDialog;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher; 

	private DefaultTableModel tableModel = null;
	private JTable table = null;

	private JButton editButton = null;

	JButton getEditButton() {
		return this.editButton;
	}

	private JButton removeButton = null;

	JButton getRemoveButton() {
		return this.removeButton;
	}
	
	public void cargarPropiedades(Map<String, String> propiedades) {
		for(Entry<String, String> entry : propiedades.entrySet()) {
			this.tableModel.addRow(new Object[] { entry.getKey(), entry.getValue()});
		}
	}

	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.setTitle(messageSource.getMessage("appLauncherApplet.config.title"));
		this.setModal(true);

		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(getNorthPanel(), BorderLayout.NORTH);
		contentPane.add(getCenterPanel(), BorderLayout.CENTER);
		contentPane.add(getButtonsPanel(), BorderLayout.SOUTH);

		this.setResizable(false);
		if (JDialog.isDefaultLookAndFeelDecorated()) {
			final boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				this.setUndecorated(true);
				getRootPane().setWindowDecorationStyle(JRootPane.QUESTION_DIALOG);
			}
		}
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	

	private JPanel getNorthPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		final JLabel appPathLabel = new JLabel(
				messageSource.getMessage("appLauncherApplet.config.message"));
		panel.add(appPathLabel);

		return panel;
	}

	private JPanel getCenterPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		panel.add(getCenterTablePanel());
		panel.add(getCenterTableButtonsPanel());

		return panel;
	}

	private JPanel getCenterTablePanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		this.tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 5429700966409027953L;

			@Override
			public boolean isCellEditable(final int row, final int col) {
				return false;
			}
		};
		this.tableModel.addColumn(messageSource.getMessage("appLauncherApplet.config.col.docType"));
		this.tableModel.addColumn(messageSource.getMessage("appLauncherApplet.config.col.application"));

		this.table = new JTable(this.tableModel);
		this.table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		final ListSelectionModel rowSM = this.table.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}

				final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					AppConfigurationDialog.this.getRemoveButton().setEnabled(false);
					AppConfigurationDialog.this.getEditButton().setEnabled(false);
				} 
				else {
					AppConfigurationDialog.this.getRemoveButton().setEnabled(true);

					int cont = 0;
					final int maxSelIx = lsm.getMaxSelectionIndex();
					for (int i = lsm.getMinSelectionIndex(); i <= maxSelIx; i++) {
						if (lsm.isSelectedIndex(i)) {
							cont++;
						}
					}

					if (cont == 1) {
						AppConfigurationDialog.this.getEditButton().setEnabled(true);
					} else {
						AppConfigurationDialog.this.getEditButton().setEnabled(
								false);
					}
				}
			}
		});

		this.table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				final int c = e.getKeyCode();
				if (c == KeyEvent.VK_DELETE) {
					e.consume();
					deleteSelectedRows();
				} else if (c == KeyEvent.VK_INSERT) {
					e.consume();
					showInsertDialog();
				}
			}
		});

		this.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				e.consume();
				if (e.getClickCount() > 1) {
					showUpdateDialog();
				}
			}
		});

		final JScrollPane scrollPanel = new JScrollPane(this.table);

		panel.add(scrollPanel);
		return panel;
	}

	private JPanel getCenterTableButtonsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1, 5, 5));

		final JButton addButton = new JButton(
				messageSource.getMessage("appLauncherApplet.button.add" //$NON-NLS-1$
						));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				showInsertDialog();
			}
		});
		panel.add(addButton);

		this.editButton = new JButton(messageSource.getMessage("appLauncherApplet.button.edit"));
		this.editButton.setEnabled(false);
		this.editButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				showUpdateDialog();
			}
		});
		panel.add(this.editButton);

		this.removeButton = new JButton(messageSource.getMessage("appLauncherApplet.button.remove"));
		this.removeButton.setEnabled(false);
		this.removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				deleteSelectedRows();
			}
		});
		panel.add(this.removeButton);

		final JPanel container = new JPanel();
		container.add(panel);

		return container;
	}

	private JPanel getButtonsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		final JButton okButton = new JButton(messageSource.getMessage("appLauncherApplet.button.ok"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				updateProperties();
				AppConfigurationDialog.this.dispose();
			}
		});
		panel.add(okButton);

		final JButton cancelButton = new JButton(messageSource.getMessage("appLauncherApplet.button.cancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				AppConfigurationDialog.this.dispose();
			}
		});
		panel.add(cancelButton);

		return panel;
	}

	void showInsertDialog() {
		appFormDialog.setDocType("");
		appFormDialog.setAppPath("");
		appFormDialog.setVisible(true);		
		if (appFormDialog.getResult() == AppFormDialog.OK) {
			this.tableModel.addRow(new Object[] { appFormDialog.getDocType(), appFormDialog.getAppPath()});
		}
	}

	void showUpdateDialog() {
		// Fila seleccionada
		final int row = this.table.getSelectedRow();

		final String docType = (String) this.table.getValueAt(row, 0);
		final String appPath = (String) this.table.getValueAt(row, 1);

		appFormDialog.setDocType(docType);
		appFormDialog.setAppPath(appPath);
		appFormDialog.setVisible(true);	
		if (appFormDialog.getResult() == AppFormDialog.OK) {
			this.tableModel.setValueAt(appFormDialog.getAppPath(), row, 1);
		}
	}

	void deleteSelectedRows() {
		final int res = JOptionPane
				.showConfirmDialog(AppConfigurationDialog.this,
						messageSource.getMessage("appLauncherApplet.config.remove.confirm.message"),
						messageSource.getMessage("appLauncherApplet.error.warning.title"),
						JOptionPane.ERROR_MESSAGE);

		if (res == JOptionPane.YES_OPTION) {
			// Eliminar filas seleccionadas
			final int[] rows = this.table.getSelectedRows();
			for (int i = rows.length - 1; i >= 0; i--) {
				this.tableModel.removeRow(rows[i]);
			}
		}
	}

	synchronized void updateProperties() {		
		Map<String, String> preferencias = new HashMap<String, String>();
		
		for (int i = 0; i < this.tableModel.getRowCount(); i++) {
			preferencias.put((String) this.tableModel.getValueAt(i, 0), (String) this.tableModel.getValueAt(i, 1));		
		}		
		applicationEventPublisher.publishEvent(new ActualizadasPreferenciaslEvent(this, preferencias));		
	}
}
