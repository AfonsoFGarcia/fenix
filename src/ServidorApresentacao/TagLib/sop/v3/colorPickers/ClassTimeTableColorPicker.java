/*
 * Created on 27/Fev/2003
 *
 * 
 */
package ServidorApresentacao.TagLib.sop.v3.colorPickers;

import DataBeans.InfoLesson;
import ServidorApresentacao.TagLib.sop.v3.ColorPicker;

/**
 * @author jpvl
 */
public class ClassTimeTableColorPicker extends ColorPicker {

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TagLib.sop.v3.ColorPicker#getColorKeyFromInfoLesson(DataBeans.InfoLesson)
	 */
	protected String getColorKeyFromInfoLesson(InfoLesson infoLesson) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(
			infoLesson.getInfoDisciplinaExecucao().getSigla());
		return strBuffer.toString();
	}

}
