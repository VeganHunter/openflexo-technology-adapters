/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.fml.action;

import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.foundation.action.transformation.FlexoRoleCreationStrategy;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

/**
 * Encodes a basic transformation which sets graphical representation of an existing {@link GraphicalElementRole} with the
 * {@link GraphicalRepresentation} addressed by action focused object
 * 
 * @author sylvain
 *
 */
public abstract class GraphicalElementRoleCreationStrategy<A extends DeclareDiagramElementInFlexoConcept<A, T>, R extends GraphicalElementRole<T, GR>, T extends DiagramElement<GR>, GR extends GraphicalRepresentation>
		extends FlexoRoleCreationStrategy<A, R, T, DiagramElement<?>> {

	private R newRole;

	public GraphicalElementRoleCreationStrategy(A transformationAction) {
		super(transformationAction);
	}

	// public abstract void bindTo(R role, T diagramElement);

	@Override
	public R createNewFlexoRole() {
		String newRoleName = getNewRoleName();
		newRole = getTransformationAction().getFactory().newInstance(getRoleType());
		newRole.setRoleName(newRoleName);
		newRole.setModelSlot(getTransformationAction().getTypedDiagramModelSlot());
		newRole.setReadOnlyLabel(true);

		newRole.bindTo(getTransformationAction().getFocusedObject());

		getTransformationAction().getFlexoConcept().addToFlexoProperties(newRole);

		return newRole;
	}

	// public abstract void normalizeGraphicalRepresentation(R role);

	public void dismissNewFlexoRole() {
		if (getNewFlexoRole() != null) {
			getTransformationAction().getFlexoConcept().removeFromFlexoProperties(newRole);
			newRole = null;
		}
	}

	@Override
	public R getNewFlexoRole() {
		return newRole;
	}

}
