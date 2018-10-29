package cz.rojik.service;

import cz.rojik.entity.ProjectEntity;

import java.util.List;

public interface ProjectService {

    List<ProjectEntity> fetchAll();
}
