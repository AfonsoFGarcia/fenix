package net.sourceforge.fenixedu.domain;

public class ProfessorshipPermissions extends ProfessorshipPermissions_Base {
    
    public  ProfessorshipPermissions() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPersonalization(true);
	setSiteArchive(true);
	setAnnouncements(true);
	setSections(true);
	setSummaries(true);
	setStudents(true);
	setPlanning(true);
	setEvaluationSpecific(true);
	setEvaluationWorksheets(true);
	setEvaluationProject(true);
	setEvaluationTests(true);
	setEvaluationExams(true);
	setEvaluationFinal(true);
	setWorksheets(true);
	setGroups(true);
	setShift(true);
	
	setEvaluationMethod(true);
	setBibliografy(true);
    }
    
    public ProfessorshipPermissions copyPremissions(){
	ProfessorshipPermissions p = new ProfessorshipPermissions();
        p.setPersonalization(getPersonalization());
        p.setSiteArchive(getSiteArchive());
        p.setAnnouncements(getAnnouncements());
        p.setSections(getSections());
        p.setSummaries(getSummaries());
        p.setStudents(getStudents());
        p.setPlanning(getPlanning());
        p.setEvaluationSpecific(getEvaluationSpecific());
        p.setEvaluationWorksheets(getEvaluationWorksheets());
        p.setEvaluationProject(getEvaluationProject());
        p.setEvaluationTests(getEvaluationTests());
        p.setEvaluationExams(getEvaluationExams());
        p.setWorksheets(getWorksheets());
        p.setGroups(getGroups());
        p.setShift(getShift());
	
        p.setEvaluationMethod(getEvaluationMethod());
        p.setBibliografy(getBibliografy());
        return p;
    }
    
}
