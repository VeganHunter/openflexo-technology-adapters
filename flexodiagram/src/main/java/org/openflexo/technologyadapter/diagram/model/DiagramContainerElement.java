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

package org.openflexo.technologyadapter.diagram.model;

import java.util.List;

import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PastingPoint;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;

/**
 * Implements containment in Openflexo Diagram built-in technology: a container may contains some shapes and connectors
 * 
 * @author sylvain
 * 
 * @param <G>
 *            type of underlying graphical representation (sub-class of {@link GraphicalRepresentation})
 */
@ModelEntity
@ImplementationClass(DiagramContainerElementImpl.class)
public interface DiagramContainerElement<G extends GraphicalRepresentation> extends DiagramElement<G> {

	public static final String SHAPES = "shapes";
	public static final String CONNECTORS = "connectors";

	/**
	 * Return the list of shapes contained in this container
	 * 
	 * @return
	 */
	@Getter(value = SHAPES, cardinality = Cardinality.LIST, inverse = DiagramElement.PARENT)
	@XMLElement(primary = true)
	@CloningStrategy(StrategyType.CLONE)
	@Embedded
	public List<DiagramShape> getShapes();

	@Setter(SHAPES)
	public void setShapes(List<DiagramShape> someShapes);

	@Adder(SHAPES)
	@PastingPoint
	public void addToShapes(DiagramShape aShape);

	@Remover(SHAPES)
	public void removeFromShapes(DiagramShape aShape);

	/**
	 * Return the list of connectors contained in this container
	 * 
	 * @return
	 */
	@Getter(value = CONNECTORS, cardinality = Cardinality.LIST, inverse = DiagramElement.PARENT)
	@XMLElement(primary = true)
	@CloningStrategy(StrategyType.CLONE)
	@Embedded
	public List<DiagramConnector> getConnectors();

	@Setter(CONNECTORS)
	public void setConnectors(List<DiagramConnector> someConnectors);

	@Adder(CONNECTORS)
	@PastingPoint
	public void addToConnectors(DiagramConnector aConnector);

	@Remover(CONNECTORS)
	public void removeFromConnectors(DiagramConnector aConnector);

	/**
	 * Return all descendants of this {@link DiagramElement} (recursive method)
	 * 
	 * @return
	 */
	public List<DiagramElement<?>> getDescendants();

}
