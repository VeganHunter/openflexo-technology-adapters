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
package org.openflexo.technologyadapter.diagram.fml.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.FlexoConceptInstancePatternRole;
import org.openflexo.foundation.viewpoint.IndividualPatternRole;
import org.openflexo.foundation.viewpoint.URIParameter;
import org.openflexo.foundation.viewpoint.VirtualModelModelSlot;
import org.openflexo.foundation.viewpoint.editionaction.AddIndividual;
import org.openflexo.foundation.viewpoint.editionaction.DeclarePatternRole;
import org.openflexo.foundation.viewpoint.inspector.FlexoConceptInspector;
import org.openflexo.technologyadapter.diagram.fml.ConnectorPatternRole;
import org.openflexo.technologyadapter.diagram.fml.DiagramEditionScheme;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapePatternRole;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

/**
 * This class represents an abstraction for a declare shape in flexo concept action among several kind of shapes.</br>
 * 
 * 
 * @author Vincent
 * 
 * @param <T1>
 */
public class DeclareConnectorInFlexoConcept extends DeclareInFlexoConcept<DeclareConnectorInFlexoConcept, DiagramConnector> {

	private static final Logger logger = Logger.getLogger(DeclareConnectorInFlexoConcept.class.getPackage().getName());

	public static FlexoActionType<DeclareConnectorInFlexoConcept, DiagramConnector, DiagramElement<?>> actionType = new FlexoActionType<DeclareConnectorInFlexoConcept, DiagramConnector, DiagramElement<?>>(
			"declare_in_flexo_concept", FlexoActionType.defaultGroup, FlexoActionType.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DeclareConnectorInFlexoConcept makeNewAction(DiagramConnector focusedObject, Vector<DiagramElement<?>> globalSelection,
				FlexoEditor editor) {
			return new DeclareConnectorInFlexoConcept(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramConnector connector, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramConnector connector, Vector<DiagramElement<?>> globalSelection) {
			return connector != null /*&& connector.getDiagramSpecification().getFlexoConcepts().size() > 0*/;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeclareConnectorInFlexoConcept.actionType, DiagramConnector.class);
	}

	public static enum NewFlexoConceptChoices {
		MAP_SINGLE_INDIVIDUAL, MAP_OBJECT_PROPERTY, MAP_SINGLE_FLEXO_CONCEPT, BLANK_FLEXO_CONCEPT
	}

	public NewFlexoConceptChoices patternChoice = NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL;

	private String flexoConceptName;
	private IFlexoOntologyClass concept;
	private IFlexoOntologyObjectProperty objectProperty;
	private String individualPatternRoleName;
	private String connectorPatternRoleName;
	private String objectPropertyStatementPatternRoleName;
	private String virtualModelPatternRoleName;

	public FlexoConcept fromFlexoConcept;
	public FlexoConcept toFlexoConcept;

	private String linkSchemeName;

	private FlexoConcept newFlexoConcept;
	private FlexoConcept virtualModelConcept;
	private ConnectorPatternRole newConnectorPatternRole;

	// public Vector<PropertyEntry> propertyEntries = new Vector<PropertyEntry>();

	DeclareConnectorInFlexoConcept(DiagramConnector focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) {
		logger.info("Declare connector in flexo concept");
		if (isValid()) {
			switch (primaryChoice) {
			case CHOOSE_EXISTING_FLEXO_CONCEPT:
				if (getPatternRole() != null) {
					System.out.println("Connector representation updated");
					// getPatternRole().setGraphicalRepresentation(getFocusedObject().getGraphicalRepresentation());
					getPatternRole().updateGraphicalRepresentation(getFocusedObject().getGraphicalRepresentation());
				}
				break;
			case CREATES_FLEXO_CONCEPT:

				/*VirtualModel.VirtualModelBuilder builder = new VirtualModel.VirtualModelBuilder(getFocusedObject()
						.getDiagramSpecification().getViewPointLibrary(), getFocusedObject().getDiagramSpecification().getViewPoint(),
						getFocusedObject().getDiagramSpecification().getResource());*/

				// Create new flexo concept
				newFlexoConcept = getFactory().newFlexoConcept();
				newFlexoConcept.setName(getFlexoConceptName());

				// And add the newly created flexo concept
				getDiagramModelSlot().getVirtualModel().addToFlexoConcepts(newFlexoConcept);

				// Find best URI base candidate
				// PropertyEntry mainPropertyDescriptor = selectBestEntryForURIBaseName();

				// Create individual pattern role if required
				IndividualPatternRole individualPatternRole = null;
				if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
					if (isTypeAwareModelSlot()) {
						TypeAwareModelSlot ontologyModelSlot = (TypeAwareModelSlot) getModelSlot();
						individualPatternRole = ontologyModelSlot.makeIndividualPatternRole(getConcept());
						individualPatternRole.setPatternRoleName(getIndividualPatternRoleName());
						individualPatternRole.setOntologicType(getConcept());
						newFlexoConcept.addToPatternRoles(individualPatternRole);
						// newFlexoConcept.setPrimaryConceptRole(individualPatternRole);
					}
				}

				// Create an flexo concept pattern role if required
				FlexoConceptInstancePatternRole flexoConceptPatternRole = null;
				if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_FLEXO_CONCEPT) {
					if (isVirtualModelModelSlot()) {
						VirtualModelModelSlot virtualModelModelSlot = (VirtualModelModelSlot) getModelSlot();
						flexoConceptPatternRole = virtualModelModelSlot.makeFlexoConceptInstancePatternRole(getVirtualModelConcept());
						flexoConceptPatternRole.setPatternRoleName(getVirtualModelPatternRoleName());
						newFlexoConcept.addToPatternRoles(flexoConceptPatternRole);
					}
				}

				// Create individual pattern role if required
				/*ObjectPropertyStatementPatternRole objectPropertyStatementPatternRole = null;
				if (patternChoice == NewFlexoConceptChoices.MAP_OBJECT_PROPERTY) {
					objectPropertyStatementPatternRole = new ObjectPropertyStatementPatternRole(builder);
					objectPropertyStatementPatternRole.setPatternRoleName(getObjectPropertyStatementPatternRoleName());
					objectPropertyStatementPatternRole.setObjectProperty(getObjectProperty());
					newFlexoConcept.addToPatternRoles(objectPropertyStatementPatternRole);
					newFlexoConcept.setPrimaryConceptRole(objectPropertyStatementPatternRole);
				}*/

				// Create connector pattern role
				newConnectorPatternRole = getFactory().newInstance(ConnectorPatternRole.class);
				newConnectorPatternRole.setPatternRoleName(getConnectorPatternRoleName());
				/*if (mainPropertyDescriptor != null) {
					newConnectorPatternRole.setLabel(new DataBinding<String>(getIndividualPatternRoleName() + "."
							+ mainPropertyDescriptor.property.getName()));
				} else {*/
				newConnectorPatternRole.setReadOnlyLabel(true);
				newConnectorPatternRole.setLabel(new DataBinding<String>("\"label\""));
				newConnectorPatternRole.setExampleLabel(getFocusedObject().getGraphicalRepresentation().getText());
				// }
				// We clone here the GR (fixed unfocusable GR bug)
				newConnectorPatternRole.setGraphicalRepresentation((ConnectorGraphicalRepresentation) getFocusedObject()
						.getGraphicalRepresentation().clone());
				newFlexoConcept.addToPatternRoles(newConnectorPatternRole);
				// newFlexoConcept.setPrimaryRepresentationRole(newConnectorPatternRole);

				// Create other individual roles
				Vector<IndividualPatternRole> otherRoles = new Vector<IndividualPatternRole>();
				/*if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
					for (PropertyEntry e : propertyEntries) {
						if (e.selectEntry) {
							if (e.property instanceof IFlexoOntologyObjectProperty) {
								IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
								if (range instanceof IFlexoOntologyClass) {
									IndividualPatternRole newPatternRole = null; // new IndividualPatternRole(builder);
									newPatternRole.setPatternRoleName(e.property.getName());
									newPatternRole.setOntologicType((IFlexoOntologyClass) range);
									newFlexoConcept.addToPatternRoles(newPatternRole);
									otherRoles.add(newPatternRole);
								}
							}
						}
					}
				}*/

				// Create new link scheme
				LinkScheme newLinkScheme = getFactory().newInstance(LinkScheme.class);
				newLinkScheme.setName(getLinkSchemeName());
				newLinkScheme.setFromTargetFlexoConcept(fromFlexoConcept);
				newLinkScheme.setToTargetFlexoConcept(toFlexoConcept);

				// Parameters
				if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
					if (isTypeAwareModelSlot()) {
						TypeAwareModelSlot<?, ?> typeAwareModelSlot = (TypeAwareModelSlot<?, ?>) getModelSlot();
						/*Vector<PropertyEntry> candidates = new Vector<PropertyEntry>();
						for (PropertyEntry e : propertyEntries) {
							if (e.selectEntry) {
								EditionSchemeParameter newParameter = null;
								if (e.property instanceof IFlexoOntologyDataProperty) {
									switch (((IFlexoOntologyDataProperty) e.property).getRange().getBuiltInDataType()) {
									case Boolean:
										newParameter = new CheckboxParameter(builder);
										newParameter.setName(e.property.getName());
										newParameter.setLabel(e.label);
										break;
									case Byte:
									case Integer:
									case Long:
									case Short:
										newParameter = new IntegerParameter(builder);
										newParameter.setName(e.property.getName());
										newParameter.setLabel(e.label);
										break;
									case Double:
									case Float:
										newParameter = new FloatParameter(builder);
										newParameter.setName(e.property.getName());
										newParameter.setLabel(e.label);
										break;
									case String:
										newParameter = new TextFieldParameter(builder);
										newParameter.setName(e.property.getName());
										newParameter.setLabel(e.label);
										break;
									default:
										break;
									}
								} else if (e.property instanceof IFlexoOntologyObjectProperty) {
									IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
									if (range instanceof IFlexoOntologyClass) {
										newParameter = new IndividualParameter(builder);
										newParameter.setName(e.property.getName());
										newParameter.setLabel(e.label);
										((IndividualParameter) newParameter).setConcept((IFlexoOntologyClass) range);
									}
								}
								if (newParameter != null) {
									newLinkScheme.addToParameters(newParameter);
								}
							}
						}*/

						URIParameter uriParameter = getFactory().newURIParameter();
						uriParameter.setName("uri");
						uriParameter.setLabel("uri");
						/*if (mainPropertyDescriptor != null) {
							uriParameter.setBaseURI(new DataBinding<String>(mainPropertyDescriptor.property.getName()));
						}*/
						newLinkScheme.addToParameters(uriParameter);

						// Declare pattern role
						for (IndividualPatternRole r : otherRoles) {
							DeclarePatternRole action = getFactory().newDeclarePatternRole();
							action.setAssignation(new DataBinding<Object>(r.getPatternRoleName()));
							action.setObject(new DataBinding<Object>("parameters." + r.getName()));
							newLinkScheme.addToActions(action);
						}

						// Add individual action
						if (individualPatternRole != null) {
							AddIndividual newAddIndividual = typeAwareModelSlot.makeAddIndividualAction(individualPatternRole,
									newLinkScheme);
							newLinkScheme.addToActions(newAddIndividual);
						}

						// Add individual action
						/*AddIndividual newAddIndividual = new AddIndividual(builder);
						newAddIndividual.setAssignation(new ViewPointDataBinding(individualPatternRole.getPatternRoleName()));
						newAddIndividual.setIndividualName(new ViewPointDataBinding("parameters.uri"));
						for (PropertyEntry e : propertyEntries) {
							if (e.selectEntry) {
								if (e.property instanceof IFlexoOntologyObjectProperty) {
									IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
									if (range instanceof IFlexoOntologyClass) {
										ObjectPropertyAssertion propertyAssertion = new ObjectPropertyAssertion(builder);
										propertyAssertion.setOntologyProperty(e.property);
										propertyAssertion.setObject(new ViewPointDataBinding("parameters." + e.property.getName()));
										newAddIndividual.addToObjectAssertions(propertyAssertion);
									}
								} else if (e.property instanceof IFlexoOntologyDataProperty) {
									DataPropertyAssertion propertyAssertion = new DataPropertyAssertion(builder);
									propertyAssertion.setOntologyProperty(e.property);
									propertyAssertion.setValue(new ViewPointDataBinding("parameters." + e.property.getName()));
									newAddIndividual.addToDataAssertions(propertyAssertion);
								}
							}
						}
						newLinkScheme.addToActions(newAddIndividual);
						*/
					}
				}

				// Add connector action
				AddConnector newAddConnector = getFactory().newInstance(AddConnector.class);
				newAddConnector.setAssignation(new DataBinding<Object>(newConnectorPatternRole.getPatternRoleName()));
				ShapePatternRole fromPatternRole = fromFlexoConcept.getPatternRoles(ShapePatternRole.class).get(0);
				ShapePatternRole toPatternRole = toFlexoConcept.getPatternRoles(ShapePatternRole.class).get(0);

				newAddConnector.setFromShape(new DataBinding<DiagramShape>(DiagramEditionScheme.FROM_TARGET + "."
						+ fromPatternRole.getPatternRoleName()));
				newAddConnector.setToShape(new DataBinding<DiagramShape>(DiagramEditionScheme.TO_TARGET + "."
						+ toPatternRole.getPatternRoleName()));

				newLinkScheme.addToActions(newAddConnector);

				// Add new drop scheme
				newFlexoConcept.addToEditionSchemes(newLinkScheme);

				// Add inspector
				FlexoConceptInspector inspector = newFlexoConcept.getInspector();
				inspector.setInspectorTitle(getFlexoConceptName());
				if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
					/*for (PropertyEntry e : propertyEntries) {
						if (e.selectEntry) {
							if (e.property instanceof IFlexoOntologyObjectProperty) {
								IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
								if (range instanceof IFlexoOntologyClass) {
									InspectorEntry newInspectorEntry = null;
									newInspectorEntry = new TextFieldInspectorEntry(builder);
									newInspectorEntry.setName(e.property.getName());
									newInspectorEntry.setLabel(e.label);
									newInspectorEntry.setIsReadOnly(true);
									newInspectorEntry.setData(new DataBinding<Object>(e.property.getName() + ".uriName"));
									inspector.addToEntries(newInspectorEntry);
								}
							} else if (e.property instanceof IFlexoOntologyDataProperty) {
								InspectorEntry newInspectorEntry = null;
								switch (((IFlexoOntologyDataProperty) e.property).getRange().getBuiltInDataType()) {
								case Boolean:
									newInspectorEntry = new CheckboxInspectorEntry(builder);
									break;
								case Byte:
								case Integer:
								case Long:
								case Short:
									newInspectorEntry = new IntegerInspectorEntry(builder);
									break;
								case Double:
								case Float:
									newInspectorEntry = new FloatInspectorEntry(builder);
									break;
								case String:
									newInspectorEntry = new TextFieldInspectorEntry(builder);
									break;
								default:
									logger.warning("Not handled: " + ((IFlexoOntologyDataProperty) e.property).getRange());
								}
								if (newInspectorEntry != null) {
									newInspectorEntry.setName(e.property.getName());
									newInspectorEntry.setLabel(e.label);
									newInspectorEntry.setData(new DataBinding<Object>(getIndividualPatternRoleName() + "."
											+ e.property.getName()));
									inspector.addToEntries(newInspectorEntry);
								}
							}
						}
					}*/
				}

