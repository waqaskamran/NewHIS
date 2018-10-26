package com.sd.his.service;

import com.sd.his.model.Drug;
import com.sd.his.repository.DrugRepository;
import com.sd.his.wrapper.DrugWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by jamal on 10/22/2018.
 */
@Service
public class DrugService {

    @Autowired
    DrugRepository drugRepository;

    public boolean isNameDrugDuplicateByName(String name) {
        return this.drugRepository.getDrugByName(name);
    }

    public boolean isNameDrugDuplicateByNameAndNotEqualId(long id,String name) {
        return this.drugRepository.getDrugByNameAndNotEqualId(id,name);
    }

    @Transactional
    public String saveDrug(DrugWrapper drugWrapper) {
        Drug drug = new Drug(drugWrapper);
        this.drugRepository.save(drug);
        return "";
    }

    public List<DrugWrapper> getPaginatedAllDrugs(Pageable pageable) {
        return this.drugRepository.findAllByCreatedOn(pageable);
    }

    public int countPaginatedAllDrugs() {
        return this.drugRepository.findAll().size();
    }

    @Transactional
    public boolean deleteDrug(long id) {
        Drug drug = this.drugRepository.findOne(id);
        if (drug != null) {
            this.drugRepository.delete(drug);
            return true;
        }
        return false;
    }

    public DrugWrapper getDrugWrapper(long id) {
        return this.drugRepository.getDrugById(id);
    }

    @Transactional
    public String updateDrug(DrugWrapper drugWrapper) {
        Drug drug = this.drugRepository.findOne(drugWrapper.getId());
        new Drug(drug, drugWrapper);
        this.drugRepository.save(drug);
        return "";
    }

    public List<DrugWrapper> searchDrugByParams(Pageable pageable, DrugWrapper drugWrapper) {
        return this.drugRepository.searchDrugByParams(pageable, drugWrapper.getName());
    }

}
