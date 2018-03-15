/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

package org.openflexo.technologyadapter.owl.controller;

import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.technologyadapter.owl.gui.OWLIconLibrary;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLObject;
import org.openflexo.technologyadapter.owl.model.action.CreateDataProperty;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateDataPropertyInitializer extends ActionInitializer<CreateDataProperty, OWLObject, OWLConcept<?>> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	CreateDataPropertyInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateDataProperty.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionRunnable<CreateDataProperty, OWLObject, OWLConcept<?>> getDefaultInitializer() {
		return (e, action) -> instanciateAndShowDialog(action, OWLFIBLibrary.CREATE_DATA_PROPERTY_DIALOG_FIB);
	}

	@Override
	protected FlexoActionRunnable<CreateDataProperty, OWLObject, OWLConcept<?>> getDefaultFinalizer() {
		return (e, action) -> {
			getController().getSelectionManager().setSelectedObject(action.getNewProperty());
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory<CreateDataProperty, OWLObject, OWLConcept<?>> actionType) {
		return OWLIconLibrary.ONTOLOGY_DATA_PROPERTY_ICON;
	}

}
