/*
 * InfoRoom.java
 *
 * Created on 31 de Outubro de 2002, 12:19
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.space.SpaceUtils;

import org.fenixedu.spaces.domain.Space;

/**
 * @author tfc130
 */

public class InfoRoom extends InfoObject implements Comparable {

    private final Space room;

    public InfoRoom(final Space room) {
        this.room = room;
    }

    public String getNome() {
        return getRoom().getName();
    }

    public String getEdificio() {
        return SpaceUtils.getSpaceBuilding(getRoom()).getName();
    }

    public Integer getPiso() {
        return getRoom().<Integer> getMetadata("level").orElse(null);
    }

    public String getTipo() {
        return getRoom().getClassification().map(c -> c.getName().getContent()).orElse("");
    }

    public Integer getCapacidadeNormal() {
        return getRoom().getAllocatableCapacity();
    }

    public Integer getCapacidadeExame() {
        return getRoom().<Integer> getMetadata("examCapacity").orElse(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoRoom && getRoom() == ((InfoRoom) obj).getRoom();
    }

    @Override
    public String toString() {
        return getRoom().toString();
    }

    @Override
    public int compareTo(Object obj) {
        return getNome().compareToIgnoreCase(((InfoRoom) obj).getNome());
    }

    public static InfoRoom newInfoFromDomain(final Space room) {
        return room == null ? null : new InfoRoom(room);
    }

    @Override
    public String getExternalId() {
        return getRoom().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public Space getRoom() {
        return room;
    }

}