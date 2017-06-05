/**
 * 
 * Copyright (c) 2015, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf;

import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.technologyadapter.emf.UMLEMFModelSlot.UMLEMFModelSlotImpl;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;

/**
 * UML Model Slot configuration
 * 
 * Will enable users to select profiles to attach to the UML model attached to the MS
 * 
 * @author xtof
 *
 */

public class UMLEMFModelSlotInstanceConfiguration extends EMFModelSlotInstanceConfiguration {

	protected UMLEMFModelSlotInstanceConfiguration(UMLEMFModelSlotImpl emfModelSlotImpl, FlexoConceptInstance fci,
			FlexoResourceCenter<?> rc) {

		super(emfModelSlotImpl, fci, rc);
		setModelUri(rc.getDefaultBaseURI() + "/Models/myUMLModel");
		setRelativePath("/");
		setFilename("myUMLModel" + getModelSlot().getModelSlotTechnologyAdapter()
				.getExpectedModelExtension((EMFMetaModelResource) getModelSlot().getMetaModelResource()));

	}

}
