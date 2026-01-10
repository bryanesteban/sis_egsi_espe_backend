package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.espe.ListoEgsi.domain.dto.question.QuestionDTO;
import com.espe.ListoEgsi.domain.model.entity.question.Question;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mapping(source = "questionary.idQuestionary", target = "idQuestionary")
    QuestionDTO toDTO(Question question);

    @Mapping(source = "idQuestionary", target = "questionary.idQuestionary")
    Question toEntity(QuestionDTO questionDTO);
}
