/*
 * Created on Oct 3, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;

public enum FunctionType {

    PRESIDENT, VICE_PRESIDENT, ASSISTANCE_PRESIDENT, DIRECTOR, ASSOCIATE_DIRECTOR, COORDINATOR, VICE_COORDINATOR, ASSOCIATE_COORDINATOR, CHIEF, RESPONSIBLE, TEACHER, EMPLOYEE, NON_TEACHING_EMPLOYEE, TEACHER_MEMBER, EMPLOYEE_MEMBER, STUDENT_MEMBER, TEACER_VOWEL, VOWEL, SECRETARY, REPRESENTATIVE, MEMBER, ASSIDUOUSNESS_RESPONSIBLE, PRINCIPAL, PRO_PRINCIPAL, VICE_PRINCIPAL, MANAGER, ADVISER,

    DELEGATE_OF_YEAR, // Delegado de Ano
    DELEGATE_OF_DEGREE, // Delegado de Licenciatura (escolhido entre delegado de
    // ano do 1�, 2� ou 3� ano)
    DELEGATE_OF_MASTER_DEGREE, // Delegado de Mestrado (delegado de ano do 1� ou
    // 2� ano do Mestrado | delegado de ano do 4� ou
    // 5� ano do Mestrado Integrado)
    DELEGATE_OF_INTEGRATED_MASTER_DEGREE, // Delegado de Mestrado Integrado
    // (delegado de ano do 4� ou 5� ano do
    // Mestrado Integrado)
    DELEGATE_OF_GGAE, // Delegado de um Grupo de Grandes �reas de Estudo
    // (delegado de Licenciatura, Mestrado ou Mestrado
    // Integrado )

    VIRTUAL /* for functions that can be created unofficially */,

    MICRO_PAYMENT_MANAGER,

    /* Research */
    PERMANENT_RESEARCHER, INVITED_RESEARCHER, TECHNICAL_STAFF, COLLABORATORS, OTHER_STAFF, PHD_STUDENT, MSC_STUDENT, POST_DOC_STUDENT;

    public String getName() {
	return name();
    }

    public static List<FunctionType> getAllDelegateFunctionTypes() {
	List<FunctionType> result = new ArrayList<FunctionType>();
	result.add(FunctionType.DELEGATE_OF_YEAR);
	result.add(FunctionType.DELEGATE_OF_DEGREE);
	result.add(FunctionType.DELEGATE_OF_MASTER_DEGREE);
	result.add(FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE);
	result.add(FunctionType.DELEGATE_OF_GGAE);

	return result;
    }

    public static List<FunctionType> getAllDegreeDelegateFunctionTypes() {
	List<FunctionType> result = getAllDelegateFunctionTypes();
	result.remove(FunctionType.DELEGATE_OF_GGAE);

	return result;
    }

    public static List<FunctionType> getResearchSubSet() {
	List<FunctionType> functions = new ArrayList<FunctionType>();
	functions.add(PERMANENT_RESEARCHER);
	functions.add(INVITED_RESEARCHER);
	functions.add(TECHNICAL_STAFF);
	functions.add(COLLABORATORS);
	functions.add(OTHER_STAFF);
	functions.add(PHD_STUDENT);
	functions.add(MSC_STUDENT);
	functions.add(POST_DOC_STUDENT);
	return functions;
    }
}
