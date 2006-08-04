/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateCurricularCourse extends Service {

    private DegreeCurricularPlan degreeCurricularPlan = null;
    private CourseGroup parentCourseGroup = null;
    private CurricularPeriod curricularPeriod = null;
    private ExecutionPeriod beginExecutionPeriod = null;
    private ExecutionPeriod endExecutionPeriod = null;

    public void run(CreateCurricularCourseArgs createCurricularCourseArgs) throws ExcepcaoPersistencia,
            FenixServiceException {

        readDomainObjects(createCurricularCourseArgs);

        final CompetenceCourse competenceCourse = rootDomainObject
                .readCompetenceCourseByOID(createCurricularCourseArgs.getCompetenceCourseID());
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        degreeCurricularPlan.createCurricularCourse(createCurricularCourseArgs.getWeight(),
                createCurricularCourseArgs.getPrerequisites(), createCurricularCourseArgs
                        .getPrerequisitesEn(), CurricularStage.DRAFT, competenceCourse,
                parentCourseGroup, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    public void run(CreateOptionalCurricularCourseArgs createOptionalCurricularCourseArgs)
            throws ExcepcaoPersistencia, FenixServiceException {

        readDomainObjects(createOptionalCurricularCourseArgs);

        degreeCurricularPlan.createCurricularCourse(parentCourseGroup,
                createOptionalCurricularCourseArgs.getName(), createOptionalCurricularCourseArgs
                        .getNameEn(), CurricularStage.DRAFT, curricularPeriod, beginExecutionPeriod,
                endExecutionPeriod);
    }

    protected void readDomainObjects(CurricularCourseArgs curricularCourseArgs)
            throws ExcepcaoPersistencia, FenixServiceException {

        degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(curricularCourseArgs.getDegreeCurricularPlanID());
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }

        parentCourseGroup = (CourseGroup) rootDomainObject.readDegreeModuleByOID(curricularCourseArgs.getParentCourseGroupID());
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }

        final CurricularPeriod degreeCurricularPeriod = (CurricularPeriod) degreeCurricularPlan.getDegreeStructure();
        
        
        // ********************************************************
        /* TODO: Important - change this code (must be generic to support several curricularPeriodInfoDTOs,
         *                   instead of year and semester)
         * 
         */
        CurricularPeriodInfoDTO curricularPeriodInfoYear = null;
        if (degreeCurricularPlan.getDegree().getDegreeType().getYears() > 1) {
            curricularPeriodInfoYear = new CurricularPeriodInfoDTO(curricularCourseArgs.getYear(), CurricularPeriodType.YEAR);
        }
        final CurricularPeriodInfoDTO curricularPeriodInfoSemester = new CurricularPeriodInfoDTO(curricularCourseArgs.getSemester(), CurricularPeriodType.SEMESTER);
        
        if (curricularPeriodInfoYear != null) {
            curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(curricularPeriodInfoYear, curricularPeriodInfoSemester);
            if (curricularPeriod == null) {
                curricularPeriod = degreeCurricularPeriod.addCurricularPeriod(curricularPeriodInfoYear, curricularPeriodInfoSemester);
            }
        } else {
            curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(curricularPeriodInfoSemester);
            if (curricularPeriod == null) {
                curricularPeriod = degreeCurricularPeriod.addCurricularPeriod(curricularPeriodInfoSemester);
            }
        }
        
        // ********************************************************

        beginExecutionPeriod = getBeginExecutionPeriod(curricularCourseArgs);
        endExecutionPeriod = (curricularCourseArgs.getEndExecutionPeriodID() == null) ? null : 
            rootDomainObject.readExecutionPeriodByOID(curricularCourseArgs.getEndExecutionPeriodID());
    }

    private ExecutionPeriod getBeginExecutionPeriod(CurricularCourseArgs curricularCourseArgs) throws FenixServiceException {
        if (curricularCourseArgs.getBeginExecutionPeriodID() == null) {
            final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            final ExecutionYear nextExecutionYear = currentExecutionYear.getNextExecutionYear();
            if (nextExecutionYear == null) {
                throw new FenixServiceException("error.no.next.execution.year");
            }
            return nextExecutionYear.readExecutionPeriodForSemester(Integer.valueOf(1));
        } else {
            return rootDomainObject.readExecutionPeriodByOID(curricularCourseArgs.getBeginExecutionPeriodID());
        }
    }
    
    private abstract static class CurricularCourseArgs {
        private Integer degreeCurricularPlanID, parentCourseGroupID, year, semester; 
        private Integer beginExecutionPeriodID, endExecutionPeriodID;
        
        public Integer getBeginExecutionPeriodID() {
            return beginExecutionPeriodID;
        }
        public void setBeginExecutionPeriodID(Integer beginExecutionPeriodID) {
            this.beginExecutionPeriodID = beginExecutionPeriodID;
        }
        public Integer getDegreeCurricularPlanID() {
            return degreeCurricularPlanID;
        }
        public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
            this.degreeCurricularPlanID = degreeCurricularPlanID;
        }
        public Integer getEndExecutionPeriodID() {
            return endExecutionPeriodID;
        }
        public void setEndExecutionPeriodID(Integer endExecutionPeriodID) {
            this.endExecutionPeriodID = endExecutionPeriodID;
        }
        public Integer getParentCourseGroupID() {
            return parentCourseGroupID;
        }
        public void setParentCourseGroupID(Integer parentCourseGroupID) {
            this.parentCourseGroupID = parentCourseGroupID;
        }
        public Integer getSemester() {
            return semester;
        }
        public void setSemester(Integer semester) {
            this.semester = semester;
        }
        public Integer getYear() {
            return year;
        }
        public void setYear(Integer year) {
            this.year = year;
        }
    }

    public static class CreateCurricularCourseArgs extends CurricularCourseArgs {
        private Double weight;
        private String prerequisites, prerequisitesEn;
        private Integer competenceCourseID;

        public CreateCurricularCourseArgs(Double weight, String prerequisites, String prerequisitesEn,
                Integer competenceCourseID, Integer parentCourseGroupID, Integer year, Integer semester,
                Integer degreeCurricularPlanID, Integer beginExecutionPeriodID,
                Integer endExecutionPeriodID) {

            setWeight(weight);
            setPrerequisites(prerequisites);
            setPrerequisitesEn(prerequisitesEn);
            setCompetenceCourseID(competenceCourseID);
            setParentCourseGroupID(parentCourseGroupID);
            setYear(year);
            setSemester(semester);
            setDegreeCurricularPlanID(degreeCurricularPlanID);
            setBeginExecutionPeriodID(beginExecutionPeriodID);
            setEndExecutionPeriodID(endExecutionPeriodID);
        }

        public Integer getCompetenceCourseID() {
            return competenceCourseID;
        }
        public void setCompetenceCourseID(Integer competenceCourseID) {
            this.competenceCourseID = competenceCourseID;
        }
        public String getPrerequisites() {
            return prerequisites;
        }
        public void setPrerequisites(String prerequisites) {
            this.prerequisites = prerequisites;
        }
        public String getPrerequisitesEn() {
            return prerequisitesEn;
        }
        public void setPrerequisitesEn(String prerequisitesEn) {
            this.prerequisitesEn = prerequisitesEn;
        }
        public Double getWeight() {
            return weight;
        }
        public void setWeight(Double weight) {
            this.weight = weight;
        }
    }

    public static class CreateOptionalCurricularCourseArgs extends CurricularCourseArgs {
        private String name, nameEn;

        public CreateOptionalCurricularCourseArgs(Integer degreeCurricularPlanID,
                Integer parentCourseGroupID, String name, String nameEn, Integer year, Integer semester,
                Integer beginExecutionPeriodID, Integer endExecutionPeriodID) {

            setDegreeCurricularPlanID(degreeCurricularPlanID);
            setParentCourseGroupID(parentCourseGroupID);
            setName(name);
            setNameEn(nameEn);
            setYear(year);
            setSemester(semester);
            setBeginExecutionPeriodID(beginExecutionPeriodID);
            setEndExecutionPeriodID(endExecutionPeriodID);
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getNameEn() {
            return nameEn;
        }
        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }
    }
}
