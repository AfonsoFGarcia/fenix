/**
 * 
 */


package net.sourceforge.fenixedu.infrastructuralTier;


import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.ReadMailingListsWithOutgoingMails;
import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.stm.Transaction;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 10:24:51,20/Out/2005
 * @version $Id$
 */
public class MailSender
{

	public static void main(String[] args)
	{
		System.out.println("#######Ol� mundo cruel");
		SuportePersistenteOJB.fixDescriptors();
		Transaction.begin();
		MailSender sender = new MailSender();
		sender.run();
		Transaction.commit();
		System.out.println("######## E COMMITEI !!!!!!!!!!!!!! :D :D: D:D");
		System.out.println("#######Adeus mundo cruel");
		
	}

	public void run()
	{
		System.out.println("$$$$$$$$$$Eu senhor todo poderoso vou ver se e preciso comprar escravos");
		try
		{
			ReadMailingListsWithOutgoingMails service = new ReadMailingListsWithOutgoingMails();
			Collection<MailingList> mailingLists = service.run();
			System.out.println("Ha " + mailingLists.size() + " queues para tratar");
			for (MailingList mailingList : mailingLists)
			{
				this.send(mailingList);

			}
		}
		catch (Exception e)
		{
			System.out.println("EXCEPCAAAAAAAAAAAAAAAO");
			e.printStackTrace();
			System.out.println("###############EXCEPCAAAAAAAAAAAAAAAO##############");
		}
	}

	public void send(MailingList mailingList)
	{
		System.out.println("#######Vou come�ar a tratar as cenas (ml:" + mailingList.getName()
				+ ")!!!!!!!!!!, sou o gajo que de facto envia");

		for (MailMessage message : mailingList.getQueue().getMessages())
		{
			// enviar !!!!!!!!!! n esquecer do REPLY TO LIST se for o caso
                    mailingList.getQueue().removeMessages(message);
                    String hello = mailingList.getCms().getConfiguration().getSmtpServerAddressToUse();
                    System.out.println("Deveria estar a usar este SMTP: " + hello);
		}
	}
}
