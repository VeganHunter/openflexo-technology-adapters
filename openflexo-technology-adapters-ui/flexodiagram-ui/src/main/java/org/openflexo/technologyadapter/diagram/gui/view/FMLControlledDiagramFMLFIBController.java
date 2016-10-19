/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexo-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.gui.view;

import java.util.logging.Logger;

import org.openflexo.fml.controller.FMLFIBController;
import org.openflexo.foundation.fml.AbstractVirtualModel;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * Represents the controller of a FIBInspector (FIBComponent) in the context of FML
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramFMLFIBController extends FMLFIBController {

	private static final Logger logger = FlexoLogger.getLogger(FMLControlledDiagramFMLFIBController.class.getPackage().getName());

	public FMLControlledDiagramFMLFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
		super(component, viewFactory);
	}

	public DiagramSpecification getDiagramSpecification(AbstractVirtualModel<?> virtualModel) {
		TypedDiagramModelSlot typedDiagramModelSlot = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(virtualModel);
		if (typedDiagramModelSlot != null) {
			return typedDiagramModelSlot.getDiagramSpecification();
		}
		return null;
	}

	private Diagram selectedDiagram;

	public Diagram getSelectedDiagram() {
		return selectedDiagram;
	}

	public void setSelectedDiagram(Diagram selectedDiagram) {

		System.out.println("Hop, on selectionne le diagramme: " + selectedDiagram);

		if ((selectedDiagram == null && this.selectedDiagram != null)
				|| (selectedDiagram != null && !selectedDiagram.equals(this.selectedDiagram))) {
			Diagram oldValue = this.selectedDiagram;
			this.selectedDiagram = selectedDiagram;
			getPropertyChangeSupport().firePropertyChange("selectedDiagram", oldValue, selectedDiagram);
		}
	}

	/*public void addCustomProperty(FlexoObject object) {
		if (object instanceof InnerResourceData) {
			System.out.println("Creating property for object " + object);
			AddFlexoProperty action = AddFlexoProperty.actionType.makeNewAction(object, null, getEditor());
			action.doAction();
		}
	}
	
	public void removeCustomProperty(FlexoProperty property) {
		System.out.println("Deleting property " + property + " for object " + property.getOwner());
		property.delete();
	}*/

}