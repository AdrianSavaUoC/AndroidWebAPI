package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.FragmentQuote2AdviceBinding
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts

class Quote2AdviceFragment : Fragment () {

    private lateinit var binding: FragmentQuote2AdviceBinding

    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }


    private fun loadMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        resultLauncher.launch(intent)
    }


    companion object {
        fun newInstance(data: List<AdviceData>?): Quote2AdviceFragment {
            val fragment = Quote2AdviceFragment()
            val args = Bundle().apply {
                putSerializable("adviceList", ArrayList(data))
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuote2AdviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val advices = arguments?.getSerializable("adviceList") as List<AdviceData>?

        val size = advices?.size
        binding.tvFragmentQ2A.text = size.toString()

        binding.button.setOnClickListener{
            loadMainActivity()
        }
    }


}