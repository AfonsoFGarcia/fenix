package ServidorAplicacao.Servicos.commons;

import DataBeans.InfoTeacher;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadTeacherByNumberTest extends ServiceTestCase {

	private String dataSetFilePath;

	/**
	 * @param testName
	 */
	public ReadTeacherByNumberTest(String testName) {
		super(testName);
		this.dataSetFilePath = "etc/datasets/servicos/commons/testReadTeacherByNumberDataSet.xml";
	}

	protected String getDataSetFilePath() {
		return this.dataSetFilePath;
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadTeacherByNumber";
	}

	public void testReadExistingTeacher() {
		try {
			Integer teacherNumber = new Integer(53);
			Object[] argsReadTeacher = { teacherNumber };

			InfoTeacher infoTeacher = (InfoTeacher) gestor.executar(null, getNameOfServiceToBeTested(), argsReadTeacher);
			assertNotNull(infoTeacher);
			assertEquals(infoTeacher.getTeacherNumber(), teacherNumber);

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testReadExistingTeacher " + ex.getMessage());
		}

	}

	public void testReadNonExistingTeacher() {
		try {
			Integer teacherNumber = new Integer(1225);
			Object[] argsReadTeacher = { teacherNumber };

			InfoTeacher infoTeacher = (InfoTeacher) gestor.executar(null, getNameOfServiceToBeTested(), argsReadTeacher);
			assertNull(infoTeacher);

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testReadNonExistingTeacher " + ex.getMessage());
		}
	}

}
