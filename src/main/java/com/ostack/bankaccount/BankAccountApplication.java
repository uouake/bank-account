package com.ostack.bankaccount;

import com.ostack.bankaccount.dao.MyAccountRepo;
import com.ostack.bankaccount.dao.OperationRepo;
import com.ostack.bankaccount.entities.Deposit;
import com.ostack.bankaccount.entities.MyAccount;
import com.ostack.bankaccount.entities.Withdraw;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class BankAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}


	@Bean
	ApplicationRunner init(MyAccountRepo myAccountRepo, OperationRepo operationRepo) {
		return args -> {
			MyAccount myAccount = MyAccount.builder()
					.creationDate(new Date())
					.balance(0.0)
					.build();

			MyAccount account = myAccountRepo.save(myAccount);

			Deposit depo1 = Deposit.builder()
					.amount(1)
					.operationDate(new Date())
					.myAccount(account)
					.build();
			Deposit depo2 = Deposit.builder()
					.amount(2)
					.myAccount(account)
					.operationDate(new Date())
					.build();

			Withdraw withdraw1 = Withdraw.builder()
					.amount(1)
					.myAccount(account)
					.operationDate(new Date())
					.build();
			Withdraw withdraw2 = Withdraw.builder()
					.amount(2)
					.myAccount(account)
					.operationDate(new Date())
					.build();

			Stream.of(depo1, depo2, withdraw1, withdraw2).forEach(operationRepo::save);

		};
	}

}
