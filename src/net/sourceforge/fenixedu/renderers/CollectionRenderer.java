package net.sourceforge.fenixedu.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.PropertyUtils;

public class CollectionRenderer extends OutputRenderer {
    private String caption;

    private String rowClasses;

    private String columnClasses;

    private String headerClasses;

    private String prefixes;
    
    private String suffixes;
    
    private Map<String, TableLink> links;
    
    private List<TableLink> sortedLinks;
    
    public CollectionRenderer() {
        super();
        
        this.links = new Hashtable<String, TableLink>();
        this.sortedLinks = new ArrayList<TableLink>();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public String getHeaderClasses() {
        return headerClasses;
    }

    public void setHeaderClasses(String headerClasses) {
        this.headerClasses = headerClasses;
    }

    public String getRowClasses() {
        return rowClasses;
    }

    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }
    
    public String getPrefixes() {
        return this.prefixes;
    }

    public void setPrefixes(String prefixes) {
        this.prefixes = prefixes;
    }

    public String getSuffixes() {
        return this.suffixes;
    }

    public void setSuffixes(String suffixes) {
        this.suffixes = suffixes;
    }

    private String getStringPart(String string, int index) {
        if (string == null) {
            return null;
        }
        
        String[] stringParts = string.split(",");
        return stringParts[index % stringParts.length];
    }
    
    private TableLink getTableLink(String name) {
        TableLink tableLink = this.links.get(name);
        
        if (tableLink == null) {
            tableLink = new TableLink(name);
    
            this.links.put(name, tableLink);
            this.sortedLinks.add(tableLink);
        }
        
        return tableLink;
    }
    
    private TableLink getTableLink(int order) {
        Collections.sort(this.sortedLinks);
        
        return this.sortedLinks.get(order);
    }

    public String getLink(String name) {
        return getTableLink(name).getLink();
    }
    
    public void setLink(String name, String value) {
        getTableLink(name).setLink(value);
    }
    
    public String getParam(String name) {
        return getTableLink(name).getParam();
    }
    
    public void setParam(String name, String value) {
        getTableLink(name).setParam(value);
    }

    public String getKey(String name) {
        return getTableLink(name).getKey();
    }
    
    public void setKey(String name, String value) {
        getTableLink(name).setKey(value);
    }

    public String getBundle(String name) {
        return getTableLink(name).getBundle();
    }
    
    public void setBundle(String name, String value) {
        getTableLink(name).setBundle(value);
    }

    public String getModule(String name) {
        return getTableLink(name).getModule();
    }
    
    public void setModule(String name, String value) {
        getTableLink(name).setModule(value);
    }

    public String getText(String name) {
        return getTableLink(name).getText();
    }
    
    public void setText(String name, String value) {
        getTableLink(name).setText(value);
    }

    public String getOrder(String name) {
        return getTableLink(name).getOrder();
    }
    
    public void setOrder(String name, String value) {
        getTableLink(name).setOrder(value);
    }

    private int getNumberOfLinks() {
        return this.links.size();
    }
    
    @Override
    protected Layout getLayout(Object object, Class type) {
        return new CollectionTabularLayout((Collection) object);
    }
    
    public class CollectionTabularLayout extends TabularLayout {

        List<MetaObject> metaObjects;

        public CollectionTabularLayout(Collection object) {
            this.metaObjects = getMetaObjects(object);
        }

        private List<MetaObject> getMetaObjects(Collection collection) {
            String schemaName = getContext().getMetaObject().getSchema();
            Schema schema = RenderKit.getInstance().findSchema(schemaName);
            
            List<MetaObject> metaObjects = new ArrayList<MetaObject>();
            
            for (Iterator iter = collection.iterator(); iter.hasNext();) {
                Object collectionObject = (Object) iter.next();
                
                metaObjects.add(MetaObjectFactory.createObject(collectionObject, schema));
            }
            
            return metaObjects;
        }

        @Override
        protected boolean hasHeader() {
            return this.metaObjects.size() > 0;
        }
        
        @Override
        protected int getNumberOfColumns() {
            if (this.metaObjects.size() > 0) {
                MetaObject metaObject = this.metaObjects.get(0);
                return metaObject.getSlots().size() + getNumberOfLinks();
            }
            else {
                return 0;
            }
        }

        @Override
        protected int getNumberOfRows() {
            return this.metaObjects.size();
        }

        protected MetaObject getObject(int index) {
            return this.metaObjects.get(index);
        }
        
