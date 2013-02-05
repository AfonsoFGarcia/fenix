package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;

public class CourseLoadBean implements Serializable {

    private ExecutionCourse executionCourseReference;
    private ShiftType type;
    private BigDecimal totalQuantity;
    private BigDecimal unitQuantity;

    public CourseLoadBean(ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
    }

    public ExecutionCourse getExecutionCourse() {
        return this.executionCourseReference;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourseReference = executionCourse;
    }

    public ShiftType getType() {
        return type;
    }

    public void setType(ShiftType type) {
        this.type = type;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(BigDecimal unitQuantity) {
        this.unitQuantity = unitQuantity;
    }
}
