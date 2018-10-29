package cz.rojik.controller.rest;

import cz.rojik.constants.MappingURLConstants;
import cz.rojik.backend.entity.ProjectEntity;
import cz.rojik.backend.service.ProjectService;
import cz.rojik.backend.util.SecurityHelper;
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
    public List<ProjectEntity> getProjects() {

        SecurityHelper.getCurrentUser();
        return projectService.fetchAll();
    }
}
