package cz.rojik.service;

import cz.rojik.dto.TemplateDTO;
import cz.rojik.exception.ImportsToChooseException;

public interface GeneratorService {

    String generateJavaClass(TemplateDTO template) throws ImportsToChooseException;

}
