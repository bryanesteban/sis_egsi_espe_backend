package com.espe.ListoEgsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.espe.ListoEgsi.domain.dto.question.AnswerDTO;
import com.espe.ListoEgsi.domain.model.entity.question.Answer;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    @Mapping(source = "question.idQuestion", target = "idQuestion")
    @Mapping(source = "phase.idPhase", target = "idPhase")
    AnswerDTO toDTO(Answer answer);

    @Mapping(source = "idQuestion", target = "question.idQuestion")
    @Mapping(source = "idPhase", target = "phase.idPhase")
    Answer toEntity(AnswerDTO answerDTO);
}
