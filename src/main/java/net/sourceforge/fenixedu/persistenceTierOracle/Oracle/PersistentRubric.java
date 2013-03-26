/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.Rubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentRubric {

    public List<IRubric> getRubricList(String rubricTableName, final BackendInstance instance) throws ExcepcaoPersistencia {
        List<IRubric> rubricList = new ArrayList<IRubric>();

        String query = "select COD, DESCRICAO from " + rubricTableName + " order by COD";

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String code = rs.getString(1);
                String description = rs.getString(2);
                IRubric rubric = new Rubric(code, description);
                rubricList.add(rubric);
            }

            rs.close();
            p.commitTransaction();

        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return rubricList;
    }

}
