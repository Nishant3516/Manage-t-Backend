package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.STIncomeExpenses;
import com.ssik.manageit.domain.STRoute;
import com.ssik.manageit.repository.STRouteRepository;
import com.ssik.manageit.service.criteria.STRouteCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link STRouteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class STRouteResourceIT {

    private static final String DEFAULT_TRANSPORT_ROUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRANSPORT_ROUTE_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_ROUTE_CHARGE = 1D;
    private static final Double UPDATED_ROUTE_CHARGE = 2D;
    private static final Double SMALLER_ROUTE_CHARGE = 1D - 1D;

    private static final String DEFAULT_TRANSPORT_ROUTE_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_TRANSPORT_ROUTE_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/st-routes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private STRouteRepository sTRouteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSTRouteMockMvc;

    private STRoute sTRoute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static STRoute createEntity(EntityManager em) {
        STRoute sTRoute = new STRoute()
            .transportRouteName(DEFAULT_TRANSPORT_ROUTE_NAME)
            .routeCharge(DEFAULT_ROUTE_CHARGE)
            .transportRouteAddress(DEFAULT_TRANSPORT_ROUTE_ADDRESS)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .createDate(DEFAULT_CREATE_DATE)
            .cancelDate(DEFAULT_CANCEL_DATE)
            .remarks(DEFAULT_REMARKS);
        return sTRoute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static STRoute createUpdatedEntity(EntityManager em) {
        STRoute sTRoute = new STRoute()
            .transportRouteName(UPDATED_TRANSPORT_ROUTE_NAME)
            .routeCharge(UPDATED_ROUTE_CHARGE)
            .transportRouteAddress(UPDATED_TRANSPORT_ROUTE_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE)
            .remarks(UPDATED_REMARKS);
        return sTRoute;
    }

    @BeforeEach
    public void initTest() {
        sTRoute = createEntity(em);
    }

    @Test
    @Transactional
    void createSTRoute() throws Exception {
        int databaseSizeBeforeCreate = sTRouteRepository.findAll().size();
        // Create the STRoute
        restSTRouteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sTRoute)))
            .andExpect(status().isCreated());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeCreate + 1);
        STRoute testSTRoute = sTRouteList.get(sTRouteList.size() - 1);
        assertThat(testSTRoute.getTransportRouteName()).isEqualTo(DEFAULT_TRANSPORT_ROUTE_NAME);
        assertThat(testSTRoute.getRouteCharge()).isEqualTo(DEFAULT_ROUTE_CHARGE);
        assertThat(testSTRoute.getTransportRouteAddress()).isEqualTo(DEFAULT_TRANSPORT_ROUTE_ADDRESS);
        assertThat(testSTRoute.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testSTRoute.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSTRoute.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
        assertThat(testSTRoute.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void createSTRouteWithExistingId() throws Exception {
        // Create the STRoute with an existing ID
        sTRoute.setId(1L);

        int databaseSizeBeforeCreate = sTRouteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSTRouteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sTRoute)))
            .andExpect(status().isBadRequest());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransportRouteNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sTRouteRepository.findAll().size();
        // set the field null
        sTRoute.setTransportRouteName(null);

        // Create the STRoute, which fails.

        restSTRouteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sTRoute)))
            .andExpect(status().isBadRequest());

        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRouteChargeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sTRouteRepository.findAll().size();
        // set the field null
        sTRoute.setRouteCharge(null);

        // Create the STRoute, which fails.

        restSTRouteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sTRoute)))
            .andExpect(status().isBadRequest());

        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSTRoutes() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList
        restSTRouteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sTRoute.getId().intValue())))
            .andExpect(jsonPath("$.[*].transportRouteName").value(hasItem(DEFAULT_TRANSPORT_ROUTE_NAME)))
            .andExpect(jsonPath("$.[*].routeCharge").value(hasItem(DEFAULT_ROUTE_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].transportRouteAddress").value(hasItem(DEFAULT_TRANSPORT_ROUTE_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @Test
    @Transactional
    void getSTRoute() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get the sTRoute
        restSTRouteMockMvc
            .perform(get(ENTITY_API_URL_ID, sTRoute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sTRoute.getId().intValue()))
            .andExpect(jsonPath("$.transportRouteName").value(DEFAULT_TRANSPORT_ROUTE_NAME))
            .andExpect(jsonPath("$.routeCharge").value(DEFAULT_ROUTE_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.transportRouteAddress").value(DEFAULT_TRANSPORT_ROUTE_ADDRESS))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getSTRoutesByIdFiltering() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        Long id = sTRoute.getId();

        defaultSTRouteShouldBeFound("id.equals=" + id);
        defaultSTRouteShouldNotBeFound("id.notEquals=" + id);

        defaultSTRouteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSTRouteShouldNotBeFound("id.greaterThan=" + id);

        defaultSTRouteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSTRouteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteName equals to DEFAULT_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldBeFound("transportRouteName.equals=" + DEFAULT_TRANSPORT_ROUTE_NAME);

        // Get all the sTRouteList where transportRouteName equals to UPDATED_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldNotBeFound("transportRouteName.equals=" + UPDATED_TRANSPORT_ROUTE_NAME);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteName not equals to DEFAULT_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldNotBeFound("transportRouteName.notEquals=" + DEFAULT_TRANSPORT_ROUTE_NAME);

        // Get all the sTRouteList where transportRouteName not equals to UPDATED_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldBeFound("transportRouteName.notEquals=" + UPDATED_TRANSPORT_ROUTE_NAME);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteNameIsInShouldWork() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteName in DEFAULT_TRANSPORT_ROUTE_NAME or UPDATED_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldBeFound("transportRouteName.in=" + DEFAULT_TRANSPORT_ROUTE_NAME + "," + UPDATED_TRANSPORT_ROUTE_NAME);

        // Get all the sTRouteList where transportRouteName equals to UPDATED_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldNotBeFound("transportRouteName.in=" + UPDATED_TRANSPORT_ROUTE_NAME);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteName is not null
        defaultSTRouteShouldBeFound("transportRouteName.specified=true");

        // Get all the sTRouteList where transportRouteName is null
        defaultSTRouteShouldNotBeFound("transportRouteName.specified=false");
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteNameContainsSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteName contains DEFAULT_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldBeFound("transportRouteName.contains=" + DEFAULT_TRANSPORT_ROUTE_NAME);

        // Get all the sTRouteList where transportRouteName contains UPDATED_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldNotBeFound("transportRouteName.contains=" + UPDATED_TRANSPORT_ROUTE_NAME);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteNameNotContainsSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteName does not contain DEFAULT_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldNotBeFound("transportRouteName.doesNotContain=" + DEFAULT_TRANSPORT_ROUTE_NAME);

        // Get all the sTRouteList where transportRouteName does not contain UPDATED_TRANSPORT_ROUTE_NAME
        defaultSTRouteShouldBeFound("transportRouteName.doesNotContain=" + UPDATED_TRANSPORT_ROUTE_NAME);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRouteChargeIsEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where routeCharge equals to DEFAULT_ROUTE_CHARGE
        defaultSTRouteShouldBeFound("routeCharge.equals=" + DEFAULT_ROUTE_CHARGE);

        // Get all the sTRouteList where routeCharge equals to UPDATED_ROUTE_CHARGE
        defaultSTRouteShouldNotBeFound("routeCharge.equals=" + UPDATED_ROUTE_CHARGE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRouteChargeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where routeCharge not equals to DEFAULT_ROUTE_CHARGE
        defaultSTRouteShouldNotBeFound("routeCharge.notEquals=" + DEFAULT_ROUTE_CHARGE);

        // Get all the sTRouteList where routeCharge not equals to UPDATED_ROUTE_CHARGE
        defaultSTRouteShouldBeFound("routeCharge.notEquals=" + UPDATED_ROUTE_CHARGE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRouteChargeIsInShouldWork() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where routeCharge in DEFAULT_ROUTE_CHARGE or UPDATED_ROUTE_CHARGE
        defaultSTRouteShouldBeFound("routeCharge.in=" + DEFAULT_ROUTE_CHARGE + "," + UPDATED_ROUTE_CHARGE);

        // Get all the sTRouteList where routeCharge equals to UPDATED_ROUTE_CHARGE
        defaultSTRouteShouldNotBeFound("routeCharge.in=" + UPDATED_ROUTE_CHARGE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRouteChargeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where routeCharge is not null
        defaultSTRouteShouldBeFound("routeCharge.specified=true");

        // Get all the sTRouteList where routeCharge is null
        defaultSTRouteShouldNotBeFound("routeCharge.specified=false");
    }

    @Test
    @Transactional
    void getAllSTRoutesByRouteChargeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where routeCharge is greater than or equal to DEFAULT_ROUTE_CHARGE
        defaultSTRouteShouldBeFound("routeCharge.greaterThanOrEqual=" + DEFAULT_ROUTE_CHARGE);

        // Get all the sTRouteList where routeCharge is greater than or equal to UPDATED_ROUTE_CHARGE
        defaultSTRouteShouldNotBeFound("routeCharge.greaterThanOrEqual=" + UPDATED_ROUTE_CHARGE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRouteChargeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where routeCharge is less than or equal to DEFAULT_ROUTE_CHARGE
        defaultSTRouteShouldBeFound("routeCharge.lessThanOrEqual=" + DEFAULT_ROUTE_CHARGE);

        // Get all the sTRouteList where routeCharge is less than or equal to SMALLER_ROUTE_CHARGE
        defaultSTRouteShouldNotBeFound("routeCharge.lessThanOrEqual=" + SMALLER_ROUTE_CHARGE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRouteChargeIsLessThanSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where routeCharge is less than DEFAULT_ROUTE_CHARGE
        defaultSTRouteShouldNotBeFound("routeCharge.lessThan=" + DEFAULT_ROUTE_CHARGE);

        // Get all the sTRouteList where routeCharge is less than UPDATED_ROUTE_CHARGE
        defaultSTRouteShouldBeFound("routeCharge.lessThan=" + UPDATED_ROUTE_CHARGE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRouteChargeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where routeCharge is greater than DEFAULT_ROUTE_CHARGE
        defaultSTRouteShouldNotBeFound("routeCharge.greaterThan=" + DEFAULT_ROUTE_CHARGE);

        // Get all the sTRouteList where routeCharge is greater than SMALLER_ROUTE_CHARGE
        defaultSTRouteShouldBeFound("routeCharge.greaterThan=" + SMALLER_ROUTE_CHARGE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteAddress equals to DEFAULT_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldBeFound("transportRouteAddress.equals=" + DEFAULT_TRANSPORT_ROUTE_ADDRESS);

        // Get all the sTRouteList where transportRouteAddress equals to UPDATED_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldNotBeFound("transportRouteAddress.equals=" + UPDATED_TRANSPORT_ROUTE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteAddress not equals to DEFAULT_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldNotBeFound("transportRouteAddress.notEquals=" + DEFAULT_TRANSPORT_ROUTE_ADDRESS);

        // Get all the sTRouteList where transportRouteAddress not equals to UPDATED_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldBeFound("transportRouteAddress.notEquals=" + UPDATED_TRANSPORT_ROUTE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteAddressIsInShouldWork() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteAddress in DEFAULT_TRANSPORT_ROUTE_ADDRESS or UPDATED_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldBeFound("transportRouteAddress.in=" + DEFAULT_TRANSPORT_ROUTE_ADDRESS + "," + UPDATED_TRANSPORT_ROUTE_ADDRESS);

        // Get all the sTRouteList where transportRouteAddress equals to UPDATED_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldNotBeFound("transportRouteAddress.in=" + UPDATED_TRANSPORT_ROUTE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteAddress is not null
        defaultSTRouteShouldBeFound("transportRouteAddress.specified=true");

        // Get all the sTRouteList where transportRouteAddress is null
        defaultSTRouteShouldNotBeFound("transportRouteAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteAddressContainsSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteAddress contains DEFAULT_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldBeFound("transportRouteAddress.contains=" + DEFAULT_TRANSPORT_ROUTE_ADDRESS);

        // Get all the sTRouteList where transportRouteAddress contains UPDATED_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldNotBeFound("transportRouteAddress.contains=" + UPDATED_TRANSPORT_ROUTE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSTRoutesByTransportRouteAddressNotContainsSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where transportRouteAddress does not contain DEFAULT_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldNotBeFound("transportRouteAddress.doesNotContain=" + DEFAULT_TRANSPORT_ROUTE_ADDRESS);

        // Get all the sTRouteList where transportRouteAddress does not contain UPDATED_TRANSPORT_ROUTE_ADDRESS
        defaultSTRouteShouldBeFound("transportRouteAddress.doesNotContain=" + UPDATED_TRANSPORT_ROUTE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSTRoutesByContactNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where contactNumber equals to DEFAULT_CONTACT_NUMBER
        defaultSTRouteShouldBeFound("contactNumber.equals=" + DEFAULT_CONTACT_NUMBER);

        // Get all the sTRouteList where contactNumber equals to UPDATED_CONTACT_NUMBER
        defaultSTRouteShouldNotBeFound("contactNumber.equals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSTRoutesByContactNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where contactNumber not equals to DEFAULT_CONTACT_NUMBER
        defaultSTRouteShouldNotBeFound("contactNumber.notEquals=" + DEFAULT_CONTACT_NUMBER);

        // Get all the sTRouteList where contactNumber not equals to UPDATED_CONTACT_NUMBER
        defaultSTRouteShouldBeFound("contactNumber.notEquals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSTRoutesByContactNumberIsInShouldWork() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where contactNumber in DEFAULT_CONTACT_NUMBER or UPDATED_CONTACT_NUMBER
        defaultSTRouteShouldBeFound("contactNumber.in=" + DEFAULT_CONTACT_NUMBER + "," + UPDATED_CONTACT_NUMBER);

        // Get all the sTRouteList where contactNumber equals to UPDATED_CONTACT_NUMBER
        defaultSTRouteShouldNotBeFound("contactNumber.in=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSTRoutesByContactNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where contactNumber is not null
        defaultSTRouteShouldBeFound("contactNumber.specified=true");

        // Get all the sTRouteList where contactNumber is null
        defaultSTRouteShouldNotBeFound("contactNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSTRoutesByContactNumberContainsSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where contactNumber contains DEFAULT_CONTACT_NUMBER
        defaultSTRouteShouldBeFound("contactNumber.contains=" + DEFAULT_CONTACT_NUMBER);

        // Get all the sTRouteList where contactNumber contains UPDATED_CONTACT_NUMBER
        defaultSTRouteShouldNotBeFound("contactNumber.contains=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSTRoutesByContactNumberNotContainsSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where contactNumber does not contain DEFAULT_CONTACT_NUMBER
        defaultSTRouteShouldNotBeFound("contactNumber.doesNotContain=" + DEFAULT_CONTACT_NUMBER);

        // Get all the sTRouteList where contactNumber does not contain UPDATED_CONTACT_NUMBER
        defaultSTRouteShouldBeFound("contactNumber.doesNotContain=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where createDate equals to DEFAULT_CREATE_DATE
        defaultSTRouteShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the sTRouteList where createDate equals to UPDATED_CREATE_DATE
        defaultSTRouteShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSTRouteShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the sTRouteList where createDate not equals to UPDATED_CREATE_DATE
        defaultSTRouteShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSTRouteShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the sTRouteList where createDate equals to UPDATED_CREATE_DATE
        defaultSTRouteShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where createDate is not null
        defaultSTRouteShouldBeFound("createDate.specified=true");

        // Get all the sTRouteList where createDate is null
        defaultSTRouteShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSTRoutesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSTRouteShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the sTRouteList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSTRouteShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSTRouteShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the sTRouteList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSTRouteShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where createDate is less than DEFAULT_CREATE_DATE
        defaultSTRouteShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the sTRouteList where createDate is less than UPDATED_CREATE_DATE
        defaultSTRouteShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSTRouteShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the sTRouteList where createDate is greater than SMALLER_CREATE_DATE
        defaultSTRouteShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSTRouteShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the sTRouteList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSTRouteShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSTRouteShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the sTRouteList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSTRouteShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSTRouteShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the sTRouteList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSTRouteShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where cancelDate is not null
        defaultSTRouteShouldBeFound("cancelDate.specified=true");

        // Get all the sTRouteList where cancelDate is null
        defaultSTRouteShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSTRoutesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSTRouteShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the sTRouteList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSTRouteShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSTRouteShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the sTRouteList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSTRouteShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSTRouteShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the sTRouteList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSTRouteShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSTRouteShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the sTRouteList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSTRouteShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where remarks equals to DEFAULT_REMARKS
        defaultSTRouteShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the sTRouteList where remarks equals to UPDATED_REMARKS
        defaultSTRouteShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRemarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where remarks not equals to DEFAULT_REMARKS
        defaultSTRouteShouldNotBeFound("remarks.notEquals=" + DEFAULT_REMARKS);

        // Get all the sTRouteList where remarks not equals to UPDATED_REMARKS
        defaultSTRouteShouldBeFound("remarks.notEquals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultSTRouteShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the sTRouteList where remarks equals to UPDATED_REMARKS
        defaultSTRouteShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where remarks is not null
        defaultSTRouteShouldBeFound("remarks.specified=true");

        // Get all the sTRouteList where remarks is null
        defaultSTRouteShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllSTRoutesByRemarksContainsSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where remarks contains DEFAULT_REMARKS
        defaultSTRouteShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the sTRouteList where remarks contains UPDATED_REMARKS
        defaultSTRouteShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTRoutesByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        // Get all the sTRouteList where remarks does not contain DEFAULT_REMARKS
        defaultSTRouteShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the sTRouteList where remarks does not contain UPDATED_REMARKS
        defaultSTRouteShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTRoutesBySTIncomeExpensesIsEqualToSomething() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);
        STIncomeExpenses sTIncomeExpenses;
        if (TestUtil.findAll(em, STIncomeExpenses.class).isEmpty()) {
            sTIncomeExpenses = STIncomeExpensesResourceIT.createEntity(em);
            em.persist(sTIncomeExpenses);
            em.flush();
        } else {
            sTIncomeExpenses = TestUtil.findAll(em, STIncomeExpenses.class).get(0);
        }
        em.persist(sTIncomeExpenses);
        em.flush();
        sTRoute.addSTIncomeExpenses(sTIncomeExpenses);
        sTRouteRepository.saveAndFlush(sTRoute);
        Long sTIncomeExpensesId = sTIncomeExpenses.getId();

        // Get all the sTRouteList where sTIncomeExpenses equals to sTIncomeExpensesId
        defaultSTRouteShouldBeFound("sTIncomeExpensesId.equals=" + sTIncomeExpensesId);

        // Get all the sTRouteList where sTIncomeExpenses equals to (sTIncomeExpensesId + 1)
        defaultSTRouteShouldNotBeFound("sTIncomeExpensesId.equals=" + (sTIncomeExpensesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSTRouteShouldBeFound(String filter) throws Exception {
        restSTRouteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sTRoute.getId().intValue())))
            .andExpect(jsonPath("$.[*].transportRouteName").value(hasItem(DEFAULT_TRANSPORT_ROUTE_NAME)))
            .andExpect(jsonPath("$.[*].routeCharge").value(hasItem(DEFAULT_ROUTE_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].transportRouteAddress").value(hasItem(DEFAULT_TRANSPORT_ROUTE_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));

        // Check, that the count call also returns 1
        restSTRouteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSTRouteShouldNotBeFound(String filter) throws Exception {
        restSTRouteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSTRouteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSTRoute() throws Exception {
        // Get the sTRoute
        restSTRouteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSTRoute() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        int databaseSizeBeforeUpdate = sTRouteRepository.findAll().size();

        // Update the sTRoute
        STRoute updatedSTRoute = sTRouteRepository.findById(sTRoute.getId()).get();
        // Disconnect from session so that the updates on updatedSTRoute are not directly saved in db
        em.detach(updatedSTRoute);
        updatedSTRoute
            .transportRouteName(UPDATED_TRANSPORT_ROUTE_NAME)
            .routeCharge(UPDATED_ROUTE_CHARGE)
            .transportRouteAddress(UPDATED_TRANSPORT_ROUTE_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE)
            .remarks(UPDATED_REMARKS);

        restSTRouteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSTRoute.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSTRoute))
            )
            .andExpect(status().isOk());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeUpdate);
        STRoute testSTRoute = sTRouteList.get(sTRouteList.size() - 1);
        assertThat(testSTRoute.getTransportRouteName()).isEqualTo(UPDATED_TRANSPORT_ROUTE_NAME);
        assertThat(testSTRoute.getRouteCharge()).isEqualTo(UPDATED_ROUTE_CHARGE);
        assertThat(testSTRoute.getTransportRouteAddress()).isEqualTo(UPDATED_TRANSPORT_ROUTE_ADDRESS);
        assertThat(testSTRoute.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testSTRoute.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSTRoute.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testSTRoute.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void putNonExistingSTRoute() throws Exception {
        int databaseSizeBeforeUpdate = sTRouteRepository.findAll().size();
        sTRoute.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSTRouteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sTRoute.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sTRoute))
            )
            .andExpect(status().isBadRequest());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSTRoute() throws Exception {
        int databaseSizeBeforeUpdate = sTRouteRepository.findAll().size();
        sTRoute.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSTRouteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sTRoute))
            )
            .andExpect(status().isBadRequest());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSTRoute() throws Exception {
        int databaseSizeBeforeUpdate = sTRouteRepository.findAll().size();
        sTRoute.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSTRouteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sTRoute)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSTRouteWithPatch() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        int databaseSizeBeforeUpdate = sTRouteRepository.findAll().size();

        // Update the sTRoute using partial update
        STRoute partialUpdatedSTRoute = new STRoute();
        partialUpdatedSTRoute.setId(sTRoute.getId());

        partialUpdatedSTRoute.transportRouteName(UPDATED_TRANSPORT_ROUTE_NAME).contactNumber(UPDATED_CONTACT_NUMBER);

        restSTRouteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSTRoute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSTRoute))
            )
            .andExpect(status().isOk());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeUpdate);
        STRoute testSTRoute = sTRouteList.get(sTRouteList.size() - 1);
        assertThat(testSTRoute.getTransportRouteName()).isEqualTo(UPDATED_TRANSPORT_ROUTE_NAME);
        assertThat(testSTRoute.getRouteCharge()).isEqualTo(DEFAULT_ROUTE_CHARGE);
        assertThat(testSTRoute.getTransportRouteAddress()).isEqualTo(DEFAULT_TRANSPORT_ROUTE_ADDRESS);
        assertThat(testSTRoute.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testSTRoute.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSTRoute.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
        assertThat(testSTRoute.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateSTRouteWithPatch() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        int databaseSizeBeforeUpdate = sTRouteRepository.findAll().size();

        // Update the sTRoute using partial update
        STRoute partialUpdatedSTRoute = new STRoute();
        partialUpdatedSTRoute.setId(sTRoute.getId());

        partialUpdatedSTRoute
            .transportRouteName(UPDATED_TRANSPORT_ROUTE_NAME)
            .routeCharge(UPDATED_ROUTE_CHARGE)
            .transportRouteAddress(UPDATED_TRANSPORT_ROUTE_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE)
            .remarks(UPDATED_REMARKS);

        restSTRouteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSTRoute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSTRoute))
            )
            .andExpect(status().isOk());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeUpdate);
        STRoute testSTRoute = sTRouteList.get(sTRouteList.size() - 1);
        assertThat(testSTRoute.getTransportRouteName()).isEqualTo(UPDATED_TRANSPORT_ROUTE_NAME);
        assertThat(testSTRoute.getRouteCharge()).isEqualTo(UPDATED_ROUTE_CHARGE);
        assertThat(testSTRoute.getTransportRouteAddress()).isEqualTo(UPDATED_TRANSPORT_ROUTE_ADDRESS);
        assertThat(testSTRoute.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testSTRoute.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSTRoute.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testSTRoute.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingSTRoute() throws Exception {
        int databaseSizeBeforeUpdate = sTRouteRepository.findAll().size();
        sTRoute.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSTRouteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sTRoute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sTRoute))
            )
            .andExpect(status().isBadRequest());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSTRoute() throws Exception {
        int databaseSizeBeforeUpdate = sTRouteRepository.findAll().size();
        sTRoute.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSTRouteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sTRoute))
            )
            .andExpect(status().isBadRequest());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSTRoute() throws Exception {
        int databaseSizeBeforeUpdate = sTRouteRepository.findAll().size();
        sTRoute.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSTRouteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sTRoute)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the STRoute in the database
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSTRoute() throws Exception {
        // Initialize the database
        sTRouteRepository.saveAndFlush(sTRoute);

        int databaseSizeBeforeDelete = sTRouteRepository.findAll().size();

        // Delete the sTRoute
        restSTRouteMockMvc
            .perform(delete(ENTITY_API_URL_ID, sTRoute.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<STRoute> sTRouteList = sTRouteRepository.findAll();
        assertThat(sTRouteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
