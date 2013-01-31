/*
 * Created on May 5, 2004
 */

package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractRegime extends InfoObject {

	private static final int activeState = 1;

	private static final int inactiveState = 0;

	private Integer state;

	private Date dateBeginContract;

	private Date dateEndContract;

	private Date dateSendDispatchCC;

	private Date dateDispatchCC;

	private Date dateSendDispatchCD;

	private Date dateDispatchCD;

	private Integer costCenterKey;

	private InfoTeacher infoTeacher;

	private InfoGrantContract infoGrantContract;

	private InfoGrantCostCenter grantCostCenterInfo;

	private GrantContract grantContract;

	/**
	 * @return Returns the costCenterKey.
	 */
	public Integer getCostCenterKey() {
		return costCenterKey;
	}

	/**
	 * @param costCenterKey
	 *            The costCenterKey to set.
	 */
	public void setCostCenterKey(Integer costCenterKey) {
		this.costCenterKey = costCenterKey;
	}

	/**
	 * @return Returns the grantCostCenterInfo.
	 */
	public InfoGrantCostCenter getGrantCostCenterInfo() {
		return grantCostCenterInfo;
	}

	/**
	 * @param grantCostCenterInfo
	 *            The grantCostCenterInfo to set.
	 */
	public void setGrantCostCenterInfo(InfoGrantCostCenter grantCostCenterInfo) {
		this.grantCostCenterInfo = grantCostCenterInfo;
	}

	/**
	 * @return Returns the dateBeginContract.
	 */
	public Date getDateBeginContract() {
		return dateBeginContract;
	}

	/**
	 * @param dateBeginContract
	 *            The dateBeginContract to set.
	 */
	public void setDateBeginContract(Date dateBeginContract) {
		this.dateBeginContract = dateBeginContract;
	}

	/**
	 * @return Returns the dateDispatchCC.
	 */
	public Date getDateDispatchCC() {
		return dateDispatchCC;
	}

	/**
	 * @param dateDispatchCC
	 *            The dateDispatchCC to set.
	 */
	public void setDateDispatchCC(Date dateDispatchCC) {
		this.dateDispatchCC = dateDispatchCC;
	}

	/**
	 * @return Returns the dateDispatchCD.
	 */
	public Date getDateDispatchCD() {
		return dateDispatchCD;
	}

	/**
	 * @param dateDispatchCD
	 *            The dateDispatchCD to set.
	 */
	public void setDateDispatchCD(Date dateDispatchCD) {
		this.dateDispatchCD = dateDispatchCD;
	}

	/**
	 * @return Returns the dateEndContract.
	 */
	public Date getDateEndContract() {
		return dateEndContract;
	}

	/**
	 * @param dateEndContract
	 *            The dateEndContract to set.
	 */
	public void setDateEndContract(Date dateEndContract) {
		this.dateEndContract = dateEndContract;
	}

	/**
	 * @return Returns the dateSendDispatchCC.
	 */
	public Date getDateSendDispatchCC() {
		return dateSendDispatchCC;
	}

	/**
	 * @param dateSendDispatchCC
	 *            The dateSendDispatchCC to set.
	 */
	public void setDateSendDispatchCC(Date dateSendDispatchCC) {
		this.dateSendDispatchCC = dateSendDispatchCC;
	}

	/**
	 * @return Returns the dateSendDispatchCD.
	 */
	public Date getDateSendDispatchCD() {
		return dateSendDispatchCD;
	}

	/**
	 * @param dateSendDispatchCD
	 *            The dateSendDispatchCD to set.
	 */
	public void setDateSendDispatchCD(Date dateSendDispatchCD) {
		this.dateSendDispatchCD = dateSendDispatchCD;
	}

	/**
	 * @return Returns the teacher.
	 */
	public InfoTeacher getInfoTeacher() {
		return infoTeacher;
	}

	/**
	 * @param teacher
	 *            The teacher to set.
	 */
	public void setInfoTeacher(InfoTeacher teacher) {
		this.infoTeacher = teacher;
	}

	/**
	 * @return Returns the state.
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * @return Returns the infoGrantContract.
	 */
	public InfoGrantContract getInfoGrantContract() {
		return infoGrantContract;
	}

	/**
	 * @param infoGrantContract
	 *            The infoGrantContract to set.
	 */
	public void setInfoGrantContract(InfoGrantContract infoGrantContract) {
		this.infoGrantContract = infoGrantContract;
	}

	public boolean getContractRegimeActive() {
		if (this.dateEndContract.after(Calendar.getInstance().getTime())) {
			return true;
		}
		return false;

	}

	public Integer getActiveStateValue() {
		return Integer.valueOf(activeState);
	}

	public Integer getInactiveStateValue() {
		return Integer.valueOf(inactiveState);
	}

	/**
	 * @return Returns the activeState.
	 */
	public static Integer getActiveState() {
		return Integer.valueOf(activeState);
	}

	/**
	 * @return Returns the inactiveState.
	 */
	public static Integer getInactiveState() {
		return Integer.valueOf(inactiveState);
	}

	/**
	 * @param GrantContractRegime
	 */
	public void copyFromDomain(GrantContractRegime grantContractRegime) {
		super.copyFromDomain(grantContractRegime);
		if (grantContractRegime != null) {
			setState(grantContractRegime.getState());
			setDateBeginContract(grantContractRegime.getDateBeginContract());
			setDateEndContract(grantContractRegime.getDateEndContract());
			setDateDispatchCC(grantContractRegime.getDateDispatchCC());
			setDateDispatchCD(grantContractRegime.getDateDispatchCD());
			setDateSendDispatchCC(grantContractRegime.getDateSendDispatchCC());
			setDateSendDispatchCD(grantContractRegime.getDateSendDispatchCD());
			if (grantContractRegime.getGrantContract().getGrantCostCenter() != null) {

				setCostCenterKey(grantContractRegime.getGrantContract().getGrantCostCenter().getIdInternal());
				setGrantCostCenterInfo(InfoGrantCostCenter.newInfoFromDomain(grantContractRegime.getGrantContract()
						.getGrantCostCenter()));

			}

		}
	}

	/**
	 * @param GrantContractRegime
	 * @return
	 */
	public static InfoGrantContractRegime newInfoFromDomain(GrantContractRegime grantContractRegime) {
		InfoGrantContractRegime infoGrantContractRegime = null;

		if (grantContractRegime != null) {
			infoGrantContractRegime = new InfoGrantContractRegime();
			infoGrantContractRegime.copyFromDomain(grantContractRegime);
		}
		return infoGrantContractRegime;
	}

	public GrantContract getGrantContract() {
		return grantContract;
	}

	public void setGrantContract(GrantContract grantContract) {
		this.grantContract = grantContract;
	}

}
