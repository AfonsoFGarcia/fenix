package ServidorApresentacao.sop;
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoAula;
/**
@author tfc130
*/
public class PrepararVerTurnoFormActionTest extends TestCasePresentation{
 
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
   
  public static Test suite() {
    TestSuite suite = new TestSuite(PrepararVerTurnoFormActionTest.class);
        
    return suite;
  }
  public void setUp() {
    super.setUp();
    // define ficheiro de configuracao Struts a utilizar
    setServletConfigFile("/WEB-INF/tests/web-sop.xml");
    
    
  }

  public PrepararVerTurnoFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulPrepararVerTurno() {
	getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);

  	// Necessario para colocar form manipularTurnosForm em sessao
  	setRequestPathInfo("/sop", "/manipularTurnosForm");
  	addRequestParameter("indexTurno", new Integer(0).toString());  	
	actionPerform();
	
    // define mapping de origem
    setRequestPathInfo("/sop", "/prepararVerTurno");
      
    // coloca credenciais na sessao
    HashSet privilegios = new HashSet();
    privilegios.add("VerTurno");
    privilegios.add("LerAulasDeTurno");
    privilegios.add("LerTurnosDeDisciplinaExecucao");
    IUserView userView = new UserView("user", privilegios);
    
    
    getSession().setAttribute("UserView", userView);
    try {
    	GestorServicos gestor = GestorServicos.manager();
		InfoExecutionCourse iDE = new InfoExecutionCourse(
							"Trabalho Final de Curso I",
							"TFCI",
							"programa1",
							new Double(0),
							new Double(0),
							new Double(0),
							new Double(0),
							new InfoExecutionPeriod("2� Semestre",	new InfoExecutionYear("2002/2003")));
    	getSession().setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, iDE);

    	Object argsLerTurnos[] = new Object[1];
    	argsLerTurnos[0] = iDE;
    	ArrayList infoTurnos = (ArrayList) gestor.executar(userView, "LerTurnosDeDisciplinaExecucao", argsLerTurnos);
    	getSession().setAttribute("infoTurnosDeDisciplinaExecucao", infoTurnos);

    	
		InfoShift infoTurno1 = new InfoShift("turno1", new TipoAula(1), new Integer(100), iDE);
    	InfoShift infoTurno = (InfoShift) infoTurnos.get(infoTurnos.indexOf((InfoShift) infoTurno1));
    	getSession().setAttribute("infoTurno", infoTurno);

    	Object argsLerAulasDeTurno[] = { new ShiftKey(infoTurno.getNome(), infoTurno.getInfoDisciplinaExecucao())};
		ArrayList infoAulasDeTurno = (ArrayList) gestor.executar(userView, "LerAulasDeTurno", argsLerAulasDeTurno);
		getSession().setAttribute("infoAulasDeTurno", infoAulasDeTurno);
	
    } catch (Exception ex) {
    	System.out.println("Erro na invocacao do servico " + ex);
    }

    // invoca ac��o
    actionPerform();
    // verifica reencaminhamento
    verifyForward("Sucesso");
    
    //verifica ausencia de erros
    verifyNoActionErrors();
  }




 
}