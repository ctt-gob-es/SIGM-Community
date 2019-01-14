package es.sigem.dipcoruna.desktop.scan.service;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageWriterWrapper {

	void init();

	void write(BufferedImage bufferedImage);

	void finishWrite();

	boolean seEscribioFichero();
	
	File getFile();
}