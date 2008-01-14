package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;

public class SectionProcessor extends SiteElementPathProcessor {

    public SectionProcessor add(ItemProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new SectionContext(parentContext);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        SectionContext ownContext = (SectionContext) context;

        int i;
        for (i = 0; ; i++) {
            String current = provider.peek(i);
            
            if (! ownContext.addSection(current)) {
                break;
            }
        }

        while (i > 1) {
            provider.next();
            i--;
        }
        
        return !ownContext.getSections().isEmpty();
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider)
            throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            SectionContext ownContext = (SectionContext) context;
            Section section = ownContext.getLastSection();
            
            if (section == null) {
                return false;
            }
            
            String contextURI = ownContext.getContextURI();
            String url = String.format(contextURI, "section", section.getIdInternal());

//            if (section instanceof FunctionalitySection) {
//            	HtmlLink sectionLink = new HtmlLink();
//            	sectionLink.setContextRelative(false);
//            	sectionLink.setUrl(url);
//            	
//            	FunctionalitySection fSection = (FunctionalitySection) section;
//            	Functionality functionality = fSection.getFunctionality();
//            	String path = functionality.getPublicPath();
//
//                HtmlLink link = new HtmlLink();
//                link.setContextRelative(false);
//				link.setUrl(path);
//
//	            if (functionality.isParameterized()) {
//	                for (String parameter : functionality.getParameterList()) {
//	                    for (String value : sectionLink.getParameterValues(parameter)) {
//	                        link.addParameter(parameter, value);
//	                    }
//	                }
//	            }
//	            
//	            return doForward(context, link.calculateUrl());
//            }

            	return doForward(context, url);

        }
    }

    public static class SectionContext extends ProcessingContext {

        private List<Section> sections;
        
        public SectionContext(ProcessingContext parent) {
            super(parent);
            
            this.sections = new ArrayList<Section>();
        }

        public boolean addSection(String name) {
            for (Section section : getCurrentPossibilities()) {
                String pathName = getElementPathName(section);
                
                if (pathName == null) {
                    continue;
                }
                
                if (pathName.equalsIgnoreCase(name)) {
                    addSection(section);
                    return true;
                }
            }
            
            return false;
        }
        
        public Site getSite() {
            return ((SiteContext) getParent()).getSite();
        }
        
        public String getContextURI() {
            return ((SiteContext) getParent()).getSiteBasePath() + "&sectionID=%s";
        }
        
        private List<Section> getCurrentPossibilities() {
            Section section = getLastSection();
            
            if (section == null) {
                Site site = getSite();
                if (site == null) {
                    return Collections.emptyList();
                }
                List<Section> sections = new ArrayList<Section>();
                for(Content content : site.getAllAssociatedSections()) {
                    sections.add((Section)content);
                }
                return sections;
            }
            else {
                return section.getAssociatedSections();
            }
        }

        public Section getLastSection() {
            List<Section> sections = getSections();
            
            if (sections.isEmpty()) {
                return null;
            }
            else {
                return sections.get(sections.size() - 1);
            }
        }

        public void addSection(Section section) {
            this.sections.add(section);
        }

        public List<Section> getSections() {
            return this.sections;
        }
        
    }
    
    public static String getSectionPath(Section section) {
        if (section == null) {
            return "";
        }
        else {
            return getSectionPath(section.getSuperiorSection()) + "/" + getElementPathName(section);
        }
    }
    
}
