package es.sigem.dipcoruna.desktop.editlauncher.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
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

@Component("appChooserDialog")
public class AppChooserDialog extends JDialog implements InitializingBean {

	private static final long serialVersionUID = 7159391470426550788L;

	public static final int OK = 0;
	private static final int CANCEL = 1;

	private final JTextField appPathField = new JTextField();
	private final JLabel appPathLabel = new JLabel(); 
	private int result = CANCEL;	
	
	@Autowired
	private SimpleMessageSource messageSource;
	
	
    public String getAppPath() {
		return this.appPathField.getText();
	}

	public void setAppPath(final String appPath) {
		this.appPathField.setText(appPath);
	}

	public int getResult() {
		return this.result;
	}

	public void setResult(final int result) {
		this.result = result;
	}
	
	
	public void setDefaultAppPath(String defaultAppPath) {
		this.appPathField.setText(defaultAppPath);
		pack();
	}
	
	public void setDocumentExtension(String documentExtension) {
		 appPathLabel.setText(messageSource.getMessage("appLauncherApplet.select.message", new Object [] {documentExtension}));
		 pack();
	}
	
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		setTitle(messageSource.getMessage("appLauncherApplet.select.title"));
		setModal(true);

		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(getPanelDescripcion(), BorderLayout.NORTH);
		contentPane.add(getPanelExplorador(), BorderLayout.CENTER);
		contentPane.add(getPanelBotonera(), BorderLayout.SOUTH);
		
		setResizable(true);		
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
				setResult(CANCEL);
			}
		});		
	}
		


	private JPanel getPanelDescripcion() {
    	final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
          
        appPathLabel.setText(messageSource.getMessage("appLauncherApplet.select.message", new Object [] {""}));
        panel.add(appPathLabel);

        return panel;
    }

	
    private JPanel getPanelExplorador() {
    	final JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.appPathField.setColumns(25);
        this.appPathField.setEditable(false);

        final JLabel appPathLabel = new JLabel(
        	messageSource.getMessage("appLauncherApplet.select.appLabel"),
    		SwingConstants.TRAILING
		);        
        appPathLabel.setLabelFor(this.appPathField);

        panel.add(appPathLabel);
        panel.add(this.appPathField);
        
        final JButton openButton = new JButton(ImageUtils.createImageIcon("images/Open16.gif"));
        openButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser filechooser = new JFileChooser();
				filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				filechooser.addChoosableFileFilter( new FileNameExtensionFilter(messageSource.getMessage("appLauncherApplet.select.chooser.filter.ejecutables"), "exe", "lnk"));
	            final int returnVal = filechooser.showOpenDialog(panel);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                final File file = filechooser.getSelectedFile();
	                if (file != null) {
	                	setAppPath(file.getAbsolutePath());
	                }
	            }
			}
    	});

        panel.add(openButton);

        return panel;
    }

	private JPanel getPanelBotonera() {
    	final JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout());

        final JButton okButton = new JButton(messageSource.getMessage("appLauncherApplet.button.ok"));		
        okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setResult(OK);
				dispose();
			}
    	});
        panel.add(okButton);

        final JButton cancelButton = new JButton(messageSource.getMessage("appLauncherApplet.button.cancel"));
        cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setAppPath(null);
				setResult(CANCEL);
				dispose();
			}
    	});
        panel.add(cancelButton);

        return panel;
	}
}
