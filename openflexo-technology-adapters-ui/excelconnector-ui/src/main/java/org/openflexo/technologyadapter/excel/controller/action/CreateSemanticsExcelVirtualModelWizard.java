/*
 * Copyright (c) 2013-2017, Openflexo
 *
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
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
 *           Additional permission under GNU GPL version 3 section 7
 *           If you modify this Program, or any covered work, by linking or
 *           combining it with software containing parts covered by the terms
 *           of EPL 1.0, the licensors of this Program grant you additional permission
 *           to convey the resulting work.
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

package org.openflexo.technologyadapter.excel.controller.action;

import java.awt.Dimension;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.action.CreateSemanticsExcelVirtualModel;
import org.openflexo.technologyadapter.excel.action.CreateSemanticsExcelVirtualModel.SEFlexoConceptIdentification;
import org.openflexo.technologyadapter.excel.model.ExcelCellRange;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateSemanticsExcelVirtualModelWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateSemanticsExcelVirtualModelWizard.class.getPackage().getName());

	private final CreateSemanticsExcelVirtualModel action;

	private final ConfigureSemanticsExcelVirtualModel configureSemanticsExcelVirtualModel;
	private final List<ConfigureNewSEFlexoConcept> configureNewSEFlexoConcepts = new ArrayList<>();

	private static final Dimension DIMENSIONS = new Dimension(600, 500);

	public CreateSemanticsExcelVirtualModelWizard(CreateSemanticsExcelVirtualModel action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(configureSemanticsExcelVirtualModel = new ConfigureSemanticsExcelVirtualModel());
		ConfigureNewSEFlexoConcept newConfigureNewSEFlexoConcept = new ConfigureNewSEFlexoConcept();
		configureNewSEFlexoConcepts.add(newConfigureNewSEFlexoConcept);
		addStep(newConfigureNewSEFlexoConcept);
	}

	@Override
	public String getWizardTitle() {
		return action.getLocales().localizedForKey("create_virtual_model_reflecting_an_excel_workbook");
	}

	@Override
	public Image getDefaultPageImage() {
		// TODO change icon
		return IconFactory.getImageIcon(FMLIconLibrary.VIRTUAL_MODEL_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public ConfigureSemanticsExcelVirtualModel getConfigureSemanticsExcelVirtualModel() {
		return configureSemanticsExcelVirtualModel;
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/ConfigureSemanticsExcelVirtualModel.fib")
	public class ConfigureSemanticsExcelVirtualModel extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateSemanticsExcelVirtualModel getAction() {
			return action;
		}

		@Override
		public String getTitle() {
			return action.getLocales().localizedForKey("choose_virtual_model_name_and_template_excel");
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getNewVirtualModelName())) {
				setIssueMessage(action.getLocales().localizedForKey("please_enter_name_for_new_virtual_model"), IssueMessageType.ERROR);
				return false;
			}

			if (getExcelWorkbookResource() == null) {
				setIssueMessage(action.getLocales().localizedForKey("please_select_an_excel_workbook_to_be_used_as_a_template"),
						IssueMessageType.ERROR);
				return false;
			}

			return true;

		}

		public String getNewVirtualModelName() {
			return action.getNewVirtualModelName();
		}

		public void setNewVirtualModelName(String newVirtualModelName) {
			if (!newVirtualModelName.equals(getNewVirtualModelName())) {
				String oldValue = getNewVirtualModelName();
				action.setNewVirtualModelName(newVirtualModelName);
				getPropertyChangeSupport().firePropertyChange("newVirtualModelName", oldValue, newVirtualModelName);
				checkValidity();
			}
		}

		public ExcelWorkbookResource getExcelWorkbookResource() {
			return action.getExcelWorkbookResource();
		}

		public void setExcelWorkbookResource(ExcelWorkbookResource excelWorkbookResource) {
			if (excelWorkbookResource != getExcelWorkbookResource()) {
				ExcelWorkbookResource oldValue = getExcelWorkbookResource();
				action.setExcelWorkbookResource(excelWorkbookResource);
				getPropertyChangeSupport().firePropertyChange("excelWorkbookResource", oldValue, excelWorkbookResource);
				checkValidity();
			}
		}

		public ExcelTechnologyAdapter getExcelTechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(ExcelTechnologyAdapter.class);
		}

	}

	@FIBPanel("Fib/Wizard/ConfigureNewSEFlexoConcept.fib")
	public class ConfigureNewSEFlexoConcept extends WizardStep {

		private SEFlexoConceptIdentification flexoConceptIdentification;
		private boolean defineOtherConcept = false;

		public ConfigureNewSEFlexoConcept() {
			flexoConceptIdentification = getAction().new SEFlexoConceptIdentification();
		}

		public SEFlexoConceptIdentification getSEFlexoConceptIdentification() {
			return flexoConceptIdentification;
		}

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateSemanticsExcelVirtualModel getAction() {
			return action;
		}

		@Override
		public String getTitle() {
			return action.getLocales().localizedForKey("identify_new_concept");
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getNewConceptName())) {
				setIssueMessage(action.getLocales().localizedForKey("please_enter_name_for_new_flexo_concept"), IssueMessageType.ERROR);
				return false;
			}

			return true;
		}

		public String getNewConceptName() {
			return flexoConceptIdentification.getConceptName();
		}

		public void setNewConceptName(String newConceptName) {
			if (!newConceptName.equals(getNewConceptName())) {
				String oldValue = getNewConceptName();
				flexoConceptIdentification.setConceptName(newConceptName);
				getPropertyChangeSupport().firePropertyChange("newConceptName", oldValue, newConceptName);
				checkValidity();
			}
		}

		public ExcelCellRange getCellRange() {
			return flexoConceptIdentification.getCellRange();
		}

		public void setCellRange(ExcelCellRange cellRange) {
			if ((cellRange == null && getCellRange() != null) || (cellRange != null && !cellRange.equals(getCellRange()))) {
				ExcelCellRange oldValue = getCellRange();
				flexoConceptIdentification.setCellRange(cellRange);
				getPropertyChangeSupport().firePropertyChange("cellRange", oldValue, cellRange);
				checkValidity();
			}
		}

		public ExcelWorkbook getExcelWorkbook() {
			try {
				return getAction().getExcelWorkbookResource().getResourceData(null);
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
			return null;
		}

		public boolean getDefineOtherConcept() {
			return defineOtherConcept;
		}

		public void setDefineOtherConcept(boolean defineOtherConcept) {
			if (defineOtherConcept != this.defineOtherConcept) {
				boolean oldValue = this.defineOtherConcept;
				this.defineOtherConcept = defineOtherConcept;
				getPropertyChangeSupport().firePropertyChange("defineOtherConcept", oldValue, defineOtherConcept);
				checkValidity();
			}
		}

		@Override
		public boolean isTransitionalStep() {
			return defineOtherConcept;
		}

		@Override
		public void performTransition() {
			ConfigureNewSEFlexoConcept newConfigureNewSEFlexoConcept = new ConfigureNewSEFlexoConcept();
			configureNewSEFlexoConcepts.add(newConfigureNewSEFlexoConcept);
			addStep(newConfigureNewSEFlexoConcept);
		}

		@Override
		public void discardTransition() {
			ConfigureNewSEFlexoConcept toRemove = configureNewSEFlexoConcepts.get(configureNewSEFlexoConcepts.size() - 1);
			removeStep(toRemove);
			configureNewSEFlexoConcepts.remove(toRemove);
		}

	}

	/*@FIBPanel("Fib/Wizard/ChooseEntitiesToBeReflected.fib")
	public class ChooseEntitiesToBeReflected extends WizardStep {
	
		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}
	
		public CreateJDBCVirtualModel getAction() {
			return action;
		}
	
		@Override
		public String getTitle() {
			return action.getLocales().localizedForKey("choose_entities_to_be_reflected");
		}
	
		@Override
		public boolean isValid() {
	
			if (getAction().getJDBCConnection().getConnection() == null) {
				setIssueMessage(getAction().getJDBCConnection().getException().getMessage(), IssueMessageType.ERROR);
				return false;
			}
	
			if (getAction().getTablesToBeReflected() == null || getAction().getTablesToBeReflected().size() == 0) {
				setIssueMessage(action.getLocales().localizedForKey("please_choose_some_entities"), IssueMessageType.ERROR);
				return false;
			}
	
			return true;
		}
	
		public void changedSelection() {
			getPropertyChangeSupport().firePropertyChange("tablesToBeReflected", null, getAction().getTablesToBeReflected());
			checkValidity();
		}
	
		public ImageIcon getTableIcon() {
			return JDBCIconLibrary.JDBC_TABLE_ICON;
		}
	
		@Override
		public boolean isTransitionalStep() {
			return true;
		}
	
		private List<ConfigureTableMapping> configureTableMappings;
	
		@Override
		public void performTransition() {
			configureTableMappings = new ArrayList<>();
			for (TableMapping tm : getAction().getTableMappings()) {
				ConfigureTableMapping newCTM = new ConfigureTableMapping(tm);
				configureTableMappings.add(newCTM);
				addStep(newCTM);
			}
		}
	
		@Override
		public void discardTransition() {
			for (ConfigureTableMapping ctm : configureTableMappings) {
				removeStep(ctm);
			}
			configureTableMappings.clear();
			getAction().clearTableMappings();
		}
	
	}
	
	@FIBPanel("Fib/Wizard/ConfigureTableMapping.fib")
	public class ConfigureTableMapping extends WizardStep {
	
		private TableMapping tableMapping;
	
		public ConfigureTableMapping(TableMapping tableMapping) {
			this.tableMapping = tableMapping;
		}
	
		public TableMapping getTableMapping() {
			return tableMapping;
		}
	
		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}
	
		public CreateJDBCVirtualModel getAction() {
			return action;
		}
	
		@Override
		public String getTitle() {
			return action.getLocales().localizedForKey("configure_table_mapping_for") + " " + tableMapping.getTable().getName();
		}
	
		@Override
		public boolean isValid() {
	
			if (getAction().getJDBCConnection().getConnection() == null) {
				setIssueMessage(getAction().getJDBCConnection().getException().getMessage(), IssueMessageType.ERROR);
				return false;
			}
	
			return true;
		}
	
		public ImageIcon getColumnIcon(JDBCColumn column) {
			if (column.isPrimaryKey()) {
				return JDBCIconLibrary.JDBC_KEY_ICON;
			}
			return JDBCIconLibrary.JDBC_COLUMN_ICON;
		}
	}
	*/
}
