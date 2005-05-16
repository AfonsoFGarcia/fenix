package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation extends GuideSituation_Base {
    protected GuideState situation;

    protected State state;

    public GuideSituation() {
    }

    public GuideSituation(GuideState situation, String remarks, Date date, IGuide guide, State state) {
        setRemarks(remarks);
        setGuide(guide);
        setSituation(situation);
        setDate(date);
        setState(state);

    }

    public String toString() {
        String result = "[GUIDE SITUATION";

        result += ", remarks=" + getRemarks();
        result += ", guide=" + getGuide();
        result += ", guide Situtation=" + getSituation();
        result += ", date=" + getDate();
        result += ", state=" + getState();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof GuideSituation) {
            GuideSituation guideSituation = (GuideSituation) obj;

            if (((getGuide() == null && guideSituation.getGuide() == null) || (getGuide()
                    .equals(guideSituation.getGuide())))
                    && ((getSituation() == null && guideSituation.getSituation() == null) || (getSituation()
                            .equals(guideSituation.getSituation())))) {
                resultado = true;
            }
        }

        return resultado;
    }

    /**
     * @return
     */
    public GuideState getSituation() {
        return situation;
    }

    /**
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * @param guide
     */
    public void setSituation(GuideState guide) {
        situation = guide;
    }

    /**
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

}