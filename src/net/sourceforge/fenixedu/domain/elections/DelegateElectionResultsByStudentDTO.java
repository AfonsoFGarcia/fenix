package net.sourceforge.fenixedu.domain.elections;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.student.Student;

public class DelegateElectionResultsByStudentDTO extends DataTranferObject {
    private Student student;
    private DelegateElection election;
    private Integer votesNumber;
    private Double votesPercentage;
    private Double votesRelativePercentage;

    public DelegateElectionResultsByStudentDTO(DelegateElection election, Student Student) {
	setElection(election);
	setStudent(Student);
	setVotesNumber(1);
	setVotesPercentage(0.0);
	setVotesRelativePercentage(0.0);
    }

    public void calculateResults(int totalStudentsCount, int totalVoteCount) {
	double percentage = ((double) getVotesNumber() / (double) totalStudentsCount);
	setVotesPercentage(((int) (percentage * 100) / 100.0) * 100);
	double relativePercentage = ((double) getVotesNumber() / (double) totalVoteCount);
	setVotesRelativePercentage(((int) (relativePercentage * 100) / 100.0) * 100);
    }

    public Student getStudent() {
	return (student);
    }

    public void setStudent(Student student) {
	this.student = student;
    }

    public DelegateElection getElection() {
	return (election);
    }

    public void setElection(DelegateElection election) {
	this.election = election;
    }

    public Integer getVotesNumber() {
	return votesNumber;
    }

    public void setVotesNumber(Integer votesNumber) {
	this.votesNumber = votesNumber;
    }

    public Double getVotesPercentage() {
	return votesPercentage;
    }

    public void setVotesPercentage(Double votesPercentage) {
	this.votesPercentage = votesPercentage;
    }

    public Double getVotesRelativePercentage() {
	return votesRelativePercentage;
    }

    public void setVotesRelativePercentage(Double votesRelativePercentage) {
	this.votesRelativePercentage = votesRelativePercentage;
    }

    public boolean getIsElectedStudent() {
	return (getElection().hasElectedStudent() && getElection().getElectedStudent().equals(getStudent()) ? true : false);
    }
}
