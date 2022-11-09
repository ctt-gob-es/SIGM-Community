package es.clave.sp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * Util to retrieve a property value. Contains the properties loaded by the placeholderConfig
 * bean on spring initialization
 */
public class PropertiesUtil extends PropertyPlaceholderConfigurer {
	/**
	 * Logger object.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class.getName());
	private static Map<String, String> propertiesMap;
	private List<Resource> locations;

	@Override
	public void setLocations(Resource... locations) {
		super.setLocations(locations);
		this.locations=new ArrayList<Resource>();
		for(Resource location:locations){
			this.locations.add(location);
		}
	}

	public List<Resource> getPropertyLocations(){
		return locations;
	}

	private static void initProps(Properties props){
		LOG.info("Loading properties");
		propertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			propertiesMap.put(keyStr, props.getProperty(keyStr));
		}
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory,
			Properties props) throws BeansException {
		super.processProperties(beanFactory, props);
		PropertiesUtil.initProps(props);

	}

	public static String getProperty(String name) {
		return (String) propertiesMap.get(name);
	}

	public static boolean hasPropertyMap(){
		return propertiesMap != null;
	}

}

