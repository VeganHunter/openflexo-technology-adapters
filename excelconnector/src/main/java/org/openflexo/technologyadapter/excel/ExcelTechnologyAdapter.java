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
package org.openflexo.technologyadapter.excel;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.rm.InJarResourceImpl;
import org.openflexo.technologyadapter.excel.fml.binding.ExcelBindingFactory;
import org.openflexo.technologyadapter.excel.rm.ExcelMetaModelRepository;
import org.openflexo.technologyadapter.excel.rm.ExcelModelRepository;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookRepository;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResourceImpl;

/**
 * This class defines and implements the Excel technology adapter
 * 
 * @author sylvain, vincent, Christophe
 * 
 */
@DeclareModelSlots({ BasicExcelModelSlot.class, SemanticsExcelModelSlot.class })
@DeclareRepositoryType({ ExcelWorkbookRepository.class, ExcelMetaModelRepository.class, ExcelModelRepository.class })
public class ExcelTechnologyAdapter extends TechnologyAdapter {

	protected static final Logger logger = Logger.getLogger(ExcelTechnologyAdapter.class.getPackage().getName());

	private static final ExcelBindingFactory BINDING_FACTORY = new ExcelBindingFactory();

	/**
	 * 
	 * Constructor.
	 * 
	 * @throws TechnologyAdapterInitializationException
	 */
	public ExcelTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return "Excel technology adapter";
	}

	@Override
	public TechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new ExcelTechnologyContextManager(this, service);
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	/**
	 * Initialize the supplied resource center with the technology<br>
	 * ResourceCenter is scanned, ResourceRepositories are created and new technology-specific resources are build and registered.
	 * 
	 * @param resourceCenter
	 */
	@Override
	public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {

		ExcelWorkbookRepository wbRepository = resourceCenter.getRepository(ExcelWorkbookRepository.class, this);
		if (wbRepository == null) {
			wbRepository = createWorkbookRepository(resourceCenter);
		}

		Iterator<I> it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			// if (item instanceof File) {
			// System.out.println("searching " + item);
			// File candidateFile = (File) item;
			ExcelWorkbookResource wbRes = tryToLookupWorkbook(resourceCenter, item);
			// }
		}

		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);

	}

	protected ExcelWorkbookResource tryToLookupWorkbook(FlexoResourceCenter<?> resourceCenter, Object candidateElement) {
		ExcelTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		if (isValidWorkbook(candidateElement)) {
			ExcelWorkbookResource wbRes = retrieveWorkbookResource(candidateElement);
			ExcelWorkbookRepository wbRepository = resourceCenter.getRepository(ExcelWorkbookRepository.class, this);
			if (wbRes != null) {
				RepositoryFolder<ExcelWorkbookResource> folder;
				try {
					folder = wbRepository.getRepositoryFolder(candidateElement, true);
					wbRepository.registerResource(wbRes, folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(wbRes, resourceCenter);
				return wbRes;
			}
		}
		return null;
	}

	/**
	 * Instantiate new workbook resource stored in supplied model file<br>
	 * *
	 */
	public ExcelWorkbookResource retrieveWorkbookResource(Object workbook) {

		ExcelWorkbookResource returned = getTechnologyContextManager().getExcelWorkbookResource(workbook);
		if (returned == null) {
			if (workbook instanceof File) {
				returned = ExcelWorkbookResourceImpl.retrieveExcelWorkbookResource((File) workbook, getTechnologyContextManager());
			} else if (workbook instanceof InJarResourceImpl) {
				returned = ExcelWorkbookResourceImpl.retrieveExcelWorkbookResource((InJarResourceImpl) workbook,
						getTechnologyContextManager());
			}
			if (returned != null) {
				getTechnologyContextManager().registerExcelWorkbook(returned);
			} else {
				logger.warning("Cannot retrieve ExcelWorkbook resource for " + workbook);
			}
		}

		return returned;
	}

	public boolean isValidWorkbook(Object candidateElement) {
		if (candidateElement instanceof File && isValidWorkbookFile(((File) candidateElement))) {
			return true;
		} else if (candidateElement instanceof InJarResourceImpl && isValidWorkbookInJar((InJarResourceImpl) candidateElement)) {
			return true;
		}
		return false;
	}

	/**
	 * Return flag indicating if supplied file appears as a valid workbook
	 * 
	 * @param candidateFile
	 * 
	 * @return
	 */
	public boolean isValidWorkbookFile(File candidateFile) {
		return candidateFile.getName().endsWith(".xlsx") || candidateFile.getName().endsWith(".xls");
	}

	public boolean isValidWorkbookInJar(InJarResourceImpl candidateInJar) {
		if (candidateInJar.getRelativePath().endsWith(".xlsx") || candidateInJar.getRelativePath().endsWith(".xls")) {
			return true;
		}
		return false;
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	@Override
	public <I> void contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			File candidateFile = (File) contents;
			if (tryToLookupWorkbook(resourceCenter, candidateFile) != null) {
			}
		}
	}

	@Override
	public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExcelTechnologyContextManager getTechnologyContextManager() {
		return (ExcelTechnologyContextManager) super.getTechnologyContextManager();
	}

	/**
	 * 
	 * Create a workbook repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	public ExcelWorkbookRepository createWorkbookRepository(FlexoResourceCenter<?> resourceCenter) {
		ExcelWorkbookRepository returned = new ExcelWorkbookRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, ExcelWorkbookRepository.class, this);
		return returned;
	}

	/**
	 * Create empty model.
	 * 
	 * @param modelFile
	 * @param modelUri
	 * @param metaModelResource
	 * @param technologyContextManager
	 * @return
	 */
	public ExcelWorkbookResource createNewWorkbook(FlexoProject project, String excelFilename, String modelUri) {

		File excelFile = new File(FlexoProject.getProjectSpecificModelsDirectory(project), excelFilename);

		modelUri = excelFile.toURI().toString();

		ExcelWorkbookResource workbookResource = ExcelWorkbookResourceImpl.makeExcelWorkbookResource(modelUri, excelFile,
				getTechnologyContextManager());

		getTechnologyContextManager().registerResource(workbookResource);

		return workbookResource;
	}

}
