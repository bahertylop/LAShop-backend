package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Address;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressDtoTest {

    @Test
    void testFromForOneObject() {
        AddressDto addressDtoRes = AddressDto.builder()
                .address("qwertyuiop")
                .id(7)
                .accountId(1)
                .build();
        Address address = Address.builder()
                .id(7)
                .address("qwertyuiop")
                .account(Account.builder().id(1).build())
                .build();

        AddressDto addressDtoMy = AddressDto.from(address);
        Assertions.assertEquals(addressDtoMy, addressDtoRes);
    }

    @Test
    void testFromForListOfObjects() {
        List<Address> addressList = List.of(
                Address.builder()
                        .id(1)
                        .address("123 Main St")
                        .account(Account.builder().id(1).build())
                        .build(),
                Address.builder()
                        .id(2)
                        .address("456 Elm St")
                        .account(Account.builder().id(2).build())
                        .build()
        );

        List<AddressDto> addressDtoList = AddressDto.from(addressList);

        List<AddressDto> expectedAddressDtoList = List.of(
                AddressDto.builder()
                        .id(1)
                        .address("123 Main St")
                        .accountId(1)
                        .build(),
                AddressDto.builder()
                        .id(2)
                        .address("456 Elm St")
                        .accountId(2)
                        .build()
        );

        Assertions.assertEquals(expectedAddressDtoList, addressDtoList);
    }

}