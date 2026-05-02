package com.krist.train.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

object FlexibleStringListSerializer : KSerializer<List<String>> {
    private val delegate = ListSerializer(String.serializer())

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun deserialize(decoder: Decoder): List<String> {
        val jsonDecoder = decoder as? JsonDecoder ?: return delegate.deserialize(decoder)
        return jsonDecoder.decodeJsonElement().toStringList()
    }

    override fun serialize(encoder: Encoder, value: List<String>) {
        delegate.serialize(encoder, value)
    }

    private fun JsonElement.toStringList(): List<String> = when (this) {
        is JsonArray -> mapNotNull { element ->
            when (element) {
                is JsonPrimitive -> element.contentOrNull
                JsonNull -> null
                else -> element.toString()
            }
        }.filter { it.isNotBlank() }
        is JsonPrimitive -> contentOrNull?.takeIf { it.isNotBlank() }?.let(::listOf).orEmpty()
        JsonNull -> emptyList()
        else -> listOf(toString())
    }
}
