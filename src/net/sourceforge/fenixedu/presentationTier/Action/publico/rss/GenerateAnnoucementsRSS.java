package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.nava.informa.core.ChannelExporterIF;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.exporters.RSS_2_0_Exporter;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;

public class GenerateAnnoucementsRSS extends RSSAction{
	
 
	@Override
	protected ChannelIF getRSSChannel(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Object[] args = {ExecutionCourse.class, Integer.valueOf(id)};
		ExecutionCourse executionCourse = executionCourse = (ExecutionCourse) ServiceManagerServiceFactory.executeService(null, "ReadDomainObject", args);
		ChannelBuilder builder = new ChannelBuilder();
    	ChannelIF channel = builder.createChannel(executionCourse.getNome());
    	channel.setDescription("An�ncios da disciplina " + executionCourse.getNome());
    	for (Announcement announcement : executionCourse.getSite().getAssociatedAnnouncements()) {
			ItemIF item = new Item();
			item.setTitle(announcement.getTitle());
			item.setDate(announcement.getLastModifiedDate());
			item.setDescription(announcement.getInformation());
			String context = PropertiesManager.getProperty("app.context");
			if(context == null) {
				context = "";
			}
			item.setLink(new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "/" + context + "/publico/viewSite.do?method=announcements&objectCode=" + executionCourse.getSite().getIdInternal() + "&executionPeriodOID=" + executionCourse.getExecutionPeriod().getIdInternal() + "#" + announcement.getIdInternal()));
			channel.addItem(item);
		}
    	return channel;
	}

}
