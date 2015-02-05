/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Powerpointconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.powerpoint.BasicPowerpointModelSlot;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlide;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;

@FIBPanel("Fib/AddPowerpointSlidePanel.fib")
@ModelEntity
@ImplementationClass(AddPowerpointSlide.AddPowerpointSlideImpl.class)
@XMLElement
@FML("AddPowerpointSlide")
public interface AddPowerpointSlide extends PowerpointAction<PowerpointSlide> {

	public static abstract class AddPowerpointSlideImpl extends TechnologySpecificActionImpl<BasicPowerpointModelSlot, PowerpointSlide>
			implements AddPowerpointSlide {

		private static final Logger logger = Logger.getLogger(AddPowerpointSlide.class.getPackage().getName());

		private DataBinding<Integer> slideIndex;

		@Override
		public Type getAssignableType() {
			return PowerpointSlide.class;
		}

		@Override
		public PowerpointSlide execute(FlexoBehaviourAction action) {

			PowerpointSlide result = null;

			FreeModelSlotInstance<PowerpointSlideshow, BasicPowerpointModelSlot> modelSlotInstance = getModelSlotInstance(action);
			if (modelSlotInstance.getResourceData() != null) {
				try {
					SlideShow ss = modelSlotInstance.getAccessedResourceData().getSlideShow();
					Slide slide = null;
					if (ss != null) {
						slide = ss.createSlide();
						Integer slideIndex = getSlideIndex().getBindingValue(action);
						if (slideIndex != null) {
							slide.setSlideNumber(slideIndex);
						}

						// Instanciate Wrapper.
						result = modelSlotInstance.getAccessedResourceData().getConverter()
								.convertPowerpointSlideToSlide(slide, modelSlotInstance.getAccessedResourceData(), null);
						modelSlotInstance.getAccessedResourceData().addToPowerpointSlides(result);
						modelSlotInstance.getAccessedResourceData().setIsModified();
					} else {
						logger.warning("Create a sheet requires a workbook");
					}
				} catch (TypeMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullReferenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.warning("Model slot not correctly initialised : model is null");
				return null;
			}

			return result;
		}

		public DataBinding<Integer> getSlideIndex() {
			if (slideIndex == null) {
				slideIndex = new DataBinding<Integer>(this, Integer.class, DataBinding.BindingDefinitionType.GET);
				slideIndex.setBindingName("slideIndex");
			}
			return slideIndex;
		}

		public void setSlideIndex(DataBinding<Integer> slideIndex) {
			if (slideIndex != null) {
				slideIndex.setOwner(this);
				slideIndex.setDeclaredType(Integer.class);
				slideIndex.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				slideIndex.setBindingName("slideIndex");
			}
			this.slideIndex = slideIndex;
		}

		@Override
		public FreeModelSlotInstance<PowerpointSlideshow, BasicPowerpointModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (FreeModelSlotInstance<PowerpointSlideshow, BasicPowerpointModelSlot>) super.getModelSlotInstance(action);
		}
	}

}
