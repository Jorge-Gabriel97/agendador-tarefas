package com.projeto.agendadortarefas.business;

import com.projeto.agendadortarefas.business.dto.TarefasDto;
import com.projeto.agendadortarefas.business.mapper.TarefaConverter;
import com.projeto.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.projeto.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.projeto.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.projeto.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefasRepository tarefasRepository;
    private final TarefaConverter tarefaConverter;
    private final JwtUtil jwtUtil;

    public TarefasDto gravarTarefa(String token, TarefasDto dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        dto.setDataCriacao(LocalDateTime.now());
        dto.getStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);

        return tarefaConverter.paraTarefaDto (
                tarefasRepository.save(entity));
    }
}
