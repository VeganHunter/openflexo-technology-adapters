/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2013 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.emf;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.fml.EMFClassClassRole;
import org.openflexo.technologyadapter.emf.fml.EMFEnumClassRole;
import org.openflexo.technologyadapter.emf.fml.EMFObjectIndividualRole;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividualAttributeDataPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividualAttributeObjectPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividualReferenceObjectPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.RemoveEMFObjectIndividualAttributeDataPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.RemoveEMFObjectIndividualAttributeObjectPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.RemoveEMFObjectIndividualReferenceObjectPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.SelectEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;

/**
 * Implementation of the ModelSlot class for the EMF technology adapter<br>
 * We expect here to connect an EMF model conform to an EMFMetaModel
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ EMFObjectIndividualRole.class, EMFClassClassRole.class, EMFEnumClassRole.class })
@DeclareEditionActions({ AddEMFObjectIndividual.class, AddEMFObjectIndividualAttributeDataPropertyValue.class,
		AddEMFObjectIndividualAttributeObjectPropertyValue.class, AddEMFObjectIndividualReferenceObjectPropertyValue.class,
		RemoveEMFObjectIndividualAttributeDataPropertyValue.class, RemoveEMFObjectIndividualAttributeObjectPropertyValue.class,
		RemoveEMFObjectIndividualReferenceObjectPropertyValue.class })
@DeclareFetchRequests({ SelectEMFObjectIndividual.class })
@ModelEntity
@ImplementationClass(EMFModelSlot.EMFModelSlotImpl.class)
@XMLElement
@FML("EMFModelSlot")
public interface EMFModelSlot extends TypeAwareModelSlot<EMFModel, EMFMetaModel> {

	@Override
	public EMFTechnologyAdapter getModelSlotTechnologyAdapter();

	public static abstract class EMFModelSlotImpl extends TypeAwareModelSlotImpl<EMFModel, EMFMetaModel> implements EMFModelSlot {

		private static final Logger logger = Logger.getLogger(EMFModelSlot.class.getPackage().getName());

		@Override
		public Class<EMFTechnologyAdapter> getTechnologyAdapterClass() {
			return EMFTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public EMFModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new EMFModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (EMFObjectIndividualRole.class.isAssignableFrom(patternRoleClass)) {
				return "individual";
			}
			return null;
		}

		@Override
		public String getURIForObject(
				TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, ? extends TypeAwareModelSlot<EMFModel, EMFMetaModel>> msInstance,
				Object o) {
			if (o instanceof IFlexoOntologyObject) {
				return ((IFlexoOntologyObject) o).getURI();
			}
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(
				TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, ? extends TypeAwareModelSlot<EMFModel, EMFMetaModel>> msInstance,
				String objectURI) {
			return msInstance.getAccessedResourceData().getObject(objectURI);
		}

		@Override
		public Type getType() {
			return EMFModel.class;
		}

		@Override
		public String getPreciseType() {
			return "EMF Model";
		};

		@Override
		public EMFTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (EMFTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public EMFModelResource createProjectSpecificEmptyModel(FlexoProject project, String filename, String modelUri,
				FlexoMetaModelResource<EMFModel, EMFMetaModel, ?> metaModelResource) {
			return getModelSlotTechnologyAdapter().createNewEMFModel(project, filename, modelUri, (EMFMetaModelResource) metaModelResource);
		}

		@Override
		public EMFModelResource createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter, String relativePath, String filename,
				String modelUri, FlexoMetaModelResource<EMFModel, EMFMetaModel, ?> metaModelResource) {
			return getModelSlotTechnologyAdapter().createNewEMFModel((FileSystemBasedResourceCenter) resourceCenter, relativePath,
					filename, modelUri, (EMFMetaModelResource) metaModelResource);
		}

		@Override
		public boolean isStrictMetaModelling() {
			return true;
		}

	}
}
