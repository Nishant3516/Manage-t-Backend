--liquibase formatted sql
				
--changeset chandan:20220707111011
ALTER TABLE public.school_picture_gallery
  ADD COLUMN  IF NOT EXISTS event_id bigint NULL;
ALTER TABLE public.school_picture_gallery
  ADD COLUMN  IF NOT EXISTS tags character varying(2550) NULL;

ALTER TABLE public.school_picture_gallery
    ADD CONSTRAINT fk_school_picture_gallery_event_id FOREIGN KEY (event_id)
    REFERENCES public.school_event (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;


ALTER TABLE public.school_video_gallery
  ADD COLUMN  IF NOT EXISTS event_id bigint NULL;
ALTER TABLE public.school_video_gallery
  ADD COLUMN  IF NOT EXISTS tags character varying(2550) NULL;

ALTER TABLE public.school_video_gallery
    ADD CONSTRAINT fk_school_video_gallery_event_id FOREIGN KEY (event_id)
    REFERENCES public.school_event (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;
