package com.techelevator.services;

import org.springframework.stereotype.Component;

import com.techelevator.model.CatFact;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCatFactService implements CatFactService {

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public CatFact getFact() {
		String url = "https://cat-data.netlify.app/api/facts/random";
		CatFact catFact = restTemplate.getForObject(url, CatFact.class);
		return catFact;

	}

}
