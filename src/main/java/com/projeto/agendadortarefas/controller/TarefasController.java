package com.projeto.agendadortarefas.controller;


import com.projeto.agendadortarefas.business.TarefaService;
import com.projeto.agendadortarefas.business.dto.TarefasDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TarefasController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefasDto> gravarTarefas(@RequestBody TarefasDto dto,
                                                    @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(tarefaService.gravarTarefa(token, dto));
    }
}
