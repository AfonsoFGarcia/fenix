package ServidorApresentacao.TagLib.sop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;

import DataBeans.InfoLesson;
import Util.DiaSemana;
import Util.Tempo;

public final class GerarHorarioTag extends TagSupport {

	// Dias de aulas
	final String[] DIAS = { "seg", "ter", "qua", "qui", "sex", "sab" };

	// Hora de aulas
	final String[] HORAS =
		{
			"8:00",
			"8:30",
			"9:00",
			"9:30",
			"10:00",
			"10:30",
			"11:00",
			"11:30",
			"12:00",
			"12:30",
			"13:00",
			"13:30",
			"14:00",
			"14:30",
			"15:00",
			"15:30",
			"16:00",
			"16:30",
			"17:00",
			"17:30",
			"18:00",
			"18:30",
			"19:00",
			"19:30",
			"20:00",
			"20:30",
			"21:00",
			"21:30",
			"22:00",
			"22:30",
			"23:00",
			"23:30" };

	final int HORA_MINIMA = 8;
	final int HORA_MAXIMA = 24;

	// Factor de divis�o das celulas.
	final int COL_SPAN_FACTOR = 24;

	// Nome do atributo que cont�m a lista de aulas.
	private String name;

	// Mensagens de erro.
	protected static MessageResources messages =
		MessageResources.getMessageResources("ApplicationResources");

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int doStartTag() throws JspException {

		// Obtem a lista de aulas.
		ArrayList listaAulas = null;
		try {
			listaAulas = (ArrayList) pageContext.findAttribute(name);
		} catch (ClassCastException e) {
			listaAulas = null;
		}
		if (listaAulas == null)
			throw new JspException(
				messages.getMessage("gerarHorario.listaAulas.naoExiste", name));

		// Gera o hor�rio a partir da lista de aulas.
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(this.geraHorario(listaAulas));
		} catch (IOException e) {
			throw new JspException(
				messages.getMessage("gerarHorario.io", e.toString()));
		}
		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		return (EVAL_PAGE);
	}

	public void release() {
		super.release();
	}

	private String geraHorario(ArrayList listaAulas) {
		StringBuffer result = new StringBuffer();

		// Verifica se a lista de aulas � correcta.
		if (!verificaAulas(listaAulas))
			return new String("<H3 align='center'> Hor�rio n�o disponivel </H3>");

		// Prepara a estrutura de dados auxiliar utilizada na cria��o do horario.
		ArrayList[][] matriz = this.preparaMatriz(listaAulas);

		// Abre tabela.
		result.append(
//			"<table align='center' border='1' rules='all' style='font-size: 10pt' "
//				+ "cellspacing='2' cellpading='5%'>");
		"<table width='100%' border='1' cellpadding='0' cellspacing='1'>");
		
		// Grupos de colunas.
		result.append("<COLGROUP span='1' style='color: blue'> </COLGROUP>");
		result.append(
			"<COLGROUP span='"
				+ COL_SPAN_FACTOR * 6
				+ "' style='color: green'> </COLGROUP>");

		// Cabe�alho da tabela.
		result.append("<THEAD style='color: blue'>");
		result.append("<tr align='center'><td width='10%' class='horarioHeader'>&nbsp;</td>");
		for (int i = 0; i < DIAS.length; i++)
			result.append(
				"<td colspan='" + COL_SPAN_FACTOR + "' width='15%' class='horarioHeader'>" + DIAS[i] + "</td>");
		result.append("</tr>");
		result.append("</THEAD>");

		// Corpo da tabela.
		result.append("<tbody>");
		for (int i = 0; i < HORAS.length; i++) {

			// Abre linha.
			result.append("<tr align='center'>");

			// Hora da linha.
			result.append("<td class='horariosHoras'>" + HORAS[i] + "</td>");

			// Preenche linha.
			for (int j = 0; j < DIAS.length; j++) {
				if (!matriz[j][i].isEmpty()) {
					ArrayList listaCellInfo = matriz[j][i];
					for (int k = 0; k < listaCellInfo.size(); k++) {
						CellInfo cellInfo = (CellInfo) listaCellInfo.get(k);
						result.append(
							"<td class='horariosBody' colspan='"
								+ cellInfo.getColSpan()
								+ "' rowspan='"
								+ cellInfo.getRowSpan()
								+ "'>"
								+ cellInfo.getContent()
								+ "</td>");
					}
				}
			}

			// Fecha linha.
			result.append("</tr>");
		}

		// Fecha tabela.
		result.append("</TBODY>");
		result.append("</table>");

		// Legenda do hor�rio.
		result.append(legenda(listaAulas));

		return result.toString();
	}

	private boolean verificaAulas(ArrayList listaAulas) {
		for (int i = 0; i < listaAulas.size(); i++) {
			InfoLesson aula = (InfoLesson) listaAulas.get(i);

			int horaInicio = aula.getInicio().get(Calendar.HOUR_OF_DAY);
			int horaFim = aula.getFim().get(Calendar.HOUR_OF_DAY);
			int minutosInicio = aula.getInicio().get(Calendar.MINUTE);
			int minutosFim = aula.getFim().get(Calendar.MINUTE);
			if ((horaInicio < HORA_MINIMA || horaInicio > HORA_MAXIMA - 1)
				|| (horaFim < HORA_MINIMA || horaFim > HORA_MAXIMA)
				|| (horaInicio > horaFim)
				|| (horaInicio == horaFim && minutosInicio > minutosFim))
				return false;
		}

		return true;
	}

	private ArrayList[][] preparaMatriz(ArrayList listaAulas) {
		ArrayList[][] matriz = new ArrayList[DIAS.length][HORAS.length];
		inicializaMatriz(matriz);
		insereAulas(matriz, listaAulas);
		insereBrancos(matriz);
		return matriz;
	}

	private void inicializaMatriz(ArrayList[][] matriz) {
		for (int i = 0; i < DIAS.length; i++)
			for (int j = 0; j < HORAS.length; j++)
				matriz[i][j] = new ArrayList();
	}

	private void insereAulas(ArrayList[][] matriz, ArrayList listaAulas) {
		for (int i = 0; i < listaAulas.size(); i++) {
			InfoLesson aula = (InfoLesson) listaAulas.get(i);
			int indiceDia = converteDia(aula.getDiaSemana().getDiaSemana());
			int indiceHora = converteHora(criaTempo(aula.getInicio()));
			int rowSpan =
				converteDuracao(
					criaTempo(aula.getInicio()),
					criaTempo(aula.getFim()));
			matriz[indiceDia][indiceHora].add(new CellInfo(rowSpan, aula));
		}
	}

	private void insereBrancos(ArrayList[][] matriz) {
		for (int i = 0; i < DIAS.length; i++) {
			int j = 0;
			while (j < HORAS.length) {
				if (matriz[i][j].isEmpty()) {
					int rowSpan = 0;
					while (j < HORAS.length && matriz[i][j].isEmpty()) {
						rowSpan++;
						j++;
					}
					matriz[i][j
						- rowSpan].add(
							new CellInfo(rowSpan, COL_SPAN_FACTOR, null));
				} else {
					j += this.calculaMancha(matriz, i, j);
				}
			}
		}
	}

	private int converteDia(Integer dia) {
		int indiceDia = -1;
		if (dia != null) {
			switch (dia.intValue()) {
				case DiaSemana.SEGUNDA_FEIRA :
					indiceDia = 0;
					break;
				case DiaSemana.TERCA_FEIRA :
					indiceDia = 1;
					break;
				case DiaSemana.QUARTA_FEIRA :
					indiceDia = 2;
					break;
				case DiaSemana.QUINTA_FEIRA :
					indiceDia = 3;
					break;
				case DiaSemana.SEXTA_FEIRA :
					indiceDia = 4;
					break;
				case DiaSemana.SABADO :
					indiceDia = 5;
					break;
				default :
					System.out.println(
						"GerarHorarioTag.converteDia(): dia de semana inv�lido.");
					indiceDia = -1;
					break;
			}
		}
		return indiceDia;
	}

	private int converteHora(Tempo inicio) {
		return (inicio.hora() - HORA_MINIMA) * 2
			+ (inicio.minutos() == 0 ? 0 : 1);
	}

	private int converteDuracao(Tempo inicio, Tempo fim) {
		return fim.hora() * 2
			+ (fim.minutos() == 0 ? 0 : 1)
			- inicio.hora() * 2
			+ (inicio.minutos() == 0 ? 0 : -1);
	}

	private int calculaMancha(ArrayList[][] matriz, int i, int j) {
		resolveSobreposicoes(matriz, i, j);
		return calculaDimensaoMancha(matriz, i, j);
	}

	private void resolveSobreposicoes(ArrayList[][] matriz, int i, int j) {
		int numSobreposicoes = matriz[i][j].size();
		int dimMancha = calculaDimensaoMancha(matriz, i, j);
		for (int k = j + 1, l = 1; l < dimMancha; k++, l++) {
			if (!matriz[i][k].isEmpty()) {
				numSobreposicoes += matriz[i][k].size();
			}
		}
//cuidado com as compara��es do getContent do cellInfo
		for (int k = j, l = 0; l < dimMancha; k++, l++) {
			if (!matriz[i][k].isEmpty()) {
				for (int m = 0; m < matriz[i][k].size(); m++) {
					if (!((CellInfo) matriz[i][k].get(m))
						.getContent()
						.equals("")) {

						// Actualiza colSpan
						((CellInfo) matriz[i][k].get(m)).setColSpan(
							COL_SPAN_FACTOR / numSobreposicoes);

						// Insere brancos de inicio.
						int up = k - j;
						if (up > 0)
							matriz[i][j].add(
								new CellInfo(
									up,
									COL_SPAN_FACTOR / numSobreposicoes,
									null));

						// Insere brancos de fim
						int span =
							((CellInfo) matriz[i][k].get(m)).getRowSpan();
						int rest = dimMancha - (k - j) - span;
						if (rest > 0)
							matriz[i][k
								+ span].add(
									0,
									new CellInfo(
										rest,
										COL_SPAN_FACTOR / numSobreposicoes,
										null));
					}
				}
			}
		}
	}

	private int calculaDimensaoMancha(ArrayList[][] matriz, int i, int j) {
		int localRowSpan = calculaDimensaoManchaLocal(matriz, i, j);
		for (int k = j + 1, l = 1; l < localRowSpan; k++, l++) {
			if (!matriz[i][k].isEmpty()) {
				return StrictMath.max(
					localRowSpan,
					l + calculaDimensaoMancha(matriz, i, k));
			}
		}
		return localRowSpan;
	}

	private int calculaDimensaoManchaLocal(
		ArrayList[][] matriz,
		int i,
		int j) {
		int maxRowSpan = 0;
		for (int k = 0; k < matriz[i][j].size(); k++) {
			int rowSpan = ((CellInfo) matriz[i][j].get(k)).getRowSpan();
			if (rowSpan > maxRowSpan)
				maxRowSpan = rowSpan;
		}
		return maxRowSpan;
	}

	private StringBuffer legenda(ArrayList listaAulas) {
		StringBuffer result = new StringBuffer();
		ArrayList listaAuxiliar = new ArrayList();
		Iterator iterator = listaAulas.iterator();
		while (iterator.hasNext()) {
			InfoLesson elem = (InfoLesson) iterator.next();
			SubtitleEntry subtitleEntry =
				new SubtitleEntry(
					elem.getInfoDisciplinaExecucao().getSigla(),
					elem.getInfoDisciplinaExecucao().getNome());
			if (!listaAuxiliar.contains(subtitleEntry))
				listaAuxiliar.add(subtitleEntry);
		}
		Collections.sort(listaAuxiliar);
		iterator = listaAuxiliar.iterator();
		result.append(
			"<br/><table align='center'><th colspan='3'>Legenda:</th>");
		while (iterator.hasNext()) {
			SubtitleEntry elem = (SubtitleEntry) iterator.next();
			result.append("<tr><td>");
			result.append(elem.getKey());
			result.append("</td><td>");
			result.append("-");
			result.append("</td><td>");
			result.append(elem.getValue());
			result.append("</td></tr>");
		}
		result.append("</table>");
		return result;
	}

	private Tempo criaTempo(Calendar c) {
		return new Tempo(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
	}
}
