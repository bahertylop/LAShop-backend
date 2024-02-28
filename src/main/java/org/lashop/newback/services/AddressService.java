package org.lashop.newback.services;

import org.lashop.newback.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAllAddresses(Long accountId);

    void addNewAddress(Long accountId, String address);

    void deleteAddress(Long accountId, Long addressId);
}
