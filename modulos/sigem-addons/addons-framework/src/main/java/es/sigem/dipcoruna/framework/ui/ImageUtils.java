package es.sigem.dipcoruna.framework.ui;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ImageUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

	public static ImageIcon createImageIcon(String path) {
		Resource resource = new ClassPathResource(path);
		if (! resource.exists()) {
			LOGGER.error("No se pudo cargar la imagen: {}", path);
			return null;
		}

		try {
			return new ImageIcon(resource.getURL());
		} catch (IOException e) {//Ya se comprobó que existe
			return null;
		}
	}

    public static Image createImage(Class<?> baseClass, String path, String description) {
    	Resource resource = new ClassPathResource(path);
    	if (! resource.exists()) {
			LOGGER.error("No se pudo cargar la imagen: {}", path);
			return null;
		}

    	try {
    		return (new ImageIcon(resource.getURL(), description)).getImage();
		} catch (IOException e) {//Ya se comprobó que existe
			return null;
		}
   }

}
