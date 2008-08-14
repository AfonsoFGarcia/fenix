/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class EvaluationMethod extends EvaluationMethod_Base {

    public EvaluationMethod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void edit(MultiLanguageString evaluationElements) {
	if (evaluationElements == null)
	    throw new NullPointerException();

	setEvaluationElements(evaluationElements);
    }

    public void delete() {
	removeExecutionCourse();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
