package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.kerberos.Script;

public class GeneratePasswordKerberos extends GeneratePasswordBase {

	@Override
	public String generatePassword(Person person) {
		String password = randPass.getPass(PropertiesManager.getIntegerProperty("passSize"));
		if (person.hasIstUsername()) {
			try {
				Script.changeKerberosPass(person.getIstUsername(), password);
			} catch (Exception rte) {
				throw new RuntimeException(rte);
			}
		}
		return password;
	}

}
