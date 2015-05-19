/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Openflexo-technologyadapter-archetype-simplemodel, a component of the software infrastructure 
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


package org.openflexo.technologyadapter.csv.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.library.CSVIconLibrary;
import org.openflexo.technologyadapter.csv.view.CSVModuleView;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class CSVAdapterController extends TechnologyAdapterController<CSVTechnologyAdapter> {
    
	static final Logger LOGGER = Logger.getLogger(CSVAdapterController.class.getPackage().getName());

	@Override
	public Class<CSVTechnologyAdapter> getTechnologyAdapterClass() {
		return CSVTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
		actionInitializer.getController().getModuleInspectorController()
				.loadDirectory(ResourceLocator.locateResource("Inspectors/CSV"));
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return CSVIconLibrary.CSV_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return CSVIconLibrary.CSV_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return CSVIconLibrary.CSV_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return CSVIconLibrary.CSV_FILE_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(final Class<? extends TechnologyObject<?>> objectClass) {
		// TODO Auto-generated method stub
		return CSVIconLibrary.CSV_TECHNOLOGY_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(final TechnologyObject<CSVTechnologyAdapter> object, final FlexoController controller, final FlexoPerspective perspective) {
		// TODO Auto-generated method stub : update your moduleView code to have somethig represented
		if (object instanceof CSVModel){
			return new CSVModuleView((CSVModel) object, controller, perspective);
		}
		return new EmptyPanel<TechnologyObject<CSVTechnologyAdapter>>(controller, perspective, object);
	}

	@Override
	public ImageIcon getIconForPatternRole(Class<? extends FlexoRole<?>> role) {
		// TODO Auto-generated method stub
		return CSVIconLibrary.CSV_TECHNOLOGY_ICON;
	}

	@Override
	public String getWindowTitleforObject(
			TechnologyObject<CSVTechnologyAdapter> obj, FlexoController controller) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public boolean hasModuleViewForObject(
			TechnologyObject<CSVTechnologyAdapter> obj, FlexoController controller) {
		// TODO Auto-generated method stub
		return obj instanceof CSVModel;
	}
}

