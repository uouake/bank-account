package com.ostack.bankaccount.dao;

import com.ostack.bankaccount.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepo extends JpaRepository<Operation, Long> {

    List<Operation> findAllByMyAccount_Id(Long accountId);
}
