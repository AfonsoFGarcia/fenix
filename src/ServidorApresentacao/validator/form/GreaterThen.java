package ServidorApresentacao.validator.form;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.Resources;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GreaterThen {

    public static boolean validateFloat(Object bean, ValidatorAction va, Field field,
            ActionErrors errors, HttpServletRequest request, ServletContext application) {

        String inputString = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String lowerValueString = field.getVarValue("value");

        if ((inputString == null) || (inputString.length() == 0)) {
            return true;
        }
        Double input = null;
        Double lowerValue = null;

        try {
            input = new Double(inputString);
            lowerValue = new Double(lowerValueString);
        } catch (NumberFormatException e) {
            errors.add(field.getKey(), Resources.getActionError(request, va, field));
            return false;
        }

        if (!GenericValidator.isBlankOrNull(inputString)) {
            if (input.floatValue() <= lowerValue.floatValue()) {
                errors.add(field.getKey(), Resources.getActionError(request, va, field));
            }
            return false;
        }

        return true;
    }

    public static boolean validateFloat0(Object bean, ValidatorAction va, Field field,
            ActionErrors errors, HttpServletRequest request, ServletContext application) {

        String inputString = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String lowerValueString = field.getVarValue("value");

        if ((inputString == null) || (inputString.length() == 0)) {
            return true;
        }
        Double input = null;
        Double lowerValue = null;

        try {
            input = new Double(inputString);
            lowerValue = new Double(lowerValueString);
        } catch (NumberFormatException e) {
            errors.add(field.getKey(), Resources.getActionError(request, va, field));
            return false;
        }

        if (!GenericValidator.isBlankOrNull(inputString)) {
            if (input.floatValue() < lowerValue.floatValue()) {
                errors.add(field.getKey(), Resources.getActionError(request, va, field));
            }
            return false;
        }

        return true;
    }

}