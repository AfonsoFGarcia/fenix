/*
 * Created on 21/Mar/2003
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.InfoGuide;
import DataBeans.guide.reimbursementGuide.InfoReimbursementGuide;
import DataBeans.util.Cloner;
import Dominio.IGuide;
import Dominio.IPessoa;
import Dominio.reimbursementGuide.IReimbursementGuide;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChooseGuide implements IServico
{

    private static ChooseGuide servico = new ChooseGuide();

    /**
	 * The singleton access method of this class.
	 *  
	 */
    public static ChooseGuide getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private ChooseGuide()
    {
    }

    /**
	 * Returns The Service Name
	 */

    public final String getNome()
    {
        return "ChooseGuide";
    }

    public List run(Integer guideNumber, Integer guideYear) throws FenixServiceException
    {

        ISuportePersistente sp = null;
        List guides = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            guides = sp.getIPersistentGuide().readByNumberAndYear(guideNumber, guideYear);

        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (guides == null)
        {
            throw new NonExistingServiceException();
        }

        Iterator iterator = guides.iterator();
        List result = new ArrayList();
        while (iterator.hasNext())
        {
            IGuide guide = (IGuide) iterator.next();
            InfoGuide infoGuide = Cloner.copyIGuide2InfoGuide(guide);
            List infoReimbursementGuides = new ArrayList();
            if (guide.getReimbursementGuides() != null)
            {
                Iterator iter = guide.getReimbursementGuides().iterator();
                while (iter.hasNext())
                {
                    IReimbursementGuide reimbursementGuide = (IReimbursementGuide) iter.next();
                    InfoReimbursementGuide infoReimbursementGuide =
                        Cloner.copyIReimbursementGuide2InfoReimbursementGuide(reimbursementGuide);
                    infoReimbursementGuides.add(infoReimbursementGuide);

                }
            }
            infoGuide.setInfoReimbursementGuides(infoReimbursementGuides);
            result.add(infoGuide);
        }
        return result;
    }

    public InfoGuide run(Integer guideNumber, Integer guideYear, Integer guideVersion) throws Exception
    {

        ISuportePersistente sp = null;
        IGuide guide = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();

            guide =
                sp.getIPersistentGuide().readByNumberAndYearAndVersion(
                    guideNumber,
                    guideYear,
                    guideVersion);

        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (guide == null)
        {
            throw new NonExistingServiceException();
        }
        InfoGuide infoGuide = Cloner.copyIGuide2InfoGuide(guide);
        List infoReimbursementGuides = new ArrayList();
        if (guide.getReimbursementGuides() != null)
        {
            Iterator iter = guide.getReimbursementGuides().iterator();
            while (iter.hasNext())
            {
                IReimbursementGuide reimbursementGuide = (IReimbursementGuide) iter.next();
                InfoReimbursementGuide infoReimbursementGuide =
                    Cloner.copyIReimbursementGuide2InfoReimbursementGuide(reimbursementGuide);
                infoReimbursementGuides.add(infoReimbursementGuide);

            }

        }
        infoGuide.setInfoReimbursementGuides(infoReimbursementGuides);
        return infoGuide;
    }

    public List run(Integer guideYear) throws Exception
    {

        ISuportePersistente sp = null;
        List guides = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();

            guides = sp.getIPersistentGuide().readByYear(guideYear);

        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (guides == null)
        {
            throw new NonExistingServiceException();
        }
        BeanComparator numberComparator = new BeanComparator("number");
        BeanComparator versionComparator = new BeanComparator("version");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(numberComparator);
        chainComparator.addComparator(versionComparator);
        Collections.sort(guides, chainComparator);

        //	CollectionUtils.filter(guides,)
        List result = getLatestVersions(guides);

        return result;

    }

    /**
	 * 
	 * This function expects to receive a list ordered by number (Ascending)
	 * and version (Descending)
	 * 
	 * @param guides
	 * @return The latest version for the guides
	 */
    private List getLatestVersions(List guides)
    {
        List result = new ArrayList();
        Integer numberAux = null;

        Collections.reverse(guides);

        Iterator iterator = guides.iterator();
        while (iterator.hasNext())
        {
            IGuide guide = (IGuide) iterator.next();

            if ((numberAux == null) || (numberAux.intValue() != guide.getNumber().intValue()))
            {
                numberAux = guide.getNumber();
                InfoGuide infoGuide = Cloner.copyIGuide2InfoGuide(guide);
                List infoReimbursementGuides = new ArrayList();
                if (guide.getReimbursementGuides() != null)
                {
                    Iterator iter = guide.getReimbursementGuides().iterator();
                    while (iter.hasNext())
                    {
                        IReimbursementGuide reimbursementGuide = (IReimbursementGuide) iter.next();
                        InfoReimbursementGuide infoReimbursementGuide =
                            Cloner.copyIReimbursementGuide2InfoReimbursementGuide(reimbursementGuide);
                        infoReimbursementGuides.add(infoReimbursementGuide);

                    }

                }
                infoGuide.setInfoReimbursementGuides(infoReimbursementGuides);
                result.add(infoGuide);
            }
        }
        Collections.reverse(result);
        return result;
    }

    public List run(
        String identificationDocumentNumber,
        TipoDocumentoIdentificacao identificationDocumentType)
        throws Exception
    {

        ISuportePersistente sp = null;
        List guides = null;
        IPessoa person = null;

        // Check if person exists

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            person =
                sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(
                    identificationDocumentNumber,
                    identificationDocumentType);

        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (person == null)
        {
            throw new NonExistingServiceException();
        }

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            guides =
                sp.getIPersistentGuide().readByPerson(
                    identificationDocumentNumber,
                    identificationDocumentType);

        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if ((guides == null) || (guides.size() == 0))
        {
            return null;
        }
        BeanComparator numberComparator = new BeanComparator("number");
        BeanComparator versionComparator = new BeanComparator("version");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(numberComparator);
        chainComparator.addComparator(versionComparator);
        Collections.sort(guides, chainComparator);

        return getLatestVersions(guides);
    }

}
