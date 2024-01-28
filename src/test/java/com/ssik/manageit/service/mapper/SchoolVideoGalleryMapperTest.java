package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolVideoGalleryMapperTest {

    private SchoolVideoGalleryMapper schoolVideoGalleryMapper;

    @BeforeEach
    public void setUp() {
        schoolVideoGalleryMapper = new SchoolVideoGalleryMapperImpl();
    }
}
