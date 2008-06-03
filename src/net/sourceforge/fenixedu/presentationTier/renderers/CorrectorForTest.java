package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class CorrectorForTest extends OutputRenderer {

	private String classes;

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {
			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				NewCorrector corrector = (NewCorrector) object;

				HtmlInlineContainer container = new HtmlInlineContainer();

				container.addChild(new HtmlText(corrector.getPercentage() + "%:"));
				
				container.addChild(renderValue(corrector.getPredicate(), corrector.getPredicate()
						.getClass(), null, "values"));

				return container;
			}
		};
	}

}
