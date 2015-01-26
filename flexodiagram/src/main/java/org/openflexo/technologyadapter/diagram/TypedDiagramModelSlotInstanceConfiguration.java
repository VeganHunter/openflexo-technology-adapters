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

package org.openflexo.technologyadapter.diagram;

import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.resource.ResourceRepository;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlotInstanceConfiguration;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;

/**
 * This class is used to stored the configuration of a {@link TypedDiagramModelSlot} which has to be instantiated
 * 
 * 
 * @author sylvain
 * 
 */
public class TypedDiagramModelSlotInstanceConfiguration extends
		TypeAwareModelSlotInstanceConfiguration<Diagram, DiagramSpecification, TypedDiagramModelSlot> {

	protected TypedDiagramModelSlotInstanceConfiguration(TypedDiagramModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
	}

	@Override
	public void setOption(org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		if (option == DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel) {
			modelUri = getAction().getFocusedObject().getProject().getURI() + "/Diagrams/myDiagram";
			relativePath = "/";
			filename = "myDiagram" + DiagramResource.DIAGRAM_SUFFIX;
		} else if (option == DefaultModelSlotInstanceConfigurationOption.CreateSharedNewModel) {
			modelUri = "ResourceCenter/Models/";
			relativePath = "/";
			filename = "myDiagram" + DiagramResource.DIAGRAM_SUFFIX;
		}
	}

	@Override
	public boolean isURIEditable() {
		return false;
	}

	@Override
	public String getModelUri() {
		ResourceRepository<?> repository = getResourceCenter()
				.getRepository(DiagramRepository.class, getModelSlot().getModelSlotTechnologyAdapter());
		String generatedUri = repository.generateURI(getFilename());
		if (repository != null) {
			while (repository.getResource(generatedUri) != null) {
				generatedUri = repository.generateURI(getFilename());
			}
		}

		return generatedUri;
	}

	@Override
	protected boolean checkValidFileName() {
		if (!super.checkValidFileName()) {
			return false;
		}
		if (!getFilename().endsWith(DiagramResource.DIAGRAM_SUFFIX)) {
			setErrorMessage(FlexoLocalization.localizedForKey("file_name_should_end_with_.diagram_suffix"));
			return false;
		}
		return true;
	}

}
