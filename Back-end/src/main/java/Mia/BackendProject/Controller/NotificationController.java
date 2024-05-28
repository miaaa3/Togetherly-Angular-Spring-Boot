package Mia.BackendProject.Controller;

import Mia.BackendProject.Dto.ReadNotifDto;
import Mia.BackendProject.Dto.UserByIdReq;

import Mia.BackendProject.Service.Implement.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("/getNotification")
    public ResponseEntity<?> GetNotificationByUser(@RequestBody UserByIdReq userByIdReq){
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userByIdReq.getIdUser()));
    }

    @PostMapping("/readNotification")
    public ResponseEntity<?> readNotification(@RequestBody ReadNotifDto readNotifDto){
        if (readNotifDto.getIdPost()==0){
            readNotifDto.setIdPost(-1);
        }


        return ResponseEntity.ok(notificationService.markNotificationAsRead(readNotifDto.getRecipientId(), readNotifDto.getUserFromId(), readNotifDto.getTypeNotif(),readNotifDto.getIdPost()));
    }

}
