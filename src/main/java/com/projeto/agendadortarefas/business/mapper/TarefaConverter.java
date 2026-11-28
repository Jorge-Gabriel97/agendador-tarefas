package com.projeto.agendadortarefas.business.mapper;

import com.projeto.agendadortarefas.business.dto.TarefasDto;
import com.projeto.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarefaConverter {

    @Mapping(source = "id", target = "id")
    TarefasEntity paraTarefaEntity(TarefasDto dto);
    TarefasDto paraTarefaDto(TarefasEntity entity);

    List<TarefasEntity> paraListaTarefasEntity(List<TarefasDto> dtos);
    List<TarefasDto> paraListaTarefasDto(List<TarefasEntity> entities);
}
