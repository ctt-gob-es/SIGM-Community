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

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author miro
 */
public class UniversalIndexKey
    implements Comparable<UniversalIndexKey>
{
    private Vector<Comparable> indexKeys;

    public UniversalIndexKey(Comparable ... comparableValues)
    {
        int size = comparableValues.length;
        if(size < 1)
            throw new IllegalArgumentException("The minimum number of parameters for constructing of UniversalIndexKey is 1.");

        indexKeys = new Vector<Comparable>(size);
        Collections.addAll(indexKeys, comparableValues);
    }

    public int compareTo(UniversalIndexKey other)
    {
        if(other == null)
        {
            throw new NullPointerException();
        }

        int thisSize = indexKeys.size();
        int otherSize = other.indexKeys.size();

//System.out.println("compareTo: indexKeys.size(): " + indexKeys.size() + ", other.indexKeys.size(): " + other.indexKeys.size());

        int size = thisSize <= otherSize ? thisSize : otherSize;
        for(int i = 0; i < size; i++)
        {
            Comparable firstComparable = indexKeys.get(i);
            Comparable secondComparable = other.indexKeys.get(i);

            if(firstComparable == null)
                return Integer.MIN_VALUE;

            if(secondComparable == null)
                return Integer.MAX_VALUE;

            try
            {
                int compareResult = firstComparable.compareTo(secondComparable);
//                System.out.println((i + 1) + ". " + firstComparable + " ? " + secondComparable + " = " + compareResult);
                if(compareResult != 0)
                    return compareResult;
            }
            catch(ClassCastException ex)
            {
                String s1 = String.valueOf(firstComparable);
                String s2 = String.valueOf(secondComparable);
                System.out.println("s1: " + s1 + ", s2: " + s2);
                return s1.compareTo(s2);
            }
        }

        if(thisSize < otherSize)
        {
            return -1;
        }

        if(thisSize > otherSize)
        {
            return 1;
        }

        return 0;
    }

    public boolean equals(Object otherObject)
    {
        if(otherObject == null || !(otherObject instanceof UniversalIndexKey))
        {
            return false;
        }

        UniversalIndexKey other = (UniversalIndexKey)otherObject;

        int thisSize = indexKeys.size();
        int otherSize = other.indexKeys.size();

//System.out.println("equals: indexKeys.size(): " + indexKeys.size() + ", other.indexKeys.size(): " + other.indexKeys.size());

        if(thisSize != otherSize)
            return false;

        for(int i = 0; i < thisSize; i++)
        {
            Comparable firstComparable = indexKeys.get(i);
            Comparable secondComparable = other.indexKeys.get(i);

            if(firstComparable == null && secondComparable == null)
                return true;

            if(firstComparable == null && secondComparable != null)
                return false;

            if(firstComparable != null && secondComparable == null)
                return false;

            boolean equalsResult = firstComparable.equals(secondComparable);
            if(!equalsResult)
                return equalsResult;
        }

        return true;
    }

    public List<Comparable> getKeys()
    {
        return indexKeys;
    }

    public Comparable getKey(int keyIndex)
    {
        return indexKeys.get(keyIndex);
    }



    public static String getMinString(int length)
    {
        return getMinString(null, length);
    }

    public static String getMinString(String source)
    {
        return getMinString(source, 0);
    }

    public static String getMinString(String source, int length)
    {
        if(length < 1 && (source == null || source.length() <= 0))
            throw new IllegalArgumentException("The lenght can not be less or equal to 0.");

        int maxLength = length;
        if(source != null && source.length() > length)
            maxLength = source.length();

        StringBuilder sb = new StringBuilder(maxLength);
        if(source != null)
        {
            sb.append(source.substring(0, source.length() - 1));
        }
        while(sb.length() < maxLength)
        {
            sb.append(Character.MIN_VALUE);
        }

        return sb.toString();
    }

    public static String getMaxString(int length)
    {
        return getMaxString(null, length);
    }

    public static String getMaxString(String source)
    {
        return getMaxString(source, 0);
    }

    public static String getMaxString(String source, int length)
    {
        if(length < 1 && (source == null || source.length() <= 0))
            throw new IllegalArgumentException("The lenght can not be less or equal to 0.");

        int maxLength = length;
        if(source != null && source.length() > length)
            maxLength = source.length();

        StringBuilder sb = new StringBuilder(maxLength);
        if(source != null)
        {
            sb.append(source.substring(0, source.length() - 1));
        }
        while(sb.length() < maxLength)
        {
            sb.append(Character.MAX_VALUE);
        }

        return sb.toString();
    }

    
    public static void main(String[] args)
    {
        System.out.println("getMaxString(\"123\", 3): " + getMaxString("123", 3));
        System.out.println("getMaxString(\"123\", 2): " + getMaxString("123", 2));
        System.out.println("getMaxString(\"123\", 5): " + getMaxString("123", 5));
        System.out.println("getMaxString(NULL, 5): " + getMaxString(null, 5));
        System.out.println("getMaxString(7): " + getMaxString(7));
        System.out.println("getMaxString(\"1234\"): " + getMaxString("1234"));
    }
}
