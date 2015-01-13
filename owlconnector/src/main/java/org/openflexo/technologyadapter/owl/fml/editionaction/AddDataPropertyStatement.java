/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.technologyadapter.owl.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.binding.DataBinding.BindingDefinitionType;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FMLRepresentationContext.FMLRepresentationOutput;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.SetDataPropertyValueAction;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.ontology.IndividualOfClass;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.validation.FixProposal;
import org.openflexo.model.validation.ValidationError;
import org.openflexo.model.validation.ValidationIssue;
import org.openflexo.model.validation.ValidationRule;
import org.openflexo.technologyadapter.owl.fml.DataPropertyStatementRole;
import org.openflexo.technologyadapter.owl.model.DataPropertyStatement;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;
import org.openflexo.toolbox.StringUtils;

@FIBPanel("Fib/AddDataPropertyStatementPanel.fib")
@ModelEntity
@ImplementationClass(AddDataPropertyStatement.AddDataPropertyStatementImpl.class)
@XMLElement
public interface AddDataPropertyStatement extends AddStatement<DataPropertyStatement>, SetDataPropertyValueAction<DataPropertyStatement> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String VALUE_KEY = "value";
	@PropertyIdentifier(type = String.class)
	public static final String DATA_PROPERTY_URI_KEY = "dataPropertyURI";

	@Override
	@Getter(value = VALUE_KEY)
	@XMLAttribute
	public DataBinding<?> getValue();

	@Override
	@Setter(VALUE_KEY)
	public void setValue(DataBinding<?> value);

	@Getter(value = DATA_PROPERTY_URI_KEY)
	@XMLAttribute
	public String _getDataPropertyURI();

	@Setter(DATA_PROPERTY_URI_KEY)
	public void _setDataPropertyURI(String dataPropertyURI);

	public static abstract class AddDataPropertyStatementImpl extends AddStatementImpl<DataPropertyStatement> implements
			AddDataPropertyStatement {

		private static final Logger logger = Logger.getLogger(AddDataPropertyStatement.class.getPackage().getName());

		private String dataPropertyURI = null;
		private DataBinding<?> value;

		public AddDataPropertyStatementImpl() {
			super();
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			out.append((getSubject() != null ? getSubject().toString() : "null") + "."
					+ (getDataProperty() != null ? getDataProperty().getName() : "null") + " = " + getValue().toString() + ";", context);
			return out.toString();
		}

		@Override
		public Type getSubjectType() {
			if (getDataProperty() != null && getDataProperty().getDomain() instanceof IFlexoOntologyClass) {
				return IndividualOfClass.getIndividualOfClass((IFlexoOntologyClass) getDataProperty().getDomain());
			}
			return super.getSubjectType();
		}

		/*@Override
		public List<DataPropertyStatementRole> getAvailablePatternRoles() {
			return getFlexoConcept().getPatternRoles(DataPropertyStatementRole.class);
		}*/

		@Override
		public DataPropertyStatementRole getFlexoRole() {
			FlexoRole superFlexoRole = super.getFlexoRole();
			if (superFlexoRole instanceof DataPropertyStatementRole) {
				return (DataPropertyStatementRole) superFlexoRole;
			} else if (superFlexoRole != null) {
				// logger.warning("Unexpected pattern role of type " + superPatternRole.getClass().getSimpleName());
				return null;
			}
			return null;
		}

		@Override
		public IFlexoOntologyStructuralProperty getProperty() {
			return getDataProperty();
		}

		@Override
		public void setProperty(IFlexoOntologyStructuralProperty aProperty) {
			setDataProperty((OWLDataProperty) aProperty);
		}

		@Override
		public OWLDataProperty getDataProperty() {
			if (getOwningVirtualModel() != null && StringUtils.isNotEmpty(dataPropertyURI)) {
				return (OWLDataProperty) getOwningVirtualModel().getOntologyDataProperty(dataPropertyURI);
			} else {
				if (getFlexoRole() != null) {
					return getFlexoRole().getDataProperty();
				}
			}
			return null;
		}

		@Override
		public void setDataProperty(IFlexoOntologyDataProperty ontologyProperty) {
			if (ontologyProperty != null) {
				if (getFlexoRole() != null) {
					if (getFlexoRole().getDataProperty().isSuperConceptOf(ontologyProperty)) {
						dataPropertyURI = ontologyProperty.getURI();
					} else {
						getFlexoRole().setDataProperty((OWLDataProperty) ontologyProperty);
					}
				} else {
					dataPropertyURI = ontologyProperty.getURI();
				}
			} else {
				dataPropertyURI = null;
			}
		}

		@Override
		public String _getDataPropertyURI() {
			if (getDataProperty() != null) {
				if (getFlexoRole() != null && getFlexoRole().getDataProperty() == getDataProperty()) {
					// No need to store an overriding type, just use default provided by pattern role
					return null;
				}
				return getDataProperty().getURI();
			}
			return dataPropertyURI;
		}

		@Override
		public void _setDataPropertyURI(String dataPropertyURI) {
			this.dataPropertyURI = dataPropertyURI;
		}

		public Object getValue(FlexoBehaviourAction action) {
			try {
				return getValue().getBindingValue(action);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		public Type getType() {
			if (getDataProperty() != null) {
				return getDataProperty().getRange().getAccessedType();
			}
			return Object.class;
		};

		@Override
		public DataBinding<?> getValue() {
			if (value == null) {
				value = new DataBinding<Object>(this, getType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getType();
					}
				};
				value.setBindingName("value");
			}
			return value;
		}

		@Override
		public void setValue(DataBinding<?> value) {
			if (value != null) {
				value = new DataBinding<Object>(value.toString(), this, getType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getType();
					}
				};
				value.setBindingName("value");
			}
			this.value = value;
		}

		@Override
		public Type getAssignableType() {
			if (getDataProperty() == null) {
				return DataPropertyStatement.class;
			}
			return StatementWithProperty.getStatementWithProperty(getDataProperty());
		}

		@Override
		public String getStringRepresentation() {
			if (getSubject() == null || getDataProperty() == null || getValue() == null) {
				return "Add DataPropertyStatement";
			}
			return getSubject() + " " + (getDataProperty() != null ? getDataProperty().getName() : "null") + " " + getValue();
		}

		@Override
		public DataPropertyStatement execute(FlexoBehaviourAction<?, ?, ?> action) {
			OWLDataProperty property = getDataProperty();
			OWLConcept<?> subject = getPropertySubject(action);
			Object value = getValue(action);
			if (property == null) {
				return null;
			}
			if (subject == null) {
				return null;
			}
			if (value == null) {
				return null;
			}
			return subject.addDataPropertyStatement(property, value);
		}

	}

	public static class AddDataPropertyStatementActionMustDefineADataProperty extends
			ValidationRule<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement> {
		public AddDataPropertyStatementActionMustDefineADataProperty() {
			super(AddDataPropertyStatement.class, "add_data_property_statement_action_must_define_a_data_property");
		}

		@Override
		public ValidationIssue<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement> applyValidation(
				AddDataPropertyStatement action) {
			if (action.getDataProperty() == null && action.getOwner() instanceof AssignationAction) {
				Vector<FixProposal<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement>> v = new Vector<FixProposal<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement>>();
				for (DataPropertyStatementRole pr : action.getFlexoConcept().getFlexoRoles(DataPropertyStatementRole.class)) {
					v.add(new SetsFlexoRole(pr));
				}
				return new ValidationError<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement>(this, action,
						"add_data_property_statement_action_does_not_define_an_data_property", v);
			}
			return null;
		}

		protected static class SetsFlexoRole extends
				FixProposal<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement> {

			private final DataPropertyStatementRole flexoRole;

			public SetsFlexoRole(DataPropertyStatementRole flexoRole) {
				super("assign_action_to_flexo_role_($flexoRole.flexoRoleName)");
				this.flexoRole = flexoRole;
			}

			public DataPropertyStatementRole getFlexoRole() {
				return flexoRole;
			}

			@Override
			protected void fixAction() {
				AddDataPropertyStatement action = getValidable();
				((AssignationAction) action.getOwner()).setAssignation(new DataBinding<Object>(flexoRole.getRoleName()));
			}

		}
	}

	public static class ValueIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddDataPropertyStatement> {
		public ValueIsRequiredAndMustBeValid() {
			super("'value'_binding_is_required_and_must_be_valid", AddDataPropertyStatement.class);
		}

		@Override
		public DataBinding<?> getBinding(AddDataPropertyStatement object) {
			return object.getValue();
		}

	}

}