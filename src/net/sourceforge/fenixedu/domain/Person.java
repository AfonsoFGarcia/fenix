package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.cms.IUserGroup;
import net.sourceforge.fenixedu.domain.cms.predicates.ContentAssignableClassPredicate;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.IFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.IPersonFunction;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.UsernameUtils;

import org.apache.commons.collections.iterators.FilterIterator;

public class Person extends Person_Base {

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    public Person(InfoPerson personToCreate, ICountry country) {

        if (personToCreate.getIdInternal() != null) {
            throw new DomainException("error.person.existentPerson");
        }

        setProperties(personToCreate);
        setUsername(personToCreate.getUsername());
        setPais(country);
        setIsPassInKerberos(Boolean.FALSE);
    }

    public Person(String name, String identificationDocumentNumber,
            IDDocumentType identificationDocumentType, Gender gender) {
        setNome(name);
        setNumeroDocumentoIdentificacao(identificationDocumentNumber);
        setIdDocumentType(identificationDocumentType);
        setGender(gender);
        setAvailableEmail(Boolean.FALSE);
        setAvailableWebSite(Boolean.FALSE);
        setAvailablePhoto(Boolean.FALSE);
        setMaritalStatus(MaritalStatus.SINGLE);
        setIsPassInKerberos(Boolean.FALSE);
    }

    public Person(String username, String name, Gender gender, String address, String phone,
            String mobile, String homepage, String email, String documentIDNumber,
            IDDocumentType documentType) {
        setUsername(username);
        setNome(name);
        setGender(gender);
        setMorada(address);
        setTelefone(phone);
        setTelemovel(mobile);
        setEnderecoWeb(homepage);
        setEmail(email);
        setNumeroDocumentoIdentificacao(documentIDNumber);
        setIdDocumentType(documentType);
        setAvailableEmail(Boolean.FALSE);
        setAvailableWebSite(Boolean.FALSE);
        setAvailablePhoto(Boolean.FALSE);
        setMaritalStatus(MaritalStatus.SINGLE);
        setIsPassInKerberos(Boolean.FALSE);
    }

    public void edit(InfoPerson personToEdit, ICountry country) {
        setProperties(personToEdit);
        if (country != null) {
            setPais(country);
        }
    }

    public void update(InfoPerson updatedPersonalData, ICountry country) {
        updateProperties(updatedPersonalData);
        setPais((ICountry) valueToUpdate(getPais(), country));
    }

    public void editPersonalContactInformation(InfoPerson personToEdit) {
        setTelemovel(personToEdit.getTelemovel());
        setWorkPhone(personToEdit.getWorkPhone());
        setEmail(personToEdit.getEmail());
        setAvailableEmail(personToEdit.getAvailableEmail());
        setEnderecoWeb(personToEdit.getEnderecoWeb());
        setAvailableWebSite(personToEdit.getAvailableWebSite());
        setAvailablePhoto(personToEdit.getAvailablePhoto());
    }

    public void edit(String name, String address, String phone, String mobile, String homepage,
            String email) {
        setNome(name);
        setMorada(address);
        setTelefone(phone);
        setTelemovel(mobile);
        setEnderecoWeb(homepage);
        setEmail(email);
    }

