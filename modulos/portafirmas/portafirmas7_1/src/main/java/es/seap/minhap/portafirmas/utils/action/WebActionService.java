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

package es.seap.minhap.portafirmas.utils.action;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.Constants;

@Service
@SuppressWarnings("deprecation")
public class WebActionService implements Job {

	private static final Logger log = Logger.getLogger(WebActionService.class);

	public WebActionService() {
	}

	@SuppressWarnings("resource")
	private int invokeURL(String url, String proxyHost, String proxyPort,
			String proxyUser, String proxyPasswd) throws Exception {
		log.info("invokeURL init");
		log.debug("url: " + url);
		log.debug("proxy: " + proxyHost);
		int statusCode = 0;
		// create a singular HttpClient object
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpHost proxy = null;
		HttpEntity entity = null;
		try {
			if (proxyHost != null && proxyPort != null) {
				if (proxyUser != null && proxyPasswd != null) {
					httpClient.getCredentialsProvider().setCredentials(
							new AuthScope(proxyHost, Integer
									.parseInt(proxyPort)),
							new UsernamePasswordCredentials(proxyUser,
									proxyPasswd));
				}
				proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
			}

			HttpGet httpget = new HttpGet(url);
			// create a method object
			log.debug("executing request: " + httpget.getRequestLine());
			log.debug("via proxy: " + proxy);

			HttpResponse response = httpClient.execute(httpget);
			entity = response.getEntity();

			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == 200) {
				log.debug("URL " + url + " invocation successful");
			} else {
				log.error("invokeURL error: " + statusCode);
			}
		} catch (MalformedURLException murle) {
			log.error("invokeURL error: " + url + " is not a valid URL", murle);
			throw new MalformedURLException("invokeURL error: " + url
					+ " is not a valid URL");
		} catch (IOException ioe) {
			log.error("invokeURL error: Unable to connect to '" + url, ioe);
			throw new IOException("invokeURL error: Unable to connect to '"
					+ url);
		} catch (Exception e) {
			log.error("invokeURL error: ", e);
			throw new Exception("invokeURL error");
		} finally {
			// clean up the connection resources
			if (entity != null) {
				entity.consumeContent();
				log.info("invokeURL end");
			}
		}
		return statusCode;
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		log.info("execute init");

		String action = (String) context.getJobDetail().getJobDataMap().get(
				Constants.JOB_ACTION);
		String proxyHost = (String) context.getJobDetail().getJobDataMap().get(
				Constants.PROXY_SERVER);
		String proxyPort = (String) context.getJobDetail().getJobDataMap().get(
				Constants.PROXY_PORT);
		String proxyUser = (String) context.getJobDetail().getJobDataMap().get(
				Constants.PROXY_USER);
		String proxyPasswd = (String) context.getJobDetail().getJobDataMap()
				.get(Constants.PROXY_PASSWORD);

		try {
			this
					.invokeURL(action, proxyHost, proxyPort, proxyUser,
							proxyPasswd);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			context.getJobDetail().getJobDataMap().put("errors", errorMessage);
			throw new JobExecutionException(e);
		}

		log.info("execute end");

	}
}
