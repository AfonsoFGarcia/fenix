package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.UsernameUtils;

public class Person extends Person_Base {

    static {
        Role.PersonRole.addListener(new PersonRoleListener());
    }

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    public String getNome() {
        return super.getName();
    }

    public void setNome(String name) {
        super.setName(name);
    }

    public Person(InfoPerson personToCreate, Country country) {

        super();
        if (personToCreate.getIdInternal() != null) {
            throw new DomainException("error.person.existentPerson");
        }

        checkConditionsToCreateNewPerson(personToCreate.getUsername(), personToCreate
                .getNumeroDocumentoIdentificacao(), personToCreate.getTipoDocumentoIdentificacao(), this);
        
        setUser(new User(personToCreate.getUsername()));
        setProperties(personToCreate);
        setPais(country);
        setIsPassInKerberos(Boolean.FALSE);
    }

    public Person(String name, String identificationDocumentNumber,
            IDDocumentType identificationDocumentType, Gender gender, String username) {

        super();
        checkConditionsToCreateNewPerson(username, identificationDocumentNumber,
                identificationDocumentType, this);
        
        setUser(new User(username));
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

        super();
        checkConditionsToCreateNewPerson(username, documentIDNumber, documentType, this);
        
        setUser(new User(username));
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
        setMaritalStatus(MaritalStatus.UNKNOWN);
        setIsPassInKerberos(Boolean.FALSE);     
    }

    public Person() {
        super();
        this.setMaritalStatus(MaritalStatus.UNKNOWN);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
    }

    public Person(String numeroDocumentoIdentificacao, IDDocumentType tipoDocumentoIdentificacao,
            String localEmissaoDocumentoIdentificacao, Date dataEmissaoDocumentoIdentificacao,
            Date dataValidadeDocumentoIdentificacao, String nome, Gender sex, MaritalStatus estadoCivil,
            Date nascimento, String nomePai, String nomeMae, String freguesiaNaturalidade,
            String concelhoNaturalidade, String distritoNaturalidade, String morada, String localidade,
            String codigoPostal, String localidadeCodigoPostal, String freguesiaMorada,
            String concelhoMorada, String distritoMorada, String telefone, String telemovel,
            String email, String enderecoWeb, String numContribuinte, String profissao, String username,
            Country pais, String codigoFiscal, Boolean availableEmail, Boolean availableWebSite,
            String workPhone) {

        super();
        checkConditionsToCreateNewPerson(username, numeroDocumentoIdentificacao,
                tipoDocumentoIdentificacao, this);

        this.setUser(new User(username));
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
        this.setPais(pais);
        this.setCodigoFiscal(codigoFiscal);
        this.setAvailableEmail(Boolean.FALSE);
        this.setAvailableWebSite(Boolean.FALSE);
        this.setAvailablePhoto(Boolean.FALSE);
        this.setAvailableEmail(availableEmail);
        this.setAvailableWebSite(availableWebSite);
        this.setWorkPhone(workPhone);     
    }

    public void edit(InfoPerson personToEdit, Country country) {
        checkConditionsToCreateNewPerson(personToEdit.getUsername(), personToEdit
                .getNumeroDocumentoIdentificacao(), personToEdit.getTipoDocumentoIdentificacao(), this);

        setProperties(personToEdit);
        if (country != null) {
            setPais(country);
        }
    }

