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

package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema.DataInfoType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema.SignedDataInfo;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema.SignedDataRefType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ResponseBaseType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.Result;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.profiles.verificationreport.schema.DetailedReportType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.profiles.verificationreport.schema.TimeStampValidityType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.profiles.verificationreport.schema.TstContentType;



public class DSSUtil {

	protected static final Log logger = LogFactory.getLog(DSSUtil.class);
	

	/**
	 * Devuelve la lista de objetos de una clase dada que se encuentre en otra lista de objetos dada como parámetro
	 * @param clase Clase de los objetos que se desean buscar
	 * @param lista Lista de objetos sobre la que se quiere buscar.
	 * @return
	 */
	public static List<Object> getListObjectsByClass (Class<?> clase, List<Object> lista) {		
		List<Object> founds = new ArrayList<Object> ();
		for (Object obj : lista) {
			if (obj.getClass().equals(clase)) {
				founds.add(obj);
			}
		}
		return founds;
	}
	
	/**
	 * Devuelve un objeto de tipo JAXBElement<?> cuyo nombre se corresponda con el QName dado como parámetro
	 * @param qname QName del objeto que se quiere buscar
	 * @param lista Lista de objetos sobre la que se quiere buscar
	 * @return
	 */
	public static JAXBElement<?> getJAXBElementByQName (QName qname, List<Object> lista) {
		int i=0;
		boolean found = false;
		JAXBElement<?> ret = null;
		while (i < lista.size() && !found) {
			if (lista.get(i) instanceof JAXBElement<?>) {
				ret = (JAXBElement<?>) lista.get(i);
				if (qname.equals(ret.getName())) {
					found = true;
				}
			}			
			i++;
		}
		
		return ret;
		
	}
	
	@SuppressWarnings("unchecked")
	public static <J extends JAXBElement<Q>, Q> Q getObjectByClass( Class<Q> clase, List<Object> objetos){
		
		Q ret = null;
		boolean found = false;
		int i=0;
		
		while (i < objetos.size() && !found) {
			if (objetos.get(i) instanceof JAXBElement<?>) {
				JAXBElement<?> jbel = (JAXBElement<?>) objetos.get(i);
				if (jbel.getValue().getClass() == clase) {
					found = true;
					ret = (Q) jbel.getValue();
				}
			} else if (objetos.get(i).getClass() == clase) {
				found = true;
				ret = (Q) objetos.get(i);
			}
			i++;
		}
		
		return ret;
    } 
	

	
	/**
	 * Obtiene el tipo de firma
	 * @param verifyResponse
	 * @return
	 */
	public static String getSignatureType (ResponseBaseType verifyResponse) {

		List<Object> optionalOutputs = verifyResponse.getOptionalOutputs().getAny();
		
		JAXBElement<?> jaxbElement = getJAXBElementByQName(new QName("urn:oasis:names:tc:dss:1.0:core:schema", "SignatureType"), optionalOutputs);
		
		return jaxbElement.getValue().toString();		
	}
	
	/**
	 * Devuelve el objeto de tipo TstContentType a partir del elemento DetailedReport
	 * @param detailedReport
	 * @return null si no lo encuentra.
	 */
	public static TstContentType getTimeStampContent (DetailedReportType detailedReport) {
		
		TstContentType tstContent = null;
		
		if (detailedReport != null) {			
		
			if (detailedReport.getProperties() != null) {
			
				if (detailedReport.getProperties().getUnsignedProperties() != null) {
				
					if (detailedReport.getProperties().getUnsignedProperties().getUnsignedSignatureProperties() != null) {
					
						if (detailedReport.getProperties().getUnsignedProperties().getUnsignedSignatureProperties().getCounterSignatureOrSignatureTimeStampOrCompleteCertificateRefs() != null) {
						
							List<Object> objs = detailedReport.getProperties().getUnsignedProperties().getUnsignedSignatureProperties().getCounterSignatureOrSignatureTimeStampOrCompleteCertificateRefs();
						
							TimeStampValidityType tsvt = (TimeStampValidityType) DSSUtil.getObjectByClass(TimeStampValidityType.class, objs);
						
							if (tsvt != null) {
								tstContent = tsvt.getTimeStampContent();
							}
						}
					}
					
				}
			}
		}
		
		return tstContent;
		
	}
	
	
	/**
	 * Obtiene una lista de SignedDataRefType de dentro de un SignedDataInfo
	 * @param signedDataInfo objeto donde se quieren buscar SignedDataRefType
	 * @return
	 */
	public static List<SignedDataRefType> getSignedDataRefListFromSignedDataInfo (SignedDataInfo signedDataInfo) {
		
		List<SignedDataRefType> signedDataRefList = new ArrayList<SignedDataRefType> ();
		
		if (signedDataInfo != null && signedDataInfo.getDataInfo() != null) {
			
			for (DataInfoType dataInfo : signedDataInfo.getDataInfo()) {
				
				if (dataInfo.getSignedDataRefs() != null && dataInfo.getSignedDataRefs().getSignedDataRef() != null) {
					
					for (SignedDataRefType signedDataRef : dataInfo.getSignedDataRefs().getSignedDataRef()) {
						
						signedDataRefList.add(signedDataRef);
						
					}
					
				}
				
			}
		}
		
		return signedDataRefList;
	}
	
	public static SignedDataRefType getSignedDataRefConEncodingOrFirst (SignedDataInfo signedDataInfo) {
		
		List<SignedDataRefType> signedDataRefList = DSSUtil.getSignedDataRefListFromSignedDataInfo(signedDataInfo);
		int i = 0;
		SignedDataRefType signedDataRefConEncodingOrFirst = null;
		
		// Nos quedamos con el que tenga un elemento "Encoding" dentro
		while (i < signedDataRefList.size() && signedDataRefConEncodingOrFirst == null) {				
			if (signedDataRefList.get(i).getEncoding() != null) {
				signedDataRefConEncodingOrFirst = signedDataRefList.get(i);					
			}
			i++;
		}
		
		
		// Si no lo encontramos, nos quedamos con el primero de la lista
		if (signedDataRefConEncodingOrFirst == null && signedDataRefList.size() > 0) {
			logger.debug("No se ha encontrado elemento signedDataRef con un elemento Encoding. Se selecciona el primero de la lista");
			signedDataRefConEncodingOrFirst = signedDataRefList.get(0);
		}
		return signedDataRefConEncodingOrFirst;
	}
	
	public static String getInfoResult (Result result) {
		StringBuffer sb = new StringBuffer("");
		sb.append(" ResultMajor: " + result.getResultMajor());
		sb.append(" ResultMinor: " + result.getResultMinor());
		sb.append(" Descripcion: " + result.getResultMessage().getValue());
		return sb.toString();
	}
	
	
	
	
	/**
	 * Devuelve una expresión xpath válida, a partir de una devuelta por el servicio de Afirma.
	 * @param xpathExpression
	 * @return
	 */
	public static String expresionXpathValida (String xpathExpression) {
		return Util.meteComillasXPath(Util.meteAsteriscoXPath(xpathExpression));
	}

}
