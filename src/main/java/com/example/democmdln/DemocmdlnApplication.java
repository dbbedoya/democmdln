package com.example.democmdln;

import com.example.democmdln.service.ExportService;
import com.example.democmdln.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

import static java.lang.System.exit;

@SpringBootApplication
public class DemocmdlnApplication implements CommandLineRunner {

	@Autowired
	private ImportService importService;

	@Autowired
	private ExportService exportService;

	private LocalDateTime runDateTime;

	public static void main(String[] args) throws Exception {

		SpringApplication app = new SpringApplication(DemocmdlnApplication.class);

		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (args.length > 0) {
			//with args
		}else{
			//no args
		}

		runDateTime = LocalDateTime.now();

		System.out.println(importService.processImport(runDateTime));

		System.out.println(exportService.processExport(runDateTime));

		exit(0);
	}
}