    public void update(InfoPerson updatedPersonalData, Country country) {
        checkConditionsToCreateNewPerson(updatedPersonalData.getUsername(), updatedPersonalData
                .getNumeroDocumentoIdentificacao(), updatedPersonalData.getTipoDocumentoIdentificacao(),
                this);
        updateProperties(updatedPersonalData);
        setPais((Country) valueToUpdate(getPais(), country));
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

    private void checkConditionsToCreateNewPerson(final String username, final String documentIDNumber,
            final IDDocumentType documentType, Person thisPerson) {

        List<Person> persons = RootDomainObject.readAllPersons();
        if ((documentIDNumber != null && documentType != null && checkIfDocumentNumberIdAndDocumentIdTypeExists(
                documentIDNumber, documentType, persons, thisPerson))
                || (username != null && checkIfUsernameExists(username, persons, thisPerson))) {
            throw new DomainException("error.person.existent.username.or.docIdAndType");
        }
    }

    public static boolean checkIfUsernameExists(String username, List<Person> persons, Person thisPerson) {
        for (Person person : persons) {
            if (!person.equals(thisPerson) && username.equals(person.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfDocumentNumberIdAndDocumentIdTypeExists(final String documentIDNumber,
            final IDDocumentType documentType, List<Person> persons, Person thisPerson) {
        for (final Person person : persons) {
            if (!person.equals(thisPerson)
                    && person.getNumeroDocumentoIdentificacao().equals(documentIDNumber)
                    && person.getIdDocumentType().equals(documentType)) {
                return true;
            }
        }
        return false;
    }        
    
    public String getUsername() {
        return (getUser() != null) ? getUser().getUsername() : null;
    }
    
    public void setUsername(String username) {        
        getUser().setUsername(username);
    }
    
    public String getPassword(){
        return (getUser() != null) ? getUser().getPassword() : null;
    }
      
    public void setPassword(String password){
        getUser().setPassword(password);
    }
        
    public void setIstUsername(String istUsername) {        
        getUser().setIstUsername(istUsername);
    }
    
    public String getIstUsername() {
        return (getUser() != null) ? getUser().getIstUsername() : null;
    }
        
    public void setIsPassInKerberos(Boolean isPassInKerberos) {        
        getUser().setIsPassInKerberos(isPassInKerberos);
    }
   
    public Boolean getIsPassInKerberos() {        
        return (getUser() != null) ? getUser().getIsPassInKerberos() : null;
    }

    public void changeUsername(String newUsername, List<Person> persons) {
        if (newUsername == null || newUsername.equals("")) {
            throw new DomainException("error.person.nullOrEmptyUsername");
        }
        
        if (getUser() == null) {
            throw new DomainException("error.person.unExistingUser");
        }

        if (checkIfUsernameExists(newUsername, persons, this)) {
            throw new DomainException("error.person.existingUsername");
        }
        
        setUsername(newUsername);
    }
        
    public void changePassword(String oldPassword, String newPassword) {

        if (newPassword == null) {
            throw new DomainException("error.person.invalidNullPassword");
        }
        
        if (getUser() == null) {
            throw new DomainException("error.person.unExistingUser");
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

    public Role getPersonRole(RoleType roleType) {

        for (Role role : this.getPersonRoles()) {
            if (role.getRoleType().equals(roleType)) {
                return role;
            }
        }
        return null;
    }

    public Boolean hasRole(final RoleType roleType) {
        for (final Role role : this.getPersonRoles()) {
            if (role.getRoleType() == roleType) {
                return true;
            }
        }
        return false;
    }

    public Student getStudentByType(DegreeType degreeType) {
        for (Student student : this.getStudents()) {
            if (student.getDegreeType().equals(degreeType)) {
                return student;
            }
        }
        return null;
    }

    public String getNacionalidade() {
        return this.getPais().getNationality();
    }

    @Override
    public List<Advisory> getAdvisories() {
        Date currentDate = Calendar.getInstance().getTime();
        List<Advisory> result = new ArrayList<Advisory>();
        for (Advisory advisory : super.getAdvisories()) {
            if (advisory.getExpires().after(currentDate)) {
                result.add(advisory);
            }
        }
        return result;
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
        setMaritalStatus((infoPerson.getMaritalStatus() == null) ? MaritalStatus.UNKNOWN : infoPerson
                .getMaritalStatus());
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
        setNumeroDocumentoIdentificacao(valueToUpdateIfNewNotNull(getNumeroDocumentoIdentificacao(),
                infoPerson.getNumeroDocumentoIdentificacao()));
        setIdDocumentType((IDDocumentType) valueToUpdateIfNewNotNull(getIdDocumentType(), infoPerson
                .getTipoDocumentoIdentificacao()));
        setCodigoFiscal(valueToUpdateIfNewNotNull(getCodigoFiscal(), infoPerson.getCodigoFiscal()));
        setCodigoPostal(valueToUpdateIfNewNotNull(getCodigoPostal(), infoPerson.getCodigoPostal()));
        setConcelhoMorada(valueToUpdateIfNewNotNull(getConcelhoMorada(), infoPerson.getConcelhoMorada()));
        setConcelhoNaturalidade(valueToUpdateIfNewNotNull(getConcelhoNaturalidade(), infoPerson
                .getConcelhoNaturalidade()));
        setDataEmissaoDocumentoIdentificacao((Date) valueToUpdateIfNewNotNull(
                getDataEmissaoDocumentoIdentificacao(), infoPerson
                        .getDataEmissaoDocumentoIdentificacao()));
        setDataValidadeDocumentoIdentificacao((Date) valueToUpdateIfNewNotNull(
                getDataValidadeDocumentoIdentificacao(), infoPerson
                        .getDataValidadeDocumentoIdentificacao()));
        setDistritoMorada(valueToUpdateIfNewNotNull(getDistritoMorada(), infoPerson.getDistritoMorada()));
        setDistritoNaturalidade(valueToUpdateIfNewNotNull(getDistritoNaturalidade(), infoPerson
                .getDistritoNaturalidade()));
        setEmail(valueToUpdate(getEmail(), infoPerson.getEmail()));
        setEnderecoWeb(valueToUpdate(getEnderecoWeb(), infoPerson.getEnderecoWeb()));
        MaritalStatus maritalStatus = (MaritalStatus) valueToUpdateIfNewNotNull(getMaritalStatus(),
                infoPerson.getMaritalStatus());
        setMaritalStatus((maritalStatus == null) ? MaritalStatus.UNKNOWN : maritalStatus);
        setFreguesiaMorada(valueToUpdateIfNewNotNull(getFreguesiaMorada(), infoPerson
                .getFreguesiaMorada()));
        setFreguesiaNaturalidade(valueToUpdateIfNewNotNull(getFreguesiaNaturalidade(), infoPerson
                .getFreguesiaNaturalidade()));
        setLocalEmissaoDocumentoIdentificacao(valueToUpdateIfNewNotNull(
                getLocalEmissaoDocumentoIdentificacao(), infoPerson
                        .getLocalEmissaoDocumentoIdentificacao()));
        setLocalidade(valueToUpdateIfNewNotNull(getLocalidade(), infoPerson.getLocalidade()));
        setLocalidadeCodigoPostal(valueToUpdateIfNewNotNull(getLocalidadeCodigoPostal(), infoPerson
                .getLocalidadeCodigoPostal()));
        setMorada(valueToUpdateIfNewNotNull(getMorada(), infoPerson.getMorada()));
        setNascimento((Date) valueToUpdateIfNewNotNull(getNascimento(), infoPerson.getNascimento()));
        setNomeMae(valueToUpdateIfNewNotNull(getNomeMae(), infoPerson.getNomeMae()));
        setNomePai(valueToUpdateIfNewNotNull(getNomePai(), infoPerson.getNomePai()));
        setNumContribuinte(valueToUpdateIfNewNotNull(getNumContribuinte(), infoPerson
                .getNumContribuinte()));
        setProfissao(valueToUpdateIfNewNotNull(getProfissao(), infoPerson.getProfissao()));
        setGender((Gender) valueToUpdateIfNewNotNull(getGender(), infoPerson.getSexo()));
        setTelefone(valueToUpdate(getTelefone(), infoPerson.getTelefone()));
        setTelemovel(valueToUpdate(getTelemovel(), infoPerson.getTelemovel()));
        // setAvailableEmail((Boolean) valueToUpdate(getAvailableEmail(),
        // infoPerson.getAvailableEmail()));
        // setAvailablePhoto((Boolean) valueToUpdate(getAvailablePhoto(),
        // infoPerson.getAvailablePhoto()));
        // setAvailableWebSite((Boolean) valueToUpdate(getAvailableWebSite(),
        // infoPerson
        // .getAvailableWebSite()));
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
        final Role role = getPersonRole(roleType);
        if (role != null) {
            removePersonRoles(role);
        }
    }

    public void indicatePrivledges(final List<Role> roles) {
        getPersonRoles().retainAll(roles);
        getPersonRoles().addAll(roles);
    }

    public List<PersonFunction> getActiveFunctions() {

        List<PersonFunction> activeFunctions = new ArrayList<PersonFunction>();

        for (PersonFunction personFunction : this.getPersonFunctions()) {
            if (personFunction.isActive(Calendar.getInstance().getTime())) {
                activeFunctions.add(personFunction);
            }
        }
        return activeFunctions;
    }

    public List<PersonFunction> getInactiveFunctions() {

        List<PersonFunction> inactiveFunctions = new ArrayList<PersonFunction>();

        for (PersonFunction personFunction : this.getPersonFunctions()) {
            if (!personFunction.isActive(Calendar.getInstance().getTime())) {
                inactiveFunctions.add(personFunction);
            }
        }
        return inactiveFunctions;
    }

    public List<Function> getActiveInherentFunctions() {

        List<Function> inherentFunctions = new ArrayList<Function>();
        for (PersonFunction personFunction : this.getActiveFunctions()) {
            inherentFunctions.addAll(personFunction.getFunction().getInherentFunctions());
        }
        return inherentFunctions;
    }

    public boolean containsActiveFunction(Function function) {

        for (PersonFunction person_Function : this.getActiveFunctions()) {
            if (person_Function.getFunction().equals(function)) {
                return true;
            }
        }
        return false;
    }

    public List<PersonFunction> getPersonFuntions(Date beginDate, Date endDate) {

        List<PersonFunction> result = new ArrayList<PersonFunction>();

        for (PersonFunction personFunction : getPersonFunctions()) {
            if (personFunction.belongsToPeriod(beginDate, endDate)) {
                result.add(personFunction);
            }
        }

        return result;
    }

    /**
     * @return a group that only contains this person
     */
    public PersonGroup getPersonGroup() {
        return new PersonGroup(this);
    }

    /**
     * 
     * IMPORTANT: This method is evil and should NOT be used! You are NOT God!
     * 
     * 
     * @return true if the person have been deleted, false otherwise
     */
    public boolean delete() {

        if (canBeDeleted()) {
            if (hasPersonalPhoto()) {
                getPersonalPhoto().delete();
            }
            // JCACHOPO: Can this for loop be replaced with the clear below?
            // for (Role role : getPersonRoles()) {
            // PersonRole.forceRemove(this, role);
            // }
            getPersonRoles().clear();

            getManageableDepartmentCredits().clear();
            getAdvisories().clear();
            removeCms();
            removePais();
            RootDomainObject.getInstance().removeParties(this);
            deleteDomainObject();
            return true;
        }

        return false;
    }

    private boolean canBeDeleted() {

        if (getStudentsCount() > 0) {
            return false;
        }
        if (getSentSmsCount() > 0) {
            return false;
        }
        if (getExportGroupingReceiversCount() > 0) {
            return false;
        }
        if (getPersonFunctionsCount() > 0) {
            return false;
        }
        if (getAssociatedQualificationsCount() > 0) {
            return false;
        }
        if (getAssociatedAlteredCurriculumsCount() > 0) {
            return false;
        }
        if (getEnrolmentEvaluationsCount() > 0) {
            return false;
        }
        if (getExportGroupingSendersCount() > 0) {
            return false;
        }
        if (getEditedWebSiteItemsCount() > 0) {
            return false;
        }
        if (getResponsabilityTransactionsCount() > 0) {
            return false;
        }
        if (getMasterDegreeCandidatesCount() > 0) {
            return false;
        }
        if (getCreatedContentsCount() > 0) {
            return false;
        }
        if (getGuidesCount() > 0) {
            return false;
        }
        if (getProjectAccessesCount() > 0) {
            return false;
        }
        if (getPersonAuthorshipsCount() > 0) {
            return false;
        }
        if (getOwnedContentsCount() > 0) {
            return false;
        }

        if (getEmployee() != null) {
            return false;
        }
        if (getExternalPerson() != null) {
            return false;
        }
        if (getTeacher() != null) {
            return false;
        }
        if (getAssociatedPersonAccount() != null) {
            return false;
        }
        if (getGrantOwner() != null) {
            return false;
        }
        return true;
    }

    public static Person readPersonByUsername(final String username) {
        final User user = User.readUserByUsername(username);
        return user != null ? user.getPerson() : null;
    }

    public static Person readPersonByIstUsername(final String istUsername) {
        final User user = User.readUserByIstUsername(istUsername);
        return user != null ? user.getPerson() : null;
    }

    private static class PersonRoleListener extends dml.runtime.RelationAdapter<Role, Person> {
        /**
         * This method is called transparently to the programmer when he adds a
         * role a person. This method's responsabilities are: to verify if the
         * person allready has the role being added; to verify if the person
         * meets the prerequisites to add this new role; to update the username;
         * to actually add the role.
         */
        @Override
        public void beforeAdd(Role role, Person person) {
            // verify if the person already has the role being inserted
            if (!person.hasRole(role.getRoleType())) {

                // verify role dependencies and throw a DomainException in case
                // they
                // aren't met.
                if (!verifiesDependencies(person, role)) {
                    throw new DomainException("error.person.addingInvalidRole", role.getRoleType()
                            .toString());
                }
            }
        }

        @Override
        public void afterAdd(Role role, Person person) {
            if (role.getRoleType().equals(RoleType.TEACHER)) {
                person.addPersonRoles(Role.getRoleByRoleType(RoleType.RESEARCHER));
            }
            person.updateUsername();
            person.updateIstUsername();
        }

        /**
         * This method is called transparently to the programmer when he removes
         * a role from a person This method's responsabilities are: to actually
         * remove the role; to remove all dependencies existant from the
         * recently removed role; to update the username.
         * 
         */
        @Override
        public void beforeRemove(Role removedRole, Person person) {
            if (person != null) {
                if (removedRole != null && person.hasRole(removedRole.getRoleType())) {
                    // Remove role dependencies
                    removeDependencies(person, removedRole);
                }
            }
        }

        @Override
        public void afterRemove(Role removedRole, Person person) {
            // Update person's username according to the removal of the role
            person.updateUsername();
            person.updateIstUsername();
        }

        private static Boolean verifiesDependencies(Person person, Role role) {
            switch (role.getRoleType()) {
            case COORDINATOR:
            case DIRECTIVE_COUNCIL:
            case SEMINARIES_COORDINATOR:
            case RESEARCHER:
                return person.hasRole(RoleType.TEACHER);
            case DEGREE_ADMINISTRATIVE_OFFICE:
            case DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER:
            case DEPARTMENT_CREDITS_MANAGER:
            case GRANT_OWNER_MANAGER:
            case MASTER_DEGREE_ADMINISTRATIVE_OFFICE:
            case TREASURY:
            case CREDITS_MANAGER:
            case DEPARTMENT_ADMINISTRATIVE_OFFICE:
                return person.hasRole(RoleType.EMPLOYEE);
            case DELEGATE:
                return person.hasRole(RoleType.STUDENT);
            case PERSON:
                return true;
            default:
                return person.hasRole(RoleType.PERSON);
            }
        }

        private static void removeDependencies(Person person, Role removedRole) {
            switch (removedRole.getRoleType()) {
            case PERSON:
                removeRoleIfPresent(person, RoleType.TEACHER);
                removeRoleIfPresent(person, RoleType.EMPLOYEE);
                removeRoleIfPresent(person, RoleType.STUDENT);
                removeRoleIfPresent(person, RoleType.GEP);
                removeRoleIfPresent(person, RoleType.GRANT_OWNER);
                removeRoleIfPresent(person, RoleType.MANAGER);
                removeRoleIfPresent(person, RoleType.OPERATOR);
                removeRoleIfPresent(person, RoleType.TIME_TABLE_MANAGER);
                removeRoleIfPresent(person, RoleType.WEBSITE_MANAGER);
                removeRoleIfPresent(person, RoleType.FIRST_TIME_STUDENT);
                break;

            case TEACHER:
                removeRoleIfPresent(person, RoleType.COORDINATOR);
                removeRoleIfPresent(person, RoleType.DIRECTIVE_COUNCIL);
                removeRoleIfPresent(person, RoleType.SEMINARIES_COORDINATOR);
                removeRoleIfPresent(person, RoleType.RESEARCHER);
                // removeRoleIfPresent(person, RoleType.EMPLOYEE);
                break;

            case EMPLOYEE:
                removeRoleIfPresent(person, RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
                removeRoleIfPresent(person, RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
                removeRoleIfPresent(person, RoleType.DEPARTMENT_CREDITS_MANAGER);
                removeRoleIfPresent(person, RoleType.GRANT_OWNER_MANAGER);
                removeRoleIfPresent(person, RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
                removeRoleIfPresent(person, RoleType.TREASURY);
                removeRoleIfPresent(person, RoleType.CREDITS_MANAGER);
                removeRoleIfPresent(person, RoleType.DEPARTMENT_MEMBER);
                break;

            case STUDENT:
                removeRoleIfPresent(person, RoleType.DELEGATE);
                break;
            }
        }

        private static void removeRoleIfPresent(Person person, RoleType roleType) {
            Role tmpRole = null;
            tmpRole = person.getPersonRole(roleType);
            if (tmpRole != null) {
                person.getPersonRoles().remove(tmpRole);
            }
        }
    }

    @Deprecated
    public String getCodigoFiscal() {
        return super.getFiscalCode();
    }

    @Deprecated
    public String getCodigoPostal() {
        return super.getAreaCode();
    }

    @Deprecated
    public String getConcelhoMorada() {
        return super.getDistrictSubdivisionOfResidence();
    }

    @Deprecated
    public String getConcelhoNaturalidade() {
        return super.getDistrictSubdivisionOfBirth();
    }

    @Deprecated
    public Date getDataEmissaoDocumentoIdentificacao() {
        return super.getEmissionDateOfDocumentId();
    }

    @Deprecated
    public Date getDataValidadeDocumentoIdentificacao() {
        return super.getExpirationDateOfDocumentId();
    }

    @Deprecated
    public String getDistritoMorada() {
        return super.getDistrictOfResidence();
    }

    @Deprecated
    public String getDistritoNaturalidade() {
        return super.getDistrictOfBirth();
    }

    @Deprecated
    public String getEnderecoWeb() {
        return super.getWebAddress();
    }

    @Deprecated
    public String getFreguesiaMorada() {
        return super.getParishOfResidence();
    }

    @Deprecated
    public String getFreguesiaNaturalidade() {
        return super.getParishOfBirth();
    }

    @Deprecated
    public String getLocalEmissaoDocumentoIdentificacao() {
        return super.getEmissionLocationOfDocumentId();
    }

    @Deprecated
    public String getLocalidade() {
        return super.getArea();
    }

    @Deprecated
    public String getLocalidadeCodigoPostal() {
        return super.getAreaOfAreaCode();
    }

    @Deprecated
    public String getMorada() {
        return super.getAddress();
    }

    @Deprecated
    public Date getNascimento() {
        return super.getDateOfBirth();
    }

    @Deprecated
    public String getNomeMae() {
        return super.getNameOfMother();
    }

    @Deprecated
    public String getNomePai() {
        return super.getNameOfFather();
    }

    @Deprecated
    public String getNumContribuinte() {
        return super.getSocialSecurityNumber();
    }

    @Deprecated
    public String getNumeroDocumentoIdentificacao() {
        return super.getDocumentIdNumber();
    }

    @Deprecated
    public String getProfissao() {
        return super.getProfession();
    }

    @Deprecated
    public String getTelefone() {
        return super.getPhone();
    }

    @Deprecated
    public String getTelemovel() {
        return super.getMobile();
    }

    @Deprecated
    public void setCodigoFiscal(String codigoFiscal) {
        super.setFiscalCode(codigoFiscal);
    }

    @Deprecated
    public void setCodigoPostal(String codigoPostal) {
        super.setAreaCode(codigoPostal);
    }

    @Deprecated
    public void setConcelhoMorada(String concelhoMorada) {
        super.setDistrictSubdivisionOfResidence(concelhoMorada);
    }

    @Deprecated
    public void setConcelhoNaturalidade(String concelhoNaturalidade) {
        super.setDistrictSubdivisionOfBirth(concelhoNaturalidade);
    }

    @Deprecated
    public void setDataEmissaoDocumentoIdentificacao(Date dataEmissaoDocumentoIdentificacao) {
        super.setEmissionDateOfDocumentId(dataEmissaoDocumentoIdentificacao);
    }

    @Deprecated
    public void setDataValidadeDocumentoIdentificacao(Date dataValidadeDocumentoIdentificacao) {
        super.setExpirationDateOfDocumentId(dataValidadeDocumentoIdentificacao);
    }

    @Deprecated
    public void setDistritoMorada(String distritoMorada) {
        super.setDistrictOfResidence(distritoMorada);
    }

    @Deprecated
    public void setDistritoNaturalidade(String distritoNaturalidade) {
        super.setDistrictOfBirth(distritoNaturalidade);
    }

    @Deprecated
    public void setEnderecoWeb(String enderecoWeb) {
        super.setWebAddress(enderecoWeb);
    }

    @Deprecated
    public void setFreguesiaMorada(String freguesiaMorada) {
        super.setParishOfResidence(freguesiaMorada);
    }

    @Deprecated
    public void setFreguesiaNaturalidade(String freguesiaNaturalidade) {
        super.setParishOfBirth(freguesiaNaturalidade);
    }

    @Deprecated
    public void setLocalEmissaoDocumentoIdentificacao(String localEmissaoDocumentoIdentificacao) {
        super.setEmissionLocationOfDocumentId(localEmissaoDocumentoIdentificacao);
    }

    @Deprecated
    public void setLocalidade(String localidade) {
        super.setArea(localidade);
    }

    @Deprecated
    public void setLocalidadeCodigoPostal(String localidadeCodigoPostal) {
        super.setAreaOfAreaCode(localidadeCodigoPostal);
    }

    @Deprecated
    public void setMorada(String morada) {
        super.setAddress(morada);
    }

    @Deprecated
    public void setNascimento(Date nascimento) {
        super.setDateOfBirth(nascimento);
    }

    @Deprecated
    public void setNomeMae(String nomeMae) {
        super.setNameOfMother(nomeMae);
    }

    @Deprecated
    public void setNomePai(String nomePai) {
        super.setNameOfFather(nomePai);
    }

    @Deprecated
    public void setNumContribuinte(String numContribuinte) {
        super.setSocialSecurityNumber(numContribuinte);
    }

    @Deprecated
    public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) {
        super.setDocumentIdNumber(numeroDocumentoIdentificacao);
    }

    @Deprecated
    public void setProfissao(String profissao) {
        super.setProfession(profissao);
    }

    @Deprecated
    public void setTelefone(String telefone) {
        super.setPhone(telefone);
    }

    @Deprecated
    public void setTelemovel(String telemovel) {
        super.setMobile(telemovel);
    }

}
