package es.sigem.dipcoruna.desktop.scan.service.device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.twain.TwainCapability;
import uk.co.mmscomputing.device.twain.TwainConstants;
import uk.co.mmscomputing.device.twain.TwainSource;
import es.sigem.dipcoruna.desktop.scan.model.config.FormatoColor;
import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;

public class SigemTwainSourceDevice implements SigemScannerDevice {
	private static final Logger LOGGER = LoggerFactory.getLogger(SigemTwainSourceDevice.class);
	private final TwainSource twainDevice;

	public SigemTwainSourceDevice(final TwainSource twainDevice) {
		this.twainDevice = twainDevice;
	}


	@Override
	public void setPerfil(final ScanProfile scanProfile) throws ScannerIOException {
		LOGGER.debug("Estabeciendo preferencias de profile {}", scanProfile);
		
		twainDevice.setResolution(scanProfile.getResolucion().getDpi());

		final TwainCapability cap = twainDevice.getCapability(TwainConstants.ICAP_PIXELTYPE);
		if (cap.intValue() !=  calcularConstanteTwain(scanProfile.getFormatoColor())) {
		    twainDevice.setCapability(TwainConstants.ICAP_PIXELTYPE,  calcularConstanteTwain(scanProfile.getFormatoColor()));
		}

		if (scanProfile.isDuplex() && twainDevice.getCapability(TwainConstants.CAP_DUPLEX).booleanValue()) {
		    twainDevice.setCapability(TwainConstants.CAP_DUPLEXENABLED, true);
		}

        try {
            if (scanProfile.isCargaAutomatica()) {
                twainDevice.setCapability(TwainConstants.CAP_FEEDERENABLED, true);
            }
            else {
                twainDevice.setCapability(TwainConstants.CAP_FEEDERENABLED, false);
            }
        }
        catch (final Exception e) {
            LOGGER.warn("Error al establecer la carga automática de hojas", e);
        }
		
        
        if (LOGGER.isDebugEnabled()) {
            listarCapacidadesDelEscaner();
        }
	}

