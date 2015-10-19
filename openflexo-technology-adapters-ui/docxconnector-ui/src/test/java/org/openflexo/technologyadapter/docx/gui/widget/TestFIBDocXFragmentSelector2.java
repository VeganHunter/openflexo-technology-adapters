/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.gui.widget;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.doc.FlexoDocFragment.FragmentConsistencyException;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the structural and behavioural features of FIBDocXFragmentSelector
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestFIBDocXFragmentSelector2 extends AbstractTestDocX {

	private static FIBDocXFragmentSelector selector;

	/*private static DocXDocument getDocument(String documentName) {
		String documentURI = resourceCenter.getDefaultBaseURI() + "TestResourceCenter" + File.separator + documentName;
		System.out.println("Searching " + documentURI);
	
		FlexoResource<DocXDocument> documentResource = serviceManager.getResourceManager().getResource(documentURI, null,
				DocXDocument.class);
		assertNotNull(documentResource);
	
		try {
			documentResource.loadResourceData(null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		DocXDocument document = documentResource.getLoadedResourceData();
		assertNotNull(document);
		assertNotNull(document.getWordprocessingMLPackage());
	
		return document;
	}*/

	@Test
	@TestOrder(2)
	public void test2InstanciateWidget() throws FragmentConsistencyException {

		DocXDocument structuredDocument = getDocument("ExampleReport.docx");
		assertNotNull(structuredDocument);

		DocXParagraph startParagraph = (DocXParagraph) structuredDocument.getElements().get(22);
		DocXParagraph endParagraph = (DocXParagraph) structuredDocument.getElements().get(32);

		System.out.println("Document: " + structuredDocument);
		System.out.println("Contents:" + structuredDocument.debugContents());
		System.out.println("Contents:" + structuredDocument.debugStructuredContents());

		assertNotNull(startParagraph);
		assertNotNull(endParagraph);
		assertNotNull(startParagraph.getFlexoDocument());
		assertNotNull(endParagraph.getFlexoDocument());

		DocXFragment fragment = (DocXFragment) structuredDocument.getFactory().makeFragment(startParagraph, endParagraph);

		selector = new FIBDocXFragmentSelector(fragment);
		selector.setServiceManager(serviceManager);
		selector.setDocument(structuredDocument);
		selector.getCustomPanel();

		assertNotNull(selector.getSelectorPanel().getController());

		gcDelegate.addTab("FIBDocXFragmentSelector", selector.getSelectorPanel().getController());
	}

}
