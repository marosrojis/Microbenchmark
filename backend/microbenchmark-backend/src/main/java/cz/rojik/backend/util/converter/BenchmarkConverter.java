package cz.rojik.backend.util.converter;

import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.properties.PathProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Component
public class BenchmarkConverter {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkConverter.class);

    @Autowired
    private PathProperties pathProperties;

    @Autowired
    private UserConverter userConverter;

    private static final String JAR_FILE_NAME = "Microbenchmark.jar";

    /**
     * Convert {@link BenchmarkEntity} to {@link BenchmarkDTO} object
     * @param entity benchmark object entity
     * @return {@link BenchmarkDTO} object
     */
    public BenchmarkDTO entityToDTO(BenchmarkEntity entity) {
        logger.trace("Convert Benchmark entity to DTO object: {}", entity);
        BenchmarkDTO result = new BenchmarkDTO();

        result.setContent(entity.getContent())
                .setName(entity.getName())
                .setCreated(entity.getCreated())
                .setDeclare(entity.getDeclare())
                .setInit(entity.getInit())
                .setMeasurement(entity.getMeasurement())
                .setWarmup(entity.getWarmup())
                .setProjectId(entity.getProjectId())
                .setUser(userConverter.entityToDTO(entity.getUser(), false))
                .setSuccess(entity.isSuccess());
        result.setId(entity.getId());

        List<MeasureMethodDTO> methods = new ArrayList<>();
        entity.getMeasureMethods().forEach(method -> methods.add(new MeasureMethodDTO(method)));

        result.setMeasureMethods(methods);

        StringBuilder sb = new StringBuilder()
                .append(entity.getProjectId())
                .append(File.separator)
                .append("target")
                .append(File.separator)
                .append("benchmark")
                .append(File.separator)
                .append(JAR_FILE_NAME);
        String jarPath = sb.toString();

        File jar = new File(pathProperties.getProjects() + jarPath);
        if (jar.isFile() && jar.exists()) {
            result.setJarUrl(pathProperties.getLinkProjects() + File.separator + jarPath);
        }

        logger.trace("Converted Benchmark measure methods DTO: {}", methods);

        return result;
    }

    /**
     * Convert {@link BenchmarkDTO} to {@link BenchmarkEntity} object
     * @param dto benchmark DTO object
     * @return {@link BenchmarkEntity} object
     */
    public BenchmarkEntity dtoToEntity(BenchmarkDTO dto) {
        logger.trace("Convert DTO to Entity: {}", dto);
        BenchmarkEntity entity = new BenchmarkEntity();

        entity.setContent(dto.getContent())
                .setName(dto.getName())
                .setCreated(dto.getCreated())
                .setDeclare(dto.getDeclare())
                .setInit(dto.getInit())
                .setMeasurement(dto.getMeasurement())
                .setWarmup(dto.getWarmup())
                .setProjectId(dto.getProjectId())
                .setSuccess(dto.isSuccess());

        return entity;
    }
}
