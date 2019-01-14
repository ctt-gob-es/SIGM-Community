package es.sigem.dipcoruna.framework.service.util.windows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WinRegistryUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(WinRegistryUtil.class);

	private static final String REG_SZ = "REG_SZ"; 
	private static final String REG_EXPAND_SZ = "REG_EXPAND_SZ";
	private static final String DOT_EXE = ".exe";
	private static final String HKEY_CLASSES_ROOT_PATH = "HKCR\\";

	private WinRegistryUtil() {		
	}

	
    private static String readRegistry(final String keyPath) {
        try {
        	ProcessBuilder pb = new ProcessBuilder("reg", "QUERY", keyPath, "/ve");
            try {
            	final Class<?> redirectClass = Class.forName("java.lang.ProcessBuilder$Redirect");
            	final Object pipeRedirectValue = redirectClass.getField("PIPE").get(null); 
            	ProcessBuilder.class.getMethod("redirectOutput", redirectClass).invoke(pb, pipeRedirectValue);
            	ProcessBuilder.class.getMethod("redirectInput", redirectClass).invoke(pb, pipeRedirectValue); 
            }
            catch (final Exception e) {
            	// Se ignora
            }

        	Process process = pb.start();

            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult().trim();

            if (output.contains(REG_EXPAND_SZ)) {
            	output = output.replace("%SystemRoot%", System.getenv("SystemRoot")) //$NON-NLS-1$ //$NON-NLS-2$
        			.replace("%ProgramFiles%", "ProgramFiles") //$NON-NLS-1$ //$NON-NLS-2$
        			.replace(REG_EXPAND_SZ, REG_SZ);
            }

            if (!output.contains(REG_SZ)) {
            	return null;
            }

            output = output.substring(output.indexOf(REG_SZ) + REG_SZ.length(), output.length()).trim();

            return output;

        }
        catch (Exception e) {
        	LOGGER.error("Ha ocurrido un error al obtener la asociacion del registro: {}", keyPath, e); 
            return null;
        }

    }

    private static class StreamReader extends Thread {
        private final InputStream is;
        private final StringWriter sw= new StringWriter();

        StreamReader(final InputStream is) {
            this.is = is;
        }

        @Override
		public void run() {
            try {
                int c;
                while ((c = this.is.read()) != -1) {
					this.sw.write(c);
				}
            }
            catch (final IOException e) {
            	// Se ignora
            }
        }

        String getResult() {
            return this.sw.toString();
        }
    }

    
    public static String getAssociatedProgram(final String extensionIncludingDot) {        
    	String proc = WinRegistryUtil.readRegistry(HKEY_CLASSES_ROOT_PATH + extensionIncludingDot);
    	if (proc == null) {
    		return null;
    	}
    	String comm = WinRegistryUtil.readRegistry(HKEY_CLASSES_ROOT_PATH + extensionIncludingDot + "\\shell\\open\\command"); 
		if (comm == null) {
			return getAssociatedProgram(proc);
		}
		if (!comm.toLowerCase().contains(DOT_EXE)) {
			return null;
		}
		comm = comm.replace("\"", "").substring(0, comm.toLowerCase().indexOf(DOT_EXE) + DOT_EXE.length());
		if (!new File(comm).exists()) {
			return null;
		}
		if (comm.toLowerCase().endsWith("rundll32.exe")) { //$NON-NLS-1$
			return null;
		}
		return comm;
    }
}
