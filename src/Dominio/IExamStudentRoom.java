/*
 * Created on 30/Mai/2003
 *
 * 
 */
package Dominio;

/**
 * @author Jo�o Mota
 *  
 */
public interface IExamStudentRoom extends IDomainObject {
    /**
     * @return
     */
    public abstract IExam getExam();

    /**
     * @return
     */
    public abstract ISala getRoom();

    /**
     * @return
     */
    public abstract IStudent getStudent();

    /**
     * @param exam
     */
    public abstract void setExam(IExam exam);

    /**
     * @param sala
     */
    public abstract void setRoom(ISala sala);

    /**
     * @param student
     */
    public abstract void setStudent(IStudent student);
}