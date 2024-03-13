package org.lashop.newback.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lashop.newback.dto.AddressDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Address;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.repositories.AddressRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAddresses() {
        Long accountId = 1L;
        List<Address> addresses = new ArrayList<>();
        addresses.add(Address.builder().id(1L).address("Address 1").account(Account.builder().id(accountId).build()).build());
        addresses.add(Address.builder().id(2L).address("Address 2").account(Account.builder().id(accountId).build()).build());
        when(addressRepository.findAllByAccountId(accountId)).thenReturn(Optional.of(addresses));

        List<AddressDto> addressDtos = addressService.getAllAddresses(accountId);

        Assertions.assertEquals(2, addressDtos.size());
        Assertions.assertEquals(1L, addressDtos.get(0).getId());
        Assertions.assertEquals("Address 1", addressDtos.get(0).getAddress());
        Assertions.assertEquals(2L, addressDtos.get(1).getId());
        Assertions.assertEquals("Address 2", addressDtos.get(1).getAddress());
    }

    @Test
    void testGetAllAddressesThrowsException() {
        Long accountId = 1L;
        when(addressRepository.findAllByAccountId(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            addressService.getAllAddresses(accountId);
        });
    }

    @Test
    void testAddNewAddress() {
        Long accountId = 1L;
        String address = "New Address";
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(Account.builder().id(accountId).build()));

        addressService.addNewAddress(accountId, address);

        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testAddNewAddressThrowsException() {
        Long accountId = 1L;
        String address = "address";
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            addressService.addNewAddress(accountId, address);
        });
    }

    @Test
    void testDeleteAddress() {
        Long accountId = 1L;
        Long addressId = 1L;
        Address address = Address.builder().id(addressId).address("Address").build();
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        addressService.deleteAddress(accountId, addressId);

        verify(addressRepository, times(1)).deleteById(addressId);
    }
}