			default:
				logger.warning("Pattern not implemented");
			}
		} else {
			logger.warning("Focused role is null !");
		}
	}

	@Override
	public boolean isValid() {
		if (getFocusedObject() == null) {
			return false;
		}
		switch (primaryChoice) {
		case CHOOSE_EXISTING_FLEXO_CONCEPT:
			return getFlexoConcept() != null && getPatternRole() != null;
		case CREATES_FLEXO_CONCEPT:
			switch (patternChoice) {
			case MAP_SINGLE_INDIVIDUAL:
				return StringUtils.isNotEmpty(getFlexoConceptName()) && concept != null
						&& StringUtils.isNotEmpty(getIndividualPatternRoleName()) && StringUtils.isNotEmpty(getConnectorPatternRoleName())
						&& fromFlexoConcept != null && toFlexoConcept != null && StringUtils.isNotEmpty(getLinkSchemeName());
			case MAP_OBJECT_PROPERTY:
				return StringUtils.isNotEmpty(getFlexoConceptName()) && objectProperty != null
						&& StringUtils.isNotEmpty(getObjectPropertyStatementPatternRoleName())
						&& StringUtils.isNotEmpty(getConnectorPatternRoleName()) && fromFlexoConcept != null && toFlexoConcept != null
						&& StringUtils.isNotEmpty(getLinkSchemeName());
			case MAP_SINGLE_FLEXO_CONCEPT:
				return StringUtils.isNotEmpty(getFlexoConceptName()) && virtualModelConcept != null
						&& StringUtils.isNotEmpty(getVirtualModelPatternRoleName()) && getSelectedEntriesCount() > 0
						&& fromFlexoConcept != null && toFlexoConcept != null && StringUtils.isNotEmpty(getLinkSchemeName());
			case BLANK_FLEXO_CONCEPT:
				return StringUtils.isNotEmpty(getFlexoConceptName()) && StringUtils.isNotEmpty(getConnectorPatternRoleName())
						&& fromFlexoConcept != null && toFlexoConcept != null && StringUtils.isNotEmpty(getLinkSchemeName());
			default:
				break;
			}
		default:
			return false;
		}
	}

	private ConnectorPatternRole patternRole;

	@Override
	public ConnectorPatternRole getPatternRole() {
		if (primaryChoice == DeclareInFlexoConceptChoices.CREATES_FLEXO_CONCEPT) {
			return newConnectorPatternRole;
		}
		return patternRole;
	}

	public void setPatternRole(ConnectorPatternRole patternRole) {
		this.patternRole = patternRole;
	}

	@Override
	public void resetPatternRole() {
		this.patternRole = null;
	}

	public IFlexoOntologyClass getConcept() {
		return concept;
	}

	public void setConcept(IFlexoOntologyClass concept) {
		this.concept = concept;
		// propertyEntries.clear();
		// IFlexoOntology ownerOntology = concept.getOntology();
		/*for (IFlexoOntologyFeature p : concept.getPropertiesTakingMySelfAsDomain()) {
			if (p.getOntology() == ownerOntology && p instanceof IFlexoOntologyStructuralProperty) {
				PropertyEntry newEntry = new PropertyEntry((IFlexoOntologyStructuralProperty) p);
				propertyEntries.add(newEntry);
			}
		}*/
	}

	public FlexoConcept getVirtualModelConcept() {
		return virtualModelConcept;
	}

	public void setVirtualModelConcept(FlexoConcept virtualModelConcept) {
		this.virtualModelConcept = virtualModelConcept;
	}

	public IFlexoOntologyObjectProperty getObjectProperty() {
		return objectProperty;
	}

	public void setObjectProperty(IFlexoOntologyObjectProperty property) {
		this.objectProperty = property;
	}

	public String getFlexoConceptName() {
		if (isTypeAwareModelSlot()) {
			if (StringUtils.isEmpty(flexoConceptName) && concept != null) {
				return concept.getName();
			}
			if (StringUtils.isEmpty(flexoConceptName) && objectProperty != null) {
				return objectProperty.getName();
			}
		}
		if (isVirtualModelModelSlot()) {
			if (StringUtils.isEmpty(flexoConceptName) && virtualModelConcept != null) {
				return virtualModelConcept.getName();
			}
		}

		return flexoConceptName;
	}

	public void setFlexoConceptName(String flexoConceptName) {
		this.flexoConceptName = flexoConceptName;
	}

	public String getIndividualPatternRoleName() {
		if (StringUtils.isEmpty(individualPatternRoleName) && concept != null) {
			return JavaUtils.getVariableName(concept.getName());
		}
		return individualPatternRoleName;
	}

	public void setIndividualPatternRoleName(String individualPatternRoleName) {
		this.individualPatternRoleName = individualPatternRoleName;
	}

	public String getVirtualModelPatternRoleName() {
		if (StringUtils.isEmpty(virtualModelPatternRoleName) && virtualModelConcept != null) {
			return JavaUtils.getVariableName(virtualModelConcept.getName());
		}
		return virtualModelPatternRoleName;
	}

	public void setVirtualModelPatternRoleName(String virtualModelPatternRoleName) {
		this.virtualModelPatternRoleName = virtualModelPatternRoleName;
	}

	public String getObjectPropertyStatementPatternRoleName() {
		if (StringUtils.isEmpty(objectPropertyStatementPatternRoleName) && objectProperty != null) {
			return JavaUtils.getVariableName(objectProperty.getName()) + "Statement";
		}
		return objectPropertyStatementPatternRoleName;
	}

	public void setObjectPropertyStatementPatternRoleName(String objectPropertyStatementPatternRoleName) {
		this.objectPropertyStatementPatternRoleName = objectPropertyStatementPatternRoleName;
	}

	public String getConnectorPatternRoleName() {
		if (StringUtils.isEmpty(connectorPatternRoleName)) {
			return "connector";
		}
		return connectorPatternRoleName;
	}

	public void setConnectorPatternRoleName(String connectorPatternRoleName) {
		this.connectorPatternRoleName = connectorPatternRoleName;
	}

	public String getLinkSchemeName() {
		if (StringUtils.isEmpty(linkSchemeName)) {
			return "link" + (fromFlexoConcept != null ? fromFlexoConcept.getName() : "") + "To"
					+ (toFlexoConcept != null ? toFlexoConcept.getName() : "");
		}
		return linkSchemeName;
	}

	public void setLinkSchemeName(String linkSchemeName) {
		this.linkSchemeName = linkSchemeName;
	}

	@Override
	public FlexoConcept getFlexoConcept() {
		if (primaryChoice == DeclareInFlexoConceptChoices.CREATES_FLEXO_CONCEPT) {
			return newFlexoConcept;
		}
		return super.getFlexoConcept();
	};

	/*public class PropertyEntry {

		public IFlexoOntologyStructuralProperty property;
		public String label;
		public boolean selectEntry = true;

		public PropertyEntry(IFlexoOntologyStructuralProperty property) {
			this.property = property;
			if (StringUtils.isNotEmpty(property.getDescription())) {
				label = property.getDescription();
			} else {
				label = property.getName() + "_of_" + getIndividualPatternRoleName();
			}
		}

		public String getRange() {
			return property.getRange().getName();
		}
	}*/

	/*private PropertyEntry selectBestEntryForURIBaseName() {
		Vector<PropertyEntry> candidates = new Vector<PropertyEntry>();
		for (PropertyEntry e : propertyEntries) {
			if (e.selectEntry && e.property instanceof IFlexoOntologyDataProperty
					&& ((IFlexoOntologyDataProperty) e.property).getRange().getBuiltInDataType() == BuiltInDataType.String) {
				candidates.add(e);
			}
		}
		if (candidates.size() > 0) {
			return candidates.firstElement();
		}
		return null;
	}

	public PropertyEntry createPropertyEntry() {
		PropertyEntry newPropertyEntry = new PropertyEntry(null);
		propertyEntries.add(newPropertyEntry);
		return newPropertyEntry;
	}

	public PropertyEntry deletePropertyEntry(PropertyEntry aPropertyEntry) {
		propertyEntries.remove(aPropertyEntry);
		return aPropertyEntry;
	}

	public void selectAllProperties() {
		for (PropertyEntry e : propertyEntries) {
			e.selectEntry = true;
		}
	}

	public void selectNoneProperties() {
		for (PropertyEntry e : propertyEntries) {
			e.selectEntry = false;
		}
	}*/

}