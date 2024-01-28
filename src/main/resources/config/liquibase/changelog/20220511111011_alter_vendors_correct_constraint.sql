--liquibase formatted sql
				
--changeset chandan:20220511111011

ALTER TABLE public.vendors DROP CONSTRAINT fk_vendors_tenant_id;

ALTER TABLE public.vendors
    ADD CONSTRAINT fk_vendors_tenant_id FOREIGN KEY (tenant_id)
    REFERENCES public.tenant (id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION;

