package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityAdviceBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts

class AdviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdviceBinding
    private lateinit var id: String
    private lateinit var advice: String

    private val adviceList = mutableListOf<AdviceData>()

    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadMaintivity () {
        val intent = Intent (this, MainActivity::class.java)
        resultLauncher.launch(intent)
    }
    private fun getFirstWord () : String? {
        val advice = intent.getStringExtra("advice")
        val words = advice?.split(Regex("[ ,?;.:!]+"))?.toMutableList()
        return words?.firstOrNull()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val adviceId = intent.getStringExtra("id")
        val adviceText = intent.getStringExtra("advice")

        if (adviceId != null) {
            id = adviceId
        }
        if (adviceText != null) {
            advice = adviceText
        }

        val newAdvice = AdviceData(id, advice)
        adviceList.add(newAdvice)

        binding.dummy.text = newAdvice.advice
        val firstWord = getFirstWord()
        binding.tvAdviceView.text = firstWord

        binding.buttonHome.setOnClickListener {
            loadMaintivity()
        }


    }
}