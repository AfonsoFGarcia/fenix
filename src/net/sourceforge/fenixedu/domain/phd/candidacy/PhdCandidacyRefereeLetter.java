package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.ist.fenixWebFramework.services.Service;

public class PhdCandidacyRefereeLetter extends PhdCandidacyRefereeLetter_Base {

    private PhdCandidacyRefereeLetter() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public PhdCandidacyRefereeLetter(final PhdCandidacyReferee referee, final PhdCandidacyRefereeLetterBean bean) {
	this();
	edit(referee, bean);
    }

    private void edit(final PhdCandidacyReferee referee, final PhdCandidacyRefereeLetterBean bean) {
	check(referee, "error.PhdCandidacyRefereeLetter.invalid.referee");
	check(referee.getPhdProgramCandidacyProcess(), "error.PhdCandidacyRefereeLetter.invalid.process");

	check(bean.getOverallPromise(), "error.PhdCandidacyRefereeLetter.invalid.overallPromise");
	check(bean.getComparisonGroup(), "error.PhdCandidacyRefereeLetter.invalid.comparisonGroup");
	checkOr(bean.getRankValue(), bean.getRank(), "error.PhdCandidacyRefereeLetter.invalid.rank");

	setCandidacyReferee(referee);
	setPhdProgramCandidacyProcess(referee.getPhdProgramCandidacyProcess());

	setOverallPromise(bean.getOverallPromise());
	setComparisonGroup(bean.getComparisonGroup());
	setRankValue(bean.getRankValue());
	setRank(bean.getRank());

	setComments(bean.getComments());
	if (bean.hasFileContent()) {
	    if (hasFile()) {
		getFile().delete();
	    }
	    setFile(new PhdCandidacyRefereeLetterFile(getPhdProgramCandidacyProcess(), bean.getFilename(), bean.getFileContent()));
	}

	setRefereeName(bean.getRefereeName());
	setRefereePosition(bean.getRefereePosition());
	setRefereeInstitution(bean.getRefereeInstitution());
	setRefereeAddress(bean.getRefereeAddress());
	setRefereeCity(bean.getRefereeCity());
	setRefereeZipCode(bean.getRefereeZipCode());
	setRefereeCountry(bean.getRefereeCountry());
	setRefereePhone(bean.getRefereePhone());
	setDate(bean.getDate());
    }

    private void edit(final PhdCandidacyRefereeLetterBean bean) {
	edit(getCandidacyReferee(), bean);
    }

    private void checkOr(final String rankValue, final ApplicantRank rank, final String message) {
	if (StringUtils.isEmpty(rankValue) && rank == null) {
	    throw new DomainException(message);
	}
	if (!StringUtils.isEmpty(rankValue) && rank != null) {
	    throw new DomainException(message);
	}
    }

    public String getRefereeEmail() {
	return getCandidacyReferee().getEmail();
    }

    public void delete() {
	getFile().delete();
	removeRefereeCountry();
	removeCandidacyReferee();
	removePhdProgramCandidacyProcess();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    @Service
    static public PhdCandidacyRefereeLetter createOrEdit(PhdCandidacyRefereeLetterBean bean) {
	if (bean.hasLetter()) {
	    bean.getLetter().edit(bean);
	    return bean.getLetter();
	} else {
	    return new PhdCandidacyRefereeLetter(bean.getCandidacyReferee(), bean);
	}
    }

}
