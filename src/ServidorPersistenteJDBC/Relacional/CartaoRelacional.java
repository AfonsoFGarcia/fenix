package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;

import Dominio.Cartao;
import ServidorPersistenteJDBC.ICartaoPersistente;

/**
 *
 * @author  Fernanda Quit�rio e Tania Pous�o
 */
public class CartaoRelacional implements ICartaoPersistente {

	public boolean alterarCartao(Cartao cartao) {
		//ORACLE: metodo escreverCartao acedia � BD oracle
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_CARTAO SET "
						+ "codigoInterno = ? , "
						+ "numCartao = ? , "
						+ "chaveFuncNaoDocente = ? , "
						+ "dataInicio = ? , "
						+ "dataFim = ? , "
						+ "quem = ? , "
						+ "quando = ? , "
						+ "estado = ? "
						+ "WHERE codigoInterno = ? ");

			sql.setInt(1, cartao.getCodigoInterno());
			sql.setInt(2, cartao.getNumCartao());
			sql.setInt(3, cartao.getChaveFuncNaoDocente());
			if (cartao.getDataInicio() != null) {
				sql.setTimestamp(4, new Timestamp((cartao.getDataInicio()).getTime()));
			} else {
				sql.setTimestamp(4, null);
			}
			if (cartao.getDataFim() != null) {
				sql.setTimestamp(5, new Timestamp((cartao.getDataFim()).getTime()));
			} else {
				sql.setTimestamp(5, null);
			}
			sql.setInt(6, cartao.getQuem());
			if (cartao.getQuando() != null) {
				sql.setTimestamp(7, new java.sql.Timestamp((cartao.getQuando()).getTime()));
			} else {
				sql.setTimestamp(7, null);
			}
			sql.setString(8, cartao.getEstado());
			sql.setInt(9, cartao.getCodigoInterno());

			sql.executeUpdate();
			sql.close();

