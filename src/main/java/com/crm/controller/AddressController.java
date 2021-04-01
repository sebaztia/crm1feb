package com.crm.controller;

import com.crm.model.Addressbook;
import com.crm.repository.AddressbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AddressController {

	@Autowired
	AddressbookRepository addBookRepo;

	@GetMapping("/list")
	public String showTheList(Model model, @RequestParam (defaultValue="0") int page) {
		model.addAttribute("data", addBookRepo.findAll(/*new PageRequest(page, 5, new Sort(
				new Order(Sort.Direction.ASC, "id")))*/));
		model.addAttribute("currentPage", page);
		return "index2";
	}
	
	@PostMapping("/save2") 
	public String saveAddBook2 (Addressbook ab) {
		addBookRepo.save(ab);
		return "redirect:/list";
	}
	
	
	@GetMapping("/delete") 
	public String deleteAddBook (Long id) {
		addBookRepo.delete(id);
		return "redirect:/";
	}
	
	@GetMapping("/findOne") 
	@ResponseBody
	public Addressbook findOne (Long id) {
		return addBookRepo.findOne(id);
	}
	
}
