package com.jacky.backend.testtop5list;

import com.jacky.backend.testtop5list.dao.AccountRepository;
import com.jacky.backend.testtop5list.dao.MyBatisMapper;
import com.jacky.backend.testtop5list.dao.VisitHistoryRepository;
import com.jacky.backend.testtop5list.entity.Account;
import com.jacky.backend.testtop5list.entity.VisitHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Jacky Zhang
 * Application class to use Spring boot framework
 */
@SpringBootApplication
public class TestTop5ListApplication {
    private final static Logger LOGGER = Logger.getLogger(TestTop5ListApplication.class.getName());
    private VisitHistoryRepository visitHistoryRepository;
    private AccountRepository accountRepository;

    @Value("${mltest.csv.folder}")
    private String csvInputPath;
    @Value("${mltest.csv.backup}")
    private String csvBackupPath;
    @Value("${mltest.fs.pathSeparator}")
    private String pathSeparator;

    /**
     * main class to boot the application
     * @param args default parameters
     */
    public static void main(String[] args) {
        SpringApplication.run(TestTop5ListApplication.class, args);
    }


    @Autowired
    TestTop5ListApplication(VisitHistoryRepository visitHistoryRepository, AccountRepository accountRepository) {
        this.visitHistoryRepository = visitHistoryRepository;
        this.accountRepository = accountRepository;
    }

    @Bean
    CommandLineRunner init(AccountRepository accountRepository) {
        return (env) -> Arrays.stream("jacky,test,guest".split(","))
                .map(name -> new Account(name, "p")).forEach(
                        act -> {
                            accountRepository.save(act);
                        }
                );
    }
    /**
     * Scheduled task for importing the visit histories from csv file into DB
     */
    @Scheduled(fixedDelay = 60000)
    public void performCsvImport() {
        LOGGER.fine("Scheduler is running .... ");
        String csvFile = OpHelper.getCsvFile(csvInputPath);
        if (csvFile != null) {
            String csvFilePath = csvInputPath + pathSeparator + csvFile;
            List<VisitHistory> list = OpHelper.extractVisitHistories(csvFilePath);
            if (CollectionUtils.isEmpty(list)) {
                LOGGER.warning("No objects retrieved from the file: " + csvFilePath);
                return;
            }
            LOGGER.fine("Processing visit histories into database...");
            visitHistoryRepository.save(list);
            LOGGER.info("Processing visit histories successfully...record size: " + list.size());
            try {
                Files.move(Paths.get(csvFilePath), Paths.get(csvBackupPath).resolve(csvFile));
                LOGGER.info("Moved file to backup folder.");
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.warning("Failed to move file to backup folder.");
            }
        } else {
            LOGGER.warning("No new csv file to be processed...");
        }
    }

}
