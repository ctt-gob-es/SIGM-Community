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

package es.seap.minhap.portafirmas.business.dao.jpaisolationimplementation;
import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

public class CustomHibernateJpaDialect extends HibernateJpaDialect {

	private static final long serialVersionUID = 1L;

	@Override
	public Object beginTransaction(final EntityManager entityManager,
			final TransactionDefinition definition)
			throws PersistenceException, SQLException, TransactionException {

		Session session = (Session) entityManager.getDelegate();
		if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
			getSession(entityManager).getTransaction().setTimeout(
					definition.getTimeout());
		}

		final TransactionData data = new TransactionData();

		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Integer previousIsolationLevel = DataSourceUtils
						.prepareConnectionForTransaction(connection, definition);
				data.setPreviousIsolationLevel(previousIsolationLevel);
				data.setConnection(connection);
			}
		});

		entityManager.getTransaction().begin();

		Object springTransactionData = prepareTransaction(entityManager,
				definition.isReadOnly(), definition.getName());

		data.setSpringTransactionData(springTransactionData);

		return data;
	}

	@Override
	public void cleanupTransaction(Object transactionData) {
		super.cleanupTransaction(((TransactionData) transactionData)
				.getSpringTransactionData());
		((TransactionData) transactionData).resetIsolationLevel();
	}

	private static class TransactionData {

		private Object springTransactionData;
		private Integer previousIsolationLevel;
		private Connection connection;

		public TransactionData() {
		}

		public void resetIsolationLevel() {
			if (this.previousIsolationLevel != null) {
				DataSourceUtils.resetConnectionAfterTransaction(connection,
						previousIsolationLevel);
			}
		}

		public Object getSpringTransactionData() {
			return this.springTransactionData;
		}

		public void setSpringTransactionData(Object springTransactionData) {
			this.springTransactionData = springTransactionData;
		}

		public void setPreviousIsolationLevel(Integer previousIsolationLevel) {
			this.previousIsolationLevel = previousIsolationLevel;
		}

		public void setConnection(Connection connection) {
			this.connection = connection;
		}

	}
}