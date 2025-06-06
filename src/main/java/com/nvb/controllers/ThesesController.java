/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.CommitteeListDTO;
import com.nvb.dto.ThesesDTO;
import com.nvb.dto.UserDTO;
import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.ThesesListDTO;
import com.nvb.pojo.UserRole;
import com.nvb.services.CommitteeService;
import com.nvb.services.EmailService;
import com.nvb.services.EvaluationCriteriaCollectionService;
import com.nvb.services.ThesesService;
import com.nvb.services.UserService;
import com.nvb.validators.WebAppValidator;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nguyenvanbao
 */
@Controller
@RequestMapping("/theses")
public class ThesesController {

    @Autowired
    private ThesesService thesesService;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private EvaluationCriteriaCollectionService evaluationCriteriaCollectionService;
    
    @Autowired
    private CommitteeService committeeService;

    @Autowired
    @Qualifier("thesesWebAppValidator")
    private WebAppValidator thesesWebAppValidator;

    @Autowired
    private EmailService emailService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(thesesWebAppValidator);
    }

    @GetMapping("")
    public String showAll(Model model, @RequestParam(required = false) Map<String, String> params) {
        try {
            List<ThesesListDTO> theses = thesesService.getAllForListView(params, true);
            model.addAttribute("theses", theses);
            int page = 1;
            if (params.get("page") != null) {
                try {
                    if (theses.isEmpty()) {
                        page = 0;
                    } else {
                        page = Integer.parseInt(params.get("page"));
                    }
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            model.addAttribute("page", page);
            return "theses/index";
        } catch (LazyInitializationException ex) {
            model.addAttribute("errorMessage", "Lỗi khi tải dữ liệu. Vui lòng thử lại sau.");
            return "theses/index";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        ThesesDTO thesesDTO = new ThesesDTO();
        List<UserDTO> lecturersList = userDetailsService.getAll(new HashMap<>(Map.of("role", UserRole.ROLE_LECTURER.toString())));
        List<UserDTO> studentsList = userDetailsService.getAll(new HashMap<>(Map.of("role", UserRole.ROLE_STUDENT.toString())));
        List<EvaluationCriteriaCollectionDTO> evaluationCriteriaCollectionsList = evaluationCriteriaCollectionService.getAll(new HashMap<>());
        List<CommitteeListDTO> committeeList = committeeService.getAllForListView(new HashMap<>(), true);

        model.addAttribute("theses", thesesDTO);
        model.addAttribute("lecturersList", lecturersList);
        model.addAttribute("studentsList", studentsList);
        model.addAttribute("evaluationCriteriaCollectionsList", evaluationCriteriaCollectionsList);
        model.addAttribute("reviewersList", lecturersList);
        model.addAttribute("committeeList", committeeList);
        // addAttribute committee sau 

        return "theses/add";
    }

    @PostMapping("/add")
    public String addTheses(Model model, @ModelAttribute("theses") @Valid ThesesDTO thesesDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<UserDTO> lecturersList = userDetailsService.getAll(
                    new HashMap<>(Map.of("role", UserRole.ROLE_LECTURER.toString())));
            List<UserDTO> studentsList = userDetailsService.getAll(
                    new HashMap<>(Map.of("role", UserRole.ROLE_STUDENT.toString())));
            List<EvaluationCriteriaCollectionDTO> evaluationCriteriaCollectionsList
                    = evaluationCriteriaCollectionService.getAll(new HashMap<>());

            model.addAttribute("lecturersList", lecturersList);
            model.addAttribute("studentsList", studentsList);
            model.addAttribute("evaluationCriteriaCollectionsList", evaluationCriteriaCollectionsList);
            model.addAttribute("reviewersList", lecturersList);
            return "theses/add";
        }
        thesesService.addOrUpdate(thesesDTO);
        if (thesesDTO.getReviewerId() != null) {
            try {
                String subject = "Thông báo phản biện khóa luận";
                String body = String.format(
                        "<h3>Thân gửi giảng viên,</h3>"
                        + "<p>Bạn vừa được phân công phản biện khóa luận <strong>%s</strong>.</p>",
                        thesesDTO.getTitle()
                );
                UserDTO reviewer = userDetailsService.get(Map.of("id", String.valueOf(thesesDTO.getReviewerId())));
                if (reviewer != null) {
                    emailService.sendEmail(reviewer.getEmail(), subject, body);
                }
            } catch (MessagingException e) {
            }
        }
        return "redirect:/theses";
    }
    
    @GetMapping("/{id}")
    public String update(Model model, @PathVariable(name = "id") int id){
        ThesesDTO thesesDTO = thesesService.get(new HashMap<>(Map.of("id", String.valueOf(id))));
        List<UserDTO> lecturersList = userDetailsService.getAll(new HashMap<>(Map.of("role", UserRole.ROLE_LECTURER.toString())));
        List<UserDTO> studentsList = userDetailsService.getAll(new HashMap<>(Map.of("role", UserRole.ROLE_STUDENT.toString())));
        List<EvaluationCriteriaCollectionDTO> evaluationCriteriaCollectionsList = evaluationCriteriaCollectionService.getAll(new HashMap<>());
        List<CommitteeListDTO> committeeList = committeeService.getAllForListView(new HashMap<>(), true);
        
        model.addAttribute("theses", thesesDTO);
        model.addAttribute("lecturersList", lecturersList);
        model.addAttribute("studentsList", studentsList);
        model.addAttribute("evaluationCriteriaCollectionsList", evaluationCriteriaCollectionsList);
        model.addAttribute("reviewersList", lecturersList);
        model.addAttribute("committeeList", committeeList);
        return "theses/add";
    }
}
