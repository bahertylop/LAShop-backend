package org.lashop.newback.services.impl;

import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.AddressDto;
import org.lashop.newback.models.Address;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.repositories.AddressRepository;
import org.lashop.newback.services.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<AddressDto> getAllAddresses(Long accountId) {
        return AddressDto.from(addressRepository.findAllByAccountId(accountId).orElseThrow(() -> new RuntimeException("account not found")));
    }

    @Override
    public void addNewAddress(Long accountId, String address) {
        Address addressNew = Address.builder()
                .address(address)
                .account(accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("account not found")))
                .build();
        addressRepository.save(addressNew);
    }

    @Override
    public void deleteAddress(Long accountId, Long addressId) {
        Optional<Address> address = addressRepository.findById(addressId);

        address.ifPresent(value -> addressRepository.deleteById(value.getId()));
    }
}
