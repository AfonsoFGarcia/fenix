/*
 * Created on 17/Mar/2003 by jpvl
 *
 */
package ServidorAplicacao.Servicos.person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoPerson;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.InvalidPasswordServiceException;
import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author jpvl
 */
public class ChangePersonalInfoTest extends TestCaseNeedAuthorizationServices {

	/**
	 * @param testName
	 */
	public ChangePersonalInfoTest(String testName) {
		super(testName);
	}
	
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(ChangePersonalInfoTest.class);
		return suite;
	}
	
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ChangePersonalInfo";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#needsAuthorization()
	 */
	protected boolean needsAuthorization() {
		return true;
	}

	// FIXME: At this time we only can change the Email, Mobile Phone and the Web Site

	public void testSuccessfullChangePersonalInfo() {
		UserView userView = getUserViewToBeTested("user");
		
		Calendar calendar = Calendar.getInstance();
				
		InfoPerson infoPerson = new InfoPerson();
//		infoPerson.setCodigoPostal("123");
//		infoPerson.setConcelhoMorada("123");
//		infoPerson.setConcelhoNaturalidade("123");
//		infoPerson.setDistritoMorada("123");
//		infoPerson.setDistritoNaturalidade("123");
		infoPerson.setEmail("123");
		infoPerson.setEnderecoWeb("123");
//		infoPerson.setEstadoCivil(new EstadoCivil(EstadoCivil.SOLTEIRO));
//		infoPerson.setFreguesiaMorada("123");
//		infoPerson.setFreguesiaNaturalidade("123");
//		infoPerson.setLocalEmissaoDocumentoIdentificacao("123");
//		infoPerson.setLocalidade("123");
//		infoPerson.setLocalidadeCodigoPostal("123");
//		infoPerson.setMorada("123");
//		infoPerson.setNacionalidade("123");
//		infoPerson.setNome("123");
//		infoPerson.setNomeMae("123");
//		infoPerson.setNomePai("123");
//		infoPerson.setNumContribuinte("123");
//		infoPerson.setNumeroDocumentoIdentificacao("123");
//		infoPerson.setProfissao("123");
//		infoPerson.setSexo(new Sexo(Sexo.FEMININO));
//		infoPerson.setTelefone("123");
		infoPerson.setTelemovel("123");
//		infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO));
//		infoPerson.setUsername("nuno");
		
//		infoPerson.setDataEmissaoDocumentoIdentificacao(calendar.getTime());
//		infoPerson.setDataValidadeDocumentoIdentificacao(calendar.getTime());
//		infoPerson.setNascimento(calendar.getTime());
//		
//		InfoCountry infoCountry = new InfoCountry();
//		infoCountry.setNationality("Inglesa");
//		infoPerson.setInfoPais(infoCountry);
		
		Object args[] = { infoPerson, userView };

		UserView userViewResult = null; 
		try {
			userViewResult = (UserView) ServiceUtils.executeService(userView, getNameOfServiceToBeTested(), args);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Calling service "+ getNameOfServiceToBeTested()+"!");
		}
		assertNotNull(userViewResult);
		assertEquals("user", userViewResult.getUtilizador());
		
		InfoPerson newInfoPerson = this.readPerson("user");
		
//		assertEquals(infoPerson.getCodigoPostal(), newInfoPerson.getCodigoPostal());
//		assertEquals(infoPerson.getConcelhoMorada(), newInfoPerson.getConcelhoMorada());
//		assertEquals(infoPerson.getConcelhoNaturalidade(), newInfoPerson.getConcelhoNaturalidade());
//		assertEquals(infoPerson.getDataEmissaoDocumentoIdentificacao(), newInfoPerson.getDataEmissaoDocumentoIdentificacao());
//		assertEquals(infoPerson.getDataValidadeDocumentoIdentificacao(), newInfoPerson.getDataValidadeDocumentoIdentificacao());
//		assertEquals(infoPerson.getDistritoMorada(), newInfoPerson.getDistritoMorada());
//		assertEquals(infoPerson.getDistritoNaturalidade(), newInfoPerson.getDistritoNaturalidade());
		assertEquals(infoPerson.getEmail(), newInfoPerson.getEmail());
		assertEquals(infoPerson.getEnderecoWeb(), newInfoPerson.getEnderecoWeb());
//		assertEquals(infoPerson.getEstadoCivil(), newInfoPerson.getEstadoCivil());
//		assertEquals(infoPerson.getFreguesiaMorada(), newInfoPerson.getFreguesiaMorada());
//		assertEquals(infoPerson.getFreguesiaNaturalidade(), newInfoPerson.getFreguesiaNaturalidade());
//		assertEquals(infoPerson.getLocalEmissaoDocumentoIdentificacao(), newInfoPerson.getLocalEmissaoDocumentoIdentificacao());
//		assertEquals(infoPerson.getLocalidade(), newInfoPerson.getLocalidade());
//		assertEquals(infoPerson.getLocalidadeCodigoPostal(), newInfoPerson.getLocalidadeCodigoPostal());
//		assertEquals(infoPerson.getMorada(), newInfoPerson.getMorada());
//		assertEquals(infoPerson.getNacionalidade(), newInfoPerson.getNacionalidade());
//		assertEquals(infoPerson.getNome(), newInfoPerson.getNome());
//		assertEquals(infoPerson.getNomeMae(), newInfoPerson.getNomeMae());
//		assertEquals(infoPerson.getNomePai(), newInfoPerson.getNomePai());
//		assertEquals(infoPerson.getNumContribuinte(), newInfoPerson.getNumContribuinte());
//		assertEquals(infoPerson.getNumeroDocumentoIdentificacao(), newInfoPerson.getNumeroDocumentoIdentificacao());
//		assertEquals(infoPerson.getNascimento(), newInfoPerson.getNascimento());
//		assertEquals(infoPerson.getProfissao(), newInfoPerson.getProfissao());
//		assertEquals(infoPerson.getTelefone(), newInfoPerson.getTelefone());
		assertEquals(infoPerson.getTelemovel(), newInfoPerson.getTelemovel());
//		assertEquals(infoPerson.getTipoDocumentoIdentificacao(), newInfoPerson.getTipoDocumentoIdentificacao());
//		assertEquals(infoPerson.getSexo(), newInfoPerson.getSexo());
//		assertEquals(infoPerson.getInfoPais().getNationality(), newInfoPerson.getInfoPais().getNationality());
		
	}
	
	public void testUnsucessfullChangePersonalInfo(){
		UserView userView = getUserViewToBeTested("nonexisting");
		
		Object args[] = { userView };
		InfoPerson infoPerson = null;
		try {
			infoPerson = (InfoPerson) ServiceUtils.executeService(userView, getNameOfServiceToBeTested(), args);
			fail ("Must throw "+ InvalidPasswordServiceException.class.getName()+" exception.");
		} catch(FenixServiceException e) {
			// All is OK
		}catch (Exception e) {
			e.printStackTrace();
			fail("Calling service "+ getNameOfServiceToBeTested()+"!");
		}
	}
	
	private UserView getUserViewToBeTested(String username) {
		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.PERSON);
		roles.add(infoRole);
		UserView userView = new UserView(username, roles);
		return userView;
	}
	
	private InfoPerson readPerson(String username) {
		IPessoa person = null;
		ISuportePersistente sp = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			IPessoaPersistente personDAO = sp.getIPessoaPersistente();
			sp.iniciarTransaccao();
			person = personDAO.lerPessoaPorUsername(username);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			try {
				sp.cancelarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				//ignored
			}
			e.printStackTrace();
			fail("Error reading person to test equal password!");
		}
		
		return Cloner.copyIPerson2InfoPerson(person);
	}
	
}
