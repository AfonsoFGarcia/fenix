/*
 * Created on 26/Nov/2004
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Pedro Santos & Rita Carvalho
 */
public class DegreeForwardFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    String forwardDegreeDescription;

    String notFoundURI;

    String invalidURI;

    String app;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();

        try {
            forwardDegreeDescription = filterConfig.getInitParameter("forwardDegreeDescription");
            notFoundURI = filterConfig.getInitParameter("notFoundURI");
            invalidURI = filterConfig.getInitParameter("invalidURI");
            app = filterConfig.getInitParameter("app");
        } catch (Exception e) {
        }
    }

    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
	if (uri.endsWith(".do")) {
	    chain.doFilter(request, response);
	    return;
	}

        String context = request.getContextPath();
        String newURI = uri.replaceFirst(app, "");
        String[] tokens = newURI.split("/");
        if (tokens.length > 0 && tokens[0].length() == 0) {
            String[] tokensTemp = new String[tokens.length - 1];
            for (int i = 1; i < tokens.length; i++) {
                tokensTemp[i - 1] = tokens[i];
            }
            tokens = tokensTemp;
        }

        try {
            if (tokens.length != 1 || !isDegree(tokens[0])) {
                chain.doFilter(request, response);
                return;
            }
        } catch (FenixServiceException e2) {
            throw new ServletException(e2);
        } catch (FenixFilterException e) {
            throw new ServletException(e);
        } catch (IOException e) {
            throw new ServletException(e);
        }

        StringBuffer forwardURI = new StringBuffer(context);

        String degreeCode = tokens[0];
        Integer degreeId;
        try {
            degreeId = getDegreeId(degreeCode);
        } catch (FenixServiceException e) {
            throw new ServletException(e);
        } catch (FenixFilterException e) {
            throw new ServletException(e);
        }

        if (degreeId != null) {
            try {
                InfoExecutionPeriod infoExecutionPeriod = getCurrentExecutionPeriod();
                if (infoExecutionPeriod != null) {
                    Integer executionPeriodId = infoExecutionPeriod.getIdInternal();
                    forwardURI.append(forwardDegreeDescription);
                    forwardURI.append(executionPeriodId);
                    forwardURI.append("&degreeID=");
                    forwardURI.append(degreeId);
                } else {
                    forwardURI.append(notFoundURI);
                }

            } catch (FenixServiceException e1) {
                throw new ServletException(e1);
            } catch (FenixFilterException e) {
                throw new ServletException(e);
            }
        } else {
            forwardURI.append(notFoundURI);
        }
        response.sendRedirect(forwardURI.toString());
    }

    /**
     * @param string
     * @return
     * @throws FenixServiceException
     */
    private boolean isDegree(final String string) throws FenixServiceException, FenixFilterException {

        List<InfoDegree> listDegrees = (List<InfoDegree>) ServiceUtils.executeService(null, "ReadDegrees", null);

        if (listDegrees != null) {
            for (final InfoDegree infoDegree : listDegrees) {
                if (infoDegree.getSigla().equalsIgnoreCase(string)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @return
     * @throws FenixServiceException
     */
    private InfoExecutionPeriod getCurrentExecutionPeriod() throws FenixServiceException, FenixFilterException {

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(
                null, "ReadCurrentExecutionPeriod", null);
        return infoExecutionPeriod;
    }

    /**
     * @param degreeCode
     * @return
     * @throws FenixServiceException
     */
    private Integer getDegreeId(String degreeCode) throws FenixServiceException, FenixFilterException {
        Object args[] = { degreeCode };
        Integer executionCourseOID = new Integer(0);

        executionCourseOID = (Integer) ServiceUtils.executeService(null,
                "ReadDegreeIdInternalByDegreeCode", args);

        return executionCourseOID;
    }

}
