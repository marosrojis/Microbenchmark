package cz.rojik.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.util.SecurityHelper;
import cz.rojik.util.converter.BenchmarkConverter;
import cz.rojik.service.TransformService;
import cz.rojik.service.dto.TemplateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TransformServiceImpl implements TransformService {

    @Autowired
    @Qualifier("benchmarkConverterRest")
    private BenchmarkConverter benchmarkConverter;

    @Override
    public BenchmarkDTO createResult(String projectId, TemplateDTO template, cz.rojik.service.dto.ResultDTO benchmarkResult) {
        Gson gson = new GsonBuilder().create();

        BenchmarkDTO result = benchmarkConverter.templateToResult(template);

        result.setProjectId(projectId)
                .setCreated(benchmarkResult.getTime())
                .setContent(gson.toJson(benchmarkResult))
                .setUser(SecurityHelper.getCurrentUser());

        return result;
    }
}
