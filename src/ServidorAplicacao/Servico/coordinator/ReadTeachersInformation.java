/*
 * Created on Dec 17, 2003
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;
import DataBeans.teacher.InfoOrientation;
import DataBeans.teacher.InfoPublicationsNumber;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoSiteTeacherInformation;
import DataBeans.teacher.InfoWeeklyOcupation;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IProfessorship;
import Dominio.IQualification;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.teacher.ICareer;
import Dominio.teacher.IExternalActivity;
import Dominio.teacher.IOldPublication;
import Dominio.teacher.IOrientation;
import Dominio.teacher.IPublicationsNumber;
import Dominio.teacher.IServiceProviderRegime;
import Dominio.teacher.IWeeklyOcupation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentCareer;
import ServidorPersistente.teacher.IPersistentExternalActivity;
import ServidorPersistente.teacher.IPersistentOldPublication;
import ServidorPersistente.teacher.IPersistentOrientation;
import ServidorPersistente.teacher.IPersistentPublicationsNumber;
import ServidorPersistente.teacher.IPersistentServiceProviderRegime;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;
import Util.CareerType;
import Util.OldPublicationType;
import Util.OrientationType;
import Util.PublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadTeachersInformation implements IService
{
    /**
	 *  
	 */
    public ReadTeachersInformation()
    {}

    public List run(Integer executionDegreeId) throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();

            ICursoExecucao executionDegree =
                (ICursoExecucao) persistentExecutionDegree.readByOID(
                    CursoExecucao.class,
                    executionDegreeId);

            List professorships = persistentProfessorship.readByExecutionDegree(executionDegree);
            List teachers = (List) CollectionUtils.collect(professorships, new Transformer()
            {
                public Object transform(Object o)
                {
                    IProfessorship professorship = (IProfessorship) o;
                    return professorship.getTeacher();
                }
            });
            teachers = removeDuplicates(teachers);
            List infoSiteTeachersInformation = new ArrayList();
            Iterator iter = teachers.iterator();
            while (iter.hasNext())
            {
                ITeacher teacher = (ITeacher) iter.next();
                infoSiteTeachersInformation.add(
                    getTeacherInformation(teacher.getPerson().getUsername()));
            }
            Collections.sort(infoSiteTeachersInformation, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    InfoSiteTeacherInformation information1 = (InfoSiteTeacherInformation) o1;
                    InfoSiteTeacherInformation information2 = (InfoSiteTeacherInformation) o2;
                    return information1.getInfoTeacher().getInfoPerson().getNome().compareTo(
                        information2.getInfoTeacher().getInfoPerson().getNome());
                }
            });
            return infoSiteTeachersInformation;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
	 * @param teachers
	 * @return
	 */
    private List removeDuplicates(List teachers)
    {
        List result = new ArrayList();
        Iterator iter = teachers.iterator();
        while (iter.hasNext())
        {
            ITeacher teacher = (ITeacher) iter.next();
            if (!result.contains(teacher))
                result.add(teacher);
        }

        return result;
    }

    private InfoSiteTeacherInformation getTeacherInformation(String user) throws ExcepcaoPersistencia
    {

        InfoSiteTeacherInformation infoSiteTeacherInformation = new InfoSiteTeacherInformation();

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
        infoSiteTeacherInformation.setInfoTeacher(infoTeacher);

        infoSiteTeacherInformation.setInfoQualifications(getInfoQualifications(sp, teacher));

        infoSiteTeacherInformation.setInfoProfessionalCareers(
            getInfoCareers(sp, teacher, CareerType.PROFESSIONAL));
        infoSiteTeacherInformation.setInfoTeachingCareers(
            getInfoCareers(sp, teacher, CareerType.TEACHING));

        IPersistentServiceProviderRegime persistentServiceProviderRegime =
            sp.getIPersistentServiceProviderRegime();
        IServiceProviderRegime serviceProviderRegime =
            persistentServiceProviderRegime.readByTeacher(teacher);
        if (serviceProviderRegime == null)
        {
            InfoServiceProviderRegime infoServiceProviderRegime = new InfoServiceProviderRegime();
            infoServiceProviderRegime.setInfoTeacher(infoTeacher);
            infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
        } else
        {
            InfoServiceProviderRegime infoServiceProviderRegime =
                Cloner.copyIServiceProviderRegime2InfoServiceProviderRegime(serviceProviderRegime);
            infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
        }

        infoSiteTeacherInformation.setInfoExternalActivities(getInfoExternalActivities(sp, teacher));

        infoSiteTeacherInformation.setInfoLecturingExecutionCourses(
            getInfoLecturingExecutionCourses(teacher, sp));
        infoSiteTeacherInformation.setInfoResponsibleExecutionCourses(
            getInfoResponsibleExecutionCourses(teacher, sp));

        IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
        IWeeklyOcupation weeklyOcupation = persistentWeeklyOcupation.readByTeacher(teacher);
        if (weeklyOcupation == null)
        {
            InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
            infoWeeklyOcupation.setInfoTeacher(infoTeacher);
            infoWeeklyOcupation.setResearch(new Integer(0));
            infoWeeklyOcupation.setManagement(new Integer(0));
            infoWeeklyOcupation.setLecture(new Integer(0));
            infoWeeklyOcupation.setSupport(new Integer(0));
            infoWeeklyOcupation.setOther(new Integer(0));
            infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
        } else
        {
            InfoWeeklyOcupation infoWeeklyOcupation =
                Cloner.copyIWeeklyOcupation2InfoWeeklyOcupation(weeklyOcupation);
            infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
        }

        infoSiteTeacherInformation.setInfoDegreeOrientation(
            getInfoOrientation(sp, teacher, OrientationType.DEGREE));
        infoSiteTeacherInformation.setInfoMasterOrientation(
            getInfoOrientation(sp, teacher, OrientationType.MASTER));
        infoSiteTeacherInformation.setInfoPhdOrientation(
            getInfoOrientation(sp, teacher, OrientationType.PHD));

        infoSiteTeacherInformation.setInfoArticleChapterPublicationsNumber(
            getInfoPublicationsNumber(sp, teacher, PublicationType.ARTICLES_CHAPTERS));
        infoSiteTeacherInformation.setInfoEditBookPublicationsNumber(
            getInfoPublicationsNumber(sp, teacher, PublicationType.EDITOR_BOOK));
        infoSiteTeacherInformation.setInfoAuthorBookPublicationsNumber(
            getInfoPublicationsNumber(sp, teacher, PublicationType.AUTHOR_BOOK));
        infoSiteTeacherInformation.setInfoMagArticlePublicationsNumber(
            getInfoPublicationsNumber(sp, teacher, PublicationType.MAG_ARTICLE));
        infoSiteTeacherInformation.setInfoComunicationPublicationsNumber(
            getInfoPublicationsNumber(sp, teacher, PublicationType.COMUNICATION));

        infoSiteTeacherInformation.setInfoOldCientificPublications(
            getInfoOldPublications(sp, teacher, OldPublicationType.CIENTIFIC));
        infoSiteTeacherInformation.setInfoOldDidacticPublications(
            getInfoOldPublications(sp, teacher, OldPublicationType.DIDACTIC));

        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        infoSiteTeacherInformation.setInfoExecutionPeriod(
            Cloner.copyIExecutionPeriod2InfoExecutionPeriod(executionPeriod));

        return infoSiteTeacherInformation;
    }

    /**
	 * @param sp
	 * @param teacher
	 * @return
	 */
    private List getInfoResponsibleExecutionCourses(ITeacher teacher, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
        final IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        List responsiblesFor = persistentResponsibleFor.readByTeacher(teacher);

        // filter only the execution courses of the current execution year
        responsiblesFor = (List) CollectionUtils.select(responsiblesFor, new Predicate()
        {
            IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
            public boolean evaluate(Object o)
            {
                IResponsibleFor responsibleFor = (IResponsibleFor) o;
                IExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
                IExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
                return executionYear.equals(this.executionYear);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(responsiblesFor, new Transformer()
        {
            public Object transform(Object o)
            {
                IResponsibleFor responsibleFor = (IResponsibleFor) o;
                IExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                List infoCurricularCourses =
                    (List) CollectionUtils.collect(curricularCourses, new Transformer()
                {
                    public Object transform(Object o)
                    {
                        ICurricularCourse curricularCourse = (ICurricularCourse) o;
                        return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                    }
                });
                InfoExecutionCourse infoExecutionCourse =
                    Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
                infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
                return infoExecutionCourse;
            }
        });
        return infoExecutionCourses;
    }

    private List getInfoExternalActivities(ISuportePersistente sp, ITeacher teacher)
        throws ExcepcaoPersistencia
    {
        IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
        List externalActivities = persistentExternalActivity.readAllByTeacher(teacher);

        List infoExternalActivities =
            (List) CollectionUtils.collect(externalActivities, new Transformer()
        {
            public Object transform(Object o)
            {
                IExternalActivity externalActivity = (IExternalActivity) o;
                return Cloner.copyIExternalActivity2InfoExternalActivity(externalActivity);
            }
        });
        return infoExternalActivities;
    }

    private List getInfoLecturingExecutionCourses(ITeacher teacher, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        final IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        List professorships = persistentProfessorship.readByTeacher(teacher);

        // filter only the execution courses of the current execution year
        professorships = (List) CollectionUtils.select(professorships, new Predicate()
        {
            IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
            public boolean evaluate(Object o)
            {
                IProfessorship professorship = (IProfessorship) o;
                IExecutionCourse executionCourse = professorship.getExecutionCourse();
                IExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
                return executionYear.equals(this.executionYear);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(professorships, new Transformer()
        {
            public Object transform(Object o)
            {
                IProfessorship professorship = (IProfessorship) o;
                IExecutionCourse executionCourse = professorship.getExecutionCourse();
                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                List infoCurricularCourses =
                    (List) CollectionUtils.collect(curricularCourses, new Transformer()
                {
                    public Object transform(Object o)
                    {
                        ICurricularCourse curricularCourse = (ICurricularCourse) o;
                        return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                    }
                });
                InfoExecutionCourse infoExecutionCourse =
                    Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
                infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
                return infoExecutionCourse;
            }
        });
        return infoExecutionCourses;
    }

    private List getInfoQualifications(ISuportePersistente sp, ITeacher teacher)
        throws ExcepcaoPersistencia
    {
        IPersistentQualification persistentQualification = sp.getIPersistentQualification();
        List qualifications = persistentQualification.readQualificationsByPerson(teacher.getPerson());
        List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer()
        {
            public Object transform(Object o)
            {
                IQualification qualification = (IQualification) o;
                return Cloner.copyIQualification2InfoQualification(qualification);
            }
        });
        return infoQualifications;
    }

    private List getInfoCareers(ISuportePersistente sp, ITeacher teacher, CareerType careerType)
        throws ExcepcaoPersistencia
    {
        IPersistentCareer persistentCareer = sp.getIPersistentCareer();
        List careers = persistentCareer.readAllByTeacherAndCareerType(teacher, careerType);
        List infoCareers = (List) CollectionUtils.collect(careers, new Transformer()
        {
            public Object transform(Object o)
            {
                ICareer career = (ICareer) o;
                return Cloner.copyICareer2InfoCareer(career);
            }
        });
        return infoCareers;
    }

    private List getInfoOldPublications(
        ISuportePersistente sp,
        ITeacher teacher,
        OldPublicationType oldPublicationType)
        throws ExcepcaoPersistencia
    {
        IPersistentOldPublication persistentOldPublication = sp.getIPersistentOldPublication();
        List oldCientificPublications =
            persistentOldPublication.readAllByTeacherAndOldPublicationType(teacher, oldPublicationType);

        List infoOldPublications =
            (List) CollectionUtils.collect(oldCientificPublications, new Transformer()
        {
            public Object transform(Object o)
            {
                IOldPublication oldPublication = (IOldPublication) o;
                return Cloner.copyIOldPublication2InfoOldPublication(oldPublication);
            }
        });
        return infoOldPublications;
    }

    private InfoOrientation getInfoOrientation(
        ISuportePersistente sp,
        ITeacher teacher,
        OrientationType orientationType)
        throws ExcepcaoPersistencia
    {
        IPersistentOrientation persistentOrientation = sp.getIPersistentOrientation();
        IOrientation orientation =
            persistentOrientation.readByTeacherAndOrientationType(teacher, orientationType);
        InfoOrientation infoOrientation = null;
        if (orientation != null)
        {
            infoOrientation = Cloner.copyIOrientation2InfoOrientation(orientation);
        } else
        {
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            infoOrientation = new InfoOrientation();
            infoOrientation.setInfoTeacher(infoTeacher);
            infoOrientation.setOrientationType(orientationType);
        }
        return infoOrientation;
    }

    private InfoPublicationsNumber getInfoPublicationsNumber(
        ISuportePersistente sp,
        ITeacher teacher,
        PublicationType publicationType)
        throws ExcepcaoPersistencia
    {
        IPersistentPublicationsNumber persistentPublicationsNumber =
            sp.getIPersistentPublicationsNumber();
        IPublicationsNumber publicationsNumber =
            persistentPublicationsNumber.readByTeacherAndPublicationType(teacher, publicationType);
        InfoPublicationsNumber infoPublicationsNumber = null;
        if (publicationsNumber != null)
        {
            infoPublicationsNumber =
                Cloner.copyIPublicationsNumber2InfoPublicationsNumber(publicationsNumber);
        } else
        {
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            infoPublicationsNumber = new InfoPublicationsNumber();
            infoPublicationsNumber.setInfoTeacher(infoTeacher);
            infoPublicationsNumber.setPublicationType(publicationType);
        }
        return infoPublicationsNumber;
    }
}
