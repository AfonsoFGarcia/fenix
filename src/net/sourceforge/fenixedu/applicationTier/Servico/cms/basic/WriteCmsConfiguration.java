/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms.basic;


import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 10:03:10,15/Nov/2005
 * @version $Id$
 */
public class WriteCmsConfiguration extends CmsService
{

	static public class CmsConfiguration
	{
		public String smtpServerAddress;

		public Boolean filterNonTextualAttachments;

		public String mailingListsHost;

		public String conversationReplyMarkers;

		public Integer maxAttachmentSize; /* in bytes */

		public Integer maxMessageSize; /* in bytes */
	}

	public Cms run(String name,CmsConfiguration changes) throws ExcepcaoPersistencia
	{
		Cms editedCms = persistentSupport.getIPersistentCms().readCmsByName(name);
		editedCms.getConfiguration().setSmtpServerAddress(changes.smtpServerAddress);
		editedCms.getConfiguration().setFilterNonTextualAttachments(changes.filterNonTextualAttachments);
		editedCms.getConfiguration().setMailingListsHost(changes.mailingListsHost);
		editedCms.getConfiguration().setConversationReplyMarkers(changes.conversationReplyMarkers);
		editedCms.getConfiguration().setMaxAttachmentSize(changes.maxAttachmentSize);
		editedCms.getConfiguration().setMaxMessageSize(changes.maxMessageSize);
		
		return editedCms;
	}	
}
