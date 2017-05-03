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
package net.java.xades.util;

import java.util.Collection;


/**
 *
 * @author miro
 */
public final class OccursRequirement
    extends ObjectId
{
    public static final OccursRequirement EXACTLY_ONE =
            new OccursRequirement(1, 1);
    public static final OccursRequirement ZERO_OR_ONE =
            new OccursRequirement(0, 1);
    public static final OccursRequirement ONE_OR_MORE =
            new OccursRequirement(1);
    public static final OccursRequirement ZERO_OR_MORE =
            new OccursRequirement(0);
    
    public OccursRequirement(int minOccurs)
    {
        this(minOccurs, Integer.MAX_VALUE);
    }

    public OccursRequirement(int minOccurs, int maxOccurs)
    {
        super(new int[] {minOccurs, maxOccurs});
    }

    public final int getMinOccurs()
    {
        return components[0];
    }

    public final int getMaxOccurs()
    {
        return components[1];
    }

    public final boolean isValid(Object object)
    {
        if(object != null)
        {
            if(object instanceof Number)
                return isValid(((Number)object).intValue());

            if(object instanceof Collection)
                return isValid(((Collection)object).size());

            if(object instanceof Object[])
                return isValid(((Object[])object).length);

            return isValid(1);
        }

        return components[0] == 0;
    }

    public final boolean isValid(int count)
    {
        return count >= components[0] && count <= components[1];
    }
}
