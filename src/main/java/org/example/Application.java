package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Application {
    private Logger logger;
    private FileHandler fileHandler;

    public Application(Logger logger) {
        this.logger = logger;
    }

    public void run(){
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("logger.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.addHandler(fileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        logger.info("Начало от " + LocalDateTime.now().toString());
        String dirTo = "";
        String pathProject = System.getProperty("user.dir");
        logger.info("текущая директория: " + pathProject);
        String fileSetting = "setting.xml";
        List<String> targetFrom = new ArrayList<>();
        SelectTarget selectTarget = new SelectTarget(pathProject,fileSetting);
        selectTarget.setFileHandler(fileHandler);
        selectTarget.fullTargetFromSetting();
        Map<String,String> targetMap = selectTarget.getMapTargets();
//        Map<String,String> targetFilter = selectTarget.getMapFilter();
        logger.info("прочитали файл настроек: " + fileSetting);
        LocalDate testLocalDate = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileSystem fs = FileSystems.getDefault();
        //System.out.println(testLocalDate);
        for (String key: targetMap.keySet()) {
            String cat = targetMap.get(key);
            //System.out.println("cat -- " + cat);
//            String filter = targetFilter.get(key);
//            System.out.println("filter -- " + filter);
            logger.info("смотрим каталог: " + cat);
            //System.out.println(cat);
            File dir = new File(cat);
            try(DirectoryStream<Path> paths = Files.newDirectoryStream(fs.getPath(cat),"*.BAK")) {
                for(Path path: paths){
                    //System.out.println("path -- " + path);
                    File file = path.toFile();
                    if(file.isFile()) {
                        logger.info(file.getName());
                        Date dateFile = new Date(file.lastModified());
                        logger.info("дата файла: " + dateFile.toInstant().toString());
                        LocalDate localDateFile = LocalDate.parse(dateFile.toInstant().toString().substring(0,10),df);
                        long unitlInDays = Math.abs(testLocalDate.until(localDateFile, ChronoUnit.DAYS));
                        //System.out.println("testLocalDate - localDateFile = " + unitlInDays);
                        logger.info("срок давности файла в днях: ");
                        if (unitlInDays >= 3){
                            logger.info("удаляем файл " + file.getName());
                            file.delete();
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            File[] files = dir.listFiles();
//            for (File file: files) {
//                if(file.isFile()) {
//                    logger.info(file.getName());
//                    Date dateFile = new Date(file.lastModified());
//                    logger.info("дата файла: " + dateFile.toInstant().toString());
//                    LocalDate localDateFile = LocalDate.parse(dateFile.toInstant().toString().substring(0,10),df);
//                    System.out.println("localDateFile " + localDateFile);
//                    long unitlInDays = Math.abs(testLocalDate.until(localDateFile, ChronoUnit.DAYS));
//                    System.out.println("testLocalDate - localDateFile = " + unitlInDays);
//                    if (unitlInDays >= 3){
//                        file.delete();
//                    }
//                }
//            }
        }
    }
}
