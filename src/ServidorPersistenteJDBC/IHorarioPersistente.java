package ServidorPersistenteJDBC;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import Dominio.Horario;

/**
 *
 * @author  Fernanda Quit�rio e Tania Pous�o
 */
public interface IHorarioPersistente {
	public boolean alterarHorario(Horario horario);
	public boolean alterarExcepcaoHorario(Horario horario);
	public boolean alterarDataFimHorario(Date dataFim, int numeroMecanografico);
	
	public boolean apagarHorario(int codigoInterno);
	public boolean apagarHorarioExcepcao(int codigoInterno);
	public boolean apagarTodosHorarios();
	
	public boolean associarHorarioRegime(int chaveHorario, int chaveRegime);
	public boolean associarExcepcaoHorarioRegime(int chaveHorario, int regime);
	
	public int calcularIndiceRotacao(int numDiasRotacao, Date dataInicioHorario, Date dataConsulta);
	public int calcularIndiceDescanso(int indiceRotacao);
	public int calculaIndiceReferenciaDescanso(int indiceRotacao, int indiceDescanso, int numDiasRotacao);
	
	public boolean desassociarHorarioRegime(int codigoInternoHorario, int codigoInternoRegime);
	public boolean desassociarHorarioExcepcaoRegime(int codigoInternoHorario, int codigoInternoRegime);
	public boolean escreverExcepcaoHorario(Horario horario);
	public boolean escreverHorario(Horario horario);
	public boolean escreverRotacoes(ArrayList rotacaoHorario);
	
	public Horario existeExcepcaoHorario(Horario horario);
	public Horario existeHorario(Horario horario);
	
	
	public Horario lerExcepcaoHorario(int codigoInterno);
	public Horario lerExcepcaoHorarioPorNumMecanografico(int numMecanografico, Timestamp dataConsulta);
	public ArrayList lerExcepcoesHorarioPorNumMecanografico(int numMecanografico);	
	public ArrayList lerExcepcoesHorarioPorNumMecanografico(int numMecanografico, Date dataInicio, Date dataFim);
	
	public ArrayList lerHistoricoHorarioPorNumMecanografico(int numMecanografico);
	
	public Horario lerHorario(int codigoInterno);
	public Horario lerHorario(int codigoInterno, Timestamp dataConsulta);
	
	public ArrayList lerHorarioActualPorNumMecanografico(int numMecanografico);
	public Horario lerHorarioPorNumFuncionario(int numMecanografico, Timestamp dataConsulta);
	public ArrayList lerHorariosPorNumMecanografico(int numMecanografico, Date dataInicio, Date dataFim);
	public Horario lerHorarioPorFuncionario(int chaveFuncNaoDocente);	
	public ArrayList lerHorariosSemFimValidade(int numMecanografico);
	
	public ArrayList lerRegimes(int chaveHorario);
	public ArrayList lerRegimesHorarioExcepcao(int chaveHorario);
	public HashMap lerRegimesRotacoes(ArrayList rotacaoHorario);

	public ArrayList lerRotacoesPorNumMecanografico(int numMecanografico);
	
	public ArrayList lerTodosHorariosExcepcao(Date dataInicio, Date dataFim);
	public ArrayList lerTodosHorarios(Date dataInicio, Date dataFim);
	public ArrayList lerTodosHorariosExcepcaoComRegime(int chaveRegime, ArrayList listaHorariosTipoComRegime, Date dataInicio, Date dataFim);
	public ArrayList lerTodosHorariosComRegime(int chaveRegime, ArrayList listaHorariosTipoComRegime, Date dataInicio, Date dataFim);
	  
	public int ultimoCodigoInterno();
	public int ultimoCodigoInternoExcepcaoHorario();
}