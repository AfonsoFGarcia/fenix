/*
 * Created on 7/Abr/2003
 *
 * 
 */
package ServidorApresentacao.TagLib.publico.sectionMenu;

import java.util.List;

import DataBeans.InfoSection;
import ServidorApresentacao.TagLib.publico.sectionMenu.renderers.ISectionMenuSlotContentRenderer;
import ServidorApresentacao.TagLib.publico.sectionMenu.renderers.SectionChooserRenderer;
import ServidorApresentacao.TagLib.publico.sectionMenu.renderers.SectionMenuContentRenderer;
import ServidorApresentacao.TagLib.publico.sectionMenu.renderers.SectionMenuTeacherContentRenderer;

/**
 * @author Jo�o Mota
 *
 * 
 */
public class SectionMenuMapRenderer {
	private SectionMenuMap sectionMenuMap;
	private ISectionMenuSlotContentRenderer sectionMenuSlotContentRenderer;
	private String path;
	private String renderer;
	/**
	 * 
	 */
	public SectionMenuMapRenderer(
		SectionMenuMap sectionMenuMap,
		ISectionMenuSlotContentRenderer sectionMenuSlotContentRenderer, String path, String renderer) {
		setSectionMenuMap(sectionMenuMap);
		setSectionMenuSlotContentRenderer(sectionMenuSlotContentRenderer);
		setPath(path);
		setRenderer(renderer);
	}

	/**
	 * @return SectionMenuMap
	 */
	public SectionMenuMap getSectionMenuMap() {
		return sectionMenuMap;
	}

	/**
	 * @return SectionMenuSlotContentRenderer
	 */
	public ISectionMenuSlotContentRenderer getSectionMenuSlotContentRenderer() {
		return sectionMenuSlotContentRenderer;
	}

	/**
	 * Sets the sectionMenuMap.
	 * @param sectionMenuMap The sectionMenuMap to set
	 */
	public void setSectionMenuMap(SectionMenuMap sectionMenuMap) {
		this.sectionMenuMap = sectionMenuMap;
	}

	/**
	 * Sets the sectionMenuSlotContentRenderer.
	 * @param sectionMenuSlotContentRenderer The sectionMenuSlotContentRenderer to set
	 */
	public void setSectionMenuSlotContentRenderer(ISectionMenuSlotContentRenderer sectionMenuSlotContentRenderer) {
		this.sectionMenuSlotContentRenderer = sectionMenuSlotContentRenderer;
	}

	public StringBuffer render() {
		StringBuffer strBuffer = new StringBuffer("");
		List sections = getSectionMenuMap().getSections();
		if (sections!=null && !sections.isEmpty()) {
		int i = 0;
		
			while (i!= sections.size()) {
			
				InfoSection infoSection = (InfoSection) sections.get(i);
				
				ISectionMenuSlotContentRenderer sectionMenuSlot = getContentRenderer(infoSection,getRenderer());
				
				boolean hasChilds = false;
				if ( (i + 1) < sections.size()){
					InfoSection nextInfoSection = (InfoSection) sections.get(i+1);
					hasChilds = nextInfoSection.getSectionDepth().intValue() > infoSection.getSectionDepth().intValue();
				}
				
				strBuffer.append(sectionMenuSlot.renderSectionLabel(i,getPath(), hasChilds));
				strBuffer.append(renderSuffix(sections, i));
				
				i++;
			}
		}
		return strBuffer;
	}

	private StringBuffer renderSuffix(List sections, int iterator) {
		StringBuffer strBuffer = new StringBuffer("");
		
		//section management
		if (renderer!=null && renderer.equals("sectionChooser")){
		}
		//main menu
		else {		
		if (((InfoSection) sections.get(iterator)).getSectionDepth().intValue() != 0 && iterator==sections.size()-1) {
						strBuffer.append("</dl>\n");	
			 
					}
		else { if(iterator!=sections.size()-1) {
		
				
				
				if (((InfoSection) sections.get(iterator+1)).getSectionDepth().intValue() == 0
				&& ((InfoSection) sections.get(iterator)).getSectionDepth().intValue() != 0) {
				strBuffer.append("</dl>\n");				
				}
				if (((InfoSection) sections.get(iterator+1)).getSectionDepth().intValue() != 0
				&& ((InfoSection) sections.get(iterator)).getSectionDepth().intValue() == 0) {				
					strBuffer.append("<dl id=\""+((InfoSection) sections.get(iterator)).getName()+"\" style=\"display:"+getStyle(sections,iterator)+";\">\n");
				}}}
		}
					
		return strBuffer;
	}




	private StringBuffer getStyle(List sections,int iterator) {
		StringBuffer strBuffer = new StringBuffer("");
		if (getSectionMenuMap().getActiveSection()==null || !((InfoSection) sections.get(iterator)).equals(getSectionMenuMap().getActiveSection())) {
			strBuffer.append("none");
		}	
		return strBuffer;
	}
	
	private StringBuffer getStyleSectionChooser(List sections,int iterator) {
			StringBuffer strBuffer = new StringBuffer("");
			
			return strBuffer;
		}
	/**
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param string
	 */
	public void setPath(String string) {
		path = string;
	}

	/**
	 * @return
	 */
	public String getRenderer() {
		return renderer;
	}

	/**
	 * @param string
	 */
	public void setRenderer(String string) {
		renderer = string;
	}

	private ISectionMenuSlotContentRenderer getContentRenderer(InfoSection infoSection,String renderer) {
		ISectionMenuSlotContentRenderer slotRenderer= new SectionMenuContentRenderer(infoSection); 
		if (renderer == null) {}//do nothing
		else {
			if (renderer.equals("teacher")){ 
				slotRenderer= new SectionMenuTeacherContentRenderer(infoSection);	
			}
			if (renderer.equals("sectionChooser")){ 
				slotRenderer= new SectionChooserRenderer(infoSection);	
			}
		}
		return slotRenderer;
	}

}
