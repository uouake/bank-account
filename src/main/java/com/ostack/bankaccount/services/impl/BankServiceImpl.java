package com.ostack.bankaccount.services.impl;

import com.ostack.bankaccount.dao.MyAccountRepo;
import com.ostack.bankaccount.dao.OperationRepo;
import com.ostack.bankaccount.dto.OperationDto;
import com.ostack.bankaccount.entities.Deposit;
import com.ostack.bankaccount.entities.MyAccount;
import com.ostack.bankaccount.entities.Operation;
import com.ostack.bankaccount.entities.Withdraw;
import com.ostack.bankaccount.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class BankServiceImpl implements BankService {

    private final MyAccountRepo myAccountRepo;
    private final OperationRepo operationRepo;

    @Autowired
    public BankServiceImpl(MyAccountRepo myAccountRepo, OperationRepo operationRepo) {
        this.myAccountRepo = myAccountRepo;
        this.operationRepo = operationRepo;
    }

    @Override
    public MyAccount viewAccount(Long accountId) {
        Optional<MyAccount> accountOptional = myAccountRepo.findById(accountId);

        if (accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            throw new RuntimeException("Account does not exist");
        }
    }

    @Override
    public void makeDeposit(Long accountId, int amount) {
        MyAccount account = viewAccount(accountId);
        double balanceInMyAccount = account.getBalance();

        Deposit deposit = Deposit.builder()
                .operationDate(new Date())
                .myAccount(account)
                .amount(amount)
                .build();

        List<Operation> operationList = new ArrayList<>(account.getOperations());
        operationList.add(deposit);
        operationRepo.save(deposit);

        account.setBalance(balanceInMyAccount + amount);
        account.setOperations(operationList);
        myAccountRepo.save(account);
    }

    @Override
    public void makeWithdraw(Long accountId, int amount) {
        MyAccount account = viewAccount(accountId);
        double balanceInMyAccount = account.getBalance();

        boolean hasEnoughtBalance = account.getBalance() >= amount;

        if (!hasEnoughtBalance) {
            throw new RuntimeException("Balance not enought");
        }

        Withdraw withdraw = Withdraw.builder()
                .operationDate(new Date())
                .myAccount(account)
                .amount(amount)
                .build();

        List<Operation> operationList = new ArrayList<>(account.getOperations());
        operationList.add(withdraw);
        operationRepo.save(withdraw);

        account.setBalance(balanceInMyAccount - amount);
        account.setOperations(operationList);
        myAccountRepo.save(account);
    }

    @Override
    public List<OperationDto> viewAccountStatement(Long accountId) {
        List<OperationDto> operationDtos = new ArrayList<>();
        List<Operation> operations = operationRepo.findAllByMyAccount_Id(accountId);
        operations.forEach(operation -> {
            operationDtos.add(OperationDto.builder()
                    .id(operation.getId())
                    .operationDate(operation.getOperationDate())
                    .amount(operation.getAmount())
                    .typeOp(operation instanceof Deposit ? "DEPOSIT" : "WITHDRAW")
                    .build());
        });
        return operationDtos;
    }
}
