package com.techelevator.services;

import org.springframework.stereotype.Component;

import com.techelevator.model.CatPic;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCatPicService implements CatPicService {

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public CatPic getPic() {
		String url = "https://cat-data.netlify.app/api/pictures/random";
		CatPic catPic = restTemplate.getForObject(url, CatPic.class);
		return catPic;

	}

}	
