package ServidorApresentacao.Action.sop.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import Util.DiaSemana;
import Util.Season;
import Util.TipoSala;

/**
 * Esta classe contem metodos que sao utilizados em varias classes Action.
 *
 * @author joao 
 **/

public class Util {

	/**
	 * Reads the existing buldings of the university and return a list
	 * with them.  This information should be placed inside of the
	 * database, instead of being placed here. The first element of
	 * the list is the selected element when the list is shown. If this
	 * element is null, then it is not added to the list.
	 **/
	public static List readExistingBuldings(String name, String value) {
		ArrayList edificios = new ArrayList();

		if (name != null)
			edificios.add(new LabelValueBean(name, value));

		edificios.add(
			new LabelValueBean("Pavilh�o Central", "Pavilh�o Central"));
		edificios.add(new LabelValueBean("Pavilh�o Civil", "Pavilh�o Civil"));
		edificios.add(
			new LabelValueBean("Pavilh�o Mec�nica II", "Pavilh�o Mec�nica II"));
		edificios.add(new LabelValueBean("Pavilh�o Minas", "Pavilh�o Minas"));
		edificios.add(
			new LabelValueBean(
				"Pavilh�o Novas Licenciaturas",
				"Pavilh�o Novas Licenciaturas"));
		edificios.add(new LabelValueBean("Pavilh�o P�s-Gradua��o", "Pavilh�o P�s-Gradua��o"));
		edificios.add(
			new LabelValueBean(
				"Pavilh�o de Electricidade",
				"Pavilh�o de Electricidade"));
		edificios.add(
			new LabelValueBean(
				"Pavilh�o de Mec�nica I",
				"Pavilh�o de Mec�nica I"));
		edificios.add(
			new LabelValueBean("Pavilh�o Mec�nica II", "Pavilh�o Mec�nica II"));

		edificios.add(
			new LabelValueBean(
				"Pavilh�o de Mec�nica III",
				"Pavilh�o de Mec�nica III"));

		edificios.add(
			new LabelValueBean(
				"Pavilh�o P�s-Gradua��o",
				"Pavilh�o P�s-Gradua��o"));
				
		edificios.add(
			new LabelValueBean(
				"Pavilh�o de Qu�mica",
				"Pavilh�o de Qu�mica"));

		edificios.add(new LabelValueBean("Torre Norte", "Torre Norte"));
		edificios.add(new LabelValueBean("Torre Sul", "Torre Sul"));
		edificios.add(
			new LabelValueBean(
				"TagusPark - Bloco A - Poente",
				"TagusPark - Bloco A - Poente"));
		edificios.add(
			new LabelValueBean(
				"TagusPark - Bloco A - Nascente",
				"TagusPark - Bloco A - Nascente"));
		edificios.add(
			new LabelValueBean(
				"TagusPark - Bloco B - Poente",
				"TagusPark - Bloco B - Poente"));
		edificios.add(
			new LabelValueBean(
				"TagusPark - Bloco B - Nascente",
				"TagusPark - Bloco B - Nascente"));

		return edificios;
	}

	/**
	 * Reads the existing kinds of salas of the university and return a list
	 * with them.  This information should be placed inside of the
	 * database, instead of being placed here. The first element of
	 * the list is the selected element when the list is shown. If this
	 * element is null, then it is not added to the list.
	 **/
	public static List readTypesOfRooms(String name, String value) {
		ArrayList tipos = new ArrayList();

		if (name != null)
			tipos.add(new LabelValueBean(name, value));

		tipos.add(
			new LabelValueBean(
				"Anfiteatro",
				(new Integer(TipoSala.ANFITEATRO)).toString()));
		tipos.add(
			new LabelValueBean(
				"Laborat�rio",
				(new Integer(TipoSala.LABORATORIO)).toString()));
		tipos.add(
			new LabelValueBean(
				"Plana",
				(new Integer(TipoSala.PLANA)).toString()));

		return tipos;
	}

