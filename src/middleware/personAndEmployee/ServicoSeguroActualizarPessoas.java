package middleware.personAndEmployee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import middleware.RoleFunctions;
import middleware.dataClean.personFilter.LimpaNaturalidades;
import middleware.dataClean.personFilter.LimpaOutput;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.Pessoa;
import Dominio.Role;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteFiltroPessoa.DB;
import Util.LeituraFicheiroPessoa;
import Util.RoleType;

/**
 * @author  Ivo Brand�o
 */
public class ServicoSeguroActualizarPessoas {

	private String _ficheiro = null;
	private Collection _listaPessoas = null;
	private Pessoa _pessoa = null;
	private String _delimitador = new String(";");

	/** Construtor */
	public ServicoSeguroActualizarPessoas(String[] args) {
		_ficheiro = args[0];
	}

	/** executa a actualizacao da tabela Pessoa na Base de Dados */
	public static void main(String[] args) throws Exception {
		ServicoSeguroActualizarPessoas servico = new ServicoSeguroActualizarPessoas(args);
		int newPersons = 0;
		int newRoles = 0;

		try {
			servico._listaPessoas = LeituraFicheiroPessoa.lerFicheiro(servico._ficheiro, servico._delimitador);
		} catch (NotExecuteException nee) {
			throw new NotExecuteException(nee.getMessage());
		}

		System.out.println("Converting Persons " + servico._listaPessoas.size() + " ... ");

		if (servico._listaPessoas != null) {
			//open databases
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			broker.beginTransaction();
			DB.initConnection();
			LimpaNaturalidades limpaNaturalidades = new LimpaNaturalidades();

			Iterator iterador = servico._listaPessoas.iterator();
			/* ciclo que percorre a Collection de Pessoas */
			while (iterador.hasNext()) {
				try {
					servico._pessoa = (Pessoa) iterador.next();
					personFilter(limpaNaturalidades, servico._pessoa);

					Criteria criteria = new Criteria();
					Query query = null;

					criteria.addEqualTo("numeroDocumentoIdentificacao", servico._pessoa.getNumeroDocumentoIdentificacao());
					criteria.addEqualTo("tipoDocumentoIdentificacao", servico._pessoa.getTipoDocumentoIdentificacao());
					query = new QueryByCriteria(Pessoa.class, criteria);
					List result = (List) broker.getCollectionByQuery(query);

					IPessoa person2Write = null;
					// A Pessoa nao Existe
					if (result.size() == 0) {
						person2Write = (IPessoa) servico._pessoa;
						newPersons++;
					} else {
						// A Pessoa Existe
						person2Write = (IPessoa) result.get(0);
						updatePerson((Pessoa) person2Write, (Pessoa) servico._pessoa);
					}

					IPersonRole personRole = RoleFunctions.readPersonRole((IPessoa) person2Write, RoleType.PERSON, broker);
					if (personRole == null) {
						criteria = new Criteria();
						query = null;
						criteria.addEqualTo("roleType", RoleType.PERSON);
						query = new QueryByCriteria(Role.class, criteria);

						result = (List) broker.getCollectionByQuery(query);

						if (result.size() == 0) {
							throw new Exception("Role Desconhecido !!!");
						} else {
							if (person2Write.getPersonRoles() == null) {
								person2Write.setPersonRoles(new ArrayList());
							}
							person2Write.getPersonRoles().add((Role) result.get(0));
							newRoles++;
						}

					}
					if (person2Write.getEstadoCivil().getEstadoCivil().intValue() > 7) {
						System.out.println("Erro : " + person2Write.getEstadoCivil().getEstadoCivil().intValue());
					}

					broker.store(person2Write);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro a converter a pessoa " + servico._pessoa + "\n");
					//broker.abortTransaction();
					//throw new Exception("Erro a converter a pessoa " + servico._pessoa + "\n" + e);
					continue;
				}
			}

			//close databases
			DB.closeConnection();
			broker.commitTransaction();
		}
		System.out.println("New persons added : " + newPersons);
		System.out.println("New Roles added : " + newRoles);
		System.out.println("  Done !");
	}

	private static void personFilter(LimpaNaturalidades limpaNaturalidades, Pessoa pessoa) {
		try {
			//locais da naturalidade
			LimpaOutput limpaOutput =
				limpaNaturalidades.limpaNats(
					pessoa.getUsername(),
					pessoa.getNacionalidade(),
					pessoa.getDistritoNaturalidade(),
					pessoa.getConcelhoNaturalidade(),
					pessoa.getFreguesiaNaturalidade());
			if (limpaOutput.getNomeDistrito() != null) {
				pessoa.setDistritoNaturalidade(StringUtils.capitaliseAllWords(limpaOutput.getNomeDistrito()));
			}
			if (limpaOutput.getNomeConcelho() != null) {
				pessoa.setConcelhoNaturalidade(StringUtils.capitaliseAllWords(limpaOutput.getNomeConcelho()));
			}
			if (limpaOutput.getNomeFreguesia() != null) {
				pessoa.setFreguesiaNaturalidade(StringUtils.capitaliseAllWords(limpaOutput.getNomeFreguesia()));
			}
			
			//locais da Morada
			limpaOutput =
				limpaNaturalidades.limpaNats(
					pessoa.getUsername(),
					pessoa.getNacionalidade(),
					pessoa.getDistritoMorada(),
					pessoa.getConcelhoMorada(),
					pessoa.getFreguesiaMorada());
			if (limpaOutput.getNomeDistrito() != null) {
				pessoa.setDistritoMorada(StringUtils.capitaliseAllWords(limpaOutput.getNomeDistrito()));
			}
			if (limpaOutput.getNomeConcelho() != null) {
				pessoa.setConcelhoMorada(StringUtils.capitaliseAllWords(limpaOutput.getNomeConcelho()));
			}
			if (limpaOutput.getNomeFreguesia() != null) {
				pessoa.setFreguesiaMorada(StringUtils.capitaliseAllWords(limpaOutput.getNomeFreguesia()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/** retorna a lista de pessoas */
	public Collection getListaPessoas() {
		return _listaPessoas;
	}

	private static void updatePerson(Pessoa person2Write, Pessoa person2Convert) throws Exception {

		try {
			// Password Backup
			String password = new String(person2Write.getPassword());
			Integer internalCode = new Integer(person2Write.getIdInternal().intValue());
			String username = new String(person2Write.getUsername());
			String mobilePhone = new String(person2Write.getTelemovel());
			String email = new String(person2Write.getEmail());
			String url = new String(person2Write.getEnderecoWeb());

			BeanUtils.copyProperties(person2Write, person2Convert);

			person2Write.setIdInternal(internalCode);
			person2Write.setPassword(password);
			person2Write.setUsername(username);
			person2Write.setEmail(email);
			person2Write.setTelemovel(mobilePhone);
			person2Write.setEnderecoWeb(url);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro a converter a Pessoa " + person2Convert.getNome());
			throw new Exception(e);
		}
	}

}