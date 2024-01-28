package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolPictureGalleryMapperTest {

    private SchoolPictureGalleryMapper schoolPictureGalleryMapper;

    @BeforeEach
    public void setUp() {
        schoolPictureGalleryMapper = new SchoolPictureGalleryMapperImpl();
    }
}
