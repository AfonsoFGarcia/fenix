package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.stm.Transaction;

public class CloseTransactionFilter implements Filter {

	private class PublicRequester implements IUserView {
		private class InformationNotAvailable extends RuntimeException {
			private static final long serialVersionUID = -7527899059986512251L;

			public InformationNotAvailable(String msg) {
				super(msg);
			}
		}

		public Person getPerson() {
			throw new InformationNotAvailable(
					"property person not available on a public requester user view");
		}

		public String getUtilizador() {
			throw new InformationNotAvailable(
					"property person not available on a public requester user view");
		}

		public String getFullName() {
			throw new InformationNotAvailable(
					"property person not available on a public requester user view");
		}

		public Collection getRoles() {
			throw new InformationNotAvailable(
					"property person not available on a public requester user view");
		}

		public boolean hasRoleType(RoleType roleType) {
			throw new InformationNotAvailable(
					"property person not available on a public requester user view");
		}

		public boolean isPublicRequester() {
			return true;
		}
	}

	public void init(FilterConfig config) {

	}

	public void destroy() {

	}

	protected IUserView getUserView(ServletRequest request) {
		IUserView userView = null;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession(false);
			if (session != null)
				userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		}

		if (userView == null)
			userView = new PublicRequester();

		return userView;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        try {
            if (Transaction.currentFenixTransaction() == null) {
                Transaction.begin();
                Transaction.currentFenixTransaction().setReadOnly();
           }
            setTransactionOwner(request);
			chain.doFilter(request, response);
		} finally {
			Transaction.forceFinish();
		}
	}

	/**
	 * 
	 */
	private void setTransactionOwner(ServletRequest request) {
		AccessControl.setUserView(this.getUserView(request));
	}
}
