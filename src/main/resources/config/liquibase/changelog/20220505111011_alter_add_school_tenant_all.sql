--liquibase formatted sql
				
--changeset chandan:20220505111011
ALTER TABLE public.income_expenses
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
ALTER TABLE public.income_expenses
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.income_expenses
    ADD CONSTRAINT fk_income_expenses_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
      
ALTER TABLE public.income_expenses
	ADD CONSTRAINT fk_income_expenses_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;


ALTER TABLE public.audit_log
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;
        
ALTER TABLE public.audit_log
    ADD CONSTRAINT fk_audit_log_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;



ALTER TABLE public.chapter_section
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.chapter_section
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;

ALTER TABLE public.chapter_section
    ADD CONSTRAINT fk_chapter_section_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
	  
ALTER TABLE public.chapter_section
	ADD CONSTRAINT fk_chapter_section_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.class_class_work
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.class_class_work
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;

ALTER TABLE public.class_class_work
    ADD CONSTRAINT fk_class_class_work_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
	  
ALTER TABLE public.class_class_work
	ADD CONSTRAINT fk_class_class_work_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.class_fee
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.class_fee
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;

ALTER TABLE public.class_fee
    ADD CONSTRAINT fk_class_fee_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
	  
ALTER TABLE public.class_class_work
	ADD CONSTRAINT fk_class_fee_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.class_home_work
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.class_home_work
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;

ALTER TABLE public.class_home_work
    ADD CONSTRAINT fk_class_home_work_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
	  
ALTER TABLE public.class_home_work
	ADD CONSTRAINT fk_class_home_work_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.class_lession_plan
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.class_lession_plan
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;

ALTER TABLE public.class_lession_plan
    ADD CONSTRAINT fk_class_lession_plan_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
	  
ALTER TABLE public.class_lession_plan
	ADD CONSTRAINT fk_class_lession_plan_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.class_lession_plan_track
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.class_lession_plan_track
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;

ALTER TABLE public.class_lession_plan_track
    ADD CONSTRAINT fk_class_lession_plan_track_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
	  
ALTER TABLE public.class_lession_plan_track
	ADD CONSTRAINT fk_class_lession_plan_track_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.class_student
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.class_student
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;

ALTER TABLE public.class_student
    ADD CONSTRAINT fk_class_student_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
	  
ALTER TABLE public.class_student
	ADD CONSTRAINT fk_class_student_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.class_subject
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.class_subject
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;

ALTER TABLE public.class_subject
    ADD CONSTRAINT fk_class_subject_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
	  
ALTER TABLE public.class_subject
	ADD CONSTRAINT fk_class_subject_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
      
ALTER TABLE public.question
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;

ALTER TABLE public.question
	ADD CONSTRAINT fk_question_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.question_paper
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.question_paper
	ADD CONSTRAINT fk_question_paper_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.question_type
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.question_type
	ADD CONSTRAINT fk_question_type_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

      ALTER TABLE public.school
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school
    ADD CONSTRAINT fk_school_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_class
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school_class
    ADD CONSTRAINT fk_school_class_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_days_off
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school_days_off
    ADD CONSTRAINT fk_school_days_off_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_days_off
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.school_days_off
	ADD CONSTRAINT fk_school_days_off_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_event
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school_event
    ADD CONSTRAINT fk_school_event_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_event
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.school_days_off
	ADD CONSTRAINT fk_school_event_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_ledger_head
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school_ledger_head
    ADD CONSTRAINT fk_school_ledger_head_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_notifications
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school_notifications
    ADD CONSTRAINT fk_school_notifications_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_notifications
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.school_days_off
	ADD CONSTRAINT fk_school_notifications_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_picture_gallery
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school_picture_gallery
    ADD CONSTRAINT fk_school_picture_gallery_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_picture_gallery
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.school_picture_gallery
	ADD CONSTRAINT fk_school_picture_gallery_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;


ALTER TABLE public.school_report
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school_report
    ADD CONSTRAINT fk_school_report_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_report
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.school_report
	ADD CONSTRAINT fk_school_report_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_user
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school_user
    ADD CONSTRAINT fk_school_user_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_user
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.school_user
	ADD CONSTRAINT fk_school_user_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_video_gallery
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.school_video_gallery
    ADD CONSTRAINT fk_school_video_gallery_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.school_video_gallery
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.school_video_gallery
	ADD CONSTRAINT fk_school_video_gallery_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.st_income_expenses
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.st_income_expenses
    ADD CONSTRAINT fk_st_income_expenses_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.st_income_expenses
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.st_income_expenses
	ADD CONSTRAINT fk_st_income_expenses_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.st_route
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.st_route
    ADD CONSTRAINT fk_st_route_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.st_route
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.st_route
	ADD CONSTRAINT fk_st_route_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_additional_charges
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.student_additional_charges
    ADD CONSTRAINT fk_student_additional_charges_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_additional_charges
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.student_additional_charges
	ADD CONSTRAINT fk_student_additional_charges_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_attendence
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.student_attendence
    ADD CONSTRAINT fk_student_attendence_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_attendence
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.student_attendence
	ADD CONSTRAINT fk_student_attendence_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_charges_summary
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.student_charges_summary
    ADD CONSTRAINT fk_student_charges_summary_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_charges_summary
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.student_charges_summary
	ADD CONSTRAINT fk_student_charges_summary_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_class_work_track
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.student_class_work_track
    ADD CONSTRAINT fk_student_class_work_track_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_class_work_track
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.student_class_work_track
	ADD CONSTRAINT fk_student_class_work_track_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_discount
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.student_discount
    ADD CONSTRAINT fk_student_discount_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_discount
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.student_discount
	ADD CONSTRAINT fk_student_discount_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_home_work_track
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.student_home_work_track
    ADD CONSTRAINT fk_student_home_work_track_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_home_work_track
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.student_home_work_track
	ADD CONSTRAINT fk_student_home_work_track_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_payments
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.student_payments
    ADD CONSTRAINT fk_student_payments_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.student_payments
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.student_payments
	ADD CONSTRAINT fk_student_payments_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.subject_chapter
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.subject_chapter
    ADD CONSTRAINT fk_subject_chapter_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.subject_chapter
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.subject_chapter
	ADD CONSTRAINT fk_subject_chapter_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.tag
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.tag
	ADD CONSTRAINT fk_tag_school_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.vendors
  ADD COLUMN  IF NOT EXISTS tenant_id bigint NULL;

ALTER TABLE public.vendors
    ADD CONSTRAINT fk_vendors_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.vendors (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

ALTER TABLE public.vendors
  ADD COLUMN  IF NOT EXISTS school_id bigint NULL;
  
ALTER TABLE public.tag
	ADD CONSTRAINT fk_vendors_id FOREIGN KEY (school_id)
    REFERENCES public.school (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

