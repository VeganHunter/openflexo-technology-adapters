/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.dsl;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.dsl.fml.DSLObjectActorReference;
import org.openflexo.technologyadapter.dsl.fml.DSLObjectRole;
import org.openflexo.technologyadapter.dsl.fml.editionaction.AddXXLine;
import org.openflexo.technologyadapter.dsl.fml.editionaction.SelectUniqueXXLine;
import org.openflexo.technologyadapter.dsl.model.DSLSystem;

/**
 * Implementation of the {@link ModelSlot} class for the XX technology adapter (plain text connector)
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ DSLObjectRole.class })
@DeclareEditionActions({ AddXXLine.class })
@DeclareFetchRequests({ SelectUniqueXXLine.class, SelectUniqueXXLine.class })
@DeclareActorReferences({ DSLObjectActorReference.class })
@ModelEntity
@ImplementationClass(DSLModelSlot.XXModelSlotImpl.class)
@XMLElement
@FML("XXModelSlot")
public interface DSLModelSlot extends FreeModelSlot<DSLSystem> {

	public static abstract class XXModelSlotImpl extends FreeModelSlotImpl<DSLSystem> implements DSLModelSlot {

		private static final Logger logger = Logger.getLogger(DSLModelSlot.class.getPackage().getName());

		@Override
		public Class<DSLTechnologyAdapter> getTechnologyAdapterClass() {
			return DSLTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (DSLObjectRole.class.isAssignableFrom(patternRoleClass)) {
				return "line";
			}
			return null;
		}

		@Override
		public Type getType() {
			return DSLSystem.class;
		}

		@Override
		public DSLTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (DSLTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

	}
}
