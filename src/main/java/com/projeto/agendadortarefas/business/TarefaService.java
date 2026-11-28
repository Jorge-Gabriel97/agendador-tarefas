package com.projeto.agendadortarefas.business;

import com.projeto.agendadortarefas.business.dto.TarefasDto;
import com.projeto.agendadortarefas.business.mapper.TarefaConverter;
import com.projeto.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.projeto.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.projeto.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.projeto.agendadortarefas.infrastructure.exceptions.UnauthorizedException;
import com.projeto.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.projeto.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefasRepository tarefasRepository;
    private final TarefaConverter tarefaConverter;
    private final JwtUtil jwtUtil;

    public TarefasDto gravarTarefa(String token, TarefasDto dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);

        return tarefaConverter.paraTarefaDto(
                tarefasRepository.save(entity));
    }

    public List<TarefasDto> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConverter.paraListaTarefasDto(
                tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDto> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        List<TarefasEntity> listaTarefas = tarefasRepository.findByEmailUsuario(email);

        return tarefaConverter.paraListaTarefasDto(listaTarefas);
    }

    public TarefasDto atualizarTarefa(String id, TarefasDto dto, String token) {
        TarefasEntity entityExistente = buscarEValidarTarefa(id, token);

        tarefaConverter.atualizarTarefaCompleta(dto, entityExistente);

        return tarefaConverter.paraTarefaDto(
                tarefasRepository.save(entityExistente));
    }

    public TarefasDto atualizarParcial(String id, TarefasDto dto, String token) {
        TarefasEntity entityExistente = buscarEValidarTarefa(id, token);

        tarefaConverter.atualizarTarefaParcial(dto, entityExistente);

        return tarefaConverter.paraTarefaDto(
                tarefasRepository.save(entityExistente));
    }

    public void deletarTarefaPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, id inexistente: " + id, e.getCause());
        }
    }

    private TarefasEntity buscarEValidarTarefa(String id, String token) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        TarefasEntity entityExistente = tarefasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o ID: " + id));

        if (!entityExistente.getEmailUsuario().equals(email)) {
            throw new UnauthorizedException("Acesso negado: Você não tem permissão para alterar esta tarefa.");
        }

        return entityExistente;
    }
}