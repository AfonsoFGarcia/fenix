package net.sourceforge.fenixedu.domain.precedences;


/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByCurricularCourse extends RestrictionByCurricularCourse_Base {

    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
        stringBuffer.append(this.getPrecedentCurricularCourse()).append("\n");
        return stringBuffer.toString();
    }
	
	public void delete(){
		removePrecedentCurricularCourse();
		super.delete();
	}
}