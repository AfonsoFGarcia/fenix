/*
 * Created on 21/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoCurricularCourseScope;
import Dominio.Branch;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.IBranch;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Data;

/**
 * @author lmac1
 */
public class EditCurricularCourseScope implements IServico {

	private static EditCurricularCourseScope service = new EditCurricularCourseScope();

	public static EditCurricularCourseScope getService() {
		return service;
	}

	private EditCurricularCourseScope() {
	}

	public final String getNome() {
		return "EditCurricularCourseScope";
	}

	public void run(InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException {

		ICurricularCourseScope oldCurricularCourseScope = null;
		ICurricularSemester newCurricularSemester = null;
		IBranch newBranch = null;

		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentBranch persistentBranch = ps.getIPersistentBranch();
			IPersistentCurricularSemester persistentCurricularSemester = ps.getIPersistentCurricularSemester();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = ps.getIPersistentCurricularCourseScope();
			
			Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();
			IBranch branch = new Branch();
			branch.setIdInternal(branchId);
			newBranch = (IBranch) persistentBranch.readByOId(branch, false);
			
			if(newBranch == null)
				throw new NonExistingServiceException("message.non.existing.branch", null);

			Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal();
			ICurricularSemester curricularSemester = new CurricularSemester();
			curricularSemester.setIdInternal(curricularSemesterId);
			newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOId(curricularSemester, false);

			if(newCurricularSemester == null)
				throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
				
			ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setIdInternal(newInfoCurricularCourseScope.getIdInternal());
			oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, true);
			
			if(oldCurricularCourseScope == null)
				throw new NonExistingServiceException("message.non.existing.curricular.course.scope", null);

			oldCurricularCourseScope.setCredits(newInfoCurricularCourseScope.getCredits());
			oldCurricularCourseScope.setTheoreticalHours(newInfoCurricularCourseScope.getTheoreticalHours());
			oldCurricularCourseScope.setPraticalHours(newInfoCurricularCourseScope.getPraticalHours());
			oldCurricularCourseScope.setTheoPratHours(newInfoCurricularCourseScope.getTheoPratHours());
			oldCurricularCourseScope.setLabHours(newInfoCurricularCourseScope.getLabHours());
			oldCurricularCourseScope.setMaxIncrementNac(newInfoCurricularCourseScope.getMaxIncrementNac());
			oldCurricularCourseScope.setMinIncrementNac(newInfoCurricularCourseScope.getMinIncrementNac());
			oldCurricularCourseScope.setWeigth(newInfoCurricularCourseScope.getWeigth());

			oldCurricularCourseScope.setBranch(newBranch);
			//it already includes the curricular year
			oldCurricularCourseScope.setCurricularSemester(newCurricularSemester);
			oldCurricularCourseScope.setEctsCredits(newInfoCurricularCourseScope.getEctsCredits());
			oldCurricularCourseScope.setBeginDate(newInfoCurricularCourseScope.getBeginDate());
			oldCurricularCourseScope.setEndDate(null);
			
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException("O �mbito com ramo "
			+ newBranch.getCode()
			+ ", do "
			+ newCurricularSemester.getCurricularYear().getYear()
			+ "� ano, "
			+ newCurricularSemester.getSemester()
			+ "� semestre");
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
