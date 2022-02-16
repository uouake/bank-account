package com.ostack.bankaccount.services.impl;

import com.ostack.bankaccount.dao.MyAccountRepo;
import com.ostack.bankaccount.dao.OperationRepo;
import com.ostack.bankaccount.dto.OperationDto;
import com.ostack.bankaccount.entities.Deposit;
import com.ostack.bankaccount.entities.MyAccount;
import com.ostack.bankaccount.entities.Operation;
import com.ostack.bankaccount.entities.Withdraw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class BankServiceImplTest {

    @Spy
    @InjectMocks
    BankServiceImpl bankService;

    @Mock
    MyAccountRepo myAccountRepo;

    @Mock
    OperationRepo operationRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void should_view_account_with_id_1() {
        Optional<MyAccount> mockedAccount = getMockedAccount();
        when(myAccountRepo.findById(1L)).thenReturn(mockedAccount);
        bankService.viewAccount(1L);
        assertThat(bankService.viewAccount(1L)).isEqualTo(mockedAccount.get());
    }

    @Test
    void should_make_deposit() {
        Optional<MyAccount> mockedAccount = getMockedAccount();
        when(myAccountRepo.findById(1L)).thenReturn(mockedAccount);
        bankService.makeDeposit(mockedAccount.get().getId(), 10);

        assertThat(mockedAccount.get().getBalance()).isEqualTo(20);
        assertThat(mockedAccount.get().getOperations().size()).isEqualTo(4);
    }

    @Test
    void should_make_withdraw() {
        Optional<MyAccount> mockedAccount = getMockedAccount();
        when(myAccountRepo.findById(1L)).thenReturn(mockedAccount);
        bankService.makeWithdraw(mockedAccount.get().getId(), 10);

        assertThat(mockedAccount.get().getBalance()).isEqualTo(0);
        assertThat(mockedAccount.get().getOperations().size()).isEqualTo(4);
    }

    @Test
    void should_view_account_statement() {
        Optional<MyAccount> mockedAccount = getMockedAccount();
        when(operationRepo.findAllByMyAccount_Id(1L)).thenReturn(mockedAccount.get().getOperations());
        bankService.viewAccountStatement(1L);

        assertThat(bankService.viewAccountStatement(1L).size()).isEqualTo(3);
        assertThat(bankService.viewAccountStatement(1L))
                .extracting(OperationDto::getTypeOp).contains("DEPOSIT", "WITHDRAW", "DEPOSIT");
    }

    private Optional<MyAccount> getMockedAccount() {
        List<Operation> operationList = Arrays.asList(
                                            Deposit.builder()
                                                    .id(2L)
                                                    .amount(1)
                                                    .operationDate(new Date())
                                                    .build(),
                                            Withdraw.builder()
                                                    .id(1L)
                                                    .operationDate(new Date())
                                                    .amount(1)
                                                    .build(),
                                            Deposit.builder()
                                                    .id(3L)
                                                    .amount(5)
                                                    .operationDate(new Date())
                                                    .build()
                                    );

        return Optional.of(MyAccount.builder()
                            .id(1L)
                            .balance(10)
                            .creationDate(new Date())
                            .operations(operationList)
                            .build());
    }
}