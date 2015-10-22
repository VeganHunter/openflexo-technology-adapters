/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.tests.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookRepository;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestCompareExcelSheet extends OpenflexoProjectAtRunTimeTestCase {
	protected static final Logger logger = Logger.getLogger(TestCompareExcelSheet.class.getPackage().getName());

	private static FlexoEditor editor;
	private static FlexoProject project;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
	}

	@Test
	@TestOrder(3)
	public void testLoadExcelWorkbook() {
		ExcelTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
				ExcelTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			ExcelWorkbookRepository excelWorkbookRepository = resourceCenter.getRepository(ExcelWorkbookRepository.class,
					technologicalAdapter);
			assertNotNull(excelWorkbookRepository);
			Collection<ExcelWorkbookResource> workbooks = excelWorkbookRepository.getAllResources();
			for (ExcelWorkbookResource excelWorkbook : workbooks) {
				if (excelWorkbook.getName().contains("exemple")) {
					try {
						ExcelWorkbook excelModel = excelWorkbook.loadResourceData(null);
						assertNotNull(excelWorkbook.getLoadedResourceData());
						assertNotNull(excelModel);
						assertEquals(excelModel, excelWorkbook.getLoadedResourceData());

						List<ExcelSheet> sheets = excelModel.getExcelSheets();

						for (ExcelSheet refSheet : sheets) {
							for (ExcelRow refRow : refSheet.getExcelRows()) {

								for (ExcelSheet sheet : sheets) {
									for (ExcelRow row : sheet.getExcelRows()) {
										if (sheet != refSheet) {
											assertNotNull(row.getRow());
											assertNotNull(row.getCellAt(0));
											assertFalse(row.getRow() == refRow.getRow());
											assertNotSame(row.getRow(), refRow.getRow());
											// Row do have the same hashcode
											// assertNotSame(row.getRow().hashCode(), refRow.getRow().hashCode());
											assertNotSame(row, refRow);
											assertNotSame(row.hash(), refRow.hash());
											assertFalse(row == refRow);
										}
										if (row != refRow) {
											for (ExcelCell c : row.getExcelCells()) {
												for (ExcelCell rc : refRow.getExcelCells()) {
													assertNotNull(c.getCell());
													assertFalse(c.getCell() == rc.getCell());
													assertNotSame(c.getCell().hashCode(), rc.getCell().hashCode());
													assertNotSame(c.hash(), rc.hash());
													assertNotSame(c, rc);
													assertNotSame(c.hash(), rc.hash());
													assertFalse(c == rc);
												}
											}
										}
									}
								}
							}
						}

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ResourceLoadingCancelledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FlexoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}