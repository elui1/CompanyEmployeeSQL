package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("companies", companyRepository.findAll());
        return "index";
    }

    @GetMapping("/addCompany")
    public String addCompany(Model model) {
        model.addAttribute("company", new Company());
        return "companyform";
    }

    @PostMapping("/processCompany")
    public String processCompany(@ModelAttribute Company company) {
        companyRepository.save(company);
        return "redirect:/";
    }

    @GetMapping("/addEmployee")
    public String addEmployee(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
        model.addAttribute("employee", new Employee());
        return "employeeform";
    }

    @PostMapping("/processEmployee")
    public String processEmployee(@ModelAttribute Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showEmployee(@PathVariable("id") long id, Model model) {
        model.addAttribute("employee", employeeRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable("id") long id, Model model) {
        model.addAttribute("employee", employeeRepository.findById(id).get());
        model.addAttribute("companies", companyRepository.findAll());
        return "employeeform";
    }

    @RequestMapping("/updateCompany/{id}")
    public String updateCompany(@PathVariable("id") long id, Model model) {
//        model.addAttribute("employee", employeeRepository.findById(id).get());
        model.addAttribute("company", companyRepository.findById(id).get());
        return "companyform";
    }

    @RequestMapping("/deleteEmployee/{id}")
    public String delEmployee(@PathVariable("id") long id) {
        employeeRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/deleteCompany/{id}")
    public String delCompany(@PathVariable("id") long id) {
        Company company = companyRepository.findById(id).get();
        Set<Employee> employees = company.getEmployees();
        for(Employee employee : employees){
            employee.setCompany(null);
            employeeRepository.save(employee);
        }
        employees = new HashSet<>();
        company.setEmployees(employees);
        companyRepository.save(company);
        companyRepository.deleteById(id);
        return "redirect:/";
    }
    //        employeeRepository.deleteAll();
    @RequestMapping("/search")
    public String search(@RequestParam("search") String search, Model model) {
        model.addAttribute("employees", employeeRepository.findByName(search));
        return "searchlist";
    }
}
