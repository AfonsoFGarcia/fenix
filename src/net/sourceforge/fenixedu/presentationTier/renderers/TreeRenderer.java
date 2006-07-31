package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlList;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;
import net.sourceforge.fenixedu.renderers.components.HtmlScript;
import net.sourceforge.fenixedu.renderers.components.HtmlStyle;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

import org.apache.commons.collections.Predicate;

/**
 * This renderer is a generic renderer that can be used to display simple list or tree views.
 * The usual way of using this renderer includes the configuration of the presentation of 
 * each item and the indication of how to get the children of each item. Additionaliy you
 * can specify control link that target each item.
 * 
 * <p>
 * The <tt>TreeRenderer</tt> also offers the possibility to include images in each items
 * and make the tree draggable using javascript. If you need a draggable tree then you
 * must choose a {@link #setTreeId(String) treeId} and a {@link #setFieldId(String) fieldId} 
 * or a{@link #setSaveUrl(String) saveUrl}.
 * 
 * <p>
 * You can also configure the style and class of several elements in the tree. You must
 * give special attention to the class you choose for the list items because when the tree
 * is draggable those classes have a special meaning. 
 * 
 * <p>
 * Example: 
 * <ul>
 *  <li>item 1</li>
 *  <li>item 2
 *      <ul>
 *          <li>item 2.1</li>
 *          <li>item 2.2</li>
 *      </ul>
 *  </li>
 *  <li>item 3
 *      <ul>
 *          <li>item 3.1</li>
 *          <li>item 3.2</li>
 *      </ul>
 *  </li>
 *  <li>item 4</li>
 * 
 * @author cfgi
 * 
 * @see #setChildren(String)
 * @see #setChildrenFor(String, String)
 * @see #setTreeId(String)
 * @see #setFieldId(String)
 * @see #setSaveUrl(String)
 */
public class TreeRenderer extends OutputRenderer {

    private String treeId;
    
    private String fieldId;
    private String saveUrl;
    private boolean moduleRelative;
    private String saveParameter;
    private String onComplete;
    private String onError;
    
    private String eachLayout;
    private String eachSchema;
    
    private String image;
    private String children;
    private String links;
    private String hiddenLinks;
    
    private String listClass;
    private String listStyle;
    private String itemClass;
    private String itemStyle;
    private String linksClasses;
    private String linksStyle;
    
    private String movedClass;
    
    private Map<String, String> layoutsMap;
    private Map<String, String> schemasMap;
    
    private Map<String, String> childrenMap;
    private Map<String, String> classesMap;
    private Map<String, String> styleMap;
    private Map<String, String> linksMap;
    private Map<String, String> hiddenLinksMap;
    private Map<String, String> imagesMap;

    public TreeRenderer() {
        super();
        
        this.childrenMap = new HashMap<String, String>();
        this.classesMap = new HashMap<String, String>();
        this.styleMap = new HashMap<String, String>();
        this.linksMap = new HashMap<String, String>();
        this.hiddenLinksMap = new HashMap<String, String>();
        this.imagesMap = new HashMap<String, String>();
        this.layoutsMap = new HashMap<String, String>();
        this.schemasMap = new HashMap<String, String>();
        
        setStyle("margin: 0px; padding: 0px;");
        setListStyle("margin-left: 20px; padding-left: 20px;");
        setItemStyle("vertical-align:middle;");
        
        setModuleRelative(true);
    }

    public String getTreeId() {
        return this.treeId;
    }

    /**
     * Chooses the id of the generated tree. This property is required when you want the
     * tree to be draggable. When the tree is generated a javaScript object with this
     * id is generated so you have to specify a valid javascript identifier.
     * 
     * <p>
     * The fiven identifier can be used in javascript to invoke operations in the tree.
     * The most important are:
     * 
     * <ul>
     *  <li><strong><tt>saveTree()</tt></strong>: submits the current tree structure to be saved</li>
     *  <li><strong><tt>expandAll()</tt></strong>: expands all nodes of the tree</li>
     *  <li><strong><tt>collapseAll()</tt></strong>: collapse all nodes of the tree</li>
     * </ul>
     * 
     * @property
     */
    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getEachLayout() {
        return this.eachLayout;
    }

