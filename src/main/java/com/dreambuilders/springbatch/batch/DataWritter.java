package com.dreambuilders.springbatch.batch;

import com.dreambuilders.springbatch.model.COVIDReport;
import com.dreambuilders.springbatch.repository.COVIDRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataWritter implements ItemWriter<COVIDReport> {

    @Autowired
    private COVIDRepository covidRepository;

    @Override
    public void write(List<? extends COVIDReport> items) throws Exception {
        covidRepository.saveAll(items);
    }
}