	/**
	 * Method getDaysOfWeek.
	 * @return ArrayList
	 */
	public static ArrayList getDaysOfWeek() {
		ArrayList weekDays = new ArrayList();
		weekDays.add(
			new LabelValueBean(
				"segunda",
				(new Integer(DiaSemana.SEGUNDA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"ter�a",
				(new Integer(DiaSemana.TERCA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"quarta",
				(new Integer(DiaSemana.QUARTA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"quinta",
				(new Integer(DiaSemana.QUINTA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"sexta",
				(new Integer(DiaSemana.SEXTA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"s�bado",
				(new Integer(DiaSemana.SABADO)).toString()));

		return weekDays;
	}

	/**
	 * Method getHoras.
	 * @return ArrayList
	 */
	public static ArrayList getHours() {
		ArrayList hoursList = new ArrayList();

		hoursList.add("8");
		hoursList.add("9");
		hoursList.add("10");
		hoursList.add("11");
		hoursList.add("12");
		hoursList.add("13");
		hoursList.add("14");
		hoursList.add("15");
		hoursList.add("16");
		hoursList.add("17");
		hoursList.add("18");
		hoursList.add("19");
		hoursList.add("20");
		hoursList.add("21");
		hoursList.add("22");
		hoursList.add("23");
		return hoursList;
	}

	/**
	 * Method getMinutos.
	 * @return ArrayList
	 */
	public static ArrayList getMinutes() {
		ArrayList minutesList = new ArrayList();
		minutesList.add("00");
		minutesList.add("30");
		return minutesList;
	}

	/**
	 * Method getDaysOfMonth.
	 * @return ArrayList
	 */
	public static ArrayList getDaysOfMonth() {
		ArrayList daysOfMonthList = new ArrayList();

		daysOfMonthList.add("1");
		daysOfMonthList.add("2");
		daysOfMonthList.add("3");
		daysOfMonthList.add("4");
		daysOfMonthList.add("5");
		daysOfMonthList.add("6");
		daysOfMonthList.add("7");
		daysOfMonthList.add("8");
		daysOfMonthList.add("9");
		daysOfMonthList.add("10");
		daysOfMonthList.add("11");
		daysOfMonthList.add("12");
		daysOfMonthList.add("13");
		daysOfMonthList.add("14");
		daysOfMonthList.add("15");
		daysOfMonthList.add("16");
		daysOfMonthList.add("17");
		daysOfMonthList.add("18");
		daysOfMonthList.add("19");
		daysOfMonthList.add("20");
		daysOfMonthList.add("21");
		daysOfMonthList.add("22");
		daysOfMonthList.add("23");
		daysOfMonthList.add("24");
		daysOfMonthList.add("25");
		daysOfMonthList.add("26");
		daysOfMonthList.add("27");
		daysOfMonthList.add("28");
		daysOfMonthList.add("29");
		daysOfMonthList.add("30");
		daysOfMonthList.add("31");
		return daysOfMonthList;
	}

	/**
	 * Method getMonthsOfYear.
	 * @return ArrayList
	 */
	public static ArrayList getMonthsOfYear() {
		ArrayList monthsOfYearList = new ArrayList();

		monthsOfYearList.add(new LabelValueBean("Janeiro", "" + Calendar.JANUARY));
		monthsOfYearList.add(new LabelValueBean("Fevereiro", "" + Calendar.FEBRUARY));
		monthsOfYearList.add(new LabelValueBean("Mar�o", "" + Calendar.MARCH));
		monthsOfYearList.add(new LabelValueBean("Abril", "" + Calendar.APRIL));
		monthsOfYearList.add(new LabelValueBean("Maio", "" + Calendar.MAY));
		monthsOfYearList.add(new LabelValueBean("Junho", "" + Calendar.JUNE));
		monthsOfYearList.add(new LabelValueBean("Julho", "" + Calendar.JULY));
		monthsOfYearList.add(new LabelValueBean("Agosto", "" + Calendar.AUGUST));
		monthsOfYearList.add(new LabelValueBean("Setembro", "" + Calendar.SEPTEMBER));
		monthsOfYearList.add(new LabelValueBean("Outubro", "" + Calendar.OCTOBER));
		monthsOfYearList.add(new LabelValueBean("Novembro", "" + Calendar.NOVEMBER));
		monthsOfYearList.add(new LabelValueBean("Dezembro", "" + Calendar.DECEMBER));

		return monthsOfYearList;
	}

	/**
	 * Method getExamSeasons.
	 * @return ArrayList
	 */
	public static ArrayList getExamSeasons() {
		ArrayList examSeasonsList = new ArrayList();

		examSeasonsList.add(new LabelValueBean(Season.SEASON1_STRING, "" + Season.SEASON1));
		examSeasonsList.add(new LabelValueBean(Season.SEASON2_STRING, "" + Season.SEASON2));

		return examSeasonsList;
	}


	/**
	 * Method getYears.
	 * @return ArrayList
	 * @deprecated : This is not to be used... it is just
	 *               a temporay solution until years are
	 *               properly read from the database.
	 */
	public static ArrayList getYears() {
		ArrayList yearsList = new ArrayList();

		yearsList.add("2002");
		yearsList.add("2003");
		yearsList.add("2004");
		return yearsList;
	}


	/**
	 * Method getHoras.
	 * @return ArrayList
	 */
	public static ArrayList getExamShifts() {
		ArrayList hoursList = new ArrayList();
		hoursList.add("9");
		hoursList.add("13");
		hoursList.add("17");
		return hoursList;
	}


}
