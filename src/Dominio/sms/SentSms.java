/*
 * Created on 7/Jun/2004
 *  
 */
package Dominio.sms;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.IPessoa;
import Util.SmsDeliveryType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SentSms extends DomainObject implements ISentSms {
    private Integer keyPerson;

    protected Integer destinationNumber;

    protected Date sendDate;

    protected Date deliveryDate;

    protected SmsDeliveryType deliveryType;

    protected IPessoa person;

    public SentSms() {
        super();
    }

    /**
     * @param idInternal
     */
    public SentSms(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the deliveryDate.
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate
     *            The deliveryDate to set.
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return Returns the deliveryType.
     */
    public SmsDeliveryType getDeliveryType() {
        return deliveryType;
    }

    /**
     * @param deliveryType
     *            The deliveryType to set.
     */
    public void setDeliveryType(SmsDeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    /**
     * @return Returns the destinationNumber.
     */
    public Integer getDestinationNumber() {
        return destinationNumber;
    }

    /**
     * @param destinationNumber
     *            The destinationNumber to set.
     */
    public void setDestinationNumber(Integer destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    /**
     * @return Returns the keyPerson.
     */
    public Integer getKeyPerson() {
        return keyPerson;
    }

    /**
     * @param keyPerson
     *            The keyPerson to set.
     */
    public void setKeyPerson(Integer keyPerson) {
        this.keyPerson = keyPerson;
    }

    /**
     * @return Returns the person.
     */
    public IPessoa getPerson() {
        return person;
    }

    /**
     * @param person
     *            The person to set.
     */
    public void setPerson(IPessoa person) {
        this.person = person;
    }

    /**
     * @return Returns the sendDate.
     */
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * @param sendDate
     *            The sendDate to set.
     */
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

}