/*
 * Shift.java
 *
 * Created on 17 de Outubro de 2002, 19:28
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.TipoAula;

public class Shift extends Shift_Base {
    protected Integer ocupation;

    protected Double percentage;

    public Shift() {
    }

    public Shift(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Shift(String nome, TipoAula tipo, Integer lotacao, IExecutionCourse disciplinaExecucao) {
        this.setNome(nome);
        this.setTipo(tipo);
        this.setLotacao(lotacao);
        this.setDisciplinaExecucao(disciplinaExecucao);
        this.setAssociatedLessons(new ArrayList());
        this.setAssociatedClasses(new ArrayList());
    }

    public Integer getOcupation() {
        return ocupation;
    }

    public void setOcupation(Integer integer) {
        ocupation = integer;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String toString() {
        String result = "[TURNO";
        result += ", codigoInterno=" + this.getIdInternal();
        result += ", nome=" + getNome();
        result += ", tipo=" + getTipo();
        result += ", lotacao=" + getLotacao();
        result += ", chaveDisciplinaExecucao=" + getChaveDisciplinaExecucao();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IShift) {
            IShift turno = (IShift) obj;
            resultado = getIdInternal().equals(turno.getIdInternal());
        }
        return resultado;
    }

    public double hours() {
        double hours = 0;
        List lessons = this.getAssociatedLessons();
        for (int i = 0; i < lessons.size(); i++) {
            ILesson lesson = (ILesson) lessons.get(i);
            hours += lesson.hours();
        }
        return hours;
    }

}
