package net.sourceforge.fenixedu.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class BundleUtil {

    private static final String RESOURCES_PREFIX = "resources.";
    private static final String RESOURCES_SUFFIX = "Resources";

    private static final String APPLICATION_MODULE = "Application";
    private static final String ENUMERATION_MODULE = "Enumeration";

    public static String getMessageFromModuleOrApplication(final String moduleName, final String key, final String... args) {
	try {
	    return MessageFormat.format(getResourceBundleByModuleName(moduleName).getString(key), (Object[]) args);
	} catch (MissingResourceException e) {
	    try {
		return MessageFormat.format(getResourceBundleByModuleName(APPLICATION_MODULE).getString(key), (Object[]) args);
	    } catch (MissingResourceException ex) {
		return key;
	    }
	}
    }

    public static String getEnumName(final Enum<?> enumeration) {
	return getEnumName(enumeration, ENUMERATION_MODULE);
    }

    public static String getEnumName(final Enum<?> enumeration, final String moduleName) {
	String className = enumeration.getClass().getSimpleName();
	if (className.isEmpty()) {
	    className = enumeration.getClass().getName();
	    className = className.substring(className.lastIndexOf('.') + 1, className.indexOf("$"));
	}

	String enumFullName = className + "." + enumeration.name();
	try {
	    return getResourceBundleByModuleName(moduleName).getString(enumFullName);
	} catch (MissingResourceException e) {
	    try {
		return getResourceBundleByModuleName(moduleName).getString(enumeration.name());
	    } catch (MissingResourceException ex) {
		return enumFullName;
	    }
	}
    }

    private static ResourceBundle getResourceBundleByModuleName(String moduleName) {
	moduleName = StringUtils.capitalize(moduleName);
	try {
	    return getResourceBundleByName(RESOURCES_PREFIX + moduleName + RESOURCES_SUFFIX);
	} catch (MissingResourceException ex) {
	    return getResourceBundleByName(RESOURCES_PREFIX + moduleName);
	}
    }

    private static ResourceBundle getResourceBundleByName(final String bundleName) {
	return ResourceBundle.getBundle(bundleName, Language.getLocale());
    }

}