	private void listarCapacidadesDelEscaner() {
	    try {	       
    	    LOGGER.debug("El escáner tiene capacidad Duplex: {}. Está la capacidad Duplex activada: {}",  
                    new Object[] {twainDevice.getCapability(TwainConstants.CAP_DUPLEX).booleanValue(), 
    	            twainDevice.getCapability(TwainConstants.CAP_DUPLEXENABLED).booleanValue()});
    	    
    	    
    	    LOGGER.debug("El escáner tiene autofeed activado (ADF): {}",  
                    new Object[] {twainDevice.getCapability(TwainConstants.CAP_FEEDERENABLED).booleanValue()});	    	   
    	}
	    catch(Exception e) {
	        LOGGER.debug("Se ha producido un error al consultar las capacidades del escaner para generar la información de log", e);
	    }
	    
	    /*
	    printCapability(TwainConstants.CAP_AUTHOR, "TwainConstants.CAP_AUTHOR");
	    printCapability(TwainConstants.CAP_CAPTION, "TwainConstants.CAP_CAPTION");
	    printCapability(TwainConstants.CAP_FEEDERENABLED, "TwainConstants.CAP_FEEDERENABLED");
	    printCapability(TwainConstants.CAP_FEEDERLOADED, "TwainConstants.CAP_FEEDERLOADED");
	    printCapability(TwainConstants.CAP_TIMEDATE, "TwainConstants.CAP_TIMEDATE");
	    printCapability(TwainConstants.CAP_SUPPORTEDCAPS, "TwainConstants.CAP_SUPPORTEDCAPS");
	    printCapability(TwainConstants.CAP_EXTENDEDCAPS, "TwainConstants.CAP_EXTENDEDCAPS");
	    printCapability(TwainConstants.CAP_AUTOFEED, "TwainConstants.CAP_AUTOFEED");
	    printCapability(TwainConstants.CAP_CLEARPAGE, "TwainConstants.CAP_CLEARPAGE");
	    printCapability(TwainConstants.CAP_FEEDPAGE, "TwainConstants.CAP_FEEDPAGE");
	    printCapability(TwainConstants.CAP_REWINDPAGE, "TwainConstants.CAP_REWINDPAGE");
	    printCapability(TwainConstants.CAP_INDICATORS, "TwainConstants.CAP_INDICATORS");
	    printCapability(TwainConstants.CAP_SUPPORTEDCAPSEXT, "TwainConstants.CAP_SUPPORTEDCAPSEXT");
	    printCapability(TwainConstants.CAP_PAPERDETECTABLE, "TwainConstants.CAP_PAPERDETECTABLE");
	    printCapability(TwainConstants.CAP_UICONTROLLABLE, "TwainConstants.CAP_UICONTROLLABLE");
	    printCapability(TwainConstants.CAP_DEVICEONLINE, "TwainConstants.CAP_DEVICEONLINE");
	    printCapability(TwainConstants.CAP_AUTOSCAN, "TwainConstants.CAP_AUTOSCAN");
	    printCapability(TwainConstants.CAP_THUMBNAILSENABLED, "TwainConstants.CAP_THUMBNAILSENABLED");
	    printCapability(TwainConstants.CAP_DUPLEX, "TwainConstants.CAP_DUPLEX");
	    printCapability(TwainConstants.CAP_DUPLEXENABLED, "TwainConstants.CAP_DUPLEXENABLED");
	    printCapability(TwainConstants.CAP_ENABLEDSUIONLY, "TwainConstants.CAP_ENABLEDSUIONLY");
	    printCapability(TwainConstants.CAP_CUSTOMDSDATA, "TwainConstants.CAP_CUSTOMDSDATA");
	    printCapability(TwainConstants.CAP_ENDORSER, "TwainConstants.CAP_ENDORSER");
	    printCapability(TwainConstants.CAP_JOBCONTROL, "TwainConstants.CAP_JOBCONTROL");
	    printCapability(TwainConstants.CAP_ALARMS, "TwainConstants.CAP_ALARMS");
	    printCapability(TwainConstants.CAP_ALARMVOLUME, "TwainConstants.CAP_ALARMVOLUME");
	    printCapability(TwainConstants.CAP_AUTOMATICCAPTURE, "TwainConstants.CAP_AUTOMATICCAPTURE");
	    printCapability(TwainConstants.CAP_TIMEBEFOREFIRSTCAPTURE, "TwainConstants.CAP_TIMEBEFOREFIRSTCAPTURE");
	    printCapability(TwainConstants.CAP_TIMEBETWEENCAPTURES, "TwainConstants.CAP_TIMEBETWEENCAPTURES");
	    printCapability(TwainConstants.CAP_CLEARBUFFERS, "TwainConstants.CAP_CLEARBUFFERS");
	    printCapability(TwainConstants.CAP_MAXBATCHBUFFERS, "TwainConstants.CAP_MAXBATCHBUFFERS");
	    printCapability(TwainConstants.CAP_DEVICETIMEDATE, "TwainConstants.CAP_DEVICETIMEDATE");
	    printCapability(TwainConstants.CAP_POWERSUPPLY, "TwainConstants.CAP_POWERSUPPLY");
	    printCapability(TwainConstants.CAP_CAMERAPREVIEWUI, "TwainConstants.CAP_CAMERAPREVIEWUI");
	    printCapability(TwainConstants.CAP_DEVICEEVENT, "TwainConstants.CAP_DEVICEEVENT");
	    printCapability(TwainConstants.CAP_PAGEMULTIPLEACQUIRE, "TwainConstants.CAP_PAGEMULTIPLEACQUIRE");
	    printCapability(TwainConstants.CAP_SERIALNUMBER, "TwainConstants.CAP_SERIALNUMBER");
	    printCapability(TwainConstants.CAP_FILESYSTEM, "TwainConstants.CAP_FILESYSTEM");
	    printCapability(TwainConstants.CAP_PRINTER, "TwainConstants.CAP_PRINTER");
	    printCapability(TwainConstants.CAP_PRINTERENABLED, "TwainConstants.CAP_PRINTERENABLED");
	    printCapability(TwainConstants.CAP_PRINTERINDEX, "TwainConstants.CAP_PRINTERINDEX");
	    printCapability(TwainConstants.CAP_PRINTERMODE, "TwainConstants.CAP_PRINTERMODE");
	    printCapability(TwainConstants.CAP_PRINTERSTRING, "TwainConstants.CAP_PRINTERSTRING");
	    printCapability(TwainConstants.CAP_PRINTERSUFFIX, "TwainConstants.CAP_PRINTERSUFFIX");
	    printCapability(TwainConstants.CAP_LANGUAGE, "TwainConstants.CAP_LANGUAGE");
	    printCapability(TwainConstants.CAP_FEEDERALIGNMENT, "TwainConstants.CAP_FEEDERALIGNMENT");
	    printCapability(TwainConstants.CAP_FEEDERORDER, "TwainConstants.CAP_FEEDERORDER");
	    printCapability(TwainConstants.CAP_PAPERBINDING, "TwainConstants.CAP_PAPERBINDING");
	    printCapability(TwainConstants.CAP_REACQUIREALLOWED, "TwainConstants.CAP_REACQUIREALLOWED");
	    printCapability(TwainConstants.CAP_PASSTHRU, "TwainConstants.CAP_PASSTHRU");
	    printCapability(TwainConstants.CAP_BATTERYMINUTES, "TwainConstants.CAP_BATTERYMINUTES");
	    printCapability(TwainConstants.CAP_BATTERYPERCENTAGE, "TwainConstants.CAP_BATTERYPERCENTAGE");
	    printCapability(TwainConstants.CAP_POWERDOWNTIME, "TwainConstants.CAP_POWERDOWNTIME");
	    printCapability(TwainConstants.ICAP_AUTOBRIGHT, "TwainConstants.ICAP_AUTOBRIGHT");
	    printCapability(TwainConstants.ICAP_BRIGHTNESS, "TwainConstants.ICAP_BRIGHTNESS");
	    printCapability(TwainConstants.ICAP_CONTRAST, "TwainConstants.ICAP_CONTRAST");
	    printCapability(TwainConstants.ICAP_CUSTHALFTONE, "TwainConstants.ICAP_CUSTHALFTONE");
	    printCapability(TwainConstants.ICAP_EXPOSURETIME, "TwainConstants.ICAP_EXPOSURETIME");
	    printCapability(TwainConstants.ICAP_FILTER, "TwainConstants.ICAP_FILTER");
	    printCapability(TwainConstants.ICAP_FLASHUSED, "TwainConstants.ICAP_FLASHUSED");
	    printCapability(TwainConstants.ICAP_GAMMA, "TwainConstants.ICAP_GAMMA");
	    printCapability(TwainConstants.ICAP_HALFTONES, "TwainConstants.ICAP_HALFTONES");
	    printCapability(TwainConstants.ICAP_HIGHLIGHT, "TwainConstants.ICAP_HIGHLIGHT");
	    printCapability(TwainConstants.ICAP_IMAGEFILEFORMAT, "TwainConstants.ICAP_IMAGEFILEFORMAT");
	    printCapability(TwainConstants.ICAP_LAMPSTATE, "TwainConstants.ICAP_LAMPSTATE");
	    printCapability(TwainConstants.ICAP_LIGHTSOURCE, "TwainConstants.ICAP_LIGHTSOURCE");
	    printCapability(TwainConstants.ICAP_ORIENTATION, "TwainConstants.ICAP_ORIENTATION");
	    printCapability(TwainConstants.ICAP_PHYSICALWIDTH, "TwainConstants.ICAP_PHYSICALWIDTH");
	    printCapability(TwainConstants.ICAP_PHYSICALHEIGHT, "TwainConstants.ICAP_PHYSICALHEIGHT");
	    printCapability(TwainConstants.ICAP_SHADOW, "TwainConstants.ICAP_SHADOW");
	    printCapability(TwainConstants.ICAP_FRAMES, "TwainConstants.ICAP_FRAMES");
	    printCapability(TwainConstants.ICAP_XNATIVERESOLUTION, "TwainConstants.ICAP_XNATIVERESOLUTION");
	    printCapability(TwainConstants.ICAP_YNATIVERESOLUTION, "TwainConstants.ICAP_YNATIVERESOLUTION");
	    printCapability(TwainConstants.ICAP_XRESOLUTION, "TwainConstants.ICAP_XRESOLUTION");
	    printCapability(TwainConstants.ICAP_YRESOLUTION, "TwainConstants.ICAP_YRESOLUTION");
	    printCapability(TwainConstants.ICAP_MAXFRAMES, "TwainConstants.ICAP_MAXFRAMES");
	    printCapability(TwainConstants.ICAP_TILES, "TwainConstants.ICAP_TILES");
	    printCapability(TwainConstants.ICAP_BITORDER, "TwainConstants.ICAP_BITORDER");
	    printCapability(TwainConstants.ICAP_CCITTKFACTOR, "TwainConstants.ICAP_CCITTKFACTOR");
	    printCapability(TwainConstants.ICAP_LIGHTPATH, "TwainConstants.ICAP_LIGHTPATH");
	    printCapability(TwainConstants.ICAP_PIXELFLAVOR, "TwainConstants.ICAP_PIXELFLAVOR");
	    printCapability(TwainConstants.ICAP_PLANARCHUNKY, "TwainConstants.ICAP_PLANARCHUNKY");
	    printCapability(TwainConstants.ICAP_ROTATION, "TwainConstants.ICAP_ROTATION");
	    printCapability(TwainConstants.ICAP_SUPPORTEDSIZES, "TwainConstants.ICAP_SUPPORTEDSIZES");
	    printCapability(TwainConstants.ICAP_THRESHOLD, "TwainConstants.ICAP_THRESHOLD");
	    printCapability(TwainConstants.ICAP_XSCALING, "TwainConstants.ICAP_XSCALING");
	    printCapability(TwainConstants.ICAP_YSCALING, "TwainConstants.ICAP_YSCALING");
	    printCapability(TwainConstants.ICAP_BITORDERCODES, "TwainConstants.ICAP_BITORDERCODES");
	    printCapability(TwainConstants.ICAP_PIXELFLAVORCODES, "TwainConstants.ICAP_PIXELFLAVORCODES");
	    printCapability(TwainConstants.ICAP_JPEGPIXELTYPE, "TwainConstants.ICAP_JPEGPIXELTYPE");
	    printCapability(TwainConstants.ICAP_TIMEFILL, "TwainConstants.ICAP_TIMEFILL");
	    printCapability(TwainConstants.ICAP_BITDEPTH, "TwainConstants.ICAP_BITDEPTH");
	    printCapability(TwainConstants.ICAP_BITDEPTHREDUCTION, "TwainConstants.ICAP_BITDEPTHREDUCTION");
	    printCapability(TwainConstants.ICAP_UNDEFINEDIMAGESIZE, "TwainConstants.ICAP_UNDEFINEDIMAGESIZE");
	    printCapability(TwainConstants.ICAP_IMAGEDATASET, "TwainConstants.ICAP_IMAGEDATASET");
	    printCapability(TwainConstants.ICAP_EXTIMAGEINFO, "TwainConstants.ICAP_EXTIMAGEINFO");
	    printCapability(TwainConstants.ICAP_MINIMUMHEIGHT, "TwainConstants.ICAP_MINIMUMHEIGHT");
	    printCapability(TwainConstants.ICAP_MINIMUMWIDTH, "TwainConstants.ICAP_MINIMUMWIDTH");
	    printCapability(TwainConstants.ICAP_AUTODISCARDBLANKPAGES, "TwainConstants.ICAP_AUTODISCARDBLANKPAGES");
	    printCapability(TwainConstants.ICAP_FLIPROTATION, "TwainConstants.ICAP_FLIPROTATION");
	    printCapability(TwainConstants.ICAP_BARCODEDETECTIONENABLED, "TwainConstants.ICAP_BARCODEDETECTIONENABLED");
	    printCapability(TwainConstants.ICAP_SUPPORTEDBARCODETYPES, "TwainConstants.ICAP_SUPPORTEDBARCODETYPES");
	    printCapability(TwainConstants.ICAP_BARCODEMAXSEARCHPRIORITIES, "TwainConstants.ICAP_BARCODEMAXSEARCHPRIORITIES");
	    printCapability(TwainConstants.ICAP_BARCODESEARCHPRIORITIES, "TwainConstants.ICAP_BARCODESEARCHPRIORITIES");
	    printCapability(TwainConstants.ICAP_BARCODESEARCHMODE, "TwainConstants.ICAP_BARCODESEARCHMODE");
	    printCapability(TwainConstants.ICAP_BARCODEMAXRETRIES, "TwainConstants.ICAP_BARCODEMAXRETRIES");
	    printCapability(TwainConstants.ICAP_BARCODETIMEOUT, "TwainConstants.ICAP_BARCODETIMEOUT");
	    printCapability(TwainConstants.ICAP_ZOOMFACTOR, "TwainConstants.ICAP_ZOOMFACTOR");
	    printCapability(TwainConstants.ICAP_PATCHCODEDETECTIONENABLED, "TwainConstants.ICAP_PATCHCODEDETECTIONENABLED");
	    printCapability(TwainConstants.ICAP_SUPPORTEDPATCHCODETYPES, "TwainConstants.ICAP_SUPPORTEDPATCHCODETYPES");
	    printCapability(TwainConstants.ICAP_PATCHCODEMAXSEARCHPRIORITIES, "TwainConstants.ICAP_PATCHCODEMAXSEARCHPRIORITIES");
	    printCapability(TwainConstants.ICAP_PATCHCODESEARCHPRIORITIES, "TwainConstants.ICAP_PATCHCODESEARCHPRIORITIES");
	    printCapability(TwainConstants.ICAP_PATCHCODESEARCHMODE, "TwainConstants.ICAP_PATCHCODESEARCHMODE");
	    printCapability(TwainConstants.ICAP_PATCHCODEMAXRETRIES, "TwainConstants.ICAP_PATCHCODEMAXRETRIES");
	    printCapability(TwainConstants.ICAP_PATCHCODETIMEOUT, "TwainConstants.ICAP_PATCHCODETIMEOUT");
	    printCapability(TwainConstants.ICAP_FLASHUSED2, "TwainConstants.ICAP_FLASHUSED2");
	    printCapability(TwainConstants.ICAP_IMAGEFILTER, "TwainConstants.ICAP_IMAGEFILTER");
	    printCapability(TwainConstants.ICAP_NOISEFILTER, "TwainConstants.ICAP_NOISEFILTER");
	    printCapability(TwainConstants.ICAP_OVERSCAN, "TwainConstants.ICAP_OVERSCAN");
	    printCapability(TwainConstants.ICAP_AUTOMATICBORDERDETECTION, "TwainConstants.ICAP_AUTOMATICBORDERDETECTION");
	    printCapability(TwainConstants.ICAP_AUTOMATICDESKEW, "TwainConstants.ICAP_AUTOMATICDESKEW");
	    printCapability(TwainConstants.ICAP_AUTOMATICROTATE, "TwainConstants.ICAP_AUTOMATICROTATE");  
	    */ 
    }

	
	
