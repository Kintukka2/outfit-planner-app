package com.kintukka.hanger.ai

import com.anthropic.client.AnthropicClient
import com.anthropic.client.okhttp.AnthropicOkHttpClient
import com.anthropic.core.JsonValue
import com.anthropic.errors.AnthropicIoException
import com.anthropic.errors.AnthropicServiceException
import com.anthropic.errors.BadRequestException
import com.anthropic.errors.InternalServerException
import com.anthropic.errors.PermissionDeniedException
import com.anthropic.errors.RateLimitException
import com.anthropic.errors.UnauthorizedException
import com.anthropic.models.messages.CacheControlEphemeral
import com.anthropic.models.messages.JsonOutputFormat
import com.anthropic.models.messages.MessageCreateParams
import com.anthropic.models.messages.MessageParam
import com.anthropic.models.messages.OutputConfig
import com.anthropic.models.messages.StopReason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class StylistOutfit(
    @SerialName("item_ids") val itemIds: List<Long>,
    val title: String,
    val why: String,
)

@Serializable
data class StylistReply(val reply: String, val outfits: List<StylistOutfit> = emptyList())

/** A failure the chat can explain to the user in plain words. */
class StylistException(message: String, val retryable: Boolean) : Exception(message)

/**
 * One stylist conversation with Claude. The system prompt (wardrobe snapshot, date, weather) is fixed for
 * the life of the session and history is append-only, so earlier turns — including the model's thinking —
 * are replayed exactly as received.
 */
class StylistSession(apiKey: String, private val systemPrompt: String) {
    private val client: AnthropicClient = AnthropicOkHttpClient.builder().apiKey(apiKey).build()
    private val history = mutableListOf<MessageParam>()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun send(text: String): StylistReply = withContext(Dispatchers.IO) {
        val user = MessageParam.builder().role(MessageParam.Role.USER).content(text).build()
        val params = MessageCreateParams.builder()
            .model(MODEL)
            .maxTokens(16000L)
            .system(systemPrompt)
            .messages(history + user)
            // Cache the wardrobe snapshot and history between turns.
            .cacheControl(CacheControlEphemeral.builder().build())
            .outputConfig(
                OutputConfig.builder()
                    .effort(OutputConfig.Effort.MEDIUM)
                    .format(JsonOutputFormat.builder().schema(replySchema()).build())
                    .build(),
            )
            // If a safety classifier declines, let the API retry on its recommended fallback model.
            .putAdditionalHeader("anthropic-beta", "server-side-fallback-2026-07-01")
            .putAdditionalBodyProperty("fallbacks", JsonValue.from("default"))
            .build()

        val response = try {
            client.messages().create(params)
        } catch (e: UnauthorizedException) {
            throw StylistException("That API key wasn't accepted. Check it in Settings.", retryable = false)
        } catch (e: PermissionDeniedException) {
            throw StylistException("This API key can't use the stylist model.", retryable = false)
        } catch (e: RateLimitException) {
            throw StylistException("The stylist is busy right now — try again in a moment.", retryable = true)
        } catch (e: BadRequestException) {
            throw StylistException("The request was rejected (${e.message?.take(120)}).", retryable = false)
        } catch (e: InternalServerException) {
            throw StylistException("The stylist service had a hiccup. Try again.", retryable = true)
        } catch (e: AnthropicServiceException) {
            throw StylistException("Something went wrong talking to the stylist (${e.statusCode()}).", retryable = true)
        } catch (e: AnthropicIoException) {
            throw StylistException("No connection — the stylist needs the internet.", retryable = true)
        }

        when (response.stopReason().orElse(null)) {
            StopReason.REFUSAL ->
                // Leave history untouched so the conversation stays valid.
                throw StylistException("The stylist can't help with that one. Try asking another way.", retryable = false)
            StopReason.MAX_TOKENS ->
                throw StylistException("That answer ran too long. Try a narrower question.", retryable = false)
            else -> Unit
        }

        history += user
        history += response.toParam()

        val body = response.content().mapNotNull { block -> block.text().orElse(null)?.text() }.joinToString("")
        runCatching { json.decodeFromString<StylistReply>(body) }
            .getOrElse { StylistReply(reply = body.ifBlank { "I couldn't put that together — try again?" }) }
    }

    private fun replySchema(): JsonOutputFormat.Schema {
        val outfit = mapOf(
            "type" to "object",
            "properties" to mapOf(
                "item_ids" to mapOf("type" to "array", "items" to mapOf("type" to "integer")),
                "title" to mapOf("type" to "string"),
                "why" to mapOf("type" to "string"),
            ),
            "required" to listOf("item_ids", "title", "why"),
            "additionalProperties" to false,
        )
        return JsonOutputFormat.Schema.builder()
            .putAdditionalProperty("type", JsonValue.from("object"))
            .putAdditionalProperty(
                "properties",
                JsonValue.from(mapOf("reply" to mapOf("type" to "string"), "outfits" to mapOf("type" to "array", "items" to outfit))),
            )
            .putAdditionalProperty("required", JsonValue.from(listOf("reply", "outfits")))
            .putAdditionalProperty("additionalProperties", JsonValue.from(false))
            .build()
    }

    companion object {
        const val MODEL = "claude-opus-5-5"
    }
}
