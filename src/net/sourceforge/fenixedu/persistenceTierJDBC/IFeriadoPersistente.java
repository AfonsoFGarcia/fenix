package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Feriado;
import net.sourceforge.fenixedu.domain.Horario;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IFeriadoPersistente {
    public boolean alterarFeriado(Feriado feriado);

    public boolean apagarFeriado(int codigoInterno);

    public boolean calendarioFeriado(String calendario, Date dia);

    public boolean diaFeriado(Date dia);

    public boolean escreverFeriado(Feriado feriado);

    public boolean escreverFeriados(List listaFeriados);

    public Horario horarioFeriado(Date dia);

    public Horario horarioFeriado(int numMecanografico, Date dia);

    public Feriado lerFeriado(int codigoInterno);

    public Feriado lerFeriado(Date dia);

    public Feriado lerFeriado(String string, Date date);

    public List lerTodosCalendarios();
}