/*
 * Created on 27/Out/2003
 *
 */

package ServidorAplicacao.Servicos.publication;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoPerson;
import DataBeans.publication.InfoAuthor;
import DataBeans.publication.InfoPublication;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.publication.EditarPublication;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Carlos Pereira & Francisco Passos
 *  
 */

public class EditPublicationTest extends ServiceTestCase {

    /**
     * @param testName
     */
    public EditPublicationTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "EditarPublication";
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }


    protected String getDataSetFilePath() {
        return "etc/testDataSetForPublications.xml";
    }

    /** ********** Inicio dos testes ao servi�o************* */

    /*
     * Successfully inserts a publication
     */
    public void testEditPublicationOK() {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            
            List infoAuthorsList = new ArrayList();
            InfoAuthor infoAuthor1 = new InfoAuthor();
            infoAuthor1.setIdInternal(new Integer(2));
            infoAuthor1.setAuthor("Yombero");
            
            //Another Author
            InfoAuthor infoAuthor2 = new InfoAuthor();
            infoAuthor2.setIdInternal(new Integer(4));
            infoAuthor2.setAuthor("NewAuthor");
            
            //Another Author
            InfoAuthor infoAuthor3 = new InfoAuthor();
            infoAuthor3.setIdInternal(new Integer(5));
            infoAuthor3.setKeyPerson(new Integer(2));
            
            InfoPerson infoPerson = new InfoPerson ();
            infoPerson.setIdInternal(new Integer(2));
            infoPerson.setUsername("mau");
            infoPerson.setNumeroDocumentoIdentificacao("222222222");
            infoPerson.setPassword("pass");
            
            infoAuthor3.setInfoPessoa(infoPerson);
            
            infoAuthorsList.add(infoAuthor1);
            infoAuthorsList.add(infoAuthor2);
            infoAuthorsList.add(infoAuthor3);
            
            InfoPublication infoPublication = new InfoPublication();
            infoPublication.setIdInternal(new Integer(5));
            infoPublication.setTitle("Tralala");
            infoPublication.setVolume("2");
            infoPublication.setYear("2010");
            infoPublication.setKeyPublicationType(new Integer(3));
            infoPublication.setInfoPublicationAuthors(infoAuthorsList);
            
            sp.iniciarTransaccao();
            EditarPublication ep = new EditarPublication();
            ep.run(infoPublication);
            sp.confirmarTransaccao();
            
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets_templates/servicos/publication/testEditPublicationOKExpectedDataSet.xml");

            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULLY run by test: "
                    + "testEditPublicationOK");
        } catch (Exception e) {
            fail("Editing Publication " + e);
        }
    }

    
    /*
     * Tries to edit a Publication that does not exist
     */
    public void testEditPublicationNonExistent() {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            
            List infoAuthorsList = new ArrayList();
            InfoAuthor infoAuthor1 = new InfoAuthor();
            infoAuthor1.setIdInternal(new Integer(2));
            infoAuthor1.setAuthor("Yombero");
            
            //Another Author
            InfoAuthor infoAuthor2 = new InfoAuthor();
            infoAuthor2.setIdInternal(new Integer(4));
            infoAuthor2.setAuthor("NewAuthor");
            
            //Another Author
            InfoAuthor infoAuthor3 = new InfoAuthor();
            infoAuthor3.setIdInternal(new Integer(5));
            infoAuthor3.setKeyPerson(new Integer(2));
            
            InfoPerson infoPerson = new InfoPerson ();
            infoPerson.setIdInternal(new Integer(2));
            infoPerson.setUsername("mau");
            infoPerson.setNumeroDocumentoIdentificacao("222222222");
            infoPerson.setPassword("pass");
            
            infoAuthor3.setInfoPessoa(infoPerson);
            
            infoAuthorsList.add(infoAuthor1);
            infoAuthorsList.add(infoAuthor2);
            infoAuthorsList.add(infoAuthor3);
            
            InfoPublication infoPublication = new InfoPublication();
            infoPublication.setIdInternal(new Integer(7));
            infoPublication.setTitle("Publication7");
            infoPublication.setVolume("3");
            infoPublication.setYear("9999");
            infoPublication.setKeyPublicationType(new Integer(2));
            infoPublication.setInfoPublicationAuthors(infoAuthorsList);
            
            sp.iniciarTransaccao();
            EditarPublication ep = new EditarPublication();
            ep.run(infoPublication);
            sp.confirmarTransaccao();
            
            fail("Editing Publication shouldn't have reached this point, since it edited"+
                    " a non existent publication");

            
        } catch (ExcepcaoInexistente e) {
            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULLY run by test: "
                    + "testEditPublicationNonExistent");
        } catch (ExcepcaoPersistencia e) {
            fail("Editing Publication :"+ e);
        } catch (FenixServiceException e) {
            fail("Editing Publication :"+ e);
        }
        
    }    
}