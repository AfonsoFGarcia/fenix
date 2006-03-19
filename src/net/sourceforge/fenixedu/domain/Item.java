package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * 
 * @author ars
 */

public class Item extends Item_Base implements INode {

    public Item() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public String getSlideName() {
        String result = getParentNode().getSlideName() + "/I" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        Section section = getSection();
        return section;
    }

    public void delete() {

        Section section = this.getSection();

        if (this.getSection() != null && this.getSection().getAssociatedItems() != null) {

            this.setSection(null);
            List<Item> items = section.getAssociatedItems();
            int associatedItemOrder;

            for (Item item : items) {
                associatedItemOrder = item.getItemOrder().intValue();
                if (associatedItemOrder > this.getItemOrder().intValue()) {
                    item.setItemOrder(new Integer(associatedItemOrder - 1));
                }
            }                       
        }
        
        super.deleteDomainObject();
    }

    public void edit(String newItemName, String newItemInformation, Boolean newItemUrgent,
            Integer newOrder) {

        if (newItemName == null || newItemInformation == null || newItemUrgent == null
                || newOrder == null)
            throw new NullPointerException();

        newOrder = organizeItemsOrder(newOrder, this.getItemOrder(), this.getSection());

        this.setInformation(newItemInformation);
        this.setItemOrder(newOrder);
        this.setName(newItemName);
        this.setUrgent(newItemUrgent);
    }

    private Integer organizeItemsOrder(Integer newOrder, Integer oldOrder, Section section) {

        List<Item> items = section.getAssociatedItems();

        int diffOrder = newOrder.intValue() - oldOrder.intValue();

        if (diffOrder != 0) {
            if (diffOrder > 0)
                for (Item item : items) {
                    int iterItemOrder = item.getItemOrder().intValue();
                    if (iterItemOrder > oldOrder.intValue() && iterItemOrder <= newOrder.intValue()) {
                        item.setItemOrder(new Integer(iterItemOrder - 1));
                    }
                }
            else {                
                for (Item item : items) {
                    int iterItemOrder = item.getItemOrder().intValue();
                    if (iterItemOrder >= newOrder.intValue() && iterItemOrder < oldOrder.intValue()) {
                        item.setItemOrder(new Integer(iterItemOrder + 1));
                    }
                }
            }
        }
        return newOrder;
    }
}
