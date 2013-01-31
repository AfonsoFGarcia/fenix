package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import pt.ist.fenixWebFramework.renderers.StringRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;

public class AtomicQuestionForAllGroupRenderer extends StringRenderer {

	private String classes;

	@Override
	public String getClasses() {
		return classes;
	}

	@Override
	public void setClasses(String classes) {
		this.classes = classes;
	}

	@Override
	public HtmlComponent render(Object object, Class type) {
		NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) object;

		StringBuilder builder = new StringBuilder("Alinea ");

		Iterator<Integer> iterator = atomicQuestion.getPath().iterator();
		while (iterator.hasNext()) {
			Integer position = iterator.next();
			builder.append(position);

			if (iterator.hasNext()) {
				builder.append(".");
			}
		}

		return new HtmlText(builder.toString());
	}

}
