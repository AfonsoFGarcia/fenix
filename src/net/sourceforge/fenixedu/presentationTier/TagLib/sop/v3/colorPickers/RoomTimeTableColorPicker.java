/*
 * Created on 27/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.ColorPicker;

/**
 * @author jpvl
 */
public class RoomTimeTableColorPicker extends ColorPicker {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.TagLib.sop.v3.ColorPicker#getColorKeyFromInfoLesson(net.sourceforge.fenixedu.dataTransferObject.InfoLesson)
     */
    protected String getColorKeyFromInfoLesson(InfoShowOccupation infoShowOccupation) {
        StringBuilder strBuffer = new StringBuilder();

        if (infoShowOccupation instanceof InfoLesson) {
            InfoLesson infoLesson = (InfoLesson) infoShowOccupation;

            strBuffer.append(infoLesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
        } else {
            strBuffer.append("EXAM");
        }

        return strBuffer.toString();
    }

    /*
     * protected String getColorKeyFromInfoLesson(InfoLesson infoLesson) {
     * StringBuilder strBuffer = new StringBuilder(); strBuffer.append(
     * infoLesson.getInfoShift().getInfoDisciplinaExecucao().getSigla()); return
     * strBuffer.toString(); }
     */
}