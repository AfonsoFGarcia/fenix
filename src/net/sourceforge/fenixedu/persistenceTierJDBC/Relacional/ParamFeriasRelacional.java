package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ParamFerias;
import net.sourceforge.fenixedu.persistenceTierJDBC.IParamFeriasPersistente;

/**
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public class ParamFeriasRelacional implements IParamFeriasPersistente {

    public boolean alterarParamFerias(ParamFerias tipoFerias) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("UPDATE ass_PARAM_FERIAS SET "
                    + "codigoInterno = ? , " + "sigla = ? , " + "designacao = ? "
                    + "WHERE codigoInterno = ? ");

            sql.setInt(1, tipoFerias.getCodigoInterno());
            sql.setString(2, tipoFerias.getSigla());
            sql.setString(3, tipoFerias.getDesignacao());
            sql.setInt(1, tipoFerias.getCodigoInterno());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("ParamFeriasRelacional.alterarParamFerias: " + e.toString());
        }
        return resultado;
    } /* alterarParamFerias */

    public boolean apagarParamFerias(int codigoInterno) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("DELETE FROM ass_PARAM_FERIAS WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("ParamFeriasRelacional.apagarParamFerias: " + e.toString());
        }
        return resultado;
    } /* apagarParamFerias */

    public boolean apagarParamFeriasPorSigla(String sigla) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("DELETE FROM ass_PARAM_FERIAS WHERE sigla = ?");

            sql.setString(1, sigla);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("ParamFeriasRelacional.apagarParamFeriasPorSigla: " + e.toString());
        }
        return resultado;
    } /* apagarParamFeriasPorSigla */

    public boolean escreverParamFerias(ParamFerias tipoFerias) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("INSERT INTO ass_PARAM_FERIAS VALUES (?, ?, ?)");

            sql.setInt(1, tipoFerias.getCodigoInterno());
            sql.setString(2, tipoFerias.getSigla());
            sql.setString(3, tipoFerias.getDesignacao());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("ParamFeriasRelacional.escreverParamFerias: " + e.toString());
        }
        return resultado;
    } /* escreverParamFerias */

    public ParamFerias lerParamFerias(int codigoInterno) {
        ParamFerias tipoFerias = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_PARAM_FERIAS WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                tipoFerias = new ParamFerias(resultado.getInt("codigoInterno"), resultado
                        .getString("sigla"), resultado.getString("designacao"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("ParamFeriasRelacional.lerParamFerias" + e.toString());
        }
        return tipoFerias;
    } /* lerParamFerias */

    public ParamFerias lerParamFeriasPorSigla(String sigla) {
        ParamFerias tipoFerias = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_PARAM_FERIAS WHERE sigla = ?");

            sql.setString(1, sigla);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                tipoFerias = new ParamFerias(resultado.getInt("codigoInterno"), resultado
                        .getString("sigla"), resultado.getString("designacao"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("ParamFeriasRelacional.lerParamFeriasPorSigla" + e.toString());
        }
        return tipoFerias;
    } /* lerParamFeriasPorSigla */

    public List lerTodosParamFerias() {
        List listaTiposFerias = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_PARAM_FERIAS");

            ResultSet resultado = sql.executeQuery();
            listaTiposFerias = new ArrayList();

            while (resultado.next()) {
                listaTiposFerias.add(new ParamFerias(resultado.getInt("codigoInterno"), resultado
                        .getString("sigla"), resultado.getString("designacao")));
            }
        } catch (Exception e) {
            System.out.println("ParamFeriasRelacional.lerTodosParamFerias: " + e.toString());
        }
        return listaTiposFerias;
    } /* lerTodosParamFerias */
}