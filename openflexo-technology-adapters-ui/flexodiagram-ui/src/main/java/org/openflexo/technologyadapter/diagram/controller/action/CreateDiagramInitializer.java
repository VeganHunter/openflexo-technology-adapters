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

package org.openflexo.technologyadapter.diagram.controller.action;

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.action.CreateDiagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateDiagramInitializer extends ActionInitializer<CreateDiagram, RepositoryFolder<DiagramResource, ?>, FlexoObject> {
	public CreateDiagramInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateDiagram.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionRunnable<CreateDiagram, RepositoryFolder<DiagramResource, ?>, FlexoObject> getDefaultInitializer() {
		return (e, action) -> {
			if (action.skipChoosePopup) {
				return true;
			}
			Wizard wizard = new CreateDiagramWizard(action, getController());
			WizardDialog dialog = new WizardDialog(wizard, getController());
			dialog.showDialog();
			if (dialog.getStatus() != Status.VALIDATED) {
				// Operation cancelled
				return false;
			}
			return true;
		};
	}

	@Override
	protected FlexoActionRunnable<CreateDiagram, RepositoryFolder<DiagramResource, ?>, FlexoObject> getDefaultFinalizer() {
		return (e, action) -> {
			// getController().setCurrentEditedObjectAsModuleView(action.getNewDiagram());
			getController().selectAndFocusObject(action.getNewDiagram());
			return true;
		};
	}

	@Override
	protected FlexoExceptionHandler<CreateDiagram, RepositoryFolder<DiagramResource, ?>, FlexoObject> getDefaultExceptionHandler() {
		return (exception, action) -> {
			if (exception instanceof NotImplementedException) {
				FlexoController.notify(action.getLocales().localizedForKey("not_implemented_yet"));
				return true;
			}
			return false;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory<CreateDiagram, RepositoryFolder<DiagramResource, ?>, FlexoObject> actionType) {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

	/**
	 * @author Vincent This method has to be removed as soon as we will have a real Wizard Management. Its purpose is to handle the
	 *         separation of FIBs for Model Slot Configurations.
	 * @return File that correspond to the FIB
	 */
	/*private Resource getModelSlotInstanceConfigurationFIB(Class modelSlotClass) {
		if (TypeAwareModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_TYPE_AWARE_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		if (FreeModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_FREE_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		if (FMLRTModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_VIRTUAL_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		return null;
	}*/

}
