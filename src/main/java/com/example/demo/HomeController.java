package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CloudinaryConfig cloudc;

//    @RequestMapping("/login")
//    public String login() {
//        return "login";
//    }

    @RequestMapping("/")
    public String listPosts(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "postform";
    }

    @PostMapping("/add")
    public String processPost(@ModelAttribute Post post, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            post.setImage(uploadResult.get("url").toString());
            postRepository.save(post);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }
    @RequestMapping("detail/{id}")
    public String showPost(@PathVariable("id") long id, Model model){
        model.addAttribute("post", postRepository.findById(id).get());
        return "show";
    }
    @RequestMapping("update/{id}")
    public String updatePost(@PathVariable("id") long id, Model model){
        model.addAttribute("post", postRepository.findById(id).get());
        return "postform";
    }
    @RequestMapping("delete/{id}")
    public String delPost(@PathVariable("id") long id){
        postRepository.deleteById(id);
        return "redirect:/";
    }
}



