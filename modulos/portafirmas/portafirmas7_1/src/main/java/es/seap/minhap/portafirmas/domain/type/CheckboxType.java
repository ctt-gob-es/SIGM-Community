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

/*

 Empresa desarrolladora: GuadalTEL S.A.

 Autor: Junta de Andaluc&iacute;a

 Derechos de explotaci&oacute;n propiedad de la Junta de Andaluc&iacute;a.

 Éste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los t&eacute;rminos de la Licencia EUPL European Public License publicada 
 por el organismo IDABC de la Comisi&oacute;n Europea, en su versi&oacute;n 1.0. o posteriores.

 Éste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÍA, incluso sin las presuntas garant&iacute;as impl&iacute;citas de USABILIDAD o ADECUACIÓN A PROPÓSITO 
 CONCRETO. Para mas informaci&oacute;n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por alg&uacute;n motivo no le es posible visualizarla, puede 
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reçu une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da 
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

package es.seap.minhap.portafirmas.domain.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

public class CheckboxType implements UserType {

	// default true value is 'S', default false is 'N'
	private static final String STRINGTRUE = "S";
	private static final String STRINGFALSE = "N";

	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}

	public Class<Boolean> returnedClass() {
		return boolean.class;
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public int hashCode(Object object) throws HibernateException {
		return object.hashCode();
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

	public boolean equals(Object object1, Object object2)
			throws HibernateException {
		boolean result = false;
		if (object1 == object2) {
			result = true;
		} else if (null == object1 || null == object2) {
			result = false;
		} else {
			result = object1.equals(object2);
		}
		return result;
	}

	public Object nullSafeGet(ResultSet resultSet, String[] names,
			SessionImplementor sessionImpl, Object owner) throws HibernateException,
			SQLException {
		boolean result = false;
		String val = resultSet.getString(names[0]);
		if (null != val && val.equals(STRINGTRUE)) {
			result = true;
		}
		return result;
	}

	public void nullSafeSet(PreparedStatement statement, Object value, int index,
			SessionImplementor sessionImpl) throws HibernateException, SQLException {
		if (value == null) {
			statement.setNull(index, Types.VARCHAR);
		} else if (((Boolean) value).booleanValue()) {
			statement.setString(index, STRINGTRUE);
		} else {
			statement.setString(index, STRINGFALSE);
		}
	}

}
