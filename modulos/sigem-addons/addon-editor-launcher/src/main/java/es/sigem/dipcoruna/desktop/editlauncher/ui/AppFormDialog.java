package es.sigem.dipcoruna.desktop.editlauncher.ui;

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
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.sigem.dipcoruna.framework.service.i18n.SimpleMessageSource;
import es.sigem.dipcoruna.framework.ui.ImageUtils;

@Component("appFormDialog")
public class AppFormDialog extends JDialog implements InitializingBean {

	private static final long serialVersionUID = 7864450586642936806L;

	static final int OK = 0;
	private static final int CANCEL = 1;

	private final JTextField docTypeField = new JTextField();
	private final JTextField appPathField = new JTextField();
	private boolean exists = false;
	private int result = CANCEL;
	
	@Autowired
	private SimpleMessageSource messageSource;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.setTitle(messageSource.getMessage("appLauncherApplet.form.title"));
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

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent we) {
				setResult(CANCEL);
			}
		});		
	}
	
	
    public String getAppPath() {
		return this.appPathField.getText();
	}

	public void setAppPath(final String appPath) {
		this.appPathField.setText(appPath);
	}

	public String getDocType() {
		return this.docTypeField.getText();
	}

	public void setDocType(final String docType) {
		this.docTypeField.setText(docType);
	}

	public int getResult() {
		return this.result;
	}

	public void setResult(final int result) {
		this.result = result;
	}
		

	private JPanel getNorthPanel() {

    	final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        final JLabel appPathLabel = new JLabel(messageSource.getMessage("appLauncherApplet.form.message"));

        panel.add(appPathLabel);

        return panel;
    }

    private JPanel getCenterPanel() {

    	final JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    	final GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.insets = new Insets(0, 5, 0, 5);

    	if (this.exists) {
	        final JLabel docTypeLabel = new JLabel(
        		messageSource.getMessage("appLauncherApplet.form.docTypeLabel"), //$NON-NLS-1$
        		SwingConstants.TRAILING
    		);

	        c.gridx = 0;
	    	c.gridy = 0;
	        panel.add(docTypeLabel, c);

	        final JLabel docTypeValueLabel = new JLabel(getDocType(), SwingConstants.LEFT);
	        c.gridx = 1;
	    	c.gridy = 0;
	        panel.add(docTypeValueLabel, c);

    	}
    	else {
	    	this.docTypeField.setColumns(10);

	        final JLabel docTypeLabel = new JLabel(
        		messageSource.getMessage("appLauncherApplet.form.docTypeLabel"), //$NON-NLS-1$
        		SwingConstants.TRAILING
    		);
	        docTypeLabel.setLabelFor(this.docTypeField);

	        c.gridx = 0;
	    	c.gridy = 0;
	        panel.add(docTypeLabel, c);

	        c.gridx = 1;
	    	c.gridy = 0;
	        panel.add(this.docTypeField, c);
    	}

    	this.appPathField.setColumns(25);

        final JLabel appPathLabel = new JLabel(
    		messageSource.getMessage("appLauncherApplet.form.appLabel"), //$NON-NLS-1$
    		SwingConstants.TRAILING
		);
        appPathLabel.setLabelFor(this.appPathField);

        c.gridx = 0;
    	c.gridy = 1;
        panel.add(appPathLabel, c);

        c.gridx = 1;
    	c.gridy = 1;
    	panel.add(this.appPathField, c);
        
        final JButton openButton = new JButton(ImageUtils.createImageIcon("images/Open16.gif"));
        openButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.addChoosableFileFilter( new FileNameExtensionFilter(messageSource.getMessage("appLauncherApplet.select.chooser.filter.ejecutables"), "exe", "lnk"));
	            final int returnVal = fc.showOpenDialog(panel);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                final File file = fc.getSelectedFile();
	                if (file != null) {
	                	AppFormDialog.this.setAppPath(file.getAbsolutePath());
	                }
	            }
			}
    	});

        c.gridx = 2;
    	c.gridy = 1;
        panel.add(openButton, c);

        return panel;
    }

	private JPanel getButtonsPanel() {

    	final JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout());

        final JButton okButton = new JButton(
    		messageSource.getMessage("appLauncherApplet.button.ok") //$NON-NLS-1$
		);
        okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {

				if (getDocType().trim().length() == 0) {
					JOptionPane.showMessageDialog(
						AppFormDialog.this,
						messageSource.getMessage("appLauncherApplet.form.error.docType.empty"),
						messageSource.getMessage("appLauncherApplet.error.error.title"),
						JOptionPane.ERROR_MESSAGE
					);
				}
				else if (getAppPath().trim().length() == 0) {
					JOptionPane.showMessageDialog(
						AppFormDialog.this,
						messageSource.getMessage("appLauncherApplet.form.error.application.empty"),
						messageSource.getMessage("appLauncherApplet.error.error.title"),
						JOptionPane.ERROR_MESSAGE
					);
				}
				else {
					AppFormDialog.this.setResult(OK);
					AppFormDialog.this.dispose();
				}
			}
    	});
        panel.add(okButton);

        final JButton cancelButton = new JButton(
    		messageSource.getMessage("appLauncherApplet.button.cancel") //$NON-NLS-1$
		);
        cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				AppFormDialog.this.setDocType(null);
				AppFormDialog.this.setAppPath(null);
				AppFormDialog.this.setResult(CANCEL);
				AppFormDialog.this.dispose();
			}
    	});
        panel.add(cancelButton);

        return panel;
	}
}
