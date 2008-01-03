package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MenuEntry;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.renderers.functionalities.MenuRenderer;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.Face;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlLinkWithPreprendedComment;
import net.sourceforge.fenixedu.renderers.components.HtmlList;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.MultiLanguageString;

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
         * Decides what to show when there are no sections to include in the
         * menu.
         * 
         * @property
         */
    public void setEmpty(String empty) {
	this.empty = empty;
    }

    protected boolean accessTemplate() {
	return true;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    public HtmlComponent createComponent(Object object, Class type) {
		if (object == null) {
		    return new HtmlText();
		}

		HtmlList list = new HtmlList();
		HttpServletRequest request = getContext().getViewState().getRequest();
		FilterFunctionalityContext context = (FilterFunctionalityContext) request
			.getAttribute(FunctionalityContext.CONTEXT_KEY);

		if (context == null) {
		    context = new FilterFunctionalityContext(request);
		}

		Collection<MenuEntry> entries = getEntries(object);

		if (entries.isEmpty()) {
		    return generateEmpty();
		}

		createList(list, context, entries);
		return list;
	    }

	    public void createList(HtmlList list, FilterFunctionalityContext context, Collection<MenuEntry> entries) {
		for (MenuEntry entry : entries) {
		    if (!entry.isVisible()) {
			continue;
		    }

		    Content content = entry.getReferingContent();
		    if (!(content instanceof Item)) {
			HtmlListItem item = list.createItem();
			item.addChild(generateComponent(context, content, true));

			if (isSelectedContent(content, context) && !entry.getChildren().isEmpty()) {
			    HtmlList subMenu = new HtmlList();
			    item.addChild(subMenu);
			    createList(subMenu, context, entry.getChildren());
			}
		    }
		}
	    }

	    private HtmlLink generateLink(String url, HtmlComponent body) {
		HtmlLink link = new HtmlLinkWithPreprendedComment(ContentInjectionRewriter.HAS_CONTEXT_PREFIX_STRING);

		link.setContextRelative(false);
		link.setUrl(url);
		link.setBody(body);

		String contextParamValue = getContextParamValue();
		if (contextParamValue != null) {
		    link.setParameter(getContextParam(), contextParamValue);
		}

		return link;

	    }

	    public HtmlComponent generateComponent(FilterFunctionalityContext context, Content content, boolean canMakeLink) {

		HtmlText text = new HtmlText(content.getName().getContent());
		text.setFace(Face.STANDARD);
		HtmlComponent component = text;

		if (content.isAvailable()) {
		    component = generateLink(getPath(context, content), component);
		}

		MultiLanguageString title = content.getTitle();
		if (title != null && !title.isEmpty()) {
		    component.setTitle(title.getContent());
		}

		return component;
	    }

	    private HtmlComponent generateEmpty() {
		return new HtmlText(getEmpty(), false);
	    }

	    private boolean isSelectedContent(Content current, FunctionalityContext context) {
		Container selectedContainer = context.getSelectedContainer();
		Container container = (Container) context.getLastContentInPath(Site.class);
		return selectedContainer != null
			&& (selectedContainer == current || (container != selectedContainer && !selectedContainer.getPathTo(
				current).isEmpty()));
	    }

	    private String getContextParamValue() {
		String contextParam = getContextParam();

		if (contextParam == null) {
		    return null;
		}

		HttpServletRequest request = getContext().getViewState().getRequest();
		return request.getParameter(contextParam);
	    }

	};
    }

    protected Collection<MenuEntry> getEntries(Object object) {
	return getSite(object).getMenu();
    }

    protected Site getSite(Object object) {
	return (Site) object;
    }

    protected String getPath(FilterFunctionalityContext context, Content content) {
	return MenuRenderer.findPathFor(context.getRequest().getContextPath(), content, context, subPath(context
		.getSelectedContainer(), content));
    }

    private List<String> subPath(Container start, Content end) {
	List<Content> contents = start.getPathTo(end);
	List<String> subPaths = new ArrayList<String>();
	if (contents.size() > 2) {
	    for (Content content : contents.subList(1, contents.size() - 1)) {
		subPaths.add(content.getName().getContent());
	    }
	}
	return subPaths;
    }
}
