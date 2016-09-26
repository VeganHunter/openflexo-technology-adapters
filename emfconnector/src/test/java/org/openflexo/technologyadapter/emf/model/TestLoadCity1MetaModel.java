/**
 * 
 * Copyright (c) 2015, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.emf.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.EMFClassClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test City1 Meta-Model
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestLoadCity1MetaModel extends OpenflexoTestCase {
	protected static final Logger logger = Logger.getLogger(TestLoadCity1MetaModel.class.getPackage().getName());

	private static EMFMetaModelResource city1MMRes;
	private static EMFModelResource city1Res;

	private static EMFModel emfModel;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		log("test0InstantiateResourceCenter()");

		instanciateTestServiceManager(EMFTechnologyAdapter.class);

		/*for (FlexoResource<?> r : serviceManager.getResourceManager().getRegisteredResources()) {
			System.out.println(" > " + r.getURI());
		}*/

	}

	@Test
	@TestOrder(2)
	public void lookupCity1MetaModel() {

		city1MMRes = (EMFMetaModelResource) serviceManager.getResourceManager()
				.getResource("http://www.thalesgroup.com/openflexo/emf/model/city1", EMFMetaModel.class);

		assertNotNull(city1MMRes);

	}

	@Test
	@TestOrder(3)
	public void lookupCity1Model() {

		city1Res = (EMFModelResource) serviceManager.getResourceManager()
				.getResource("http://openflexo.org/test/TestResourceCenter/TestResourceCenter/EMF/Model/city1/my.city1", EMFModel.class);

		assertNotNull(city1Res);
		assertSame(city1MMRes, city1Res.getMetaModelResource());

	}

	@Test
	@TestOrder(4)
	public void loadCity1Model() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		assertFalse(city1Res.isLoaded());
		assertFalse(city1MMRes.isLoaded());

		emfModel = city1Res.getResourceData(null);
		assertNotNull(emfModel);

		assertTrue(city1Res.isLoaded());
		assertTrue(city1MMRes.isLoaded());

	}

	@Test
	@TestOrder(5)
	public void performSomeTests() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		Resource resource = emfModel.getEMFResource();

		TreeIterator<EObject> iterator = resource.getAllContents();
		while (iterator.hasNext()) {
			EObject eObject = iterator.next();
			EMFClassClass emfClassClass = emfModel.getMetaModel().getConverter().getClasses().get(eObject.eClass());
			assertNotNull(emfClassClass);

			/*if (emfObjectIndividualType == null) {
				System.out.println("Zut alors je cherche " + eObject.eClass());
				System.out.println("emfModel=" + emfModel + " uri=" + emfModel.getURI());
				for (EClass c : emfModel.getMetaModel().getConverter().getClasses().keySet()) {
					System.out.println("> for " + c + " " + emfModel.getMetaModel().getConverter().getClasses().get(c));
				}
			
				System.out.println("classes=" + emfModel.getMetaModel().getConverter().getClasses());
				System.exit(-1);
			}*/

			/*if (emfObjectIndividualType.equals(flexoOntologyClass)
					|| ((EMFClassClass) flexoOntologyClass).isSuperClassOf(emfObjectIndividualType)) {
				selectedEMFIndividuals.add(eObject);
			}*/
		}

	}

}
