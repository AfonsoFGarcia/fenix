package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Fernanda Quit�rio
 */
public class InsertSection implements IServico {

	private static InsertSection service = new InsertSection();

	public static InsertSection getService() {

		return service;
	}

	private InsertSection() {
	}

	public final String getNome() {

		return "InsertSection";
	}

	private int organizeExistingSectionsOrder(ISection superiorSection, ISite site, int insertSectionOrder)
		throws FenixServiceException {

		IPersistentSection persistentSection = null;
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			persistentSection = persistentSuport.getIPersistentSection();

			List sectionsList = persistentSection.readBySiteAndSection(site, superiorSection);

			if (sectionsList != null) {

				if (insertSectionOrder == -1) {
					insertSectionOrder = sectionsList.size();
				}

				Iterator iterSections = sectionsList.iterator();
				while (iterSections.hasNext()) {

					ISection iterSection = (ISection) iterSections.next();
					int sectionOrder = iterSection.getSectionOrder().intValue();

					if (sectionOrder >= insertSectionOrder) {
						persistentSection.simpleLockWrite(iterSection);
						iterSection.setSectionOrder(new Integer(sectionOrder + 1));

					}

				}
			}
		} catch (ExistingPersistentException excepcaoPersistencia) {
			throw new ExistingServiceException(excepcaoPersistencia);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}
		return insertSectionOrder;
	}

	//infoItem with an infoSection

	public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String sectionName, Integer sectionOrder)
		throws FenixServiceException {

		ISite site = null;
		ISection section = null;
		ISection fatherSection = null;

		try {

			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSuport.getIDisciplinaExecucaoPersistente();
			IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
			IPersistentSection persistentSection = persistentSuport.getIPersistentSection();

			DisciplinaExecucao executionCourse = new DisciplinaExecucao(infoExecutionCourseCode);
			IDisciplinaExecucao iExecutionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(executionCourse, false);
			ISite iSite = persistentSite.readByExecutionCourse(iExecutionCourse);

			ISection parentSection = null;
			if (sectionCode != null) {
				parentSection = (ISection) persistentSection.readByOId(new Section(sectionCode), false);
			}

			sectionOrder = new Integer(organizeExistingSectionsOrder(parentSection, iSite, sectionOrder.intValue()));

			Calendar calendario = Calendar.getInstance();
			section = new Section();
			section.setSuperiorSection(parentSection);
			section.setSectionOrder(sectionOrder);
			section.setName(sectionName);
			section.setSite(iSite);
			section.setLastModifiedDate(calendario.getTime());

			persistentSection.lockWrite(section);

		} catch (ExistingPersistentException excepcaoPersistencia) {

			throw new ExistingServiceException(excepcaoPersistencia);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}

		return new Boolean(true);
	}
}