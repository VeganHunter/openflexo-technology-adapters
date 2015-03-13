/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.freeplane.controller;

import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.fib.utils.InspectorGroup;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceNature;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.controller.acitoninit.AddChildNodeInitializer;
import org.openflexo.technologyadapter.freeplane.controller.acitoninit.DeleteNodeInitializer;
import org.openflexo.technologyadapter.freeplane.controller.acitoninit.NewFreeplaneMapInitializer;
import org.openflexo.technologyadapter.freeplane.controller.acitoninit.NewSiblingAboveNodeInitializer;
import org.openflexo.technologyadapter.freeplane.controller.acitoninit.NewSiblingNodeInitializer;
import org.openflexo.technologyadapter.freeplane.fml.FMLControlledFreeplaneVirtualModelInstanceNature;
import org.openflexo.technologyadapter.freeplane.libraries.FreeplaneIconLibrary;
import org.openflexo.technologyadapter.freeplane.listeners.FreeplaneListenersInitilizer;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.view.FMLControlledFreeplaneModuleView;
import org.openflexo.technologyadapter.freeplane.view.FreeplaneModuleView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class FreeplaneAdapterController extends TechnologyAdapterController<FreeplaneTechnologyAdapter> {

	@Override
	public Class<FreeplaneTechnologyAdapter> getTechnologyAdapterClass() {
		return FreeplaneTechnologyAdapter.class;
	}

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		freeplaneInspectorGroup = controller.loadInspectorGroup("Freeplane", getFMLTechnologyAdapterInspectorGroup());
		// actionInitializer.getController().getModuleInspectorController()
		// .loadDirectory(ResourceLocator.locateResource("Inspectors/Freeplane"));
	}

	private InspectorGroup freeplaneInspectorGroup;

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return freeplaneInspectorGroup;
	}

	@Override
	protected void initializeActions(final ControllerActionInitializer actionInitializer) {
		new AddChildNodeInitializer(actionInitializer);
		new NewSiblingAboveNodeInitializer(actionInitializer);
		new NewSiblingNodeInitializer(actionInitializer);
		new DeleteNodeInitializer(actionInitializer);
		new NewFreeplaneMapInitializer(actionInitializer);
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return FreeplaneIconLibrary.FREEPLANE_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return FreeplaneIconLibrary.FREEPLANE_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return FreeplaneIconLibrary.FREEPLANE_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return FreeplaneIconLibrary.FREEPLANE_FILE_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(final Class<? extends TechnologyObject<?>> objectClass) {
		return FreeplaneIconLibrary.FREEPLANE_TECHNOLOGY_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(final TechnologyObject<FreeplaneTechnologyAdapter> object,
			final FlexoController controller, final FlexoPerspective perspective) {
		if (object instanceof IFreeplaneMap) {
			FreeplaneListenersInitilizer.init((IFreeplaneMap) object, controller);
			return new FreeplaneModuleView((IFreeplaneMap) object, controller, perspective);
		}

		return new EmptyPanel<TechnologyObject<FreeplaneTechnologyAdapter>>(controller, perspective, object);
	}

	@Override
	public ImageIcon getIconForPatternRole(final Class<? extends FlexoRole<?>> arg0) {
		return FreeplaneIconLibrary.FREEPLANE_TECHNOLOGY_ICON;
	}

	@Override
	public String getWindowTitleforObject(final TechnologyObject<FreeplaneTechnologyAdapter> object, final FlexoController arg1) {
		if (object instanceof IFreeplaneMap) {
			return FreeplaneBasicAdapter.getInstance().getMapName();
		}
		return object.toString();
	}

	/**
	 * @return true if <code>object</code> is instance of {@link IFreeplaneMap} or {@link IFreeplaneNode}
	 */
	@Override
	public boolean hasModuleViewForObject(final TechnologyObject<FreeplaneTechnologyAdapter> object, final FlexoController controller) {
		return object instanceof IFreeplaneMap;
	}

	@Override
	public List<? extends VirtualModelInstanceNature> getSpecificVirtualModelInstanceNatures(final VirtualModelInstance vmInstance) {
		if (vmInstance.hasNature(FMLControlledFreeplaneVirtualModelInstanceNature.INSTANCE)) {
			return Collections.singletonList(FMLControlledFreeplaneVirtualModelInstanceNature.INSTANCE);
		}
		return super.getSpecificVirtualModelInstanceNatures(vmInstance);
	}

	@Override
	public ModuleView<VirtualModelInstance> createVirtualModelInstanceModuleViewForSpecificNature(final VirtualModelInstance vmInstance,
			final VirtualModelInstanceNature nature, final FlexoController controller, final FlexoPerspective perspective) {
		if (vmInstance.hasNature(nature) & nature == FMLControlledFreeplaneVirtualModelInstanceNature.INSTANCE) {
			FreeplaneListenersInitilizer.init(vmInstance, controller);
			return new FMLControlledFreeplaneModuleView(controller, vmInstance, perspective);
		}
		return super.createVirtualModelInstanceModuleViewForSpecificNature(vmInstance, nature, controller, perspective);
	}
}
