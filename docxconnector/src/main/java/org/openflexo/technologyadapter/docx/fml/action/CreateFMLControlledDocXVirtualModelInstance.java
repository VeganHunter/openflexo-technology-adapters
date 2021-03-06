/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.docx.fml.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.doc.fml.action.CreateFMLControlledDocumentVirtualModelInstance;
import org.openflexo.foundation.doc.nature.FMLControlledDocumentVirtualModelNature;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXContainerNature;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelNature;

public class CreateFMLControlledDocXVirtualModelInstance
		extends CreateFMLControlledDocumentVirtualModelInstance<CreateFMLControlledDocXVirtualModelInstance> {

	private static final Logger logger = Logger.getLogger(CreateFMLControlledDocXVirtualModelInstance.class.getPackage().getName());

	public static CreateFMLControlledDocumentVirtualModelInstanceActionType<CreateFMLControlledDocXVirtualModelInstance> actionType = new CreateFMLControlledDocumentVirtualModelInstanceActionType<CreateFMLControlledDocXVirtualModelInstance>(
			FMLControlledDocXContainerNature.INSTANCE) {

		@Override
		public CreateFMLControlledDocXVirtualModelInstance makeNewAction(FlexoObject focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateFMLControlledDocXVirtualModelInstance(focusedObject, globalSelection, editor);
		}
	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFMLControlledDocXVirtualModelInstance.actionType, VirtualModelInstance.class);
	}

	private CreateFMLControlledDocXVirtualModelInstance(FlexoObject focusedObject, Vector<FlexoObject> globalSelection,
			FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class).getLocales();
		}
		return super.getLocales();
	}

	@Override
	public FMLControlledDocumentVirtualModelNature<?> getVirtualModelNature() {
		return FMLControlledDocXVirtualModelNature.INSTANCE;
	}

}
