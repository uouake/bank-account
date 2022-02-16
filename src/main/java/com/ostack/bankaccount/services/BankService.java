package com.ostack.bankaccount.services;

import com.ostack.bankaccount.dto.OperationDto;
import com.ostack.bankaccount.entities.MyAccount;
import com.ostack.bankaccount.entities.Operation;

import java.util.List;

public interface BankService {

    MyAccount viewAccount(Long accountId);
    void makeDeposit(Long accountId, int amount);
    void makeWithdraw(Long accountId, int amount);
    List<OperationDto> viewAccountStatement(Long accountId);
}
