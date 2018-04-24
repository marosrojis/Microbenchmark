package cz.rojik.service;

import cz.rojik.dto.Template;

public interface GeneratorService {

    String generateJavaClass(Template template);

}
