package cz.rojik.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.backend.dto.ResultDTO;
import cz.rojik.backend.util.SecurityHelper;
import cz.rojik.controller.rest.util.converter.ResultConverter;
import cz.rojik.service.TransformService;
import cz.rojik.service.dto.TemplateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TransformServiceImpl implements TransformService {

    @Autowired
    @Qualifier("resultConverterRest")
    private ResultConverter resultConverter;

    @Override
    public ResultDTO createResult(String projectId, TemplateDTO template, cz.rojik.service.dto.ResultDTO benchmarkResult) {
        Gson gson = new GsonBuilder().create();

        ResultDTO result = resultConverter.templateToResult(template);

        result.setProjectId(projectId)
                .setCreated(benchmarkResult.getTime())
                .setContent(gson.toJson(benchmarkResult))
                .setUser(SecurityHelper.getCurrentUser());

        return result;
    }
}
