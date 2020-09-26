package com.dreambuilders.springbatch.batch;

import com.dreambuilders.springbatch.model.COVIDReport;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DataProcessor implements ItemProcessor<COVIDReport, COVIDReport> {
    @Override
    public COVIDReport process(COVIDReport report) throws Exception {
        log.info("Processing report...");
        String country = report.getCountry().equals("India") ? "IND" : report.getCountry();
        report.setCountry(country);
        return report;
    }
}
