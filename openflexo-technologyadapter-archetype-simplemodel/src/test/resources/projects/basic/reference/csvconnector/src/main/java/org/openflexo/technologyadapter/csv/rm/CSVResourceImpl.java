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

package org.openflexo.technologyadapter.csv.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoFileResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.csv.CSVTechnologyContextManager;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.toolbox.IProgress;

public abstract class CSVResourceImpl extends FlexoFileResourceImpl<CSVModel> implements CSVResource {
    
    private static final Logger LOGGER = Logger.getLogger(CSVResourceImpl.class.getPackage().getName());

    public static CSVResource makeCSVResource(String modelURI, File modelFile,
            CSVTechnologyContextManager technologyContextManager) {
        try {
            ModelFactory factory = new ModelFactory(CSVResource.class);
            CSVResourceImpl returned = (CSVResourceImpl) factory.newInstance(CSVResource.class);
            returned.setName(modelFile.getName());
            returned.setFile(modelFile);
            returned.setURI(modelURI);
            returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
            returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
            returned.setTechnologyContextManager(technologyContextManager);
            technologyContextManager.registerResource(returned);

            return returned;
        } catch (ModelDefinitionException e) {
            final String msg = "Error while initializing CSV model resource";
            LOGGER.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    public static CSVResource retrieveCSVResource(File modelFile, CSVTechnologyContextManager technologyContextManager) {
        try {
            ModelFactory factory = new ModelFactory(CSVResource.class);
            CSVResourceImpl returned = (CSVResourceImpl) factory.newInstance(CSVResource.class);
            returned.setName(modelFile.getName());
            returned.setFile(modelFile);
            returned.setURI(modelFile.toURI().toString());
            returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
            returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
            returned.setTechnologyContextManager(technologyContextManager);
            technologyContextManager.registerResource(returned);
            return returned;
        } catch (ModelDefinitionException e) {
            final String msg = "Error while initializing CSV model resource";
            LOGGER.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    @Override
    public CSVModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {
        // TODO: Auto-generated Method
        return null;
    }

    @Override
    public void save(IProgress progress) throws SaveResourceException {
        CSVModel resourceData = null;
        try {
            resourceData = getResourceData(progress);
        } catch (FileNotFoundException e) {
            final String msg = "Error while saving CSV model resource";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SaveResourceException(this);
        } catch (ResourceLoadingCancelledException e) {
            final String msg = "Error while saving CSV model resource";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SaveResourceException(this);
        } catch (FlexoException e) {
            final String msg = "Error while saving CSV model resource";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SaveResourceException(this);
        }

        if (!hasWritePermission()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Permission denied : " + getFile().getAbsolutePath());
            }
            throw new SaveResourcePermissionDeniedException(this);
        }
        if (resourceData != null) {
            FlexoFileResourceImpl.FileWritingLock lock = willWriteOnDisk();
            writeToFile();
            hasWrittenOnDisk(lock);
            notifyResourceStatusChanged();
            resourceData.clearIsModified(false);
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Succeeding to save Resource " + getURI() + " : " + getFile().getName());
            }
        }
    }

    private void writeToFile() throws SaveResourceException {
        //TODO : Auto-generated method skeletton.
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getFile());
        } catch (FileNotFoundException e) {
            final String msg = "Error while saving CSV model resource";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SaveResourceException(this);
        } finally {
            IOUtils.closeQuietly(out);
        }

        LOGGER.info("Wrote " + getFile());
    }

    @Override
    public Class<CSVModel> getResourceDataClass() {
        return CSVModel.class;
    }
}