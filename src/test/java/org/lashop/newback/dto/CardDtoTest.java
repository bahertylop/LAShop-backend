package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Card;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardDtoTest {

    @Test
    void testFromForOneObject() {
        CardDto cardDtoRes = CardDto.builder()
                .id(1)
                .paySystem("Visa")
                .cardNumber("1234567890123456")
                .cardDate("12/24")
                .cardCVV("123")
                .accountId(1)
                .build();

        Card card = Card.builder()
                .id(1)
                .paySystem("Visa")
                .cardNumber("1234567890123456")
                .cardDate("12/24")
                .cardCVV("123")
                .account(Account.builder().id(1).build())
                .build();

        CardDto cardDtoMy = CardDto.from(card);
        Assertions.assertEquals(cardDtoRes, cardDtoMy);
    }

    @Test
    void testFromForListOfObjects() {
        List<Card> cardList = List.of(
                Card.builder()
                        .id(1)
                        .paySystem("Visa")
                        .cardNumber("1234567890123456")
                        .cardDate("12/24")
                        .cardCVV("123")
                        .account(Account.builder().id(1).build())
                        .build(),
                Card.builder()
                        .id(2)
                        .paySystem("MasterCard")
                        .cardNumber("9876543210987654")
                        .cardDate("12/25")
                        .cardCVV("456")
                        .account(Account.builder().id(2).build())
                        .build()
        );

        List<CardDto> cardDtoList = CardDto.from(cardList);

        List<CardDto> expectedCardDtoList = List.of(
                CardDto.builder()
                        .id(1)
                        .paySystem("Visa")
                        .cardNumber("1234567890123456")
                        .cardDate("12/24")
                        .cardCVV("123")
                        .accountId(1)
                        .build(),
                CardDto.builder()
                        .id(2)
                        .paySystem("MasterCard")
                        .cardNumber("9876543210987654")
                        .cardDate("12/25")
                        .cardCVV("456")
                        .accountId(2)
                        .build()
        );

        Assertions.assertEquals(expectedCardDtoList, cardDtoList);
    }
}