/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package Util;

/**
 * @author jpvl
 */
public class RoleType {
	/* must match with field ROLE_TYPE in table ROLE */
	public static final int PERSON_TYPE = 1;
	public static final int STUDENT_TYPE = 2;
	public static final int TEACHER_TYPE = 3;
	public static final int TIME_TABLE_MANAGER_TYPE = 4;
	public static final int MASTER_DEGREE_CANDIDATE_TYPE = 5;
	public static final int MASTER_DEGREE_ADMINISTRATIVE_OFFICE_TYPE = 6;
	public static final int TREASURY_TYPE = 7;
	public static final int COORDINATOR_TYPE = 8;
	
	public static final RoleType PERSON = new RoleType(RoleType.PERSON_TYPE);
	public static final RoleType STUDENT = new RoleType(RoleType.STUDENT_TYPE);
	public static final RoleType TEACHER = new RoleType(RoleType.TEACHER_TYPE);
	public static final RoleType TIME_TABLE_MANAGER = new RoleType(RoleType.TIME_TABLE_MANAGER_TYPE);
	public static final RoleType MASTER_DEGREE_CANDIDATE = new RoleType(RoleType.MASTER_DEGREE_CANDIDATE_TYPE);
	public static final RoleType MASTER_DEGREE_ADMINISTRATIVE_OFFICE = new RoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_TYPE);
	public static final RoleType TREASURY = new RoleType(RoleType.TREASURY_TYPE);
	public static final RoleType COORDINATOR = new RoleType(RoleType.COORDINATOR_TYPE);
	
	private int id;

	private RoleType (int id){
		this.id = id;
	}
	
	public RoleType(RoleType roleType) {
		this.id = roleType.getId();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return (
			(obj instanceof RoleType) && (((RoleType) obj).getId() == this.id));
	}

	public int getId() {
		return this.id;
	}
	
	public String toString(){
		String result = "Role Type:\n";
		result += "\n  - Role Type : " + id;
		
		return result;		
	}

}
