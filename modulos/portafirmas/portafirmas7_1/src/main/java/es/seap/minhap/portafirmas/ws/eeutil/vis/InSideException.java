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


package es.seap.minhap.portafirmas.ws.eeutil.vis;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.1
 * 2017-10-06T08:13:16.536+02:00
 * Generated source version: 3.0.1
 */

@WebFault(name = "ErrorTest", targetNamespace = "http://service.ws.inside.dsic.mpt.es/")
public class InSideException extends Exception {
    
    private es.seap.minhap.portafirmas.ws.eeutil.vis.EstadoInfo errorTest;

    public InSideException() {
        super();
    }
    
    public InSideException(String message) {
        super(message);
    }
    
    public InSideException(String message, Throwable cause) {
        super(message, cause);
    }

    public InSideException(String message, es.seap.minhap.portafirmas.ws.eeutil.vis.EstadoInfo errorTest) {
        super(message);
        this.errorTest = errorTest;
    }

    public InSideException(String message, es.seap.minhap.portafirmas.ws.eeutil.vis.EstadoInfo errorTest, Throwable cause) {
        super(message, cause);
        this.errorTest = errorTest;
    }

    public es.seap.minhap.portafirmas.ws.eeutil.vis.EstadoInfo getFaultInfo() {
        return this.errorTest;
    }
}
