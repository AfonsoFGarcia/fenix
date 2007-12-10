package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

public enum TSDProcessPhaseStatus {
	OPEN,
	CLOSED,
	CURRENT;
	
	public String getName() {
		return name();
	}
	
	public String getQualifiedName()
	{
		return getClass().getSimpleName() + "." + name();
	}
	
	public String getFullyQualifiedName()
	{
		return getClass().getName() + "." + name();
	}
}
