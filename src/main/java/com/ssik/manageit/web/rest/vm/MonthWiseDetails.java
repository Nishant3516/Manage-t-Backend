package com.ssik.manageit.web.rest.vm;

import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.dto.StudentChargesSummaryDTO;

public class MonthWiseDetails {

    Double janMonthTotal = 0.0;
    Double febMonthTotal = 0.0;
    Double marMonthTotal = 0.0;
    Double aprMonthTotal = 0.0;
    Double mayMonthTotal = 0.0;
    Double junMonthTotal = 0.0;
    Double julMonthTotal = 0.0;
    Double augMonthTotal = 0.0;
    Double sepMonthTotal = 0.0;
    Double octMonthTotal = 0.0;
    Double novMonthTotal = 0.0;
    Double decMonthTotal = 0.0;

    public Double getJanMonthTotal() {
        return janMonthTotal;
    }

    public void setJanMonthTotal(Double janMonthTotal) {
        this.janMonthTotal = janMonthTotal;
    }

    public Double getFebMonthTotal() {
        return febMonthTotal;
    }

    public void setFebMonthTotal(Double febMonthTotal) {
        this.febMonthTotal = febMonthTotal;
    }

    public Double getMarMonthTotal() {
        return marMonthTotal;
    }

    public void setMarMonthTotal(Double marMonthTotal) {
        this.marMonthTotal = marMonthTotal;
    }

    public Double getAprMonthTotal() {
        return aprMonthTotal;
    }

    public void setAprMonthTotal(Double aprMonthTotal) {
        this.aprMonthTotal = aprMonthTotal;
    }

    public Double getMayMonthTotal() {
        return mayMonthTotal;
    }

    public void setMayMonthTotal(Double mayMonthTotal) {
        this.mayMonthTotal = mayMonthTotal;
    }

    public Double getJunMonthTotal() {
        return junMonthTotal;
    }

    public void setJunMonthTotal(Double junMonthTotal) {
        this.junMonthTotal = junMonthTotal;
    }

    public Double getJulMonthTotal() {
        return julMonthTotal;
    }

    public void setJulMonthTotal(Double julMonthTotal) {
        this.julMonthTotal = julMonthTotal;
    }

    public Double getAugMonthTotal() {
        return augMonthTotal;
    }

    public void setAugMonthTotal(Double augMonthTotal) {
        this.augMonthTotal = augMonthTotal;
    }

    public Double getSepMonthTotal() {
        return sepMonthTotal;
    }

    public void setSepMonthTotal(Double sepMonthTotal) {
        this.sepMonthTotal = sepMonthTotal;
    }

    public Double getOctMonthTotal() {
        return octMonthTotal;
    }

    public void setOctMonthTotal(Double octMonthTotal) {
        this.octMonthTotal = octMonthTotal;
    }

    public Double getNovMonthTotal() {
        return novMonthTotal;
    }

    public void setNovMonthTotal(Double novMonthTotal) {
        this.novMonthTotal = novMonthTotal;
    }

    public Double getDecMonthTotal() {
        return decMonthTotal;
    }

    public void setDecMonthTotal(Double decMonthTotal) {
        this.decMonthTotal = decMonthTotal;
    }

    public StudentChargesSummaryDTO toStudentChargesSummaryDTO(String summaryType, ClassStudentDTO classStudentDto) {
        StudentChargesSummaryDTO studentChargesSummaryDTO = new StudentChargesSummaryDTO();
        studentChargesSummaryDTO.setSummaryType(summaryType);
        studentChargesSummaryDTO.setFeeYear("");
        studentChargesSummaryDTO.setClassStudent(classStudentDto);
        studentChargesSummaryDTO.setAprSummary("" + this.getAprMonthTotal());
        studentChargesSummaryDTO.setMaySummary("" + this.getMayMonthTotal());
        studentChargesSummaryDTO.setJunSummary("" + this.getJunMonthTotal());
        studentChargesSummaryDTO.setJulSummary("" + this.getJulMonthTotal());
        studentChargesSummaryDTO.setAugSummary("" + this.getAugMonthTotal());
        studentChargesSummaryDTO.setSepSummary("" + this.getSepMonthTotal());
        studentChargesSummaryDTO.setOctSummary("" + this.getOctMonthTotal());
        studentChargesSummaryDTO.setNovSummary("" + this.getNovMonthTotal());
        studentChargesSummaryDTO.setDecSummary("" + this.getDecMonthTotal());
        studentChargesSummaryDTO.setJanSummary("" + this.getJanMonthTotal());
        studentChargesSummaryDTO.setFebSummary("" + this.getFebMonthTotal());
        studentChargesSummaryDTO.setMarSummary("" + this.getMarMonthTotal());
        return studentChargesSummaryDTO;
    }
}
