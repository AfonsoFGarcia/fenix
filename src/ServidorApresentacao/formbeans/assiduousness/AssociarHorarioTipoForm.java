package ServidorApresentacao.formbeans.assiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import Dominio.Horario;
import Dominio.HorarioTipo;
import Dominio.IStrategyHorarios;
import Dominio.SuporteStrategyHorarios;

/**
 *
 * @author  Fernanda Quit�rio & Tania Pous�o
 */
public class AssociarHorarioTipoForm extends ActionForm {
	private String _numMecanografico = null;

	private String _numDias = null;
	private String _posicao = null;

	private ArrayList _listaSiglas = new ArrayList();

	private String _diaInicio = null;
	private String _mesInicio = null;
	private String _anoInicio = null;
	private String _diaFim = null;
	private String _mesFim = null;
	private String _anoFim = null;

	private String _sigla = null;
	
	private boolean _excepcaoHorario = false;

	public String getNumMecanografico() {
		return (_numMecanografico);
	}

	public String getNumDias() {
		return _numDias;
	}
	public String getPosicao() {
		return _posicao;
	}

	public ArrayList getListaSiglas() {
		return (_listaSiglas);
	}

	public String getDiaInicio() {
		return (_diaInicio);
	}
	public String getMesInicio() {
		return (_mesInicio);
	}
	public String getAnoInicio() {
		return (_anoInicio);
	}

	public String getDiaFim() {
		return (_diaFim);
	}
	public String getMesFim() {
		return (_mesFim);
	}
	public String getAnoFim() {
		return (_anoFim);
	}
	public String getSigla() {
		return (_sigla);
	}
	public boolean isExcepcaoHorario() {
		return _excepcaoHorario;
	}

	public void setNumMecanografico(String numMecanografico) {
		_numMecanografico = numMecanografico;
	}

	public void setNumDias(String numDias) {
		_numDias = numDias;
	}
	public void setPosicao(String posicao) {
		_posicao = posicao;
	}

	public void setListaSiglas(ArrayList listaSiglas) {
		_listaSiglas = listaSiglas;
	}

	public void setDiaInicio(String diaInicio) {
		_diaInicio = diaInicio;
	}
	public void setMesInicio(String mesInicio) {
		_mesInicio = mesInicio;
	}
	public void setAnoInicio(String anoInicio) {
		_anoInicio = anoInicio;
	}
	public void setDiaFim(String diaFim) {
		_diaFim = diaFim;
	}
	public void setMesFim(String mesFim) {
		_mesFim = mesFim;
	}
	public void setAnoFim(String anoFim) {
		_anoFim = anoFim;
	}
	public void setSigla(String sigla) {
		_sigla = sigla;
	}	
	public void setExcepcaoHorario(boolean excepcaoHorario) {
		_excepcaoHorario = excepcaoHorario;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		_numMecanografico = "";

		_numDias = "";
		_posicao = "";

		//TODO: sigla por omissao

		_diaFim = "";
		_mesFim = "";
		_anoFim = "";
		
		_excepcaoHorario = false;
	}

