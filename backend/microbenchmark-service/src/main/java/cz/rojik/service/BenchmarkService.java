package cz.rojik.service;

import cz.rojik.dto.LibrariesDTO;
import cz.rojik.dto.ResultDTO;
import cz.rojik.exception.ImportsToChooseException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import cz.rojik.dto.TemplateDTO;

public interface BenchmarkService {

    String createProject(TemplateDTO template) throws ImportsToChooseException;

    String importLibraries(LibrariesDTO libraries);

    boolean compile(String projectId);

    ResultDTO runBenchmark(String projectId, SimpMessageHeaderAccessor socketHeader);
}
