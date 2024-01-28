package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.VendorType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vendors.
 */
@Entity
@Table(name = "vendors")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vendors implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "vendor_photo")
    private byte[] vendorPhoto;

    @Column(name = "vendor_photo_content_type")
    private String vendorPhotoContentType;

    @Column(name = "vendor_photo_link")
    private String vendorPhotoLink;

    @Column(name = "vendor_id")
    private String vendorId;

    @NotNull
    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "email")
    private String email;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor_type")
    private VendorType vendorType;

    @OneToMany(mappedBy = "vendor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vendor", "ledgerHead" }, allowSetters = true)
    private Set<IncomeExpenses> incomeExpenses = new HashSet<>();

    @OneToMany(mappedBy = "operatedBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classStudent", "stRoute", "operatedBy" }, allowSetters = true)
    private Set<STIncomeExpenses> sTIncomeExpenses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vendors id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getVendorPhoto() {
        return this.vendorPhoto;
    }

    public Vendors vendorPhoto(byte[] vendorPhoto) {
        this.setVendorPhoto(vendorPhoto);
        return this;
    }

    public void setVendorPhoto(byte[] vendorPhoto) {
        this.vendorPhoto = vendorPhoto;
    }

    public String getVendorPhotoContentType() {
        return this.vendorPhotoContentType;
    }

    public Vendors vendorPhotoContentType(String vendorPhotoContentType) {
        this.vendorPhotoContentType = vendorPhotoContentType;
        return this;
    }

    public void setVendorPhotoContentType(String vendorPhotoContentType) {
        this.vendorPhotoContentType = vendorPhotoContentType;
    }

    public String getVendorPhotoLink() {
        return this.vendorPhotoLink;
    }

    public Vendors vendorPhotoLink(String vendorPhotoLink) {
        this.setVendorPhotoLink(vendorPhotoLink);
        return this;
    }

    public void setVendorPhotoLink(String vendorPhotoLink) {
        this.vendorPhotoLink = vendorPhotoLink;
    }

    public String getVendorId() {
        return this.vendorId;
    }

    public Vendors vendorId(String vendorId) {
        this.setVendorId(vendorId);
        return this;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public Vendors vendorName(String vendorName) {
        this.setVendorName(vendorName);
        return this;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Vendors phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Vendors dateOfBirth(Instant dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public Vendors addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public Vendors addressLine2(String addressLine2) {
        this.setAddressLine2(addressLine2);
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getNickName() {
        return this.nickName;
    }

    public Vendors nickName(String nickName) {
        this.setNickName(nickName);
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return this.email;
    }

    public Vendors email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public Vendors createDate(LocalDate createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public Vendors cancelDate(LocalDate cancelDate) {
        this.setCancelDate(cancelDate);
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public VendorType getVendorType() {
        return this.vendorType;
    }

    public Vendors vendorType(VendorType vendorType) {
        this.setVendorType(vendorType);
        return this;
    }

    public void setVendorType(VendorType vendorType) {
        this.vendorType = vendorType;
    }

    public Set<IncomeExpenses> getIncomeExpenses() {
        return this.incomeExpenses;
    }

    public void setIncomeExpenses(Set<IncomeExpenses> incomeExpenses) {
        if (this.incomeExpenses != null) {
            this.incomeExpenses.forEach(i -> i.setVendor(null));
        }
        if (incomeExpenses != null) {
            incomeExpenses.forEach(i -> i.setVendor(this));
        }
        this.incomeExpenses = incomeExpenses;
    }

    public Vendors incomeExpenses(Set<IncomeExpenses> incomeExpenses) {
        this.setIncomeExpenses(incomeExpenses);
        return this;
    }

    public Vendors addIncomeExpenses(IncomeExpenses incomeExpenses) {
        this.incomeExpenses.add(incomeExpenses);
        incomeExpenses.setVendor(this);
        return this;
    }

    public Vendors removeIncomeExpenses(IncomeExpenses incomeExpenses) {
        this.incomeExpenses.remove(incomeExpenses);
        incomeExpenses.setVendor(null);
        return this;
    }

    public Set<STIncomeExpenses> getSTIncomeExpenses() {
        return this.sTIncomeExpenses;
    }

    public void setSTIncomeExpenses(Set<STIncomeExpenses> sTIncomeExpenses) {
        if (this.sTIncomeExpenses != null) {
            this.sTIncomeExpenses.forEach(i -> i.setOperatedBy(null));
        }
        if (sTIncomeExpenses != null) {
            sTIncomeExpenses.forEach(i -> i.setOperatedBy(this));
        }
        this.sTIncomeExpenses = sTIncomeExpenses;
    }

    public Vendors sTIncomeExpenses(Set<STIncomeExpenses> sTIncomeExpenses) {
        this.setSTIncomeExpenses(sTIncomeExpenses);
        return this;
    }

    public Vendors addSTIncomeExpenses(STIncomeExpenses sTIncomeExpenses) {
        this.sTIncomeExpenses.add(sTIncomeExpenses);
        sTIncomeExpenses.setOperatedBy(this);
        return this;
    }

    public Vendors removeSTIncomeExpenses(STIncomeExpenses sTIncomeExpenses) {
        this.sTIncomeExpenses.remove(sTIncomeExpenses);
        sTIncomeExpenses.setOperatedBy(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vendors)) {
            return false;
        }
        return id != null && id.equals(((Vendors) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vendors{" +
            "id=" + getId() +
            ", vendorPhoto='" + getVendorPhoto() + "'" +
            ", vendorPhotoContentType='" + getVendorPhotoContentType() + "'" +
            ", vendorPhotoLink='" + getVendorPhotoLink() + "'" +
            ", vendorId='" + getVendorId() + "'" +
            ", vendorName='" + getVendorName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", nickName='" + getNickName() + "'" +
            ", email='" + getEmail() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", vendorType='" + getVendorType() + "'" +
            "}";
    }
}
