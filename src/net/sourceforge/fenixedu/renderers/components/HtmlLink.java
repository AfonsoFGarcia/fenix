package net.sourceforge.fenixedu.renderers.components;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class HtmlLink extends HtmlComponent {

    private String text;
    
    private String url;
    private String module;
    private String anchor;
    private String contentType;
    private String charSet;
    private boolean moduleRelative;
    private boolean contextRelative;
    
    private HtmlComponent body;
    
    private Map<String, String[]> parameters;
    
    public HtmlLink() {
        super();
        
        parameters = new Hashtable<String, String[]>();
        setModuleRelative(true);
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        String realUrl = null;

        // parse parameters from url if the url has the form 'url?param=value&other=otherValue'
        if (url != null) {
            int indexOfQuestion = url.indexOf('?');
            
            if (indexOfQuestion != -1) {
                realUrl = url.substring(0, indexOfQuestion);
                
                if (indexOfQuestion < url.length()) {
                    String parameters = url.substring(indexOfQuestion + 1);
                    String[] parameresParts = parameters.split("&");
                    
                    for (int i = 0; i < parameresParts.length; i++) {
                        String part = parameresParts[i];
                        
                        String[] paramParts = part.split("=", -1);
                        if (paramParts.length == 2) {
                            addParameter(paramParts[0], paramParts[1]);
                        }
                    }
                }
            }
        }
        
        this.url = realUrl != null ? realUrl : url;
    }

    public String getModule() {
        return this.module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setParameter(String name, String value) {
        this.parameters.put(name, new String[] { value });
    }
    
    public void setParameter(String name, Object value) {
        if (value != null) {
            setParameter(name, value.toString());
        }
    }

    public void addParameter(String name, Object value) {
        String[] values = this.parameters.get(name);
        
        if (values == null) {
            setParameter(name, value);
        }
        else {
            if (value != null) {
                String[] newValues = new String[values.length + 1];
                System.arraycopy(values, 0, newValues, 0, values.length);
                
                newValues[values.length] = value.toString();
            }
        }
    }
    
    public Map<String, String[]> getParameters() {
        return this.parameters;
    }

    public HtmlComponent getBody() {
        return body;
    }

    public void setBody(HtmlComponent body) {
        this.body = body;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isContextRelative() {
        return this.contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
        
        if (! contextRelative) {
            setModuleRelative(false);
        }
    }

    public boolean isModuleRelative() {
        return this.moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
        this.moduleRelative = moduleRelative;
        
        if (moduleRelative) {
            setContextRelative(true);
        }
    }

    @Override
    public List<HtmlComponent> getChildren() {
        ArrayList<HtmlComponent> children = new ArrayList<HtmlComponent>(super.getChildren());
        
        if (getBody() != null) {
            children.add(getBody());
        }
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("a");
        
        tag.setAttribute("href", calculateUrl());
        tag.setAttribute("charset", getCharSet());
        tag.setAttribute("type", getContentType());

        if (getText() != null) {
            tag.addChild(new HtmlTag(null, getText()));
        }
        
        if (getBody() != null) {
            tag.addChild(getBody().getOwnTag(context));
        }
        
        return tag;
    }

    public String calculateUrl() {
        StringBuilder buffer = new StringBuilder();
        
        if (getModule() != null) {
            buffer.append(RenderUtils.getContextRelativePath(getModule()));
        }
        else if (isModuleRelative()) {
            buffer.append(RenderUtils.getModuleRelativePath(""));
        }
        else if (isContextRelative()) {
            buffer.append(RenderUtils.getContextRelativePath(""));
        }
        
        if (getUrl() != null) {
            buffer.append(getUrl());
        
            if (! getParameters().isEmpty()) {
                if (getUrl().indexOf('?') == -1) {
                    buffer.append("?");
                }
                else {
                    buffer.append("&");
                }
                
                Set<String> keys = getParameters().keySet();
                int count = keys.size();
                
                for (String key : keys) {
                    String[] values = getParameters().get(key);
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0) {
                            buffer.append("&");
                        }

                        buffer.append(key);
                        buffer.append("=");
    
                        try {
                            buffer.append(URLEncoder.encode(values[i], "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    count--;
                    if (count > 0) {
                        buffer.append("&");
                    }
                }
            }
        }
        
        if (getAnchor() != null) {
            buffer.append("#");
            buffer.append(getAnchor());
        }
        
        // allways make a link
        if (buffer.length() == 0) {
            buffer.append("#");
        }
        
        return buffer.toString();
    }
}
