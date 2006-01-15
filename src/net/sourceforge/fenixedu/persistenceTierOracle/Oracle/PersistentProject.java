/*
 * Created on Jan 10, 2005
 *
 */

package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.Project;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentProject;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentProject implements IPersistentProject {

    public List<Project> readByUserLogin(String userLogin) throws ExcepcaoPersistencia {
        List<Project> projects = new ArrayList<Project>();

        StringBuilder query = new StringBuilder();
        query
                .append("select p.projectCode, p.title, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO from  V_PROJECTOS p , web_user_projs up where up.login ='");
        query.append(userLogin);
        query.append("' and p.projectCode = up.id_proj order by p.projectCode");
        // String query = " select p.projectCode, p.title, p.origem, p.tipo,
        // p.custo, p.coordenacao, p.UNID_EXPLORACAO "
        // + " from V_PROJECTOS p , web_user_projs up " + " where up.login = ?
        // and p.projectCode = up.id_proj order by p.projectCode";

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(query.toString());

            // stmt.setString(1, userLogin);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setProjectCode(rs.getString("projectCode"));
                project.setTitle(rs.getString("title"));
                project.setOrigin(rs.getString("origem"));
                project.setType(new LabelValueBean(rs.getString("tipo"), ""));
                project.setCost(rs.getString("custo"));
                project.setCoordination(rs.getString("coordenacao"));
                project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
                projects.add(project);
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return projects;
    }

    public List<Project> readByProjectsCodes(List<Integer> projectCodes) throws ExcepcaoPersistencia {
        List<Project> projects = new ArrayList<Project>();
        if (projectCodes != null && projectCodes.size() != 0) {
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer
                    .append("select p.projectCode, p.title, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO from  V_PROJECTOS p where p.projectCode IN (");
            for (int i = 0; i < projectCodes.size(); i++) {
                if (i != 0)
                    stringBuffer.append(", ");
                stringBuffer.append(projectCodes.get(i));
            }
            stringBuffer.append(") order by p.projectCode");
            String query = stringBuffer.toString();

            try {
                PersistentSuportOracle p = PersistentSuportOracle.getInstance();
                p.startTransaction();

                PreparedStatement stmt = p.prepareStatement(query);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectCode(rs.getString("projectCode"));
                    project.setTitle(rs.getString("title"));
                    project.setOrigin(rs.getString("origem"));
                    project.setType(new LabelValueBean(rs.getString("tipo"), ""));
                    project.setCost(rs.getString("custo"));
                    project.setCoordination(rs.getString("coordenacao"));
                    project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
                    projects.add(project);
                }
                rs.close();
                p.commitTransaction();
            } catch (SQLException e) {
                throw new ExcepcaoPersistencia();
            }
        }
        return projects;
    }

    public List<Project> readByCoordinatorAndNotProjectsCodes(Integer coordinatorId, List projectCodes) throws ExcepcaoPersistencia {
        List<Project> projects = new ArrayList<Project>();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("select p.projectCode, p.title, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO from  V_PROJECTOS p , web_user_projs up where up.login = '");
        stringBuffer.append(coordinatorId);
        stringBuffer.append("' and p.projectCode = up.id_proj");
        if (projectCodes != null && projectCodes.size() != 0) {
            stringBuffer.append(" and p.projectCode NOT IN (");
            for (int i = 0; i < projectCodes.size(); i++) {
                if (i != 0)
                    stringBuffer.append(", ");
                stringBuffer.append(projectCodes.get(i));
            }
            stringBuffer.append(")");
        }
        stringBuffer.append(" order by p.projectCode");
        String query = stringBuffer.toString();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setProjectCode(rs.getString("projectCode"));
                project.setTitle(rs.getString("title"));
                project.setOrigin(rs.getString("origem"));
                project.setType(new LabelValueBean(rs.getString("tipo"), ""));
                project.setCost(rs.getString("custo"));
                project.setCoordination(rs.getString("coordenacao"));
                project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
                projects.add(project);
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return projects;
    }

    public Project readProject(Integer projectCode) throws ExcepcaoPersistencia {
        String query = "select title, c.nome, tp.descricao, p.origem, p.tipo, p.custo, p.coordenacao, p.UNID_EXPLORACAO  from V_Projectos p, V_COORD c , V_TIPOS_PROJECTOS tp  where p.idCoord = c.idCoord and tp.cod = p.tipo and p.projectCode ="
                + projectCode;
        Project project = new Project();
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                project.setProjectCode(projectCode.toString());
                project.setTitle(rs.getString(1));
                project.setCoordinatorName(rs.getString(2));
                project.setOrigin(rs.getString("origem"));
                project.setType(new LabelValueBean(rs.getString("tipo"), rs.getString("descricao")));
                project.setCost(rs.getString("custo"));
                project.setCoordination(rs.getString("coordenacao"));
                project.setExplorationUnit(new Integer(rs.getInt("UNID_EXPLORACAO")));
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return project;
    }

    public boolean isUserProject(Integer userCode, Integer projectCode) throws ExcepcaoPersistencia {
        boolean result = false;
        String query = " select count(*) from web_user_projs up where up.login='" + userCode + "' and up.id_proj=" + projectCode;

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                if (rs.getInt(1) > 0)
                    result = true;

            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return result;
    }

    public int countUserProject(Integer userCode) throws ExcepcaoPersistencia {
        int result = 0;
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("select count(*) from web_user_projs up where up.login='");
        stringBuffer.append(userCode);
        stringBuffer.append("'");
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(stringBuffer.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                result = rs.getInt(1);
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return result;
    }

}
