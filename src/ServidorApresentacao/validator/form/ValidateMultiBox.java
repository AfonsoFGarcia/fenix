

package ServidorApresentacao.validator.form;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Jo�o Mota
 *
 * 30/Jun/2003
 * fenix-branch
 * ServidorApresentacao.validator.form
 * 
 */
public class ValidateMultiBox {

	public static boolean validate(
		Object bean,
		ValidatorAction va,
		Field field,
		ActionErrors errors,
		HttpServletRequest request,
		ServletContext application) {

		try {

			DynaActionForm form = (DynaActionForm) bean;

			String sProperty = field.getVarValue("arrayProperty");
			String[] multiBox = (String[]) form.get(sProperty);
			String feminin = field.getVarValue("femininProperty");
			if ((multiBox != null) && (multiBox.length > 0)) {
				return true;
			} else {
				if (feminin != null && feminin.equals("true")) {
					errors.add(
						field.getKey(),
						new ActionError(
							"errors.required.a.checkbox",
							field.getArg0().getKey()));
				} else {
					errors.add(
						field.getKey(),
						new ActionError(
							"errors.required.checkbox",
							field.getArg0().getKey()));
				}

				return false;
			}
		} catch (Exception e) {
			errors.add(
				field.getKey(),
				new ActionError(
					"errors.required.undefined.checkbox",
					field.getArg0().getKey()));
			return false;
		}

	}

}
