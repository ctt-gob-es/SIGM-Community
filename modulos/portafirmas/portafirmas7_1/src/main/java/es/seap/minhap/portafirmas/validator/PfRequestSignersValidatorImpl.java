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

//package es.seap.minhap.portafirmas.validator;
//
//import java.io.Serializable;
//
//import org.hibernate.mapping.Property;
//import org.hibernate.validator.PropertyConstraint;
//import org.hibernate.validator.Validator;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Service;
//
//import es.seap.minhap.portafirmas.business.RequestBO;
//
//@Service
//public class PfRequestSignersValidatorImpl implements
//		Validator<PfRequestSignersValidator>, PropertyConstraint, ApplicationContextAware, Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	// Inyecto el contexto de la aplicación
//	private ApplicationContext applicationContext;
//
//	public void setApplicationContext(ApplicationContext applicationContext)
//			throws BeansException {
//		this.applicationContext = applicationContext;
//	}
//
//	/**
//	 * Procedimiento vac&iacute;o, no hace nada
//	 */
//	public void initialize(PfRequestSignersValidator parameters) {
//
//	}
//	/**
//	 * Chequea que los firmantes del objeto que pasamos como par&aacute;metro, sean usuarios v&aacute;lidos.
//	 * @param value objeto que contiene los usuarios firmantes
//	 * @return devuelve true si todos los firmantes del objeto exiten en bbdd, false en caso contrario
//	 */
//	public boolean isValid(Object value) {
//		RequestBO requestBO = applicationContext.getBean(RequestBO.class);
//		return requestBO.checkRequest((String) value);
//	}
//	/**
//	 * Procedimiento vac&iacute;o, no hace nada
//	 */
//	public void apply(Property arg0) {
//
//	}
//	
//}
