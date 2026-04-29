package com.MindMate.controller.publicControllers;

import com.MindMate.dto.AccountDto.RegisterResponse;
import com.MindMate.dto.PageResponseDto;
import com.MindMate.dto.publicDto.SnapshotsDto;
import com.MindMate.dto.expertDto.ExpertProfileDto;
import com.MindMate.dto.publicDto.TipDto;
import com.MindMate.model.account.Expert;
import com.MindMate.dto.AccountDto.LoginRequest;
import com.MindMate.model.account.User;
import com.MindMate.service.ExpertService;
import com.MindMate.service.publicService.AccountService;
import com.MindMate.service.publicService.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
   private AccountService accountService;
    @Autowired
    private ExpertService expertService;
    @Autowired
    private TipService tipService;

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody User user){
      RegisterResponse response = accountService.registerUser(user);
        if(response == null) return new ResponseEntity<>("username Already exist" ,  HttpStatus.CONFLICT);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register/expert")
    public ResponseEntity<?> registerExpert(@RequestBody Expert expert){
       RegisterResponse response = accountService.registerExpert(expert);
        if(response == null) return new ResponseEntity<>("username Already exist" ,  HttpStatus.CONFLICT);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> accountLogin(@RequestBody LoginRequest req){
        return accountService.login(req);
    }

    @GetMapping("/getAllExperts")
    public ResponseEntity<PageResponseDto<ExpertProfileDto>> getAllExperts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(expertService.getAllExperts(page , size));
    }

    @GetMapping("/getExpert/{id}")
    public ResponseEntity<ExpertProfileDto> getExpertByExpertId(@PathVariable("id") Long id){
        return ResponseEntity.ok(expertService.getExpertById(id));
    }

    @GetMapping("/snapshots")
    public ResponseEntity<SnapshotsDto> getSnapshots(){

        return ResponseEntity.ok(accountService.getSnapshots());
    }

    @GetMapping("/tip")
    public ResponseEntity<TipDto> getTodayTip(){
        return ResponseEntity.ok(tipService.getTip());
    }
}
