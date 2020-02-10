package com.neighborhood.info.mappingcomplaint;

import com.neighborhood.info.mappingcomplaint.data.cache.DataCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ComplaintMappingApplication {

	public static void main(String[] args) {
       new DataCache(args[0], args[1], args[2]).loadCache();
		SpringApplication.run(ComplaintMappingApplication.class, args);
	}

}
