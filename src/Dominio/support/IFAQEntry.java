/*
 * Created on 2004/08/30
 *
 */
package Dominio.support;

import Dominio.IDomainObject;

/**
 * @author Luis Cruz
 *  
 */
public interface IFAQEntry extends IDomainObject {
    public IFAQSection getParentSection();

    public void setParentSection(IFAQSection parentSection);

    public String getAnswer();

    public void setAnswer(String answer);

    public String getQuestion();

    public void setQuestion(String question);
}