        @Override
        protected HtmlComponent getHeaderComponent(int columnIndex) {
            if (columnIndex < getNumberOfColumns() - getNumberOfLinks()) {
                String slotLabel = getObject(0).getSlots().get(columnIndex).getLabel();
                return new HtmlText(slotLabel);
            }
            else {
                return new HtmlText();
            }
        }

        @Override
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
            MetaObject object = getObject(rowIndex);

            if (columnIndex < getNumberOfColumns() - getNumberOfLinks()) {
                getContext().setMetaObject(object);
            
                HtmlComponent component = renderSlot(object.getSlots().get(columnIndex));
                component = wrapPrefixAndSuffix(component, columnIndex);
            
                return component;
            }
            else {
                return generateLinkComponent(object, columnIndex - (getNumberOfColumns() - getNumberOfLinks()));
            }
        }
        
        private HtmlComponent generateLinkComponent(MetaObject object, int number) {
            TableLink tableLink = getTableLink(number);
            
            HtmlLink link = new HtmlLink();
            link.setUrl(tableLink.getLink());
            link.setModule(tableLink.getModule());
            link.setText(getLinkText(tableLink));
            setLinkParameters(object, link, tableLink);
            
            return link;
        }

        private String getLinkText(TableLink tableLink) {
            String text = tableLink.getText();
            
            if (text != null) {
                return text;
            }
            
            String key = tableLink.getKey();
            String bundle = tableLink.getBundle();
            
            if (key == null) {
                return tableLink.getName();
            }
            
            text = RenderUtils.getResourceString(bundle, key);
            
            if (text != null) {
                return text;
            }
            
            return tableLink.getName();
        }

        private void setLinkParameters(MetaObject metaObject, HtmlLink link, TableLink tableLink) {
            String linkParam = tableLink.getParam();
            
            if (linkParam == null) {
                return;
            }
            
            Object object = metaObject.getObject();
            String parameters[] = tableLink.getParam().split(",");

            // "a", "a=b", "a/b", "a/b=c" 
            for (int i = 0; i < parameters.length; i++) {
                String name = parameters[i];
         
                String slotName;
                String realName;
                String customValue;
                
                String[] parameterParts = name.split("=", -1);
                if (parameterParts.length >= 1) {
                    String[] nameParts = parameterParts[0].split("/");
                    
                    slotName = nameParts[0];
                    
                    if (nameParts.length == 2) {
                        realName = nameParts[1];
                    }
                    else {
                        realName = slotName;
                    }
                    
                    if (parameterParts.length > 1) {
                        customValue = parameterParts[1];
                    }
                    else {
                        customValue = null;
                    }
                }
                else {
                    slotName = parameterParts[0];
                    realName = parameterParts[0];
                    customValue = null;
                }
                
                try {
                    String value = customValue != null ? customValue : String.valueOf(PropertyUtils.getProperty(object, slotName));
                    link.setParameter(realName, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        protected HtmlComponent wrapPrefixAndSuffix(HtmlComponent component, int columnIndex) {
            HtmlInlineContainer container = null;
            
            String prefix = getStringPart(getPrefixes(), columnIndex);
            if (prefix != null) {
                container = new HtmlInlineContainer();
                container.addChild(new HtmlText(prefix));
                container.addChild(component);
            }
            
            String suffix = getStringPart(getSuffixes(), columnIndex);
            if (suffix != null) {
                if (container != null) {
                    container.addChild(new HtmlText(suffix));
                }
                else {
                    container = new HtmlInlineContainer();
                    container.addChild(component);
                    container.addChild(new HtmlText(suffix));
                }
            }
            
            return container != null ? container : component;
        }

    }
    
    private class TableLink implements Comparable<TableLink> {

        private String name;
        private String link;
        private String module;
        private String param;
        private String text;
        private String key;
        private String bundle;
        private String order;
        
        public TableLink(String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBundle() {
            return this.bundle;
        }
        
        public void setBundle(String bundle) {
            this.bundle = bundle;
        }
        
        public String getKey() {
            return this.key;
        }
        
        public void setKey(String key) {
            this.key = key;
        }
        
        public String getLink() {
            return this.link;
        }
        
        public void setLink(String link) {
            this.link = link;
        }
        
        public String getModule() {
            return this.module;
        }
        
        public void setModule(String module) {
            this.module = module;
        }
        
        public String getParam() {
            return this.param;
        }
        
        public void setParam(String param) {
            this.param = param;
        }
        
        public String getText() {
            return this.text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getOrder() {
            return this.order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public int compareTo(TableLink other) {
            if (getOrder() == null) {
                return 0;
            }
            
            if (other.getOrder() == null) {
                return 0;
            }
            
            return getOrder().compareTo(other.getOrder());
        }
    }
}
