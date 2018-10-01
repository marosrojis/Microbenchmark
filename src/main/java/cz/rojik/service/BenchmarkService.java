package cz.rojik.service;

import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.exception.ImportsToChooseException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface BenchmarkService {

    String createProject(TemplateDTO template) throws ImportsToChooseException;

    boolean compile(String projectId);

    ResultDTO runBenchmark(String projectId, SimpMessageHeaderAccessor socketHeader);
}
