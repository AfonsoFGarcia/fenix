package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.FunctionalitySection;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.publico.SimpleFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.renderers.functionalities.MenuRenderer;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlList;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

/**
 * This renderer is responsible for presenting a
 * {@link net.sourceforge.fenixedu.domain.Site} as a menu. It uses all the
 * sections defined in the site and it's template to generate the menu and
 * expands some sections if they were selected. The renderer uses parameters to
 * interact with the environment, so some parameters like <tt>sectionID</tt>
 * are appended automatically and others, like the one indicated by
 * {@link #setContextParam(String)}, are appened to mantain the required
 * context for the target site.
 */
public class SiteMenuRenderer extends OutputRenderer {

    private boolean contextRelative;
    private boolean moduleRelative;
    private String sectionUrl;
    private String contextParam;
    private String empty;

    public SiteMenuRenderer() {
        super();
        
        setModuleRelative(true);
        setContextRelative(true);
    }
    
    public boolean isContextRelative() {
        return this.contextRelative;
    }

    /**
     * Indicates that the url for the sections ir not relative to the
     * applications context. This can be usefull to redirect to another
     * application or the remove dependencies on Struts.
     * 
     * @property
     */
    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    public boolean isModuleRelative() {
        return this.moduleRelative;
    }

    /**
     * Indicates that the link for sections is not relative to the module.
     * 
     * @property
     */
    public void setModuleRelative(boolean moduleRelative) {
        this.moduleRelative = moduleRelative;
    }

    public String getSectionUrl() {
        return this.sectionUrl;
    }

    /**
     * The url of the action responsible for showing the section content.
     * 
     * @property
     */
    public void setSectionUrl(String sectionUrl) {
        this.sectionUrl = sectionUrl;
    }
    
    public String getContextParam() {
        return this.contextParam;
    }

    /**
     * The name of the parameters that provides a context for this site.
     * 
     * @property
     */
    public void setContextParam(String contextParam) {
        this.contextParam = contextParam;
    }

    public String getEmpty() {
		return empty;
	}

    /**
     * Decides what to show when there are no sections to include in the menu.
     * 
     * @property
     */
	public void setEmpty(String empty) {
		this.empty = empty;
	}

	@Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object == null) {
                    return new HtmlText();
                }

                HtmlList list = new HtmlList();
                
                HttpServletRequest request = getContext().getViewState().getRequest();
                FunctionalityContext context = (FunctionalityContext) request.getAttribute(FunctionalityContext.CONTEXT_KEY);
                if (context == null) {
                    context = new SimpleFunctionalityContext(request);
                }
                  
                List<Section> sections = getSections(object);
                
                if (sections.isEmpty()) {
                	return generateEmpty();
                }
                else {
                	addSiteSections(context, object, sections, list);
                    return list;
                }
            }

            private HtmlComponent generateEmpty() {		
				return new HtmlText(getEmpty(), false);
			}

			private void addSiteSections(FunctionalityContext context,
                    Object object, List<Section> sections, HtmlList list) {

                for (Section section : sections) {
                    if (!section.isVisible(context)) {
                        continue;
                    }

                    HtmlListItem item = list.createItem();
                    HtmlComponent nameComponent = createSectionComponent(context, section);

                    // TODO: make a better design and add configuration to this renderer
                    if (! isTopSection(section)) {
                        nameComponent.setStyle("margin-left: 1em;");
                    }

                    item.addChild(nameComponent);

                    if (isSelectedSection(section)) {
                        List<Section> subSections = Site.getOrderedSections(getSubSections(object, section));

                        if (subSections != null && !subSections.isEmpty()) {
                            HtmlList subList = new HtmlList();

                            addSiteSections(context, object, subSections, subList);
                            item.addChild(subList);
                        }
                    }
                }
            }
            
            private boolean isSelectedSection(Section current) {
                Section selectedSection = getSelectedSection();
                
                if (selectedSection == null) {
                    return false;
                }
                
                for (Section section = selectedSection; section != null; section = section.getSuperiorSection()) {
                    if (section == current) {
                        return true;
                    }
                }
                
                return false;
            }
            
            private Section getSelectedSection() {
                HttpServletRequest request = getContext().getViewState().getRequest();
                String idValue = request.getParameter("sectionID");
                
                if (idValue == null) {
                    return null;
                }
                else {
                    Integer sectionId = new Integer(idValue);
                    return RootDomainObject.getInstance().readSectionByOID(sectionId);
                }
            }
            
            private HtmlComponent createSectionComponent(FunctionalityContext context, Section section) {
                if (section instanceof FunctionalitySection) {
                   Functionality functionality = ((FunctionalitySection) section).getFunctionality();
                   HtmlComponent component = MenuRenderer.getFunctionalityNameComponent(context, functionality, true);
                   
                   if (component instanceof HtmlLink) {
                	   ((HtmlLink) component).setParameter("sectionID", section.getIdInternal());
                   }
                   
                   return component;
                }
                else {
                    HtmlLink link = new HtmlLink();
                
                    link.setModuleRelative(isModuleRelative());
                    link.setContextRelative(isContextRelative());
                    link.setUrl(getSectionUrl());
                    link.setParameter("sectionID", section.getIdInternal());
                    
                    String contextParamValue = getContextParamValue();
                    if (contextParamValue != null) {
                        link.setParameter(getContextParam(), contextParamValue);
                    }
                    
                    link.setBody(new HtmlText(section.getName().getContent()));
                    
                    return link;
                }
            }

            private String getContextParamValue() {
            	String contextParam = getContextParam();
            	
            	if (contextParam == null) {
            		return null;
            	}
            	
                HttpServletRequest request = getContext().getViewState().getRequest();;
				return request.getParameter(contextParam);
            }

        };
    }

    /**
     * @return the list of sections to render
     */
    protected List<Section> getSections(Object object) {
        return getSite(object).getAllOrderedTopLevelSections();
    }

    /**
     * @return <code>true</code> if the section is treated as a top section
     */
    protected boolean isTopSection(Section section) {
        return section.getSuperiorSection() == null;
    }

	protected Site getSite(Object object) {
		return (Site) object;
	}

	protected List<Section> getSubSections(Object object, Section section) {
		return getSite(object).getAssociatedSections(section);
	}

}
