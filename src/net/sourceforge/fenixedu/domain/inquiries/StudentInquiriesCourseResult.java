package net.sourceforge.fenixedu.domain.inquiries;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class StudentInquiriesCourseResult extends StudentInquiriesCourseResult_Base {

    public StudentInquiriesCourseResult() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isUnsatisfactory() {
	return getUnsatisfactoryResultsCUEvaluation() || getUnsatisfactoryResultsCUOrganization();
    }

    @Override
    public Boolean getUnsatisfactoryResultsCUEvaluation() {
	return super.getUnsatisfactoryResultsCUEvaluation() != null && super.getUnsatisfactoryResultsCUEvaluation();
    }

    @Override
    public Boolean getUnsatisfactoryResultsCUOrganization() {
	return super.getUnsatisfactoryResultsCUOrganization() != null && super.getUnsatisfactoryResultsCUOrganization();
    }

    @Override
    public Boolean getAuditCU() {
	return super.getAuditCU() != null && super.getAuditCU();
    }

    private Double getValueForPresentation(Double value) {
	//TODO: ugly hack, refactor
	if(value == null){
	    return new Double(0);
	}
	BigDecimal round = new BigDecimal(value);
	round.setScale(2, RoundingMode.HALF_EVEN);
	return round.doubleValue();
    }

    public Double getEvaluatedRatioForPresentation() {
	return getValueForPresentation(super.getEvaluatedRatio());
    }

    public Double getApprovedRatioForPresentation() {
	return getValueForPresentation(super.getApprovedRatio());
    }

    public Double getValidInitialFormAnswersNumberForPresentation() {
	return getValueForPresentation(super.getValidInitialFormAnswersNumber());
    }

    public Double getValidInitialFormAnswersRatioForPresentation() {
	return getValueForPresentation(super.getValidInitialFormAnswersRatio());
    }

    public Double getValidInquiryAnswersNumberForPresentation() {
	return getValueForPresentation(super.getValidInquiryAnswersNumber());
    }

    public Double getValidInquiryAnswersRatioForPresentation() {
	return getValueForPresentation(super.getValidInquiryAnswersRatio());
    }

    public Double getNoInquiryAnswersNumberForPresentation() {
	return getValueForPresentation(super.getNoInquiryAnswersNumber());
    }

    public Double getNoInquiryAnswersRatioForPresentation() {
	return getValueForPresentation(super.getNoInquiryAnswersRatio());
    }

    public Double getInvalidInquiryAnswersNumberForPresentation() {
	return getValueForPresentation(super.getInvalidInquiryAnswersNumber());
    }

    public Double getInvalidInquiryAnswersRatioForPresentation() {
	return getValueForPresentation(super.getInvalidInquiryAnswersRatio());
    }

    public Double getGradeAverageForPresentation() {
	return getValueForPresentation(super.getGradeAverage());
    }

    public Double getScheduleLoadForPresentation() {
	return getValueForPresentation(super.getScheduleLoad());
    }

    public Double getEctsForPresentation() {
	return getValueForPresentation(super.getEcts());
    }

    public Double getEstimatedEctsAverageForPresentation() {
	return getValueForPresentation(super.getEstimatedEctsAverage());
    }

    public Double getEstimatedEctsStandardDeviationForPresentation() {
	return getValueForPresentation(super.getEstimatedEctsStandardDeviation());
    }

    public Double getAverage_NHTAForPresentation() {
	return getValueForPresentation(super.getAverage_NHTA());
    }

    public Double getStandardDeviation_NHTAForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_NHTA());
    }

    public Double getAverage_perc_weeklyHoursForPresentation() {
	return getValueForPresentation(super.getAverage_perc_weeklyHours());
    }

    public Double getStandardDeviation_perc_NHTAForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_perc_NHTA());
    }

    public Double getAverage_NDEForPresentation() {
	return getValueForPresentation(super.getAverage_NDE());
    }

    public Double getStandardDeviation_NDEForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_NDE());
    }

    public Double getNumber_P1_1ForPresentation() {
	return getValueForPresentation(super.getNumber_P1_1());
    }

    public Double getPerc_10_12ForPresentation() {
	return getValueForPresentation(super.getPerc_10_12());
    }

    public Double getPerc_13_14ForPresentation() {
	return getValueForPresentation(super.getPerc_13_14());
    }

    public Double getPerc_15_16ForPresentation() {
	return getValueForPresentation(super.getPerc_15_16());
    }

    public Double getPerc_17_18ForPresentation() {
	return getValueForPresentation(super.getPerc_17_18());
    }

    public Double getPerc_19_20ForPresentation() {
	return getValueForPresentation(super.getPerc_19_20());
    }

    public Double getPerc_flunkedForPresentation() {
	return getValueForPresentation(super.getPerc_flunked());
    }

    public Double getPerc_nonEvaluatedForPresentation() {
	return getValueForPresentation(super.getPerc_nonEvaluated());
    }

    public Double getNumber_P1_2_aForPresentation() {
	return getValueForPresentation(super.getNumber_P1_2_a());
    }

    public Double getPerc__P1_2_aForPresentation() {
	return getValueForPresentation(super.getPerc__P1_2_a());
    }

    public Double getNumber_P1_2_bForPresentation() {
	return getValueForPresentation(super.getNumber_P1_2_b());
    }

    public Double getPerc__P1_2_bForPresentation() {
	return getValueForPresentation(super.getPerc__P1_2_b());
    }

    public Double getNumber_P1_2_cForPresentation() {
	return getValueForPresentation(super.getNumber_P1_2_c());
    }

    public Double getPerc__P1_2_cForPresentation() {
	return getValueForPresentation(super.getPerc__P1_2_c());
    }

    public Double getNumber_P1_2_dForPresentation() {
	return getValueForPresentation(super.getNumber_P1_2_d());
    }

    public Double getPerc__P1_2_dForPresentation() {
	return getValueForPresentation(super.getPerc__P1_2_d());
    }

    public Double getNumber_P1_2_eForPresentation() {
	return getValueForPresentation(super.getNumber_P1_2_e());
    }

    public Double getPerc__P1_2_eForPresentation() {
	return getValueForPresentation(super.getPerc__P1_2_e());
    }

    public Double getNumber_P_1_2_fForPresentation() {
	return getValueForPresentation(super.getNumber_P_1_2_f());
    }

    public Double getPerc__P1_2_fForPresentation() {
	return getValueForPresentation(super.getPerc__P1_2_f());
    }

    public Double getNumber_P1_2_gForPresentation() {
	return getValueForPresentation(super.getNumber_P1_2_g());
    }

    public Double getPerc__P1_2_gForPresentation() {
	return getValueForPresentation(super.getPerc__P1_2_g());
    }

    public Double getAverage_P1_3ForPresentation() {
	return getValueForPresentation(super.getAverage_P1_3());
    }

    public Double getStandardDeviation_P1_3ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P1_3());
    }

    public Double getPerc_P1_3_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_3_1());
    }

    public Double getPerc_P1_3_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_3_2());
    }

    public Double getPerc_P1_3_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_3_3());
    }

    public Double getPerc_P1_3_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_3_4());
    }

    public Double getPerc_P1_3_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_3_5());
    }

    public Double getPerc_P1_3_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_3_6());
    }

    public Double getPerc_P1_3_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_3_7());
    }

    public Double getPerc_P1_3_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_3_8());
    }

    public Double getPerc_P1_3_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_3_9());
    }

    public Double getAverage_P1_4ForPresentation() {
	return getValueForPresentation(super.getAverage_P1_4());
    }

    public Double getStandardDeviation_P1_4ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P1_4());
    }

    public Double getPerc_P1_4_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_4_1());
    }

    public Double getPerc_P1_4_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_4_2());
    }

    public Double getPerc_P1_4_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P1_4_3());
    }

    public Double getAverage_P2_1ForPresentation() {
	return getValueForPresentation(super.getAverage_P2_1());
    }

    public Double getStandardDeviation_P2_1ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P2_1());
    }

    public Double getPerc_P2_1_0ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_1_0());
    }

    public Double getPerc_P2_1_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_1_1());
    }

    public Double getPerc_P2_1_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_1_2());
    }

    public Double getPerc_P2_1_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_1_3());
    }

    public Double getAverage_P2_2ForPresentation() {
	return getValueForPresentation(super.getAverage_P2_2());
    }

    public Double getStandardDeviation_P2_2ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P2_2());
    }

    public Double getPerc_P2_2_0ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_2_0());
    }

    public Double getPerc_P2_2_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_2_1());
    }

    public Double getPerc_P2_2_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_2_2());
    }

    public Double getPerc_P2_2_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_2_3());
    }

    public Double getAverage_P2_3ForPresentation() {
	return getValueForPresentation(super.getAverage_P2_3());
    }

    public Double getStandardDeviation_P2_3ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P2_3());
    }

    public Double getPerc_P2_3_0ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_3_0());
    }

    public Double getPerc_P2_3_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_3_1());
    }

    public Double getPerc_P2_3_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_3_2());
    }

    public Double getPerc_P2_3_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_3_3());
    }

    public Double getAverage_P2_4ForPresentation() {
	return getValueForPresentation(super.getAverage_P2_4());
    }

    public Double getStandardDeviation_P2_4ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P2_4());
    }

    public Double getPerc_P2_4_0ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_4_0());
    }

    public Double getPerc_P2_4_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_4_1());
    }

    public Double getPerc_P2_4_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_4_2());
    }

    public Double getPerc_P2_4_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P2_4_3());
    }

    public Double getAverage_P3_1ForPresentation() {
	return getValueForPresentation(super.getAverage_P3_1());
    }

    public Double getStandardDeviation_P3_1ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P3_1());
    }

    public Double getPerc_P3_1_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_1_1());
    }

    public Double getPerc_P3_1_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_1_2());
    }

    public Double getPerc_P3_1_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_1_3());
    }

    public Double getPerc_P3_1_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_1_4());
    }

    public Double getPerc_P3_1_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_1_5());
    }

    public Double getPerc_P3_1_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_1_6());
    }

    public Double getPerc_P3_1_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_1_7());
    }

    public Double getPerc_P3_1_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_1_8());
    }

    public Double getPerc_P3_1_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_1_9());
    }

    public Double getAverage_P3_2ForPresentation() {
	return getValueForPresentation(super.getAverage_P3_2());
    }

    public Double getStandardDeviation_P3_2ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P3_2());
    }

    public Double getPerc_P3_2_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_2_1());
    }

    public Double getPerc_P3_2_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_2_2());
    }

    public Double getPerc_P3_2_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_2_3());
    }

    public Double getPerc_P3_2_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_2_4());
    }

    public Double getPerc_P3_2_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_2_5());
    }

    public Double getPerc_P3_2_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_2_6());
    }

    public Double getPerc_P3_2_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_2_7());
    }

    public Double getPerc_P3_2_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_2_8());
    }

    public Double getPerc_P3_2_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_2_9());
    }

    public Double getAverage_P3_3ForPresentation() {
	return getValueForPresentation(super.getAverage_P3_3());
    }

    public Double getStandardDeviation_P3_3ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P3_3());
    }

    public Double getPerc_P3_3_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_3_1());
    }

    public Double getPerc_P3_3_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_3_2());
    }

    public Double getPerc_P3_3_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_3_3());
    }

    public Double getPerc_P3_3_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_3_4());
    }

    public Double getPerc_P3_3_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_3_5());
    }

    public Double getPerc_P3_3_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_3_6());
    }

    public Double getPerc_P3_3_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_3_7());
    }

    public Double getPerc_P3_3_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_3_8());
    }

    public Double getPerc_P3_3_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_3_9());
    }

    public Double getAverage_P3_4ForPresentation() {
	return getValueForPresentation(super.getAverage_P3_4());
    }

    public Double getStandardDeviation_P3_4ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P3_4());
    }

    public Double getPerc_P3_4_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_4_1());
    }

    public Double getPerc_P3_4_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_4_2());
    }

    public Double getPerc_P3_4_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_4_3());
    }

    public Double getPerc_P3_4_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_4_4());
    }

    public Double getPerc_P3_4_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_4_5());
    }

    public Double getPerc_P3_4_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_4_6());
    }

    public Double getPerc_P3_4_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_4_7());
    }

    public Double getPerc_P3_4_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_4_8());
    }

    public Double getPerc_P3_4_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P3_4_9());
    }

    public Double getAverage_P4ForPresentation() {
	return getValueForPresentation(super.getAverage_P4());
    }

    public Double getStandardDeviation_P4ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P4());
    }

    public Double getPerc_P4_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P4_1());
    }

    public Double getPerc_P4_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P4_2());
    }

    public Double getPerc_P4_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P4_3());
    }

    public Double getPerc_P4_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P4_4());
    }

    public Double getPerc_P4_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P4_5());
    }

    public Double getPerc_P4_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P4_6());
    }

    public Double getPerc_P4_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P4_7());
    }

    public Double getPerc_P4_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P4_8());
    }

    public Double getPerc_P4_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P4_9());
    }

    public Double getAverage_P5ForPresentation() {
	return getValueForPresentation(super.getAverage_P5());
    }

    public Double getStandardDeviation_P5ForPresentation() {
	return getValueForPresentation(super.getStandardDeviation_P5());
    }

    public Double getPerc_P5_1ForPresentation() {
	return getValueForPresentation(super.getPerc_P5_1());
    }

    public Double getPerc_P5_2ForPresentation() {
	return getValueForPresentation(super.getPerc_P5_2());
    }

    public Double getPerc_P5_3ForPresentation() {
	return getValueForPresentation(super.getPerc_P5_3());
    }

    public Double getPerc_P5_4ForPresentation() {
	return getValueForPresentation(super.getPerc_P5_4());
    }

    public Double getPerc_P5_5ForPresentation() {
	return getValueForPresentation(super.getPerc_P5_5());
    }

    public Double getPerc_P5_6ForPresentation() {
	return getValueForPresentation(super.getPerc_P5_6());
    }

    public Double getPerc_P5_7ForPresentation() {
	return getValueForPresentation(super.getPerc_P5_7());
    }

    public Double getPerc_P5_8ForPresentation() {
	return getValueForPresentation(super.getPerc_P5_8());
    }

    public Double getPerc_P5_9ForPresentation() {
	return getValueForPresentation(super.getPerc_P5_9());
    }

}
