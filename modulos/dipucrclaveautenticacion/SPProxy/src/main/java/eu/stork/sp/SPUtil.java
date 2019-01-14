package eu.stork.sp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.stork.peps.auth.commons.PEPSErrors;
import eu.stork.peps.auth.commons.PEPSParameters;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PEPSValues;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;
import eu.stork.peps.auth.commons.exceptions.SecurityPEPSException;

public class SPUtil {

	/** Sistema de traceo. */
	static Log logger = LogFactory.getLog(SPUtil.class);

	public static Properties loadConfigs(String path) throws IOException {
		Properties properties = new Properties();
		properties
				.load(SPUtil.class.getClassLoader().getResourceAsStream(path));
		return properties;
	}

	public static final String createPersonalAttributeList(
			PersonalAttributeList personalAttributeList) {
		String attrName = null;
		PersonalAttribute personalAttribute = null;
		ArrayList<String> attrValues = null;

		StringBuffer sb = new StringBuffer();

		// No se envia al SVDI
		personalAttributeList.remove("signedDoc");

		Iterator<String> it = personalAttributeList.keySet().iterator();

		while (it.hasNext()) {
			attrName = it.next();

			PEPSUtil.validateParameter(SPUtil.class.getCanonicalName(),
					"eIdentifier", attrName);

			personalAttribute = personalAttributeList.get(attrName);
			attrValues = (ArrayList<String>) personalAttribute.getValue();

			sb.append(attrName);
			sb.append(PEPSValues.ATTRIBUTE_TUPLE_SEP.toString());

			sb.append(personalAttribute.isRequired());

			if (!attrValues.isEmpty()) {
				sb.append(":[");
				for (String value : attrValues) {

					PEPSUtil.validateParameter(SPUtil.class.getCanonicalName(),
							PEPSParameters.ATTRIBUTE_VALUE.toString(), value);

					sb.append(value);

					if (attrValues.iterator().hasNext()) {
						sb.append(PEPSValues.ATTRIBUTE_VALUE_SEP.toString());
					}
				}
				sb.append("]");
			}
			sb.append(PEPSValues.ATTRIBUTE_SEP.toString());
		}
		return sb.toString();
	}

	public static final PersonalAttributeList createPersonalAttributeList(
			String attrList) {

		String attrName = null;
		String attrType = null;
		String[] vals = null;
		PersonalAttribute pa = null;
		ArrayList<String> values = null;

		StringTokenizer st = new StringTokenizer(attrList,
				PEPSValues.ATTRIBUTE_SEP.toString());

		PersonalAttributeList personalList = new PersonalAttributeList();

		PEPSUtil.validateParameter(SPUtil.class.getCanonicalName(),
				PEPSParameters.ATTRIBUTE_LIST.toString(), attrList);

		while (st.hasMoreTokens()) {
			pa = new PersonalAttribute();
			String[] params = st.nextToken().split(
					PEPSValues.ATTRIBUTE_TUPLE_SEP.toString());
			if (params.length >= 2 && params.length <= 3) {
				attrName = params[0];
				attrType = params[1];

				PEPSUtil.validateParameter(SPUtil.class.getCanonicalName(),
						PEPSParameters.ATTRIBUTE_NAME.toString(), attrName);

				PEPSUtil.validateParameter(SPUtil.class.getCanonicalName(),
						PEPSParameters.ATTRIBUTE_TYPE.toString(), attrType);

				pa.setName(attrName);
				pa.setIsRequired(new Boolean(attrType).booleanValue());
				if (params.length == 3) {
					String tmpAttrValue;
					if (params[2] != null && params[2].startsWith("[")
							&& params[2].endsWith("]")) {
						tmpAttrValue = params[2].substring(1,
								params[2].length() - 1);
					} else {
						tmpAttrValue = params[2];
					}
					values = new ArrayList<String>();
					vals = tmpAttrValue.split(PEPSValues.ATTRIBUTE_VALUE_SEP
							.toString());

					if (vals.length > 0) {
						for (String val : vals) {

							PEPSUtil.validateParameter(
									SPUtil.class.getCanonicalName(),
									PEPSParameters.ATTRIBUTE_VALUE.toString(),
									val);

							values.add(val);
						}
					} else {
						logger.warn("SVDI está proporcionando el atributo "
								+ PEPSParameters.ATTRIBUTE_VALUE.toString()
								+ " sin un valor");
						for (String val : vals) {

							PEPSUtil.validateParameter(
									SPUtil.class.getCanonicalName(),
									PEPSParameters.ATTRIBUTE_VALUE.toString(),
									val);

							values.add(val);
						}
					}

					pa.setValue(values);
				}
				personalList.put(attrName, pa);
			} else {
				logger.error("Error en la comprobación de la longitud de los párametros");
				throw new SecurityPEPSException(
						PEPSUtil.getConfig(PEPSErrors.INVALID_ATTRIBUTE_VALUE
								.errorCode()),
						PEPSUtil.getConfig(PEPSErrors.INVALID_ATTRIBUTE_VALUE
								.errorMessage()));
			}
		}
		return personalList;
	}

}
