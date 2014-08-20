/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2014 Openflexo
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

package org.openflexo.technologyadapter.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.technologyadapter.DeclareModelSlot;
import org.openflexo.foundation.technologyadapter.DeclareModelSlots;
import org.openflexo.foundation.technologyadapter.DeclareRepositoryType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModelImpl;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelFactory;
import org.openflexo.technologyadapter.xml.rm.XMLFileResourceImpl;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.technologyadapter.xml.rm.XMLResource;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResource;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResourceImpl;
import org.openflexo.xml.XMLRootElementInfo;
import org.openflexo.xml.XMLRootElementReader;

/**
 * 
 * @author sylvain, luka, Christophe
 * 
 */

@DeclareModelSlots({ // ModelSlot(s) declaration
	// Pure XML, without XSD
	@DeclareModelSlot(FML = "XMLModelSlot", modelSlotClass = XMLModelSlot.class) ,
	//Classical type-safe interpretation
	@DeclareModelSlot(FML = "XSDModelSlot", modelSlotClass = XSDModelSlot.class) })
@DeclareRepositoryType({ XMLModelRepository.class, XSDMetaModelRepository.class})

public class XMLTechnologyAdapter extends TechnologyAdapter {

	private static final String   TAName          = "XML technology adapter";
	private static final String   XML_EXTENSION   = ".xml";
	private static final String   XSD_EXTENSION   = ".xsd";

	private XMLModelFactory       xmlModelFactory = null;
	protected static XMLRootElementReader REreader = new XMLRootElementReader();
	protected static final Logger logger          = Logger.getLogger(XMLTechnologyAdapter.class.getPackage().getName());

	protected HashMap<String, XMLMetaModel> privateMetamodels = null;


	public XMLTechnologyAdapter() {
		super();
		xmlModelFactory = new XMLModelFactory();
		privateMetamodels = new HashMap<String, XMLMetaModel>();
	}

	@Override
	public String getName() {
		return TAName;
	}

	/**
	 * Return flag indicating if supplied file represents a valid XSD schema
	 * 
	 * @param aMetaModelFile
	 * @return
	 */
	public boolean isValidMetaModelFile(File aMetaModelFile) {
		// TODO: also check that file is valid and maps a valid XSD schema

		return aMetaModelFile.isFile() && aMetaModelFile.getName().endsWith(".xsd");
	}

	public String retrieveMetaModelURI(File aMetaModelFile, TechnologyContextManager technologyContextManager) {
		// No MetaModel in this connector
		// logger.warning("NO MetaModel exists for XMLTechnologyAdapter");
		return null;
	}

	public FlexoResource<XMLModel> retrieveMetaModelResource(File aMetaModelFile, TechnologyContextManager technologyContextManager) {
		// No MetaModel in this connector
		// logger.warning("NO MetaModel exists for XMLTechnologyAdapter");
		return null;
	}

