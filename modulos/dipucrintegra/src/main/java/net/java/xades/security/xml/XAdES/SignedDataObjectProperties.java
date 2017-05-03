// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/*
 * This file is part of the jXAdES library. 
 * jXAdES is an open implementation for the Java platform of the XAdES standard for advanced XML digital signature. 
 * This library can be consulted and downloaded from http://universitatjaumei.jira.com/browse/JXADES.
 * 
 */
package net.java.xades.security.xml.XAdES;

import java.util.ArrayList;

public class SignedDataObjectProperties extends XAdESStructure
{
    public SignedDataObjectProperties(SignedProperties signedProperties, String xadesPrefix,
            String xadesNamespace, String xmlSignaturePrefix)
    {
        super(signedProperties, "SignedDataObjectProperties", xadesPrefix, xadesNamespace,
                xmlSignaturePrefix);
    }

    public void setDataObjectFormat(ArrayList<DataObjectFormat> dataObjectFormat)
    {
        for (DataObjectFormat dof : dataObjectFormat)
        {
            new DataObjectFormatDetails(this, dof, xadesPrefix, xadesNamespace, xmlSignaturePrefix);
        }
    }

    public void setCommitmentTypeIndication(CommitmentTypeIndication commitmentTypeIndication)
    {
        new CommitmentTypeIndicationDetails(this, commitmentTypeIndication, xadesPrefix,
                xadesNamespace, xmlSignaturePrefix);
    }

    public void setAllDataObjectsTimeStamp(
            ArrayList<AllDataObjectsTimeStamp> allDataObjectsTimeStamp, String tsaURL)
    {
        for (AllDataObjectsTimeStamp adots : allDataObjectsTimeStamp)
        {
            new AllDataObjectsTimeStampDetails(this, adots, xadesPrefix, xadesNamespace,
                    xmlSignaturePrefix, tsaURL);
        }
    }

    public void setIndividualDataObjectsTimeStamp(
            ArrayList<IndividualDataObjectsTimeStamp> individualDataObjectsTimeStamp, String tsaURL)
    {
        for (IndividualDataObjectsTimeStamp idots : individualDataObjectsTimeStamp)
        {
            new IndividualDataObjectsTimeStampDetails(this, idots, xadesPrefix, xadesNamespace,
                    xmlSignaturePrefix, tsaURL);
        }
    }
}