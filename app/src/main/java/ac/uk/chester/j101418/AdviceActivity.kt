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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val dummyId = intent.getStringExtra("id")
        val dummy = intent.getStringExtra("advice")


        if (dummyId != null) {
            id = dummyId
        }
        if (dummy != null) {
            advice = dummy
        }

        val newAdvice = AdviceData(id, advice)
        adviceList.add(newAdvice)


        val words = dummy?.split(Regex("[ ,?;.:!]+"))?.toMutableList()
        val firstWord = words?.firstOrNull()

        binding.tvAdviceView.text = firstWord

        binding.dummy.text = newAdvice.advice

        binding.buttonHome.setOnClickListener {
            loadMaintivity()
        }


    }
}