package com.rahul.userservice.service;

import com.rahul.userservice.dto.LoginRequestDto;
import com.rahul.userservice.dto.LoginResponseDto;
import com.rahul.userservice.dto.SignupRequestDto;
import com.rahul.userservice.dto.UserDto;
import com.rahul.userservice.entity.User;
import com.rahul.userservice.exception.BadRequestException;
import com.rahul.userservice.exception.ResourceNotFoundException;
import com.rahul.userservice.repo.UserRepo;
import com.rahul.userservice.utils.Bcrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signUp(SignupRequestDto signupRequestDto){
        log.info("Signup a user with email: {}", signupRequestDto.getEmail());

        boolean exists = userRepo.existsByEmail(signupRequestDto.getEmail());
        if(exists){
            throw new BadRequestException("User already exists");
        }
        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(Bcrypt.hash(signupRequestDto.getPassword()));

        user = userRepo.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        log.info("Login req for a user with email: {}", loginRequestDto.getEmail());

        User user = userRepo.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new BadRequestException("Incorrect Email or Password"));

        boolean isPasswordMatch = Bcrypt.match(loginRequestDto.getPassword(), user.getPassword());
        if(!isPasswordMatch){
            throw new BadRequestException("Incorrect Email or Password");
        }
        String token = jwtService.generateAccessToken(user);
        return new LoginResponseDto(
                token,
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }
}
