using System.Collections.Generic;
using CatCards.DAO;
using CatCards.Models;
using CatCards.Services;
using Microsoft.AspNetCore.Mvc;

namespace CatCards.Controllers
{
    [Route("/api/cards")]
    [ApiController]
    public class CatController : ControllerBase
    {
        private readonly ICatCardDao cardDao;
        private readonly ICatFactService catFactService;
        private readonly ICatPicService catPicService;

        public CatController(ICatCardDao _cardDao, ICatFactService _catFact, ICatPicService _catPic)
        {
            catFactService = _catFact;
            catPicService = _catPic;
            cardDao = _cardDao;
        }

        public ActionResult<List<CatCard>> GetAllCards()
        {
            return Ok(cardDao.GetAllCards());
        }

        [HttpGet("{id}")]
        public ActionResult<CatCard> GetCard(int id)
        {
            CatCard card = cardDao.GetCard(id);
            if (card != null)
            {
                return Ok(card);
            }
            else
            {
                return NotFound();
            }
        }

        [HttpGet("random")]
        public ActionResult<CatCard> GetRandomCard()
        {
            CatFact fact = catFactService.GetFact();
            CatPic pic = catPicService.GetPic();

            CatCard card = new CatCard()
            {
                CatFact = fact.Text,
                ImgUrl = pic.File
            };

            return card;
        }

        [HttpPost]
        public ActionResult<CatCard> SaveCard(CatCard incomingCard)
        {
            CatCard newCard = cardDao.SaveCard(incomingCard);
            return Created("/api/cards/" + newCard.CatCardId, newCard);
        }

        [HttpPut("{id}")]
        public ActionResult<CatCard> UpdateExistingCard(int id, CatCard changedCard)
        {
            if (cardDao.GetCard(id) != null)
            {
                if (changedCard.CatCardId == 0)
                {
                    changedCard.CatCardId = id;
                }
                return Ok(cardDao.UpdateCard(changedCard));
            }
            return NotFound();
        }

        [HttpDelete("{id}")]
        public ActionResult DeleteExistingCard(int id)
        {
            if (cardDao.GetCard(id) != null)
            {
                if (cardDao.RemoveCard(id))
                {
                    return NoContent();
                }
            }
            else
            {
                return NotFound();
            }
            return BadRequest();
        }
    }
}
