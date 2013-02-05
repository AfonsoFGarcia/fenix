package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean.SummaryType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Summary;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class SummaryPlainRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object == null) {
                    return new HtmlText();
                }

                Summary summary = (Summary) object;
                StringBuilder builder = new StringBuilder();
                Lesson lesson = null;

                builder.append(summary.getSummaryDateYearMonthDay().getDayOfMonth()).append("/");
                builder.append(summary.getSummaryDateYearMonthDay().getMonthOfYear()).append("/");
                builder.append(summary.getSummaryDateYearMonthDay().getYear());
                builder.append(" - ").append(RenderUtils.getResourceString("DEFAULT", "label.lesson") + ": ");

                if (summary.isExtraSummary()) {

                    builder.append(RenderUtils.getEnumString(SummaryType.EXTRA_SUMMARY, null)).append(" ");
                    builder.append(" (").append(summary.getSummaryHourHourMinuteSecond().getHour());
                    builder.append(":").append(summary.getSummaryHourHourMinuteSecond().getMinuteOfHour()).append(") ");

                } else {

                    lesson = summary.getLesson();
                    if (lesson != null) {

                        builder.append(lesson.getDiaSemana().toString()).append(" (");
                        builder.append(DateFormatUtil.format("HH:mm", lesson.getInicio().getTime()));
                        builder.append("-").append(DateFormatUtil.format("HH:mm", lesson.getFim().getTime()));
                        builder.append(") ");
                    }
                }
                if (lesson != null && lesson.hasSala()) {
                    builder.append(lesson.getSala().getName().toString());
                }

                return new HtmlText(builder.toString());
            }
        };
    }

}