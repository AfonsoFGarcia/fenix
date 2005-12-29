package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LoginRedirectAction extends Action {
 
    private static final String APP_CONTEXT_APPENDER;
    static {
        final String appContext = PropertiesManager.getProperty("app.context");
        APP_CONTEXT_APPENDER = appContext.length() > 0 ? "/" + appContext : "";
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ServletOutputStream servletOutputStream = response.getOutputStream();
        writeHTMLRedirectPage(servletOutputStream, request.getSession(false));
        servletOutputStream.close();
        return null;
    }

    private void writeHTMLRedirectPage(final ServletOutputStream servletOutputStream, final HttpSession session) throws IOException {
        servletOutputStream.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        servletOutputStream.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"pt\" xml:lang=\"pt\">");

        servletOutputStream.println("<head>");
        servletOutputStream.println("<title>.IST - Login</title>");

        servletOutputStream.println("<link href=\"" + APP_CONTEXT_APPENDER + "/CSS/logdotist.css\" rel=\"stylesheet\" media=\"screen\" type=\"text/css\" />");
        servletOutputStream.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />");
        servletOutputStream.println("</head>");

        servletOutputStream.println("<body>");
        servletOutputStream.println("<div id=\"container\">");

        servletOutputStream.println("<div id=\"dotist_id\">");
        servletOutputStream.println("<img alt=\"Logo .IST\" src=\"" + APP_CONTEXT_APPENDER + "/images/dotist-id.gif\"/>");
        servletOutputStream.println("</div>");

        servletOutputStream.println("<br />");
        servletOutputStream.println("<div id=\"txt\">");
        servletOutputStream.println("<h1>Login</h1>");
        servletOutputStream.println("</div>");

        servletOutputStream.println("<br />");
        servletOutputStream.println("<div id=\"txt\"><center><p>");
        servletOutputStream.println("Para ser redirecionado para a p�gina que tinha selecionado antes da sess�o expirar, carregue no bot�o Continuar");
        servletOutputStream.println("</p></center></div>");

        writeForm(servletOutputStream, session);

        servletOutputStream.println("<br />");
        servletOutputStream.println("<div id=\"txt\"><center><p>");
        servletOutputStream.println("Se deseja seguir para a p�gina inicial habitual, carregue no bot�o P�gina Inicial");
        servletOutputStream.println("</p></center></div>");

        servletOutputStream.println("<form method=\"get\" action=\"home.do\">");
        servletOutputStream.println("<center><input type=\"submit\" value=\"P�gina Inicial\" class=\"button\"></center>");
        servletOutputStream.println("</form>");

        servletOutputStream.println("<br />");
        servletOutputStream.println("<div id=\"txt\"><center><p>");
        servletOutputStream.println("Se necessitar de ajuda, contacte-nos utilizando: <a href=\"mailto:suporte@dot.ist.utl.pt\">suporte@dot.ist.utl.pt</a>");
        servletOutputStream.println("</p></center></div>");

        servletOutputStream.println("</div>");
        servletOutputStream.println("</body>");

        servletOutputStream.println("</html>");
    }

    private void writeForm(final ServletOutputStream servletOutputStream, final HttpSession session) throws IOException {
        final String originalURI = (String) session.getAttribute("ORIGINAL_URI");
        final Map<String, String[]> parameterMap = (Map<String, String[]>) session.getAttribute("ORIGINAL_PARAMETER_MAP");
        final Map<String, Object> attributeMap = (Map<String, Object>) session.getAttribute("ORIGINAL_ATTRIBUTE_MAP");

        final int seperatorIndex = originalURI.indexOf('?');
        final int endIndex = (seperatorIndex > 0) ? seperatorIndex : originalURI.length();

        servletOutputStream.println("<form method=\"get\" action=\"" + originalURI.substring(0, endIndex) + "\">");

        for (final Entry<String, String[]> patameterEntry : parameterMap.entrySet()) {
            final String parameterName = patameterEntry.getKey();
            for (final String parameterValue : patameterEntry.getValue()) {
                servletOutputStream.println("<input type=\"hidden\" name=\"" + parameterName + "\" value=\"" + parameterValue + "\">");
            }
        }

        for (final Entry<String, Object> attributeEntry : attributeMap.entrySet()) {
            final String attributeName = attributeEntry.getKey();
            final Object attributeValue = attributeEntry.getValue();
            servletOutputStream.println("<input type=\"hidden\" name=\"" + attributeName + "\" value=\"" + attributeValue + "\">");
        }

        servletOutputStream.println("<center><input type=\"submit\" value=\"Continuar\" class=\"button\"></center>");
        servletOutputStream.println("</form>");
        
    }
}