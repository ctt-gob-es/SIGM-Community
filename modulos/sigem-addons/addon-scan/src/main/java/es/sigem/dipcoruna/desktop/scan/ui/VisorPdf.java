package es.sigem.dipcoruna.desktop.scan.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;


//No es un componente de spring
public class VisorPdf extends JDialog {
	private static final long serialVersionUID = 659011835660070218L;
	private final SwingController controller;

	public VisorPdf() {
		this.setModal(true);
		final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int)screenDimension.getWidth() - 10, (int)screenDimension.getHeight() - 20);
		this.setLocationRelativeTo(null);
		this.controller = new SwingController();

		this.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosed(final WindowEvent e) {
				controller.closeDocument();
			}

			@Override
            public void windowClosing(final WindowEvent e) {
				controller.closeDocument();
			}
		});
	}


    public void verPdf(final String filePath) {
        // Build a SwingViewFactory configured with the controller
        final SwingViewBuilder factory = new SwingViewBuilder(controller);
        factory.buildViewMenu();
        final JPanel viewerComponentPanel = factory.buildViewerPanel();
        viewerComponentPanel.remove(factory.buildSaveAsFileButton());
        viewerComponentPanel.remove(factory.buildAnnotationlToolBar());
        viewerComponentPanel.remove(factory.buildAnnotationUtilityToolBar());
        viewerComponentPanel.remove(factory.buildAnnotationPanel());
        // add copy keyboard command
        ComponentKeyBinding.install(controller, viewerComponentPanel);
        // add interactive mouse link annotation support via callback
        controller.getDocumentViewController().setAnnotationCallback(new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));


        this.add(viewerComponentPanel);
        // Open a PDF document to view
        controller.openDocument(filePath);
        repaint();
    }



    public static void main(final String[] args) {
    	final VisorPdf visor = new VisorPdf();
    	visor.verPdf("C:/Users/EMILIO/AppData/Local/Temp/sigem-scan/itrvHjraLprJBr8WNEKP.pdf");
    	visor.setVisible(true);
	}
}
