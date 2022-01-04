package com.jrmydorm.todo.signup

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jrmydorm.todo.R
import com.jrmydorm.todo.databinding.FragmentSignupBinding
import com.jrmydorm.todo.models.LoginForm
import com.jrmydorm.todo.models.SignUpForm
import com.jrmydorm.todo.network.Api
import com.jrmydorm.todo.network.Api.SHARED_PREF_TOKEN_KEY
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstNameEditText = binding.firstname
        val lastNameEditText = binding.lastname
        val emailEditText = binding.email
        val passwordEditText = binding.password
        val passwordConfirmationEditText = binding.passwordConfirmation

        val signUpButton = binding.signup
        val loadingProgressBar = binding.loading
        signUpButton.setOnClickListener {
            if (firstNameEditText.text.isBlank() or lastNameEditText.text.isBlank() or emailEditText.text.isBlank() or passwordEditText.text.isBlank() or (passwordEditText.text.toString() != passwordConfirmationEditText.text.toString())) {
                Toast.makeText(context, "Formulaire invalide", Toast.LENGTH_LONG).show()
            } else {
                val signUpForm = SignUpForm(
                    firstName =  firstNameEditText.text.toString(),
                    lastName = lastNameEditText.text.toString(),
                    email = emailEditText.text.toString(),
                    password = passwordEditText.text.toString()
                )
                lifecycleScope.launch {
                    val  result = Api.userWebService.signup(signUpForm)
                    println(result.isSuccessful);
                    if(result.isSuccessful &&   result.body() != null) {
                        val signupResponse = result.body()!!
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, signupResponse.token)
                        }
                        findNavController().navigate(R.id.action_signupFragment_to_taskListFragment)
                        Toast.makeText(context, "Inscription réussie !", Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(context, "Erreur d'inscription", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}