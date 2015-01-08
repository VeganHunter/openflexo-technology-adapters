package org.openflexo.technologyadapter.diagram.fml;

import org.openflexo.antar.binding.BindingModel;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.fml.AbstractVirtualModel;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModelObject;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;

@ModelEntity(isAbstract = true)
@ImplementationClass(OverridingGraphicalRepresentation.OverridingGraphicalRepresentationImpl.class)
@Imports({ @Import(OverridingGraphicalRepresentation.ShapeOverridingGraphicalRepresentation.class),
		@Import(OverridingGraphicalRepresentation.ConnectorOverridingGraphicalRepresentation.class) })
public interface OverridingGraphicalRepresentation<GR extends GraphicalRepresentation> extends VirtualModelObject {

	@PropertyIdentifier(type = FMLDiagramPaletteElementBinding.class)
	public static final String PALETTE_ELEMENT_BINDING_KEY = "paletteElementBinding";
	@PropertyIdentifier(type = GraphicalElementRole.class)
	public static final String PATTERN_ROLE_KEY = "patternRole";
	@PropertyIdentifier(type = GraphicalRepresentation.class)
	public static final String GRAPHICAL_REPRESENTATION_KEY = "graphicalRepresentation";

	// TODO: remove inverse
	@Getter(value = PALETTE_ELEMENT_BINDING_KEY, inverse = FMLDiagramPaletteElementBinding.OVERRIDING_GRAPHICAL_REPRESENTATIONS_KEY)
	public FMLDiagramPaletteElementBinding getDiagramPaletteElementBinding();

	@Setter(PALETTE_ELEMENT_BINDING_KEY)
	public void setDiagramPaletteElementBinding(FMLDiagramPaletteElementBinding diagramPaletteElementBinding);

	@Getter(value = PATTERN_ROLE_KEY)
	@XMLAttribute
	public GraphicalElementRole<?, GR> getPatternRole();

	@Setter(PATTERN_ROLE_KEY)
	public void setPatternRole(GraphicalElementRole<?, GR> aPatternRole);

	@Getter(value = GRAPHICAL_REPRESENTATION_KEY)
	@XMLElement
	public GR getGraphicalRepresentation();

	@Setter(GRAPHICAL_REPRESENTATION_KEY)
	public void setGraphicalRepresentation(GR graphicalRepresentation);

	public abstract class OverridingGraphicalRepresentationImpl<GR extends GraphicalRepresentation> extends FlexoConceptObjectImpl
			implements OverridingGraphicalRepresentation<GR> {

		// FMLDiagramPaletteElementBinding paletteElementBinding;
		// private String patternRoleName;

		// Do not use, required for deserialization
		public OverridingGraphicalRepresentationImpl() {
			super();
		}

		// Do not use, required for deserialization
		public OverridingGraphicalRepresentationImpl(GraphicalElementRole<?, GR> patternRole) {
			super();
			setPatternRole(patternRole);
			// patternRoleName = patternRole.getPatternRoleName();
		}

		@Override
		public FlexoConcept getFlexoConcept() {
			return getDiagramPaletteElementBinding().getFlexoConcept();
		}

		@Override
		public AbstractVirtualModel<?> getVirtualModel() {
			return getDiagramPaletteElementBinding().getVirtualModel();
		}

		@Override
		public BindingModel getBindingModel() {
			if (getDiagramPaletteElementBinding() != null) {
				return getDiagramPaletteElementBinding().getBindingModel();
			}
			return null;
		}

		/*public FMLDiagramPaletteElementBinding getPaletteElementBinding() {
			return paletteElementBinding;
		}*/

		/*@Override
		public String getPatternRoleName() {
			return patternRoleName;
		}

		@Override
		public void setPatternRoleName(String patternRoleName) {
			this.patternRoleName = patternRoleName;
		}*/

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			return "<not_implemented:" + getStringRepresentation() + ">";
		}

		public DiagramPalette getPalette() {
			return getDiagramPaletteElementBinding().getPaletteElement().getPalette();
		}

		@Override
		public String getURI() {
			return null;
		}

	}

	@ModelEntity
	@XMLElement
	public static interface ShapeOverridingGraphicalRepresentation extends OverridingGraphicalRepresentation<ShapeGraphicalRepresentation> {
	}

	@ModelEntity
	@XMLElement
	public static interface ConnectorOverridingGraphicalRepresentation extends
			OverridingGraphicalRepresentation<ConnectorGraphicalRepresentation> {
	}

}