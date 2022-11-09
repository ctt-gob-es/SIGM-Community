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

package es.seap.minhap.portafirmas.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class IEEmulate
 */
public class IEEmulate implements Filter {

//	private Logger log = Logger.getLogger(IEEmulate.class);

	public void destroy() {
		
	}

	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain chain) throws IOException, ServletException {
		((HttpServletResponse) response).setHeader("X-UA-Compatible", "IE=EmulateIE8");
	    chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		
	}

}
