package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class PersonTest extends DomainTestBase {

	InfoPerson infoPerson1;

	InfoPerson infoPerson2;

	InfoPerson infoPerson3;

	IRole personRole;

	IRole teacherRole;

	IRole coordinatorRole;

	IRole grantOwnerRole;

	IRole studentRole;

	IRole employeeRole;

	IStudent degreeStudent;

	IStudent masterDegreeStudent;

	IDDocumentType documentType;

	ICountry country;

	MaritalStatus maritalStatus = MaritalStatus.WIDOWER;

	Boolean availableEmail = Boolean.TRUE;

	Boolean availablePhoto = Boolean.FALSE;

	Boolean availableWebSite = Boolean.TRUE;

	String username = "Z12345";

	String codigoFiscal = "codigoFiscal";

	String codigoPostal = "codigoPostal";

	String concelhoMorada = "concelhoMorada";

	String concelhoNaturalidade = "concelhoNaturalidade";

	String distritoMorada = "distritoMorada";

	String email = "email@host.com";

	String enderecoWeb = "http://enderecoWeb.com";

	String freguesiaMorada = "freguesiaMorada";

	String freguesiaNaturalidade = "freguesiaNaturalidade";

	String localEmissaoDocumentoIdentificacao = "localEmissao";

	String localidade = "localidade";

	String localidadeCodigoPostal = "localidadeCodigoPostal";

	String address = "endereco";

	String nacionalidade = "nacionalidade";

	String nome = "nome";

	String nomeMae = "nomeMae";

	String nomePai = "nomePai";

	String numContribuinte = "numContribuinte";

	String numDocumentoIdentificacao = "numDocID";

	String profissao = "profissao";

	String telefone = "telefone";

	String telemovel = "telemovel";

	String workPhone = "workPhone";

	Date dataEmissao = new Date(System.currentTimeMillis() - 7000000);

	Date dataValidade = new Date(System.currentTimeMillis() + 7000000);

	Date dataNascimento = new Date(System.currentTimeMillis() - 20000000);

	Gender sexo = Gender.MALE;

	protected void setUp() throws Exception {
		super.setUp();

		country = new Country("Portugal", "Portuguese", "PT");

		documentType = IDDocumentType.AIR_FORCE_IDENTITY_CARD;

		personRole = new Role();
		personRole.setRoleType(RoleType.PERSON);
		teacherRole = new Role();
		teacherRole.setRoleType(RoleType.TEACHER);
		coordinatorRole = new Role();
		coordinatorRole.setRoleType(RoleType.COORDINATOR);
		grantOwnerRole = new Role();
		grantOwnerRole.setRoleType(RoleType.GRANT_OWNER);
		studentRole = new Role();
		studentRole.setRoleType(RoleType.STUDENT);
		employeeRole = new Role();
		employeeRole.setRoleType(RoleType.EMPLOYEE);

		degreeStudent = new Student();
		degreeStudent.setDegreeType(DegreeType.DEGREE);
		masterDegreeStudent = new Student();
		masterDegreeStudent.setDegreeType(DegreeType.MASTER_DEGREE);

		infoPerson1 = new InfoPerson();
		infoPerson1.setAvailableEmail(availableEmail);
		infoPerson1.setAvailablePhoto(availablePhoto);
		infoPerson1.setAvailableWebSite(availableWebSite);
		infoPerson1.setCodigoFiscal(codigoFiscal);
		infoPerson1.setCodigoPostal(codigoPostal);
		infoPerson1.setConcelhoMorada(concelhoMorada);
		infoPerson1.setConcelhoNaturalidade(concelhoNaturalidade);
		infoPerson1.setDataEmissaoDocumentoIdentificacao(dataEmissao);
		infoPerson1.setDataValidadeDocumentoIdentificacao(dataValidade);
		infoPerson1.setDistritoMorada(distritoMorada);
		infoPerson1.setEmail(email);
		infoPerson1.setEnderecoWeb(enderecoWeb);
		infoPerson1.setFreguesiaMorada(freguesiaMorada);
		infoPerson1.setFreguesiaNaturalidade(freguesiaNaturalidade);
		infoPerson1
				.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
		infoPerson1.setLocalidade(localidade);
		infoPerson1.setLocalidadeCodigoPostal(localidadeCodigoPostal);
		infoPerson1.setMaritalStatus(maritalStatus);
		infoPerson1.setMorada(address);
		infoPerson1.setNacionalidade(nacionalidade);
		infoPerson1.setNascimento(dataNascimento);
		infoPerson1.setNome(nome);
		infoPerson1.setNomeMae(nomeMae);
		infoPerson1.setNomePai(nomePai);
		infoPerson1.setNumContribuinte(numContribuinte);
		infoPerson1.setNumeroDocumentoIdentificacao(numDocumentoIdentificacao);
		infoPerson1.setProfissao(profissao);
		infoPerson1.setSexo(sexo);
		infoPerson1.setTelefone(telefone);
		infoPerson1.setTelemovel(telemovel);
		infoPerson1.setTipoDocumentoIdentificacao(documentType);
		infoPerson1.setWorkPhone(workPhone);

		infoPerson2 = new InfoPerson();
		infoPerson2.setAvailableEmail(!availableEmail);
		infoPerson2.setAvailablePhoto(!availablePhoto);
		infoPerson2.setAvailableWebSite(!availableWebSite);
		infoPerson2.setCodigoFiscal(codigoFiscal + 1);
		infoPerson2.setCodigoPostal(codigoPostal + 1);
		infoPerson2.setConcelhoMorada(concelhoMorada + 1);
		infoPerson2.setConcelhoNaturalidade(concelhoNaturalidade + 1);
		infoPerson2.setDataEmissaoDocumentoIdentificacao(new Date(1));
		infoPerson2.setDataValidadeDocumentoIdentificacao(new Date(2));
		infoPerson2.setDistritoMorada(distritoMorada + 1);
		infoPerson2.setEmail(email + 1);
		infoPerson2.setEnderecoWeb(enderecoWeb + 1);
		infoPerson2.setFreguesiaMorada(freguesiaMorada + 1);
		infoPerson2.setFreguesiaNaturalidade(freguesiaNaturalidade + 1);
		infoPerson2
				.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao + 1);
		infoPerson2.setLocalidade(localidade + 1);
		infoPerson2.setLocalidadeCodigoPostal(localidadeCodigoPostal + 1);
		infoPerson2.setMaritalStatus(MaritalStatus.CIVIL_UNION);
		infoPerson2.setMorada(address + 1);
		infoPerson2.setNacionalidade(nacionalidade + 1);
		infoPerson2.setNascimento(new Date(3));
		infoPerson2.setNome(nome + 1);
		infoPerson2.setNomeMae(nomeMae + 1);
		infoPerson2.setNomePai(nomePai + 1);
		infoPerson2.setNumContribuinte(numContribuinte + 1);
		infoPerson2
				.setNumeroDocumentoIdentificacao(numDocumentoIdentificacao + 1);
		infoPerson2.setProfissao(profissao + 1);
		infoPerson2.setSexo(Gender.FEMALE);
		infoPerson2.setTelefone(telefone + 1);
		infoPerson2.setTelemovel(telemovel + 1);
		infoPerson2.setTipoDocumentoIdentificacao(documentType);
		infoPerson2.setWorkPhone(workPhone + 1);

		infoPerson3 = new InfoPerson();
		infoPerson3.setAvailableEmail(!availableEmail);
		infoPerson3.setAvailablePhoto(!availablePhoto);
		infoPerson3.setAvailableWebSite(!availableWebSite);
		infoPerson3.setCodigoFiscal(codigoFiscal);
		infoPerson3.setCodigoPostal(codigoPostal);
		infoPerson3.setConcelhoMorada(concelhoMorada);
		infoPerson3.setConcelhoNaturalidade(concelhoNaturalidade);
		infoPerson3.setDataEmissaoDocumentoIdentificacao(dataEmissao);
		infoPerson3.setDataValidadeDocumentoIdentificacao(dataValidade);
		infoPerson3.setDistritoMorada(distritoMorada);
		infoPerson3.setEmail(email + 1);
		infoPerson3.setEnderecoWeb(enderecoWeb + 1);
		infoPerson3.setFreguesiaMorada(freguesiaMorada);
		infoPerson3.setFreguesiaNaturalidade(freguesiaNaturalidade);
		infoPerson3
				.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
		infoPerson3.setLocalidade(localidade);
		infoPerson3.setLocalidadeCodigoPostal(localidadeCodigoPostal);
		infoPerson3.setMaritalStatus(maritalStatus);
		infoPerson3.setMorada(address);
		infoPerson3.setNacionalidade(nacionalidade);
		infoPerson3.setNascimento(dataNascimento);
		infoPerson3.setNome(nome);
		infoPerson3.setNomeMae(nomeMae);
		infoPerson3.setNomePai(nomePai);
		infoPerson3.setNumContribuinte(numContribuinte);
		infoPerson3.setNumeroDocumentoIdentificacao(numDocumentoIdentificacao);
		infoPerson3.setProfissao(profissao);
		infoPerson3.setSexo(sexo);
		infoPerson3.setTelefone(telefone);
		infoPerson3.setTelemovel(telemovel + 1);
		infoPerson3.setTipoDocumentoIdentificacao(documentType);
		infoPerson3.setWorkPhone(workPhone + 1);

	}

	public void testCreatePerson1() {
		IPerson person = new Person(infoPerson1, country);

		assertPersonContent(person, country);
	}

	public void testCreatePerson2() {
		IPerson person = new Person(nome, numDocumentoIdentificacao,
				documentType, sexo);

		assertEquals(person.getNome(), nome);
		assertEquals(person.getNumeroDocumentoIdentificacao(),
				numDocumentoIdentificacao);
		assertEquals(person.getIdDocumentType(), documentType);
		assertEquals(person.getGender(), sexo);
	}

	public void testCreatePerson3() {
		IPerson person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);

		assertEquals(person.getUsername(), username);
		assertEquals(person.getNome(), nome);
		assertEquals(person.getGender(), Gender.MALE);
		assertEquals(person.getMorada(), address);
		assertEquals(person.getTelefone(), telefone);
		assertEquals(person.getTelemovel(), telemovel);
		assertEquals(person.getEnderecoWeb(), enderecoWeb);
		assertEquals(person.getEmail(), email);
		assertEquals(person.getNumeroDocumentoIdentificacao(),
				numDocumentoIdentificacao);
		assertEquals(person.getIdDocumentType(), IDDocumentType.EXTERNAL);
		assertEquals(person.getAvailableEmail(), Boolean.FALSE);
		assertEquals(person.getAvailablePhoto(), Boolean.FALSE);
		assertEquals(person.getAvailableWebSite(), Boolean.FALSE);
	}

	public void testEditPerson() {
		IPerson person = new Person(infoPerson2, country);

		person.edit(infoPerson1, country);
		assertPersonContent(person, country);
	}

	public void testEditPersonalContactInformation() {
		IPerson person = new Person(infoPerson3, country);

		person.editPersonalContactInformation(infoPerson1);
		assertPersonContent(person, country);
	}

	public void testEdit() {
		IPerson person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		person.edit("new" + nome, "new" + address, "new" + telefone, "new"
				+ telemovel, "new" + enderecoWeb, "new" + email);

		assertEquals(person.getNome(), "new" + nome);
		assertEquals(person.getMorada(), "new" + address);
		assertEquals(person.getTelefone(), "new" + telefone);
		assertEquals(person.getEnderecoWeb(), "new" + enderecoWeb);
		assertEquals(person.getEmail(), "new" + email);

	}

	public void testChangeUsername() {
		IPerson person = new Person(infoPerson1, country);
		person.setUsername("user1");
		IPerson person2 = new Person(infoPerson2, country);
		person2.setUsername("user2");
		IPerson person3 = new Person(infoPerson3, country);
		person3.setUsername("user3");

		List<IPerson> persons = new ArrayList<IPerson>();
		persons.add(person);
		persons.add(person2);
		persons.add(person3);

		person.changeUsername("newUsername", persons);
		assertEquals(person.getUsername(), "newUsername");

		try {
			person.changeUsername("user2", persons);
			fail("Username already exists - should have thrown a DomainException");
		} catch (DomainException domainException) {
			// caught expected exception
		}
		assertEquals(person.getUsername(), "newUsername");

		try {
			person.changeUsername("", persons);
			fail("Empty username - should have thrown a DomainException");
		} catch (DomainException domainException) {
			// caught expected exception
		}
		assertEquals(person.getUsername(), "newUsername");

		try {
			person.changeUsername(null, persons);
			fail("Null username - should have thrown a DomainException");
		} catch (DomainException domainException) {
			// caught expected exception
		}
		assertEquals(person.getUsername(), "newUsername");

	}

	public void testChangePassword() {

		String someOldPassword = "someOldPassword";
		String newPassword = "newPassword";

		IPerson person = new Person(infoPerson1, country);
		person.setPassword(PasswordEncryptor.encryptPassword(someOldPassword));

		try {
			person.changePassword("fakePassword", someOldPassword);
			fail("Invalid old password!");
		} catch (DomainException domainException) {
			// this is good, the old password must be correct
		}

		try {
			person.changePassword(someOldPassword, someOldPassword);
			fail("New password must differ from old password");
		} catch (DomainException domainException) {
			// this is good, the new password cannot be the same
		}

		try {
			person.changePassword(someOldPassword, null);
			fail("New password must differ from null");
		} catch (DomainException domainException) {
			// this is good, it should not allow null passwords
		}

		try {
			person.changePassword(someOldPassword, "");
			fail("New password must not be empty");
		} catch (DomainException domainException) {
			// nor empty passwords
		}

		try {
			person.changePassword(someOldPassword, new String(
					numDocumentoIdentificacao));
			fail("New password must differ from the identification document number");
		} catch (DomainException domainException) {
			// nor the identification number
		}

		try {
			person.changePassword(someOldPassword, new String(codigoFiscal));
			fail("New password must differ from the fiscal code number");
		} catch (DomainException domainException) {
			// nor the fiscal code
		}

		try {
			person.changePassword(someOldPassword, new String(numContribuinte));
			fail("New password must differ from the taxpayer number");
		} catch (DomainException domainException) {
			// nor the taxpayer number
		}

		try {
			person.changePassword(someOldPassword, newPassword);
		} catch (DomainException domainException) {
			fail("Should have changed the password, it is a valid one");
		}

		assertTrue(PasswordEncryptor.areEquals(person.getPassword(),
				newPassword));
	}

	public void testAddRole1() {
		IPerson person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		assertEquals(person.getPersonRolesCount(), 0);
		person.getPersonRoles().add(personRole);
		assertEquals(person.getPersonRolesCount(), 1);
		assertTrue(person.getPersonRoles().contains(personRole));
		person.getPersonRoles().add(teacherRole);
		assertEquals(person.getPersonRolesCount(), 2);
		assertTrue(person.getPersonRoles().contains(teacherRole));
		person.getPersonRoles().add(coordinatorRole);
		assertEquals(person.getPersonRolesCount(), 3);
		assertTrue(person.getPersonRoles().contains(coordinatorRole));
	}

	public void testAddRole2() {
		IPerson person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		assertEquals(person.getPersonRolesCount(), 0);
		try {
			person.getPersonRoles().add(teacherRole);
			fail("The test shouldn't have successfully added the TeacherRole - Dependencies not working correctly");
		} catch (DomainException domainException) {
			assertEquals(person.getUsername(), username);
			// everything went has planned
		}
		person.getPersonRoles().add(personRole);
		try {
			person.getPersonRoles().add(teacherRole);
			// everything went has planned
		} catch (DomainException domainException) {
			fail("The test should have successfully added the TeacherRole - Dependencies not working correctly");
		}
	}

	public void testAddRole3() {
		IPerson person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		assertEquals(person.getPersonRolesCount(), 0);
		person.getPersonRoles().add(personRole);
		try {
			person.getPersonRoles().add(personRole);
			fail("The test shouldn't have successfully added the PersonRole - the person already had that role");
		} catch (DomainException domainException) {
			// everything went has planned
		}
	}

	public void testRemoveRole1() {
		IPerson person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		assertEquals(person.getPersonRolesCount(), 0);
		person.getPersonRoles().add(personRole);
		assertEquals(person.getPersonRolesCount(), 1);
		person.getPersonRoles().remove(personRole);
		assertEquals(person.getPersonRolesCount(), 0);
	}

	public void testRemoveRole2() {
		IPerson person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		person.getPersonRoles().add(personRole);
		person.getPersonRoles().add(teacherRole);
		person.getPersonRoles().add(coordinatorRole);
		assertEquals(person.getPersonRolesCount(), 3);

		person.getPersonRoles().remove(teacherRole);
		assertEquals(person.getPersonRolesCount(), 1);
		assertFalse(person.getPersonRoles().contains(teacherRole));
		assertFalse(person.getPersonRoles().contains(coordinatorRole));
	}

	public void testUsernameUpdate1() {
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		person.getStudents().add(degreeStudent);

		assertEquals(person.getUsername(), username);
		person.getPersonRoles().add(personRole);
		assertEquals(person.getUsername(), "T12345");
		person.getPersonRoles().add(grantOwnerRole);
		assertEquals(person.getUsername(), "B12345");
		person.getPersonRoles().add(studentRole);
		assertEquals(person.getUsername(), "L12345");
		person.getPersonRoles().add(employeeRole);
		assertEquals(person.getUsername(), "F12345");
		person.getPersonRoles().add(teacherRole);
		assertEquals(person.getUsername(), "D12345");
	}

	public void testUsernameUpdate2() {
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		person.getStudents().add(degreeStudent);
		person.getStudents().add(masterDegreeStudent);

		assertEquals(person.getUsername(), username);
		person.getPersonRoles().add(personRole);
		assertEquals(person.getUsername(), "T12345");
		person.getPersonRoles().add(grantOwnerRole);
		assertEquals(person.getUsername(), "B12345");
		person.getPersonRoles().add(studentRole);
		assertEquals(person.getUsername(), "M12345");
		person.getPersonRoles().add(employeeRole);
		assertEquals(person.getUsername(), "F12345");
		person.getPersonRoles().add(teacherRole);
		assertEquals(person.getUsername(), "D12345");
	}

	public void testUsernameUpdate3() {
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		person.getStudents().add(degreeStudent);

		person.getPersonRoles().add(personRole);
		person.getPersonRoles().add(grantOwnerRole);
		person.getPersonRoles().add(studentRole);
		person.getPersonRoles().add(employeeRole);
		person.getPersonRoles().add(teacherRole);

		person.getPersonRoles().remove(teacherRole);
		assertEquals(person.getUsername(), "F12345");
		person.getPersonRoles().remove(employeeRole);
		assertEquals(person.getUsername(), "L12345");
		person.getPersonRoles().remove(studentRole);
		assertEquals(person.getUsername(), "B12345");
		person.getPersonRoles().remove(grantOwnerRole);
		assertEquals(person.getUsername(), "T12345");
	}

	public void testUsernameUpdate4() {
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);

		assertEquals(person.getUsername(), username);
		person.getPersonRoles().add(personRole);
		person.getPersonRoles().add(grantOwnerRole);
		person.getPersonRoles().add(studentRole);
		person.getPersonRoles().remove(grantOwnerRole);
		assertEquals(person.getUsername(), "L12345");
	}

	private void assertPersonContent(IPerson person, ICountry country) {

		assertEquals(person.getPais(), country);
		assertEquals(person.getAvailableEmail(), availableEmail);
		assertEquals(person.getAvailablePhoto(), availablePhoto);
		assertEquals(person.getAvailableWebSite(), availableWebSite);
		assertEquals(person.getCodigoFiscal(), codigoFiscal);
		assertEquals(person.getCodigoPostal(), codigoPostal);
		assertEquals(person.getConcelhoMorada(), concelhoMorada);
		assertEquals(person.getConcelhoNaturalidade(), concelhoNaturalidade);
		assertEquals(person.getDataEmissaoDocumentoIdentificacao(), dataEmissao);
		assertEquals(person.getDataValidadeDocumentoIdentificacao(),
				dataValidade);
		assertEquals(person.getDistritoMorada(), distritoMorada);
		assertEquals(person.getEmail(), email);
		assertEquals(person.getEnderecoWeb(), enderecoWeb);
		assertEquals(person.getFreguesiaMorada(), freguesiaMorada);
		assertEquals(person.getFreguesiaNaturalidade(), freguesiaNaturalidade);
		assertEquals(person.getPais().getName(), "Portugal");
		assertEquals(person.getPais().getCode(), "PT");
		assertEquals(person.getPais().getNationality(), "Portuguese");
		assertEquals(person.getLocalEmissaoDocumentoIdentificacao(),
				localEmissaoDocumentoIdentificacao);
		assertEquals(person.getLocalidade(), localidade);
		assertEquals(person.getLocalidadeCodigoPostal(), localidadeCodigoPostal);
		assertEquals(person.getMaritalStatus(), maritalStatus);
		assertEquals(person.getMorada(), address);
		if (country == null) {
			assertEquals(person.getNacionalidade(), nacionalidade);
		} else {
			assertEquals(person.getNacionalidade(), person.getPais()
					.getNationality());
		}
		assertEquals(person.getNascimento(), dataNascimento);
		assertEquals(person.getNome(), nome);
		assertEquals(person.getNomeMae(), nomeMae);
		assertEquals(person.getNomePai(), nomePai);
		assertEquals(person.getNumContribuinte(), numContribuinte);
		assertEquals(person.getNumeroDocumentoIdentificacao(),
				numDocumentoIdentificacao);
		assertEquals(person.getProfissao(), profissao);
		assertEquals(person.getGender(), sexo);
		assertEquals(person.getTelefone(), telefone);
		assertEquals(person.getTelemovel(), telemovel);
		assertEquals(person.getIdDocumentType(), documentType);
		assertEquals(person.getWorkPhone(), workPhone);

	}

}
