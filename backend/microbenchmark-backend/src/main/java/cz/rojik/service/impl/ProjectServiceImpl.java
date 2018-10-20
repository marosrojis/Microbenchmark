package cz.rojik.service.impl;

import cz.rojik.entity.Project;
import cz.rojik.repository.ProjectRepository;
import cz.rojik.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> fetAll() {
        return projectRepository.findAll();
    }
}
