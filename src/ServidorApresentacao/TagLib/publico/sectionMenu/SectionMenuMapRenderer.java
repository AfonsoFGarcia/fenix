/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.TagLib.publico.sectionMenu;

import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoSection;
import ServidorApresentacao.TagLib.publico.sectionMenu.renderers.SectionMenuContentRenderer;
import ServidorApresentacao.TagLib.publico.sectionMenu.renderers.SectionMenuSlotContentRenderer;
import ServidorApresentacao.TagLib.publico.sectionMenu.renderers.SectionMenuTeacherContentRenderer;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SectionMenuMapRenderer {
	private SectionMenuMap sectionMenuMap;
	private SectionMenuSlotContentRenderer sectionMenuSlotContentRenderer;
	private String path;
	private String renderer;
	/**
	 * 
	 */
	public SectionMenuMapRenderer(
		SectionMenuMap sectionMenuMap,
		SectionMenuSlotContentRenderer sectionMenuSlotContentRenderer, String path, String renderer) {
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
	public SectionMenuSlotContentRenderer getSectionMenuSlotContentRenderer() {
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
	public void setSectionMenuSlotContentRenderer(SectionMenuSlotContentRenderer sectionMenuSlotContentRenderer) {
		this.sectionMenuSlotContentRenderer = sectionMenuSlotContentRenderer;
	}

	public StringBuffer render() {
		StringBuffer strBuffer = new StringBuffer("");
		List sections = getSectionMenuMap().getSections();
		Iterator iter = sections.iterator();
		if (sections!=null && !sections.isEmpty()) {
		int i = 0;
		
			while (i!= sections.size()) {
			
				InfoSection infoSection = (InfoSection) sections.get(i);
				
				SectionMenuSlotContentRenderer sectionMenuSlot = getContentRenderer(infoSection,getRenderer());
					
				strBuffer.append(sectionMenuSlot.renderSectionLabel(i,getPath()));
				strBuffer.append(renderSuffix(sections, i));
				
				i=i+1;
			}
		}
		return strBuffer;
	}

	private StringBuffer renderSuffix(List sections, int iterator) {
		StringBuffer strBuffer = new StringBuffer("");
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
				
			
			
		return strBuffer;
	}

	private StringBuffer getStyle(List sections,int iterator) {
		StringBuffer strBuffer = new StringBuffer("");
		if (getSectionMenuMap().getActiveSection()==null || !((InfoSection) sections.get(iterator)).equals(getSectionMenuMap().getActiveSection())) {
			strBuffer.append("none");
		}	
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

	private SectionMenuSlotContentRenderer getContentRenderer(InfoSection infoSection,String renderer) {
		SectionMenuSlotContentRenderer slotRenderer= new SectionMenuContentRenderer(infoSection); 
		if (renderer == null) {}//do nothing
		else {
			if (renderer.equals("teacher")){ slotRenderer= new SectionMenuTeacherContentRenderer(infoSection);	} 			
		}
		return slotRenderer;
	}

}
