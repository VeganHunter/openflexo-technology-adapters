/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Openflexo-technologyadapter-archetype, a component of the software infrastructure 
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


package org.openflexo.technologyadapter.csv.virtualmodel.action;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.editionaction.AddIndividual;
import org.openflexo.foundation.fml.editionaction.DataPropertyAssertion;
import org.openflexo.foundation.fml.editionaction.ObjectPropertyAssertion;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.csv.CSVTypeAwareModelSlot;
import org.openflexo.technologyadapter.csv.metamodel.CSVMetaModel;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.technologyadapter.csv.model.CSVObjectIndividual;

/**
 * Create CSV Object.
 * 
 * @author gbesancon
 * 
 */

@FIBPanel("Fib/AddCSVObjectIndividual.fib")
@ModelEntity
@ImplementationClass(AddCSVObjectIndividual.AddCSVObjectIndividualImpl.class)
@XMLElement
public interface AddCSVObjectIndividual extends AddIndividual<CSVTypeAwareModelSlot, CSVObjectIndividual> {

	public static abstract class AddCSVObjectIndividualImpl extends AddIndividualImpl<CSVTypeAwareModelSlot, CSVObjectIndividual> implements
			AddCSVObjectIndividual {

		private static final Logger logger = Logger.getLogger(AddCSVObjectIndividual.class.getPackage().getName());

		public AddCSVObjectIndividualImpl() {
			super();
		}

		@Override
		public Class<CSVObjectIndividual> getOntologyIndividualClass() {
			return CSVObjectIndividual.class;
		}

		@Override
		public CSVObjectIndividual execute(FlexoBehaviourAction action) {
			CSVObjectIndividual result = null;
			// TODO : Implement Action
			return result;
		}

	}
}