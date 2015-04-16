/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml.fml.editionaction;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLModelSlot;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;

@ModelEntity
@ImplementationClass(GetXMLDocumentRoot.GetXMLDocumentRootImpl.class)
@XMLElement
@FML("GetXMLDocumentRoot")
public interface GetXMLDocumentRoot extends XMLAction<XMLModelSlot, XMLIndividual> {

	public static abstract class GetXMLDocumentRootImpl extends TechnologySpecificActionImpl<XMLModelSlot, XMLIndividual> implements
			GetXMLDocumentRoot {

		private static final Logger logger = Logger.getLogger(GetXMLDocumentRoot.class.getPackage().getName());

		public GetXMLDocumentRootImpl() {
			super();
		}

		@Override
		public XMLIndividual execute(FlexoBehaviourAction action) {

			ModelSlotInstance<XMLModelSlot, XMLModel> modelSlotInstance = (ModelSlotInstance<XMLModelSlot, XMLModel>) getModelSlotInstance(action);
			XMLModel model = modelSlotInstance.getAccessedResourceData();

			XMLIndividual rootIndiv = model.getRoot();

			return rootIndiv;
		}

		@Override
		public Type getAssignableType() {
			return Object.class;
		}

	}
}
