/*
 * Created on 7/Nov/2003
 * 
 */
package DataBeans.teacher;

import java.util.Date;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoExternalActivity extends InfoObject implements ISiteComponent
{

    private InfoTeacher infoTeacher;
    private String activity;
    private Date lastModificationDate;

    public InfoExternalActivity()
    {
    }

    /**
	 * @return Returns the activity.
	 */
    public String getActivity()
    {
        return activity;
    }

    /**
	 * @param activity
	 *            The activity to set.
	 */
    public void setActivity(String activity)
    {
        this.activity = activity;
    }

    /**
	 * @return Returns the infoTeacher.
	 */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
	 * @param infoTeacher
	 *            The infoTeacher to set.
	 */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof InfoExternalActivity)
        {
            resultado = getInfoTeacher().equals(((InfoExternalActivity) obj).getInfoTeacher());
        }
        return resultado;
    }
    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

}
