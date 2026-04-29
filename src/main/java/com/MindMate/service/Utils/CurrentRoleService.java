package com.MindMate.service.Utils;

import com.MindMate.exception.customExceptions.ExpertNotFoundException;
import com.MindMate.exception.customExceptions.UserNotFoundException;
import com.MindMate.model.account.Account;
import com.MindMate.model.account.Expert;
import com.MindMate.model.account.User;
import com.MindMate.repository.AccountRepo;
import com.MindMate.repository.ExpertRepo;
import com.MindMate.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentRoleService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private AccountRepo accountRepo;

    public User getCurrentUser(){
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);
    }

    public Expert getCurrentExpert(){
        return expertRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(ExpertNotFoundException::new);
    }

    public Account getCurrentAccount(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account account =  accountRepo.findByUsername(username);
        if(account == null) throw new RuntimeException("username not found");
        return account;
    }
}
