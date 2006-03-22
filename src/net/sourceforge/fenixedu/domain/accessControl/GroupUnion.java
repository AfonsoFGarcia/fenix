package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.collections.iterators.UniqueFilterIterator;

public final class GroupUnion extends NodeGroup {

    private static final long serialVersionUID = 1L;

    public GroupUnion(IGroup ... groups) {
        super(groups);
    }

    public GroupUnion(Collection<IGroup> groups) {
        super(groups);
    }

    @Override
    public Iterator<Person> getElementsIterator() {
        IteratorChain iteratorChain = new IteratorChain();

        for (IGroup part : this.getChildren()) {
            iteratorChain.addIterator(part.getElementsIterator());
        }

        return new UniqueFilterIterator(iteratorChain);
    }

    @Override
    public boolean isMember(Person person) {
        for (IGroup group : getChildren()) {
            if (group.isMember(person)) {
                return true;
            }
        }
        
        return false;
    }
}
