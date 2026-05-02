package com.krist.train.domain.model

object TrainingPlanJsonExtractor {
    fun extractFirstObject(raw: String): String {
        val text = raw.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        val start = text.indexOf('{')
        require(start >= 0) { "AI response did not contain a JSON object" }

        var depth = 0
        var inString = false
        var escaped = false

        for (index in start until text.length) {
            val char = text[index]

            if (escaped) {
                escaped = false
                continue
            }

            when (char) {
                '\\' -> if (inString) escaped = true
                '"' -> inString = !inString
                '{' -> if (!inString) depth += 1
                '}' -> if (!inString) {
                    depth -= 1
                    if (depth == 0) return text.substring(start, index + 1)
                }
            }
        }

        error("AI response contained an incomplete JSON object")
    }
}
