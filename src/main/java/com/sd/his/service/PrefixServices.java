package com.sd.his.service;

import com.sd.his.model.Prefix;
import com.sd.his.repository.PrefixRepository;
import com.sd.his.wrapper.PrefixWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PrefixServices {

    @Autowired
    PrefixRepository prefixRepository;

    public List<Prefix> getAll(){
        return prefixRepository.getAll();
    }

    @Transactional(rollbackOn = Throwable.class)
    public void saveConfiguration(PrefixWrapper prefixWrapper){
        Prefix prefix = this.prefixRepository.getOne(prefixWrapper.getId());
        prefix.setName(prefixWrapper.getName());
        prefix.setModule(prefixWrapper.getModule());
        prefix.setCurrentValue(prefixWrapper.getCurrentValue());
        prefix.setStartValue(prefixWrapper.getStartValue());
        prefixRepository.save(prefix);
    }
}
