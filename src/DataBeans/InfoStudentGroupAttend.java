/*
 * Created on 22/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InfoStudentGroupAttend {
		
		private InfoFrequenta infoAttend;
		private InfoStudentGroup infoStudentGroup;
	
	
		/** 
		* Construtor
		*/
	
		public InfoStudentGroupAttend() {}
	
		/** 
		 * Construtor
		 */
		public InfoStudentGroupAttend(InfoStudentGroup infoStudentGroup,InfoFrequenta infoAttend) {
		
			this.infoStudentGroup = infoStudentGroup;
			this.infoAttend = infoAttend;
		}
	
	
	
		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object arg0) {
			boolean result = false;
				if (arg0 instanceof InfoStudentGroupAttend) {
					result = (getInfoStudentGroup().equals(((InfoStudentGroupAttend) arg0).getInfoStudentGroup()))&&
							 (getInfoAttend().equals(((InfoStudentGroupAttend) arg0).getInfoAttend()));
				} 
				return result;		
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			String result = "[INFO_STUDENT_GROUP_ATTEND";
			result += ", infoStudentGroup=" + getInfoStudentGroup();
			result += ", infoAttend=" + getInfoAttend();
			result += "]";
			return result;
		}

	
		
		/**
		* @return InfoStudentGroup
		*/
		public InfoStudentGroup getInfoStudentGroup() {
			return infoStudentGroup;
		}
		
		/**
		* @return InfoFrequenta
		*/
		public InfoFrequenta getInfoAttend() {
			return infoAttend;
		}

		
		/**
		* Sets the infoStudentGroup.
		* @param infoStudentGroup The infoStudentGroup to set
		*/
		public void setInfoStudentGroup(InfoStudentGroup infoStudentGroup) {
			this.infoStudentGroup = infoStudentGroup;
		}

		/**
		* Sets the infoAttend.
		* @param infoAttend The infoAttend to set
		*/
		public void setInfoAttend(InfoFrequenta infoAttend) {
			this.infoAttend = infoAttend;
		}

}
