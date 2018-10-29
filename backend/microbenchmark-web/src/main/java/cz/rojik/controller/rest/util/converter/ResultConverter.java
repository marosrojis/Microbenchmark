package cz.rojik.controller.rest.util.converter;

import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.ResultDTO;
import cz.rojik.service.dto.TemplateDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component("resultConverterRest")
public class ResultConverter {

    public ResultDTO templateToResult(TemplateDTO template) {
        ResultDTO result = new ResultDTO();

        result.setDeclare(template.getDeclare())
                .setInit(template.getInit())
                .setMeasurement(template.getMeasurement())
                .setWarmup(template.getWarmup());

        List<MeasureMethodDTO> methods = IntStream.range(0, template.getTestMethods().size())
                .mapToObj(index -> new MeasureMethodDTO(index, template.getTestMethods().get(index)))
                .collect(Collectors.toList());

        result.setMeasureMethods(methods);

        return result;
    }
}
