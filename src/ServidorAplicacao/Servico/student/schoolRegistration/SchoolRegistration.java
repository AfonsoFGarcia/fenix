/*
 * Created on Jul 19, 2004
 *
 */
package ServidorAplicacao.Servico.student.schoolRegistration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import Dominio.ICountry;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudentCurricularPlan;
import Dominio.Pessoa;
import Dominio.Role;
import Dominio.student.ISchoolRegistrationInquiryAnswer;
import Dominio.student.SchoolRegistrationInquiryAnswer;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.enrollment.WriteEnrollment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentRole;
import ServidorPersistente.IPersistentSchoolRegistrationInquiryAnswer;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;
import Util.enrollment.CurricularCourseEnrollmentType;

import commons.CollectionUtils;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SchoolRegistration implements IService {

    public SchoolRegistration() {
        super();
    }

    public void run(UserView userView, HashMap answers, InfoPerson infoPerson) throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
        String user = userView.getUtilizador();
        Integer studentNumber = new Integer(user.substring(1));
        
        System.out.println("O id da pessoa do catano e camandro �: " + infoPerson.getIdInternal());
        writeInquiryAnswers(suportePersistente,studentNumber,answers);
        System.out.println("J� escrevi as respostas!");
        enrollStudent1stTime1stYear(suportePersistente,studentNumber);
        System.out.println("J� inscrevi o aluno rookie!");
        updatePersonalInfo(suportePersistente,infoPerson);
        System.out.println("J� estaaaaaaaaa!");

    }

    private void enrollStudent1stTime1stYear(ISuportePersistente sp, Integer studentNumber) throws ExcepcaoPersistencia,
    		FenixServiceException {
        
        IStudentCurricularPlanPersistente scpPersistent = sp.getIStudentCurricularPlanPersistente();
        IPersistentEnrollment persistentEnrollment = sp.getIPersistentEnrolment();
        IPersistentExecutionPeriod persistentEP = sp.getIPersistentExecutionPeriod();
        IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
        
        IStudentCurricularPlan scp = scpPersistent.readActiveStudentCurricularPlan(studentNumber,TipoCurso.LICENCIATURA_OBJ);        
        IDegreeCurricularPlan dcp = scp.getDegreeCurricularPlan();
        List curricularCourses = dcp.getCurricularCoursesByYearAndSemesterAndBranch(1,new Integer(1),scp.getBranch());

        WriteEnrollment we = new WriteEnrollment();
        for(int iter=0; iter<curricularCourses.size(); iter++)
        {         
            ICurricularCourse cc = (ICurricularCourse) curricularCourses.get(iter);
            Integer executionPeriodId = persistentEP.readActualExecutionPeriod().getIdInternal();
            we.run(null,scp.getIdInternal(),cc.getIdInternal(),executionPeriodId,CurricularCourseEnrollmentType.DEFINITIVE);
        }
    }

    private void writeInquiryAnswers(ISuportePersistente sp, Integer studentNumber, HashMap answers) throws ExcepcaoPersistencia,
            FenixServiceException {

        IPersistentSchoolRegistrationInquiryAnswer persistentSRIA = sp.getIPersistentSchoolRegistrationInquiryAnswer();
        ISchoolRegistrationInquiryAnswer schoolRegistrationInquiryAnswer = persistentSRIA.readAnswersByStudentNumber(studentNumber);

        if (schoolRegistrationInquiryAnswer == null)
        {
            schoolRegistrationInquiryAnswer = new SchoolRegistrationInquiryAnswer();            
        }
        persistentSRIA.simpleLockWrite(schoolRegistrationInquiryAnswer);
        schoolRegistrationInquiryAnswer.setKeyStudent(studentNumber);
                
        Iterator iterator = answers.keySet().iterator();
        while (iterator.hasNext()) {         
            String key = (String) iterator.next();            
            schoolRegistrationInquiryAnswer.setAnswer(new Integer(key), new Boolean((String) answers.get(key)));
        }     
    }
    
    private void updatePersonalInfo(ISuportePersistente sp, InfoPerson infoPerson)throws ExcepcaoPersistencia,
    		FenixServiceException {
        
        IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();
        IPessoa pessoa = (IPessoa) pessoaPersistente.readByOID(Pessoa.class,infoPerson.getIdInternal());
        IPersistentRole pRole = sp.getIPersistentRole();
        IRole newRole = (IRole) pRole.readByRoleType(RoleType.STUDENT);
        IPersistentCountry pCountry = sp.getIPersistentCountry();
        ICountry country = (ICountry) pCountry.readCountryByNationality(infoPerson.getNacionalidade());
        
        pessoaPersistente.simpleLockWrite(pessoa);
        
        pessoa.setCodigoPostal(infoPerson.getCodigoPostal());
        pessoa.setConcelhoMorada(infoPerson.getConcelhoMorada());
        pessoa.setConcelhoNaturalidade(infoPerson.getConcelhoNaturalidade());
        pessoa.setDataEmissaoDocumentoIdentificacao(infoPerson.getDataEmissaoDocumentoIdentificacao());
        pessoa.setDataValidadeDocumentoIdentificacao(infoPerson.getDataValidadeDocumentoIdentificacao());
        pessoa.setDistritoMorada(infoPerson.getDistritoMorada());
        pessoa.setDistritoNaturalidade(infoPerson.getDistritoNaturalidade());
        pessoa.setEmail(infoPerson.getEmail());
        pessoa.setAvailableEmail(infoPerson.getAvailableEmail());
        pessoa.setEnderecoWeb(infoPerson.getEnderecoWeb());
        pessoa.setAvailableWebSite(infoPerson.getAvailableWebSite());
        pessoa.setEstadoCivil(infoPerson.getEstadoCivil());
        pessoa.setFreguesiaMorada(infoPerson.getFreguesiaMorada());
        pessoa.setFreguesiaNaturalidade(infoPerson.getFreguesiaNaturalidade());
        pessoa.setLocalidade(infoPerson.getLocalidade());
        pessoa.setLocalidadeCodigoPostal(infoPerson.getLocalidadeCodigoPostal());
        pessoa.setMorada(infoPerson.getMorada());
        pessoa.setNacionalidade(infoPerson.getNacionalidade());
        pessoa.setNomeMae(infoPerson.getNomeMae());
        pessoa.setNomePai(infoPerson.getNomePai());
        pessoa.setNumContribuinte(infoPerson.getNumContribuinte());        
        pessoa.setPassword(PasswordEncryptor.encryptPassword("pass"/*infoPerson.getPassword()*/));
        pessoa.setProfissao(infoPerson.getProfissao());       
        pessoa.setTelefone(infoPerson.getTelefone());
        pessoa.setTelemovel(infoPerson.getTelemovel());
        pessoa.setPais(country);

        //remove firstTimeStudentRole and add studentRole
        CollectionUtils.filter(pessoa.getPersonRoles(), new Predicate() {
        	
        	public boolean evaluate(Object arg0) {
        		IRole role = (Role) arg0;
        		
        		IRole newRole = new Role();
        		newRole.setRoleType(RoleType.FIRST_TIME_STUDENT);
        		
        		return !role.equals(newRole);
        	}
        });
        
        Object[] obj = {newRole};
        
        CollectionUtils.addAll(pessoa.getPersonRoles(), obj);
        
    }
    
}