    /**
     * Chooses the default layout that should be used to present each item.
     * 
     * @property
     */
    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return this.eachSchema;
    }

    /**
     * Chooses the default schema that should be used when presenting each item.
     * 
     * @property
     */
    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getFieldId() {
        return this.fieldId;
    }

    public String getLayoutFor(String type) {
        return this.layoutsMap.get(type);
    }
    
    /**
     * Chooses the layout to be used to presente objects of the specified type. You
     * can use the simple name of the type.
     *
     * @property
     */
    public void setLayoutFor(String type, String layout) {
        this.layoutsMap.put(type, layout);
    }
    
    public String getSchemaFor(String type) {
        return this.schemasMap.get(type);
    }
    
    /**
     * Chooses the schema to be used when presenting objects of the specified type. You
     * can use the simple name of the type.
     *
     * @property
     */
    public void setSchemaFor(String type, String schema) {
        this.schemasMap.put(type, schema);
    }
    
    public String getChildren() {
        return this.children;
    }

    /**
     * This property specifies the name of the method that should be used, by default, to
     * obtain the list of children for each item. If this property is not specified then,
     * by default, items are considered to be leaf items.
     * 
     * @property
     */
    public void setChildren(String children) {
        this.children = children;
    }

    public String getChildrenFor(String type) {
        return this.childrenMap.get(type);
    }
    
    /**
     * This property specifies the name of the method that should be used to obtain the
     * list of children for item that are objects of the specified type. You can use
     * the simple name or the complete type name if there are naem conflicts.
     * 
     * @property
     */
    public void setChildrenFor(String type, String method) {
        this.childrenMap.put(type, method);
    }
    
    /**
     * This property indicates two things. First it indicates that you want a draggable
     * tree. Second it specifies the id of the form field were the tree structure will
     * be saved when you invoke the <tt>saveTree()</tt> operation of the tree.
     * 
     * <p>
     * Note that the form containing the field will be automatically submitted. So you
     * can have a form somewere in the page that looks like:
     * 
     * <pre>
     *  &lt;form action=&quot;/someAction.do&quot;&gt;
     *      &lt;input id=&quot;myFieldId&quot; type=&quot;hidden&quot; name=&quot;tree&quot; value=&quot;&quot;/&gt; 
     *  &lt;/form&gt;
     * </pre>
     * 
     * @property
     */
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getSaveUrl() {
        return this.saveUrl;
    }

    /**
     * This property is similar to {@link #setFieldId(String) fieldId}, that is, is also 
     * specifies that you want a draggable tree. Nevertheless when you specify this propert
     * instead of the {@link #setFieldId(String) fieldId} the tree is saved is asynchronous
     * way to the server to the url indicated in this property.
     * 
     * @see #setOnComplete(String)
     * @see #setOnError(String)
     * @see #setSaveParameter(String)
     * @see #setModuleRelative(boolean)
     * 
     * @property
     */
    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public boolean isModuleRelative() {
        return this.moduleRelative;
    }

    /**
     * This property indicates if the <tt>saveUrl</tt> is relative to the current module.
     * This property is <code>true</code> by default.
     *  
     * @property
     */
    public void setModuleRelative(boolean moduleRelative) {
        this.moduleRelative = moduleRelative;
    }

    public String getSaveParameter() {
        return this.saveParameter;
    }

    /**
     * When the tree is saved in an asynchronous way this property chooses the name of the
     * parameter that will contain the tree structure.
     * 
     * @property
     */
    public void setSaveParameter(String saveParameter) {
        this.saveParameter = saveParameter;
    }

    public String getOnComplete() {
        return this.onComplete;
    }

    /**
     * This property specifies the javascript function to be executed when the tree structure 
     * is saved correctly, that is, submited without protocol errors to the server. You
     * can specify an anonymous function like <tt>function() { alert('complete'); }</tt>
     * 
     * @property
     */
    public void setOnComplete(String onComplete) {
        this.onComplete = onComplete;
    }

    public String getOnError() {
        return this.onError;
    }

    /**
     * This property specifies the javascript function to be executed when the tree structure 
     * cannot be saved correctly, that is, some error is reported by the server. You
     * can specify an anonymous function like <tt>function() { alert('failed'); }</tt>
     * 
     * @property
     */
    public void setOnError(String onError) {
        this.onError = onError;
    }

    public String getListClass() {
        return this.listClass;
    }

    /**
     * Chooses the html classes to apply to each list.
     * 
     * @property
     */
    public void setListClass(String listClass) {
        this.listClass = listClass;
    }

    public String getListStyle() {
        return this.listStyle;
    }

    /**
     * Chooses the style to apply to each each list.
     * 
     * @property
     */
    public void setListStyle(String listStyle) {
        this.listStyle = listStyle;
    }

    public String getItemClass() {
        return this.itemClass;
    }

    /**
     * The html classes to apply to each list item.
     * 
     * <p>
     * <strong>Note:</strong> if the tree is draggable this class has a special
     * meaning. If you don't specify images for the items this property will be
     * used to determine the image that will be used for the item. Currently only the
     * "folder" and "sheet" classes are supported. So, you you specify the list
     * item class it's also a good idea to override the image for each item.
     * 
     * @see #setImageFor(String, String)
     * 
     * @property
     */
    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemStyle() {
        return this.itemStyle;
    }

    /**
     * The css style for each list item.
     * 
     * @property
     */
    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public String getClassesFor(String type) {
        return this.classesMap.get(type);
    }
    
    /**
     * The css classes to be used in items corresponding to objects of the specified type.
     * 
     * @property
     */
    public void setClassesFor(String type, String method) {
        this.classesMap.put(type, method);
    }

    public String getLinks() {
        return this.links;
    }

    /**
     * Specifies the default link text to be included near each item. The text will be passed 
     * through {@link RenderUtils#getFormattedProperties(String, Object)} with the object of the
     * item. This way you can include references the object's properties in the text.
     * 
     * @property
     */
    public void setLinks(String links) {
        this.links = links;
    }
    
    public String getHiddenLinks() {
        return hiddenLinks;
    }

    /**
     * This property is similar to the {@link #setLinks(String) links} property but the links
     * defined with this property will be hidden when the tree is draggable. This property is
     * usefull to specify links that simulate the same actions available by drag and drop.
     * 
     * @property
     */
    public void setHiddenLinks(String hiddenlinks) {
        this.hiddenLinks = hiddenlinks;
    }

    public String getLinksFor(String type) {
        return this.linksMap.get(type);
    }
    
    /**
     * Specifies the links that should appear near items corresponding to objects of the
     * given type. The link text is replaced in the same way than {@link #setLinks(String)}.
     * 
     * @property
     */
    public void setLinksFor(String type, String method) {
        this.linksMap.put(type, method);
    }
    
    public String getHiddenLinksFor(String type) {
        return this.hiddenLinksMap.get(type);
    }
    
    /**
     * Specifies the hidden links that should appear near items corresponding to objects of the
     * given type. The link text is replaced in the same way than {@link #setHiddenLinks(String)}.
     * 
     * @property
     */
    public void setHiddenLinksFor(String type, String method) {
        this.hiddenLinksMap.put(type, method);
    }
    
    public String getLinksClasses() {
        return this.linksClasses;
    }

    /**
     * Chooses the css classes to apply to the container surrounding the item's links.
     * If this property is not set a default class, based on the tree id, is applied. 
     *
     * @property
     */
    public void setLinksClasses(String linksClass) {
        this.linksClasses = linksClass;
    }

    public String getLinksStyle() {
        return this.linksStyle;
    }

    /**
     * Chooses the css style to apply to each item's links.
     * 
     * @property
     */
    public void setLinksStyle(String linksStyle) {
        this.linksStyle = linksStyle;
    }
    
    public String getMovedClass() {
        return this.movedClass;
    }

    /**
     * Chooses the css class to apply to elements that have been moved.
     * 
     * @property
     */
    public void setMovedClass(String moveClass) {
        this.movedClass = moveClass;
    }

    public String getImage() {
        return image;
    }

    /**
     * Sets the path for the default image to be used for lists items.
     * 
     * @property
     */
    public void setImage(String image) {
        this.image = image;
    }

    public String getImageFor(String type) {
        return this.imagesMap.get(type);
    }
    
    /**
     * Sets the path of the image that should be used as a icon of items correponding
     * to objects of the given type. The path is context relative. 
     * 
     * @property
     */
    public void setImageFor(String type, String method) {
        this.imagesMap.put(type, method);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new TreeLayout();
    }

    public boolean isDraggable() {
        return getTreeId() != null && (getFieldId() != null || getSaveUrl() != null);
    }

    private class TreeLayout extends Layout {

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            HtmlList list = createList((Collection) object);
            
            if (!isDraggable()) {
                return list;
            }
            else {
                return createDragScript(list);
            }
        }

        private HtmlComponent createDragScript(HtmlList list) {
            String id = getTreeId();
            list.setId(id);

            HtmlLink link = new HtmlLink();
            link.setModuleRelative(false);
            
            link.setUrl("/javaScript/drag-drop-folder-tree/js/ajax.js");
            HtmlScript scriptA = new HtmlScript("text/javascript", link.calculateUrl());
            
            link.setUrl("/javaScript/drag-drop-folder-tree/js/drag-drop-folder-tree.js");
            HtmlScript scriptB = new HtmlScript("text/javascript", link.calculateUrl());
            
            HtmlLink requestUrlLink = new HtmlLink();
            requestUrlLink.setModuleRelative(isModuleRelative());
            requestUrlLink.setUrl(getSaveUrl());
            
            String requestUrl = getSaveUrl() != null ? requestUrlLink.calculateUrl() : null;
            
            HtmlLink imagesUrlLink = new HtmlLink();
            imagesUrlLink.setModuleRelative(false);
            imagesUrlLink.setUrl("/javaScript/drag-drop-folder-tree/images/");
            
            HtmlScript scriptC = new HtmlScript();
            scriptC.setContentType("text/javascript");
            scriptC.setScript(getTreeScript(id, requestUrl, imagesUrlLink.calculateUrl()));
            
            HtmlStyle style = new HtmlStyle();
            style.setStyleBody(
                    String.format("#%s,#%s-container { %s }", id, id, getStyle()) + "\n" +
                    String.format("#%s ul { %s }", id, getListStyle()) + "\n" + 
                    String.format("#%s li,#%s-container li { %s }", id, id, getItemStyle()) + "\n"
            );
            
            HtmlScript scriptD = new HtmlScript();
            scriptD.setContentType("text/javascript");
            scriptD.setScript(createHideLinksScript());
            
            HtmlContainer container = new HtmlBlockContainer();

            container.addChild(list);
            container.addChild(style);
            container.addChild(scriptA);
            container.addChild(scriptB);
            container.addChild(scriptC);
            container.addChild(scriptD);
            
            return container;
        }

        private CharSequence createHideLinksScript() {
            return String.format(
                    "\n" +
                    "var items = document.getElementById('%s').getElementsByTagName('SPAN');\n" +
                    "for (var item=0; item<items.length; item++) {\n" +
                    "  var span = items[item];\n" +
                    "\n" +
                    "  if (span.className && span.className.indexOf('%s') >= 0) {\n" +
                    "    var childSpans = span.getElementsByTagName('SPAN');\n" +
                    "    if (childSpans.length > 0) {\n" +
                    "      span.getElementsByTagName('SPAN')[0].style.display = 'none';\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n",
                    getTreeId(), 
                    getUsableLinksClasses());
        }

        private String getTreeScript(String id, String requestUrl, String imagesFolder) {
            StringBuilder script = new StringBuilder();
            
            script.append(id + " = new JSDragDropTree();\n");
            script.append(id + ".setTreeId('" + id + "');\n");
            script.append(id + ".setImageFolder('" + imagesFolder + "');\n");

            if (requestUrl != null) {
                script.append(id + ".setRequestUrl('" + requestUrl + "');\n");
            }
            
            if (getSaveParameter() != null) {
                script.append(id + ".setRequestParameter('" + getSaveParameter() + "');\n");
            }
            
            if (getFieldId() != null) {
                script.append(id + ".setFieldId('" + getFieldId() + "');\n");
            }

            if (getOnComplete() != null) {
                script.append(id + ".setOnComplete(" + getOnComplete() + ");\n");
            }
            
            if (getOnError() != null) {
                script.append(id + ".setOnError(" + getOnError() + ");\n");
            }

            if (getMovedClass() != null) {
                script.append(id + ".setMovedClass('" + getMovedClass() + "');\n");
            }
            
            if (TreeRenderer.this.imagesMap.size() > 0) {
                script.append(id + ".setIncludeImage(false);\n");
            }

            script.append(id + ".initTree();\n");
            
            return script.toString();
        }

        public HtmlList createList(Collection collection) {
            HtmlList list = new HtmlList();
            
            list.setClasses(getListClass());
            
            if (collection == null) {
                return list;
            }

            for (Object object : collection) {
                HtmlListItem item = list.createItem();

                item.setClasses(getClassFor(object));
                item.setStyle(getStyleFor(object));
                
                Schema schema = RenderKit.getInstance().findSchema(getSchemaFor(object));
                HtmlComponent component = renderValue(object, object.getClass(), schema, getLayoutFor(object));
                
                String imageUrl = getImageFor(object);
                if (imageUrl != null) {
                    HtmlLink imageLink = new HtmlLink();
                    imageLink.setModuleRelative(false);
                    imageLink.setUrl(imageUrl);
                    
                    HtmlImage image = new HtmlImage();
                    image.setSource(imageLink.calculateUrl());
                    
                    item.addChild(image);
                }
                
                item.addChild(component);
                
                String linksForItem = getLinksFor(object);
                String hiddenLinksForItem = getHiddenLinksFor(object);
                
                if (linksForItem != null || hiddenLinksForItem != null) {
                    HtmlContainer linksContainer = new HtmlInlineContainer();
                    linksContainer.setClasses(getUsableLinksClasses());
                    linksContainer.setStyle(getLinksStyle());

                    if (hiddenLinksForItem != null) {
                        HtmlContainer hiddenContainer = new HtmlInlineContainer();
                        hiddenContainer.addChild(new HtmlText(RenderUtils.getFormattedProperties(hiddenLinksForItem, object)));
                        linksContainer.addChild(hiddenContainer);
                    }
                    
                    linksContainer.addChild(new HtmlText(RenderUtils.getFormattedProperties(linksForItem, object)));
                    item.addChild(linksContainer);
                }
                
                try {
                    String children = getChildrenFor(object);
                 
                    if (children != null) {
                        Collection subCollection = (Collection) RendererPropertyUtils.getProperty(object, children, false);
                        item.addChild(createList(subCollection));
                    }
                    else {
                        item.setAttribute("noChildren", "true");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            return list;
        }

        private String getUsableLinksClasses() {
            if (getLinksClasses() == null) {
                if (getTreeId() != null) {
                    return getTreeId() + "-links";
                }
                else {
                    return null;
                }
            }
            else {
                return getLinksClasses();
            }
        }

        private String getChildrenFor(Object object) {
            return getValueFor(object, TreeRenderer.this.childrenMap, getChildren());
        }
        
        private String getClassFor(Object object) {
            return getValueFor(object, TreeRenderer.this.classesMap, getItemClass());
        }
        
        private String getStyleFor(Object object) {
            return getValueFor(object, TreeRenderer.this.styleMap, null);
        }
        
        private String getLinksFor(Object object) {
            return getValueFor(object, TreeRenderer.this.linksMap, getLinks());
        }
        
        private String getHiddenLinksFor(Object object) {
            return getValueFor(object, TreeRenderer.this.hiddenLinksMap, getHiddenLinks());
        }

        private String getImageFor(Object object) {
            return getValueFor(object, TreeRenderer.this.imagesMap, getImage());
        }

        private String getSchemaFor(Object object) {
            return getValueFor(object, TreeRenderer.this.schemasMap, getEachSchema());
        }

        private String getLayoutFor(Object object) {
            return getValueFor(object, TreeRenderer.this.layoutsMap, getEachLayout());
        }

        private String getValueFor(Object object, Map<String, String> map, String defaultValue) {
            String value = null;
            String key = null;
            
            for (String type : map.keySet()) {
                if (object.getClass().getName().contains(type)) {
                    if (key == null || key.length() < type.length()) {
                        key = type;
                        value = map.get(key);
                    }
                }
            }
            
            if (value == null) {
                value = defaultValue;
            }
            
            return value;
        }
    }
}
