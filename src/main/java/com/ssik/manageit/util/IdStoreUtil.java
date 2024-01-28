package com.ssik.manageit.util;

import com.ssik.manageit.domain.enumeration.IdType;
import com.ssik.manageit.service.IdStoreQueryService;
import com.ssik.manageit.service.IdStoreService;
import com.ssik.manageit.service.dto.IdStoreDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdStoreUtil {

    @Autowired
    IdStoreService idStoreService;

    @Autowired
    IdStoreQueryService idStoreQueryService;

    public Long getAndSaveNextId(IdType idType) {
        IdStoreDTO foundIdStore = null;
        Long nextId = null;
        List<IdStoreDTO> idStores = idStoreService.findAll();
        for (IdStoreDTO idStore : idStores) {
            if (idStore.getEntrytype().name().equalsIgnoreCase(idType.name())) {
                foundIdStore = idStore;
                break;
            }
        }
        nextId = foundIdStore.getLastGeneratedId() + 1;
        foundIdStore.setLastGeneratedId(nextId);
        idStoreService.save(foundIdStore);
        return nextId;
    }
}
