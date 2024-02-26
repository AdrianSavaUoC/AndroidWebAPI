package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityQuoteBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts

class QuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuoteBinding

    private val quoteList = mutableListOf<QuoteData>()
    private lateinit var id : String
    private lateinit var quote : String
    private lateinit var author : String

    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadMainActivity () {
        val intent = Intent(this, MainActivity::class.java )
        resultLauncher.launch(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonHome.setOnClickListener {
            loadMainActivity()
        }

        val id = intent.getStringExtra("_id")
        val quote = intent.getStringExtra("quote")
        val author = intent.getStringExtra("author")


        if (id != null) {
            this.id = id
        }
        if (quote != null) {
            this.quote = quote
        }
        if (author != null) {
            this.author = author
        }

        val newQuote = QuoteData(this.id, this.quote, this.author)

        quoteList.add(newQuote)

        binding.tvQuote.text = newQuote.quote
        binding.tvAuthor.text = newQuote.author


    }
}