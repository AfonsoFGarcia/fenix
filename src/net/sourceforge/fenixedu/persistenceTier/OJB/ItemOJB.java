/*
 * ItemOJB.java
 *
 * Created on 21 de Agosto de 2002, 16:36
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * 
 * @author ars
 */

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;

public class ItemOJB extends PersistentObjectOJB implements IPersistentItem {

    public IItem readBySectionAndName(ISection section, String name) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("section.idInternal", section.getIdInternal());
        crit.addEqualTo("name", name);
        crit.addEqualTo("section.site.executionCourse.code", section.getSite().getExecutionCourse()
                .getSigla());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.name", section.getSite()
                .getExecutionCourse().getExecutionPeriod().getName());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.executionYear.year", section
                .getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        return (IItem) queryObject(Item.class, crit);

    }

    public List readAllItemsBySection(ISection section) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("section.idInternal", section.getIdInternal());
        crit.addEqualTo("section.site.executionCourse.code", section.getSite().getExecutionCourse()
                .getSigla());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.name", section.getSite()
                .getExecutionCourse().getExecutionPeriod().getName());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.executionYear.year", section
                .getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        return queryList(Item.class, crit);

    }

    public void delete(IItem item) throws ExcepcaoPersistencia {
        super.delete(item);
    }

}