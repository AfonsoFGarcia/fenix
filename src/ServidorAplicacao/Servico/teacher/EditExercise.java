/*
 * Created on 27/Fev/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import Dominio.IMetadata;
import Dominio.Metadata;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 *
 * @author Susana Fernandes
 *
 */
public class EditExercise implements IServico
{
	private static EditExercise service = new EditExercise();

	public static EditExercise getService()
	{
		return service;
	}

	public EditExercise()
	{
	}

	public String getNome()
	{
		return "EditExercise";
	}

	public boolean run(
		Integer executionCourseId,
		Integer metadataId,
		String author,
		String description,
		String difficulty,
		Calendar learningTime,
		String level,
		String mainSubject,
		String secondarySubject)
		throws FenixServiceException
	{

		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

			IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();

			IMetadata metadata =
				(IMetadata) persistentMetadata.readByOId(new Metadata(metadataId), true);
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
			persistentMetadata.simpleLockWrite(metadata);
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		return true;
	}

}
