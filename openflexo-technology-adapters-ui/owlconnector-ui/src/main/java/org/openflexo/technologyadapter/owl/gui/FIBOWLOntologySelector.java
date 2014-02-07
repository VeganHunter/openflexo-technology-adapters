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
package org.openflexo.technologyadapter.owl.gui;

import java.io.File;
import java.util.logging.Logger;

import org.openflexo.components.widget.FIBFlexoObjectSelector;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntologyLibrary;
import org.openflexo.toolbox.FileResource;

/**
 * Widget allowing to select an OWL Ontology while browsing in OWL Ontology library
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBOWLOntologySelector extends FIBFlexoObjectSelector<OWLOntology> {

	static final Logger logger = Logger.getLogger(FIBOWLOntologySelector.class.getPackage().getName());

	public static FileResource FIB_FILE = new FileResource("Fib/FIBOWLOntologySelector.fib");

	public FIBOWLOntologySelector(OWLOntology editedObject) {
		super(editedObject);
	}

	/*@Override
	public void delete() {
		super.delete();
		ontologyLibrary = null;
	}*/

	@Override
	public File getFIBFile() {
		return FIB_FILE;
	}

	@Override
	public Class<OWLOntology> getRepresentedType() {
		return OWLOntology.class;
	}

	@Override
	public String renderedString(OWLOntology editedObject) {
		if (editedObject != null) {
			return editedObject.getName();
		}
		return "";
	}

	private OWLOntologyLibrary ontologyLibrary;

	public OWLOntologyLibrary getOntologyLibrary() {
		return ontologyLibrary;
	}

	@CustomComponentParameter(name = "ontologyLibrary", type = CustomComponentParameter.Type.MANDATORY)
	public void setOntologyLibrary(OWLOntologyLibrary ontologyLibrary) {
		this.ontologyLibrary = ontologyLibrary;
	}

	/**
	 * This method must be implemented if we want to implement completion<br>
	 * Completion will be performed on that selectable values<br>
	 * Return all viewpoints of this library
	 */
	/*@Override
	protected Enumeration<IFlexoOntology> getAllSelectableValues() {
		if (getOntologyLibrary() != null) {
			Vector<IFlexoOntology> allOntologies = new Vector<IFlexoOntology>(getOntologyLibrary().getAllOntologies());
			return allOntologies.elements();
		}
		return null;
	}*/

	// Please uncomment this for a live test
	// Never commit this uncommented since it will not compile on continuous build
	// To have icon, you need to choose "Test interface" in the editor (otherwise, flexo controller is not insanciated in EDIT mode)
	/*public static void main(String[] args) {
		FIBAbstractEditor editor = new FIBAbstractEditor() {
			@Override
			public Object[] getData() {
				FlexoResourceCenter resourceCenter = FlexoResourceCenterService.instance().getFlexoResourceCenter();
				FIBOntologySelector selector = new FIBOntologySelector(null);
				selector.setOntologyLibrary(resourceCenter.retrieveBaseOntologyLibrary());
				return makeArray(selector);
			}

			@Override
			public File getFIBFile() {
				return FIB_FILE;
			}

			@Override
			public FIBController makeNewController(FIBComponent component) {
				return new FlexoFIBController<FIBViewPointSelector>(component);
			}
		};
		editor.launch();
	}*/

}