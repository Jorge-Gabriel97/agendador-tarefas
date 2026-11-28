package com.projeto.agendadortarefas.controller;

import com.projeto.agendadortarefas.business.TarefaService;
import com.projeto.agendadortarefas.business.dto.TarefasDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefasController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefasDto> gravarTarefas(@RequestBody TarefasDto dto,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefaService.gravarTarefa(token, dto));
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<TarefasDto>> buscaListaDeTarefasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {
        return ResponseEntity.ok(tarefaService.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal));
    }

    @GetMapping
    public ResponseEntity<List<TarefasDto>> buscaTarefasPorEmail(@RequestHeader("Authorization") String token) {
        List<TarefasDto> tarefas = tarefaService.buscaTarefasPorEmail(token);
        return ResponseEntity.ok(tarefas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefasDto> atualizarTarefaCompleta(
            @PathVariable String id,
            @RequestBody TarefasDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefaService.atualizarTarefa(id, dto, token));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TarefasDto> atualizarTarefaParcial(
            @PathVariable String id,
            @RequestBody TarefasDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefaService.atualizarParcial(id, dto, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefaPorId(@PathVariable String id) {
        tarefaService.deletarTarefaPorId(id);
        return ResponseEntity.noContent().build();
    }
}