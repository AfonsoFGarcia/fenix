
/*
 * 
 * Created on 16/Dez/2002
 *
 */
package middleware.ileecDataMigration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWDisciplinaIleec;
import middleware.middlewareDomain.MWPrecedenciaDisciplinaDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWPrecedenciaDisciplinaDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IPrecedence;
import Dominio.IRestriction;
import Dominio.IRestrictionByCurricularCourse;
import Dominio.Precedence;
import Dominio.RestrictionDoneCurricularCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.IPersistentRestriction;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;
import Util.PrecedenceScopeToApply;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public class migrateILeecPrecedenceDataByCurricularCourse
{

    public static int fenixPrecedenceCreated = 0;

    public static void main(String[] args)
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            migratePrecedenciasDisciplinaDisciplinaIleec(sp);

            System.out.println("Created Precedences: " + fenixPrecedenceCreated);
            System.out.println("FIM");
        }
        catch (ExcepcaoPersistencia e)
        {
            System.out.println("[ERROR] Could not obtain the ileec precendences");
            e.printStackTrace();
        }
    }

    /**
     * 
     * @throws Exception
     */
    public static void migratePrecedenciasDisciplinaDisciplinaIleec(ISuportePersistente sp)
    {
        try
        {

            PersistentMiddlewareSupportOJB persistentMiddlewareSupportOJB =
                PersistentMiddlewareSupportOJB.getInstance();
            IPersistentMWPrecedenciaDisciplinaDisciplinaIleec pmwpdd =
                persistentMiddlewareSupportOJB.getIPersistentMWPrecedenciaDisciplinaDisciplinaIleec();

            sp.iniciarTransaccao();

            List listPrecedences = pmwpdd.readAll();
            sp.confirmarTransaccao();

            Iterator iterator = listPrecedences.iterator();
            while (iterator.hasNext())
            {
                MWPrecedenciaDisciplinaDisciplinaIleec mwpdd =
                    (MWPrecedenciaDisciplinaDisciplinaIleec) iterator.next();

                MWDisciplinaIleec precedenteIleecCurricularCourse =
                    getILeecCurricularCourse(
                        sp,
                        persistentMiddlewareSupportOJB,
                        mwpdd.getIdPrecedente());

                MWDisciplinaIleec disciplinaIleecCurricularCourse =
                    getILeecCurricularCourse(
                        sp,
                        persistentMiddlewareSupportOJB,
                        mwpdd.getIdDisciplina());

                writePrecedences(disciplinaIleecCurricularCourse, precedenteIleecCurricularCourse);
            }
        }
        catch (ExcepcaoPersistencia e)
        {
            System.out.println("[ERROR] Could not obtain the ileec precendences");
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param sp
     * @param ileecDisciplinaComPrecedencia
     * @param ileecDisciplinaPrecedente
     * @throws ExcepcaoPersistencia
     */
    public static void writePrecedences(
        MWDisciplinaIleec ileecDisciplinaComPrecedencia,
        MWDisciplinaIleec ileecDisciplinaPrecedente)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentPrecedence pPrecedencia = sp.getIPersistentPrecedence();

            ICurricularCourse fenixDisciplinaComPrecedencia =
                getFenixCurricularCourse(sp, ileecDisciplinaComPrecedencia.getCodigoDisciplina());

            ICurricularCourse fenixDisciplinaPrecedente =
                getFenixCurricularCourse(sp, ileecDisciplinaPrecedente.getCodigoDisciplina());

            if (fenixDisciplinaComPrecedencia == null)
            {
                System.out.println(
                    "A cadeira do ileec nao existe no fenix e tem precedencia do tipo: "
                        + ileecDisciplinaComPrecedencia.getTipoPrecedencia());
                System.out.println(ileecDisciplinaComPrecedencia);
                return;
            }

            sp.iniciarTransaccao();
            List listaPrecedencias =
                pPrecedencia.readByCurricularCourse(
                    fenixDisciplinaComPrecedencia,
                    PrecedenceScopeToApply.TO_APPLY_TO_SPAN);
            sp.confirmarTransaccao();

            if ((listaPrecedencias == null) || (listaPrecedencias.isEmpty()))
            {

                fenixPrecedenceCreated++;
                IPrecedence precedence = createPrecedence(fenixDisciplinaComPrecedencia, true);
                IRestrictionByCurricularCourse restriction =
                    createRestriction(fenixDisciplinaPrecedente, precedence, true);

            }
            else
            {
                //System.out.println("Tipo de precedencia: " + ); 
                if (ileecDisciplinaComPrecedencia.getTipoPrecedencia().equals(new Integer(1)))
                {
                    System.out.println("vou TENTAR criar uma precedencia do tipo OU");
                    IPrecedence precedence = createPrecedence(fenixDisciplinaComPrecedencia, false);
                    IRestrictionByCurricularCourse restriction =
                        createRestriction(fenixDisciplinaPrecedente, precedence, false);

                    List listRestrictions = new ArrayList();
                    listRestrictions.add(restriction);
                    precedence.setRestrictions(listRestrictions);

                    if (!existingPrecedence(listaPrecedencias, precedence))
                    {
                        precedence = createPrecedence(fenixDisciplinaComPrecedencia, true);
                        restriction = createRestriction(fenixDisciplinaPrecedente, precedence, true);

                        System.out.println("vou CRIAR uma precedencia do tipo OU");
                    }
                }
                else
                {
                    System.out.println("vou TENTAR criar uma precedencia do tipo E");
                    IPrecedence precedence = (IPrecedence) listaPrecedencias.get(0);
                    IRestrictionByCurricularCourse restriction =
                        createRestriction(fenixDisciplinaPrecedente, precedence, false);

                    List listRestrictions = precedence.getRestrictions();
                    if (!existingRestrictions(listRestrictions, restriction))
                    {
                        restriction = createRestriction(fenixDisciplinaPrecedente, precedence, true);
                        System.out.println("vou CRIAR uma precedencia do tipo E");
                    }
                }
            }
        }
        catch (ExcepcaoPersistencia e)
        {
            System.out.println("[ERROR] Could not create the fenix precendences");
            e.printStackTrace();
        }
    }

    public static IPrecedence createPrecedence(
        ICurricularCourse curricularCourse,
        boolean lockPrecedence)
        throws ExcepcaoPersistencia
    {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentPrecedence pPrecedencia = sp.getIPersistentPrecedence();
        IPrecedence precedence = new Precedence();

        sp.iniciarTransaccao();
        if (lockPrecedence)
            pPrecedencia.simpleLockWrite(precedence);

        precedence.setCurricularCourse(curricularCourse);
        precedence.setPrecedenceScopeToApply(PrecedenceScopeToApply.TO_APPLY_TO_SPAN);
        sp.confirmarTransaccao();

        return precedence;
    }

    public static IRestrictionByCurricularCourse createRestriction(
        ICurricularCourse curricularCourse,
        IPrecedence precedence,
        boolean lockRestriction)
        throws ExcepcaoPersistencia
    {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentRestriction pRestricao = sp.getIPersistentRestriction();

        IRestrictionByCurricularCourse restriction =  new RestrictionDoneCurricularCourse();
        sp.iniciarTransaccao();
        if (lockRestriction)
            pRestricao.simpleLockWrite(restriction);

        restriction.setPrecedence(precedence);
        restriction.setPrecedentCurricularCourse(curricularCourse);

        sp.confirmarTransaccao();
        return restriction;
    }

    /**
     * 
     * @param persistentMiddlewareSupportOJB
     * @param curricularCourseId
     * @return
     */
    public static MWDisciplinaIleec getILeecCurricularCourse(
        ISuportePersistente sp,
        PersistentMiddlewareSupportOJB persistentMiddlewareSupportOJB,
        Integer curricularCourseId)
    {
        try
        {

            IPersistentMWDisciplinaIleec pmwd =
                persistentMiddlewareSupportOJB.getIPersistentMWDisciplinaIleec();

            sp.iniciarTransaccao();
            MWDisciplinaIleec mwCourseIleec = pmwd.readByIdInternal(curricularCourseId);
            sp.confirmarTransaccao();

            return mwCourseIleec;
        }
        catch (ExcepcaoPersistencia e)
        {
            System.out.println("[ERROR] Could not obtain the ileec curricular course");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param sp
     * @param curricularCourseCode
     * @return
     */
    public static CurricularCourse getFenixCurricularCourse(
        ISuportePersistente sp,
        String curricularCourseCode)
    {
        CurricularCourse fenixCurricularCourse = null;
        try
        {

            IPersistentCurricularCourse pCurricularCourse = sp.getIPersistentCurricularCourse();

            sp.iniciarTransaccao();
            IDegreeCurricularPlan dcp;
            dcp = getDegreeCurricularPlan(new Integer(14), sp);
            List listFenixCurricularCourse =
                pCurricularCourse.readbyCourseCodeAndDegreeCurricularPlan(curricularCourseCode, dcp);
            if (listFenixCurricularCourse.size() > 1)
            {
                System.out.println(
                    "[ERROR] Duplicate entry with code "
                        + curricularCourseCode
                        + " and DegreeCurricularPlan  "
                        + dcp);
            }

            if (!listFenixCurricularCourse.isEmpty())
            {
                fenixCurricularCourse = (CurricularCourse) listFenixCurricularCourse.get(0);
            }
            sp.confirmarTransaccao();
        }
        catch (Throwable e)
        {
            System.out.println("[ERROR] Could not obtain fenix curricular course");
            e.printStackTrace();
        }

        return fenixCurricularCourse;
    }

    /**
    * @param degreeCode
    * @param fenixPersistentSuport
    * @return
    * @throws Throwable
    */
    private static IDegreeCurricularPlan getDegreeCurricularPlan(
        Integer degreeCode,
        ISuportePersistente fenixPersistentSuport)
        throws Throwable
    {
        IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
        IPersistentMWDegreeTranslation persistentMWDegreeTranslation =
            mws.getIPersistentMWDegreeTranslation();
        IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
            fenixPersistentSuport.getIPersistentDegreeCurricularPlan();
        MWDegreeTranslation mwDegreeTranslation =
            persistentMWDegreeTranslation.readByDegreeCode(degreeCode);
        if (mwDegreeTranslation != null)
        {
            ICurso degree = mwDegreeTranslation.getDegree();
            List result =
                persistentDegreeCurricularPlan.readByDegreeAndState(
                    degree,
                    DegreeCurricularPlanState.ACTIVE_OBJ);
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) result.get(0);
            return degreeCurricularPlan;
        }
        else
        {
            return null;
        }
    }

    /**
     * 
     * @param listRestrictions
     * @param restriction
     * @return
     */
    public static boolean existingRestrictions(
        List listRestrictions,
        IRestrictionByCurricularCourse restriction)
    {

        if (listRestrictions == null)
            return false;

        Iterator iterator = listRestrictions.iterator();
        while (iterator.hasNext())
        {
            Object restr = iterator.next();

            if (!(restr instanceof IRestrictionByCurricularCourse))
                return false;

            if (restr.equals(restriction))
                return true;
        }

        return false;
    }

    /**
     * 
     * @param listPrecedences
     * @param precedence
     * @return
     */
    public static boolean existingPrecedence(List listPrecedences, IPrecedence precedence) throws ExcepcaoPersistencia
    {
        Iterator iterator = listPrecedences.iterator();
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        
        while (iterator.hasNext())
        {
            sp.iniciarTransaccao();
            IPrecedence prec = (IPrecedence) iterator.next();
            if (precedence.equals(prec))
            {
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                System.out.println("precedencia existente vou retornar TRUE");
                //System.out.println("CC: " + prec.getCurricularCourse());
                //System.out.println("Restricao: "+ prec.getRestrictions());
                IRestriction r;
                r = (IRestriction) precedence;
                System.out.println(r);
            
                System.out.println("------------------------------------------------------");
                //System.out.println("CC: " + precedence.getCurricularCourse());
                //System.out.println("Restricao: "+ precedence.getRestrictions());
                r = (IRestriction) prec;
                System.out.println(r);
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                return true;
            }
            sp.confirmarTransaccao();
        }
        return false;
    }
}
