package com.example.work2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.work2.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.secondFragmentOnThirdFragmentBtn.setOnClickListener{
            val thirdFragment = ThirdFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, thirdFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.secondFragmentOnFirstFragmentBtn.setOnClickListener{
            val firstFragment = FirstFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, firstFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}