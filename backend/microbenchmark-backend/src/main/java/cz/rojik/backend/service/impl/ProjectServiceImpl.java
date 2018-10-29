package cz.rojik.backend.service.impl;

import cz.rojik.backend.entity.ProjectEntity;
import cz.rojik.backend.repository.ProjectRepository;
import cz.rojik.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<ProjectEntity> fetchAll() {
        return projectRepository.findAll();
    }
}
