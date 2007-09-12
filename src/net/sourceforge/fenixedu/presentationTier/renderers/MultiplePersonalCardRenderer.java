package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.contexts.OutputContext;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class MultiplePersonalCardRenderer extends OutputRenderer {

    private String noPhotoAvailableImage;

    private String formatImageURL;

    private String imageDescription;

    private String imageName;

    private String imageHeight;

    private String imageWidth;

    private String photoCellClasses;

    private String infoCellClasses;

    private String classes;

    private boolean contextRelative;

    private String eachLayout;

    private String eachSchema;

    private String divClasses;
    
    private String sortBy;

    
    public String getDivClasses() {
        return divClasses;
    }

    public void setDivClasses(String divClasses) {
        this.divClasses = divClasses;
    }

    public String getClasses() {
	return classes;
    }

    public void setClasses(String classes) {
	this.classes = classes;
    }

    public boolean isContextRelative() {
	return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
	this.contextRelative = contextRelative;
    }

    public String getEachLayout() {
	return eachLayout;
    }

    public void setEachLayout(String eachLayout) {
	this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
	return eachSchema;
    }

    public void setEachSchema(String eachSchema) {
	this.eachSchema = eachSchema;
    }

    public String getFormatImageURL() {
	return formatImageURL;
    }

    public void setFormatImageURL(String formatImageURL) {
	this.formatImageURL = formatImageURL;
    }

    public String getImageDescription() {
	return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
	this.imageDescription = imageDescription;
    }

    public String getImageHeight() {
	return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
	this.imageHeight = imageHeight;
    }

    public String getImageName() {
	return imageName;
    }

    public void setImageName(String imageName) {
	this.imageName = imageName;
    }

    public String getImageWidth() {
	return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
	this.imageWidth = imageWidth;
    }

    public String getInfoCellClasses() {
	return infoCellClasses;
    }

    public void setInfoCellClasses(String infoCellClasses) {
	this.infoCellClasses = infoCellClasses;
    }

    public String getNoPhotoAvailableImage() {
	return noPhotoAvailableImage;
    }

    public void setNoPhotoAvailableImage(String noPhotoAvailableImage) {
	this.noPhotoAvailableImage = noPhotoAvailableImage;
    }

    public String getPhotoCellClasses() {
	return photoCellClasses;
    }

    public void setPhotoCellClasses(String photoCellClasses) {
	this.photoCellClasses = photoCellClasses;
    }

    public String getSortBy() {
	return sortBy;
    }

    public void setSortBy(String sortBy) {
	this.sortBy = sortBy;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	List<Person> people = (getSortBy() == null) ? (List<Person>) object : RenderUtils
		.sortCollectionWithCriteria((List<Person>) object, getSortBy());
	return new MultiplePersonalCardLayout(people);
    }

    public class MultiplePersonalCardLayout extends Layout {

	List<Person> people;

	private PersonalCardRenderer getCardRenderer() {

	    PersonalCardRenderer card = new PersonalCardRenderer();
	    card.setSubLayout(getEachLayout());
	    card.setSubSchema(getEachSchema());
	    card.setNoPhotoAvailableImage(getNoPhotoAvailableImage());
	    card.setFormatImageURL(getFormatImageURL());
	    card.setImageDescription(getImageDescription());
	    card.setImageName(getImageName());
	    card.setImageHeight(getImageHeight());
	    card.setImageWidth(getImageWidth());
	    card.setPhotoCellClasses(getPhotoCellClasses());
	    card.setInfoCellClasses(getInfoCellClasses());
	    card.setClasses(getDivClasses());
	    card.setContextRelative(isContextRelative());
	    return card;
	}

	public MultiplePersonalCardLayout(List<Person> people) {
	    this.people = people;
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    HtmlBlockContainer container = new HtmlBlockContainer();
	    
	    PresentationContext newContext = getContext().createSubContext(
			getContext().getMetaObject());
	    newContext.setProperties(new Properties());
	    newContext.setRenderMode(RenderMode.getMode("output"));
	    PersonalCardRenderer renderer = getCardRenderer();
	   
	    for(Person person : people) {
		container.addChild(RenderKit.getInstance().renderUsing(renderer, newContext, person,
			Person.class));
	    }
	    return container;
	}
    }
}
