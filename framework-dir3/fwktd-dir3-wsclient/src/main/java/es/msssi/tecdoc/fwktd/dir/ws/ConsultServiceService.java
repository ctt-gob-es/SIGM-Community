
package es.msssi.tecdoc.fwktd.dir.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "ConsultServiceService", targetNamespace = "http://ws.dir.fwktd.tecdoc.msssi.es/", wsdlLocation = "http://127.0.0.1:7001/fwktd-dir3-ws/ConsultServiceService?WSDL")
public class ConsultServiceService
    extends Service
{

    private final static URL CONSULTSERVICESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(es.msssi.tecdoc.fwktd.dir.ws.ConsultServiceService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = es.msssi.tecdoc.fwktd.dir.ws.ConsultServiceService.class.getResource(".");
            url = new URL(baseUrl, "http://127.0.0.1:7001/fwktd-dir3-ws/ConsultServiceService?WSDL");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://127.0.0.1:7001/fwktd-dir3-ws/ConsultServiceService?WSDL', retrying as a local file");
            logger.warning(e.getMessage());
        }
        CONSULTSERVICESERVICE_WSDL_LOCATION = url;
    }

    public ConsultServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ConsultServiceService() {
        super(CONSULTSERVICESERVICE_WSDL_LOCATION, new QName("http://ws.dir.fwktd.tecdoc.msssi.es/", "ConsultServiceService"));
    }

    /**
     * 
     * @return
     *     returns ConsultService
     */
    @WebEndpoint(name = "ConsultServicePort")
    public ConsultService getConsultServicePort() {
        return super.getPort(new QName("http://ws.dir.fwktd.tecdoc.msssi.es/", "ConsultServicePort"), ConsultService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ConsultService
     */
    @WebEndpoint(name = "ConsultServicePort")
    public ConsultService getConsultServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.dir.fwktd.tecdoc.msssi.es/", "ConsultServicePort"), ConsultService.class, features);
    }

}