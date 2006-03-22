/**
 * 
 */


package net.sourceforge.fenixedu.domain.cms.messaging.email;


import java.io.UnsupportedEncodingException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 13:17:42,8/Fev/2006
 * @version $Id$
 */
public class EMailAddress
{
	private String user;
	private static final String validationPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$";
	private String domain;

	private String personalName;

	public EMailAddress()
	{

	}

	public EMailAddress(String user, String domain, String personalName)
	{
		this(user, domain);
		this.personalName = personalName;
	}

	public EMailAddress(String user, String domain)
	{
		this.user = user;
		this.domain = domain;
	}

	public String getDomain()
	{
		return domain;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public boolean isValid()
	{
		return true;
	}

	public static boolean isValid(String address)
	{
		boolean result = false;
		try
		{
			if (address.startsWith("--"))
			{
				int a=2;
				a++;
				a--;
			}
			InternetAddress javaxAddress = new InternetAddress(address);
			result = address.matches(EMailAddress.validationPattern);
			
		}
		catch (AddressException e)
		{
			// thats fine
		}
		return result;
	}
	
	public static boolean isValid(String personalName, String address)
	{
		boolean result = false;
		try
		{
			InternetAddress javaxAddress = new InternetAddress(address,personalName);
			result = address.matches(EMailAddress.validationPattern);
		}
		catch (UnsupportedEncodingException e) {
			// thats also fine			
		}
		return result;
	}

	public EMailAddress(String address)
	{
		String[] components = address.split("@");
		this.user=components[0];
		this.domain=components[1];

	}

	public String getPersonalName()
	{
		return personalName;
	}

	public void setPersonalName(String personalName)
	{
		this.personalName = personalName;
	}

	public InternetAddress getInternetAddress() throws UnsupportedEncodingException, AddressException
	{
		InternetAddress address = new InternetAddress(new StringBuffer(this.user).append("@").append(this.domain).toString());
		if (this.personalName != null && !this.personalName.equals(""))
		{
			address.setPersonal(this.personalName);
		}

		return address;
	}
	
	public String getAddress()
	{
		StringBuffer result = new StringBuffer();
		result.append(this.user).append("@").append(this.domain);
		
		return result.toString();
	}
}
