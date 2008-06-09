/*
 * JavaTipoCurso2SqlTipoCursoFieldConversion.java
 * 
 * Created on 21 de Novembro de 2002, 15:57
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.EntryPhase;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class EntryPhase2SqlEntryPhaseFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof EntryPhase) {
            EntryPhase s = (EntryPhase) source;
            return s.getEntryPhase();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new EntryPhase(src);
        }

        return source;

    }
}