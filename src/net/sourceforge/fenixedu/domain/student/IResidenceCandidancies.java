/*
 * Created on Aug 3, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IStudent;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IResidenceCandidancies extends IDomainObject {

    public Boolean getDislocated();

    public String getObservations();

    public IStudent getStudent();

    public Date getCreationDate();

    public void setCreationDate(Date creationDate);

    public void setDislocated(Boolean dislocated);

    public void setObservations(String observations);

    public void setStudent(IStudent student);

}