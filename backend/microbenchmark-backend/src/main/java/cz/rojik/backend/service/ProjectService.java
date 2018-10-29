package cz.rojik.backend.service;

import cz.rojik.backend.entity.ProjectEntity;

import java.util.List;

public interface ProjectService {

    List<ProjectEntity> fetchAll();
}
