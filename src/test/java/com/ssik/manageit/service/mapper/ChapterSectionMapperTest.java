package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChapterSectionMapperTest {

    private ChapterSectionMapper chapterSectionMapper;

    @BeforeEach
    public void setUp() {
        chapterSectionMapper = new ChapterSectionMapperImpl();
    }
}
