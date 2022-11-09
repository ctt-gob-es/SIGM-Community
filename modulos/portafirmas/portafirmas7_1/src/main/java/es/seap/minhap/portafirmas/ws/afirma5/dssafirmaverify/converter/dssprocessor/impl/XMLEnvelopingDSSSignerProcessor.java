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

package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.impl;

import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.AnyType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ResponseBaseType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ReturnUpdatedSignature;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.VerifyRequest;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.DSSSignerProcessor;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.constantes.DSSTiposFirmaConstantes;


/**
 * Procesa las peticiones a los servicios DSS de afirma para firmas XMLDSig Enveloping
 * @author rus
 *
 */
public class XMLEnvelopingDSSSignerProcessor extends DSSSignerProcessor{


	@Override
	public void fillSignatureObjectAndInputDocuments(VerifyRequest vf,
			byte[] sign) {
		
	
	
		// TODO Auto-generated method stub
		
	}



	@Override
	public void fillReturnUpdatedSignature(AnyType at, String upgradeFormat) {

		ReturnUpdatedSignature returnUpdatedSignature = of_oasis_core.createReturnUpdatedSignature();

		// Se genera el nodo en base a la ampliaci�n solicitada
		if (Afirma5Constantes.UPGRADE_TIMESTAMP.equals(upgradeFormat)) {
			returnUpdatedSignature.setType(DSSTiposFirmaConstantes.DSS_SIGNATURE_MODE_T);
		} // Aqu� se ir�n a�adiendo las diferentes posibilidades de ampliaci�n de firmas 

		at.getAny().add(returnUpdatedSignature);
	}


	@Override
	public byte[] getUpgradedSignature(ResponseBaseType verifyResponse) {
		// TODO Auto-generated method stub
		return null;
	}


}