	public boolean isValidModelFile(File aModelFile, FlexoResource<XMLMetaModel> metaModelResource,
			TechnologyContextManager technologyContextManager) {

		if (isValidModelFile(aModelFile)) {

			XMLRootElementInfo rootInfo = null;
			try {

				// FIXME : maybe the RootElement info should be stored with File Resource ?
				// to avoid looping through all XSD MM each time you have an xml file to test

				rootInfo = REreader.readRootElement(aModelFile);

				String schemaURI = rootInfo.getAttribute("targetNamespace");

				String mmURI = metaModelResource.getURI();
				if (schemaURI != null && mmURI != null) {
					if (schemaURI.equals(mmURI)) {
						logger.info("Found a conformant XML Model File [" + schemaURI + "]" + aModelFile.getAbsolutePath());
						return !schemaURI.isEmpty();
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

		return false;

	}

	public boolean isValidModelFile(File aModelFile) {
		if (aModelFile.exists() && aModelFile.getName().endsWith(XML_EXTENSION))
			return true;
		else
			return false;
	}

	public String retrieveModelURI(File aModelFile, FlexoResource<XMLModel> metaModelResource) {
		return aModelFile.toURI().toString();
	}

	/**
	 * Create empty model.
	 * 
	 * @param modelFile
	 * @param modelUri
	 * @param technologyContextManager
	 * @return
	 */
	/*
	public XMLFileResource createEmptyModel(File modelFile, TechnologyContextManager technologyContextManager) {

		XMLFileResource ModelResource = XMLFileResourceImpl.makeXMLFileResource(modelFile,
				(XMLTechnologyContextManager) technologyContextManager);
		technologyContextManager.registerResource(ModelResource);
		return ModelResource;

	}

	public XMLFileResource createEmptyModel(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
			String modelUri, FlexoResource<XMLModel> metaModelResource, TechnologyContextManager technologyContextManager) {

		File modelDirectory = new File(resourceCenter.getRootDirectory(), relativePath);
		File modelFile = new File(modelDirectory, filename);
		return createEmptyModel(modelFile, technologyContextManager);
	}

	public XMLFileResource createEmptyModel(FlexoProject project, String filename, String modelUri,
			FlexoResource<XMLModel> metaModelResource, TechnologyContextManager technologyContextManager) {

		File modelFile = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);

		return createEmptyModel(modelFile, technologyContextManager);
	}

*/
	/**
	 * Create empty model.
	 * 
	 * @param modelFile
	 * @param modelUri
	 * @param metaModelResource
	 * @param resourceCenter 
	 * @param technologyContextManager
	 * @return
	 */
	/*
	public XMLXSDFileResource createNewXMLFile(File modelFile, String modelUri, FlexoResource<XMLMetaModel> metaModelResource, FlexoResourceCenter<?> resourceCenter) {

		modelUri = modelFile.toURI().toString();

		XMLXSDFileResource modelResource = XMLXSDFileResourceImpl.makeXMLXSDFileResource(modelUri, modelFile,
				(XSDMetaModelResource) metaModelResource, (XMLTechnologyContextManager) getTechnologyContextManager());

		referenceResource(modelResource,resourceCenter);

		return modelResource;

	}
	 */
	/**
	 * Creates new model conform to the supplied meta model
	 * 
	 * @param project
	 * @param metaModel
	 * @return
	 */
	/*
	public XMLXSDFileResource createNewXMLFile(FlexoProject project, String filename, String modelUri, FlexoResource<XMLMetaModel> metaModel) {

		File modelFile = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);

		// TODO: modelURI is not used here!!!! => check the API, as it is
		// processed by TA
		logger.warning("modelURI are not useful in this context");

		return createNewXMLFile(modelFile, modelUri, metaModel,project);

	}
	 */
	/*
	public FlexoResource<XMLXSDModel> createNewXMLFile(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
			FlexoResource<XMLMetaModel> metaModelResource) {

		File modelDirectory = new File(resourceCenter.getRootDirectory(), relativePath);
		File modelFile = new File(modelDirectory, filename);

		String modelUri = modelFile.toURI().toString();

		return createNewXMLFile(modelFile, modelUri, metaModelResource,resourceCenter);
	}

	public FlexoResource<XMLXSDModel> createNewXMLFile(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
			String modelUri, FlexoResource<XMLMetaModel> metaModelResource) {

		File modelDirectory = new File(resourceCenter.getRootDirectory(), relativePath);
		File modelFile = new File(modelDirectory, filename);

		return createNewXMLFile(modelFile, modelUri, metaModelResource,resourceCenter);
	}
	 */
	@Override
	public TechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {

		return new XMLTechnologyContextManager(this, service);
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getExpectedModelExtension(FlexoResource<?> metaModel) {
		return XML_EXTENSION;
	}


	public String getExpectedMetaModelExtension() {
		return XSD_EXTENSION;
	}


	@Override
	public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {

		XMLModelRepository mRepository = resourceCenter.getRepository(XMLModelRepository.class, this);
		if (mRepository == null) {
			mRepository = createXMLModelRepository(resourceCenter);
		}

		XSDMetaModelRepository mmRepository = resourceCenter.getRepository(XSDMetaModelRepository.class, this);
		if (mmRepository == null) {
			mmRepository = createXMLMetaModelRepository(resourceCenter);
		}

		// First pass on meta-models only
		Iterator<I> it = resourceCenter.iterator();
		XSDMetaModelResource mmRes = null;
		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				File candidateFile = (File) item;
				mmRes = tryToLookupMetaModel(resourceCenter, candidateFile);
			}
		}

		// Second pass on models
		it = resourceCenter.iterator();
		XMLResource mRes = null;

		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				File candidateFile = (File) item;
				mRes = tryToLookupModel(resourceCenter, candidateFile);
			}
		}

	}


	protected XSDMetaModelResource tryToLookupMetaModel(FlexoResourceCenter<?> resourceCenter, File candidateFile) {

		XSDMetaModelRepository mmRepository = resourceCenter.getRepository(XSDMetaModelRepository.class, this);
		XMLTechnologyContextManager xmlContextManager = (XMLTechnologyContextManager) getTechnologyContextManager();
		XSDMetaModelResource mmRes = null;

		if (isValidMetaModelFile(candidateFile)) {

			XMLRootElementInfo rootInfo = null;
			String uri = null;

			try {

				rootInfo = REreader.readRootElement(candidateFile);

				uri = rootInfo.getAttribute("targetNamespace");

			} catch (IOException e) {
				logger.warning("Unable to parse file: " + candidateFile);
				e.printStackTrace();
				return null;
			}

			if (uri != null){

				mmRes = (XSDMetaModelResource) xmlContextManager.getResourceWithURI(uri);

				if (mmRes == null) {

					mmRes = XSDMetaModelResourceImpl.makeXSDMetaModelResource(candidateFile, uri, xmlContextManager);
					mmRes.setName(candidateFile.getName());
					mmRes.setServiceManager(getTechnologyAdapterService().getServiceManager());
				}
				else {
					logger.warning("Found another file with an already existing URI: " + uri);
					return null;
				}

				// Register Resource


				if (mmRes != null) {
					RepositoryFolder<XSDMetaModelResource> folder;
					xmlContextManager.registerResource(mmRes);
					try {

						folder = mmRepository.getRepositoryFolder(candidateFile, true);
						if (folder != null){
							mmRepository.registerResource(mmRes, folder);
						}else{
							mmRepository.registerResource(mmRes);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					referenceResource(mmRes, resourceCenter);

					return mmRes;
				}
			}
		}
		return null;
	}

	protected XMLResource tryToLookupModel(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
		XMLTechnologyContextManager xmlContextManager = (XMLTechnologyContextManager) getTechnologyContextManager();
		XMLModelRepository modelRepository = resourceCenter.getRepository(XMLModelRepository.class, this);
		XSDMetaModelRepository mmRepository = null;

		XMLResource mRes = null;

		if (isValidModelFile(candidateFile)) {
			mRes = XMLFileResourceImpl.makeXMLFileResource(candidateFile, xmlContextManager );

			if (mRes != null) {

				xmlContextManager.registerResource(mRes);
				RepositoryFolder<XMLResource> folder;
				try {
					folder = modelRepository.getRepositoryFolder(candidateFile, true);
					if (folder != null){
						modelRepository.registerResource(mRes, folder);
					}else{
						modelRepository.registerResource(mRes);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(mRes, resourceCenter);

				// then find the MetaModel

				String mmURI = mRes.getTargetNamespace();

				if (mmURI != null && mmURI.length() > 0){

					XSDMetaModelResource mmRsc =  null;

					// Looping all resource-centers to find a MetaModel Resource corresponding to this File

					//*********************************************************************************
					// TODO
					// If you find something....
					// either it is an XSD or an already registered MM (initialized or not)
					// or it is a non existent (in Memory) MM
					//
					// But how to promote an AdHoc MM to actual MM ?
					// And serialize it as an XSD?

					List<FlexoResourceCenter> rscCenters = xmlContextManager.getResourceCenterService().getResourceCenters();
					for (FlexoResourceCenter<?> rscCenter : rscCenters) {
						mmRepository = rscCenter.getRepository(XSDMetaModelRepository.class, this);
						if (mmRepository != null) {
							mmRsc =  mmRepository.getResource(mmURI);
							if (mmRsc != null) {
								mRes.setMetaModelResource(mmRsc);
							}
						}
					}

					// Found nothing in RC, create a new one in TA MM collection
					// TODO: should be able to save somewhere and share it between several files
					// TODO: should be possible to expose it through TA

					if (mmRsc == null) {
						XMLMetaModel mm = privateMetamodels.get(mmURI);
						if (mm == null){
							mm = XMLMetaModelImpl.getModelFactory().newInstance(XMLMetaModel.class);
							mm.setURI(mmURI);
							mm.setReadOnly(false);
							privateMetamodels.put(mmURI, mm);
							logger.info("Added a MetaModel for Resource in TA private MetaModels: " + mm.getURI());

						}
						else {
							logger.info("Found a MetaModel for Resource in TA private MetaModels: " + mm.getURI());
						}

						mRes.getModel().setMetaModel(mm);
					}

				}
				else {
					// This Model has no MetaModel URI, we create a private one for the model

					XMLMetaModel mm = XMLMetaModelImpl.getModelFactory().newInstance(XMLMetaModel.class);
					mm.setURI(mRes.getURI() +"/Metamodel");
					mm.setReadOnly(false);
					mRes.getModel().setMetaModel(mm);
				}
			}
		}

		return null;
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
	 * Create a XMLModel repository for current {@link TechnologyAdapter} and
	 * supplied {@link FlexoResourceCenter}
	 * 
	 */
	public XSDMetaModelRepository createXMLMetaModelRepository(FlexoResourceCenter<?> resourceCenter) {
		XSDMetaModelRepository returned = new XSDMetaModelRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, XSDMetaModelRepository.class, this);
		return returned;
	}


	/**
	 * 
	 * Create a XMLModel repository for current {@link TechnologyAdapter} and
	 * supplied {@link FlexoResourceCenter}
	 * 
	 */
	public XMLModelRepository createXMLModelRepository(FlexoResourceCenter<?> resourceCenter) {
		XMLModelRepository returned = new XMLModelRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, XMLModelRepository.class, this);
		return returned;
	}


	public XMLModelFactory getXMLModelFactory() {
		return xmlModelFactory;
	}



}
