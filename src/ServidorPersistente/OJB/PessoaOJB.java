/*
 * PessoaOJB.java
 *
 * Created on 15 de Outubro de 2002, 15:16
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;
import org.odmg.QueryException;

import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoDocumentoIdentificacao;

public class PessoaOJB extends ObjectFenixOJB implements IPessoaPersistente {
    
    public PessoaOJB() {}
    
	public void escreverPessoa(IPessoa personToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

			IPessoa personFromDB1 = null;
			IPessoa personFromDB2 = null;

		// If there is nothing to write, simply return.
		if (personToWrite == null)
			return;

		// Read person from database.
		personFromDB1 = this.lerPessoaPorUsername(personToWrite.getUsername());
		personFromDB2 = this.lerPessoaPorNumDocIdETipoDocId(personToWrite.getNumeroDocumentoIdentificacao(), personToWrite.getTipoDocumentoIdentificacao());

		// If person is not in database, then write it.
		if ((personFromDB1 == null) && (personFromDB2 == null))
			super.lockWrite(personToWrite);
			
		// else If the person is mapped to the database, then write any existing changes.
		else if ((personFromDB1 != null) &&
				 (personToWrite instanceof Pessoa) && 
				 (((Pessoa) personFromDB1).getCodigoInterno().equals(((Pessoa) personToWrite).getCodigoInterno())))

					super.lockWrite(personToWrite);
			
		else if ((personFromDB2 != null) &&
				 (personToWrite instanceof Pessoa) && 
				 (((Pessoa) personFromDB2).getCodigoInterno().equals(((Pessoa) personToWrite).getCodigoInterno())))
					super.lockWrite(personToWrite);
			
			
			
		// else Throw an already existing exception
		else
			throw new ExistingPersistentException();
	}
    
    public void apagarPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Pessoa.class.getName();
            oqlQuery += " where numeroDocumentoIdentificacao = $1 and tipoDocumentoIdentificacao = $2";
            query.create(oqlQuery);
            query.bind(numeroDocumentoIdentificacao);
            query.bind(tipoDocumentoIdentificacao.getTipo());
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while(iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public void apagarPessoa(IPessoa pessoa) throws ExcepcaoPersistencia {
        super.delete(pessoa);
    }
    
    public void apagarTodasAsPessoas() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Pessoa.class.getName();
        super.deleteAll(oqlQuery);
    }

    public IPessoa lerPessoaPorUsername(String username) throws ExcepcaoPersistencia {
		IPessoa person = null;

		PersistenceBroker broker = ((HasBroker) odmg.currentTransaction()).getBroker(); 
		//PersistenceBrokerFactory.defaultPersistenceBroker();
		//((HasBroker) tx).getBroker();

		Criteria criteria = new Criteria();
		criteria.addEqualTo("username",username);
		Query queryPB = new QueryByCriteria(Pessoa.class, criteria);
		person = (IPessoa) broker.getObjectByQuery(queryPB);
		return person;
    	
//        try {
//            
//            String oqlQuery = "select all from " + Pessoa.class.getName();
//            oqlQuery += " where username = $1";
//            query.create(oqlQuery);
//            query.bind(username);
//            List result = (List) query.execute();
//            lockRead(result);
//            if(result.size() != 0)
//                pessoa = (IPessoa) result.get(0);
//            return pessoa;
//        } catch(QueryException ex) {
//            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//        }
    }

    public IPessoa lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {
        try {
            IPessoa p = null;
            String oqlQuery = "select all from " + Pessoa.class.getName();
            oqlQuery += " where numeroDocumentoIdentificacao = $1 and tipoDocumentoIdentificacao = $2";
            query.create(oqlQuery);
            query.bind(numeroDocumentoIdentificacao);
            query.bind(tipoDocumentoIdentificacao.getTipo());
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0)
                p = (IPessoa) result.get(0);
            return p;
        } catch(QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public ArrayList lerTodasAsPessoas() throws ExcepcaoPersistencia {
        try {
            ArrayList listap = new ArrayList();
            String oqlQuery = "select all from " + Pessoa.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    listap.add((IPessoa)iterator.next());
            }
            return listap;
        } catch(QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }       
    
    public void alterarPessoa(String numDocId, TipoDocumentoIdentificacao tipoDocId, IPessoa pessoa) throws ExcepcaoPersistencia {
    }
    
}
