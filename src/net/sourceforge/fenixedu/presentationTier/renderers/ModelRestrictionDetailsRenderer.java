package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import pt.ist.fenixWebFramework.renderers.Renderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class ModelRestrictionDetailsRenderer extends Renderer {

	// Our very own polymorphic key finding hashtable... niiiice
	private static Map<Class, String> templateMap = new HashMap<Class, String>() {
		@Override
		public String get(Object key) {
			Class type = (Class) key;

			while (true) {
				// Found none, return a nice null
				if (type == null) {
					return null;
				}

				// Evaluate current type
				String value = super.get(type);
				if (value != null) {
					return value;
				}

				// Not found, try the superclass
				type = type.getSuperclass();
			}
		}
	};

	static {
		templateMap.put(NewModelGroup.class, "/teacher/tests/templates/testModel/model/modelGroup.jsp");
		templateMap.put(NewModelRestriction.class, "/teacher/tests/templates/testModel/model/modelRestriction.jsp");
		templateMap.put(NewTestModel.class, "/teacher/tests/templates/testModel/model/testModel.jsp");
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				Properties properties = new Properties();
				properties.setProperty("template", templateMap.get(object.getClass()));

				return renderValue(object, type, null, "template", properties);
			}

		};
	}

}
