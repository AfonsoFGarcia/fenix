/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoTeacher;
import DataBeans.teacher.InfoCareer;
import DataBeans.teacher.InfoCategory;
import DataBeans.teacher.InfoProfessionalCareer;
import DataBeans.teacher.InfoTeachingCareer;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditCareerTest extends ServiceNeedsAuthenticationTestCase
{

    public EditCareerTest(String testName)
    {
        super(testName);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
    protected String getNameOfServiceToBeTested()
    {
        return "EditCareer";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
	 */
    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testEditCareerDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testExpectedEditCareerDataSet.xml";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
	 */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
	 */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "manager", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
	 */
    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "jccm", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
    protected Object[] getAuthorizeArguments()
    {
        InfoCareer infoCareer = new InfoProfessionalCareer();
        infoCareer.setIdInternal(new Integer(1));
        infoCareer.setBeginYear(new Integer(1999));
        infoCareer.setEndYear(new Integer(2000));
        ((InfoProfessionalCareer) infoCareer).setEntity("empresa");
        ((InfoProfessionalCareer) infoCareer).setFunction("director");

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(1));

        infoCareer.setInfoTeacher(infoTeacher);

        Object[] args = { new Integer(1), infoCareer };
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testCreateNewProfessionalCareer()
    {
        try
        {
            InfoCareer infoCareer = new InfoProfessionalCareer();
            infoCareer.setBeginYear(new Integer(1995));
            infoCareer.setEndYear(new Integer(1999));
            ((InfoProfessionalCareer) infoCareer).setEntity("empresa");
            ((InfoProfessionalCareer) infoCareer).setFunction("presidente");

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoCareer.setInfoTeacher(infoTeacher);
            Object[] args = { null, infoCareer };

            gestor.executar(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedCreateProfessionalCareerDataSet.xml");
        } catch (FenixServiceException ex)
        {
            fail("Creating a new professional career " + ex);
        } catch (Exception ex)
        {
            fail("Creating a new professional career " + ex);
        }
    }

    public void testCreateNewTeachingCareer()
    {
        try
        {
            InfoCategory infoCategory = new InfoCategory();
            infoCategory.setIdInternal(new Integer(1));

            InfoCareer infoCareer = new InfoTeachingCareer();
            infoCareer.setBeginYear(new Integer(1999));
            infoCareer.setEndYear(new Integer(2000));
            ((InfoTeachingCareer) infoCareer).setCourseOrPosition("PO");
            ((InfoTeachingCareer) infoCareer).setInfoCategory(infoCategory);

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoCareer.setInfoTeacher(infoTeacher);
            Object[] args = { null, infoCareer };

            gestor.executar(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedCreateTeachingCareerDataSet.xml");
        } catch (Exception ex)
        {
            fail("Creating a new teaching career " + ex);
        }
    }

    public void testEditExistingProfessionalCareer()
    {
        try
        {
            InfoCareer infoCareer = new InfoProfessionalCareer();
            infoCareer.setBeginYear(new Integer(1999));
            infoCareer.setEndYear(new Integer(2000));
            ((InfoProfessionalCareer) infoCareer).setEntity("empresa");
            ((InfoProfessionalCareer) infoCareer).setFunction("presidente");
            infoCareer.setIdInternal(new Integer(1));

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoCareer.setInfoTeacher(infoTeacher);
            Object[] args = { new Integer(1), infoCareer };

            gestor.executar(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedEditProfessionalCareerDataSet.xml");
        } catch (Exception ex)
        {
            fail("Editing a professional career " + ex);
        }
    }

    public void testEditExistingTeachingCareer()
    {
        try
        {
            InfoCategory infoCategory = new InfoCategory();
            infoCategory.setIdInternal(new Integer(1));
            
            InfoCareer infoCareer = new InfoTeachingCareer();
            infoCareer.setBeginYear(new Integer(1999));
            infoCareer.setEndYear(new Integer(2000));
            ((InfoTeachingCareer) infoCareer).setCourseOrPosition("ASA");
            ((InfoTeachingCareer) infoCareer).setInfoCategory(infoCategory);
            infoCareer.setIdInternal(new Integer(2));

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoCareer.setInfoTeacher(infoTeacher);
            Object[] args = { new Integer(2), infoCareer };

            gestor.executar(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedEditTeachingCareerDataSet.xml");
        } catch (Exception ex)
        {
            fail("Editing a teaching career " + ex);
        }
    }
}
