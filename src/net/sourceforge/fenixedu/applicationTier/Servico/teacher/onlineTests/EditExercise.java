/*
 * Created on 27/Fev/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class EditExercise extends Service {

	public boolean run(Integer executionCourseId, Integer metadataId, String author, String description,
			String difficulty, Calendar learningTime, String level, String mainSubject,
			String secondarySubject) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();

		Metadata metadata = (Metadata) persistentMetadata.readByOID(Metadata.class, metadataId);
		if (metadata == null)
			throw new InvalidArgumentsServiceException();
		if (author != null)
			metadata.setAuthor(author);
		if (!difficulty.equals("-1"))
			metadata.setDifficulty(difficulty);
		metadata.setDescription(description);
		metadata.setLearningTime(learningTime);
		metadata.setLevel(level);
		metadata.setMainSubject(mainSubject);
		metadata.setSecondarySubject(secondarySubject);

		return true;
	}

}