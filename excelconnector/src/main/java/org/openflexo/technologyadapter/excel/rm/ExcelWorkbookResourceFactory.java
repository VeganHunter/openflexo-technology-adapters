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

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.model.io.BasicExcelModelConverter;

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
		if (resource.getIODelegate() instanceof StreamIODelegate) {
			return createExcelWorkbook((StreamIODelegate) resource.getIODelegate());
		}
		logger.severe("Cannot create excel workbook for this io delegate: " + resource.getIODelegate());
		return null;
	}

	protected static <I> ExcelWorkbook createExcelWorkbook(StreamIODelegate<I> ioDelegate) {
		Workbook wb = null;
		ExcelWorkbook newWorkbook = null;

		try {
			if (!ioDelegate.exists() && ioDelegate.getSerializationArtefactName().endsWith(".xls")) {
				wb = new HSSFWorkbook();
				wb.createSheet("Default");
			}
			else if (!ioDelegate.exists() && ioDelegate.getSerializationArtefactName().endsWith(".xlsx")) {
				wb = new XSSFWorkbook();
				wb.createSheet("Default");
			}
			else {
				wb = WorkbookFactory.create(ioDelegate.getInputStream());
			}
			BasicExcelModelConverter converter = new BasicExcelModelConverter();
			newWorkbook = converter.convertExcelWorkbook(wb,
					((ExcelWorkbookResource) ioDelegate.getFlexoResource()).getTechnologyAdapter());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return newWorkbook;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return (resourceCenter.retrieveName(serializationArtefact).endsWith(EXCEL_FILE_EXTENSION)
				|| resourceCenter.retrieveName(serializationArtefact).endsWith(EXCELX_FILE_EXTENSION))
				&& !(resourceCenter.retrieveName(serializationArtefact).startsWith("~"));
	}

	@Override
	public <I> I getConvertableArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return null;
	}

	@Override
	protected <I> ExcelWorkbookResource registerResource(ExcelWorkbookResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<ExcelTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the ExcelWorkbookRepository of supplied
		// resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getExcelWorkbookRepository(resourceCenter));

		return resource;
	}

}
