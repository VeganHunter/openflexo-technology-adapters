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

package org.openflexo.technologyadapter.excel.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

/**
 * Implementation of ResourceFactory for {@link ExcelWorkbookResource}
 * 
 * @author sylvain
 *
 */
public class ExcelWorkbookResourceFactory extends FlexoResourceFactory<ExcelWorkbookResource, ExcelWorkbook, ExcelTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(ExcelWorkbookResourceFactory.class.getPackage().getName());

	public static String EXCEL_FILE_EXTENSION = ".xls";
	public static String EXCELX_FILE_EXTENSION = ".xlsx";

	public ExcelWorkbookResourceFactory() throws ModelDefinitionException {
		super(ExcelWorkbookResource.class);
	}

	@Override
	public ExcelWorkbook makeEmptyResourceData(ExcelWorkbookResource resource) {
		return new ExcelWorkbook(resource.getTechnologyAdapter());
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return (resourceCenter.retrieveName(serializationArtefact).endsWith(EXCEL_FILE_EXTENSION)
				|| resourceCenter.retrieveName(serializationArtefact).endsWith(EXCELX_FILE_EXTENSION))
				&& !(resourceCenter.retrieveName(serializationArtefact).startsWith("~"));
	}

	@Override
	protected <I> ExcelWorkbookResource registerResource(ExcelWorkbookResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<ExcelTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the ExcelWorkbookRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getExcelWorkbookRepository(resourceCenter));

		return resource;
	}

	/*
	
	public static ExcelWorkbookResource makeExcelWorkbookResource(File excelFile,
			ExcelTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, ExcelWorkbookResource.class));
			ExcelWorkbookResourceImpl returned = (ExcelWorkbookResourceImpl) factory.newInstance(ExcelWorkbookResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			returned.initName(excelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(excelFile, factory));
			// returned.setURI(modelURI);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			// technologyContextManager.registerResource(returned);
			try {
				ExcelWorkbook resourceData = returned.loadResourceData(null);
				returned.setResourceData(resourceData);
				resourceData.setResource(returned);
				returned.save(null);
				returned.isLoaded = true;
			} catch (SaveResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidExcelFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ExcelWorkbookResource retrieveExcelWorkbookResource(File modelFile,
			ExcelTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			if (technologyContextManager.getResourceWithURI(modelFile.toURI().toString()) != null) {
				return (ExcelWorkbookResource) technologyContextManager.getResourceWithURI(modelFile.toURI().toString());
			} else {
				ModelFactory factory = new ModelFactory(
						ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, ExcelWorkbookResource.class));
				ExcelWorkbookResourceImpl returned = (ExcelWorkbookResourceImpl) factory.newInstance(ExcelWorkbookResource.class);
				returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
				returned.setTechnologyContextManager(technologyContextManager);
				returned.initName(modelFile.getName());
				returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
				// returned.setURI(modelFile.toURI().toString());
				returned.setResourceCenter(resourceCenter);
				returned.setServiceManager(
						technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
				// technologyContextManager.registerResource(returned);
				return returned;
			}
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ExcelWorkbookResource retrieveExcelWorkbookResource(InJarResourceImpl workbookInJar,
			ExcelTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(InJarFlexoIODelegate.class, ExcelWorkbookResource.class));
			ExcelWorkbookResourceImpl returned = (ExcelWorkbookResourceImpl) factory.newInstance(ExcelWorkbookResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			String name = FilenameUtils.getBaseName(workbookInJar.getURL().getFile());
			String uri = workbookInJar.getURI();
			returned.initName(name);
	
			returned.setFlexoIODelegate(InJarFlexoIODelegateImpl.makeInJarFlexoIODelegate(workbookInJar, factory));
	
			returned.setURI(uri);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			// technologyContextManager.registerResource(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	 */
}