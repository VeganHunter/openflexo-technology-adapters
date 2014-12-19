package org.openflexo.technologyadapter.powerpoint.controller;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.foundation.viewpoint.annotations.FIBPanel;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

/**
 * Generic wizard step to be called when requiring a slide selection from a PPT file<br>
 * Also allows to provides name and title for selected slide
 * 
 * @author sylvain
 *
 */
@FIBPanel("Fib/Wizard/ChoosePPTSlide.fib")
public class ChoosePPTSlide extends WizardStep {

	private static final Logger logger = Logger.getLogger(ChoosePPTSlide.class.getPackage().getName());

	private final FlexoController controller;

	private String diagramName;
	private String diagramTitle;

	private SlideShow selectedSlideShow;
	private ArrayList<Slide> currentSlides;
	private File file;
	private Slide slide;

	public ChoosePPTSlide(FlexoController controller) {
		this.controller = controller;
	}

	public FlexoController getController() {
		return controller;
	}

	public ApplicationContext getServiceManager() {
		return getController().getApplicationContext();
	}

	@Override
	public String getTitle() {
		return FlexoLocalization.localizedForKey("choose_powerpoint_slide");
	}

	@Override
	public boolean isValid() {

		if (getFile() == null || !getFile().exists()) {
			setIssueMessage(FlexoLocalization.localizedForKey("please_select_a_valid_powerpoint_file"), IssueMessageType.ERROR);
			return false;
		}

		else if (getSlide() == null) {
			setIssueMessage(FlexoLocalization.localizedForKey("please_select_a_slide_to_import"), IssueMessageType.ERROR);
			return false;
		}

		else if (StringUtils.isEmpty(getDiagramName())) {
			setIssueMessage(FlexoLocalization.localizedForKey("please_set_diagram_name"), IssueMessageType.ERROR);
			return false;
		}

		return true;
	}

	public void loadSlideShow() {
		try {
			FileInputStream fis = new FileInputStream(getFile());
			selectedSlideShow = new SlideShow(fis);
			if (currentSlides == null) {
				currentSlides = new ArrayList<Slide>();
			} else {
				currentSlides.clear();
			}
			for (Slide slide : selectedSlideShow.getSlides()) {
				currentSlides.add(slide);
			}
			getPropertyChangeSupport().firePropertyChange("selectedSlideShow", null, selectedSlideShow);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		if ((file == null && this.file != null) || (file != null && !file.equals(this.file))) {
			boolean wasValid = isValid();
			File oldValue = this.file;
			this.file = file;
			if (file != null) {
				loadSlideShow();
			}
			getPropertyChangeSupport().firePropertyChange("file", oldValue, file);
			getPropertyChangeSupport().firePropertyChange("currentSlides", null, getCurrentSlides());
			getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
			checkValidity();
		}
	}

	public SlideShow getSelectedSlideShow() {
		return selectedSlideShow;
	}

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {

		boolean wasValid = isValid();
		this.slide = slide;
		getPropertyChangeSupport().firePropertyChange("slide", null, getSlide());
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("diagramName", null, getDiagramName());
		getPropertyChangeSupport().firePropertyChange("diagramTitle", null, getDiagramTitle());
		checkValidity();
	}

	public ArrayList<Slide> getCurrentSlides() {
		return currentSlides;
	}

	public ImageIcon getMiniature(Slide s) {
		return getScreenShot(s, 75);
	}

	public ImageIcon getOverview(Slide s) {
		return getScreenShot(s, 400);
	}

	public ImageIcon getScreenShot(Slide s, double size) {
		if (s != null && s.getSlideShow() != null) {
			try {
				Dimension d = s.getSlideShow().getPageSize();
				BufferedImage i = new BufferedImage((int) size, (int) (size * d.height / d.width), BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = i.createGraphics();
				graphics.transform(AffineTransform.getScaleInstance(size / d.width, size / d.width));
				s.draw(graphics);
				return new ImageIcon(i);
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.warning("Some fonts are cannot be previewed (Calibri, Gothic MS)");
			} catch (Exception e) {
				logger.warning("Unable to create a preview for the slide " + s.getSlideNumber());
			}
		}
		return null;
	}

	public String getDiagramName() {
		if (StringUtils.isEmpty(diagramName) && getSlide() != null) {
			return getFile().getName() + "-Slide" + getSlide().getSlideNumber();
		}
		return diagramName;
	}

	public void setDiagramName(String diagramName) {
		if ((diagramName == null && this.diagramName != null) || (diagramName != null && !diagramName.equals(this.diagramName))) {
			String oldValue = this.diagramName;
			this.diagramName = diagramName;
			getPropertyChangeSupport().firePropertyChange("diagramName", oldValue, diagramName);
			getPropertyChangeSupport().firePropertyChange("diagramTitle", oldValue, diagramName);
			checkValidity();
		}
	}

	public String getDiagramTitle() {
		if (StringUtils.isEmpty(diagramTitle)) {
			return getDiagramName();
		}
		return diagramTitle;
	}

	public void setDiagramTitle(String diagramTitle) {
		if ((diagramTitle == null && this.diagramTitle != null) || (diagramTitle != null && !diagramTitle.equals(this.diagramTitle))) {
			String oldValue = this.diagramTitle;
			this.diagramTitle = diagramTitle;
			getPropertyChangeSupport().firePropertyChange("diagramTitle", oldValue, diagramTitle);
			checkValidity();
		}
	}

}