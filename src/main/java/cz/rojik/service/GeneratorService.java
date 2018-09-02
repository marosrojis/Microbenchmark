package cz.rojik.service;

import cz.rojik.dto.TemplateDTO;

public interface GeneratorService {

    String generateJavaClass(TemplateDTO template);

}
