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


package es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

/**
 * This class was generated by Apache CXF 2.3.0
 * Fri Apr 26 09:49:46 CEST 2013
 * Generated source version: 2.3.0
 * 
 */

public final class Validacion_ValidarCertificado_Client {

    private static final QName SERVICE_NAME = new QName("http://afirmaws/services/ValidarCertificado", "ValidacionService");

    private Validacion_ValidarCertificado_Client() {
    }

    public static void main(String args[]) throws Exception {
        URL wsdlURL = ValidacionService.WSDL_LOCATION;
        if (args.length > 0) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        ValidacionService ss = new ValidacionService(wsdlURL, SERVICE_NAME);
        Validacion port = ss.getValidarCertificado();  
        
        {
        System.out.println("Invoking validarCertificado...");
        java.lang.String _validarCertificado_parametrosIn = "";
        java.lang.String _validarCertificado__return = port.validarCertificado(_validarCertificado_parametrosIn);
        System.out.println("validarCertificado.result=" + _validarCertificado__return);


        }

        System.exit(0);
    }

}
