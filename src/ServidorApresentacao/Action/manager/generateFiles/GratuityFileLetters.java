/*
 * Created on 28/Jan/2004
 *  
 */
package ServidorApresentacao.Action.manager.generateFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoGratuitySituation;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;

/**
 * @author T�nia Pous�o
 *  
 */
public class GratuityFileLetters
{
	public static final String SEPARATOR = ";";
	public static final double INSURANCE = 2.50;
	public static final String NOTHING = "-";
	public static final String WITHOUT_ADDRESS = "Sem morada";
	public static final String NOTHING_TO_PAY = "Nada a pagar";

	public static File buildFile(List infoGratuitySituations) throws Exception
	{
		if (infoGratuitySituations == null)
		{
			return null;
		}

		String fileName = null;
		File file = null;
		BufferedWriter writer = null;

		String fileNameErrors = null;
		File fileErrors = null;
		BufferedWriter writerErrors = null;
		try
		{
			//build the file's name with the first element
			fileName = nameFile((InfoGratuitySituation) infoGratuitySituations.get(0));
			file = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
			writer = new BufferedWriter(new FileWriter(file));

			//errors if student hasn�t address or nothing to pay
			fileNameErrors = "erros_" + nameFile((InfoGratuitySituation) infoGratuitySituations.get(0));
			fileErrors =
				new File(System.getProperty("java.io.tmpdir") + File.separator + fileNameErrors);
			writerErrors = new BufferedWriter(new FileWriter(fileErrors));

			writeHeader(writer);

			Iterator iterator = infoGratuitySituations.listIterator();
			while (iterator.hasNext())
			{
				InfoGratuitySituation infoGratuitySituation = (InfoGratuitySituation) iterator.next();

				if (valid(infoGratuitySituation, writerErrors))
				{
					writeLine(writer, infoGratuitySituation);
				}
			}

			writer.close();
			writerErrors.close();

			return file;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			throw new Exception();
		}
	}
	
	private static String nameFile(InfoGratuitySituation infoGratuitySituation)
	{
		StringBuffer fileName = new StringBuffer();

		String year = infoGratuitySituation
		.getInfoGratuityValues()
		.getInfoExecutionDegree()
		.getInfoExecutionYear()
		.getYear().replace('/','-');		
		fileName.append("cartasPropinas");
		fileName.append(year);
		fileName.append(".txt");

		return fileName.toString();
	}
	
	/**
	 * Creates a header that describes all column in file like <name column>;...; <name column>
	 * 
	 * @param writer
	 */
	private static void writeHeader(BufferedWriter writer) throws IOException
	{
		StringBuffer header = new StringBuffer();
		header.append("NOME");
		header.append(SEPARATOR);
		header.append("MORADA");
		header.append(SEPARATOR);
		header.append("LOCALIDADE");
		header.append(SEPARATOR);
		header.append("CODIGO_POSTAL");
		header.append(SEPARATOR);
		header.append("LOCALIDADE_CODIGO_POSTAL");
		header.append(SEPARATOR);
		header.append("NUMERO");
		header.append(SEPARATOR);
		header.append("MESTRADO");
		header.append(SEPARATOR);
		header.append("PROPINA");
		header.append(SEPARATOR);
		header.append("SEGURO");
		header.append(SEPARATOR);
		header.append("TOTAL");

		writer.write(header.toString());
		writer.newLine();
	}

