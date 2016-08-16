/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.Rectangle;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateTechnologyRole;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramSpecification;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.FileUtils;

/**
 * Test the creation of a VirtualModel whose instances have {@link FMLControlledDiagramVirtualModelNature}
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestControlledDiagramVirtualModel extends OpenflexoTestCase {

	private final String DIAGRAM_SPECIFICATION_NAME = "myDiagramSpecification";
	private final String DIAGRAM_SPECIFICATION_URI = "http://myDiagramSpecification";
	private final String PALETTE_NAME = "myDiagramSpecificationPalette";
	private final String PALETTE_ELEMENT_NAME = "myPaletteElement";

	private final String VIEWPOINT_NAME = "TestViewPoint";
	private final String VIEWPOINT_URI = "http://openflexo.org/test/TestViewPoint";

	public static DiagramTechnologyAdapter technologicalAdapter;
	public static FlexoServiceManager applicationContext;
	public static FlexoResourceCenter<?> resourceCenter;
	public static DiagramSpecificationRepository repository;
	public static FlexoEditor editor;

	public static DiagramSpecificationResource diagramSpecificationResource;
	public static DiagramPaletteResource paletteResource;

	public static DiagramPalette palette;
	public static DiagramPaletteElement diagramPaletteElement;

	public static ViewPoint viewPoint;
	public static ViewPointResource viewPointResource;
	public static TypedDiagramModelSlot typedDiagramModelSlot;
	public static VirtualModel virtualModel;
	public static DropScheme dropScheme;

	/**
	 * Initialize
	 */
	@Test
	@TestOrder(1)
	public void testInitialize() {

		log("testInitialize()");

		applicationContext = instanciateTestServiceManager(DiagramTechnologyAdapter.class);

		technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);

		// Looks for the first FileSystemBasedResourceCenter
		for (FlexoResourceCenter rc : applicationContext.getResourceCenterService().getResourceCenters() ){
			if (rc instanceof DirectoryResourceCenter && !rc.getResourceCenterEntry().isSystemEntry()){
				resourceCenter = (DirectoryResourceCenter) rc;
				break;
			}
		}
		assertNotNull(resourceCenter);

		repository = resourceCenter.getRepository(DiagramSpecificationRepository.class, technologicalAdapter);

		assertNotNull(repository);

		editor = new FlexoTestEditor(null, applicationContext);

		assertNotNull(applicationContext);
		assertNotNull(technologicalAdapter);
	}

	/**
	 * Test Create diagram specification resource
	 */
	@Test
	@TestOrder(2)
	public void testCreateDiagramSpecification() {

		log("testCreateDiagramSpecification()");

		CreateDiagramSpecification action = CreateDiagramSpecification.actionType.makeNewAction(repository.getRootFolder(), null, editor);
		action.setNewDiagramSpecificationName(DIAGRAM_SPECIFICATION_NAME);
		action.setNewDiagramSpecificationURI(DIAGRAM_SPECIFICATION_URI);

		action.doAction();

		assertTrue(action.hasActionExecutionSucceeded());

		diagramSpecificationResource = action.getNewDiagramSpecification().getResource();

		assertNotNull(diagramSpecificationResource);
		assertTrue(diagramSpecificationResource.getFlexoIODelegate().exists());

	}

	/**
	 * Test palettes
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(3)
	public void testCreatePalette() throws SaveResourceException {

		log("testCreatePalette()");

		CreateDiagramPalette action = CreateDiagramPalette.actionType.makeNewAction(diagramSpecificationResource.getDiagramSpecification(),
				null, editor);
		action.setNewPaletteName(PALETTE_NAME);

		action.doAction();

		assertTrue(action.hasActionExecutionSucceeded());

		paletteResource = action.getNewPalette().getResource();

		assertNotNull(paletteResource);
		assertTrue(paletteResource.getFlexoIODelegate().exists());
		assertTrue(diagramSpecificationResource.getDiagramPaletteResources().contains(paletteResource));

		assertEquals(1, diagramSpecificationResource.getDiagramSpecification().getPalettes().size());

		// Add palette element
		DiagramPalette palette = paletteResource.getDiagramPalette();

		diagramPaletteElement = paletteResource.getFactory().makeDiagramPaletteElement();
		diagramPaletteElement.setName(PALETTE_ELEMENT_NAME);
		ShapeGraphicalRepresentation shapeGR = paletteResource.getFactory().makeShapeGraphicalRepresentation();
		shapeGR.setShapeSpecification(paletteResource.getFactory().makeShape(ShapeType.RECTANGLE));
		shapeGR.setForeground(paletteResource.getFactory().makeForegroundStyle(Color.RED));
		shapeGR.setBackground(paletteResource.getFactory().makeColoredBackground(Color.BLUE));
		shapeGR.setX(10);
		shapeGR.setY(10);
		shapeGR.setWidth(100);
		shapeGR.setHeight(80);
		diagramPaletteElement.setGraphicalRepresentation(shapeGR);
		paletteResource.getDiagramPalette().addToElements(diagramPaletteElement);

		paletteResource.save(null);

	}

	/**
	 * Test the VP creation
	 */
	@Test
	@TestOrder(4)
	public void testCreateViewPoint() {

		log("testCreateViewPoint()");

		viewPoint = ViewPointImpl.newViewPoint(VIEWPOINT_NAME, VIEWPOINT_URI,
				((FileSystemBasedResourceCenter) resourceCenter).getDirectory(), serviceManager.getViewPointLibrary(), resourceCenter);
		viewPointResource = (ViewPointResource) viewPoint.getResource();
		// assertTrue(viewPointResource.getDirectory().exists());
		assertTrue(viewPointResource.getDirectory() != null);
		assertTrue(viewPointResource.getFlexoIODelegate().exists());
	}

	/**
	 * Test the VirtualModel creation
	 */
	@Test
	@TestOrder(5)
	public void testCreateVirtualModel() throws SaveResourceException {

		log("testCreateVirtualModel()");

		virtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel", viewPoint);
		assertTrue(ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) virtualModel.getResource()).getDirectory()).exists());
		assertTrue(((VirtualModelResource) virtualModel.getResource()).getFlexoIODelegate().exists());

		typedDiagramModelSlot = technologicalAdapter.makeModelSlot(TypedDiagramModelSlot.class, virtualModel);
		typedDiagramModelSlot.setMetaModelResource(diagramSpecificationResource);
		typedDiagramModelSlot.setName("diagram");
		assertNotNull(typedDiagramModelSlot);

		virtualModel.addToModelSlots(typedDiagramModelSlot);
		assertTrue(virtualModel.getModelSlots(TypedDiagramModelSlot.class).size() == 1);

		FlexoConcept flexoConcept = virtualModel.getFMLModelFactory().newInstance(FlexoConcept.class);
		virtualModel.addToFlexoConcepts(flexoConcept);

		CreateTechnologyRole createShapeRole = CreateTechnologyRole.actionType.makeNewAction(flexoConcept, null, editor);
		createShapeRole.setRoleName("shape");
		createShapeRole.setFlexoRoleClass(ShapeRole.class);
		createShapeRole.doAction();
		assertTrue(createShapeRole.hasActionExecutionSucceeded());

		ShapeRole role = (ShapeRole) createShapeRole.getNewFlexoRole();
		FMLModelFactory factory = flexoConcept.getFMLModelFactory();
		ShapeGraphicalRepresentation shapeGR = factory.newInstance(ShapeGraphicalRepresentation.class);
		Rectangle rectangleShape = factory.newInstance(Rectangle.class);
		shapeGR.setShapeSpecification(rectangleShape);
		role.setGraphicalRepresentation(shapeGR);

		CreateFlexoBehaviour createDropScheme = CreateFlexoBehaviour.actionType.makeNewAction(flexoConcept, null, editor);
		createDropScheme.setFlexoBehaviourName("drop");
		createDropScheme.setFlexoBehaviourClass(DropScheme.class);
		createDropScheme.doAction();
		assertTrue(createDropScheme.hasActionExecutionSucceeded());
		dropScheme = (DropScheme) createDropScheme.getNewFlexoBehaviour();

		CreateEditionAction createAddShape = CreateEditionAction.actionType.makeNewAction(dropScheme.getControlGraph(), null, editor);
		// createAddShape.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
		createAddShape.setModelSlot(typedDiagramModelSlot);
		createAddShape.setEditionActionClass(AddShape.class);
		createAddShape.doAction();
		assertTrue(createAddShape.hasActionExecutionSucceeded());

		FMLDiagramPaletteElementBinding newBinding = virtualModel.getFMLModelFactory().newInstance(FMLDiagramPaletteElementBinding.class);
		newBinding.setPaletteElement(diagramPaletteElement);
		newBinding.setBoundFlexoConcept(flexoConcept);
		newBinding.setDropScheme(dropScheme);

		typedDiagramModelSlot.addToPaletteElementBindings(newBinding);

		virtualModel.getResource().save(null);

		System.out.println(virtualModel.getFMLModelFactory().stringRepresentation(virtualModel));

		assertTrue(virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE));
		assertEquals(typedDiagramModelSlot, FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(virtualModel));
	}

	/**
	 * Reload the DiagramSpecification and VirtualModel
	 */
	@Test
	@TestOrder(6)
	public void testReloadDiagramSpecificationAndVirtualModel() {

		log("testReloadDiagramSpecification()");

		applicationContext = instanciateTestServiceManager(DiagramTechnologyAdapter.class);

		technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		resourceCenter = applicationContext.getResourceCenterService().getResourceCenters().get(0);
		repository = resourceCenter.getRepository(DiagramSpecificationRepository.class, technologicalAdapter);

		File newDirectory = new File(((FileSystemBasedResourceCenter) resourceCenter).getDirectory(), "CopyFromPreviousRC");
		newDirectory.mkdirs();

		try {
			File dsDir = new File(newDirectory,
					ResourceLocator.retrieveResourceAsFile(diagramSpecificationResource.getDirectory()).getName());
			dsDir.mkdirs();
			FileUtils.copyContentDirToDir(ResourceLocator.retrieveResourceAsFile(diagramSpecificationResource.getDirectory()), dsDir);
			File vpDir = new File(newDirectory, ResourceLocator.retrieveResourceAsFile(viewPointResource.getDirectory()).getName());
			vpDir.mkdirs();
			FileUtils.copyContentDirToDir(ResourceLocator.retrieveResourceAsFile(viewPointResource.getDirectory()), vpDir);
			// We wait here for the thread monitoring ResourceCenters to detect new files
			((FileSystemBasedResourceCenter) resourceCenter).performDirectoryWatchingNow();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DiagramSpecificationResource retrievedDSResource = repository.getResource(DIAGRAM_SPECIFICATION_URI);
		assertNotNull(retrievedDSResource);
		assertEquals(1, retrievedDSResource.getDiagramSpecification().getPalettes().size());

		ViewPointResource retrievedVPResource = serviceManager.getViewPointLibrary().getViewPointResource(VIEWPOINT_URI);
		assertNotNull(retrievedVPResource);

		assertEquals(1, retrievedVPResource.getVirtualModelResources().size());
		VirtualModelResource retrievedVMResource = retrievedVPResource.getVirtualModelResources().get(0);

		assertTrue(FMLControlledDiagramVirtualModelNature.INSTANCE.hasNature(retrievedVMResource.getVirtualModel()));

		TypedDiagramModelSlot retrievedDiagramMS = FMLControlledDiagramVirtualModelNature
				.getTypedDiagramModelSlot(retrievedVMResource.getVirtualModel());
		assertNotNull(retrievedDiagramMS);
		assertEquals(1, retrievedDiagramMS.getPaletteElementBindings().size());

		FMLDiagramPaletteElementBinding retrievedBinding = retrievedDiagramMS.getPaletteElementBindings().get(0);
		assertNotNull(retrievedBinding.getPaletteElement());
		assertNotNull(retrievedBinding.getDropScheme());

	}
}
