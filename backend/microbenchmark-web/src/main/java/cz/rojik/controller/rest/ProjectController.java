package cz.rojik.controller.rest;

import cz.rojik.constants.MappingURLConstants;
import cz.rojik.entity.Project;
import cz.rojik.service.ProjectService;
import cz.rojik.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.TEST)
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public List<Project> getProjects() {

        SecurityHelper.getCurrentUser();
        return projectService.fetchAll();
    }
}
