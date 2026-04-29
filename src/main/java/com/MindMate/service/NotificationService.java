package com.MindMate.service;

import com.MindMate.exception.customExceptions.AccessDeniedException;
import com.MindMate.model.Notification;
import com.MindMate.model.account.Account;
import com.MindMate.model.enums.NotificationStatus;
import com.MindMate.repository.AccountRepo;
import com.MindMate.repository.NotificationRepo;
import com.MindMate.security.SecurityUtils;
import com.MindMate.service.Utils.CurrentRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private CurrentRoleService currentRoleService;
    @Autowired
    private AccountRepo accountRepo;

    public List<Notification> getAllNotification(){
       Account  account = currentRoleService.getCurrentAccount();
       return notificationRepo.findByAccount(account);
    }

    public void addNotification(Notification notification){
        notificationRepo.save(notification);
    }

    public Notification markAsRead(Long id) {
        Notification notification = notificationRepo.findById(id).orElseThrow(RuntimeException::new);
        if(notification.getStatus() == NotificationStatus.READ) return notification;
        if(!currentRoleService.getCurrentAccount().equals(notification.getAccount())) throw new AccessDeniedException();

        notification.setStatus(NotificationStatus.READ);
        return notificationRepo.save(notification);
    }
}