    public static boolean checkIfUsernameExists(String username, List<IPerson> persons) {
        for (IPerson person : persons) {
            if (username.equals(person.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public void changeUsername(String newUsername, List<IPerson> persons) {
        if (newUsername == null || newUsername.equals("")) {
            throw new DomainException("error.person.nullOrEmptyUsername");
        }

        if (checkIfUsernameExists(newUsername, persons)) {
            throw new DomainException("error.person.existingUsername");
        }

        setUsername(newUsername);
    }

    public void changePassword(String oldPassword, String newPassword) {

        if (newPassword == null) {
            throw new DomainException("error.person.invalidNullPassword");
        }

        if (!PasswordEncryptor.areEquals(getPassword(), oldPassword)) {
            throw new DomainException("error.person.invalidExistingPassword");
        }

        if (PasswordEncryptor.areEquals(getPassword(), newPassword)) {
            throw new DomainException("error.person.invalidSamePassword");
        }

        if (newPassword.equals("")) {
            throw new DomainException("error.person.invalidEmptyPassword");
        }

        if (getNumeroDocumentoIdentificacao().equalsIgnoreCase(newPassword)) {
            throw new DomainException("error.person.invalidIDPassword");
        }

        if (getCodigoFiscal() != null && getCodigoFiscal().equalsIgnoreCase(newPassword)) {
            throw new DomainException("error.person.invalidFiscalCodePassword");
        }

        if (getNumContribuinte() != null && getNumContribuinte().equalsIgnoreCase(newPassword)) {
            throw new DomainException("error.person.invalidTaxPayerPassword");
        }

        setPassword(PasswordEncryptor.encryptPassword(newPassword));
    }

    public void updateUsername() {
        this.setUsername(UsernameUtils.updateUsername(this));
    }

    public void updateIstUsername() {
        this.setIstUsername(UsernameUtils.updateIstUsername(this));
    }

    public IRole getPersonRole(RoleType roleType) {

        for (IRole role : this.getPersonRoles()) {
            if (role.getRoleType().equals(roleType)) {
                return role;
            }
        }
        return null;
    }

    public Boolean hasRole(final RoleType roleType) {
        for (final IRole role : this.getPersonRoles()) {
            if (role.getRoleType() == roleType) {
                return true;
            }
        }
        return false;
    }

    public IStudent getStudentByType(DegreeType degreeType) {
        for (IStudent student : this.getStudents()) {
            if (student.getDegreeType().equals(degreeType)){
                return student;
            }
        }
        return null;
    }

    public String getNacionalidade() {
        return this.getPais().getNationality();
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    private void setProperties(InfoPerson infoPerson) {

        setNome(infoPerson.getNome());

        if (infoPerson.getNumeroDocumentoIdentificacao() != null)
            setNumeroDocumentoIdentificacao(infoPerson.getNumeroDocumentoIdentificacao());
        if (infoPerson.getTipoDocumentoIdentificacao() != null)
            setIdDocumentType(infoPerson.getTipoDocumentoIdentificacao());

        setCodigoFiscal(infoPerson.getCodigoFiscal());
        setCodigoPostal(infoPerson.getCodigoPostal());
        setConcelhoMorada(infoPerson.getConcelhoMorada());
        setConcelhoNaturalidade(infoPerson.getConcelhoNaturalidade());
        setDataEmissaoDocumentoIdentificacao(infoPerson.getDataEmissaoDocumentoIdentificacao());
        setDataValidadeDocumentoIdentificacao(infoPerson.getDataValidadeDocumentoIdentificacao());
        setDistritoMorada(infoPerson.getDistritoMorada());
        setDistritoNaturalidade(infoPerson.getDistritoNaturalidade());
        setEmail(infoPerson.getEmail());
        setEnderecoWeb(infoPerson.getEnderecoWeb());
        setMaritalStatus((infoPerson.getMaritalStatus() == null) ? MaritalStatus.UNKNOWN : infoPerson.getMaritalStatus());
        setFreguesiaMorada(infoPerson.getFreguesiaMorada());
        setFreguesiaNaturalidade(infoPerson.getFreguesiaNaturalidade());
        setLocalEmissaoDocumentoIdentificacao(infoPerson.getLocalEmissaoDocumentoIdentificacao());
        setLocalidade(infoPerson.getLocalidade());
        setLocalidadeCodigoPostal(infoPerson.getLocalidadeCodigoPostal());
        setMorada(infoPerson.getMorada());
        setNascimento(infoPerson.getNascimento());
        setNomeMae(infoPerson.getNomeMae());
        setNomePai(infoPerson.getNomePai());
        setNumContribuinte(infoPerson.getNumContribuinte());

        setProfissao(infoPerson.getProfissao());
        setGender(infoPerson.getSexo());
        setTelefone(infoPerson.getTelefone());
        setTelemovel(infoPerson.getTelemovel());

        // Generate person's Password
        if (getPassword() == null)
            setPassword(PasswordEncryptor.encryptPassword(GeneratePassword.getInstance()
                    .generatePassword(this)));

        setAvailableEmail(infoPerson.getAvailableEmail());
        setAvailablePhoto(Boolean.TRUE);
        setAvailableWebSite(infoPerson.getAvailableWebSite());
        setWorkPhone(infoPerson.getWorkPhone());
    }

    private void updateProperties(InfoPerson infoPerson) {

        setNome(valueToUpdateIfNewNotNull(getNome(), infoPerson.getNome()));
        setNumeroDocumentoIdentificacao(valueToUpdateIfNewNotNull(getNumeroDocumentoIdentificacao(), infoPerson.getNumeroDocumentoIdentificacao()));
        setIdDocumentType((IDDocumentType) valueToUpdateIfNewNotNull(getIdDocumentType(), infoPerson
                .getTipoDocumentoIdentificacao()));
        setCodigoFiscal(valueToUpdateIfNewNotNull(getCodigoFiscal(), infoPerson.getCodigoFiscal()));
        setCodigoPostal(valueToUpdateIfNewNotNull(getCodigoPostal(), infoPerson.getCodigoPostal()));
        setConcelhoMorada(valueToUpdateIfNewNotNull(getConcelhoMorada(), infoPerson.getConcelhoMorada()));
        setConcelhoNaturalidade(valueToUpdateIfNewNotNull(getConcelhoNaturalidade(), infoPerson.getConcelhoNaturalidade()));
        setDataEmissaoDocumentoIdentificacao((Date) valueToUpdateIfNewNotNull(getDataEmissaoDocumentoIdentificacao(), infoPerson.getDataEmissaoDocumentoIdentificacao()));
        setDataValidadeDocumentoIdentificacao((Date) valueToUpdateIfNewNotNull(getDataValidadeDocumentoIdentificacao(), infoPerson.getDataValidadeDocumentoIdentificacao()));
        setDistritoMorada(valueToUpdateIfNewNotNull(getDistritoMorada(), infoPerson.getDistritoMorada()));
        setDistritoNaturalidade(valueToUpdateIfNewNotNull(getDistritoNaturalidade(), infoPerson.getDistritoNaturalidade()));
        setEmail(valueToUpdate(getEmail(), infoPerson.getEmail()));
        setEnderecoWeb(valueToUpdate(getEnderecoWeb(), infoPerson.getEnderecoWeb()));
        MaritalStatus maritalStatus = (MaritalStatus) valueToUpdateIfNewNotNull(getMaritalStatus(), infoPerson.getMaritalStatus());        
        setMaritalStatus((maritalStatus == null) ? MaritalStatus.UNKNOWN : maritalStatus);
        setFreguesiaMorada(valueToUpdateIfNewNotNull(getFreguesiaMorada(), infoPerson.getFreguesiaMorada()));
        setFreguesiaNaturalidade(valueToUpdateIfNewNotNull(getFreguesiaNaturalidade(), infoPerson.getFreguesiaNaturalidade()));
        setLocalEmissaoDocumentoIdentificacao(valueToUpdateIfNewNotNull(getLocalEmissaoDocumentoIdentificacao(), infoPerson.getLocalEmissaoDocumentoIdentificacao()));
        setLocalidade(valueToUpdateIfNewNotNull(getLocalidade(), infoPerson.getLocalidade()));
        setLocalidadeCodigoPostal(valueToUpdateIfNewNotNull(getLocalidadeCodigoPostal(), infoPerson.getLocalidadeCodigoPostal()));
        setMorada(valueToUpdateIfNewNotNull(getMorada(), infoPerson.getMorada()));
        setNascimento((Date) valueToUpdateIfNewNotNull(getNascimento(), infoPerson.getNascimento()));
        setNomeMae(valueToUpdateIfNewNotNull(getNomeMae(), infoPerson.getNomeMae()));
        setNomePai(valueToUpdateIfNewNotNull(getNomePai(), infoPerson.getNomePai()));
        setNumContribuinte(valueToUpdateIfNewNotNull(getNumContribuinte(), infoPerson.getNumContribuinte()));
        setProfissao(valueToUpdateIfNewNotNull(getProfissao(), infoPerson.getProfissao()));
        setGender((Gender) valueToUpdateIfNewNotNull(getGender(), infoPerson.getSexo()));
        setTelefone(valueToUpdate(getTelefone(), infoPerson.getTelefone()));
        setTelemovel(valueToUpdate(getTelemovel(), infoPerson.getTelemovel()));
//        setAvailableEmail((Boolean) valueToUpdate(getAvailableEmail(), infoPerson.getAvailableEmail()));
//        setAvailablePhoto((Boolean) valueToUpdate(getAvailablePhoto(), infoPerson.getAvailablePhoto()));
//        setAvailableWebSite((Boolean) valueToUpdate(getAvailableWebSite(), infoPerson
//                .getAvailableWebSite()));
        setWorkPhone(valueToUpdate(getWorkPhone(), infoPerson.getWorkPhone()));

        // setPassword(valueToUpdate(getPassword(),
        // PasswordEncryptor.encryptPassword(GeneratePassword
        // .getInstance().generatePassword(this))));

    }

    private String valueToUpdate(String actualValue, String newValue) {

        if (actualValue == null || actualValue.length() == 0) {
            return newValue;
        }
        return actualValue;

    }

    private Object valueToUpdate(Object actualValue, Object newValue) {

        if (actualValue == null) {
            return newValue;
        }
        return actualValue;

    }

    private String valueToUpdateIfNewNotNull(String actualValue, String newValue) {

        if (newValue == null || newValue.length() == 0) {
            return actualValue;
        }
        return newValue;

    }

    private Object valueToUpdateIfNewNotNull(Object actualValue, Object newValue) {

        if (newValue == null) {
            return actualValue;
        }
        return newValue;

    }

    /***************************************************************************
     * OTHER METHODS *
     **************************************************************************/

    public Person() {
        super();
        this.setMaritalStatus(MaritalStatus.UNKNOWN);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
    }

    /*
     * Construtor sem pa�s Acrescentado por Fernanda Quit�rio & T�nia Pous�o
     * Devido ao JDBC
     */
    public Person(Integer codigoInterno, String numeroDocumentoIdentificacao,
            IDDocumentType iDDocumentType, String localEmissaoDocumentoIdentificacao,
            Date dataEmissaoDocumentoIdentificacao, Date dataValidadeDocumentoIdentificacao,
            String nome, Gender sex, MaritalStatus estadoCivil, Date nascimento, String nomePai,
            String nomeMae, String nacionalidade, String freguesiaNaturalidade,
            String concelhoNaturalidade, String distritoNaturalidade, String morada, String localidade,
            String codigoPostal, String localidadeCodigoPostal, String freguesiaMorada,
            String concelhoMorada, String distritoMorada, String telefone, String telemovel,
            String email, String enderecoWeb, String numContribuinte, String profissao, String username,
            String password, String codigoFiscal) {
        this.setIdInternal(codigoInterno);
        this.setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        this.setIdDocumentType(iDDocumentType);
        this.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
        this.setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
        this.setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
        this.setNome(nome);
        this.setGender(sex);
        this.setMaritalStatus(estadoCivil);
        this.setNascimento(nascimento);
        this.setNomePai(nomePai);
        this.setNomeMae(nomeMae);
        this.setFreguesiaNaturalidade(freguesiaNaturalidade);
        this.setConcelhoNaturalidade(concelhoNaturalidade);
        this.setDistritoNaturalidade(distritoNaturalidade);
        this.setMorada(morada);
        this.setLocalidade(localidade);
        this.setCodigoPostal(codigoPostal);
        this.setLocalidadeCodigoPostal(localidadeCodigoPostal);
        this.setFreguesiaMorada(freguesiaMorada);
        this.setConcelhoMorada(concelhoMorada);
        this.setDistritoMorada(distritoMorada);
        this.setTelefone(telefone);
        this.setTelemovel(telemovel);
        this.setEmail(email);
        this.setEnderecoWeb(enderecoWeb);
        this.setNumContribuinte(numContribuinte);
        this.setProfissao(profissao);
        this.setUsername(username);
        this.setPassword(password);
        this.setCodigoFiscal(codigoFiscal);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);

    }

    public Person(String numeroDocumentoIdentificacao, IDDocumentType tipoDocumentoIdentificacao,
            String localEmissaoDocumentoIdentificacao, Date dataEmissaoDocumentoIdentificacao,
            Date dataValidadeDocumentoIdentificacao, String nome, Gender sex, MaritalStatus estadoCivil,
            Date nascimento, String nomePai, String nomeMae, String nacionalidade,
            String freguesiaNaturalidade, String concelhoNaturalidade, String distritoNaturalidade,
            String morada, String localidade, String codigoPostal, String localidadeCodigoPostal,
            String freguesiaMorada, String concelhoMorada, String distritoMorada, String telefone,
            String telemovel, String email, String enderecoWeb, String numContribuinte,
            String profissao, String username, String password, ICountry pais, String codigoFiscal) {
        this.setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
        this.setIdDocumentType(tipoDocumentoIdentificacao);
        this.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
        this.setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
        this.setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
        this.setNome(nome);
        this.setGender(sex);
        this.setMaritalStatus(estadoCivil);
        this.setNascimento(nascimento);
        this.setNomePai(nomePai);
        this.setNomeMae(nomeMae);
        this.setFreguesiaNaturalidade(freguesiaNaturalidade);
        this.setConcelhoNaturalidade(concelhoNaturalidade);
        this.setDistritoNaturalidade(distritoNaturalidade);
        this.setMorada(morada);
        this.setLocalidade(localidade);
        this.setCodigoPostal(codigoPostal);
        this.setLocalidadeCodigoPostal(localidadeCodigoPostal);
        this.setFreguesiaMorada(freguesiaMorada);
        this.setConcelhoMorada(concelhoMorada);
        this.setDistritoMorada(distritoMorada);
        this.setTelefone(telefone);
        this.setTelemovel(telemovel);
        this.setEmail(email);
        this.setEnderecoWeb(enderecoWeb);
        this.setNumContribuinte(numContribuinte);
        this.setProfissao(profissao);
        this.setUsername(username);
        this.setPassword(password);
        this.setPais(pais);
        this.setCodigoFiscal(codigoFiscal);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
    }

    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Person idInternal= ");
        stringBuilder.append(getIdInternal());
        stringBuilder.append(" username= ");
        stringBuilder.append(getUsername());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public String getSlideName() {
        return "/photos/person/P" + getIdInternal();
    }

    public String getSlideNameForCandidateDocuments() {
        return "/candidateDocuments/person/P" + getIdInternal();
    }

    public void removeRoleByType(final RoleType roleType) {
        final IRole role = getPersonRole(roleType);
        if (role != null) {
            removePersonRoles(role);
        }
    }

    public void indicatePrivledges(final List<IRole> roles) {
        for (int i = 0; i < getPersonRolesCount(); i++) {
            final IRole role = getPersonRoles().get(i);
            if (!roles.contains(role)) {
                removePersonRoles(role);
            }
        }

        for (final IRole role : roles) {
            if (!hasPersonRoles(role)) {
                addPersonRoles(role);
            }
        }
    }

    public Iterator<IUserGroup> getUserGroupsIterator()
    {
    	return new FilterIterator(this.getContentsIterator(),new ContentAssignableClassPredicate(IUserGroup.class));
    }

	public int getUserGroupsCount()
	{
		int groupsCount = 0;
		Iterator<IUserGroup> iterator = this.getUserGroupsIterator();
		while (iterator.hasNext())
		{
			iterator.next();
			groupsCount++;
		}
		return groupsCount;
	}
	
    public List<IPersonFunction> getActiveFunctions() {

        List<IPersonFunction> activeFunctions = new ArrayList<IPersonFunction>();

        for (IPersonFunction personFunction : this.getPersonFunctions()) {
            if (personFunction.isActive(prepareCalendar().getTime())) {
                activeFunctions.add(personFunction);
            }
        }
        return activeFunctions;
    }

    public List<IPersonFunction> getInactiveFunctions() {

        List<IPersonFunction> inactiveFunctions = new ArrayList<IPersonFunction>();

        for (IPersonFunction personFunction : this.getPersonFunctions()) {
           if (!personFunction.isActive(prepareCalendar().getTime())) {
                inactiveFunctions.add(personFunction);
            }
        }
        return inactiveFunctions;
    }

    public List<IFunction> getActiveInherentFunctions(){

        List<IFunction> inherentFunctions = new ArrayList<IFunction>();
        for (IPersonFunction personFunction : this.getActiveFunctions()) {
            inherentFunctions.addAll(personFunction.getFunction().getInherentFunctions());
        }
        return inherentFunctions;
    }

    private Calendar prepareCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public boolean containsActiveFunction(IFunction function) {

        for (IPersonFunction person_Function : this.getActiveFunctions()) {
            if (person_Function.getFunction().equals(function)) {
                return true;
            }
        }
        return false;
    }
}