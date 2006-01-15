/*
 * Created on 7/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers;

import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.dataTransferObject.InfoSection;

/**
 * @author Jo�o Mota
 * 
 *  
 */
public class SectionMenuContentRenderer extends TagSupport implements ISectionMenuSlotContentRenderer {

    private InfoSection infoSection;

    public SectionMenuContentRenderer() {
    }

    public SectionMenuContentRenderer(InfoSection infoSection) {
        setInfoSection(infoSection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.TagLib.publico.sectionMenu.renderers.SectionMenuSlotContentRenderer#renderSectionLabel(ServidorApresentacao.TagLib.publico.sectionMenu.SectionMenuSlot)
     */
    public StringBuilder renderSectionLabel(int i, String path, boolean hasChilds) {
        StringBuilder strBuffer = new StringBuilder();
        strBuffer.append(renderDepthContent(getInfoSection(), i, path, hasChilds));
        return strBuffer;
    }

    private StringBuilder renderDepthContent(InfoSection infoSection, int i, String path,
            boolean hasChilds) {
        StringBuilder strBuffer = new StringBuilder();
        int depth = infoSection.getSectionDepth().intValue();
        if (depth == 0) {
            //adds the info

            strBuffer.append("<li>\n");
          //  strBuffer.append(renderDepthIdent(getInfoSection()));

            strBuffer.append("<a href=\"").append(path).append("/viewSite.do?method=section&amp;index=")
                    .append(i).append("&amp;objectCode=").append(
                            getInfoSection().getInfoSite().getIdInternal()).append("\"");
//            if (hasChilds)
//                strBuffer.append(" onclick=\"houdini('").append(infoSection.getName()).append("');\"");

            strBuffer.append(">\n").append(infoSection.getName());
            strBuffer.append("</a>").append("</li>\n");
        } else {
            //adds the info
            strBuffer.append("<dd style=\"padding:0 0 0 ");
            strBuffer.append((getInfoSection().getSectionDepth().intValue()+1)*10);
            strBuffer.append("px\">");

         //   strBuffer.append(renderDepthIdent(getInfoSection()));
            strBuffer.append("<a href=\"viewSite.do?method=section&amp;index=" + i + "&amp;objectCode="
                    + getInfoSection().getInfoSite().getIdInternal() + "\" >");
            //falta o index

            strBuffer.append(infoSection.getName());

            strBuffer.append("</a>");
            strBuffer.append("</dd>\n");
        }

        return strBuffer;
    }

    /**
     * @return InfoSection
     */
    public InfoSection getInfoSection() {
        return infoSection;
    }

    /**
     * Sets the infoSection.
     * 
     * @param infoSection
     *            The infoSection to set
     */
    public void setInfoSection(InfoSection infoSection) {
        this.infoSection = infoSection;
    }

}