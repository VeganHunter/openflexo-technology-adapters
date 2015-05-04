/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.odt;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.rm.InJarResourceImpl;
import org.openflexo.technologyadapter.odt.rm.ODTDocumentRepository;
import org.openflexo.technologyadapter.odt.rm.ODTDocumentResource;
import org.openflexo.technologyadapter.odt.rm.ODTDocumentResourceImpl;

/**
 * This class defines and implements the ODT technology adapter, which allows to manage .odt documents (OpenDocument format, which is the
 * default format for OpenOffice)<br>
 * 
 * This technology adapter internally use JOpenDocument library.
 * 
 * @author sylvain
 * 
 */

@DeclareModelSlots({ ODTModelSlot.class })
@DeclareRepositoryType({ ODTDocumentRepository.class })
public class ODTTechnologyAdapter extends TechnologyAdapter {

	private static String ODT_FILE_EXTENSION = ".odt";

	protected static final Logger logger = Logger.getLogger(ODTTechnologyAdapter.class.getPackage().getName());

	public ODTTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return new String("ODT Technology Adapter");
	}

	@Override
	public ODTTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new ODTTechnologyContextManager(this, getTechnologyAdapterService().getServiceManager().getResourceCenterService());
	}

	@Override
	public ODTTechnologyContextManager getTechnologyContextManager() {
		return (ODTTechnologyContextManager) super.getTechnologyContextManager();
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {

		ODTDocumentRepository odtRepository = resourceCenter.getRepository(ODTDocumentRepository.class, this);
		if (odtRepository == null) {
			odtRepository = createODTDocumentRepository(resourceCenter);
		}

		Iterator<I> it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			// if (item instanceof File) {
			// System.out.println("searching " + item);
			// File candidateFile = (File) item;
			ODTDocumentResource wbRes = tryToLookupODT(resourceCenter, item);
			// }
		}

		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
	}

	protected ODTDocumentResource tryToLookupODT(FlexoResourceCenter<?> resourceCenter, Object candidateElement) {
		ODTTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		if (isValidODT(candidateElement)) {
			ODTDocumentResource wbRes = retrieveODTResource(candidateElement);
			ODTDocumentRepository wbRepository = resourceCenter.getRepository(ODTDocumentRepository.class, this);
			if (wbRes != null) {
				RepositoryFolder<ODTDocumentResource> folder;
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
	public ODTDocumentResource retrieveODTResource(Object odtDocumentItem) {

		ODTDocumentResource returned = null; // getTechnologyContextManager().getExcelWorkbookResource(workbook);
		if (returned == null) {
			if (odtDocumentItem instanceof File) {
				returned = ODTDocumentResourceImpl.retrieveODTDocumentResource((File) odtDocumentItem, getTechnologyContextManager());
			}
			if (returned != null) {
				getTechnologyContextManager().registerODTDocumentResource(returned);
			} else {
				logger.warning("Cannot retrieve ODTDocumentResource resource for " + odtDocumentItem);
			}
		}

		return returned;
	}

	public boolean isValidODT(Object candidateElement) {
		if (candidateElement instanceof File && isValidODTFile(((File) candidateElement))) {
			return true;
		} else if (candidateElement instanceof InJarResourceImpl && isValidODTInJar((InJarResourceImpl) candidateElement)) {
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
	public boolean isValidODTFile(File candidateFile) {
		return candidateFile.getName().endsWith(ODT_FILE_EXTENSION);
	}

	public boolean isValidODTInJar(InJarResourceImpl candidateInJar) {
		if (candidateInJar.getRelativePath().endsWith(ODT_FILE_EXTENSION)) {
			return true;
		}
		return false;
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <I> void contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub

	}

	@Override
	public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * Create a {@link ODTDocumentRepository} for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	public ODTDocumentRepository createODTDocumentRepository(FlexoResourceCenter<?> resourceCenter) {
		ODTDocumentRepository returned = new ODTDocumentRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, ODTDocumentRepository.class, this);
		return returned;
	}

	public ODTDocumentResource createNewODTDocument(FlexoProject project, String filename, String modelUri) {
		// TODO Auto-generated method stub
		return null;
	}

	public ODTDocumentResource createNewODTDocument(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
			String modelUri) {
		// TODO Auto-generated method stub
		return null;
	}

}