package cz.rojik.util.converter;

import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.service.dto.TemplateDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Component("benchmarkConverterRest")
public class BenchmarkConverter {

    /**
     * Convert {@link TemplateDTO} to {@link BenchmarkDTO}.
     * @param template template object to convert
     * @return {@link BenchmarkDTO} object
     */
    public BenchmarkDTO templateToResult(TemplateDTO template) {
        BenchmarkDTO result = new BenchmarkDTO();

        result.setDeclare(template.getDeclare())
                .setInit(template.getInit())
                .setMeasurement(template.getMeasurement())
                .setWarmup(template.getWarmup())
                .setName(template.getName());

        List<MeasureMethodDTO> methods = IntStream.range(0, template.getTestMethods().size())
                .mapToObj(index -> new MeasureMethodDTO(index, template.getTestMethods().get(index)))
                .collect(Collectors.toList());

        result.setMeasureMethods(methods);

        return result;
    }
}
