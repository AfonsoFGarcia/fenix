/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.gaugingTests.physics;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 26/Nov/2003
 *  
 */
public class GaugingTestResult extends GaugingTestResult_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IGaugingTestResult) {
            final IGaugingTestResult gaugingTest = (IGaugingTestResult) obj;
            return this.getIdInternal().equals(gaugingTest.getIdInternal());
        }
        return false;
    }

}