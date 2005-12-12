

package net.sourceforge.fenixedu.domain.cms;


import java.util.Date;

import net.sourceforge.fenixedu.domain.IPerson;
import relations.CmsUsers;
import relations.ContentCreation;
import relations.ContentHierarchy;
import relations.ContentOwnership;

public abstract class Content extends Content_Base implements Comparable<Content>
{

	public class CannotChangeContentCreator extends RuntimeException
	{
		private static final long serialVersionUID = -3318576493828673007L;

		public CannotChangeContentCreator(String msg)
		{
			super(msg);
		}
	}

	public Content()
	{
		super();
		this.setCreationDate(new Date(System.currentTimeMillis()));
	}

	public int compareTo(Content arg0)
	{
		return this.getCreationDate().compareTo(arg0.getCreationDate());
	}

	public void delete()
	{
		for (IBin parent : this.getParents())
		{
			ContentHierarchy.remove(this, parent);
		}

		ContentCreation.remove(this.getCreator(), this);

		for (IPerson owner : this.getOwners())
		{
			ContentOwnership.remove(owner, this);
			if (owner.getCreatedContents().size() == 0 && owner.getOwnedContents().size() == 0)
			{
				CmsUsers.remove(this.getCms(), owner);
			}
		}

		super.deleteDomainObject();
	}

	@Override
	public void setCreator(IPerson creator)
	{
		{
			if (this.getCreator() == null)
			{
				super.setCreator(creator);
			}
			else
			{
				throw new CannotChangeContentCreator("Cannot change a content creator");
			}
		}
	}
}
