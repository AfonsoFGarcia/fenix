/*
 * Created on 12/Jul/2003 by jpvl
 *  
 */
package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Employee;
import Dominio.EmployeeHistoric;
import Dominio.IEmployee;
import Dominio.IPerson;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;

/**
 * @author jpvl
 */
public class EmployeeOJB extends PersistentObjectOJB implements IPersistentEmployee {

    public IEmployee readByNumber(Integer number) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("employeeNumber", number);
        return (Employee) queryObject(Employee.class, criteria);
    }

    public IEmployee readByIdInternal(int idInternal) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", new Integer(String.valueOf(idInternal)));
        return (Employee) queryObject(Employee.class, criteria);
    }

    public IEmployee readByPerson(int keyPerson) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", new Integer(String.valueOf(keyPerson)));
        return (Employee) queryObject(Employee.class, criteria);
    }

    public IEmployee readByPerson(IPerson person) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", person.getIdInternal());
        return (Employee) queryObject(Employee.class, criteria);
    }

    public List readHistoricByKeyEmployee(int keyEmployee) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyEmployee", new Integer(keyEmployee));

        Criteria criteria2 = new Criteria();
        criteria2.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria2.addIsNull("endDate");

        Criteria criteria3 = new Criteria();
        criteria3.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteria3.addNotNull("endDate");
        criteria3.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());

        criteria.addAndCriteria(criteria2);
        criteria2.addOrCriteria(criteria3);

        return queryList(EmployeeHistoric.class, criteria); //employee's
        // historic list
    }

}