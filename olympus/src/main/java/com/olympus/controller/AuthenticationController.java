package com.olympus.controller;

import com.olympus.config.security.AuthDetailsImpl;
import com.olympus.config.Constant;
import com.olympus.config.jwt.JwtProvider;
import com.olympus.dto.request.AuthRequest;
import com.olympus.dto.response.BaseResponse;
import com.olympus.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/v1/auth")
@Tag(name = "2. Authentication", description = "Authentication Management APIs")
public class AuthenticationController {
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final IAuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(JwtProvider jwtProvider,
                                    AuthenticationManager authenticationManager,
                                    IAuthenticationService authenticationService) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Authenticate an user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")}),
    })
    @SecurityRequirements
    @PostMapping()
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken((AuthDetailsImpl) authentication.getPrincipal());
        Map<String, String> data = new HashMap<>();
        data.put("token", jwt);
        authenticationService.reset(authRequest.getEmail());
        BaseResponse<Map<String, String>, ?> response = BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_AUTH, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Test authentication")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(),
                            mediaType = "application/json")}),
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/test")
    public ResponseEntity<?> testAuthenticate() {
        Map<String, String> data = new HashMap<>();
        data.put("OK", "User is authenticated");
        BaseResponse<Map<String, String>, ?> response = BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_OK, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
