package org.lashop.newback.dto.responses;

import lombok.Builder;
import lombok.Data;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.dto.OrderDto;

import java.util.List;

@Data
@Builder
public class ProfileResponse {

    private AccountDto accountDto;

    private List<OrderDto> orders;
}
