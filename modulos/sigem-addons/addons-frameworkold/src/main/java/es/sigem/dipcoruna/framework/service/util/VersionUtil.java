package es.sigem.dipcoruna.framework.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionUtil {

	private VersionUtil(){}

	/**
	 * Compara dos cadenas en formato version ("mayor.minor.rev-RCx") devolviendo los siguientes valores: <br/>
	 * <ul>
	 * <li>0, cuando ambos argumentos son iguales </li>
	 * <li>-1 cuando el prmier argumento es menor que el segundo</li>
	 * <li>1 cuando el prmier argumento es mayor que el segundo</li>
	 * </ul>
	 * @param versionAComparar
	 * @param versionObtenida
	 * @return resultado comparación
	 */
	public static int compareVersiones(final String version1, final String version2) {
		final Integer[] testVer = getVersionNumbers(version1);
		final Integer[] baseVer = getVersionNumbers(version2);

		for (int i = 0; i < testVer.length; i++) {
			if (testVer[i] != baseVer[i]) {
				return testVer[i].compareTo((baseVer[i]));
			}
		}

		return 0;
	}


	private static Integer[] getVersionNumbers(final String ver) {
		final Matcher m = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(.*)").matcher(ver);

		if (!m.matches()) {
			throw new IllegalArgumentException("Malformed FW version");
		}


		return new Integer[] {
		        Integer.parseInt(m.group(1)), // major
				Integer.parseInt(m.group(2)), // minor
				Integer.parseInt(m.group(3)), // rev
				obtenerRC(m.group(4))
		};
	}

    private static Integer obtenerRC(final String cadena) {
        final Matcher mrc = Pattern.compile("-RC(\\d+)(.*)").matcher(cadena);
        if (mrc.matches()) {
            return Integer.parseInt(mrc.group(1));
        }
        //Cuando no es una RC, se considera una versión estable que tiene preferencia sobre otras RCs
        return Integer.MAX_VALUE;
    }
}
