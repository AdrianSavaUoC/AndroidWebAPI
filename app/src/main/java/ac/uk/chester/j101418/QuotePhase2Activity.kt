package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityQuotePhase2Binding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class QuotePhase2Activity : AppCompatActivity() {

    private lateinit var binding : ActivityQuotePhase2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuotePhase2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fragmentQ2A = Quote2AdviceFragment()
        val fragmentQuoteList = QuoteListFragment()


        //this is how you switch between fragments.
        // you bind a button  onClick Listener fo the bellow, with the appropriate fragment.



        val decisionFragment = intent.getStringExtra("decisionFragment")

        if (decisionFragment == "fragmentQ2A") {
            val word = intent.getStringExtra("firstWord")
            val fragment = Quote2AdviceFragment.newInstance(word)
            supportFragmentManager.beginTransaction()
                .add(R.id.flQuotesFragment, fragment)
                .commit()
        }
        else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flQuotesFragment, fragmentQuoteList)
                commit()
            }
        }



    }
}