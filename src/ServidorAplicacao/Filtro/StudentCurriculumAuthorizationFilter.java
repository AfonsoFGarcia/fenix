package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoRole;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.ITutor;
import Dominio.StudentCurricularPlan;
import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IGroupProposal;
import Dominio.finalDegreeWork.IProposal;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 * @author Fernanda Quit�rio 12/Fev/2004
 */
public class StudentCurriculumAuthorizationFilter extends AccessControlFilter {

    public StudentCurriculumAuthorizationFilter() {
        super();
    }

    /**
     * @param collection
     * @return boolean
     */
    private boolean containsRole(Collection roles) {
        CollectionUtils.intersection(roles, getNeededRoles());

        if (roles.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response)
            throws Exception {
        IUserView id = (IUserView) request.getRequester();
        String messageException = hasProvilege(id, request.getArguments());
        if ((id == null) || (id.getRoles() == null)
                || (!containsRole(id.getRoles())) || (messageException != null)) {
            throw new NotAuthorizedFilterException(messageException);
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    private Collection getNeededRoles() {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.TEACHER);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.STUDENT);
        roles.add(infoRole);

        return roles;
    }

    private List getRoleList(List roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

    /**
     * @param id
     * @param argumentos
     * @return null if authorized string with message if not authorized
     */
    private String hasProvilege(IUserView id, Object[] arguments) {
        List roles = getRoleList((List) id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        Integer studentCurricularPlanID = (Integer) arguments[1];

        IStudentCurricularPlan studentCurricularPlan = null;

        // Read The DegreeCurricularPlan
        try {
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = SuportePersistenteOJB
                    .getInstance().getIStudentCurricularPlanPersistente();

            studentCurricularPlan = readStudentCurricularPlan(
                    studentCurricularPlanID, persistentStudentCurricularPlan);

        } catch (Exception e) {
            return "noAuthorization";
        }

        if (studentCurricularPlan == null
                || studentCurricularPlan.getStudent() == null) {
            return "noAuthorization";
        }

        try {
            IStudent student = studentCurricularPlan.getStudent();

            IPersistentFinalDegreeWork persistentFinalDegreeWork = SuportePersistenteOJB
                    .getInstance().getIPersistentFinalDegreeWork();

            IGroup group = persistentFinalDegreeWork
                    .readFinalDegreeWorkGroupByUsername(student.getPerson()
                            .getUsername());
            if (group != null) {
                ICursoExecucao executionDegree = group.getExecutionDegree();
                for (int i = 0; i < executionDegree.getCoordinatorsList()
                        .size(); i++) {
                    ICoordinator coordinator = (ICoordinator) executionDegree
                            .getCoordinatorsList().get(i);
                    if (coordinator.getTeacher().getPerson().getUsername()
                            .equals(id.getUtilizador())) {
                        // The student is a candidate for a final degree work of
                        // the degree of the
                        // coordinator making the request. Allow access.
                        return null;
                    }
                }

                for (int i = 0; i < group.getGroupProposals().size(); i++) {
                    IGroupProposal groupProposal = (IGroupProposal) group
                            .getGroupProposals().get(i);
                    IProposal proposal = groupProposal
                            .getFinalDegreeWorkProposal();
                    ITeacher teacher = proposal.getOrientator();

                    if (teacher.getPerson().getUsername().equals(
                            id.getUtilizador())) {
                        // The student is a candidate for a final degree work of
                        // oriented by the
                        // teacher making the request. Allow access.
                        return null;
                    }

                    teacher = proposal.getCoorientator();
                    if (teacher != null
                            && teacher.getPerson().getUsername().equals(
                                    id.getUtilizador())) {
                        // The student is a candidate for a final degree work of
                        // cooriented by the
                        // teacher making the request. Allow access.
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            // check other possible authorizations
        }

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            if (!studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                    .getTipoCurso().equals(TipoCurso.MESTRADO_OBJ)
                    && !(roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE) || roles
                            .contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))) {
                return "noAuthorization";
            }
            if (!(roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE) || roles
                    .contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))) {
                return null;
            }
        }

        if (arguments[0] != null) {
            roleTemp = new ArrayList();
            roleTemp.add(RoleType.COORDINATOR);
            if (CollectionUtils.containsAny(roles, roleTemp)) {
                try {
                    ISuportePersistente sp = SuportePersistenteOJB
                            .getInstance();
                    ICursoExecucaoPersistente persistentExecutionDegree = sp
                            .getICursoExecucaoPersistente();

                    ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree
                            .readByOID(CursoExecucao.class,
                                    (Integer) arguments[0]);

                    if (executionDegree == null) {
                        return "noAuthorization";
                    }
                    IPersistentCoordinator persistentCoordinator = sp
                            .getIPersistentCoordinator();
                    List coordinatorsList = persistentCoordinator
                            .readCoordinatorsByExecutionDegree(executionDegree);
                    if (coordinatorsList == null) {
                        return "noAuthorization";
                    }

                    final String username = id.getUtilizador();
                    ICoordinator coordinator = (ICoordinator) CollectionUtils
                            .find(coordinatorsList, new Predicate() {

                                public boolean evaluate(Object input) {
                                    ICoordinator coordinatorTemp = (ICoordinator) input;
                                    if (username.equals(coordinatorTemp
                                            .getTeacher().getPerson()
                                            .getUsername())) {
                                        return true;
                                    }
                                    return false;
                                }
                            });
                    if (coordinator == null) {
                        return "noAuthorization";
                    }

                    if (!coordinator.getExecutionDegree().getCurricularPlan()
                            .getDegree().getIdInternal().equals(
                                    studentCurricularPlan
                                            .getDegreeCurricularPlan()
                                            .getDegree().getIdInternal())) {
                        return "noAuthorization";
                    }
                    IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                            .getIStudentCurricularPlanPersistente();
                    List activeStudentCurricularPlans = persistentStudentCurricularPlan
                            .readAllActiveStudentCurricularPlan(studentCurricularPlan
                                    .getStudent().getNumber());
                    boolean hasAnActiveCurricularPlanThatCoincidesWithTheCoordinatorsCurricularPlan = false;
                    for (int i = 0; i < activeStudentCurricularPlans.size(); i++) {
                        IStudentCurricularPlan activeStudentCurricularPlan = (IStudentCurricularPlan) activeStudentCurricularPlans
                                .get(i);
                        if (coordinator.getExecutionDegree()
                                .getCurricularPlan().getIdInternal().equals(
                                        activeStudentCurricularPlan
                                                .getDegreeCurricularPlan()
                                                .getIdInternal())) {
                            hasAnActiveCurricularPlanThatCoincidesWithTheCoordinatorsCurricularPlan = true;
                        }
                    }
                    if (!hasAnActiveCurricularPlanThatCoincidesWithTheCoordinatorsCurricularPlan) {
                        return "noAuthorization";
                    }
                } catch (Exception e) {
                    return "noAuthorization";
                }
                return null;
            }
        }
        roleTemp = new ArrayList();
        roleTemp.add(RoleType.STUDENT);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            try {
                if (!id.getUtilizador().equals(
                        studentCurricularPlan.getStudent().getPerson()
                                .getUsername())) {
                    return "noAuthorization";
                }
            } catch (Exception e) {
                return "noAuthorization";
            }
            return null;
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.TEACHER);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            try {
                ITeacher teacher = SuportePersistenteOJB.getInstance()
                        .getIPersistentTeacher().readTeacherByUsername(
                                id.getUtilizador());
                if (teacher == null) {
                    return "noAuthorization";
                }

                if (!verifyStudentTutor(teacher, studentCurricularPlan
                        .getStudent())) {
                    return new String("error.enrollment.notStudentTutor+"
                            + studentCurricularPlan.getStudent().getNumber()
                                    .toString());
                }

            } catch (Exception e) {
                return "noAuthorization";
            }
            return null;
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roleTemp.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            if (!studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                    .getTipoCurso().equals(TipoCurso.LICENCIATURA_OBJ)) {
                return "noAuthorization";
            }
            return null;
        }
        return "noAuthorization";
    }

    private boolean verifyStudentTutor(ITeacher teacher, IStudent student)
            throws ExcepcaoPersistencia {
        ITutor tutor = SuportePersistenteOJB.getInstance()
                .getIPersistentTutor().readTutorByTeacherAndStudent(teacher,
                        student);

        return (tutor != null);
    }

    private IStudentCurricularPlan readStudentCurricularPlan(
            Integer studentCurricularPlanID,
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan)
            throws ExcepcaoPersistencia {
        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
        return studentCurricularPlan;
    }
}