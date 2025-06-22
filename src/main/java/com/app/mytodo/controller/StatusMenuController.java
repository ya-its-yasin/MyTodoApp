package com.app.mytodo.controller;

import com.app.mytodo.bean.MyTaskStatusBean;
import com.app.mytodo.service.StatusMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/status")
public class StatusMenuController {

    @Autowired
    private StatusMenuService statusMenuService;

    @GetMapping
    public String getAllStatuses(Model model){
        List<MyTaskStatusBean> statusBeans = statusMenuService.getAllStatuses();
        model.addAttribute("statusBeans", statusBeans);
        model.addAttribute("newStatusBean", new MyTaskStatusBean());
        return "status-menu";
    }

    @PostMapping("/update")
    public String updateStatus(@ModelAttribute MyTaskStatusBean statusBean) {
        statusMenuService.updateStatus(statusBean);
        return "redirect:/status";
    }

    @PostMapping("/{id}/delete")
    public String deleteStatus(@PathVariable Integer id) {
        statusMenuService.deleteStatus(id);
        return "redirect:/status";
    }

    @PostMapping("/add")
    public String addStatus(@ModelAttribute("statusBean") MyTaskStatusBean statusBean) {
        if ("NA".equals(statusBean.getRight())) statusBean.setRight(null);
        if ("NA".equals(statusBean.getLeft())) statusBean.setLeft(null);
        if ("NA".equals(statusBean.getJumpTo())) statusBean.setJumpTo(null);
        statusMenuService.createStatus(statusBean);
        return "redirect:/status";
    }

    @GetMapping("/home")
    public String showManageStatusPage() {
        return "redirect:/";
    }

}
