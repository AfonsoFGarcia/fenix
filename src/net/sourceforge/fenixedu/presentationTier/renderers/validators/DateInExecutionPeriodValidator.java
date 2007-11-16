/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import java.text.ParseException;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.validators.DateValidator;
import net.sourceforge.fenixedu.util.DateFormatUtil;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DateInExecutionPeriodValidator extends DateValidator {

    public DateInExecutionPeriodValidator(Validatable component) {
	super(component);
    }

    public DateInExecutionPeriodValidator(Validatable component, String dateFormat) {
	super(component, dateFormat);
    }

    @Override
    public void performValidation() {
	super.performValidation();

	if (isValid()) {
	    try {
		DateTime dateTime = new DateTime(DateFormatUtil.parse(getDateFormat(), getComponent().getValue()).getTime());
		setValid(ExecutionPeriod.readByDateTime(dateTime) != null);
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