	/*
    private void printCapability(int cap, String nombreCap) {
        try {
            LOGGER.debug("Capability: {} valor: {}",  new Object[] {nombreCap, twainDevice.getCapability(cap)});                          
        }
          catch(Exception e) {
                LOGGER.debug("Error al leer capability {}", nombreCap);
        }       
    }
	 */

    private int calcularConstanteTwain(final FormatoColor formatoColor) {
		switch(formatoColor) {
		case COLOR:
			return TwainConstants.TWPT_RGB;
		case BLANCO_NEGRO:
			return TwainConstants.TWPT_BW;
		case ESCALA_GRISES:
			return TwainConstants.TWPT_GRAY;
		default:
			throw new IllegalArgumentException("Formato de color no válido: " + formatoColor);
		}
	}

	// Métodos wrapper //

	@Override
	public boolean getCancel() {
		return twainDevice.getCancel();
	}


	@Override
	public void select(final String arg0) throws ScannerIOException {
		twainDevice.select(arg0);
	}

	@Override
	public void setCancel(final boolean arg0) {
		twainDevice.setCancel(arg0);
	}

	@Override
	public void setRegionOfInterest(final int arg0, final int arg1, final int arg2, final int arg3)
			throws ScannerIOException {
		twainDevice.setRegionOfInterest(arg0, arg1, arg2, arg3);
	}

	@Override
	public void setRegionOfInterest(final double arg0, final double arg1, final double arg2,
			final double arg3) throws ScannerIOException {
		twainDevice.setRegionOfInterest(arg0, arg1, arg2, arg3);
	}

	@Override
	public void setResolution(final double arg0) throws ScannerIOException {
		twainDevice.setResolution(arg0);
	}

	@Override
	public void setShowProgressBar(final boolean arg0) throws ScannerIOException {
		twainDevice.setShowProgressBar(arg0);
	}

	@Override
	public void setShowUserInterface(final boolean arg0) throws ScannerIOException {
		twainDevice.setShowUserInterface(arg0);
	}
}
