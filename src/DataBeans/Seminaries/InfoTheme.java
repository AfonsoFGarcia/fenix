/*
 * Created on 1/Ago/2003, 21:13:13
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 1/Ago/2003, 21:13:13
 * 
 */
public class InfoTheme
{
    private Integer idInternal;
    private String description;
    private String name;
    private String shortName;
	/**
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return
	 */
	public Integer getIdInternal()
	{
		return idInternal;
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string)
	{
		description= string;
	}

	/**
	 * @param integer
	 */
	public void setIdInternal(Integer integer)
	{
		idInternal= integer;
	}

	/**
	 * @param string
	 */
	public void setName(String string)
	{
		name= string;
	}

	/**
	 * @return
	 */
	public String getShortName()
	{
		return shortName;
	}

	/**
	 * @param string
	 */
	public void setShortName(String string)
	{
		shortName= string;
	}
    
    public String toString()
    {
        String retorno;
        retorno= "[InfoTheme:";
        retorno += "ID=" + this.getIdInternal();
        retorno += "Name=" + this.getName();
        retorno += ",Description=" + this.getDescription();
        retorno += ",Short Name=" + this.getShortName() + "]";
        return retorno;
    }

}