	/**
	 * Verify if the student hasn't address and nothing to pay if true the student doesn't receive the
	 * letter
	 * 
	 * @param infoGratuitySituation
	 * @return
	 */
	private static boolean valid(
			InfoGratuitySituation infoGratuitySituation,
			BufferedWriter writerErrors)
	throws IOException
	{
		if (isNullOrEmpty(infoGratuitySituation
				.getInfoStudentCurricularPlan()
				.getInfoStudent()
				.getInfoPerson()
				.getMorada())
				|| isNullOrEmpty(
						infoGratuitySituation
						.getInfoStudentCurricularPlan()
						.getInfoStudent()
						.getInfoPerson()
						.getLocalidade())
				|| isNullOrEmpty(
						infoGratuitySituation
						.getInfoStudentCurricularPlan()
						.getInfoStudent()
						.getInfoPerson()
						.getCodigoPostal())
				|| isNullOrEmpty(
						infoGratuitySituation
						.getInfoStudentCurricularPlan()
						.getInfoStudent()
						.getInfoPerson()
						.getLocalidadeCodigoPostal()))
		{
			//write the error
			writerErrors.write(
					infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getNumber()
					+ " "
					+ WITHOUT_ADDRESS);
			writerErrors.newLine();
			return false;
		}
		else if (
				infoGratuitySituation.getRemainingValue().doubleValue() <= 0
				&& infoGratuitySituation.getInsurancePayed().equals(SessionConstants.PAYED_INSURANCE))
		{
			writerErrors.write(
					infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getNumber()
					+ " "
					+ NOTHING_TO_PAY);
			writerErrors.newLine();
			return false;
		}

		return true;
	}

	/**
	 * Verify if the string is null pu empty
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isNullOrEmpty(String string)
	{
		return (string == null || string.length() <= 0);
	}
	
	/**
	 * Creates a line with student's data separate by tab
	 * 
	 * @param writer
	 * @param infoGratuitySituation
	 */
	private static void writeLine(BufferedWriter writer, InfoGratuitySituation infoGratuitySituation)
		throws IOException
	{
		StringBuffer line = new StringBuffer();
		//student�s name
		line.append(
			infoGratuitySituation
				.getInfoStudentCurricularPlan()
				.getInfoStudent()
				.getInfoPerson()
				.getNome());
		line.append("\t");
		//student�s address
		line.append(
			infoGratuitySituation
				.getInfoStudentCurricularPlan()
				.getInfoStudent()
				.getInfoPerson()
				.getMorada());
		line.append("\t");
		//address's area
		line.append(
			infoGratuitySituation
				.getInfoStudentCurricularPlan()
				.getInfoStudent()
				.getInfoPerson()
				.getLocalidade());
		line.append("\t");
		//address's area code
		line.append(
			infoGratuitySituation
				.getInfoStudentCurricularPlan()
				.getInfoStudent()
				.getInfoPerson()
				.getCodigoPostal());
		line.append("\t");
		//address's area code's area
		line.append(
			infoGratuitySituation
				.getInfoStudentCurricularPlan()
				.getInfoStudent()
				.getInfoPerson()
				.getLocalidadeCodigoPostal());
		line.append("\t");
		//student�s number
		line.append(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getNumber());
		line.append("\t");
		//degree's name
		line.append(
			infoGratuitySituation
				.getInfoStudentCurricularPlan()
				.getInfoDegreeCurricularPlan()
				.getInfoDegree()
				.getNome());
		line.append("\t");
		//gratuity value
		if (infoGratuitySituation.getRemainingValue().doubleValue() != 0)
		{
			line.append(infoGratuitySituation.getRemainingValue());
		}
		else
		{
			//nothig to payed
			line.append(NOTHING);
		}
		line.append("\t");
		//first verify if the student already payed the insurance
		//and if the student not payed add to the total value
		//after the insurance value is appended to the line
		double totalValue = infoGratuitySituation.getRemainingValue().doubleValue();
		if (infoGratuitySituation.getInsurancePayed().equals(SessionConstants.NOT_PAYED_INSURANCE))
		{
			//insurance not payed
			line.append(INSURANCE);

			totalValue = totalValue + INSURANCE;
		}
		else
		{
			//insurance payed
			line.append(NOTHING);
		}
		line.append("\t");
		//total value
		if (totalValue != 0)
		{
			line.append(totalValue);
		}
		else
		{
			//nothig to payed
			line.append(NOTHING);
		}

		//write the line
		writer.write(line.toString());
		writer.newLine();
	}
}