	public void setForm(ArrayList listaSiglas, String sigla) {
		setListaSiglas(listaSiglas);

		if (sigla != null) {
			setSigla(sigla);
		} else {
			setSigla((String) _listaSiglas.get(0));
		}

		Calendar agora = Calendar.getInstance();
		setDiaInicio((new Integer(agora.get(Calendar.DAY_OF_MONTH))).toString());
		setMesInicio((new Integer(agora.get(Calendar.MONTH) + 1)).toString());
		setAnoInicio((new Integer(agora.get(Calendar.YEAR))).toString());
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();

		if (mapping.getPath().equals(new String("/associarHorarioTipo"))) {
			if (request.getParameter("numMecanografico") != null) {
				if ((request.getParameter("numMecanografico")).length() < 1) {
					errors.add("numMecanografico", new ActionError("error.numero.obrigatorio"));
				} else {
					try {
						int numMecanografico = (new Integer(request.getParameter("numMecanografico"))).intValue();
					} catch (java.lang.NumberFormatException e) {
						errors.add("numMecanografico", new ActionError("error.numero.naoInteiro"));
						return errors;
					}
				}
			}
		} else if (mapping.getPath().equals(new String("/adicionarHorarioTipoRotacao"))) {
			if ((request.getParameter("numDias") != null) && (request.getParameter("posicao") != null)) {
				if (((request.getParameter("numDias")).length() < 1) || ((request.getParameter("posicao")).length() < 1)) {
					errors.add("numero", new ActionError("error.numero.obrigatorio"));
				} else {
					int numDias = 0;
					int posicao = 0;
					try {
						numDias = (new Integer(getNumDias())).intValue();
						posicao = (new Integer(getPosicao())).intValue();
					} catch (java.lang.NumberFormatException e) {
						errors.add("numero", new ActionError("error.numero.naoInteiro"));
						return errors;
					}

					// testa a posi��o e a dura��o da nova rota��o 
					ArrayList rotacaoHorario = null;
					if ((rotacaoHorario = (ArrayList) session.getAttribute("rotacaoHorario")) != null) {
						ListIterator iterador = rotacaoHorario.listIterator();
						Horario horario = null;
						int posMaxPrimeiroHorario = 0;

						if (iterador.hasNext()) {
							horario = (Horario) iterador.next();
							if ((posicao >= horario.getPosicao()) && (posicao <= (horario.getPosicao() + horario.getNumDias() - 1))) {
								errors.add("numero", new ActionError("error.rotacaoHorario"));
								return errors;
							}

							if ((posicao >= posMaxPrimeiroHorario) && (posicao < horario.getPosicao())) {
								if (numDias >= (horario.getPosicao() - posMaxPrimeiroHorario)) {
									errors.add("rotacao", new ActionError("error.rotacaoHorario"));
									return errors;
								} else {
									/* dia de descanso entre a rotacao dos hor�rios */
									if (((posicao - posMaxPrimeiroHorario) == 1) || (((horario.getPosicao()) - (posicao + numDias - 1)) == 1)) {
										errors.add("dia de descanso", new ActionError("error.rotacaoHorario.diaDescanso"));
										return errors;
									}
								}
							}

							posMaxPrimeiroHorario = horario.getPosicao() + horario.getNumDias() - 1;
						}

						while (iterador.hasNext()) {
							horario = (Horario) iterador.next();
							if ((posicao >= horario.getPosicao()) && (posicao <= (horario.getPosicao() + horario.getNumDias() - 1))) {
								errors.add("rotacao", new ActionError("error.rotacaoHorario"));
								return errors;
							}

							if ((posicao > posMaxPrimeiroHorario) && (posicao < horario.getPosicao())) {
								if (numDias >= (horario.getPosicao() - posMaxPrimeiroHorario)) {
									errors.add("rotacao", new ActionError("error.rotacaoHorario"));
									return errors;
								} else {
									/* dia de descanso entre a rotacao dos hor�rios */
									if (((posicao - posMaxPrimeiroHorario) == 1) || (((horario.getPosicao()) - (posicao + numDias - 1)) == 1)) {
										errors.add("dia de descanso", new ActionError("error.rotacaoHorario.diaDescanso"));
										return errors;
									}
								}
							}
							posMaxPrimeiroHorario = horario.getPosicao() + horario.getNumDias() - 1;
						}

						if (posicao > posMaxPrimeiroHorario) {
							// dia de descanso entre a rotacao dos hor�rios 
							if ((posicao - posMaxPrimeiroHorario) == 1) {
								errors.add("dia de descanso", new ActionError("error.rotacaoHorario.diaDescanso"));
								return errors;
							}
						}
					}
				}
			}
		}

		ArrayList listaSiglas = (ArrayList) session.getAttribute("listaSiglas");
		ArrayList listaHorarios = (ArrayList) session.getAttribute("listaHorarios");
		ArrayList listaRegimesHorarios = (ArrayList) session.getAttribute("listaRegimesHorarios");

		int indiceLista = 0;
		if (listaSiglas != null) {
			indiceLista = listaSiglas.indexOf(getSigla());
		} 
		
		HorarioTipo horarioTipo = null;
		if (listaHorarios != null) {
			horarioTipo = (HorarioTipo) listaHorarios.get(indiceLista);
			// validacao dos outros campos
			IStrategyHorarios horario = SuporteStrategyHorarios.getInstance().callStrategy(horarioTipo.getModalidade());
			errors = horario.validateAssociarHorarioTipo(this, horarioTipo, (ArrayList) listaRegimesHorarios.get(indiceLista));
		} 
		return errors;
	} /* validate */
}