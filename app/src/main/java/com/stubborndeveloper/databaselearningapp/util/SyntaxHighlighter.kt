package com.stubborndeveloper.databaselearningapp.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

object SyntaxHighlighter {

    private val keywords = setOf(
        "select", "from", "where", "insert", "into", "values", "update", "set", "delete",
        "create", "table", "primary", "key", "not", "null", "unique", "references", "drop",
        "alter", "add", "column", "and", "or", "order", "by", "as", "inner", "join", "left",
        "right", "on", "group", "having", "limit", "offset", "case", "when", "then", "else", "end"
    )

    fun highlight(text: String): AnnotatedString {
        return buildAnnotatedString {
            append(text)
            val regex = "\\b(${keywords.joinToString("|")})\\b".toRegex(RegexOption.IGNORE_CASE)
            regex.findAll(text).forEach { match ->
                addStyle(
                    style = SpanStyle(color = Color(0xFF660099)),
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }
        }
    }
}