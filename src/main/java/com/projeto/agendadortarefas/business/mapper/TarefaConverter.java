package com.projeto.agendadortarefas.business.mapper;

import com.projeto.agendadortarefas.business.dto.TarefasDto;
import com.projeto.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarefaConverter {

    @Mapping(source = "id", target = "id")
    TarefasEntity paraTarefaEntity(TarefasDto dto);

    TarefasDto paraTarefaDto(TarefasEntity entity);

    List<TarefasEntity> paraListaTarefasEntity(List<TarefasDto> dtos);

    List<TarefasDto> paraListaTarefasDto(List<TarefasEntity> entities);

    // PUT: sobrescreve todos os campos mutáveis (inclusive com null, se omitidos no JSON)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "emailUsuario", ignore = true)
    void atualizarTarefaCompleta(TarefasDto dto, @MappingTarget TarefasEntity entity);

    // PATCH: atualiza apenas os campos enviados (diferentes de null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "emailUsuario", ignore = true)
    void atualizarTarefaParcial(TarefasDto dto, @MappingTarget TarefasEntity entity);
}