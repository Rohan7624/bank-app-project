package net.javaguides.banking.service.impl;

import jakarta.persistence.Id;
import net.javaguides.banking.dto.AccountDto;
import net.javaguides.banking.entity.Account;
import net.javaguides.banking.mapper.AccountMapper;
import net.javaguides.banking.repository.AccountRepository;
import net.javaguides.banking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;


    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.MapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.MapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto GetAccountById(Long id){

        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account Doesn't exits"));
        return AccountMapper.MapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, Double amount) {

        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account Doesn't exits"));

        Double total = account.getBalance()+amount;
        account.setBalance(total);
        Account savedAccount =  accountRepository.save(account);
        return AccountMapper.MapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, Double amount) {

        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account Doesn't exits"));

        if(account.getBalance() < amount){
            throw new RuntimeException("Insuffient account balance");
        }

        Double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.MapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
         return accounts.stream().map((account)-> AccountMapper.MapToAccountDto(account))
                 .collect(Collectors.toList());
    }

    @Override
    public void deleteAcount(Long id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account Doesn't exits"));
        accountRepository.deleteById(id);
    }


//    @Override
//    public List<AccountDto> getAllAccounts() {
//        List<Account> accounts = AccountRepository.findAll();
//        return null;
//    }


}
