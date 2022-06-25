package com.lock.demo.controller

import com.lock.demo.domain.Message
import com.lock.demo.service.MessageLockerService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping


@RequestMapping
@Controller
class ContextController(
    private val messageLockerService: MessageLockerService
) {
    @PostMapping("/lock/message")
    fun lockMessage(@RequestBody message: Message): ResponseEntity<String> {
        val canLock = messageLockerService.tryLock(message)
        if (!canLock) {
            return ResponseEntity.ok("Aguarde enquanto estamos processando a primeira mensagem")
        }

        return ResponseEntity.ok("Em processamento")
    }

    @PostMapping("/unlock/message")
    fun unlockMessage(@RequestBody message: Message): ResponseEntity<String> {
        val message = messageLockerService.tryUnlock(message)
            ?: return ResponseEntity.ok("Nao existe nenhuma mensagem a ser respondida")
        return ResponseEntity.ok("Mensagem enviada ${message.content} para o whatsapp")
    }
}