package com.ssik.manageit.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

	private GitProperties gitProperties;
	private BuildProperties buildProperties;
	private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

	public CacheConfiguration(JHipsterProperties jHipsterProperties) {
		JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

		jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Object.class, Object.class,
						ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
				.withExpiry(
						ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
				.build());
	}

	@Bean
	public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
		return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
	}

	@Bean
	public JCacheManagerCustomizer cacheManagerCustomizer() {
		return cm -> {
			createCache(cm, com.ssik.manageit.repository.UserRepository.USERS_BY_LOGIN_CACHE);
			createCache(cm, com.ssik.manageit.repository.UserRepository.USERS_BY_EMAIL_CACHE);
			createCache(cm, com.ssik.manageit.domain.User.class.getName());
			createCache(cm, com.ssik.manageit.domain.Authority.class.getName());
			createCache(cm, com.ssik.manageit.domain.User.class.getName() + ".authorities");
			createCache(cm, com.ssik.manageit.domain.AuditLog.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolReport.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolReport.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.SchoolDaysOff.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolDaysOff.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.SchoolEvent.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolEvent.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.SchoolPictureGallery.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolPictureGallery.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.SchoolVideoGallery.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolVideoGallery.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.SchoolUser.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolUser.class.getName() + ".auditLogs");
			createCache(cm, com.ssik.manageit.domain.SchoolUser.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.SchoolUser.class.getName() + ".classSubjects");
			createCache(cm, com.ssik.manageit.domain.School.class.getName());
			createCache(cm, com.ssik.manageit.domain.School.class.getName() + ".idStores");
			createCache(cm, com.ssik.manageit.domain.School.class.getName() + ".auditLogs");
			createCache(cm, com.ssik.manageit.domain.School.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.School.class.getName() + ".schoolLedgerHeads");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".classStudents");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".classLessionPlans");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".schools");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".schoolNotifications");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".classFees");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".classSubjects");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".schoolUsers");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".schoolDaysOffs");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".schoolEvents");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".schoolPictureGalleries");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".vchoolVideoGalleries");
			createCache(cm, com.ssik.manageit.domain.SchoolClass.class.getName() + ".schoolReports");
			createCache(cm, com.ssik.manageit.domain.IdStore.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolNotifications.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolNotifications.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.ClassStudent.class.getName());
			createCache(cm, com.ssik.manageit.domain.ClassStudent.class.getName() + ".studentDiscounts");
			createCache(cm, com.ssik.manageit.domain.ClassStudent.class.getName() + ".studentAdditionalCharges");
			createCache(cm, com.ssik.manageit.domain.ClassStudent.class.getName() + ".studentPayments");
			createCache(cm, com.ssik.manageit.domain.ClassStudent.class.getName() + ".studentAttendences");
			createCache(cm, com.ssik.manageit.domain.ClassStudent.class.getName() + ".studentHomeWorkTracks");
			createCache(cm, com.ssik.manageit.domain.ClassStudent.class.getName() + ".studentClassWorkTracks");
			createCache(cm, com.ssik.manageit.domain.SchoolLedgerHead.class.getName());
			createCache(cm, com.ssik.manageit.domain.SchoolLedgerHead.class.getName() + ".classFees");
			createCache(cm, com.ssik.manageit.domain.SchoolLedgerHead.class.getName() + ".studentDiscounts");
			createCache(cm, com.ssik.manageit.domain.SchoolLedgerHead.class.getName() + ".studentAdditionalCharges");
			createCache(cm, com.ssik.manageit.domain.SchoolLedgerHead.class.getName() + ".schools");
			createCache(cm, com.ssik.manageit.domain.ClassFee.class.getName());
			createCache(cm, com.ssik.manageit.domain.ClassFee.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.StudentDiscount.class.getName());
			createCache(cm, com.ssik.manageit.domain.StudentAdditionalCharges.class.getName());
			createCache(cm, com.ssik.manageit.domain.StudentPayments.class.getName());
			createCache(cm, com.ssik.manageit.domain.StudentAttendence.class.getName());
			createCache(cm, com.ssik.manageit.domain.ClassHomeWork.class.getName());
			createCache(cm, com.ssik.manageit.domain.ClassHomeWork.class.getName() + ".studentHomeWorkTracks");
			createCache(cm, com.ssik.manageit.domain.ClassClassWork.class.getName());
			createCache(cm, com.ssik.manageit.domain.ClassClassWork.class.getName() + ".studentClassWorkTracks");
			createCache(cm, com.ssik.manageit.domain.ClassLessionPlan.class.getName());
			createCache(cm, com.ssik.manageit.domain.ClassLessionPlan.class.getName() + ".classLessionPlanTracks");
			createCache(cm, com.ssik.manageit.domain.ClassLessionPlanTrack.class.getName());
			createCache(cm, com.ssik.manageit.domain.StudentHomeWorkTrack.class.getName());
			createCache(cm, com.ssik.manageit.domain.StudentClassWorkTrack.class.getName());
			createCache(cm, com.ssik.manageit.domain.ClassSubject.class.getName());
			createCache(cm, com.ssik.manageit.domain.ClassSubject.class.getName() + ".subjectChapters");
			createCache(cm, com.ssik.manageit.domain.ClassSubject.class.getName() + ".classLessionPlans");
			createCache(cm, com.ssik.manageit.domain.ClassSubject.class.getName() + ".schoolClasses");
			createCache(cm, com.ssik.manageit.domain.ClassSubject.class.getName() + ".schoolUsers");
			createCache(cm, com.ssik.manageit.domain.SubjectChapter.class.getName());
			createCache(cm, com.ssik.manageit.domain.SubjectChapter.class.getName() + ".chapterSections");
			createCache(cm, com.ssik.manageit.domain.SubjectChapter.class.getName() + ".classLessionPlans");
			createCache(cm, com.ssik.manageit.domain.ChapterSection.class.getName());
			createCache(cm, com.ssik.manageit.domain.ChapterSection.class.getName() + ".classHomeWorks");
			createCache(cm, com.ssik.manageit.domain.ChapterSection.class.getName() + ".classClassWorks");
			createCache(cm, com.ssik.manageit.domain.ChapterSection.class.getName() + ".classLessionPlans");
			createCache(cm, com.ssik.manageit.domain.StudentChargesSummary.class.getName());
			createCache(cm, com.ssik.manageit.domain.ClassStudent.class.getName() + ".studentChargesSummaries");
			createCache(cm, com.ssik.manageit.domain.SchoolLedgerHead.class.getName() + ".studentChargesSummaries");
			// jhipster-needle-ehcache-add-entry
		};
	}

	private void createCache(javax.cache.CacheManager cm, String cacheName) {
		javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
		if (cache != null) {
			cache.clear();
		} else {
			cm.createCache(cacheName, jcacheConfiguration);
		}
	}

	@Autowired(required = false)
	public void setGitProperties(GitProperties gitProperties) {
		this.gitProperties = gitProperties;
	}

	@Autowired(required = false)
	public void setBuildProperties(BuildProperties buildProperties) {
		this.buildProperties = buildProperties;
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
	}
}
