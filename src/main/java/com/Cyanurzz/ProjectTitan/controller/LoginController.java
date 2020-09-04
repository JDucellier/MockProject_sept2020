package com.Cyanurzz.ProjectTitan.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Cyanurzz.ProjectTitan.entity.User;
import com.Cyanurzz.ProjectTitan.repository.UserRepository;


@Controller
public class LoginController  {
	
	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
    
	@GetMapping("/login")
	public String showLogin(Model model, @RequestParam (value = "error", required = false) String error) {
		if (error != null) {
			model.addAttribute("errorMessage", "Identifiants incorrects !");
		}
		return "login";
	}
	
	@GetMapping("/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/?logout=true";
	}

	@GetMapping("/register")
	public String showRegister(Model model) {
	    model.addAttribute("user", new User());
		return "register";
	}
	
	@PostMapping("/register")
	public String addMember(RedirectAttributes redirAttrs, Model model, User user) {
		
		if (userRepository.findByUsername(user.getUsername()) != null) {
			model.addAttribute("errorMessage", "Pseudo déjà utilisé !");
			return "register";
		} else if (userRepository.findByEmail(user.getEmail()) != null){
			model.addAttribute("errorMessage", "Email déjà utilisé !");
			return "register";
		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole("MEMBER");
			userRepository.save(user);
			return "redirect:/login";
		}

	}
	
}
