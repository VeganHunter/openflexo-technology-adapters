/*
 * (c) Copyright 2013 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of either : 
 * - GNU General Public License as published by
 * the Free Software Foundation version 3 of the License.
 * - EUPL v1.1 : European Union Public Licence
 * 
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License or EUPL for more details.
 *
 * You should have received a copy of the GNU General Public License or 
 * European Union Public Licence along with OpenFlexo. 
 * If not, see <http://www.gnu.org/licenses/>, or http://ec.europa.eu/idabc/eupl.html
 *
 */

package org.openflexo.technologyadapter.oslc.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.TechnologySpecificAction;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.oslc.OSLCTechnologyAdapter;
import org.openflexo.technologyadapter.oslc.gui.OSLCIconLibrary;
import org.openflexo.technologyadapter.oslc.gui.view.FIBOSLCCatalog;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCService;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirement;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirementCollection;
import org.openflexo.technologyadapter.oslc.rm.OSLCResourceResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.AddOSLCRequirement;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.AddOSLCResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCRequirement;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCService;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.virtualmodel.core.OSLCResourceRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.core.OSLCServiceProviderRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.core.OSLCServiceRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.rm.OSLCRequirementCollectionRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.rm.OSLCRequirementRole;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class OSLCAdapterController extends TechnologyAdapterController<OSLCTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(OSLCAdapterController.class.getPackage().getName());

	@Override
	public Class<OSLCTechnologyAdapter> getTechnologyAdapterClass() {
		return OSLCTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
		actionInitializer.getController().getModuleInspectorController()
				.loadDirectory(ResourceLocator.locateResource("src/main/resources/Inspectors/oslc"));
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return OSLCIconLibrary.OSLC_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return OSLCIconLibrary.OSLC_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return OSLCIconLibrary.OSLC_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return OSLCIconLibrary.OSLC_FILE_ICON;
	}

	/**
	 * Return icon representing supplied edition action
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForEditionAction(Class<? extends TechnologySpecificAction<?, ?>> editionActionClass) {
		if (AddOSLCResource.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCResource.class), IconLibrary.DUPLICATE);
		}
		else if (AddOSLCRequirement.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCRequirement.class), IconLibrary.DUPLICATE);
		}
		else if (SelectOSLCResource.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCResource.class), IconLibrary.IMPORT);
		}
		else if (SelectOSLCRequirement.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCRequirement.class), IconLibrary.IMPORT);
		}
		else if (SelectOSLCServiceProvider.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCServiceProvider.class), IconLibrary.IMPORT);
		}
		else if (SelectOSLCService.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCService.class), IconLibrary.IMPORT);
		}
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<OSLCTechnologyAdapter> arg0, FlexoController arg1, FlexoPerspective arg2) {
		if (arg0 instanceof OSLCServiceProviderCatalog) {
			return new FIBOSLCCatalog((OSLCResource) arg0, arg1);
		}
		return new EmptyPanel<TechnologyObject<OSLCTechnologyAdapter>>(arg1, arg2, arg0);
	}

	@Override
	public ImageIcon getIconForPatternRole(Class<? extends FlexoRole<?>> arg0) {
		if (OSLCResourceRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCResource.class);
		}
		if (OSLCRequirementRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCRequirement.class);
		}
		if (OSLCServiceProviderRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCServiceProvider.class);
		}
		if (OSLCServiceRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCService.class);
		}
		if (OSLCRequirementCollectionRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCRequirementCollection.class);
		}
		return null;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<OSLCTechnologyAdapter> arg0, FlexoController arg1) {
		if (arg0 instanceof OSLCResourceResource) {
			return ((OSLCResourceResource) arg0).getName();
		}
		return arg0.toString();
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<OSLCTechnologyAdapter> arg0, FlexoController arg1) {
		if (arg0 instanceof OSLCServiceProviderCatalog) {
			return true;
		}
		return false;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		return OSLCIconLibrary.iconForObject(objectClass);
	}

}
