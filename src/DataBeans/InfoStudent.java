/*
 * InfoStudent.java
 * 
 * Created on 13 de Dezembro de 2002, 16:04
 */

package DataBeans;

import Dominio.IStudent;
import Util.StudentState;
import Util.TipoCurso;

/**
 * @author tfc130
 */

public class InfoStudent extends InfoObject {

    protected Integer number;

    protected StudentState state = new StudentState(1);

    private InfoPerson infoPerson;

    protected TipoCurso degreeType;

    private InfoStudentKind infoStudentKind;

    public InfoStudent() {
    }

    public InfoStudent(Integer numero, StudentState estado, InfoPerson pessoa,
            TipoCurso degreeType) {
        setNumber(numero);
        setState(estado);
        setInfoPerson(pessoa);
        setDegreeType(degreeType);
    }

    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    public void setInfoPerson(InfoPerson pessoa) {
        infoPerson = pessoa;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer numero) {
        number = numero;
    }

    public StudentState getState() {
        return state;
    }

    public void setState(StudentState estado) {
        state = estado;
    }

    public TipoCurso getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(TipoCurso degreeType) {
        this.degreeType = degreeType;
    }

    // FIXME: The type of degree should be tested also
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoStudent) {
            InfoStudent infoAluno = (InfoStudent) obj;
            resultado = getNumber().equals(infoAluno.getNumber());
        }
        return resultado;
    }

    public String toString() {
        String result = "[InfoStudent";
        result += ", numero=" + number;
        result += ", degreeType=" + degreeType;
        result += ", estado=" + state;
        if (infoPerson != null) result += ", pessoa" + infoPerson.toString();
        result += "]";
        return result;
    }

    public InfoStudentKind getInfoStudentKind() {
        return infoStudentKind;
    }

    public void setInfoStudentKind(InfoStudentKind info) {
        infoStudentKind = info;
    }

    public void copyFromDomain(IStudent student) {
        super.copyFromDomain(student);

        if (student != null) {
            setNumber(student.getNumber());
            setDegreeType(student.getDegreeType());
            setState(student.getState());
        }
    }
    
    public static InfoStudent newInfoFromDomain(IStudent student) {
        InfoStudent infoStudent = null;
        if(student != null) {
           infoStudent = new InfoStudent();
           infoStudent.copyFromDomain(student);
        }
        return infoStudent;
    }
}