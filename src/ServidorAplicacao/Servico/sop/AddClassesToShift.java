/*
 *
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import DataBeans.InfoShift;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class AddClassesToShift implements IServico {

    private static AddClassesToShift _servico = new AddClassesToShift();

    /**
     * The singleton access method of this class.
     */
    public static AddClassesToShift getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private AddClassesToShift() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "AddClassesToShift";
    }

    public Boolean run(InfoShift infoShift, List classOIDs) throws FenixServiceException {

        boolean result = false;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurno shift = (ITurno) sp.getITurnoPersistente().readByOID(Turno.class,
                    infoShift.getIdInternal());
            sp.getITurnoPersistente().simpleLockWrite(shift);

            for (int i = 0; i < classOIDs.size(); i++) {
                ITurma schoolClass = (ITurma) sp.getITurmaPersistente().readByOID(Turma.class,
                        (Integer) classOIDs.get(i));

                //				ITurmaTurno classShift = new TurmaTurno(schoolClass, shift);
                //				try {
                //					sp.getITurmaTurnoPersistente().lockWrite(classShift);
                //				} catch (ExistingPersistentException e) {
                //					throw new ExistingServiceException(e);
                //				}
                shift.getAssociatedClasses().add(schoolClass);
                sp.getITurmaPersistente().simpleLockWrite(schoolClass);
                schoolClass.getAssociatedShifts().add(shift);
            }

            result = true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return new Boolean(result);
    }

}