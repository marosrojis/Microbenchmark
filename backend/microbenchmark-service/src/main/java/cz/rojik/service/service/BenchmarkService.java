package cz.rojik.service.service;

import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.ResultDTO;
import cz.rojik.service.exception.BenchmarkRunException;
import cz.rojik.service.exception.ImportsToChooseException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import cz.rojik.service.dto.TemplateDTO;

public interface BenchmarkService {

    String createProject(TemplateDTO template) throws ImportsToChooseException;

    String importLibraries(LibrariesDTO libraries);

    boolean compile(String projectId);

    ResultDTO runBenchmark(String projectId, TemplateDTO template, SimpMessageHeaderAccessor socketHeader) throws BenchmarkRunException;
}
