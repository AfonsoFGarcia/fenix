package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoAula;

/**
 * @author Ana e Ricardo
 * 
 * 19/7/2004
 */

public abstract class InfoShowOccupation extends InfoObject implements ISiteComponent {

    public abstract InfoShift getInfoShift();

    public abstract TipoAula getTipo();

    public abstract InfoRoomOccupation getInfoRoomOccupation();

    public abstract DiaSemana getDiaSemana();

    public abstract Calendar getInicio();

    public abstract Calendar getFim();

}