/*
 * JavaSexo2SqlSexoFieldConversion.java
 *
 * Created on 13 de Novembro de 2002, 22:21
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.Sexo;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author David Santos
 */
public class JavaSexo2SqlSexoFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof Sexo) {
            Sexo s = (Sexo) source;
            return s.getSexo();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new Sexo(src);
        }

        return source;

    }
}