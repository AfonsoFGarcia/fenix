/*
 * Created on 2:16:00 PM,Mar 10, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import net.sourceforge.fenixedu.domain.Shift;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 2:16:00 PM, Mar 10, 2005
 */
public class InfoExternalShiftInfo
{
    private String name;
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return this.name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @param shift
     * @return
     */
    public static InfoExternalShiftInfo newFromShift(Shift shift)
    {
        InfoExternalShiftInfo info = new InfoExternalShiftInfo();
        info.setName(shift.getNome());
        return info;
    }
}
