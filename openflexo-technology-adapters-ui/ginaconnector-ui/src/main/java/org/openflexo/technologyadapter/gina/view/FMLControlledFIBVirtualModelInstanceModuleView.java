/*
 * (c) Copyright 2013- Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.gina.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.foundation.task.Progress;
import org.openflexo.gina.swing.editor.FIBEditor;
import org.openflexo.gina.swing.editor.controller.FIBEditorController;
import org.openflexo.gina.swing.utils.FIBJPanel;
import org.openflexo.icon.IconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot.VariableAssignment;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.controller.GINAAdapterController;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

@SuppressWarnings("serial")
public class FMLControlledFIBVirtualModelInstanceModuleView extends JPanel implements ModuleView<VirtualModelInstance> {

	private final FlexoController controller;
	private final FlexoPerspective perspective;
	private final VirtualModelInstance virtualModelInstance;

	private FIBEditorController editorController;
	private GINAFIBComponent component;
	private FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot> modelSlotInstance;
	private FIBJPanel componentView;

	public FMLControlledFIBVirtualModelInstanceModuleView(VirtualModelInstance representedObject, FlexoController controller,
			FlexoPerspective perspective) {
		// super(representedObject, controller,
		// FMLControlledFIBVirtualModelInstanceNature.getGINAFIBComponent(representedObject).getComponent(), true);

		super(new BorderLayout());

		this.controller = controller;
		this.perspective = perspective;
		this.virtualModelInstance = representedObject;

		modelSlotInstance = FMLControlledFIBVirtualModelInstanceNature.getModelSlotInstance(representedObject);
		component = FMLControlledFIBVirtualModelInstanceNature.getGINAFIBComponent(representedObject);

		if (component != null && component.getComponent() != null && component.getComponent().getModelFactory() != null) {
			System.out.println(component.getComponent().getModelFactory().stringRepresentation(component.getComponent()));
		}

		component.bindTo(getRepresentedObject().getVirtualModel(), modelSlotInstance.getModelSlot());

		componentView = new FIBJPanel<Object>(component.getComponent(), null, FlexoLocalization.getMainLocalizer()) {
			@Override
			public void delete() {
				// TODO Auto-generated method stub

			}

			@Override
			public Class<Object> getRepresentedType() {
				// TODO Auto-generated method stub
				return Object.class;
			}
		};

		// _setTransparent(this);

		add(componentView, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		editButton = new JButton(FlexoLocalization.localizedForKey("edit"), IconLibrary.EDIT_ICON);
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isEditMode()) {
					switchToEditMode();
				}
			}
		});
		bottomPanel.add(editButton);

		doneButton = new JButton(FlexoLocalization.localizedForKey("done"), IconLibrary.VALID_ICON);
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isEditMode()) {
					switchToNormalMode();
				}
			}
		});
		bottomPanel.add(doneButton);

		saveButton = new JButton(FlexoLocalization.localizedForKey("save"), IconLibrary.SAVE_ICON);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isEditMode()) {
					try {
						saveEditedComponent();
					} catch (SaveResourceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		bottomPanel.add(saveButton);

		doneButton.setVisible(false);
		saveButton.setVisible(false);

		add(bottomPanel, BorderLayout.SOUTH);
	}

	private JButton editButton;
	private JButton doneButton;
	private JButton saveButton;
	private boolean editMode = false;

	public boolean isEditMode() {
		return editMode;
	}

	public void switchToEditMode() {
		FlexoTask switchToEditMode = new FlexoTask("opening_fib_editor") {
			@Override
			public void performTask() throws InterruptedException {
				setExpectedProgressSteps(50);
				_switchToEditMode();
			}
		};
		getFlexoController().getApplicationContext().getTaskManager().scheduleExecution(switchToEditMode);
	}

	public boolean _switchToEditMode() {

		System.out.println("switchToEditMode, editMode=" + editMode);

		if (editMode) {
			return false;
		}

		editMode = true;

		Progress.progress(FlexoLocalization.localizedForKey("switching_to_edit_mode"));

		if (editorController == null) {
			Progress.progress(FlexoLocalization.localizedForKey("loading_fib_editor"));
			editorController = getFIBEditor(false).openFIBComponent(component.getComponent(), virtualModelInstance.getResource(),
					controller.getFlexoFrame());
		}

		Progress.progress(FlexoLocalization.localizedForKey("initialize_fib_editor"));

		remove(componentView);
		add(editorController.getEditorPanel(), BorderLayout.CENTER);

		editButton.setVisible(false);
		doneButton.setVisible(true);
		saveButton.setVisible(true);

		revalidate();
		repaint();

		return true;
	}

	public boolean switchToNormalMode() {

		System.out.println("switchToNormalMode, editMode=" + editMode);

		if (!editMode) {
			return false;
		}

		editMode = false;

		if (editorController != null) {
			remove(editorController.getEditorPanel());
		}

		add(componentView, BorderLayout.CENTER);

		editButton.setVisible(true);
		doneButton.setVisible(false);
		saveButton.setVisible(false);

		revalidate();
		repaint();

		return true;
	}

	public boolean saveEditedComponent() throws SaveResourceException {

		if (!editMode) {
			return false;
		}

		component.getResource().save(null);

		return true;
	}

	public FlexoController getFlexoController() {
		return controller;
	}

	public GINAAdapterController getAdapterController() {
		GINATechnologyAdapter ta = getFlexoController().getApplicationContext().getTechnologyAdapterService()
				.getTechnologyAdapter(GINATechnologyAdapter.class);
		return getFlexoController().getApplicationContext().getTechnologyAdapterControllerService().getTechnologyAdapterController(ta);
	}

	public FIBEditor getFIBEditor(boolean launchInTask) {
		if (getAdapterController() != null) {
			return getAdapterController().getFIBEditor(launchInTask);
		}
		return null;
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {

		for (VariableAssignment variableAssignment : modelSlotInstance.getModelSlot().getAssignments()) {
			try {
				Object value = variableAssignment.getValue().getBindingValue(getRepresentedObject());
				// System.out.println("> Variable " + variableAssignment.getVariable() + " value=" + value);
				componentView.getController().setVariableValue(variableAssignment.getVariable(), value);
			} catch (TypeMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullReferenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Test
		// JFIBDialog.instanciateAndShowDialog(component.getComponent(), null, getFlexoController().getFlexoFrame(), false);

		// _setTransparent(this);

		// _setChildTransparent(this);

		// revalidate();
		// repaint();

		// If you want to add right and left panels to your module view, do it here. Un comment following code with your component.
		// SwingUtilities.invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// perspective.setTopRightView(customJComponent);
		// controller.getControllerModel().setRightViewVisible(true);
		// }
		// });

		// Sets palette view of editor to be the top right view

		// getDiagramTechnologyAdapterController(controller).getInspectors().attachToEditor(getEditor());
		// getDiagramTechnologyAdapterController(controller).getDialogInspectors().attachToEditor(getEditor());
		// getDiagramTechnologyAdapterController(controller).getScaleSelector().attachToEditor(getEditor());

		// perspective.setBottomRightView(getDiagramTechnologyAdapterController(controller).getInspectors().getPanelGroup());

		/*SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(true);
			}
		});*/

		// controller.getControllerModel().setRightViewVisible(true);

	}

	/**
	 * Remove ModuleView from controller.
	 */
	@Override
	public void deleteModuleView() {
		getFlexoController().removeModuleView(this);
	}

	/**
	 * @return perspective given during construction of ModuleView.
	 */
	@Override
	public FlexoPerspective getPerspective() {
		return this.perspective;
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willShow() {
		// Nothing to implement by default, empty body
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willHide() {
		// Nothing to implement by default, empty body
	}

	public GINAFIBComponent getGINAFIBComponent() {
		return component;
	}

	@Override
	public boolean isAutoscrolled() {
		// If you want to handle scrollable by yourself instead of letting Openflexo doing it, change return to true.
		return false;
	}

	@Override
	public VirtualModelInstance getRepresentedObject() {
		return virtualModelInstance;
	}
}