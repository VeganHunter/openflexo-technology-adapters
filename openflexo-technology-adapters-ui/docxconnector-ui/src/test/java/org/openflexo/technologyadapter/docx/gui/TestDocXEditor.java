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

package org.openflexo.technologyadapter.docx.gui;

import static org.junit.Assert.assertNotNull;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.JPanel;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.components.doc.deprecated.EditorPanel;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.gui.widget.FIBDocXDocumentBrowser;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the structural and behavioural features of FIBOWLPropertySelector
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDocXEditor extends AbstractTestDocX {

	// private static SwingGraphicalContextDelegate gcDelegate;

	private static DocXDocument simpleDocument;
	private static DocXDocument structuredDocument;
	private static DocXDocument documentWithTable;
	private static DocXDocument documentWithImage;
	private static DocXDocument exampleReport;

	@BeforeClass
	public static void setupClass() {
		instanciateTestServiceManager(DocXTechnologyAdapter.class);
		initGUI();
	}

	/*
	 * private DocXDocument getDocument(String documentName) { String
	 * documentURI = resourceCenter.getDefaultBaseURI() + "TestResourceCenter/"
	 * + documentName;
	 * 
	 * FlexoResource<DocXDocument> documentResource =
	 * serviceManager.getResourceManager().getResource(documentURI, null,
	 * DocXDocument.class);
	 * 
	 * assertNotNull(documentResource);
	 * 
	 * try { documentResource.loadResourceData(null); } catch
	 * (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (ResourceLoadingCancelledException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (FlexoException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * DocXDocument document = documentResource.getLoadedResourceData();
	 * assertNotNull(document);
	 * assertNotNull(document.getWordprocessingMLPackage());
	 * 
	 * return document; }
	 */

	@Test
	@TestOrder(1)
	public void testInitRetrieveDocuments() {

		DocXTechnologyAdapter docXTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);
		assertNotNull(docXTA);

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://openflexo.org/docx-test");
		assertNotNull(resourceCenter);
		DocXDocumentRepository docXRepository = docXTA.getDocXDocumentRepository(resourceCenter);
		assertNotNull(docXRepository);

	}

	@Test
	@TestOrder(2)
	public void testOpenSimpleDocumentEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		simpleDocument = getDocument("SimpleDocument.docx");
		assertNotNull(simpleDocument);
		openDocXEditor(simpleDocument.getResource());
	}

	@Test
	@TestOrder(3)
	public void testOpenStructuredDocumentEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		structuredDocument = getDocument("StructuredDocument.docx");
		assertNotNull(structuredDocument);
		openDocXEditor(structuredDocument.getResource());
	}

	@Test
	@TestOrder(4)
	public void testOpenDocumentWithTableEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		documentWithTable = getDocument("DocumentWithTable.docx");
		assertNotNull(documentWithTable);
		openDocXEditor(documentWithTable.getResource());
	}

	@Test
	@TestOrder(5)
	public void testOpenDocumentWithImageEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		documentWithImage = getDocument("DocumentWithImage.docx");
		assertNotNull(documentWithImage);
		openDocXEditor(documentWithImage.getResource());
	}

	@Test
	@TestOrder(6)
	public void testOpenExampleReportEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		exampleReport = getDocument("ExampleReport.docx");
		assertNotNull(exampleReport);
		openDocXEditor(exampleReport.getResource());
	}

	/*
	 * public static void initGUI() { gcDelegate = new
	 * SwingGraphicalContextDelegate(TestDocX4allEditor.class.getSimpleName());
	 * }
	 * 
	 * @AfterClass public static void waitGUI() { gcDelegate.waitGUI(); }
	 * 
	 * @Before public void setUp() { gcDelegate.setUp(); }
	 * 
	 * @Override
	 * 
	 * @After public void tearDown() throws Exception { gcDelegate.tearDown(); }
	 */

	private void openDocXEditor(FlexoResource<DocXDocument> docResource)
			throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		DocXDocument doc = docResource.getResourceData(null);
		FIBDocXDocumentBrowser docBrowser = new FIBDocXDocumentBrowser(doc, serviceManager.getApplicationFIBLibraryService()) {
			@Override
			public void singleClick(Object object) {
				System.out.println("Je viens cliquer sur " + object);
			}
		};
		EditorPanel<?, ?> editorPanel = new EditorPanel<>(doc);
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(docBrowser, BorderLayout.WEST);
		pane.add(editorPanel, BorderLayout.CENTER);
		gcDelegate.addTab(docResource.getName(), pane);

	}

	/*
	 * private JEditorPane createEditorView(DocXDocument document, ToolBarStates
	 * _toolbarStates) {
	 * 
	 * // Clipboard clipboard = getContext().getClipboard(); //
	 * clipboard.addFlavorListener(_toolbarStates); // As a FlavorListener,
	 * _toolbarStates will ONLY be notified // when there is a DataFlavor change
	 * in Clipboard. // Therefore, make sure that toolbarStates' _isPasteEnable
	 * property // is initialised correctly.
	 * 
	 * WordMLTextPane editorView = new WordMLTextPane();
	 * editorView.addFocusListener(_toolbarStates);
	 * editorView.addCaretListener(_toolbarStates);
	 * editorView.setTransferHandler(new TransferHandler());
	 * 
	 * WordMLEditorKit editorKit = (WordMLEditorKit) editorView.getEditorKit();
	 * editorKit.addInputAttributeListener(_toolbarStates);
	 * 
	 * WordMLDocument doc = null;
	 * 
	 * try { doc =
	 * editorKit.openDocument(document.getWordprocessingMLPackage());
	 * 
	 * doc.putProperty(WordMLDocument.FILE_PATH_PROPERTY,
	 * document.getResource().getURI());
	 * doc.addDocumentListener(_toolbarStates); doc.setDocumentFilter(new
	 * WordMLDocumentFilter()); editorView.setDocument(doc);
	 * editorView.putClientProperty(Constants.LOCAL_VIEWS_SYNCHRONIZED_FLAG,
	 * Boolean.TRUE);
	 * 
	 * if (DocUtil.isSharedDocument(doc)) {
	 * editorKit.initPlutextClient(editorView); }
	 * 
	 * } catch (Exception exc) { exc.printStackTrace(); doc = null; }
	 * 
	 * return editorView; }
	 */

}