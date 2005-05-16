/*
 * Created on 6/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.List;

import org.apache.struts.util.MessageResources;

/**
 * @author T�nia Pous�o
 * 
 */
public class PaymentPhase extends PaymentPhase_Base {
    private List transactionList;

    /**
     * @return Returns the description.
     */
    public String getDescription() {

        MessageResources messages = MessageResources
                .getMessageResources("ServidorApresentacao.ApplicationResources");

        String newDescription = null;
        newDescription = messages.getMessage(super.getDescription());
        if (newDescription == null) {
            newDescription = super.getDescription();
        }
        return newDescription;
    }

    /**
     * @return Returns the transactionList.
     */
    public List getTransactionList() {
        return transactionList;
    }

    /**
     * @param transactionList
     *            The transactionList to set.
     */
    public void setTransactionList(List transactionList) {
        this.transactionList = transactionList;
    }

    public String toString() {
        StringBuffer object = new StringBuffer();
        object = object.append("\n[PaymentPhase: ").append("idInternal= ").append(getIdInternal())
                .append(" starDate= ").append(getStartDate()).append("; endDate= ").append(getEndDate()).append(
                        "; value= ").append(getValue()).append("; description= ").append(
                        getDescription()).append("\n");

        return object.toString();
    }

}