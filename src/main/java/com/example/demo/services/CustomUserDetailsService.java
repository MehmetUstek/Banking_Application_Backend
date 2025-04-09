package com.example.demo.services;

import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        public CustomUserDetailsService(UserRepository userRepository) {
                this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String customerNumber) throws UsernameNotFoundException {
                User user = userRepository.findById(customerNumber)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + customerNumber));

                List<GrantedAuthority> authorities = user.getAuthorities()
                                .stream()
                                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                                .collect(Collectors.toList());

                return new org.springframework.security.core.userdetails.User(
                                user.getCustomerNumber(),
                                user.getPassword(),
                                user.isEnabled(),
                                true, // accountNonExpired
                                true, // credentialsNonExpired
                                true, // accountNonLocked
                                authorities);
        }
}