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

package es.seap.minhap.portafirmas.domain.type;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import es.seap.minhap.portafirmas.utils.Constants;

public class StringClobType implements UserType {

	public int[] sqlTypes() {
		return new int[] { Types.CLOB };
	}

	public Class<String> returnedClass() {
		return String.class;
	}

	public boolean equals(Object object1, Object object2) {
		return (object1 == object2)
				|| (object1 != null && object2 != null && (object1
						.equals(object2)));
	}

	public Object deepCopy(Object value) {
		Object object = null;
		if (value != null) {
			object = new String((String) value);
		}
		return object;
	}

	public boolean isMutable() {
		return false;
	}

	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	public int hashCode(Object object) throws HibernateException {
		return object.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		Object object = null;
		if (rs.getClass().getPackage().getName().indexOf(
				Constants.BBDD_POSTGRES) > -1) {
			object = rs.getString(names[0]);
		} else {
			Clob clob = rs.getClob(names[0]);
			if (clob != null) {
				object = clob.getSubString(1, (int) clob.length());
			}
		}
		return object;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, sqlTypes()[0]);
		} else {
			if (st.getConnection().getClass().getPackage().getName()
					.indexOf(Constants.BBDD_POSTGRES) > -1) {
				st.setString(index, (String) value);
			} else {
				StringReader reader = new StringReader((String) value);
				st.setCharacterStream(index, reader, ((String) value).length());
			}
		}
	}

}
