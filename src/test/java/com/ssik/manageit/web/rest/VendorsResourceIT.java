package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.Vendors;
import com.ssik.manageit.domain.enumeration.VendorType;
import com.ssik.manageit.repository.VendorsRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link VendorsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VendorsResourceIT {

    private static final byte[] DEFAULT_VENDOR_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VENDOR_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VENDOR_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VENDOR_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_VENDOR_PHOTO_LINK = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_PHOTO_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_OF_BIRTH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_BIRTH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_NICK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NICK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final VendorType DEFAULT_VENDOR_TYPE = VendorType.EXTERNAL;
    private static final VendorType UPDATED_VENDOR_TYPE = VendorType.INTERNAL;

    private static final String ENTITY_API_URL = "/api/vendors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VendorsRepository vendorsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendorsMockMvc;

    private Vendors vendors;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendors createEntity(EntityManager em) {
        Vendors vendors = new Vendors()
            .vendorPhoto(DEFAULT_VENDOR_PHOTO)
            .vendorPhotoContentType(DEFAULT_VENDOR_PHOTO_CONTENT_TYPE)
            .vendorPhotoLink(DEFAULT_VENDOR_PHOTO_LINK)
            .vendorId(DEFAULT_VENDOR_ID)
            .vendorName(DEFAULT_VENDOR_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .nickName(DEFAULT_NICK_NAME)
            .email(DEFAULT_EMAIL)
            .createDate(DEFAULT_CREATE_DATE)
            .cancelDate(DEFAULT_CANCEL_DATE)
            .vendorType(DEFAULT_VENDOR_TYPE);
        return vendors;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendors createUpdatedEntity(EntityManager em) {
        Vendors vendors = new Vendors()
            .vendorPhoto(UPDATED_VENDOR_PHOTO)
            .vendorPhotoContentType(UPDATED_VENDOR_PHOTO_CONTENT_TYPE)
            .vendorPhotoLink(UPDATED_VENDOR_PHOTO_LINK)
            .vendorId(UPDATED_VENDOR_ID)
            .vendorName(UPDATED_VENDOR_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .nickName(UPDATED_NICK_NAME)
            .email(UPDATED_EMAIL)
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE)
            .vendorType(UPDATED_VENDOR_TYPE);
        return vendors;
    }

    @BeforeEach
    public void initTest() {
        vendors = createEntity(em);
    }

    @Test
    @Transactional
    void createVendors() throws Exception {
        int databaseSizeBeforeCreate = vendorsRepository.findAll().size();
        // Create the Vendors
        restVendorsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendors)))
            .andExpect(status().isCreated());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeCreate + 1);
        Vendors testVendors = vendorsList.get(vendorsList.size() - 1);
        assertThat(testVendors.getVendorPhoto()).isEqualTo(DEFAULT_VENDOR_PHOTO);
        assertThat(testVendors.getVendorPhotoContentType()).isEqualTo(DEFAULT_VENDOR_PHOTO_CONTENT_TYPE);
        assertThat(testVendors.getVendorPhotoLink()).isEqualTo(DEFAULT_VENDOR_PHOTO_LINK);
        assertThat(testVendors.getVendorId()).isEqualTo(DEFAULT_VENDOR_ID);
        assertThat(testVendors.getVendorName()).isEqualTo(DEFAULT_VENDOR_NAME);
        assertThat(testVendors.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testVendors.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testVendors.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testVendors.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testVendors.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testVendors.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVendors.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testVendors.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
        assertThat(testVendors.getVendorType()).isEqualTo(DEFAULT_VENDOR_TYPE);
    }

    @Test
    @Transactional
    void createVendorsWithExistingId() throws Exception {
        // Create the Vendors with an existing ID
        vendors.setId(1L);

        int databaseSizeBeforeCreate = vendorsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendorsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendors)))
            .andExpect(status().isBadRequest());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVendorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendorsRepository.findAll().size();
        // set the field null
        vendors.setVendorName(null);

        // Create the Vendors, which fails.

        restVendorsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendors)))
            .andExpect(status().isBadRequest());

        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVendors() throws Exception {
        // Initialize the database
        vendorsRepository.saveAndFlush(vendors);

        // Get all the vendorsList
        restVendorsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendors.getId().intValue())))
            .andExpect(jsonPath("$.[*].vendorPhotoContentType").value(hasItem(DEFAULT_VENDOR_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].vendorPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_VENDOR_PHOTO))))
            .andExpect(jsonPath("$.[*].vendorPhotoLink").value(hasItem(DEFAULT_VENDOR_PHOTO_LINK)))
            .andExpect(jsonPath("$.[*].vendorId").value(hasItem(DEFAULT_VENDOR_ID)))
            .andExpect(jsonPath("$.[*].vendorName").value(hasItem(DEFAULT_VENDOR_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())))
            .andExpect(jsonPath("$.[*].vendorType").value(hasItem(DEFAULT_VENDOR_TYPE.toString())));
    }

    @Test
    @Transactional
    void getVendors() throws Exception {
        // Initialize the database
        vendorsRepository.saveAndFlush(vendors);

        // Get the vendors
        restVendorsMockMvc
            .perform(get(ENTITY_API_URL_ID, vendors.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendors.getId().intValue()))
            .andExpect(jsonPath("$.vendorPhotoContentType").value(DEFAULT_VENDOR_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.vendorPhoto").value(Base64Utils.encodeToString(DEFAULT_VENDOR_PHOTO)))
            .andExpect(jsonPath("$.vendorPhotoLink").value(DEFAULT_VENDOR_PHOTO_LINK))
            .andExpect(jsonPath("$.vendorId").value(DEFAULT_VENDOR_ID))
            .andExpect(jsonPath("$.vendorName").value(DEFAULT_VENDOR_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()))
            .andExpect(jsonPath("$.vendorType").value(DEFAULT_VENDOR_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVendors() throws Exception {
        // Get the vendors
        restVendorsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVendors() throws Exception {
        // Initialize the database
        vendorsRepository.saveAndFlush(vendors);

        int databaseSizeBeforeUpdate = vendorsRepository.findAll().size();

        // Update the vendors
        Vendors updatedVendors = vendorsRepository.findById(vendors.getId()).get();
        // Disconnect from session so that the updates on updatedVendors are not directly saved in db
        em.detach(updatedVendors);
        updatedVendors
            .vendorPhoto(UPDATED_VENDOR_PHOTO)
            .vendorPhotoContentType(UPDATED_VENDOR_PHOTO_CONTENT_TYPE)
            .vendorPhotoLink(UPDATED_VENDOR_PHOTO_LINK)
            .vendorId(UPDATED_VENDOR_ID)
            .vendorName(UPDATED_VENDOR_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .nickName(UPDATED_NICK_NAME)
            .email(UPDATED_EMAIL)
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE)
            .vendorType(UPDATED_VENDOR_TYPE);

        restVendorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVendors.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVendors))
            )
            .andExpect(status().isOk());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeUpdate);
        Vendors testVendors = vendorsList.get(vendorsList.size() - 1);
        assertThat(testVendors.getVendorPhoto()).isEqualTo(UPDATED_VENDOR_PHOTO);
        assertThat(testVendors.getVendorPhotoContentType()).isEqualTo(UPDATED_VENDOR_PHOTO_CONTENT_TYPE);
        assertThat(testVendors.getVendorPhotoLink()).isEqualTo(UPDATED_VENDOR_PHOTO_LINK);
        assertThat(testVendors.getVendorId()).isEqualTo(UPDATED_VENDOR_ID);
        assertThat(testVendors.getVendorName()).isEqualTo(UPDATED_VENDOR_NAME);
        assertThat(testVendors.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testVendors.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testVendors.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testVendors.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testVendors.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testVendors.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVendors.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testVendors.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testVendors.getVendorType()).isEqualTo(UPDATED_VENDOR_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingVendors() throws Exception {
        int databaseSizeBeforeUpdate = vendorsRepository.findAll().size();
        vendors.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendors.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendors))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendors() throws Exception {
        int databaseSizeBeforeUpdate = vendorsRepository.findAll().size();
        vendors.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendors))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendors() throws Exception {
        int databaseSizeBeforeUpdate = vendorsRepository.findAll().size();
        vendors.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendors)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendorsWithPatch() throws Exception {
        // Initialize the database
        vendorsRepository.saveAndFlush(vendors);

        int databaseSizeBeforeUpdate = vendorsRepository.findAll().size();

        // Update the vendors using partial update
        Vendors partialUpdatedVendors = new Vendors();
        partialUpdatedVendors.setId(vendors.getId());

        partialUpdatedVendors
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE);

        restVendorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendors.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendors))
            )
            .andExpect(status().isOk());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeUpdate);
        Vendors testVendors = vendorsList.get(vendorsList.size() - 1);
        assertThat(testVendors.getVendorPhoto()).isEqualTo(DEFAULT_VENDOR_PHOTO);
        assertThat(testVendors.getVendorPhotoContentType()).isEqualTo(DEFAULT_VENDOR_PHOTO_CONTENT_TYPE);
        assertThat(testVendors.getVendorPhotoLink()).isEqualTo(DEFAULT_VENDOR_PHOTO_LINK);
        assertThat(testVendors.getVendorId()).isEqualTo(DEFAULT_VENDOR_ID);
        assertThat(testVendors.getVendorName()).isEqualTo(DEFAULT_VENDOR_NAME);
        assertThat(testVendors.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testVendors.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testVendors.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testVendors.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testVendors.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testVendors.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVendors.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testVendors.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testVendors.getVendorType()).isEqualTo(DEFAULT_VENDOR_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateVendorsWithPatch() throws Exception {
        // Initialize the database
        vendorsRepository.saveAndFlush(vendors);

        int databaseSizeBeforeUpdate = vendorsRepository.findAll().size();

        // Update the vendors using partial update
        Vendors partialUpdatedVendors = new Vendors();
        partialUpdatedVendors.setId(vendors.getId());

        partialUpdatedVendors
            .vendorPhoto(UPDATED_VENDOR_PHOTO)
            .vendorPhotoContentType(UPDATED_VENDOR_PHOTO_CONTENT_TYPE)
            .vendorPhotoLink(UPDATED_VENDOR_PHOTO_LINK)
            .vendorId(UPDATED_VENDOR_ID)
            .vendorName(UPDATED_VENDOR_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .nickName(UPDATED_NICK_NAME)
            .email(UPDATED_EMAIL)
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE)
            .vendorType(UPDATED_VENDOR_TYPE);

        restVendorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendors.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendors))
            )
            .andExpect(status().isOk());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeUpdate);
        Vendors testVendors = vendorsList.get(vendorsList.size() - 1);
        assertThat(testVendors.getVendorPhoto()).isEqualTo(UPDATED_VENDOR_PHOTO);
        assertThat(testVendors.getVendorPhotoContentType()).isEqualTo(UPDATED_VENDOR_PHOTO_CONTENT_TYPE);
        assertThat(testVendors.getVendorPhotoLink()).isEqualTo(UPDATED_VENDOR_PHOTO_LINK);
        assertThat(testVendors.getVendorId()).isEqualTo(UPDATED_VENDOR_ID);
        assertThat(testVendors.getVendorName()).isEqualTo(UPDATED_VENDOR_NAME);
        assertThat(testVendors.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testVendors.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testVendors.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testVendors.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testVendors.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testVendors.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVendors.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testVendors.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testVendors.getVendorType()).isEqualTo(UPDATED_VENDOR_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingVendors() throws Exception {
        int databaseSizeBeforeUpdate = vendorsRepository.findAll().size();
        vendors.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendors.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendors))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendors() throws Exception {
        int databaseSizeBeforeUpdate = vendorsRepository.findAll().size();
        vendors.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendors))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendors() throws Exception {
        int databaseSizeBeforeUpdate = vendorsRepository.findAll().size();
        vendors.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vendors)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendors in the database
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendors() throws Exception {
        // Initialize the database
        vendorsRepository.saveAndFlush(vendors);

        int databaseSizeBeforeDelete = vendorsRepository.findAll().size();

        // Delete the vendors
        restVendorsMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendors.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vendors> vendorsList = vendorsRepository.findAll();
        assertThat(vendorsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
