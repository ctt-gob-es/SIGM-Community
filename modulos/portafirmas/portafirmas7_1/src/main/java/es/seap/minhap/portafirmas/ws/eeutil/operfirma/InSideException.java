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


package es.seap.minhap.portafirmas.ws.eeutil.operfirma;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.3.0
 * Tue Feb 17 12:30:29 CET 2015
 * Generated source version: 2.3.0
 * 
 */

@WebFault(name = "ErrorTest", targetNamespace = "http://service.ws.inside.dsic.mpt.es/")
public class InSideException extends Exception {
    public static final long serialVersionUID = 20150217123029L;
    
    private es.seap.minhap.portafirmas.ws.eeutil.operfirma.EstadoInfo errorTest;

    public InSideException() {
        super();
    }
    
    public InSideException(String message) {
        super(message);
    }
    
    public InSideException(String message, Throwable cause) {
        super(message, cause);
    }

    public InSideException(String message, es.seap.minhap.portafirmas.ws.eeutil.operfirma.EstadoInfo errorTest) {
        super(message);
        this.errorTest = errorTest;
    }

    public InSideException(String message, es.seap.minhap.portafirmas.ws.eeutil.operfirma.EstadoInfo errorTest, Throwable cause) {
        super(message, cause);
        this.errorTest = errorTest;
    }

    public es.seap.minhap.portafirmas.ws.eeutil.operfirma.EstadoInfo getFaultInfo() {
        return this.errorTest;
    }
}
