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

package org.openflexo.technologyadapter.docx.gui.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.docx4all.swing.text.DocumentElement;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionSource;
import org.openflexo.foundation.doc.nature.FMLControlledDocumentVirtualModelInstanceNature;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionType;
import org.openflexo.technologyadapter.docx.controller.DocXAdapterController;
import org.openflexo.technologyadapter.docx.gui.widget.DocXEditor;
import org.openflexo.technologyadapter.docx.gui.widget.FIBDocXDocumentBrowser;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXObject;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.technologyadapter.docx.model.DocXTable;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelInstanceNature;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.listener.FlexoActionButton;

/**
 * A {@link ModuleView} for a federated document inside a {@link VirtualModelInstance}<br>
 * It is stated that the related {@link VirtualModelInstance} has the {@link FMLControlledDocumentVirtualModelInstanceNature}
 * 
 * @author sylvain
 *
 */
@SuppressWarnings("serial")
public class FMLControlledDocXDocumentModuleView extends JPanel
		implements ModuleView<VirtualModelInstance>, FlexoActionSource, PropertyChangeListener {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FMLControlledDocXDocumentModuleView.class.getPackage().getName());

	private final VirtualModelInstance virtualModelInstance;
	private final FlexoPerspective perspective;

	private final DocXEditor docxEditor;

	private final FIBDocXDocumentBrowser browser;
	private final JPanel topPanel;

	public FMLControlledDocXDocumentModuleView(VirtualModelInstance virtualModelInstance, FlexoPerspective perspective) {
		super();
		setLayout(new BorderLayout());
		this.virtualModelInstance = virtualModelInstance;
		this.perspective = perspective;

		if (!virtualModelInstance.hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE)) {
			logger.severe("Supplied VirtualModelInstance does not have the FMLControlledDocXVirtualModelInstanceNature");
		}

		docxEditor = new DocXEditor(getDocument(), true);
		add(docxEditor, BorderLayout.CENTER);

		browser = new FIBDocXDocumentBrowser(getDocument(), perspective.getController()) {
			@Override
			public void setSelectedDocumentElement(DocXObject selected) {
				super.setSelectedDocumentElement(selected);
				selectElementInDocumentEditor(selected);
			}
		};
		add(browser, BorderLayout.EAST);

		topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		for (ActionScheme actionScheme : virtualModelInstance.getVirtualModel().getActionSchemes()) {
			ActionSchemeActionType actionType = new ActionSchemeActionType(actionScheme, virtualModelInstance);
			topPanel.add(new FlexoActionButton(actionType, this, perspective.getController()));
		}
		add(topPanel, BorderLayout.NORTH);

		validate();

		getRepresentedObject().getPropertyChangeSupport().addPropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
	}

	public VirtualModelInstance getVirtualModelInstance() {
		return virtualModelInstance;
	}

	public DocXDocument getDocument() {
		return FMLControlledDocXVirtualModelInstanceNature.getDocument(getVirtualModelInstance());
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	@Override
	public void deleteModuleView() {
		getRepresentedObject().getPropertyChangeSupport().removePropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
		perspective.getController().removeModuleView(this);
		// TODO: delete docx editor
	}

	@Override
	public VirtualModelInstance getRepresentedObject() {
		return getVirtualModelInstance();
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void willHide() {
		System.out.println("FMLControlledDocXDocumentModuleView WILL HIDE !!!!!!");
	}

	@Override
	public void willShow() {
		System.out.println("FMLControlledDocXDocumentModuleView WILL SHOW !!!!!!");
		getPerspective().focusOnObject(getRepresentedObject());
	}

	public DocXAdapterController getDocXAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DocXAdapterController.class);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {

		perspective.setTopRightView(null);
		perspective.setBottomRightView(null);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(false);
			}
		});

		controller.getControllerModel().setRightViewVisible(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getRepresentedObject() && evt.getPropertyName().equals(getRepresentedObject().getDeletedProperty())) {
			deleteModuleView();
		}
	}

	@Override
	public FlexoObject getFocusedObject() {
		return getVirtualModelInstance();
	}

	@Override
	public List<? extends FlexoObject> getGlobalSelection() {
		return null;
	}

	@Override
	public FlexoEditor getEditor() {
		return perspective.getController().getEditor();
	}

	protected void selectElementInDocumentEditor(DocXObject element) {

		System.out.println("****************** selectElementInDocumentEditor with " + element);

		try {

			// List<DocXElement> fragmentElements = fragment.getElements();

			final List<DocumentElement> elts = new ArrayList<DocumentElement>();

			DocumentElement docElement = null;

			if (element instanceof DocXParagraph) {
				docElement = docxEditor.getMLDocument().getElement(((DocXParagraph) element).getP());
				elts.add(docElement);
			}
			if (element instanceof DocXTable) {
				docElement = docxEditor.getMLDocument().getElement(((DocXTable) element).getTbl());
				elts.add(docElement);
			}

			// Thread.dumpStack();
			docxEditor.getMLDocument().setSelectedElements(elts);

			if (docElement != null) {
				scrollTo(docElement);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		docxEditor.getEditorView().revalidate();
		docxEditor.getEditorView().repaint();

	}

	private void scrollTo(final DocumentElement docElement) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (!docxEditor.getEditorView().scrollToElement(docElement, false)) {
					scrollTo(docElement);
				}
				docxEditor.getEditorView().revalidate();
				docxEditor.getEditorView().repaint();
			}
		});
	}

}
