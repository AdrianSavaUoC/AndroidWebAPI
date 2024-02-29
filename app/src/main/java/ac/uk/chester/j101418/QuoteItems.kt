package ac.uk.chester.j101418

import java.io.Serializable

data class QuoteItems (
    val id: String,
    val content: String,
    val author: String
) : Serializable