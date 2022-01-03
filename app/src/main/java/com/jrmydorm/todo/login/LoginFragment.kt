package com.jrmydorm.todo.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings.Global.putString
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jrmydorm.todo.databinding.FragmentLoginBinding

import com.jrmydorm.todo.R
import com.jrmydorm.todo.models.LoginForm
import com.jrmydorm.todo.network.Api
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailEditText = binding.email
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading
        loginButton.setOnClickListener {
            if (emailEditText.text.isBlank() or passwordEditText.text.isBlank()) {
                Toast.makeText(context, "Formulaire invalide", Toast.LENGTH_LONG).show()
            } else {
                val loginForm = LoginForm(
                    email = emailEditText.text.toString(),
                    password = passwordEditText.text.toString()
                )
                lifecycleScope.launch {
                    val  result = Api.userWebService.login(loginForm)
                    if(result.isSuccessful &&   result.body() != null) {
                        val loginResponse = result.body()!!
                        val SHARED_PREF_TOKEN_KEY = "auth_token_key"
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, loginResponse.token)
                        }
                        findNavController().navigate(R.id.action_loginFragment_to_taskListFragment)
                    } else {
                        Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show()
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