package com.draw.infra.external

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * Discord API Client for sending message to discord channel
 *
 * @see <a href="https://discord.com/developers/docs/resources/webhook#execute-webhook">Discord API</a>
 */
@FeignClient(name = "discord-api", url = "https://discord.com")
interface DiscordApiClient {

    @PostMapping(value = ["\${discord.webhook.url}"])
    fun sendMessage(@RequestBody message: DiscordMessage)
}
