package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ResearchJournalIssueCreationBean implements Serializable {

    private ScientificJournal journal;

    private JournalIssue issue;

    private String scientificJournalName;

    private String journalIssueName;

    private ResearchActivityParticipationRole role;

    private MultiLanguageString roleMessage;

    public MultiLanguageString getRoleMessage() {
        return roleMessage;
    }

    public void setRoleMessage(MultiLanguageString roleMessage) {
        this.roleMessage = roleMessage;
    }

    public ResearchJournalIssueCreationBean() {
        this.issue = null;
        this.journal = null;
    }

    public JournalIssue getJournalIssue() {
        return issue;
    }

    public void setJournalIssue(JournalIssue issue) {
        this.issue = issue;
    }

    public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
        this.role = role;
    }

    public String getJournalIssueName() {
        return journalIssueName;
    }

    public void setJournalIssueName(String journalIssueName) {
        this.journalIssueName = journalIssueName;
    }

    public ScientificJournal getScientificJournal() {
        return this.journal;
    }

    public void setScientificJournal(ScientificJournal journal) {
        this.journal = journal;
    }

    public String getScientificJournalName() {
        return scientificJournalName;
    }

    public void setScientificJournalName(String scientificJournalName) {
        this.scientificJournalName = scientificJournalName;
    }
}
