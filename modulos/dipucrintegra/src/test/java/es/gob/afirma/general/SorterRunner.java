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
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-,2011 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.general.SorterRunner.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>01/04/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 01/04/2011.
 */
package es.gob.afirma.general;

import java.util.Comparator;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Sorter;
import org.junit.runners.model.InitializationError;

/**
 * <p>Class allows sorter execution junit test in an alphabetical order.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 01/04/2011.
 */
public class SorterRunner extends org.junit.runners.BlockJUnit4ClassRunner {

	/**
	 * Constructor method for the class SorterRunner.java.
	 * @param klass
	 * @throws InitializationError
	 */
	public SorterRunner(Class<?> klass) throws InitializationError {
		super(klass);
		sort(new Sorter(comparator));
	}

	/**
	 * Attribute that represents .
	 */
	private static final Comparator<Description> comparator = new Comparator<Description>() {

		/**
		 * 
		 * @param o1
		 * @param o2
		 * @return
		 */
		public int compare(Description o1, Description o2) {
			return o1.getDisplayName().compareTo(o2.getDisplayName());
		}
	};

}
