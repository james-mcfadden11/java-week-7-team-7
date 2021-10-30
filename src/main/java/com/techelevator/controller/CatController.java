package com.techelevator.controller;

import com.techelevator.dao.CatCardDao;
import com.techelevator.model.CatCard;
import com.techelevator.model.CatCardNotFoundException;
import com.techelevator.services.CatFactService;
import com.techelevator.services.CatPicService;
import com.techelevator.services.RestCatFactService;
import com.techelevator.services.RestCatPicService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping (path = "/api/cards")
public class CatController {

    private CatCardDao catCardDao;
    private CatFactService catFactService;
    private CatPicService catPicService;

    public CatController(CatCardDao catCardDao, CatFactService catFactService, CatPicService catPicService) {
        this.catCardDao = catCardDao;
        this.catFactService = catFactService;
        this.catPicService = catPicService;
    }

    // GET /api/cards: Provides a list of all Cat Cards in the user's collection.
    @RequestMapping (path = "", method = RequestMethod.GET)
    public List<CatCard> listCatCards() {
        return catCardDao.list();
    }

    // GET /api/cards/{id}: Provides a Cat Card with the given ID.
    @RequestMapping (path = "/{id}", method = RequestMethod.GET)
    public CatCard getCardWithId(@PathVariable long id) throws CatCardNotFoundException {
        // try/catch not necessary?
        return catCardDao.get(id);
    }

    // GET /api/cards/random: Provides a new, randomly created Cat Card
    // containing information from the cat fact and picture services.
    @RequestMapping (path = "/random", method = RequestMethod.GET)
    public CatCard getRandomCatCard() {
        CatCard catCard = new CatCard();
        RestCatFactService catFact = new RestCatFactService();
        RestCatPicService catPic = new RestCatPicService();

        catCard.setCatFact(catFact.getFact().getText());
        catCard.setImgUrl(catPic.getPic().getFile());

        return catCard;
    }

    // POST /api/cards: Saves a card to the user's collection.
    @RequestMapping (path = "", method = RequestMethod.POST)
    public boolean saveCard(@Valid @RequestBody CatCard catCard) {
        boolean wasSuccessful;
        try {
            catCardDao.save(catCard);
            wasSuccessful = true;
        } catch (CatCardNotFoundException ex) {
            System.out.println(ex.getMessage());
            wasSuccessful = false;
        }
        return wasSuccessful;
    }

    // PUT /api/cards/{id}: Updates a card in the user's collection
    @RequestMapping (path = "/{id}", method = RequestMethod.PUT)
    public boolean updateCard(@Valid @RequestBody CatCard catCard, @PathVariable long id) {
        boolean wasSuccessful;
        try {
            catCardDao.update(catCard.getCatCardId(), catCard);
            wasSuccessful = true;
        } catch (CatCardNotFoundException ex) {
            System.out.println(ex.getMessage());
            wasSuccessful = false;
        }
        return wasSuccessful;
    }

    // DELETE /api/cards/{id}: Removes a card from the user's collection.
    @RequestMapping (path = "/{id}", method = RequestMethod.DELETE)
    public void deleteCard(@Valid @PathVariable long id) throws CatCardNotFoundException {
        catCardDao.delete(id);
    }

}