			resultado = true;
		} catch (Exception e) {
			System.out.println("CartaoRelacional.alterarCartao: " + e.toString());
		} finally {
			return resultado;
		}
	} /* alterarCartao */

	public boolean apagarCartao(int codigoInterno) {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_CARTAO " + "WHERE codigoInterno = ?");

			sql.setInt(1, codigoInterno);

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("CartaoRelacional.apagarCartao: " + e.toString());
		} finally {
			return resultado;
		}
	} /* apagarCartao */

	public boolean apagarCartaoPorNumero(int numCartao) {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_CARTAO " + "WHERE numCartao = ?");

			sql.setInt(1, numCartao);

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("CartaoRelacional.apagarCartaoPorNumero: " + e.toString());
		} finally {
			return resultado;
		}
	} /* apagarCartaoPorNumero */

	public boolean apagarTodosCartoes() {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_CARTAO");
			sql.executeUpdate();
			sql.close();
			
			/*sql = UtilRelacional.prepararComando("ALTER TABLE ass_CARTAO auto_increment=1");
			sql.execute();
			sql.close();*/
			resultado = true;
		} catch (Exception e) {
			System.out.println("CartaoRelacional.apagarTodosCartoes: " + e.toString());
		} finally {
			return resultado;
		}
	} /* apagarTodosCartoes */

	public Cartao cartaoAtribuido(int numCartao) {
		Cartao cartao = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_CARTAO " + "WHERE numCartao = ? AND estado = \"atribuido\"");

			sql.setInt(1, numCartao);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				Timestamp dataFim = null;
				if (resultado.getString("dataFim") != null) {
					dataFim = Timestamp.valueOf(resultado.getString("dataFim"));
				}

				cartao =
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.cartaoAtribuido: " + e.toString());
		} finally {
			return cartao;
		}
	} /* cartaoAtribuido */

	public ArrayList cartaoUtilizado(int numCartao, Timestamp dataInicio, Timestamp dataFim) {
		ArrayList listaCartoes = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_CARTAO "
						+ "WHERE numCartao = ? AND ((dataInicio BETWEEN ? AND ?) "
						+ "OR (dataFim BETWEEN ? AND ?) OR (dataInicio <= ? AND dataFim >= ?))");

			Timestamp dataInicioQuery = new Timestamp(dataInicio.getTime());
			Timestamp dataFimQuery = new Timestamp(dataFim.getTime());

			sql.setInt(1, numCartao);
			sql.setTimestamp(2, dataInicioQuery);
			sql.setTimestamp(3, dataFimQuery);
			sql.setTimestamp(4, dataInicioQuery);
			sql.setTimestamp(5, dataFimQuery);
			sql.setTimestamp(6, dataInicioQuery);
			sql.setTimestamp(7, dataFimQuery);

			ResultSet resultado = sql.executeQuery();

			listaCartoes = new ArrayList();
			Timestamp dataFimCartao = null;
			while (resultado.next()) {
				if (resultado.getString("dataFim") != null) {
					dataFimCartao = Timestamp.valueOf(resultado.getString("dataFim"));
				}

				listaCartoes.add(
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						dataFimCartao,
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.cartaoUtilizado: " + e.toString());
			return null;
		} finally {
			return listaCartoes;
		}
	} /* cartaoUtilizado */

	public boolean escreverCartao(Cartao cartao) {
		//	ORACLE: metodo escreverCartao acedia � BD oracle
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO ass_CARTAO " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

			sql.setInt(1, cartao.getCodigoInterno());
			sql.setInt(2, cartao.getNumCartao());
			sql.setInt(3, cartao.getChaveFuncNaoDocente());
			if (cartao.getDataInicio() != null) {
				sql.setTimestamp(4, new Timestamp((cartao.getDataInicio()).getTime()));
			} else {
				sql.setTimestamp(4, null);
			}
			if (cartao.getDataFim() != null) {
				sql.setTimestamp(5, new Timestamp((cartao.getDataFim()).getTime()));
			} else {
				sql.setTimestamp(5, null);
			}
			sql.setInt(6, cartao.getQuem());
			if (cartao.getQuando() != null) {
				sql.setTimestamp(7, new Timestamp((cartao.getQuando()).getTime()));
			} else {
				sql.setTimestamp(7, null);
			}
			sql.setString(8, cartao.getEstado());

			sql.executeUpdate();
			sql.close();

			resultado = true;
		} catch (Exception e) {
			System.out.println("CartaoRelacional.escreverCartao: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreverCartao */

	public boolean escreverCartoes(ArrayList listaCartoes) {
		boolean resultado = false;

		ListIterator iterador = listaCartoes.listIterator();
		Cartao cartao = null;

		try {
			while (iterador.hasNext()) {
				cartao = (Cartao) iterador.next();

				PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO ass_CARTAO " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

				sql.setInt(1, cartao.getCodigoInterno());
				sql.setInt(2, cartao.getNumCartao());
				sql.setInt(3, cartao.getChaveFuncNaoDocente());
				if (cartao.getDataInicio() != null) {
					sql.setTimestamp(4, new Timestamp((cartao.getDataInicio()).getTime()));
				} else {
					sql.setTimestamp(4, null);
				}
				if (cartao.getDataFim() != null) {
					sql.setTimestamp(5, new Timestamp((cartao.getDataFim()).getTime()));
				} else {
					sql.setTimestamp(5, null);
				}
				sql.setInt(6, cartao.getQuem());
				if (cartao.getQuando() != null) {
					sql.setTimestamp(7, new Timestamp((cartao.getQuando()).getTime()));
				} else {
					sql.setTimestamp(7, null);
				}
				sql.setString(8, cartao.getEstado());

				sql.executeUpdate();
				sql.close();
				resultado = true;
			}
		} catch (Exception e) {
			System.out.println("CartaoRelacional.escreverCartoes: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreverCartoes */

	public boolean existeCartao(int numCartao) {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_CARTAO " + "WHERE numCartao = ?");

			sql.setInt(1, numCartao);

			ResultSet resultadoQuery = sql.executeQuery();
			if (resultadoQuery.next()) {
				resultado = true;
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.existeCartao: " + e.toString());
		} finally {
			return resultado;
		}
	} /* existeCartao */

	public Cartao lerCartao(int codigoInterno) {
		Cartao cartao = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_CARTAO " + "WHERE codigoInterno = ?");

			sql.setInt(1, codigoInterno);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				Timestamp dataFim = null;
				if (resultado.getString("dataFim") != null) {
					dataFim = Timestamp.valueOf(resultado.getString("dataFim"));
				}

				cartao =
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartao: " + e.toString());
		} finally {
			return cartao;
		}
	} /* lerCartao */

	public Cartao lerCartaoActualFuncionario(int chaveFuncNaoDocente) {
		Cartao cartao = null;

		try {
			Calendar agora = Calendar.getInstance();
			Timestamp dataAgora = new Timestamp(agora.getTimeInMillis());

			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_CARTAO " + "WHERE chaveFuncNaoDocente = ? AND dataFim >= ?");

			sql.setInt(1, chaveFuncNaoDocente);
			sql.setTimestamp(2, dataAgora);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				Timestamp dataFim = null;
				if (resultado.getString("dataFim") != null) {
					dataFim = Timestamp.valueOf(resultado.getString("dataFim"));
				}

				cartao =
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartaoActualFuncionario: " + e.toString());
		} finally {
			return cartao;
		}
	} /* lerCartaoActualFuncionario */

	public Cartao lerCartaoLivre() {
		Cartao cartao = null;
		ArrayList listaCartoes = null;

		try {

			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT numCartao FROM ass_CARTAO " + "WHERE estado = \"atribuido\" OR estado = \"invalido\"");
			ResultSet resultado = sql.executeQuery();

			listaCartoes = new ArrayList();
			String query = new String("SELECT * FROM cartao WHERE numCartao NOT IN (");
			while (resultado.next()) {
				if (!resultado.isLast()) {
					query = query.concat("?,");
				} else {
					query = query.concat("?)");
				}
				listaCartoes.add(new Integer(resultado.getInt("numCartao")));
			}
			sql.close();

			sql = UtilRelacional.prepararComando(query);

			ListIterator iterador = listaCartoes.listIterator();
			int numCartao = 0;
			int contador = 1;
			while (iterador.hasNext()) {
				numCartao = ((Integer) iterador.next()).intValue();

				sql.setInt(contador, numCartao);
				contador++;
			}

			resultado = sql.executeQuery();

			if (resultado.next()) {
				Timestamp dataFim = null;
				if (resultado.getString("dataFim") != null) {
					dataFim = Timestamp.valueOf(resultado.getString("dataFim"));
				}

				cartao =
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartaoLivre: " + e.toString());
		} finally {
			return cartao;
		}
	} /* lerCartaoLivre */

	public Cartao lerCartaoPorNumero(int numCartao) {
		Cartao cartao = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_CARTAO " + "WHERE numCartao = ?");

			sql.setInt(1, numCartao);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				Timestamp dataFim = null;
				if (resultado.getString("dataFim") != null) {
					dataFim = Timestamp.valueOf(resultado.getString("dataFim"));
				}

				cartao =
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartaoPorNumero: " + e.toString());
		} finally {
			return cartao;
		}
	} /* lerCartaoPorNumero */

	public Cartao lerCartaoPorNumero(int numCartao, Timestamp dataMarcacao) {
		Cartao cartao = null;

		try {

			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_CARTAO " + "WHERE numCartao = ? AND (dataInicio <= ? AND dataFim >= ?)");

			sql.setInt(1, numCartao);
			sql.setTimestamp(2, new Timestamp(dataMarcacao.getTime()));
			sql.setTimestamp(3, new Timestamp(dataMarcacao.getTime()));

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				Timestamp dataFim = null;
				if (resultado.getString("dataFim") != null) {
					dataFim = Timestamp.valueOf(resultado.getString("dataFim"));
				}

				cartao =
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartaoPorNumero: " + e.toString());
		} finally {
			return cartao;
		}
	} /* lerCartaoPorNumero */

	public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncNaoDocente) {
		Cartao cartao = null;

		try {
			Calendar agora = Calendar.getInstance();
			Timestamp dataAgora = new Timestamp(agora.getTimeInMillis());

			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_CARTAO " + "WHERE chaveFuncNaoDocente = ? AND dataFim >= ? AND numCartao >= 900000");

			sql.setInt(1, chaveFuncNaoDocente);
			sql.setTimestamp(2, dataAgora);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				cartao =
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						Timestamp.valueOf(resultado.getString("dataFim")),
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartaoSubstitutoFuncionario: " + e.toString());
		} finally {
			return cartao;
		}
	} /* lerCartaoSubstitutoFuncionario */

	public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncNaoDocente, Timestamp data) {
		Cartao cartao = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_CARTAO WHERE chaveFuncNaoDocente = ? AND ? BETWEEN dataInicio AND dataFim AND numCartao >= 900000");

			sql.setInt(1, chaveFuncNaoDocente);
			sql.setTimestamp(2, data);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				cartao =
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						Timestamp.valueOf(resultado.getString("dataFim")),
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartaoSubstitutoFuncionario: " + e.toString());
		} finally {
			return cartao;
		}
	} /* lerCartaoSubstitutoFuncionario */
	
	public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncNaoDocente, Timestamp dataInicio, Timestamp dataFim){
		Cartao cartao = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_CARTAO "
						+ "WHERE chaveFuncNaoDocente = ? AND ((dataInicio BETWEEN ? AND ?) "
						+ "OR (dataFim BETWEEN ? AND ?) OR (dataInicio <= ? AND dataFim >= ?)) AND numCartao >= 900000");

			Timestamp dataInicioQuery = new Timestamp(dataInicio.getTime());
			Timestamp dataFimQuery = new Timestamp(dataFim.getTime());

			sql.setInt(1, chaveFuncNaoDocente);
			sql.setTimestamp(2, dataInicioQuery);
			sql.setTimestamp(3, dataFimQuery);
			sql.setTimestamp(4, dataInicioQuery);
			sql.setTimestamp(5, dataFimQuery);
			sql.setTimestamp(6, dataInicioQuery);
			sql.setTimestamp(7, dataFimQuery);

			ResultSet resultado = sql.executeQuery();

			Timestamp dataFimCartao = null;
			while (resultado.next()) {
				if (resultado.getString("dataFim") != null) {
					dataFimCartao = Timestamp.valueOf(resultado.getString("dataFim"));
				}

				cartao = 
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						dataFimCartao,
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartaoSubstitutoFuncionario: " + e.toString());
			return null;
		} finally {
			return cartao;
		}
}
	public ArrayList lerCartoesFuncionario(int chaveFuncNaoDocente) {
		ArrayList listaCartoes = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_CARTAO " + "WHERE chaveFuncNaoDocente = ?");

			sql.setInt(1, chaveFuncNaoDocente);

			ResultSet resultado = sql.executeQuery();
			listaCartoes = new ArrayList();
			while (resultado.next()) {
				listaCartoes.add(new Integer(resultado.getInt("numCartao")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartoesFuncionario: " + e.toString());
			return null;
		} finally {
			return listaCartoes;
		}
	} /* lerCartoesFuncionario */

	public ArrayList lerCartoesFuncionarioComValidade(int chaveFuncNaoDocente, Timestamp dataInicio, Timestamp dataFim) {
		ArrayList listaCartoes = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_CARTAO "
						+ "WHERE chaveFuncNaoDocente = ? AND ((dataInicio BETWEEN ? AND ?) OR (dataFim BETWEEN ? AND ?) "
						+ " OR (dataInicio <= ? AND dataFim >= ?))");

			sql.setInt(1, chaveFuncNaoDocente);
			sql.setTimestamp(2, new Timestamp(dataInicio.getTime()));
			sql.setTimestamp(3, new Timestamp(dataFim.getTime()));
			sql.setTimestamp(4, new Timestamp(dataInicio.getTime()));
			sql.setTimestamp(5, new Timestamp(dataFim.getTime()));
			sql.setTimestamp(6, new Timestamp(dataInicio.getTime()));
			sql.setTimestamp(7, new Timestamp(dataFim.getTime()));

			ResultSet resultado = sql.executeQuery();
			listaCartoes = new ArrayList();
			while (resultado.next()) {
				listaCartoes.add((new Integer(resultado.getInt("numCartao"))));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerCartoesFuncionarioComValidade: " + e.toString());
			return null;
		} finally {
			return listaCartoes;
		}
	} /* lerCartoesFuncionarioComValidade */

	public ArrayList lerHistorialCartao(int numCartao) {
		ArrayList listaCartoes = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_CARTAO " + "WHERE numCartao = ?");

			sql.setInt(1, numCartao);

			ResultSet resultado = sql.executeQuery();
			listaCartoes = new ArrayList();
			while (resultado.next()) {
				Timestamp dataFim = null;
				if (resultado.getString("dataFim") != null) {
					dataFim = Timestamp.valueOf(resultado.getString("dataFim"));
				}

				listaCartoes.add(
					new Cartao(
						resultado.getInt("codigoInterno"),
						resultado.getInt("numCartao"),
						resultado.getInt("chaveFuncNaoDocente"),
						Timestamp.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("quem"),
						Timestamp.valueOf(resultado.getString("quando")),
						resultado.getString("estado")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerHistorialCartao: " + e.toString());
			return null;
		} finally {
			return listaCartoes;
		}
	} /* lerHistorialCartao */

	public ArrayList lerTodosCartoes() {
		ArrayList listaCartoes = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT DISTINCT numCartao FROM ass_CARTAO "
						+ "WHERE (numcartao>=900000 AND estado<>\"invalido\") OR numCartao<900000 ORDER BY numCartao");

			ResultSet resultado = sql.executeQuery();
			listaCartoes = new ArrayList();
			while (resultado.next()) {
				listaCartoes.add(new Integer(resultado.getInt("numCartao")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerTodosCartoes: " + e.toString());
			return null;
		} finally {
			return listaCartoes;
		}
	} /* lerTodosCartoes */

	public ArrayList lerTodosCartoesSubstitutos() {
		ArrayList listaCartoesSubstitutos = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT DISTINCT numCartao FROM ass_CARTAO "
						+ "WHERE (numCartao >= 900000) AND (estado <> \"invalido\") ORDER BY numCartao");

			ResultSet resultado = sql.executeQuery();
			listaCartoesSubstitutos = new ArrayList();
			while (resultado.next()) {
				listaCartoesSubstitutos.add(new Integer(resultado.getInt("numCartao")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerTodosCartoesSubstitutos: " + e.toString());
			return null;
		} finally {
			return listaCartoesSubstitutos;
		}
	} /* lerTodosCartoesSubstitutos */

	public ArrayList lerTodosCartoesSubstitutosAtribuidos() {
		ArrayList listaCartoes = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT DISTINCT numCartao FROM ass_CARTAO " + "WHERE numCartao>=900000 AND estado=\"atribuido\" ORDER BY numCartao");

			ResultSet resultado = sql.executeQuery();
			listaCartoes = new ArrayList();
			while (resultado.next()) {
				listaCartoes.add(new Integer(resultado.getInt("numCartao")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerTodosCartoesSubstitutosAtribuidos: " + e.toString());
			return null;
		} finally {
			return listaCartoes;
		}
	} /* lerTodosCartoesSubstitutosAtribuidos */

	public Cartao lerUltimaUtilizacaoCartao(int numCartao, Timestamp dataMarcacao) {
		Cartao cartao = null;
		Timestamp dataUltimaUtilizacao = null;
		try {

			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT MAX(dataFim) FROM ass_CARTAO " + "WHERE numCartao=? and dataFim<=?");

			sql.setInt(1, numCartao);
			sql.setTimestamp(2, new Timestamp(dataMarcacao.getTime()));

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				dataUltimaUtilizacao = Timestamp.valueOf(resultado.getString("MAX(dataFim)"));
				sql.close();

				sql = UtilRelacional.prepararComando("SELECT * FROM ass_CARTAO " + "WHERE numCartao=? AND dataFim=?");

				sql.setInt(1, numCartao);
				sql.setTimestamp(2, dataUltimaUtilizacao);
				resultado = sql.executeQuery();

				if (resultado.next()) {
					Timestamp dataFim = null;
					if (resultado.getString("dataFim") != null) {
						dataFim = Timestamp.valueOf(resultado.getString("dataFim"));
					}

					cartao =
						new Cartao(
							resultado.getInt("codigoInterno"),
							resultado.getInt("numCartao"),
							resultado.getInt("chaveFuncNaoDocente"),
							Timestamp.valueOf(resultado.getString("dataInicio")),
							dataFim,
							resultado.getInt("quem"),
							Timestamp.valueOf(resultado.getString("quando")),
							resultado.getString("estado"));
				}
				sql.close();
			} else {
				sql.close();
			}
		} catch (Exception e) {
			System.out.println("CartaoRelacional.lerUltimaUtilizacaoCartao: " + e.toString());
		} finally {
			return cartao;
		}
	} /* lerUltimaUtilizacaoCartao */

	public int ultimoCodigoInterno() {
		int ultimo = 0;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT MAX(codigoInterno) FROM ass_CARTAO");

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				ultimo = resultado.getInt(1);
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("CartaoRelacional.ultimoCodigoInterno: " + e.toString());
		} finally {
			return ultimo;
		}
	} /* ultimoCodigoInterno */
}