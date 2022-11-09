/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */


/*
 * 
 */

package es.seap.minhap.portafirmas.dsic.csv.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.3.0
 * Thu Oct 20 12:12:45 CEST 2011
 * Generated source version: 2.3.0
 * 
 */


@WebServiceClient(name = "WSDocCSVImplService", 
                  wsdlLocation = "file:/home/rus/desarrollo/servicio-csv/ObtenerDocCSV.wsdl",
                  targetNamespace = "http://ws.csv.dsic.mpt.es/") 
public class WSDocCSVImplService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://ws.csv.dsic.mpt.es/", "WSDocCSVImplService");
    public final static QName WSDocCSVImplPort = new QName("http://ws.csv.dsic.mpt.es/", "WSDocCSVImplPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/rus/desarrollo/servicio-csv/ObtenerDocCSV.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/home/rus/desarrollo/servicio-csv/ObtenerDocCSV.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public WSDocCSVImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public WSDocCSVImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public WSDocCSVImplService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     * 
     * @return
     *     returns WSDocCSV
     */
    @WebEndpoint(name = "WSDocCSVImplPort")
    public WSDocCSV getWSDocCSVImplPort() {
        return super.getPort(WSDocCSVImplPort, WSDocCSV.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WSDocCSV
     */
    @WebEndpoint(name = "WSDocCSVImplPort")
    public WSDocCSV getWSDocCSVImplPort(WebServiceFeature... features) {
        return super.getPort(WSDocCSVImplPort, WSDocCSV.class, features);
    }

}
