package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.IGroup;

public abstract class NodeGroup extends Group {
    
    private static final long serialVersionUID = 1L;
    
    private List<IGroup> children;
    
    protected NodeGroup() {
        super();
        
        this.children = new ArrayList<IGroup>();
    }
    
    public NodeGroup(IGroup ... groups) {
        this();
        
        for (IGroup group : groups) {
            this.children.add(group);
        }
    }
    
    public NodeGroup(Collection<IGroup> groups) {
        this();
        
        this.children.addAll(groups);
    }
    
    protected List<IGroup> getChildren() {
        return this.children;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        
        if (! this.getClass().isAssignableFrom(other.getClass())) {
            return false;
        }

        NodeGroup otherNodeGroup = (NodeGroup) other;
        return this.children.equals(otherNodeGroup.children);
    }

    @Override
    public int hashCode() {
        return this.children.hashCode();
    }
}
