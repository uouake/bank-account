package com.ostack.bankaccount.controllers;

import com.ostack.bankaccount.dto.OperationDto;
import com.ostack.bankaccount.entities.MyAccount;
import com.ostack.bankaccount.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/{id}")
public class BankController {

    private final BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public MyAccount viewAccount(@PathVariable Long id) {
        return bankService.viewAccount(id);
    }

    @GetMapping(value = "/statements")
    public List<OperationDto> viewAccountStatement(@PathVariable Long id) {
        return bankService.viewAccountStatement(id);
    }

    @PostMapping(value = "/operation")
    public void makeOperations(@PathVariable Long id,
                               @RequestParam(name = "typeOperation") String typeOperation,
                               @RequestParam(name = "amount") int amount) {
        if (typeOperation.equalsIgnoreCase("DEPOT")) {
            bankService.makeDeposit(id, amount);
        } else {
            bankService.makeWithdraw(id, amount);
        }
    }

}
