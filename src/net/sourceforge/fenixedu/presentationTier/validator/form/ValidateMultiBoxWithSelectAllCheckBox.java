/*
 * Created on 2003/04/07
 *
 */

package net.sourceforge.fenixedu.presentationTier.validator.form;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ValidateMultiBoxWithSelectAllCheckBox {

    public static boolean validate(Object bean, ValidatorAction va, Field field, ActionErrors errors,
            HttpServletRequest request, ServletContext application) {

        try {

            DynaActionForm form = (DynaActionForm) bean;
            Boolean selectAllBox = (Boolean) form.get(field.getProperty());
            String sProperty2 = field.getVarValue("secondProperty");
            String[] multiBox = (String[]) form.get(sProperty2);

            if (((selectAllBox != null) && selectAllBox.booleanValue()) || (multiBox != null)
                    && (multiBox.length > 0)) {
                return true;
            }
            errors.add(field.getKey(), new ActionError("errors.required.checkbox", field.getArg0()
                    .getKey()));
            return false;

        } catch (Exception e) {
            errors.add(field.getKey(), new ActionError("errors.required.checkbox", field.getArg0()
                    .getKey()));
            return false;
        }

    }

}