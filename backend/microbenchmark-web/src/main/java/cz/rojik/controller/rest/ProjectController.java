package cz.rojik.controller.rest;

import cz.rojik.constants.MappingURLConstants;
import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.LibrariesToChooseDTO;
import cz.rojik.service.dto.ProjectDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.ImportsToChooseException;
import cz.rojik.service.exception.MavenCompileException;
import cz.rojik.service.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller for java project manipulation.
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.PROJECT)
public class ProjectController {

    private static Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    /**
     * Create maven project with all methods to measure.
     * At first method generate maven project with JMH annotations, configuration and method to measure.
     * Second method analyze java code and generate imports selected java classes.
     * @param template methods to measure
     * @return if everything is successful, return generated unique project ID. Some java classes with the same name are in more than one package. If needed to select right java class with package, return map of undecided libraries.
     */
    @PostMapping(MappingURLConstants.PROJECT_CREATE)
    public ResponseEntity<?> create(@Valid @RequestBody TemplateDTO template) {
        String projectId;

        try {
            projectId = projectService.createProject(template);
        } catch (ImportsToChooseException exception) {
            LibrariesToChooseDTO libraries = new LibrariesToChooseDTO(exception.getProjectId(), exception.getImportsToChoose());
            logger.debug("Return response CONFLICT - select libraries to import {} in project {}", libraries.getImports(), libraries.getProjectId());
            return new ResponseEntity<>(libraries, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(new ProjectDTO(projectId), HttpStatus.OK);
    }

    /**
     * Import collection of selected libraries to specific project
     * @param libraries selected libraries to import to project
     * @return unique project ID
     */
    @PostMapping(MappingURLConstants.PROJECT_IMPORT_LIBRARIES)
    public ResponseEntity<ProjectDTO> importLibraries(@Valid @RequestBody LibrariesDTO libraries) {
        String projectId = projectService.importLibraries(libraries);

        return new ResponseEntity<>(new ProjectDTO(projectId), HttpStatus.OK);
    }

    /**
     * Compile generated JMH project with all java imports, JMH annotations and methods to measure.
     * @param projectId project ID to compile
     * @return if everything is successful, return generated unique project ID. If errors is occurred during compiling, return errors with compiled java class.
     */
    @PostMapping(MappingURLConstants.PROJECT_COMPILE)
    public ResponseEntity<?> compile(@PathVariable String projectId) {
        try {
            projectService.compile(projectId);
        } catch (MavenCompileException exception) {
            logger.debug("Return response BAD_REQUEST - compiler errors {} in project {}: {}", exception.getErrors(), projectId, exception.getMessage());
            return new ResponseEntity<>(exception.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ProjectDTO(projectId), HttpStatus.OK);
    }

    /**
     * Method for kill running project.
     * @param projectId project to kill
     */
    @PostMapping(MappingURLConstants.PROJECT_KILL)
    public ResponseEntity kill(@PathVariable String projectId) {
        projectService.kill(projectId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
