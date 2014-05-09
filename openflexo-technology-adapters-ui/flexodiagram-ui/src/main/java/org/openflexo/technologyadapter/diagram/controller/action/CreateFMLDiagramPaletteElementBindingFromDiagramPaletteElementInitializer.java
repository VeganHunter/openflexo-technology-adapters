/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.technologyadapter.diagram.controller.action;

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElementInitializer extends ActionInitializer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement, DiagramPaletteElement, DiagramPaletteElement> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElementInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement.actionType, actionInitializer);
	}


	@Override
	protected FlexoActionInitializer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement>() {
			@Override
			public boolean run(EventObject e, CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement action) {
				action.setImage((DiagramIconLibrary.FML_PALETTE_ELEMENT_BINDING_ICON_64X64).getImage());
				return instanciateAndShowDialog(action, DiagramCst.CREATE_FML_DIAGRAM_PALETTE_ELEMENT_BINDING_FROM_DIAGRAM_PALETTE_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement>() {
			@Override
			public boolean run(EventObject e, CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement action) {
				return true;
			}
		};
	}

	@Override
	protected FlexoExceptionHandler<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement> getDefaultExceptionHandler() {
		return new FlexoExceptionHandler<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement>() {
			@Override
			public boolean handleException(FlexoException exception, CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement action) {
				if (exception instanceof NotImplementedException) {
					FlexoController.notify(FlexoLocalization.localizedForKey("not_implemented_yet"));
					return true;
				}
				return false;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

}