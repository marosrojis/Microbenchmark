package cz.rojik.controller.rest;

import cz.rojik.constants.MappingURLConstants;
import cz.rojik.entity.Project;
import cz.rojik.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.TEST2)
public class ProjectController2 {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public List<Project> getProjects() {
        return projectService.fetAll();
    }
}
