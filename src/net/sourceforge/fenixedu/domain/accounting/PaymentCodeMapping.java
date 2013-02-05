package net.sourceforge.fenixedu.domain.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class PaymentCodeMapping extends PaymentCodeMapping_Base {

    static public class PaymentCodeMappingBean implements Serializable {
        private ExecutionInterval executionInterval;
        private PaymentCode oldCode;
        private PaymentCode newCode;

        public PaymentCodeMappingBean() {
        }

        public ExecutionInterval getExecutionInterval() {
            return this.executionInterval;
        }

        public void setExecutionInterval(final ExecutionInterval executionInterval) {
            this.executionInterval = executionInterval;
        }

        public boolean hasExecutionInterval() {
            return getExecutionInterval() != null;
        }

        public PaymentCode getOldCode() {
            return this.oldCode;
        }

        public void setOldCode(final PaymentCode oldCode) {
            this.oldCode = oldCode;
        }

        public PaymentCode getNewCode() {
            return this.newCode;
        }

        public void setNewCode(PaymentCode newCode) {
            this.newCode = newCode;
        }

        public PaymentCodeMapping create() {
            return PaymentCodeMapping.create(getExecutionInterval(), getOldCode(), getNewCode());
        }

        public void clear() {
            setOldCode(null);
            setNewCode(null);
        }
    }

    protected PaymentCodeMapping() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public PaymentCodeMapping(final ExecutionInterval executionInterval, final PaymentCode oldCode, final PaymentCode newCode) {
        this();
        check(executionInterval, "error.PaymentCodeMapping.invalid.execution.interval");
        check(oldCode, "error.PaymentCodeMapping.invalid.old.code");
        check(newCode, "error.PaymentCodeMapping.invalid.new.code");
        check(executionInterval, oldCode, newCode);
        setExecutionInterval(executionInterval);
        setOldPaymentCode(oldCode);
        setNewPaymentCode(newCode);
    }

    private void check(final ExecutionInterval executionInterval, final PaymentCode oldCode, final PaymentCode newCode) {
        if (oldCode.equals(newCode)) {
            throw new DomainException("error.PaymentCodeMapping.old.code.equals.new.code");
        }
        final PaymentCode code = executionInterval.findNewCodeInPaymentCodeMapping(oldCode);
        if (code != null) {
            throw new DomainException("error.PaymentCodeMapping.find.existing.code", oldCode.getFormattedCode(),
                    code.getFormattedCode());
        }
    }

    @Service
    public void delete() {
        removeExecutionInterval();
        removeOldPaymentCode();
        removeNewPaymentCode();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public boolean hasOldPaymentCode(final PaymentCode oldCode) {
        return hasOldPaymentCode() && getOldPaymentCode().equals(oldCode);
    }

    public boolean has(final ExecutionInterval executionInterval) {
        return getExecutionInterval().equals(executionInterval);
    }

    @Service
    static public PaymentCodeMapping create(final ExecutionInterval executionInterval, final PaymentCode oldCode,
            final PaymentCode newCode) {
        return new PaymentCodeMapping(executionInterval, oldCode, newCode);
    }

}
