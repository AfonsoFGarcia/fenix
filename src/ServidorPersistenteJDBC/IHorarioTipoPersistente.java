package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.HorarioTipo;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IHorarioTipoPersistente {
    public boolean alterarHorarioTipo(HorarioTipo horarioTipo);

    public boolean apagarHorarioTipo(String sigla);

    public boolean associarHorarioTipoRegime(int chaveHorarioTipo, int chaveRegime);

    public boolean escreverHorarioTipo(HorarioTipo horarioTipo);

    public HorarioTipo lerHorarioTipo(String sigla);

    public HorarioTipo lerHorarioTipo(int codigoInterno);

    public List lerHorariosTipo();

    public List lerHorariosTipoSemTurnos();

    public List lerHorarioTipoRegime(int chaveHorarioTipo);

    public List lerRegimes(int chaveHorario);

    public List lerTodosHorariosTipoComRegime(int chaveRegime);
}

