package com.mycompany.springmarket.controller;

import com.mycompany.springmarket.entity.RoleEntity;
import com.mycompany.springmarket.entity.UserEntity;
import com.mycompany.springmarket.models.ERole;
import com.mycompany.springmarket.payload.request.LoginRequest;

import com.mycompany.springmarket.payload.request.SignupRequest;
import com.mycompany.springmarket.payload.response.JwtResponse;
import com.mycompany.springmarket.payload.response.MessageResponse;
import com.mycompany.springmarket.repository.RoleRepository;
import com.mycompany.springmarket.repository.UserRepository;
import com.mycompany.springmarket.security.jwt.JwtUtils;
import com.mycompany.springmarket.security.services.UserDetailsImpl;
import com.mycompany.springmarket.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        }catch (Exception e){
            e.printStackTrace();
            return (ResponseEntity<?>) ResponseEntity.status(31);
        }

    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        UserEntity user = new UserEntity();

        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<RoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        RoleEntity modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Unauthorized"));
        }
        try {
            String username = principal.getName();
            userService.deleteUserAndOrders(username);
            return ResponseEntity.ok(new MessageResponse("User and associated orders deleted successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error occurred while deleting user."));
        }
    }


}
