/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import java.text.ParseException;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.validators.DateValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DateInExecutionPeriodValidator extends DateValidator {

    public DateInExecutionPeriodValidator() {
        super();
    }

    public DateInExecutionPeriodValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);
    }

    public DateInExecutionPeriodValidator(HtmlChainValidator htmlChainValidator, String dateFormat) {
        super(htmlChainValidator, dateFormat);
    }

    @Override
    public void performValidation() {
        super.performValidation();

        if (isValid()) {
            try {
                DateTime dateTime = new DateTime(DateFormatUtil.parse(getDateFormat(), getComponent().getValue()).getTime());
                setValid(ExecutionSemester.readByDateTime(dateTime) != null);
                if (!isValid()) {
                    setMessage("renderers.validator.dateInExecutionPeriod.notInExecutionPeriod");
                }
            } catch (ParseException e) {
                setValid(false);
                e.printStackTrace();
            }
        }
    }

}
