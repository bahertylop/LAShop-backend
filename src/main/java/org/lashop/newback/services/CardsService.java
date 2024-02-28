package org.lashop.newback.services;

import org.lashop.newback.dto.CardDto;
import org.lashop.newback.dto.CartDto;

import java.util.List;

public interface CardsService {

    List<CardDto> getAllCards(Long accountId);

    void addNewCard(Long accountId, CardDto cardDto);

    void deleteCard(Long cardId);
}
