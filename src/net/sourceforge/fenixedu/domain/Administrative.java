/*
 * Created on 1/Mar/2004
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author T�nia Pous�o
 *  
 */
public class Administrative extends Administrative_Base {
    
    public Administrative(int idInternal, int keyEmployee){
        this.setIdInternal(idInternal);
        this.setKeyEmployee(keyEmployee);        
    }
}