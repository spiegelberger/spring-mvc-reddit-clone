package com.spiegelberger.springit.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spiegelberger.springit.domain.Comment;
import com.spiegelberger.springit.domain.Link;
import com.spiegelberger.springit.repository.CommentRepository;
import com.spiegelberger.springit.service.LinkService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class LinkController {
	
//	private static final Logger logger = LoggerFactory.getLogger(LinkController.class);
	
	
	private LinkService linkService;
	private CommentRepository commentRepository;
	
	
	@Autowired
	public LinkController(LinkService linkService, CommentRepository commentRepository) {
		this.linkService = linkService;
		this.commentRepository = commentRepository;
	}


	@GetMapping("/")
	public String list(Model model) {
		model.addAttribute("links", linkService.findAll());
		return "link/list";
	}
		
	@GetMapping("/link/{id}")
	public String read(@PathVariable Long id, Model model) {
		Optional<Link> optionalLink = linkService.findById(id);
		if(optionalLink.isPresent()) {
			Link currentLink = optionalLink.get();
			
			Comment comment = new Comment();
			comment.setLink(currentLink);
			
			model.addAttribute("comment", comment);			
			model.addAttribute("link", currentLink);
			model.addAttribute("success", model.containsAttribute("success"));
			return "link/view";
		}
		else {
			return "redirect:/";
		}
	}
	
	
	@GetMapping("/link/submit")
	public String newLinkForm(Model model) {
		model.addAttribute("link", new Link());
		return "link/submit";
	}
	
	
	@PostMapping("link/submit")
	public String createLink(@Valid Link link, BindingResult bindingResult,
					Model model, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			log.info("Validation errors were found while submitting a new link");
			model.addAttribute("link", link);
			return "link/submit";
		}
		else {
			linkService.save(link);
			log.info("New Link was saved successfully.");
			redirectAttributes
				.addAttribute("id", link.getId())
				.addFlashAttribute("success", true);
			return "redirect:/link/{id}";
		}
	}
	
	@Secured({"ROLE_USER"})
	@PostMapping("link/comments")
	public String addComment(@Valid Comment comment,BindingResult bindingResult,
					Model model, RedirectAttributes redirectAttributes) {
	    if( bindingResult.hasErrors() ) {
	        log.info("Something went wrong.");
	    } 
	    else {
	        log.info("New Comment was saved successfully!");
	        commentRepository.save(comment);
	    }
	    return "redirect:/link/" + comment.getLink().getId();
	}
	
	
}
