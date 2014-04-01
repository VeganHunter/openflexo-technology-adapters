/*
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

package org.openflexo.technologyadapter.csv.model;

import java.util.List;
import java.io.File;

import org.openflexo.foundation.ontology.FlexoOntologyObjectImpl;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyIndividual;
import org.openflexo.foundation.ontology.IFlexoOntologyModel;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FlexoModel;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.metamodel.CSVMetaModel;

import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.rm.CSVModelResource;

public class CSVModel extends FlexoOntologyObjectImpl<CSVTechnologyAdapter>
implements FlexoModel<CSVModel, CSVMetaModel>, IFlexoOntologyModel<CSVTechnologyAdapter>
{
	private CSVModelResource csvResource;

	public CSVModel(String uri, File file, CSVTechnologyAdapter technologyAdapter)
	{
		super();
		// TODO Auto-generated method stub
	}

	@Override
	@Getter(value = "csvResource", ignoreType = true)
	public FlexoResource<CSVModel> getResource()
	{
		return this.csvResource;
	}

	@Override
	@Setter("csvResource")
	public void setResource(FlexoResource<CSVModel> resource)
	{
		this.csvResource = ((CSVModelResource)resource);
	}


	public void loadWhenUnloaded() {
		// TODO Auto-generated method stub
		
	}

		

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getImportedOntologies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAccessibleClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAccessibleIndividuals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAccessibleObjectProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAccessibleDataProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyConcept getDeclaredOntologyObject(String objectURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyClass getDeclaredClass(String classURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyIndividual getDeclaredIndividual(String individualURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyObjectProperty getDeclaredObjectProperty(
			String propertyURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyDataProperty getDeclaredDataProperty(String propertyURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyStructuralProperty getDeclaredProperty(String objectURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyClass getRootConcept() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List getSubContainers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getConcepts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getDataTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyConcept getOntologyObject(String objectURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyClass getClass(String classURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyIndividual getIndividual(String individualURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyObjectProperty getObjectProperty(String propertyURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyDataProperty getDataProperty(String propertyURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntologyStructuralProperty getProperty(String objectURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getIndividuals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getDataProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getObjectProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getMetaModels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSVMetaModel getMetaModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String objectURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSVTechnologyAdapter getTechnologyAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFlexoOntology getFlexoOntology() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayableDescription() {
		// TODO Auto-generated method stub
		return null;
	}

		
}