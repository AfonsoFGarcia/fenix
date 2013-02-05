package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.GroupElementBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DisassociableParentsForQuestion implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        GroupElementBean groupElementBean = (GroupElementBean) source;

        List<NewQuestionGroup> allParents;

        if (groupElementBean.getChild().getParentQuestionGroups().size() == 1) {
            allParents = new ArrayList<NewQuestionGroup>();
        } else {
            allParents = groupElementBean.getChild().getParentQuestionGroups();
        }

        return allParents;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
