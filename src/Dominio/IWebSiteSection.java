package Dominio;




/**
 * @author Fernanda Quit�rio
 * 23/09/2003
 * 
 */
public interface IWebSiteSection extends IDomainObject{
	
	public String getName();
	public Integer getExcerptSize();
	public IWebSite getWebSite();
	public Integer getSize();
	public String getSortingOrder();
	public String getWhatToSort();
//	public List getItemsList();
    
	public void setName(String name);
	public void setWebSite(IWebSite site);
	public void setExcerptSize(Integer excerptSize);
	public void setSize(Integer size);
	public void setSortingOrder(String sortingOrder);
	public void setWhatToSort(String whatToSort);
//	public void setItemsList(List items);
}
