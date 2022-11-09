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

package es.seap.minhap.portafirmas.servlet;


/*import org.apache.axis.transport.http.AxisServlet;*/

/**
 * Servlet que expone los servicios web de integraci&oacute;n con aplicaciones implementados con Axis.
 * El servicio expuesto es PfServicioWS
 * @author hugo
 *
 */
public class PfirmaAxisServlet {
/*extends AxisServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		new ContextualHttpServletRequest(request) {
			@Override
			public void process() throws Exception {
				doGetWork(request, response);
			}
		}.run();

	}

	@Override
	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		new ContextualHttpServletRequest(request) {
			@Override
			public void process() throws Exception {
				doPostWork(request, response);
			}
		}.run();
	}

	private void doGetWork(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		super.doGet(request, response);
	}

	private void doPostWork(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		super.doPost(req, res);
	}
*/
}
