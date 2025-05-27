package com.skillverify.userservice.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper madeMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration()
			  .setFieldMatchingEnabled(true)
			  .setFieldAccessLevel(AccessLevel.PRIVATE)
			  .setSkipNullEnabled(true);

		return mapper;
	}

}
