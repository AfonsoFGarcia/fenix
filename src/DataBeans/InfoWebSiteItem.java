package DataBeans;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author  Fernanda Quit�rio
 * 24/09/2003
 * 
 */
public class InfoWebSiteItem extends InfoObject {
	private String title;
	private String mainEntryText;
	private String excerpt;
	private Boolean published;
	private Timestamp creationDate;
	private String keywords;
	private Date onlineBeginDay;
	private Date onlineEndDay;
	private Date itemBeginDay;
	private Date itemEndDay;
	private Boolean toDelete;

	private InfoWebSiteSection infoWebSiteSection;
	private InfoPerson infoEditor;

	/**
	 * @return
	 */
	public Boolean getToDelete() {
		return toDelete;
	}

	/**
	 * @param toDelete
	 */
	public void setToDelete(Boolean toDelete) {
		this.toDelete = toDelete;
	}

	/**
	 * @return
	 */
	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 */
	public void setCreationDate(Timestamp creationDate) {
		
		this.creationDate = creationDate;
	}

	/**
	 * @return
	 */
	public InfoPerson getInfoEditor() {
		return infoEditor;
	}

	/**
	 * @param editor
	 */
	public void setInfoEditor(InfoPerson editor) {
		this.infoEditor = editor;
	}

	/**
	 * @return
	 */
	public String getExcerpt() {
		return excerpt;
	}

	/**
	 * @param excerpt
	 */
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	/**
	 * @return
	 */
	public Date getItemBeginDay() {
		return itemBeginDay;
	}

	/**
	 * @param itemBeginDay
	 */
	public void setItemBeginDay(Date itemBeginDay) {
		this.itemBeginDay = itemBeginDay;
	}

	/**
	 * @return
	 */
	public Date getItemEndDay() {
		return itemEndDay;
	}

	/**
	 * @param itemEndDay
	 */
	public void setItemEndDay(Date itemEndDay) {
		this.itemEndDay = itemEndDay;
	}

	/**
	 * @return
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return
	 */
	public String getMainEntryText() {
		return mainEntryText;
	}

	/**
	 * @param mainEntryText
	 */
	public void setMainEntryText(String mainEntryText) {
		this.mainEntryText = mainEntryText;
	}

	/**
	 * @return
	 */
	public Date getOnlineBeginDay() {
		return onlineBeginDay;
	}

	/**
	 * @param onlineBeginDay
	 */
	public void setOnlineBeginDay(Date onlineBeginDay) {
		this.onlineBeginDay = onlineBeginDay;
	}

	/**
	 * @return
	 */
	public Date getOnlineEndDay() {
		return onlineEndDay;
	}

	/**
	 * @param onlineEndDay
	 */
	public void setOnlineEndDay(Date onlineEndDay) {
		this.onlineEndDay = onlineEndDay;
	}

	/**
	 * @return
	 */
	public Boolean getPublished() {
		return published;
	}

	/**
	 * @param published
	 */
	public void setPublished(Boolean published) {
		this.published = published;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return
	 */
	public InfoWebSiteSection getInfoWebSiteSection() {
		return infoWebSiteSection;
	}

	/**
	 * @param infoWebSiteSection
	 */
	public void setInfoWebSiteSection(InfoWebSiteSection infoWebSiteSection) {
		this.infoWebSiteSection = infoWebSiteSection;
	}

	/**
	 * Constructor
	 */
	public InfoWebSiteItem() {
		setToDelete(Boolean.FALSE);
	}

	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof InfoWebSiteItem) {
			InfoWebSiteItem webSiteItem = (InfoWebSiteItem) arg0;

			if (elementsAreEqual(webSiteItem.getTitle(), this.getTitle())
				&& elementsAreEqual(webSiteItem.getCreationDate(), this.getCreationDate())
				&& elementsAreEqual(webSiteItem.getInfoEditor(), this.getInfoEditor())
				&& elementsAreEqual(webSiteItem.getExcerpt(), this.getExcerpt())
				&& elementsAreEqual(webSiteItem.getItemBeginDay(), this.getItemBeginDay())
				&& elementsAreEqual(webSiteItem.getItemEndDay(), this.getItemEndDay())
				&& elementsAreEqual(webSiteItem.getKeywords(), this.getKeywords())
				&& elementsAreEqual(webSiteItem.getMainEntryText(), this.getMainEntryText())
				&& elementsAreEqual(webSiteItem.getOnlineBeginDay(), this.getOnlineBeginDay())
				&& elementsAreEqual(webSiteItem.getOnlineEndDay(), this.getOnlineEndDay())
				&& elementsAreEqual(webSiteItem.getPublished(), this.getPublished())
				&& elementsAreEqual(webSiteItem.getInfoWebSiteSection(), this.getInfoWebSiteSection())) {
				result = true;
			}
		}
		return result;
	}

	private boolean elementsAreEqual(Object element1, Object element2) {
		boolean result = false;
		if ((element1 == null && element2 == null) || (element1 != null && element2 != null && element1.equals(element2))) {
			result = true;
		}
		return result;
	}
	public String toString() {
		String result = "[INFOWEBSITEITEM";
		result += ", codInt=" + this.getIdInternal();
		result += ", title=" + this.getTitle();
		result += ", mainEntryText=" + this.getMainEntryText();
		result += ", excerpt=" + this.getExcerpt();
		result += ", published=" + this.getPublished();
		result += ", creationDate=" + this.getCreationDate();
		result += ", keywords=" + this.getKeywords();
		result += ", onlineBeginDay=" + this.getOnlineBeginDay();
		result += ", onlineEndDay=" + this.getOnlineEndDay();
		result += ", itemBeginDay=" + this.getItemBeginDay();
		result += ", itemEndDay=" + this.getItemEndDay();
		result += ", webSiteSection=" + this.getInfoWebSiteSection();
		result += ", editor=" + this.getInfoEditor();
		result += "]";
		return result;
	} 

}
