/*
 * StudentOJB.java
 * 
 * Created on 28 December 2002, 17:19
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author Ricardo Nortadas
 */

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;

import org.apache.ojb.broker.query.Criteria;

public class StudentOJB extends PersistentObjectOJB implements IPersistentStudent {
//
	
	public Student readByUsername(String username) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("person.user.username", username);

        Student student = (Student) queryObject(Student.class, crit);

        return student;
    }

    //	---------------------------------------------------------------------------------------------------------

    // feitos por David \ Ricardo

    public Student readStudentByNumberAndDegreeType(Integer number, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", number);
        criteria.addEqualTo("degreeType", degreeType);

        return (Student) queryObject(Student.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Student.class, new Criteria());
    }

    public Student readByPersonAndDegreeType(Integer personId, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", personId);
        crit.addEqualTo("degreeType", degreeType);
        return (Student) queryObject(Student.class, crit);

    }

    public Integer generateStudentNumber(DegreeType degreeType) throws ExcepcaoPersistencia {

        Integer number = new Integer(0);
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeType", degreeType);
        List result = queryList(Student.class, crit, "number", false);

        if ((result != null) && (result.size() != 0)) {
            number = ((Student) result.get(0)).getNumber();
        }

        // FIXME: ISTO E UMA SOLUCAO TEMPORARIA DEVIDO A EXISTIREM ALUNOS
        // NA SECRETARIA QUE
        // POR UM MOTIVO OU OUTRO NAO SE ENCONTRAM NA BASE DE DADOS

        if (degreeType.equals(DegreeType.MASTER_DEGREE) && (number.intValue() < 5411)) {
            number = new Integer(5411);
        }

        return new Integer(number.intValue() + 1);

    }

    public List readMasterDegreeStudentsByNameIDnumberIDtypeAndStudentNumber(String studentName,
            String idNumber, IDDocumentType idType, Integer studentNumber)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        if (studentName != null) {
            criteria.addLike("person.name", studentName);
        }

        if (idNumber != null) {
            criteria.addEqualTo("person.documentIdNumber", idNumber);
        }

        if (idType != null) {
            criteria.addEqualTo("person.idDocumentType", idType);
        }

        if (studentNumber != null) {
            criteria.addEqualTo("number", studentNumber);
        }

        criteria.addEqualTo("degreeType", DegreeType.MASTER_DEGREE);

        return queryList(Student.class, criteria);
    }

    public Integer countAll() {
        return new Integer(count(Student.class, new Criteria()));
    }

    public List readStudentByPersonRole(RoleType roleType) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.personRoles.roleType", roleType);
        return queryList(Student.class, criteria);
    }

    public List readAllBetweenNumbers(Integer fromNumber, Integer toNumber) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addGreaterOrEqualThan("number", fromNumber);
        criteria.addLessOrEqualThan("number", toNumber);
        return queryList(Student.class, criteria);
    }

    public List readAllWithPayedTuition() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("payedTuition", new Boolean(true));
        return queryList(Student.class, criteria);
    }

    public Collection<Student> readStudentsByDegreeType(DegreeType degreeType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeType", degreeType);
        return queryList(Student.class, criteria);
    }

}