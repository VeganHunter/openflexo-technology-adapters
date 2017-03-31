/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Fml-technologyadapter-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.gina.controller.action;

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.fml.AbstractVirtualModel;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.controller.GINAAdapterController;
import org.openflexo.technologyadapter.gina.controller.GINAIconLibrary;
import org.openflexo.technologyadapter.gina.fml.action.GivesFMLControlledFIBVirtualModelNature;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class GivesFMLControlledFIBVirtualModelNatureInitializer
		extends ActionInitializer<GivesFMLControlledFIBVirtualModelNature, AbstractVirtualModel<?>, FMLObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public GivesFMLControlledFIBVirtualModelNatureInitializer(ControllerActionInitializer actionInitializer) {
		super(GivesFMLControlledFIBVirtualModelNature.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<GivesFMLControlledFIBVirtualModelNature> getDefaultInitializer() {
		return new FlexoActionInitializer<GivesFMLControlledFIBVirtualModelNature>() {
			@Override
			public boolean run(EventObject e, GivesFMLControlledFIBVirtualModelNature action) {
				Wizard wizard = new GivesFMLControlledFIBVirtualModelNatureWizard(action, getController());
				WizardDialog dialog = new WizardDialog(wizard, getController());
				dialog.showDialog();
				if (dialog.getStatus() != Status.VALIDATED) {
					// Operation cancelled
					return false;
				}
				return true;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<GivesFMLControlledFIBVirtualModelNature> getDefaultFinalizer() {
		return new FlexoActionFinalizer<GivesFMLControlledFIBVirtualModelNature>() {
			@Override
			public boolean run(EventObject e, GivesFMLControlledFIBVirtualModelNature action) {

				GINAAdapterController diagramTAController = (GINAAdapterController) getController()
						.getTechnologyAdapterController(GINATechnologyAdapter.class);
				getController().switchToPerspective(diagramTAController.getFMLControlledFIBNaturePerspective());
				getController().selectAndFocusObject(action.getFocusedObject());
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return IconFactory.getImageIcon(GINAIconLibrary.FIB_COMPONENT_ICON, FMLIconLibrary.VIRTUAL_MODEL_MARKER);
	}

}
