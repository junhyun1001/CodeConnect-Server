package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.member.ProfileDto;
import CodeConnect.CodeConnect.dto.member.SignUpRequestDto;
import CodeConnect.CodeConnect.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseDto<List<ProfileDto>> getProfile(@AuthenticationPrincipal String email){
        return profileService.showProfile(email);
    }

}
