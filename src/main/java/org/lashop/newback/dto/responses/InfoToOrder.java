package org.lashop.newback.dto.responses;

import lombok.Builder;
import lombok.Data;
import org.lashop.newback.dto.AddressDto;
import org.lashop.newback.dto.CardDto;
import org.lashop.newback.dto.CartDto;

import java.util.List;

@Data
@Builder
public class InfoToOrder {

    private List<AddressDto> addresses;
    private List<CardDto> cards;
    private List<CartDto> cart;
}
