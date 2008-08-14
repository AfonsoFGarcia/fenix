package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;

import org.apache.commons.collections.comparators.ComparatorChain;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class VigilantsForGivenVigilantGroup implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	VigilantGroupBean bean = (VigilantGroupBean) source;
	VigilantGroup vigilantGroup = bean.getSelectedVigilantGroup();
	List<Vigilant> vigilants = new ArrayList<Vigilant>();

	if (source instanceof ConvokeBean) {
	    ConvokeBean convokeBean = (ConvokeBean) bean;
	    vigilants.addAll(convokeBean.getVigilantsSugestion());
	    WrittenEvaluation evaluation = convokeBean.getWrittenEvaluation();
	    if (evaluation != null && evaluation.getVigilancies().size() > 0) {
		for (Vigilancy convoke : evaluation.getVigilancies()) {
		    vigilants.remove(convoke.getVigilant());
		}
	    }
	} else {
	    vigilants.addAll(vigilantGroup.getVigilants());
	    ComparatorChain chain = new ComparatorChain();
	    chain.addComparator(Vigilant.CATEGORY_COMPARATOR);
	    chain.addComparator(Vigilant.USERNAME_COMPARATOR);
	    Collections.sort(vigilants, chain);
	}

	return vigilants;

    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

}