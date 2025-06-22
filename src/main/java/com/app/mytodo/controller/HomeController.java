package com.app.mytodo.controller;

import com.app.mytodo.bean.MyTaskBean;
import com.app.mytodo.entity.MyTaskStatus;
import com.app.mytodo.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    HomeService homeService;

    @GetMapping()
    public String hello(Model model) {
        Map<MyTaskStatus, List<MyTaskBean>> taskBeansMap;
        taskBeansMap = homeService.getAllTasks();
        model.addAttribute("taskMap", taskBeansMap);
        model.addAttribute("myTaskBean", new MyTaskBean());
        return "home";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute("myTaskBean") MyTaskBean myTaskBean) {
        homeService.addTask(myTaskBean);
        return "redirect:/";
    }

    @PostMapping("/myTasks/{id}/moveright")
    public String moveRight(@PathVariable Integer id) {
        homeService.moveStatusRight(id);
        return "redirect:/";
    }

    @PostMapping("/myTasks/{id}/moveleft")
    public String moveLeft(@PathVariable Integer id) {
        homeService.moveStatusLeft(id);
        return "redirect:/";
    }

    @PostMapping("/myTasks/{id}/updateDescription")
    public String updateDescription(@PathVariable Integer id, @RequestParam String description) {
        homeService.updateDescription(id, description);
        return "redirect:/";
    }

    @PostMapping("/myTasks/{id}/delete")
    public String deleteTask(@PathVariable Integer id) {
        homeService.deleteTask(id);
        return "redirect:/";
    }

    @PostMapping("/myTasks/{id}/jump")
    public String jumpTask(@PathVariable Integer id) {
        homeService.jumpTask(id);
        return "redirect:/";
    }

    @GetMapping("/status-menu")
    public String showManageStatusPage() {
        return "redirect:/status";
    }

/*    @PostMapping("/myTasks/moveAllReadyToDone")
    public String moveAllReadyToDone() {
        homeService.moveAllReadyToDone();
        return "redirect:/";
    }*/

}
