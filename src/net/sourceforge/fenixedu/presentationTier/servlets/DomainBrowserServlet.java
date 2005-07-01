package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.ojb.broker.core.proxy.ProxyHelper;

import dml.CompilerArgs;
import dml.DmlCompiler;
import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;
import dml.Role;
import dml.Slot;

/**
 * 
 * @author Jo�o Cachopo
 *
 */
public class DomainBrowserServlet extends HttpServlet {

    private static DomainModel domainModel;
    private static HashMap<DomainClass,String> domainClassesDescAttr = new HashMap<DomainClass,String>();


    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String domainModelName = getInitParameter("domainmodel");
        if (domainModelName != null) {
            String domainPath = getServletContext().getRealPath(domainModelName);
            System.out.println("Using the domain at path: '" + domainPath + "'");
            CompilerArgs compArgs = new CompilerArgs(new String[] {"-d", "/tmp", "-f", domainPath});
            try { 
                domainModel = DmlCompiler.getDomainModel(compArgs);
            } catch (Exception e) {
                domainModel = null;
            }
        }
    }


    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");

        PrintWriter out = res.getWriter();
        out.println("<HEAD><TITLE>Fenix Domain Browser</TITLE></HEAD><BODY>\n");

        String path = req.getPathInfo();
        try {
            if ("/listAll".equals(path)) {
                renderListAll(out, RequestParams.parse(req, "domainClass"));
            } else if ("/showObj".equals(path)) {
                renderDomainObject(out, RequestParams.parse(req, "domainClass", "objId"));
            } else if ("/listRole".equals(path)) {
                renderDomainObjectRole(out, RequestParams.parse(req, "domainClass", "objId", "role"));
            } else {
                renderMainIndex(out);
            }        
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Error: " + e.getMessage());
        }
        

	out.println("</BODY>\n");
	out.close();
    }


    protected void renderDomainObject(PrintWriter out, RequestParams params) throws IOException,Exception {
        if (params == null) {
            renderMainIndex(out);
            return;
        }
        
        SuportePersistenteOJB supPers = SuportePersistenteOJB.getInstance();

        try {
            supPers.beginTransaction();

            DomainObject domObj = getDomainObject(supPers.getIPersistentObject().readByOID(params.javaClass, params.oid));
            
            if (domObj == null) {
                out.println("<P>No such object</P>");
                return;
            }

            out.println("<H1>");
            out.println(params.classFullName);
            out.println(" Instance: ");
            out.println(getObjectDescription(domObj, params.domClass));
            out.println("</H1>\n");

            out.println("<H2>Attributes</H2>\n");
            out.println("<TABLE>\n");
            for (String attr : getAllAttrs(params.domClass)) {
                out.println("<TR><TD class=\"attrname\">");
                out.println(attr);
                out.println("</TD><TD class=\"attrvalue\">");
                out.println(getAttributeValue(domObj, attr));
                out.println("</TD><TR>");
            }
            out.println("</TABLE>\n");

            out.println("<H2>Relations</H2>");
            out.println("<TABLE>\n");
            for (Role role : getAllRoles(params.domClass)) {
                out.println("<TR><TD class=\"rolename\">");
                out.println(role.getName());
                out.println("</TD><TD class=\"rolevalue\">");
                Object roleValue = getAttributeValue(domObj, role.getName());
                if (role.getMultiplicityUpper() == 1) {
                    if (roleValue == null) {
                        out.println("null");
                    } else {
                        DomainEntity roleType = role.getType();
                        if (roleType instanceof DomainClass) {
                            renderObjectId(out, getDomainObject(roleValue), (DomainClass)roleType);
                        } else {
                            out.println(roleValue);
                        }
                    }
                } else {
                    if (roleValue == null) {
                        out.println("null collection");
                    } else {
                        Collection col = (Collection)roleValue;
                        out.println("<A HREF=\"listRole?domainClass=");
                        out.println(domObj.getClass().getName());
                        out.println("&objId=");
                        out.println(domObj.getIdInternal());
                        out.println("&role=");
                        out.println(role.getName());
                        out.println("\">");
                        out.println(role.getType().getFullName());
                        out.println("[");
                        out.println(col.size());
                        out.println("]</A>");
                    }
                }
                
                out.println("</TD><TR>");
            }
            out.println("</TABLE>\n");            
        } finally {
            supPers.abortTransaction();
        }
    }

    protected void renderDomainObjectRole(PrintWriter out, RequestParams params) throws IOException,Exception {
        if (params == null) {
            renderMainIndex(out);
            return;
        }

        SuportePersistenteOJB supPers = SuportePersistenteOJB.getInstance();

        try {
            supPers.beginTransaction();

            DomainObject domObj = getDomainObject(supPers.getIPersistentObject().readByOID(params.javaClass, params.oid));
            if (domObj == null) {
                out.println("<P>No such object</P>");
                return;
            }

            out.println("<H1>");
            renderObjectId(out, domObj, params.domClass);
            out.println("'s ");
            out.println(params.roleName);
            out.println(":</H1>\n");

            Collection insts = (Collection)getAttributeValue(domObj, params.roleName);
            renderCollection(out, insts, (DomainClass)params.role.getType(), params);

        } finally {
            supPers.abortTransaction();
        }
    }


    protected void renderListAll(PrintWriter out, RequestParams params) throws IOException,Exception {
        if (params == null) {
            renderMainIndex(out);
            return;
        }

        out.println("<H1>");
        out.println(params.classFullName);
        out.println(" Entities</H1>\n");

        SuportePersistenteOJB supPers = SuportePersistenteOJB.getInstance();

        try {
            supPers.beginTransaction();

            Collection insts = supPers.getIPersistentObject().readAll(params.javaClass);
            renderCollection(out, insts, params.domClass, params);
            
        } finally {
            supPers.abortTransaction();
        }
        
    }

    protected void renderCollection(PrintWriter out, Collection insts, DomainClass domClass, RequestParams params) throws IOException,Exception {
        if (insts == null) {
            out.println("<P>Null collection</P>");
            return;
        }

        int total = insts.size();
        int first = params.start;
        int last = Math.min(first + params.max - 1, total);

        out.println("<P>Total instances: ");
        out.println(total);
        out.println("</P>\n");
        
        if ((total > last) || (first > 1)) {
            out.println("<P>Showing from ");
            out.println(first);
            out.println(" to ");
            out.println(last);
            out.println("</P>\n");
        }

        if (first > total) {
            return;
        }

        if (last < total) {
            params.start = last + 1;

            out.println("<P><A HREF=\"");
            out.println(params.buildURL());
            out.println("\">Show next</A></P>");

            params.start = first;
        }

        out.println("<UL>\n");
        
        Iterator iter = insts.iterator();

        // skip initial values until reaching start
        for (int i = first; i > 1; i--) {
            iter.next();
        }

        // show the (eventually partial) list
        for (int count = first; count <= last; count++) {
            out.println("<LI>");
            renderObjectId(out, getDomainObject(iter.next()), domClass);
            out.println("</LI>\n");
        }
        out.println("</UL>\n");
    }

    protected void renderObjectId(PrintWriter out, DomainObject domObj, DomainClass domClass) throws IOException {
        out.println("<A HREF=\"showObj?domainClass=");
        out.println(domObj.getClass().getName());
        out.println("&objId=");
        out.println(domObj.getIdInternal());
        out.println("\">");
        out.println(getObjectDescription(domObj, domClass));
        out.println("</A>");
    }

    protected String getObjectDescription(DomainObject domObj, DomainClass domClass) {
        String description = getSpecializedDescription(domObj);

        if (description == null) {
            String descAttribute = getDescAttribute(domClass);
            description = getAttributeValue(domObj, descAttribute).toString();
        }
        return description;
    }


    protected String getDescAttribute(DomainClass domClass) {
        String descAttribute = domainClassesDescAttr.get(domClass);
        if (descAttribute == null) {
            descAttribute = guessDescAttribute(domClass);
            domainClassesDescAttr.put(domClass, descAttribute);
        }
        return descAttribute;
    }

    protected String guessDescAttribute(DomainClass domClass) {
        String descAttr = "idInternal";

        for (String attr : getAllAttrs(domClass)) {
            if (attr.equals("name") || attr.equals("nome")) {
                descAttr = attr;
                break;
            }
        }

        return descAttr;
    }

    protected Object getAttributeValue(Object obj, String attr) {
        try {
            Method reader = obj.getClass().getMethod("get" + capitalize(attr));
            return reader.invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    protected void renderMainIndex(PrintWriter out) throws IOException {
        out.println("<H1>All Entities</H1>\n");

        if (domainModel != null) {
            out.println("<UL>\n");
            for (DomainClass domClass : getAllDomainClasses()) {
                out.println("<LI><A HREF=\"/uml/");
                out.println(domClass.getFullName());
                out.println(".html\">UML</A> <A HREF=\"");
                out.println("listAll?domainClass=");
                out.println(domClass.getFullName());
                out.println("\">");
                out.println(domClass.getName());
                out.println("</A></LI>\n");
            }
            out.println("</UL>\n");
        } else {
            out.println("<P>There are none...</P>\n");
        }
    }


    private static HashMap<DomainClass,List<String>> domainClassesAttributes = new HashMap<DomainClass,List<String>>();

    protected synchronized List<String> getAllAttrs(DomainClass domClass) {
        List<String> attrs = domainClassesAttributes.get(domClass);
        if (attrs == null) {
            attrs = new ArrayList<String>();

            DomainEntity domEntity = domClass;
            while (domEntity instanceof DomainClass) {
                DomainClass dClass = (DomainClass)domEntity;
                Iterator slots = dClass.getSlots();
                while (slots.hasNext()) {
                    attrs.add(((Slot)slots.next()).getName());
                }
                domEntity = dClass.getSuperclass();
            }
            Collections.sort(attrs);
            domainClassesAttributes.put(domClass, attrs);
        }
        return attrs;
    }


    private static HashMap<DomainClass,List<Role>> domainClassesRoles = new HashMap<DomainClass,List<Role>>();

    private static final Comparator<Role> ROLE_COMPARATOR = new Comparator<Role>() {
        public int compare(Role r1, Role r2) {
            return r1.getName().compareTo(r2.getName());
        }
    };

    protected List<Role> getAllRoles(DomainClass domClass) {
        List<Role> roles = domainClassesRoles.get(domClass);
        if (roles == null) {
            roles = new ArrayList<Role>();

            DomainEntity domEntity = domClass;
            while (domEntity instanceof DomainClass) {
                DomainClass dClass = (DomainClass)domEntity;
                Iterator roleSlots = dClass.getRoleSlots();
                while (roleSlots.hasNext()) {
                    roles.add((Role)roleSlots.next());
                }
                domEntity = dClass.getSuperclass();
            }
            Collections.sort(roles, ROLE_COMPARATOR);
            domainClassesRoles.put(domClass, roles);
        }
        return roles;
    }



    private static List<DomainClass> allDomainClasses = null;

    protected List<DomainClass> getAllDomainClasses() {
        if (allDomainClasses == null) {
            allDomainClasses = new ArrayList<DomainClass>();
            for (Iterator iter = domainModel.getClasses(); iter.hasNext();) {
                allDomainClasses.add((DomainClass)iter.next());
            }
            Collections.sort(allDomainClasses, new Comparator<DomainClass>() {
                public int compare(DomainClass dc1, DomainClass dc2) {
                    return dc1.getName().compareTo(dc2.getName());
                }
            });
        }
        return allDomainClasses;
    }

    



    protected String capitalize(String str) {
        if ((str == null) || Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
    }

    protected DomainObject getDomainObject(Object obj) {
        return (DomainObject)ProxyHelper.getRealObject(obj);
    }


    static class RequestParams {
        private static final String[] OPTIONAL_PARAMS = { "start", "max" };

        public static final int DEFAULT_MAX = 100;

        String path;
        String classFullName = null;
        DomainClass domClass = null;
        Class javaClass = null;
        int oid = -1;
        String roleName = null;
        Role role = null;
        int start = 1;
        int max = DEFAULT_MAX;

        String buildURL() {
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            String sep = "?";
            if (classFullName != null) {
                sb.append(sep);
                sb.append("domainClass=");
                sb.append(classFullName);
                sep = "&";
            }
            if (oid != -1) {
                sb.append(sep);
                sb.append("objId=");
                sb.append(oid);
                sep = "&";
            }
            if (roleName != null) {
                sb.append(sep);
                sb.append("role=");
                sb.append(roleName);
                sep = "&";
            }
            if (start > 1) {
                sb.append(sep);
                sb.append("start=");
                sb.append(start);
                sep = "&";
            }
            if (max != DEFAULT_MAX) {
                sb.append(sep);
                sb.append("max=");
                sb.append(max);
                sep = "&";
            }
            return sb.toString();
        }

        static RequestParams parse(HttpServletRequest req, String... paramNames) {
            RequestParams params = new RequestParams();
            
            params.path = req.getPathInfo().substring(1);

            try {
                for (String name : paramNames) {
                    String value = req.getParameter(name);
                    if (value == null) {
                        return null;
                    }
                    
                    if (name.equals("domainClass")) {
                        params.classFullName = value;
                        params.javaClass = Class.forName(value);
                        params.domClass = domainModel.findClass(value);
                        if (params.domClass == null) {
                            return null;
                        }
                    } else if (name.equals("objId")) {
                        params.oid = Integer.parseInt(value);
                    } else if (name.equals("role")) {
                        params.roleName = value;
                        params.role = params.domClass.findRoleSlot(value);
                        if (params.role == null) {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
            } catch (Exception e) {
                return null;
            }

            // parse optional parameters
            for (String name : OPTIONAL_PARAMS) {
                String value = req.getParameter(name);
                if (value != null) {
                    try {
                        if (name.equals("start")) {
                            params.start = Integer.parseInt(value);
                        } else if (name.equals("max")) {
                            params.max = Integer.parseInt(value);
                        }
                    } catch (Exception e) {
                        // ignore it
                    }
                }
            }            
            
            return params;
        }
    }

    static class SpecializedMethod {
        Method method = null;
    }

    private static HashMap<Class,SpecializedMethod> specializedMethods = new HashMap<Class,SpecializedMethod>();

    protected static String getSpecializedDescription(Object obj) {
        Method m = findSpecializedMethod(obj.getClass());
        if (m != null) {
            try {
                return (String)m.invoke(null, obj);
            } catch (Exception e) {
                // ignore errors on the method
            }
        }
        return null;
    }

    protected synchronized static Method findSpecializedMethod(Class objClass) {
        SpecializedMethod sm = specializedMethods.get(objClass);

        if (sm == null) {
            sm = new SpecializedMethod();
            specializedMethods.put(objClass, sm);

            while (objClass != null) {
                try {
                    Method m = DomainObjectsExternalDescriptions.class.getMethod("getShortDescription", objClass);
                    sm.method = m;
                    // finish the loop because we found it already...
                    break;
                } catch (Exception e) {
                    // not found the method, so continue the loop
                }
                objClass = objClass.getSuperclass();
            }
        }

        return sm.method;
    }
}
