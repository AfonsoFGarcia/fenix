

package net.sourceforge.fenixedu.domain.cms.messaging;


import java.util.Comparator;
import java.util.Iterator;

import javax.mail.MessagingException;

import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.cms.Content;
import net.sourceforge.fenixedu.domain.cms.predicates.ContentAssignableClassPredicate;
import net.sourceforge.fenixedu.domain.cms.predicates.ContentPredicate;

import org.apache.commons.collections.iterators.FilterIterator;

public class MailingList extends MailingList_Base
{
	public MailingList()
	{
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	private class LastConversationsFirstComparator implements Comparator
	{
		public int compare(Object arg0, Object arg1)
		{
			MailConversation conversation1 = (MailConversation) arg0;
			MailConversation conversation2 = (MailConversation) arg1;
			return - conversation1.getCreationDate().compareTo(conversation2.getCreationDate());
		}
	}
	
	private class LastMessagesFirstComparator implements Comparator
	{
		public int compare(Object arg0, Object arg1)
		{
			MailMessage message1 = (MailMessage) arg0;
			MailMessage message2 = (MailMessage) arg1;
			return - message1.getCreationDate().compareTo(message2.getCreationDate());
		}
	}
	
	private class ConversationIsAboutSubject extends ContentPredicate
	{
		private String subject;

		public ConversationIsAboutSubject(String subject)
		{
			this.subject = subject;
		}

		public boolean evaluate(Content mailConversation)
		{
			boolean result = false;
			MailConversation conversation = (MailConversation) mailConversation;
			result = conversation.isAboutSubject(this.subject);
			return result;
		}

	}

	public Iterator<MailConversation> getMailConversationsIterator()
	{
		return new OrderedIterator(new FilterIterator(this.getChildrenIterator(), new ContentAssignableClassPredicate(MailConversation.class)),new LastConversationsFirstComparator());
	}

	public Iterator<MailConversation> getConversationsOnSubjectIterator(String subject)
	{
		return new OrderedIterator(new FilterIterator(this.getMailConversationsIterator(), new ConversationIsAboutSubject(subject)),new LastConversationsFirstComparator());
	}
	
	public Iterator<MailMessage> getMailMessagesIterator()
	{
		return new OrderedIterator(new FilterIterator(this.getChildrenIterator(), new ContentAssignableClassPredicate(MailMessage.class)),new LastMessagesFirstComparator());
	}

	public MailConversation getMostRecentMailConversationOnSubject(String subject)
	{
		Iterator<MailConversation> conversationsIterator = this.getConversationsOnSubjectIterator(subject);
		MailConversation result = null;
		while (conversationsIterator.hasNext())
		{
			MailConversation currentConversation = conversationsIterator.next();
			if (result == null || result.getCreationDate().before(currentConversation.getCreationDate()))
			{
				result = currentConversation;
			}
		}

		return result;
	}

	public int getMailConversationsCount()
	{
		int count =0;
		Iterator<MailConversation> mailConversationsIterator = this.getMailConversationsIterator();
		while(mailConversationsIterator.hasNext())
		{
			count++;
			mailConversationsIterator.next();
		}
		return count;
	}

	public int getMailMessagesCount()
	{
		int count =0;
		Iterator<MailMessage> mailMessagesIterator = this.getMailMessagesIterator();
		while(mailMessagesIterator.hasNext())
		{
			count++;
			mailMessagesIterator.next();
		}
		return count;		
	}

	public int getSize() throws MessagingException
	{
		int size=0;
		Iterator<MailConversation> mailConversationsIterator = this.getMailConversationsIterator();
		while(mailConversationsIterator.hasNext())
		{
			MailConversation conversation = mailConversationsIterator.next();
			size+=conversation.getSize();
		}
		return size;
	}
	
	@Override
	public void delete()
	{
		Iterator<MailConversation> iterator = this.getMailConversationsIterator();
		while (iterator.hasNext())
		{
			MailConversation conversation = iterator.next();
			if(conversation.getMailingListsCount()==0)
				conversation.delete();
		}
        removeRootDomainObject();
		super.delete();
	}
}