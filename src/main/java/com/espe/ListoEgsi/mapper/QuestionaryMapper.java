package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.espe.ListoEgsi.domain.dto.question.QuestionaryDTO;
import com.espe.ListoEgsi.domain.model.entity.question.Questionary;

@Mapper(componentModel = "spring")
public interface QuestionaryMapper {

    QuestionaryMapper INSTANCE = Mappers.getMapper(QuestionaryMapper.class);

    QuestionaryDTO toDTO(Questionary questionary);

    Questionary toEntity(QuestionaryDTO questionaryDTO);
}
