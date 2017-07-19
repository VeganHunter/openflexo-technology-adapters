/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.gina.fml.editionaction;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.editionaction.TechnologySpecificAction;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext.ReturnException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlotInstanceConfiguration.FIBComponentModelSlotInstanceConfigurationOption;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;

/**
 * {@link EditionAction} used to configure a {@link FIBComponentModelSlot}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(ConfigureGINAFIBComponent.ConfigureGINAFIBComponentImpl.class)
@XMLElement
@FML("ConfigureGINAFIBComponent")
public interface ConfigureGINAFIBComponent extends TechnologySpecificAction<FIBComponentModelSlot, GINAFIBComponent, GINAFIBComponent> {

	public static abstract class ConfigureGINAFIBComponentImpl extends
			TechnologySpecificActionImpl<FIBComponentModelSlot, GINAFIBComponent, GINAFIBComponent> implements ConfigureGINAFIBComponent {

		private static final Logger logger = Logger.getLogger(ConfigureGINAFIBComponentImpl.class.getPackage().getName());

		@Override
		public Type getAssignableType() {
			return GINAFIBComponent.class;
		}

		@Override
		public GINAFIBComponent execute(RunTimeEvaluationContext evaluationContext) throws ReturnException, FlexoException {

			// System.out.println("ConfigureGINAFIBComponent for :" + getAssignedFlexoProperty());

			if (getAssignedFlexoProperty() instanceof FIBComponentModelSlot) {

				FIBComponentModelSlot modelSlot = ((FIBComponentModelSlot) getAssignedFlexoProperty());

				// System.out.println("Le template: " + modelSlot.getTemplateResource());

				// System.out.println("FCI=" + evaluationContext.getFlexoConceptInstance());
				// System.out.println("VMI=" + evaluationContext.getVirtualModelInstance());

				FlexoConceptInstance fci = evaluationContext.getFlexoConceptInstance();
				ModelSlotInstance<FIBComponentModelSlot, GINAFIBComponent> msi = fci.getModelSlotInstance(modelSlot);
				if (msi == null) {
					FIBComponentModelSlotInstanceConfiguration msiConfig = (FIBComponentModelSlotInstanceConfiguration) modelSlot
							.createConfiguration(fci, fci.getResourceCenter());
					msiConfig.setOption(FIBComponentModelSlotInstanceConfigurationOption.ReadOnlyUseFIBComponent);
					msi = msiConfig.createModelSlotInstance(fci, fci.getVirtualModelInstance());
					fci.addToActors(msi);
					// System.out.println("Hop, on vient de creer le msi " + msi);
				}

				if (modelSlot.getTemplateResource() == null) {
					logger.warning("No template defined for " + modelSlot);
					return null;
				}

				try {
					return modelSlot.getTemplateResource().getResourceData(null);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;

		}
	}
}
