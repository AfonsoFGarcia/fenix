/*
 * ISalaPersistente.java
 * 
 * Created on 17 de Outubro de 2002, 15:09
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.space.IRoom;

public interface ISalaPersistente extends IPersistentObject {
    public IRoom readByName(String nome) throws ExcepcaoPersistencia;

    public List<IRoom> readAll() throws ExcepcaoPersistencia;

    public List readSalas(String nome, String edificio, Integer piso, Integer tipo,
            Integer capacidadeNormal, Integer capacidadeExame) throws ExcepcaoPersistencia;

    public List readAvailableRooms(Integer examOID) throws ExcepcaoPersistencia;

    public List<IRoom> readForRoomReservation() throws ExcepcaoPersistencia;

    public List readByPavillions(List pavillionsName) throws ExcepcaoPersistencia;

    public List<IRoom> readByNormalCapacity(Integer capacity) throws ExcepcaoPersistencia;

}