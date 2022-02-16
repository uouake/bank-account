package com.ostack.bankaccount.dao;

import com.ostack.bankaccount.entities.MyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyAccountRepo extends JpaRepository<MyAccount, Long> {
}
