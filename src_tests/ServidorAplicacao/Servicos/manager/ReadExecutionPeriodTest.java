///*
// * Created on 27/Set/2003
// */
//package ServidorAplicacao.Servicos.manager;
//
//import DataBeans.InfoExecutionPeriod;
//import DataBeans.InfoExecutionYear;
//
///**
// * @author lmac1
// */
//public class ReadExecutionPeriodTest extends TestCaseManagerReadServices {
//	    
//	/**
//	 * @param testName
//	 */
//	 public ReadExecutionPeriodTest(String testName) {
//		super(testName);
//	 }
//
//	 protected String getNameOfServiceToBeTested() {
//		return "ReadExecutionPeriod";
//	 }
//       
//	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() { 
//		Object[] args = { new Integer(100) };
//		return args;
//	}	
//
//	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
//		Object[] args = { new Integer(1) };
//		return args;
//	}
//        	 
//	protected Object getObjectToCompare() {	
//		InfoExecutionYear infoExecutionYear =new InfoExecutionYear("2002/2003");		
//		return new InfoExecutionPeriod("2� Semestre", infoExecutionYear);
//	}
//}