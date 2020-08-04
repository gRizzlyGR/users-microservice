package com.grizzi.microservices.users.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializer {
	public static String asJsonString(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsString(obj);
	}
}
