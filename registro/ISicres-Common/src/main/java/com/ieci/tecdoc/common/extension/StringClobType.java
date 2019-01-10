package com.ieci.tecdoc.common.extension;

import java.beans.Encoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;

import es.dipucr.api.helper.UTFHelper;

public class StringClobType implements UserType
{
    public int[] sqlTypes()
    {
        return new int[] { Types.CLOB };
    }

    public Class returnedClass()
    {
        return String.class;
    }

    public boolean equals(Object x, Object y)
    {
        return (x == y)
            || (x != null
                && y != null
                && (x.equals(y)));
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
        throws HibernateException, SQLException
    {
        String line;
		String str = "";
		Reader reader = rs.getCharacterStream(names[0]);
		if (reader == null){
			return null;
		}
		BufferedReader b = new BufferedReader(reader);
		try {
			while ((line = b.readLine()) != null) {
				if(StringUtils.isNotEmpty(str)){
					str += "\n";
				}
				str += line;	
			}
		} catch (IOException e) {
			throw new SQLException(e.toString());
		}
		return str;

		/*
		 * Clob clob = rs.getClob(names[0]); String text = ""; if (clob !=
		 * null){ text = clob.getSubString(1, (int) clob.length()); }
		 * 
		 * return text;
		 */
	}

    public void nullSafeSet(PreparedStatement st, Object value, int index)
        throws HibernateException, SQLException
    {
    	String valueLatin9 = UTFHelper.parseUTF8ToISO885916((String)value);
    	
        StringReader r = new StringReader( valueLatin9 );
        st.setCharacterStream( index, r, valueLatin9.length() );
    }
    
    public void nullSafeSet1(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		StringReader r = new StringReader((String) value);
		st.setString(index, (String) value);
	}

    public Object deepCopy(Object value)
    {
        if (value == null) return null;
        return new String((String) value);
    }

    public boolean isMutable()
    {
        return false;
    }
}

