package org.lashop.newback.services.impl;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.CardDto;
import org.lashop.newback.models.Card;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.repositories.CardRepository;
import org.lashop.newback.services.CardsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardsServiceImpl implements CardsService {

    private final CardRepository cardsRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<CardDto> getUserCards(Long accountId) {
        return CardDto.from(cardsRepository.findAllByAccountId(accountId).orElseThrow(() -> new RuntimeException("account not found")));
    }

    @Override
    public void addNewCard(Long accountId, CardDto cardDto) {
        Card card = Card.builder()
                .cardNumber(cardDto.getCardNumber())
                .cardDate(cardDto.getCardDate())
                .cardCVV(cardDto.getCardCVV())
                .paySystem(cardDto.getPaySystem())
                .account(accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("account not found")))
                .build();

        cardsRepository.save(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        Optional<Card> card = cardsRepository.findById(cardId);

        card.ifPresent(value -> cardsRepository.deleteById(value.getId()));
    }
}
