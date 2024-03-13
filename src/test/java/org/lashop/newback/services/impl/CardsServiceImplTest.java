package org.lashop.newback.services.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lashop.newback.dto.CardDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Card;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.repositories.CardRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CardsServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CardsServiceImpl cardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserCards() {
        Long accountId = 1L;
        List<Card> cards = new ArrayList<>();
        Account account = Account.builder().id(accountId).build();
        cards.add(Card.builder().id(1L).cardNumber("1111222233334444").account(account).build());
        cards.add(Card.builder().id(2L).cardNumber("5555666677778888").account(account).build());
        when(cardRepository.findAllByAccountId(accountId)).thenReturn(Optional.of(cards));

        List<CardDto> cardDtos = cardsService.getUserCards(accountId);

        Assertions.assertEquals(2, cardDtos.size());
        Assertions.assertEquals(1L, cardDtos.get(0).getId());
        Assertions.assertEquals("1111222233334444", cardDtos.get(0).getCardNumber());
        Assertions.assertEquals(2L, cardDtos.get(1).getId());
        Assertions.assertEquals("5555666677778888", cardDtos.get(1).getCardNumber());
    }

    @Test
    void testGetUserCardsThrowsException() {
        Long accountId = 1L;
        when(cardRepository.findAllByAccountId(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            cardsService.getUserCards(accountId);
        });
    }

    @Test
    void testAddNewCard() {
        Long accountId = 1L;
        CardDto cardDto = CardDto.builder()
                .cardNumber("1234567890123456")
                .cardDate("12/23")
                .cardCVV("123")
                .paySystem("Visa")
                .build();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(Account.builder().id(accountId).build()));

        cardsService.addNewCard(accountId, cardDto);

        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void testAddNewCardThrowsExceptionWhenAccountNotFound() {
        Long accountId = 1L;
        CardDto cardDto = CardDto.builder()
                .cardNumber("1234567890123456")
                .cardDate("12/23")
                .cardCVV("123")
                .paySystem("Visa")
                .build();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            cardsService.addNewCard(accountId, cardDto);
        });
    }

    @Test
    void testDeleteCard() {
        Long cardId = 1L;
        Optional<Card> card = Optional.of(Card.builder().id(cardId).build());
        when(cardRepository.findById(cardId)).thenReturn(card);

        cardsService.deleteCard(cardId);

        verify(cardRepository, times(1)).deleteById(cardId);
    }
}
