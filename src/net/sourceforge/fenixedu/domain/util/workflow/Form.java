package net.sourceforge.fenixedu.domain.util.workflow;

import java.io.Serializable;
import java.util.List;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public abstract class Form implements Serializable {

    public abstract List<LabelFormatter> validate();

    public abstract String getFormName();

    public String getFormDescription() {
        return "";
    }

    public boolean isInput() {
        return true;
    }
}