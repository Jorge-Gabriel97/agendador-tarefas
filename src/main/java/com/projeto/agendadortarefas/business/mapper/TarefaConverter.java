package com.projeto.agendadortarefas.business.mapper;

import com.projeto.agendadortarefas.business.dto.TarefasDto;
import com.projeto.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarefaConverter {

    TarefasEntity paraTarefaEntity(TarefasDto dto);
    TarefasDto paraTarefaDto(TarefasEntity entity);
}
